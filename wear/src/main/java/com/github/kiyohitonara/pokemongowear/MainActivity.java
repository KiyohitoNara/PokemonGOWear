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


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, ResultCallback<DataItemBuffer> {
    private GoogleApiClient mGoogleApiClient;

    private NotificationAdapter mNotificationAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
        mGoogleApiClient.connect();

        mNotificationAdapter = new NotificationAdapter(getApplicationContext(), getFragmentManager());

        GridViewPager gridViewPager = (GridViewPager) findViewById(R.id.view_pager);
        gridViewPager.setAdapter(mNotificationAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:/PokemonGOWear")).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onResult(DataItemBuffer dataItemBuffer) {
        for (DataItem dataItem : dataItemBuffer) {
            if (dataItem.getUri().getPath().equals("/PokemonGOWear")) {
                mNotificationAdapter.update(DataMapItem.fromDataItem(dataItem).getDataMap().getStringArrayList("notification"));
                mNotificationAdapter.notifyDataSetChanged();
            }
        }

        dataItemBuffer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
