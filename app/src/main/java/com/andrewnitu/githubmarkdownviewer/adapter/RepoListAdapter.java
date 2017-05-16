package com.andrewnitu.githubmarkdownviewer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.model.Repo;

import java.util.List;

/**
 * Created by Andrew Nitu on 5/15/2017.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {
    List<Repo> repos;

    public RepoListAdapter(List<Repo> repos){
        this.repos = repos;
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main, viewGroup, false);
        RepoViewHolder viewHolder = new RepoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RepoViewHolder rvh, int i) {
        rvh.repoTitle.setText(repos.get(i).getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {
        TextView repoTitle;
        TextView repoUrl;

        RepoViewHolder(View itemView) {
            super(itemView);
            repoTitle = (TextView)itemView.findViewById(R.id.repo_title);
        }
    }
}
