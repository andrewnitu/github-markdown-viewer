package com.andrewnitu.githubmarkdownviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    String baseUrl = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void httpRequest(String url) {
        InputStream response;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    public void retrieveRepos(View view) {
        EditText usernameBox = (EditText)findViewById(R.id.editText);

        String requestUrl = baseUrl + "/users/" + usernameBox.getText() + "/repos";

        httpRequest(requestUrl);
    }

    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            // All your networking logic
            // should be here
        }
    });
}
