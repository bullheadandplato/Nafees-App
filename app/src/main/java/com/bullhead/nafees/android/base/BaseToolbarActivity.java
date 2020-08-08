package com.bullhead.nafees.android.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.helper.UiUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;


@SuppressLint("Registered")
@SuppressWarnings("unused")
public abstract class BaseToolbarActivity extends BaseActivity {
    protected Toolbar      toolbar;
    protected AppBarLayout appBarLayout;
    protected TabLayout    tabLayout;
    private   FrameLayout  contentView;
    private   ImageView    blurView;
    private   ProgressBar  progressBar;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_toolbar);
        toolbar      = findViewById(R.id.toolbar);
        contentView  = findViewById(R.id.baseChildContent);
        progressBar  = findViewById(R.id.mainLoadingBar);
        blurView     = findViewById(R.id.baseBlurView);
        tabLayout    = findViewById(R.id.tabLayout);
        appBarLayout = findViewById(R.id.baseAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && getSupportParentActivityIntent() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * <p>
     * When you want to hide action bar use this method instead of
     * directly using <code>getSupportActionBar().hide();</code>
     * </p>
     */
    protected final void hideActionbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            appBarLayout.setLiftOnScroll(true);
        }
    }


    @Override
    @CallSuper
    public void setContentView(@LayoutRes int viewId) {
        contentView.addView(getLayoutInflater().inflate(viewId, null));
    }


    @Override
    public void setContentView(View view) {
        this.contentView.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        this.contentView.addView(view, params);
    }

    public void showProgress() {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            blurView.setImageBitmap(UiUtils.blur(this, contentView, 20, 1f));
            blurView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hideProgress() {
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            blurView.setImageResource(android.R.color.transparent);
            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            blurView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
