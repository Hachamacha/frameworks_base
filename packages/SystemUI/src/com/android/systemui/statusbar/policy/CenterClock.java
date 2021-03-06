/*
 * Copyright (C) 2006 The Android Open Source Project
<<<<<<< HEAD
=======
 * Patched by Sven Dawitz; Copyright (C) 2011 CyanogenMod Project
>>>>>>> upstream/master
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.policy;

import android.content.ContentResolver;
<<<<<<< HEAD
import android.provider.Settings;
import android.database.ContentObserver;
import android.content.Context;
import android.os.Handler;
=======
import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
>>>>>>> upstream/master
import android.util.AttributeSet;
import android.view.View;

public class CenterClock extends Clock {

<<<<<<< HEAD
    protected class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.STATUS_BAR_CLOCK_STYLE), false, this);
        }
    }

=======
>>>>>>> upstream/master
    public CenterClock(Context context) {
        this(context, null);
    }

    public CenterClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
<<<<<<< HEAD
    }

    public void updateClockVisibility(boolean show) {
        ContentResolver resolver = mContext.getContentResolver();
        mClockStyle = (Settings.System.getInt(resolver,Settings.System.STATUS_BAR_CLOCK_STYLE, 1));
        if (mClockStyle == CLOCK_STYLE_CENTER)
            setVisibility(show ? View.VISIBLE : View.GONE);
=======

        mHandler = new Handler();
        SettingsObserver settingsObserver = new SettingsObserver(mHandler);
        settingsObserver.observe();

        updateSettings();
    }

    @Override
    protected void updateSettings() {
        super.updateSettings();

        ContentResolver resolver = mContext.getContentResolver();

        boolean mShowClock = (Settings.System.getInt(resolver, Settings.System.STATUS_BAR_CLOCK, 1) == 2);
        if (mShowClock)
            setVisibility(View.VISIBLE);
>>>>>>> upstream/master
        else
            setVisibility(View.GONE);

    }
}
