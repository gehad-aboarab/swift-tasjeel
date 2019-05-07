package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Entities.Fine;
import com.cmp404.cloud_brokerapplication.Entities.Insurance;
import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DubaiPoliceActivity extends Activity {
    private Spinner finesSpinner;
    private Button payButton, payAllButton;
    private BrokerApplication application;
    private DatabaseHelper database;

    private Fine selectedFine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dubai_police);

        application = (BrokerApplication) getApplication();
        database = application.database;

        finesSpinner = (Spinner) findViewById(R.id.police_finesSpinner);
        payButton = (Button) findViewById(R.id.police_payButton);
        payAllButton = (Button) findViewById(R.id.police_payAllButton);

        initFinesSpinner();

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.trafficFines.size() > 0 && application.trafficFines != null) {
                    selectedFine = (Fine) finesSpinner.getSelectedItem();
                    payFine(selectedFine.fineNo);
                } else
                    Toast.makeText(getApplicationContext(), "No fines selected.", Toast.LENGTH_LONG).show();
            }
        });

        payAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.trafficFines.size() > 0 && application.trafficFines != null)
                    payFine("all");
                else
                    Toast.makeText(getApplicationContext(), "No fines to pay.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initFinesSpinner() {
        String base = application.BASE_URL;
        String systemPath = application.DUBAI_POLICE_PATH;
        String service = application.DUBAI_POLICE_GET_FINES;
        String params = application.currentUser.licensePlate;

        Log.d("DubaiPolice_Tag", base+systemPath+service+params);
        String result = application.externalInterface.callWebService(base + systemPath + service + params);
        Log.d("DubaiPolice_Tag", result);

        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray resultArray = resultObject.getJSONArray("result");
            application.trafficFines = new ArrayList<>();
            if (resultArray.length() != 0) {
                for (int i = 0; i < resultArray.length(); i++) {
                    application.trafficFines.add(new Fine(resultArray.getJSONObject(i)));
                }
                ArrayAdapter<Fine> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, application.trafficFines);
                finesSpinner.setAdapter(spinnerAdapter);

            } else if (resultArray.length() == 0 || result.equals("{}")){
                ArrayList<String> fines = new ArrayList<>();
                fines.add("No fines issued to your license plate");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fines);
                finesSpinner.setAdapter(spinnerAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void payFine(String fineNo) {
        String base = application.BASE_URL;
        String systemPath = application.DUBAI_POLICE_PATH;
        String service = application.DUBAI_POLICE_PAYMENT;
        String params = fineNo + "/" + application.currentUser.licensePlate + "/" + application.currentUser.creditCard + "/" + "1000";

        Log.d("DubaiPolice_Tag", base + systemPath + service + params);
        String result = application.externalInterface.callWebService(base + systemPath + service + params);

        if (!result.equals("{}")) {
            Toast.makeText(getApplicationContext(), "Fine payment made!", Toast.LENGTH_LONG).show();
            if (fineNo.equals("all")) {
                application.currentUser.paidFines = true;
            }
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_LONG).show();
        }
    }
}
