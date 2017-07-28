package com.andrewnitu.githubmarkdownviewer.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmRepo extends RealmObject {
    @PrimaryKey
    private String id;

    private String name;
    private String ownerUserName;

    public RealmRepo() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }
}