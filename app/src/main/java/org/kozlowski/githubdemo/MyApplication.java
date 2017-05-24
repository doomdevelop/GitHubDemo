package org.kozlowski.githubdemo;

import android.app.Application;

import org.kozlowski.githubdemo.di.DaggerMainComponent;
import org.kozlowski.githubdemo.di.MainComponent;
import org.kozlowski.githubdemo.di.MainModule;
import org.kozlowski.githubdemo.di.NetModule;

/**
 * Created by and on 16.05.17.
 */

public class MyApplication extends Application {
    private MainComponent mainComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        // list of modules that are part of this component need to be created here too
        //The deprecation is intended to notify you of unused methods and modules and can be ignore.
        //http://stackoverflow.com/questions/36521302/dagger-2-2-component-builder-module-method-deprecated/
        mainComponent  = DaggerMainComponent.builder().mainModule(new MainModule(this))
            .netModule(new NetModule(getString(R.string.github_url)))
            .build();
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
