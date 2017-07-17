package com.andrewnitu.githubmarkdownviewer.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmFile;
import com.andrewnitu.githubmarkdownviewer.model.db.RealmRepo;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Andrew Nitu on 6/22/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    Preference deleteRepos;
    Preference deleteFiles;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        deleteRepos = findPreference("clearSavedRepos");
        deleteFiles = findPreference("clearSavedFiles");

        deleteRepos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Realm realmInstance = Realm.getDefaultInstance();

                RealmQuery<RealmRepo> repoQuery = realmInstance.where(RealmRepo.class);

                RealmResults<RealmRepo> repoResults = repoQuery.findAll();

                realmInstance.beginTransaction();

                repoResults.deleteAllFromRealm();

                realmInstance.commitTransaction();

                return false;
            }
        });

        deleteFiles.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Realm realmInstance = Realm.getDefaultInstance();

                RealmQuery<RealmFile> fileQuery = realmInstance.where(RealmFile.class);

                RealmResults<RealmFile> fileResults = fileQuery.findAll();

                realmInstance.beginTransaction();

                fileResults.deleteAllFromRealm();

                realmInstance.commitTransaction();

                return false;
            }
        });
    }
}
