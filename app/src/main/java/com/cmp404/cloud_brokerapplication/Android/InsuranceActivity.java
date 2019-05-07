package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.Helpers.WebServicesIO;
import com.cmp404.cloud_brokerapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InsuranceActivity extends Activity {
    private RadioGroup insuranceRadioGroup;
    private RadioButton renewRadioButton, registerRadioButton;
    private TextView companiesLabel, packageLabel;
    private Spinner companiesSpinner, packagesSpinner;
    private BrokerApplication application;
    private DatabaseHelper database;

    private String selectedCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

        application = (BrokerApplication) getApplication();
        database = application.database;

        insuranceRadioGroup = (RadioGroup) findViewById(R.id.insurance_radioGroup);
        renewRadioButton = (RadioButton) findViewById(R.id.insurance_renewRadioButton);
        registerRadioButton = (RadioButton) findViewById(R.id.insurance_registerRadioButton);
        companiesLabel = (TextView) findViewById(R.id.insurance_companiesLabel);
        companiesSpinner = (Spinner) findViewById(R.id.insurance_companiesSpinner);
        packageLabel = (TextView) findViewById(R.id.insurance_packageLabel);
        packagesSpinner = (Spinner) findViewById(R.id.insurance_packagesSpinner);

        insuranceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.insurance_renewRadioButton) {
                    companiesLabel.setVisibility(View.GONE);
                    companiesSpinner.setVisibility(View.GONE);
                    packageLabel.setVisibility(View.GONE);
                    packagesSpinner.setVisibility(View.GONE);

                } else if (checkedId == R.id.insurance_registerRadioButton) {
                    initCompaniesSpinner();

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

                ArrayAdapter<String> companiesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, application.insuranceCompanies);
                companiesSpinner.setAdapter(companiesAdapter);
            }
        }.execute();

        companiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCompany = application.insuranceCompanies.get(i);
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
        String company = selectedCompany + "/";
        String service = application.INSURANCE_GET_PACKAGES;
        String result = application.externalInterface.callWebService(base + systemPath + company + service);
//        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray resultArray = resultObject.getJSONArray("result");

            application.insurancePackages = new ArrayList<>();
            for(int i=0; i<resultArray.length(); i++){
                JSONObject obj = resultArray.getJSONObject(i);
                String display = obj.getString("id") + " - " + obj.getString("description") + " - AED" + obj.getString("fee");
                application.insurancePackages.add(display);
            }

            ArrayAdapter<String> packagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, application.insurancePackages);
            packagesSpinner.setAdapter(packagesAdapter);

            packageLabel.setVisibility(View.VISIBLE);
            packagesSpinner.setVisibility(View.VISIBLE);

            

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
