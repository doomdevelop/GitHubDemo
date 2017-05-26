package org.kozlowski.githubdemo.di;

import com.securepreferences.SecurePreferences;

import org.kozlowski.githubdemo.MyApplication;
import org.kozlowski.githubdemo.repository.LocalRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by and on 16.05.17.
 */
@Module
public class MainModule {
    private MyApplication mApplication;


    @Provides
    @Singleton
    public LocalRepository provideLocalRepository(SecurePreferences securePreferences) {
        return new LocalRepository(securePreferences);
    }

    public MainModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication providesApplication() {
        return mApplication;
    }
}
