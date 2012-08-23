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

import static com.android.internal.util.Preconditions.checkNotNull;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.internal.util.Objects;

/**
 * Policy for networks matching a {@link NetworkTemplate}, including usage cycle
 * and limits to be enforced.
 *
 * @hide
 */
public class NetworkPolicy implements Parcelable, Comparable<NetworkPolicy> {
<<<<<<< HEAD
    public static final int CYCLE_NONE = -1;
=======
>>>>>>> upstream/master
    public static final long WARNING_DISABLED = -1;
    public static final long LIMIT_DISABLED = -1;
    public static final long SNOOZE_NEVER = -1;

    public final NetworkTemplate template;
    public int cycleDay;
<<<<<<< HEAD
    public String cycleTimezone;
    public long warningBytes;
    public long limitBytes;
    public long lastWarningSnooze;
    public long lastLimitSnooze;
    public boolean metered;
    public boolean inferred;

    private static final long DEFAULT_MTU = 1500;

    @Deprecated
    public NetworkPolicy(NetworkTemplate template, int cycleDay, String cycleTimezone,
            long warningBytes, long limitBytes, boolean metered) {
        this(template, cycleDay, cycleTimezone, warningBytes, limitBytes, SNOOZE_NEVER,
                SNOOZE_NEVER, metered, false);
    }

    public NetworkPolicy(NetworkTemplate template, int cycleDay, String cycleTimezone,
            long warningBytes, long limitBytes, long lastWarningSnooze, long lastLimitSnooze,
            boolean metered, boolean inferred) {
        this.template = checkNotNull(template, "missing NetworkTemplate");
        this.cycleDay = cycleDay;
        this.cycleTimezone = checkNotNull(cycleTimezone, "missing cycleTimezone");
        this.warningBytes = warningBytes;
        this.limitBytes = limitBytes;
        this.lastWarningSnooze = lastWarningSnooze;
        this.lastLimitSnooze = lastLimitSnooze;
        this.metered = metered;
        this.inferred = inferred;
=======
    public long warningBytes;
    public long limitBytes;
    public long lastSnooze;

    private static final long DEFAULT_MTU = 1500;

    public NetworkPolicy(NetworkTemplate template, int cycleDay, long warningBytes, long limitBytes,
            long lastSnooze) {
        this.template = checkNotNull(template, "missing NetworkTemplate");
        this.cycleDay = cycleDay;
        this.warningBytes = warningBytes;
        this.limitBytes = limitBytes;
        this.lastSnooze = lastSnooze;
>>>>>>> upstream/master
    }

    public NetworkPolicy(Parcel in) {
        template = in.readParcelable(null);
        cycleDay = in.readInt();
<<<<<<< HEAD
        cycleTimezone = in.readString();
        warningBytes = in.readLong();
        limitBytes = in.readLong();
        lastWarningSnooze = in.readLong();
        lastLimitSnooze = in.readLong();
        metered = in.readInt() != 0;
        inferred = in.readInt() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(template, flags);
        dest.writeInt(cycleDay);
        dest.writeString(cycleTimezone);
        dest.writeLong(warningBytes);
        dest.writeLong(limitBytes);
        dest.writeLong(lastWarningSnooze);
        dest.writeLong(lastLimitSnooze);
        dest.writeInt(metered ? 1 : 0);
        dest.writeInt(inferred ? 1 : 0);
    }

    @Override
=======
        warningBytes = in.readLong();
        limitBytes = in.readLong();
        lastSnooze = in.readLong();
    }

    /** {@inheritDoc} */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(template, flags);
        dest.writeInt(cycleDay);
        dest.writeLong(warningBytes);
        dest.writeLong(limitBytes);
        dest.writeLong(lastSnooze);
    }

    /** {@inheritDoc} */
>>>>>>> upstream/master
    public int describeContents() {
        return 0;
    }

    /**
<<<<<<< HEAD
     * Test if given measurement is over {@link #warningBytes}.
     */
    public boolean isOverWarning(long totalBytes) {
        return warningBytes != WARNING_DISABLED && totalBytes >= warningBytes;
    }

    /**
=======
>>>>>>> upstream/master
     * Test if given measurement is near enough to {@link #limitBytes} to be
     * considered over-limit.
     */
    public boolean isOverLimit(long totalBytes) {
        // over-estimate, since kernel will trigger limit once first packet
        // trips over limit.
        totalBytes += 2 * DEFAULT_MTU;
        return limitBytes != LIMIT_DISABLED && totalBytes >= limitBytes;
    }

<<<<<<< HEAD
    /**
     * Clear any existing snooze values, setting to {@link #SNOOZE_NEVER}.
     */
    public void clearSnooze() {
        lastWarningSnooze = SNOOZE_NEVER;
        lastLimitSnooze = SNOOZE_NEVER;
    }

    /**
     * Test if this policy has a cycle defined, after which usage should reset.
     */
    public boolean hasCycle() {
        return cycleDay != CYCLE_NONE;
    }

    @Override
=======
    /** {@inheritDoc} */
>>>>>>> upstream/master
    public int compareTo(NetworkPolicy another) {
        if (another == null || another.limitBytes == LIMIT_DISABLED) {
            // other value is missing or disabled; we win
            return -1;
        }
        if (limitBytes == LIMIT_DISABLED || another.limitBytes < limitBytes) {
            // we're disabled or other limit is smaller; they win
            return 1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
<<<<<<< HEAD
        return Objects.hashCode(template, cycleDay, cycleTimezone, warningBytes, limitBytes,
                lastWarningSnooze, lastLimitSnooze, metered, inferred);
=======
        return Objects.hashCode(template, cycleDay, warningBytes, limitBytes, lastSnooze);
>>>>>>> upstream/master
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NetworkPolicy) {
            final NetworkPolicy other = (NetworkPolicy) obj;
<<<<<<< HEAD
            return cycleDay == other.cycleDay && warningBytes == other.warningBytes
                    && limitBytes == other.limitBytes
                    && lastWarningSnooze == other.lastWarningSnooze
                    && lastLimitSnooze == other.lastLimitSnooze && metered == other.metered
                    && inferred == other.inferred
                    && Objects.equal(cycleTimezone, other.cycleTimezone)
                    && Objects.equal(template, other.template);
=======
            return Objects.equal(template, other.template) && cycleDay == other.cycleDay
                    && warningBytes == other.warningBytes && limitBytes == other.limitBytes
                    && lastSnooze == other.lastSnooze;
>>>>>>> upstream/master
        }
        return false;
    }

    @Override
    public String toString() {
<<<<<<< HEAD
        final StringBuilder builder = new StringBuilder("NetworkPolicy");
        builder.append("[").append(template).append("]:");
        builder.append(" cycleDay=").append(cycleDay);
        builder.append(", cycleTimezone=").append(cycleTimezone);
        builder.append(", warningBytes=").append(warningBytes);
        builder.append(", limitBytes=").append(limitBytes);
        builder.append(", lastWarningSnooze=").append(lastWarningSnooze);
        builder.append(", lastLimitSnooze=").append(lastLimitSnooze);
        builder.append(", metered=").append(metered);
        builder.append(", inferred=").append(inferred);
        return builder.toString();
    }

    public static final Creator<NetworkPolicy> CREATOR = new Creator<NetworkPolicy>() {
        @Override
=======
        return "NetworkPolicy[" + template + "]: cycleDay=" + cycleDay + ", warningBytes="
                + warningBytes + ", limitBytes=" + limitBytes + ", lastSnooze=" + lastSnooze;
    }

    public static final Creator<NetworkPolicy> CREATOR = new Creator<NetworkPolicy>() {
>>>>>>> upstream/master
        public NetworkPolicy createFromParcel(Parcel in) {
            return new NetworkPolicy(in);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public NetworkPolicy[] newArray(int size) {
            return new NetworkPolicy[size];
        }
    };
}
