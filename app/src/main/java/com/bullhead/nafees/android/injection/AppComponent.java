package com.bullhead.nafees.android.injection;

import com.bullhead.nafees.android.App;
import com.bullhead.nafees.api.ApiModule;
import com.bullhead.nafees.api.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component(modules = {AppContextModule.class, AndroidInjectionModule.class,
        ActivityModule.class, FragmentModule.class, RetrofitModule.class,
        ApiModule.class})
@Singleton
public interface AppComponent extends AndroidInjector<App> {
}
