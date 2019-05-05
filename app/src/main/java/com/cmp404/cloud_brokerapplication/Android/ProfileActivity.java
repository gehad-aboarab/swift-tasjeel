package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.cmp404.cloud_brokerapplication.R;

public class ProfileActivity extends Activity {
    private TextView nameTextView, licensePlateTextView;
    private BrokerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        application = (BrokerApplication)getApplication();

        nameTextView = (TextView)findViewById(R.id.profile_nameTextView);
        licensePlateTextView = (TextView)findViewById(R.id.profile_licenseTextView);

        nameTextView.setText(application.currentUserName);
        licensePlateTextView.setText(application.currentLicenseNo);
    }


}
