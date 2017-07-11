package com.andrewnitu.githubmarkdownviewer.model.local;

/**
 * Data class to represent a GitHub Repo
 *
 * @author Andrew Nitu
 */
public class Repo {
    String repoName;
    String userName; // Format as path with leading slash but no trailing

    public Repo (String repoName, String userName) {
        this.repoName = repoName;
        this.userName = userName;
    }

    public String getName(){
        return repoName;
    }

    public String getOwnerUserName(){
        return userName;
    }
}
