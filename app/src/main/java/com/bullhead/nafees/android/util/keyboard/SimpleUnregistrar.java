package com.bullhead.nafees.android.util.keyboard;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

public class SimpleUnregistrar implements Unregistrar {

    private WeakReference<Activity> mActivityWeakReference;

    private WeakReference<ViewTreeObserver.OnGlobalLayoutListener> mOnGlobalLayoutListenerWeakReference;

    SimpleUnregistrar(Activity activity, ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener) {
        mActivityWeakReference               = new WeakReference<>(activity);
        mOnGlobalLayoutListenerWeakReference = new WeakReference<>(globalLayoutListener);
    }

    @Override
    public void unregister() {
        Activity                                activity             = mActivityWeakReference.get();
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = mOnGlobalLayoutListenerWeakReference.get();

        if (null != activity && null != globalLayoutListener) {
            View activityRoot = KeyboardVisibilityEvent.getActivityRoot(activity);
            activityRoot.getViewTreeObserver()
                    .removeOnGlobalLayoutListener(globalLayoutListener);
        }

        mActivityWeakReference.clear();
        mOnGlobalLayoutListenerWeakReference.clear();
    }

}
