package org.kozlowski.githubdemo.ui.component.recyclerview;

import android.widget.TextView;

import org.kozlowski.githubdemo.R;

import butterknife.BindView;

/**
 * Created by and on 16.05.17.
 */

public class RepoItem {

    private String repoName;
    private String description;
    private String loginOwner;
    private String htmlUrl;

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoginOwner() {
        return loginOwner;
    }

    public void setLoginOwner(String loginOwner) {
        this.loginOwner = loginOwner;
    }

    @Override
    public String toString() {
        return "RepoItem{" +
            "repoName='" + repoName + '\'' +
            ", description='" + description + '\'' +
            ", loginOwner='" + loginOwner + '\'' +
            ", htmlUrl='" + htmlUrl + '\'' +
            '}';
    }
}
