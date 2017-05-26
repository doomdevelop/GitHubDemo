package org.kozlowski.githubdemo.ui.main;

import android.os.Bundle;
import android.util.Log;

import org.kozlowski.githubdemo.data.error.ReadUserDataException;
import org.kozlowski.githubdemo.data.model.User;
import org.kozlowski.githubdemo.domain.usecase.GetRepositoriesListUseCase;
import org.kozlowski.githubdemo.domain.usecase.ReadUserDataUseCase;
import org.kozlowski.githubdemo.domain.usecase.WriteUserDataUseCase;
import org.kozlowski.githubdemo.ui.base.Presenter;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;
import org.kozlowski.githubdemo.util.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

import static org.kozlowski.githubdemo.util.Constants.GENERATE_GITHUB_TOKEN_URL;


/**
 * Created by and on 16.05.17.
 */

public class MainPresenter extends Presenter<MainView> {
    protected GetRepositoriesListUseCase getRepositoriesListUseCase;
    protected ReadUserDataUseCase readUserDataUseCase;
    protected WriteUserDataUseCase writeUserDataUseCase;

    private Observable<String> itemLongClickObservable;
    private State state = State.AUTHORIZED;
    private String user;
    private String token;
    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter(GetRepositoriesListUseCase getRepositoriesListUseCase,
                         ReadUserDataUseCase readUserDataUseCase, WriteUserDataUseCase writeUserDataUseCase) {
        this.getRepositoriesListUseCase = getRepositoriesListUseCase;
        this.readUserDataUseCase = readUserDataUseCase;
        this.writeUserDataUseCase = writeUserDataUseCase;
    }

    @Override
    public void onAttachView() {
        super.onAttachView();
        switch (state){
            case AUTHORIZED:
                view.changeTokenLayoutVisibility(true);
                view.changeRepoListLayoutVisibility(false);
                view.removeTextChangeListener();
                break;
            case UNAUTHORIZED:
                view.changeTokenLayoutVisibility(false);
                view.changeRepoListLayoutVisibility(true);
                view.initTextChangeListener();
                break;
        }
    }

    @Override
    public void onDetachView() {
        super.onDetachView();
        switch (state){
            case AUTHORIZED:
                view.removeTextChangeListener();
                break;
        }
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        view.initRecyclerView();
        readUser();
    }

    public void setItemLongClick(Observable<String> itemLongClick){
        itemLongClick.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.d(TAG,"long click of item : "+s);
                view.showUrlInBrowser(s);
            }
        });
    }

    public void onPageObservable( Flowable<Integer> paginationObservable){
        paginationObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                loadRepositories(new User(MainPresenter.this.user,MainPresenter.this.token),integer);
            }
        });
    }

    public void onPastedToken(String token){
        this.token = token;
        view.changeButtonConnect(canEnableBtnConnect());
    }

    public void onPassedUser(String user){
        this.user = user;
        view.changeButtonConnect(canEnableBtnConnect());
    }

    public void onConnectButtonClick(){
        final User user =new User(this.user,this.token);
        writeUser(user);
        loadRepositories(user,1);
    }

    private boolean canEnableBtnConnect(){
        return user != null && user.length()>0 && token != null && token.length()>0;
    }

    private void readUser(){
        readUserDataUseCase.execute(new ReadUserObserver(),null);
    }

    private void writeUser(User user){
        writeUserDataUseCase.execute(new WriteUserobserver(),WriteUserDataUseCase.Params.create(user));
    }

    private void storeLocalyUserData(User user){
        this.user = user.getUserName();
        this.token = user.getUserToken();
    }

    private void loadRepositories(User user,int page) {
        view.changeProgressVisibility(false);
        getRepositoriesListUseCase.execute(new ListRepositoriesObserver(),
            GetRepositoriesListUseCase.Params.create(user.getUserName(),user.getUserToken(),page));
    }

    private void showLoginView(){
        view.changeProgressVisibility(true);
        view.changeTokenLayoutVisibility(false);
        view.changeRepoListLayoutVisibility(true);
        view.initTextChangeListener();
        view.showUrlInBrowser(GENERATE_GITHUB_TOKEN_URL);
    }
    private class ListRepositoriesObserver extends DisposableObserver<List<RepoItem>> {


        @Override
        public void onNext(@io.reactivex.annotations.NonNull List<RepoItem> repoItems) {
            Log.d(TAG, "get responce data size: " + repoItems.size());
            state = State.AUTHORIZED;
            view.showRepos(repoItems);
            view.changeProgressVisibility(true);
            view.changeTokenLayoutVisibility(true);
            view.changeRepoListLayoutVisibility(false);
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            Log.e(TAG, "error : ", e);
            state = State.UNAUTHORIZED;
            showLoginView();
        }

        @Override
        public void onComplete() {
            view.changeProgressVisibility(true);
        }
    }

    private class WriteUserobserver extends DisposableObserver<Boolean>{

        @Override
        public void onNext(@NonNull Boolean aBoolean) {
            Log.d(TAG,"writen observer:"+aBoolean);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(TAG,"Error on write observer:",e);
        }

        @Override
        public void onComplete() {

        }
    }

    private class ReadUserObserver extends DisposableObserver<User>{

        @Override
        public void onNext(@NonNull User user) {
            Log.d(TAG,"get user: "+user.getUserName());
            state = State.AUTHORIZED;
            storeLocalyUserData(user);
            loadRepositories(user,1);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if(e instanceof ReadUserDataException){
                state = State.UNAUTHORIZED;
                showLoginView();
            }else {
                Log.e(TAG, "Error on read observer:", e);
            }
        }

        @Override
        public void onComplete() {

        }
    }

    private enum State{
        UNAUTHORIZED,AUTHORIZED;
    }
}
