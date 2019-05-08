package com.cmp404.cloud_brokerapplication.Android;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Entities.Fine;
import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RtaActivity extends Activity {
    private EditText feesEditText;
    private Button payButton;
    private BrokerApplication application;
    private DatabaseHelper database;

    private String renewalFees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rta);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        application = (BrokerApplication) getApplication();
        database = application.database;

        feesEditText = (EditText) findViewById(R.id.rta_finesEditText);
        payButton = (Button) findViewById(R.id.rta_payButton);

        initRenewalFees();

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(renewalFees.equals("0")){
                    Toast.makeText(getApplicationContext(), "No renewal fees to pay.", Toast.LENGTH_LONG).show();
                } else {
                    payRenewalFees();
                }
            }
        });
    }

    public void initRenewalFees(){
        String base = application.BASE_URL;
        String systemPath = application.RTA_PATH;
        String service = application.RTA_RENEWAL_FEES;
        String params = application.currentUser.getLicensePlate() + "/" + application.currentUser.getRegistrationNo();

        Log.d("Rta_Tag", base+systemPath+service+params);
        String result = application.externalInterface.callWebService(base + systemPath + service + params);
        Log.d("Rta_Tag", result);

        try {
            JSONObject resultObject = new JSONObject(result);
            if (!result.equals("{}")) {
                renewalFees = resultObject.getString("fee");
                String display = "AED" + renewalFees;
                feesEditText.setText(display);

            } else {
                renewalFees = "0";
                feesEditText.setText("No renewal fees");
                application.currentUser.setPaidRenewal(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void payRenewalFees() {
        String base = application.BASE_URL;
        String systemPath = application.RTA_PATH;
        String service = application.RTA_RENEW_REGISTRATION;
        String params = application.currentUser.getLicensePlate() + "/" + application.currentUser.getRegistrationNo() +
                "/" + application.currentUser.getCreditCard() + "/" + renewalFees;

        Log.d("Rta_Tag", base+systemPath+service+params);
        String result = application.externalInterface.callWebService(base + systemPath + service + params);
        Log.d("Rta_Tag", result);

            if(!result.equals("{}")) {
                application.currentUser.setPaidRenewal(true);
                Toast.makeText(getApplicationContext(), "Renewal fees paid successfully.", Toast.LENGTH_LONG).show();
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_LONG).show();

            }
    }
}
