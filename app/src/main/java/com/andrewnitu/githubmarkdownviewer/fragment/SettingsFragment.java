package com.andrewnitu.githubmarkdownviewer.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.andrewnitu.githubmarkdownviewer.R;

/**
 * Created by Andrew Nitu on 6/22/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
