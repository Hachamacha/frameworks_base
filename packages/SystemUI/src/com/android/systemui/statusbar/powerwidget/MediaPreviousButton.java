package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.Context;
import android.view.KeyEvent;

public class MediaPreviousButton extends MediaKeyEventButton {
    public MediaPreviousButton() { mType = BUTTON_MEDIA_PREVIOUS; }

    @Override
<<<<<<< HEAD
    protected void updateState(Context context) {
=======
    protected void updateState() {
>>>>>>> upstream/master
        mIcon = R.drawable.stat_media_previous;
        mState = STATE_DISABLED;
    }

    @Override
<<<<<<< HEAD
    protected void toggleState(Context context) {
        sendMediaKeyEvent(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    @Override
    protected boolean handleLongClick(Context context) {
=======
    protected void toggleState() {
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    @Override
    protected boolean handleLongClick() {
>>>>>>> upstream/master
        return false;
    }
}
