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


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

public class NotificationAdapter extends FragmentGridPagerAdapter {
    private Context mContext;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private static final String TAG = NotificationAdapter.class.getSimpleName();

    public NotificationAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);

        mContext = context;
    }

    @Override
    public Fragment getFragment(int row, int column) {
        return mFragments.isEmpty() ? CardFragment.create(mContext.getString(R.string.app_name), mContext.getString(R.string.empty_notification)) : mFragments.get(row);
    }

    @Override
    public int getRowCount() {
        return mFragments.isEmpty() ? 1 : mFragments.size();
    }

    @Override
    public int getColumnCount(int row) {
        return 1;
    }

    public void update(ArrayList<String> notifications) {
        mFragments.clear();

        for (String notification : notifications) {
            mFragments.add(CardFragment.create(mContext.getString(R.string.app_name), notification));
        }
    }
}
