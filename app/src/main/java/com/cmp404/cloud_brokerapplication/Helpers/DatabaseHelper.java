package com.cmp404.cloud_brokerapplication.Helpers;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by Gehad on 5/3/2019.
 */

public class DatabaseHelper {
    private MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String databaseName = "cloud-computing";

    public DatabaseHelper() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://Gehad:Aboarab97@cloud-computing-zqxty.mongodb.net/test?retryWrites=true"));
        database = mongoClient.getDatabase(databaseName);
    }

    public JSONObject login(String email, String password) {
        MongoCollection<Document> collection = database.getCollection("broker-user");
        FindIterable<Document> documents = collection.find();

        Log.d("DatabaseHelper", documents.toString());
        // Loop through the documents and return needed plan
        for (Document document : documents) {
            JSONObject result;
            if (document.get("email").equals(email) && document.get("password").equals(password)) {
                document.append("id", document.get("_id").toString());
                document.remove("_id");

                result = new JSONObject(document);
                Log.d("DatabaseHelper" ,result.toString());
                return result;
            }
        }
        return null;
    }

}
