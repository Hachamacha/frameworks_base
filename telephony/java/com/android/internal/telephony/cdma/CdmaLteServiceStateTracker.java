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

package com.android.internal.telephony.cdma;

import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.RILConstants;
<<<<<<< HEAD
import com.android.internal.telephony.IccCard;
=======
>>>>>>> upstream/master

import android.content.Intent;
import android.telephony.SignalStrength;
import android.telephony.ServiceState;
import android.telephony.cdma.CdmaCellLocation;
import android.os.AsyncResult;
import android.os.Message;
<<<<<<< HEAD
import android.os.SystemProperties;
=======
>>>>>>> upstream/master
import android.provider.Telephony.Intents;

import android.text.TextUtils;
import android.util.Log;
import android.util.EventLog;

import com.android.internal.telephony.gsm.GsmDataConnectionTracker;

<<<<<<< HEAD
import java.io.FileDescriptor;
import java.io.PrintWriter;

=======
>>>>>>> upstream/master
public class CdmaLteServiceStateTracker extends CdmaServiceStateTracker {
    CDMALTEPhone mCdmaLtePhone;

    private ServiceState  mLteSS;  // The last LTE state from Voice Registration
<<<<<<< HEAD

    public CdmaLteServiceStateTracker(CDMALTEPhone phone) {
        super(phone);
=======
    private boolean mNeedToRegForSimLoaded = true;

    public CdmaLteServiceStateTracker(CDMALTEPhone phone) {
        super(phone);
        cm.registerForSIMReady(this, EVENT_SIM_READY, null);
>>>>>>> upstream/master
        mCdmaLtePhone = phone;

        mLteSS = new ServiceState();
        if (DBG) log("CdmaLteServiceStateTracker Constructors");
    }

    @Override
<<<<<<< HEAD
=======
    public void dispose() {
        cm.unregisterForSIMReady(this);
        super.dispose();
    }

    @Override
>>>>>>> upstream/master
    public void handleMessage(Message msg) {
        AsyncResult ar;
        int[] ints;
        String[] strings;
        switch (msg.what) {
        case EVENT_POLL_STATE_GPRS:
            if (DBG) log("handleMessage EVENT_POLL_STATE_GPRS");
            ar = (AsyncResult)msg.obj;
            handlePollStateResult(msg.what, ar);
            break;
<<<<<<< HEAD
        case EVENT_RUIM_RECORDS_LOADED:
=======
        case EVENT_SIM_READY:
            if (DBG) log("handleMessage EVENT_SIM_READY");
            isSubscriptionFromRuim = false;
            // Register SIM_RECORDS_LOADED dynamically.
            // This is to avoid confilct with RUIM_READY scenario)
            if (mNeedToRegForSimLoaded) {
                phone.mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
                mNeedToRegForSimLoaded = false;
            }
            pollState();
            // Signal strength polling stops when radio is off.
            queueNextSignalStrengthPoll();

            // load ERI file
            phone.prepareEri();
            break;
        case EVENT_SIM_RECORDS_LOADED:
>>>>>>> upstream/master
            CdmaLteUiccRecords sim = (CdmaLteUiccRecords)phone.mIccRecords;
            if ((sim != null) && sim.isProvisioned()) {
                mMdn = sim.getMdn();
                mMin = sim.getMin();
                parseSidNid(sim.getSid(), sim.getNid());
                mPrlVersion = sim.getPrlVersion();;
                mIsMinInfoReady = true;
                updateOtaspState();
            }
            // SID/NID/PRL is loaded. Poll service state
            // again to update to the roaming state with
            // the latest variables.
            pollState();
            break;
        default:
            super.handleMessage(msg);
        }
    }

    /**
     * Set the cdmaSS for EVENT_POLL_STATE_REGISTRATION_CDMA
     */
    @Override
    protected void setCdmaTechnology(int radioTechnology) {
        // Called on voice registration state response.
        // Just record new CDMA radio technology
        newSS.setRadioTechnology(radioTechnology);
    }

    /**
     * Handle the result of one of the pollState()-related requests
     */
    @Override
    protected void handlePollStateResultMessage(int what, AsyncResult ar) {
        if (what == EVENT_POLL_STATE_GPRS) {
            if (DBG) log("handlePollStateResultMessage: EVENT_POLL_STATE_GPRS");
            String states[] = (String[])ar.result;

            int type = 0;
            int regState = -1;
            if (states.length > 0) {
                try {
                    regState = Integer.parseInt(states[0]);

                    // states[3] (if present) is the current radio technology
                    if (states.length >= 4 && states[3] != null) {
                        type = Integer.parseInt(states[3]);
                    }
                } catch (NumberFormatException ex) {
                    loge("handlePollStateResultMessage: error parsing GprsRegistrationState: "
                                    + ex);
                }
            }

            mLteSS.setRadioTechnology(type);
            mLteSS.setState(regCodeToServiceState(regState));
        } else {
            super.handlePollStateResultMessage(what, ar);
        }
    }

    @Override
    protected void setSignalStrengthDefaultValues() {
        // TODO Make a constructor only has boolean gsm as parameter
        mSignalStrength = new SignalStrength(99, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, SignalStrength.INVALID_SNR, -1, false);
    }

    @Override
    protected void pollState() {
        pollingContext = new int[1];
        pollingContext[0] = 0;

        switch (cm.getRadioState()) {
            case RADIO_UNAVAILABLE:
                newSS.setStateOutOfService();
                newCellLoc.setStateInvalid();
                setSignalStrengthDefaultValues();
                mGotCountryCode = false;

                pollStateDone();
                break;

            case RADIO_OFF:
                newSS.setStateOff();
                newCellLoc.setStateInvalid();
                setSignalStrengthDefaultValues();
                mGotCountryCode = false;

                pollStateDone();
                break;

            default:
                // Issue all poll-related commands at once, then count
                // down the responses which are allowed to arrive
                // out-of-order.

                pollingContext[0]++;
                // RIL_REQUEST_OPERATOR is necessary for CDMA
                cm.getOperator(obtainMessage(EVENT_POLL_STATE_OPERATOR_CDMA, pollingContext));

                pollingContext[0]++;
                // RIL_REQUEST_VOICE_REGISTRATION_STATE is necessary for CDMA
                cm.getVoiceRegistrationState(obtainMessage(EVENT_POLL_STATE_REGISTRATION_CDMA,
                        pollingContext));

                int networkMode = android.provider.Settings.Secure.getInt(phone.getContext()
                        .getContentResolver(),
                        android.provider.Settings.Secure.PREFERRED_NETWORK_MODE,
                        RILConstants.PREFERRED_NETWORK_MODE);
                if (DBG) log("pollState: network mode here is = " + networkMode);
                if ((networkMode == RILConstants.NETWORK_MODE_GLOBAL)
                        || (networkMode == RILConstants.NETWORK_MODE_LTE_ONLY)) {
                    pollingContext[0]++;
                    // RIL_REQUEST_DATA_REGISTRATION_STATE
                    cm.getDataRegistrationState(obtainMessage(EVENT_POLL_STATE_GPRS,
                                                pollingContext));
                }
                break;
        }
    }

    @Override
    protected void pollStateDone() {
<<<<<<< HEAD
        // determine data RadioTechnology from both LET and CDMA SS
        if (mLteSS.getState() == ServiceState.STATE_IN_SERVICE) {
            //in LTE service
            mNewRilRadioTechnology = mLteSS.getRilRadioTechnology();
            mNewDataConnectionState = mLteSS.getState();
            newSS.setRadioTechnology(mNewRilRadioTechnology);
            log("pollStateDone LTE/eHRPD STATE_IN_SERVICE mNewRilRadioTechnology = " +
                    mNewRilRadioTechnology);
        } else {
            // LTE out of service, get CDMA Service State
            mNewRilRadioTechnology = newSS.getRilRadioTechnology();
            mNewDataConnectionState = radioTechnologyToDataServiceState(mNewRilRadioTechnology);
            log("pollStateDone CDMA STATE_IN_SERVICE mNewRilRadioTechnology = " +
                    mNewRilRadioTechnology + " mNewDataConnectionState = " +
                    mNewDataConnectionState);
=======
        // determine data NetworkType from both LET and CDMA SS
        if (mLteSS.getState() == ServiceState.STATE_IN_SERVICE) {
            //in LTE service
            newNetworkType = mLteSS.getRadioTechnology();
            mNewDataConnectionState = mLteSS.getState();
            newSS.setRadioTechnology(newNetworkType);
            log("pollStateDone LTE/eHRPD STATE_IN_SERVICE newNetworkType = " + newNetworkType);
        } else {
            // LTE out of service, get CDMA Service State
            newNetworkType = newSS.getRadioTechnology();
            mNewDataConnectionState = radioTechnologyToDataServiceState(newNetworkType);
            log("pollStateDone CDMA STATE_IN_SERVICE newNetworkType = " + newNetworkType +
                " mNewDataConnectionState = " + mNewDataConnectionState);
>>>>>>> upstream/master
        }

        // TODO: Add proper support for LTE Only, we should be looking at
        //       the preferred network mode, to know when newSS state should
        //       be coming from mLteSs state. This was needed to pass a VZW
        //       LTE Only test.
        //
        // If CDMA service is OOS, double check if the device is running with LTE only
        // mode. If that is the case, derive the service state from LTE side.
        // To set in LTE only mode, sqlite3 /data/data/com.android.providers.settings/
        // databases/settings.db "update secure set value='11' where name='preferred_network_mode'"
        if (newSS.getState() == ServiceState.STATE_OUT_OF_SERVICE) {
            int networkMode = android.provider.Settings.Secure.getInt(phone.getContext()
                                  .getContentResolver(),
                                  android.provider.Settings.Secure.PREFERRED_NETWORK_MODE,
                                  RILConstants.PREFERRED_NETWORK_MODE);
            if (networkMode == RILConstants.NETWORK_MODE_LTE_ONLY) {
                if (DBG) log("pollState: LTE Only mode");
                newSS.setState(mLteSS.getState());
            }
        }

        if (DBG) log("pollStateDone: oldSS=[" + ss + "] newSS=[" + newSS + "]");

        boolean hasRegistered = ss.getState() != ServiceState.STATE_IN_SERVICE
                && newSS.getState() == ServiceState.STATE_IN_SERVICE;

        boolean hasDeregistered = ss.getState() == ServiceState.STATE_IN_SERVICE
                && newSS.getState() != ServiceState.STATE_IN_SERVICE;

        boolean hasCdmaDataConnectionAttached =
            mDataConnectionState != ServiceState.STATE_IN_SERVICE
                && mNewDataConnectionState == ServiceState.STATE_IN_SERVICE;

        boolean hasCdmaDataConnectionDetached =
            mDataConnectionState == ServiceState.STATE_IN_SERVICE
                && mNewDataConnectionState != ServiceState.STATE_IN_SERVICE;

        boolean hasCdmaDataConnectionChanged =
            mDataConnectionState != mNewDataConnectionState;

<<<<<<< HEAD
        boolean hasRadioTechnologyChanged = mRilRadioTechnology != mNewRilRadioTechnology;
=======
        boolean hasNetworkTypeChanged = networkType != newNetworkType;
>>>>>>> upstream/master

        boolean hasChanged = !newSS.equals(ss);

        boolean hasRoamingOn = !ss.getRoaming() && newSS.getRoaming();

        boolean hasRoamingOff = ss.getRoaming() && !newSS.getRoaming();

        boolean hasLocationChanged = !newCellLoc.equals(cellLoc);

        boolean has4gHandoff =
                mNewDataConnectionState == ServiceState.STATE_IN_SERVICE &&
<<<<<<< HEAD
                (((mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE) &&
                  (mNewRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD)) ||
                 ((mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD) &&
                  (mNewRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE)));

        boolean hasMultiApnSupport =
                (((mNewRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE) ||
                  (mNewRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD)) &&
                 ((mRilRadioTechnology != ServiceState.RIL_RADIO_TECHNOLOGY_LTE) &&
                  (mRilRadioTechnology != ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD)));

        boolean hasLostMultiApnSupport =
            ((mNewRilRadioTechnology >= ServiceState.RIL_RADIO_TECHNOLOGY_IS95A) &&
             (mNewRilRadioTechnology <= ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A));
=======
                (((networkType == ServiceState.RADIO_TECHNOLOGY_LTE) &&
                  (newNetworkType == ServiceState.RADIO_TECHNOLOGY_EHRPD)) ||
                 ((networkType == ServiceState.RADIO_TECHNOLOGY_EHRPD) &&
                  (newNetworkType == ServiceState.RADIO_TECHNOLOGY_LTE)));

        boolean hasMultiApnSupport =
                (((newNetworkType == ServiceState.RADIO_TECHNOLOGY_LTE) ||
                  (newNetworkType == ServiceState.RADIO_TECHNOLOGY_EHRPD)) &&
                 ((networkType != ServiceState.RADIO_TECHNOLOGY_LTE) &&
                  (networkType != ServiceState.RADIO_TECHNOLOGY_EHRPD)));

        boolean hasLostMultiApnSupport =
            ((newNetworkType >= ServiceState.RADIO_TECHNOLOGY_IS95A) &&
             (newNetworkType <= ServiceState.RADIO_TECHNOLOGY_EVDO_A));
>>>>>>> upstream/master

        if (DBG) {
            log("pollStateDone:"
                + " hasRegistered=" + hasRegistered
                + " hasDeegistered=" + hasDeregistered
                + " hasCdmaDataConnectionAttached=" + hasCdmaDataConnectionAttached
                + " hasCdmaDataConnectionDetached=" + hasCdmaDataConnectionDetached
                + " hasCdmaDataConnectionChanged=" + hasCdmaDataConnectionChanged
<<<<<<< HEAD
                + " hasRadioTechnologyChanged = " + hasRadioTechnologyChanged
=======
                + " hasNetworkTypeChanged = " + hasNetworkTypeChanged
>>>>>>> upstream/master
                + " hasChanged=" + hasChanged
                + " hasRoamingOn=" + hasRoamingOn
                + " hasRoamingOff=" + hasRoamingOff
                + " hasLocationChanged=" + hasLocationChanged
                + " has4gHandoff = " + has4gHandoff
                + " hasMultiApnSupport=" + hasMultiApnSupport
                + " hasLostMultiApnSupport=" + hasLostMultiApnSupport);
        }
        // Add an event log when connection state changes
        if (ss.getState() != newSS.getState()
                || mDataConnectionState != mNewDataConnectionState) {
            EventLog.writeEvent(EventLogTags.CDMA_SERVICE_STATE_CHANGE, ss.getState(),
                    mDataConnectionState, newSS.getState(), mNewDataConnectionState);
        }

        ServiceState tss;
        tss = ss;
        ss = newSS;
        newSS = tss;
        // clean slate for next time
        newSS.setStateOutOfService();
        mLteSS.setStateOutOfService();

        if ((hasMultiApnSupport)
                && (phone.mDataConnectionTracker instanceof CdmaDataConnectionTracker)) {
            if (DBG) log("GsmDataConnectionTracker Created");
            phone.mDataConnectionTracker.dispose();
            phone.mDataConnectionTracker = new GsmDataConnectionTracker(mCdmaLtePhone);
        }

        if ((hasLostMultiApnSupport)
                && (phone.mDataConnectionTracker instanceof GsmDataConnectionTracker)) {
            if (DBG)log("GsmDataConnectionTracker disposed");
            phone.mDataConnectionTracker.dispose();
            phone.mDataConnectionTracker = new CdmaDataConnectionTracker(phone);
        }

        CdmaCellLocation tcl = cellLoc;
        cellLoc = newCellLoc;
        newCellLoc = tcl;

        mDataConnectionState = mNewDataConnectionState;
<<<<<<< HEAD
        mRilRadioTechnology = mNewRilRadioTechnology;
        mNewRilRadioTechnology = 0;

        newSS.setStateOutOfService(); // clean slate for next time

        if (hasRadioTechnologyChanged) {
            phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    ServiceState.rilRadioTechnologyToString(mRilRadioTechnology));
=======
        networkType = newNetworkType;

        newSS.setStateOutOfService(); // clean slate for next time

        if (hasNetworkTypeChanged) {
            phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    ServiceState.radioTechnologyToString(networkType));
>>>>>>> upstream/master
        }

        if (hasRegistered) {
            mNetworkAttachedRegistrants.notifyRegistrants();
        }

        if (hasChanged) {
            if (phone.isEriFileLoaded()) {
                String eriText;
                // Now the CDMAPhone sees the new ServiceState so it can get the
                // new ERI text
                if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
                    eriText = phone.getCdmaEriText();
<<<<<<< HEAD
                } else if (ss.getState() == ServiceState.STATE_POWER_OFF) {
                    eriText = phone.mIccRecords.getServiceProviderName();
                    if (TextUtils.isEmpty(eriText)) {
                        // Sets operator alpha property by retrieving from
                        // build-time system property
                        eriText = SystemProperties.get("ro.cdma.home.operator.alpha");
                    }
                } else {
                    // Note that ServiceState.STATE_OUT_OF_SERVICE is valid used
                    // for mRegistrationState 0,2,3 and 4
=======
                } else {
                    // Note that ServiceState.STATE_OUT_OF_SERVICE is valid used
                    // for
                    // mRegistrationState 0,2,3 and 4
>>>>>>> upstream/master
                    eriText = phone.getContext()
                            .getText(com.android.internal.R.string.roamingTextSearching).toString();
                }
                ss.setOperatorAlphaLong(eriText);
            }

<<<<<<< HEAD
            if (phone.getIccCard().getState() == IccCard.State.READY) {
                // SIM is found on the device. If ERI roaming is OFF, and SID/NID matches
                // one configfured in SIM, use operator name  from CSIM record.
=======
            if (cm.getSimState().isSIMReady()) {
                // SIM is found on the device. If ERI roaming is OFF and SID/NID matches
                // one configfured in SIM, use operator name from CSIM record.
>>>>>>> upstream/master
                boolean showSpn =
                    ((CdmaLteUiccRecords)phone.mIccRecords).getCsimSpnDisplayCondition();
                int iconIndex = ss.getCdmaEriIconIndex();

                if (showSpn && (iconIndex == EriInfo.ROAMING_INDICATOR_OFF) &&
                    isInHomeSidNid(ss.getSystemId(), ss.getNetworkId())) {
                    ss.setOperatorAlphaLong(phone.mIccRecords.getServiceProviderName());
                }
            }

            String operatorNumeric;

            phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ALPHA,
                    ss.getOperatorAlphaLong());

<<<<<<< HEAD
            String prevOperatorNumeric =
                    SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, "");
=======
>>>>>>> upstream/master
            operatorNumeric = ss.getOperatorNumeric();
            phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, operatorNumeric);

            if (operatorNumeric == null) {
<<<<<<< HEAD
                if (DBG) log("operatorNumeric is null");
=======
>>>>>>> upstream/master
                phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY, "");
                mGotCountryCode = false;
            } else {
                String isoCountryCode = "";
<<<<<<< HEAD
                String mcc = operatorNumeric.substring(0, 3);
=======
>>>>>>> upstream/master
                try {
                    isoCountryCode = MccTable.countryCodeForMcc(Integer.parseInt(operatorNumeric
                            .substring(0, 3)));
                } catch (NumberFormatException ex) {
                    loge("countryCodeForMcc error" + ex);
                } catch (StringIndexOutOfBoundsException ex) {
                    loge("countryCodeForMcc error" + ex);
                }

                phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY,
                        isoCountryCode);
                mGotCountryCode = true;
<<<<<<< HEAD

                if (shouldFixTimeZoneNow(phone, operatorNumeric, prevOperatorNumeric,
                        mNeedFixZone)) {
=======
                if (mNeedFixZone) {
>>>>>>> upstream/master
                    fixTimeZone(isoCountryCode);
                }
            }

            phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ISROAMING,
                    ss.getRoaming() ? "true" : "false");

            updateSpnDisplay();
            phone.notifyServiceStateChanged(ss);
        }

        if (hasCdmaDataConnectionAttached || has4gHandoff) {
            mAttachedRegistrants.notifyRegistrants();
        }

        if (hasCdmaDataConnectionDetached) {
            mDetachedRegistrants.notifyRegistrants();
        }

<<<<<<< HEAD
        if ((hasCdmaDataConnectionChanged || hasRadioTechnologyChanged)) {
=======
        if ((hasCdmaDataConnectionChanged || hasNetworkTypeChanged)) {
>>>>>>> upstream/master
            phone.notifyDataConnection(null);
        }

        if (hasRoamingOn) {
            mRoamingOnRegistrants.notifyRegistrants();
        }

        if (hasRoamingOff) {
            mRoamingOffRegistrants.notifyRegistrants();
        }

        if (hasLocationChanged) {
            phone.notifyLocationChanged();
        }
    }

    @Override
    protected void onSignalStrengthResult(AsyncResult ar) {
        SignalStrength oldSignalStrength = mSignalStrength;

        if (ar.exception != null) {
            // Most likely radio is resetting/disconnected change to default
            // values.
            setSignalStrengthDefaultValues();
        } else {
            int[] ints = (int[])ar.result;

            int lteRssi = -1;
            int lteRsrp = -1;
            int lteRsrq = -1;
            int lteRssnr = SignalStrength.INVALID_SNR;
            int lteCqi = -1;

            int offset = 2;
            int cdmaDbm = (ints[offset] > 0) ? -ints[offset] : -120;
            int cdmaEcio = (ints[offset + 1] > 0) ? -ints[offset + 1] : -160;
            int evdoRssi = (ints[offset + 2] > 0) ? -ints[offset + 2] : -120;
            int evdoEcio = (ints[offset + 3] > 0) ? -ints[offset + 3] : -1;
            int evdoSnr = ((ints[offset + 4] > 0) && (ints[offset + 4] <= 8)) ? ints[offset + 4]
                    : -1;

<<<<<<< HEAD
            if (mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE) {
=======
            if (networkType == ServiceState.RADIO_TECHNOLOGY_LTE) {
>>>>>>> upstream/master
                lteRssi = ints[offset+5];
                lteRsrp = ints[offset+6];
                lteRsrq = ints[offset+7];
                lteRssnr = ints[offset+8];
                lteCqi = ints[offset+9];
            }

<<<<<<< HEAD
            if (mRilRadioTechnology != ServiceState.RIL_RADIO_TECHNOLOGY_LTE) {
=======
            if (networkType != ServiceState.RADIO_TECHNOLOGY_LTE) {
>>>>>>> upstream/master
                mSignalStrength = new SignalStrength(99, -1, cdmaDbm, cdmaEcio, evdoRssi, evdoEcio,
                        evdoSnr, false);
            } else {
                mSignalStrength = new SignalStrength(99, -1, cdmaDbm, cdmaEcio, evdoRssi, evdoEcio,
                        evdoSnr, lteRssi, lteRsrp, lteRsrq, lteRssnr, lteCqi, true);
            }
        }

        try {
            phone.notifySignalStrength();
        } catch (NullPointerException ex) {
            loge("onSignalStrengthResult() Phone already destroyed: " + ex
                    + "SignalStrength not notified");
        }
    }

    @Override
    public boolean isConcurrentVoiceAndDataAllowed() {
        // Note: it needs to be confirmed which CDMA network types
        // can support voice and data calls concurrently.
        // For the time-being, the return value will be false.
<<<<<<< HEAD
        return (mRilRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE);
=======
        return (networkType == ServiceState.RADIO_TECHNOLOGY_LTE);
>>>>>>> upstream/master
    }

    /**
     * Check whether the specified SID and NID pair appears in the HOME SID/NID list
     * read from NV or SIM.
     *
     * @return true if provided sid/nid pair belongs to operator's home network.
     */
    private boolean isInHomeSidNid(int sid, int nid) {
        // if SID/NID is not available, assume this is home network.
        if (isSidsAllZeros()) return true;

        // length of SID/NID shold be same
        if (mHomeSystemId.length != mHomeNetworkId.length) return true;

        if (sid == 0) return true;

        for (int i = 0; i < mHomeSystemId.length; i++) {
            // Use SID only if NID is a reserved value.
            // SID 0 and NID 0 and 65535 are reserved. (C.0005 2.6.5.2)
            if ((mHomeSystemId[i] == sid) &&
                ((mHomeNetworkId[i] == 0) || (mHomeNetworkId[i] == 65535) ||
                 (nid == 0) || (nid == 65535) || (mHomeNetworkId[i] == nid))) {
                return true;
            }
        }
        // SID/NID are not in the list. So device is not in home network
        return false;
    }

    @Override
    protected void log(String s) {
        Log.d(LOG_TAG, "[CdmaLteSST] " + s);
    }

    @Override
    protected void loge(String s) {
        Log.e(LOG_TAG, "[CdmaLteSST] " + s);
    }
<<<<<<< HEAD

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("CdmaLteServiceStateTracker extends:");
        super.dump(fd, pw, args);
        pw.println(" mCdmaLtePhone=" + mCdmaLtePhone);
        pw.println(" mLteSS=" + mLteSS);
    }
=======
>>>>>>> upstream/master
}
