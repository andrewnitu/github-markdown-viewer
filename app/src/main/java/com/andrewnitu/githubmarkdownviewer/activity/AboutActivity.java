package com.andrewnitu.githubmarkdownviewer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.andrewnitu.githubmarkdownviewer.R;

public class AboutActivity extends AppCompatActivity {
    Button githubButton;
    Button playstoreButton;

    @Override // from AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        githubButton = (Button) findViewById(R.id.github_button);
        playstoreButton = (Button) findViewById(R.id.playstore_button);

        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/andrewnitu/githubmarkdownviewer")));
            }
        });

        playstoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.andrewnitu.githubmarkdownviewer"));
                startActivity(intent);
            }
        });
    }

    @Override // from AppCompatActivity
    public boolean onOptionsItemSelected(MenuItem item) {
        // Magic from StackOverflow
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
