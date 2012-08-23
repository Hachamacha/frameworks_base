package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
<<<<<<< HEAD
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
=======
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
>>>>>>> upstream/master
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScreenTimeoutButton extends PowerButton {

    // timeout values
    private static final int SCREEN_TIMEOUT_MIN    =  15000;
    private static final int SCREEN_TIMEOUT_LOW    =  30000;
    private static final int SCREEN_TIMEOUT_NORMAL =  60000;
    private static final int SCREEN_TIMEOUT_HIGH   = 120000;
    private static final int SCREEN_TIMEOUT_MAX    = 300000;

    // cm modes
    private static final int CM_MODE_15_60_300 = 0;
    private static final int CM_MODE_30_120_300 = 1;

<<<<<<< HEAD
    private Toast mToast = null;
=======
    private static Toast TOAST = null;
>>>>>>> upstream/master

    private static final List<Uri> OBSERVED_URIS = new ArrayList<Uri>();
    static {
        OBSERVED_URIS.add(Settings.System.getUriFor(Settings.System.SCREEN_OFF_TIMEOUT));
    }

    public ScreenTimeoutButton() { mType = BUTTON_SCREENTIMEOUT; }

    @Override
<<<<<<< HEAD
    protected void setupButton(View view) {
        super.setupButton(view);
        if (view == null && mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    @Override
    protected void updateState(Context context) {
        int timeout = getScreenTimeout(context);
=======
    protected void updateState() {
        int timeout=getScreenTtimeout(mView.getContext());
>>>>>>> upstream/master

        if (timeout <= SCREEN_TIMEOUT_LOW) {
            mIcon = R.drawable.stat_screen_timeout_off;
            mState = STATE_DISABLED;
        } else if (timeout <= SCREEN_TIMEOUT_HIGH) {
            mIcon = R.drawable.stat_screen_timeout_off;
            mState = STATE_INTERMEDIATE;
        } else {
            mIcon = R.drawable.stat_screen_timeout_on;
            mState = STATE_ENABLED;
        }
    }

    @Override
<<<<<<< HEAD
    protected void toggleState(Context context) {
        int screenTimeout = getScreenTimeout(context);
        int currentMode = getCurrentCMMode(context);

        if (screenTimeout < SCREEN_TIMEOUT_MIN) {
            if (currentMode == CM_MODE_15_60_300) {
                screenTimeout = SCREEN_TIMEOUT_MIN;
            } else {
                screenTimeout = SCREEN_TIMEOUT_LOW;
            }
        } else if (screenTimeout < SCREEN_TIMEOUT_LOW) {
            if (currentMode == CM_MODE_15_60_300) {
                screenTimeout = SCREEN_TIMEOUT_NORMAL;
            } else {
                screenTimeout = SCREEN_TIMEOUT_LOW;
            }
        } else if (screenTimeout < SCREEN_TIMEOUT_NORMAL) {
            if (currentMode == CM_MODE_15_60_300) {
                screenTimeout = SCREEN_TIMEOUT_NORMAL;
            } else {
                screenTimeout = SCREEN_TIMEOUT_HIGH;
            }
        } else if (screenTimeout < SCREEN_TIMEOUT_HIGH) {
            if (currentMode == CM_MODE_15_60_300) {
                screenTimeout = SCREEN_TIMEOUT_MAX;
            } else {
                screenTimeout = SCREEN_TIMEOUT_HIGH;
            }
        } else if (screenTimeout < SCREEN_TIMEOUT_MAX) {
            screenTimeout = SCREEN_TIMEOUT_MAX;
        } else if (currentMode == CM_MODE_30_120_300) {
            screenTimeout = SCREEN_TIMEOUT_LOW;
        } else {
            screenTimeout = SCREEN_TIMEOUT_MIN;
=======
    protected void toggleState() {
        Context context = mView.getContext();
        int screentimeout = getScreenTtimeout(context);
        int currentMode = getCurrentCMMode(context);

        if (screentimeout < SCREEN_TIMEOUT_MIN) {
            if (currentMode == CM_MODE_15_60_300) {
                screentimeout = SCREEN_TIMEOUT_MIN;
            } else {
                screentimeout = SCREEN_TIMEOUT_LOW;
            }
        } else if (screentimeout < SCREEN_TIMEOUT_LOW) {
            if (currentMode == CM_MODE_15_60_300) {
                screentimeout = SCREEN_TIMEOUT_NORMAL;
            } else {
                screentimeout = SCREEN_TIMEOUT_LOW;
            }
        } else if (screentimeout < SCREEN_TIMEOUT_NORMAL) {
            if (currentMode == CM_MODE_15_60_300) {
                screentimeout = SCREEN_TIMEOUT_NORMAL;
            } else {
                screentimeout = SCREEN_TIMEOUT_HIGH;
            }
        } else if (screentimeout < SCREEN_TIMEOUT_HIGH) {
            if (currentMode == CM_MODE_15_60_300) {
                screentimeout = SCREEN_TIMEOUT_MAX;
            } else {
                screentimeout = SCREEN_TIMEOUT_HIGH;
            }
        } else if (screentimeout < SCREEN_TIMEOUT_MAX) {
            screentimeout = SCREEN_TIMEOUT_MAX;
        } else if (currentMode == CM_MODE_30_120_300) {
            screentimeout = SCREEN_TIMEOUT_LOW;
        } else {
            screentimeout = SCREEN_TIMEOUT_MIN;
>>>>>>> upstream/master
        }

        Settings.System.putInt(
                context.getContentResolver(),
<<<<<<< HEAD
                Settings.System.SCREEN_OFF_TIMEOUT, screenTimeout);

        // cancel any previous toast
        if (mToast != null) {
            mToast.cancel();
        }

        // inform users of how long the timeout is now
        final String toast = makeTimeoutToastString(context, screenTimeout);
        mToast = Toast.makeText(context, toast, Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, mToast.getXOffset() / 2, mToast.getYOffset() / 2);
        mToast.show();
=======
                Settings.System.SCREEN_OFF_TIMEOUT, screentimeout);

        // create our toast
        if(TOAST == null) {
            TOAST = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }

        // cancel any previous toast
        TOAST.cancel();

        // inform users of how long the timeout is now
        TOAST.setText("Screen timeout set to: " + timeoutToString(screentimeout));
        TOAST.setGravity(Gravity.CENTER, TOAST.getXOffset() / 2, TOAST.getYOffset() / 2);
        TOAST.show();
>>>>>>> upstream/master
    }

    @Override
    protected List<Uri> getObservedUris() {
        return OBSERVED_URIS;
    }

    @Override
<<<<<<< HEAD
    protected boolean handleLongClick(Context context) {
        Intent intent = new Intent("android.settings.DISPLAY_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    private String makeTimeoutToastString(Context context, int timeout) {
        Resources res = context.getResources();
        int resId;

        /* ms -> seconds */
        timeout /= 1000;

        if (timeout >= 60 && timeout % 60 == 0) {
            /* seconds -> minutes */
            timeout /= 60;
            if (timeout >= 60 && timeout % 60 == 0) {
                /* minutes -> hours */
                timeout /= 60;
                resId = timeout == 1
                        ? com.android.internal.R.string.hour
                        : com.android.internal.R.string.hours;
            } else {
                resId = timeout == 1
                        ? com.android.internal.R.string.minute
                        : com.android.internal.R.string.minutes;
            }
        } else {
            resId = timeout == 1
                    ? com.android.internal.R.string.second
                    : com.android.internal.R.string.seconds;
        }

        return res.getString(R.string.powerwidget_screen_timeout_toast,
                timeout, res.getString(resId));
    }

    private static int getScreenTimeout(Context context) {
=======
    protected boolean handleLongClick() {
        Intent intent = new Intent("android.settings.DISPLAY_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mView.getContext().startActivity(intent);
        return true;
    }

    private static int getScreenTtimeout(Context context) {
>>>>>>> upstream/master
        return Settings.System.getInt(
                context.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, 0);
    }

<<<<<<< HEAD
=======
    private static String timeoutToString(int timeout) {
        String[] tags = new String[] {
                "second(s)",
                "minute(s)",
                "hour(s)"
            };

        // default to however many seconds we have
        int tmp = (timeout / 1000);
        String sTimeout = tmp + " " + tags[0];

        for(int i = 1; i < tags.length && tmp >= 60; i++) {
            tmp /= (60 * i);
            sTimeout = tmp + " " + tags[i];
        }

        return sTimeout;
    }

>>>>>>> upstream/master
    private static int getCurrentCMMode(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.EXPANDED_SCREENTIMEOUT_MODE,
                CM_MODE_15_60_300);
    }
}


