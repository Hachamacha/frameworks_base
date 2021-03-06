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

package android.net.wifi;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;

/**
 * TODO: Add soft AP states as part of WIFI_STATE_XXX
 * Retain WIFI_STATE_ENABLING that indicates driver is loading
 * Add WIFI_STATE_AP_ENABLED to indicate soft AP has started
 * and WIFI_STATE_FAILED for failure
 * Deprecate WIFI_STATE_UNKNOWN
 *
 * Doing this will simplify the logic for sending broadcasts
 */
import static android.net.wifi.WifiManager.WIFI_AP_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_AP_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_AP_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_AP_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_AP_STATE_FAILED;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.backup.IBackupManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.pm.PackageManager;
=======
>>>>>>> upstream/master
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.DhcpInfoInternal;
import android.net.DhcpStateMachine;
import android.net.InterfaceConfiguration;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkUtils;
import android.net.wifi.WpsResult.Status;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pService;
import android.net.wifi.StateChangeResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.Settings;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;

import com.android.internal.app.IBatteryStats;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Protocol;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * Track the state of Wifi connectivity. All event handling is done here,
 * and all changes in connectivity state are initiated here.
 *
 * Wi-Fi now supports three modes of operation: Client, Soft Ap and Direct
 * In the current implementation, we do not support any concurrency and thus only
 * one of Client, Soft Ap or Direct operation is supported at any time.
 *
 * The WifiStateMachine supports Soft Ap and Client operations while WifiP2pService
 * handles Direct. WifiP2pService and WifiStateMachine co-ordinate to ensure only
 * one exists at a certain time.
 *
 * @hide
 */
public class WifiStateMachine extends StateMachine {

    private static final String TAG = "WifiStateMachine";
    private static final String NETWORKTYPE = "WIFI";
    private static final boolean DBG = false;

    /* TODO: This is no more used with the hostapd code. Clean up */
    private static final String SOFTAP_IFACE = "wl0.1";

    private WifiMonitor mWifiMonitor;
<<<<<<< HEAD
    private WifiNative mWifiNative;
    private WifiConfigStore mWifiConfigStore;
    private INetworkManagementService mNwService;
    private ConnectivityManager mCm;

    private final boolean mP2pSupported;
    private final String mPrimaryDeviceType;

=======
    private INetworkManagementService mNwService;
    private ConnectivityManager mCm;

>>>>>>> upstream/master
    /* Scan results handling */
    private List<ScanResult> mScanResults;
    private static final Pattern scanResultPattern = Pattern.compile("\t+");
    private static final int SCAN_RESULT_CACHE_SIZE = 80;
    private final LruCache<String, ScanResult> mScanResultCache;

<<<<<<< HEAD
    /* Chipset supports background scan */
    private final boolean mBackgroundScanSupported;

=======
>>>>>>> upstream/master
    private String mInterfaceName;
    /* Tethering interface could be seperate from wlan interface */
    private String mTetherInterfaceName;

    private int mLastSignalLevel = -1;
    private String mLastBssid;
    private int mLastNetworkId;
    private boolean mEnableRssiPolling = false;
    private boolean mEnableBackgroundScan = false;
    private int mRssiPollToken = 0;
    private int mReconnectCount = 0;
    private boolean mIsScanMode = false;
    private boolean mScanResultIsPending = false;
<<<<<<< HEAD
    /* Tracks if the current scan settings are active */
    private boolean mSetScanActive = false;
    /* High perf mode is true if an app has held a high perf Wifi Lock */
    private boolean mHighPerfMode = false;

    private boolean mBluetoothConnectionActive = false;

    private BroadcastReceiver mScreenReceiver;
    private IntentFilter mScreenFilter;
    private PowerManager.WakeLock mSuspendWakeLock;

=======

    private boolean mBluetoothConnectionActive = false;

>>>>>>> upstream/master
    /**
     * Interval in milliseconds between polling for RSSI
     * and linkspeed information
     */
    private static final int POLL_RSSI_INTERVAL_MSECS = 3000;

    /**
     * Delay between supplicant restarts upon failure to establish connection
     */
    private static final int SUPPLICANT_RESTART_INTERVAL_MSECS = 5000;

    /**
     * Number of times we attempt to restart supplicant
     */
    private static final int SUPPLICANT_RESTART_TRIES = 5;

    private int mSupplicantRestartCount = 0;
    /* Tracks sequence number on stop failure message */
    private int mSupplicantStopFailureToken = 0;

    /**
     * Tether state change notification time out
     */
    private static final int TETHER_NOTIFICATION_TIME_OUT_MSECS = 5000;

    /* Tracks sequence number on a tether notification time out */
    private int mTetherToken = 0;

<<<<<<< HEAD
    /**
     * Driver start time out.
     */
    private static final int DRIVER_START_TIME_OUT_MSECS = 10000;

    /* Tracks sequence number on a driver time out */
    private int mDriverStartToken = 0;

    private LinkProperties mLinkProperties;

    /* Tracks sequence number on a periodic scan message */
    private int mPeriodicScanToken = 0;

=======
    private LinkProperties mLinkProperties;

>>>>>>> upstream/master
    // Wakelock held during wifi start/stop and driver load/unload
    private PowerManager.WakeLock mWakeLock;

    private Context mContext;

    private DhcpInfoInternal mDhcpInfoInternal;
    private WifiInfo mWifiInfo;
    private NetworkInfo mNetworkInfo;
    private SupplicantStateTracker mSupplicantStateTracker;
<<<<<<< HEAD
=======
    private WpsStateMachine mWpsStateMachine;
>>>>>>> upstream/master
    private DhcpStateMachine mDhcpStateMachine;

    private AlarmManager mAlarmManager;
    private PendingIntent mScanIntent;
<<<<<<< HEAD
    private PendingIntent mDriverStopIntent;

=======
>>>>>>> upstream/master
    /* Tracks current frequency mode */
    private AtomicInteger mFrequencyBand = new AtomicInteger(WifiManager.WIFI_FREQUENCY_BAND_AUTO);

    /* Tracks if we are filtering Multicast v4 packets. Default is to filter. */
    private AtomicBoolean mFilteringMulticastV4Packets = new AtomicBoolean(true);

    // Channel for sending replies.
    private AsyncChannel mReplyChannel = new AsyncChannel();

    private WifiP2pManager mWifiP2pManager;
    //Used to initiate a connection with WifiP2pService
    private AsyncChannel mWifiP2pChannel = new AsyncChannel();
    private AsyncChannel mWifiApConfigChannel = new AsyncChannel();

    // Event log tags (must be in sync with event-log-tags)
    private static final int EVENTLOG_WIFI_STATE_CHANGED        = 50021;
    private static final int EVENTLOG_WIFI_EVENT_HANDLED        = 50022;
    private static final int EVENTLOG_SUPPLICANT_STATE_CHANGED  = 50023;

    /* The base for wifi message types */
    static final int BASE = Protocol.BASE_WIFI;
    /* Load the driver */
    static final int CMD_LOAD_DRIVER                      = BASE + 1;
    /* Unload the driver */
    static final int CMD_UNLOAD_DRIVER                    = BASE + 2;
    /* Indicates driver load succeeded */
    static final int CMD_LOAD_DRIVER_SUCCESS              = BASE + 3;
    /* Indicates driver load failed */
    static final int CMD_LOAD_DRIVER_FAILURE              = BASE + 4;
    /* Indicates driver unload succeeded */
    static final int CMD_UNLOAD_DRIVER_SUCCESS            = BASE + 5;
    /* Indicates driver unload failed */
    static final int CMD_UNLOAD_DRIVER_FAILURE            = BASE + 6;

    /* Start the supplicant */
    static final int CMD_START_SUPPLICANT                 = BASE + 11;
    /* Stop the supplicant */
    static final int CMD_STOP_SUPPLICANT                  = BASE + 12;
    /* Start the driver */
    static final int CMD_START_DRIVER                     = BASE + 13;
    /* Stop the driver */
    static final int CMD_STOP_DRIVER                      = BASE + 14;
    /* Indicates Static IP succeded */
    static final int CMD_STATIC_IP_SUCCESS                = BASE + 15;
    /* Indicates Static IP failed */
    static final int CMD_STATIC_IP_FAILURE                = BASE + 16;
    /* Indicates supplicant stop failed */
    static final int CMD_STOP_SUPPLICANT_FAILED           = BASE + 17;
    /* Delayed stop to avoid shutting down driver too quick*/
    static final int CMD_DELAYED_STOP_DRIVER              = BASE + 18;
<<<<<<< HEAD
    /* A delayed message sent to start driver when it fail to come up */
    static final int CMD_DRIVER_START_TIMED_OUT           = BASE + 19;
=======

>>>>>>> upstream/master

    /* Start the soft access point */
    static final int CMD_START_AP                         = BASE + 21;
    /* Indicates soft ap start succeded */
    static final int CMD_START_AP_SUCCESS                 = BASE + 22;
    /* Indicates soft ap start failed */
    static final int CMD_START_AP_FAILURE                 = BASE + 23;
    /* Stop the soft access point */
    static final int CMD_STOP_AP                          = BASE + 24;
    /* Set the soft access point configuration */
    static final int CMD_SET_AP_CONFIG                    = BASE + 25;
    /* Soft access point configuration set completed */
    static final int CMD_SET_AP_CONFIG_COMPLETED          = BASE + 26;
    /* Request the soft access point configuration */
    static final int CMD_REQUEST_AP_CONFIG                = BASE + 27;
    /* Response to access point configuration request */
    static final int CMD_RESPONSE_AP_CONFIG               = BASE + 28;
    /* Invoked when getting a tether state change notification */
    static final int CMD_TETHER_STATE_CHANGE              = BASE + 29;
    /* A delayed message sent to indicate tether state change failed to arrive */
    static final int CMD_TETHER_NOTIFICATION_TIMED_OUT    = BASE + 30;

    static final int CMD_BLUETOOTH_ADAPTER_STATE_CHANGE   = BASE + 31;

    /* Supplicant commands */
    /* Is supplicant alive ? */
    static final int CMD_PING_SUPPLICANT                  = BASE + 51;
    /* Add/update a network configuration */
    static final int CMD_ADD_OR_UPDATE_NETWORK            = BASE + 52;
    /* Delete a network */
    static final int CMD_REMOVE_NETWORK                   = BASE + 53;
    /* Enable a network. The device will attempt a connection to the given network. */
    static final int CMD_ENABLE_NETWORK                   = BASE + 54;
    /* Enable all networks */
    static final int CMD_ENABLE_ALL_NETWORKS              = BASE + 55;
<<<<<<< HEAD
    /* Blacklist network. De-prioritizes the given BSSID for connection. */
    static final int CMD_BLACKLIST_NETWORK                = BASE + 56;
    /* Clear the blacklist network list */
    static final int CMD_CLEAR_BLACKLIST                  = BASE + 57;
    /* Save configuration */
    static final int CMD_SAVE_CONFIG                      = BASE + 58;
    /* Get configured networks*/
    static final int CMD_GET_CONFIGURED_NETWORKS          = BASE + 59;
=======
    /* Disable a network. The device does not attempt a connection to the given network. */
    static final int CMD_DISABLE_NETWORK                  = BASE + 56;
    /* Blacklist network. De-prioritizes the given BSSID for connection. */
    static final int CMD_BLACKLIST_NETWORK                = BASE + 57;
    /* Clear the blacklist network list */
    static final int CMD_CLEAR_BLACKLIST                  = BASE + 58;
    /* Save configuration */
    static final int CMD_SAVE_CONFIG                      = BASE + 59;
>>>>>>> upstream/master

    /* Supplicant commands after driver start*/
    /* Initiate a scan */
    static final int CMD_START_SCAN                       = BASE + 71;
    /* Set scan mode. CONNECT_MODE or SCAN_ONLY_MODE */
    static final int CMD_SET_SCAN_MODE                    = BASE + 72;
    /* Set scan type. SCAN_ACTIVE or SCAN_PASSIVE */
    static final int CMD_SET_SCAN_TYPE                    = BASE + 73;
    /* Disconnect from a network */
    static final int CMD_DISCONNECT                       = BASE + 74;
    /* Reconnect to a network */
    static final int CMD_RECONNECT                        = BASE + 75;
    /* Reassociate to a network */
    static final int CMD_REASSOCIATE                      = BASE + 76;
<<<<<<< HEAD
    /* Controls suspend mode optimizations
     *
     * When high perf mode is enabled, suspend mode optimizations are disabled
     *
     * When high perf mode is disabled, suspend mode optimizations are enabled
=======
    /* Controls power mode and suspend mode optimizations
     *
     * When high perf mode is enabled, power mode is set to
     * POWER_MODE_ACTIVE and suspend mode optimizations are disabled
     *
     * When high perf mode is disabled, power mode is set to
     * POWER_MODE_AUTO and suspend mode optimizations are enabled
>>>>>>> upstream/master
     *
     * Suspend mode optimizations include:
     * - packet filtering
     * - turn off roaming
     * - DTIM wake up settings
     */
    static final int CMD_SET_HIGH_PERF_MODE               = BASE + 77;
    /* Set the country code */
    static final int CMD_SET_COUNTRY_CODE                 = BASE + 80;
    /* Enables RSSI poll */
    static final int CMD_ENABLE_RSSI_POLL                 = BASE + 82;
    /* RSSI poll */
    static final int CMD_RSSI_POLL                        = BASE + 83;
    /* Set up packet filtering */
    static final int CMD_START_PACKET_FILTERING           = BASE + 84;
    /* Clear packet filter */
    static final int CMD_STOP_PACKET_FILTERING            = BASE + 85;
<<<<<<< HEAD
    /* Set suspend mode optimizations in the driver */
    static final int CMD_SET_SUSPEND_OPTIMIZATIONS        = BASE + 86;
    /* Clear suspend mode optimizations in the driver */
    static final int CMD_CLEAR_SUSPEND_OPTIMIZATIONS      = BASE + 87;
    /* When there are no saved networks, we do a periodic scan to notify user of
     * an open network */
    static final int CMD_NO_NETWORKS_PERIODIC_SCAN        = BASE + 88;
=======
>>>>>>> upstream/master

    /* arg1 values to CMD_STOP_PACKET_FILTERING and CMD_START_PACKET_FILTERING */
    static final int MULTICAST_V6  = 1;
    static final int MULTICAST_V4  = 0;

<<<<<<< HEAD
   /* Set the frequency band */
=======
    /* Connect to a specified network (network id
     * or WifiConfiguration) This involves increasing
     * the priority of the network, enabling the network
     * (while disabling others) and issuing a reconnect.
     * Note that CMD_RECONNECT just does a reconnect to
     * an existing network. All the networks get enabled
     * upon a successful connection or a failure.
     */
    static final int CMD_CONNECT_NETWORK                  = BASE + 86;
    /* Save the specified network. This involves adding
     * an enabled network (if new) and updating the
     * config and issuing a save on supplicant config.
     */
    static final int CMD_SAVE_NETWORK                     = BASE + 87;
    /* Delete the specified network. This involves
     * removing the network and issuing a save on
     * supplicant config.
     */
    static final int CMD_FORGET_NETWORK                   = BASE + 88;
    /* Start Wi-Fi protected setup */
    static final int CMD_START_WPS                        = BASE + 89;
    /* Set the frequency band */
>>>>>>> upstream/master
    static final int CMD_SET_FREQUENCY_BAND               = BASE + 90;
    /* Enable background scan for configured networks */
    static final int CMD_ENABLE_BACKGROUND_SCAN           = BASE + 91;

    /* Commands from/to the SupplicantStateTracker */
    /* Reset the supplicant state tracker */
    static final int CMD_RESET_SUPPLICANT_STATE           = BASE + 111;

<<<<<<< HEAD
    /* P2p commands */
    public static final int CMD_ENABLE_P2P                = BASE + 131;
    public static final int CMD_DISABLE_P2P               = BASE + 132;
=======
    /* Commands/events reported by WpsStateMachine */
    /* Indicates the completion of WPS activity */
    static final int WPS_COMPLETED_EVENT                  = BASE + 121;
    /* Reset the WPS state machine */
    static final int CMD_RESET_WPS_STATE                  = BASE + 122;

    /* Interaction with WifiP2pService */
    public static final int WIFI_ENABLE_PENDING           = BASE + 131;
    public static final int P2P_ENABLE_PROCEED            = BASE + 132;
>>>>>>> upstream/master

    private static final int CONNECT_MODE   = 1;
    private static final int SCAN_ONLY_MODE = 2;

    private static final int SCAN_ACTIVE = 1;
    private static final int SCAN_PASSIVE = 2;

    private static final int SUCCESS = 1;
    private static final int FAILURE = -1;

    /* Phone in emergency call back mode */
    private static final int IN_ECM_STATE = 1;
    private static final int NOT_IN_ECM_STATE = 0;

    /**
     * The maximum number of times we will retry a connection to an access point
     * for which we have failed in acquiring an IP address from DHCP. A value of
     * N means that we will make N+1 connection attempts in all.
     * <p>
     * See {@link Settings.Secure#WIFI_MAX_DHCP_RETRY_COUNT}. This is the default
     * value if a Settings value is not present.
     */
    private static final int DEFAULT_MAX_DHCP_RETRIES = 9;

<<<<<<< HEAD
    /* Tracks if power save is enabled in driver */
    private boolean mPowerSaveEnabled = true;;
=======
    static final int POWER_MODE_ACTIVE = 1;
    static final int POWER_MODE_AUTO = 0;

    /* Tracks the power mode for restoration after a DHCP request/renewal goes through */
    private int mPowerMode = POWER_MODE_AUTO;
>>>>>>> upstream/master

    /**
     * Default framework scan interval in milliseconds. This is used in the scenario in which
     * wifi chipset does not support background scanning to set up a
     * periodic wake up scan so that the device can connect to a new access
     * point on the move. {@link Settings.Secure#WIFI_FRAMEWORK_SCAN_INTERVAL_MS} can
     * override this.
     */
    private final int mDefaultFrameworkScanIntervalMs;

    /**
<<<<<<< HEAD
     * Supplicant scan interval in milliseconds.
     * Comes from {@link Settings.Secure#WIFI_SUPPLICANT_SCAN_INTERVAL_MS} or
     * from the default config if the setting is not set
     */
    private long mSupplicantScanIntervalMs;
=======
     * Default supplicant scan interval in milliseconds.
     * {@link Settings.Secure#WIFI_SUPPLICANT_SCAN_INTERVAL_MS} can override this.
     */
    private final int mDefaultSupplicantScanIntervalMs;
>>>>>>> upstream/master

    /**
     * Minimum time interval between enabling all networks.
     * A device can end up repeatedly connecting to a bad network on screen on/off toggle
     * due to enabling every time. We add a threshold to avoid this.
     */
    private static final int MIN_INTERVAL_ENABLE_ALL_NETWORKS_MS = 10 * 60 * 1000; /* 10 minutes */
    private long mLastEnableAllNetworksTime;

    /**
     * Starting and shutting down driver too quick causes problems leading to driver
     * being in a bad state. Delay driver stop.
     */
<<<<<<< HEAD
    private final int mDriverStopDelayMs;
=======
    private static final int DELAYED_DRIVER_STOP_MS = 2 * 60 * 1000; /* 2 minutes */
>>>>>>> upstream/master
    private int mDelayedStopCounter;
    private boolean mInDelayedStop = false;

    private static final int MIN_RSSI = -200;
    private static final int MAX_RSSI = 256;

    /* Default parent state */
    private State mDefaultState = new DefaultState();
    /* Temporary initial state */
    private State mInitialState = new InitialState();
    /* Unloading the driver */
    private State mDriverUnloadingState = new DriverUnloadingState();
    /* Loading the driver */
    private State mDriverUnloadedState = new DriverUnloadedState();
    /* Driver load/unload failed */
    private State mDriverFailedState = new DriverFailedState();
    /* Driver loading */
    private State mDriverLoadingState = new DriverLoadingState();
    /* Driver loaded */
    private State mDriverLoadedState = new DriverLoadedState();
    /* Driver loaded, waiting for supplicant to start */
    private State mSupplicantStartingState = new SupplicantStartingState();
    /* Driver loaded and supplicant ready */
    private State mSupplicantStartedState = new SupplicantStartedState();
    /* Waiting for supplicant to stop and monitor to exit */
    private State mSupplicantStoppingState = new SupplicantStoppingState();
    /* Driver start issued, waiting for completed event */
    private State mDriverStartingState = new DriverStartingState();
    /* Driver started */
    private State mDriverStartedState = new DriverStartedState();
    /* Driver stopping */
    private State mDriverStoppingState = new DriverStoppingState();
    /* Driver stopped */
    private State mDriverStoppedState = new DriverStoppedState();
    /* Scan for networks, no connection will be established */
    private State mScanModeState = new ScanModeState();
    /* Connecting to an access point */
    private State mConnectModeState = new ConnectModeState();
<<<<<<< HEAD
    /* Connected at 802.11 (L2) level */
    private State mL2ConnectedState = new L2ConnectedState();
    /* fetching IP after connection to access point (assoc+auth complete) */
    private State mObtainingIpState = new ObtainingIpState();
    /* Waiting for link quality verification to be complete */
    private State mVerifyingLinkState = new VerifyingLinkState();
=======
    /* Fetching IP after network connection (assoc+auth complete) */
    private State mConnectingState = new ConnectingState();
>>>>>>> upstream/master
    /* Connected with IP addr */
    private State mConnectedState = new ConnectedState();
    /* disconnect issued, waiting for network disconnect confirmation */
    private State mDisconnectingState = new DisconnectingState();
    /* Network is not connected, supplicant assoc+auth is not complete */
    private State mDisconnectedState = new DisconnectedState();
    /* Waiting for WPS to be completed*/
<<<<<<< HEAD
    private State mWpsRunningState = new WpsRunningState();
=======
    private State mWaitForWpsCompletionState = new WaitForWpsCompletionState();
>>>>>>> upstream/master

    /* Soft ap is starting up */
    private State mSoftApStartingState = new SoftApStartingState();
    /* Soft ap is running */
    private State mSoftApStartedState = new SoftApStartedState();
    /* Soft ap is running and we are waiting for tether notification */
    private State mTetheringState = new TetheringState();
    /* Soft ap is running and we are tethered through connectivity service */
    private State mTetheredState = new TetheredState();
    /* Waiting for untether confirmation to stop soft Ap */
    private State mSoftApStoppingState = new SoftApStoppingState();

<<<<<<< HEAD
=======
    /* Wait till p2p is disabled */
    private State mWaitForP2pDisableState = new WaitForP2pDisableState();

>>>>>>> upstream/master
    private class TetherStateChange {
        ArrayList<String> available;
        ArrayList<String> active;
        TetherStateChange(ArrayList<String> av, ArrayList<String> ac) {
            available = av;
            active = ac;
        }
    }


    /**
     * One of  {@link WifiManager#WIFI_STATE_DISABLED},
     *         {@link WifiManager#WIFI_STATE_DISABLING},
     *         {@link WifiManager#WIFI_STATE_ENABLED},
     *         {@link WifiManager#WIFI_STATE_ENABLING},
     *         {@link WifiManager#WIFI_STATE_UNKNOWN}
     *
     */
    private final AtomicInteger mWifiState = new AtomicInteger(WIFI_STATE_DISABLED);

    /**
     * One of  {@link WifiManager#WIFI_AP_STATE_DISABLED},
     *         {@link WifiManager#WIFI_AP_STATE_DISABLING},
     *         {@link WifiManager#WIFI_AP_STATE_ENABLED},
     *         {@link WifiManager#WIFI_AP_STATE_ENABLING},
     *         {@link WifiManager#WIFI_AP_STATE_FAILED}
     *
     */
    private final AtomicInteger mWifiApState = new AtomicInteger(WIFI_AP_STATE_DISABLED);

    private final AtomicInteger mLastEnableUid = new AtomicInteger(Process.myUid());
    private final AtomicInteger mLastApEnableUid = new AtomicInteger(Process.myUid());

    private static final int SCAN_REQUEST = 0;
    private static final String ACTION_START_SCAN =
        "com.android.server.WifiManager.action.START_SCAN";

<<<<<<< HEAD
    private static final String DELAYED_STOP_COUNTER = "DelayedStopCounter";
    private static final int DRIVER_STOP_REQUEST = 0;
    private static final String ACTION_DELAYED_DRIVER_STOP =
        "com.android.server.WifiManager.action.DELAYED_DRIVER_STOP";

=======
>>>>>>> upstream/master
    /**
     * Keep track of whether WIFI is running.
     */
    private boolean mIsRunning = false;

    /**
     * Keep track of whether we last told the battery stats we had started.
     */
    private boolean mReportedRunning = false;

    /**
     * Most recently set source of starting WIFI.
     */
    private final WorkSource mRunningWifiUids = new WorkSource();

    /**
     * The last reported UIDs that were responsible for starting WIFI.
     */
    private final WorkSource mLastRunningWifiUids = new WorkSource();

    private final IBatteryStats mBatteryStats;
<<<<<<< HEAD
=======
    private boolean mNextWifiActionExplicit = false;
    private int mLastExplicitNetworkId;
    private long mLastNetworkChoiceTime;
    private static final long EXPLICIT_CONNECT_ALLOWED_DELAY_MS = 2 * 60 * 1000;

>>>>>>> upstream/master

    public WifiStateMachine(Context context, String wlanInterface) {
        super(TAG);

        mContext = context;
        mInterfaceName = wlanInterface;

        mNetworkInfo = new NetworkInfo(ConnectivityManager.TYPE_WIFI, 0, NETWORKTYPE, "");
        mBatteryStats = IBatteryStats.Stub.asInterface(ServiceManager.getService("batteryinfo"));

        IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
        mNwService = INetworkManagementService.Stub.asInterface(b);

<<<<<<< HEAD
        mP2pSupported = mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_WIFI_DIRECT);

        mWifiNative = new WifiNative(mInterfaceName);
        mWifiConfigStore = new WifiConfigStore(context, mWifiNative);
        mWifiMonitor = new WifiMonitor(this, mWifiNative);
        mDhcpInfoInternal = new DhcpInfoInternal();
        mWifiInfo = new WifiInfo();
        mSupplicantStateTracker = new SupplicantStateTracker(context, this, mWifiConfigStore,
                getHandler());
=======
        mWifiMonitor = new WifiMonitor(this);
        mDhcpInfoInternal = new DhcpInfoInternal();
        mWifiInfo = new WifiInfo();
        mSupplicantStateTracker = new SupplicantStateTracker(context, this, getHandler());
        mWpsStateMachine = new WpsStateMachine(context, this, getHandler());
>>>>>>> upstream/master
        mLinkProperties = new LinkProperties();

        WifiApConfigStore wifiApConfigStore = WifiApConfigStore.makeWifiApConfigStore(
                context, getHandler());
        wifiApConfigStore.loadApConfiguration();
        mWifiApConfigChannel.connectSync(mContext, getHandler(), wifiApConfigStore.getMessenger());

        mNetworkInfo.setIsAvailable(false);
        mLinkProperties.clear();
        mLastBssid = null;
        mLastNetworkId = WifiConfiguration.INVALID_NETWORK_ID;
        mLastSignalLevel = -1;

        mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent scanIntent = new Intent(ACTION_START_SCAN, null);
        mScanIntent = PendingIntent.getBroadcast(mContext, SCAN_REQUEST, scanIntent, 0);

        mDefaultFrameworkScanIntervalMs = mContext.getResources().getInteger(
                com.android.internal.R.integer.config_wifi_framework_scan_interval);

<<<<<<< HEAD
        mDriverStopDelayMs = mContext.getResources().getInteger(
                com.android.internal.R.integer.config_wifi_driver_stop_delay);

        mBackgroundScanSupported = mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_wifi_background_scan_support);

        mPrimaryDeviceType = mContext.getResources().getString(
                com.android.internal.R.string.config_wifi_p2p_device_type);
=======
        mDefaultSupplicantScanIntervalMs = mContext.getResources().getInteger(
                com.android.internal.R.integer.config_wifi_supplicant_scan_interval);
>>>>>>> upstream/master

        mContext.registerReceiver(
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ArrayList<String> available = intent.getStringArrayListExtra(
                            ConnectivityManager.EXTRA_AVAILABLE_TETHER);
                    ArrayList<String> active = intent.getStringArrayListExtra(
                            ConnectivityManager.EXTRA_ACTIVE_TETHER);
                    sendMessage(CMD_TETHER_STATE_CHANGE, new TetherStateChange(available, active));
                }
            },new IntentFilter(ConnectivityManager.ACTION_TETHER_STATE_CHANGED));

        mContext.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        startScan(false);
                    }
                },
                new IntentFilter(ACTION_START_SCAN));

<<<<<<< HEAD
        mScreenFilter = new IntentFilter();
        mScreenFilter.addAction(Intent.ACTION_SCREEN_ON);
        mScreenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    enableRssiPolling(true);
                    if (mBackgroundScanSupported) {
                        enableBackgroundScanCommand(false);
                    }
                    enableAllNetworks();
                    sendMessage(CMD_CLEAR_SUSPEND_OPTIMIZATIONS);
                } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    enableRssiPolling(false);
                    if (mBackgroundScanSupported) {
                        enableBackgroundScanCommand(true);
                    }
                    //Allow 2s for suspend optimizations to be set
                    mSuspendWakeLock.acquire(2000);
                    sendMessage(CMD_SET_SUSPEND_OPTIMIZATIONS);
                }
            }
        };
        mContext.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                       int counter = intent.getIntExtra(DELAYED_STOP_COUNTER, 0);
                       sendMessage(obtainMessage(CMD_DELAYED_STOP_DRIVER, counter, 0));
                    }
                },
                new IntentFilter(ACTION_DELAYED_DRIVER_STOP));

=======
>>>>>>> upstream/master
        mScanResultCache = new LruCache<String, ScanResult>(SCAN_RESULT_CACHE_SIZE);

        PowerManager powerManager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

<<<<<<< HEAD
        mSuspendWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WifiSuspend");
        mSuspendWakeLock.setReferenceCounted(false);

=======
>>>>>>> upstream/master
        addState(mDefaultState);
            addState(mInitialState, mDefaultState);
            addState(mDriverUnloadingState, mDefaultState);
            addState(mDriverUnloadedState, mDefaultState);
                addState(mDriverFailedState, mDriverUnloadedState);
            addState(mDriverLoadingState, mDefaultState);
            addState(mDriverLoadedState, mDefaultState);
            addState(mSupplicantStartingState, mDefaultState);
            addState(mSupplicantStartedState, mDefaultState);
                addState(mDriverStartingState, mSupplicantStartedState);
                addState(mDriverStartedState, mSupplicantStartedState);
                    addState(mScanModeState, mDriverStartedState);
                    addState(mConnectModeState, mDriverStartedState);
<<<<<<< HEAD
                        addState(mL2ConnectedState, mConnectModeState);
                            addState(mObtainingIpState, mL2ConnectedState);
                            addState(mVerifyingLinkState, mL2ConnectedState);
                            addState(mConnectedState, mL2ConnectedState);
                        addState(mDisconnectingState, mConnectModeState);
                        addState(mDisconnectedState, mConnectModeState);
                        addState(mWpsRunningState, mConnectModeState);
=======
                        addState(mConnectingState, mConnectModeState);
                        addState(mConnectedState, mConnectModeState);
                        addState(mDisconnectingState, mConnectModeState);
                        addState(mDisconnectedState, mConnectModeState);
                        addState(mWaitForWpsCompletionState, mConnectModeState);
>>>>>>> upstream/master
                addState(mDriverStoppingState, mSupplicantStartedState);
                addState(mDriverStoppedState, mSupplicantStartedState);
            addState(mSupplicantStoppingState, mDefaultState);
            addState(mSoftApStartingState, mDefaultState);
            addState(mSoftApStartedState, mDefaultState);
                addState(mTetheringState, mSoftApStartedState);
                addState(mTetheredState, mSoftApStartedState);
            addState(mSoftApStoppingState, mDefaultState);
<<<<<<< HEAD

        setInitialState(mInitialState);

        setProcessedMessagesSize(100);
=======
            addState(mWaitForP2pDisableState, mDefaultState);

        setInitialState(mInitialState);

>>>>>>> upstream/master
        if (DBG) setDbg(true);

        //start the state machine
        start();
    }

    /*********************************************************
     * Methods exposed for public use
     ********************************************************/

<<<<<<< HEAD
    public Messenger getMessenger() {
        return new Messenger(getHandler());
    }
=======
>>>>>>> upstream/master
    /**
     * TODO: doc
     */
    public boolean syncPingSupplicant(AsyncChannel channel) {
        Message resultMsg = channel.sendMessageSynchronously(CMD_PING_SUPPLICANT);
        boolean result = (resultMsg.arg1 != FAILURE);
        resultMsg.recycle();
        return result;
    }

    /**
     * TODO: doc
     */
    public void startScan(boolean forceActive) {
        sendMessage(obtainMessage(CMD_START_SCAN, forceActive ?
                SCAN_ACTIVE : SCAN_PASSIVE, 0));
    }

    /**
     * TODO: doc
     */
    public void setWifiEnabled(boolean enable) {
        mLastEnableUid.set(Binder.getCallingUid());
        if (enable) {
            /* Argument is the state that is entered prior to load */
            sendMessage(obtainMessage(CMD_LOAD_DRIVER, WIFI_STATE_ENABLING, 0));
            sendMessage(CMD_START_SUPPLICANT);
        } else {
            sendMessage(CMD_STOP_SUPPLICANT);
            /* Argument is the state that is entered upon success */
            sendMessage(obtainMessage(CMD_UNLOAD_DRIVER, WIFI_STATE_DISABLED, 0));
        }
    }

    /**
     * TODO: doc
     */
    public void setWifiApEnabled(WifiConfiguration wifiConfig, boolean enable) {
        mLastApEnableUid.set(Binder.getCallingUid());
        if (enable) {
            /* Argument is the state that is entered prior to load */
            sendMessage(obtainMessage(CMD_LOAD_DRIVER, WIFI_AP_STATE_ENABLING, 0));
            sendMessage(obtainMessage(CMD_START_AP, wifiConfig));
        } else {
            sendMessage(CMD_STOP_AP);
            /* Argument is the state that is entered upon success */
            sendMessage(obtainMessage(CMD_UNLOAD_DRIVER, WIFI_AP_STATE_DISABLED, 0));
        }
    }

    public void setWifiApConfiguration(WifiConfiguration config) {
        mWifiApConfigChannel.sendMessage(CMD_SET_AP_CONFIG, config);
    }

    public WifiConfiguration syncGetWifiApConfiguration() {
        Message resultMsg = mWifiApConfigChannel.sendMessageSynchronously(CMD_REQUEST_AP_CONFIG);
        WifiConfiguration ret = (WifiConfiguration) resultMsg.obj;
        resultMsg.recycle();
        return ret;
    }

    /**
     * TODO: doc
     */
    public int syncGetWifiState() {
        return mWifiState.get();
    }

    /**
     * TODO: doc
     */
    public String syncGetWifiStateByName() {
        switch (mWifiState.get()) {
            case WIFI_STATE_DISABLING:
                return "disabling";
            case WIFI_STATE_DISABLED:
                return "disabled";
            case WIFI_STATE_ENABLING:
                return "enabling";
            case WIFI_STATE_ENABLED:
                return "enabled";
            case WIFI_STATE_UNKNOWN:
                return "unknown state";
            default:
                return "[invalid state]";
        }
    }

    /**
     * TODO: doc
     */
    public int syncGetWifiApState() {
        return mWifiApState.get();
    }

    /**
     * TODO: doc
     */
    public String syncGetWifiApStateByName() {
        switch (mWifiApState.get()) {
            case WIFI_AP_STATE_DISABLING:
                return "disabling";
            case WIFI_AP_STATE_DISABLED:
                return "disabled";
            case WIFI_AP_STATE_ENABLING:
                return "enabling";
            case WIFI_AP_STATE_ENABLED:
                return "enabled";
            case WIFI_AP_STATE_FAILED:
                return "failed";
            default:
                return "[invalid state]";
        }
    }

    /**
     * Get status information for the current connection, if any.
     * @return a {@link WifiInfo} object containing information about the current connection
     *
     */
    public WifiInfo syncRequestConnectionInfo() {
        return mWifiInfo;
    }

    public DhcpInfo syncGetDhcpInfo() {
        synchronized (mDhcpInfoInternal) {
            return mDhcpInfoInternal.makeDhcpInfo();
        }
    }

    /**
     * TODO: doc
     */
    public void setDriverStart(boolean enable, boolean ecm) {
        if (enable) {
            sendMessage(CMD_START_DRIVER);
        } else {
            sendMessage(obtainMessage(CMD_STOP_DRIVER, ecm ? IN_ECM_STATE : NOT_IN_ECM_STATE, 0));
        }
    }

    /**
     * TODO: doc
     */
    public void setScanOnlyMode(boolean enable) {
      if (enable) {
          sendMessage(obtainMessage(CMD_SET_SCAN_MODE, SCAN_ONLY_MODE, 0));
      } else {
          sendMessage(obtainMessage(CMD_SET_SCAN_MODE, CONNECT_MODE, 0));
      }
    }

    /**
     * TODO: doc
     */
    public void setScanType(boolean active) {
      if (active) {
          sendMessage(obtainMessage(CMD_SET_SCAN_TYPE, SCAN_ACTIVE, 0));
      } else {
          sendMessage(obtainMessage(CMD_SET_SCAN_TYPE, SCAN_PASSIVE, 0));
      }
    }

    /**
     * TODO: doc
     */
    public List<ScanResult> syncGetScanResultsList() {
        return mScanResults;
    }

    /**
     * Disconnect from Access Point
     */
    public void disconnectCommand() {
        sendMessage(CMD_DISCONNECT);
    }

    /**
     * Initiate a reconnection to AP
     */
    public void reconnectCommand() {
        sendMessage(CMD_RECONNECT);
    }

    /**
     * Initiate a re-association to AP
     */
    public void reassociateCommand() {
        sendMessage(CMD_REASSOCIATE);
    }

    /**
     * Add a network synchronously
     *
     * @return network id of the new network
     */
    public int syncAddOrUpdateNetwork(AsyncChannel channel, WifiConfiguration config) {
        Message resultMsg = channel.sendMessageSynchronously(CMD_ADD_OR_UPDATE_NETWORK, config);
        int result = resultMsg.arg1;
        resultMsg.recycle();
        return result;
    }

<<<<<<< HEAD
    public List<WifiConfiguration> syncGetConfiguredNetworks(AsyncChannel channel) {
        Message resultMsg = channel.sendMessageSynchronously(CMD_GET_CONFIGURED_NETWORKS);
        List<WifiConfiguration> result = (List<WifiConfiguration>) resultMsg.obj;
        resultMsg.recycle();
        return result;
=======
    public List<WifiConfiguration> syncGetConfiguredNetworks() {
        return WifiConfigStore.getConfiguredNetworks();
>>>>>>> upstream/master
    }

    /**
     * Delete a network
     *
     * @param networkId id of the network to be removed
     */
    public boolean syncRemoveNetwork(AsyncChannel channel, int networkId) {
        Message resultMsg = channel.sendMessageSynchronously(CMD_REMOVE_NETWORK, networkId);
        boolean result = (resultMsg.arg1 != FAILURE);
        resultMsg.recycle();
        return result;
    }

    /**
     * Enable a network
     *
     * @param netId network id of the network
     * @param disableOthers true, if all other networks have to be disabled
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     */
    public boolean syncEnableNetwork(AsyncChannel channel, int netId, boolean disableOthers) {
        Message resultMsg = channel.sendMessageSynchronously(CMD_ENABLE_NETWORK, netId,
                disableOthers ? 1 : 0);
        boolean result = (resultMsg.arg1 != FAILURE);
        resultMsg.recycle();
        return result;
    }

    /**
     * Disable a network
     *
     * @param netId network id of the network
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     */
    public boolean syncDisableNetwork(AsyncChannel channel, int netId) {
<<<<<<< HEAD
        Message resultMsg = channel.sendMessageSynchronously(WifiManager.DISABLE_NETWORK, netId);
        boolean result = (resultMsg.arg1 != WifiManager.DISABLE_NETWORK_FAILED);
=======
        Message resultMsg = channel.sendMessageSynchronously(CMD_DISABLE_NETWORK, netId,
                WifiConfiguration.DISABLED_UNKNOWN_REASON);
        boolean result = (resultMsg.arg1 != FAILURE);
>>>>>>> upstream/master
        resultMsg.recycle();
        return result;
    }

    /**
     * Blacklist a BSSID. This will avoid the AP if there are
     * alternate APs to connect
     *
     * @param bssid BSSID of the network
     */
    public void addToBlacklist(String bssid) {
        sendMessage(obtainMessage(CMD_BLACKLIST_NETWORK, bssid));
    }

    /**
     * Clear the blacklist list
     *
     */
    public void clearBlacklist() {
        sendMessage(obtainMessage(CMD_CLEAR_BLACKLIST));
    }

<<<<<<< HEAD
=======
    public void connectNetwork(int netId) {
        sendMessage(obtainMessage(CMD_CONNECT_NETWORK, netId, 0));
    }

    public void connectNetwork(WifiConfiguration wifiConfig) {
        /* arg1 is used to indicate netId, force a netId value of
         * WifiConfiguration.INVALID_NETWORK_ID when we are passing
         * a configuration since the default value of 0 is a valid netId
         */
        sendMessage(obtainMessage(CMD_CONNECT_NETWORK, WifiConfiguration.INVALID_NETWORK_ID,
                0, wifiConfig));
    }

    public void saveNetwork(WifiConfiguration wifiConfig) {
        sendMessage(obtainMessage(CMD_SAVE_NETWORK, wifiConfig));
    }

    public void forgetNetwork(int netId) {
        sendMessage(obtainMessage(CMD_FORGET_NETWORK, netId, 0));
    }

    public void disableNetwork(Messenger replyTo, int netId, int reason) {
        Message message = obtainMessage(CMD_DISABLE_NETWORK, netId, reason);
        message.replyTo = replyTo;
        sendMessage(message);
    }

    public void startWps(Messenger replyTo, WpsInfo config) {
        Message msg = obtainMessage(CMD_START_WPS, config);
        msg.replyTo = replyTo;
        sendMessage(msg);
    }

>>>>>>> upstream/master
    public void enableRssiPolling(boolean enabled) {
       sendMessage(obtainMessage(CMD_ENABLE_RSSI_POLL, enabled ? 1 : 0, 0));
    }

    public void enableBackgroundScanCommand(boolean enabled) {
       sendMessage(obtainMessage(CMD_ENABLE_BACKGROUND_SCAN, enabled ? 1 : 0, 0));
    }

    public void enableAllNetworks() {
        sendMessage(CMD_ENABLE_ALL_NETWORKS);
    }

    /**
     * Start filtering Multicast v4 packets
     */
    public void startFilteringMulticastV4Packets() {
        mFilteringMulticastV4Packets.set(true);
        sendMessage(obtainMessage(CMD_START_PACKET_FILTERING, MULTICAST_V4, 0));
    }

    /**
     * Stop filtering Multicast v4 packets
     */
    public void stopFilteringMulticastV4Packets() {
        mFilteringMulticastV4Packets.set(false);
        sendMessage(obtainMessage(CMD_STOP_PACKET_FILTERING, MULTICAST_V4, 0));
    }

    /**
     * Start filtering Multicast v4 packets
     */
    public void startFilteringMulticastV6Packets() {
        sendMessage(obtainMessage(CMD_START_PACKET_FILTERING, MULTICAST_V6, 0));
    }

    /**
     * Stop filtering Multicast v4 packets
     */
    public void stopFilteringMulticastV6Packets() {
        sendMessage(obtainMessage(CMD_STOP_PACKET_FILTERING, MULTICAST_V6, 0));
    }

    /**
     * Set high performance mode of operation.
     * Enabling would set active power mode and disable suspend optimizations;
     * disabling would set auto power mode and enable suspend optimizations
     * @param enable true if enable, false otherwise
     */
    public void setHighPerfModeEnabled(boolean enable) {
        sendMessage(obtainMessage(CMD_SET_HIGH_PERF_MODE, enable ? 1 : 0, 0));
    }

    /**
     * Set the country code
     * @param countryCode following ISO 3166 format
     * @param persist {@code true} if the setting should be remembered.
     */
    public void setCountryCode(String countryCode, boolean persist) {
        if (persist) {
            Settings.Secure.putString(mContext.getContentResolver(),
                    Settings.Secure.WIFI_COUNTRY_CODE,
                    countryCode);
        }
        sendMessage(obtainMessage(CMD_SET_COUNTRY_CODE, countryCode));
    }

    /**
     * Set the operational frequency band
     * @param band
     * @param persist {@code true} if the setting should be remembered.
     */
    public void setFrequencyBand(int band, boolean persist) {
        if (persist) {
            Settings.Secure.putInt(mContext.getContentResolver(),
                    Settings.Secure.WIFI_FREQUENCY_BAND,
                    band);
        }
        sendMessage(obtainMessage(CMD_SET_FREQUENCY_BAND, band, 0));
    }

    /**
     * Returns the operational frequency band
     */
    public int getFrequencyBand() {
        return mFrequencyBand.get();
    }

    /**
     * Returns the wifi configuration file
     */
    public String getConfigFile() {
<<<<<<< HEAD
        return mWifiConfigStore.getConfigFile();
=======
        return WifiConfigStore.getConfigFile();
>>>>>>> upstream/master
    }

    /**
     * Send a message indicating bluetooth adapter connection state changed
     */
    public void sendBluetoothAdapterStateChange(int state) {
        sendMessage(obtainMessage(CMD_BLUETOOTH_ADAPTER_STATE_CHANGE, state, 0));
    }

    /**
     * Save configuration on supplicant
     *
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     *
     * TODO: deprecate this
     */
    public boolean syncSaveConfig(AsyncChannel channel) {
        Message resultMsg = channel.sendMessageSynchronously(CMD_SAVE_CONFIG);
        boolean result = (resultMsg.arg1 != FAILURE);
        resultMsg.recycle();
        return result;
    }

    public void updateBatteryWorkSource(WorkSource newSource) {
        synchronized (mRunningWifiUids) {
            try {
                if (newSource != null) {
                    mRunningWifiUids.set(newSource);
                }
                if (mIsRunning) {
                    if (mReportedRunning) {
                        // If the work source has changed since last time, need
                        // to remove old work from battery stats.
                        if (mLastRunningWifiUids.diff(mRunningWifiUids)) {
                            mBatteryStats.noteWifiRunningChanged(mLastRunningWifiUids,
                                    mRunningWifiUids);
                            mLastRunningWifiUids.set(mRunningWifiUids);
                        }
                    } else {
                        // Now being started, report it.
                        mBatteryStats.noteWifiRunning(mRunningWifiUids);
                        mLastRunningWifiUids.set(mRunningWifiUids);
                        mReportedRunning = true;
                    }
                } else {
                    if (mReportedRunning) {
                        // Last reported we were running, time to stop.
                        mBatteryStats.noteWifiStopped(mLastRunningWifiUids);
                        mLastRunningWifiUids.clear();
                        mReportedRunning = false;
                    }
                }
                mWakeLock.setWorkSource(newSource);
            } catch (RemoteException ignore) {
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String LS = System.getProperty("line.separator");
        sb.append("current HSM state: ").append(getCurrentState().getName()).append(LS);
        sb.append("mLinkProperties ").append(mLinkProperties).append(LS);
        sb.append("mWifiInfo ").append(mWifiInfo).append(LS);
        sb.append("mDhcpInfoInternal ").append(mDhcpInfoInternal).append(LS);
        sb.append("mNetworkInfo ").append(mNetworkInfo).append(LS);
        sb.append("mLastSignalLevel ").append(mLastSignalLevel).append(LS);
        sb.append("mLastBssid ").append(mLastBssid).append(LS);
        sb.append("mLastNetworkId ").append(mLastNetworkId).append(LS);
        sb.append("mReconnectCount ").append(mReconnectCount).append(LS);
        sb.append("mIsScanMode ").append(mIsScanMode).append(LS);
        sb.append("Supplicant status").append(LS)
<<<<<<< HEAD
                .append(mWifiNative.status()).append(LS).append(LS);

        sb.append(mWifiConfigStore.dump());
        return sb.toString();
    }

    @Override
    protected boolean recordProcessedMessage(Message msg) {
        //Ignore screen on/off & common messages when driver has started
        if (getCurrentState() == mConnectedState || getCurrentState() == mDisconnectedState) {
            switch (msg.what) {
                case CMD_LOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_START_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_HIGH_PERF_MODE:
                case CMD_SET_SUSPEND_OPTIMIZATIONS:
                case CMD_CLEAR_SUSPEND_OPTIMIZATIONS:
                case CMD_ENABLE_BACKGROUND_SCAN:
                case CMD_ENABLE_ALL_NETWORKS:
                return false;
            }
        }

        switch (msg.what) {
            case CMD_START_SCAN:
            case CMD_ENABLE_RSSI_POLL:
            case CMD_RSSI_POLL:
            case CMD_DELAYED_STOP_DRIVER:
            case WifiMonitor.SCAN_RESULTS_EVENT:
            case WifiWatchdogStateMachine.RSSI_FETCH:
                return false;
            default:
                return true;
        }
    }

=======
                .append(WifiNative.statusCommand()).append(LS).append(LS);

        sb.append(WifiConfigStore.dump());
        return sb.toString();
    }

>>>>>>> upstream/master
    /*********************************************************
     * Internal private functions
     ********************************************************/

    private void checkAndSetConnectivityInstance() {
        if (mCm == null) {
            mCm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }

    private boolean startTethering(ArrayList<String> available) {

        boolean wifiAvailable = false;

        checkAndSetConnectivityInstance();

        String[] wifiRegexs = mCm.getTetherableWifiRegexs();

        for (String intf : available) {
            for (String regex : wifiRegexs) {
                if (intf.matches(regex)) {

                    InterfaceConfiguration ifcg = null;
                    try {
                        ifcg = mNwService.getInterfaceConfig(intf);
                        if (ifcg != null) {
                            /* IP/netmask: 192.168.43.1/255.255.255.0 */
<<<<<<< HEAD
                            ifcg.setLinkAddress(new LinkAddress(
                                    NetworkUtils.numericToInetAddress("192.168.43.1"), 24));
                            ifcg.setInterfaceUp();
=======
                            ifcg.addr = new LinkAddress(NetworkUtils.numericToInetAddress(
                                    "192.168.43.1"), 24);
                            ifcg.interfaceFlags = "[up]";
>>>>>>> upstream/master

                            mNwService.setInterfaceConfig(intf, ifcg);
                        }
                    } catch (Exception e) {
                        loge("Error configuring interface " + intf + ", :" + e);
                        return false;
                    }

                    if(mCm.tether(intf) != ConnectivityManager.TETHER_ERROR_NO_ERROR) {
                        loge("Error tethering on " + intf);
                        return false;
                    }
                    mTetherInterfaceName = intf;
                    return true;
                }
            }
        }
        // We found no interfaces to tether
        return false;
    }

    private void stopTethering() {

        checkAndSetConnectivityInstance();

        /* Clear the interface config to allow dhcp correctly configure new
           ip settings */
        InterfaceConfiguration ifcg = null;
        try {
            ifcg = mNwService.getInterfaceConfig(mInterfaceName);
            if (ifcg != null) {
<<<<<<< HEAD
                ifcg.setLinkAddress(
                        new LinkAddress(NetworkUtils.numericToInetAddress("0.0.0.0"), 0));
=======
                ifcg.addr = new LinkAddress(NetworkUtils.numericToInetAddress(
                            "0.0.0.0"), 0);
>>>>>>> upstream/master
                mNwService.setInterfaceConfig(mInterfaceName, ifcg);
            }
        } catch (Exception e) {
            loge("Error resetting interface " + mInterfaceName + ", :" + e);
        }

        if (mCm.untether(mTetherInterfaceName) != ConnectivityManager.TETHER_ERROR_NO_ERROR) {
            loge("Untether initiate failed!");
        }
    }

    private boolean isWifiTethered(ArrayList<String> active) {

        checkAndSetConnectivityInstance();

        String[] wifiRegexs = mCm.getTetherableWifiRegexs();
        for (String intf : active) {
            for (String regex : wifiRegexs) {
                if (intf.matches(regex)) {
                    return true;
                }
            }
        }
        // We found no interfaces that are tethered
        return false;
    }

    /**
     * Set the country code from the system setting value, if any.
     */
    private void setCountryCode() {
        String countryCode = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.WIFI_COUNTRY_CODE);
        if (countryCode != null && !countryCode.isEmpty()) {
            setCountryCode(countryCode, false);
        } else {
            //use driver default
        }
    }

    /**
     * Set the frequency band from the system setting value, if any.
     */
    private void setFrequencyBand() {
        int band = Settings.Secure.getInt(mContext.getContentResolver(),
                Settings.Secure.WIFI_FREQUENCY_BAND, WifiManager.WIFI_FREQUENCY_BAND_AUTO);
        setFrequencyBand(band, false);
    }

    private void setWifiState(int wifiState) {
        final int previousWifiState = mWifiState.get();

        try {
            if (wifiState == WIFI_STATE_ENABLED) {
                mBatteryStats.noteWifiOn();
            } else if (wifiState == WIFI_STATE_DISABLED) {
                mBatteryStats.noteWifiOff();
            }
        } catch (RemoteException e) {
            loge("Failed to note battery stats in wifi");
        }

        mWifiState.set(wifiState);

        if (DBG) log("setWifiState: " + syncGetWifiStateByName());

        final Intent intent = new Intent(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_WIFI_STATE, wifiState);
        intent.putExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, previousWifiState);
        mContext.sendStickyBroadcast(intent);
    }

    private void setWifiApState(int wifiApState) {
        final int previousWifiApState = mWifiApState.get();

        try {
            if (wifiApState == WIFI_AP_STATE_ENABLED) {
                mBatteryStats.noteWifiOn();
            } else if (wifiApState == WIFI_AP_STATE_DISABLED) {
                mBatteryStats.noteWifiOff();
            }
        } catch (RemoteException e) {
            loge("Failed to note battery stats in wifi");
        }

        // Update state
        mWifiApState.set(wifiApState);

        if (DBG) log("setWifiApState: " + syncGetWifiApStateByName());

        final Intent intent = new Intent(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_WIFI_AP_STATE, wifiApState);
        intent.putExtra(WifiManager.EXTRA_PREVIOUS_WIFI_AP_STATE, previousWifiApState);
        mContext.sendStickyBroadcast(intent);
    }

    /**
     * Parse the scan result line passed to us by wpa_supplicant (helper).
     * @param line the line to parse
     * @return the {@link ScanResult} object
     */
    private ScanResult parseScanResult(String line) {
        ScanResult scanResult = null;
        if (line != null) {
            /*
             * Cache implementation (LinkedHashMap) is not synchronized, thus,
             * must synchronized here!
             */
            synchronized (mScanResultCache) {
                String[] result = scanResultPattern.split(line);
                if (3 <= result.length && result.length <= 5) {
                    String bssid = result[0];
                    // bssid | frequency | level | flags | ssid
                    int frequency;
                    int level;
                    try {
                        frequency = Integer.parseInt(result[1]);
                        level = Integer.parseInt(result[2]);
                        /* some implementations avoid negative values by adding 256
                         * so we need to adjust for that here.
                         */
                        if (level > 0) level -= 256;
                    } catch (NumberFormatException e) {
                        frequency = 0;
                        level = 0;
                    }

                    /*
                     * The formatting of the results returned by
                     * wpa_supplicant is intended to make the fields
                     * line up nicely when printed,
                     * not to make them easy to parse. So we have to
                     * apply some heuristics to figure out which field
                     * is the SSID and which field is the flags.
                     */
                    String ssid;
                    String flags;
                    if (result.length == 4) {
                        if (result[3].charAt(0) == '[') {
                            flags = result[3];
                            ssid = "";
                        } else {
                            flags = "";
                            ssid = result[3];
                        }
                    } else if (result.length == 5) {
                        flags = result[3];
                        ssid = result[4];
                    } else {
                        // Here, we must have 3 fields: no flags and ssid
                        // set
                        flags = "";
                        ssid = "";
                    }

                    // bssid + ssid is the hash key
                    String key = bssid + ssid;
                    scanResult = mScanResultCache.get(key);
                    if (scanResult != null) {
                        scanResult.level = level;
                        scanResult.SSID = ssid;
                        scanResult.capabilities = flags;
                        scanResult.frequency = frequency;
                    } else {
                        // Do not add scan results that have no SSID set
                        if (0 < ssid.trim().length()) {
                            scanResult =
                                new ScanResult(
                                    ssid, bssid, flags, level, frequency);
                            mScanResultCache.put(key, scanResult);
                        }
                    }
                } else {
                    loge("Misformatted scan result text with " +
                          result.length + " fields: " + line);
                }
            }
        }

        return scanResult;
    }

    /**
     * scanResults input format
     * 00:bb:cc:dd:cc:ee       2427    166     [WPA-EAP-TKIP][WPA2-EAP-CCMP]   Net1
     * 00:bb:cc:dd:cc:ff       2412    165     [WPA-EAP-TKIP][WPA2-EAP-CCMP]   Net2
     */
    private void setScanResults(String scanResults) {
        if (scanResults == null) {
            return;
        }

        List<ScanResult> scanList = new ArrayList<ScanResult>();

        int lineCount = 0;

        int scanResultsLen = scanResults.length();
        // Parse the result string, keeping in mind that the last line does
        // not end with a newline.
        for (int lineBeg = 0, lineEnd = 0; lineEnd <= scanResultsLen; ++lineEnd) {
            if (lineEnd == scanResultsLen || scanResults.charAt(lineEnd) == '\n') {
                ++lineCount;

                if (lineCount == 1) {
                    lineBeg = lineEnd + 1;
                    continue;
                }
                if (lineEnd > lineBeg) {
                    String line = scanResults.substring(lineBeg, lineEnd);
                    ScanResult scanResult = parseScanResult(line);
                    if (scanResult != null) {
                        scanList.add(scanResult);
                    } else {
                        //TODO: hidden network handling
                    }
                }
                lineBeg = lineEnd + 1;
            }
        }

        mScanResults = scanList;
    }

<<<<<<< HEAD
=======
    private String fetchSSID() {
        String status = WifiNative.statusCommand();
        if (status == null) {
            return null;
        }
        // extract ssid from a series of "name=value"
        String[] lines = status.split("\n");
        for (String line : lines) {
            String[] prop = line.split(" *= *");
            if (prop.length < 2) continue;
            String name = prop[0];
            String value = prop[1];
            if (name.equalsIgnoreCase("ssid")) return value;
        }
        return null;
    }

>>>>>>> upstream/master
    /*
     * Fetch RSSI and linkspeed on current connection
     */
    private void fetchRssiAndLinkSpeedNative() {
        int newRssi = -1;
        int newLinkSpeed = -1;

<<<<<<< HEAD
        String signalPoll = mWifiNative.signalPoll();
=======
        String signalPoll = WifiNative.signalPoll();
>>>>>>> upstream/master

        if (signalPoll != null) {
            String[] lines = signalPoll.split("\n");
            for (String line : lines) {
                String[] prop = line.split("=");
                if (prop.length < 2) continue;
                try {
                    if (prop[0].equals("RSSI")) {
                        newRssi = Integer.parseInt(prop[1]);
                    } else if (prop[0].equals("LINKSPEED")) {
                        newLinkSpeed = Integer.parseInt(prop[1]);
                    }
                } catch (NumberFormatException e) {
                    //Ignore, defaults on rssi and linkspeed are assigned
                }
            }
        }

        if (newRssi != -1 && MIN_RSSI < newRssi && newRssi < MAX_RSSI) { // screen out invalid values
            /* some implementations avoid negative values by adding 256
             * so we need to adjust for that here.
             */
            if (newRssi > 0) newRssi -= 256;
            mWifiInfo.setRssi(newRssi);
            /*
             * Rather then sending the raw RSSI out every time it
             * changes, we precalculate the signal level that would
             * be displayed in the status bar, and only send the
             * broadcast if that much more coarse-grained number
             * changes. This cuts down greatly on the number of
<<<<<<< HEAD
             * broadcasts, at the cost of not informing others
             * interested in RSSI of all the changes in signal
             * level.
             */
            int newSignalLevel = WifiManager.calculateSignalLevel(newRssi, WifiManager.RSSI_LEVELS);
=======
             * broadcasts, at the cost of not mWifiInforming others
             * interested in RSSI of all the changes in signal
             * level.
             */
            // TODO: The second arg to the call below needs to be a symbol somewhere, but
            // it's actually the size of an array of icons that's private
            // to StatusBar Policy.
            int newSignalLevel = WifiManager.calculateSignalLevel(newRssi, 4);
>>>>>>> upstream/master
            if (newSignalLevel != mLastSignalLevel) {
                sendRssiChangeBroadcast(newRssi);
            }
            mLastSignalLevel = newSignalLevel;
        } else {
            mWifiInfo.setRssi(MIN_RSSI);
        }

        if (newLinkSpeed != -1) {
            mWifiInfo.setLinkSpeed(newLinkSpeed);
        }
    }

<<<<<<< HEAD
    private void configureLinkProperties() {
        if (mWifiConfigStore.isUsingStaticIp(mLastNetworkId)) {
            mLinkProperties = mWifiConfigStore.getLinkProperties(mLastNetworkId);
=======
    private void setHighPerfModeEnabledNative(boolean enable) {
        if(!WifiNative.setSuspendOptimizationsCommand(!enable)) {
            loge("set suspend optimizations failed!");
        }
        if (enable) {
            if (!WifiNative.setPowerModeCommand(POWER_MODE_ACTIVE)) {
                loge("set power mode active failed!");
            }
        } else {
            if (!WifiNative.setPowerModeCommand(POWER_MODE_AUTO)) {
                loge("set power mode auto failed!");
            }
        }
    }

    private void configureLinkProperties() {
        if (WifiConfigStore.isUsingStaticIp(mLastNetworkId)) {
            mLinkProperties = WifiConfigStore.getLinkProperties(mLastNetworkId);
>>>>>>> upstream/master
        } else {
            synchronized (mDhcpInfoInternal) {
                mLinkProperties = mDhcpInfoInternal.makeLinkProperties();
            }
<<<<<<< HEAD
            mLinkProperties.setHttpProxy(mWifiConfigStore.getProxyProperties(mLastNetworkId));
=======
            mLinkProperties.setHttpProxy(WifiConfigStore.getProxyProperties(mLastNetworkId));
>>>>>>> upstream/master
        }
        mLinkProperties.setInterfaceName(mInterfaceName);
        if (DBG) {
            log("netId=" + mLastNetworkId  + " Link configured: " +
                    mLinkProperties.toString());
        }
    }

    private int getMaxDhcpRetries() {
        return Settings.Secure.getInt(mContext.getContentResolver(),
                                      Settings.Secure.WIFI_MAX_DHCP_RETRY_COUNT,
                                      DEFAULT_MAX_DHCP_RETRIES);
    }

    private void sendScanResultsAvailableBroadcast() {
        Intent intent = new Intent(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        mContext.sendBroadcast(intent);
    }

    private void sendRssiChangeBroadcast(final int newRssi) {
        Intent intent = new Intent(WifiManager.RSSI_CHANGED_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_NEW_RSSI, newRssi);
        mContext.sendBroadcast(intent);
    }

    private void sendNetworkStateChangeBroadcast(String bssid) {
        Intent intent = new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION);
<<<<<<< HEAD
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_NETWORK_INFO, new NetworkInfo(mNetworkInfo));
        intent.putExtra(WifiManager.EXTRA_LINK_PROPERTIES, new LinkProperties (mLinkProperties));
        if (bssid != null)
            intent.putExtra(WifiManager.EXTRA_BSSID, bssid);
        if (mNetworkInfo.getDetailedState() == DetailedState.VERIFYING_POOR_LINK ||
                mNetworkInfo.getDetailedState() == DetailedState.CONNECTED) {
            intent.putExtra(WifiManager.EXTRA_WIFI_INFO, new WifiInfo(mWifiInfo));
        }
        mContext.sendStickyBroadcast(intent);
    }

=======
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT
                | Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra(WifiManager.EXTRA_NETWORK_INFO, mNetworkInfo);
        intent.putExtra(WifiManager.EXTRA_LINK_PROPERTIES, new LinkProperties (mLinkProperties));
        if (bssid != null)
            intent.putExtra(WifiManager.EXTRA_BSSID, bssid);
        if (mNetworkInfo.getState() == NetworkInfo.State.CONNECTED)
            intent.putExtra(WifiManager.EXTRA_WIFI_INFO, new WifiInfo(mWifiInfo));
        mContext.sendStickyBroadcast(intent);
    }

    private void sendErrorBroadcast(int errorCode) {
        Intent intent = new Intent(WifiManager.ERROR_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_ERROR_CODE, errorCode);
        mContext.sendBroadcast(intent);
    }

>>>>>>> upstream/master
    private void sendLinkConfigurationChangedBroadcast() {
        Intent intent = new Intent(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_LINK_PROPERTIES, new LinkProperties(mLinkProperties));
        mContext.sendBroadcast(intent);
    }

    private void sendSupplicantConnectionChangedBroadcast(boolean connected) {
        Intent intent = new Intent(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
        intent.putExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, connected);
        mContext.sendBroadcast(intent);
    }

    /**
     * Record the detailed state of a network.
     * @param state the new @{code DetailedState}
     */
    private void setNetworkDetailedState(NetworkInfo.DetailedState state) {
        if (DBG) {
            log("setDetailed state, old ="
                    + mNetworkInfo.getDetailedState() + " and new state=" + state);
        }

        if (state != mNetworkInfo.getDetailedState()) {
            mNetworkInfo.setDetailedState(state, null, null);
        }
    }

    private DetailedState getNetworkDetailedState() {
        return mNetworkInfo.getDetailedState();
    }


    private SupplicantState handleSupplicantStateChange(Message message) {
        StateChangeResult stateChangeResult = (StateChangeResult) message.obj;
        SupplicantState state = stateChangeResult.state;
        // Supplicant state change
        // [31-13] Reserved for future use
        // [8 - 0] Supplicant state (as defined in SupplicantState.java)
        // 50023 supplicant_state_changed (custom|1|5)
        EventLog.writeEvent(EVENTLOG_SUPPLICANT_STATE_CHANGED, state.ordinal());
        mWifiInfo.setSupplicantState(state);
        // Network id is only valid when we start connecting
        if (SupplicantState.isConnecting(state)) {
            mWifiInfo.setNetworkId(stateChangeResult.networkId);
        } else {
            mWifiInfo.setNetworkId(WifiConfiguration.INVALID_NETWORK_ID);
        }

        if (state == SupplicantState.ASSOCIATING) {
            /* BSSID is valid only in ASSOCIATING state */
            mWifiInfo.setBSSID(stateChangeResult.BSSID);
        }
<<<<<<< HEAD
        mWifiInfo.setSSID(stateChangeResult.SSID);

        mSupplicantStateTracker.sendMessage(Message.obtain(message));
=======

        mSupplicantStateTracker.sendMessage(Message.obtain(message));
        mWpsStateMachine.sendMessage(Message.obtain(message));
>>>>>>> upstream/master

        return state;
    }

    /**
     * Resets the Wi-Fi Connections by clearing any state, resetting any sockets
     * using the interface, stopping DHCP & disabling interface
     */
    private void handleNetworkDisconnect() {
        if (DBG) log("Stopping DHCP and clearing IP");

        /*
         * stop DHCP
         */
        if (mDhcpStateMachine != null) {
<<<<<<< HEAD
            /* In case we were in middle of DHCP operation
               restore back powermode */
            handlePostDhcpSetup();

=======
>>>>>>> upstream/master
            mDhcpStateMachine.sendMessage(DhcpStateMachine.CMD_STOP_DHCP);
            mDhcpStateMachine.quit();
            mDhcpStateMachine = null;
        }

        try {
            mNwService.clearInterfaceAddresses(mInterfaceName);
            mNwService.disableIpv6(mInterfaceName);
        } catch (Exception e) {
            loge("Failed to clear addresses or disable ipv6" + e);
        }

        /* Reset data structures */
        mWifiInfo.setInetAddress(null);
        mWifiInfo.setBSSID(null);
        mWifiInfo.setSSID(null);
        mWifiInfo.setNetworkId(WifiConfiguration.INVALID_NETWORK_ID);
        mWifiInfo.setRssi(MIN_RSSI);
        mWifiInfo.setLinkSpeed(-1);
<<<<<<< HEAD
        mWifiInfo.setMeteredHint(false);

        setNetworkDetailedState(DetailedState.DISCONNECTED);
        mWifiConfigStore.updateStatus(mLastNetworkId, DetailedState.DISCONNECTED);

        /* send event to CM & network change broadcast */
=======
        mWifiInfo.setExplicitConnect(false);

        /* send event to CM & network change broadcast */
        setNetworkDetailedState(DetailedState.DISCONNECTED);
>>>>>>> upstream/master
        sendNetworkStateChangeBroadcast(mLastBssid);

        /* Clear network properties */
        mLinkProperties.clear();
        /* Clear IP settings if the network used DHCP */
<<<<<<< HEAD
        if (!mWifiConfigStore.isUsingStaticIp(mLastNetworkId)) {
            mWifiConfigStore.clearIpConfiguration(mLastNetworkId);
=======
        if (!WifiConfigStore.isUsingStaticIp(mLastNetworkId)) {
            WifiConfigStore.clearIpConfiguration(mLastNetworkId);
>>>>>>> upstream/master
        }

        mLastBssid= null;
        mLastNetworkId = WifiConfiguration.INVALID_NETWORK_ID;
    }

    void handlePreDhcpSetup() {
        if (!mBluetoothConnectionActive) {
            /*
             * There are problems setting the Wi-Fi driver's power
             * mode to active when bluetooth coexistence mode is
             * enabled or sense.
             * <p>
             * We set Wi-Fi to active mode when
             * obtaining an IP address because we've found
             * compatibility issues with some routers with low power
             * mode.
             * <p>
             * In order for this active power mode to properly be set,
             * we disable coexistence mode until we're done with
             * obtaining an IP address.  One exception is if we
             * are currently connected to a headset, since disabling
             * coexistence would interrupt that connection.
             */
            // Disable the coexistence mode
<<<<<<< HEAD
            mWifiNative.setBluetoothCoexistenceMode(
                    mWifiNative.BLUETOOTH_COEXISTENCE_MODE_DISABLED);
        }

        /* Disable power save during DHCP */
        if (mPowerSaveEnabled) {
            mPowerSaveEnabled = false;
            mWifiNative.setPowerSave(mPowerSaveEnabled);
=======
            WifiNative.setBluetoothCoexistenceModeCommand(
                    WifiNative.BLUETOOTH_COEXISTENCE_MODE_DISABLED);
        }

        mPowerMode =  WifiNative.getPowerModeCommand();
        if (mPowerMode < 0) {
            // Handle the case where supplicant driver does not support
            // getPowerModeCommand.
            mPowerMode = WifiStateMachine.POWER_MODE_AUTO;
        }
        if (mPowerMode != WifiStateMachine.POWER_MODE_ACTIVE) {
            WifiNative.setPowerModeCommand(WifiStateMachine.POWER_MODE_ACTIVE);
>>>>>>> upstream/master
        }
    }


    void handlePostDhcpSetup() {
<<<<<<< HEAD
        /* Restore power save */
        mPowerSaveEnabled = true;
        mWifiNative.setPowerSave(mPowerSaveEnabled);

        // Set the coexistence mode back to its default value
        mWifiNative.setBluetoothCoexistenceMode(
                mWifiNative.BLUETOOTH_COEXISTENCE_MODE_SENSE);
=======
        /* restore power mode */
        WifiNative.setPowerModeCommand(mPowerMode);

        // Set the coexistence mode back to its default value
        WifiNative.setBluetoothCoexistenceModeCommand(
                WifiNative.BLUETOOTH_COEXISTENCE_MODE_SENSE);
>>>>>>> upstream/master
    }

    private void handleSuccessfulIpConfiguration(DhcpInfoInternal dhcpInfoInternal) {
        synchronized (mDhcpInfoInternal) {
            mDhcpInfoInternal = dhcpInfoInternal;
        }
        mLastSignalLevel = -1; // force update of signal strength
        mReconnectCount = 0; //Reset IP failure tracking
<<<<<<< HEAD
        mWifiConfigStore.setIpConfiguration(mLastNetworkId, dhcpInfoInternal);
        InetAddress addr = NetworkUtils.numericToInetAddress(dhcpInfoInternal.ipAddress);
        mWifiInfo.setInetAddress(addr);
        mWifiInfo.setMeteredHint(dhcpInfoInternal.hasMeteredHint());
        if (getNetworkDetailedState() == DetailedState.CONNECTED) {
            //DHCP renewal in connected state
            LinkProperties linkProperties = dhcpInfoInternal.makeLinkProperties();
            linkProperties.setHttpProxy(mWifiConfigStore.getProxyProperties(mLastNetworkId));
=======
        WifiConfigStore.setIpConfiguration(mLastNetworkId, dhcpInfoInternal);
        InetAddress addr = NetworkUtils.numericToInetAddress(dhcpInfoInternal.ipAddress);
        mWifiInfo.setInetAddress(addr);
        if (getNetworkDetailedState() == DetailedState.CONNECTED) {
            //DHCP renewal in connected state
            LinkProperties linkProperties = dhcpInfoInternal.makeLinkProperties();
            linkProperties.setHttpProxy(WifiConfigStore.getProxyProperties(mLastNetworkId));
>>>>>>> upstream/master
            linkProperties.setInterfaceName(mInterfaceName);
            if (!linkProperties.equals(mLinkProperties)) {
                if (DBG) {
                    log("Link configuration changed for netId: " + mLastNetworkId
                            + " old: " + mLinkProperties + "new: " + linkProperties);
                }
                mLinkProperties = linkProperties;
                sendLinkConfigurationChangedBroadcast();
            }
        } else {
            configureLinkProperties();
<<<<<<< HEAD
=======
            setNetworkDetailedState(DetailedState.CONNECTED);
            sendNetworkStateChangeBroadcast(mLastBssid);
>>>>>>> upstream/master
        }
    }

    private void handleFailedIpConfiguration() {
        loge("IP configuration failed");

        mWifiInfo.setInetAddress(null);
<<<<<<< HEAD
        mWifiInfo.setMeteredHint(false);
=======
>>>>>>> upstream/master
        /**
         * If we've exceeded the maximum number of retries for DHCP
         * to a given network, disable the network
         */
<<<<<<< HEAD
        int maxRetries = getMaxDhcpRetries();
        // maxRetries == 0 means keep trying forever
        if (maxRetries > 0 && ++mReconnectCount > maxRetries) {
            loge("Failed " +
                    mReconnectCount + " times, Disabling " + mLastNetworkId);
            mWifiConfigStore.disableNetwork(mLastNetworkId,
=======
        if (++mReconnectCount > getMaxDhcpRetries()) {
            loge("Failed " +
                    mReconnectCount + " times, Disabling " + mLastNetworkId);
            WifiConfigStore.disableNetwork(mLastNetworkId,
>>>>>>> upstream/master
                    WifiConfiguration.DISABLED_DHCP_FAILURE);
            mReconnectCount = 0;
        }

        /* DHCP times out after about 30 seconds, we do a
         * disconnect and an immediate reconnect to try again
         */
<<<<<<< HEAD
        mWifiNative.disconnect();
        mWifiNative.reconnect();
=======
        WifiNative.disconnectCommand();
        WifiNative.reconnectCommand();
>>>>>>> upstream/master
    }

    /* Current design is to not set the config on a running hostapd but instead
     * stop and start tethering when user changes config on a running access point
     *
     * TODO: Add control channel setup through hostapd that allows changing config
     * on a running daemon
     */
    private void startSoftApWithConfig(final WifiConfiguration config) {
        // start hostapd on a seperate thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    mNwService.startAccessPoint(config, mInterfaceName, SOFTAP_IFACE);
                } catch (Exception e) {
                    loge("Exception in softap start " + e);
                    try {
                        mNwService.stopAccessPoint(mInterfaceName);
                        mNwService.startAccessPoint(config, mInterfaceName, SOFTAP_IFACE);
                    } catch (Exception e1) {
                        loge("Exception in softap re-start " + e1);
                        sendMessage(CMD_START_AP_FAILURE);
                        return;
                    }
                }
                if (DBG) log("Soft AP start successful");
                sendMessage(CMD_START_AP_SUCCESS);
            }
        }).start();
    }

    /********************************************************
     * HSM states
     *******************************************************/

    class DefaultState extends State {
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
                case AsyncChannel.CMD_CHANNEL_HALF_CONNECTED:
                    if (message.arg1 == AsyncChannel.STATUS_SUCCESSFUL) {
                        mWifiP2pChannel.sendMessage(AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
                    } else {
                        loge("WifiP2pService connection failure, error=" + message.arg1);
                    }
                    break;
                case AsyncChannel.CMD_CHANNEL_DISCONNECTED:
                    loge("WifiP2pService channel lost, message.arg1 =" + message.arg1);
                    //TODO: Re-establish connection to state machine after a delay
                    //mWifiP2pChannel.connect(mContext, getHandler(), mWifiP2pManager.getMessenger());
                    break;
                case CMD_BLUETOOTH_ADAPTER_STATE_CHANGE:
                    mBluetoothConnectionActive = (message.arg1 !=
                            BluetoothAdapter.STATE_DISCONNECTED);
                    break;
                    /* Synchronous call returns */
                case CMD_PING_SUPPLICANT:
                case CMD_ENABLE_NETWORK:
<<<<<<< HEAD
                case CMD_ADD_OR_UPDATE_NETWORK:
                case CMD_REMOVE_NETWORK:
                case CMD_SAVE_CONFIG:
                    replyToMessage(message, message.what, FAILURE);
                    break;
                case CMD_GET_CONFIGURED_NETWORKS:
                    replyToMessage(message, message.what, (List<WifiConfiguration>) null);
=======
                case CMD_DISABLE_NETWORK:
                case CMD_ADD_OR_UPDATE_NETWORK:
                case CMD_REMOVE_NETWORK:
                case CMD_SAVE_CONFIG:
                    mReplyChannel.replyToMessage(message, message.what, FAILURE);
>>>>>>> upstream/master
                    break;
                case CMD_ENABLE_RSSI_POLL:
                    mEnableRssiPolling = (message.arg1 == 1);
                    break;
                case CMD_ENABLE_BACKGROUND_SCAN:
                    mEnableBackgroundScan = (message.arg1 == 1);
                    break;
<<<<<<< HEAD
                case CMD_SET_HIGH_PERF_MODE:
                    mHighPerfMode = (message.arg1 == 1);
                    break;
=======
>>>>>>> upstream/master
                    /* Discard */
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_STOP_SUPPLICANT_FAILED:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_DELAYED_STOP_DRIVER:
<<<<<<< HEAD
                case CMD_DRIVER_START_TIMED_OUT:
=======
>>>>>>> upstream/master
                case CMD_START_AP:
                case CMD_START_AP_SUCCESS:
                case CMD_START_AP_FAILURE:
                case CMD_STOP_AP:
                case CMD_TETHER_STATE_CHANGE:
                case CMD_TETHER_NOTIFICATION_TIMED_OUT:
                case CMD_START_SCAN:
                case CMD_DISCONNECT:
                case CMD_RECONNECT:
                case CMD_REASSOCIATE:
                case WifiMonitor.SUP_CONNECTION_EVENT:
                case WifiMonitor.SUP_DISCONNECTION_EVENT:
                case WifiMonitor.NETWORK_CONNECTION_EVENT:
                case WifiMonitor.NETWORK_DISCONNECTION_EVENT:
                case WifiMonitor.SCAN_RESULTS_EVENT:
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                case WifiMonitor.AUTHENTICATION_FAILURE_EVENT:
                case WifiMonitor.WPS_OVERLAP_EVENT:
                case CMD_BLACKLIST_NETWORK:
                case CMD_CLEAR_BLACKLIST:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
=======
                case CMD_SET_HIGH_PERF_MODE:
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_CONNECT_NETWORK:
                case CMD_SAVE_NETWORK:
                case CMD_FORGET_NETWORK:
>>>>>>> upstream/master
                case CMD_RSSI_POLL:
                case CMD_ENABLE_ALL_NETWORKS:
                case DhcpStateMachine.CMD_PRE_DHCP_ACTION:
                case DhcpStateMachine.CMD_POST_DHCP_ACTION:
                /* Handled by WifiApConfigStore */
                case CMD_SET_AP_CONFIG:
                case CMD_SET_AP_CONFIG_COMPLETED:
                case CMD_REQUEST_AP_CONFIG:
                case CMD_RESPONSE_AP_CONFIG:
<<<<<<< HEAD
                case WifiWatchdogStateMachine.POOR_LINK_DETECTED:
                case WifiWatchdogStateMachine.GOOD_LINK_DETECTED:
                case CMD_CLEAR_SUSPEND_OPTIMIZATIONS:
                case CMD_NO_NETWORKS_PERIODIC_SCAN:
                    break;
                case CMD_SET_SUSPEND_OPTIMIZATIONS:
                    mSuspendWakeLock.release();
=======
>>>>>>> upstream/master
                    break;
                case WifiMonitor.DRIVER_HUNG_EVENT:
                    setWifiEnabled(false);
                    setWifiEnabled(true);
                    break;
<<<<<<< HEAD
                case WifiManager.CONNECT_NETWORK:
                    replyToMessage(message, WifiManager.CONNECT_NETWORK_FAILED,
                            WifiManager.BUSY);
                    break;
                case WifiManager.FORGET_NETWORK:
                    replyToMessage(message, WifiManager.FORGET_NETWORK_FAILED,
                            WifiManager.BUSY);
                    break;
                case WifiManager.SAVE_NETWORK:
                    replyToMessage(message, WifiManager.SAVE_NETWORK_FAILED,
                            WifiManager.BUSY);
                    break;
                case WifiManager.START_WPS:
                    replyToMessage(message, WifiManager.WPS_FAILED,
                            WifiManager.BUSY);
                    break;
                case WifiManager.CANCEL_WPS:
                    replyToMessage(message, WifiManager.CANCEL_WPS_FAILED,
                            WifiManager.BUSY);
                    break;
                case WifiManager.DISABLE_NETWORK:
                    replyToMessage(message, WifiManager.DISABLE_NETWORK_FAILED,
                            WifiManager.BUSY);
                    break;
                case WifiWatchdogStateMachine.RSSI_FETCH:
                    replyToMessage(message, WifiWatchdogStateMachine.RSSI_FETCH_FAILED);
=======
                case CMD_START_WPS:
                    /* Return failure when the state machine cannot handle WPS initiation*/
                    mReplyChannel.replyToMessage(message, WifiManager.CMD_WPS_COMPLETED,
                                new WpsResult(Status.FAILURE));
                    break;
                case WifiP2pService.P2P_ENABLE_PENDING:
                    // turn off wifi and defer to be handled in DriverUnloadedState
                    setWifiEnabled(false);
                    deferMessage(message);
>>>>>>> upstream/master
                    break;
                default:
                    loge("Error! unhandled message" + message);
                    break;
            }
            return HANDLED;
        }
    }

    class InitialState extends State {
        @Override
        //TODO: could move logging into a common class
        public void enter() {
            if (DBG) log(getName() + "\n");
            // [31-8] Reserved for future use
            // [7 - 0] HSM state change
            // 50021 wifi_state_changed (custom|1|5)
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

<<<<<<< HEAD
            if (mWifiNative.isDriverLoaded()) {
=======
            if (WifiNative.isDriverLoaded()) {
>>>>>>> upstream/master
                transitionTo(mDriverLoadedState);
            }
            else {
                transitionTo(mDriverUnloadedState);
            }

            //Connect to WifiP2pService
            mWifiP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
            mWifiP2pChannel.connect(mContext, getHandler(), mWifiP2pManager.getMessenger());

            /* IPv6 is disabled at boot time and is controlled by framework
             * to be enabled only as long as we are connected to an access point
             *
             * This fixes issues, a few being:
             * - IPv6 addresses and routes stick around after disconnection
             * - When connected, the kernel is unaware and can fail to start IPv6 negotiation
             * - The kernel sometimes starts autoconfiguration when 802.1x is not complete
             */
            try {
                mNwService.disableIpv6(mInterfaceName);
            } catch (RemoteException re) {
                loge("Failed to disable IPv6: " + re);
            } catch (IllegalStateException e) {
                loge("Failed to disable IPv6: " + e);
            }
        }
    }

    class DriverLoadingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            final Message message = new Message();
            message.copyFrom(getCurrentMessage());
            /* TODO: add a timeout to fail when driver load is hung.
             * Similarly for driver unload.
             */
            new Thread(new Runnable() {
                public void run() {
                    mWakeLock.acquire();
                    //enabling state
                    switch(message.arg1) {
                        case WIFI_STATE_ENABLING:
                            setWifiState(WIFI_STATE_ENABLING);
                            break;
                        case WIFI_AP_STATE_ENABLING:
                            setWifiApState(WIFI_AP_STATE_ENABLING);
                            break;
                    }

<<<<<<< HEAD
                    if(mWifiNative.loadDriver()) {
=======
                    if(WifiNative.loadDriver()) {
>>>>>>> upstream/master
                        if (DBG) log("Driver load successful");
                        sendMessage(CMD_LOAD_DRIVER_SUCCESS);
                    } else {
                        loge("Failed to load driver!");
                        switch(message.arg1) {
                            case WIFI_STATE_ENABLING:
                                setWifiState(WIFI_STATE_UNKNOWN);
                                break;
                            case WIFI_AP_STATE_ENABLING:
                                setWifiApState(WIFI_AP_STATE_FAILED);
                                break;
                        }
                        sendMessage(CMD_LOAD_DRIVER_FAILURE);
                    }
                    mWakeLock.release();
                }
            }).start();
        }

        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
                case CMD_LOAD_DRIVER_SUCCESS:
                    transitionTo(mDriverLoadedState);
                    break;
                case CMD_LOAD_DRIVER_FAILURE:
                    transitionTo(mDriverFailedState);
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverLoadedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_UNLOAD_DRIVER:
                    transitionTo(mDriverUnloadingState);
                    break;
                case CMD_START_SUPPLICANT:
                    try {
                        mNwService.wifiFirmwareReload(mInterfaceName, "STA");
                    } catch (Exception e) {
                        loge("Failed to reload STA firmware " + e);
                        // continue
                    }
                   try {
                       //A runtime crash can leave the interface up and
                       //this affects connectivity when supplicant starts up.
                       //Ensure interface is down before a supplicant start.
                        mNwService.setInterfaceDown(mInterfaceName);
                        //Set privacy extensions
                        mNwService.setInterfaceIpv6PrivacyExtensions(mInterfaceName, true);
                    } catch (RemoteException re) {
                        loge("Unable to change interface settings: " + re);
                    } catch (IllegalStateException ie) {
                        loge("Unable to change interface settings: " + ie);
                    }

<<<<<<< HEAD
                    if(mWifiNative.startSupplicant(mP2pSupported)) {
=======
                    if(WifiNative.startSupplicant()) {
>>>>>>> upstream/master
                        if (DBG) log("Supplicant start successful");
                        mWifiMonitor.startMonitoring();
                        transitionTo(mSupplicantStartingState);
                    } else {
                        loge("Failed to start supplicant!");
                        sendMessage(obtainMessage(CMD_UNLOAD_DRIVER, WIFI_STATE_UNKNOWN, 0));
                    }
                    break;
                case CMD_START_AP:
                    transitionTo(mSoftApStartingState);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverUnloadingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            final Message message = new Message();
            message.copyFrom(getCurrentMessage());
            new Thread(new Runnable() {
                public void run() {
                    if (DBG) log(getName() + message.toString() + "\n");
                    mWakeLock.acquire();
<<<<<<< HEAD
                    if(mWifiNative.unloadDriver()) {
=======
                    if(WifiNative.unloadDriver()) {
>>>>>>> upstream/master
                        if (DBG) log("Driver unload successful");
                        sendMessage(CMD_UNLOAD_DRIVER_SUCCESS);

                        switch(message.arg1) {
                            case WIFI_STATE_DISABLED:
                            case WIFI_STATE_UNKNOWN:
                                setWifiState(message.arg1);
                                break;
                            case WIFI_AP_STATE_DISABLED:
                            case WIFI_AP_STATE_FAILED:
                                setWifiApState(message.arg1);
                                break;
                        }
                    } else {
                        loge("Failed to unload driver!");
                        sendMessage(CMD_UNLOAD_DRIVER_FAILURE);

                        switch(message.arg1) {
                            case WIFI_STATE_DISABLED:
                            case WIFI_STATE_UNKNOWN:
                                setWifiState(WIFI_STATE_UNKNOWN);
                                break;
                            case WIFI_AP_STATE_DISABLED:
                            case WIFI_AP_STATE_FAILED:
                                setWifiApState(WIFI_AP_STATE_FAILED);
                                break;
                        }
                    }
                    mWakeLock.release();
                }
            }).start();
        }

        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
                case CMD_UNLOAD_DRIVER_SUCCESS:
                    transitionTo(mDriverUnloadedState);
                    break;
                case CMD_UNLOAD_DRIVER_FAILURE:
                    transitionTo(mDriverFailedState);
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverUnloadedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
                case CMD_LOAD_DRIVER:
<<<<<<< HEAD
                    transitionTo(mDriverLoadingState);
=======
                    mWifiP2pChannel.sendMessage(WIFI_ENABLE_PENDING);
                    transitionTo(mWaitForP2pDisableState);
                    break;
                case WifiP2pService.P2P_ENABLE_PENDING:
                    mReplyChannel.replyToMessage(message, P2P_ENABLE_PROCEED);
>>>>>>> upstream/master
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverFailedState extends State {
        @Override
        public void enter() {
            loge(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            return NOT_HANDLED;
        }
    }


    class SupplicantStartingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
<<<<<<< HEAD

        private void initializeWpsDetails() {
            String detail;
            detail = SystemProperties.get("ro.product.name", "");
            if (!mWifiNative.setDeviceName(detail)) {
                loge("Failed to set device name " +  detail);
            }
            detail = SystemProperties.get("ro.product.manufacturer", "");
            if (!mWifiNative.setManufacturer(detail)) {
                loge("Failed to set manufacturer " + detail);
            }
            detail = SystemProperties.get("ro.product.model", "");
            if (!mWifiNative.setModelName(detail)) {
                loge("Failed to set model name " + detail);
            }
            detail = SystemProperties.get("ro.product.model", "");
            if (!mWifiNative.setModelNumber(detail)) {
                loge("Failed to set model number " + detail);
            }
            detail = SystemProperties.get("ro.serialno", "");
            if (!mWifiNative.setSerialNumber(detail)) {
                loge("Failed to set serial number " + detail);
            }
            if (!mWifiNative.setConfigMethods("physical_display virtual_push_button keypad")) {
                loge("Failed to set WPS config methods");
            }
            if (!mWifiNative.setDeviceType(mPrimaryDeviceType)) {
                loge("Failed to set primary device type " + mPrimaryDeviceType);
            }
        }

=======
>>>>>>> upstream/master
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case WifiMonitor.SUP_CONNECTION_EVENT:
                    if (DBG) log("Supplicant connection established");
                    setWifiState(WIFI_STATE_ENABLED);
                    mSupplicantRestartCount = 0;
                    /* Reset the supplicant state to indicate the supplicant
                     * state is not known at this time */
                    mSupplicantStateTracker.sendMessage(CMD_RESET_SUPPLICANT_STATE);
<<<<<<< HEAD
=======
                    mWpsStateMachine.sendMessage(CMD_RESET_WPS_STATE);
>>>>>>> upstream/master
                    /* Initialize data structures */
                    mLastBssid = null;
                    mLastNetworkId = WifiConfiguration.INVALID_NETWORK_ID;
                    mLastSignalLevel = -1;

<<<<<<< HEAD
                    mWifiInfo.setMacAddress(mWifiNative.getMacAddress());
                    mWifiConfigStore.initialize();
                    initializeWpsDetails();
=======
                    mWifiInfo.setMacAddress(WifiNative.getMacAddressCommand());

                    WifiConfigStore.initialize(mContext);
>>>>>>> upstream/master

                    sendSupplicantConnectionChangedBroadcast(true);
                    transitionTo(mDriverStartedState);
                    break;
                case WifiMonitor.SUP_DISCONNECTION_EVENT:
                    if (++mSupplicantRestartCount <= SUPPLICANT_RESTART_TRIES) {
                        loge("Failed to setup control channel, restart supplicant");
<<<<<<< HEAD
                        mWifiNative.killSupplicant();
=======
                        WifiNative.killSupplicant();
>>>>>>> upstream/master
                        transitionTo(mDriverLoadedState);
                        sendMessageDelayed(CMD_START_SUPPLICANT, SUPPLICANT_RESTART_INTERVAL_MSECS);
                    } else {
                        loge("Failed " + mSupplicantRestartCount +
                                " times to start supplicant, unload driver");
                        mSupplicantRestartCount = 0;
                        transitionTo(mDriverLoadedState);
                        sendMessage(obtainMessage(CMD_UNLOAD_DRIVER, WIFI_STATE_UNKNOWN, 0));
                    }
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class SupplicantStartedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
            /* Initialize for connect mode operation at start */
            mIsScanMode = false;
            /* Wifi is available as long as we have a connection to supplicant */
            mNetworkInfo.setIsAvailable(true);
<<<<<<< HEAD

            int defaultInterval = mContext.getResources().getInteger(
                    com.android.internal.R.integer.config_wifi_supplicant_scan_interval);

            mSupplicantScanIntervalMs = Settings.Secure.getLong(mContext.getContentResolver(),
                    Settings.Secure.WIFI_SUPPLICANT_SCAN_INTERVAL_MS,
                    defaultInterval);

            mWifiNative.setScanInterval((int)mSupplicantScanIntervalMs / 1000);
=======
            /* Set scan interval */
            long supplicantScanIntervalMs = Settings.Secure.getLong(mContext.getContentResolver(),
                    Settings.Secure.WIFI_SUPPLICANT_SCAN_INTERVAL_MS,
                    mDefaultSupplicantScanIntervalMs);
            WifiNative.setScanIntervalCommand((int)supplicantScanIntervalMs / 1000);
>>>>>>> upstream/master
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            WifiConfiguration config;
<<<<<<< HEAD
=======
            boolean eventLoggingEnabled = true;
>>>>>>> upstream/master
            switch(message.what) {
                case CMD_STOP_SUPPLICANT:   /* Supplicant stopped by user */
                    transitionTo(mSupplicantStoppingState);
                    break;
                case WifiMonitor.SUP_DISCONNECTION_EVENT:  /* Supplicant connection lost */
                    loge("Connection lost, restart supplicant");
<<<<<<< HEAD
                    mWifiNative.killSupplicant();
                    mWifiNative.closeSupplicantConnection();
=======
                    WifiNative.killSupplicant();
                    WifiNative.closeSupplicantConnection();
>>>>>>> upstream/master
                    mNetworkInfo.setIsAvailable(false);
                    handleNetworkDisconnect();
                    sendSupplicantConnectionChangedBroadcast(false);
                    mSupplicantStateTracker.sendMessage(CMD_RESET_SUPPLICANT_STATE);
<<<<<<< HEAD
=======
                    mWpsStateMachine.sendMessage(CMD_RESET_WPS_STATE);
>>>>>>> upstream/master
                    transitionTo(mDriverLoadedState);
                    sendMessageDelayed(CMD_START_SUPPLICANT, SUPPLICANT_RESTART_INTERVAL_MSECS);
                    break;
                case WifiMonitor.SCAN_RESULTS_EVENT:
<<<<<<< HEAD
                    setScanResults(mWifiNative.scanResults());
=======
                    eventLoggingEnabled = false;
                    setScanResults(WifiNative.scanResultsCommand());
>>>>>>> upstream/master
                    sendScanResultsAvailableBroadcast();
                    mScanResultIsPending = false;
                    break;
                case CMD_PING_SUPPLICANT:
<<<<<<< HEAD
                    boolean ok = mWifiNative.ping();
                    replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
                    break;
                case CMD_ADD_OR_UPDATE_NETWORK:
                    config = (WifiConfiguration) message.obj;
                    replyToMessage(message, CMD_ADD_OR_UPDATE_NETWORK,
                            mWifiConfigStore.addOrUpdateNetwork(config));
                    break;
                case CMD_REMOVE_NETWORK:
                    ok = mWifiConfigStore.removeNetwork(message.arg1);
                    replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
                    break;
                case CMD_ENABLE_NETWORK:
                    ok = mWifiConfigStore.enableNetwork(message.arg1, message.arg2 == 1);
                    replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
=======
                    boolean ok = WifiNative.pingCommand();
                    mReplyChannel.replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
                    break;
                case CMD_ADD_OR_UPDATE_NETWORK:
                    config = (WifiConfiguration) message.obj;
                    mReplyChannel.replyToMessage(message, CMD_ADD_OR_UPDATE_NETWORK,
                            WifiConfigStore.addOrUpdateNetwork(config));
                    break;
                case CMD_REMOVE_NETWORK:
                    ok = WifiConfigStore.removeNetwork(message.arg1);
                    mReplyChannel.replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
                    break;
                case CMD_ENABLE_NETWORK:
                    ok = WifiConfigStore.enableNetwork(message.arg1, message.arg2 == 1);
                    mReplyChannel.replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
>>>>>>> upstream/master
                    break;
                case CMD_ENABLE_ALL_NETWORKS:
                    long time =  android.os.SystemClock.elapsedRealtime();
                    if (time - mLastEnableAllNetworksTime > MIN_INTERVAL_ENABLE_ALL_NETWORKS_MS) {
<<<<<<< HEAD
                        mWifiConfigStore.enableAllNetworks();
                        mLastEnableAllNetworksTime = time;
                    }
                    break;
                case WifiManager.DISABLE_NETWORK:
                    if (mWifiConfigStore.disableNetwork(message.arg1,
                            WifiConfiguration.DISABLED_UNKNOWN_REASON) == true) {
                        replyToMessage(message, WifiManager.DISABLE_NETWORK_SUCCEEDED);
                    } else {
                        replyToMessage(message, WifiManager.DISABLE_NETWORK_FAILED,
                                WifiManager.ERROR);
                    }
                    break;
                case CMD_BLACKLIST_NETWORK:
                    mWifiNative.addToBlacklist((String)message.obj);
                    break;
                case CMD_CLEAR_BLACKLIST:
                    mWifiNative.clearBlacklist();
                    break;
                case CMD_SAVE_CONFIG:
                    ok = mWifiConfigStore.saveConfig();
                    replyToMessage(message, CMD_SAVE_CONFIG, ok ? SUCCESS : FAILURE);
=======
                        WifiConfigStore.enableAllNetworks();
                        mLastEnableAllNetworksTime = time;
                    }
                    break;
                case CMD_DISABLE_NETWORK:
                    ok = WifiConfigStore.disableNetwork(message.arg1, message.arg2);
                    mReplyChannel.replyToMessage(message, message.what, ok ? SUCCESS : FAILURE);
                    break;
                case CMD_BLACKLIST_NETWORK:
                    WifiNative.addToBlacklistCommand((String)message.obj);
                    break;
                case CMD_CLEAR_BLACKLIST:
                    WifiNative.clearBlacklistCommand();
                    break;
                case CMD_SAVE_CONFIG:
                    ok = WifiConfigStore.saveConfig();
                    mReplyChannel.replyToMessage(message, CMD_SAVE_CONFIG, ok ? SUCCESS : FAILURE);
>>>>>>> upstream/master

                    // Inform the backup manager about a data change
                    IBackupManager ibm = IBackupManager.Stub.asInterface(
                            ServiceManager.getService(Context.BACKUP_SERVICE));
                    if (ibm != null) {
                        try {
                            ibm.dataChanged("com.android.providers.settings");
                        } catch (Exception e) {
                            // Try again later
                        }
                    }
                    break;
<<<<<<< HEAD
                case CMD_GET_CONFIGURED_NETWORKS:
                    replyToMessage(message, message.what,
                            mWifiConfigStore.getConfiguredNetworks());
                    break;
=======
>>>>>>> upstream/master
                    /* Cannot start soft AP while in client mode */
                case CMD_START_AP:
                    loge("Failed to start soft AP with a running supplicant");
                    setWifiApState(WIFI_AP_STATE_FAILED);
                    break;
                case CMD_SET_SCAN_MODE:
                    mIsScanMode = (message.arg1 == SCAN_ONLY_MODE);
                    break;
<<<<<<< HEAD
                case WifiManager.SAVE_NETWORK:
                    config = (WifiConfiguration) message.obj;
                    NetworkUpdateResult result = mWifiConfigStore.saveNetwork(config);
                    if (result.getNetworkId() != WifiConfiguration.INVALID_NETWORK_ID) {
                        replyToMessage(message, WifiManager.SAVE_NETWORK_SUCCEEDED);
                    } else {
                        loge("Failed to save network");
                        replyToMessage(message, WifiManager.SAVE_NETWORK_FAILED,
                                WifiManager.ERROR);
                    }
                    break;
                case WifiManager.FORGET_NETWORK:
                    if (mWifiConfigStore.forgetNetwork(message.arg1)) {
                        replyToMessage(message, WifiManager.FORGET_NETWORK_SUCCEEDED);
                    } else {
                        loge("Failed to forget network");
                        replyToMessage(message, WifiManager.FORGET_NETWORK_FAILED,
                                WifiManager.ERROR);
                    }
=======
                case CMD_SAVE_NETWORK:
                    config = (WifiConfiguration) message.obj;
                    WifiConfigStore.saveNetwork(config);
                    break;
                case CMD_FORGET_NETWORK:
                    WifiConfigStore.forgetNetwork(message.arg1);
>>>>>>> upstream/master
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            if (eventLoggingEnabled) {
                EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
            }
>>>>>>> upstream/master
            return HANDLED;
        }

        @Override
        public void exit() {
            mNetworkInfo.setIsAvailable(false);
        }
    }

    class SupplicantStoppingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
<<<<<<< HEAD

            /* Send any reset commands to supplicant before shutting it down */
            handleNetworkDisconnect();

            if (DBG) log("stopping supplicant");
            if (!mWifiNative.stopSupplicant()) {
=======
            if (DBG) log("stopping supplicant");
            if (!WifiNative.stopSupplicant()) {
>>>>>>> upstream/master
                loge("Failed to stop supplicant");
            }

            /* Send ourselves a delayed message to indicate failure after a wait time */
            sendMessageDelayed(obtainMessage(CMD_STOP_SUPPLICANT_FAILED,
                    ++mSupplicantStopFailureToken, 0), SUPPLICANT_RESTART_INTERVAL_MSECS);

            mNetworkInfo.setIsAvailable(false);
<<<<<<< HEAD
            setWifiState(WIFI_STATE_DISABLING);
            sendSupplicantConnectionChangedBroadcast(false);
            mSupplicantStateTracker.sendMessage(CMD_RESET_SUPPLICANT_STATE);
=======
            handleNetworkDisconnect();
            setWifiState(WIFI_STATE_DISABLING);
            sendSupplicantConnectionChangedBroadcast(false);
            mSupplicantStateTracker.sendMessage(CMD_RESET_SUPPLICANT_STATE);
            mWpsStateMachine.sendMessage(CMD_RESET_WPS_STATE);
>>>>>>> upstream/master
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case WifiMonitor.SUP_CONNECTION_EVENT:
                    loge("Supplicant connection received while stopping");
                    break;
                case WifiMonitor.SUP_DISCONNECTION_EVENT:
                    if (DBG) log("Supplicant connection lost");
                    /* Socket connection can be lost when we do a graceful shutdown
                     * or when the driver is hung. Ensure supplicant is stopped here.
                     */
<<<<<<< HEAD
                    mWifiNative.killSupplicant();
                    mWifiNative.closeSupplicantConnection();
=======
                    WifiNative.killSupplicant();
                    WifiNative.closeSupplicantConnection();
>>>>>>> upstream/master
                    transitionTo(mDriverLoadedState);
                    break;
                case CMD_STOP_SUPPLICANT_FAILED:
                    if (message.arg1 == mSupplicantStopFailureToken) {
                        loge("Timed out on a supplicant stop, kill and proceed");
<<<<<<< HEAD
                        mWifiNative.killSupplicant();
                        mWifiNative.closeSupplicantConnection();
=======
                        WifiNative.killSupplicant();
                        WifiNative.closeSupplicantConnection();
>>>>>>> upstream/master
                        transitionTo(mDriverLoadedState);
                    }
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverStartingState extends State {
<<<<<<< HEAD
        private int mTries;
=======
>>>>>>> upstream/master
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
<<<<<<< HEAD

            mTries = 1;
            /* Send ourselves a delayed message to start driver a second time */
            sendMessageDelayed(obtainMessage(CMD_DRIVER_START_TIMED_OUT,
                        ++mDriverStartToken, 0), DRIVER_START_TIME_OUT_MSECS);
=======
>>>>>>> upstream/master
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
               case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    SupplicantState state = handleSupplicantStateChange(message);
                    /* If suplicant is exiting out of INTERFACE_DISABLED state into
                     * a state that indicates driver has started, it is ready to
                     * receive driver commands
                     */
                    if (SupplicantState.isDriverActive(state)) {
                        transitionTo(mDriverStartedState);
                    }
                    break;
<<<<<<< HEAD
                case CMD_DRIVER_START_TIMED_OUT:
                    if (message.arg1 == mDriverStartToken) {
                        if (mTries >= 2) {
                            loge("Failed to start driver after " + mTries);
                            transitionTo(mDriverStoppedState);
                        } else {
                            loge("Driver start failed, retrying");
                            mWakeLock.acquire();
                            mWifiNative.startDriver();
                            mWakeLock.release();

                            ++mTries;
                            /* Send ourselves a delayed message to start driver again */
                            sendMessageDelayed(obtainMessage(CMD_DRIVER_START_TIMED_OUT,
                                        ++mDriverStartToken, 0), DRIVER_START_TIME_OUT_MSECS);
                        }
                    }
                    break;
=======
>>>>>>> upstream/master
                    /* Queue driver commands & connection events */
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case WifiMonitor.NETWORK_CONNECTION_EVENT:
                case WifiMonitor.NETWORK_DISCONNECTION_EVENT:
                case WifiMonitor.AUTHENTICATION_FAILURE_EVENT:
                case WifiMonitor.WPS_OVERLAP_EVENT:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                case CMD_START_SCAN:
                case CMD_DISCONNECT:
                case CMD_REASSOCIATE:
                case CMD_RECONNECT:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverStartedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            mIsRunning = true;
            mInDelayedStop = false;
            updateBatteryWorkSource(null);

            /**
             * Enable bluetooth coexistence scan mode when bluetooth connection is active.
             * When this mode is on, some of the low-level scan parameters used by the
             * driver are changed to reduce interference with bluetooth
             */
<<<<<<< HEAD
            mWifiNative.setBluetoothCoexistenceScanMode(mBluetoothConnectionActive);
=======
            WifiNative.setBluetoothCoexistenceScanModeCommand(mBluetoothConnectionActive);
>>>>>>> upstream/master
            /* set country code */
            setCountryCode();
            /* set frequency band of operation */
            setFrequencyBand();
            /* initialize network state */
            setNetworkDetailedState(DetailedState.DISCONNECTED);

            /* Remove any filtering on Multicast v6 at start */
<<<<<<< HEAD
            mWifiNative.stopFilteringMulticastV6Packets();

            /* Reset Multicast v4 filtering state */
            if (mFilteringMulticastV4Packets.get()) {
                mWifiNative.startFilteringMulticastV4Packets();
            } else {
                mWifiNative.stopFilteringMulticastV4Packets();
            }

            mWifiNative.setPowerSave(mPowerSaveEnabled);

            if (mIsScanMode) {
                mWifiNative.setScanResultHandling(SCAN_ONLY_MODE);
                mWifiNative.disconnect();
                transitionTo(mScanModeState);
            } else {
                mWifiNative.setScanResultHandling(CONNECT_MODE);
                mWifiNative.reconnect();
                // Status pulls in the current supplicant state and network connection state
                // events over the monitor connection. This helps framework sync up with
                // current supplicant state
                mWifiNative.status();
                transitionTo(mDisconnectedState);
            }

            if (mP2pSupported) mWifiP2pChannel.sendMessage(WifiStateMachine.CMD_ENABLE_P2P);

            mContext.registerReceiver(mScreenReceiver, mScreenFilter);
=======
            WifiNative.stopFilteringMulticastV6Packets();

            /* Reset Multicast v4 filtering state */
            if (mFilteringMulticastV4Packets.get()) {
                WifiNative.startFilteringMulticastV4Packets();
            } else {
                WifiNative.stopFilteringMulticastV4Packets();
            }

            if (mIsScanMode) {
                WifiNative.setScanResultHandlingCommand(SCAN_ONLY_MODE);
                WifiNative.disconnectCommand();
                transitionTo(mScanModeState);
            } else {
                WifiNative.setScanResultHandlingCommand(CONNECT_MODE);
                WifiNative.reconnectCommand();
                transitionTo(mDisconnectedState);
            }
>>>>>>> upstream/master
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
<<<<<<< HEAD
            switch(message.what) {
               case CMD_SET_SCAN_TYPE:
                    mSetScanActive = (message.arg1 == SCAN_ACTIVE);
                    mWifiNative.setScanMode(mSetScanActive);
                    break;
                case CMD_START_SCAN:
                    boolean forceActive = (message.arg1 == SCAN_ACTIVE);
                    if (forceActive && !mSetScanActive) {
                        mWifiNative.setScanMode(forceActive);
                    }
                    mWifiNative.scan();
                    if (forceActive && !mSetScanActive) {
                        mWifiNative.setScanMode(mSetScanActive);
                    }
                    mScanResultIsPending = true;
                    break;
                case CMD_SET_COUNTRY_CODE:
                    String country = (String) message.obj;
                    if (DBG) log("set country code " + country);
                    if (!mWifiNative.setCountryCode(country.toUpperCase())) {
=======
            boolean eventLoggingEnabled = true;
            switch(message.what) {
                case CMD_SET_SCAN_TYPE:
                    if (message.arg1 == SCAN_ACTIVE) {
                        WifiNative.setScanModeCommand(true);
                    } else {
                        WifiNative.setScanModeCommand(false);
                    }
                    break;
                case CMD_START_SCAN:
                    eventLoggingEnabled = false;
                    WifiNative.scanCommand(message.arg1 == SCAN_ACTIVE);
                    mScanResultIsPending = true;
                    break;
                case CMD_SET_HIGH_PERF_MODE:
                    setHighPerfModeEnabledNative(message.arg1 == 1);
                    break;
                case CMD_SET_COUNTRY_CODE:
                    String country = (String) message.obj;
                    if (DBG) log("set country code " + country);
                    if (!WifiNative.setCountryCodeCommand(country.toUpperCase())) {
>>>>>>> upstream/master
                        loge("Failed to set country code " + country);
                    }
                    break;
                case CMD_SET_FREQUENCY_BAND:
                    int band =  message.arg1;
                    if (DBG) log("set frequency band " + band);
<<<<<<< HEAD
                    if (mWifiNative.setBand(band)) {
=======
                    if (WifiNative.setBandCommand(band)) {
>>>>>>> upstream/master
                        mFrequencyBand.set(band);
                        //Fetch the latest scan results when frequency band is set
                        startScan(true);
                    } else {
                        loge("Failed to set frequency band " + band);
                    }
                    break;
                case CMD_BLUETOOTH_ADAPTER_STATE_CHANGE:
                    mBluetoothConnectionActive = (message.arg1 !=
                            BluetoothAdapter.STATE_DISCONNECTED);
<<<<<<< HEAD
                    mWifiNative.setBluetoothCoexistenceScanMode(mBluetoothConnectionActive);
=======
                    WifiNative.setBluetoothCoexistenceScanModeCommand(mBluetoothConnectionActive);
>>>>>>> upstream/master
                    break;
                case CMD_STOP_DRIVER:
                    int mode = message.arg1;

                    /* Already doing a delayed stop && not in ecm state */
                    if (mInDelayedStop && mode != IN_ECM_STATE) {
                        if (DBG) log("Already in delayed stop");
                        break;
                    }
                    mInDelayedStop = true;
                    mDelayedStopCounter++;
                    if (DBG) log("Delayed stop message " + mDelayedStopCounter);

                    if (mode == IN_ECM_STATE) {
                        /* send a shut down immediately */
                        sendMessage(obtainMessage(CMD_DELAYED_STOP_DRIVER, mDelayedStopCounter, 0));
                    } else {
                        /* send regular delayed shut down */
<<<<<<< HEAD
                        Intent driverStopIntent = new Intent(ACTION_DELAYED_DRIVER_STOP, null);
                        driverStopIntent.putExtra(DELAYED_STOP_COUNTER, mDelayedStopCounter);
                        mDriverStopIntent = PendingIntent.getBroadcast(mContext,
                                DRIVER_STOP_REQUEST, driverStopIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                + mDriverStopDelayMs, mDriverStopIntent);
=======
                        sendMessageDelayed(obtainMessage(CMD_DELAYED_STOP_DRIVER,
                                mDelayedStopCounter, 0), DELAYED_DRIVER_STOP_MS);
>>>>>>> upstream/master
                    }
                    break;
                case CMD_START_DRIVER:
                    if (mInDelayedStop) {
                        mInDelayedStop = false;
                        mDelayedStopCounter++;
<<<<<<< HEAD
                        mAlarmManager.cancel(mDriverStopIntent);
=======
>>>>>>> upstream/master
                        if (DBG) log("Delayed stop ignored due to start");
                    }
                    break;
                case CMD_DELAYED_STOP_DRIVER:
<<<<<<< HEAD
                    if (DBG) log("delayed stop " + message.arg1 + " " + mDelayedStopCounter);
                    if (message.arg1 != mDelayedStopCounter) break;
                    if (getCurrentState() != mDisconnectedState) {
                        mWifiNative.disconnect();
                        handleNetworkDisconnect();
                    }
                    mWakeLock.acquire();
                    mWifiNative.stopDriver();
=======
                    if (message.arg1 != mDelayedStopCounter) break;
                    if (getCurrentState() != mDisconnectedState) {
                        WifiNative.disconnectCommand();
                        handleNetworkDisconnect();
                    }
                    mWakeLock.acquire();
                    WifiNative.stopDriverCommand();
>>>>>>> upstream/master
                    transitionTo(mDriverStoppingState);
                    mWakeLock.release();
                    break;
                case CMD_START_PACKET_FILTERING:
                    if (message.arg1 == MULTICAST_V6) {
<<<<<<< HEAD
                        mWifiNative.startFilteringMulticastV6Packets();
                    } else if (message.arg1 == MULTICAST_V4) {
                        mWifiNative.startFilteringMulticastV4Packets();
=======
                        WifiNative.startFilteringMulticastV6Packets();
                    } else if (message.arg1 == MULTICAST_V4) {
                        WifiNative.startFilteringMulticastV4Packets();
>>>>>>> upstream/master
                    } else {
                        loge("Illegal arugments to CMD_START_PACKET_FILTERING");
                    }
                    break;
                case CMD_STOP_PACKET_FILTERING:
                    if (message.arg1 == MULTICAST_V6) {
<<<<<<< HEAD
                        mWifiNative.stopFilteringMulticastV6Packets();
                    } else if (message.arg1 == MULTICAST_V4) {
                        mWifiNative.stopFilteringMulticastV4Packets();
=======
                        WifiNative.stopFilteringMulticastV6Packets();
                    } else if (message.arg1 == MULTICAST_V4) {
                        WifiNative.stopFilteringMulticastV4Packets();
>>>>>>> upstream/master
                    } else {
                        loge("Illegal arugments to CMD_STOP_PACKET_FILTERING");
                    }
                    break;
<<<<<<< HEAD
                case CMD_SET_SUSPEND_OPTIMIZATIONS:
                    if (!mHighPerfMode) {
                        mWifiNative.setSuspendOptimizations(true);
                    }
                    mSuspendWakeLock.release();
                    break;
                case CMD_CLEAR_SUSPEND_OPTIMIZATIONS:
                    mWifiNative.setSuspendOptimizations(false);
                    break;
                case CMD_SET_HIGH_PERF_MODE:
                    mHighPerfMode = (message.arg1 == 1);
                    if (mHighPerfMode) {
                        //Disable any suspend optimizations
                        mWifiNative.setSuspendOptimizations(false);
                    }
                    break;
                default:
                    return NOT_HANDLED;
            }
=======
                default:
                    return NOT_HANDLED;
            }
            if (eventLoggingEnabled) {
                EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
            }
>>>>>>> upstream/master
            return HANDLED;
        }
        @Override
        public void exit() {
            if (DBG) log(getName() + "\n");
            mIsRunning = false;
            updateBatteryWorkSource(null);
            mScanResults = null;
<<<<<<< HEAD

            if (mP2pSupported) mWifiP2pChannel.sendMessage(WifiStateMachine.CMD_DISABLE_P2P);
            mContext.unregisterReceiver(mScreenReceiver);
=======
>>>>>>> upstream/master
        }
    }

    class DriverStoppingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    SupplicantState state = handleSupplicantStateChange(message);
                    if (state == SupplicantState.INTERFACE_DISABLED) {
                        transitionTo(mDriverStoppedState);
                    }
                    break;
                    /* Queue driver commands */
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                case CMD_START_SCAN:
                case CMD_DISCONNECT:
                case CMD_REASSOCIATE:
                case CMD_RECONNECT:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DriverStoppedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    StateChangeResult stateChangeResult = (StateChangeResult) message.obj;
                    SupplicantState state = stateChangeResult.state;
                    // A WEXT bug means that we can be back to driver started state
                    // unexpectedly
                    if (SupplicantState.isDriverActive(state)) {
                        transitionTo(mDriverStartedState);
                    }
                    break;
                case CMD_START_DRIVER:
                    mWakeLock.acquire();
<<<<<<< HEAD
                    mWifiNative.startDriver();
=======
                    WifiNative.startDriverCommand();
>>>>>>> upstream/master
                    mWakeLock.release();
                    transitionTo(mDriverStartingState);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class ScanModeState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_SET_SCAN_MODE:
                    if (message.arg1 == SCAN_ONLY_MODE) {
                        /* Ignore */
                        return HANDLED;
                    } else {
<<<<<<< HEAD
                        mWifiNative.setScanResultHandling(message.arg1);
                        mWifiNative.reconnect();
=======
                        WifiNative.setScanResultHandlingCommand(message.arg1);
                        WifiNative.reconnectCommand();
>>>>>>> upstream/master
                        mIsScanMode = false;
                        transitionTo(mDisconnectedState);
                    }
                    break;
                    /* Ignore */
                case CMD_DISCONNECT:
                case CMD_RECONNECT:
                case CMD_REASSOCIATE:
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                case WifiMonitor.NETWORK_CONNECTION_EVENT:
                case WifiMonitor.NETWORK_DISCONNECTION_EVENT:
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class ConnectModeState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            StateChangeResult stateChangeResult;
            switch(message.what) {
                case WifiMonitor.AUTHENTICATION_FAILURE_EVENT:
                    mSupplicantStateTracker.sendMessage(WifiMonitor.AUTHENTICATION_FAILURE_EVENT);
                    break;
<<<<<<< HEAD
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    SupplicantState state = handleSupplicantStateChange(message);
                    // A driver/firmware hang can now put the interface in a down state.
                    // We detect the interface going down and recover from it
=======
                case WifiMonitor.WPS_OVERLAP_EVENT:
                    /* We just need to broadcast the error */
                    sendErrorBroadcast(WifiManager.WPS_OVERLAP_ERROR);
                    break;
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    SupplicantState state = handleSupplicantStateChange(message);
                    // Due to a WEXT bug, during the time of driver start/stop
                    // we can go into a driver stopped state in an unexpected way.
                    // The sequence eventually puts interface
                    // up and we should be back to a connected state
>>>>>>> upstream/master
                    if (!SupplicantState.isDriverActive(state)) {
                        if (mNetworkInfo.getState() != NetworkInfo.State.DISCONNECTED) {
                            handleNetworkDisconnect();
                        }
<<<<<<< HEAD
                        log("Detected an interface down, restart driver");
                        transitionTo(mDriverStoppedState);
                        sendMessage(CMD_START_DRIVER);
=======
                        transitionTo(mDriverStoppedState);
>>>>>>> upstream/master
                        break;
                    }

                    // Supplicant can fail to report a NETWORK_DISCONNECTION_EVENT
                    // when authentication times out after a successful connection,
                    // we can figure this from the supplicant state. If supplicant
                    // state is DISCONNECTED, but the mNetworkInfo says we are not
                    // disconnected, we need to handle a disconnection
                    if (state == SupplicantState.DISCONNECTED &&
                            mNetworkInfo.getState() != NetworkInfo.State.DISCONNECTED) {
                        if (DBG) log("Missed CTRL-EVENT-DISCONNECTED, disconnect");
                        handleNetworkDisconnect();
                        transitionTo(mDisconnectedState);
                    }
                    break;
                    /* Do a redundant disconnect without transition */
                case CMD_DISCONNECT:
<<<<<<< HEAD
                    mWifiNative.disconnect();
                    break;
                case CMD_RECONNECT:
                    mWifiNative.reconnect();
                    break;
                case CMD_REASSOCIATE:
                    mWifiNative.reassociate();
                    break;
                case WifiManager.CONNECT_NETWORK:
                    /* The connect message can contain a network id passed as arg1 on message or
                     * or a config passed as obj on message.
                     * For a new network, a config is passed to create and connect.
                     * For an existing network, a network id is passed
                     */
                    int netId = message.arg1;
                    WifiConfiguration config = (WifiConfiguration) message.obj;

                    /* Save the network config */
                    if (config != null) {
                        NetworkUpdateResult result = mWifiConfigStore.saveNetwork(config);
                        netId = result.getNetworkId();
                    }

                    if (mWifiConfigStore.selectNetwork(netId) &&
                            mWifiNative.reconnect()) {
                        /* The state tracker handles enabling networks upon completion/failure */
                        mSupplicantStateTracker.sendMessage(WifiManager.CONNECT_NETWORK);
                        replyToMessage(message, WifiManager.CONNECT_NETWORK_SUCCEEDED);
                        /* Expect a disconnection from the old connection */
                        transitionTo(mDisconnectingState);
                    } else {
                        loge("Failed to connect config: " + config + " netId: " + netId);
                        replyToMessage(message, WifiManager.CONNECT_NETWORK_FAILED,
                                WifiManager.ERROR);
                        break;
                    }
                    break;
                case WifiManager.START_WPS:
                    WpsInfo wpsInfo = (WpsInfo) message.obj;
                    WpsResult result;
                    switch (wpsInfo.setup) {
                        case WpsInfo.PBC:
                            result = mWifiConfigStore.startWpsPbc(wpsInfo);
                            break;
                        case WpsInfo.KEYPAD:
                            result = mWifiConfigStore.startWpsWithPinFromAccessPoint(wpsInfo);
                            break;
                        case WpsInfo.DISPLAY:
                            result = mWifiConfigStore.startWpsWithPinFromDevice(wpsInfo);
                            break;
                        default:
                            result = new WpsResult(Status.FAILURE);
                            Log.e(TAG, "Invalid setup for WPS");
                            break;
                    }
                    if (result.status == Status.SUCCESS) {
                        replyToMessage(message, WifiManager.START_WPS_SUCCEEDED, result);
                        transitionTo(mWpsRunningState);
                    } else {
                        Log.e(TAG, "Failed to start WPS with config " + wpsInfo.toString());
                        replyToMessage(message, WifiManager.WPS_FAILED, WifiManager.ERROR);
                    }
                    break;
                case WifiMonitor.SCAN_RESULTS_EVENT:
                    /* Set the scan setting back to "connect" mode */
                    mWifiNative.setScanResultHandling(CONNECT_MODE);
=======
                    WifiNative.disconnectCommand();
                    break;
                case CMD_RECONNECT:
                    WifiNative.reconnectCommand();
                    break;
                case CMD_REASSOCIATE:
                    WifiNative.reassociateCommand();
                    break;
                case CMD_CONNECT_NETWORK:
                    int netId = message.arg1;
                    WifiConfiguration config = (WifiConfiguration) message.obj;

                    /* We connect to a specific network by issuing a select
                     * to the WifiConfigStore. This enables the network,
                     * while disabling all other networks in the supplicant.
                     * Disabling a connected network will cause a disconnection
                     * from the network. A reconnectCommand() will then initiate
                     * a connection to the enabled network.
                     */
                    if (config != null) {
                        netId = WifiConfigStore.selectNetwork(config);
                    } else {
                        WifiConfigStore.selectNetwork(netId);
                    }

                    /* The state tracker handles enabling networks upon completion/failure */
                    mSupplicantStateTracker.sendMessage(CMD_CONNECT_NETWORK);

                    WifiNative.reconnectCommand();
                    mLastExplicitNetworkId = netId;
                    mLastNetworkChoiceTime  = SystemClock.elapsedRealtime();
                    mNextWifiActionExplicit = true;
                    if (DBG) log("Setting wifi connect explicit for netid " + netId);
                    /* Expect a disconnection from the old connection */
                    transitionTo(mDisconnectingState);
                    break;
                case CMD_START_WPS:
                    mWpsStateMachine.sendMessage(Message.obtain(message));
                    transitionTo(mWaitForWpsCompletionState);
                    break;
                case WifiMonitor.SCAN_RESULTS_EVENT:
                    /* Set the scan setting back to "connect" mode */
                    WifiNative.setScanResultHandlingCommand(CONNECT_MODE);
>>>>>>> upstream/master
                    /* Handle scan results */
                    return NOT_HANDLED;
                case WifiMonitor.NETWORK_CONNECTION_EVENT:
                    if (DBG) log("Network connection established");
                    mLastNetworkId = message.arg1;
                    mLastBssid = (String) message.obj;

<<<<<<< HEAD
                    mWifiInfo.setBSSID(mLastBssid);
                    mWifiInfo.setNetworkId(mLastNetworkId);
                    /* send event to CM & network change broadcast */
                    setNetworkDetailedState(DetailedState.OBTAINING_IPADDR);
                    sendNetworkStateChangeBroadcast(mLastBssid);
                    transitionTo(mObtainingIpState);
=======
                    //TODO: make supplicant modification to push this in events
                    mWifiInfo.setSSID(fetchSSID());
                    mWifiInfo.setBSSID(mLastBssid);
                    mWifiInfo.setNetworkId(mLastNetworkId);
                    if (mNextWifiActionExplicit &&
                        mWifiInfo.getNetworkId() == mLastExplicitNetworkId &&
                        SystemClock.elapsedRealtime() < mLastNetworkChoiceTime +
                                                            EXPLICIT_CONNECT_ALLOWED_DELAY_MS) {
                        mWifiInfo.setExplicitConnect(true);
                    }
                    mNextWifiActionExplicit = false;
                    /* send event to CM & network change broadcast */
                    setNetworkDetailedState(DetailedState.OBTAINING_IPADDR);
                    sendNetworkStateChangeBroadcast(mLastBssid);
                    transitionTo(mConnectingState);
>>>>>>> upstream/master
                    break;
                case WifiMonitor.NETWORK_DISCONNECTION_EVENT:
                    if (DBG) log("Network connection lost");
                    handleNetworkDisconnect();
                    transitionTo(mDisconnectedState);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

<<<<<<< HEAD
    class L2ConnectedState extends State {
=======
    class ConnectingState extends State {

        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            try {
                mNwService.enableIpv6(mInterfaceName);
            } catch (RemoteException re) {
                loge("Failed to enable IPv6: " + re);
            } catch (IllegalStateException e) {
                loge("Failed to enable IPv6: " + e);
            }

            if (!WifiConfigStore.isUsingStaticIp(mLastNetworkId)) {
                //start DHCP
                mDhcpStateMachine = DhcpStateMachine.makeDhcpStateMachine(
                        mContext, WifiStateMachine.this, mInterfaceName);
                mDhcpStateMachine.registerForPreDhcpNotification();
                mDhcpStateMachine.sendMessage(DhcpStateMachine.CMD_START_DHCP);
            } else {
                DhcpInfoInternal dhcpInfoInternal = WifiConfigStore.getIpConfiguration(
                        mLastNetworkId);
                InterfaceConfiguration ifcg = new InterfaceConfiguration();
                ifcg.addr = dhcpInfoInternal.makeLinkAddress();
                ifcg.interfaceFlags = "[up]";
                try {
                    mNwService.setInterfaceConfig(mInterfaceName, ifcg);
                    if (DBG) log("Static IP configuration succeeded");
                    sendMessage(CMD_STATIC_IP_SUCCESS, dhcpInfoInternal);
                } catch (RemoteException re) {
                    loge("Static IP configuration failed: " + re);
                    sendMessage(CMD_STATIC_IP_FAILURE);
                } catch (IllegalStateException e) {
                    loge("Static IP configuration failed: " + e);
                    sendMessage(CMD_STATIC_IP_FAILURE);
                }
            }
        }
      @Override
      public boolean processMessage(Message message) {
          if (DBG) log(getName() + message.toString() + "\n");

          switch(message.what) {
              case DhcpStateMachine.CMD_PRE_DHCP_ACTION:
                  handlePreDhcpSetup();
                  mDhcpStateMachine.sendMessage(DhcpStateMachine.CMD_PRE_DHCP_ACTION_COMPLETE);
                  break;
              case DhcpStateMachine.CMD_POST_DHCP_ACTION:
                  handlePostDhcpSetup();
                  if (message.arg1 == DhcpStateMachine.DHCP_SUCCESS) {
                      handleSuccessfulIpConfiguration((DhcpInfoInternal) message.obj);
                      transitionTo(mConnectedState);
                  } else if (message.arg1 == DhcpStateMachine.DHCP_FAILURE) {
                      handleFailedIpConfiguration();
                      transitionTo(mDisconnectingState);
                  }
                  break;
              case CMD_STATIC_IP_SUCCESS:
                  handleSuccessfulIpConfiguration((DhcpInfoInternal) message.obj);
                  transitionTo(mConnectedState);
                  break;
              case CMD_STATIC_IP_FAILURE:
                  handleFailedIpConfiguration();
                  transitionTo(mDisconnectingState);
                  break;
              case CMD_DISCONNECT:
                  WifiNative.disconnectCommand();
                  transitionTo(mDisconnectingState);
                  break;
                  /* Ignore connection to same network */
              case CMD_CONNECT_NETWORK:
                  int netId = message.arg1;
                  if (mWifiInfo.getNetworkId() == netId) {
                      break;
                  }
                  return NOT_HANDLED;
              case CMD_SAVE_NETWORK:
                  deferMessage(message);
                  break;
                  /* Ignore */
              case WifiMonitor.NETWORK_CONNECTION_EVENT:
                  break;
              case CMD_SET_SCAN_MODE:
                  if (message.arg1 == SCAN_ONLY_MODE) {
                      sendMessage(CMD_DISCONNECT);
                      deferMessage(message);
                  }
                  break;
                  /* Defer scan when IP is being fetched */
              case CMD_START_SCAN:
                  deferMessage(message);
                  break;
                  /* Defer any power mode changes since we must keep active power mode at DHCP */
              case CMD_SET_HIGH_PERF_MODE:
                  deferMessage(message);
                  break;
              default:
                return NOT_HANDLED;
          }
          EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
          return HANDLED;
      }
    }

    class ConnectedState extends State {
>>>>>>> upstream/master
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
            mRssiPollToken++;
            if (mEnableRssiPolling) {
<<<<<<< HEAD
                sendMessage(obtainMessage(CMD_RSSI_POLL, mRssiPollToken, 0));
            }
        }

        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
=======
                sendMessage(obtainMessage(WifiStateMachine.CMD_RSSI_POLL, mRssiPollToken, 0));
            }
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            boolean eventLoggingEnabled = true;
>>>>>>> upstream/master
            switch (message.what) {
              case DhcpStateMachine.CMD_PRE_DHCP_ACTION:
                  handlePreDhcpSetup();
                  mDhcpStateMachine.sendMessage(DhcpStateMachine.CMD_PRE_DHCP_ACTION_COMPLETE);
                  break;
              case DhcpStateMachine.CMD_POST_DHCP_ACTION:
                  handlePostDhcpSetup();
                  if (message.arg1 == DhcpStateMachine.DHCP_SUCCESS) {
<<<<<<< HEAD
                      if (DBG) log("DHCP successful");
                      handleSuccessfulIpConfiguration((DhcpInfoInternal) message.obj);
                      transitionTo(mVerifyingLinkState);
                  } else if (message.arg1 == DhcpStateMachine.DHCP_FAILURE) {
                      if (DBG) log("DHCP failed");
=======
                      handleSuccessfulIpConfiguration((DhcpInfoInternal) message.obj);
                  } else if (message.arg1 == DhcpStateMachine.DHCP_FAILURE) {
>>>>>>> upstream/master
                      handleFailedIpConfiguration();
                      transitionTo(mDisconnectingState);
                  }
                  break;
                case CMD_DISCONNECT:
<<<<<<< HEAD
                    mWifiNative.disconnect();
=======
                    WifiNative.disconnectCommand();
>>>>>>> upstream/master
                    transitionTo(mDisconnectingState);
                    break;
                case CMD_SET_SCAN_MODE:
                    if (message.arg1 == SCAN_ONLY_MODE) {
                        sendMessage(CMD_DISCONNECT);
                        deferMessage(message);
                    }
                    break;
                case CMD_START_SCAN:
<<<<<<< HEAD
=======
                    eventLoggingEnabled = false;
>>>>>>> upstream/master
                    /* When the network is connected, re-scanning can trigger
                     * a reconnection. Put it in scan-only mode during scan.
                     * When scan results are received, the mode is switched
                     * back to CONNECT_MODE.
                     */
<<<<<<< HEAD
                    mWifiNative.setScanResultHandling(SCAN_ONLY_MODE);
                    /* Have the parent state handle the rest */
                    return NOT_HANDLED;
                    /* Ignore connection to same network */
                case WifiManager.CONNECT_NETWORK:
=======
                    WifiNative.setScanResultHandlingCommand(SCAN_ONLY_MODE);
                    /* Have the parent state handle the rest */
                    return NOT_HANDLED;
                    /* Ignore connection to same network */
                case CMD_CONNECT_NETWORK:
>>>>>>> upstream/master
                    int netId = message.arg1;
                    if (mWifiInfo.getNetworkId() == netId) {
                        break;
                    }
                    return NOT_HANDLED;
<<<<<<< HEAD
                case WifiManager.SAVE_NETWORK:
                    WifiConfiguration config = (WifiConfiguration) message.obj;
                    NetworkUpdateResult result = mWifiConfigStore.saveNetwork(config);
                    if (mWifiInfo.getNetworkId() == result.getNetworkId()) {
                        if (result.hasIpChanged()) {
                            log("Reconfiguring IP on connection");
                            transitionTo(mObtainingIpState);
=======
                case CMD_SAVE_NETWORK:
                    WifiConfiguration config = (WifiConfiguration) message.obj;
                    NetworkUpdateResult result = WifiConfigStore.saveNetwork(config);
                    if (mWifiInfo.getNetworkId() == result.getNetworkId()) {
                        if (result.hasIpChanged()) {
                            log("Reconfiguring IP on connection");
                            transitionTo(mConnectingState);
>>>>>>> upstream/master
                        }
                        if (result.hasProxyChanged()) {
                            log("Reconfiguring proxy on connection");
                            configureLinkProperties();
                            sendLinkConfigurationChangedBroadcast();
                        }
                    }
<<<<<<< HEAD

                    if (result.getNetworkId() != WifiConfiguration.INVALID_NETWORK_ID) {
                        replyToMessage(message, WifiManager.SAVE_NETWORK_SUCCEEDED);
                    } else {
                        loge("Failed to save network");
                        replyToMessage(message, WifiManager.SAVE_NETWORK_FAILED,
                                WifiManager.ERROR);
                    }
=======
>>>>>>> upstream/master
                    break;
                    /* Ignore */
                case WifiMonitor.NETWORK_CONNECTION_EVENT:
                    break;
                case CMD_RSSI_POLL:
<<<<<<< HEAD
                    if (message.arg1 == mRssiPollToken) {
                        // Get Info and continue polling
                        fetchRssiAndLinkSpeedNative();
                        sendMessageDelayed(obtainMessage(CMD_RSSI_POLL,
=======
                    eventLoggingEnabled = false;
                    if (message.arg1 == mRssiPollToken) {
                        // Get Info and continue polling
                        fetchRssiAndLinkSpeedNative();
                        sendMessageDelayed(obtainMessage(WifiStateMachine.CMD_RSSI_POLL,
>>>>>>> upstream/master
                                mRssiPollToken, 0), POLL_RSSI_INTERVAL_MSECS);
                    } else {
                        // Polling has completed
                    }
                    break;
                case CMD_ENABLE_RSSI_POLL:
                    mEnableRssiPolling = (message.arg1 == 1);
                    mRssiPollToken++;
                    if (mEnableRssiPolling) {
                        // first poll
                        fetchRssiAndLinkSpeedNative();
<<<<<<< HEAD
                        sendMessageDelayed(obtainMessage(CMD_RSSI_POLL,
                                mRssiPollToken, 0), POLL_RSSI_INTERVAL_MSECS);
                    }
                    break;
                case WifiWatchdogStateMachine.RSSI_FETCH:
                    fetchRssiAndLinkSpeedNative();
                    replyToMessage(message, WifiWatchdogStateMachine.RSSI_FETCH_SUCCEEDED,
                            mWifiInfo.getRssi());
                    break;
                default:
                    return NOT_HANDLED;
            }

            return HANDLED;
        }

        @Override
        public void exit() {
=======
                        sendMessageDelayed(obtainMessage(WifiStateMachine.CMD_RSSI_POLL,
                                mRssiPollToken, 0), POLL_RSSI_INTERVAL_MSECS);
                    }
                    break;
                default:
                    return NOT_HANDLED;
            }
            if (eventLoggingEnabled) {
                EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
            }
            return HANDLED;
        }
        @Override
        public void exit() {

            /* Request a CS wakelock during transition to mobile */
            checkAndSetConnectivityInstance();
            mCm.requestNetworkTransitionWakelock(TAG);

>>>>>>> upstream/master
            /* If a scan result is pending in connected state, the supplicant
             * is in SCAN_ONLY_MODE. Restore CONNECT_MODE on exit
             */
            if (mScanResultIsPending) {
<<<<<<< HEAD
                mWifiNative.setScanResultHandling(CONNECT_MODE);
            }
        }
    }

    class ObtainingIpState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            if (!mWifiConfigStore.isUsingStaticIp(mLastNetworkId)) {
                //start DHCP
                mDhcpStateMachine = DhcpStateMachine.makeDhcpStateMachine(
                        mContext, WifiStateMachine.this, mInterfaceName);
                mDhcpStateMachine.registerForPreDhcpNotification();
                mDhcpStateMachine.sendMessage(DhcpStateMachine.CMD_START_DHCP);
            } else {
                DhcpInfoInternal dhcpInfoInternal = mWifiConfigStore.getIpConfiguration(
                        mLastNetworkId);
                InterfaceConfiguration ifcg = new InterfaceConfiguration();
                ifcg.setLinkAddress(dhcpInfoInternal.makeLinkAddress());
                ifcg.setInterfaceUp();
                try {
                    mNwService.setInterfaceConfig(mInterfaceName, ifcg);
                    if (DBG) log("Static IP configuration succeeded");
                    sendMessage(CMD_STATIC_IP_SUCCESS, dhcpInfoInternal);
                } catch (RemoteException re) {
                    loge("Static IP configuration failed: " + re);
                    sendMessage(CMD_STATIC_IP_FAILURE);
                } catch (IllegalStateException e) {
                    loge("Static IP configuration failed: " + e);
                    sendMessage(CMD_STATIC_IP_FAILURE);
                }
            }
        }
      @Override
      public boolean processMessage(Message message) {
          if (DBG) log(getName() + message.toString() + "\n");
          switch(message.what) {
            case CMD_STATIC_IP_SUCCESS:
                  handleSuccessfulIpConfiguration((DhcpInfoInternal) message.obj);
                  transitionTo(mVerifyingLinkState);
                  break;
              case CMD_STATIC_IP_FAILURE:
                  handleFailedIpConfiguration();
                  transitionTo(mDisconnectingState);
                  break;
             case WifiManager.SAVE_NETWORK:
                  deferMessage(message);
                  break;
                  /* Defer any power mode changes since we must keep active power mode at DHCP */
              case CMD_SET_HIGH_PERF_MODE:
                  deferMessage(message);
                  break;
              default:
                  return NOT_HANDLED;
          }
          return HANDLED;
      }
    }

    class VerifyingLinkState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
            setNetworkDetailedState(DetailedState.VERIFYING_POOR_LINK);
            mWifiConfigStore.updateStatus(mLastNetworkId, DetailedState.VERIFYING_POOR_LINK);
            sendNetworkStateChangeBroadcast(mLastBssid);
        }
        @Override
        public boolean processMessage(Message message) {
            switch (message.what) {
                case WifiWatchdogStateMachine.POOR_LINK_DETECTED:
                    //stay here
                    break;
                case WifiWatchdogStateMachine.GOOD_LINK_DETECTED:
                    try {
                        mNwService.enableIpv6(mInterfaceName);
                    } catch (RemoteException re) {
                        loge("Failed to enable IPv6: " + re);
                    } catch (IllegalStateException e) {
                        loge("Failed to enable IPv6: " + e);
                    }

                    setNetworkDetailedState(DetailedState.CONNECTED);
                    mWifiConfigStore.updateStatus(mLastNetworkId, DetailedState.CONNECTED);
                    sendNetworkStateChangeBroadcast(mLastBssid);
                    transitionTo(mConnectedState);
                    break;
                default:
                    return NOT_HANDLED;
            }
            return HANDLED;
        }
    }

    class ConnectedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
       }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
               case WifiWatchdogStateMachine.POOR_LINK_DETECTED:
                    if (DBG) log("Watchdog reports poor link");
                    try {
                        mNwService.disableIpv6(mInterfaceName);
                    } catch (RemoteException re) {
                        loge("Failed to disable IPv6: " + re);
                    } catch (IllegalStateException e) {
                        loge("Failed to disable IPv6: " + e);
                    }
                    /* Report a disconnect */
                    setNetworkDetailedState(DetailedState.DISCONNECTED);
                    mWifiConfigStore.updateStatus(mLastNetworkId, DetailedState.DISCONNECTED);
                    sendNetworkStateChangeBroadcast(mLastBssid);

                    transitionTo(mVerifyingLinkState);
                    break;
                default:
                    return NOT_HANDLED;
            }
            return HANDLED;
        }
        @Override
        public void exit() {
            /* Request a CS wakelock during transition to mobile */
            checkAndSetConnectivityInstance();
            mCm.requestNetworkTransitionWakelock(TAG);
        }
=======
                WifiNative.setScanResultHandlingCommand(CONNECT_MODE);
            }
        }
>>>>>>> upstream/master
    }

    class DisconnectingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
                case CMD_SET_SCAN_MODE:
                    if (message.arg1 == SCAN_ONLY_MODE) {
                        deferMessage(message);
                    }
                    break;
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    /* If we get a SUPPLICANT_STATE_CHANGE_EVENT before NETWORK_DISCONNECTION_EVENT
                     * we have missed the network disconnection, transition to mDisconnectedState
                     * and handle the rest of the events there
                     */
                    deferMessage(message);
                    handleNetworkDisconnect();
                    transitionTo(mDisconnectedState);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class DisconnectedState extends State {
        private boolean mAlarmEnabled = false;
        /* This is set from the overlay config file or from a secure setting.
         * A value of 0 disables scanning in the framework.
         */
        private long mFrameworkScanIntervalMs;

        private void setScanAlarm(boolean enabled) {
            if (enabled == mAlarmEnabled) return;
            if (enabled) {
                if (mFrameworkScanIntervalMs > 0) {
                    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + mFrameworkScanIntervalMs,
                            mFrameworkScanIntervalMs,
                            mScanIntent);
                    mAlarmEnabled = true;
                }
            } else {
                mAlarmManager.cancel(mScanIntent);
                mAlarmEnabled = false;
            }
        }

        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            mFrameworkScanIntervalMs = Settings.Secure.getLong(mContext.getContentResolver(),
                    Settings.Secure.WIFI_FRAMEWORK_SCAN_INTERVAL_MS,
                    mDefaultFrameworkScanIntervalMs);
            /*
             * We initiate background scanning if it is enabled, otherwise we
             * initiate an infrequent scan that wakes up the device to ensure
             * a user connects to an access point on the move
             */
            if (mEnableBackgroundScan) {
                /* If a regular scan result is pending, do not initiate background
                 * scan until the scan results are returned. This is needed because
                 * initiating a background scan will cancel the regular scan and
                 * scan results will not be returned until background scanning is
                 * cleared
                 */
                if (!mScanResultIsPending) {
<<<<<<< HEAD
                    mWifiNative.enableBackgroundScan(true);
=======
                    WifiNative.enableBackgroundScanCommand(true);
>>>>>>> upstream/master
                }
            } else {
                setScanAlarm(true);
            }
<<<<<<< HEAD

            /**
             * If we have no networks saved, the supplicant stops doing the periodic scan.
             * The scans are useful to notify the user of the presence of an open network.
             * Note that these are not wake up scans.
             */
            if (mWifiConfigStore.getConfiguredNetworks().size() == 0) {
                sendMessageDelayed(obtainMessage(CMD_NO_NETWORKS_PERIODIC_SCAN,
                            ++mPeriodicScanToken, 0), mSupplicantScanIntervalMs);
            }
=======
>>>>>>> upstream/master
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
<<<<<<< HEAD
            boolean ret = HANDLED;
            switch (message.what) {
                case CMD_NO_NETWORKS_PERIODIC_SCAN:
                    if (message.arg1 == mPeriodicScanToken &&
                            mWifiConfigStore.getConfiguredNetworks().size() == 0) {
                        sendMessage(CMD_START_SCAN);
                        sendMessageDelayed(obtainMessage(CMD_NO_NETWORKS_PERIODIC_SCAN,
                                    ++mPeriodicScanToken, 0), mSupplicantScanIntervalMs);
                    }
                    break;
                case WifiManager.FORGET_NETWORK:
                case CMD_REMOVE_NETWORK:
                    // Set up a delayed message here. After the forget/remove is handled
                    // the handled delayed message will determine if there is a need to
                    // scan and continue
                    sendMessageDelayed(obtainMessage(CMD_NO_NETWORKS_PERIODIC_SCAN,
                                ++mPeriodicScanToken, 0), mSupplicantScanIntervalMs);
                    ret = NOT_HANDLED;
                    break;
                case CMD_SET_SCAN_MODE:
                    if (message.arg1 == SCAN_ONLY_MODE) {
                        mWifiNative.setScanResultHandling(message.arg1);
                        //Supplicant disconnect to prevent further connects
                        mWifiNative.disconnect();
=======
            switch (message.what) {
                case CMD_SET_SCAN_MODE:
                    if (message.arg1 == SCAN_ONLY_MODE) {
                        WifiNative.setScanResultHandlingCommand(message.arg1);
                        //Supplicant disconnect to prevent further connects
                        WifiNative.disconnectCommand();
>>>>>>> upstream/master
                        mIsScanMode = true;
                        transitionTo(mScanModeState);
                    }
                    break;
                case CMD_ENABLE_BACKGROUND_SCAN:
                    mEnableBackgroundScan = (message.arg1 == 1);
                    if (mEnableBackgroundScan) {
<<<<<<< HEAD
                        mWifiNative.enableBackgroundScan(true);
                        setScanAlarm(false);
                    } else {
                        mWifiNative.enableBackgroundScan(false);
=======
                        WifiNative.enableBackgroundScanCommand(true);
                        setScanAlarm(false);
                    } else {
                        WifiNative.enableBackgroundScanCommand(false);
>>>>>>> upstream/master
                        setScanAlarm(true);
                    }
                    break;
                    /* Ignore network disconnect */
                case WifiMonitor.NETWORK_DISCONNECTION_EVENT:
                    break;
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    StateChangeResult stateChangeResult = (StateChangeResult) message.obj;
                    setNetworkDetailedState(WifiInfo.getDetailedStateOf(stateChangeResult.state));
                    /* ConnectModeState does the rest of the handling */
<<<<<<< HEAD
                    ret = NOT_HANDLED;
                    break;
                case CMD_START_SCAN:
                    /* Disable background scan temporarily during a regular scan */
                    if (mEnableBackgroundScan) {
                        mWifiNative.enableBackgroundScan(false);
                    }
                    /* Handled in parent state */
                    ret = NOT_HANDLED;
                    break;
                case WifiMonitor.SCAN_RESULTS_EVENT:
                    /* Re-enable background scan when a pending scan result is received */
                    if (mEnableBackgroundScan && mScanResultIsPending) {
                        mWifiNative.enableBackgroundScan(true);
                    }
                    /* Handled in parent state */
                    ret = NOT_HANDLED;
                    break;
                default:
                    ret = NOT_HANDLED;
            }
            return ret;
=======
                    return NOT_HANDLED;
                case CMD_START_SCAN:
                    /* Disable background scan temporarily during a regular scan */
                    if (mEnableBackgroundScan) {
                        WifiNative.enableBackgroundScanCommand(false);
                    }
                    /* Handled in parent state */
                    return NOT_HANDLED;
                case WifiMonitor.SCAN_RESULTS_EVENT:
                    /* Re-enable background scan when a pending scan result is received */
                    if (mEnableBackgroundScan && mScanResultIsPending) {
                        WifiNative.enableBackgroundScanCommand(true);
                    }
                    /* Handled in parent state */
                    return NOT_HANDLED;
                default:
                    return NOT_HANDLED;
            }
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
            return HANDLED;
>>>>>>> upstream/master
        }

        @Override
        public void exit() {
            /* No need for a background scan upon exit from a disconnected state */
            if (mEnableBackgroundScan) {
<<<<<<< HEAD
                mWifiNative.enableBackgroundScan(false);
=======
                WifiNative.enableBackgroundScanCommand(false);
>>>>>>> upstream/master
            }
            setScanAlarm(false);
        }
    }

<<<<<<< HEAD
    class WpsRunningState extends State {
        //Tracks the source to provide a reply
        private Message mSourceMessage;
=======
    class WaitForWpsCompletionState extends State {
>>>>>>> upstream/master
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
<<<<<<< HEAD
            mSourceMessage = Message.obtain(getCurrentMessage());
=======
>>>>>>> upstream/master
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch (message.what) {
<<<<<<< HEAD
                case WifiMonitor.WPS_SUCCESS_EVENT:
                    replyToMessage(mSourceMessage, WifiManager.WPS_COMPLETED);
                    mSourceMessage.recycle();
                    mSourceMessage = null;
                    transitionTo(mDisconnectedState);
                    break;
                case WifiMonitor.WPS_OVERLAP_EVENT:
                    replyToMessage(mSourceMessage, WifiManager.WPS_FAILED,
                            WifiManager.WPS_OVERLAP_ERROR);
                    mSourceMessage.recycle();
                    mSourceMessage = null;
                    transitionTo(mDisconnectedState);
                    break;
                case WifiMonitor.WPS_FAIL_EVENT:
                    //arg1 has the reason for the failure
                    replyToMessage(mSourceMessage, WifiManager.WPS_FAILED, message.arg1);
                    mSourceMessage.recycle();
                    mSourceMessage = null;
                    transitionTo(mDisconnectedState);
                    break;
                case WifiMonitor.WPS_TIMEOUT_EVENT:
                    replyToMessage(mSourceMessage, WifiManager.WPS_FAILED,
                            WifiManager.WPS_TIMED_OUT);
                    mSourceMessage.recycle();
                    mSourceMessage = null;
                    transitionTo(mDisconnectedState);
                    break;
                case WifiManager.START_WPS:
                    replyToMessage(message, WifiManager.WPS_FAILED, WifiManager.IN_PROGRESS);
                    break;
                case WifiManager.CANCEL_WPS:
                    if (mWifiNative.cancelWps()) {
                        replyToMessage(message, WifiManager.CANCEL_WPS_SUCCEDED);
                    } else {
                        replyToMessage(message, WifiManager.CANCEL_WPS_FAILED, WifiManager.ERROR);
                    }
                    transitionTo(mDisconnectedState);
                    break;
=======
>>>>>>> upstream/master
                /* Defer all commands that can cause connections to a different network
                 * or put the state machine out of connect mode
                 */
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
<<<<<<< HEAD
                case WifiManager.CONNECT_NETWORK:
                case CMD_ENABLE_NETWORK:
                case CMD_RECONNECT:
                case CMD_REASSOCIATE:
                case WifiMonitor.NETWORK_CONNECTION_EVENT: /* Handled after exiting WPS state */
=======
                case CMD_CONNECT_NETWORK:
                case CMD_ENABLE_NETWORK:
                case CMD_RECONNECT:
                case CMD_REASSOCIATE:
                case WifiMonitor.NETWORK_CONNECTION_EVENT: /* Handled after IP & proxy update */
>>>>>>> upstream/master
                    deferMessage(message);
                    break;
                case WifiMonitor.NETWORK_DISCONNECTION_EVENT:
                    if (DBG) log("Network connection lost");
                    handleNetworkDisconnect();
                    break;
<<<<<<< HEAD
                case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
                    //Throw away supplicant state changes when WPS is running.
                    //We will start getting supplicant state changes once we get
                    //a WPS success or failure
=======
                case WPS_COMPLETED_EVENT:
                    /* we are still disconnected until we see a network connection
                     * notification */
                    transitionTo(mDisconnectedState);
>>>>>>> upstream/master
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
            return HANDLED;
        }

        @Override
        public void exit() {
            mWifiConfigStore.enableAllNetworks();
            mWifiConfigStore.loadConfiguredNetworks();
        }
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
            return HANDLED;
        }
>>>>>>> upstream/master
    }

    class SoftApStartingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            final Message message = getCurrentMessage();
            if (message.what == CMD_START_AP) {
                final WifiConfiguration config = (WifiConfiguration) message.obj;

                if (config == null) {
                    mWifiApConfigChannel.sendMessage(CMD_REQUEST_AP_CONFIG);
                } else {
                    mWifiApConfigChannel.sendMessage(CMD_SET_AP_CONFIG, config);
                    startSoftApWithConfig(config);
                }
            } else {
                throw new RuntimeException("Illegal transition to SoftApStartingState: " + message);
            }
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                case CMD_TETHER_STATE_CHANGE:
<<<<<<< HEAD
=======
                case WifiP2pService.P2P_ENABLE_PENDING:
>>>>>>> upstream/master
                    deferMessage(message);
                    break;
                case WifiStateMachine.CMD_RESPONSE_AP_CONFIG:
                    WifiConfiguration config = (WifiConfiguration) message.obj;
                    if (config != null) {
                        startSoftApWithConfig(config);
                    } else {
                        loge("Softap config is null!");
                        sendMessage(CMD_START_AP_FAILURE);
                    }
                    break;
                case CMD_START_AP_SUCCESS:
                    setWifiApState(WIFI_AP_STATE_ENABLED);
                    transitionTo(mSoftApStartedState);
                    break;
                case CMD_START_AP_FAILURE:
                    // initiate driver unload
                    sendMessage(obtainMessage(CMD_UNLOAD_DRIVER, WIFI_AP_STATE_FAILED, 0));
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class SoftApStartedState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_STOP_AP:
                    if (DBG) log("Stopping Soft AP");
                    setWifiApState(WIFI_AP_STATE_DISABLING);

                    /* We have not tethered at this point, so we just shutdown soft Ap */
                    try {
                        mNwService.stopAccessPoint(mInterfaceName);
                    } catch(Exception e) {
                        loge("Exception in stopAccessPoint()");
                    }
                    transitionTo(mDriverLoadedState);
                    break;
                case CMD_START_AP:
                    // Ignore a start on a running access point
                    break;
                    /* Fail client mode operation when soft AP is enabled */
                case CMD_START_SUPPLICANT:
                   loge("Cannot start supplicant with a running soft AP");
                    setWifiState(WIFI_STATE_UNKNOWN);
                    break;
                case CMD_TETHER_STATE_CHANGE:
                    TetherStateChange stateChange = (TetherStateChange) message.obj;
                    if (startTethering(stateChange.available)) {
                        transitionTo(mTetheringState);
                    }
                    break;
<<<<<<< HEAD
                default:
                    return NOT_HANDLED;
            }
=======
                case WifiP2pService.P2P_ENABLE_PENDING:
                    // turn of soft Ap and defer to be handled in DriverUnloadedState
                    setWifiApEnabled(null, false);
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
            return HANDLED;
        }
    }

    class WaitForP2pDisableState extends State {
        private int mSavedArg;
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            //Preserve the argument arg1 that has information used in DriverLoadingState
            mSavedArg = getCurrentMessage().arg1;
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case WifiP2pService.WIFI_ENABLE_PROCEED:
                    //restore argument from original message (CMD_LOAD_DRIVER)
                    message.arg1 = mSavedArg;
                    transitionTo(mDriverLoadingState);
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
                case CMD_SET_HIGH_PERF_MODE:
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class TetheringState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            /* Send ourselves a delayed message to shut down if tethering fails to notify */
            sendMessageDelayed(obtainMessage(CMD_TETHER_NOTIFICATION_TIMED_OUT,
                    ++mTetherToken, 0), TETHER_NOTIFICATION_TIME_OUT_MSECS);
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_TETHER_STATE_CHANGE:
                    TetherStateChange stateChange = (TetherStateChange) message.obj;
                    if (isWifiTethered(stateChange.active)) {
                        transitionTo(mTetheredState);
                    }
                    return HANDLED;
                case CMD_TETHER_NOTIFICATION_TIMED_OUT:
                    if (message.arg1 == mTetherToken) {
                        loge("Failed to get tether update, shutdown soft access point");
                        setWifiApEnabled(null, false);
                    }
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
<<<<<<< HEAD
=======
                case WifiP2pService.P2P_ENABLE_PENDING:
>>>>>>> upstream/master
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class TetheredState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());
        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_TETHER_STATE_CHANGE:
                    TetherStateChange stateChange = (TetherStateChange) message.obj;
                    if (!isWifiTethered(stateChange.active)) {
                        loge("Tethering reports wifi as untethered!, shut down soft Ap");
                        setWifiApEnabled(null, false);
                    }
                    return HANDLED;
                case CMD_STOP_AP:
                    if (DBG) log("Untethering before stopping AP");
                    setWifiApState(WIFI_AP_STATE_DISABLING);
                    stopTethering();
                    transitionTo(mSoftApStoppingState);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

    class SoftApStoppingState extends State {
        @Override
        public void enter() {
            if (DBG) log(getName() + "\n");
            EventLog.writeEvent(EVENTLOG_WIFI_STATE_CHANGED, getName());

            /* Send ourselves a delayed message to shut down if tethering fails to notify */
            sendMessageDelayed(obtainMessage(CMD_TETHER_NOTIFICATION_TIMED_OUT,
                    ++mTetherToken, 0), TETHER_NOTIFICATION_TIME_OUT_MSECS);

        }
        @Override
        public boolean processMessage(Message message) {
            if (DBG) log(getName() + message.toString() + "\n");
            switch(message.what) {
                case CMD_TETHER_STATE_CHANGE:
                    TetherStateChange stateChange = (TetherStateChange) message.obj;

                    /* Wait till wifi is untethered */
                    if (isWifiTethered(stateChange.active)) break;

                    try {
                        mNwService.stopAccessPoint(mInterfaceName);
                    } catch(Exception e) {
                        loge("Exception in stopAccessPoint()");
                    }
                    transitionTo(mDriverLoadedState);
                    break;
                case CMD_TETHER_NOTIFICATION_TIMED_OUT:
                    if (message.arg1 == mTetherToken) {
                        loge("Failed to get tether update, force stop access point");
                        try {
                            mNwService.stopAccessPoint(mInterfaceName);
                        } catch(Exception e) {
                            loge("Exception in stopAccessPoint()");
                        }
                        transitionTo(mDriverLoadedState);
                    }
                    break;
                case CMD_LOAD_DRIVER:
                case CMD_UNLOAD_DRIVER:
                case CMD_START_SUPPLICANT:
                case CMD_STOP_SUPPLICANT:
                case CMD_START_AP:
                case CMD_STOP_AP:
                case CMD_START_DRIVER:
                case CMD_STOP_DRIVER:
                case CMD_SET_SCAN_MODE:
                case CMD_SET_SCAN_TYPE:
<<<<<<< HEAD
=======
                case CMD_SET_HIGH_PERF_MODE:
>>>>>>> upstream/master
                case CMD_SET_COUNTRY_CODE:
                case CMD_SET_FREQUENCY_BAND:
                case CMD_START_PACKET_FILTERING:
                case CMD_STOP_PACKET_FILTERING:
<<<<<<< HEAD
=======
                case WifiP2pService.P2P_ENABLE_PENDING:
>>>>>>> upstream/master
                    deferMessage(message);
                    break;
                default:
                    return NOT_HANDLED;
            }
<<<<<<< HEAD
=======
            EventLog.writeEvent(EVENTLOG_WIFI_EVENT_HANDLED, message.what);
>>>>>>> upstream/master
            return HANDLED;
        }
    }

<<<<<<< HEAD
    //State machine initiated requests can have replyTo set to null indicating
    //there are no recepients, we ignore those reply actions
    private void replyToMessage(Message msg, int what) {
        if (msg.replyTo == null) return;
        Message dstMsg = obtainMessageWithArg2(msg);
        dstMsg.what = what;
        mReplyChannel.replyToMessage(msg, dstMsg);
    }

    private void replyToMessage(Message msg, int what, int arg1) {
        if (msg.replyTo == null) return;
        Message dstMsg = obtainMessageWithArg2(msg);
        dstMsg.what = what;
        dstMsg.arg1 = arg1;
        mReplyChannel.replyToMessage(msg, dstMsg);
    }

    private void replyToMessage(Message msg, int what, Object obj) {
        if (msg.replyTo == null) return;
        Message dstMsg = obtainMessageWithArg2(msg);
        dstMsg.what = what;
        dstMsg.obj = obj;
        mReplyChannel.replyToMessage(msg, dstMsg);
    }

    /**
     * arg2 on the source message has a unique id that needs to be retained in replies
     * to match the request
     *
     * see WifiManager for details
     */
    private Message obtainMessageWithArg2(Message srcMsg) {
        Message msg = Message.obtain();
        msg.arg2 = srcMsg.arg2;
        return msg;
    }

=======
>>>>>>> upstream/master
    private void log(String s) {
        Log.d(TAG, s);
    }

    private void loge(String s) {
        Log.e(TAG, s);
    }
}
