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

package com.android.systemui.statusbar.policy;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
<<<<<<< HEAD
=======
import android.content.res.Resources;
>>>>>>> upstream/master
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
<<<<<<< HEAD
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.input.InputManager;
=======
import android.graphics.RectF;
>>>>>>> upstream/master
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.ServiceManager;
import android.provider.Settings;
<<<<<<< HEAD
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.HapticFeedbackConstants;
=======
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.HapticFeedbackConstants;
import android.view.IWindowManager;
>>>>>>> upstream/master
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

<<<<<<< HEAD
import com.android.systemui.R;

public class KeyButtonView extends ImageView {
    private static final String TAG = "StatusBar.KeyButtonView";

    final float GLOW_MAX_SCALE_FACTOR = 1.8f;
    float BUTTON_QUIESCENT_ALPHA = 1f;

    long mDownTime;
    int mCode;
    int mTouchSlop;
    private int mNavigationBarTint;
    Drawable mGlowBG;
    int mGlowBGColor = Integer.MIN_VALUE;
    int mGlowWidth, mGlowHeight;
    float mGlowAlpha = 0f, mGlowScale = 1f, mDrawingAlpha = 1f;
    boolean mSupportsLongpress = true;
    protected boolean mHandlingLongpress = false;
    RectF mRect = new RectF(0f,0f,0f,0f);
    AnimatorSet mPressedAnim;

    int durationSpeedOn = 500;
    int durationSpeedOff = 50;

    Runnable mCheckLongPress = new Runnable() {
        public void run() {
        	if (isPressed()) {
                setHandlingLongpress(true);                
                if (!performLongClick() && (mCode != 0)) {
                    // we tried to do custom long click and failed - let's
                    // do long click on the primary 'key'
                    sendEvent(KeyEvent.ACTION_DOWN, KeyEvent.FLAG_LONG_PRESS);
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
=======
import com.android.internal.util.ArrayUtils;
import com.android.systemui.R;
import com.android.systemui.statusbar.phone.NavbarEditor;
import com.android.systemui.statusbar.phone.NavbarEditor.ButtonInfo;
import com.android.systemui.statusbar.phone.NavigationBarView;

public class KeyButtonView extends ImageView {
    private static final String TAG = "StatusBar.KeyButtonView";
    private int mSoftKeyColor;

    final float GLOW_MAX_SCALE_FACTOR = 1.8f;
    final float BUTTON_QUIESCENT_ALPHA = 0.6f;

    IWindowManager mWindowManager;
    long mDownTime;
    int mCode;
    int mTouchSlop;
    Drawable mGlowBG;
    float mGlowAlpha = 0f, mGlowScale = 1f, mDrawingAlpha = 1f;
    boolean mSupportsLongpress = true;
    RectF mRect = new RectF(0f,0f,0f,0f);

    Runnable mCheckLongPress = new Runnable() {
        public void run() {
            if (isPressed()) {
                // Slog.d("KeyButtonView", "longpressed: " + this);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, KeyEvent.FLAG_LONG_PRESS);
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
                } else {
                    // Just an old-fashioned ImageView
                    performLongClick();
>>>>>>> upstream/master
                }
            }
        }
    };

    public KeyButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyButtonView,
                defStyle, 0);

        mCode = a.getInteger(R.styleable.KeyButtonView_keyCode, 0);
        
        mSupportsLongpress = a.getBoolean(R.styleable.KeyButtonView_keyRepeat, true);

        mGlowBG = a.getDrawable(R.styleable.KeyButtonView_glowBackground);
        if (mGlowBG != null) {
<<<<<<< HEAD
            setDrawingAlpha(BUTTON_QUIESCENT_ALPHA);
            mGlowWidth = mGlowBG.getIntrinsicWidth();
            mGlowHeight = mGlowBG.getIntrinsicHeight();
        }
     
        a.recycle();

        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        SettingsObserver settingsObserver = new SettingsObserver(new Handler());
        settingsObserver.observe();
    }
    
    public void setSupportsLongPress(boolean supports) {
        mSupportsLongpress = supports;
    }

    public void setHandlingLongpress(boolean handling) {
        mHandlingLongpress = handling;
    }

    public void setCode(int code) {
    	mCode = code;
    }

    public int getCode() {
            return mCode;
    }
    
    public void setGlowBackground(int id) {
        mGlowBG = getResources().getDrawable(id);
        if (mGlowBG != null) {
            setDrawingAlpha(BUTTON_QUIESCENT_ALPHA);
            mGlowWidth = mGlowBG.getIntrinsicWidth();
            mGlowHeight = mGlowBG.getIntrinsicHeight();
            mDrawingAlpha = BUTTON_QUIESCENT_ALPHA;
        }
       /* if (mGlowBG != null) {
            int defaultColor = mContext.getResources().getColor(
                    com.android.internal.R.color.holo_blue_light);
            //ContentResolver resolver = mContext.getContentResolver();
            //mGlowBGColor = Settings.System.getInt(resolver,
             //       Settings.System.NAVIGATION_BAR_GLOW_TINT, defaultColor);

            if (mGlowBGColor == Integer.MIN_VALUE) {
            	mGlowBGColor = defaultColor;
            }
            mGlowBG.setColorFilter(null);
            mGlowBG.setColorFilter(mGlowBGColor, PorterDuff.Mode.SRC_ATOP);

            
        } */
    }
    
=======
            mDrawingAlpha = BUTTON_QUIESCENT_ALPHA;
        }
        
        a.recycle();

        mWindowManager = IWindowManager.Stub.asInterface(
                ServiceManager.getService(Context.WINDOW_SERVICE));

        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        SettingsObserver settingsObserver = new SettingsObserver(new Handler());
        settingsObserver.observe();
    }

>>>>>>> upstream/master
    @Override
    protected void onDraw(Canvas canvas) {
        if (mGlowBG != null) {
            canvas.save();
            final int w = getWidth();
            final int h = getHeight();
<<<<<<< HEAD
            final float aspect = (float)mGlowWidth / mGlowHeight;
            final int drawW = (int)(h*aspect);
            final int drawH = h;
            final int margin = (drawW-w)/2;
            canvas.scale(mGlowScale, mGlowScale, w*0.5f, h*0.5f);
            mGlowBG.setBounds(-margin, 0, drawW-margin, drawH);
            mGlowBG.setAlpha((int)(mDrawingAlpha * mGlowAlpha * 255));
=======
            canvas.scale(mGlowScale, mGlowScale, w*0.5f, h*0.5f);
            mGlowBG.setBounds(0, 0, w, h);
            mGlowBG.setAlpha((int)(mGlowAlpha * 255));
>>>>>>> upstream/master
            mGlowBG.draw(canvas);
            canvas.restore();
            mRect.right = w;
            mRect.bottom = h;
<<<<<<< HEAD
        }
        super.onDraw(canvas);
=======
            canvas.saveLayerAlpha(mRect, (int)(mDrawingAlpha * 255), Canvas.ALL_SAVE_FLAG);
        }
        super.onDraw(canvas);
        if (mGlowBG != null) {
            canvas.restore();
        }
>>>>>>> upstream/master
    }

    public float getDrawingAlpha() {
        if (mGlowBG == null) return 0;
        return mDrawingAlpha;
    }

    public void setDrawingAlpha(float x) {
        if (mGlowBG == null) return;
<<<<<<< HEAD
        // Calling setAlpha(int), which is an ImageView-specific
        // method that's different from setAlpha(float). This sets
        // the alpha on this ImageView's drawable directly
        setAlpha((int) (x * 255));
=======
>>>>>>> upstream/master
        mDrawingAlpha = x;
        invalidate();
    }

    public float getGlowAlpha() {
        if (mGlowBG == null) return 0;
        return mGlowAlpha;
    }

    public void setGlowAlpha(float x) {
        if (mGlowBG == null) return;
        mGlowAlpha = x;
        invalidate();
    }

    public float getGlowScale() {
        if (mGlowBG == null) return 0;
        return mGlowScale;
    }

    public void setGlowScale(float x) {
        if (mGlowBG == null) return;
        mGlowScale = x;
        final float w = getWidth();
        final float h = getHeight();
        if (GLOW_MAX_SCALE_FACTOR <= 1.0f) {
            // this only works if we know the glow will never leave our bounds
            invalidate();
        } else {
            final float rx = (w * (GLOW_MAX_SCALE_FACTOR - 1.0f)) / 2.0f + 1.0f;
            final float ry = (h * (GLOW_MAX_SCALE_FACTOR - 1.0f)) / 2.0f + 1.0f;
            com.android.systemui.SwipeHelper.invalidateGlobalRegion(
                    this,
                    new RectF(getLeft() - rx,
                              getTop() - ry,
                              getRight() + rx,
                              getBottom() + ry));

            // also invalidate our immediate parent to help avoid situations where nearby glows
            // interfere
            ((View)getParent()).invalidate();
        }
    }

    public void setPressed(boolean pressed) {
        if (mGlowBG != null) {
            if (pressed != isPressed()) {
<<<<<<< HEAD
                if (mPressedAnim != null && mPressedAnim.isRunning()) {
                    mPressedAnim.cancel();
                }
                final AnimatorSet as = mPressedAnim = new AnimatorSet();
=======
                AnimatorSet as = new AnimatorSet();
>>>>>>> upstream/master
                if (pressed) {
                    if (mGlowScale < GLOW_MAX_SCALE_FACTOR) 
                        mGlowScale = GLOW_MAX_SCALE_FACTOR;
                    if (mGlowAlpha < BUTTON_QUIESCENT_ALPHA)
                        mGlowAlpha = BUTTON_QUIESCENT_ALPHA;
<<<<<<< HEAD
                    setDrawingAlpha(BUTTON_QUIESCENT_ALPHA);
=======
                    setDrawingAlpha(1f);
>>>>>>> upstream/master
                    as.playTogether(
                        ObjectAnimator.ofFloat(this, "glowAlpha", 1f),
                        ObjectAnimator.ofFloat(this, "glowScale", GLOW_MAX_SCALE_FACTOR)
                    );
<<<<<<< HEAD
                    as.setDuration(durationSpeedOff);
=======
                    as.setDuration(0);
>>>>>>> upstream/master
                } else {
                    as.playTogether(
                        ObjectAnimator.ofFloat(this, "glowAlpha", 0f),
                        ObjectAnimator.ofFloat(this, "glowScale", 1f),
                        ObjectAnimator.ofFloat(this, "drawingAlpha", BUTTON_QUIESCENT_ALPHA)
                    );
<<<<<<< HEAD
                    as.setDuration(durationSpeedOn);
=======
                    as.setDuration(0);
>>>>>>> upstream/master
                }
                as.start();
            }
        }
        super.setPressed(pressed);
    }

<<<<<<< HEAD
    public boolean onTouchEvent(MotionEvent ev) {
=======
    public void setInfo (String itemKey, boolean isVertical) {
        ButtonInfo item = NavbarEditor.buttonMap.get(itemKey);
        setTag(itemKey);
        final Resources res = getResources();
        setContentDescription(res.getString(item.contentDescription));
        mCode = item.keyCode;
        boolean isSmallButton = ArrayUtils.contains(NavbarEditor.smallButtonIds, getId());
        Drawable keyD;
        if (isSmallButton) {
            keyD = res.getDrawable(item.sideResource);
        } else if (!isVertical) {
            keyD = res.getDrawable(item.portResource);
        } else {
            keyD = res.getDrawable(item.landResource);
        }
        //Reason for setImageDrawable vs setImageResource is because setImageResource calls relayout() w/o
        //any checks. setImageDrawable performs size checks and only calls relayout if necessary. We rely on this
        //because otherwise the setX/setY attributes which are post layout cause it to mess up the layout.
        setImageDrawable(keyD);
        if (itemKey.equals(NavbarEditor.NAVBAR_EMPTY)) {
            if (isSmallButton) {
                setVisibility(NavigationBarView.getEditMode() ? View.VISIBLE : View.INVISIBLE);
            } else {
                setVisibility(NavigationBarView.getEditMode() ? View.VISIBLE : View.GONE);
            }
        } else if (itemKey.equals(NavbarEditor.NAVBAR_CONDITIONAL_MENU)) {
            setVisibility(NavigationBarView.getEditMode() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (NavigationBarView.getEditMode()) {
            return false;
        }
>>>>>>> upstream/master
        final int action = ev.getAction();
        int x, y;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Slog.d("KeyButtonView", "press");
<<<<<<< HEAD
            	setHandlingLongpress(false);
=======
>>>>>>> upstream/master
                mDownTime = SystemClock.uptimeMillis();
                setPressed(true);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, 0, mDownTime);
                } else {
                    // Provide the same haptic feedback that the system offers for virtual keys.
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                    postDelayed(mCheckLongPress, ViewConfiguration.getLongPressTimeout());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int)ev.getX();
                y = (int)ev.getY();
                setPressed(x >= -mTouchSlop
                        && x < getWidth() + mTouchSlop
                        && y >= -mTouchSlop
                        && y < getHeight() + mTouchSlop);
                break;
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
            case MotionEvent.ACTION_UP:
                final boolean doIt = isPressed();
                setPressed(false);
                if (mCode != 0) {
<<<<<<< HEAD
                	if ((doIt) && (!mHandlingLongpress)) {
=======
                    if (doIt) {
>>>>>>> upstream/master
                        sendEvent(KeyEvent.ACTION_UP, 0);
                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
                        playSoundEffect(SoundEffectConstants.CLICK);
                    } else {
                        sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                    }
                } else {
                    // no key code, just a regular ImageView
<<<<<<< HEAD
                    if ((doIt) && (!mHandlingLongpress)) {
=======
                    if (doIt) {
>>>>>>> upstream/master
                        performClick();
                    }
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
        }

        return true;
    }

    void sendEvent(int action, int flags) {
        sendEvent(action, flags, SystemClock.uptimeMillis());
    }

    void sendEvent(int action, int flags, long when) {
        final int repeatCount = (flags & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent ev = new KeyEvent(mDownTime, when, action, mCode, repeatCount,
                0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                flags | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
<<<<<<< HEAD
        InputManager.getInstance().injectInputEvent(ev,
                InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }
	
=======
        try {
            //Slog.d(TAG, "injecting event " + ev);
            mWindowManager.injectInputEventNoWait(ev);
        } catch (RemoteException ex) {
            // System process is dead
        }
    }

>>>>>>> upstream/master
    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
<<<<<<< HEAD
            resolver.registerContentObserver(
                    Settings.System.getUriFor(Settings.System.NAVIGATION_BAR_BUTTON_ALPHA), false, this);
			resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.NAVIGATION_BAR_TINT), false, this);
			resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.NAVIGATION_BAR_GLOW_DURATION[1]), false, this);	
=======

            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SOFT_KEY_COLOR), false, this);
>>>>>>> upstream/master
            updateSettings();
        }

        @Override
        public void onChange(boolean selfChange) {
            updateSettings();
        }
    }

    protected void updateSettings() {
        ContentResolver resolver = mContext.getContentResolver();

<<<<<<< HEAD
	durationSpeedOff = Settings.System.getInt(resolver,
	Settings.System.NAVIGATION_BAR_GLOW_DURATION[0], 10);
 	durationSpeedOn = Settings.System.getInt(resolver,
 	Settings.System.NAVIGATION_BAR_GLOW_DURATION[1], 100);

        BUTTON_QUIESCENT_ALPHA = Settings.System.getFloat(resolver, Settings.System.NAVIGATION_BAR_BUTTON_ALPHA, 0.7f);
	    mNavigationBarTint = Settings.System.getInt(resolver, Settings.System.NAVIGATION_BAR_TINT, 0xFFFFFFFF);
        setDrawingAlpha(BUTTON_QUIESCENT_ALPHA);
		setColorFilter(mNavigationBarTint);
        invalidate();
    }
}

=======
        mSoftKeyColor = Settings.System.getInt(resolver, Settings.System.SOFT_KEY_COLOR, 0xFFAEAEAE);
        setColorFilter(mSoftKeyColor);


    }
}


>>>>>>> upstream/master
