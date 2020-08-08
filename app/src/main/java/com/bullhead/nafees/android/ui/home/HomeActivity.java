package com.bullhead.nafees.android.ui.home;

import android.os.Bundle;

import com.bullhead.nafees.android.base.BaseActivity;
import com.bullhead.nafees.android.databinding.ActivityMainBinding;

public class HomeActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private TabsManager         tabsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tabsManager = new TabsManager(binding.bottomTabLayout, binding.viewPager, this);
        tabsManager.setup();
    }

    @Override
    protected void onDestroy() {
        tabsManager.destroy();
        super.onDestroy();
    }
}
