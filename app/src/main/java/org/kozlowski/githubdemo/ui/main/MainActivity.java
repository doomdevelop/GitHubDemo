package org.kozlowski.githubdemo.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import net.grandcentrix.thirtyinch.TiActivity;

import org.kozlowski.githubdemo.MyApplication;
import org.kozlowski.githubdemo.R;
import org.kozlowski.githubdemo.data.model.RepoData;
import org.kozlowski.githubdemo.domain.usecase.GetRepositoriesListUseCase;
import org.kozlowski.githubdemo.repository.ApiRepository;
import org.kozlowski.githubdemo.ui.base.BaseActivity;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoAdapter;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoScrollListener;
import org.kozlowski.githubdemo.ui.component.recyclerview.SimpleDividerItemDecoration;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.rv_repositories)
    RecyclerView recyclerView;
    @BindView(R.id.pbProgress)
    ProgressBar progressBar;

    private LinearLayoutManager layoutManager;

    @Inject @Singleton
    protected MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeDagger() {
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.getMainComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RepoAdapter());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        initItemClickObservable();
        initRepoScrollListener();
    }

    private void initRepoScrollListener(){
        Flowable<Integer> paginationObservable = Flowable.<Integer>create(
            new FlowableOnSubscribe<Integer>(){
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Integer> e) throws Exception {
                recyclerView.addOnScrollListener(new RepoScrollListener(e));
            }
        },BackpressureStrategy.BUFFER).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread());
        presenter.onPageObservable(paginationObservable);
    }

    private void initItemClickObservable(){
       Observable<String> clickObservable = Observable.<String>create(new
                                                                        ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
                ((RepoAdapter)recyclerView.getAdapter()).setItemLongClickEmitter(e);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
           .observeOn(AndroidSchedulers.mainThread());
        presenter.setItemLongClick(clickObservable);
    }

    @Override
    public void showRepos(List<RepoItem> repoItems) {
        ((RepoAdapter)recyclerView.getAdapter()).addItems(repoItems);
    }

    @Override
    public void showUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void changeProgressVisibility(boolean disable) {
        progressBar.setVisibility(disable == true ? View.GONE : View.VISIBLE);
    }

    @Override
    public void start() {

    }

    @Override
    public void showError(String errorMessage) {

    }
}
