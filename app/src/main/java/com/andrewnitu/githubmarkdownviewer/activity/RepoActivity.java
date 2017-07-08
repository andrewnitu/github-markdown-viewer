package com.andrewnitu.githubmarkdownviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.adapter.FileListAdapter;
import com.andrewnitu.githubmarkdownviewer.adapter.TouchListener;
import com.andrewnitu.githubmarkdownviewer.model.local.File;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class RepoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TouchListener {
    final String baseUrl = "https://api.github.com";

    TextView usernameText;
    TextView reponameText;
    String username;
    String reponame;
    String branchname;

    private RecyclerView recyclerView;
    private FileListAdapter adapter;
    private ArrayList<String> branches;
    private ArrayList<File> files;
    private ArrayAdapter<String> dataAdapter;
    private Spinner branchPicker;

    @Override // from AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        findViewById(R.id.loading_panel).setVisibility(View.GONE);

        // Initialize the ArrayList
        branches = new ArrayList<String>();
        files = new ArrayList<File>();

        // Bind the text fields
        usernameText = (TextView) findViewById(R.id.username);
        reponameText = (TextView) findViewById(R.id.reponame);

        // Bind the spinner and set listener
        branchPicker = (Spinner) findViewById(R.id.branch_picker);
        branchPicker.setOnItemSelectedListener(this);

        // Retrieve passed data from home activity
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        reponame = intent.getStringExtra("Reponame");

        // Set the text fields to the passed data
        usernameText.setText(username);
        reponameText.setText(reponame);

        // Initialize the adapter for the spinner
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branches);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        branchPicker.setAdapter(dataAdapter);

        // Pull the list of branches to populate the spinner
        branchesRequest(username, reponame);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Add the list separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new FileListAdapter(files);
        recyclerView.setAdapter(adapter);

        adapter.setTouchListener(this);
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

    @Override // from TouchListener
    public void itemClicked(View view, int index) {
        Intent intent = new Intent(this, ViewActivity.class);

        // Attach the username, reponame, and file path within that repo to the intent
        intent.putExtra("Username", username);
        intent.putExtra("Reponame", reponame);
        intent.putExtra("Branchname", branchname);
        intent.putExtra("Filepath", files.get(index).getPath());

        // Start the intent
        startActivity(intent);
    }

    @Override // from AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        branchname = parent.getItemAtPosition(position).toString();

        findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);

        filesListRequest(username, reponame, branchname);
    }

    @Override // from AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Fill out?
    }

    public void branchesRequest(final String reqUserName, final String reqRepoName) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the URL to request the repositories for a user
        String requestURL = baseUrl + "/repos/" + reqUserName + "/" + reqRepoName + "/branches";

        // Request a string response from the provided URL
        JsonArrayRequest stringRequest = new JsonArrayRequest(requestURL,
                new Response.Listener<JSONArray>() {
                    // Do on a successful request
                    @Override
                    public void onResponse(JSONArray response) {
                        Boolean hasMaster = false;
                        try {
                            int numExtracted = 0;

                            // For each repo
                            while (numExtracted < response.length()) {
                                // Retrieve the name
                                String branchName = response.getJSONObject(numExtracted).getString("name");

                                // Add the current branch to the list if it's NOT master,
                                // if it IS master remember that master exists and don't add it
                                if (branchName.equals("master")) {
                                    hasMaster = true;
                                }
                                else {
                                    branches.add(branchName);
                                }
                                numExtracted++;
                            }
                        } catch (JSONException e) {
                        }

                        // Sort the list of branches WITHOUT master
                        Collections.sort(branches);

                        // If the repo has master, add master to the beginning of the list so it shows first
                        if (hasMaster) {
                            branches.add(0, "master");
                        }

                        dataAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Couldn't get branches!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void filesListRequest(final String reqUserName, final String reqRepoName, final String reqBranchName) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the URL to request the repositories for a user
        String requestURL = baseUrl + "/repos/" + reqUserName + "/" + reqRepoName + "/git/trees/" + reqBranchName + "?recursive=1";

        // Request a string response from the provided URL
        JsonObjectRequest stringRequest = new JsonObjectRequest(requestURL, null,
                new Response.Listener<JSONObject>() {
                    // Do on a successful request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray tree = response.getJSONArray("tree");

                            int numExtracted = 0;

                            // For each file
                            while (numExtracted < tree.length()) {
                                // Retrieve the name
                                String path = tree.getJSONObject(numExtracted).getString("path");

                                // Add a new file to the list with the appropriate parameters
                                // TODO Finalize these parameters
                                files.add(new File(path, path));

                                numExtracted++;
                            }
                        } catch (JSONException e) {
                        }

                        // Update the RecyclerView (don't wait for the user to)
                        adapter.notifyDataSetChanged();

                        findViewById(R.id.loading_panel).setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Couldn't find branches!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
