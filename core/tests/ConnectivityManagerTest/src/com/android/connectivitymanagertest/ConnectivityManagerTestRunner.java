/*
 * Copyright (C) 2010, The Android Open Source Project
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

package com.android.connectivitymanagertest;

import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import com.android.connectivitymanagertest.functional.ConnectivityManagerMobileTest;
import com.android.connectivitymanagertest.functional.WifiConnectionTest;

import junit.framework.TestSuite;

/**
 * Instrumentation Test Runner for all connectivity manager tests.
 *
 * To run the connectivity manager tests:
 *
 * adb shell am instrument -e ssid <ssid> \
 *     -w com.android.connectivitymanagertest/.ConnectivityManagerTestRunner
 */

public class ConnectivityManagerTestRunner extends InstrumentationTestRunner {
<<<<<<< HEAD
    public boolean mWifiOnlyFlag = false;
    public String mTestSsid = null;
=======
    public String TEST_SSID = null;
>>>>>>> upstream/master

    @Override
    public TestSuite getAllTests() {
        TestSuite suite = new InstrumentationTestSuite(this);
<<<<<<< HEAD
        suite.addTestSuite(ConnectivityManagerMobileTest.class);
=======
        if (!UtilHelper.isWifiOnly()) {
            suite.addTestSuite(ConnectivityManagerMobileTest.class);
        } else {
            // create a new test suite
            suite.setName("ConnectivityManagerWifiOnlyFunctionalTests");
            String[] methodNames = {"testConnectToWifi", "testConnectToWifWithKnownAP",
                    "testDisconnectWifi", "testDataConnectionOverAMWithWifi",
                    "testDataConnectionWithWifiToAMToWifi", "testWifiStateChange"};
            Class<ConnectivityManagerMobileTest> testClass = ConnectivityManagerMobileTest.class;
            for (String method: methodNames) {
                suite.addTest(TestSuite.createTest(testClass, method));
            }
        }
>>>>>>> upstream/master
        suite.addTestSuite(WifiConnectionTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return ConnectivityManagerTestRunner.class.getClassLoader();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        String testSSID = (String) icicle.get("ssid");
        if (testSSID != null) {
<<<<<<< HEAD
            mTestSsid = testSSID;
        }
        String wifiOnlyFlag = (String) icicle.get("wifi-only");
        if (wifiOnlyFlag != null) {
            mWifiOnlyFlag = true;
=======
            TEST_SSID = testSSID;
>>>>>>> upstream/master
        }
    }
}
