package org.kozlowski.githubdemo.ui.main;

import android.os.Bundle;
import android.util.Log;

import org.kozlowski.githubdemo.data.model.RepoData;
import org.kozlowski.githubdemo.domain.usecase.GetRepositoriesListUseCase;
import org.kozlowski.githubdemo.ui.base.Presenter;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by and on 16.05.17.
 */

public class MainPresenter extends Presenter<MainView> {
    protected GetRepositoriesListUseCase getRepositoriesListUseCase;
    private Disposable apiCallDisposable;
    private Disposable nextPageDisposable;
    private Disposable itemLongClickDisposable;
    private Observable<String> itemLongClickObservable;

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter(GetRepositoriesListUseCase getRepositoriesListUseCase) {
        this.getRepositoriesListUseCase = getRepositoriesListUseCase;
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        view.initRecyclerView();
        loadRepositories(1);
    }

    public void setItemLongClick(Observable<String> itemLongClick){
        itemLongClickDisposable = itemLongClick.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.d(TAG,"long click of item : "+s);
                view.showUrlInBrowser(s);
            }
        });
    }

    public void onPageObservable( Flowable<Integer> paginationObservable){
        nextPageDisposable = paginationObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                loadRepositories(integer);
            }
        });
    }

    private void loadRepositories(int page) {
        view.changeProgressVisibility(false);
        getRepositoriesListUseCase.setPage(page);
        getRepositoriesListUseCase.execute(new ListRepositoriesObserver());
    }

    private class ListRepositoriesObserver implements Observer<List<RepoItem>> {

        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            apiCallDisposable = d;
        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull List<RepoItem> repoItems) {
            Log.d(TAG, "get responce data size: " + repoItems.size());
            view.showRepos(repoItems);
            view.changeProgressVisibility(true);
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            Log.e(TAG, "error : ", e);
            view.changeProgressVisibility(true);
        }

        @Override
        public void onComplete() {
            view.changeProgressVisibility(true);
        }
    }
}
