package com.bullhead.nafees.android.ui.widget.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bullhead.nafees.android.R;


public class ContainedProgressButton extends ProgressButton {
    public ContainedProgressButton(@NonNull Context context) {
        super(context);
    }

    public ContainedProgressButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainedProgressButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.progress_button;
    }
}
