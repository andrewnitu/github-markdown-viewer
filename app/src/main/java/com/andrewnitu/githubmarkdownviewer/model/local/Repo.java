package com.andrewnitu.githubmarkdownviewer.model.local;

/**
 * Data class to represent a GitHub Repo
 *
 * @author Andrew Nitu
 */
public class Repo {
    String name;
    String path; // Format as path with leading slash but no trailing

    public Repo (String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName(){
        return name;
    }

    public String getPath(){
        return path;
    }
}
