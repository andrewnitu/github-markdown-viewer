package com.andrewnitu.githubmarkdownviewer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmFile;
import com.andrewnitu.githubmarkdownviewer.model.local.File;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Andrew Nitu on 5/15/2017.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.BranchViewHolder> {
    private List<File> files;

    private Realm realmInstance;

    private ClickListener clickListener = null;

    public FileListAdapter(List<File> files){
        this.files = files;

        realmInstance = Realm.getDefaultInstance();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @Override
    public BranchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_file, viewGroup, false);
        return new BranchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BranchViewHolder bvh, int i) {
        bvh.branchTitle.setText(files.get(i).getPath());

        if (realmInstance.where(RealmFile.class).equalTo("ownerUserName", files.get(i).getOwnerUserName())
                .equalTo("repoName", files.get(i).getRepoName()).equalTo("path", files.get(i).getPath()).equalTo("branchName", files.get(i).getBranchName()).findFirst() != null) {
            bvh.branchFavouriteIcon.setImageResource(R.drawable.ic_star_filled);
        }
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView branchTitle;
        private ImageView branchFavouriteIcon;

        private BranchViewHolder(View itemView) {
            super(itemView);

            branchTitle = (TextView) itemView.findViewById(R.id.file_title);
            branchFavouriteIcon = (ImageView) itemView.findViewById(R.id.favourite_icon);

            branchFavouriteIcon.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                if (v.getId() == branchFavouriteIcon.getId()) {
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
