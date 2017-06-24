package com.andrewnitu.githubmarkdownviewer.model;

/**
 * Data class to represent a GitHub branch (of a Repo)
 *
 * @author Andrew Nitu
 */
public class Branch {
    /**
     * The current name of this branch
     */
    private String name;

    /**
     * @param name The name this branch is initially given
     */
    public Branch (String name) {
        this.name = name;
    }

    /**
     * Returns the name of the branch
     *
     * @return The name of the branch
     */
    public String getName(){
        return name;
    }
}
