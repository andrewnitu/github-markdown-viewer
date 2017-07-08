package com.andrewnitu.githubmarkdownviewer.realm;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder().name("ghmdviewerfavos.realm").build();
        Realm.setDefaultConfiguration(config);
        Log.d("Realm setup", "Realm initialized and configured");
    }
}
