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
import android.app.NotificationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

public class ListenerService extends WearableListenerService {
    private GoogleApiClient mGoogleApiClient;

    private static final int NOTIFICATION_ID = 1;

    private static final String TAG = ListenerService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent dataEvent : dataEventBuffer) {
            DataItem dataItem = dataEvent.getDataItem();

            if (dataItem.getUri().getPath().equals("/PokemonGOWear")) {
                ArrayList<String> arrayList = DataMapItem.fromDataItem(dataItem).getDataMap().getStringArrayList("notification");

                if (arrayList.isEmpty()) {
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
                } else {
                    Notification notification = new Notification.Builder(this)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(arrayList.get(0))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setColor(getColor(R.color.indigo700))
                            .setLocalOnly(true)
                            .build();

                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
