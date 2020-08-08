package com.bullhead.nafees.android;

import android.app.Application;

import com.bullhead.nafees.android.injection.AppComponent;
import com.bullhead.nafees.android.injection.AppContextModule;
import com.bullhead.nafees.android.injection.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class App extends Application implements HasAndroidInjector {
    private static final String TAG = App.class.getSimpleName();
    private static       App    instance;
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppComponent daggerAppComponent = DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build();
        daggerAppComponent.inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
