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
import com.andrewnitu.githubmarkdownviewer.activity.ViewActivity;
import com.andrewnitu.githubmarkdownviewer.adapter.ClickListener;
import com.andrewnitu.githubmarkdownviewer.adapter.favorites.FileListFavoritesAdapter;
import com.andrewnitu.githubmarkdownviewer.component.RecyclerViewEmptySupport;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmFile;
import com.andrewnitu.githubmarkdownviewer.model.local.File;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class FavoritesFileFragment extends Fragment implements ClickListener {
    View rootView;

    private Realm realmInstance;


    private ArrayList<File> files;
    private FileListFavoritesAdapter adapter;

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
        rootView = inflater.inflate(R.layout.fragment_file_favorites, container, false);

        RecyclerViewEmptySupport recyclerView;

        // Get our Realm instance
        realmInstance = Realm.getDefaultInstance();

        RealmQuery<RealmFile> allFileQuery = realmInstance.where(RealmFile.class);

        RealmResults<RealmFile> allFileResults = allFileQuery.findAll();

        files = new ArrayList<>();

        for (int i = 0; i < allFileResults.size(); i++) {
            files.add(new File(allFileResults.get(i).getOwnerUserName(), allFileResults.get(i).getRepoName(), allFileResults.get(i).getBranchName(), allFileResults.get(i).getPath()));
        }

        // Bind our UI elements
        recyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.recycler_view);

        // Give our RecyclerView an adapter
        adapter = new FileListFavoritesAdapter(files);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        recyclerView.setEmptyView(rootView.findViewById(R.id.recyclerview_empty_text));

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        // Set up RecyclerView row dividers
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        return rootView;
    }

    @Override
    public void onRowClicked(View view, int index) {
        Intent intent = new Intent(getActivity(), ViewActivity.class);

        // Attach the username, reponame, and file path within that repo to the intent
        intent.putExtra("Filepath", files.get(index).getPath());
        intent.putExtra("Username", files.get(index).getOwnerUserName());
        intent.putExtra("Reponame", files.get(index).getRepoName());
        intent.putExtra("Branchname", files.get(index).getBranchName());

        // Start the intent
        startActivity(intent);
    }

    @Override
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
