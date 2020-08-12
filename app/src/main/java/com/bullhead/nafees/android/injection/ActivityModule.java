package com.bullhead.nafees.android.injection;


import com.bullhead.nafees.android.ui.home.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract HomeActivity mainActivity();

}
