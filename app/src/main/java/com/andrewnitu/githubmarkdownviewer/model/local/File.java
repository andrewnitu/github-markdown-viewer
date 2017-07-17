package com.andrewnitu.githubmarkdownviewer.model.local;

/**
 * Data class to represent a GitHub File (inside a Repo, on a Branch)
 *
 * @author Andrew Nitu
 */
public class File {
    String name;
    String branch;
    String url;

    public File (String name, String branch, String url) {
        this.name = name;
        this.branch = branch;
        this.url = url;
    }

    public String getName(){
        return name;
    }

    public String getBranch(){
        return branch;
    }

    public String getUrl(){
        return url;
    }
}
