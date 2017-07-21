package com.andrewnitu.githubmarkdownviewer.model.local;

/**
 * Data class to represent a GitHub File (inside a Repo, on a Branch)
 *
 * @author Andrew Nitu
 */
public class File {
    String ownerUserName;
    String repoName;
    String branchName;
    String path;

    public File(String ownerUserName, String repoName,String branchName, String path) {
        this.ownerUserName = ownerUserName;
        this.repoName = repoName;
        this.path = path;
        this.branchName = branchName;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
