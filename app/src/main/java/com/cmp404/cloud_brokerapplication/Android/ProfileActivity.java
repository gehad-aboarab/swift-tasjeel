package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.cmp404.cloud_brokerapplication.R;

public class ProfileActivity extends Activity {
 private TextView name, plate;
 private BrokerApplication Application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView)findViewById(R.id.profile_namemaintextview);
        plate = (TextView)findViewById(R.id.profile_licensetextview);
        Application = (BrokerApplication)getApplication();
        name.setText(Application.currentUserName);
        plate.setText(Application.currentLicenseNo);
    }


}
