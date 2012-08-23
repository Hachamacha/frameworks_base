/*
<<<<<<< HEAD
 * Copyright (C) 2012 The Android Open Source Project
=======
 * Copyright (C) 2010 The Android Open Source Project
>>>>>>> upstream/master
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
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebViewCore.EventHub;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles injecting accessibility JavaScript and related JavaScript -> Java
 * APIs.
 */
class AccessibilityInjector {
    // The WebViewClassic this injector is responsible for managing.
    private final WebViewClassic mWebViewClassic;

    // Cached reference to mWebViewClassic.getContext(), for convenience.
    private final Context mContext;

    // Cached reference to mWebViewClassic.getWebView(), for convenience.
    private final WebView mWebView;

    // The Java objects that are exposed to JavaScript.
    private TextToSpeech mTextToSpeech;
    private CallbackHandler mCallback;

    // Lazily loaded helper objects.
    private AccessibilityManager mAccessibilityManager;
    private AccessibilityInjectorFallback mAccessibilityInjectorFallback;
    private JSONObject mAccessibilityJSONObject;

    // Whether the accessibility script has been injected into the current page.
    private boolean mAccessibilityScriptInjected;

    // Constants for determining script injection strategy.
    private static final int ACCESSIBILITY_SCRIPT_INJECTION_UNDEFINED = -1;
    private static final int ACCESSIBILITY_SCRIPT_INJECTION_OPTED_OUT = 0;
    @SuppressWarnings("unused")
    private static final int ACCESSIBILITY_SCRIPT_INJECTION_PROVIDED = 1;

    // Alias for TTS API exposed to JavaScript.
    private static final String ALIAS_TTS_JS_INTERFACE = "accessibility";

    // Alias for traversal callback exposed to JavaScript.
    private static final String ALIAS_TRAVERSAL_JS_INTERFACE = "accessibilityTraversal";

    // Template for JavaScript that injects a screen-reader.
    private static final String ACCESSIBILITY_SCREEN_READER_JAVASCRIPT_TEMPLATE =
            "javascript:(function() {" +
                    "    var chooser = document.createElement('script');" +
                    "    chooser.type = 'text/javascript';" +
                    "    chooser.src = '%1s';" +
                    "    document.getElementsByTagName('head')[0].appendChild(chooser);" +
                    "  })();";

    // Template for JavaScript that performs AndroidVox actions.
    private static final String ACCESSIBILITY_ANDROIDVOX_TEMPLATE =
            "cvox.AndroidVox.performAction('%1s')";

    /**
     * Creates an instance of the AccessibilityInjector based on
     * {@code webViewClassic}.
     *
     * @param webViewClassic The WebViewClassic that this AccessibilityInjector
     *            manages.
     */
    public AccessibilityInjector(WebViewClassic webViewClassic) {
        mWebViewClassic = webViewClassic;
        mWebView = webViewClassic.getWebView();
        mContext = webViewClassic.getContext();
        mAccessibilityManager = AccessibilityManager.getInstance(mContext);
    }

    /**
     * Attempts to load scripting interfaces for accessibility.
     * <p>
     * This should be called when the window is attached.
     * </p>
     */
    public void addAccessibilityApisIfNecessary() {
        if (!isAccessibilityEnabled() || !isJavaScriptEnabled()) {
            return;
        }

        addTtsApis();
        addCallbackApis();
    }

    /**
     * Attempts to unload scripting interfaces for accessibility.
     * <p>
     * This should be called when the window is detached.
     * </p>
     */
    public void removeAccessibilityApisIfNecessary() {
        removeTtsApis();
        removeCallbackApis();
    }

    /**
     * Initializes an {@link AccessibilityNodeInfo} with the actions and
     * movement granularity levels supported by this
     * {@link AccessibilityInjector}.
     * <p>
     * If an action identifier is added in this method, this
     * {@link AccessibilityInjector} should also return {@code true} from
     * {@link #supportsAccessibilityAction(int)}.
     * </p>
     *
     * @param info The info to initialize.
     * @see View#onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo)
     */
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        info.setMovementGranularities(AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER
                | AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD
                | AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE
                | AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH
                | AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE);
        info.addAction(AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        info.addAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
        info.addAction(AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT);
        info.addAction(AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT);
        info.addAction(AccessibilityNodeInfo.ACTION_CLICK);
        info.setClickable(true);
    }

    /**
     * Returns {@code true} if this {@link AccessibilityInjector} should handle
     * the specified action.
     *
     * @param action An accessibility action identifier.
     * @return {@code true} if this {@link AccessibilityInjector} should handle
     *         the specified action.
     */
    public boolean supportsAccessibilityAction(int action) {
        switch (action) {
            case AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY:
            case AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY:
            case AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT:
            case AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT:
            case AccessibilityNodeInfo.ACTION_CLICK:
                return true;
            default:
                return false;
        }
    }

    /**
     * Performs the specified accessibility action.
     *
     * @param action The identifier of the action to perform.
     * @param arguments The action arguments, or {@code null} if no arguments.
     * @return {@code true} if the action was successful.
     * @see View#performAccessibilityAction(int, Bundle)
     */
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (!isAccessibilityEnabled()) {
            mAccessibilityScriptInjected = false;
            toggleFallbackAccessibilityInjector(false);
            return false;
        }

        if (mAccessibilityScriptInjected) {
            return sendActionToAndroidVox(action, arguments);
        }
        
        if (mAccessibilityInjectorFallback != null) {
            return mAccessibilityInjectorFallback.performAccessibilityAction(action, arguments);
        }

        return false;
    }

    /**
     * Attempts to handle key events when accessibility is turned on.
     *
     * @param event The key event to handle.
     * @return {@code true} if the event was handled.
     */
    public boolean handleKeyEventIfNecessary(KeyEvent event) {
        if (!isAccessibilityEnabled()) {
            mAccessibilityScriptInjected = false;
            toggleFallbackAccessibilityInjector(false);
            return false;
        }

        if (mAccessibilityScriptInjected) {
            // if an accessibility script is injected we delegate to it the key
            // handling. this script is a screen reader which is a fully fledged
            // solution for blind users to navigate in and interact with web
            // pages.
            if (event.getAction() == KeyEvent.ACTION_UP) {
                mWebViewClassic.sendBatchableInputMessage(EventHub.KEY_UP, 0, 0, event);
            } else if (event.getAction() == KeyEvent.ACTION_DOWN) {
                mWebViewClassic.sendBatchableInputMessage(EventHub.KEY_DOWN, 0, 0, event);
            } else {
                return false;
            }

            return true;
        }

        if (mAccessibilityInjectorFallback != null) {
            // if an accessibility injector is present (no JavaScript enabled or
            // the site opts out injecting our JavaScript screen reader) we let
            // it decide whether to act on and consume the event.
            return mAccessibilityInjectorFallback.onKeyEvent(event);
        }

        return false;
    }

    /**
     * Attempts to handle selection change events when accessibility is using a
     * non-JavaScript method.
     *
     * @param selectionString The selection string.
     */
    public void handleSelectionChangedIfNecessary(String selectionString) {
        if (mAccessibilityInjectorFallback != null) {
            mAccessibilityInjectorFallback.onSelectionStringChange(selectionString);
=======
import android.provider.Settings;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.webkit.WebViewCore.EventHub;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class injects accessibility into WebViews with disabled JavaScript or
 * WebViews with enabled JavaScript but for which we have no accessibility
 * script to inject.
 * </p>
 * Note: To avoid changes in the framework upon changing the available
 *       navigation axis, or reordering the navigation axis, or changing
 *       the key bindings, or defining sequence of actions to be bound to
 *       a given key this class is navigation axis agnostic. It is only
 *       aware of one navigation axis which is in fact the default behavior
 *       of webViews while using the DPAD/TrackBall.
 * </p>
 * In general a key binding is a mapping from modifiers + key code to
 * a sequence of actions. For more detail how to specify key bindings refer to
 * {@link android.provider.Settings.Secure#ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS}.
 * </p>
 * The possible actions are invocations to
 * {@link #setCurrentAxis(int, boolean, String)}, or
 * {@link #traverseCurrentAxis(int, boolean, String)}
 * {@link #traverseGivenAxis(int, int, boolean, String)}
 * {@link #prefromAxisTransition(int, int, boolean, String)}
 * referred via the values of:
 * {@link #ACTION_SET_CURRENT_AXIS},
 * {@link #ACTION_TRAVERSE_CURRENT_AXIS},
 * {@link #ACTION_TRAVERSE_GIVEN_AXIS},
 * {@link #ACTION_PERFORM_AXIS_TRANSITION},
 * respectively.
 * The arguments for the action invocation are specified as offset
 * hexademical pairs. Note the last argument of the invocation
 * should NOT be specified in the binding as it is provided by
 * this class. For details about the key binding implementation
 * refer to {@link AccessibilityWebContentKeyBinding}.
 */
class AccessibilityInjector {
    private static final String LOG_TAG = "AccessibilityInjector";

    private static final boolean DEBUG = true;

    private static final int ACTION_SET_CURRENT_AXIS = 0;
    private static final int ACTION_TRAVERSE_CURRENT_AXIS = 1;
    private static final int ACTION_TRAVERSE_GIVEN_AXIS = 2;
    private static final int ACTION_PERFORM_AXIS_TRANSITION = 3;
    private static final int ACTION_TRAVERSE_DEFAULT_WEB_VIEW_BEHAVIOR_AXIS = 4;

    // the default WebView behavior abstracted as a navigation axis
    private static final int NAVIGATION_AXIS_DEFAULT_WEB_VIEW_BEHAVIOR = 7;

    // these are the same for all instances so make them process wide
    private static ArrayList<AccessibilityWebContentKeyBinding> sBindings =
        new ArrayList<AccessibilityWebContentKeyBinding>();

    // handle to the WebView this injector is associated with.
    private final WebView mWebView;

    // events scheduled for sending as soon as we receive the selected text
    private final Stack<AccessibilityEvent> mScheduledEventStack = new Stack<AccessibilityEvent>();

    // the current traversal axis
    private int mCurrentAxis = 2; // sentence

    // we need to consume the up if we have handled the last down
    private boolean mLastDownEventHandled;

    // getting two empty selection strings in a row we let the WebView handle the event
    private boolean mIsLastSelectionStringNull;

    // keep track of last direction
    private int mLastDirection;

    /**
     * Creates a new injector associated with a given {@link WebView}.
     *
     * @param webView The associated WebView.
     */
    public AccessibilityInjector(WebView webView) {
        mWebView = webView;
        ensureWebContentKeyBindings();
    }

    /**
     * Processes a key down <code>event</code>.
     *
     * @return True if the event was processed.
     */
    public boolean onKeyEvent(KeyEvent event) {
        // We do not handle ENTER in any circumstances.
        if (isEnterActionKey(event.getKeyCode())) {
            return false;
        }

        if (event.getAction() == KeyEvent.ACTION_UP) {
            return mLastDownEventHandled;
        }

        mLastDownEventHandled = false;

        AccessibilityWebContentKeyBinding binding = null;
        for (AccessibilityWebContentKeyBinding candidate : sBindings) {
            if (event.getKeyCode() == candidate.getKeyCode()
                    && event.hasModifiers(candidate.getModifiers())) {
                binding = candidate;
                break;
            }
        }

        if (binding == null) {
            return false;
        }

        for (int i = 0, count = binding.getActionCount(); i < count; i++) {
            int actionCode = binding.getActionCode(i);
            String contentDescription = Integer.toHexString(binding.getAction(i));
            switch (actionCode) {
                case ACTION_SET_CURRENT_AXIS:
                    int axis = binding.getFirstArgument(i);
                    boolean sendEvent = (binding.getSecondArgument(i) == 1);
                    setCurrentAxis(axis, sendEvent, contentDescription);
                    mLastDownEventHandled = true;
                    break;
                case ACTION_TRAVERSE_CURRENT_AXIS:
                    int direction = binding.getFirstArgument(i);
                    // on second null selection string in same direction - WebView handles the event
                    if (direction == mLastDirection && mIsLastSelectionStringNull) {
                        mIsLastSelectionStringNull = false;
                        return false;
                    }
                    mLastDirection = direction;
                    sendEvent = (binding.getSecondArgument(i) == 1);
                    mLastDownEventHandled = traverseCurrentAxis(direction, sendEvent,
                            contentDescription);
                    break;
                case ACTION_TRAVERSE_GIVEN_AXIS:
                    direction = binding.getFirstArgument(i);
                    // on second null selection string in same direction => WebView handle the event
                    if (direction == mLastDirection && mIsLastSelectionStringNull) {
                        mIsLastSelectionStringNull = false;
                        return false;
                    }
                    mLastDirection = direction;
                    axis =  binding.getSecondArgument(i);
                    sendEvent = (binding.getThirdArgument(i) == 1);
                    traverseGivenAxis(direction, axis, sendEvent, contentDescription);
                    mLastDownEventHandled = true;
                    break;
                case ACTION_PERFORM_AXIS_TRANSITION:
                    int fromAxis = binding.getFirstArgument(i);
                    int toAxis = binding.getSecondArgument(i);
                    sendEvent = (binding.getThirdArgument(i) == 1);
                    prefromAxisTransition(fromAxis, toAxis, sendEvent, contentDescription);
                    mLastDownEventHandled = true;
                    break;
                case ACTION_TRAVERSE_DEFAULT_WEB_VIEW_BEHAVIOR_AXIS:
                    // This is a special case since we treat the default WebView navigation
                    // behavior as one of the possible navigation axis the user can use.
                    // If we are not on the default WebView navigation axis this is NOP.
                    if (mCurrentAxis == NAVIGATION_AXIS_DEFAULT_WEB_VIEW_BEHAVIOR) {
                        // While WebVew handles navigation we do not get null selection
                        // strings so do not check for that here as the cases above.
                        mLastDirection = binding.getFirstArgument(i);
                        sendEvent = (binding.getSecondArgument(i) == 1);
                        traverseGivenAxis(mLastDirection, NAVIGATION_AXIS_DEFAULT_WEB_VIEW_BEHAVIOR,
                            sendEvent, contentDescription);
                        mLastDownEventHandled = false;
                    } else {
                        mLastDownEventHandled = true;
                    }
                    break;
                default:
                    Log.w(LOG_TAG, "Unknown action code: " + actionCode);
            }
        }

        return mLastDownEventHandled;
    }

    /**
     * Set the current navigation axis which will be used while
     * calling {@link #traverseCurrentAxis(int, boolean, String)}.
     *
     * @param axis The axis to set.
     * @param sendEvent Whether to send an accessibility event to
     *        announce the change.
     */
    private void setCurrentAxis(int axis, boolean sendEvent, String contentDescription) {
        mCurrentAxis = axis;
        if (sendEvent) {
            AccessibilityEvent event = getPartialyPopulatedAccessibilityEvent();
            event.getText().add(String.valueOf(axis));
            event.setContentDescription(contentDescription);
            sendAccessibilityEvent(event);
>>>>>>> upstream/master
        }
    }

    /**
<<<<<<< HEAD
     * Prepares for injecting accessibility scripts into a new page.
     *
     * @param url The URL that will be loaded.
     */
    public void onPageStarted(String url) {
        mAccessibilityScriptInjected = false;
    }

    /**
     * Attempts to inject the accessibility script using a {@code <script>} tag.
     * <p>
     * This should be called after a page has finished loading.
     * </p>
     *
     * @param url The URL that just finished loading.
     */
    public void onPageFinished(String url) {
        if (!isAccessibilityEnabled()) {
            mAccessibilityScriptInjected = false;
            toggleFallbackAccessibilityInjector(false);
            return;
        }

        if (!shouldInjectJavaScript(url)) {
            toggleFallbackAccessibilityInjector(true);
            return;
        }

        toggleFallbackAccessibilityInjector(false);

        final String injectionUrl = getScreenReaderInjectionUrl();
        mWebView.loadUrl(injectionUrl);

        mAccessibilityScriptInjected = true;
    }

    /**
     * Toggles the non-JavaScript method for handling accessibility.
     *
     * @param enabled {@code true} to enable the non-JavaScript method, or
     *            {@code false} to disable it.
     */
    private void toggleFallbackAccessibilityInjector(boolean enabled) {
        if (enabled && (mAccessibilityInjectorFallback == null)) {
            mAccessibilityInjectorFallback = new AccessibilityInjectorFallback(mWebViewClassic);
        } else {
            mAccessibilityInjectorFallback = null;
        }
    }

    /**
     * Determines whether it's okay to inject JavaScript into a given URL.
     *
     * @param url The URL to check.
     * @return {@code true} if JavaScript should be injected, {@code false} if a
     *         non-JavaScript method should be used.
     */
    private boolean shouldInjectJavaScript(String url) {
        // Respect the WebView's JavaScript setting.
        if (!isJavaScriptEnabled()) {
            return false;
        }

        // Allow the page to opt out of Accessibility script injection.
        if (getAxsUrlParameterValue(url) == ACCESSIBILITY_SCRIPT_INJECTION_OPTED_OUT) {
            return false;
        }

        // The user must explicitly enable Accessibility script injection.
        if (!isScriptInjectionEnabled()) {
            return false;
        }

=======
     * Performs conditional transition one axis to another.
     *
     * @param fromAxis The axis which must be the current for the transition to occur.
     * @param toAxis The axis to which to transition.
     * @param sendEvent Flag if to send an event to announce successful transition.
     * @param contentDescription A description of the performed action.
     */
    private void prefromAxisTransition(int fromAxis, int toAxis, boolean sendEvent,
            String contentDescription) {
        if (mCurrentAxis == fromAxis) {
            setCurrentAxis(toAxis, sendEvent, contentDescription);
        }
    }

    /**
     * Traverse the document along the current navigation axis.
     *
     * @param direction The direction of traversal.
     * @param sendEvent Whether to send an accessibility event to
     *        announce the change.
     * @param contentDescription A description of the performed action.
     * @see #setCurrentAxis(int, boolean, String)
     */
    private boolean traverseCurrentAxis(int direction, boolean sendEvent,
            String contentDescription) {
        return traverseGivenAxis(direction, mCurrentAxis, sendEvent, contentDescription);
    }

    /**
     * Traverse the document along the given navigation axis.
     *
     * @param direction The direction of traversal.
     * @param axis The axis along which to traverse.
     * @param sendEvent Whether to send an accessibility event to
     *        announce the change.
     * @param contentDescription A description of the performed action.
     */
    private boolean traverseGivenAxis(int direction, int axis, boolean sendEvent,
            String contentDescription) {
        WebViewCore webViewCore = mWebView.getWebViewCore();
        if (webViewCore == null) {
            return false;
        }

        AccessibilityEvent event = null;
        if (sendEvent) {
            event = getPartialyPopulatedAccessibilityEvent();
            // the text will be set upon receiving the selection string
            event.setContentDescription(contentDescription);
        }
        mScheduledEventStack.push(event);

        // if the axis is the default let WebView handle the event which will
        // result in cursor ring movement and selection of its content
        if (axis == NAVIGATION_AXIS_DEFAULT_WEB_VIEW_BEHAVIOR) {
            return false;
        }

        webViewCore.sendMessage(EventHub.MODIFY_SELECTION, direction, axis);
>>>>>>> upstream/master
        return true;
    }

    /**
<<<<<<< HEAD
     * @return {@code true} if the user has explicitly enabled Accessibility
     *         script injection.
     */
    private boolean isScriptInjectionEnabled() {
        final int injectionSetting = Settings.Secure.getInt(
                mContext.getContentResolver(), Settings.Secure.ACCESSIBILITY_SCRIPT_INJECTION, 0);
        return (injectionSetting == 1);
    }

    /**
     * Attempts to initialize and add interfaces for TTS, if that hasn't already
     * been done.
     */
    private void addTtsApis() {
        if (mTextToSpeech != null) {
            return;
        }

        final String pkgName = mContext.getPackageName();

        mTextToSpeech = new TextToSpeech(mContext, null, null, pkgName + ".**webview**", true);
        mWebView.addJavascriptInterface(mTextToSpeech, ALIAS_TTS_JS_INTERFACE);
    }

    /**
     * Attempts to shutdown and remove interfaces for TTS, if that hasn't
     * already been done.
     */
    private void removeTtsApis() {
        if (mTextToSpeech == null) {
            return;
        }

        mWebView.removeJavascriptInterface(ALIAS_TTS_JS_INTERFACE);
        mTextToSpeech.stop();
        mTextToSpeech.shutdown();
        mTextToSpeech = null;
    }

    private void addCallbackApis() {
        if (mCallback != null) {
            return;
        }

        mCallback = new CallbackHandler(ALIAS_TRAVERSAL_JS_INTERFACE);
        mWebView.addJavascriptInterface(mCallback, ALIAS_TRAVERSAL_JS_INTERFACE);
    }

    private void removeCallbackApis() {
        if (mCallback == null) {
            return;
        }

        mWebView.removeJavascriptInterface(ALIAS_TRAVERSAL_JS_INTERFACE);
        mCallback = null;
    }

    /**
     * Returns the script injection preference requested by the URL, or
     * {@link #ACCESSIBILITY_SCRIPT_INJECTION_UNDEFINED} if the page has no
     * preference.
     *
     * @param url The URL to check.
     * @return A script injection preference.
     */
    private int getAxsUrlParameterValue(String url) {
        if (url == null) {
            return ACCESSIBILITY_SCRIPT_INJECTION_UNDEFINED;
        }

        try {
            final List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), null);

            for (NameValuePair param : params) {
                if ("axs".equals(param.getName())) {
                    return verifyInjectionValue(param.getValue());
                }
            }
        } catch (URISyntaxException e) {
            // Do nothing.
        }

        return ACCESSIBILITY_SCRIPT_INJECTION_UNDEFINED;
    }

    private int verifyInjectionValue(String value) {
        try {
            final int parsed = Integer.parseInt(value);

            switch (parsed) {
                case ACCESSIBILITY_SCRIPT_INJECTION_OPTED_OUT:
                    return ACCESSIBILITY_SCRIPT_INJECTION_OPTED_OUT;
                case ACCESSIBILITY_SCRIPT_INJECTION_PROVIDED:
                    return ACCESSIBILITY_SCRIPT_INJECTION_PROVIDED;
            }
        } catch (NumberFormatException e) {
            // Do nothing.
        }

        return ACCESSIBILITY_SCRIPT_INJECTION_UNDEFINED;
    }

    /**
     * @return The URL for injecting the screen reader.
     */
    private String getScreenReaderInjectionUrl() {
        final String screenReaderUrl = Settings.Secure.getString(
                mContext.getContentResolver(), Settings.Secure.ACCESSIBILITY_SCREEN_READER_URL);
        return String.format(ACCESSIBILITY_SCREEN_READER_JAVASCRIPT_TEMPLATE, screenReaderUrl);
    }

    /**
     * @return {@code true} if JavaScript is enabled in the {@link WebView}
     *         settings.
     */
    private boolean isJavaScriptEnabled() {
        return mWebView.getSettings().getJavaScriptEnabled();
    }

    /**
     * @return {@code true} if accessibility is enabled.
     */
    private boolean isAccessibilityEnabled() {
        return mAccessibilityManager.isEnabled();
    }

    /**
     * Packs an accessibility action into a JSON object and sends it to AndroidVox.
     *
     * @param action The action identifier.
     * @param arguments The action arguments, if applicable.
     * @return The result of the action.
     */
    private boolean sendActionToAndroidVox(int action, Bundle arguments) {
        if (mAccessibilityJSONObject == null) {
            mAccessibilityJSONObject = new JSONObject();
        } else {
            // Remove all keys from the object.
            final Iterator<?> keys = mAccessibilityJSONObject.keys();
            while (keys.hasNext()) {
                keys.next();
                keys.remove();
            }
        }

        try {
            mAccessibilityJSONObject.accumulate("action", action);

            switch (action) {
                case AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY:
                case AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY:
                    if (arguments != null) {
                        final int granularity = arguments.getInt(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT);
                        mAccessibilityJSONObject.accumulate("granularity", granularity);
                    }
                    break;
                case AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT:
                case AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT:
                    if (arguments != null) {
                        final String element = arguments.getString(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING);
                        mAccessibilityJSONObject.accumulate("element", element);
                    }
                    break;
            }
        } catch (JSONException e) {
            return false;
        }

        final String jsonString = mAccessibilityJSONObject.toString();
        final String jsCode = String.format(ACCESSIBILITY_ANDROIDVOX_TEMPLATE, jsonString);
        return mCallback.performAction(mWebView, jsCode);
    }

    /**
     * Exposes result interface to JavaScript.
     */
    private static class CallbackHandler {
        private static final String JAVASCRIPT_ACTION_TEMPLATE =
                "javascript:(function() { %s.onResult(%d, %s); })();";

        // Time in milliseconds to wait for a result before failing.
        private static final long RESULT_TIMEOUT = 5000;

        private final AtomicInteger mResultIdCounter = new AtomicInteger();
        private final Object mResultLock = new Object();
        private final String mInterfaceName;

        private boolean mResult = false;
        private long mResultId = -1;

        private CallbackHandler(String interfaceName) {
            mInterfaceName = interfaceName;
        }

        /**
         * Performs an action and attempts to wait for a result.
         *
         * @param webView The WebView to perform the action on.
         * @param code JavaScript code that evaluates to a result.
         * @return The result of the action, or false if it timed out.
         */
        private boolean performAction(WebView webView, String code) {
            final int resultId = mResultIdCounter.getAndIncrement();
            final String url = String.format(
                    JAVASCRIPT_ACTION_TEMPLATE, mInterfaceName, resultId, code);
            webView.loadUrl(url);

            return getResultAndClear(resultId);
        }

        /**
         * Gets the result of a request to perform an accessibility action.
         *
         * @param resultId The result id to match the result with the request.
         * @return The result of the request.
         */
        private boolean getResultAndClear(int resultId) {
            synchronized (mResultLock) {
                final boolean success = waitForResultTimedLocked(resultId);
                final boolean result = success ? mResult : false;
                clearResultLocked();
                return result;
            }
        }

        /**
         * Clears the result state.
         */
        private void clearResultLocked() {
            mResultId = -1;
            mResult = false;
        }

        /**
         * Waits up to a given bound for a result of a request and returns it.
         *
         * @param resultId The result id to match the result with the request.
         * @return Whether the result was received.
         */
        private boolean waitForResultTimedLocked(int resultId) {
            long waitTimeMillis = RESULT_TIMEOUT;
            final long startTimeMillis = SystemClock.uptimeMillis();
            while (true) {
                try {
                    if (mResultId == resultId) {
                        return true;
                    }
                    if (mResultId > resultId) {
                        return false;
                    }
                    final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                    waitTimeMillis = RESULT_TIMEOUT - elapsedTimeMillis;
                    if (waitTimeMillis <= 0) {
                        return false;
                    }
                    mResultLock.wait(waitTimeMillis);
                } catch (InterruptedException ie) {
                    /* ignore */
                }
            }
        }

        /**
         * Callback exposed to JavaScript. Handles returning the result of a
         * request to a waiting (or potentially timed out) thread.
         *
         * @param id The result id of the request as a {@link String}.
         * @param result The result of the request as a {@link String}.
         */
        @SuppressWarnings("unused")
        public void onResult(String id, String result) {
            final long resultId;

            try {
                resultId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return;
            }

            synchronized (mResultLock) {
                if (resultId > mResultId) {
                    mResult = Boolean.parseBoolean(result);
                    mResultId = resultId;
                }
                mResultLock.notifyAll();
            }
=======
     * Called when the <code>selectionString</code> has changed.
     */
    public void onSelectionStringChange(String selectionString) {
        if (DEBUG) {
            Log.d(LOG_TAG, "Selection string: " + selectionString);
        }
        mIsLastSelectionStringNull = (selectionString == null);
        if (mScheduledEventStack.isEmpty()) {
            return;
        }
        AccessibilityEvent event = mScheduledEventStack.pop();
        if (event != null) {
            event.getText().add(selectionString);
            sendAccessibilityEvent(event);
        }
    }

    /**
     * Sends an {@link AccessibilityEvent}.
     *
     * @param event The event to send.
     */
    private void sendAccessibilityEvent(AccessibilityEvent event) {
        if (DEBUG) {
            Log.d(LOG_TAG, "Dispatching: " + event);
        }
        // accessibility may be disabled while waiting for the selection string
        AccessibilityManager accessibilityManager =
            AccessibilityManager.getInstance(mWebView.getContext());
        if (accessibilityManager.isEnabled()) {
            accessibilityManager.sendAccessibilityEvent(event);
        }
    }

    /**
     * @return An accessibility event whose members are populated except its
     *         text and content description.
     */
    private AccessibilityEvent getPartialyPopulatedAccessibilityEvent() {
        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_VIEW_SELECTED);
        event.setClassName(mWebView.getClass().getName());
        event.setPackageName(mWebView.getContext().getPackageName());
        event.setEnabled(mWebView.isEnabled());
        return event;
    }

    /**
     * Ensures that the Web content key bindings are loaded.
     */
    private void ensureWebContentKeyBindings() {
        if (sBindings.size() > 0) {
            return;
        }

        String webContentKeyBindingsString  = Settings.Secure.getString(
                mWebView.getContext().getContentResolver(),
                Settings.Secure.ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS);

        SimpleStringSplitter semiColonSplitter = new SimpleStringSplitter(';');
        semiColonSplitter.setString(webContentKeyBindingsString);

        while (semiColonSplitter.hasNext()) {
            String bindingString = semiColonSplitter.next();
            if (TextUtils.isEmpty(bindingString)) {
                Log.e(LOG_TAG, "Disregarding malformed Web content key binding: "
                        + webContentKeyBindingsString);
                continue;
            }
            String[] keyValueArray = bindingString.split("=");
            if (keyValueArray.length != 2) {
                Log.e(LOG_TAG, "Disregarding malformed Web content key binding: " + bindingString);
                continue;
            }
            try {
                long keyCodeAndModifiers = Long.decode(keyValueArray[0].trim());
                String[] actionStrings = keyValueArray[1].split(":");
                int[] actions = new int[actionStrings.length];
                for (int i = 0, count = actions.length; i < count; i++) {
                    actions[i] = Integer.decode(actionStrings[i].trim());
                }
                sBindings.add(new AccessibilityWebContentKeyBinding(keyCodeAndModifiers, actions));
            } catch (NumberFormatException nfe) {
                Log.e(LOG_TAG, "Disregarding malformed key binding: " + bindingString);
            }
        }
    }

    private boolean isEnterActionKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_ENTER
                || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER;
    }

    /**
     * Represents a web content key-binding.
     */
    private static final class AccessibilityWebContentKeyBinding {

        private static final int MODIFIERS_OFFSET = 32;
        private static final long MODIFIERS_MASK = 0xFFFFFFF00000000L;

        private static final int KEY_CODE_OFFSET = 0;
        private static final long KEY_CODE_MASK = 0x00000000FFFFFFFFL;

        private static final int ACTION_OFFSET = 24;
        private static final int ACTION_MASK = 0xFF000000;

        private static final int FIRST_ARGUMENT_OFFSET = 16;
        private static final int FIRST_ARGUMENT_MASK = 0x00FF0000;

        private static final int SECOND_ARGUMENT_OFFSET = 8;
        private static final int SECOND_ARGUMENT_MASK = 0x0000FF00;

        private static final int THIRD_ARGUMENT_OFFSET = 0;
        private static final int THIRD_ARGUMENT_MASK = 0x000000FF;

        private final long mKeyCodeAndModifiers;

        private final int [] mActionSequence;

        /**
         * @return The key code of the binding key.
         */
        public int getKeyCode() {
            return (int) ((mKeyCodeAndModifiers & KEY_CODE_MASK) >> KEY_CODE_OFFSET);
        }

        /**
         * @return The meta state of the binding key.
         */
        public int getModifiers() {
            return (int) ((mKeyCodeAndModifiers & MODIFIERS_MASK) >> MODIFIERS_OFFSET);
        }

        /**
         * @return The number of actions in the key binding.
         */
        public int getActionCount() {
            return mActionSequence.length;
        }

        /**
         * @param index The action for a given action <code>index</code>.
         */
        public int getAction(int index) {
            return mActionSequence[index];
        }

        /**
         * @param index The action code for a given action <code>index</code>.
         */
        public int getActionCode(int index) {
            return (mActionSequence[index] & ACTION_MASK) >> ACTION_OFFSET;
        }

        /**
         * @param index The first argument for a given action <code>index</code>.
         */
        public int getFirstArgument(int index) {
            return (mActionSequence[index] & FIRST_ARGUMENT_MASK) >> FIRST_ARGUMENT_OFFSET;
        }

        /**
         * @param index The second argument for a given action <code>index</code>.
         */
        public int getSecondArgument(int index) {
            return (mActionSequence[index] & SECOND_ARGUMENT_MASK) >> SECOND_ARGUMENT_OFFSET;
        }

        /**
         * @param index The third argument for a given action <code>index</code>.
         */
        public int getThirdArgument(int index) {
            return (mActionSequence[index] & THIRD_ARGUMENT_MASK) >> THIRD_ARGUMENT_OFFSET;
        }

        /**
         * Creates a new instance.
         * @param keyCodeAndModifiers The key for the binding (key and modifiers).
         * @param actionSequence The sequence of action for the binding.
         */
        public AccessibilityWebContentKeyBinding(long keyCodeAndModifiers, int[] actionSequence) {
            mKeyCodeAndModifiers = keyCodeAndModifiers;
            mActionSequence = actionSequence;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("modifiers: ");
            builder.append(getModifiers());
            builder.append(", keyCode: ");
            builder.append(getKeyCode());
            builder.append(", actions[");
            for (int i = 0, count = getActionCount(); i < count; i++) {
                builder.append("{actionCode");
                builder.append(i);
                builder.append(": ");
                builder.append(getActionCode(i));
                builder.append(", firstArgument: ");
                builder.append(getFirstArgument(i));
                builder.append(", secondArgument: ");
                builder.append(getSecondArgument(i));
                builder.append(", thirdArgument: ");
                builder.append(getThirdArgument(i));
                builder.append("}");
            }
            builder.append("]");
            return builder.toString();
>>>>>>> upstream/master
        }
    }
}
