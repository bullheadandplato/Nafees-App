package com.bullhead.nafees.android.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bullhead.nafees.android.ui.preferences.SettingsFragment;
import com.bullhead.nafees.android.ui.video.VideoFragment;

public class HomePagerAdapter extends FragmentStateAdapter {
    public HomePagerAdapter(@NonNull FragmentActivity fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return VideoFragment.newInstance(false);
            case 1:
                return VideoFragment.newInstance(true);
            case 2:
                return SettingsFragment.newInstance();
        }
        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
