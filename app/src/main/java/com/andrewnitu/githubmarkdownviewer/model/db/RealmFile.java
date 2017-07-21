package com.andrewnitu.githubmarkdownviewer.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmFile extends RealmObject {
    @PrimaryKey
    private String id;

    private String ownerUserName;
    private String repoName;
    private String path;
    private String branchName;

    public RealmFile() {}

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getPath() {
        return path;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}