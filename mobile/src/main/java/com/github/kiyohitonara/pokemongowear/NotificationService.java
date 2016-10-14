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


import android.app.Notification;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

public class NotificationService extends NotificationListenerService {
    private GoogleApiClient mGoogleApiClient;

    private ArrayList<String> mArrayList = new ArrayList<>();

    private static final String TAG = NotificationService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getPackageName().equals("com.nianticlabs.pokemongo")) {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.key_go_plus), false)) {
                CharSequence notificationText = statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);

                if (!TextUtils.isEmpty(notificationText)) {
                    mArrayList.add(0, notificationText.toString());

                    putNotification();
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getPackageName().equals("com.nianticlabs.pokemongo")) {
            if (!mArrayList.isEmpty()) {
                mArrayList.clear();

                putNotification();
            }
        }
    }

    private void putNotification() {
        if (mGoogleApiClient.isConnected()) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/PokemonGOWear");
            putDataMapRequest.getDataMap().putStringArrayList("notification", mArrayList);

            Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
