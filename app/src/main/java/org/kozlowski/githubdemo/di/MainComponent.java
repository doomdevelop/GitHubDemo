package org.kozlowski.githubdemo.di;

import org.kozlowski.githubdemo.ui.main.MainActivity;
import org.kozlowski.githubdemo.ui.main.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by and on 16.05.17.
 */
@Singleton
@Component(modules={MainModule.class, NetModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
