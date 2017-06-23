package com.andrewnitu.githubmarkdownviewer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.activity.RepoActivity;
import com.andrewnitu.githubmarkdownviewer.adapter.RepoListAdapter;
import com.andrewnitu.githubmarkdownviewer.adapter.TouchListener;
import com.andrewnitu.githubmarkdownviewer.model.Repo;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserSelectFragment extends Fragment implements TouchListener {
    final String baseUrl = "https://api.github.com";

    private EditText usernameBox;
    private RecyclerView recyclerView;
    private ArrayList<Repo> repos;
    private RepoListAdapter adapter;
    private String username;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_user_select, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);

        repos = new ArrayList<Repo>();

        usernameBox = (EditText) rootView.findViewById(R.id.edit_text);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new RepoListAdapter(repos);
        recyclerView.setAdapter(adapter);

        adapter.setTouchListener(this);

        return rootView;
    }

    public void repoRequest(final String reqUsername) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Create the URL to request the repositories for a user
        String requestURL = baseUrl + "/users/" + reqUsername + "/repos";

        // Request a string response from the provided URL
        JsonArrayRequest stringRequest = new JsonArrayRequest(requestURL,
                new Response.Listener<JSONArray>() {
                    // Do on a successful request
                    @Override
                    public void onResponse(JSONArray response) {
                        // If successful, clear the current repo list to make way for the new one
                        repos.clear();
                        username = reqUsername;

                        try {
                            int numExtracted = 0;

                            // For each repo
                            while (numExtracted < response.length()) {
                                // Retrieve the name
                                String repoName = response.getJSONObject(numExtracted).getString("name");

                                // TODO: Add the URL property
                                repos.add(new Repo(repoName,
                                        response.getJSONObject(numExtracted).getString("name")));
                                numExtracted++;
                            }
                        } catch (JSONException e) {
                        }

                        rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);

                        // Update the RecyclerView (don't wait for the user to)
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);

                        // Give an error!
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Couldn't find that user!", Toast.LENGTH_LONG);
                        toast.show();

                        usernameBox.setText("");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void retrieveRepos(View view) {
        // Close the keyboard (using magic)
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);

        repoRequest(usernameBox.getText().toString());
    }

    @Override
    public void itemClicked(View view, int index) {
        Intent intent = new Intent(getActivity(), RepoActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("Reponame", repos.get(index).getName());

        startActivity(intent);
    }
}
