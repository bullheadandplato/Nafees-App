package com.bullhead.nafees.android.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bullhead.nafees.android.R;
import com.google.android.material.button.MaterialButton;


/**
 * Created by bullhead on 3/15/18.
 * Error View
 */

@SuppressWarnings({"unused"})
public class ErrorView extends LinearLayout {
    private ImageView      icError;
    private TextView       tvError;
    private MaterialButton retryButton;

    public ErrorView(Context context) {
        super(context);
        init(context, null);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.error_view, this, true);
        tvError     = findViewById(R.id.tvNoData);
        icError     = findViewById(R.id.icNoData);
        retryButton = findViewById(R.id.btnRetry);
        if (attrs != null) {
            applyAttrs(attrs);
        }
    }

    private void applyAttrs(@NonNull AttributeSet attrs) {
        TypedArray typedArray      = getContext().obtainStyledAttributes(attrs, R.styleable.ErrorView);
        String     errorText       = typedArray.getString(R.styleable.ErrorView_errorText);
        String     retryButtonText = typedArray.getString(R.styleable.ErrorView_retryButtonText);
        int        iconResource    = typedArray.getResourceId(R.styleable.ErrorView_errorIcon, R.drawable.ic_signal_wifi_off_black_24dp);
        if (errorText != null) {
            tvError.setText(errorText);
        }
        if (retryButtonText != null) {
            retryButton.setText(retryButtonText);
        }
        icError.setImageResource(iconResource);
        typedArray.recycle();
    }

    public void setListener(@Nullable RetryListener retryListener) {
        if (retryListener == null) {
            retryButton.setVisibility(GONE);
            return;
        }
        retryButton.setVisibility(VISIBLE);
        retryButton.setOnClickListener(v -> retryListener.onRetry());
    }

    public MaterialButton getRetryButton() {
        return retryButton;
    }

    public void changeErrorImage(@DrawableRes int image) {
        this.icError.setImageResource(image);
    }

    public void changeErrorImage(Bitmap bitmap) {
        this.icError.setImageBitmap(bitmap);
    }

    public void hideImage() {
        this.icError.setVisibility(GONE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void showImage() {
        this.icError.setVisibility(VISIBLE);
    }

    public void setErrorText(@StringRes int text) {
        this.tvError.setText(text);
    }

    public void setErrorText(String text) {
        this.tvError.setText(text);
    }

    public void hideText() {
        this.tvError.setVisibility(GONE);
    }

    public void showText() {
        this.tvError.setVisibility(VISIBLE);
    }

    public ImageView getIcError() {
        return this.icError;
    }

    public TextView getTvError() {
        return tvError;
    }

    @FunctionalInterface
    public interface RetryListener {
        void onRetry();
    }

}
