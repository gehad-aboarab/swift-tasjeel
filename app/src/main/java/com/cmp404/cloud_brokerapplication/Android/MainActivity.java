package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.R;
import com.cmp404.cloud_brokerapplication.Helpers.Response;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.main_loginButton);
        signupButton = (Button) findViewById(R.id.main_signupButton);

        ButtonListener buttonListener = new ButtonListener();
        loginButton.setOnClickListener(buttonListener);
        signupButton.setOnClickListener(buttonListener);

        testAccess();
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

    public void testAccess(){
        new AsyncTask<Void, Void, Boolean>(){
            private Response response;

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean)
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("http://google.com").openConnection();
                    connection.getInputStream();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
//                try {
//                    response = Util.get("http://localhost:8080/Broker/rest/dubai-police/check-fines/1234");
//                    if (response.status != HttpURLConnection.HTTP_OK)
//                        return false;
//                    Log.d("CLOUD", response.body);
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            Toast.makeText(MainActivity.this, response.body, Toast.LENGTH_SHORT).show();
////                        }
////                    });
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                return true;
            }
        }.execute();
    }
}
