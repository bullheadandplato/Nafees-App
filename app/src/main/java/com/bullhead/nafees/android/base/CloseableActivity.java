package com.bullhead.nafees.android.base;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.helper.UiUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import static android.content.Context.UI_MODE_SERVICE;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class CloseableActivity extends DialogFragment {
    private static final String TAG = CloseableActivity.class.getSimpleName();

    private Toolbar     toolbar;
    private FrameLayout contentView;
    private ImageView   blurView;
    private ProgressBar progressBar;
    private Options     options;

    private Context context;
    private boolean nightMode;

    @Nullable
    private CloseListener closeListener;

    @Nullable
    public static <A extends CloseableActivity> A show(Class<A> aClass, @NonNull FragmentManager fm) {
        try {
            A activity = aClass.newInstance();
            activity.show(fm, "terms");
            return activity;
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public abstract Options getOptions();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.options = getOptions();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    public void setCloseListener(@Nullable CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (closeListener != null) {
            closeListener.onClose();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width  = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            int color = ContextCompat
                    .getColor(context, options.secondaryColor);
            dialog.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(color));
            if (!nightMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dialog.getWindow().setStatusBarColor(color);
                    dialog.getWindow().setNavigationBarColor(color);
                    dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialog.getWindow().setStatusBarColor(color);
                    dialog.getWindow().getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                dialog.getWindow().setNavigationBarColor(color);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fullscreen, container, false);
        setupViews(view);
        setupToolbar();
        return view;
    }

    private void setupViews(@NonNull View view) {
        toolbar     = view.findViewById(R.id.toolbar);
        contentView = view.findViewById(R.id.baseChildContent);
        progressBar = view.findViewById(R.id.mainLoadingBar);
        blurView    = view.findViewById(R.id.baseBlurView);
        TabLayout    tabLayout    = view.findViewById(R.id.tabLayout);
        AppBarLayout appBarLayout = view.findViewById(R.id.baseAppBar);
        contentView.addView(LayoutInflater.from(context)
                .inflate(options.layout, null));
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        assert toolbar.getNavigationIcon() != null;
        toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, options.primaryColor));
        toolbar.setNavigationOnClickListener(view1 -> dismiss());
        toolbar.setBackgroundColor(ContextCompat.getColor(context, options.secondaryColor));
        toolbar.setTitle(options.title);
        toolbar.setTitleTextColor(ContextCompat.getColor(context, options.primaryColor));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        assert uiModeManager != null;
        nightMode = uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    protected void showProgress() {
        try {
            if (getDialog() != null && getDialog().getWindow() != null && context != null) {
                getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                blurView.setImageBitmap(UiUtils.blur(context, contentView, 20, 1f));
                blurView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "showProgress: failed because " + e.getLocalizedMessage());
        }
    }

    protected void hideProgress() {
        try {
            if (getDialog() != null && getDialog().getWindow() != null && context != null) {
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                blurView.setImageResource(android.R.color.transparent);
                contentView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                blurView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "hideProgress: failed because " + e.getLocalizedMessage());
        }
    }


    public interface CloseListener {
        void onClose();
    }

    /**
     * Options for CloseableActivity
     * Use this class to define colors and title of activity
     */
    public static class Options {
        private final int primaryColor;
        private final int secondaryColor;
        private final int layout;
        private final int title;

        private Options(int primaryColor, int secondaryColor, int layout, int title) {
            this.primaryColor   = primaryColor;
            this.secondaryColor = secondaryColor;
            this.layout         = layout;
            this.title          = title;
        }

        @NonNull
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private int primaryColor   = 0;
            private int secondaryColor = 0;
            private int layout         = 0;

            private int title;

            private Builder() {
            }

            public Builder layout(@LayoutRes int layout) {
                this.layout = layout;
                return this;
            }

            public Builder title(@StringRes int title) {
                this.title = title;
                return this;
            }

            public Builder primaryColor(@ColorRes int color) {
                this.primaryColor = color;
                return this;
            }

            public Builder secondaryColor(@ColorRes int color) {
                this.secondaryColor = color;
                return this;
            }

            public Options build() {
                if (primaryColor == 0 || secondaryColor == 0) {
                    throw new IllegalArgumentException("primary and secondary colors are compulsory");
                } else if (layout == 0) {
                    throw new IllegalArgumentException("Layout res should be provided");
                }
                return new Options(primaryColor, secondaryColor, layout, title);
            }
        }
    }
}

