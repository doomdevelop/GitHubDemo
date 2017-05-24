package org.kozlowski.githubdemo.ui.base;

import android.os.Bundle;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by and on 17.05.17.
 */

public abstract class Presenter<T extends Presenter.View> {
    protected T view;

    protected boolean retried = false;

    protected AtomicBoolean isViewAlive = new AtomicBoolean();

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public void initialize(Bundle extras) {
    }

    public void start() {
        isViewAlive.set(true);
    }

    public void finalizeView() {
        isViewAlive.set(false);
    }

    public interface View {
        void start();

        void finish();

        void showError(String errorMessage);
    }
}
