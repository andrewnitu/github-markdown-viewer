package com.andrewnitu.githubmarkdownviewer.activity;

/**
 * Created by Andrew Nitu on 6/8/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.adapter.RepoListAdapter;
import com.andrewnitu.githubmarkdownviewer.adapter.TouchListener;
import com.andrewnitu.githubmarkdownviewer.model.Repo;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewActivity extends AppCompatActivity implements TouchListener {
    final String baseUrl = "https://api.github.com";
    String username;
    String reponame;
    String filepath;
    String branchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        reponame = intent.getStringExtra("Reponame");
        filepath = intent.getStringExtra("Filepath");
        branchname = intent.getStringExtra("Branchname");

        fileRequest(username, reponame, filepath, branchname);
    }

    public void fileRequest(final String reqUsername, final String reqReponame, final String reqFilepath, final String reqBranchname) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the URL to request the specific file
        final String requestURL = baseUrl + "/repos/" + reqUsername + "/" + reqReponame + "/contents/" + reqFilepath + "?ref=" + reqBranchname;

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(requestURL,
                new Response.Listener<String>() {
                    // Do on a successful request
                    @Override
                    public void onResponse(String response) {
                        Log.e("SUCCESS", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Give an error!
                        Log.e("FAILURE", "At URL: " + requestURL);

                        Toast toast = Toast.makeText(getApplicationContext(), "Couldn't obtain that file!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/vnd.github.VERSION.html");

                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    @Override
    public void itemClicked(View view, int index) {

    }
}
