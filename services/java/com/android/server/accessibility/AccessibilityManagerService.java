/*
 ** Copyright 2009, The Android Open Source Project
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 **
 **     http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 */

package com.android.server.accessibility;

<<<<<<< HEAD
import static android.accessibilityservice.AccessibilityServiceInfo.DEFAULT;
import static android.accessibilityservice.AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceConnection;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.StatusBarManager;
=======
import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceConnection;
import android.accessibilityservice.IEventListener;
import android.app.PendingIntent;
>>>>>>> upstream/master
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
<<<<<<< HEAD
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
=======
>>>>>>> upstream/master
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
<<<<<<< HEAD
import android.content.pm.ServiceInfo;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
=======
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
>>>>>>> upstream/master
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
<<<<<<< HEAD
import android.os.SystemClock;
=======
>>>>>>> upstream/master
import android.provider.Settings;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IWindow;
<<<<<<< HEAD
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
=======
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
>>>>>>> upstream/master
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.accessibility.IAccessibilityManager;
import android.view.accessibility.IAccessibilityManagerClient;

<<<<<<< HEAD
import com.android.internal.R;
import com.android.internal.content.PackageMonitor;
import com.android.internal.statusbar.IStatusBarService;
=======
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.SomeArgs;
>>>>>>> upstream/master
import com.android.server.wm.WindowManagerService;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is instantiated by the system as a system level service and can be
 * accessed only by the system. The task of this service is to be a centralized
 * event dispatch for {@link AccessibilityEvent}s generated across all processes
 * on the device. Events are dispatched to {@link AccessibilityService}s.
 *
 * @hide
 */
<<<<<<< HEAD
public class AccessibilityManagerService extends IAccessibilityManager.Stub {
=======
public class AccessibilityManagerService extends IAccessibilityManager.Stub
        implements HandlerCaller.Callback {
>>>>>>> upstream/master

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "AccessibilityManagerService";

<<<<<<< HEAD
    private static final String FUNCTION_REGISTER_UI_TEST_AUTOMATION_SERVICE =
        "registerUiTestAutomationService";

    private static final char COMPONENT_NAME_SEPARATOR = ':';

    private static final int OWN_PROCESS_ID = android.os.Process.myPid();

    private static final int MSG_SHOW_ENABLE_TOUCH_EXPLORATION_DIALOG = 1;

    private static final int MSG_TOGGLE_TOUCH_EXPLORATION = 2;

    private static final int MSG_SEND_ACCESSIBILITY_EVENT_TO_INPUT_FILTER = 3;

    private static int sIdCounter = 0;

    private static int sNextWindowId;

=======
    private static final String FUNCTION_REGISTER_EVENT_LISTENER =
        "registerEventListener";

    private static int sIdCounter = 0;

    private static final int OWN_PROCESS_ID = android.os.Process.myPid();

    private static final int DO_SET_SERVICE_INFO = 10;

    private static int sNextWindowId;

    final HandlerCaller mCaller;

>>>>>>> upstream/master
    final Context mContext;

    final Object mLock = new Object();

    final List<Service> mServices = new ArrayList<Service>();

    final List<IAccessibilityManagerClient> mClients =
        new ArrayList<IAccessibilityManagerClient>();

    final Map<ComponentName, Service> mComponentNameToServiceMap = new HashMap<ComponentName, Service>();

    private final List<AccessibilityServiceInfo> mInstalledServices = new ArrayList<AccessibilityServiceInfo>();

    private final Set<ComponentName> mEnabledServices = new HashSet<ComponentName>();

<<<<<<< HEAD
    private final Set<ComponentName> mTouchExplorationGrantedServices = new HashSet<ComponentName>();

=======
>>>>>>> upstream/master
    private final SparseArray<AccessibilityConnectionWrapper> mWindowIdToInteractionConnectionWrapperMap =
        new SparseArray<AccessibilityConnectionWrapper>();

    private final SparseArray<IBinder> mWindowIdToWindowTokenMap = new SparseArray<IBinder>();

<<<<<<< HEAD
    private final SimpleStringSplitter mStringColonSplitter = new SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
=======
    private final SimpleStringSplitter mStringColonSplitter = new SimpleStringSplitter(':');
>>>>>>> upstream/master

    private PackageManager mPackageManager;

    private int mHandledFeedbackTypes = 0;

    private boolean mIsAccessibilityEnabled;

    private AccessibilityInputFilter mInputFilter;

    private boolean mHasInputFilter;

    private final List<AccessibilityServiceInfo> mEnabledServicesForFeedbackTempList = new ArrayList<AccessibilityServiceInfo>();

    private boolean mIsTouchExplorationEnabled;

    private final WindowManagerService mWindowManagerService;

    private final SecurityPolicy mSecurityPolicy;

<<<<<<< HEAD
    private final MainHanler mMainHandler;

    private Service mUiAutomationService;

    private Service mQueryBridge;

    private boolean mTouchExplorationGestureEnded;

    private boolean mTouchExplorationGestureStarted;

    private AlertDialog mEnableTouchExplorationDialog;
=======
    /**
     * Handler for delayed event dispatch.
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Service service = (Service) message.obj;
            int eventType = message.arg1;

            synchronized (mLock) {
                notifyEventListenerLocked(service, eventType);
            }
        }
    };
>>>>>>> upstream/master

    /**
     * Creates a new instance.
     *
     * @param context A {@link Context} instance.
     */
    public AccessibilityManagerService(Context context) {
        mContext = context;
        mPackageManager = mContext.getPackageManager();
<<<<<<< HEAD
        mWindowManagerService = (WindowManagerService) ServiceManager.getService(
                Context.WINDOW_SERVICE);
        mSecurityPolicy = new SecurityPolicy();
        mMainHandler = new MainHanler();
=======
        mCaller = new HandlerCaller(context, this);
        mWindowManagerService = (WindowManagerService) ServiceManager.getService(
                Context.WINDOW_SERVICE);
        mSecurityPolicy = new SecurityPolicy();

>>>>>>> upstream/master
        registerPackageChangeAndBootCompletedBroadcastReceiver();
        registerSettingsContentObservers();
    }

    /**
     * Registers a {@link BroadcastReceiver} for the events of
     * adding/changing/removing/restarting a package and boot completion.
     */
    private void registerPackageChangeAndBootCompletedBroadcastReceiver() {
        Context context = mContext;

        PackageMonitor monitor = new PackageMonitor() {
            @Override
            public void onSomePackagesChanged() {
                synchronized (mLock) {
<<<<<<< HEAD
                    // We will update when the automation service dies.
                    if (mUiAutomationService == null) {
                        populateAccessibilityServiceListLocked();
                        manageServicesLocked();
                    }
=======
                    populateAccessibilityServiceListLocked();
                    manageServicesLocked();
>>>>>>> upstream/master
                }
            }

            @Override
            public void onPackageRemoved(String packageName, int uid) {
                synchronized (mLock) {
                    Iterator<ComponentName> it = mEnabledServices.iterator();
                    while (it.hasNext()) {
                        ComponentName comp = it.next();
                        String compPkg = comp.getPackageName();
                        if (compPkg.equals(packageName)) {
                            it.remove();
<<<<<<< HEAD
                            // Update the enabled services setting.
                            persistComponentNamesToSettingLocked(
                                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                                    mEnabledServices);
                            // Update the touch exploration granted services setting.
                            mTouchExplorationGrantedServices.remove(comp);
                            persistComponentNamesToSettingLocked(
                                    Settings.Secure.
                                            TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES,
                                    mEnabledServices);
=======
                            updateEnabledAccessibilitySerivcesSettingLocked(mEnabledServices);
>>>>>>> upstream/master
                            return;
                        }
                    }
                }
            }

            @Override
            public boolean onHandleForceStop(Intent intent, String[] packages,
                    int uid, boolean doit) {
                synchronized (mLock) {
<<<<<<< HEAD
=======
                    boolean changed = false;
>>>>>>> upstream/master
                    Iterator<ComponentName> it = mEnabledServices.iterator();
                    while (it.hasNext()) {
                        ComponentName comp = it.next();
                        String compPkg = comp.getPackageName();
                        for (String pkg : packages) {
                            if (compPkg.equals(pkg)) {
                                if (!doit) {
                                    return true;
                                }
                                it.remove();
<<<<<<< HEAD
                                persistComponentNamesToSettingLocked(
                                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                                        mEnabledServices);
                            }
                        }
                    }
=======
                                changed = true;
                            }
                        }
                    }
                    if (changed) {
                        updateEnabledAccessibilitySerivcesSettingLocked(mEnabledServices);
                    }
>>>>>>> upstream/master
                    return false;
                }
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
                    synchronized (mLock) {
<<<<<<< HEAD
                        // We will update when the automation service dies.
                        if (mUiAutomationService == null) {
                            populateAccessibilityServiceListLocked();
                            populateEnabledAccessibilityServicesLocked();
                            populateTouchExplorationGrantedAccessibilityServicesLocked();
                            handleAccessibilityEnabledSettingChangedLocked();
                            handleTouchExplorationEnabledSettingChangedLocked();
                            updateInputFilterLocked();
                            sendStateToClientsLocked();
                        }
                    }
=======
                        populateAccessibilityServiceListLocked();
                        // get accessibility enabled setting on boot
                        mIsAccessibilityEnabled = Settings.Secure.getInt(
                                mContext.getContentResolver(),
                                Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;

                        manageServicesLocked();

                        // get touch exploration enabled setting on boot
                        mIsTouchExplorationEnabled = Settings.Secure.getInt(
                                mContext.getContentResolver(),
                                Settings.Secure.TOUCH_EXPLORATION_ENABLED, 0) == 1;
                        updateInputFilterLocked();

                        sendStateToClientsLocked();
                    }
                    
>>>>>>> upstream/master
                    return;
                }

                super.onReceive(context, intent);
            }
<<<<<<< HEAD
        };

        // package changes
        monitor.register(context, null, true);

        // boot completed
        IntentFilter bootFiler = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        mContext.registerReceiver(monitor, bootFiler, null, monitor.getRegisteredHandler());
=======

            private void updateEnabledAccessibilitySerivcesSettingLocked(
                    Set<ComponentName> enabledServices) {
                Iterator<ComponentName> it = enabledServices.iterator();
                StringBuilder str = new StringBuilder();
                while (it.hasNext()) {
                    if (str.length() > 0) {
                        str.append(':');
                    }
                    str.append(it.next().flattenToShortString());
                }
                Settings.Secure.putString(mContext.getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                        str.toString());
            }
        };

        // package changes
        monitor.register(context, true);

        // boot completed
        IntentFilter bootFiler = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        mContext.registerReceiver(monitor, bootFiler);
>>>>>>> upstream/master
    }

    /**
     * {@link ContentObserver}s for {@link Settings.Secure#ACCESSIBILITY_ENABLED}
     * and {@link Settings.Secure#ENABLED_ACCESSIBILITY_SERVICES} settings.
     */
    private void registerSettingsContentObservers() {
        ContentResolver contentResolver = mContext.getContentResolver();

        Uri accessibilityEnabledUri = Settings.Secure.getUriFor(
                Settings.Secure.ACCESSIBILITY_ENABLED);
        contentResolver.registerContentObserver(accessibilityEnabledUri, false,
            new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
<<<<<<< HEAD
                    synchronized (mLock) {
                        // We will update when the automation service dies.
                        if (mUiAutomationService == null) {
                            handleAccessibilityEnabledSettingChangedLocked();
                            updateInputFilterLocked();
                            sendStateToClientsLocked();
                        }
=======

                    synchronized (mLock) {
                        handleAccessibilityEnabledSettingChangedLocked();
>>>>>>> upstream/master
                    }
                }
            });

        Uri touchExplorationRequestedUri = Settings.Secure.getUriFor(
                Settings.Secure.TOUCH_EXPLORATION_ENABLED);
        contentResolver.registerContentObserver(touchExplorationRequestedUri, false,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
<<<<<<< HEAD
                        synchronized (mLock) {
                            // We will update when the automation service dies.
                            if (mUiAutomationService == null) {
                                handleTouchExplorationEnabledSettingChangedLocked();
                                updateInputFilterLocked();
                                sendStateToClientsLocked();
                            }
=======

                        synchronized (mLock) {
                            mIsTouchExplorationEnabled = Settings.Secure.getInt(
                                    mContext.getContentResolver(),
                                    Settings.Secure.TOUCH_EXPLORATION_ENABLED, 0) == 1;
                            updateInputFilterLocked();
                            sendStateToClientsLocked();
>>>>>>> upstream/master
                        }
                    }
                });

        Uri accessibilityServicesUri =
            Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        contentResolver.registerContentObserver(accessibilityServicesUri, false,
            new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
<<<<<<< HEAD
                    synchronized (mLock) {
                        // We will update when the automation service dies.
                        if (mUiAutomationService == null) {
                            populateEnabledAccessibilityServicesLocked();
                            manageServicesLocked();
                        }
                    }
                }
            });

        Uri touchExplorationGrantedServicesUri = Settings.Secure.getUriFor(
                Settings.Secure.TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES);
        contentResolver.registerContentObserver(touchExplorationGrantedServicesUri, false,
            new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    synchronized (mLock) {
                        // We will update when the automation service dies.
                        if (mUiAutomationService == null) {
                            populateTouchExplorationGrantedAccessibilityServicesLocked();
                            unbindAllServicesLocked();
                            manageServicesLocked();
                        }
=======

                    synchronized (mLock) {
                        manageServicesLocked();
>>>>>>> upstream/master
                    }
                }
            });
    }

    public int addClient(IAccessibilityManagerClient client) throws RemoteException {
        synchronized (mLock) {
            final IAccessibilityManagerClient addedClient = client;
            mClients.add(addedClient);
            // Clients are registered all the time until their process is
            // killed, therefore we do not have a corresponding unlinkToDeath.
            client.asBinder().linkToDeath(new DeathRecipient() {
                public void binderDied() {
                    synchronized (mLock) {
                        addedClient.asBinder().unlinkToDeath(this, 0);
                        mClients.remove(addedClient);
                    }
                }
            }, 0);
            return getState();
        }
    }

    public boolean sendAccessibilityEvent(AccessibilityEvent event) {
<<<<<<< HEAD
        final int eventType = event.getEventType();

        // The event for gesture start should be strictly before the
        // first hover enter event for the gesture.
        if (eventType == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER
                && mTouchExplorationGestureStarted) {
            mTouchExplorationGestureStarted = false;
            AccessibilityEvent gestureStartEvent = AccessibilityEvent.obtain(
                    AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START);
            sendAccessibilityEvent(gestureStartEvent);
        }

        synchronized (mLock) {
            if (mSecurityPolicy.canDispatchAccessibilityEvent(event)) {
                mSecurityPolicy.updateActiveWindowAndEventSourceLocked(event);
                notifyAccessibilityServicesDelayedLocked(event, false);
                notifyAccessibilityServicesDelayedLocked(event, true);
            }
            if (mHasInputFilter && mInputFilter != null) {
                mMainHandler.obtainMessage(MSG_SEND_ACCESSIBILITY_EVENT_TO_INPUT_FILTER,
                        AccessibilityEvent.obtain(event)).sendToTarget();

            }
            event.recycle();
            mHandledFeedbackTypes = 0;
        }

        // The event for gesture end should be strictly after the
        // last hover exit event for the gesture.
        if (eventType == AccessibilityEvent.TYPE_VIEW_HOVER_EXIT
                && mTouchExplorationGestureEnded) {
            mTouchExplorationGestureEnded = false;
            AccessibilityEvent gestureEndEvent = AccessibilityEvent.obtain(
                    AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END);
            sendAccessibilityEvent(gestureEndEvent);
        }

=======
        synchronized (mLock) {
            if (mSecurityPolicy.canDispatchAccessibilityEvent(event)) {
                mSecurityPolicy.updateRetrievalAllowingWindowAndEventSourceLocked(event);
                notifyAccessibilityServicesDelayedLocked(event, false);
                notifyAccessibilityServicesDelayedLocked(event, true);
            }
        }
        event.recycle();
        mHandledFeedbackTypes = 0;
>>>>>>> upstream/master
        return (OWN_PROCESS_ID != Binder.getCallingPid());
    }

    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList() {
        synchronized (mLock) {
            return mInstalledServices;
        }
    }

    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType) {
        List<AccessibilityServiceInfo> result = mEnabledServicesForFeedbackTempList;
        result.clear();
        List<Service> services = mServices;
        synchronized (mLock) {
            while (feedbackType != 0) {
                final int feedbackTypeBit = (1 << Integer.numberOfTrailingZeros(feedbackType));
                feedbackType &= ~feedbackTypeBit;
                final int serviceCount = services.size();
                for (int i = 0; i < serviceCount; i++) {
                    Service service = services.get(i);
                    if ((service.mFeedbackType & feedbackTypeBit) != 0) {
                        result.add(service.mAccessibilityServiceInfo);
                    }
                }
            }
        }
        return result;
    }

    public void interrupt() {
        synchronized (mLock) {
            for (int i = 0, count = mServices.size(); i < count; i++) {
                Service service = mServices.get(i);
                try {
                    service.mServiceInterface.onInterrupt();
                } catch (RemoteException re) {
                    Slog.e(LOG_TAG, "Error during sending interrupt request to "
                        + service.mService, re);
                }
            }
        }
    }

<<<<<<< HEAD
=======
    public void executeMessage(Message message) {
        switch (message.what) {
            case DO_SET_SERVICE_INFO: {
                SomeArgs arguments = ((SomeArgs) message.obj);

                AccessibilityServiceInfo info = (AccessibilityServiceInfo) arguments.arg1;
                Service service = (Service) arguments.arg2;

                synchronized (mLock) {
                    // If the XML manifest had data to configure the service its info
                    // should be already set. In such a case update only the dynamically
                    // configurable properties.
                    AccessibilityServiceInfo oldInfo = service.mAccessibilityServiceInfo;
                    if (oldInfo != null) {
                        oldInfo.updateDynamicallyConfigurableProperties(info);
                        service.setDynamicallyConfigurableProperties(oldInfo);
                    } else {
                        service.setDynamicallyConfigurableProperties(info);
                    }
                }
            } return;
            default:
                Slog.w(LOG_TAG, "Unknown message type: " + message.what);
        }
    }

>>>>>>> upstream/master
    public int addAccessibilityInteractionConnection(IWindow windowToken,
            IAccessibilityInteractionConnection connection) throws RemoteException {
        synchronized (mLock) {
            final IWindow addedWindowToken = windowToken;
<<<<<<< HEAD
=======
            final IAccessibilityInteractionConnection addedConnection = connection;
>>>>>>> upstream/master
            final int windowId = sNextWindowId++;
            AccessibilityConnectionWrapper wrapper = new AccessibilityConnectionWrapper(windowId,
                    connection);
            wrapper.linkToDeath();
            mWindowIdToWindowTokenMap.put(windowId, addedWindowToken.asBinder());
            mWindowIdToInteractionConnectionWrapperMap.put(windowId, wrapper);
            if (DEBUG) {
                Slog.i(LOG_TAG, "Adding interaction connection to windowId: " + windowId);
            }
            return windowId;
        }
    }

    public void removeAccessibilityInteractionConnection(IWindow windowToken) {
        synchronized (mLock) {
            final int count = mWindowIdToWindowTokenMap.size();
            for (int i = 0; i < count; i++) {
                if (mWindowIdToWindowTokenMap.valueAt(i) == windowToken.asBinder()) {
                    final int windowId = mWindowIdToWindowTokenMap.keyAt(i);
                    AccessibilityConnectionWrapper wrapper =
                        mWindowIdToInteractionConnectionWrapperMap.get(windowId);
                    wrapper.unlinkToDeath();
                    removeAccessibilityInteractionConnectionLocked(windowId);
                    return;
                }
            }
        }
    }

<<<<<<< HEAD
    public void registerUiTestAutomationService(IAccessibilityServiceClient serviceClient,
            AccessibilityServiceInfo accessibilityServiceInfo) {
        mSecurityPolicy.enforceCallingPermission(Manifest.permission.RETRIEVE_WINDOW_CONTENT,
                FUNCTION_REGISTER_UI_TEST_AUTOMATION_SERVICE);
=======
    public void registerEventListener(IEventListener listener) {
        mSecurityPolicy.enforceCallingPermission(Manifest.permission.RETRIEVE_WINDOW_CONTENT,
                FUNCTION_REGISTER_EVENT_LISTENER);
>>>>>>> upstream/master
        ComponentName componentName = new ComponentName("foo.bar",
                "AutomationAccessibilityService");
        synchronized (mLock) {
            // If an automation services is connected to the system all services are stopped
            // so the automation one is the only one running. Settings are not changed so when
            // the automation service goes away the state is restored from the settings.

            // Disable all services.
            final int runningServiceCount = mServices.size();
            for (int i = 0; i < runningServiceCount; i++) {
                Service runningService = mServices.get(i);
                runningService.unbind();
            }
            // If necessary enable accessibility and announce that.
            if (!mIsAccessibilityEnabled) {
                mIsAccessibilityEnabled = true;
                sendStateToClientsLocked();
            }
        }
        // Hook the automation service up.
<<<<<<< HEAD
        mUiAutomationService = new Service(componentName, accessibilityServiceInfo, true);
        mUiAutomationService.onServiceConnected(componentName, serviceClient.asBinder());
    }

    public void unregisterUiTestAutomationService(IAccessibilityServiceClient serviceClient) {
        synchronized (mLock) {
            // Automation service is not bound, so pretend it died to perform clean up.
            if (mUiAutomationService != null
                    && mUiAutomationService.mServiceInterface == serviceClient) {
                mUiAutomationService.binderDied();
            }
        }
    }

    boolean onGesture(int gestureId) {
        synchronized (mLock) {
            boolean handled = notifyGestureLocked(gestureId, false);
            if (!handled) {
                handled = notifyGestureLocked(gestureId, true);
            }
            return handled;
        }
    }

    /**
     * Gets the bounds of the accessibility focus in the active window.
     *
     * @param outBounds The output to which to write the focus bounds.
     * @return Whether accessibility focus was found and the bounds are populated.
     */
    boolean getAccessibilityFocusBoundsInActiveWindow(Rect outBounds) {
        // Instead of keeping track of accessibility focus events per
        // window to be able to find the focus in the active window,
        // we take a stateless approach and look it up. This is fine
        // since we do this only when the user clicks/long presses.
        Service service = getQueryBridge();
        final int connectionId = service.mId;
        AccessibilityInteractionClient client = AccessibilityInteractionClient.getInstance();
        client.addConnection(connectionId, service);
        try {
            AccessibilityNodeInfo root = AccessibilityInteractionClient.getInstance()
                    .getRootInActiveWindow(connectionId);
            if (root == null) {
                return false;
            }
            AccessibilityNodeInfo focus = root.findFocus(
                    AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);
            if (focus == null) {
                return false;
            }
            focus.getBoundsInScreen(outBounds);
            return true;
        } finally {
            client.removeConnection(connectionId);
        }
    }

    /**
     * Gets the bounds of the active window.
     *
     * @param outBounds The output to which to write the bounds.
     */
    void getActiveWindowBounds(Rect outBounds) {
        synchronized (mLock) {
            final int windowId = mSecurityPolicy.mActiveWindowId;
            IBinder token = mWindowIdToWindowTokenMap.get(windowId);
            mWindowManagerService.getWindowFrame(token, outBounds);
        }
    }

    int getActiveWindowId() {
        return mSecurityPolicy.mActiveWindowId;
    }

    private Service getQueryBridge() {
        if (mQueryBridge == null) {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            mQueryBridge = new Service(null, info, true);
        }
        return mQueryBridge;
    }

    public void touchExplorationGestureEnded() {
        mTouchExplorationGestureEnded = true;
    }

    public void touchExplorationGestureStarted() {
        mTouchExplorationGestureStarted = true;
    }

    private boolean notifyGestureLocked(int gestureId, boolean isDefault) {
        // TODO: Now we are giving the gestures to the last enabled
        //       service that can handle them which is the last one
        //       in our list since we write the last enabled as the
        //       last record in the enabled services setting. Ideally,
        //       the user should make the call which service handles
        //       gestures. However, only one service should handle
        //       gestures to avoid user frustration when different
        //       behavior is observed from different combinations of
        //       enabled accessibility services.
        for (int i = mServices.size() - 1; i >= 0; i--) {
            Service service = mServices.get(i);
            if (service.mReqeustTouchExplorationMode && service.mIsDefault == isDefault) {
                service.notifyGesture(gestureId);
                return true;
            }
        }
        return false;
=======
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        Service service = new Service(componentName, accessibilityServiceInfo, true);
        service.onServiceConnected(componentName, listener.asBinder());
>>>>>>> upstream/master
    }

    /**
     * Removes an AccessibilityInteractionConnection.
     *
     * @param windowId The id of the window to which the connection is targeted.
     */
    private void removeAccessibilityInteractionConnectionLocked(int windowId) {
        mWindowIdToWindowTokenMap.remove(windowId);
        mWindowIdToInteractionConnectionWrapperMap.remove(windowId);
        if (DEBUG) {
            Slog.i(LOG_TAG, "Removing interaction connection to windowId: " + windowId);
        }
    }

    /**
     * Populates the cached list of installed {@link AccessibilityService}s.
     */
    private void populateAccessibilityServiceListLocked() {
        mInstalledServices.clear();

        List<ResolveInfo> installedServices = mPackageManager.queryIntentServices(
                new Intent(AccessibilityService.SERVICE_INTERFACE),
                PackageManager.GET_SERVICES | PackageManager.GET_META_DATA);

        for (int i = 0, count = installedServices.size(); i < count; i++) {
            ResolveInfo resolveInfo = installedServices.get(i);
<<<<<<< HEAD
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            // For now we are enforcing this if the target version is JellyBean or
            // higher and in a later release we will enforce this for everyone.
            if (serviceInfo.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.JELLY_BEAN
                    && !android.Manifest.permission.BIND_ACCESSIBILITY_SERVICE.equals(
                    serviceInfo.permission)) {
                Slog.w(LOG_TAG, "Skipping accessibilty service " + new ComponentName(
                        serviceInfo.packageName, serviceInfo.name).flattenToShortString()
                        + ": it does not require the permission "
                        + android.Manifest.permission.BIND_ACCESSIBILITY_SERVICE);
                continue;
            }
=======
>>>>>>> upstream/master
            AccessibilityServiceInfo accessibilityServiceInfo;
            try {
                accessibilityServiceInfo = new AccessibilityServiceInfo(resolveInfo, mContext);
                mInstalledServices.add(accessibilityServiceInfo);
            } catch (XmlPullParserException xppe) {
                Slog.e(LOG_TAG, "Error while initializing AccessibilityServiceInfo", xppe);
            } catch (IOException ioe) {
                Slog.e(LOG_TAG, "Error while initializing AccessibilityServiceInfo", ioe);
            }
        }
    }

<<<<<<< HEAD
    private void populateEnabledAccessibilityServicesLocked() {
        populateComponentNamesFromSettingLocked(
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                mEnabledServices);
    }

    private void populateTouchExplorationGrantedAccessibilityServicesLocked() {
        populateComponentNamesFromSettingLocked(
                Settings.Secure.TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES,
                mTouchExplorationGrantedServices);
    }

=======
>>>>>>> upstream/master
    /**
     * Performs {@link AccessibilityService}s delayed notification. The delay is configurable
     * and denotes the period after the last event before notifying the service.
     *
     * @param event The event.
     * @param isDefault True to notify default listeners, not default services.
     */
    private void notifyAccessibilityServicesDelayedLocked(AccessibilityEvent event,
            boolean isDefault) {
        try {
            for (int i = 0, count = mServices.size(); i < count; i++) {
                Service service = mServices.get(i);

                if (service.mIsDefault == isDefault) {
                    if (canDispathEventLocked(service, event, mHandledFeedbackTypes)) {
                        mHandledFeedbackTypes |= service.mFeedbackType;
<<<<<<< HEAD
                        service.notifyAccessibilityEvent(event);
=======
                        notifyAccessibilityServiceDelayedLocked(service, event);
>>>>>>> upstream/master
                    }
                }
            }
        } catch (IndexOutOfBoundsException oobe) {
            // An out of bounds exception can happen if services are going away
            // as the for loop is running. If that happens, just bail because
            // there are no more services to notify.
            return;
        }
    }

    /**
<<<<<<< HEAD
=======
     * Performs an {@link AccessibilityService} delayed notification. The delay is configurable
     * and denotes the period after the last event before notifying the service.
     *
     * @param service The service.
     * @param event The event.
     */
    private void notifyAccessibilityServiceDelayedLocked(Service service,
            AccessibilityEvent event) {
        synchronized (mLock) {
            final int eventType = event.getEventType();
            // Make a copy since during dispatch it is possible the event to
            // be modified to remove its source if the receiving service does
            // not have permission to access the window content.
            AccessibilityEvent newEvent = AccessibilityEvent.obtain(event);
            AccessibilityEvent oldEvent = service.mPendingEvents.get(eventType);
            service.mPendingEvents.put(eventType, newEvent);

            final int what = eventType | (service.mId << 16);
            if (oldEvent != null) {
                mHandler.removeMessages(what);
                oldEvent.recycle();
            }

            Message message = mHandler.obtainMessage(what, service);
            message.arg1 = eventType;
            mHandler.sendMessageDelayed(message, service.mNotificationTimeout);
        }
    }

    /**
     * Notifies a service for a scheduled event given the event type.
     *
     * @param service The service.
     * @param eventType The type of the event to dispatch.
     */
    private void notifyEventListenerLocked(Service service, int eventType) {
        IEventListener listener = service.mServiceInterface;

        // If the service died/was disabled while the message for dispatching
        // the accessibility event was propagating the listener may be null.
        if (listener == null) {
            return;
        }

        AccessibilityEvent event = service.mPendingEvents.get(eventType);

        // Check for null here because there is a concurrent scenario in which this
        // happens: 1) A binder thread calls notifyAccessibilityServiceDelayedLocked
        // which posts a message for dispatching an event. 2) The message is pulled
        // from the queue by the handler on the service thread and the latter is
        // just about to acquire the lock and call this method. 3) Now another binder
        // thread acquires the lock calling notifyAccessibilityServiceDelayedLocked
        // so the service thread waits for the lock; 4) The binder thread replaces
        // the event with a more recent one (assume the same event type) and posts a
        // dispatch request releasing the lock. 5) Now the main thread is unblocked and
        // dispatches the event which is removed from the pending ones. 6) And ... now
        // the service thread handles the last message posted by the last binder call
        // but the event is already dispatched and hence looking it up in the pending
        // ones yields null. This check is much simpler that keeping count for each
        // event type of each service to catch such a scenario since only one message
        // is processed at a time.
        if (event == null) {
            return;
        }

        service.mPendingEvents.remove(eventType);
        try {
            if (mSecurityPolicy.canRetrieveWindowContent(service)) {
                event.setConnectionId(service.mId);
            } else {
                event.setSource(null);
            }
            event.setSealed(true);
            listener.onAccessibilityEvent(event);
            event.recycle();
            if (DEBUG) {
                Slog.i(LOG_TAG, "Event " + event + " sent to " + listener);
            }
        } catch (RemoteException re) {
            Slog.e(LOG_TAG, "Error during sending " + event + " to " + service.mService, re);
        }
    }

    /**
>>>>>>> upstream/master
     * Adds a service.
     *
     * @param service The service to add.
     */
    private void tryAddServiceLocked(Service service) {
        try {
            if (mServices.contains(service) || !service.isConfigured()) {
                return;
            }
            service.linkToOwnDeath();
            mServices.add(service);
            mComponentNameToServiceMap.put(service.mComponentName, service);
            updateInputFilterLocked();
<<<<<<< HEAD
            tryEnableTouchExplorationLocked(service);
=======
>>>>>>> upstream/master
        } catch (RemoteException e) {
            /* do nothing */
        }
    }

    /**
     * Removes a service.
     *
     * @param service The service.
     * @return True if the service was removed, false otherwise.
     */
    private boolean tryRemoveServiceLocked(Service service) {
        final boolean removed = mServices.remove(service);
        if (!removed) {
            return false;
        }
        mComponentNameToServiceMap.remove(service.mComponentName);
<<<<<<< HEAD
        service.unlinkToOwnDeath();
        service.dispose();
        updateInputFilterLocked();
        tryDisableTouchExplorationLocked(service);
=======
        mHandler.removeMessages(service.mId);
        service.unlinkToOwnDeath();
        service.dispose();
        updateInputFilterLocked();
>>>>>>> upstream/master
        return removed;
    }

    /**
     * Determines if given event can be dispatched to a service based on the package of the
     * event source and already notified services for that event type. Specifically, a
     * service is notified if it is interested in events from the package and no other service
     * providing the same feedback type has been notified. Exception are services the
     * provide generic feedback (feedback type left as a safety net for unforeseen feedback
     * types) which are always notified.
     *
     * @param service The potential receiver.
     * @param event The event.
     * @param handledFeedbackTypes The feedback types for which services have been notified.
     * @return True if the listener should be notified, false otherwise.
     */
    private boolean canDispathEventLocked(Service service, AccessibilityEvent event,
            int handledFeedbackTypes) {

        if (!service.isConfigured()) {
            return false;
        }

<<<<<<< HEAD
        if (!event.isImportantForAccessibility()
                && !service.mIncludeNotImportantViews) {
            return false;
        }

=======
>>>>>>> upstream/master
        int eventType = event.getEventType();
        if ((service.mEventTypes & eventType) != eventType) {
            return false;
        }

        Set<String> packageNames = service.mPackageNames;
        CharSequence packageName = event.getPackageName();

        if (packageNames.isEmpty() || packageNames.contains(packageName)) {
            int feedbackType = service.mFeedbackType;
            if ((handledFeedbackTypes & feedbackType) != feedbackType
                    || feedbackType == AccessibilityServiceInfo.FEEDBACK_GENERIC) {
                return true;
            }
        }

        return false;
    }

    /**
     * Manages services by starting enabled ones and stopping disabled ones.
     */
    private void manageServicesLocked() {
<<<<<<< HEAD
=======
        unbindAutomationService();
        populateEnabledServicesLocked(mEnabledServices);
>>>>>>> upstream/master
        final int enabledInstalledServicesCount = updateServicesStateLocked(mInstalledServices,
                mEnabledServices);
        // No enabled installed services => disable accessibility to avoid
        // sending accessibility events with no recipient across processes.
        if (mIsAccessibilityEnabled && enabledInstalledServicesCount == 0) {
            Settings.Secure.putInt(mContext.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        }
    }

    /**
     * Unbinds all bound services.
     */
    private void unbindAllServicesLocked() {
        List<Service> services = mServices;

        for (int i = 0, count = services.size(); i < count; i++) {
            Service service = services.get(i);
            if (service.unbind()) {
                i--;
                count--;
            }
        }
    }

    /**
<<<<<<< HEAD
     * Populates a set with the {@link ComponentName}s stored in a colon
     * separated value setting.
     *
     * @param settingName The setting to parse.
     * @param outComponentNames The output component names.
     */
    private void populateComponentNamesFromSettingLocked(String settingName,
            Set<ComponentName> outComponentNames) {
        outComponentNames.clear();

        String settingValue = Settings.Secure.getString(mContext.getContentResolver(), settingName);

        if (settingValue != null) {
            TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
            splitter.setString(settingValue);
=======
     * Unbinds the automation service if such is running.
     */
    private void unbindAutomationService() {
        List<Service> runningServices = mServices;
        int runningServiceCount = mServices.size();
        for (int i = 0; i < runningServiceCount; i++) {
            Service service = runningServices.get(i);
            if (service.mIsAutomation) {
                 service.unbind();
                 return;
            }
        }
    }

    /**
     * Populates a list with the {@link ComponentName}s of all enabled
     * {@link AccessibilityService}s.
     *
     * @param enabledServices The list.
     */
    private void populateEnabledServicesLocked(Set<ComponentName> enabledServices) {
        enabledServices.clear();

        String servicesValue = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        if (servicesValue != null) {
            TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
            splitter.setString(servicesValue);
>>>>>>> upstream/master
            while (splitter.hasNext()) {
                String str = splitter.next();
                if (str == null || str.length() <= 0) {
                    continue;
                }
                ComponentName enabledService = ComponentName.unflattenFromString(str);
                if (enabledService != null) {
<<<<<<< HEAD
                    outComponentNames.add(enabledService);
=======
                    enabledServices.add(enabledService);
>>>>>>> upstream/master
                }
            }
        }
    }

    /**
<<<<<<< HEAD
     * Persists the component names in the specified setting in a
     * colon separated fashion.
     *
     * @param settingName The setting name.
     * @param componentNames The component names.
     */
    private void persistComponentNamesToSettingLocked(String settingName,
            Set<ComponentName> componentNames) {
        StringBuilder builder = new StringBuilder();
        for (ComponentName componentName : componentNames) {
            if (builder.length() > 0) {
                builder.append(COMPONENT_NAME_SEPARATOR);
            }
            builder.append(componentName.flattenToShortString());
        }
        Settings.Secure.putString(mContext.getContentResolver(), settingName, builder.toString());
    }

    /**
=======
>>>>>>> upstream/master
     * Updates the state of each service by starting (or keeping running) enabled ones and
     * stopping the rest.
     *
     * @param installedServices All installed {@link AccessibilityService}s.
     * @param enabledServices The {@link ComponentName}s of the enabled services.
     * @return The number of enabled installed services.
     */
    private int updateServicesStateLocked(List<AccessibilityServiceInfo> installedServices,
            Set<ComponentName> enabledServices) {

        Map<ComponentName, Service> componentNameToServiceMap = mComponentNameToServiceMap;
        boolean isEnabled = mIsAccessibilityEnabled;

        int enabledInstalledServices = 0;
        for (int i = 0, count = installedServices.size(); i < count; i++) {
            AccessibilityServiceInfo installedService = installedServices.get(i);
            ComponentName componentName = ComponentName.unflattenFromString(
                    installedService.getId());
            Service service = componentNameToServiceMap.get(componentName);

            if (isEnabled) {
                if (enabledServices.contains(componentName)) {
                    if (service == null) {
                        service = new Service(componentName, installedService, false);
                    }
                    service.bind();
                    enabledInstalledServices++;
                } else {
                    if (service != null) {
                        service.unbind();
                    }
                }
            } else {
                if (service != null) {
                    service.unbind();
                }
            }
        }

        return enabledInstalledServices;
    }

    /**
     * Sends the state to the clients.
     */
    private void sendStateToClientsLocked() {
        final int state = getState();
        for (int i = 0, count = mClients.size(); i < count; i++) {
            try {
                mClients.get(i).setState(state);
            } catch (RemoteException re) {
                mClients.remove(i);
                count--;
                i--;
            }
        }
    }

    /**
     * Gets the current state as a set of flags.
     *
     * @return The state.
     */
    private int getState() {
        int state = 0;
        if (mIsAccessibilityEnabled) {
            state |= AccessibilityManager.STATE_FLAG_ACCESSIBILITY_ENABLED;
        }
        // Touch exploration relies on enabled accessibility.
        if (mIsAccessibilityEnabled && mIsTouchExplorationEnabled) {
            state |= AccessibilityManager.STATE_FLAG_TOUCH_EXPLORATION_ENABLED;
        }
        return state;
    }

    /**
     * Updates the touch exploration state.
     */
    private void updateInputFilterLocked() {
        if (mIsAccessibilityEnabled && mIsTouchExplorationEnabled) {
            if (!mHasInputFilter) {
                mHasInputFilter = true;
                if (mInputFilter == null) {
<<<<<<< HEAD
                    mInputFilter = new AccessibilityInputFilter(mContext, this);
=======
                    mInputFilter = new AccessibilityInputFilter(mContext);
>>>>>>> upstream/master
                }
                mWindowManagerService.setInputFilter(mInputFilter);
            }
            return;
        }
        if (mHasInputFilter) {
            mHasInputFilter = false;
            mWindowManagerService.setInputFilter(null);
        }
    }

    /**
     * Updated the state based on the accessibility enabled setting.
     */
    private void handleAccessibilityEnabledSettingChangedLocked() {
        mIsAccessibilityEnabled = Settings.Secure.getInt(
                mContext.getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
        if (mIsAccessibilityEnabled) {
            manageServicesLocked();
        } else {
            unbindAllServicesLocked();
        }
<<<<<<< HEAD
    }

    /**
     * Updates the state based on the touch exploration enabled setting.
     */
    private void handleTouchExplorationEnabledSettingChangedLocked() {
        mIsTouchExplorationEnabled = Settings.Secure.getInt(
                mContext.getContentResolver(),
                Settings.Secure.TOUCH_EXPLORATION_ENABLED, 0) == 1;
    }

    private void tryEnableTouchExplorationLocked(final Service service) {
        if (!mIsTouchExplorationEnabled && service.mRequestTouchExplorationMode) {
            final boolean canToggleTouchExploration = mTouchExplorationGrantedServices.contains(
                    service.mComponentName);
            if (!service.mIsAutomation && !canToggleTouchExploration) {
                mMainHandler.obtainMessage(MSG_SHOW_ENABLE_TOUCH_EXPLORATION_DIALOG,
                        service).sendToTarget();
            } else {
                mMainHandler.obtainMessage(MSG_TOGGLE_TOUCH_EXPLORATION, 1, 0).sendToTarget();
            }
        }
    }

    private void tryDisableTouchExplorationLocked(Service service) {
        if (mIsTouchExplorationEnabled) {
            synchronized (mLock) {
                final int serviceCount = mServices.size();
                for (int i = 0; i < serviceCount; i++) {
                    Service other = mServices.get(i);
                    if (other != service && other.mRequestTouchExplorationMode) {
                        return;
                    }
                }
                mMainHandler.obtainMessage(MSG_TOGGLE_TOUCH_EXPLORATION, 0, 0).sendToTarget();
            }
        }
=======
        updateInputFilterLocked();
        sendStateToClientsLocked();
>>>>>>> upstream/master
    }

    private class AccessibilityConnectionWrapper implements DeathRecipient {
        private final int mWindowId;
        private final IAccessibilityInteractionConnection mConnection;

        public AccessibilityConnectionWrapper(int windowId,
                IAccessibilityInteractionConnection connection) {
            mWindowId = windowId;
            mConnection = connection;
        }

        public void linkToDeath() throws RemoteException {
            mConnection.asBinder().linkToDeath(this, 0);
        }

        public void unlinkToDeath() {
            mConnection.asBinder().unlinkToDeath(this, 0);
        }

        @Override
        public void binderDied() {
            unlinkToDeath();
            synchronized (mLock) {
                removeAccessibilityInteractionConnectionLocked(mWindowId);
            }
        }
    }

<<<<<<< HEAD
    private class MainHanler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            final int type = msg.what;
            switch (type) {
                case MSG_TOGGLE_TOUCH_EXPLORATION: {
                    final int value = msg.arg1;
                    Settings.Secure.putInt(mContext.getContentResolver(),
                            Settings.Secure.TOUCH_EXPLORATION_ENABLED, value);
                } break;
                case MSG_SHOW_ENABLE_TOUCH_EXPLORATION_DIALOG: {
                    final Service service = (Service) msg.obj;
                    String label = service.mResolveInfo.loadLabel(
                            mContext.getPackageManager()).toString();
                    synchronized (mLock) {
                        if (mIsTouchExplorationEnabled) {
                            return;
                        }
                        if (mEnableTouchExplorationDialog != null
                                && mEnableTouchExplorationDialog.isShowing()) {
                            return;
                        }
                        mEnableTouchExplorationDialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // The user allowed the service to toggle touch exploration.
                                    mTouchExplorationGrantedServices.add(service.mComponentName);
                                    persistComponentNamesToSettingLocked(
                                            Settings.Secure.
                                                   TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES,
                                            mTouchExplorationGrantedServices);
                                    // Enable touch exploration.
                                    Settings.Secure.putInt(mContext.getContentResolver(),
                                            Settings.Secure.TOUCH_EXPLORATION_ENABLED, 1);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle(R.string.enable_explore_by_touch_warning_title)
                            .setMessage(mContext.getString(
                                R.string.enable_explore_by_touch_warning_message, label))
                            .create();
                        mEnableTouchExplorationDialog.getWindow().setType(
                                WindowManager.LayoutParams.TYPE_INPUT_METHOD_DIALOG);
                        mEnableTouchExplorationDialog.setCanceledOnTouchOutside(true);
                        mEnableTouchExplorationDialog.show();
                    }
                } break;
                case MSG_SEND_ACCESSIBILITY_EVENT_TO_INPUT_FILTER: {
                    AccessibilityEvent event = (AccessibilityEvent) msg.obj;
                    if (mHasInputFilter && mInputFilter != null) {
                        mInputFilter.onAccessibilityEvent(event);
                    }
                    event.recycle();
                } break;
            }
        }
    }

=======
>>>>>>> upstream/master
    /**
     * This class represents an accessibility service. It stores all per service
     * data required for the service management, provides API for starting/stopping the
     * service and is responsible for adding/removing the service in the data structures
     * for service management. The class also exposes configuration interface that is
     * passed to the service it represents as soon it is bound. It also serves as the
     * connection for the service.
     */
    class Service extends IAccessibilityServiceConnection.Stub
            implements ServiceConnection, DeathRecipient {
<<<<<<< HEAD

        // We pick the MSB to avoid collision since accessibility event types are
        // used as message types allowing us to remove messages per event type. 
        private static final int MSG_ON_GESTURE = 0x80000000;

=======
>>>>>>> upstream/master
        int mId = 0;

        AccessibilityServiceInfo mAccessibilityServiceInfo;

        IBinder mService;

<<<<<<< HEAD
        IAccessibilityServiceClient mServiceInterface;
=======
        IEventListener mServiceInterface;
>>>>>>> upstream/master

        int mEventTypes;

        int mFeedbackType;

        Set<String> mPackageNames = new HashSet<String>();

        boolean mIsDefault;

<<<<<<< HEAD
        boolean mRequestTouchExplorationMode;

        boolean mIncludeNotImportantViews;

=======
>>>>>>> upstream/master
        long mNotificationTimeout;

        ComponentName mComponentName;

        Intent mIntent;

        boolean mCanRetrieveScreenContent;

<<<<<<< HEAD
        boolean mReqeustTouchExplorationMode;

=======
>>>>>>> upstream/master
        boolean mIsAutomation;

        final Rect mTempBounds = new Rect();

<<<<<<< HEAD
        final ResolveInfo mResolveInfo;

=======
>>>>>>> upstream/master
        // the events pending events to be dispatched to this service
        final SparseArray<AccessibilityEvent> mPendingEvents =
            new SparseArray<AccessibilityEvent>();

<<<<<<< HEAD
        /**
         * Handler for delayed event dispatch.
         */
        public Handler mHandler = new Handler(mMainHandler.getLooper()) {
            @Override
            public void handleMessage(Message message) {
                final int type = message.what;
                switch (type) {
                    case MSG_ON_GESTURE: {
                        final int gestureId = message.arg1;
                        notifyGestureInternal(gestureId);
                    } break;
                    default: {
                        final int eventType = type;
                        notifyAccessibilityEventInternal(eventType);
                    } break;
                }
            }
        };

        public Service(ComponentName componentName,
                AccessibilityServiceInfo accessibilityServiceInfo, boolean isAutomation) {
            mResolveInfo = accessibilityServiceInfo.getResolveInfo();
=======
        public Service(ComponentName componentName,
                AccessibilityServiceInfo accessibilityServiceInfo, boolean isAutomation) {
>>>>>>> upstream/master
            mId = sIdCounter++;
            mComponentName = componentName;
            mAccessibilityServiceInfo = accessibilityServiceInfo;
            mIsAutomation = isAutomation;
            if (!isAutomation) {
                mCanRetrieveScreenContent = accessibilityServiceInfo.getCanRetrieveWindowContent();
<<<<<<< HEAD
                mReqeustTouchExplorationMode =
                    (accessibilityServiceInfo.flags
                            & AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE) != 0;
=======
>>>>>>> upstream/master
                mIntent = new Intent().setComponent(mComponentName);
                mIntent.putExtra(Intent.EXTRA_CLIENT_LABEL,
                        com.android.internal.R.string.accessibility_binding_label);
                mIntent.putExtra(Intent.EXTRA_CLIENT_INTENT, PendingIntent.getActivity(
                        mContext, 0, new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0));
            } else {
                mCanRetrieveScreenContent = true;
            }
            setDynamicallyConfigurableProperties(accessibilityServiceInfo);
        }

        public void setDynamicallyConfigurableProperties(AccessibilityServiceInfo info) {
            mEventTypes = info.eventTypes;
            mFeedbackType = info.feedbackType;
            String[] packageNames = info.packageNames;
            if (packageNames != null) {
                mPackageNames.addAll(Arrays.asList(packageNames));
            }
            mNotificationTimeout = info.notificationTimeout;
<<<<<<< HEAD
            mIsDefault = (info.flags & DEFAULT) != 0;

            if (mIsAutomation || info.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion
                    >= Build.VERSION_CODES.JELLY_BEAN) {
                mIncludeNotImportantViews =
                    (info.flags & FLAG_INCLUDE_NOT_IMPORTANT_VIEWS) != 0;
            }

            mRequestTouchExplorationMode = (info.flags
                    & AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE) != 0;

            // If this service is up and running we may have to enable touch
            // exploration, otherwise this will happen when the service connects.
            synchronized (mLock) {
                if (isConfigured()) {
                    if (mRequestTouchExplorationMode) {
                        tryEnableTouchExplorationLocked(this);
                    } else {
                        tryDisableTouchExplorationLocked(this);
                    }
                }
=======
            mIsDefault = (info.flags & AccessibilityServiceInfo.DEFAULT) != 0;

            synchronized (mLock) {
                tryAddServiceLocked(this);
>>>>>>> upstream/master
            }
        }

        /**
         * Binds to the accessibility service.
         *
         * @return True if binding is successful.
         */
        public boolean bind() {
            if (!mIsAutomation && mService == null) {
                return mContext.bindService(mIntent, this, Context.BIND_AUTO_CREATE);
            }
            return false;
        }

        /**
         * Unbinds form the accessibility service and removes it from the data
         * structures for service management.
         *
         * @return True if unbinding is successful.
         */
        public boolean unbind() {
            if (mService != null) {
                synchronized (mLock) {
                    tryRemoveServiceLocked(this);
                }
                if (!mIsAutomation) {
                    mContext.unbindService(this);
                }
                return true;
            }
            return false;
        }

        /**
         * Returns if the service is configured i.e. at least event types of interest
         * and feedback type must be set.
         *
         * @return True if the service is configured, false otherwise.
         */
        public boolean isConfigured() {
            return (mEventTypes != 0 && mFeedbackType != 0 && mService != null);
        }

<<<<<<< HEAD
        @Override
        public AccessibilityServiceInfo getServiceInfo() {
            synchronized (mLock) {
                return mAccessibilityServiceInfo;
            }
        }

        @Override
        public void setServiceInfo(AccessibilityServiceInfo info) {
            synchronized (mLock) {
                // If the XML manifest had data to configure the service its info
                // should be already set. In such a case update only the dynamically
                // configurable properties.
                AccessibilityServiceInfo oldInfo = mAccessibilityServiceInfo;
                if (oldInfo != null) {
                    oldInfo.updateDynamicallyConfigurableProperties(info);
                    setDynamicallyConfigurableProperties(oldInfo);
                } else {
                    setDynamicallyConfigurableProperties(info);
                }
            }
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = service;
            mServiceInterface = IAccessibilityServiceClient.Stub.asInterface(service);
=======
        public void setServiceInfo(AccessibilityServiceInfo info) {
            mCaller.obtainMessageOO(DO_SET_SERVICE_INFO, info, this).sendToTarget();
        }

        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = service;
            mServiceInterface = IEventListener.Stub.asInterface(service);
>>>>>>> upstream/master
            try {
                mServiceInterface.setConnection(this, mId);
                synchronized (mLock) {
                    tryAddServiceLocked(this);
                }
            } catch (RemoteException re) {
                Slog.w(LOG_TAG, "Error while setting Controller for service: " + service, re);
            }
        }

<<<<<<< HEAD
        @Override
        public float findAccessibilityNodeInfoByViewId(int accessibilityWindowId,
                long accessibilityNodeId, int viewId, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid)
                throws RemoteException {
            final int resolvedWindowId = resolveAccessibilityWindowId(accessibilityWindowId);
            final int windowLeft;
            final int windowTop;
=======
        public float findAccessibilityNodeInfoByViewIdInActiveWindow(int viewId,
                int interactionId, IAccessibilityInteractionConnectionCallback callback,
                long interrogatingTid)
                throws RemoteException {
>>>>>>> upstream/master
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                mSecurityPolicy.enforceCanRetrieveWindowContent(this);
                final boolean permissionGranted = mSecurityPolicy.canRetrieveWindowContent(this);
                if (!permissionGranted) {
                    return 0;
                } else {
<<<<<<< HEAD
                    connection = getConnectionLocked(resolvedWindowId);
                    if (connection == null) {
                        return 0;
                    }
                }
                IBinder token = mWindowIdToWindowTokenMap.get(resolvedWindowId);
                mWindowManagerService.getWindowFrame(token, mTempBounds);
                windowLeft = mTempBounds.left;
                windowTop = mTempBounds.top;
            }
            final int flags = (mIncludeNotImportantViews) ?
                    AccessibilityNodeInfo.INCLUDE_NOT_IMPORTANT_VIEWS : 0;
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findAccessibilityNodeInfoByViewId(accessibilityNodeId, viewId,
                        windowLeft, windowTop, interactionId, callback, flags, interrogatingPid,
                        interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error findAccessibilityNodeInfoByViewId().");
=======
                    connection = getConnectionToRetrievalAllowingWindowLocked();
                    if (connection == null) {
                        if (DEBUG) {
                            Slog.e(LOG_TAG, "No interaction connection to a retrieve "
                                    + "allowing window.");
                        }
                        return 0;
                    }
                }
            }
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findAccessibilityNodeInfoByViewId(viewId, interactionId, callback,
                        interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error finding node.");
>>>>>>> upstream/master
                }
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
<<<<<<< HEAD
            return getCompatibilityScale(resolvedWindowId);
        }

        @Override
        public float findAccessibilityNodeInfosByText(int accessibilityWindowId,
                long accessibilityNodeId, String text, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid)
                throws RemoteException {
            final int resolvedWindowId = resolveAccessibilityWindowId(accessibilityWindowId);
            final int windowLeft;
            final int windowTop;
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                mSecurityPolicy.enforceCanRetrieveWindowContent(this);
                final boolean permissionGranted =
                    mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this, resolvedWindowId);
                if (!permissionGranted) {
                    return 0;
                } else {
                    connection = getConnectionLocked(resolvedWindowId);
                    if (connection == null) {
                        return 0;
                    }
                }
                IBinder token = mWindowIdToWindowTokenMap.get(resolvedWindowId);
                mWindowManagerService.getWindowFrame(token, mTempBounds);
                windowLeft = mTempBounds.left;
                windowTop = mTempBounds.top;
            }
            final int flags = (mIncludeNotImportantViews) ?
                    AccessibilityNodeInfo.INCLUDE_NOT_IMPORTANT_VIEWS : 0;
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findAccessibilityNodeInfosByText(accessibilityNodeId, text, windowLeft,
                        windowTop, interactionId, callback, flags, interrogatingPid,
                        interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error calling findAccessibilityNodeInfosByText()");
                }
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
            return getCompatibilityScale(resolvedWindowId);
        }

        @Override
        public float findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId,
                long accessibilityNodeId, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, int flags,
                long interrogatingTid) throws RemoteException {
            final int resolvedWindowId = resolveAccessibilityWindowId(accessibilityWindowId);
            final int windowLeft;
            final int windowTop;
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                mSecurityPolicy.enforceCanRetrieveWindowContent(this);
                final boolean permissionGranted =
                    mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this, resolvedWindowId);
                if (!permissionGranted) {
                    return 0;
                } else {
                    connection = getConnectionLocked(resolvedWindowId);
                    if (connection == null) {
                        return 0;
                    }
                }
                IBinder token = mWindowIdToWindowTokenMap.get(resolvedWindowId);
                mWindowManagerService.getWindowFrame(token, mTempBounds);
                windowLeft = mTempBounds.left;
                windowTop = mTempBounds.top;
            }
            final int allFlags = flags | ((mIncludeNotImportantViews) ?
                    AccessibilityNodeInfo.INCLUDE_NOT_IMPORTANT_VIEWS : 0);
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findAccessibilityNodeInfoByAccessibilityId(accessibilityNodeId,
                        windowLeft, windowTop, interactionId, callback, allFlags, interrogatingPid,
                        interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error calling findAccessibilityNodeInfoByAccessibilityId()");
                }
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
            return getCompatibilityScale(resolvedWindowId);
        }

        @Override
        public float findFocus(int accessibilityWindowId, long accessibilityNodeId,
                int focusType, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid)
                throws RemoteException {
            final int resolvedWindowId = resolveAccessibilityWindowId(accessibilityWindowId);
            final int windowLeft;
            final int windowTop;
=======
            return getCompatibilityScale(mSecurityPolicy.getRetrievalAllowingWindowLocked());
        }

        public float findAccessibilityNodeInfosByViewTextInActiveWindow(
                String text, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long threadId)
                throws RemoteException {
            return findAccessibilityNodeInfosByViewText(text,
                    mSecurityPolicy.mRetrievalAlowingWindowId, View.NO_ID, interactionId, callback,
                    threadId);
        }

        public float findAccessibilityNodeInfosByViewText(String text,
                int accessibilityWindowId, int accessibilityViewId, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid)
                throws RemoteException {
>>>>>>> upstream/master
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                mSecurityPolicy.enforceCanRetrieveWindowContent(this);
                final boolean permissionGranted =
<<<<<<< HEAD
                    mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this, resolvedWindowId);
                if (!permissionGranted) {
                    return 0;
                } else {
                    connection = getConnectionLocked(resolvedWindowId);
                    if (connection == null) {
                        return 0;
                    }
                }
                IBinder token = mWindowIdToWindowTokenMap.get(resolvedWindowId);
                mWindowManagerService.getWindowFrame(token, mTempBounds);
                windowLeft = mTempBounds.left;
                windowTop = mTempBounds.top;
            }
            final int flags = (mIncludeNotImportantViews) ?
                    AccessibilityNodeInfo.INCLUDE_NOT_IMPORTANT_VIEWS : 0;
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findFocus(accessibilityNodeId, focusType, windowLeft, windowTop,
                        interactionId, callback, flags, interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error calling findAccessibilityFocus()");
=======
                    mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this, accessibilityWindowId);
                if (!permissionGranted) {
                    return 0;
                } else {
                    connection = getConnectionToRetrievalAllowingWindowLocked();
                    if (connection == null) {
                        if (DEBUG) {
                            Slog.e(LOG_TAG, "No interaction connection to focused window.");
                        }
                        return 0;
                    }
                }
            }
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findAccessibilityNodeInfosByViewText(text, accessibilityViewId,
                        interactionId, callback, interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error finding node.");
>>>>>>> upstream/master
                }
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
<<<<<<< HEAD
            return getCompatibilityScale(resolvedWindowId);
        }

        @Override
        public float focusSearch(int accessibilityWindowId, long accessibilityNodeId,
                int direction, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid)
                throws RemoteException {
            final int resolvedWindowId = resolveAccessibilityWindowId(accessibilityWindowId);
            final int windowLeft;
            final int windowTop;
=======
            return getCompatibilityScale(accessibilityWindowId);
        }

        public float findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId,
                int accessibilityViewId, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid)
                throws RemoteException {
>>>>>>> upstream/master
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                mSecurityPolicy.enforceCanRetrieveWindowContent(this);
                final boolean permissionGranted =
<<<<<<< HEAD
                    mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this, resolvedWindowId);
                if (!permissionGranted) {
                    return 0;
                } else {
                    connection = getConnectionLocked(resolvedWindowId);
                    if (connection == null) {
                        return 0;
                    }
                }
                IBinder token = mWindowIdToWindowTokenMap.get(resolvedWindowId);
                mWindowManagerService.getWindowFrame(token, mTempBounds);
                windowLeft = mTempBounds.left;
                windowTop = mTempBounds.top;
            }
            final int flags = (mIncludeNotImportantViews) ?
                    AccessibilityNodeInfo.INCLUDE_NOT_IMPORTANT_VIEWS : 0;
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.focusSearch(accessibilityNodeId, direction, windowLeft, windowTop,
                        interactionId, callback, flags, interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error calling accessibilityFocusSearch()");
=======
                    mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this, accessibilityWindowId);
                if (!permissionGranted) {
                    return 0;
                } else {
                    AccessibilityConnectionWrapper wrapper =
                        mWindowIdToInteractionConnectionWrapperMap.get(accessibilityWindowId);
                    if (wrapper == null) {
                        if (DEBUG) {
                            Slog.e(LOG_TAG, "No interaction connection to window: "
                                    + accessibilityWindowId);
                        }
                        return 0;
                    }
                    connection = wrapper.mConnection;
                }
            }
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.findAccessibilityNodeInfoByAccessibilityId(accessibilityViewId,
                        interactionId, callback, interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error requesting node with accessibilityViewId: "
                            + accessibilityViewId);
>>>>>>> upstream/master
                }
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
<<<<<<< HEAD
            return getCompatibilityScale(resolvedWindowId);
        }

        @Override
        public boolean performAccessibilityAction(int accessibilityWindowId,
                long accessibilityNodeId, int action, Bundle arguments, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) {
            final int resolvedWindowId = resolveAccessibilityWindowId(accessibilityWindowId);
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                final boolean permissionGranted = mSecurityPolicy.canPerformActionLocked(this,
                        resolvedWindowId, action, arguments);
                if (!permissionGranted) {
                    return false;
                } else {
                    connection = getConnectionLocked(resolvedWindowId);
                    if (connection == null) {
                        return false;
                    }
                }
            }
            final int flags = (mIncludeNotImportantViews) ?
                    AccessibilityNodeInfo.INCLUDE_NOT_IMPORTANT_VIEWS : 0;
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.performAccessibilityAction(accessibilityNodeId, action, arguments,
                        interactionId, callback, flags, interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error calling performAccessibilityAction()");
=======
            return getCompatibilityScale(accessibilityWindowId);
        }

        public boolean performAccessibilityAction(int accessibilityWindowId,
                int accessibilityViewId, int action, int interactionId,
                IAccessibilityInteractionConnectionCallback callback, long interrogatingTid) {
            IAccessibilityInteractionConnection connection = null;
            synchronized (mLock) {
                final boolean permissionGranted = mSecurityPolicy.canPerformActionLocked(this,
                        accessibilityWindowId, action);
                if (!permissionGranted) {
                    return false;
                } else {
                    AccessibilityConnectionWrapper wrapper =
                        mWindowIdToInteractionConnectionWrapperMap.get(accessibilityWindowId);
                    if (wrapper == null) {
                        if (DEBUG) {
                            Slog.e(LOG_TAG, "No interaction connection to window: "
                                    + accessibilityWindowId);
                        }
                        return false;
                    }
                    connection = wrapper.mConnection;
                }
            }
            final int interrogatingPid = Binder.getCallingPid();
            final long identityToken = Binder.clearCallingIdentity();
            try {
                connection.performAccessibilityAction(accessibilityViewId, action, interactionId,
                        callback, interrogatingPid, interrogatingTid);
            } catch (RemoteException re) {
                if (DEBUG) {
                    Slog.e(LOG_TAG, "Error requesting node with accessibilityViewId: "
                            + accessibilityViewId);
>>>>>>> upstream/master
                }
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
            return true;
        }

<<<<<<< HEAD
        public boolean performGlobalAction(int action) {
            switch (action) {
                case AccessibilityService.GLOBAL_ACTION_BACK: {
                    sendDownAndUpKeyEvents(KeyEvent.KEYCODE_BACK);
                } return true;
                case AccessibilityService.GLOBAL_ACTION_HOME: {
                    sendDownAndUpKeyEvents(KeyEvent.KEYCODE_HOME);
                } return true;
                case AccessibilityService.GLOBAL_ACTION_RECENTS: {
                    openRecents();
                } return true;
                case AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS: {
                    expandStatusBar();
                } return true;
            }
            return false;
        }

=======
>>>>>>> upstream/master
        public void onServiceDisconnected(ComponentName componentName) {
            /* do nothing - #binderDied takes care */
        }

        public void linkToOwnDeath() throws RemoteException {
            mService.linkToDeath(this, 0);
        }

        public void unlinkToOwnDeath() {
            mService.unlinkToDeath(this, 0);
        }

        public void dispose() {
            try {
                // Clear the proxy in the other process so this
                // IAccessibilityServiceConnection can be garbage collected.
                mServiceInterface.setConnection(null, mId);
            } catch (RemoteException re) {
                /* ignore */
            }
            mService = null;
            mServiceInterface = null;
        }

        public void binderDied() {
            synchronized (mLock) {
<<<<<<< HEAD
                // The death recipient is unregistered in tryRemoveServiceLocked
=======
                unlinkToOwnDeath();
>>>>>>> upstream/master
                tryRemoveServiceLocked(this);
                // We no longer have an automation service, so restore
                // the state based on values in the settings database.
                if (mIsAutomation) {
<<<<<<< HEAD
                    mUiAutomationService = null;

                    populateEnabledAccessibilityServicesLocked();
                    populateTouchExplorationGrantedAccessibilityServicesLocked();

                    handleAccessibilityEnabledSettingChangedLocked();
                    sendStateToClientsLocked();

                    handleTouchExplorationEnabledSettingChangedLocked();
                    updateInputFilterLocked();

                    populateAccessibilityServiceListLocked();
                    manageServicesLocked();
                }
            }
        }

        /**
         * Performs a notification for an {@link AccessibilityEvent}.
         *
         * @param event The event.
         */
        public void notifyAccessibilityEvent(AccessibilityEvent event) {
            synchronized (mLock) {
                final int eventType = event.getEventType();
                // Make a copy since during dispatch it is possible the event to
                // be modified to remove its source if the receiving service does
                // not have permission to access the window content.
                AccessibilityEvent newEvent = AccessibilityEvent.obtain(event);
                AccessibilityEvent oldEvent = mPendingEvents.get(eventType);
                mPendingEvents.put(eventType, newEvent);

                final int what = eventType;
                if (oldEvent != null) {
                    mHandler.removeMessages(what);
                    oldEvent.recycle();
                }

                Message message = mHandler.obtainMessage(what);
                mHandler.sendMessageDelayed(message, mNotificationTimeout);
            }
        }

        /**
         * Notifies an accessibility service client for a scheduled event given the event type.
         *
         * @param eventType The type of the event to dispatch.
         */
        private void notifyAccessibilityEventInternal(int eventType) {
            IAccessibilityServiceClient listener;
            AccessibilityEvent event;

            synchronized (mLock) {
                listener = mServiceInterface;

                // If the service died/was disabled while the message for dispatching
                // the accessibility event was propagating the listener may be null.
                if (listener == null) {
                    return;
                }

                event = mPendingEvents.get(eventType);

                // Check for null here because there is a concurrent scenario in which this
                // happens: 1) A binder thread calls notifyAccessibilityServiceDelayedLocked
                // which posts a message for dispatching an event. 2) The message is pulled
                // from the queue by the handler on the service thread and the latter is
                // just about to acquire the lock and call this method. 3) Now another binder
                // thread acquires the lock calling notifyAccessibilityServiceDelayedLocked
                // so the service thread waits for the lock; 4) The binder thread replaces
                // the event with a more recent one (assume the same event type) and posts a
                // dispatch request releasing the lock. 5) Now the main thread is unblocked and
                // dispatches the event which is removed from the pending ones. 6) And ... now
                // the service thread handles the last message posted by the last binder call
                // but the event is already dispatched and hence looking it up in the pending
                // ones yields null. This check is much simpler that keeping count for each
                // event type of each service to catch such a scenario since only one message
                // is processed at a time.
                if (event == null) {
                    return;
                }

                mPendingEvents.remove(eventType);
                if (mSecurityPolicy.canRetrieveWindowContent(this)) {
                    event.setConnectionId(mId);
                } else {
                    event.setSource(null);
                }
                event.setSealed(true);
            }

            try {
                listener.onAccessibilityEvent(event);
                if (DEBUG) {
                    Slog.i(LOG_TAG, "Event " + event + " sent to " + listener);
                }
            } catch (RemoteException re) {
                Slog.e(LOG_TAG, "Error during sending " + event + " to " + listener, re);
            } finally {
                event.recycle();
            }
        }

        public void notifyGesture(int gestureId) {
            mHandler.obtainMessage(MSG_ON_GESTURE, gestureId, 0).sendToTarget();
        }

        private void notifyGestureInternal(int gestureId) {
            IAccessibilityServiceClient listener = mServiceInterface;
            if (listener != null) {
                try {
                    listener.onGesture(gestureId);
                } catch (RemoteException re) {
                    Slog.e(LOG_TAG, "Error during sending gesture " + gestureId
                            + " to " + mService, re);
=======
                    handleAccessibilityEnabledSettingChangedLocked();
>>>>>>> upstream/master
                }
            }
        }

<<<<<<< HEAD
        private void sendDownAndUpKeyEvents(int keyCode) {
            final long token = Binder.clearCallingIdentity();

            // Inject down.
            final long downTime = SystemClock.uptimeMillis();
            KeyEvent down = KeyEvent.obtain(downTime, downTime, KeyEvent.ACTION_DOWN, keyCode, 0, 0,
                    KeyCharacterMap.VIRTUAL_KEYBOARD, 0, KeyEvent.FLAG_FROM_SYSTEM,
                    InputDevice.SOURCE_KEYBOARD, null);
            InputManager.getInstance().injectInputEvent(down,
                    InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
            down.recycle();

            // Inject up.
            final long upTime = SystemClock.uptimeMillis();
            KeyEvent up = KeyEvent.obtain(downTime, upTime, KeyEvent.ACTION_UP, keyCode, 0, 0,
                    KeyCharacterMap.VIRTUAL_KEYBOARD, 0, KeyEvent.FLAG_FROM_SYSTEM,
                    InputDevice.SOURCE_KEYBOARD, null);
            InputManager.getInstance().injectInputEvent(up,
                    InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
            up.recycle();

            Binder.restoreCallingIdentity(token);
        }

        private void expandStatusBar() {
            final long token = Binder.clearCallingIdentity();

            StatusBarManager statusBarManager = (StatusBarManager) mContext.getSystemService(
                    android.app.Service.STATUS_BAR_SERVICE);
            statusBarManager.expand();

            Binder.restoreCallingIdentity(token);
        }

        private void openRecents() {
            final long token = Binder.clearCallingIdentity();

            IStatusBarService statusBarService = IStatusBarService.Stub.asInterface(
                    ServiceManager.getService("statusbar"));
            try {
                statusBarService.toggleRecentApps();
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error toggling recent apps.");
            }

            Binder.restoreCallingIdentity(token);
        }

        private IAccessibilityInteractionConnection getConnectionLocked(int windowId) {
            if (DEBUG) {
                Slog.i(LOG_TAG, "Trying to get interaction connection to windowId: " + windowId);
            }
            AccessibilityConnectionWrapper wrapper = mWindowIdToInteractionConnectionWrapperMap.get(
                    windowId);
            if (wrapper != null && wrapper.mConnection != null) {
                return wrapper.mConnection;
            }
            if (DEBUG) {
                Slog.e(LOG_TAG, "No interaction connection to window: " + windowId);
            }
            return null;
        }

        private int resolveAccessibilityWindowId(int accessibilityWindowId) {
            if (accessibilityWindowId == AccessibilityNodeInfo.ACTIVE_WINDOW_ID) {
                return mSecurityPolicy.mActiveWindowId;
            }
            return accessibilityWindowId;
=======
        private IAccessibilityInteractionConnection getConnectionToRetrievalAllowingWindowLocked() {
            final int windowId = mSecurityPolicy.getRetrievalAllowingWindowLocked();
            if (DEBUG) {
                Slog.i(LOG_TAG, "Trying to get interaction connection to windowId: " + windowId);
            }
            AccessibilityConnectionWrapper wrapper =
                mWindowIdToInteractionConnectionWrapperMap.get(windowId);
            return (wrapper != null) ? wrapper.mConnection : null;
>>>>>>> upstream/master
        }

        private float getCompatibilityScale(int windowId) {
            IBinder windowToken = mWindowIdToWindowTokenMap.get(windowId);
            return mWindowManagerService.getWindowCompatibilityScale(windowToken);
        }
    }

    final class SecurityPolicy {
<<<<<<< HEAD
        private static final int VALID_ACTIONS =
            AccessibilityNodeInfo.ACTION_CLICK
            | AccessibilityNodeInfo.ACTION_LONG_CLICK
            | AccessibilityNodeInfo.ACTION_FOCUS
            | AccessibilityNodeInfo.ACTION_CLEAR_FOCUS
            | AccessibilityNodeInfo.ACTION_SELECT
            | AccessibilityNodeInfo.ACTION_CLEAR_SELECTION
            | AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS
            | AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS
            | AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY
            | AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY
            | AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT
            | AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT
            | AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
            | AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD;

        private static final int RETRIEVAL_ALLOWING_EVENT_TYPES =
            AccessibilityEvent.TYPE_VIEW_CLICKED
            | AccessibilityEvent.TYPE_VIEW_FOCUSED
            | AccessibilityEvent.TYPE_VIEW_HOVER_ENTER
            | AccessibilityEvent.TYPE_VIEW_HOVER_EXIT
            | AccessibilityEvent.TYPE_VIEW_LONG_CLICKED
            | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
            | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            | AccessibilityEvent.TYPE_VIEW_SELECTED
            | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED
            | AccessibilityEvent.TYPE_VIEW_SCROLLED
            | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED
            | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED;

        private int mActiveWindowId;
=======
        private static final int VALID_ACTIONS = AccessibilityNodeInfo.ACTION_FOCUS
            | AccessibilityNodeInfo.ACTION_CLEAR_FOCUS | AccessibilityNodeInfo.ACTION_SELECT
            | AccessibilityNodeInfo.ACTION_CLEAR_SELECTION;

        private static final int RETRIEVAL_ALLOWING_EVENT_TYPES =
            AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED
            | AccessibilityEvent.TYPE_VIEW_HOVER_ENTER | AccessibilityEvent.TYPE_VIEW_HOVER_EXIT
            | AccessibilityEvent.TYPE_VIEW_LONG_CLICKED | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
            | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_SELECTED
            | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED
            | AccessibilityEvent.TYPE_VIEW_SCROLLED;

        private static final int RETRIEVAL_ALLOWING_WINDOW_CHANGE_EVENT_TYPES =
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_HOVER_ENTER
            | AccessibilityEvent.TYPE_VIEW_HOVER_EXIT;

        private int mRetrievalAlowingWindowId;
>>>>>>> upstream/master

        private boolean canDispatchAccessibilityEvent(AccessibilityEvent event) {
            // Send window changed event only for the retrieval allowing window.
            return (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
<<<<<<< HEAD
                    || event.getWindowId() == mActiveWindowId);
        }

        public void updateActiveWindowAndEventSourceLocked(AccessibilityEvent event) {
            // The active window is either the window that has input focus or
            // the window that the user is currently touching. If the user is
            // touching a window that does not have input focus as soon as the
            // the user stops touching that window the focused window becomes
            // the active one.
            final int windowId = event.getWindowId();
            final int eventType = event.getEventType();
            switch (eventType) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                    if (getFocusedWindowId() == windowId) {
                        mActiveWindowId = windowId;
                    }
                } break;
                case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT: {
                    mActiveWindowId = windowId;
                } break;
                case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END: {
                    mActiveWindowId = getFocusedWindowId();
                } break;
=======
                    || event.getWindowId() == mRetrievalAlowingWindowId);
        }

        public void updateRetrievalAllowingWindowAndEventSourceLocked(AccessibilityEvent event) {
            final int windowId = event.getWindowId();
            final int eventType = event.getEventType();
            if ((eventType & RETRIEVAL_ALLOWING_WINDOW_CHANGE_EVENT_TYPES) != 0) {
                mRetrievalAlowingWindowId = windowId;
>>>>>>> upstream/master
            }
            if ((eventType & RETRIEVAL_ALLOWING_EVENT_TYPES) == 0) {
                event.setSource(null);
            }
        }

        public int getRetrievalAllowingWindowLocked() {
<<<<<<< HEAD
            return mActiveWindowId;
=======
            return mRetrievalAlowingWindowId;
>>>>>>> upstream/master
        }

        public boolean canGetAccessibilityNodeInfoLocked(Service service, int windowId) {
            return canRetrieveWindowContent(service) && isRetrievalAllowingWindow(windowId);
        }

<<<<<<< HEAD
        public boolean canPerformActionLocked(Service service, int windowId, int action,
                Bundle arguments) {
=======
        public boolean canPerformActionLocked(Service service, int windowId, int action) {
>>>>>>> upstream/master
            return canRetrieveWindowContent(service)
                && isRetrievalAllowingWindow(windowId)
                && isActionPermitted(action);
        }

        public boolean canRetrieveWindowContent(Service service) {
            return service.mCanRetrieveScreenContent;
        }

        public void enforceCanRetrieveWindowContent(Service service) throws RemoteException {
            // This happens due to incorrect registration so make it apparent.
            if (!canRetrieveWindowContent(service)) {
                Slog.e(LOG_TAG, "Accessibility serivce " + service.mComponentName + " does not " +
                        "declare android:canRetrieveWindowContent.");
                throw new RemoteException();
            }
        }

        private boolean isRetrievalAllowingWindow(int windowId) {
<<<<<<< HEAD
            return (mActiveWindowId == windowId);
=======
            return (mRetrievalAlowingWindowId == windowId);
>>>>>>> upstream/master
        }

        private boolean isActionPermitted(int action) {
             return (VALID_ACTIONS & action) != 0;
        }

        private void enforceCallingPermission(String permission, String function) {
            if (OWN_PROCESS_ID == Binder.getCallingPid()) {
                return;
            }
            final int permissionStatus = mContext.checkCallingPermission(permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                throw new SecurityException("You do not have " + permission
                        + " required to call " + function);
            }
        }
<<<<<<< HEAD

        private int getFocusedWindowId() {
            // We call this only on window focus change or after touch
            // exploration gesture end and the shown windows are not that
            // many, so the linear look up is just fine.
            IBinder token = mWindowManagerService.getFocusedWindowClientToken();
            if (token != null) {
                SparseArray<IBinder> windows = mWindowIdToWindowTokenMap;
                final int windowCount = windows.size();
                for (int i = 0; i < windowCount; i++) {
                    if (windows.valueAt(i) == token) {
                        return windows.keyAt(i);
                    }
                }
            }
            return -1;
        }
=======
>>>>>>> upstream/master
    }
}
