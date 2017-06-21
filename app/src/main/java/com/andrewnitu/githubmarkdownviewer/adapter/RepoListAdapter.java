package com.andrewnitu.githubmarkdownviewer.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private List<Repo> repos;

    private TouchListener touchListener = null;

    public RepoListAdapter(List<Repo> repos){
        this.repos = repos;
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_repo, viewGroup, false);
        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder rvh, int i) {
        rvh.repoTitle.setText(repos.get(i).getName());
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView repoTitle;
        protected TextView repoUrl;

        public RepoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            repoTitle = (TextView)itemView.findViewById(R.id.repo_title);
        }

        @Override
        public void onClick(View v) {
            if (touchListener != null) {
                touchListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }
}
