package com.bullhead.nafees.android.ui.preferences;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.CloseableActivity;
import com.bullhead.nafees.android.util.bean.GoPipEvent;
import com.mikepenz.aboutlibraries.LibsBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AboutLibsActivity extends CloseableActivity {
    @NonNull
    @Override
    public Options getOptions() {
        return Options.builder()
                .layout(R.layout.dialog_about_libs)
                .primaryColor(R.color.primaryColor)
                .secondaryColor(R.color.secondaryColor)
                .title(R.string.open_source_title)
                .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment fragment = new LibsBuilder()
                .withFields(R.string.class.getFields()) // in some cases it may be needed to provide the R class, if it can not be automatically resolved
                .supportFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEnterPip(GoPipEvent event) {
        dismiss();
    }

}
