package com.bullhead.nafees.android.util;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.helper.Helper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public final class DialogUtil {
    private DialogUtil() {

    }

    public static void showOk(@NonNull Activity activity,
                              @Nullable CharSequence title,
                              @Nullable CharSequence message) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void showOk(@NonNull Activity activity,
                              @StringRes int title,
                              @StringRes int message,
                              Object... messageArgs) {
        showOk(activity, Helper.htmlString(title), Helper.htmlString(message, messageArgs));
    }

    public static void showOk(@NonNull Activity activity,
                              @Nullable String message) {
        if (message != null) {
            showOk(activity, null, Helper.toHtml(message));
        }
    }

    public static void showOk(@NonNull Activity activity,
                              @StringRes int message) {
        showOk(activity, null, Helper.htmlString(message));
    }
}
