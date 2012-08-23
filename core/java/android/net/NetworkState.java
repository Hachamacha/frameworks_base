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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Snapshot of network state.
 *
 * @hide
 */
public class NetworkState implements Parcelable {

    public final NetworkInfo networkInfo;
    public final LinkProperties linkProperties;
    public final LinkCapabilities linkCapabilities;
    /** Currently only used by testing. */
    public final String subscriberId;
<<<<<<< HEAD
    public final String networkId;

    public NetworkState(NetworkInfo networkInfo, LinkProperties linkProperties,
            LinkCapabilities linkCapabilities) {
        this(networkInfo, linkProperties, linkCapabilities, null, null);
    }

    public NetworkState(NetworkInfo networkInfo, LinkProperties linkProperties,
            LinkCapabilities linkCapabilities, String subscriberId, String networkId) {
=======

    public NetworkState(NetworkInfo networkInfo, LinkProperties linkProperties,
            LinkCapabilities linkCapabilities) {
        this(networkInfo, linkProperties, linkCapabilities, null);
    }

    public NetworkState(NetworkInfo networkInfo, LinkProperties linkProperties,
            LinkCapabilities linkCapabilities, String subscriberId) {
>>>>>>> upstream/master
        this.networkInfo = networkInfo;
        this.linkProperties = linkProperties;
        this.linkCapabilities = linkCapabilities;
        this.subscriberId = subscriberId;
<<<<<<< HEAD
        this.networkId = networkId;
=======
>>>>>>> upstream/master
    }

    public NetworkState(Parcel in) {
        networkInfo = in.readParcelable(null);
        linkProperties = in.readParcelable(null);
        linkCapabilities = in.readParcelable(null);
        subscriberId = in.readString();
<<<<<<< HEAD
        networkId = in.readString();
    }

    @Override
=======
    }

    /** {@inheritDoc} */
>>>>>>> upstream/master
    public int describeContents() {
        return 0;
    }

<<<<<<< HEAD
    @Override
=======
    /** {@inheritDoc} */
>>>>>>> upstream/master
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(networkInfo, flags);
        out.writeParcelable(linkProperties, flags);
        out.writeParcelable(linkCapabilities, flags);
        out.writeString(subscriberId);
<<<<<<< HEAD
        out.writeString(networkId);
    }

    public static final Creator<NetworkState> CREATOR = new Creator<NetworkState>() {
        @Override
=======
    }

    public static final Creator<NetworkState> CREATOR = new Creator<NetworkState>() {
>>>>>>> upstream/master
        public NetworkState createFromParcel(Parcel in) {
            return new NetworkState(in);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public NetworkState[] newArray(int size) {
            return new NetworkState[size];
        }
    };

}
