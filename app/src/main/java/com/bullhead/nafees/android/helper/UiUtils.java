package com.bullhead.nafees.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

public final class UiUtils {
    private UiUtils() {
        throw new IllegalStateException("Nah! 0xcafebabe");
    }

    /**
     * Blur given view. It creates bitmap of view
     * that should be set explicitly to {@link android.widget.ImageView}
     * If view width or height is 0 than it will throw {@link IllegalArgumentException}
     *
     * @param context Context of the view
     * @param view    View to blur
     * @param radius  blur radius
     * @param scale   view scaling while bullring
     * @return Blur bitmap
     */
    public static Bitmap blur(@NonNull Context context,
                              @NonNull View view,
                              @FloatRange(from = 0.1) float radius,
                              @FloatRange(from = 0.1) float scale) {
        Bitmap image = loadBitmapFromView(view);
        return blur(context, image, radius, scale);

    }

    /**
     * Blur given view. It creates bitmap of view
     * that should be set explicitly to {@link android.widget.ImageView}
     * If view width or height is 0 than it will throw {@link IllegalArgumentException}
     *
     * @param context Context of the view
     * @param image   Bitmap to blur
     * @param radius  blur radius
     * @param scale   view scaling while bullring
     * @return Blur bitmap
     */
    public static Bitmap blur(@NonNull Context context,
                              @NonNull Bitmap image,
                              @FloatRange(from = 0.1) float radius,
                              @FloatRange(from = 0.1) float scale) {
        int width  = Math.round(image.getWidth() * scale);
        int height = Math.round(image.getHeight() * scale);

        Bitmap inputBitmap  = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript        rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic;
        theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn  = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);


        return outputBitmap;
    }

    /**
     * Create bitmap of a view
     *
     * @param view view to create bitmap of. Width and height must of > 0
     * @return View's bitmap
     */
    public static Bitmap loadBitmapFromView(@NonNull View view) {
        int width  = view.getWidth();
        int height = view.getHeight();

        int measuredWidth  = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        view.draw(c);

        return b;
    }
}