package com.cmp404.cloud_brokerapplication.Android;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Entities.Insurance;
import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.Helpers.WebServicesIO;
import com.cmp404.cloud_brokerapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InsuranceActivity extends Activity {
    private RadioGroup insuranceRadioGroup;
    private RadioButton renewRadioButton, registerRadioButton;
    private Button registerButton, renewButton;
    private TextView companiesLabel, packageLabel;
    private Spinner companiesSpinner, packagesSpinner;
    private EditText referenceEditText;
    private BrokerApplication application;
    private DatabaseHelper database;

    private Insurance selectedCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        application = (BrokerApplication) getApplication();
        database = application.database;

        insuranceRadioGroup = (RadioGroup) findViewById(R.id.insurance_radioGroup);
        renewRadioButton = (RadioButton) findViewById(R.id.insurance_renewRadioButton);
        registerRadioButton = (RadioButton) findViewById(R.id.insurance_registerRadioButton);
        companiesLabel = (TextView) findViewById(R.id.insurance_companiesLabel);
        companiesSpinner = (Spinner) findViewById(R.id.insurance_companiesSpinner);
        packageLabel = (TextView) findViewById(R.id.insurance_packageLabel);
        packagesSpinner = (Spinner) findViewById(R.id.insurance_packagesSpinner);
        registerButton = (Button) findViewById(R.id.insurance_registerButton);
        renewButton = (Button) findViewById(R.id.insurance_renewButton);
        referenceEditText = (EditText) findViewById(R.id.insurance_referenceEditText);

        insuranceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.insurance_renewRadioButton) {
                    initCompaniesSpinner();
                    packageLabel.setVisibility(View.GONE);
                    packagesSpinner.setVisibility(View.GONE);
                    referenceEditText.setVisibility(View.VISIBLE);
                    renewButton.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.insurance_registerRadioButton) {
                    initCompaniesSpinner();
                    referenceEditText.setVisibility(View.GONE);
                    renewButton.setVisibility(View.GONE);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCompany!=null) {
                    int selectedPackagePosition = packagesSpinner.getSelectedItemPosition();
                    String selectedPackageNo = selectedCompany.getPackage(selectedPackagePosition);
                    registerForInsurancePackage(selectedPackageNo);
                }
            }
        });

        renewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String referenceNo = referenceEditText.getText().toString();
                if(referenceNo != null){
                    String insurancePlan = getInsurancePlan(referenceNo);
                    if(insurancePlan.equals("{}")){
                        referenceEditText.setError("Invalid reference number, please try again.");
                        referenceEditText.requestFocus();
                    } else {
                        try {
                            JSONObject resultObject = new JSONObject(insurancePlan);
                            String packageNo = resultObject.getString("package-no");
                            String company = resultObject.getString("company");
                            String expiry = resultObject.getString("expiry");

                            String dialogMessage = "Insurance plan with reference number " + referenceNo + ":\n";
                            dialogMessage += "package number: " + packageNo + "\n";
                            dialogMessage += "company: " + company + "\n";
                            dialogMessage += "expiry: " + expiry + "\n";

                            AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceActivity.this);
                            AlertDialog.OnClickListener listener = new DialogInterface.OnClickListener() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == AlertDialog.BUTTON_POSITIVE) {
                                        renewInsurancePlan(referenceNo);
                                    }
                                }
                            };

                            AlertDialog dialog = builder.setTitle("Renew Plan")
                                    .setMessage(dialogMessage +
                                            "Are you sure you want to renew this plan?")
                                    .setPositiveButton("Renew", listener)
                                    .setNegativeButton("Cancel", listener)
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    referenceEditText.setError("Please enter your reference number.");
                    referenceEditText.requestFocus();
                }
            }
        });
    }

    public void initCompaniesSpinner(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                database.initInsuranceCompanies();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                companiesLabel.setVisibility(View.VISIBLE);
                companiesSpinner.setVisibility(View.VISIBLE);

                ArrayAdapter<Insurance> companiesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, application.insuranceCompanies);
                companiesSpinner.setAdapter(companiesAdapter);
            }
        }.execute();

        companiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCompany = application.insuranceCompanies.get(i);
                if(registerRadioButton.isChecked())
                    initPackagesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    public void initPackagesSpinner(){
        String base = application.BASE_URL;
        String systemPath = application.INSURANCE_PATH;
        String company = selectedCompany.path + "/";
        String service = application.INSURANCE_GET_PACKAGES;
        String result = application.externalInterface.callWebService(base + systemPath + company + service);

        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray resultArray = resultObject.getJSONArray("result");

            ArrayList<String> packages = new ArrayList<>();

            for(int i=0; i<resultArray.length(); i++){
                JSONObject obj = resultArray.getJSONObject(i);
                selectedCompany.addPackage(obj);
                String display = obj.getString("description") + " - AED" + obj.getString("fee");
                packages.add(display);
            }

            ArrayAdapter<String> packagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, packages);
            packagesSpinner.setAdapter(packagesAdapter);

            packageLabel.setVisibility(View.VISIBLE);
            packagesSpinner.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);

            referenceEditText.setVisibility(View.GONE);
            renewButton.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void registerForInsurancePackage(String packageNo){
        String base = application.BASE_URL;
        String systemPath = application.INSURANCE_PATH;
        String company = selectedCompany.path + "/";
        String service = application.INSURANCE_REGISTER_PACKAGE;
        String params = application.currentUser.getName() + "/" + application.currentUser.getContact() + "/" + packageNo;

        Log.d("InsuranceActivity_Log", base + systemPath + company + service + params);
        String result = application.externalInterface.callWebService(base + systemPath + company + service + params);
        try {
            if(!result.equals("{}")) {
                JSONObject resultObject = new JSONObject(result);
                application.currentUser.setInsurancePackageRef(resultObject.getString("reference-no"));
                application.currentUser.setInsuranceDone(true);
                Toast.makeText(getApplicationContext(), "Registered for package successfully!", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void renewInsurancePlan(String referenceNo) {
        String base = application.BASE_URL;
        String systemPath = application.INSURANCE_PATH;
        String company = selectedCompany.path + "/";
        String service = application.INSURANCE_RENEW_PLAN;
        String params = referenceNo + "/" + application.currentUser.getCreditCard() + "/" + "1000";

        String result = application.externalInterface.callWebService(base + systemPath + company + service + params);
        if(!result.equals("{}")){
            application.currentUser.setInsurancePackageRef(referenceNo);
            application.currentUser.setInsuranceDone(true);
            Toast.makeText(getApplicationContext(), "Plan renewed successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            referenceEditText.setError("Invalid reference number, please try again.");
            referenceEditText.requestFocus();
        }
    }

    public String getInsurancePlan(String referenceNo){
        String base = application.BASE_URL;
        String systemPath = application.INSURANCE_PATH;
        String company = selectedCompany.path + "/";
        String service = application.INSURANCE_GET_PLAN;
        String params = referenceNo;

        Log.d("InsuranceActivity_Log", base + systemPath + company + service + params);
        String result = application.externalInterface.callWebService(base + systemPath + company + service + params);
        Log.d("InsuranceActivity_Log", result);
        return result;
    }
}
