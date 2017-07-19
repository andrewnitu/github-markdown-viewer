package com.andrewnitu.githubmarkdownviewer.fragment.favourites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewnitu.githubmarkdownviewer.R;

public class FavoritesRepoFragment extends Fragment {
    View rootView;
    ActionBar savedActivityToolbar;

    public FavoritesRepoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.fragment_repo_favorites, container, false);

        return rootView;
    }

}
