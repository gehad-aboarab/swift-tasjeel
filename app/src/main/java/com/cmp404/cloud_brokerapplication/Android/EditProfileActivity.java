package com.cmp404.cloud_brokerapplication.Android;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmp404.cloud_brokerapplication.R;

public class EditProfileActivity extends Activity {
    private BrokerApplication application;
    private EditText nameEditText, emailEditText, licenseEditText, registrationEditText, ccEditText;
    private Button saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        application = (BrokerApplication)getApplication();

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        nameEditText = (EditText) findViewById(R.id.editProfile_nameEditText);
        emailEditText = (EditText) findViewById(R.id.editProfile_emailEditText);
        licenseEditText = (EditText) findViewById(R.id.editProfile_licenseEditText);
        registrationEditText = (EditText) findViewById(R.id.editProfile_registrationEditText);
        ccEditText = (EditText) findViewById(R.id.editProfile_ccEditText);
        saveButton = (Button) findViewById(R.id.editProfile_saveButton);
        cancelButton = (Button) findViewById(R.id.editProfile_cancelButton);

        nameEditText.setText(application.currentUser.getName());
        emailEditText.setText(application.currentUser.getContact());
        licenseEditText.setText(application.currentUser.getLicensePlate());
        registrationEditText.setText(application.currentUser.getRegistrationNo());
        ccEditText.setText(application.currentUser.getCreditCard());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                if(nameEditText.getText().toString().isEmpty()){
                    cancel = true;
                    nameEditText.setError("Field cannot be empty.");
                    nameEditText.requestFocus();
                } if(emailEditText.getText().toString().isEmpty()){
                    cancel = true;
                    emailEditText.setError("Field cannot be empty.");
                    emailEditText.requestFocus();
                } if(licenseEditText.getText().toString().isEmpty()){
                    cancel = true;
                    licenseEditText.setError("Field cannot be empty.");
                    licenseEditText.requestFocus();
                } if(registrationEditText.getText().toString().isEmpty()){
                    cancel = true;
                    registrationEditText.setError("Field cannot be empty.");
                    registrationEditText.requestFocus();
                } if(ccEditText.getText().toString().isEmpty()){
                    cancel = true;
                    ccEditText.setError("Field cannot be empty.");
                    ccEditText.requestFocus();
                }

                if(!cancel) {
                    application.currentUser.setName(nameEditText.getText().toString());
                    application.currentUser.setContact(emailEditText.getText().toString());
                    application.currentUser.setLicensePlate(licenseEditText.getText().toString());
                    application.currentUser.setRegistrationNo(registrationEditText.getText().toString());
                    application.currentUser.setCreditCard(ccEditText.getText().toString());
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
