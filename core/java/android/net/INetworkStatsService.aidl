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

package android.net;

<<<<<<< HEAD
import android.net.INetworkStatsSession;
=======
>>>>>>> upstream/master
import android.net.NetworkStats;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;

/** {@hide} */
interface INetworkStatsService {

<<<<<<< HEAD
    /** Start a statistics query session. */
    INetworkStatsSession openSession();

    /** Return network layer usage total for traffic that matches template. */
    long getNetworkTotalBytes(in NetworkTemplate template, long start, long end);

    /** Return data layer snapshot of UID network usage. */
    NetworkStats getDataLayerSnapshotForUid(int uid);
    /** Return set of any ifaces associated with mobile networks since boot. */
    String[] getMobileIfaces();

=======
    /** Return historical network layer stats for traffic that matches template. */
    NetworkStatsHistory getHistoryForNetwork(in NetworkTemplate template, int fields);
    /** Return historical network layer stats for specific UID traffic that matches template. */
    NetworkStatsHistory getHistoryForUid(in NetworkTemplate template, int uid, int set, int tag, int fields);

    /** Return network layer usage summary for traffic that matches template. */
    NetworkStats getSummaryForNetwork(in NetworkTemplate template, long start, long end);
    /** Return network layer usage summary per UID for traffic that matches template. */
    NetworkStats getSummaryForAllUid(in NetworkTemplate template, long start, long end, boolean includeTags);

    /** Return data layer snapshot of UID network usage. */
    NetworkStats getDataLayerSnapshotForUid(int uid);
>>>>>>> upstream/master
    /** Increment data layer count of operations performed for UID and tag. */
    void incrementOperationCount(int uid, int tag, int operationCount);

    /** Mark given UID as being in foreground for stats purposes. */
    void setUidForeground(int uid, boolean uidForeground);
    /** Force update of statistics. */
    void forceUpdate();
<<<<<<< HEAD
    /** Advise persistance threshold; may be overridden internally. */
    void advisePersistThreshold(long thresholdBytes);
=======
>>>>>>> upstream/master

}
