package com.andrewnitu.githubmarkdownviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.adapter.ClickListener;
import com.andrewnitu.githubmarkdownviewer.adapter.FileListAdapter;
import com.andrewnitu.githubmarkdownviewer.component.RecyclerViewEmptyFirstLoadSupport;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmFile;
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
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RepoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ClickListener {
    final String baseUrl = "https://api.github.com";

    TextView usernameText;
    TextView reponameText;
    String username;
    String reponame;
    String branchname;

    private FileListAdapter adapter;
    private ArrayList<String> branches;
    private ArrayList<File> files;
    private ArrayAdapter<String> dataAdapter;

    private Realm realmInstance;

    @Override // from AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        RecyclerViewEmptyFirstLoadSupport recyclerView;
        Spinner branchPicker;

        // Get our Realm instance to write to
        realmInstance = Realm.getDefaultInstance();

        // Initialize the ArrayList
        branches = new ArrayList<>();
        files = new ArrayList<>();

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
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        branchPicker.setAdapter(dataAdapter);

        // Pull the list of branches to populate the spinner
        branchesRequest(username, reponame);

        recyclerView = (RecyclerViewEmptyFirstLoadSupport) findViewById(R.id.recycler_view);

        adapter = new FileListAdapter(files);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        recyclerView.setEmptyView(findViewById(R.id.recyclerview_empty_text));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Add the list separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    @Override // from AppCompatActivity
    public boolean onOptionsItemSelected(MenuItem item) {
        // Magic from StackOverflow
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // from ClickListener
    public void onRowClicked(View view, int index) {
        Intent intent = new Intent(this, ViewActivity.class);

        // Attach the username, reponame, and file path within that repo to the intent
        intent.putExtra("Filepath", files.get(index).getPath());
        intent.putExtra("Username", files.get(index).getOwnerUserName());
        intent.putExtra("Reponame", files.get(index).getRepoName());
        intent.putExtra("Branchname", files.get(index).getBranchName());

        // Start the intent
        startActivity(intent);
    }

    @Override // from ClickListener
    public void onFavouriteClicked(View view, int index) {
        RealmQuery<RealmFile> fileQuery = realmInstance.where(RealmFile.class).equalTo("ownerUserName", files.get(index).getOwnerUserName())
                .equalTo("repoName", files.get(index).getRepoName()).equalTo("path", files.get(index).getPath()).equalTo("branchName", files.get(index).getBranchName());

        RealmResults<RealmFile> fileResults = fileQuery.findAll();

        int numResults = fileResults.size();

        realmInstance.beginTransaction();
        if (numResults == 0) {
            Log.d("Realm Transaction", "Added Repo object");
            RealmFile file = realmInstance.createObject(RealmFile.class, UUID.randomUUID().toString());
            file.setOwnerUserName(files.get(index).getOwnerUserName());
            file.setRepoName(files.get(index).getRepoName());
            file.setPath(files.get(index).getPath());
            file.setBranchName(files.get(index).getBranchName());
            switchFavouritesIcon(true, view);
        } else {
            Log.d("Realm Transaction", "Removed Repo object");
            fileResults.first().deleteFromRealm();
            switchFavouritesIcon(false, view);
        }
        realmInstance.commitTransaction();
    }

    public void switchFavouritesIcon(boolean state, View view) {
        ImageView icon = (ImageView) view.findViewById(R.id.favourite_icon);
        if (state) {
            icon.setImageResource(R.drawable.ic_star_filled);
        } else {
            icon.setImageResource(R.drawable.ic_star_empty);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realmInstance.close();
    }

    @Override // from AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        files.clear();

        branchname = parent.getItemAtPosition(position).toString();

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

                            // For each branch
                            while (numExtracted < response.length()) {
                                // Retrieve the name
                                String branchName = response.getJSONObject(numExtracted).getString("name");

                                // Add the current branch to the list if it's NOT master,
                                // if it IS master remember that master exists and don't add it
                                if (branchName.equals("master")) {
                                    hasMaster = true;
                                } else {
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

        // Create the URL to request all files in a user's repo under a branch
        final String requestURL = baseUrl + "/repos/" + reqUserName + "/" + reqRepoName + "/git/trees/" + reqBranchName + "?recursive=1";

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
                                if (tree.getJSONObject(numExtracted).has("url")) {
                                    // Retrieve the name
                                    String path = tree.getJSONObject(numExtracted).getString("path");

                                    // Add a new file to the list with the appropriate parameters
                                    files.add(new File(reqUserName, reqRepoName, reqBranchName, path));
                                }

                                numExtracted++;
                            }
                        } catch (JSONException e) {
                            Log.e("JSON Parse Error", "Error parsing file JSON!");
                        }

                        // Update the RecyclerView (don't wait for the user to)
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Fetch Error", "Error fetching list of files!");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
