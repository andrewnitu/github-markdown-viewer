package com.andrewnitu.githubmarkdownviewer.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewEmptyFirstLoadSupport extends RecyclerView {
    private View emptyView;
    private boolean firstLoad = true;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0 && !firstLoad) {
                    firstLoad = true;
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptyFirstLoadSupport.this.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmptyFirstLoadSupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public RecyclerViewEmptyFirstLoadSupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptyFirstLoadSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptyFirstLoadSupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}