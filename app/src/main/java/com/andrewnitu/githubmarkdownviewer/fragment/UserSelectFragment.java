package com.andrewnitu.githubmarkdownviewer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.activity.RepoActivity;
import com.andrewnitu.githubmarkdownviewer.adapter.ClickListener;
import com.andrewnitu.githubmarkdownviewer.adapter.RepoListAdapter;
import com.andrewnitu.githubmarkdownviewer.component.RecyclerViewEmptyFirstLoadSupport;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmRepo;
import com.andrewnitu.githubmarkdownviewer.model.local.Repo;
import com.andrewnitu.githubmarkdownviewer.utility.ExtractIntFromEnd;
import com.andrewnitu.githubmarkdownviewer.utility.PaginationLinks;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserSelectFragment extends Fragment implements ClickListener {
    final String baseUrl = "https://api.github.com";

    private EditText usernameBox;
    private ArrayList<Repo> repos;
    private RepoListAdapter adapter;
    private String username;

    private Boolean firstSearch = true;

    private Realm realmInstance;

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView;

        rootView = inflater.inflate(R.layout.fragment_user_select, container, false);

        RecyclerViewEmptyFirstLoadSupport recyclerView;

        // Get our Realm instance
        realmInstance = Realm.getDefaultInstance();

        // Initialize empty Repo array which will be loaded into
        repos = new ArrayList<>();

        // Bind our UI elements
        usernameBox = (EditText) rootView.findViewById(R.id.edit_text);
        recyclerView = (RecyclerViewEmptyFirstLoadSupport) rootView.findViewById(R.id.recycler_view);

        // Give our RecyclerView an adapter
        adapter = new RepoListAdapter(repos);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        recyclerView.setEmptyView(rootView.findViewById(R.id.recyclerview_empty_text));

        // Set up our submit button
        View.OnClickListener btnSubmitClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                retrieveRepos(v);
            }
        };
        Button btnSubmit = (Button) rootView.findViewById(R.id.submit);
        btnSubmit.setOnClickListener(btnSubmitClickListener);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Set up RecyclerView row dividers
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        return rootView;
    }

    public void repoRequest(final String reqUsername) {
        int firstPage;
        int lastPage;

        // Instantiate the RequestQueue
        final RequestQueue queue = Volley.newRequestQueue(getContext());

        // Create the URL to request the repositories for a user
        final String requestUrl = baseUrl + "/users/" + reqUsername + "/repos";

        // TODO needs heavy reworking, works but likely horribly inefficient
        JsonObjectRequest headersRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>() {
                    // Do on a successful request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int firstPage;
                            int lastPage;

                            firstPage = 1;

                            if (response.has("Link")) {
                                String linkHeader = response.getString("Link");
                                PaginationLinks paginationLinks = new PaginationLinks(linkHeader);
                                lastPage = ExtractIntFromEnd.extractEndInt(paginationLinks.getLast());
                            }
                            else {
                                lastPage = 1;
                            }

                            JsonArrayRequest arrayRequest;
                            String requestUrlString = requestUrl;

                            for (int i = firstPage; i <= lastPage; i++) {
                                requestUrlString = requestUrl + "?page=" + i + "&per_page=100";

                                arrayRequest = new JsonArrayRequest(requestUrlString,
                                        new Response.Listener<JSONArray>() {
                                            // Do on a successful request
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                // If successful, clear the current repo list to make way for the new one
                                                username = reqUsername;

                                                try {
                                                    int numExtracted = 0;

                                                    // For each repo
                                                    while (numExtracted < response.length()) {
                                                        // Retrieve the repository name
                                                        String repoName = response.getJSONObject(numExtracted).getString("name");

                                                        // TODO: Add the URL property
                                                        repos.add(new Repo(repoName, reqUsername));
                                                        numExtracted++;
                                                    }

                                                    Collections.sort(repos, new Comparator<Repo>() {
                                                        @Override
                                                        public int compare(Repo o1, Repo o2) {
                                                            return o1.getName().compareToIgnoreCase(o2.getName());
                                                        }
                                                    });
                                                } catch (JSONException e) {
                                                    Log.e("Debug", "Error parsing files response JSON");
                                                }

                                                // Update the RecyclerView (don't wait for the user to)
                                                adapter.notifyDataSetChanged();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Give an error!
                                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Couldn't find that user!", Toast.LENGTH_LONG);
                                                toast.show();

                                                usernameBox.setText("");
                                            }
                                        });

                                queue.add(arrayRequest);
                            }

                        } catch (JSONException e) {
                            Log.e("test", "some kinda fail");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Give an error!
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Couldn't get the necessary headers", Toast.LENGTH_LONG);
                        toast.show();

                        usernameBox.setText("");
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                JSONObject jsonResponse = new JSONObject(response.headers);
                return Response.success(jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        queue.add(headersRequest);
    }

    public void retrieveRepos(View view) {
        // Close the keyboard (using magic)
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (!usernameBox.getText().toString().equals(username)) {
            repos.clear();
            repoRequest(usernameBox.getText().toString());
        }
    }

    @Override
    public void onRowClicked(View view, int index) {
        Intent intent = new Intent(getActivity(), RepoActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("Reponame", repos.get(index).getName());

        startActivity(intent);
    }

    @Override
    public void onFavouriteClicked(View view, int index) {
        RealmQuery<RealmRepo> repoQuery = realmInstance.where(RealmRepo.class).equalTo("name", repos.get(index).getName()).equalTo("ownerUserName", repos.get(index).getOwnerUserName());

        RealmResults<RealmRepo> repoResults = repoQuery.findAll();

        int numResults = repoResults.size();

        realmInstance.beginTransaction();
        if (numResults == 0) {
            Log.d("Realm Transaction", "Added Repo object");
            RealmRepo repo = realmInstance.createObject(RealmRepo.class, UUID.randomUUID().toString());
            repo.setName(repos.get(index).getName());
            repo.setOwnerUserName(repos.get(index).getOwnerUserName());
            switchFavouritesIcon(true, view);
        } else {
            Log.d("Realm Transaction", "Removed Repo object");
            repoResults.first().deleteFromRealm();
            switchFavouritesIcon(false, view);
        }
        realmInstance.commitTransaction();
    }

    // Switches the favourites icon in the view
    // Takes a state (true for favourited, false for unfavourited) and the View of the row clicked
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
}