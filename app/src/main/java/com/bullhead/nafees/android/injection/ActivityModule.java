package com.bullhead.nafees.android.injection;


import com.bullhead.nafees.android.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
}
