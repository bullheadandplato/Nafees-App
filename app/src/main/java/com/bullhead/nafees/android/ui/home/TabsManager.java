package com.bullhead.nafees.android.ui.home;


import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bullhead.nafees.android.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public final class TabsManager {
    private final TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = (tab, position) -> {
        switch (position) {
            case 0:
                tab.setIcon(R.drawable.ic_round_eco_24);
                break;
            case 1:
                tab.setIcon(R.drawable.ic_round_favorite_24);
                break;
            case 2:
                tab.setIcon(R.drawable.ic_round_settings_applications_24);
                break;
        }
    };
    private       TabLayout                                  tabLayout;
    private       ViewPager2                                 viewPager;
    private       TabLayoutMediator                          tabLayoutMediator;
    private       FragmentActivity                           activity;

    TabsManager(TabLayout tabLayout,
                ViewPager2 viewPager,
                FragmentActivity activity) {
        this.tabLayout    = tabLayout;
        this.viewPager    = viewPager;
        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, this.tabConfigurationStrategy);
        this.activity     = activity;
    }

    void setup() {
        if (viewPager == null || activity == null) {
            return;
        }
        viewPager.setAdapter(new HomePagerAdapter(activity));
        tabLayoutMediator.attach();
    }

    void destroy() {
        tabLayout = null;
        viewPager = null;
        activity  = null;
    }
}
