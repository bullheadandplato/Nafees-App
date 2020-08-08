package com.bullhead.nafees.android.injection;

import com.bullhead.nafees.android.ui.video.VideoFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract VideoFragment videoFragment();
}
