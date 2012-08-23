package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class GPSButton extends PowerButton {

    private static final List<Uri> OBSERVED_URIS = new ArrayList<Uri>();
    static {
        OBSERVED_URIS.add(Settings.Secure.getUriFor(Settings.Secure.LOCATION_PROVIDERS_ALLOWED));
    }

    public GPSButton() { mType = BUTTON_GPS; }

    @Override
<<<<<<< HEAD
    protected void updateState(Context context) {
        if (getGpsState(context)) {
=======
    protected void updateState() {
        if(getGpsState(mView.getContext())) {
>>>>>>> upstream/master
            mIcon = R.drawable.stat_gps_on;
            mState = STATE_ENABLED;
        } else {
            mIcon = R.drawable.stat_gps_off;
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
        ContentResolver resolver = context.getContentResolver();
        boolean enabled = getGpsState(context);
        Settings.Secure.setLocationProviderEnabled(resolver,
                LocationManager.GPS_PROVIDER, !enabled);
    }

    @Override
<<<<<<< HEAD
    protected boolean handleLongClick(Context context) {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
=======
    protected boolean handleLongClick() {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
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
    private boolean getGpsState(Context context) {
=======
    private static boolean getGpsState(Context context) {
>>>>>>> upstream/master
        ContentResolver resolver = context.getContentResolver();
        return Settings.Secure.isLocationProviderEnabled(resolver,
                LocationManager.GPS_PROVIDER);
    }
}
