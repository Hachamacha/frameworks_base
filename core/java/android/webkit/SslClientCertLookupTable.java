/*
 * Copyright (C) 2011 The Android Open Source Project
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
import java.security.PrivateKey;
=======
>>>>>>> upstream/master
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple class to store client certificates that user has chosen.
 */
final class SslClientCertLookupTable {
    private static SslClientCertLookupTable sTable;
<<<<<<< HEAD
    private final Map<String, PrivateKey> privateKeys;
=======
    private final Map<String, byte[]> privateKeys;
>>>>>>> upstream/master
    private final Map<String, byte[][]> certificateChains;
    private final Set<String> denied;

    public static synchronized SslClientCertLookupTable getInstance() {
        if (sTable == null) {
            sTable = new SslClientCertLookupTable();
        }
        return sTable;
    }

    private SslClientCertLookupTable() {
<<<<<<< HEAD
        privateKeys = new HashMap<String, PrivateKey>();
=======
        privateKeys = new HashMap<String, byte[]>();
>>>>>>> upstream/master
        certificateChains = new HashMap<String, byte[][]>();
        denied = new HashSet<String>();
    }

<<<<<<< HEAD
    public void Allow(String host_and_port, PrivateKey privateKey, byte[][] chain) {
=======
    public void Allow(String host_and_port, byte[] privateKey, byte[][] chain) {
>>>>>>> upstream/master
        privateKeys.put(host_and_port, privateKey);
        certificateChains.put(host_and_port, chain);
        denied.remove(host_and_port);
    }

    public void Deny(String host_and_port) {
        privateKeys.remove(host_and_port);
        certificateChains.remove(host_and_port);
        denied.add(host_and_port);
    }

    public boolean IsAllowed(String host_and_port) {
        return privateKeys.containsKey(host_and_port);
    }

    public boolean IsDenied(String host_and_port) {
        return denied.contains(host_and_port);
    }

<<<<<<< HEAD
    public PrivateKey PrivateKey(String host_and_port) {
=======
    public byte[] PrivateKey(String host_and_port) {
>>>>>>> upstream/master
        return privateKeys.get(host_and_port);
    }

    public byte[][] CertificateChain(String host_and_port) {
        return certificateChains.get(host_and_port);
    }
}
