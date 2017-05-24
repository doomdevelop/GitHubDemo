package org.kozlowski.githubdemo.domain.usecase;


import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by and on 17.05.17.
 */

public abstract class UseCase {

    private Observable subscription;
    private int id;
    private Scheduler newThreadScheduler;
    private Scheduler androidThread;

    public int getId() {
        return id;
    }

    public UseCase() {
        this.id = (int) System.currentTimeMillis();
        this.newThreadScheduler = Schedulers.newThread();
        this.androidThread = AndroidSchedulers.mainThread();
    }

    public UseCase(Scheduler newThreadScheduler, Scheduler androidThread) {
        this.id = (int) System.currentTimeMillis();
        this.newThreadScheduler = newThreadScheduler;
        this.androidThread = androidThread;
    }

    /**
     * Builds an {@link io.reactivex.Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable buildUseCaseObservable();

    /**
     * Executes the current use case.
     *
     * @param useCaseobserver The guy who will be listen to the observable build with {@link #buildUseCaseObservable()}.
     */
    @SuppressWarnings("unchecked")
    public void execute(Observer useCaseobserver) {
        this.buildUseCaseObservable().subscribeOn(newThreadScheduler).observeOn(androidThread)
            .subscribe(useCaseobserver);
    }
}

