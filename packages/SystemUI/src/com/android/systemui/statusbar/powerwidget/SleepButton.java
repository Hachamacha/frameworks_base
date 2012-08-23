package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.os.PowerManager;
import android.os.SystemClock;

public class SleepButton extends PowerButton {
    public SleepButton() { mType = BUTTON_SLEEP; }

    @Override
<<<<<<< HEAD
    protected void updateState(Context context) {
=======
    protected void updateState() {
>>>>>>> upstream/master
        mIcon = R.drawable.stat_sleep;
        mState = STATE_DISABLED;
    }

    @Override
<<<<<<< HEAD
    protected void toggleState(Context context) {
        PowerManager pm = (PowerManager)
                context.getSystemService(Context.POWER_SERVICE);
=======
    protected void toggleState() {
        PowerManager pm = (PowerManager)mView.getContext()
                .getSystemService(Context.POWER_SERVICE);
>>>>>>> upstream/master
        pm.goToSleep(SystemClock.uptimeMillis() + 1);
    }

    @Override
<<<<<<< HEAD
    protected boolean handleLongClick(Context context) {
        Intent intent = new Intent("android.settings.DISPLAY_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
=======
    protected boolean handleLongClick() {
        Intent intent = new Intent("android.settings.DISPLAY_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mView.getContext().startActivity(intent);
>>>>>>> upstream/master
        return true;
    }
}
