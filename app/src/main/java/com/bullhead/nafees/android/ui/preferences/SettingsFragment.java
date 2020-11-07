package com.bullhead.nafees.android.ui.preferences;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bullhead.nafees.android.BuildConfig;
import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.CloseableActivity;
import com.bullhead.nafees.android.notification.NotificationSubscribeManager;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Inject
    NotificationSubscribeManager subscribeManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public static SettingsFragment newInstance() {
        Bundle           args     = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        setupNotificationPref();
        setupCodePrefs();
        setupLicensePref();
        setupSharingListener();
    }

    private void setupLicensePref() {
        Preference preference = findPreference("licences");
        if (preference != null) {
            preference.setOnPreferenceClickListener(p -> {
                if (getContext() != null) {
                    CloseableActivity.show(AboutLibsActivity.class, getChildFragmentManager());
                }
                return true;
            });
        }
    }

    private void setupNotificationPref() {
        Preference notification = findPreference("notification");
        if (notification != null) {
            notification.setOnPreferenceChangeListener((preference1, newValue) -> {
                boolean value = (boolean) newValue;
                if (value) {
                    subscribeManager.subscribe();
                } else {
                    subscribeManager.unsubscribe();
                }
                return true;
            });
        }
    }

    private void setupCodePrefs() {
        Preference code = findPreference("code");
        if (code != null) {
            code.setOnPreferenceClickListener(p -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("https://github.com/bullheadandplato/Nafees-App"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //ignore
                }
                return true;
            });
        }
    }

    private void setupSharingListener() {
        Preference sharing = findPreference("share");
        if (sharing != null) {
            sharing.setOnPreferenceClickListener(v -> {
                if (getActivity() != null) {
                    openShare(getActivity());
                }
                return true;
            });
        }
    }

    private void openShare(@NonNull Activity activity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_text,
                        getString(R.string.app_name), BuildConfig.APPLICATION_ID));
        sendIntent.setType("text/plain");
        try {
            activity.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            //ignore
        }
    }
}