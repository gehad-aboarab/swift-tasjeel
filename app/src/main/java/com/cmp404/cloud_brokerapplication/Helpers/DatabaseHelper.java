package com.cmp404.cloud_brokerapplication.Helpers;

import android.util.Log;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseHelper {
    final StitchAppClient client =
            Stitch.initializeDefaultAppClient("cloudbrokerapplication-cnhwv");
    final RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
    final RemoteMongoCollection<Document> collection =
            mongoClient.getDatabase("cloud-computing").getCollection("broker-user");

    public DatabaseHelper() {
        client.getAuth().loginWithCredential(new AnonymousCredential());
    }

    public JSONObject exists(final String email) {
        final ArrayList<Document> documents = new ArrayList<Document>();
        RemoteFindIterable<Document> iterable = collection.find();

        Task task = iterable.into(documents);
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Document d : documents) {
            if (d.get("email").equals(email)) {
                return new JSONObject(d);
            }
        }
        return null;
    }

    public JSONObject login(final String email, final String password) {
        JSONObject result = exists(email);
        if(result != null) {
            try {
                if (result.get("email").equals(email) && !result.get("password").equals(password)) {
                    result = new JSONObject();
                    result.put("valid", "false");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public JSONObject signup(final String email,
                             final String password,
                             final String name,
                             final String registrationNo,
                             final String licenseNo,
                             final String creditCard) {

        if(exists(email) == null) {
            Document document = new Document();
            document.put("name", name);
            document.put("email", email);
            document.put("password", password);
            document.put("registrationNo", registrationNo);
            document.put("licenseNo", licenseNo);
            document.put("creditCard", creditCard);
            collection.insertOne(document);

            return new JSONObject(document);

        } else{
            JSONObject result = new JSONObject();
            try {
                result.put("valid", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}
