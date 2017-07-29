package com.andrewnitu.githubmarkdownviewer.adapter.favorites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.adapter.ClickListener;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmFile;
import com.andrewnitu.githubmarkdownviewer.model.local.File;

import java.util.List;

import io.realm.Realm;

public class FileListFavoritesAdapter extends RecyclerView.Adapter<FileListFavoritesAdapter.FileViewHolder> {
    private List<File> files;

    private Realm realmInstance;

    private ClickListener clickListener = null;

    public FileListFavoritesAdapter(List<File> files){
        this.files = files;

        realmInstance = Realm.getDefaultInstance();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_file_favorites, viewGroup, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FileViewHolder fvh, int i) {
        fvh.fileTitleAndBranch.setText(files.get(i).getBranchName() + " - " + files.get(i).getPath());
        fvh.fileRepoAndOwnerUserName.setText(files.get(i).getOwnerUserName() + " - " + files.get(i).getRepoName());

        if (realmInstance.where(RealmFile.class).equalTo("ownerUserName", files.get(i).getOwnerUserName())
                .equalTo("repoName", files.get(i).getRepoName()).equalTo("path", files.get(i).getPath()).equalTo("branchName", files.get(i).getBranchName()).findFirst() != null) {
            fvh.fileFavouriteIcon.setImageResource(R.drawable.ic_star_filled);
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fileTitleAndBranch;
        private TextView fileRepoAndOwnerUserName;
        private ImageView fileFavouriteIcon;

        private FileViewHolder(View itemView) {
            super(itemView);

            fileTitleAndBranch = (TextView) itemView.findViewById(R.id.file_title_and_branch);
            fileRepoAndOwnerUserName = (TextView) itemView.findViewById(R.id.file_repo_and_owner_user_name);
            fileFavouriteIcon = (ImageView) itemView.findViewById(R.id.favourite_icon);

            fileFavouriteIcon.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                if (v.getId() == fileFavouriteIcon.getId()) {
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
