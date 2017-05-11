package com.andrewnitu.githubmarkdownviewer;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    String baseUrl = "https://api.github.com";

    private EditText usernameBox;
    private RecyclerView list;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void httpRequest(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", "Got response: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Connection", "Error connecting");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void retrieveRepos(View view) {
        usernameBox = (EditText) findViewById(R.id.editText);
        list = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        list.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(myDataset);
        list.setAdapter(mAdapter);

        String requestURL = baseUrl + "/users/" + usernameBox.getText() + "/repos";

        Log.d("Button", "Sending request " + requestURL);

        httpRequest(requestURL);
    }
}
