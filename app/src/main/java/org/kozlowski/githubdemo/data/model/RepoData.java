package org.kozlowski.githubdemo.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by and on 16.05.17.
 */

public class RepoData {
    private String name;
    private String description;
    private Owner owner;
    @SerializedName("html_url")
    private String htmlUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
