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

package android.net.wifi;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pDevice;
<<<<<<< HEAD
import android.text.TextUtils;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
=======
>>>>>>> upstream/master
import android.util.Log;

import java.io.InputStream;
import java.lang.Process;
import java.util.ArrayList;
import java.util.List;

/**
<<<<<<< HEAD
 * Native calls for bring up/shut down of the supplicant daemon and for
 * sending requests to the supplicant daemon
 *
 * waitForEvent() is called on the monitor thread for events. All other methods
 * must be serialized from the framework.
=======
 * Native calls for sending requests to the supplicant daemon, and for
 * receiving asynchronous events. All methods of the form "xxxxCommand()"
 * must be single-threaded, to avoid requests and responses initiated
 * from multiple threads from being intermingled.
 * <p/>
 * Note that methods whose names are not of the form "xxxCommand()" do
 * not talk to the supplicant daemon.
 * Also, note that all WifiNative calls should happen in the
 * WifiStateTracker class except for waitForEvent() call which is
 * on a separate monitor channel for WifiMonitor
 *
 * TODO: clean up the API and move the functionality from JNI to here. We should
 * be able to get everything done with doBooleanCommand, doIntCommand and
 * doStringCommand native commands
>>>>>>> upstream/master
 *
 * {@hide}
 */
public class WifiNative {

<<<<<<< HEAD
    private static final boolean DBG = false;
    private final String mTAG;
    private static final int DEFAULT_GROUP_OWNER_INTENT = 7;

=======
>>>>>>> upstream/master
    static final int BLUETOOTH_COEXISTENCE_MODE_ENABLED = 0;
    static final int BLUETOOTH_COEXISTENCE_MODE_DISABLED = 1;
    static final int BLUETOOTH_COEXISTENCE_MODE_SENSE = 2;

<<<<<<< HEAD
    String mInterface = "";
=======
    public native static String getErrorString(int errorCode);
>>>>>>> upstream/master

    public native static boolean loadDriver();

    public native static boolean isDriverLoaded();

    public native static boolean unloadDriver();

<<<<<<< HEAD
    public native static boolean startSupplicant(boolean p2pSupported);
=======
    public native static boolean startSupplicant();

    public native static boolean startP2pSupplicant();

    /* Does a graceful shutdown of supplicant. Is a common stop function for both p2p and sta.
     *
     * Note that underneath we use a harsh-sounding "terminate" supplicant command
     * for a graceful stop and a mild-sounding "stop" interface
     * to kill the process
     */
    public native static boolean stopSupplicant();
>>>>>>> upstream/master

    /* Sends a kill signal to supplicant. To be used when we have lost connection
       or when the supplicant is hung */
    public native static boolean killSupplicant();

<<<<<<< HEAD
    private native boolean connectToSupplicant(String iface);

    private native void closeSupplicantConnection(String iface);

    /**
     * Wait for the supplicant to send an event, returning the event string.
     * @return the event string sent by the supplicant.
     */
    private native String waitForEvent(String iface);

    private native boolean doBooleanCommand(String iface, String command);

    private native int doIntCommand(String iface, String command);

    private native String doStringCommand(String iface, String command);

    public WifiNative(String iface) {
        mInterface = iface;
        mTAG = "WifiNative-" + iface;
    }

    public boolean connectToSupplicant() {
        return connectToSupplicant(mInterface);
    }

    public void closeSupplicantConnection() {
        closeSupplicantConnection(mInterface);
    }

    public String waitForEvent() {
        return waitForEvent(mInterface);
    }

    private boolean doBooleanCommand(String command) {
        if (DBG) Log.d(mTAG, "doBoolean: " + command);
        return doBooleanCommand(mInterface, command);
    }

    private int doIntCommand(String command) {
        if (DBG) Log.d(mTAG, "doInt: " + command);
        return doIntCommand(mInterface, command);
    }

    private String doStringCommand(String command) {
        if (DBG) Log.d(mTAG, "doString: " + command);
        return doStringCommand(mInterface, command);
    }

    public boolean ping() {
        String pong = doStringCommand("PING");
        return (pong != null && pong.equals("PONG"));
    }

    public boolean scan() {
       return doBooleanCommand("SCAN");
    }

    public boolean setScanMode(boolean setActive) {
        if (setActive) {
            return doBooleanCommand("DRIVER SCAN-ACTIVE");
        } else {
            return doBooleanCommand("DRIVER SCAN-PASSIVE");
        }
    }

    /* Does a graceful shutdown of supplicant. Is a common stop function for both p2p and sta.
     *
     * Note that underneath we use a harsh-sounding "terminate" supplicant command
     * for a graceful stop and a mild-sounding "stop" interface
     * to kill the process
     */
    public boolean stopSupplicant() {
        return doBooleanCommand("TERMINATE");
    }

    public String listNetworks() {
        return doStringCommand("LIST_NETWORKS");
    }

    public int addNetwork() {
        return doIntCommand("ADD_NETWORK");
    }

    public boolean setNetworkVariable(int netId, String name, String value) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value)) return false;
        return doBooleanCommand("SET_NETWORK " + netId + " " + name + " " + value);
    }

    public String getNetworkVariable(int netId, String name) {
        if (TextUtils.isEmpty(name)) return null;
        return doStringCommand("GET_NETWORK " + netId + " " + name);
    }

    public boolean removeNetwork(int netId) {
        return doBooleanCommand("REMOVE_NETWORK " + netId);
    }

    public boolean enableNetwork(int netId, boolean disableOthers) {
        if (disableOthers) {
            return doBooleanCommand("SELECT_NETWORK " + netId);
        } else {
            return doBooleanCommand("ENABLE_NETWORK " + netId);
        }
    }

    public boolean disableNetwork(int netId) {
        return doBooleanCommand("DISABLE_NETWORK " + netId);
    }

    public boolean reconnect() {
        return doBooleanCommand("RECONNECT");
    }

    public boolean reassociate() {
        return doBooleanCommand("REASSOCIATE");
    }

    public boolean disconnect() {
        return doBooleanCommand("DISCONNECT");
    }

    public String status() {
        return doStringCommand("STATUS");
    }

    public String getMacAddress() {
        //Macaddr = XX.XX.XX.XX.XX.XX
        String ret = doStringCommand("DRIVER MACADDR");
        if (!TextUtils.isEmpty(ret)) {
            String[] tokens = ret.split(" = ");
            if (tokens.length == 2) return tokens[1];
        }
        return null;
    }

    public String scanResults() {
        return doStringCommand("SCAN_RESULTS");
    }

    public boolean startDriver() {
        return doBooleanCommand("DRIVER START");
    }

    public boolean stopDriver() {
        return doBooleanCommand("DRIVER STOP");
    }
=======
    public native static boolean connectToSupplicant();

    public native static void closeSupplicantConnection();

    public native static boolean pingCommand();

    public native static boolean scanCommand(boolean forceActive);

    public native static boolean setScanModeCommand(boolean setActive);

    public native static String listNetworksCommand();

    public native static int addNetworkCommand();

    public native static boolean setNetworkVariableCommand(int netId, String name, String value);

    public native static String getNetworkVariableCommand(int netId, String name);

    public native static boolean removeNetworkCommand(int netId);

    public native static boolean enableNetworkCommand(int netId, boolean disableOthers);

    public native static boolean disableNetworkCommand(int netId);

    public native static boolean reconnectCommand();

    public native static boolean reassociateCommand();

    public native static boolean disconnectCommand();

    public native static String statusCommand();

    public native static String getMacAddressCommand();

    public native static String scanResultsCommand();

    public native static boolean startDriverCommand();

    public native static boolean stopDriverCommand();
>>>>>>> upstream/master


    /**
     * Start filtering out Multicast V4 packets
     * @return {@code true} if the operation succeeded, {@code false} otherwise
<<<<<<< HEAD
     *
     * Multicast filtering rules work as follows:
     *
     * The driver can filter multicast (v4 and/or v6) and broadcast packets when in
     * a power optimized mode (typically when screen goes off).
     *
     * In order to prevent the driver from filtering the multicast/broadcast packets, we have to
     * add a DRIVER RXFILTER-ADD rule followed by DRIVER RXFILTER-START to make the rule effective
     *
     * DRIVER RXFILTER-ADD Num
     *   where Num = 0 - Unicast, 1 - Broadcast, 2 - Mutil4 or 3 - Multi6
     *
     * and DRIVER RXFILTER-START
     * In order to stop the usage of these rules, we do
     *
     * DRIVER RXFILTER-STOP
     * DRIVER RXFILTER-REMOVE Num
     *   where Num is as described for RXFILTER-ADD
     *
     * The  SETSUSPENDOPT driver command overrides the filtering rules
     */
    public boolean startFilteringMulticastV4Packets() {
        return doBooleanCommand("DRIVER RXFILTER-STOP")
            && doBooleanCommand("DRIVER RXFILTER-REMOVE 2")
            && doBooleanCommand("DRIVER RXFILTER-START");
    }
=======
     */
    public native static boolean startFilteringMulticastV4Packets();
>>>>>>> upstream/master

    /**
     * Stop filtering out Multicast V4 packets.
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
<<<<<<< HEAD
    public boolean stopFilteringMulticastV4Packets() {
        return doBooleanCommand("DRIVER RXFILTER-STOP")
            && doBooleanCommand("DRIVER RXFILTER-ADD 2")
            && doBooleanCommand("DRIVER RXFILTER-START");
    }
=======
    public native static boolean stopFilteringMulticastV4Packets();
>>>>>>> upstream/master

    /**
     * Start filtering out Multicast V6 packets
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
<<<<<<< HEAD
    public boolean startFilteringMulticastV6Packets() {
        return doBooleanCommand("DRIVER RXFILTER-STOP")
            && doBooleanCommand("DRIVER RXFILTER-REMOVE 3")
            && doBooleanCommand("DRIVER RXFILTER-START");
    }
=======
    public native static boolean startFilteringMulticastV6Packets();
>>>>>>> upstream/master

    /**
     * Stop filtering out Multicast V6 packets.
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
<<<<<<< HEAD
    public boolean stopFilteringMulticastV6Packets() {
        return doBooleanCommand("DRIVER RXFILTER-STOP")
            && doBooleanCommand("DRIVER RXFILTER-ADD 3")
            && doBooleanCommand("DRIVER RXFILTER-START");
    }

    public int getBand() {
       String ret = doStringCommand("DRIVER GETBAND");
        if (!TextUtils.isEmpty(ret)) {
            //reply is "BAND X" where X is the band
            String[] tokens = ret.split(" ");
            try {
                if (tokens.length == 2) return Integer.parseInt(tokens[1]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public boolean setBand(int band) {
        return doBooleanCommand("DRIVER SETBAND " + band);
    }

   /**
=======
    public native static boolean stopFilteringMulticastV6Packets();

    public native static boolean setPowerModeCommand(int mode);

    public native static int getBandCommand();

    public native static boolean setBandCommand(int band);

    public native static int getPowerModeCommand();

    /**
>>>>>>> upstream/master
     * Sets the bluetooth coexistence mode.
     *
     * @param mode One of {@link #BLUETOOTH_COEXISTENCE_MODE_DISABLED},
     *            {@link #BLUETOOTH_COEXISTENCE_MODE_ENABLED}, or
     *            {@link #BLUETOOTH_COEXISTENCE_MODE_SENSE}.
     * @return Whether the mode was successfully set.
     */
<<<<<<< HEAD
    public boolean setBluetoothCoexistenceMode(int mode) {
        return doBooleanCommand("DRIVER BTCOEXMODE " + mode);
    }
=======
    public native static boolean setBluetoothCoexistenceModeCommand(int mode);
>>>>>>> upstream/master

    /**
     * Enable or disable Bluetooth coexistence scan mode. When this mode is on,
     * some of the low-level scan parameters used by the driver are changed to
     * reduce interference with A2DP streaming.
     *
     * @param isSet whether to enable or disable this mode
     * @return {@code true} if the command succeeded, {@code false} otherwise.
     */
<<<<<<< HEAD
    public boolean setBluetoothCoexistenceScanMode(boolean setCoexScanMode) {
        if (setCoexScanMode) {
            return doBooleanCommand("DRIVER BTCOEXSCAN-START");
        } else {
            return doBooleanCommand("DRIVER BTCOEXSCAN-STOP");
        }
    }

    public boolean saveConfig() {
        // Make sure we never write out a value for AP_SCAN other than 1
        return doBooleanCommand("AP_SCAN 1") && doBooleanCommand("SAVE_CONFIG");
    }

    public boolean setScanResultHandling(int mode) {
        return doBooleanCommand("AP_SCAN " + mode);
    }

    public boolean addToBlacklist(String bssid) {
        if (TextUtils.isEmpty(bssid)) return false;
        return doBooleanCommand("BLACKLIST " + bssid);
    }

    public boolean clearBlacklist() {
        return doBooleanCommand("BLACKLIST clear");
    }

    public boolean setSuspendOptimizations(boolean enabled) {
        if (enabled) {
            return doBooleanCommand("DRIVER SETSUSPENDMODE 1");
        } else {
            return doBooleanCommand("DRIVER SETSUSPENDMODE 0");
        }
    }

    public boolean setCountryCode(String countryCode) {
        return doBooleanCommand("DRIVER COUNTRY " + countryCode);
    }

    public void enableBackgroundScan(boolean enable) {
        if (enable) {
            doBooleanCommand("SET pno 1");
        } else {
            doBooleanCommand("SET pno 0");
        }
    }

    public void setScanInterval(int scanInterval) {
        doBooleanCommand("SCAN_INTERVAL " + scanInterval);
    }
=======
    public native static boolean setBluetoothCoexistenceScanModeCommand(boolean setCoexScanMode);

    public native static boolean saveConfigCommand();

    public native static boolean reloadConfigCommand();

    public native static boolean setScanResultHandlingCommand(int mode);

    public native static boolean addToBlacklistCommand(String bssid);

    public native static boolean clearBlacklistCommand();

    public native static boolean startWpsPbcCommand(String bssid);

    public native static boolean startWpsWithPinFromAccessPointCommand(String bssid, String apPin);

    public native static String startWpsWithPinFromDeviceCommand(String bssid);

    public native static boolean setSuspendOptimizationsCommand(boolean enabled);

    public native static boolean setCountryCodeCommand(String countryCode);

    /**
     * Wait for the supplicant to send an event, returning the event string.
     * @return the event string sent by the supplicant.
     */
    public native static String waitForEvent();

    public native static void enableBackgroundScanCommand(boolean enable);

    public native static void setScanIntervalCommand(int scanInterval);

    private native static boolean doBooleanCommand(String command);

    private native static int doIntCommand(String command);

    private native static String doStringCommand(String command);
>>>>>>> upstream/master

    /** Example output:
     * RSSI=-65
     * LINKSPEED=48
     * NOISE=9999
     * FREQUENCY=0
     */
<<<<<<< HEAD
    public String signalPoll() {
        return doStringCommand("SIGNAL_POLL");
    }

    public boolean startWpsPbc(String bssid) {
        if (TextUtils.isEmpty(bssid)) {
            return doBooleanCommand("WPS_PBC");
        } else {
            return doBooleanCommand("WPS_PBC " + bssid);
        }
    }

    public boolean startWpsPbc(String iface, String bssid) {
        if (TextUtils.isEmpty(bssid)) {
            return doBooleanCommand("WPS_PBC interface=" + iface);
        } else {
            return doBooleanCommand("WPS_PBC interface=" + iface + " " + bssid);
        }
    }

    public boolean startWpsPinKeypad(String pin) {
        if (TextUtils.isEmpty(pin)) return false;
        return doBooleanCommand("WPS_PIN any " + pin);
    }

    public boolean startWpsPinKeypad(String iface, String pin) {
        if (TextUtils.isEmpty(pin)) return false;
        return doBooleanCommand("WPS_PIN interface=" + iface + " any " + pin);
    }


    public String startWpsPinDisplay(String bssid) {
        if (TextUtils.isEmpty(bssid)) {
            return doStringCommand("WPS_PIN any");
        } else {
            return doStringCommand("WPS_PIN " + bssid);
        }
    }

    public String startWpsPinDisplay(String iface, String bssid) {
        if (TextUtils.isEmpty(bssid)) {
            return doStringCommand("WPS_PIN interface=" + iface + " any");
        } else {
            return doStringCommand("WPS_PIN interface=" + iface + " " + bssid);
        }
    }

    /* Configures an access point connection */
    public boolean startWpsRegistrar(String bssid, String pin) {
        if (TextUtils.isEmpty(bssid) || TextUtils.isEmpty(pin)) return false;
        return doBooleanCommand("WPS_REG " + bssid + " " + pin);
    }

    public boolean cancelWps() {
        return doBooleanCommand("WPS_CANCEL");
    }

    public boolean setPersistentReconnect(boolean enabled) {
        int value = (enabled == true) ? 1 : 0;
        return doBooleanCommand("SET persistent_reconnect " + value);
    }

    public boolean setDeviceName(String name) {
        return doBooleanCommand("SET device_name " + name);
    }

    public boolean setDeviceType(String type) {
        return doBooleanCommand("SET device_type " + type);
    }

    public boolean setConfigMethods(String cfg) {
        return doBooleanCommand("SET config_methods " + cfg);
    }

    public boolean setManufacturer(String value) {
        return doBooleanCommand("SET manufacturer " + value);
    }

    public boolean setModelName(String value) {
        return doBooleanCommand("SET model_name " + value);
    }

    public boolean setModelNumber(String value) {
        return doBooleanCommand("SET model_number " + value);
    }

    public boolean setSerialNumber(String value) {
        return doBooleanCommand("SET serial_number " + value);
    }

    public boolean setP2pSsidPostfix(String postfix) {
        return doBooleanCommand("SET p2p_ssid_postfix " + postfix);
    }

    public boolean setP2pGroupIdle(String iface, int time) {
        return doBooleanCommand("SET interface=" + iface + " p2p_group_idle " + time);
    }

    public void setPowerSave(boolean enabled) {
        if (enabled) {
            doBooleanCommand("SET ps 1");
        } else {
            doBooleanCommand("SET ps 0");
        }
    }

    public boolean setP2pPowerSave(String iface, boolean enabled) {
        if (enabled) {
            return doBooleanCommand("P2P_SET interface=" + iface + " ps 1");
        } else {
            return doBooleanCommand("P2P_SET interface=" + iface + " ps 0");
        }
    }

    /**
     * "sta" prioritizes STA connection over P2P and "p2p" prioritizes
     * P2P connection over STA
     */
    public boolean setConcurrencyPriority(String s) {
        return doBooleanCommand("P2P_SET conc_pref " + s);
    }

    public boolean p2pFind() {
        return doBooleanCommand("P2P_FIND");
    }

    public boolean p2pFind(int timeout) {
=======
    public static String signalPoll() {
        return doStringCommand("SIGNAL_POLL");
    }

    public static boolean wpsPbc() {
        return doBooleanCommand("WPS_PBC");
    }

    public static boolean wpsPin(String pin) {
        return doBooleanCommand("WPS_PIN any " + pin);
    }

    public static boolean setPersistentReconnect(boolean enabled) {
        int value = (enabled == true) ? 1 : 0;
        return WifiNative.doBooleanCommand("SET persistent_reconnect " + value);
    }

    public static boolean setDeviceName(String name) {
        return WifiNative.doBooleanCommand("SET device_name " + name);
    }

    public static boolean setDeviceType(String type) {
        return WifiNative.doBooleanCommand("SET device_type " + type);
    }

    public static boolean p2pFind() {
        return doBooleanCommand("P2P_FIND");
    }

    public static boolean p2pFind(int timeout) {
>>>>>>> upstream/master
        if (timeout <= 0) {
            return p2pFind();
        }
        return doBooleanCommand("P2P_FIND " + timeout);
    }

<<<<<<< HEAD
    public boolean p2pStopFind() {
       return doBooleanCommand("P2P_STOP_FIND");
    }

    public boolean p2pListen() {
        return doBooleanCommand("P2P_LISTEN");
    }

    public boolean p2pListen(int timeout) {
=======
    public static boolean p2pListen() {
        return doBooleanCommand("P2P_LISTEN");
    }

    public static boolean p2pListen(int timeout) {
>>>>>>> upstream/master
        if (timeout <= 0) {
            return p2pListen();
        }
        return doBooleanCommand("P2P_LISTEN " + timeout);
    }

<<<<<<< HEAD
    public boolean p2pFlush() {
=======
    public static boolean p2pFlush() {
>>>>>>> upstream/master
        return doBooleanCommand("P2P_FLUSH");
    }

    /* p2p_connect <peer device address> <pbc|pin|PIN#> [label|display|keypad]
        [persistent] [join|auth] [go_intent=<0..15>] [freq=<in MHz>] */
<<<<<<< HEAD
    public String p2pConnect(WifiP2pConfig config, boolean joinExistingGroup) {
=======
    public static String p2pConnect(WifiP2pConfig config, boolean joinExistingGroup) {
>>>>>>> upstream/master
        if (config == null) return null;
        List<String> args = new ArrayList<String>();
        WpsInfo wps = config.wps;
        args.add(config.deviceAddress);

        switch (wps.setup) {
            case WpsInfo.PBC:
                args.add("pbc");
                break;
            case WpsInfo.DISPLAY:
<<<<<<< HEAD
                if (TextUtils.isEmpty(wps.pin)) {
                    args.add("pin");
                } else {
                    args.add(wps.pin);
                }
=======
                //TODO: pass the pin back for display
                args.add("pin");
>>>>>>> upstream/master
                args.add("display");
                break;
            case WpsInfo.KEYPAD:
                args.add(wps.pin);
                args.add("keypad");
                break;
            case WpsInfo.LABEL:
                args.add(wps.pin);
                args.add("label");
            default:
                break;
        }

        //TODO: Add persist behavior once the supplicant interaction is fixed for both
        // group and client scenarios
        /* Persist unless there is an explicit request to not do so*/
        //if (config.persist != WifiP2pConfig.Persist.NO) args.add("persistent");

<<<<<<< HEAD
        if (joinExistingGroup) {
            args.add("join");
        } else {
            //TODO: This can be adapted based on device plugged in state and
            //device battery state
            int groupOwnerIntent = config.groupOwnerIntent;
            if (groupOwnerIntent < 0 || groupOwnerIntent > 15) {
                groupOwnerIntent = DEFAULT_GROUP_OWNER_INTENT;
            }
            args.add("go_intent=" + groupOwnerIntent);
        }
=======
        if (joinExistingGroup) args.add("join");

        int groupOwnerIntent = config.groupOwnerIntent;
        if (groupOwnerIntent < 0 || groupOwnerIntent > 15) {
            groupOwnerIntent = 3; //default value
        }
        args.add("go_intent=" + groupOwnerIntent);
>>>>>>> upstream/master

        String command = "P2P_CONNECT ";
        for (String s : args) command += s + " ";

        return doStringCommand(command);
    }

<<<<<<< HEAD
    public boolean p2pCancelConnect() {
        return doBooleanCommand("P2P_CANCEL");
    }

    public boolean p2pProvisionDiscovery(WifiP2pConfig config) {
        if (config == null) return false;

        switch (config.wps.setup) {
            case WpsInfo.PBC:
                return doBooleanCommand("P2P_PROV_DISC " + config.deviceAddress + " pbc");
            case WpsInfo.DISPLAY:
                //We are doing display, so provision discovery is keypad
                return doBooleanCommand("P2P_PROV_DISC " + config.deviceAddress + " keypad");
            case WpsInfo.KEYPAD:
                //We are doing keypad, so provision discovery is display
                return doBooleanCommand("P2P_PROV_DISC " + config.deviceAddress + " display");
            default:
                break;
        }
        return false;
    }

    public boolean p2pGroupAdd() {
        return doBooleanCommand("P2P_GROUP_ADD");
    }

    public boolean p2pGroupRemove(String iface) {
        if (TextUtils.isEmpty(iface)) return false;
        return doBooleanCommand("P2P_GROUP_REMOVE " + iface);
    }

    public boolean p2pReject(String deviceAddress) {
=======
    public static boolean p2pCancelConnect() {
        return doBooleanCommand("P2P_CANCEL");
    }

    public static boolean p2pGroupAdd() {
        return doBooleanCommand("P2P_GROUP_ADD");
    }

    public static boolean p2pGroupRemove(String iface) {
        if (iface == null) return false;
        return doBooleanCommand("P2P_GROUP_REMOVE " + iface);
    }

    public static boolean p2pReject(String deviceAddress) {
>>>>>>> upstream/master
        return doBooleanCommand("P2P_REJECT " + deviceAddress);
    }

    /* Invite a peer to a group */
<<<<<<< HEAD
    public boolean p2pInvite(WifiP2pGroup group, String deviceAddress) {
        if (TextUtils.isEmpty(deviceAddress)) return false;
=======
    public static boolean p2pInvite(WifiP2pGroup group, String deviceAddress) {
        if (deviceAddress == null) return false;
>>>>>>> upstream/master

        if (group == null) {
            return doBooleanCommand("P2P_INVITE peer=" + deviceAddress);
        } else {
            return doBooleanCommand("P2P_INVITE group=" + group.getInterface()
                    + " peer=" + deviceAddress + " go_dev_addr=" + group.getOwner().deviceAddress);
        }
    }

    /* Reinvoke a persistent connection */
<<<<<<< HEAD
    public boolean p2pReinvoke(int netId, String deviceAddress) {
        if (TextUtils.isEmpty(deviceAddress) || netId < 0) return false;
=======
    public static boolean p2pReinvoke(int netId, String deviceAddress) {
        if (deviceAddress == null || netId < 0) return false;
>>>>>>> upstream/master

        return doBooleanCommand("P2P_INVITE persistent=" + netId + " peer=" + deviceAddress);
    }


<<<<<<< HEAD
    public String p2pGetDeviceAddress() {
        String status = status();
        if (status == null) return "";

        String[] tokens = status.split("\n");
        for (String token : tokens) {
            if (token.startsWith("p2p_device_address=")) {
=======
    public static String p2pGetInterfaceAddress(String deviceAddress) {
        if (deviceAddress == null) return null;

        //  "p2p_peer deviceAddress" returns a multi-line result containing
        //      intended_addr=fa:7b:7a:42:82:13
        String peerInfo = p2pPeer(deviceAddress);
        if (peerInfo == null) return null;
        String[] tokens= peerInfo.split("\n");

        for (String token : tokens) {
            //TODO: update from interface_addr when wpa_supplicant implementation is fixed
            if (token.startsWith("intended_addr=")) {
>>>>>>> upstream/master
                String[] nameValue = token.split("=");
                if (nameValue.length != 2) break;
                return nameValue[1];
            }
        }
<<<<<<< HEAD
        return "";
    }

    public int getGroupCapability(String deviceAddress) {
        int gc = 0;
        if (TextUtils.isEmpty(deviceAddress)) return gc;
        String peerInfo = p2pPeer(deviceAddress);
        if (TextUtils.isEmpty(peerInfo)) return gc;

        String[] tokens = peerInfo.split("\n");
        for (String token : tokens) {
            if (token.startsWith("group_capab=")) {
                String[] nameValue = token.split("=");
                if (nameValue.length != 2) break;
                try {
                    return Integer.decode(nameValue[1]);
                } catch(NumberFormatException e) {
                    return gc;
                }
            }
        }
        return gc;
    }

    public String p2pPeer(String deviceAddress) {
        return doStringCommand("P2P_PEER " + deviceAddress);
    }

    public boolean p2pServiceAdd(WifiP2pServiceInfo servInfo) {
        /*
         * P2P_SERVICE_ADD bonjour <query hexdump> <RDATA hexdump>
         * P2P_SERVICE_ADD upnp <version hex> <service>
         *
         * e.g)
         * [Bonjour]
         * # IP Printing over TCP (PTR) (RDATA=MyPrinter._ipp._tcp.local.)
         * P2P_SERVICE_ADD bonjour 045f697070c00c000c01 094d795072696e746572c027
         * # IP Printing over TCP (TXT) (RDATA=txtvers=1,pdl=application/postscript)
         * P2P_SERVICE_ADD bonjour 096d797072696e746572045f697070c00c001001
         *  09747874766572733d311a70646c3d6170706c69636174696f6e2f706f7374736372797074
         *
         * [UPnP]
         * P2P_SERVICE_ADD upnp 10 uuid:6859dede-8574-59ab-9332-123456789012
         * P2P_SERVICE_ADD upnp 10 uuid:6859dede-8574-59ab-9332-123456789012::upnp:rootdevice
         * P2P_SERVICE_ADD upnp 10 uuid:6859dede-8574-59ab-9332-123456789012::urn:schemas-upnp
         * -org:device:InternetGatewayDevice:1
         * P2P_SERVICE_ADD upnp 10 uuid:6859dede-8574-59ab-9322-123456789012::urn:schemas-upnp
         * -org:service:ContentDirectory:2
         */
        for (String s : servInfo.getSupplicantQueryList()) {
            String command = "P2P_SERVICE_ADD";
            command += (" " + s);
            if (!doBooleanCommand(command)) {
                return false;
            }
        }
        return true;
    }

    public boolean p2pServiceDel(WifiP2pServiceInfo servInfo) {
        /*
         * P2P_SERVICE_DEL bonjour <query hexdump>
         * P2P_SERVICE_DEL upnp <version hex> <service>
         */
        for (String s : servInfo.getSupplicantQueryList()) {
            String command = "P2P_SERVICE_DEL ";

            String[] data = s.split(" ");
            if (data.length < 2) {
                return false;
            }
            if ("upnp".equals(data[0])) {
                command += s;
            } else if ("bonjour".equals(data[0])) {
                command += data[0];
                command += (" " + data[1]);
            } else {
                return false;
            }
            if (!doBooleanCommand(command)) {
                return false;
            }
        }
        return true;
    }

    public boolean p2pServiceFlush() {
        return doBooleanCommand("P2P_SERVICE_FLUSH");
    }

    public String p2pServDiscReq(String addr, String query) {
        String command = "P2P_SERV_DISC_REQ";
        command += (" " + addr);
        command += (" " + query);

        return doStringCommand(command);
    }

    public boolean p2pServDiscCancelReq(String id) {
        return doBooleanCommand("P2P_SERV_DISC_CANCEL_REQ " + id);
    }
=======
        return null;
    }

    public static String p2pGetDeviceAddress() {
        String status = statusCommand();
        if (status == null) return "";

        String[] tokens = status.split("\n");
        for (String token : tokens) {
            if (token.startsWith("p2p_device_address=")) {
                String[] nameValue = token.split("=");
                if (nameValue.length != 2) break;
                return nameValue[1];
            }
        }
        return "";
    }

    public static String p2pPeer(String deviceAddress) {
        return doStringCommand("P2P_PEER " + deviceAddress);
    }
>>>>>>> upstream/master
}
