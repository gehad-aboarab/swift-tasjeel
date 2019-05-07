package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;
import com.cmp404.cloud_brokerapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private BrokerApplication application;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, forgotPasswordButton;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        application = (BrokerApplication) getApplication();
        database = application.database;

        emailEditText = (EditText) findViewById(R.id.login_emailEditText);
        passwordEditText = (EditText) findViewById(R.id.login_passwordEditText);
        loginButton = (Button) findViewById(R.id.login_loginButton);
        forgotPasswordButton = (Button) findViewById(R.id.login_forgotPasswordButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

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

                if(!cancel){
                    attemptLogin(email, password);
                }

            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void attemptLogin(final String email, final String password){
        new AsyncTask<Void, Void, Boolean>() {
            private JSONObject result;

            @Override
            protected Boolean doInBackground(Void... params) {
                result = database.login(email, password);
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
                        passwordEditText.setError("You have entered an incorrect password, please try again!");
                        passwordEditText.requestFocus();
                    }
                } else {
                  passwordEditText.setError("You have entered incorrect credentials, please try again!");
                  passwordEditText.requestFocus();
                }
            }
        }.execute();
    }

    public void loadProfile(JSONObject user){
        String name, registration_no, license_no, credit_card, email;
        try {
            name = user.getString("name");
            license_no = user.getString("license-no");
            registration_no = user.getString("registration-no");
            credit_card = user.getString("credit-card");
            email = user.getString("email");

            if(name!=null && license_no!=null && registration_no!=null && license_no!=null && credit_card!=null && email!=null)
                application.loadProfile(name, email, registration_no, license_no, credit_card);
            else
                Toast.makeText(getApplicationContext(), "Server error occurred. Please try again.", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
