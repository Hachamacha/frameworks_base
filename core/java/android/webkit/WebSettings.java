/*
 * Copyright (C) 2007 The Android Open Source Project
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

package android.webkit;

<<<<<<< HEAD
import android.os.Message;
import android.os.Build;
=======
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.EventLog;

import java.util.Locale;
>>>>>>> upstream/master

/**
 * Manages settings state for a WebView. When a WebView is first created, it
 * obtains a set of default settings. These default settings will be returned
 * from any getter call. A WebSettings object obtained from
 * WebView.getSettings() is tied to the life of the WebView. If a WebView has
 * been destroyed, any method call on WebSettings will throw an
 * IllegalStateException.
 */
<<<<<<< HEAD
// This is an abstract base class: concrete WebViewProviders must
// create a class derived from this, and return an instance of it in the
// WebViewProvider.getWebSettingsProvider() method implementation.
public abstract class WebSettings {
    /**
     * Enum for controlling the layout of html.
     * <ul>
     *   <li>NORMAL means no rendering changes.</li>
     *   <li>SINGLE_COLUMN moves all content into one column that is the width of the
     *       view.</li>
     *   <li>NARROW_COLUMNS makes all columns no wider than the screen if possible.</li>
     * </ul>
=======
public class WebSettings {
    /**
     * Enum for controlling the layout of html.
     * NORMAL means no rendering changes.
     * SINGLE_COLUMN moves all content into one column that is the width of the
     * view.
     * NARROW_COLUMNS makes all columns no wider than the screen if possible.
>>>>>>> upstream/master
     */
    // XXX: These must match LayoutAlgorithm in Settings.h in WebCore.
    public enum LayoutAlgorithm {
        NORMAL,
        /**
         * @deprecated This algorithm is now obsolete.
         */
        @Deprecated
        SINGLE_COLUMN,
        NARROW_COLUMNS
    }

    /**
     * Enum for specifying the text size.
<<<<<<< HEAD
     * <ul>
     *   <li>SMALLEST is 50%</li>
     *   <li>SMALLER is 75%</li>
     *   <li>NORMAL is 100%</li>
     *   <li>LARGER is 150%</li>
     *   <li>LARGEST is 200%</li>
     * </ul>
     *
=======
     * SMALLEST is 50%
     * SMALLER is 75%
     * NORMAL is 100%
     * LARGER is 150%
     * LARGEST is 200%
>>>>>>> upstream/master
     * @deprecated Use {@link WebSettings#setTextZoom(int)} and {@link WebSettings#getTextZoom()} instead.
     */
    public enum TextSize {
        SMALLEST(50),
        SMALLER(75),
        NORMAL(100),
        LARGER(150),
        LARGEST(200);
        TextSize(int size) {
            value = size;
        }
        int value;
    }

    /**
     * Enum for specifying the WebView's desired density.
<<<<<<< HEAD
     * <ul>
     *   <li>FAR makes 100% looking like in 240dpi</li>
     *   <li>MEDIUM makes 100% looking like in 160dpi</li>
     *   <li>CLOSE makes 100% looking like in 120dpi</li>
     * </ul>
=======
     * FAR makes 100% looking like in 240dpi
     * MEDIUM makes 100% looking like in 160dpi
     * CLOSE makes 100% looking like in 120dpi
>>>>>>> upstream/master
     */
    public enum ZoomDensity {
        FAR(150),      // 240dpi
        MEDIUM(100),    // 160dpi
        CLOSE(75);     // 120dpi
        ZoomDensity(int size) {
            value = size;
        }
        int value;
    }

    /**
<<<<<<< HEAD
     * Default cache usage pattern. Use with {@link #setCacheMode}.
=======
     * Default cache usage pattern  Use with {@link #setCacheMode}.
>>>>>>> upstream/master
     */
    public static final int LOAD_DEFAULT = -1;

    /**
<<<<<<< HEAD
     * Normal cache usage pattern. Use with {@link #setCacheMode}.
=======
     * Normal cache usage pattern  Use with {@link #setCacheMode}.
>>>>>>> upstream/master
     */
    public static final int LOAD_NORMAL = 0;

    /**
<<<<<<< HEAD
     * Use cache if content is there, even if expired (eg, history nav).
=======
     * Use cache if content is there, even if expired (eg, history nav)
>>>>>>> upstream/master
     * If it is not in the cache, load from network.
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_CACHE_ELSE_NETWORK = 1;

    /**
<<<<<<< HEAD
     * Don't use the cache, load from network.
=======
     * Don't use the cache, load from network
>>>>>>> upstream/master
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_NO_CACHE = 2;

    /**
     * Don't use the network, load from cache only.
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_CACHE_ONLY = 3;

    public enum RenderPriority {
        NORMAL,
        HIGH,
        LOW
    }

    /**
     * The plugin state effects how plugins are treated on a page. ON means
     * that any object will be loaded even if a plugin does not exist to handle
     * the content. ON_DEMAND means that if there is a plugin installed that
     * can handle the content, a placeholder is shown until the user clicks on
     * the placeholder. Once clicked, the plugin will be enabled on the page.
     * OFF means that all plugins will be turned off and any fallback content
     * will be used.
     */
    public enum PluginState {
        ON,
        ON_DEMAND,
        OFF
    }

<<<<<<< HEAD
    /**
     * Hidden constructor to prevent clients from creating a new settings
     * instance or deriving the class.
     *
     * @hide
     */
    protected WebSettings() {
=======
    // TODO: Keep this up to date
    private static final String PREVIOUS_VERSION = "3.1";

    // WebView associated with this WebSettings.
    private WebView mWebView;
    // BrowserFrame used to access the native frame pointer.
    private BrowserFrame mBrowserFrame;
    // Flag to prevent multiple SYNC messages at one time.
    private boolean mSyncPending = false;
    // Custom handler that queues messages until the WebCore thread is active.
    private final EventHandler mEventHandler;

    // Private settings so we don't have to go into native code to
    // retrieve the values. After setXXX, postSync() needs to be called.
    //
    // The default values need to match those in WebSettings.cpp
    // If the defaults change, please also update the JavaDocs so developers
    // know what they are.
    private LayoutAlgorithm mLayoutAlgorithm = LayoutAlgorithm.NARROW_COLUMNS;
    private Context         mContext;
    private int             mTextSize = 100;
    private String          mStandardFontFamily = "sans-serif";
    private String          mFixedFontFamily = "monospace";
    private String          mSansSerifFontFamily = "sans-serif";
    private String          mSerifFontFamily = "serif";
    private String          mCursiveFontFamily = "cursive";
    private String          mFantasyFontFamily = "fantasy";
    private String          mDefaultTextEncoding;
    private String          mUserAgent;
    private boolean         mUseDefaultUserAgent;
    private String          mAcceptLanguage;
    private int             mMinimumFontSize = 8;
    private int             mMinimumLogicalFontSize = 8;
    private int             mDefaultFontSize = 16;
    private int             mDefaultFixedFontSize = 13;
    private int             mPageCacheCapacity = 0;
    private boolean         mLoadsImagesAutomatically = true;
    private boolean         mBlockNetworkImage = false;
    private boolean         mBlockNetworkLoads;
    private boolean         mJavaScriptEnabled = false;
    private boolean         mHardwareAccelSkia = false;
    private boolean         mShowVisualIndicator = false;
    private PluginState     mPluginState = PluginState.OFF;
    private boolean         mJavaScriptCanOpenWindowsAutomatically = false;
    private boolean         mUseDoubleTree = false;
    private boolean         mUseWideViewport = false;
    private boolean         mSupportMultipleWindows = false;
    private boolean         mShrinksStandaloneImagesToFit = false;
    private long            mMaximumDecodedImageSize = 0; // 0 means default
    private boolean         mPrivateBrowsingEnabled = false;
    private boolean         mSyntheticLinksEnabled = true;
    // HTML5 API flags
    private boolean         mAppCacheEnabled = false;
    private boolean         mDatabaseEnabled = false;
    private boolean         mDomStorageEnabled = false;
    private boolean         mWorkersEnabled = false;  // only affects V8.
    private boolean         mGeolocationEnabled = true;
    private boolean         mXSSAuditorEnabled = false;
    // HTML5 configuration parameters
    private long            mAppCacheMaxSize = Long.MAX_VALUE;
    private String          mAppCachePath = "";
    private String          mDatabasePath = "";
    // The WebCore DatabaseTracker only allows the database path to be set
    // once. Keep track of when the path has been set.
    private boolean         mDatabasePathHasBeenSet = false;
    private String          mGeolocationDatabasePath = "";
    // Don't need to synchronize the get/set methods as they
    // are basic types, also none of these values are used in
    // native WebCore code.
    private ZoomDensity     mDefaultZoom = ZoomDensity.MEDIUM;
    private RenderPriority  mRenderPriority = RenderPriority.NORMAL;
    private int             mOverrideCacheMode = LOAD_DEFAULT;
    private int             mDoubleTapZoom = 100;
    private boolean         mSaveFormData = true;
    private boolean         mAutoFillEnabled = false;
    private boolean         mSavePassword = true;
    private boolean         mLightTouchEnabled = false;
    private boolean         mNeedInitialFocus = true;
    private boolean         mNavDump = false;
    private boolean         mSupportZoom = true;
    private boolean         mBuiltInZoomControls = false;
    private boolean         mDisplayZoomControls = true;
    private boolean         mAllowFileAccess = true;
    private boolean         mAllowContentAccess = true;
    private boolean         mLoadWithOverviewMode = false;
    private boolean         mEnableSmoothTransition = false;
    private boolean         mForceUserScalable = false;

    // AutoFill Profile data
    /**
     * @hide for now, pending API council approval.
     */
    public static class AutoFillProfile {
        private int mUniqueId;
        private String mFullName;
        private String mEmailAddress;
        private String mCompanyName;
        private String mAddressLine1;
        private String mAddressLine2;
        private String mCity;
        private String mState;
        private String mZipCode;
        private String mCountry;
        private String mPhoneNumber;

        public AutoFillProfile(int uniqueId, String fullName, String email,
                String companyName, String addressLine1, String addressLine2,
                String city, String state, String zipCode, String country,
                String phoneNumber) {
            mUniqueId = uniqueId;
            mFullName = fullName;
            mEmailAddress = email;
            mCompanyName = companyName;
            mAddressLine1 = addressLine1;
            mAddressLine2 = addressLine2;
            mCity = city;
            mState = state;
            mZipCode = zipCode;
            mCountry = country;
            mPhoneNumber = phoneNumber;
        }

        public int getUniqueId() { return mUniqueId; }
        public String getFullName() { return mFullName; }
        public String getEmailAddress() { return mEmailAddress; }
        public String getCompanyName() { return mCompanyName; }
        public String getAddressLine1() { return mAddressLine1; }
        public String getAddressLine2() { return mAddressLine2; }
        public String getCity() { return mCity; }
        public String getState() { return mState; }
        public String getZipCode() { return mZipCode; }
        public String getCountry() { return mCountry; }
        public String getPhoneNumber() { return mPhoneNumber; }
    }


    private AutoFillProfile mAutoFillProfile;

    private boolean         mUseWebViewBackgroundForOverscroll = true;

    // private WebSettings, not accessible by the host activity
    static private int      mDoubleTapToastCount = 3;

    private static final String PREF_FILE = "WebViewSettings";
    private static final String DOUBLE_TAP_TOAST_COUNT = "double_tap_toast_count";

    // Class to handle messages before WebCore is ready.
    private class EventHandler {
        // Message id for syncing
        static final int SYNC = 0;
        // Message id for setting priority
        static final int PRIORITY = 1;
        // Message id for writing double-tap toast count
        static final int SET_DOUBLE_TAP_TOAST_COUNT = 2;
        // Actual WebCore thread handler
        private Handler mHandler;

        private synchronized void createHandler() {
            // as mRenderPriority can be set before thread is running, sync up
            setRenderPriority();

            // create a new handler
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case SYNC:
                            synchronized (WebSettings.this) {
                                if (mBrowserFrame.mNativeFrame != 0) {
                                    nativeSync(mBrowserFrame.mNativeFrame);
                                }
                                mSyncPending = false;
                            }
                            break;

                        case PRIORITY: {
                            setRenderPriority();
                            break;
                        }

                        case SET_DOUBLE_TAP_TOAST_COUNT: {
                            SharedPreferences.Editor editor = mContext
                                    .getSharedPreferences(PREF_FILE,
                                            Context.MODE_PRIVATE).edit();
                            editor.putInt(DOUBLE_TAP_TOAST_COUNT,
                                    mDoubleTapToastCount);
                            editor.commit();
                            break;
                        }
                    }
                }
            };
        }

        private void setRenderPriority() {
            synchronized (WebSettings.this) {
                if (mRenderPriority == RenderPriority.NORMAL) {
                    android.os.Process.setThreadPriority(
                            android.os.Process.THREAD_PRIORITY_DEFAULT);
                } else if (mRenderPriority == RenderPriority.HIGH) {
                    android.os.Process.setThreadPriority(
                            android.os.Process.THREAD_PRIORITY_FOREGROUND +
                            android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
                } else if (mRenderPriority == RenderPriority.LOW) {
                    android.os.Process.setThreadPriority(
                            android.os.Process.THREAD_PRIORITY_BACKGROUND);
                }
            }
        }

        /**
         * Send a message to the private queue or handler.
         */
        private synchronized boolean sendMessage(Message msg) {
            if (mHandler != null) {
                mHandler.sendMessage(msg);
                return true;
            } else {
                return false;
            }
        }
    }

    // User agent strings.
    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (X11; " +
        "Linux x86_64) AppleWebKit/534.24 (KHTML, like Gecko) " +
        "Chrome/11.0.696.34 Safari/534.24";
    private static final String IPHONE_USERAGENT =
            "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us)"
            + " AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0"
            + " Mobile/7A341 Safari/528.16";
    private static Locale sLocale;
    private static Object sLockForLocaleSettings;

    /**
     * Package constructor to prevent clients from creating a new settings
     * instance.
     */
    WebSettings(Context context, WebView webview) {
        mEventHandler = new EventHandler();
        mContext = context;
        mWebView = webview;
        mDefaultTextEncoding = context.getString(com.android.internal.
                                                 R.string.default_text_encoding);

        if (sLockForLocaleSettings == null) {
            sLockForLocaleSettings = new Object();
            sLocale = Locale.getDefault();
        }
        mAcceptLanguage = getCurrentAcceptLanguage();
        mUserAgent = getCurrentUserAgent();
        mUseDefaultUserAgent = true;

        mBlockNetworkLoads = mContext.checkPermission(
                "android.permission.INTERNET", android.os.Process.myPid(),
                android.os.Process.myUid()) != PackageManager.PERMISSION_GRANTED;
    }

    private static final String ACCEPT_LANG_FOR_US_LOCALE = "en-US";

    /**
     * Looks at sLocale and returns current AcceptLanguage String.
     * @return Current AcceptLanguage String.
     */
    private String getCurrentAcceptLanguage() {
        Locale locale;
        synchronized(sLockForLocaleSettings) {
            locale = sLocale;
        }
        StringBuilder buffer = new StringBuilder();
        addLocaleToHttpAcceptLanguage(buffer, locale);

        if (!Locale.US.equals(locale)) {
            if (buffer.length() > 0) {
                buffer.append(", ");
            }
            buffer.append(ACCEPT_LANG_FOR_US_LOCALE);
        }

        return buffer.toString();
    }

    /**
     * Convert obsolete language codes, including Hebrew/Indonesian/Yiddish,
     * to new standard.
     */
    private static String convertObsoleteLanguageCodeToNew(String langCode) {
        if (langCode == null) {
            return null;
        }
        if ("iw".equals(langCode)) {
            // Hebrew
            return "he";
        } else if ("in".equals(langCode)) {
            // Indonesian
            return "id";
        } else if ("ji".equals(langCode)) {
            // Yiddish
            return "yi";
        }
        return langCode;
    }

    private static void addLocaleToHttpAcceptLanguage(StringBuilder builder,
                                                      Locale locale) {
        String language = convertObsoleteLanguageCodeToNew(locale.getLanguage());
        if (language != null) {
            builder.append(language);
            String country = locale.getCountry();
            if (country != null) {
                builder.append("-");
                builder.append(country);
            }
        }
    }

    /**
     * Looks at sLocale and mContext and returns current UserAgent String.
     * @return Current UserAgent String.
     */
    private synchronized String getCurrentUserAgent() {
        Locale locale;
        synchronized(sLockForLocaleSettings) {
            locale = sLocale;
        }
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            if (Character.isDigit(version.charAt(0))) {
                // Release is a version, eg "3.1"
                buffer.append(version);
            } else {
                // Release is a codename, eg "Honeycomb"
                // In this case, use the previous release's version
                buffer.append(PREVIOUS_VERSION);
            }
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(convertObsoleteLanguageCodeToNew(language));
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        buffer.append(";");
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append(" ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        String mobile = mContext.getResources().getText(
            com.android.internal.R.string.web_user_agent_target_content).toString();
        final String base = mContext.getResources().getText(
                com.android.internal.R.string.web_user_agent).toString();
        return String.format(base, buffer, mobile);
>>>>>>> upstream/master
    }

    /**
     * Enables dumping the pages navigation cache to a text file.
<<<<<<< HEAD
     *
=======
>>>>>>> upstream/master
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public void setNavDump(boolean enabled) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets whether dumping the navigation cache is enabled.
     *
=======
        mNavDump = enabled;
    }

    /**
     * Returns true if dumping the navigation cache is enabled.
>>>>>>> upstream/master
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public boolean getNavDump() {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should support zooming using its on-screen zoom
     * controls and gestures. The particular zoom mechanisms that should be used
     * can be set with {@link #setBuiltInZoomControls}. This setting does not
     * affect zooming performed using the {@link WebView#zoomIn()} and
     * {@link WebView#zoomOut()} methods. The default is true.
     *
     * @param support whether the WebView should support zoom
     */
    public void setSupportZoom(boolean support) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView supports zoom.
     *
     * @return true if the WebView supports zoom
     * @see #setSupportZoom
     */
    public boolean supportZoom() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should use its built-in zoom mechanisms. The
     * built-in zoom mechanisms comprise on-screen zoom controls, which are
     * displayed over the WebView's content, and the use of a pinch gesture to
     * control zooming. Whether or not these on-screen controls are displayed
     * can be set with {@link #setDisplayZoomControls}. The default is false.
     * <p>
     * The built-in mechanisms are the only currently supported zoom
     * mechanisms, so it is recommended that this setting is always enabled.
     *
     * @param enabled whether the WebView should use its built-in zoom mechanisms
     */
    // This method was intended to select between the built-in zoom mechanisms
    // and the separate zoom controls. The latter were obtained using
    // {@link WebView#getZoomControls}, which is now hidden.
    public void setBuiltInZoomControls(boolean enabled) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the zoom mechanisms built into WebView are being used.
     *
     * @return true if the zoom mechanisms built into WebView are being used
     * @see #setBuiltInZoomControls
     */
    public boolean getBuiltInZoomControls() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should display on-screen zoom controls when
     * using the built-in zoom mechanisms. See {@link #setBuiltInZoomControls}.
     * The default is true.
     *
     * @param enabled whether the WebView should display on-screen zoom controls
     */
    public void setDisplayZoomControls(boolean enabled) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView displays on-screen zoom controls when using
     * the built-in zoom mechanisms.
     *
     * @return true if the WebView displays on-screen zoom controls when using
     *         the built-in zoom mechanisms
     * @see #setDisplayZoomControls
     */
    public boolean getDisplayZoomControls() {
        throw new MustOverrideException();
    }

    /**
     * Enables or disables file access within WebView. File access is enabled by
=======
        return mNavDump;
    }

    /**
     * If WebView only supports touch, a different navigation model will be
     * applied. Otherwise, the navigation to support both touch and keyboard
     * will be used.
     * @hide
    public void setSupportTouchOnly(boolean touchOnly) {
        mSupportTounchOnly = touchOnly;
    }
     */

    boolean supportTouchOnly() {
        // for debug only, use mLightTouchEnabled for mSupportTounchOnly
        return mLightTouchEnabled;
    }

    /**
     * Set whether the WebView supports zoom
     */
    public void setSupportZoom(boolean support) {
        mSupportZoom = support;
        mWebView.updateMultiTouchSupport(mContext);
    }

    /**
     * Returns whether the WebView supports zoom
     */
    public boolean supportZoom() {
        return mSupportZoom;
    }

    /**
     * Sets whether the zoom mechanism built into WebView is used.
     */
    public void setBuiltInZoomControls(boolean enabled) {
        mBuiltInZoomControls = enabled;
        mWebView.updateMultiTouchSupport(mContext);
    }

    /**
     * Returns true if the zoom mechanism built into WebView is being used.
     */
    public boolean getBuiltInZoomControls() {
        return mBuiltInZoomControls;
    }

    /**
     * Sets whether the on screen zoom buttons are used.
     * A combination of built in zoom controls enabled
     * and on screen zoom controls disabled allows for pinch to zoom
     * to work without the on screen controls
     */
    public void setDisplayZoomControls(boolean enabled) {
        mDisplayZoomControls = enabled;
        mWebView.updateMultiTouchSupport(mContext);
    }

    /**
     * Returns true if the on screen zoom buttons are being used.
     */
    public boolean getDisplayZoomControls() {
        return mDisplayZoomControls;
    }

    /**
     * Enable or disable file access within WebView. File access is enabled by
>>>>>>> upstream/master
     * default.  Note that this enables or disables file system access only.
     * Assets and resources are still accessible using file:///android_asset and
     * file:///android_res.
     */
    public void setAllowFileAccess(boolean allow) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView supports file access.
     *
     * @see #setAllowFileAccess
     */
    public boolean getAllowFileAccess() {
        throw new MustOverrideException();
    }

    /**
     * Enables or disables content URL access within WebView.  Content URL
     * access allows WebView to load content from a content provider installed
     * in the system. The default is enabled.
     */
    public void setAllowContentAccess(boolean allow) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView supports content URL access.
     *
     * @see #setAllowContentAccess
     */
    public boolean getAllowContentAccess() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView loads a page with overview mode.
     */
    public void setLoadWithOverviewMode(boolean overview) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView loads pages with overview mode.
     */
    public boolean getLoadWithOverviewMode() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView will enable smooth transition while panning or
=======
        mAllowFileAccess = allow;
    }

    /**
     * Returns true if this WebView supports file access.
     */
    public boolean getAllowFileAccess() {
        return mAllowFileAccess;
    }

    /**
     * Enable or disable content url access within WebView.  Content url access
     * allows WebView to load content from a content provider installed in the
     * system.  The default is enabled.
     */
    public void setAllowContentAccess(boolean allow) {
        mAllowContentAccess = allow;
    }

    /**
     * Returns true if this WebView supports content url access.
     */
    public boolean getAllowContentAccess() {
        return mAllowContentAccess;
    }

    /**
     * Set whether the WebView loads a page with overview mode.
     */
    public void setLoadWithOverviewMode(boolean overview) {
        mLoadWithOverviewMode = overview;
    }

    /**
     * Returns true if this WebView loads page with overview mode
     */
    public boolean getLoadWithOverviewMode() {
        return mLoadWithOverviewMode;
    }

    /**
     * Set whether the WebView will enable smooth transition while panning or
>>>>>>> upstream/master
     * zooming or while the window hosting the WebView does not have focus.
     * If it is true, WebView will choose a solution to maximize the performance.
     * e.g. the WebView's content may not be updated during the transition.
     * If it is false, WebView will keep its fidelity. The default value is false.
     */
    public void setEnableSmoothTransition(boolean enable) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView enables smooth transition while panning or
     * zooming.
     *
     * @see #setEnableSmoothTransition
     */
    public boolean enableSmoothTransition() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView uses its background for over scroll background.
     * If true, it will use the WebView's background. If false, it will use an
     * internal pattern. Default is true.
     *
=======
        mEnableSmoothTransition = enable;
    }

    /**
     * Returns true if the WebView enables smooth transition while panning or
     * zooming.
     */
    public boolean enableSmoothTransition() {
        return mEnableSmoothTransition;
    }

    /**
     * Set whether the WebView uses its background for over scroll background.
     * If true, it will use the WebView's background. If false, it will use an
     * internal pattern. Default is true.
>>>>>>> upstream/master
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public void setUseWebViewBackgroundForOverscrollBackground(boolean view) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView uses WebView's background instead of
     * internal pattern for over scroll background.
     *
     * @see #setUseWebViewBackgroundForOverscrollBackground
=======
        mUseWebViewBackgroundForOverscroll = view;
    }

    /**
     * Returns true if this WebView uses WebView's background instead of
     * internal pattern for over scroll background.
>>>>>>> upstream/master
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public boolean getUseWebViewBackgroundForOverscrollBackground() {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView is saving form data.
     */
    public void setSaveFormData(boolean save) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is saving form data and displaying prior
     * entries/autofill++.  Always false in private browsing mode.
     */
    public boolean getSaveFormData() {
        throw new MustOverrideException();
    }

    /**
     * Stores whether the WebView is saving password.
     */
    public void setSavePassword(boolean save) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is saving password.
     */
    public boolean getSavePassword() {
        throw new MustOverrideException();
    }

    /**
     * Sets the text zoom of the page in percent. Default is 100.
     *
     * @param textZoom the percent value for increasing or decreasing the text
     */
    public synchronized void setTextZoom(int textZoom) {
        throw new MustOverrideException();
    }

    /**
     * Gets the text zoom of the page in percent.
     *
     * @return a percent value describing the text zoom
     * @see #setTextSizeZoom
     */
    public synchronized int getTextZoom() {
        throw new MustOverrideException();
    }

    /**
     * Sets the text size of the page.
     *
     * @param t the TextSize value for increasing or decreasing the text
     * @see WebSettings.TextSize
     * @deprecated Use {@link #setTextZoom(int)} instead.
     */
    public synchronized void setTextSize(TextSize t) {
        throw new MustOverrideException();
    }

    /**
     * Gets the text size of the page. If the text size was previously specified
     * in percent using {@link #setTextZoom(int)}, this will return
     * the closest matching {@link TextSize}.
     *
     * @return a TextSize enum value describing the text size
     * @see WebSettings.TextSize
     * @deprecated Use {@link #getTextZoom()} instead.
     */
    public synchronized TextSize getTextSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the default zoom density of the page. This should be called from UI
     * thread.
     *
     * @param zoom a ZoomDensity value
     * @see WebSettings.ZoomDensity
     */
    public void setDefaultZoom(ZoomDensity zoom) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default zoom density of the page. This should be called from UI
     * thread.
     * @return a ZoomDensity value
     * @see WebSettings.ZoomDensity
     */
    public ZoomDensity getDefaultZoom() {
        throw new MustOverrideException();
=======
        return mUseWebViewBackgroundForOverscroll;
    }

    /**
     * Store whether the WebView is saving form data.
     */
    public void setSaveFormData(boolean save) {
        mSaveFormData = save;
    }

    /**
     *  Return whether the WebView is saving form data and displaying prior
     *  entries/autofill++.  Always false in private browsing mode.
     */
    public boolean getSaveFormData() {
        return mSaveFormData && !mPrivateBrowsingEnabled;
    }

    /**
     *  Store whether the WebView is saving password.
     */
    public void setSavePassword(boolean save) {
        mSavePassword = save;
    }

    /**
     *  Return whether the WebView is saving password.
     */
    public boolean getSavePassword() {
        return mSavePassword;
    }

    /**
     * Set the text zoom of the page in percent. Default is 100.
     * @param textZoom A percent value for increasing or decreasing the text.
     */
    public synchronized void setTextZoom(int textZoom) {
        if (mTextSize != textZoom) {
            if (WebView.mLogEvent) {
                EventLog.writeEvent(EventLogTags.BROWSER_TEXT_SIZE_CHANGE,
                        mTextSize, textZoom);
            }
            mTextSize = textZoom;
            postSync();
        }
    }

    /**
     * Get the text zoom of the page in percent.
     * @return A percent value describing the text zoom.
     * @see setTextSizeZoom
     */
    public synchronized int getTextZoom() {
        return mTextSize;
    }

    /**
     * Set the text size of the page.
     * @param t A TextSize value for increasing or decreasing the text.
     * @see WebSettings.TextSize
     * @deprecated Use {@link #setTextZoom(int)} instead
     */
    public synchronized void setTextSize(TextSize t) {
        setTextZoom(t.value);
    }

    /**
     * Get the text size of the page. If the text size was previously specified
     * in percent using {@link #setTextZoom(int)}, this will return
     * the closest matching {@link TextSize}.
     * @return A TextSize enum value describing the text size.
     * @see WebSettings.TextSize
     * @deprecated Use {@link #getTextZoom()} instead
     */
    public synchronized TextSize getTextSize() {
        TextSize closestSize = null;
        int smallestDelta = Integer.MAX_VALUE;
        for (TextSize size : TextSize.values()) {
            int delta = Math.abs(mTextSize - size.value);
            if (delta == 0) {
                return size;
            }
            if (delta < smallestDelta) {
                smallestDelta = delta;
                closestSize = size;
            }
        }
        return closestSize != null ? closestSize : TextSize.NORMAL;
    }

    /**
     * Set the double-tap zoom of the page in percent. Default is 100.
     * @param doubleTapZoom A percent value for increasing or decreasing the double-tap zoom.
     * @hide
     */
    public void setDoubleTapZoom(int doubleTapZoom) {
        if (mDoubleTapZoom != doubleTapZoom) {
            mDoubleTapZoom = doubleTapZoom;
            mWebView.updateDoubleTapZoom(doubleTapZoom);
        }
    }

    /**
     * Get the double-tap zoom of the page in percent.
     * @return A percent value describing the double-tap zoom.
     * @hide
     */
    public int getDoubleTapZoom() {
        return mDoubleTapZoom;
    }

    /**
     * Set the default zoom density of the page. This should be called from UI
     * thread.
     * @param zoom A ZoomDensity value
     * @see WebSettings.ZoomDensity
     */
    public void setDefaultZoom(ZoomDensity zoom) {
        if (mDefaultZoom != zoom) {
            mDefaultZoom = zoom;
            mWebView.adjustDefaultZoomDensity(zoom.value);
        }
    }

    /**
     * Get the default zoom density of the page. This should be called from UI
     * thread.
     * @return A ZoomDensity value
     * @see WebSettings.ZoomDensity
     */
    public ZoomDensity getDefaultZoom() {
        return mDefaultZoom;
>>>>>>> upstream/master
    }

    /**
     * Enables using light touches to make a selection and activate mouseovers.
     */
    public void setLightTouchEnabled(boolean enabled) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets whether light touches are enabled.
     */
    public boolean getLightTouchEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Controlled a rendering optimization that is no longer present. Setting
     * it now has no effect.
     *
     * @deprecated This setting now has no effect.
     */
    @Deprecated
    public synchronized void setUseDoubleTree(boolean use) {
        // Specified to do nothing, so no need for derived classes to override.
    }

    /**
     * Controlled a rendering optimization that is no longer present. Setting
     * it now has no effect.
     *
     * @deprecated This setting now has no effect.
     */
    @Deprecated
    public synchronized boolean getUseDoubleTree() {
        // Returns false unconditionally, so no need for derived classes to override.
=======
        mLightTouchEnabled = enabled;
    }

    /**
     * Returns true if light touches are enabled.
     */
    public boolean getLightTouchEnabled() {
        return mLightTouchEnabled;
    }

    /**
     * @deprecated This setting controlled a rendering optimization
     * that is no longer present. Setting it now has no effect.
     */
    @Deprecated
    public synchronized void setUseDoubleTree(boolean use) {
        return;
    }

    /**
     * @deprecated This setting controlled a rendering optimization
     * that is no longer present. Setting it now has no effect.
     */
    @Deprecated
    public synchronized boolean getUseDoubleTree() {
>>>>>>> upstream/master
        return false;
    }

    /**
<<<<<<< HEAD
     * Tells the WebView about user-agent string.
     *
     * @param ua 0 if the WebView should use an Android user-agent string,
     *           1 if the WebView should use a desktop user-agent string
=======
     * Tell the WebView about user-agent string.
     * @param ua 0 if the WebView should use an Android user-agent string,
     *           1 if the WebView should use a desktop user-agent string.
     *
>>>>>>> upstream/master
     * @deprecated Please use setUserAgentString instead.
     */
    @Deprecated
    public synchronized void setUserAgent(int ua) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets the user-agent as an int.
     *
     * @return 0 if the WebView is using an Android user-agent string,
     *         1 if the WebView is using a desktop user-agent string,
     *         -1 if the WebView is using user defined user-agent string
=======
        String uaString = null;
        if (ua == 1) {
            if (DESKTOP_USERAGENT.equals(mUserAgent)) {
                return; // do nothing
            } else {
                uaString = DESKTOP_USERAGENT;
            }
        } else if (ua == 2) {
            if (IPHONE_USERAGENT.equals(mUserAgent)) {
                return; // do nothing
            } else {
                uaString = IPHONE_USERAGENT;
            }
        } else if (ua != 0) {
            return; // do nothing
        }
        setUserAgentString(uaString);
    }

    /**
     * Return user-agent as int
     * @return int  0 if the WebView is using an Android user-agent string.
     *              1 if the WebView is using a desktop user-agent string.
     *             -1 if the WebView is using user defined user-agent string.
     *
>>>>>>> upstream/master
     * @deprecated Please use getUserAgentString instead.
     */
    @Deprecated
    public synchronized int getUserAgent() {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to use the wide viewport.
     */
    public synchronized void setUseWideViewPort(boolean use) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is using a wide viewport.
     *
     * @return true if the WebView is using a wide viewport
     */
    public synchronized boolean getUseWideViewPort() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView whether it supports multiple windows. TRUE means
     * that {@link WebChromeClient#onCreateWindow(WebView, boolean,
     * boolean, Message)} is implemented by the host application.
     */
    public synchronized void setSupportMultipleWindows(boolean support) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is supporting multiple windows.
     *
     * @return true if the WebView is supporting multiple windows. This means
=======
        if (DESKTOP_USERAGENT.equals(mUserAgent)) {
            return 1;
        } else if (IPHONE_USERAGENT.equals(mUserAgent)) {
            return 2;
        } else if (mUseDefaultUserAgent) {
            return 0;
        }
        return -1;
    }

    /**
     * Tell the WebView to use the wide viewport
     */
    public synchronized void setUseWideViewPort(boolean use) {
        if (mUseWideViewport != use) {
            mUseWideViewport = use;
            postSync();
        }
    }

    /**
     * @return True if the WebView is using a wide viewport
     */
    public synchronized boolean getUseWideViewPort() {
        return mUseWideViewport;
    }

    /**
     * Tell the WebView whether it supports multiple windows. TRUE means
     *         that {@link WebChromeClient#onCreateWindow(WebView, boolean,
     *         boolean, Message)} is implemented by the host application.
     */
    public synchronized void setSupportMultipleWindows(boolean support) {
        if (mSupportMultipleWindows != support) {
            mSupportMultipleWindows = support;
            postSync();
        }
    }

    /**
     * @return True if the WebView is supporting multiple windows. This means
>>>>>>> upstream/master
     *         that {@link WebChromeClient#onCreateWindow(WebView, boolean,
     *         boolean, Message)} is implemented by the host application.
     */
    public synchronized boolean supportMultipleWindows() {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Sets the underlying layout algorithm. This will cause a relayout of the
     * WebView. The default is NARROW_COLUMNS.
     *
     * @param l a LayoutAlgorithm enum specifying the algorithm to use
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized void setLayoutAlgorithm(LayoutAlgorithm l) {
        throw new MustOverrideException();
    }

    /**
     * Gets the current layout algorithm.
     *
     * @return a LayoutAlgorithm enum value describing the layout algorithm
     *         being used
     * @see #setLayoutAlgorithm
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized LayoutAlgorithm getLayoutAlgorithm() {
        throw new MustOverrideException();
    }

    /**
     * Sets the standard font family name. The default is "sans-serif".
     *
     * @param font a font family name
     */
    public synchronized void setStandardFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the standard font family name.
     *
     * @return the standard font family name as a string
     * @see #setStandardFontFamily
     */
    public synchronized String getStandardFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the fixed font family name. The default is "monospace".
     *
     * @param font a font family name
     */
    public synchronized void setFixedFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the fixed font family name.
     *
     * @return the fixed font family name as a string
     * @see #setFixedFontFamily
     */
    public synchronized String getFixedFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the sans-serif font family name.
     *
     * @param font a font family name
     */
    public synchronized void setSansSerifFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the sans-serif font family name.
     *
     * @return the sans-serif font family name as a string
     */
    public synchronized String getSansSerifFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the serif font family name. The default is "sans-serif".
     *
     * @param font a font family name
     */
    public synchronized void setSerifFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the serif font family name. The default is "serif".
     *
     * @return the serif font family name as a string
     * @see #setSerifFontFamily
     */
    public synchronized String getSerifFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the cursive font family name. The default is "cursive".
     *
     * @param font a font family name
     */
    public synchronized void setCursiveFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the cursive font family name.
     *
     * @return the cursive font family name as a string
     * @see #setCursiveFontFamily
     */
    public synchronized String getCursiveFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the fantasy font family name. The default is "fantasy".
     *
     * @param font a font family name
     */
    public synchronized void setFantasyFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the fantasy font family name.
     *
     * @return the fantasy font family name as a string
     * @see #setFantasyFontFamily
     */
    public synchronized String getFantasyFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the minimum font size. The default is 8.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setMinimumFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the minimum font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setMinimumFontSize
     */
    public synchronized int getMinimumFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the minimum logical font size. The default is 8.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setMinimumLogicalFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the minimum logical font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setMinimumLogicalFontSize
     */
    public synchronized int getMinimumLogicalFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the default font size. The default is 16.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setDefaultFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setDefaultFontSize
     */
    public synchronized int getDefaultFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the default fixed font size. The default is 16.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setDefaultFixedFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default fixed font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setDefaultFixedFontSize
     */
    public synchronized int getDefaultFixedFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should load image resources. Note that this method
     * controls loading of all images, including those embedded using the data
     * URI scheme. Use {@link #setBlockNetworkImage} to control loading only
     * of images specified using network URI schemes. Note that if the value of this
     * setting is changed from false to true, all images resources referenced
     * by content currently displayed by the WebView are loaded automatically.
     * The default is true.
     *
     * @param flag whether the WebView should load image resources
     */
    public synchronized void setLoadsImagesAutomatically(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView loads image resources. This includes
     * images embedded using the data URI scheme.
     *
     * @return true if the WebView loads image resources
     * @see #setLoadsImagesAutomatically
     */
    public synchronized boolean getLoadsImagesAutomatically() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should not load image resources from the
     * network (resources accessed via http and https URI schemes).  Note
     * that this method has no effect unless
     * {@link #getLoadsImagesAutomatically} returns true. Also note that
     * disabling all network loads using {@link #setBlockNetworkLoads}
     * will also prevent network images from loading, even if this flag is set
     * to false. When the value of this setting is changed from true to false,
     * network images resources referenced by content currently displayed by
     * the WebView are fetched automatically. The default is false.
     *
     * @param flag whether the WebView should not load image resources from the
     *             network
     * @see #setBlockNetworkLoads
     */
    public synchronized void setBlockNetworkImage(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView does not load image resources from the network.
     *
     * @return true if the WebView does not load image resources from the network
     * @see #setBlockNetworkImage
     */
    public synchronized boolean getBlockNetworkImage() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should not load resources from the network.
     * Use {@link #setBlockNetworkImage} to only avoid loading
     * image resources. Note that if the value of this setting is
     * changed from true to false, network resources referenced by content
     * currently displayed by the WebView are not fetched until
     * {@link android.webkit.WebView#reload} is called.
     * If the application does not have the
     * {@link android.Manifest.permission#INTERNET} permission, attempts to set
     * a value of false will cause a {@link java.lang.SecurityException}
     * to be thrown. The default value is false if the application has the
     * {@link android.Manifest.permission#INTERNET} permission, otherwise it is
     * true.
     *
     * @param flag whether the WebView should not load any resources from the
     *             network
     * @see android.webkit.WebView#reload
     */
    public synchronized void setBlockNetworkLoads(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView does not load any resources from the network.
     *
     * @return true if the WebView does not load any resources from the network
     * @see #setBlockNetworkLoads
     */
    public synchronized boolean getBlockNetworkLoads() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to enable JavaScript execution.
     * <b>The default is false.</b>
     *
     * @param flag true if the WebView should execute JavaScript
     */
    public synchronized void setJavaScriptEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets whether JavaScript running in the context of a file scheme URL
     * should be allowed to access content from any origin. This includes
     * access to content from other file scheme URLs. See
     * {@link #setAllowFileAccessFromFileURLs}. To enable the most restrictive,
     * and therefore secure policy, this setting should be disabled.
     * <p>
     * The default value is true for API level
     * {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH_MR1} and below,
     * and false for API level {@link android.os.Build.VERSION_CODES#JELLY_BEAN}
     * and above.
     *
     * @param flag whether JavaScript running in the context of a file scheme
     *             URL should be allowed to access content from any origin
     */
    public abstract void setAllowUniversalAccessFromFileURLs(boolean flag);

    /**
     * Sets whether JavaScript running in the context of a file scheme URL
     * should be allowed to access content from other file scheme URLs. To
     * enable the most restrictive, and therefore secure policy, this setting
     * should be disabled. Note that the value of this setting is ignored if
     * the value of {@link #getAllowUniversalAccessFromFileURLs} is true.
     * <p>
     * The default value is true for API level
     * {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH_MR1} and below,
     * and false for API level {@link android.os.Build.VERSION_CODES#JELLY_BEAN}
     * and above.
     *
     * @param flag whether JavaScript running in the context of a file scheme
     *             URL should be allowed to access content from other file
     *             scheme URLs
     */
    public abstract void setAllowFileAccessFromFileURLs(boolean flag);

    /**
     * Tells the WebView to enable plugins.
     *
     * @param flag true if the WebView should load plugins
=======
        return mSupportMultipleWindows;
    }

    /**
     * Set the underlying layout algorithm. This will cause a relayout of the
     * WebView.
     * @param l A LayoutAlgorithm enum specifying the algorithm to use.
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized void setLayoutAlgorithm(LayoutAlgorithm l) {
        // XXX: This will only be affective if libwebcore was built with
        // ANDROID_LAYOUT defined.
        if (mLayoutAlgorithm != l) {
            mLayoutAlgorithm = l;
            postSync();
        }
    }

    /**
     * Return the current layout algorithm. The default is NARROW_COLUMNS.
     * @return LayoutAlgorithm enum value describing the layout algorithm
     *         being used.
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized LayoutAlgorithm getLayoutAlgorithm() {
        return mLayoutAlgorithm;
    }

    /**
     * Set the standard font family name.
     * @param font A font family name.
     */
    public synchronized void setStandardFontFamily(String font) {
        if (font != null && !font.equals(mStandardFontFamily)) {
            mStandardFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the standard font family name. The default is "sans-serif".
     * @return The standard font family name as a string.
     */
    public synchronized String getStandardFontFamily() {
        return mStandardFontFamily;
    }

    /**
     * Set the fixed font family name.
     * @param font A font family name.
     */
    public synchronized void setFixedFontFamily(String font) {
        if (font != null && !font.equals(mFixedFontFamily)) {
            mFixedFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the fixed font family name. The default is "monospace".
     * @return The fixed font family name as a string.
     */
    public synchronized String getFixedFontFamily() {
        return mFixedFontFamily;
    }

    /**
     * Set the sans-serif font family name.
     * @param font A font family name.
     */
    public synchronized void setSansSerifFontFamily(String font) {
        if (font != null && !font.equals(mSansSerifFontFamily)) {
            mSansSerifFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the sans-serif font family name.
     * @return The sans-serif font family name as a string.
     */
    public synchronized String getSansSerifFontFamily() {
        return mSansSerifFontFamily;
    }

    /**
     * Set the serif font family name. The default is "sans-serif".
     * @param font A font family name.
     */
    public synchronized void setSerifFontFamily(String font) {
        if (font != null && !font.equals(mSerifFontFamily)) {
            mSerifFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the serif font family name. The default is "serif".
     * @return The serif font family name as a string.
     */
    public synchronized String getSerifFontFamily() {
        return mSerifFontFamily;
    }

    /**
     * Set the cursive font family name.
     * @param font A font family name.
     */
    public synchronized void setCursiveFontFamily(String font) {
        if (font != null && !font.equals(mCursiveFontFamily)) {
            mCursiveFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the cursive font family name. The default is "cursive".
     * @return The cursive font family name as a string.
     */
    public synchronized String getCursiveFontFamily() {
        return mCursiveFontFamily;
    }

    /**
     * Set the fantasy font family name.
     * @param font A font family name.
     */
    public synchronized void setFantasyFontFamily(String font) {
        if (font != null && !font.equals(mFantasyFontFamily)) {
            mFantasyFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the fantasy font family name. The default is "fantasy".
     * @return The fantasy font family name as a string.
     */
    public synchronized String getFantasyFontFamily() {
        return mFantasyFontFamily;
    }

    /**
     * Set the minimum font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setMinimumFontSize(int size) {
        size = pin(size);
        if (mMinimumFontSize != size) {
            mMinimumFontSize = size;
            postSync();
        }
    }

    /**
     * Get the minimum font size. The default is 8.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getMinimumFontSize() {
        return mMinimumFontSize;
    }

    /**
     * Set the minimum logical font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setMinimumLogicalFontSize(int size) {
        size = pin(size);
        if (mMinimumLogicalFontSize != size) {
            mMinimumLogicalFontSize = size;
            postSync();
        }
    }

    /**
     * Get the minimum logical font size. The default is 8.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getMinimumLogicalFontSize() {
        return mMinimumLogicalFontSize;
    }

    /**
     * Set the default font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setDefaultFontSize(int size) {
        size = pin(size);
        if (mDefaultFontSize != size) {
            mDefaultFontSize = size;
            postSync();
        }
    }

    /**
     * Get the default font size. The default is 16.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getDefaultFontSize() {
        return mDefaultFontSize;
    }

    /**
     * Set the default fixed font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setDefaultFixedFontSize(int size) {
        size = pin(size);
        if (mDefaultFixedFontSize != size) {
            mDefaultFixedFontSize = size;
            postSync();
        }
    }

    /**
     * Get the default fixed font size. The default is 16.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getDefaultFixedFontSize() {
        return mDefaultFixedFontSize;
    }

    /**
     * Set the number of pages cached by the WebKit for the history navigation.
     * @param size A non-negative integer between 0 (no cache) and 20 (max).
     * @hide
     */
    public synchronized void setPageCacheCapacity(int size) {
        if (size < 0) size = 0;
        if (size > 20) size = 20;
        if (mPageCacheCapacity != size) {
            mPageCacheCapacity = size;
            postSync();
        }
    }

    /**
     * Tell the WebView to load image resources automatically.
     * @param flag True if the WebView should load images automatically.
     */
    public synchronized void setLoadsImagesAutomatically(boolean flag) {
        if (mLoadsImagesAutomatically != flag) {
            mLoadsImagesAutomatically = flag;
            postSync();
        }
    }

    /**
     * Return true if the WebView will load image resources automatically.
     * The default is true.
     * @return True if the WebView loads images automatically.
     */
    public synchronized boolean getLoadsImagesAutomatically() {
        return mLoadsImagesAutomatically;
    }

    /**
     * Tell the WebView to block network images. This is only checked when
     * {@link #getLoadsImagesAutomatically} is true. If you set the value to
     * false, images will automatically be loaded. Use this api to reduce
     * bandwidth only. Use {@link #setBlockNetworkLoads} if possible.
     * @param flag True if the WebView should block network images.
     * @see #setBlockNetworkLoads
     */
    public synchronized void setBlockNetworkImage(boolean flag) {
        if (mBlockNetworkImage != flag) {
            mBlockNetworkImage = flag;
            postSync();
        }
    }

    /**
     * Return true if the WebView will block network images. The default is
     * false.
     * @return True if the WebView blocks network images.
     */
    public synchronized boolean getBlockNetworkImage() {
        return mBlockNetworkImage;
    }

    /**
     * Tell the WebView to block all network load requests. If you set the
     * value to false, you must call {@link android.webkit.WebView#reload} to
     * fetch remote resources. This flag supercedes the value passed to
     * {@link #setBlockNetworkImage}.
     * @param flag True if the WebView should block all network loads.
     * @see android.webkit.WebView#reload
     */
    public synchronized void setBlockNetworkLoads(boolean flag) {
        if (mBlockNetworkLoads != flag) {
            mBlockNetworkLoads = flag;
            verifyNetworkAccess();
            postSync();
        }
    }

    /**
     * Return true if the WebView will block all network loads. The default is
     * false.
     * @return True if the WebView blocks all network loads.
     */
    public synchronized boolean getBlockNetworkLoads() {
        return mBlockNetworkLoads;
    }


    private void verifyNetworkAccess() {
        if (!mBlockNetworkLoads) {
            if (mContext.checkPermission("android.permission.INTERNET",
                    android.os.Process.myPid(), android.os.Process.myUid()) !=
                        PackageManager.PERMISSION_GRANTED) {
                throw new SecurityException
                        ("Permission denied - " +
                                "application missing INTERNET permission");
            }
        }
    }

    /**
     * Tell the WebView to enable javascript execution.
     * @param flag True if the WebView should execute javascript.
     */
    public synchronized void setJavaScriptEnabled(boolean flag) {
        if (mJavaScriptEnabled != flag) {
            mJavaScriptEnabled = flag;
            postSync();
        }
    }

    /**
     * Tell the WebView to use Skia's hardware accelerated rendering path
     * @param flag True if the WebView should use Skia's hw-accel path
     * @hide
     */
    public synchronized void setHardwareAccelSkiaEnabled(boolean flag) {
        if (mHardwareAccelSkia != flag) {
            mHardwareAccelSkia = flag;
            postSync();
        }
    }

    /**
     * @return True if the WebView is using hardware accelerated skia
     * @hide
     */
    public synchronized boolean getHardwareAccelSkiaEnabled() {
        return mHardwareAccelSkia;
    }

    /**
     * Tell the WebView to show the visual indicator
     * @param flag True if the WebView should show the visual indicator
     * @hide
     */
    public synchronized void setShowVisualIndicator(boolean flag) {
        if (mShowVisualIndicator != flag) {
            mShowVisualIndicator = flag;
            postSync();
        }
    }

    /**
     * @return True if the WebView is showing the visual indicator
     * @hide
     */
    public synchronized boolean getShowVisualIndicator() {
        return mShowVisualIndicator;
    }

    /**
     * Tell the WebView to enable plugins.
     * @param flag True if the WebView should load plugins.
>>>>>>> upstream/master
     * @deprecated This method has been deprecated in favor of
     *             {@link #setPluginState}
     */
    @Deprecated
    public synchronized void setPluginsEnabled(boolean flag) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to enable, disable, or have plugins on demand. On
     * demand mode means that if a plugin exists that can handle the embedded
     * content, a placeholder icon will be shown instead of the plugin. When
     * the placeholder is clicked, the plugin will be enabled.
     *
     * @param state a PluginState value
     */
    public synchronized void setPluginState(PluginState state) {
        throw new MustOverrideException();
    }

    /**
     * Sets a custom path to plugins used by the WebView. This method is
     * obsolete since each plugin is now loaded from its own package.
     *
     * @param pluginsPath a String path to the directory containing plugins
     * @deprecated This method is no longer used as plugins are loaded from
     *             their own APK via the system's package manager.
     */
    @Deprecated
    public synchronized void setPluginsPath(String pluginsPath) {
        // Specified to do nothing, so no need for derived classes to override.
    }

    /**
     * Sets the path to where database storage API databases should be saved.
     * Note that the WebCore Database Tracker only allows the path to be set once.
     *
     * @param databasePath a String path to the directory where databases should
     *                     be saved. May be the empty string but should never
     *                     be null.
     */
    // This will update WebCore when the Sync runs in the C++ side.
    public synchronized void setDatabasePath(String databasePath) {
        throw new MustOverrideException();
    }

    /**
     * Sets the path where the Geolocation permissions database should be saved.
     *
     * @param databasePath a String path to the directory where the Geolocation
     *                     permissions database should be saved. May be the
     *                     empty string but should never be null.
     */
    // This will update WebCore when the Sync runs in the C++ side.
    public synchronized void setGeolocationDatabasePath(String databasePath) {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to enable Application Caches API.
     *
     * @param flag true if the WebView should enable Application Caches
     */
    public synchronized void setAppCacheEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets a custom path to the Application Caches files. The client
     * must ensure it exists before this call.
     *
     * @param appCachePath a String path to the directory containing
     *                     Application Caches files. The appCache path can be
     *                     the empty string but should not be null. Passing
     *                     null for this parameter will result in a no-op.
     */
    public synchronized void setAppCachePath(String appCachePath) {
        throw new MustOverrideException();
    }

    /**
     * Sets the maximum size for the Application Caches content.
     *
     * @param appCacheMaxSize the maximum size in bytes
     */
    public synchronized void setAppCacheMaxSize(long appCacheMaxSize) {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the database storage API is enabled.
     *
     * @param flag true if the WebView should use the database storage API
     */
    public synchronized void setDatabaseEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the DOM storage API is enabled.
     *
     * @param flag true if the WebView should use the DOM storage API
     */
    public synchronized void setDomStorageEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the DOM Storage APIs are enabled.
     *
     * @return true if the DOM Storage APIs are enabled
     */
    public synchronized boolean getDomStorageEnabled() {
        throw new MustOverrideException();
    }
    /**
     * Gets the path to where database storage API databases are saved for
     * the current WebView.
     *
     * @return the String path to the database storage API databases
     */
    public synchronized String getDatabasePath() {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the database storage API is enabled.
     *
     * @return true if the database storage API is enabled
     */
    public synchronized boolean getDatabaseEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether Geolocation is enabled.
     *
     * @param flag whether Geolocation should be enabled
     */
    public synchronized void setGeolocationEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether JavaScript is enabled.
     *
     * @return true if JavaScript is enabled
     * @see #setJavaScriptEnabled
     */
    public synchronized boolean getJavaScriptEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Gets whether JavaScript running in the context of a file scheme URL can
     * access content from any origin. This includes access to content from
     * other file scheme URLs.
     *
     * @return whether JavaScript running in the context of a file scheme URL
     *         can access content from any origin
     * @see #setAllowUniversalAccessFromFileURLs
     */
    public abstract boolean getAllowUniversalAccessFromFileURLs();

    /**
     * Gets whether JavaScript running in the context of a file scheme URL can
     * access content from other file scheme URLs.
     *
     * @return whether JavaScript running in the context of a file scheme URL
     *         can access content from other file scheme URLs
     * @see #setAllowFileAccessFromFileURLs
     */
    public abstract boolean getAllowFileAccessFromFileURLs();

    /**
     * Gets whether plugins are enabled.
     *
     * @return true if plugins are enabled
=======
        setPluginState(flag ? PluginState.ON : PluginState.OFF);
    }

    /**
     * Tell the WebView to enable, disable, or have plugins on demand. On
     * demand mode means that if a plugin exists that can handle the embedded
     * content, a placeholder icon will be shown instead of the plugin. When
     * the placeholder is clicked, the plugin will be enabled.
     * @param state One of the PluginState values.
     */
    public synchronized void setPluginState(PluginState state) {
        if (mPluginState != state) {
            mPluginState = state;
            postSync();
        }
    }

    /**
     * Set a custom path to plugins used by the WebView. This method is
     * obsolete since each plugin is now loaded from its own package.
     * @param pluginsPath String path to the directory containing plugins.
     * @deprecated This method is no longer used as plugins are loaded from
     * their own APK via the system's package manager.
     */
    @Deprecated
    public synchronized void setPluginsPath(String pluginsPath) {
    }

    /**
     * Set the path to where database storage API databases should be saved.
     * Nota that the WebCore Database Tracker only allows the path to be set once.
     * This will update WebCore when the Sync runs in the C++ side.
     * @param databasePath String path to the directory where databases should
     *     be saved. May be the empty string but should never be null.
     */
    public synchronized void setDatabasePath(String databasePath) {
        if (databasePath != null && !mDatabasePathHasBeenSet) {
            mDatabasePath = databasePath;
            mDatabasePathHasBeenSet = true;
            postSync();
        }
    }

    /**
     * Set the path where the Geolocation permissions database should be saved.
     * This will update WebCore when the Sync runs in the C++ side.
     * @param databasePath String path to the directory where the Geolocation
     *     permissions database should be saved. May be the empty string but
     *     should never be null.
     */
    public synchronized void setGeolocationDatabasePath(String databasePath) {
        if (databasePath != null
                && !databasePath.equals(mGeolocationDatabasePath)) {
            mGeolocationDatabasePath = databasePath;
            postSync();
        }
    }

    /**
     * Tell the WebView to enable Application Caches API.
     * @param flag True if the WebView should enable Application Caches.
     */
    public synchronized void setAppCacheEnabled(boolean flag) {
        if (mAppCacheEnabled != flag) {
            mAppCacheEnabled = flag;
            postSync();
        }
    }

    /**
     * Set a custom path to the Application Caches files. The client
     * must ensure it exists before this call.
     * @param appCachePath String path to the directory containing Application
     * Caches files. The appCache path can be the empty string but should not
     * be null. Passing null for this parameter will result in a no-op.
     */
    public synchronized void setAppCachePath(String appCachePath) {
        if (appCachePath != null && !appCachePath.equals(mAppCachePath)) {
            mAppCachePath = appCachePath;
            postSync();
        }
    }

    /**
     * Set the maximum size for the Application Caches content.
     * @param appCacheMaxSize the maximum size in bytes.
     */
    public synchronized void setAppCacheMaxSize(long appCacheMaxSize) {
        if (appCacheMaxSize != mAppCacheMaxSize) {
            mAppCacheMaxSize = appCacheMaxSize;
            postSync();
        }
    }

    /**
     * Set whether the database storage API is enabled.
     * @param flag boolean True if the WebView should use the database storage
     *     API.
     */
    public synchronized void setDatabaseEnabled(boolean flag) {
       if (mDatabaseEnabled != flag) {
           mDatabaseEnabled = flag;
           postSync();
       }
    }

    /**
     * Set whether the DOM storage API is enabled.
     * @param flag boolean True if the WebView should use the DOM storage
     *     API.
     */
    public synchronized void setDomStorageEnabled(boolean flag) {
       if (mDomStorageEnabled != flag) {
           mDomStorageEnabled = flag;
           postSync();
       }
    }

    /**
     * Returns true if the DOM Storage API's are enabled.
     * @return True if the DOM Storage API's are enabled.
     */
    public synchronized boolean getDomStorageEnabled() {
       return mDomStorageEnabled;
    }

    /**
     * Return the path to where database storage API databases are saved for
     * the current WebView.
     * @return the String path to the database storage API databases.
     */
    public synchronized String getDatabasePath() {
        return mDatabasePath;
    }

    /**
     * Returns true if database storage API is enabled.
     * @return True if the database storage API is enabled.
     */
    public synchronized boolean getDatabaseEnabled() {
        return mDatabaseEnabled;
    }

    /**
     * Tell the WebView to enable WebWorkers API.
     * @param flag True if the WebView should enable WebWorkers.
     * Note that this flag only affects V8. JSC does not have
     * an equivalent setting.
     * @hide pending api council approval
     */
    public synchronized void setWorkersEnabled(boolean flag) {
        if (mWorkersEnabled != flag) {
            mWorkersEnabled = flag;
            postSync();
        }
    }

    /**
     * Sets whether Geolocation is enabled.
     * @param flag Whether Geolocation should be enabled.
     */
    public synchronized void setGeolocationEnabled(boolean flag) {
        if (mGeolocationEnabled != flag) {
            mGeolocationEnabled = flag;
            postSync();
        }
    }

    /**
     * Sets whether XSS Auditor is enabled.
     * @param flag Whether XSS Auditor should be enabled.
     * @hide Only used by LayoutTestController.
     */
    public synchronized void setXSSAuditorEnabled(boolean flag) {
        if (mXSSAuditorEnabled != flag) {
            mXSSAuditorEnabled = flag;
            postSync();
        }
    }

    /**
     * Return true if javascript is enabled. <b>Note: The default is false.</b>
     * @return True if javascript is enabled.
     */
    public synchronized boolean getJavaScriptEnabled() {
        return mJavaScriptEnabled;
    }

    /**
     * Return true if plugins are enabled.
     * @return True if plugins are enabled.
>>>>>>> upstream/master
     * @deprecated This method has been replaced by {@link #getPluginState}
     */
    @Deprecated
    public synchronized boolean getPluginsEnabled() {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets the current plugin state.
     *
     * @return a value corresponding to the enum PluginState
     */
    public synchronized PluginState getPluginState() {
        throw new MustOverrideException();
    }

    /**
     * Gets the directory that contains the plugin libraries. This method is
     * obsolete since each plugin is now loaded from its own package.
     *
     * @return an empty string
=======
        return mPluginState == PluginState.ON;
    }

    /**
     * Return the current plugin state.
     * @return A value corresponding to the enum PluginState.
     */
    public synchronized PluginState getPluginState() {
        return mPluginState;
    }

    /**
     * Returns the directory that contains the plugin libraries. This method is
     * obsolete since each plugin is now loaded from its own package.
     * @return An empty string.
>>>>>>> upstream/master
     * @deprecated This method is no longer used as plugins are loaded from
     * their own APK via the system's package manager.
     */
    @Deprecated
    public synchronized String getPluginsPath() {
<<<<<<< HEAD
        // Unconditionally returns empty string, so no need for derived classes to override.
=======
>>>>>>> upstream/master
        return "";
    }

    /**
<<<<<<< HEAD
     * Tells JavaScript to open windows automatically. This applies to the
     * JavaScript function window.open(). The default is false.
     *
     * @param flag true if JavaScript can open windows automatically
     */
    public synchronized void setJavaScriptCanOpenWindowsAutomatically(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether JavaScript can open windows automatically.
     *
     * @return true if JavaScript can open windows automatically during
     *         window.open()
     * @see #setJavaScriptCanOpenWindowsAutomatically
     */
    public synchronized boolean getJavaScriptCanOpenWindowsAutomatically() {
        throw new MustOverrideException();
    }
    /**
     * Sets the default text encoding name to use when decoding html pages.
     * The default is "Latin-1".
     *
     * @param encoding the text encoding name
     */
    public synchronized void setDefaultTextEncodingName(String encoding) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default text encoding name.
     *
     * @return the default text encoding name as a string
     * @see #setDefaultTextEncodingName
     */
    public synchronized String getDefaultTextEncodingName() {
        throw new MustOverrideException();
    }

    /**
     * Sets the WebView's user-agent string. If the string "ua" is null or empty,
     * it will use the system default user-agent string.
     */
    public synchronized void setUserAgentString(String ua) {
        throw new MustOverrideException();
    }

    /**
     * Gets the WebView's user-agent string.
     */
    public synchronized String getUserAgentString() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView whether it needs to set a node to have focus when
     * {@link WebView#requestFocus(int, android.graphics.Rect)} is called.
     *
     * @param flag whether the WebView needs to set a node
     */
    public void setNeedInitialFocus(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets the priority of the Render thread. Unlike the other settings, this
     * one only needs to be called once per process. The default is NORMAL.
     *
     * @param priority a RenderPriority
     */
    public synchronized void setRenderPriority(RenderPriority priority) {
        throw new MustOverrideException();
    }

    /**
     * Overrides the way the cache is used. The way the cache is used is based
=======
     * Tell javascript to open windows automatically. This applies to the
     * javascript function window.open().
     * @param flag True if javascript can open windows automatically.
     */
    public synchronized void setJavaScriptCanOpenWindowsAutomatically(
            boolean flag) {
        if (mJavaScriptCanOpenWindowsAutomatically != flag) {
            mJavaScriptCanOpenWindowsAutomatically = flag;
            postSync();
        }
    }

    /**
     * Return true if javascript can open windows automatically. The default
     * is false.
     * @return True if javascript can open windows automatically during
     *         window.open().
     */
    public synchronized boolean getJavaScriptCanOpenWindowsAutomatically() {
        return mJavaScriptCanOpenWindowsAutomatically;
    }

    /**
     * Set the default text encoding name to use when decoding html pages.
     * @param encoding The text encoding name.
     */
    public synchronized void setDefaultTextEncodingName(String encoding) {
        if (encoding != null && !encoding.equals(mDefaultTextEncoding)) {
            mDefaultTextEncoding = encoding;
            postSync();
        }
    }

    /**
     * Get the default text encoding name. The default is "Latin-1".
     * @return The default text encoding name as a string.
     */
    public synchronized String getDefaultTextEncodingName() {
        return mDefaultTextEncoding;
    }

    /**
     * Set the WebView's user-agent string. If the string "ua" is null or empty,
     * it will use the system default user-agent string.
     */
    public synchronized void setUserAgentString(String ua) {
        if (ua == null || ua.length() == 0) {
            synchronized(sLockForLocaleSettings) {
                Locale currentLocale = Locale.getDefault();
                if (!sLocale.equals(currentLocale)) {
                    sLocale = currentLocale;
                    mAcceptLanguage = getCurrentAcceptLanguage();
                }
            }
            ua = getCurrentUserAgent();
            mUseDefaultUserAgent = true;
        } else  {
            mUseDefaultUserAgent = false;
        }

        if (!ua.equals(mUserAgent)) {
            mUserAgent = ua;
            postSync();
        }
    }

    /**
     * Return the WebView's user-agent string.
     */
    public synchronized String getUserAgentString() {
        if (DESKTOP_USERAGENT.equals(mUserAgent) ||
                IPHONE_USERAGENT.equals(mUserAgent) ||
                !mUseDefaultUserAgent) {
            return mUserAgent;
        }

        boolean doPostSync = false;
        synchronized(sLockForLocaleSettings) {
            Locale currentLocale = Locale.getDefault();
            if (!sLocale.equals(currentLocale)) {
                sLocale = currentLocale;
                mUserAgent = getCurrentUserAgent();
                mAcceptLanguage = getCurrentAcceptLanguage();
                doPostSync = true;
            }
        }
        if (doPostSync) {
            postSync();
        }
        return mUserAgent;
    }

    /* package api to grab the Accept Language string. */
    /*package*/ synchronized String getAcceptLanguage() {
        synchronized(sLockForLocaleSettings) {
            Locale currentLocale = Locale.getDefault();
            if (!sLocale.equals(currentLocale)) {
                sLocale = currentLocale;
                mAcceptLanguage = getCurrentAcceptLanguage();
            }
        }
        return mAcceptLanguage;
    }

    /* package */ boolean isNarrowColumnLayout() {
        return getLayoutAlgorithm() == LayoutAlgorithm.NARROW_COLUMNS;
    }

    /**
     * Tell the WebView whether it needs to set a node to have focus when
     * {@link WebView#requestFocus(int, android.graphics.Rect)} is called.
     *
     * @param flag
     */
    public void setNeedInitialFocus(boolean flag) {
        if (mNeedInitialFocus != flag) {
            mNeedInitialFocus = flag;
        }
    }

    /* Package api to get the choice whether it needs to set initial focus. */
    /* package */ boolean getNeedInitialFocus() {
        return mNeedInitialFocus;
    }

    /**
     * Set the priority of the Render thread. Unlike the other settings, this
     * one only needs to be called once per process. The default is NORMAL.
     *
     * @param priority RenderPriority, can be normal, high or low.
     */
    public synchronized void setRenderPriority(RenderPriority priority) {
        if (mRenderPriority != priority) {
            mRenderPriority = priority;
            mEventHandler.sendMessage(Message.obtain(null,
                    EventHandler.PRIORITY));
        }
    }

    /**
     * Override the way the cache is used. The way the cache is used is based
>>>>>>> upstream/master
     * on the navigation option. For a normal page load, the cache is checked
     * and content is re-validated as needed. When navigating back, content is
     * not revalidated, instead the content is just pulled from the cache.
     * This function allows the client to override this behavior.
<<<<<<< HEAD
     *
     * @param mode one of the LOAD_ values
     */
    public void setCacheMode(int mode) {
        throw new MustOverrideException();
    }

    /**
     * Gets the current setting for overriding the cache mode. For a full
     * description, see the {@link #setCacheMode(int)} function.
     */
    public int getCacheMode() {
        throw new MustOverrideException();
    }
=======
     * @param mode One of the LOAD_ values.
     */
    public void setCacheMode(int mode) {
        if (mode != mOverrideCacheMode) {
            mOverrideCacheMode = mode;
            postSync();
        }
    }

    /**
     * Return the current setting for overriding the cache mode. For a full
     * description, see the {@link #setCacheMode(int)} function.
     */
    public int getCacheMode() {
        return mOverrideCacheMode;
    }

    /**
     * If set, webkit alternately shrinks and expands images viewed outside
     * of an HTML page to fit the screen. This conflicts with attempts by
     * the UI to zoom in and out of an image, so it is set false by default.
     * @param shrink Set true to let webkit shrink the standalone image to fit.
     * {@hide}
     */
    public void setShrinksStandaloneImagesToFit(boolean shrink) {
        if (mShrinksStandaloneImagesToFit != shrink) {
            mShrinksStandaloneImagesToFit = shrink;
            postSync();
        }
     }

    /**
     * Specify the maximum decoded image size. The default is
     * 2 megs for small memory devices and 8 megs for large memory devices.
     * @param size The maximum decoded size, or zero to set to the default.
     * @hide pending api council approval
     */
    public void setMaximumDecodedImageSize(long size) {
        if (mMaximumDecodedImageSize != size) {
            mMaximumDecodedImageSize = size;
            postSync();
        }
    }

    /**
     * Returns whether to use fixed viewport.  Use fixed viewport
     * whenever wide viewport is on.
     */
    /* package */ boolean getUseFixedViewport() {
        return getUseWideViewPort();
    }

    /**
     * Returns whether private browsing is enabled.
     */
    /* package */ boolean isPrivateBrowsingEnabled() {
        return mPrivateBrowsingEnabled;
    }

    /**
     * Sets whether private browsing is enabled.
     * @param flag Whether private browsing should be enabled.
     */
    /* package */ synchronized void setPrivateBrowsingEnabled(boolean flag) {
        if (mPrivateBrowsingEnabled != flag) {
            mPrivateBrowsingEnabled = flag;

            // AutoFill is dependant on private browsing being enabled so
            // reset it to take account of the new value of mPrivateBrowsingEnabled.
            setAutoFillEnabled(mAutoFillEnabled);

            postSync();
        }
    }

    /**
     * Returns whether the viewport metatag can disable zooming
     * @hide
     */
    public boolean forceUserScalable() {
        return mForceUserScalable;
    }

    /**
     * Sets whether viewport metatag can disable zooming.
     * @param flag Whether or not to forceably enable user scalable.
     * @hide
     */
    public synchronized void setForceUserScalable(boolean flag) {
        mForceUserScalable = flag;
    }

    synchronized void setSyntheticLinksEnabled(boolean flag) {
        if (mSyntheticLinksEnabled != flag) {
            mSyntheticLinksEnabled = flag;
            postSync();
        }
    }

    /**
     * @hide
     */
    public synchronized void setAutoFillEnabled(boolean enabled) {
        // AutoFill is always disabled in private browsing mode.
        boolean autoFillEnabled = enabled && !mPrivateBrowsingEnabled;
        if (mAutoFillEnabled != autoFillEnabled) {
            mAutoFillEnabled = autoFillEnabled;
            postSync();
        }
    }

    /**
     * @hide
     */
    public synchronized boolean getAutoFillEnabled() {
        return mAutoFillEnabled;
    }

    /**
     * @hide
     */
    public synchronized void setAutoFillProfile(AutoFillProfile profile) {
        if (mAutoFillProfile != profile) {
            mAutoFillProfile = profile;
            postSync();
        }
    }

    /**
     * @hide
     */
    public synchronized AutoFillProfile getAutoFillProfile() {
        return mAutoFillProfile;
    }

    int getDoubleTapToastCount() {
        return mDoubleTapToastCount;
    }

    void setDoubleTapToastCount(int count) {
        if (mDoubleTapToastCount != count) {
            mDoubleTapToastCount = count;
            // write the settings in the non-UI thread
            mEventHandler.sendMessage(Message.obtain(null,
                    EventHandler.SET_DOUBLE_TAP_TOAST_COUNT));
        }
    }

    /**
     * @hide
     */
    public void setProperty(String key, String value) {
        if (mWebView.nativeSetProperty(key, value)) {
            mWebView.contentInvalidateAll();
        }
    }

    /**
     * @hide
     */
    public String getProperty(String key) {
        return mWebView.nativeGetProperty(key);
    }

    /**
     * Transfer messages from the queue to the new WebCoreThread. Called from
     * WebCore thread.
     */
    /*package*/
    synchronized void syncSettingsAndCreateHandler(BrowserFrame frame) {
        mBrowserFrame = frame;
        if (DebugFlags.WEB_SETTINGS) {
            junit.framework.Assert.assertTrue(frame.mNativeFrame != 0);
        }

        SharedPreferences sp = mContext.getSharedPreferences(PREF_FILE,
                Context.MODE_PRIVATE);
        if (mDoubleTapToastCount > 0) {
            mDoubleTapToastCount = sp.getInt(DOUBLE_TAP_TOAST_COUNT,
                    mDoubleTapToastCount);
        }
        nativeSync(frame.mNativeFrame);
        mSyncPending = false;
        mEventHandler.createHandler();
    }

    /**
     * Let the Settings object know that our owner is being destroyed.
     */
    /*package*/
    synchronized void onDestroyed() {
    }

    private int pin(int size) {
        // FIXME: 72 is just an arbitrary max text size value.
        if (size < 1) {
            return 1;
        } else if (size > 72) {
            return 72;
        }
        return size;
    }

    /* Post a SYNC message to handle syncing the native settings. */
    private synchronized void postSync() {
        // Only post if a sync is not pending
        if (!mSyncPending) {
            mSyncPending = mEventHandler.sendMessage(
                    Message.obtain(null, EventHandler.SYNC));
        }
    }

    // Synchronize the native and java settings.
    private native void nativeSync(int nativeFrame);
>>>>>>> upstream/master
}
