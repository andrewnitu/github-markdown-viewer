package com.andrewnitu.githubmarkdownviewer.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config =
                new RealmConfiguration.Builder().name("GHMDViewerFavourites").build();
        Realm.setDefaultConfiguration(config);
    }
}
