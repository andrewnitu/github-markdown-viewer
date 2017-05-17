package com.andrewnitu.githubmarkdownviewer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.adapter.RepoListAdapter;
import com.andrewnitu.githubmarkdownviewer.model.Repo;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String baseUrl = "https://api.github.com";

    private EditText usernameBox;
    private RecyclerView recyclerView;
    private ArrayList<Repo> repos;
    private RepoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repos = new ArrayList<Repo>();

        usernameBox = (EditText) findViewById(R.id.edit_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        adapter = new RepoListAdapter(repos);
        recyclerView.setAdapter(adapter);
    }

    public void httpRequest(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        repos.clear();

                        try {
                            Log.e("Response", "Got response: " + response.toString(4));

                            int numExtracted = 0;

                            while (numExtracted < response.length()) {
                                repos.add(new Repo(response.getJSONObject(numExtracted).getString("name"),
                                        response.getJSONObject(numExtracted).getString("name")));
                                numExtracted++;
                            }
                        } catch (JSONException e) {
                        }

                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Connection", "Error connecting");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void retrieveRepos(View view) {
        String requestURL = baseUrl + "/users/" + usernameBox.getText() + "/repos";

        Log.d("Button", "Sending request " + requestURL);

        httpRequest(requestURL);
    }
}
