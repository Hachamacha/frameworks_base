/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.internal.policy.impl;

<<<<<<< HEAD
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.R;

import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.app.Dialog;
=======
import android.app.Activity;
import android.app.AlertDialog;
>>>>>>> upstream/master
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Vibrator;
=======
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
>>>>>>> upstream/master
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
<<<<<<< HEAD
import android.view.IWindowManager;
=======
>>>>>>> upstream/master
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
<<<<<<< HEAD
import android.view.WindowManagerPolicy.WindowManagerFuncs;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
=======
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.app.ShutdownThread;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.google.android.collect.Lists;
>>>>>>> upstream/master

import com.android.internal.app.ThemeUtils;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.List;
import java.util.UUID;
=======
>>>>>>> upstream/master

/**
 * Needed for takeScreenshot
 */
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;


/**
 * Helper to show the global actions dialog.  Each item is an {@link Action} that
 * may show depending on whether the keyguard is showing, and whether the device
 * is provisioned.
 */
class GlobalActions implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener  {

    private static final String TAG = "GlobalActions";

    private static final boolean SHOW_SILENT_TOGGLE = true;

    private final Context mContext;
<<<<<<< HEAD
    private final WindowManagerFuncs mWindowManagerFuncs;

=======
>>>>>>> upstream/master
    private Context mUiContext;
    private final AudioManager mAudioManager;

    private ArrayList<Action> mItems;
    private AlertDialog mDialog;

<<<<<<< HEAD
    private Action mSilentModeAction;
    private ToggleAction mAirplaneModeOn;
=======
    private SilentModeAction mSilentModeAction;
    private ToggleAction mAirplaneModeOn;
    private ToggleAction mNavBarOn;
>>>>>>> upstream/master

    private MyAdapter mAdapter;

    private boolean mKeyguardShowing = false;
    private boolean mDeviceProvisioned = false;
    private ToggleAction.State mAirplaneState = ToggleAction.State.Off;
<<<<<<< HEAD
    private boolean mIsWaitingForEcmExit = false;
    private boolean mHasTelephony;
    private boolean mHasVibrator;

    private IWindowManager mIWindowManager;
=======
    private ToggleAction.State mNavState = ToggleAction.State.On;
    private boolean mIsWaitingForEcmExit = false;
>>>>>>> upstream/master

    /**
     * @param context everything needs a context :(
     */
<<<<<<< HEAD
    public GlobalActions(Context context, WindowManagerFuncs windowManagerFuncs) {
        mContext = context;
        mWindowManagerFuncs = windowManagerFuncs;
=======
    public GlobalActions(Context context) {
        mContext = context;
>>>>>>> upstream/master
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        // receive broadcasts
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED);
        context.registerReceiver(mBroadcastReceiver, filter);

        ThemeUtils.registerThemeChangeReceiver(context, mThemeChangeReceiver);

        // get notified of phone state changes
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);
<<<<<<< HEAD
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mHasTelephony = cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE);
        mContext.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.AIRPLANE_MODE_ON), true,
                mAirplaneModeObserver);
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mHasVibrator = vibrator != null && vibrator.hasVibrator();
=======
>>>>>>> upstream/master
    }

    /**
     * Show the global actions dialog (creating if necessary)
     * @param keyguardShowing True if keyguard is showing
     */
    public void showDialog(boolean keyguardShowing, boolean isDeviceProvisioned) {
        mKeyguardShowing = keyguardShowing;
        mDeviceProvisioned = isDeviceProvisioned;
<<<<<<< HEAD
        if (mDialog != null) {
            if (mUiContext != null) {
                mUiContext = null;
            }
            mDialog.dismiss();
            mDialog = null;
            // Show delayed, so that the dismiss of the previous dialog completes
            mHandler.sendEmptyMessage(MESSAGE_SHOW);
        } else {
            handleShow();
        }
    }

    private void handleShow() {
        mDialog = createDialog();
=======
        if (mDialog != null && mUiContext == null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (mDialog == null) {
            mDialog = createDialog();
        }
>>>>>>> upstream/master
        prepareDialog();

        mDialog.show();
        mDialog.getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_DISABLE_EXPAND);
    }

    private Context getUiContext() {
        if (mUiContext == null) {
            mUiContext = ThemeUtils.createUiContext(mContext);
        }
        return mUiContext != null ? mUiContext : mContext;
    }

    /**
     * Create the global actions dialog.
     * @return A new dialog.
     */
    private AlertDialog createDialog() {
<<<<<<< HEAD
        // Simple toggle style if there's no vibrator, otherwise use a tri-state
        if (!mHasVibrator) {
            mSilentModeAction = new SilentModeToggleAction();
        } else {
            mSilentModeAction = new SilentModeTriStateAction(mContext, mAudioManager, mHandler);
        }
=======
        mSilentModeAction = new SilentModeAction(mAudioManager, mHandler);

>>>>>>> upstream/master
        mAirplaneModeOn = new ToggleAction(
                R.drawable.ic_lock_airplane_mode,
                R.drawable.ic_lock_airplane_mode_off,
                R.string.global_actions_toggle_airplane_mode,
                R.string.global_actions_airplane_mode_on_status,
                R.string.global_actions_airplane_mode_off_status) {

            void onToggle(boolean on) {
<<<<<<< HEAD
                if (mHasTelephony && Boolean.parseBoolean(
=======
                if (Boolean.parseBoolean(
>>>>>>> upstream/master
                        SystemProperties.get(TelephonyProperties.PROPERTY_INECM_MODE))) {
                    mIsWaitingForEcmExit = true;
                    // Launch ECM exit dialog
                    Intent ecmDialogIntent =
                            new Intent(TelephonyIntents.ACTION_SHOW_NOTICE_ECM_BLOCK_OTHERS, null);
                    ecmDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(ecmDialogIntent);
                } else {
                    changeAirplaneModeSystemSetting(on);
                }
            }

            @Override
            protected void changeStateFromPress(boolean buttonOn) {
<<<<<<< HEAD
                if (!mHasTelephony) return;

=======
>>>>>>> upstream/master
                // In ECM mode airplane state cannot be changed
                if (!(Boolean.parseBoolean(
                        SystemProperties.get(TelephonyProperties.PROPERTY_INECM_MODE)))) {
                    mState = buttonOn ? State.TurningOn : State.TurningOff;
                    mAirplaneState = mState;
                }
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return false;
            }
        };
<<<<<<< HEAD
        onAirplaneModeChanged();
=======

        // create the toggleaction for the soft key show/hide togle
        mNavBarOn = new ToggleAction(
                R.drawable.zzic_lock_navbar_on,
                R.drawable.zzic_lock_navbar_off,
                R.string.zzglobal_actions_navbar_mode,
                R.string.zzglobal_actions_navbar_mode_on,
                R.string.zzglobal_actions_navbar_mode_off) {

            void onToggle(boolean on) {
                changeNavBar(on);
            }

            @Override
            protected void changeStateFromPress(boolean buttonOn) {
                mState = buttonOn ? State.TurningOn : State.TurningOff;
                    mNavState = mState;
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public boolean showBeforeProvisioning() {
                return false;
            }
        };
>>>>>>> upstream/master

        mItems = new ArrayList<Action>();

        // first: power off
        mItems.add(
            new SinglePressAction(
                    com.android.internal.R.drawable.ic_lock_power_off,
                    R.string.global_action_power_off) {

                public void onPress() {
                    // shutdown by making sure radio and power are handled accordingly.
<<<<<<< HEAD
                    mWindowManagerFuncs.shutdown();
=======
                    ShutdownThread.shutdown(getUiContext(), true);
>>>>>>> upstream/master
                }

                public boolean showDuringKeyguard() {
                    return true;
                }

                public boolean showBeforeProvisioning() {
                    return true;
                }
            });

        // next: reboot
        mItems.add(
<<<<<<< HEAD
            new SinglePressAction(R.drawable.ic_lock_reboot, R.string.global_action_reboot) {
                public void onPress() {
                    mWindowManagerFuncs.reboot();
                }

                public boolean onLongPress() {
                    mWindowManagerFuncs.rebootSafeMode();
                    return true;
=======
            new SinglePressAction(com.android.internal.R.drawable.ic_lock_reboot, R.string.global_action_reboot) {
                public void onPress() {
                    ShutdownThread.reboot(getUiContext(), "null", true);
>>>>>>> upstream/master
                }

                public boolean showDuringKeyguard() {
                    return true;
                }

                public boolean showBeforeProvisioning() {
                    return true;
                }
            });

<<<<<<< HEAD
	
        // next: screenshot
        mItems.add(
            new SinglePressAction(R.drawable.ic_lock_screenshot, R.string.global_action_screenshot) {
=======
        // next: screenshot
        mItems.add(
            new SinglePressAction(com.android.internal.R.drawable.ic_lock_screenshot, R.string.global_action_screenshot) {
>>>>>>> upstream/master
                public void onPress() {
                    takeScreenshot();
                }

                public boolean showDuringKeyguard() {
                    return true;
                }

                public boolean showBeforeProvisioning() {
                    return true;
                }
            });

<<<<<<< HEAD
=======
        //Soft key toggles if needed
        boolean mHasSoftKeys = mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_showNavigationBar);
        if (mHasSoftKeys) {
            mItems.add(mNavBarOn);
        }

>>>>>>> upstream/master
        // next: airplane mode
        mItems.add(mAirplaneModeOn);

        // last: silent mode
        if (SHOW_SILENT_TOGGLE) {
            mItems.add(mSilentModeAction);
        }

<<<<<<< HEAD
        List<UserInfo> users = mContext.getPackageManager().getUsers();
        if (users.size() > 1) {
            UserInfo currentUser;
            try {
                currentUser = ActivityManagerNative.getDefault().getCurrentUser();
            } catch (RemoteException re) {
                currentUser = null;
            }
            for (final UserInfo user : users) {
                boolean isCurrentUser = currentUser == null
                        ? user.id == 0 : (currentUser.id == user.id);
                SinglePressAction switchToUser = new SinglePressAction(
                        com.android.internal.R.drawable.ic_menu_cc,
                        (user.name != null ? user.name : "Primary")
                        + (isCurrentUser ? " \u2714" : "")) {
                    public void onPress() {
                        try {
                            ActivityManagerNative.getDefault().switchUser(user.id);
                            getWindowManager().lockNow();
                        } catch (RemoteException re) {
                            Log.e(TAG, "Couldn't switch user " + re);
                        }
                    }

                    public boolean showDuringKeyguard() {
                        return true;
                    }

                    public boolean showBeforeProvisioning() {
                        return false;
                    }
                };
                mItems.add(switchToUser);
            }
        }
=======
>>>>>>> upstream/master

        mAdapter = new MyAdapter();

        final AlertDialog.Builder ab = new AlertDialog.Builder(getUiContext());

        ab.setAdapter(mAdapter, this)
                .setInverseBackgroundForced(true);

        final AlertDialog dialog = ab.create();
        dialog.getListView().setItemsCanFocus(true);
<<<<<<< HEAD
        dialog.getListView().setLongClickable(true);
        dialog.getListView().setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                            long id) {
                        return mAdapter.getItem(position).onLongPress();
                    }
        });
=======
>>>>>>> upstream/master
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);

        dialog.setOnDismissListener(this);

        return dialog;
    }

<<<<<<< HEAD

=======
>>>>>>> upstream/master
    /**
     * functions needed for taking screenhots.  
     * This leverages the built in ICS screenshot functionality 
     */
    final Object mScreenshotLock = new Object();
    ServiceConnection mScreenshotConnection = null;

    final Runnable mScreenshotTimeout = new Runnable() {
        @Override public void run() {
            synchronized (mScreenshotLock) {
                if (mScreenshotConnection != null) {
                    mContext.unbindService(mScreenshotConnection);
                    mScreenshotConnection = null;
                }
            }
        }
    };

    private void takeScreenshot() {
        synchronized (mScreenshotLock) {
            if (mScreenshotConnection != null) {
                return;
            }
            ComponentName cn = new ComponentName("com.android.systemui",
                    "com.android.systemui.screenshot.TakeScreenshotService");
            Intent intent = new Intent();
            intent.setComponent(cn);
            ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    synchronized (mScreenshotLock) {
                        if (mScreenshotConnection != this) {
                            return;
                        }
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, 1);
                        final ServiceConnection myConn = this;
                        Handler h = new Handler(mHandler.getLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                synchronized (mScreenshotLock) {
                                    if (mScreenshotConnection == myConn) {
                                        mContext.unbindService(mScreenshotConnection);
                                        mScreenshotConnection = null;
                                        mHandler.removeCallbacks(mScreenshotTimeout);
                                    }
                                }
                            }
                        };
                        msg.replyTo = new Messenger(h);
                        msg.arg1 = msg.arg2 = 0;

                        /*  remove for the time being
                        if (mStatusBar != null && mStatusBar.isVisibleLw())
                            msg.arg1 = 1;
                        if (mNavigationBar != null && mNavigationBar.isVisibleLw())
                            msg.arg2 = 1;
                         */                        

                        /* wait for the dialog box to close */
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException ie) {
                        }
                        
                        /* take the screenshot */
                        try {
                            messenger.send(msg);
                        } catch (RemoteException e) {
                        }
                    }
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {}
            };
            if (mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE)) {
                mScreenshotConnection = conn;
                mHandler.postDelayed(mScreenshotTimeout, 10000);
            }
        }
    }
    
    private void prepareDialog() {
<<<<<<< HEAD
        refreshSilentMode();
        mAirplaneModeOn.updateState(mAirplaneState);
=======
        final boolean silentModeOn =
                mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL;
        mAirplaneModeOn.updateState(mAirplaneState);

        //need to get the initial navbutton toggle on or off
        final boolean whatToggle = Settings.System.getInt(mContext.getContentResolver(), Settings.System.NAVBAR_TOGGLE_SHOW, 0) == 1;
        mNavState = whatToggle ? ToggleAction.State.On : ToggleAction.State.Off;
        mNavBarOn.updateState(mNavState);

>>>>>>> upstream/master
        mAdapter.notifyDataSetChanged();
        if (mKeyguardShowing) {
            mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        } else {
            mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
        }

        mDialog.setTitle(R.string.global_actions);

        if (SHOW_SILENT_TOGGLE) {
            IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
            mContext.registerReceiver(mRingerModeReceiver, filter);
        }
    }

<<<<<<< HEAD
    private void refreshSilentMode() {
        if (!mHasVibrator) {
            final boolean silentModeOn =
                    mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL;
            ((ToggleAction)mSilentModeAction).updateState(
                    silentModeOn ? ToggleAction.State.On : ToggleAction.State.Off);
        }
    }
=======
>>>>>>> upstream/master

    /** {@inheritDoc} */
    public void onDismiss(DialogInterface dialog) {
        if (SHOW_SILENT_TOGGLE) {
            mContext.unregisterReceiver(mRingerModeReceiver);
        }
    }

    /** {@inheritDoc} */
    public void onClick(DialogInterface dialog, int which) {
<<<<<<< HEAD
        if (!(mAdapter.getItem(which) instanceof SilentModeTriStateAction)) {
=======
        if (!(mAdapter.getItem(which) instanceof SilentModeAction)) {
>>>>>>> upstream/master
            dialog.dismiss();
        }
        mAdapter.getItem(which).onPress();
    }

    /**
     * The adapter used for the list within the global actions dialog, taking
     * into account whether the keyguard is showing via
     * {@link GlobalActions#mKeyguardShowing} and whether the device is provisioned
     * via {@link GlobalActions#mDeviceProvisioned}.
     */
    private class MyAdapter extends BaseAdapter {

        public int getCount() {
            int count = 0;

            for (int i = 0; i < mItems.size(); i++) {
                final Action action = mItems.get(i);

                if (mKeyguardShowing && !action.showDuringKeyguard()) {
                    continue;
                }
                if (!mDeviceProvisioned && !action.showBeforeProvisioning()) {
                    continue;
                }
                count++;
            }
            return count;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position).isEnabled();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public Action getItem(int position) {

            int filteredPos = 0;
            for (int i = 0; i < mItems.size(); i++) {
                final Action action = mItems.get(i);
                if (mKeyguardShowing && !action.showDuringKeyguard()) {
                    continue;
                }
                if (!mDeviceProvisioned && !action.showBeforeProvisioning()) {
                    continue;
                }
                if (filteredPos == position) {
                    return action;
                }
                filteredPos++;
            }

            throw new IllegalArgumentException("position " + position
                    + " out of range of showable actions"
                    + ", filtered count=" + getCount()
                    + ", keyguardshowing=" + mKeyguardShowing
                    + ", provisioned=" + mDeviceProvisioned);
        }


        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Action action = getItem(position);
            final Context context = getUiContext();
            return action.create(context, convertView, parent, LayoutInflater.from(context));
        }
    }

    // note: the scheme below made more sense when we were planning on having
    // 8 different things in the global actions dialog.  seems overkill with
    // only 3 items now, but may as well keep this flexible approach so it will
    // be easy should someone decide at the last minute to include something
    // else, such as 'enable wifi', or 'enable bluetooth'

    /**
     * What each item in the global actions dialog must be able to support.
     */
    private interface Action {
        View create(Context context, View convertView, ViewGroup parent, LayoutInflater inflater);

        void onPress();

<<<<<<< HEAD
        public boolean onLongPress();

=======
>>>>>>> upstream/master
        /**
         * @return whether this action should appear in the dialog when the keygaurd
         *    is showing.
         */
        boolean showDuringKeyguard();

        /**
         * @return whether this action should appear in the dialog before the
         *   device is provisioned.
         */
        boolean showBeforeProvisioning();

        boolean isEnabled();
    }

    /**
     * A single press action maintains no state, just responds to a press
     * and takes an action.
     */
    private static abstract class SinglePressAction implements Action {
        private final int mIconResId;
        private final int mMessageResId;
<<<<<<< HEAD
        private final CharSequence mMessage;
=======
>>>>>>> upstream/master

        protected SinglePressAction(int iconResId, int messageResId) {
            mIconResId = iconResId;
            mMessageResId = messageResId;
<<<<<<< HEAD
            mMessage = null;
        }

        protected SinglePressAction(int iconResId, CharSequence message) {
            mIconResId = iconResId;
            mMessageResId = 0;
            mMessage = message;
        }
=======
        }

>>>>>>> upstream/master
        public boolean isEnabled() {
            return true;
        }

        abstract public void onPress();

<<<<<<< HEAD
        public boolean onLongPress() {
            return false;
        }

=======
>>>>>>> upstream/master
        public View create(
                Context context, View convertView, ViewGroup parent, LayoutInflater inflater) {
            View v = inflater.inflate(R.layout.global_actions_item, parent, false);

            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            TextView messageView = (TextView) v.findViewById(R.id.message);

            v.findViewById(R.id.status).setVisibility(View.GONE);

            icon.setImageDrawable(context.getResources().getDrawable(mIconResId));
<<<<<<< HEAD
            if (mMessage != null) {
                messageView.setText(mMessage);
            } else {
                messageView.setText(mMessageResId);
            }
=======
            messageView.setText(mMessageResId);
>>>>>>> upstream/master

            return v;
        }
    }

    /**
     * A toggle action knows whether it is on or off, and displays an icon
     * and status message accordingly.
     */
    private static abstract class ToggleAction implements Action {

        enum State {
            Off(false),
            TurningOn(true),
            TurningOff(true),
            On(false);

            private final boolean inTransition;

            State(boolean intermediate) {
                inTransition = intermediate;
            }

            public boolean inTransition() {
                return inTransition;
            }
        }

        protected State mState = State.Off;

        // prefs
        protected int mEnabledIconResId;
        protected int mDisabledIconResid;
        protected int mMessageResId;
        protected int mEnabledStatusMessageResId;
        protected int mDisabledStatusMessageResId;

        /**
         * @param enabledIconResId The icon for when this action is on.
         * @param disabledIconResid The icon for when this action is off.
         * @param essage The general information message, e.g 'Silent Mode'
         * @param enabledStatusMessageResId The on status message, e.g 'sound disabled'
         * @param disabledStatusMessageResId The off status message, e.g. 'sound enabled'
         */
        public ToggleAction(int enabledIconResId,
                int disabledIconResid,
<<<<<<< HEAD
                int message,
=======
                int essage,
>>>>>>> upstream/master
                int enabledStatusMessageResId,
                int disabledStatusMessageResId) {
            mEnabledIconResId = enabledIconResId;
            mDisabledIconResid = disabledIconResid;
<<<<<<< HEAD
            mMessageResId = message;
=======
            mMessageResId = essage;
>>>>>>> upstream/master
            mEnabledStatusMessageResId = enabledStatusMessageResId;
            mDisabledStatusMessageResId = disabledStatusMessageResId;
        }

        /**
         * Override to make changes to resource IDs just before creating the
         * View.
         */
        void willCreate() {

        }

        public View create(Context context, View convertView, ViewGroup parent,
                LayoutInflater inflater) {
            willCreate();

            View v = inflater.inflate(R
                            .layout.global_actions_item, parent, false);

            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            TextView messageView = (TextView) v.findViewById(R.id.message);
            TextView statusView = (TextView) v.findViewById(R.id.status);
            final boolean enabled = isEnabled();

            if (messageView != null) {
                messageView.setText(mMessageResId);
                messageView.setEnabled(enabled);
            }

            boolean on = ((mState == State.On) || (mState == State.TurningOn));
            if (icon != null) {
                icon.setImageDrawable(context.getResources().getDrawable(
                        (on ? mEnabledIconResId : mDisabledIconResid)));
                icon.setEnabled(enabled);
            }

            if (statusView != null) {
                statusView.setText(on ? mEnabledStatusMessageResId : mDisabledStatusMessageResId);
                statusView.setVisibility(View.VISIBLE);
                statusView.setEnabled(enabled);
            }
            v.setEnabled(enabled);

            return v;
        }

        public final void onPress() {
            if (mState.inTransition()) {
                Log.w(TAG, "shouldn't be able to toggle when in transition");
                return;
            }

            final boolean nowOn = !(mState == State.On);
            onToggle(nowOn);
            changeStateFromPress(nowOn);
        }

<<<<<<< HEAD
        public boolean onLongPress() {
            return false;
        }

=======
>>>>>>> upstream/master
        public boolean isEnabled() {
            return !mState.inTransition();
        }

        /**
         * Implementations may override this if their state can be in on of the intermediate
         * states until some notification is received (e.g airplane mode is 'turning off' until
         * we know the wireless connections are back online
         * @param buttonOn Whether the button was turned on or off
         */
        protected void changeStateFromPress(boolean buttonOn) {
            mState = buttonOn ? State.On : State.Off;
        }

        abstract void onToggle(boolean on);

        public void updateState(State state) {
            mState = state;
        }
    }

<<<<<<< HEAD
    private class SilentModeToggleAction extends ToggleAction {
        public SilentModeToggleAction() {
            super(R.drawable.ic_audio_vol_mute,
                    R.drawable.ic_audio_vol,
                    R.string.global_action_toggle_silent_mode,
                    R.string.global_action_silent_mode_on_status,
                    R.string.global_action_silent_mode_off_status);
        }

        void onToggle(boolean on) {
            if (on) {
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public boolean showBeforeProvisioning() {
            return false;
        }
    }

    private static class SilentModeTriStateAction implements Action, View.OnClickListener {
=======
    private static class SilentModeAction implements Action, View.OnClickListener {
>>>>>>> upstream/master

        private final int[] ITEM_IDS = { R.id.option1, R.id.option2, R.id.option3 };

        private final AudioManager mAudioManager;
        private final Handler mHandler;
<<<<<<< HEAD
        private final Context mContext;

        SilentModeTriStateAction(Context context, AudioManager audioManager, Handler handler) {
            mAudioManager = audioManager;
            mHandler = handler;
            mContext = context;
=======

        SilentModeAction(AudioManager audioManager, Handler handler) {
            mAudioManager = audioManager;
            mHandler = handler;
>>>>>>> upstream/master
        }

        private int ringerModeToIndex(int ringerMode) {
            // They just happen to coincide
            return ringerMode;
        }

        private int indexToRingerMode(int index) {
            // They just happen to coincide
            return index;
        }

        public View create(Context context, View convertView, ViewGroup parent,
                LayoutInflater inflater) {
            View v = inflater.inflate(R.layout.global_actions_silent_mode, parent, false);

            int selectedIndex = ringerModeToIndex(mAudioManager.getRingerMode());
            for (int i = 0; i < 3; i++) {
                View itemView = v.findViewById(ITEM_IDS[i]);
                itemView.setSelected(selectedIndex == i);
                // Set up click handler
                itemView.setTag(i);
                itemView.setOnClickListener(this);
            }
            return v;
        }

        public void onPress() {
        }

<<<<<<< HEAD
        public boolean onLongPress() {
            return false;
        }

=======
>>>>>>> upstream/master
        public boolean showDuringKeyguard() {
            return true;
        }

        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean isEnabled() {
            return true;
        }

        void willCreate() {
        }

        public void onClick(View v) {
            if (!(v.getTag() instanceof Integer)) return;

            int index = (Integer) v.getTag();
            mAudioManager.setRingerMode(indexToRingerMode(index));
            mHandler.sendEmptyMessageDelayed(MESSAGE_DISMISS, DIALOG_DISMISS_DELAY);
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)
                    || Intent.ACTION_SCREEN_OFF.equals(action)) {
                String reason = intent.getStringExtra(PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY);
                if (!PhoneWindowManager.SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS.equals(reason)) {
                    mHandler.sendEmptyMessage(MESSAGE_DISMISS);
                }
            } else if (TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED.equals(action)) {
                // Airplane mode can be changed after ECM exits if airplane toggle button
                // is pressed during ECM mode
                if (!(intent.getBooleanExtra("PHONE_IN_ECM_STATE", false)) &&
                        mIsWaitingForEcmExit) {
                    mIsWaitingForEcmExit = false;
                    changeAirplaneModeSystemSetting(true);
                }
            }
        }
    };

    private BroadcastReceiver mThemeChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            mUiContext = null;
        }
    };

    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
<<<<<<< HEAD
            if (!mHasTelephony) return;
=======
>>>>>>> upstream/master
            final boolean inAirplaneMode = serviceState.getState() == ServiceState.STATE_POWER_OFF;
            mAirplaneState = inAirplaneMode ? ToggleAction.State.On : ToggleAction.State.Off;
            mAirplaneModeOn.updateState(mAirplaneState);
            mAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver mRingerModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                mHandler.sendEmptyMessage(MESSAGE_REFRESH);
            }
        }
    };

<<<<<<< HEAD
    private ContentObserver mAirplaneModeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onAirplaneModeChanged();
        }
    };

    private static final int MESSAGE_DISMISS = 0;
    private static final int MESSAGE_REFRESH = 1;
    private static final int MESSAGE_SHOW = 2;
=======
    private static final int MESSAGE_DISMISS = 0;
    private static final int MESSAGE_REFRESH = 1;
>>>>>>> upstream/master
    private static final int DIALOG_DISMISS_DELAY = 300; // ms

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
<<<<<<< HEAD
            switch (msg.what) {
            case MESSAGE_DISMISS:
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                break;
            case MESSAGE_REFRESH:
                refreshSilentMode();
                mAdapter.notifyDataSetChanged();
                break;
            case MESSAGE_SHOW:
                handleShow();
                break;
=======
            if (msg.what == MESSAGE_DISMISS) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            } else if (msg.what == MESSAGE_REFRESH) {
                mAdapter.notifyDataSetChanged();
>>>>>>> upstream/master
            }
        }
    };

<<<<<<< HEAD
    private void onAirplaneModeChanged() {
        // Let the service state callbacks handle the state.
        if (mHasTelephony) return;

        boolean airplaneModeOn = Settings.System.getInt(
                mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON,
                0) == 1;
        mAirplaneState = airplaneModeOn ? ToggleAction.State.On : ToggleAction.State.Off;
        mAirplaneModeOn.updateState(mAirplaneState);
    }

=======
>>>>>>> upstream/master
    /**
     * Change the airplane mode system setting
     */
    private void changeAirplaneModeSystemSetting(boolean on) {
        Settings.System.putInt(
                mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON,
                on ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra("state", on);
        mContext.sendBroadcast(intent);
<<<<<<< HEAD
        if (!mHasTelephony) {
            mAirplaneState = on ? ToggleAction.State.On : ToggleAction.State.Off;
        }
    }

    private IWindowManager getWindowManager() {
        if (mIWindowManager == null) {
            IBinder b = ServiceManager.getService(Context.WINDOW_SERVICE);
            mIWindowManager = IWindowManager.Stub.asInterface(b);
        }
        return mIWindowManager;
=======
    }

    // Change the soft key toggles
    private void changeNavBar(boolean on) {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.NAVBAR_TOGGLE_SHOW, on ? 1 : 0);
>>>>>>> upstream/master
    }
}
