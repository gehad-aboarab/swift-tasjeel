package com.cmp404.cloud_brokerapplication.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.cmp404.cloud_brokerapplication.Android.BrokerApplication;

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class WebServicesIO {
    public String result;
    public BrokerApplication application;

    public WebServicesIO(BrokerApplication application){
        this.application = application;
    }

    public String callWebService(final String url){
        try {
            result = new AsyncTask<Void, Void, String>(){
                private Response response;

                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        response = Util.get(url);
                        if (response.status != HttpURLConnection.HTTP_OK)
                            return "{}";
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return response.body;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result.equals("{}")) {
                        Log.d("WebServicesIO_Tag", "Oops..");
                    } else {
                        Log.d("WebServicesIO_Tag", response.body);
                    }
                }

            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }
}

