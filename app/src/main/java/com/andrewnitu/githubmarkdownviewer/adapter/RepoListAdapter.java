package com.andrewnitu.githubmarkdownviewer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.model.local.Repo;

import java.util.List;

/**
 * Created by Andrew Nitu on 5/15/2017.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {
    private List<Repo> repos;

    private ClickListener clickListener = null;

    public RepoListAdapter(List<Repo> repos){
        this.repos = repos;
        this.clickListener = clickListener;
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
    public void onBindViewHolder(final RepoViewHolder rvh, int i) {
        rvh.repoTitle.setText(repos.get(i).getName());
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView repoTitle;
        private ImageView repoFavouriteIcon;

        private RepoViewHolder(View itemView) {
            super(itemView);

            repoTitle = (TextView) itemView.findViewById(R.id.repo_title);
            repoFavouriteIcon = (ImageView) itemView.findViewById(R.id.favourite_icon);

            itemView.setOnClickListener(this);
            repoFavouriteIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                if (v.getId() == repoFavouriteIcon.getId()) {
                    clickListener.onFavouriteClicked(v, getAdapterPosition());
                }
                else {
                    clickListener.onRowClicked(v, getAdapterPosition());
                }
            }
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
