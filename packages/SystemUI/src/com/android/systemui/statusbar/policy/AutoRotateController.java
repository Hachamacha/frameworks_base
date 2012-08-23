/*
 * Copyright (C) 2010 The Android Open Source Project
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

<<<<<<< HEAD
import com.android.internal.view.RotationPolicy;

import android.content.Context;
import android.widget.CompoundButton;

public final class AutoRotateController implements CompoundButton.OnCheckedChangeListener {
    private final Context mContext;
    private final CompoundButton mCheckbox;
    private final RotationLockCallbacks mCallbacks;

    private boolean mAutoRotation;

    private final RotationPolicy.RotationPolicyListener mRotationPolicyListener =
            new RotationPolicy.RotationPolicyListener() {
        @Override
        public void onChange() {
            updateState();
        }
    };

    public AutoRotateController(Context context, CompoundButton checkbox,
            RotationLockCallbacks callbacks) {
        mContext = context;
        mCheckbox = checkbox;
        mCallbacks = callbacks;

        mCheckbox.setOnCheckedChangeListener(this);

        RotationPolicy.registerRotationPolicyListener(context, mRotationPolicyListener);
        updateState();
=======
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import android.util.Slog;
import android.view.IWindowManager;
import android.widget.CompoundButton;

/**
 * TODO: Listen for changes to the setting.
 */
public class AutoRotateController implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "StatusBar.AutoRotateController";

    private Context mContext;
    private CompoundButton mCheckBox;

    private boolean mAutoRotation;

    public AutoRotateController(Context context, CompoundButton checkbox) {
        mContext = context;
        mAutoRotation = getAutoRotation();
        mCheckBox = checkbox;
        checkbox.setChecked(mAutoRotation);
        checkbox.setOnCheckedChangeListener(this);
>>>>>>> upstream/master
    }

    public void onCheckedChanged(CompoundButton view, boolean checked) {
        if (checked != mAutoRotation) {
<<<<<<< HEAD
            mAutoRotation = checked;
            RotationPolicy.setRotationLock(mContext, !checked);
        }
    }

    public void release() {
        RotationPolicy.unregisterRotationPolicyListener(mContext,
                mRotationPolicyListener);
    }

    private void updateState() {
        mAutoRotation = !RotationPolicy.isRotationLocked(mContext);
        mCheckbox.setChecked(mAutoRotation);

        boolean visible = RotationPolicy.isRotationLockToggleVisible(mContext);
        mCallbacks.setRotationLockControlVisibility(visible);
        mCheckbox.setEnabled(visible);
    }

    public interface RotationLockCallbacks {
        void setRotationLockControlVisibility(boolean show);
=======
            setAutoRotation(checked);
        }
    }

    private boolean getAutoRotation() {
        ContentResolver cr = mContext.getContentResolver();
        return 0 != Settings.System.getInt(cr, Settings.System.ACCELEROMETER_ROTATION, 0);
    }

    private void setAutoRotation(final boolean autorotate) {
        mAutoRotation = autorotate;
        AsyncTask.execute(new Runnable() {
                public void run() {
                    try {
                        IWindowManager wm = IWindowManager.Stub.asInterface(
                                ServiceManager.getService(Context.WINDOW_SERVICE));
                        if (autorotate) {
                            wm.thawRotation();
                        } else {
                            wm.freezeRotation(-1);
                        }
                    } catch (RemoteException exc) {
                        Log.w(TAG, "Unable to save auto-rotate setting");
                    }
                }
            });
>>>>>>> upstream/master
    }
}
