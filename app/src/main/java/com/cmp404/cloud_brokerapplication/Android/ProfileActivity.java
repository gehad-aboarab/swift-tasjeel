package com.cmp404.cloud_brokerapplication.Android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmp404.cloud_brokerapplication.R;

public class ProfileActivity extends Activity {
    private TextView nameTextView, licensePlateTextView, emailTextView, registrationTextView;
    private Button processButton, editProfileButton;
    private BrokerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        application = (BrokerApplication)getApplication();

        nameTextView = (TextView)findViewById(R.id.profile_nameTextView);
        licensePlateTextView = (TextView)findViewById(R.id.profile_licenseTextView);
        registrationTextView = (TextView) findViewById(R.id.profile_registrationTextView);
        emailTextView = (TextView) findViewById(R.id.profile_emailTextView);
        processButton = (Button) findViewById(R.id.profile_startButton);
        editProfileButton = (Button) findViewById(R.id.profile_editProfileButton);

        nameTextView.setText(application.currentUser.getName());
        licensePlateTextView.setText(application.currentUser.getLicensePlate());
        registrationTextView.setText(application.currentUser.getRegistrationNo());
        emailTextView.setText(application.currentUser.getContact());

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }


}
