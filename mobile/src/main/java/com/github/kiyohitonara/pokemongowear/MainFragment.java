/*
 * Copyright 2016 Kiyohito Nara
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.kiyohitonara.pokemongowear;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;


public class MainFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private SwitchPreference mGoPlusSwitchPreference;

    public static final String TAG = MainFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle bundle, String string) {
        addPreferencesFromResource(R.xml.preferences);

        mGoPlusSwitchPreference = (SwitchPreference) getPreferenceManager().findPreference(getString(R.string.key_go_plus));
        mGoPlusSwitchPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object object) {
        if (Boolean.parseBoolean(object.toString()) && !isEnabledReadNotification()) {
            mGoPlusSwitchPreference.setChecked(false);

            ((MainActivity) getActivity()).startNotificationListenerSetting();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(getString(R.string.key_go_plus), false) && !isEnabledReadNotification()) {
            mGoPlusSwitchPreference.setChecked(false);
        }
    }

    private boolean isEnabledReadNotification() {
        String listeners = Settings.Secure.getString(getActivity().getContentResolver(), "enabled_notification_listeners");

        if (!TextUtils.isEmpty(listeners)) {
            for (String listener : listeners.split(":")) {
                if (listener.startsWith(getActivity().getPackageName())) {
                    return true;
                }
            }
        }

        return false;
    }
}