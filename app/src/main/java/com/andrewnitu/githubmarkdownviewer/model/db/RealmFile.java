package com.andrewnitu.githubmarkdownviewer.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmFile extends RealmObject {
    @PrimaryKey
    private String id;

    private String name;
    private String url;

    public RealmFile() {}

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}