package com.andrewnitu.githubmarkdownviewer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.model.File;

import java.util.List;

/**
 * Created by Andrew Nitu on 5/15/2017.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.BranchViewHolder> {
    private List<File> files;

    private TouchListener touchListener = null;

    public FileListAdapter(List<File> files){
        this.files = files;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @Override
    public BranchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_branch, viewGroup, false);
        return new BranchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BranchViewHolder bvh, int i) {
        bvh.branchTitle.setText(files.get(i).getName());
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView branchTitle;
        protected TextView branchUrl;

        public BranchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            branchTitle = (TextView)itemView.findViewById(R.id.branch_title);
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
