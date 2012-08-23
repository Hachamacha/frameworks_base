package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class AirplaneButton extends PowerButton {

    private static final List<Uri> OBSERVED_URIS = new ArrayList<Uri>();
    static {
        OBSERVED_URIS.add(Settings.System.getUriFor(Settings.System.AIRPLANE_MODE_ON));
    }

    public AirplaneButton() { mType = BUTTON_AIRPLANE; }

    @Override
<<<<<<< HEAD
    protected void updateState(Context context) {
        if (getState(context)) {
=======
    protected void updateState() {
        if (getState(mView.getContext())) {
>>>>>>> upstream/master
            mIcon = R.drawable.stat_airplane_on;
            mState = STATE_ENABLED;
        } else {
            mIcon = R.drawable.stat_airplane_off;
            mState = STATE_DISABLED;
        }
    }

    @Override
<<<<<<< HEAD
    protected void toggleState(Context context) {
=======
    protected void toggleState() {
        Context context = mView.getContext();
>>>>>>> upstream/master
        boolean state = getState(context);
        Settings.System.putInt(context.getContentResolver(),
            Settings.System.AIRPLANE_MODE_ON, state ? 0 : 1);
        // notify change
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        // Reverse state when sending the intent, since we grabbed it before the toggle.
        intent.putExtra("state", !state);
        context.sendBroadcast(intent);
    }

    @Override
<<<<<<< HEAD
    protected boolean handleLongClick(Context context) {
        Intent intent = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
=======
    protected boolean handleLongClick() {
        Intent intent = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mView.getContext().startActivity(intent);
>>>>>>> upstream/master
        return true;
    }

    @Override
    protected List<Uri> getObservedUris() {
        return OBSERVED_URIS;
    }

<<<<<<< HEAD
    private boolean getState(Context context) {
=======
    private static boolean getState(Context context) {
>>>>>>> upstream/master
        return Settings.System.getInt(context.getContentResolver(),
                 Settings.System.AIRPLANE_MODE_ON,0) == 1;
    }
}

