package com.andrewnitu.githubmarkdownviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.andrewnitu.githubmarkdownviewer.R;

public class RepoActivity extends AppCompatActivity {
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repo);

        mTextView = (TextView) findViewById(R.id.repoName);

        Intent intent = getIntent();
        int position = intent.getIntExtra("ItemPosition", -1);

        mTextView.setText("Item Position " + position + "  Clicked");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
