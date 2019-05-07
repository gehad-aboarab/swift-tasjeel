package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends Activity {
    private BrokerApplication application;
    private EditText nameEditText, emailEditText, passwordEditText, licenseEditText, registrationEditText, ccEditText, cvvEditText;
    private Button signupButton;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        application = (BrokerApplication) getApplication();
        database = application.database;

        emailEditText = (EditText) findViewById(R.id.signup_emailEditText);
        passwordEditText = (EditText) findViewById(R.id.signup_passwordEditText);
        nameEditText = (EditText) findViewById(R.id.signup_nameEditText);
        licenseEditText = (EditText) findViewById(R.id.signup_plateEditText);
        registrationEditText = (EditText) findViewById(R.id.signup_registrationEditText);
        ccEditText = (EditText) findViewById(R.id.signup_creditEditText);
        cvvEditText = (EditText) findViewById(R.id.signup_cvvEditText);
        signupButton = (Button) findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String license = licenseEditText.getText().toString();
                String registration = registrationEditText.getText().toString();
                String cc = ccEditText.getText().toString();
                String cvv = cvvEditText.getText().toString();

                if (email.isEmpty()) {
                    emailEditText.setError("Please enter an email address.");
                    emailEditText.requestFocus();
                    cancel = true;
                } else if (!email.contains("@") || !email.contains(".")) {
                    emailEditText.setError("Please enter a valid email address.");
                    emailEditText.requestFocus();
                    cancel = true;
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Please enter an email address.");
                    passwordEditText.requestFocus();
                    cancel = true;
                }
                if (name.isEmpty()) {
                    nameEditText.setError("Please enter an email address.");
                    nameEditText.requestFocus();
                    cancel = true;
                }
                if (license.isEmpty()) {
                    licenseEditText.setError("Please enter an email address.");
                    licenseEditText.requestFocus();
                    cancel = true;
                }
                if (registration.isEmpty()) {
                    registrationEditText.setError("Please enter an email address.");
                    registrationEditText.requestFocus();
                    cancel = true;
                }
                if (cc.isEmpty()) {
                    ccEditText.setError("Please enter an email address.");
                    ccEditText.requestFocus();
                    cancel = true;
                }
                if (cvv.isEmpty()) {
                    cvvEditText.setError("Please enter an email address.");
                    cvvEditText.requestFocus();
                    cancel = true;
                }

                if(!cancel){
                    attemptSignup(email, password, name, registration, license, cc);
                }
            }
        });

    }

    public void attemptSignup(final String email, final String password, final String name, final String registration, final String license, final String cc){
        new AsyncTask<Void, Void, Boolean>() {
            private JSONObject result;

            @Override
            protected Boolean doInBackground(Void... params) {
                result = database.signup(email, password, name, registration, license, cc);
                if(result == null)
                    return false;
                else
                    return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                // Access system if authentication verified, otherwise show error
                if (success) {
                    try {
                    if(result.get("password").equals(password)){
                        loadProfile(result);
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    emailEditText.setError("This email is used by another account.");
                    emailEditText.requestFocus();
                }


                } else {
                    Toast.makeText(getApplicationContext(), "Server error occured, please try again!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    public void loadProfile(JSONObject user){
//        String name, registration_no, license_no, credit_card, email;
//        try {
//            name = user.getString("name");
//            license_no = user.getString("license-no");
//            registration_no = user.getString("registration-no");
//            credit_card = user.getString("credit-card");
//            email = user.getString("email");

            if(user!=null)//name!=null && license_no!=null && registration_no!=null && license_no!=null && credit_card!=null && email!=null)
                application.loadProfile(user); //name, email, registration_no, license_no, credit_card);
            else
                Toast.makeText(getApplicationContext(), "Server error occurred. Please try again.", Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
