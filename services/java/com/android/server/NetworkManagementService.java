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

package com.android.server;

<<<<<<< HEAD
import static android.Manifest.permission.CONNECTIVITY_INTERNAL;
import static android.Manifest.permission.DUMP;
import static android.Manifest.permission.SHUTDOWN;
=======
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.DUMP;
import static android.Manifest.permission.MANAGE_NETWORK_POLICY;
>>>>>>> upstream/master
import static android.net.NetworkStats.SET_DEFAULT;
import static android.net.NetworkStats.TAG_NONE;
import static android.net.NetworkStats.UID_ALL;
import static android.net.TrafficStats.UID_TETHERING;
<<<<<<< HEAD
import static com.android.server.NetworkManagementService.NetdResponseCode.InterfaceGetCfgResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.InterfaceListResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.InterfaceRxThrottleResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.InterfaceTxThrottleResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.IpFwdStatusResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.TetherDnsFwdTgtListResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.TetherInterfaceListResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.TetherStatusResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.TetheringStatsResult;
import static com.android.server.NetworkManagementService.NetdResponseCode.TtyListResult;
import static com.android.server.NetworkManagementSocketTagger.PROP_QTAGUID_ENABLED;

import android.content.Context;
=======
import static android.provider.Settings.Secure.NETSTATS_ENABLED;
import static com.android.server.NetworkManagementSocketTagger.PROP_QTAGUID_ENABLED;

import android.content.Context;
import android.content.pm.PackageManager;
>>>>>>> upstream/master
import android.net.INetworkManagementEventObserver;
import android.net.InterfaceConfiguration;
import android.net.LinkAddress;
import android.net.NetworkStats;
import android.net.NetworkUtils;
import android.net.RouteInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
<<<<<<< HEAD
import android.os.Handler;
import android.os.INetworkManagementService;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
=======
import android.os.Binder;
import android.os.INetworkManagementService;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
>>>>>>> upstream/master
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;

import com.android.internal.net.NetworkStatsFactory;
<<<<<<< HEAD
import com.android.server.NativeDaemonConnector.Command;
import com.google.android.collect.Maps;
=======
import com.google.android.collect.Sets;
>>>>>>> upstream/master

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
import java.util.HashSet;
>>>>>>> upstream/master
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

/**
 * @hide
 */
public class NetworkManagementService extends INetworkManagementService.Stub
        implements Watchdog.Monitor {
    private static final String TAG = "NetworkManagementService";
    private static final boolean DBG = false;
    private static final String NETD_TAG = "NetdConnector";

<<<<<<< HEAD
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
=======
    private static final int ADD = 1;
    private static final int REMOVE = 2;
>>>>>>> upstream/master

    private static final String DEFAULT = "default";
    private static final String SECONDARY = "secondary";

    /**
     * Name representing {@link #setGlobalAlert(long)} limit when delivered to
     * {@link INetworkManagementEventObserver#limitReached(String, String)}.
     */
    public static final String LIMIT_GLOBAL_ALERT = "globalAlert";

    class NetdResponseCode {
        /* Keep in sync with system/netd/ResponseCode.h */
        public static final int InterfaceListResult       = 110;
        public static final int TetherInterfaceListResult = 111;
        public static final int TetherDnsFwdTgtListResult = 112;
        public static final int TtyListResult             = 113;

        public static final int TetherStatusResult        = 210;
        public static final int IpFwdStatusResult         = 211;
        public static final int InterfaceGetCfgResult     = 213;
        public static final int SoftapStatusResult        = 214;
        public static final int InterfaceRxCounterResult  = 216;
        public static final int InterfaceTxCounterResult  = 217;
        public static final int InterfaceRxThrottleResult = 218;
        public static final int InterfaceTxThrottleResult = 219;
        public static final int QuotaCounterResult        = 220;
        public static final int TetheringStatsResult      = 221;
<<<<<<< HEAD
        public static final int DnsProxyQueryResult       = 222;
=======
>>>>>>> upstream/master

        public static final int InterfaceChange           = 600;
        public static final int BandwidthControl          = 601;
    }

    /**
     * Binder context for this service
     */
    private Context mContext;

    /**
     * connector object for communicating with netd
     */
    private NativeDaemonConnector mConnector;

<<<<<<< HEAD
    private final Handler mMainHandler = new Handler();

    private Thread mThread;
    private CountDownLatch mConnectedSignal = new CountDownLatch(1);

    private final RemoteCallbackList<INetworkManagementEventObserver> mObservers =
            new RemoteCallbackList<INetworkManagementEventObserver>();
=======
    private Thread mThread;
    private final CountDownLatch mConnectedSignal = new CountDownLatch(1);

    // TODO: replace with RemoteCallbackList
    private ArrayList<INetworkManagementEventObserver> mObservers;
>>>>>>> upstream/master

    private final NetworkStatsFactory mStatsFactory = new NetworkStatsFactory();

    private Object mQuotaLock = new Object();
    /** Set of interfaces with active quotas. */
<<<<<<< HEAD
    private HashMap<String, Long> mActiveQuotas = Maps.newHashMap();
    /** Set of interfaces with active alerts. */
    private HashMap<String, Long> mActiveAlerts = Maps.newHashMap();
=======
    private HashSet<String> mActiveQuotaIfaces = Sets.newHashSet();
    /** Set of interfaces with active alerts. */
    private HashSet<String> mActiveAlertIfaces = Sets.newHashSet();
>>>>>>> upstream/master
    /** Set of UIDs with active reject rules. */
    private SparseBooleanArray mUidRejectOnQuota = new SparseBooleanArray();

    private volatile boolean mBandwidthControlEnabled;

    /**
     * Constructs a new NetworkManagementService instance
     *
     * @param context  Binder context for this service
     */
    private NetworkManagementService(Context context) {
        mContext = context;
<<<<<<< HEAD
=======
        mObservers = new ArrayList<INetworkManagementEventObserver>();
>>>>>>> upstream/master

        if ("simulator".equals(SystemProperties.get("ro.product.device"))) {
            return;
        }

        mConnector = new NativeDaemonConnector(
<<<<<<< HEAD
                new NetdCallbackReceiver(), "netd", 10, NETD_TAG, 160);
=======
                new NetdCallbackReceiver(), "netd", 10, NETD_TAG);
>>>>>>> upstream/master
        mThread = new Thread(mConnector, NETD_TAG);

        // Add ourself to the Watchdog monitors.
        Watchdog.getInstance().addMonitor(this);
    }

    public static NetworkManagementService create(Context context) throws InterruptedException {
<<<<<<< HEAD
        final NetworkManagementService service = new NetworkManagementService(context);
        final CountDownLatch connectedSignal = service.mConnectedSignal;
        if (DBG) Slog.d(TAG, "Creating NetworkManagementService");
        service.mThread.start();
        if (DBG) Slog.d(TAG, "Awaiting socket connection");
        connectedSignal.await();
=======
        NetworkManagementService service = new NetworkManagementService(context);
        if (DBG) Slog.d(TAG, "Creating NetworkManagementService");
        service.mThread.start();
        if (DBG) Slog.d(TAG, "Awaiting socket connection");
        service.mConnectedSignal.await();
>>>>>>> upstream/master
        if (DBG) Slog.d(TAG, "Connected");
        return service;
    }

    public void systemReady() {
<<<<<<< HEAD
        prepareNativeDaemon();
        if (DBG) Slog.d(TAG, "Prepared");
    }

    @Override
    public void registerObserver(INetworkManagementEventObserver observer) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        mObservers.register(observer);
    }

    @Override
    public void unregisterObserver(INetworkManagementEventObserver observer) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        mObservers.unregister(observer);
=======
        // only enable bandwidth control when support exists, and requested by
        // system setting.
        final boolean hasKernelSupport = new File("/proc/net/xt_qtaguid/ctrl").exists();
        final boolean shouldEnable =
                Settings.Secure.getInt(mContext.getContentResolver(), NETSTATS_ENABLED, 1) != 0;

        if (hasKernelSupport && shouldEnable) {
            Slog.d(TAG, "enabling bandwidth control");
            try {
                mConnector.doCommand("bandwidth enable");
                mBandwidthControlEnabled = true;
            } catch (NativeDaemonConnectorException e) {
                Log.wtf(TAG, "problem enabling bandwidth controls", e);
            }
        } else {
            Slog.d(TAG, "not enabling bandwidth control");
        }

        SystemProperties.set(PROP_QTAGUID_ENABLED, mBandwidthControlEnabled ? "1" : "0");
    }

    public void registerObserver(INetworkManagementEventObserver obs) {
        Slog.d(TAG, "Registering observer");
        mObservers.add(obs);
    }

    public void unregisterObserver(INetworkManagementEventObserver obs) {
        Slog.d(TAG, "Unregistering observer");
        mObservers.remove(mObservers.indexOf(obs));
>>>>>>> upstream/master
    }

    /**
     * Notify our observers of an interface status change
     */
    private void notifyInterfaceStatusChanged(String iface, boolean up) {
<<<<<<< HEAD
        final int length = mObservers.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mObservers.getBroadcastItem(i).interfaceStatusChanged(iface, up);
            } catch (RemoteException e) {
            }
        }
        mObservers.finishBroadcast();
=======
        for (INetworkManagementEventObserver obs : mObservers) {
            try {
                obs.interfaceStatusChanged(iface, up);
            } catch (Exception ex) {
                Slog.w(TAG, "Observer notifier failed", ex);
            }
        }
>>>>>>> upstream/master
    }

    /**
     * Notify our observers of an interface link state change
     * (typically, an Ethernet cable has been plugged-in or unplugged).
     */
    private void notifyInterfaceLinkStateChanged(String iface, boolean up) {
<<<<<<< HEAD
        final int length = mObservers.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mObservers.getBroadcastItem(i).interfaceLinkStateChanged(iface, up);
            } catch (RemoteException e) {
            }
        }
        mObservers.finishBroadcast();
=======
        for (INetworkManagementEventObserver obs : mObservers) {
            try {
                obs.interfaceLinkStateChanged(iface, up);
            } catch (Exception ex) {
                Slog.w(TAG, "Observer notifier failed", ex);
            }
        }
>>>>>>> upstream/master
    }

    /**
     * Notify our observers of an interface addition.
     */
    private void notifyInterfaceAdded(String iface) {
<<<<<<< HEAD
        final int length = mObservers.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mObservers.getBroadcastItem(i).interfaceAdded(iface);
            } catch (RemoteException e) {
            }
        }
        mObservers.finishBroadcast();
=======
        for (INetworkManagementEventObserver obs : mObservers) {
            try {
                obs.interfaceAdded(iface);
            } catch (Exception ex) {
                Slog.w(TAG, "Observer notifier failed", ex);
            }
        }
>>>>>>> upstream/master
    }

    /**
     * Notify our observers of an interface removal.
     */
    private void notifyInterfaceRemoved(String iface) {
        // netd already clears out quota and alerts for removed ifaces; update
        // our sanity-checking state.
<<<<<<< HEAD
        mActiveAlerts.remove(iface);
        mActiveQuotas.remove(iface);

        final int length = mObservers.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mObservers.getBroadcastItem(i).interfaceRemoved(iface);
            } catch (RemoteException e) {
            }
        }
        mObservers.finishBroadcast();
=======
        mActiveAlertIfaces.remove(iface);
        mActiveQuotaIfaces.remove(iface);

        for (INetworkManagementEventObserver obs : mObservers) {
            try {
                obs.interfaceRemoved(iface);
            } catch (Exception ex) {
                Slog.w(TAG, "Observer notifier failed", ex);
            }
        }
>>>>>>> upstream/master
    }

    /**
     * Notify our observers of a limit reached.
     */
    private void notifyLimitReached(String limitName, String iface) {
<<<<<<< HEAD
        final int length = mObservers.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                mObservers.getBroadcastItem(i).limitReached(limitName, iface);
            } catch (RemoteException e) {
            }
        }
        mObservers.finishBroadcast();
    }

    /**
     * Prepare native daemon once connected, enabling modules and pushing any
     * existing in-memory rules.
     */
    private void prepareNativeDaemon() {
        mBandwidthControlEnabled = false;

        // only enable bandwidth control when support exists
        final boolean hasKernelSupport = new File("/proc/net/xt_qtaguid/ctrl").exists();
        if (hasKernelSupport) {
            Slog.d(TAG, "enabling bandwidth control");
            try {
                mConnector.execute("bandwidth", "enable");
                mBandwidthControlEnabled = true;
            } catch (NativeDaemonConnectorException e) {
                Log.wtf(TAG, "problem enabling bandwidth controls", e);
            }
        } else {
            Slog.d(TAG, "not enabling bandwidth control");
        }

        SystemProperties.set(PROP_QTAGUID_ENABLED, mBandwidthControlEnabled ? "1" : "0");

        // push any existing quota or UID rules
        synchronized (mQuotaLock) {
            int size = mActiveQuotas.size();
            if (size > 0) {
                Slog.d(TAG, "pushing " + size + " active quota rules");
                final HashMap<String, Long> activeQuotas = mActiveQuotas;
                mActiveQuotas = Maps.newHashMap();
                for (Map.Entry<String, Long> entry : activeQuotas.entrySet()) {
                    setInterfaceQuota(entry.getKey(), entry.getValue());
                }
            }

            size = mActiveAlerts.size();
            if (size > 0) {
                Slog.d(TAG, "pushing " + size + " active alert rules");
                final HashMap<String, Long> activeAlerts = mActiveAlerts;
                mActiveAlerts = Maps.newHashMap();
                for (Map.Entry<String, Long> entry : activeAlerts.entrySet()) {
                    setInterfaceAlert(entry.getKey(), entry.getValue());
                }
            }

            size = mUidRejectOnQuota.size();
            if (size > 0) {
                Slog.d(TAG, "pushing " + size + " active uid rules");
                final SparseBooleanArray uidRejectOnQuota = mUidRejectOnQuota;
                mUidRejectOnQuota = new SparseBooleanArray();
                for (int i = 0; i < uidRejectOnQuota.size(); i++) {
                    setUidNetworkRules(uidRejectOnQuota.keyAt(i), uidRejectOnQuota.valueAt(i));
                }
            }
        }
    }

=======
        for (INetworkManagementEventObserver obs : mObservers) {
            try {
                obs.limitReached(limitName, iface);
            } catch (Exception ex) {
                Slog.w(TAG, "Observer notifier failed", ex);
            }
        }
    }

    /**
     * Let us know the daemon is connected
     */
    protected void onDaemonConnected() {
        if (DBG) Slog.d(TAG, "onConnected");
        mConnectedSignal.countDown();
    }


>>>>>>> upstream/master
    //
    // Netd Callback handling
    //

<<<<<<< HEAD
    private class NetdCallbackReceiver implements INativeDaemonConnectorCallbacks {
        @Override
        public void onDaemonConnected() {
            // event is dispatched from internal NDC thread, so we prepare the
            // daemon back on main thread.
            if (mConnectedSignal != null) {
                mConnectedSignal.countDown();
                mConnectedSignal = null;
            } else {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        prepareNativeDaemon();
                    }
                });
            }
        }

        @Override
=======
    class NetdCallbackReceiver implements INativeDaemonConnectorCallbacks {
        /** {@inheritDoc} */
        public void onDaemonConnected() {
            NetworkManagementService.this.onDaemonConnected();
        }

        /** {@inheritDoc} */
>>>>>>> upstream/master
        public boolean onEvent(int code, String raw, String[] cooked) {
            switch (code) {
            case NetdResponseCode.InterfaceChange:
                    /*
                     * a network interface change occured
                     * Format: "NNN Iface added <name>"
                     *         "NNN Iface removed <name>"
                     *         "NNN Iface changed <name> <up/down>"
                     *         "NNN Iface linkstatus <name> <up/down>"
                     */
                    if (cooked.length < 4 || !cooked[1].equals("Iface")) {
                        throw new IllegalStateException(
                                String.format("Invalid event from daemon (%s)", raw));
                    }
                    if (cooked[2].equals("added")) {
                        notifyInterfaceAdded(cooked[3]);
                        return true;
                    } else if (cooked[2].equals("removed")) {
                        notifyInterfaceRemoved(cooked[3]);
                        return true;
                    } else if (cooked[2].equals("changed") && cooked.length == 5) {
                        notifyInterfaceStatusChanged(cooked[3], cooked[4].equals("up"));
                        return true;
                    } else if (cooked[2].equals("linkstate") && cooked.length == 5) {
                        notifyInterfaceLinkStateChanged(cooked[3], cooked[4].equals("up"));
                        return true;
                    }
                    throw new IllegalStateException(
                            String.format("Invalid event from daemon (%s)", raw));
                    // break;
            case NetdResponseCode.BandwidthControl:
                    /*
                     * Bandwidth control needs some attention
                     * Format: "NNN limit alert <alertName> <ifaceName>"
                     */
                    if (cooked.length < 5 || !cooked[1].equals("limit")) {
                        throw new IllegalStateException(
                                String.format("Invalid event from daemon (%s)", raw));
                    }
                    if (cooked[2].equals("alert")) {
                        notifyLimitReached(cooked[3], cooked[4]);
                        return true;
                    }
                    throw new IllegalStateException(
                            String.format("Invalid event from daemon (%s)", raw));
                    // break;
            default: break;
            }
            return false;
        }
    }


    //
    // INetworkManagementService members
    //

<<<<<<< HEAD
    @Override
    public String[] listInterfaces() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            return NativeDaemonEvent.filterMessageList(
                    mConnector.executeForList("interface", "list"), InterfaceListResult);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public InterfaceConfiguration getInterfaceConfig(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);

        final NativeDaemonEvent event;
        try {
            event = mConnector.execute("interface", "getcfg", iface);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }

        event.checkCode(InterfaceGetCfgResult);

        // Rsp: 213 xx:xx:xx:xx:xx:xx yyy.yyy.yyy.yyy zzz flag1 flag2 flag3
        final StringTokenizer st = new StringTokenizer(event.getMessage());

        InterfaceConfiguration cfg;
        try {
            cfg = new InterfaceConfiguration();
            cfg.setHardwareAddress(st.nextToken(" "));
            InetAddress addr = null;
            int prefixLength = 0;
            try {
                addr = NetworkUtils.numericToInetAddress(st.nextToken());
=======
    public String[] listInterfaces() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");

        try {
            return mConnector.doListCommand("interface list", NetdResponseCode.InterfaceListResult);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Cannot communicate with native daemon to list interfaces");
        }
    }

    public InterfaceConfiguration getInterfaceConfig(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(ACCESS_NETWORK_STATE, TAG);
        String rsp;
        try {
            rsp = mConnector.doCommand("interface getcfg " + iface).get(0);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Cannot communicate with native daemon to get interface config");
        }
        Slog.d(TAG, String.format("rsp <%s>", rsp));

        // Rsp: 213 xx:xx:xx:xx:xx:xx yyy.yyy.yyy.yyy zzz [flag1 flag2 flag3]
        StringTokenizer st = new StringTokenizer(rsp);

        InterfaceConfiguration cfg;
        try {
            try {
                int code = Integer.parseInt(st.nextToken(" "));
                if (code != NetdResponseCode.InterfaceGetCfgResult) {
                    throw new IllegalStateException(
                        String.format("Expected code %d, but got %d",
                                NetdResponseCode.InterfaceGetCfgResult, code));
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalStateException(
                        String.format("Invalid response from daemon (%s)", rsp));
            }

            cfg = new InterfaceConfiguration();
            cfg.hwAddr = st.nextToken(" ");
            InetAddress addr = null;
            int prefixLength = 0;
            try {
                addr = NetworkUtils.numericToInetAddress(st.nextToken(" "));
>>>>>>> upstream/master
            } catch (IllegalArgumentException iae) {
                Slog.e(TAG, "Failed to parse ipaddr", iae);
            }

            try {
<<<<<<< HEAD
                prefixLength = Integer.parseInt(st.nextToken());
=======
                prefixLength = Integer.parseInt(st.nextToken(" "));
>>>>>>> upstream/master
            } catch (NumberFormatException nfe) {
                Slog.e(TAG, "Failed to parse prefixLength", nfe);
            }

<<<<<<< HEAD
            cfg.setLinkAddress(new LinkAddress(addr, prefixLength));
            while (st.hasMoreTokens()) {
                cfg.setFlag(st.nextToken());
            }
        } catch (NoSuchElementException nsee) {
            throw new IllegalStateException("Invalid response from daemon: " + event);
        }
        return cfg;
    }

    @Override
    public void setInterfaceConfig(String iface, InterfaceConfiguration cfg) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        LinkAddress linkAddr = cfg.getLinkAddress();
        if (linkAddr == null || linkAddr.getAddress() == null) {
            throw new IllegalStateException("Null LinkAddress given");
        }

        final Command cmd = new Command("interface", "setcfg", iface,
                linkAddr.getAddress().getHostAddress(),
                linkAddr.getNetworkPrefixLength());
        for (String flag : cfg.getFlags()) {
            cmd.appendArg(flag);
        }

        try {
            mConnector.execute(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void setInterfaceDown(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        final InterfaceConfiguration ifcg = getInterfaceConfig(iface);
        ifcg.setInterfaceDown();
        setInterfaceConfig(iface, ifcg);
    }

    @Override
    public void setInterfaceUp(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        final InterfaceConfiguration ifcg = getInterfaceConfig(iface);
        ifcg.setInterfaceUp();
        setInterfaceConfig(iface, ifcg);
    }

    @Override
    public void setInterfaceIpv6PrivacyExtensions(String iface, boolean enable) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute(
                    "interface", "ipv6privacyextensions", iface, enable ? "enable" : "disable");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    /* TODO: This is right now a IPv4 only function. Works for wifi which loses its
       IPv6 addresses on interface down, but we need to do full clean up here */
    @Override
    public void clearInterfaceAddresses(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("interface", "clearaddrs", iface);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void enableIpv6(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("interface", "ipv6", iface, "enable");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void disableIpv6(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("interface", "ipv6", iface, "disable");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void addRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        modifyRoute(interfaceName, ADD, route, DEFAULT);
    }

    @Override
    public void removeRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        modifyRoute(interfaceName, REMOVE, route, DEFAULT);
    }

    @Override
    public void addSecondaryRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        modifyRoute(interfaceName, ADD, route, SECONDARY);
    }

    @Override
    public void removeSecondaryRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        modifyRoute(interfaceName, REMOVE, route, SECONDARY);
    }

    private void modifyRoute(String interfaceName, String action, RouteInfo route, String type) {
        final Command cmd = new Command("interface", "route", action, interfaceName, type);

        // create triplet: dest-ip-addr prefixlength gateway-ip-addr
        final LinkAddress la = route.getDestination();
        cmd.appendArg(la.getAddress().getHostAddress());
        cmd.appendArg(la.getNetworkPrefixLength());

        if (route.getGateway() == null) {
            if (la.getAddress() instanceof Inet4Address) {
                cmd.appendArg("0.0.0.0");
            } else {
                cmd.appendArg("::0");
            }
        } else {
            cmd.appendArg(route.getGateway().getHostAddress());
        }

        try {
            mConnector.execute(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
=======
            cfg.addr = new LinkAddress(addr, prefixLength);
            cfg.interfaceFlags = st.nextToken("]").trim() +"]";
        } catch (NoSuchElementException nsee) {
            throw new IllegalStateException(
                    String.format("Invalid response from daemon (%s)", rsp));
        }
        Slog.d(TAG, String.format("flags <%s>", cfg.interfaceFlags));
        return cfg;
    }

    public void setInterfaceConfig(
            String iface, InterfaceConfiguration cfg) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        LinkAddress linkAddr = cfg.addr;
        if (linkAddr == null || linkAddr.getAddress() == null) {
            throw new IllegalStateException("Null LinkAddress given");
        }
        String cmd = String.format("interface setcfg %s %s %d %s", iface,
                linkAddr.getAddress().getHostAddress(),
                linkAddr.getNetworkPrefixLength(),
                cfg.interfaceFlags);
        try {
            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native daemon to interface setcfg - " + e);
        }
    }

    public void setInterfaceDown(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        try {
            InterfaceConfiguration ifcg = getInterfaceConfig(iface);
            ifcg.interfaceFlags = ifcg.interfaceFlags.replace("up", "down");
            setInterfaceConfig(iface, ifcg);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native daemon for interface down - " + e);
        }
    }

    public void setInterfaceUp(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        try {
            InterfaceConfiguration ifcg = getInterfaceConfig(iface);
            ifcg.interfaceFlags = ifcg.interfaceFlags.replace("down", "up");
            setInterfaceConfig(iface, ifcg);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native daemon for interface up - " + e);
        }
    }

    public void setInterfaceIpv6PrivacyExtensions(String iface, boolean enable)
            throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        String cmd = String.format("interface ipv6privacyextensions %s %s", iface,
                enable ? "enable" : "disable");
        try {
            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native daemon to set ipv6privacyextensions - " + e);
        }
    }



    /* TODO: This is right now a IPv4 only function. Works for wifi which loses its
       IPv6 addresses on interface down, but we need to do full clean up here */
    public void clearInterfaceAddresses(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        String cmd = String.format("interface clearaddrs %s", iface);
        try {
            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native daemon to interface clearallips - " + e);
        }
    }

    public void enableIpv6(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand(String.format("interface ipv6 %s enable", iface));
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for enabling ipv6");
        }
    }

    public void disableIpv6(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand(String.format("interface ipv6 %s disable", iface));
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for disabling ipv6");
        }
    }

    public void addRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        modifyRoute(interfaceName, ADD, route, DEFAULT);
    }

    public void removeRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        modifyRoute(interfaceName, REMOVE, route, DEFAULT);
    }

    public void addSecondaryRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        modifyRoute(interfaceName, ADD, route, SECONDARY);
    }

    public void removeSecondaryRoute(String interfaceName, RouteInfo route) {
        mContext.enforceCallingOrSelfPermission(CHANGE_NETWORK_STATE, TAG);
        modifyRoute(interfaceName, REMOVE, route, SECONDARY);
    }

    private void modifyRoute(String interfaceName, int action, RouteInfo route, String type) {
        ArrayList<String> rsp;

        StringBuilder cmd;

        switch (action) {
            case ADD:
            {
                cmd = new StringBuilder("interface route add " + interfaceName + " " + type);
                break;
            }
            case REMOVE:
            {
                cmd = new StringBuilder("interface route remove " + interfaceName + " " + type);
                break;
            }
            default:
                throw new IllegalStateException("Unknown action type " + action);
        }

        // create triplet: dest-ip-addr prefixlength gateway-ip-addr
        LinkAddress la = route.getDestination();
        cmd.append(' ');
        cmd.append(la.getAddress().getHostAddress());
        cmd.append(' ');
        cmd.append(la.getNetworkPrefixLength());
        cmd.append(' ');
        if (route.getGateway() == null) {
            if (la.getAddress() instanceof Inet4Address) {
                cmd.append("0.0.0.0");
            } else {
                cmd.append ("::0");
            }
        } else {
            cmd.append(route.getGateway().getHostAddress());
        }
        try {
            rsp = mConnector.doCommand(cmd.toString());
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native dameon to add routes - "
                    + e);
        }

        if (DBG) {
            for (String line : rsp) {
                Log.v(TAG, "add route response is " + line);
            }
>>>>>>> upstream/master
        }
    }

    private ArrayList<String> readRouteList(String filename) {
        FileInputStream fstream = null;
        ArrayList<String> list = new ArrayList<String>();

        try {
            fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s;

            // throw away the title line

            while (((s = br.readLine()) != null) && (s.length() != 0)) {
                list.add(s);
            }
        } catch (IOException ex) {
            // return current list, possibly empty
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException ex) {}
            }
        }

        return list;
    }

<<<<<<< HEAD
    @Override
    public RouteInfo[] getRoutes(String interfaceName) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
    public RouteInfo[] getRoutes(String interfaceName) {
        mContext.enforceCallingOrSelfPermission(ACCESS_NETWORK_STATE, TAG);
>>>>>>> upstream/master
        ArrayList<RouteInfo> routes = new ArrayList<RouteInfo>();

        // v4 routes listed as:
        // iface dest-addr gateway-addr flags refcnt use metric netmask mtu window IRTT
        for (String s : readRouteList("/proc/net/route")) {
            String[] fields = s.split("\t");

            if (fields.length > 7) {
                String iface = fields[0];

                if (interfaceName.equals(iface)) {
                    String dest = fields[1];
                    String gate = fields[2];
                    String flags = fields[3]; // future use?
                    String mask = fields[7];
                    try {
                        // address stored as a hex string, ex: 0014A8C0
                        InetAddress destAddr =
                                NetworkUtils.intToInetAddress((int)Long.parseLong(dest, 16));
                        int prefixLength =
                                NetworkUtils.netmaskIntToPrefixLength(
                                (int)Long.parseLong(mask, 16));
                        LinkAddress linkAddress = new LinkAddress(destAddr, prefixLength);

                        // address stored as a hex string, ex 0014A8C0
                        InetAddress gatewayAddr =
                                NetworkUtils.intToInetAddress((int)Long.parseLong(gate, 16));

                        RouteInfo route = new RouteInfo(linkAddress, gatewayAddr);
                        routes.add(route);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing route " + s + " : " + e);
                        continue;
                    }
                }
            }
        }

        // v6 routes listed as:
        // dest-addr prefixlength ?? ?? gateway-addr ?? ?? ?? ?? iface
        for (String s : readRouteList("/proc/net/ipv6_route")) {
            String[]fields = s.split("\\s+");
            if (fields.length > 9) {
                String iface = fields[9].trim();
                if (interfaceName.equals(iface)) {
                    String dest = fields[0];
                    String prefix = fields[1];
                    String gate = fields[4];

                    try {
                        // prefix length stored as a hex string, ex 40
                        int prefixLength = Integer.parseInt(prefix, 16);

                        // address stored as a 32 char hex string
                        // ex fe800000000000000000000000000000
                        InetAddress destAddr = NetworkUtils.hexToInet6Address(dest);
                        LinkAddress linkAddress = new LinkAddress(destAddr, prefixLength);

                        InetAddress gateAddr = NetworkUtils.hexToInet6Address(gate);

                        RouteInfo route = new RouteInfo(linkAddress, gateAddr);
                        routes.add(route);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing route " + s + " : " + e);
                        continue;
                    }
                }
            }
        }
<<<<<<< HEAD
        return routes.toArray(new RouteInfo[routes.size()]);
    }

    @Override
    public void shutdown() {
        // TODO: remove from aidl if nobody calls externally
        mContext.enforceCallingOrSelfPermission(SHUTDOWN, TAG);
=======
        return (RouteInfo[]) routes.toArray(new RouteInfo[0]);
    }

    public void shutdown() {
        if (mContext.checkCallingOrSelfPermission(
                android.Manifest.permission.SHUTDOWN)
                != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires SHUTDOWN permission");
        }
>>>>>>> upstream/master

        Slog.d(TAG, "Shutting down");
    }

<<<<<<< HEAD
    @Override
    public boolean getIpForwardingEnabled() throws IllegalStateException{
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);

        final NativeDaemonEvent event;
        try {
            event = mConnector.execute("ipfwd", "status");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }

        // 211 Forwarding enabled
        event.checkCode(IpFwdStatusResult);
        return event.getMessage().endsWith("enabled");
    }

    @Override
    public void setIpForwardingEnabled(boolean enable) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("ipfwd", enable ? "enable" : "disable");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void startTethering(String[] dhcpRange) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        // cmd is "tether start first_start first_stop second_start second_stop ..."
        // an odd number of addrs will fail

        final Command cmd = new Command("tether", "start");
        for (String d : dhcpRange) {
            cmd.appendArg(d);
        }

        try {
            mConnector.execute(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void stopTethering() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("tether", "stop");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public boolean isTetheringStarted() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);

        final NativeDaemonEvent event;
        try {
            event = mConnector.execute("tether", "status");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }

        // 210 Tethering services started
        event.checkCode(TetherStatusResult);
        return event.getMessage().endsWith("started");
    }

    @Override
    public void tetherInterface(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("tether", "interface", "add", iface);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void untetherInterface(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("tether", "interface", "remove", iface);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public String[] listTetheredInterfaces() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            return NativeDaemonEvent.filterMessageList(
                    mConnector.executeForList("tether", "interface", "list"),
                    TetherInterfaceListResult);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void setDnsForwarders(String[] dns) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);

        final Command cmd = new Command("tether", "dns", "set");
        for (String s : dns) {
            cmd.appendArg(NetworkUtils.numericToInetAddress(s).getHostAddress());
        }

        try {
            mConnector.execute(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public String[] getDnsForwarders() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            return NativeDaemonEvent.filterMessageList(
                    mConnector.executeForList("tether", "dns", "list"), TetherDnsFwdTgtListResult);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    private void modifyNat(String action, String internalInterface, String externalInterface)
            throws SocketException {
        final Command cmd = new Command("nat", action, internalInterface, externalInterface);

        final NetworkInterface internalNetworkInterface = NetworkInterface.getByName(
                internalInterface);
        if (internalNetworkInterface == null) {
            cmd.appendArg("0");
        } else {
            Collection<InterfaceAddress> interfaceAddresses = internalNetworkInterface
                    .getInterfaceAddresses();
            cmd.appendArg(interfaceAddresses.size());
            for (InterfaceAddress ia : interfaceAddresses) {
                InetAddress addr = NetworkUtils.getNetworkPart(
                        ia.getAddress(), ia.getNetworkPrefixLength());
                cmd.appendArg(addr.getHostAddress() + "/" + ia.getNetworkPrefixLength());
            }
        }

        try {
            mConnector.execute(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void enableNat(String internalInterface, String externalInterface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            modifyNat("enable", internalInterface, externalInterface);
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void disableNat(String internalInterface, String externalInterface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            modifyNat("disable", internalInterface, externalInterface);
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String[] listTtys() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            return NativeDaemonEvent.filterMessageList(
                    mConnector.executeForList("list_ttys"), TtyListResult);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void attachPppd(
            String tty, String localAddr, String remoteAddr, String dns1Addr, String dns2Addr) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("pppd", "attach", tty,
                    NetworkUtils.numericToInetAddress(localAddr).getHostAddress(),
                    NetworkUtils.numericToInetAddress(remoteAddr).getHostAddress(),
                    NetworkUtils.numericToInetAddress(dns1Addr).getHostAddress(),
                    NetworkUtils.numericToInetAddress(dns2Addr).getHostAddress());
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void detachPppd(String tty) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("pppd", "detach", tty);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void startAccessPoint(
            WifiConfiguration wifiConfig, String wlanIface, String softapIface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            wifiFirmwareReload(wlanIface, "AP");
            if (wifiConfig == null) {
                mConnector.execute("softap", "set", wlanIface, softapIface);
            } else {
                mConnector.execute("softap", "set", wlanIface, softapIface, wifiConfig.SSID,
                        getSecurityType(wifiConfig), wifiConfig.preSharedKey);
            }
            mConnector.execute("softap", "startap");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    private static String getSecurityType(WifiConfiguration wifiConfig) {
=======
    public boolean getIpForwardingEnabled() throws IllegalStateException{
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");

        ArrayList<String> rsp;
        try {
            rsp = mConnector.doCommand("ipfwd status");
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate with native daemon to ipfwd status");
        }

        for (String line : rsp) {
            String[] tok = line.split(" ");
            if (tok.length < 3) {
                Slog.e(TAG, "Malformed response from native daemon: " + line);
                return false;
            }

            int code = Integer.parseInt(tok[0]);
            if (code == NetdResponseCode.IpFwdStatusResult) {
                // 211 Forwarding <enabled/disabled>
                return "enabled".equals(tok[2]);
            } else {
                throw new IllegalStateException(String.format("Unexpected response code %d", code));
            }
        }
        throw new IllegalStateException("Got an empty response");
    }

    public void setIpForwardingEnabled(boolean enable) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        mConnector.doCommand(String.format("ipfwd %sable", (enable ? "en" : "dis")));
    }

    public void startTethering(String[] dhcpRange)
             throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        // cmd is "tether start first_start first_stop second_start second_stop ..."
        // an odd number of addrs will fail
        String cmd = "tether start";
        for (String d : dhcpRange) {
            cmd += " " + d;
        }

        try {
            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Unable to communicate to native daemon");
        }
    }

    public void stopTethering() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand("tether stop");
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Unable to communicate to native daemon to stop tether");
        }
    }

    public boolean isTetheringStarted() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");

        ArrayList<String> rsp;
        try {
            rsp = mConnector.doCommand("tether status");
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon to get tether status");
        }

        for (String line : rsp) {
            String[] tok = line.split(" ");
            if (tok.length < 3) {
                throw new IllegalStateException("Malformed response for tether status: " + line);
            }
            int code = Integer.parseInt(tok[0]);
            if (code == NetdResponseCode.TetherStatusResult) {
                // XXX: Tethering services <started/stopped> <TBD>...
                return "started".equals(tok[2]);
            } else {
                throw new IllegalStateException(String.format("Unexpected response code %d", code));
            }
        }
        throw new IllegalStateException("Got an empty response");
    }

    public void tetherInterface(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand("tether interface add " + iface);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for adding tether interface");
        }
    }

    public void untetherInterface(String iface) {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand("tether interface remove " + iface);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for removing tether interface");
        }
    }

    public String[] listTetheredInterfaces() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        try {
            return mConnector.doListCommand(
                    "tether interface list", NetdResponseCode.TetherInterfaceListResult);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for listing tether interfaces");
        }
    }

    public void setDnsForwarders(String[] dns) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            String cmd = "tether dns set";
            for (String s : dns) {
                cmd += " " + NetworkUtils.numericToInetAddress(s).getHostAddress();
            }
            try {
                mConnector.doCommand(cmd);
            } catch (NativeDaemonConnectorException e) {
                throw new IllegalStateException(
                        "Unable to communicate to native daemon for setting tether dns");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Error resolving dns name", e);
        }
    }

    public String[] getDnsForwarders() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        try {
            return mConnector.doListCommand(
                    "tether dns list", NetdResponseCode.TetherDnsFwdTgtListResult);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for listing tether dns");
        }
    }

    private void modifyNat(String cmd, String internalInterface, String externalInterface)
            throws SocketException {
        cmd = String.format("nat %s %s %s", cmd, internalInterface, externalInterface);

        NetworkInterface internalNetworkInterface =
                NetworkInterface.getByName(internalInterface);
        if (internalNetworkInterface == null) {
            cmd += " 0";
        } else {
            Collection<InterfaceAddress>interfaceAddresses =
                    internalNetworkInterface.getInterfaceAddresses();
            cmd += " " + interfaceAddresses.size();
            for (InterfaceAddress ia : interfaceAddresses) {
                InetAddress addr = NetworkUtils.getNetworkPart(ia.getAddress(),
                        ia.getNetworkPrefixLength());
                cmd = cmd + " " + addr.getHostAddress() + "/" + ia.getNetworkPrefixLength();
            }
        }

        mConnector.doCommand(cmd);
    }

    public void enableNat(String internalInterface, String externalInterface)
            throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        if (DBG) Log.d(TAG, "enableNat(" + internalInterface + ", " + externalInterface + ")");
        try {
            modifyNat("enable", internalInterface, externalInterface);
        } catch (Exception e) {
            Log.e(TAG, "enableNat got Exception " + e.toString());
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for enabling NAT interface");
        }
    }

    public void disableNat(String internalInterface, String externalInterface)
            throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        if (DBG) Log.d(TAG, "disableNat(" + internalInterface + ", " + externalInterface + ")");
        try {
            modifyNat("disable", internalInterface, externalInterface);
        } catch (Exception e) {
            Log.e(TAG, "disableNat got Exception " + e.toString());
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for disabling NAT interface");
        }
    }

    public String[] listTtys() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        try {
            return mConnector.doListCommand("list_ttys", NetdResponseCode.TtyListResult);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Unable to communicate to native daemon for listing TTYs");
        }
    }

    public void attachPppd(String tty, String localAddr, String remoteAddr, String dns1Addr,
            String dns2Addr) throws IllegalStateException {
        try {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
            mConnector.doCommand(String.format("pppd attach %s %s %s %s %s", tty,
                    NetworkUtils.numericToInetAddress(localAddr).getHostAddress(),
                    NetworkUtils.numericToInetAddress(remoteAddr).getHostAddress(),
                    NetworkUtils.numericToInetAddress(dns1Addr).getHostAddress(),
                    NetworkUtils.numericToInetAddress(dns2Addr).getHostAddress()));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Error resolving addr", e);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon to attach pppd", e);
        }
    }

    public void detachPppd(String tty) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand(String.format("pppd detach %s", tty));
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon to detach pppd", e);
        }
    }

    public void startAccessPoint(WifiConfiguration wifiConfig, String wlanIface, String softapIface)
             throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_WIFI_STATE, "NetworkManagementService");
        try {
            wifiFirmwareReload(wlanIface, "AP");
            mConnector.doCommand(String.format("softap start " + wlanIface));
            if (wifiConfig == null) {
                mConnector.doCommand(String.format("softap set " + wlanIface + " " + softapIface));
            } else {
                /**
                 * softap set arg1 arg2 arg3 [arg4 arg5 arg6 arg7 arg8]
                 * argv1 - wlan interface
                 * argv2 - softap interface
                 * argv3 - SSID
                 * argv4 - Security
                 * argv5 - Key
                 * argv6 - Channel
                 * argv7 - Preamble
                 * argv8 - Max SCB
                 */
                 String str = String.format("softap set " + wlanIface + " " + softapIface +
                                       " %s %s %s", convertQuotedString(wifiConfig.SSID),
                                       getSecurityType(wifiConfig),
                                       convertQuotedString(wifiConfig.preSharedKey));
                mConnector.doCommand(str);
            }
            mConnector.doCommand(String.format("softap startap"));
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon to start softap", e);
        }
    }

    private String convertQuotedString(String s) {
        if (s == null) {
            return s;
        }
        /* Replace \ with \\, then " with \" and add quotes at end */
        return '"' + s.replaceAll("\\\\","\\\\\\\\").replaceAll("\"","\\\\\"") + '"';
    }

    private String getSecurityType(WifiConfiguration wifiConfig) {
>>>>>>> upstream/master
        switch (wifiConfig.getAuthType()) {
            case KeyMgmt.WPA_PSK:
                return "wpa-psk";
            case KeyMgmt.WPA2_PSK:
                return "wpa2-psk";
            default:
                return "open";
        }
    }

    /* @param mode can be "AP", "STA" or "P2P" */
<<<<<<< HEAD
    @Override
    public void wifiFirmwareReload(String wlanIface, String mode) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("softap", "fwreload", wlanIface, mode);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void stopAccessPoint(String wlanIface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("softap", "stopap");
            mConnector.execute("softap", "stop", wlanIface);
            wifiFirmwareReload(wlanIface, "STA");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void setAccessPoint(WifiConfiguration wifiConfig, String wlanIface, String softapIface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            if (wifiConfig == null) {
                mConnector.execute("softap", "set", wlanIface, softapIface);
            } else {
                mConnector.execute("softap", "set", wlanIface, softapIface, wifiConfig.SSID,
                        getSecurityType(wifiConfig), wifiConfig.preSharedKey);
            }
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public NetworkStats getNetworkStatsSummaryDev() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        return mStatsFactory.readNetworkStatsSummaryDev();
    }

    @Override
    public NetworkStats getNetworkStatsSummaryXt() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        return mStatsFactory.readNetworkStatsSummaryXt();
=======
    public void wifiFirmwareReload(String wlanIface, String mode) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_WIFI_STATE, "NetworkManagementService");

        try {
            mConnector.doCommand(String.format("softap fwreload " + wlanIface + " " + mode));
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon ", e);
        }
    }

    public void stopAccessPoint(String wlanIface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_WIFI_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand("softap stopap");
            mConnector.doCommand("softap stop " + wlanIface);
            wifiFirmwareReload(wlanIface, "STA");
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon to stop soft AP",
                    e);
        }
    }

    public void setAccessPoint(WifiConfiguration wifiConfig, String wlanIface, String softapIface)
            throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        mContext.enforceCallingOrSelfPermission(
            android.Manifest.permission.CHANGE_WIFI_STATE, "NetworkManagementService");
        try {
            if (wifiConfig == null) {
                mConnector.doCommand(String.format("softap set " + wlanIface + " " + softapIface));
            } else {
                String str = String.format("softap set " + wlanIface + " " + softapIface
                        + " %s %s %s", convertQuotedString(wifiConfig.SSID),
                        getSecurityType(wifiConfig),
                        convertQuotedString(wifiConfig.preSharedKey));
                mConnector.doCommand(str);
            }
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon to set soft AP",
                    e);
        }
    }

    private long getInterfaceCounter(String iface, boolean rx) {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        try {
            String rsp;
            try {
                rsp = mConnector.doCommand(
                        String.format("interface read%scounter %s", (rx ? "rx" : "tx"), iface)).get(0);
            } catch (NativeDaemonConnectorException e1) {
                Slog.e(TAG, "Error communicating with native daemon", e1);
                return -1;
            }

            String[] tok = rsp.split(" ");
            if (tok.length < 2) {
                Slog.e(TAG, String.format("Malformed response for reading %s interface",
                        (rx ? "rx" : "tx")));
                return -1;
            }

            int code;
            try {
                code = Integer.parseInt(tok[0]);
            } catch (NumberFormatException nfe) {
                Slog.e(TAG, String.format("Error parsing code %s", tok[0]));
                return -1;
            }
            if ((rx && code != NetdResponseCode.InterfaceRxCounterResult) || (
                    !rx && code != NetdResponseCode.InterfaceTxCounterResult)) {
                Slog.e(TAG, String.format("Unexpected response code %d", code));
                return -1;
            }
            return Long.parseLong(tok[1]);
        } catch (Exception e) {
            Slog.e(TAG, String.format(
                    "Failed to read interface %s counters", (rx ? "rx" : "tx")), e);
        }
        return -1;
    }

    @Override
    public NetworkStats getNetworkStatsSummary() {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        return mStatsFactory.readNetworkStatsSummary();
>>>>>>> upstream/master
    }

    @Override
    public NetworkStats getNetworkStatsDetail() {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
>>>>>>> upstream/master
        return mStatsFactory.readNetworkStatsDetail(UID_ALL);
    }

    @Override
    public void setInterfaceQuota(String iface, long quotaBytes) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master

        // silently discard when control disabled
        // TODO: eventually migrate to be always enabled
        if (!mBandwidthControlEnabled) return;

        synchronized (mQuotaLock) {
<<<<<<< HEAD
            if (mActiveQuotas.containsKey(iface)) {
                throw new IllegalStateException("iface " + iface + " already has quota");
            }

            try {
                // TODO: support quota shared across interfaces
                mConnector.execute("bandwidth", "setiquota", iface, quotaBytes);
                mActiveQuotas.put(iface, quotaBytes);
            } catch (NativeDaemonConnectorException e) {
                throw e.rethrowAsParcelableException();
=======
            if (mActiveQuotaIfaces.contains(iface)) {
                throw new IllegalStateException("iface " + iface + " already has quota");
            }

            final StringBuilder command = new StringBuilder();
            command.append("bandwidth setiquota ").append(iface).append(" ").append(quotaBytes);

            try {
                // TODO: support quota shared across interfaces
                mConnector.doCommand(command.toString());
                mActiveQuotaIfaces.add(iface);
            } catch (NativeDaemonConnectorException e) {
                throw new IllegalStateException("Error communicating to native daemon", e);
>>>>>>> upstream/master
            }
        }
    }

    @Override
    public void removeInterfaceQuota(String iface) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master

        // silently discard when control disabled
        // TODO: eventually migrate to be always enabled
        if (!mBandwidthControlEnabled) return;

        synchronized (mQuotaLock) {
<<<<<<< HEAD
            if (!mActiveQuotas.containsKey(iface)) {
=======
            if (!mActiveQuotaIfaces.contains(iface)) {
>>>>>>> upstream/master
                // TODO: eventually consider throwing
                return;
            }

<<<<<<< HEAD
            mActiveQuotas.remove(iface);
            mActiveAlerts.remove(iface);

            try {
                // TODO: support quota shared across interfaces
                mConnector.execute("bandwidth", "removeiquota", iface);
            } catch (NativeDaemonConnectorException e) {
                throw e.rethrowAsParcelableException();
=======
            final StringBuilder command = new StringBuilder();
            command.append("bandwidth removeiquota ").append(iface);

            mActiveQuotaIfaces.remove(iface);
            mActiveAlertIfaces.remove(iface);

            try {
                // TODO: support quota shared across interfaces
                mConnector.doCommand(command.toString());
            } catch (NativeDaemonConnectorException e) {
                // TODO: include current iptables state
                throw new IllegalStateException("Error communicating to native daemon", e);
>>>>>>> upstream/master
            }
        }
    }

    @Override
    public void setInterfaceAlert(String iface, long alertBytes) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master

        // silently discard when control disabled
        // TODO: eventually migrate to be always enabled
        if (!mBandwidthControlEnabled) return;

        // quick sanity check
<<<<<<< HEAD
        if (!mActiveQuotas.containsKey(iface)) {
=======
        if (!mActiveQuotaIfaces.contains(iface)) {
>>>>>>> upstream/master
            throw new IllegalStateException("setting alert requires existing quota on iface");
        }

        synchronized (mQuotaLock) {
<<<<<<< HEAD
            if (mActiveAlerts.containsKey(iface)) {
                throw new IllegalStateException("iface " + iface + " already has alert");
            }

            try {
                // TODO: support alert shared across interfaces
                mConnector.execute("bandwidth", "setinterfacealert", iface, alertBytes);
                mActiveAlerts.put(iface, alertBytes);
            } catch (NativeDaemonConnectorException e) {
                throw e.rethrowAsParcelableException();
=======
            if (mActiveAlertIfaces.contains(iface)) {
                throw new IllegalStateException("iface " + iface + " already has alert");
            }

            final StringBuilder command = new StringBuilder();
            command.append("bandwidth setinterfacealert ").append(iface).append(" ").append(
                    alertBytes);

            try {
                // TODO: support alert shared across interfaces
                mConnector.doCommand(command.toString());
                mActiveAlertIfaces.add(iface);
            } catch (NativeDaemonConnectorException e) {
                throw new IllegalStateException("Error communicating to native daemon", e);
>>>>>>> upstream/master
            }
        }
    }

    @Override
    public void removeInterfaceAlert(String iface) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master

        // silently discard when control disabled
        // TODO: eventually migrate to be always enabled
        if (!mBandwidthControlEnabled) return;

        synchronized (mQuotaLock) {
<<<<<<< HEAD
            if (!mActiveAlerts.containsKey(iface)) {
=======
            if (!mActiveAlertIfaces.contains(iface)) {
>>>>>>> upstream/master
                // TODO: eventually consider throwing
                return;
            }

<<<<<<< HEAD
            try {
                // TODO: support alert shared across interfaces
                mConnector.execute("bandwidth", "removeinterfacealert", iface);
                mActiveAlerts.remove(iface);
            } catch (NativeDaemonConnectorException e) {
                throw e.rethrowAsParcelableException();
=======
            final StringBuilder command = new StringBuilder();
            command.append("bandwidth removeinterfacealert ").append(iface);

            try {
                // TODO: support alert shared across interfaces
                mConnector.doCommand(command.toString());
                mActiveAlertIfaces.remove(iface);
            } catch (NativeDaemonConnectorException e) {
                throw new IllegalStateException("Error communicating to native daemon", e);
>>>>>>> upstream/master
            }
        }
    }

    @Override
    public void setGlobalAlert(long alertBytes) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master

        // silently discard when control disabled
        // TODO: eventually migrate to be always enabled
        if (!mBandwidthControlEnabled) return;

<<<<<<< HEAD
        try {
            mConnector.execute("bandwidth", "setglobalalert", alertBytes);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
=======
        final StringBuilder command = new StringBuilder();
        command.append("bandwidth setglobalalert ").append(alertBytes);

        try {
            mConnector.doCommand(command.toString());
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon", e);
>>>>>>> upstream/master
        }
    }

    @Override
    public void setUidNetworkRules(int uid, boolean rejectOnQuotaInterfaces) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master

        // silently discard when control disabled
        // TODO: eventually migrate to be always enabled
        if (!mBandwidthControlEnabled) return;

<<<<<<< HEAD
        synchronized (mQuotaLock) {
=======
        synchronized (mUidRejectOnQuota) {
>>>>>>> upstream/master
            final boolean oldRejectOnQuota = mUidRejectOnQuota.get(uid, false);
            if (oldRejectOnQuota == rejectOnQuotaInterfaces) {
                // TODO: eventually consider throwing
                return;
            }

<<<<<<< HEAD
            try {
                mConnector.execute("bandwidth",
                        rejectOnQuotaInterfaces ? "addnaughtyapps" : "removenaughtyapps", uid);
=======
            final StringBuilder command = new StringBuilder();
            command.append("bandwidth");
            if (rejectOnQuotaInterfaces) {
                command.append(" addnaughtyapps");
            } else {
                command.append(" removenaughtyapps");
            }
            command.append(" ").append(uid);

            try {
                mConnector.doCommand(command.toString());
>>>>>>> upstream/master
                if (rejectOnQuotaInterfaces) {
                    mUidRejectOnQuota.put(uid, true);
                } else {
                    mUidRejectOnQuota.delete(uid);
                }
            } catch (NativeDaemonConnectorException e) {
<<<<<<< HEAD
                throw e.rethrowAsParcelableException();
=======
                throw new IllegalStateException("Error communicating to native daemon", e);
>>>>>>> upstream/master
            }
        }
    }

    @Override
    public boolean isBandwidthControlEnabled() {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(MANAGE_NETWORK_POLICY, TAG);
>>>>>>> upstream/master
        return mBandwidthControlEnabled;
    }

    @Override
    public NetworkStats getNetworkStatsUidDetail(int uid) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        if (Binder.getCallingUid() != uid) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        }
>>>>>>> upstream/master
        return mStatsFactory.readNetworkStatsDetail(uid);
    }

    @Override
    public NetworkStats getNetworkStatsTethering(String[] ifacePairs) {
<<<<<<< HEAD
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
=======
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
>>>>>>> upstream/master

        if (ifacePairs.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "unexpected ifacePairs; length=" + ifacePairs.length);
        }

        final NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 1);
        for (int i = 0; i < ifacePairs.length; i += 2) {
            final String ifaceIn = ifacePairs[i];
            final String ifaceOut = ifacePairs[i + 1];
            if (ifaceIn != null && ifaceOut != null) {
                stats.combineValues(getNetworkStatsTethering(ifaceIn, ifaceOut));
            }
        }
        return stats;
    }

    private NetworkStats.Entry getNetworkStatsTethering(String ifaceIn, String ifaceOut) {
<<<<<<< HEAD
        final NativeDaemonEvent event;
        try {
            event = mConnector.execute("bandwidth", "gettetherstats", ifaceIn, ifaceOut);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }

        event.checkCode(TetheringStatsResult);

        // 221 ifaceIn ifaceOut rx_bytes rx_packets tx_bytes tx_packets
        final StringTokenizer tok = new StringTokenizer(event.getMessage());
        tok.nextToken();
        tok.nextToken();
=======
        final StringBuilder command = new StringBuilder();
        command.append("bandwidth gettetherstats ").append(ifaceIn).append(" ").append(ifaceOut);

        final String rsp;
        try {
            rsp = mConnector.doCommand(command.toString()).get(0);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException("Error communicating to native daemon", e);
        }

        final String[] tok = rsp.split(" ");
        /* Expecting: "code ifaceIn ifaceOut rx_bytes rx_packets tx_bytes tx_packets" */
        if (tok.length != 7) {
            throw new IllegalStateException("Native daemon returned unexpected result: " + rsp);
        }

        final int code;
        try {
            code = Integer.parseInt(tok[0]);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Failed to parse native daemon return code for " + ifaceIn + " " + ifaceOut);
        }
        if (code != NetdResponseCode.TetheringStatsResult) {
            throw new IllegalStateException(
                    "Unexpected return code from native daemon for " + ifaceIn + " " + ifaceOut);
        }
>>>>>>> upstream/master

        try {
            final NetworkStats.Entry entry = new NetworkStats.Entry();
            entry.iface = ifaceIn;
            entry.uid = UID_TETHERING;
            entry.set = SET_DEFAULT;
            entry.tag = TAG_NONE;
<<<<<<< HEAD
            entry.rxBytes = Long.parseLong(tok.nextToken());
            entry.rxPackets = Long.parseLong(tok.nextToken());
            entry.txBytes = Long.parseLong(tok.nextToken());
            entry.txPackets = Long.parseLong(tok.nextToken());
=======
            entry.rxBytes = Long.parseLong(tok[3]);
            entry.rxPackets = Long.parseLong(tok[4]);
            entry.txBytes = Long.parseLong(tok[5]);
            entry.txPackets = Long.parseLong(tok[6]);
>>>>>>> upstream/master
            return entry;
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "problem parsing tethering stats for " + ifaceIn + " " + ifaceOut + ": " + e);
        }
    }

<<<<<<< HEAD
    @Override
    public void setInterfaceThrottle(String iface, int rxKbps, int txKbps) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("interface", "setthrottle", iface, rxKbps, txKbps);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
=======
    public void setInterfaceThrottle(String iface, int rxKbps, int txKbps) {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            mConnector.doCommand(String.format(
                    "interface setthrottle %s %d %d", iface, rxKbps, txKbps));
        } catch (NativeDaemonConnectorException e) {
            Slog.e(TAG, "Error communicating with native daemon to set throttle", e);
>>>>>>> upstream/master
        }
    }

    private int getInterfaceThrottle(String iface, boolean rx) {
<<<<<<< HEAD
        final NativeDaemonEvent event;
        try {
            event = mConnector.execute("interface", "getthrottle", iface, rx ? "rx" : "tx");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }

        if (rx) {
            event.checkCode(InterfaceRxThrottleResult);
        } else {
            event.checkCode(InterfaceTxThrottleResult);
        }

        try {
            return Integer.parseInt(event.getMessage());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("unexpected response:" + event);
        }
    }

    @Override
    public int getInterfaceRxThrottle(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        return getInterfaceThrottle(iface, true);
    }

    @Override
    public int getInterfaceTxThrottle(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        return getInterfaceThrottle(iface, false);
    }

    @Override
    public void setDefaultInterfaceForDns(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("resolver", "setdefaultif", iface);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void setDnsServersForInterface(String iface, String[] servers) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);

        final Command cmd = new Command("resolver", "setifdns", iface);
        for (String s : servers) {
            InetAddress a = NetworkUtils.numericToInetAddress(s);
            if (a.isAnyLocalAddress() == false) {
                cmd.appendArg(a.getHostAddress());
            }
        }

        try {
            mConnector.execute(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void flushDefaultDnsCache() {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("resolver", "flushdefaultif");
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
        }
    }

    @Override
    public void flushInterfaceDnsCache(String iface) {
        mContext.enforceCallingOrSelfPermission(CONNECTIVITY_INTERNAL, TAG);
        try {
            mConnector.execute("resolver", "flushif", iface);
        } catch (NativeDaemonConnectorException e) {
            throw e.rethrowAsParcelableException();
=======
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.ACCESS_NETWORK_STATE, "NetworkManagementService");
        try {
            String rsp;
            try {
                rsp = mConnector.doCommand(
                        String.format("interface getthrottle %s %s", iface,
                                (rx ? "rx" : "tx"))).get(0);
            } catch (NativeDaemonConnectorException e) {
                Slog.e(TAG, "Error communicating with native daemon to getthrottle", e);
                return -1;
            }

            String[] tok = rsp.split(" ");
            if (tok.length < 2) {
                Slog.e(TAG, "Malformed response to getthrottle command");
                return -1;
            }

            int code;
            try {
                code = Integer.parseInt(tok[0]);
            } catch (NumberFormatException nfe) {
                Slog.e(TAG, String.format("Error parsing code %s", tok[0]));
                return -1;
            }
            if ((rx && code != NetdResponseCode.InterfaceRxThrottleResult) || (
                    !rx && code != NetdResponseCode.InterfaceTxThrottleResult)) {
                Slog.e(TAG, String.format("Unexpected response code %d", code));
                return -1;
            }
            return Integer.parseInt(tok[1]);
        } catch (Exception e) {
            Slog.e(TAG, String.format(
                    "Failed to read interface %s throttle value", (rx ? "rx" : "tx")), e);
        }
        return -1;
    }

    public int getInterfaceRxThrottle(String iface) {
        return getInterfaceThrottle(iface, true);
    }

    public int getInterfaceTxThrottle(String iface) {
        return getInterfaceThrottle(iface, false);
    }

    public void setDefaultInterfaceForDns(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            String cmd = "resolver setdefaultif " + iface;

            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Error communicating with native daemon to set default interface", e);
        }
    }

    public void setDnsServersForInterface(String iface, String[] servers)
            throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.CHANGE_NETWORK_STATE,
                "NetworkManagementService");
        try {
            String cmd = "resolver setifdns " + iface;
            for (String s : servers) {
                InetAddress a = NetworkUtils.numericToInetAddress(s);
                if (a.isAnyLocalAddress() == false) {
                    cmd += " " + a.getHostAddress();
                }
            }
            mConnector.doCommand(cmd);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Error setting dnsn for interface", e);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Error communicating with native daemon to set dns for interface", e);
        }
    }

    public void flushDefaultDnsCache() throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            String cmd = "resolver flushdefaultif";

            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Error communicating with native deamon to flush default interface", e);
        }
    }

    public void flushInterfaceDnsCache(String iface) throws IllegalStateException {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE, "NetworkManagementService");
        try {
            String cmd = "resolver flushif " + iface;

            mConnector.doCommand(cmd);
        } catch (NativeDaemonConnectorException e) {
            throw new IllegalStateException(
                    "Error communicating with native daemon to flush interface " + iface, e);
>>>>>>> upstream/master
        }
    }

    /** {@inheritDoc} */
    public void monitor() {
        if (mConnector != null) {
            mConnector.monitor();
        }
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        mContext.enforceCallingOrSelfPermission(DUMP, TAG);

<<<<<<< HEAD
        pw.println("NetworkManagementService NativeDaemonConnector Log:");
        mConnector.dump(fd, pw, args);
        pw.println();

        pw.print("Bandwidth control enabled: "); pw.println(mBandwidthControlEnabled);

        synchronized (mQuotaLock) {
            pw.print("Active quota ifaces: "); pw.println(mActiveQuotas.toString());
            pw.print("Active alert ifaces: "); pw.println(mActiveAlerts.toString());
=======
        pw.print("Bandwidth control enabled: "); pw.println(mBandwidthControlEnabled);

        synchronized (mQuotaLock) {
            pw.print("Active quota ifaces: "); pw.println(mActiveQuotaIfaces.toString());
            pw.print("Active alert ifaces: "); pw.println(mActiveAlertIfaces.toString());
>>>>>>> upstream/master
        }

        synchronized (mUidRejectOnQuota) {
            pw.print("UID reject on quota ifaces: [");
            final int size = mUidRejectOnQuota.size();
            for (int i = 0; i < size; i++) {
                pw.print(mUidRejectOnQuota.keyAt(i));
                if (i < size - 1) pw.print(",");
            }
            pw.println("]");
        }
    }
}
