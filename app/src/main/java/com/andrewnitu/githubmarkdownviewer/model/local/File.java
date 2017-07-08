package com.andrewnitu.githubmarkdownviewer.model.local;

/**
 * Data class to represent a GitHub File (inside a Repo, on a Branch)
 *
 * @author Andrew Nitu
 */
public class File {
    String name;
    String path;

    public File (String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName(){
        return name;
    }

    public String getPath(){
        return name;
    }
}
