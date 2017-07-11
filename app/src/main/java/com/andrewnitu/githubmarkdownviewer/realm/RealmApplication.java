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

        // TODO Check this deleteRealmIfMigrationNeeded() - What does it mean and how can we make it foolproof?
        RealmConfiguration config =
                new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("ghmdviewerfavos.realm").build();
        Realm.setDefaultConfiguration(config);
        Log.d("Realm setup", "Realm initialized and configured");
    }
}
