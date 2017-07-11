package com.andrewnitu.githubmarkdownviewer.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmFile extends RealmObject {
    @PrimaryKey
    private String name;

    public RealmFile() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}