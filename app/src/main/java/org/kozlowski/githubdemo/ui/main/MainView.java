package org.kozlowski.githubdemo.ui.main;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import org.kozlowski.githubdemo.data.model.RepoData;
import org.kozlowski.githubdemo.ui.base.Presenter;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;

import java.util.List;

/**
 * Created by and on 16.05.17.
 */

public interface MainView extends Presenter.View {
    void initRecyclerView();
    void showRepos(List<RepoItem> repoItems);
    void showUrlInBrowser(String url);
    void changeProgressVisibility(boolean gone);
    void changeRepoListLayoutVisibility(boolean gone);
    void changeTokenLayoutVisibility(boolean gone);
    void initTextChangeListener();
    void removeTextChangeListener();
    void changeButtonConnect(boolean enabled);

}
