/*
 ** Copyright 2011, The Android Open Source Project
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

package android.view.accessibility;

import android.accessibilityservice.IAccessibilityServiceConnection;
import android.graphics.Rect;
<<<<<<< HEAD
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseLongArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
=======
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;

import java.util.Collections;
import java.util.List;
>>>>>>> upstream/master
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is a singleton that performs accessibility interaction
 * which is it queries remote view hierarchies about snapshots of their
 * views as well requests from these hierarchies to perform certain
 * actions on their views.
 *
 * Rationale: The content retrieval APIs are synchronous from a client's
 *     perspective but internally they are asynchronous. The client thread
 *     calls into the system requesting an action and providing a callback
 *     to receive the result after which it waits up to a timeout for that
 *     result. The system enforces security and the delegates the request
 *     to a given view hierarchy where a message is posted (from a binder
 *     thread) describing what to be performed by the main UI thread the
 *     result of which it delivered via the mentioned callback. However,
 *     the blocked client thread and the main UI thread of the target view
 *     hierarchy can be the same thread, for example an accessibility service
 *     and an activity run in the same process, thus they are executed on the
 *     same main thread. In such a case the retrieval will fail since the UI
 *     thread that has to process the message describing the work to be done
 *     is blocked waiting for a result is has to compute! To avoid this scenario
 *     when making a call the client also passes its process and thread ids so
 *     the accessed view hierarchy can detect if the client making the request
 *     is running in its main UI thread. In such a case the view hierarchy,
 *     specifically the binder thread performing the IPC to it, does not post a
 *     message to be run on the UI thread but passes it to the singleton
 *     interaction client through which all interactions occur and the latter is
 *     responsible to execute the message before starting to wait for the
 *     asynchronous result delivered via the callback. In this case the expected
 *     result is already received so no waiting is performed.
 *
 * @hide
 */
public final class AccessibilityInteractionClient
        extends IAccessibilityInteractionConnectionCallback.Stub {

    public static final int NO_ID = -1;

    private static final String LOG_TAG = "AccessibilityInteractionClient";

    private static final boolean DEBUG = false;

<<<<<<< HEAD
    private static final boolean CHECK_INTEGRITY = true;

=======
>>>>>>> upstream/master
    private static final long TIMEOUT_INTERACTION_MILLIS = 5000;

    private static final Object sStaticLock = new Object();

<<<<<<< HEAD
    private static final LongSparseArray<AccessibilityInteractionClient> sClients =
        new LongSparseArray<AccessibilityInteractionClient>();
=======
    private static AccessibilityInteractionClient sInstance;
>>>>>>> upstream/master

    private final AtomicInteger mInteractionIdCounter = new AtomicInteger();

    private final Object mInstanceLock = new Object();

<<<<<<< HEAD
    private volatile int mInteractionId = -1;
=======
    private int mInteractionId = -1;
>>>>>>> upstream/master

    private AccessibilityNodeInfo mFindAccessibilityNodeInfoResult;

    private List<AccessibilityNodeInfo> mFindAccessibilityNodeInfosResult;

    private boolean mPerformAccessibilityActionResult;

    private Message mSameThreadMessage;

    private final Rect mTempBounds = new Rect();

    // The connection cache is shared between all interrogating threads.
    private static final SparseArray<IAccessibilityServiceConnection> sConnectionCache =
        new SparseArray<IAccessibilityServiceConnection>();

<<<<<<< HEAD
    // The connection cache is shared between all interrogating threads since
    // at any given time there is only one window allowing querying.
    private static final AccessibilityNodeInfoCache sAccessibilityNodeInfoCache =
        new AccessibilityNodeInfoCache();

    /**
     * @return The client for the current thread.
     */
    public static AccessibilityInteractionClient getInstance() {
        final long threadId = Thread.currentThread().getId();
        return getInstanceForThread(threadId);
    }

    /**
     * <strong>Note:</strong> We keep one instance per interrogating thread since
     * the instance contains state which can lead to undesired thread interleavings.
     * We do not have a thread local variable since other threads should be able to
     * look up the correct client knowing a thread id. See ViewRootImpl for details.
     *
     * @return The client for a given <code>threadId</code>.
     */
    public static AccessibilityInteractionClient getInstanceForThread(long threadId) {
        synchronized (sStaticLock) {
            AccessibilityInteractionClient client = sClients.get(threadId);
            if (client == null) {
                client = new AccessibilityInteractionClient();
                sClients.put(threadId, client);
            }
            return client;
        }
    }

    private AccessibilityInteractionClient() {
        /* reducing constructor visibility */
    }

=======
    /**
     * @return The singleton of this class.
     */
    public static AccessibilityInteractionClient getInstance() {
        synchronized (sStaticLock) {
            if (sInstance == null) {
                sInstance = new AccessibilityInteractionClient();
            }
            return sInstance;
        }
    }

>>>>>>> upstream/master
    /**
     * Sets the message to be processed if the interacted view hierarchy
     * and the interacting client are running in the same thread.
     *
     * @param message The message.
     */
    public void setSameThreadMessage(Message message) {
        synchronized (mInstanceLock) {
            mSameThreadMessage = message;
            mInstanceLock.notifyAll();
        }
    }

    /**
<<<<<<< HEAD
     * Gets the root {@link AccessibilityNodeInfo} in the currently active window.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @return The root {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public AccessibilityNodeInfo getRootInActiveWindow(int connectionId) {
        return findAccessibilityNodeInfoByAccessibilityId(connectionId,
                AccessibilityNodeInfo.ACTIVE_WINDOW_ID, AccessibilityNodeInfo.ROOT_NODE_ID,
                AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS);
    }

    /**
     * Finds an {@link AccessibilityNodeInfo} by accessibility id.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param prefetchFlags flags to guide prefetching.
     * @return An {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public AccessibilityNodeInfo findAccessibilityNodeInfoByAccessibilityId(int connectionId,
            int accessibilityWindowId, long accessibilityNodeId, int prefetchFlags) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                AccessibilityNodeInfo cachedInfo = sAccessibilityNodeInfoCache.get(
                        accessibilityNodeId);
                if (cachedInfo != null) {
                    return cachedInfo;
                }
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final float windowScale = connection.findAccessibilityNodeInfoByAccessibilityId(
                        accessibilityWindowId, accessibilityNodeId, interactionId, this,
                        prefetchFlags, Thread.currentThread().getId());
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    List<AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(
                            interactionId);
                    finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, windowScale);
                    if (infos != null && !infos.isEmpty()) {
                        return infos.get(0);
                    }
=======
     * Finds an {@link AccessibilityNodeInfo} by accessibility id.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param accessibilityWindowId A unique window id.
     * @param accessibilityViewId A unique View accessibility id.
     * @return An {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public AccessibilityNodeInfo findAccessibilityNodeInfoByAccessibilityId(int connectionId,
            int accessibilityWindowId, int accessibilityViewId) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final float windowScale = connection.findAccessibilityNodeInfoByAccessibilityId(
                        accessibilityWindowId, accessibilityViewId, interactionId, this,
                        Thread.currentThread().getId());
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(
                            interactionId);
                    finalizeAccessibilityNodeInfo(info, connectionId, windowScale);
                    return info;
>>>>>>> upstream/master
                }
            } else {
                if (DEBUG) {
                    Log.w(LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (RemoteException re) {
            if (DEBUG) {
                Log.w(LOG_TAG, "Error while calling remote"
                        + " findAccessibilityNodeInfoByAccessibilityId", re);
            }
        }
        return null;
    }

    /**
<<<<<<< HEAD
     * Finds an {@link AccessibilityNodeInfo} by View id. The search is performed in
     * the window whose id is specified and starts from the node whose accessibility
     * id is specified.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param viewId The id of the view.
     * @return An {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public AccessibilityNodeInfo findAccessibilityNodeInfoByViewId(int connectionId,
            int accessibilityWindowId, long accessibilityNodeId, int viewId) {
=======
     * Finds an {@link AccessibilityNodeInfo} by View id. The search is performed
     * in the currently active window and starts from the root View in the window.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param viewId The id of the view.
     * @return An {@link AccessibilityNodeInfo} if found, null otherwise.
     */
    public AccessibilityNodeInfo findAccessibilityNodeInfoByViewIdInActiveWindow(int connectionId,
            int viewId) {
>>>>>>> upstream/master
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final float windowScale =
<<<<<<< HEAD
                    connection.findAccessibilityNodeInfoByViewId(accessibilityWindowId,
                            accessibilityNodeId, viewId, interactionId, this,
                            Thread.currentThread().getId());
=======
                    connection.findAccessibilityNodeInfoByViewIdInActiveWindow(viewId,
                            interactionId, this, Thread.currentThread().getId());
>>>>>>> upstream/master
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(
                            interactionId);
<<<<<<< HEAD
                    finalizeAndCacheAccessibilityNodeInfo(info, connectionId, windowScale);
=======
                    finalizeAccessibilityNodeInfo(info, connectionId, windowScale);
>>>>>>> upstream/master
                    return info;
                }
            } else {
                if (DEBUG) {
                    Log.w(LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (RemoteException re) {
            if (DEBUG) {
                Log.w(LOG_TAG, "Error while calling remote"
                        + " findAccessibilityNodeInfoByViewIdInActiveWindow", re);
            }
        }
        return null;
    }

    /**
     * Finds {@link AccessibilityNodeInfo}s by View text. The match is case
<<<<<<< HEAD
     * insensitive containment. The search is performed in the window whose
     * id is specified and starts from the node whose accessibility id is
     * specified.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param text The searched text.
     * @return A list of found {@link AccessibilityNodeInfo}s.
     */
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(int connectionId,
            int accessibilityWindowId, long accessibilityNodeId, String text) {
=======
     * insensitive containment. The search is performed in the currently
     * active window and starts from the root View in the window.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param text The searched text.
     * @return A list of found {@link AccessibilityNodeInfo}s.
     */
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewTextInActiveWindow(
            int connectionId, String text) {
>>>>>>> upstream/master
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
<<<<<<< HEAD
                final float windowScale = connection.findAccessibilityNodeInfosByText(
                        accessibilityWindowId, accessibilityNodeId, text, interactionId, this,
                        Thread.currentThread().getId());
=======
                final float windowScale =
                    connection.findAccessibilityNodeInfosByViewTextInActiveWindow(text,
                            interactionId, this, Thread.currentThread().getId());
>>>>>>> upstream/master
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    List<AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(
                            interactionId);
<<<<<<< HEAD
                    finalizeAndCacheAccessibilityNodeInfos(infos, connectionId, windowScale);
=======
                    finalizeAccessibilityNodeInfos(infos, connectionId, windowScale);
>>>>>>> upstream/master
                    return infos;
                }
            } else {
                if (DEBUG) {
                    Log.w(LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (RemoteException re) {
            if (DEBUG) {
                Log.w(LOG_TAG, "Error while calling remote"
<<<<<<< HEAD
                        + " findAccessibilityNodeInfosByViewText", re);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Finds the {@link android.view.accessibility.AccessibilityNodeInfo} that has the
     * specified focus type. The search is performed in the window whose id is specified
     * and starts from the node whose accessibility id is specified.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param focusType The focus type.
     * @return The accessibility focused {@link AccessibilityNodeInfo}.
     */
    public AccessibilityNodeInfo findFocus(int connectionId, int accessibilityWindowId,
            long accessibilityNodeId, int focusType) {
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final float windowScale = connection.findFocus(accessibilityWindowId,
                        accessibilityNodeId, focusType, interactionId, this,
                        Thread.currentThread().getId());
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(
                            interactionId);
                    finalizeAndCacheAccessibilityNodeInfo(info, connectionId, windowScale);
                    return info;
                }
            } else {
                if (DEBUG) {
                    Log.w(LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (RemoteException re) {
            if (DEBUG) {
                Log.w(LOG_TAG, "Error while calling remote findAccessibilityFocus", re);
=======
                        + " findAccessibilityNodeInfosByViewTextInActiveWindow", re);
>>>>>>> upstream/master
            }
        }
        return null;
    }

    /**
<<<<<<< HEAD
     * Finds the accessibility focused {@link android.view.accessibility.AccessibilityNodeInfo}.
     * The search is performed in the window whose id is specified and starts from the
     * node whose accessibility id is specified.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param direction The direction in which to search for focusable.
     * @return The accessibility focused {@link AccessibilityNodeInfo}.
     */
    public AccessibilityNodeInfo focusSearch(int connectionId, int accessibilityWindowId,
            long accessibilityNodeId, int direction) {
=======
     * Finds {@link AccessibilityNodeInfo}s by View text. The match is case
     * insensitive containment. The search is performed in the window whose
     * id is specified and starts from the View whose accessibility id is
     * specified.
     *
     * @param connectionId The id of a connection for interacting with the system.
     * @param text The searched text.
     * @param accessibilityWindowId A unique window id.
     * @param accessibilityViewId A unique View accessibility id from where to start the search.
     *        Use {@link android.view.View#NO_ID} to start from the root.
     * @return A list of found {@link AccessibilityNodeInfo}s.
     */
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewText(int connectionId,
            String text, int accessibilityWindowId, int accessibilityViewId) {
>>>>>>> upstream/master
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
<<<<<<< HEAD
                final float windowScale = connection.focusSearch(accessibilityWindowId,
                        accessibilityNodeId, direction, interactionId, this,
                        Thread.currentThread().getId());
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    AccessibilityNodeInfo info = getFindAccessibilityNodeInfoResultAndClear(
                            interactionId);
                    finalizeAndCacheAccessibilityNodeInfo(info, connectionId, windowScale);
                    return info;
=======
                final float windowScale = connection.findAccessibilityNodeInfosByViewText(text,
                        accessibilityWindowId, accessibilityViewId, interactionId, this,
                        Thread.currentThread().getId());
                // If the scale is zero the call has failed.
                if (windowScale > 0) {
                    List<AccessibilityNodeInfo> infos = getFindAccessibilityNodeInfosResultAndClear(
                            interactionId);
                    finalizeAccessibilityNodeInfos(infos, connectionId, windowScale);
                    return infos;
>>>>>>> upstream/master
                }
            } else {
                if (DEBUG) {
                    Log.w(LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (RemoteException re) {
            if (DEBUG) {
<<<<<<< HEAD
                Log.w(LOG_TAG, "Error while calling remote accessibilityFocusSearch", re);
            }
        }
        return null;
=======
                Log.w(LOG_TAG, "Error while calling remote"
                        + " findAccessibilityNodeInfosByViewText", re);
            }
        }
        return Collections.emptyList();
>>>>>>> upstream/master
    }

    /**
     * Performs an accessibility action on an {@link AccessibilityNodeInfo}.
     *
     * @param connectionId The id of a connection for interacting with the system.
<<<<<<< HEAD
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param action The action to perform.
     * @param arguments Optional action arguments.
     * @return Whether the action was performed.
     */
    public boolean performAccessibilityAction(int connectionId, int accessibilityWindowId,
            long accessibilityNodeId, int action, Bundle arguments) {
=======
     * @param accessibilityWindowId The id of the window.
     * @param accessibilityViewId A unique View accessibility id.
     * @param action The action to perform.
     * @return Whether the action was performed.
     */
    public boolean performAccessibilityAction(int connectionId, int accessibilityWindowId,
            int accessibilityViewId, int action) {
>>>>>>> upstream/master
        try {
            IAccessibilityServiceConnection connection = getConnection(connectionId);
            if (connection != null) {
                final int interactionId = mInteractionIdCounter.getAndIncrement();
                final boolean success = connection.performAccessibilityAction(
<<<<<<< HEAD
                        accessibilityWindowId, accessibilityNodeId, action, arguments,
                        interactionId, this, Thread.currentThread().getId());
                if (success) {
                    return getPerformAccessibilityActionResultAndClear(interactionId);
=======
                        accessibilityWindowId, accessibilityViewId, action, interactionId, this,
                        Thread.currentThread().getId());
                if (success) {
                    return getPerformAccessibilityActionResult(interactionId);
>>>>>>> upstream/master
                }
            } else {
                if (DEBUG) {
                    Log.w(LOG_TAG, "No connection for connection id: " + connectionId);
                }
            }
        } catch (RemoteException re) {
            if (DEBUG) {
                Log.w(LOG_TAG, "Error while calling remote performAccessibilityAction", re);
            }
        }
        return false;
    }

<<<<<<< HEAD
    public void clearCache() {
        sAccessibilityNodeInfoCache.clear();
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
        sAccessibilityNodeInfoCache.onAccessibilityEvent(event);
    }

=======
>>>>>>> upstream/master
    /**
     * Gets the the result of an async request that returns an {@link AccessibilityNodeInfo}.
     *
     * @param interactionId The interaction id to match the result with the request.
     * @return The result {@link AccessibilityNodeInfo}.
     */
    private AccessibilityNodeInfo getFindAccessibilityNodeInfoResultAndClear(int interactionId) {
        synchronized (mInstanceLock) {
            final boolean success = waitForResultTimedLocked(interactionId);
            AccessibilityNodeInfo result = success ? mFindAccessibilityNodeInfoResult : null;
            clearResultLocked();
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo info,
                int interactionId) {
        synchronized (mInstanceLock) {
            if (interactionId > mInteractionId) {
                mFindAccessibilityNodeInfoResult = info;
                mInteractionId = interactionId;
            }
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Gets the the result of an async request that returns {@link AccessibilityNodeInfo}s.
     *
     * @param interactionId The interaction id to match the result with the request.
     * @return The result {@link AccessibilityNodeInfo}s.
     */
    private List<AccessibilityNodeInfo> getFindAccessibilityNodeInfosResultAndClear(
                int interactionId) {
        synchronized (mInstanceLock) {
            final boolean success = waitForResultTimedLocked(interactionId);
<<<<<<< HEAD
            List<AccessibilityNodeInfo> result = null;
            if (success) {
                result = mFindAccessibilityNodeInfosResult;
            } else {
                result = Collections.emptyList();
            }
            clearResultLocked();
            if (Build.IS_DEBUGGABLE && CHECK_INTEGRITY) {
                checkFindAccessibilityNodeInfoResultIntegrity(result);
            }
=======
            List<AccessibilityNodeInfo> result = success ? mFindAccessibilityNodeInfosResult : null;
            clearResultLocked();
>>>>>>> upstream/master
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> infos,
                int interactionId) {
        synchronized (mInstanceLock) {
            if (interactionId > mInteractionId) {
<<<<<<< HEAD
                if (infos != null) {
                    // If the call is not an IPC, i.e. it is made from the same process, we need to
                    // instantiate new result list to avoid passing internal instances to clients.
                    final boolean isIpcCall = (Binder.getCallingPid() != Process.myPid());
                    if (!isIpcCall) {
                        mFindAccessibilityNodeInfosResult =
                            new ArrayList<AccessibilityNodeInfo>(infos);
                    } else {
                        mFindAccessibilityNodeInfosResult = infos;
                    }
                } else {
                    mFindAccessibilityNodeInfosResult = Collections.emptyList();
                }
=======
                mFindAccessibilityNodeInfosResult = infos;
>>>>>>> upstream/master
                mInteractionId = interactionId;
            }
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Gets the result of a request to perform an accessibility action.
     *
     * @param interactionId The interaction id to match the result with the request.
     * @return Whether the action was performed.
     */
<<<<<<< HEAD
    private boolean getPerformAccessibilityActionResultAndClear(int interactionId) {
=======
    private boolean getPerformAccessibilityActionResult(int interactionId) {
>>>>>>> upstream/master
        synchronized (mInstanceLock) {
            final boolean success = waitForResultTimedLocked(interactionId);
            final boolean result = success ? mPerformAccessibilityActionResult : false;
            clearResultLocked();
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setPerformAccessibilityActionResult(boolean succeeded, int interactionId) {
        synchronized (mInstanceLock) {
            if (interactionId > mInteractionId) {
                mPerformAccessibilityActionResult = succeeded;
                mInteractionId = interactionId;
            }
            mInstanceLock.notifyAll();
        }
    }

    /**
     * Clears the result state.
     */
    private void clearResultLocked() {
        mInteractionId = -1;
        mFindAccessibilityNodeInfoResult = null;
        mFindAccessibilityNodeInfosResult = null;
        mPerformAccessibilityActionResult = false;
    }

    /**
     * Waits up to a given bound for a result of a request and returns it.
     *
     * @param interactionId The interaction id to match the result with the request.
     * @return Whether the result was received.
     */
    private boolean waitForResultTimedLocked(int interactionId) {
        long waitTimeMillis = TIMEOUT_INTERACTION_MILLIS;
        final long startTimeMillis = SystemClock.uptimeMillis();
        while (true) {
            try {
                Message sameProcessMessage = getSameProcessMessageAndClear();
                if (sameProcessMessage != null) {
                    sameProcessMessage.getTarget().handleMessage(sameProcessMessage);
                }

                if (mInteractionId == interactionId) {
                    return true;
                }
                if (mInteractionId > interactionId) {
                    return false;
                }
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                waitTimeMillis = TIMEOUT_INTERACTION_MILLIS - elapsedTimeMillis;
                if (waitTimeMillis <= 0) {
                    return false;
                }
                mInstanceLock.wait(waitTimeMillis);
            } catch (InterruptedException ie) {
                /* ignore */
            }
        }
    }

    /**
     * Applies compatibility scale to the info bounds if it is not equal to one.
     *
     * @param info The info whose bounds to scale.
     * @param scale The scale to apply.
     */
    private void applyCompatibilityScaleIfNeeded(AccessibilityNodeInfo info, float scale) {
        if (scale == 1.0f) {
            return;
        }
        Rect bounds = mTempBounds;
        info.getBoundsInParent(bounds);
        bounds.scale(scale);
        info.setBoundsInParent(bounds);

        info.getBoundsInScreen(bounds);
        bounds.scale(scale);
        info.setBoundsInScreen(bounds);
    }

    /**
     * Finalize an {@link AccessibilityNodeInfo} before passing it to the client.
     *
     * @param info The info.
     * @param connectionId The id of the connection to the system.
     * @param windowScale The source window compatibility scale.
     */
<<<<<<< HEAD
    private void finalizeAndCacheAccessibilityNodeInfo(AccessibilityNodeInfo info, int connectionId,
=======
    private void finalizeAccessibilityNodeInfo(AccessibilityNodeInfo info, int connectionId,
>>>>>>> upstream/master
            float windowScale) {
        if (info != null) {
            applyCompatibilityScaleIfNeeded(info, windowScale);
            info.setConnectionId(connectionId);
            info.setSealed(true);
<<<<<<< HEAD
            sAccessibilityNodeInfoCache.add(info);
=======
>>>>>>> upstream/master
        }
    }

    /**
     * Finalize {@link AccessibilityNodeInfo}s before passing them to the client.
     *
     * @param infos The {@link AccessibilityNodeInfo}s.
     * @param connectionId The id of the connection to the system.
     * @param windowScale The source window compatibility scale.
     */
<<<<<<< HEAD
    private void finalizeAndCacheAccessibilityNodeInfos(List<AccessibilityNodeInfo> infos,
=======
    private void finalizeAccessibilityNodeInfos(List<AccessibilityNodeInfo> infos,
>>>>>>> upstream/master
            int connectionId, float windowScale) {
        if (infos != null) {
            final int infosCount = infos.size();
            for (int i = 0; i < infosCount; i++) {
                AccessibilityNodeInfo info = infos.get(i);
<<<<<<< HEAD
                finalizeAndCacheAccessibilityNodeInfo(info, connectionId, windowScale);
=======
                finalizeAccessibilityNodeInfo(info, connectionId, windowScale);
>>>>>>> upstream/master
            }
        }
    }

    /**
     * Gets the message stored if the interacted and interacting
     * threads are the same.
     *
     * @return The message.
     */
    private Message getSameProcessMessageAndClear() {
        synchronized (mInstanceLock) {
            Message result = mSameThreadMessage;
            mSameThreadMessage = null;
            return result;
        }
    }

    /**
     * Gets a cached accessibility service connection.
     *
     * @param connectionId The connection id.
     * @return The cached connection if such.
     */
    public IAccessibilityServiceConnection getConnection(int connectionId) {
        synchronized (sConnectionCache) {
            return sConnectionCache.get(connectionId);
        }
    }

    /**
     * Adds a cached accessibility service connection.
     *
     * @param connectionId The connection id.
     * @param connection The connection.
     */
    public void addConnection(int connectionId, IAccessibilityServiceConnection connection) {
        synchronized (sConnectionCache) {
            sConnectionCache.put(connectionId, connection);
        }
    }

    /**
     * Removes a cached accessibility service connection.
     *
     * @param connectionId The connection id.
     */
    public void removeConnection(int connectionId) {
        synchronized (sConnectionCache) {
            sConnectionCache.remove(connectionId);
        }
    }
<<<<<<< HEAD

    /**
     * Checks whether the infos are a fully connected tree with no duplicates.
     *
     * @param infos The result list to check.
     */
    private void checkFindAccessibilityNodeInfoResultIntegrity(List<AccessibilityNodeInfo> infos) {
        if (infos.size() == 0) {
            return;
        }
        // Find the root node.
        AccessibilityNodeInfo root = infos.get(0);
        final int infoCount = infos.size();
        for (int i = 1; i < infoCount; i++) {
            for (int j = i; j < infoCount; j++) {
                AccessibilityNodeInfo candidate = infos.get(j);
                if (root.getParentNodeId() == candidate.getSourceNodeId()) {
                    root = candidate;
                    break;
                }
            }
        }
        if (root == null) {
            Log.e(LOG_TAG, "No root.");
        }
        // Check for duplicates.
        HashSet<AccessibilityNodeInfo> seen = new HashSet<AccessibilityNodeInfo>();
        Queue<AccessibilityNodeInfo> fringe = new LinkedList<AccessibilityNodeInfo>();
        fringe.add(root);
        while (!fringe.isEmpty()) {
            AccessibilityNodeInfo current = fringe.poll();
            if (!seen.add(current)) {
                Log.e(LOG_TAG, "Duplicate node.");
                return;
            }
            SparseLongArray childIds = current.getChildNodeIds();
            final int childCount = childIds.size();
            for (int i = 0; i < childCount; i++) {
                final long childId = childIds.valueAt(i);
                for (int j = 0; j < infoCount; j++) {
                    AccessibilityNodeInfo child = infos.get(j);
                    if (child.getSourceNodeId() == childId) {
                        fringe.add(child);
                    }
                }
            }
        }
        final int disconnectedCount = infos.size() - seen.size();
        if (disconnectedCount > 0) {
            Log.e(LOG_TAG, disconnectedCount + " Disconnected nodes.");
        }
    }
=======
>>>>>>> upstream/master
}
