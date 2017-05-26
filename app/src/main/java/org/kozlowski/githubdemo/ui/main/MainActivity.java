package org.kozlowski.githubdemo.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.kozlowski.githubdemo.MyApplication;
import org.kozlowski.githubdemo.R;
import org.kozlowski.githubdemo.ui.base.BaseActivity;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoAdapter;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoScrollListener;
import org.kozlowski.githubdemo.ui.component.recyclerview.SimpleDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.rv_repositories)
    RecyclerView recyclerView;
    @BindView(R.id.pb_progress)
    ProgressBar progressBar;
    @BindView(R.id.fl_listLayout)
    FrameLayout repoListLayout;
    @BindView(R.id.rl_tokenAuthLaoyut)
    RelativeLayout tokenLayout;
    @BindView(R.id.et_token)
    EditText tokenEditText;
    @BindView(R.id.et_userName)
    EditText githubUserEditText;
    @BindView(R.id.btn_connect)
    Button connectButton;

    private LinearLayoutManager layoutManager;

    @Inject
    @Singleton
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

    @OnClick(R.id.btn_connect)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                presenter.onConnectButtonClick();
                break;
        }
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

    private void initRepoScrollListener() {
        Flowable<Integer> paginationObservable = Flowable.<Integer>create(
            new FlowableOnSubscribe<Integer>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Integer> e) throws Exception {
                    recyclerView.addOnScrollListener(new RepoScrollListener(e));
                }
            }, BackpressureStrategy.BUFFER).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread());
        presenter.onPageObservable(paginationObservable);
    }

    private void initItemClickObservable() {
        Observable<String> clickObservable =
            Observable.<String>create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
                    ((RepoAdapter) recyclerView.getAdapter()).setItemLongClickEmitter(e);
                }
            }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
        presenter.setItemLongClick(clickObservable);
    }

    @Override
    public void showRepos(List<RepoItem> repoItems) {
        ((RepoAdapter) recyclerView.getAdapter()).addItems(repoItems);
    }

    @Override
    public void showUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void changeProgressVisibility(boolean gone) {
        progressBar.setVisibility(gone == true ? View.GONE : View.VISIBLE);
    }

    @Override
    public void changeRepoListLayoutVisibility(boolean gone) {
        repoListLayout.setVisibility(gone == true ? View.GONE : View.VISIBLE);
    }

    @Override
    public void changeTokenLayoutVisibility(boolean gone) {
        tokenLayout.setVisibility(gone == true ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initTextChangeListener() {
        tokenEditText.addTextChangedListener(textTokenWatcher);
        githubUserEditText.addTextChangedListener(textUserWatcher);

    }

    @Override
    public void removeTextChangeListener() {
        tokenEditText.removeTextChangedListener(textTokenWatcher);
        githubUserEditText.removeTextChangedListener(textUserWatcher);
    }

    @Override
    public void changeButtonConnect(boolean enabled) {
        connectButton.setEnabled(enabled);
    }

    private final TextWatcher textTokenWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                presenter.onPastedToken(editable.toString());
            }
        }
    };


    private final TextWatcher textUserWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                presenter.onPassedUser(editable.toString());
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onAttachView();

    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onDetachView();
    }

    @Override
    public void showError(String errorMessage) {

    }
}
