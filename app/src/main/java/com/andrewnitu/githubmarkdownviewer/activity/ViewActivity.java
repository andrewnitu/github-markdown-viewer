package com.andrewnitu.githubmarkdownviewer.activity;

/**
 * Created by Andrew Nitu on 6/8/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.adapter.ClickListener;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {
    final String baseUrl = "https://api.github.com";
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    String username;
    String reponame;
    String filepath;
    String branchname;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        reponame = intent.getStringExtra("Reponame");
        filepath = intent.getStringExtra("Filepath");
        branchname = intent.getStringExtra("Branchname");

        webView = (WebView) findViewById(R.id.web_view);
        //webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);

        fileRequest(username, reponame, filepath, branchname);
    }

    @Override // from AppCompatActivity
    public boolean onOptionsItemSelected(MenuItem item) {
        // Magic from StackOverflow
        switch ( item.getItemId() ) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                        webView.loadDataWithBaseURL("", response, mimeType, encoding, "");
                        findViewById(R.id.loading_panel).setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
}
