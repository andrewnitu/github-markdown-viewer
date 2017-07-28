package com.andrewnitu.githubmarkdownviewer.fragment.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.activity.RepoActivity;
import com.andrewnitu.githubmarkdownviewer.adapter.ClickListener;
import com.andrewnitu.githubmarkdownviewer.adapter.favorites.RepoListFavoritesAdapter;
import com.andrewnitu.githubmarkdownviewer.component.RecyclerViewEmptySupport;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmRepo;
import com.andrewnitu.githubmarkdownviewer.model.local.Repo;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class FavoritesRepoFragment extends Fragment implements ClickListener {
    View rootView;

    private Realm realmInstance;

    private ArrayList<Repo> repos;
    private RepoListFavoritesAdapter adapter;

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.fragment_repo_favorites, container, false);

        RecyclerViewEmptySupport recyclerView;

        // Get our Realm instance
        realmInstance = Realm.getDefaultInstance();

        RealmQuery<RealmRepo> allRepoQuery = realmInstance.where(RealmRepo.class);

        RealmResults<RealmRepo> allRepoResults = allRepoQuery.findAll();

        repos = new ArrayList<>();

        for (int i = 0; i < allRepoResults.size(); i++) {
            repos.add(new Repo(allRepoResults.get(i).getName(), allRepoResults.get(i).getOwnerUserName()));
        }

        // Bind our UI elements
        recyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.recycler_view);

        recyclerView.setEmptyView(rootView.findViewById(R.id.recyclerview_empty_text));

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Set up RecyclerView row dividers
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        // Give our RecyclerView an adapter
        adapter = new RepoListFavoritesAdapter(repos);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        return rootView;
    }

    @Override
    public void onRowClicked(View view, int index) {
        Intent intent = new Intent(getActivity(), RepoActivity.class);
        intent.putExtra("Username", repos.get(index).getOwnerUserName());
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
