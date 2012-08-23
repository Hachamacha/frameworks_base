package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.Context;
import android.view.KeyEvent;

public class MediaNextButton extends MediaKeyEventButton {
    public MediaNextButton() { mType = BUTTON_MEDIA_NEXT; }

    @Override
<<<<<<< HEAD
    protected void updateState(Context context) {
=======
    protected void updateState() {
>>>>>>> upstream/master
        mIcon = R.drawable.stat_media_next;
        mState = STATE_DISABLED;
    }

    @Override
<<<<<<< HEAD
    protected void toggleState(Context context) {
        sendMediaKeyEvent(context, KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    @Override
    protected boolean handleLongClick(Context context) {
=======
    protected void toggleState() {
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    @Override
    protected boolean handleLongClick() {
>>>>>>> upstream/master
        return false;
    }
}
