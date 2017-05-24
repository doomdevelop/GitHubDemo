package org.kozlowski.githubdemo.data;

import org.kozlowski.githubdemo.data.model.RepoData;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by and on 19.05.17.
 */

public class DaTaRepoMapper {
    public static List<RepoItem> transform(List<RepoData> repoDataList){
        final List<RepoItem> repoItemList = new ArrayList<>(repoDataList.size());
        for(RepoData repoData : repoDataList){
            repoItemList.add(transform(repoData));
        }
        return repoItemList;
    }

    private static RepoItem transform(RepoData repoData){
        final RepoItem repoItem = new RepoItem();
        repoItem.setHtmlUrl(repoData.getHtmlUrl());
        repoItem.setDescription(repoData.getDescription());
        repoItem.setLoginOwner(repoData.getOwner() != null ? repoData.getOwner().getLogin():null);
        repoItem.setRepoName(repoData.getName());
        return repoItem;
    }
}
