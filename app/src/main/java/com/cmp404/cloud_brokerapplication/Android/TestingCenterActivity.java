package com.cmp404.cloud_brokerapplication.Android;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.cmp404.cloud_brokerapplication.Entities.TestingCenter;
import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.R;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestingCenterActivity extends Activity {
    private RadioGroup radioGroup;
    private RadioButton timingsRadioButton, bookedRadioButton;
    private Button bookButton, checkButton;
    private TextView companiesLabel, timeslotsLabel;
    private Spinner companiesSpinner, timeslotsSpinner;
    private EditText referenceEditText;
    private BrokerApplication application;
    private DatabaseHelper database;

    private TestingCenter selectedCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_center);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        application = (BrokerApplication) getApplication();
        database = application.database;

        radioGroup = (RadioGroup) findViewById(R.id.testing_radioGroup);
        timingsRadioButton = (RadioButton) findViewById(R.id.testing_timingsRadioButton);
        bookedRadioButton = (RadioButton) findViewById(R.id.testing_bookedRadioButton);
        companiesLabel = (TextView) findViewById(R.id.testing_companyTextView);
        companiesSpinner = (Spinner) findViewById(R.id.testing_companiesSpinner);
        timeslotsLabel = (TextView) findViewById(R.id.testing_timeslotsTextView);
        timeslotsSpinner = (Spinner) findViewById(R.id.testing_timeslotsSpinner);
        bookButton = (Button) findViewById(R.id.testing_bookButton);
        checkButton = (Button) findViewById(R.id.testing_checkButton);
        referenceEditText = (EditText) findViewById(R.id.testing_customerNoEditText);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.testing_timingsRadioButton) {
                    initCompaniesSpinner();
                    referenceEditText.setVisibility(View.GONE);
                    checkButton.setVisibility(View.GONE);
                } else if (checkedId == R.id.testing_bookedRadioButton) {
                    initCompaniesSpinner();
                    timeslotsLabel.setVisibility(View.GONE);
                    timeslotsSpinner.setVisibility(View.GONE);
                }
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCompany!=null) {
                    int selectedTimingPosition = timeslotsSpinner.getSelectedItemPosition();
                    TestingCenter.Timing selectedTiming = selectedCompany.getTiming(selectedTimingPosition);
                    bookTiming(selectedTiming);
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String referenceNo = referenceEditText.getText().toString();
                if(referenceNo != null){
                    String bookedTimeslot = getBookedTimeslot(referenceNo);
                    if(bookedTimeslot.equals("{}")){
                        referenceEditText.setError("Invalid customer reference number, please try again.");
                        referenceEditText.requestFocus();
                    } else {
                        try {
                            JSONObject resultObject = new JSONObject(bookedTimeslot);
                            String timingId = resultObject.getString("timing-id");
                            String company = resultObject.getString("company");
                            String date = resultObject.getString("date");
                            String time = resultObject.getString("time");
                            String location = resultObject.getString("location");

                            String dialogMessage = "Appointment timeslot with customer reference number " + referenceNo + ":\n";
                            dialogMessage += "timing id: " + timingId + "\n";
                            dialogMessage += "company: " + company + "\n";
                            dialogMessage += "date: " + date + "\n";
                            dialogMessage += "time: " + time + "\n";
                            dialogMessage += "location: " + location + "\n";

                            AlertDialog.Builder builder = new AlertDialog.Builder(TestingCenterActivity.this);
                            AlertDialog.OnClickListener listener = new DialogInterface.OnClickListener() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            };

                            AlertDialog dialog = builder.setTitle("Appointment Timeslot Details")
                                    .setMessage(dialogMessage)
                                    .setPositiveButton("OK", listener)
                                    .setNegativeButton("Cancel", listener)
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    referenceEditText.setError("Please enter your customer reference number.");
                    referenceEditText.requestFocus();
                }
            }
        });


    }

    public void initCompaniesSpinner(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                database.initTestingCenters();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                companiesLabel.setVisibility(View.VISIBLE);
                companiesSpinner.setVisibility(View.VISIBLE);

                ArrayAdapter<TestingCenter> companiesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, application.testingCenters);
                companiesSpinner.setAdapter(companiesAdapter);
            }
        }.execute();

        companiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCompany = application.testingCenters.get(i);
                if(timingsRadioButton.isChecked())
                    initTimingsSpinner();
                else if(bookedRadioButton.isChecked()){
                    referenceEditText.setVisibility(View.VISIBLE);
                    checkButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    public void initTimingsSpinner(){
        String base = application.BASE_URL;
        String systemPath = application.TESTING_CENTER_PATH;
        String company = selectedCompany.path + "/";
        String service = application.TESTING_CENTER_GET_TIMINGS;
        String result = application.externalInterface.callWebService(base + systemPath + company + service);

        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray resultArray = resultObject.getJSONArray("result");

            ArrayList<String> timings = new ArrayList<>();

            for(int i=0; i<resultArray.length(); i++){
                JSONObject obj = resultArray.getJSONObject(i);
                selectedCompany.addTiming(obj);
                String display = obj.getString("date") + " at " + obj.getString("time");
                timings.add(display);
            }

            ArrayAdapter<String> packagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timings);
            timeslotsSpinner.setAdapter(packagesAdapter);

            timeslotsLabel.setVisibility(View.VISIBLE);
            timeslotsSpinner.setVisibility(View.VISIBLE);
            bookButton.setVisibility(View.VISIBLE);

            referenceEditText.setVisibility(View.GONE);
            checkButton.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void bookTiming(TestingCenter.Timing selectedTiming) {
        String base = application.BASE_URL;
        String systemPath = application.TESTING_CENTER_PATH;
        String company = selectedCompany.path + "/";
        String service = application.TESTING_CENTER_BOOK_TIMING;
        String params = selectedTiming.date.substring(0,2) + "/" + selectedTiming.date.substring(3,5) + "/" +
                selectedTiming.date.substring(6,10) + "/" + selectedTiming.time.substring(0,2) + selectedTiming.time.substring(3,5);

        Log.d("TestingCenter_Log", base + systemPath + company + service + params);
        String result = application.externalInterface.callWebService(base + systemPath + company + service + params);
        Log.d("TestingCenter_Log", result);
        try {
            if(!result.equals("{}")) {
                JSONObject resultObject = new JSONObject(result);
                application.currentUser.setTestingCenterRef(resultObject.getString("customer-no"));
                application.currentUser.setBookingDone(true);
                Toast.makeText(getApplicationContext(), "Book timeslot successfully.", Toast.LENGTH_LONG).show();
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBookedTimeslot(String customerNo){
        String base = application.BASE_URL;
        String systemPath = application.TESTING_CENTER_PATH;
        String company = selectedCompany.path + "/";
        String service = application.TESTING_CENTER_BOOKED_TIMING;
        String params = customerNo;

        Log.d("TestingCenter_Log", base + systemPath + company + service + params);
        String result = application.externalInterface.callWebService(base + systemPath + company + service + params);
        Log.d("TestingCenter_Log", result);
        return result;
    }

}
