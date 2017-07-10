package com.andrewnitu.githubmarkdownviewer.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmFile extends RealmObject {
    @PrimaryKey
    private String name;

    public RealmFile(String name) {
    }

    /* getters and setters */
}