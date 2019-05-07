package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Helpers.Util;
import com.cmp404.cloud_brokerapplication.R;
import com.cmp404.cloud_brokerapplication.Helpers.Response;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private Button loginButton, signupButton;
    private BrokerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (BrokerApplication) getApplication();
        application.initDB();

        loginButton = (Button) findViewById(R.id.main_loginButton);
        signupButton = (Button) findViewById(R.id.main_signupButton);

        ButtonListener buttonListener = new ButtonListener();
        loginButton.setOnClickListener(buttonListener);
        signupButton.setOnClickListener(buttonListener);

    }

    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.main_loginButton) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } else if(view.getId() == R.id.main_signupButton) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }

        }
    }
}
