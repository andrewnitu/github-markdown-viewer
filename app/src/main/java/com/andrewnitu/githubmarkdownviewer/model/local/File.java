package com.andrewnitu.githubmarkdownviewer.model.local;

/**
 * Data class to represent a GitHub File (inside a Repo, on a Branch)
 *
 * @author Andrew Nitu
 */
public class File {
    String name;
    String url;

    public File (String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}
