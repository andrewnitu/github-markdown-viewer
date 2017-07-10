package com.andrewnitu.githubmarkdownviewer.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRepo extends RealmObject {
    @PrimaryKey
    private String name;
    private String ownerUserName;

    public RealmRepo(String name, String ownerUserName) {
        this.name = name;
        this.ownerUserName = ownerUserName;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }
}