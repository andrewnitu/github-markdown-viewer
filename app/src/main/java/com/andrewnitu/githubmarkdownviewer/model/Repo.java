package com.andrewnitu.githubmarkdownviewer.model;

/**
 * Created by Andrew Nitu on 5/15/2017.
 */

public class Repo {
    String name;
    String url;

    public Repo (String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName(){
        return name;
    }
}
