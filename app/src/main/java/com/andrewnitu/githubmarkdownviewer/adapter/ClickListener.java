package com.andrewnitu.githubmarkdownviewer.adapter;

import android.view.View;

/**
 * Created by Andrew Nitu on 5/20/2017.
 */

public interface ClickListener {
    void onFavouriteClicked(View view, int position);
    void onRowClicked(View view, int position);
}