package com.bullhead.nafees.android.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.bullhead.nafees.android.util.Prefs;
import com.bullhead.nafees.android.util.Style;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


@SuppressLint("Registered")
@SuppressWarnings("unused")
public abstract class BaseActivity extends DaggerAppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Inject
    protected Style style;
    @Inject
    protected Prefs prefs;

    private UiModeManager uiModeManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        applyModeChange();
    }

    protected final void applyModeChange() {
        uiModeManager.setNightMode(isNightMode() ?
                UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO);
        getWindow().setStatusBarColor(style.getSecondaryColor());
        if (!isNightMode()) {
            applyLightNavigation();
        } else {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }


    public final void applyLightNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().setNavigationBarColor(style.getSecondaryColor());
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public final boolean isNightMode() {
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }


    public final void toggleNightMode() {
        if (isNightMode()) {
            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        } else {
            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        }

    }


    public void startActivityFinishing(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
        finish();
    }

    public void startActivityFinishingAffinity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
        finishAffinity();
    }

    public void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
