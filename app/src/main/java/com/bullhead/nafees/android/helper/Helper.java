package com.bullhead.nafees.android.helper;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.bullhead.nafees.android.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class Helper {
    private Helper() {
        throw new IllegalArgumentException("this is helper without instance");
    }


    public static boolean isUploaded(@NonNull String url) {
        return url.startsWith("http");
    }

    public static Spanned htmlString(@StringRes int id, Object... args) {
        String html = App.getInstance().getString(id, args);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }
        return Html.fromHtml(html);
    }

    public static Spanned toHtml(@NonNull String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY);
        }
        return Html.fromHtml(message);
    }

    /**
     * Format the given timestamp.<p> If time less than one day than just return
     * hours and minutes 10:20 AM otherwise return with date 02 May - 12:11 PM
     * </p>
     *
     * @param timestamp Time to format
     * @return formatted time
     */
    public static String getFormattedTime(long timestamp) {

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1); // 24  60  60 * 1000;

        long timeDifference = System.currentTimeMillis() - timestamp;

        return timeDifference < oneDayInMillis
                ? DateFormat.format("HH:mm", timestamp).toString()
                : DateFormat.format("dd MMM - HH:mm", timestamp).toString();
    }


    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(in);
             BufferedOutputStream bos = new BufferedOutputStream(out)) {

            byte[] buffer = new byte[1024 * 1024];
            int    read;
            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }

        }
    }

    public static float dpFromPx(final float px) {
        return px / App.getInstance().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * App.getInstance().getResources().getDisplayMetrics().density;
    }

    /**
     * Helper method which should be used with {@link androidx.recyclerview.widget.GridLayoutManager}
     * to set the proper count of item per row
     *
     * @param itemWidthDp Width of each item like 180,100. This width is in DPs.
     * @return number of item that should be displayed in each row.
     */
    public static int getSpanCountByItemWidth(final int itemWidthDp) {
        int width = App.getInstance().getResources().getDisplayMetrics().widthPixels;
        return (int) (width / pxFromDp(itemWidthDp));
    }

    /**
     * Format give milliseconds in duration. THIS METHOD DOES NOT CALCULATE TIME DIFFERENCE
     *
     * @param duration in milliseconds
     * @return 00:11:22 in this format
     */
    public static String formatDuration(long duration) {
        long hours   = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        if (hours < 1) {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }


}
