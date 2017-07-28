package com.andrewnitu.githubmarkdownviewer.adapter;

import android.view.View;

public interface ClickListener {
    void onFavouriteClicked(View view, int position);
    void onRowClicked(View view, int position);
}