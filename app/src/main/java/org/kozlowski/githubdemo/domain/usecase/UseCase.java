package org.kozlowski.githubdemo.domain.usecase;


import org.reactivestreams.Subscription;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by and on 17.05.17.
 */

public abstract class UseCase<T, Params> {

    private Observable subscription;
    private Scheduler newThreadScheduler;
    private Scheduler androidThread;
    private final CompositeDisposable disposables;


    public UseCase() {
        this.newThreadScheduler = Schedulers.newThread();
        this.androidThread = AndroidSchedulers.mainThread();
        this.disposables = new CompositeDisposable();
    }

    /**
     * Builds an {@link io.reactivex.Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable buildUseCaseObservable(Params params);

    /**
     * Executes the current use case.
     *
     * @param observer {@link DisposableObserver} which will be listening to the observable build
     * by {@link #buildUseCaseObservable(Params)} ()} method.
     *
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    public void execute(DisposableObserver<T> observer,@Nullable Params params) {
        Preconditions.checkNotNull(observer);
        final Observable<T> observable = this.buildUseCaseObservable(params)
            .subscribeOn(newThreadScheduler)
            .observeOn(androidThread);
        addDisposable(observable.subscribeWith(observer));
    }
    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        Preconditions.checkNotNull(disposable);
        Preconditions.checkNotNull(disposables);
        disposables.add(disposable);
    }
}

