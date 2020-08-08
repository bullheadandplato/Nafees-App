package com.bullhead.nafees.android.ui.widget.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bullhead.nafees.android.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ExtendedFloatingProgressButton extends FrameLayout {

    private ProgressBar                  progressBar;
    private ExtendedFloatingActionButton button;

    public ExtendedFloatingProgressButton(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ExtendedFloatingProgressButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExtendedFloatingProgressButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    private void init(@NonNull Context context,
                      @Nullable AttributeSet attributeSet) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.progress_extended_floating_button, this, true);
        button      = view.findViewById(R.id.relicButton);
        progressBar = view.findViewById(R.id.relicButtonProgress);
        if (attributeSet != null) {
            @SuppressLint("CustomViewStyleable")
            TypedArray attrs = context
                    .obtainStyledAttributes(attributeSet, R.styleable.ProgressButton);
            button.setText(attrs
                    .getString(R.styleable.ProgressButton_text));
            button.setEnabled(attrs.
                    getBoolean(R.styleable.ProgressButton_enabled, true));
            int icon = attrs.getResourceId(R.styleable.ProgressButton_icon, 0);
            if (icon != 0) {
                button.setIconResource(icon);
            }
            attrs.recycle();
        }
        button.setOnClickListener(view1 -> view.performClick());
    }

    public void showProgress() {
        button.setEnabled(false);
        button.shrink();
        button.setIconTintResource(R.color.primaryColor);
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgress() {
        button.setIconTintResource(android.R.color.white);
        button.setEnabled(true);
        button.extend();
        progressBar.setVisibility(GONE);
    }

    public ExtendedFloatingActionButton getButton() {
        return button;
    }

    public void toggleProgress(boolean show) {
        if (show) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setText(@StringRes int text) {
        button.setText(text);
    }

    public void setText(@NonNull CharSequence text) {
        button.setText(text);
    }
}
