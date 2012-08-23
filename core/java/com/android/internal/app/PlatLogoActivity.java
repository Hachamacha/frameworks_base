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

package com.android.internal.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
<<<<<<< HEAD
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
=======
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
>>>>>>> upstream/master
import android.widget.Toast;

public class PlatLogoActivity extends Activity {
    Toast mToast;
    ImageView mContent;
<<<<<<< HEAD
    int mCount;
    final Handler mHandler = new Handler();

    private View makeView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(
                new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ));
        final int p = (int)(8 * metrics.density);
        view.setPadding(p, p, p, p);

        Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);
        Typeface normal = Typeface.create("sans-serif", Typeface.BOLD);

        final float size = 14 * metrics.density;
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = (int) (-4*metrics.density);

        TextView tv = new TextView(this);
        if (light != null) tv.setTypeface(light);
        tv.setTextSize(1.25f*size);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(4*metrics.density, 0, 2*metrics.density, 0x66000000);
        tv.setText("Android " + Build.VERSION.RELEASE);
        view.addView(tv, lp);
   
        tv = new TextView(this);
        if (normal != null) tv.setTypeface(normal);
        tv.setTextSize(size);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(4*metrics.density, 0, 2*metrics.density, 0x66000000);
        tv.setText("JELLY BEAN");
        view.addView(tv, lp);

        return view;
    }
=======
    Vibrator mZzz = new Vibrator();
    int mCount;
    final Handler mHandler = new Handler();

    Runnable mSuperLongPress = new Runnable() {
        public void run() {
            mCount++;
            mZzz.vibrate(50 * mCount);
            final float scale = 1f + 0.25f * mCount * mCount;
            mContent.setScaleX(scale);
            mContent.setScaleY(scale);

            if (mCount <= 3) {
                mHandler.postDelayed(mSuperLongPress, ViewConfiguration.getLongPressTimeout());
            } else {
                try {
                    startActivity(new Intent(Intent.ACTION_MAIN)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("com.android.systemui","com.android.systemui.Nyandroid"));
                } catch (ActivityNotFoundException ex) {
                    android.util.Log.e("PlatLogoActivity", "Couldn't find platlogo screensaver.");
                }
                finish();
            }
        }
    };
>>>>>>> upstream/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD

        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mToast.setView(makeView());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mContent = new ImageView(this);
        mContent.setImageResource(com.android.internal.R.drawable.platlogo_alt);
        mContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        
        final int p = (int)(32 * metrics.density);
        mContent.setPadding(p, p, p, p);

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToast.show();
                mContent.setImageResource(com.android.internal.R.drawable.platlogo);
            }
        });

        mContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_MAIN)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .addCategory("com.android.internal.category.PLATLOGO"));
                        //.setClassName("com.android.systemui","com.android.systemui.BeanBag"));
                } catch (ActivityNotFoundException ex) {
                    android.util.Log.e("PlatLogoActivity", "Couldn't find a bag of beans.");
                }
                finish();
=======
        
        mToast = Toast.makeText(this, "Android 4.0: Ice Cream Sandwich", Toast.LENGTH_SHORT);

        mContent = new ImageView(this);
        mContent.setImageResource(com.android.internal.R.drawable.platlogo);
        mContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    mContent.setPressed(true);
                    mHandler.removeCallbacks(mSuperLongPress);
                    mCount = 0;
                    mHandler.postDelayed(mSuperLongPress, 2*ViewConfiguration.getLongPressTimeout());
                } else if (action == MotionEvent.ACTION_UP) {
                    if (mContent.isPressed()) {
                        mContent.setPressed(false);
                        mHandler.removeCallbacks(mSuperLongPress);
                        mToast.show();
                    }
                }
>>>>>>> upstream/master
                return true;
            }
        });
        
        setContentView(mContent);
    }
}
