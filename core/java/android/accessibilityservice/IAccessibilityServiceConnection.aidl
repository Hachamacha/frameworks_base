/*
 * Copyright (C) 2009 The Android Open Source Project
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

package android.accessibilityservice;

<<<<<<< HEAD
import android.os.Bundle;
=======
>>>>>>> upstream/master
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;

/**
 * Interface given to an AccessibilitySerivce to talk to the AccessibilityManagerService.
 *
 * @hide
 */
interface IAccessibilityServiceConnection {

    void setServiceInfo(in AccessibilityServiceInfo info);

    /**
<<<<<<< HEAD
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} by accessibility id.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param flags Additional flags.
=======
     * Finds an {@link AccessibilityNodeInfo} by accessibility id.
     *
     * @param accessibilityWindowId A unique window id.
     * @param accessibilityViewId A unique View accessibility id.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
>>>>>>> upstream/master
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
    float findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId,
<<<<<<< HEAD
        long accessibilityNodeId, int interactionId,
        IAccessibilityInteractionConnectionCallback callback, int flags, long threadId);

    /**
     * Finds {@link android.view.accessibility.AccessibilityNodeInfo}s by View text.
     * The match is case insensitive containment. The search is performed in the window
     * whose id is specified and starts from the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param text The searched text.
=======
        int accessibilityViewId, int interactionId,
        IAccessibilityInteractionConnectionCallback callback, long threadId);

    /**
     * Finds {@link AccessibilityNodeInfo}s by View text. The match is case
     * insensitive containment. The search is performed in the window whose
     * id is specified and starts from the View whose accessibility id is
     * specified.
     *
     * @param text The searched text.
     * @param accessibilityWindowId A unique window id.
     * @param accessibilityViewId A unique View accessibility id from where to start the search.
     *        Use {@link android.view.View#NO_ID} to start from the root.
>>>>>>> upstream/master
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
<<<<<<< HEAD
    float findAccessibilityNodeInfosByText(int accessibilityWindowId, long accessibilityNodeId,
        String text, int interactionId, IAccessibilityInteractionConnectionCallback callback,
        long threadId);

    /**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} by View id. The search
     * is performed in the window whose id is specified and starts from the node whose
     * accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param id The id of the node.
=======
    float findAccessibilityNodeInfosByViewText(String text, int accessibilityWindowId,
        int accessibilityViewId, int interractionId,
        IAccessibilityInteractionConnectionCallback callback, long threadId);

    /**
     * Finds {@link AccessibilityNodeInfo}s by View text. The match is case
     * insensitive containment. The search is performed in the currently
     * active window and start from the root View in the window.
     *
     * @param text The searched text.
     * @param accessibilityId The id of the view from which to start searching.
     *        Use {@link android.view.View#NO_ID} to start from the root.
>>>>>>> upstream/master
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
<<<<<<< HEAD
    float findAccessibilityNodeInfoByViewId(int accessibilityWindowId, long accessibilityNodeId,
        int viewId, int interactionId, IAccessibilityInteractionConnectionCallback callback,
        long threadId);

    /**
     * Finds the {@link android.view.accessibility.AccessibilityNodeInfo} that has the specified
     * focus type. The search is performed in the window whose id is specified and starts from
     * the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param focusType The type of focus to find.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
    float findFocus(int accessibilityWindowId, long accessibilityNodeId, int focusType,
        int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId);

    /**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} to take accessibility
     * focus in the given direction. The search is performed in the window whose id is
     * specified and starts from the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param direction The direction in which to search for focusable.
=======
    float findAccessibilityNodeInfosByViewTextInActiveWindow(String text,
        int interactionId, IAccessibilityInteractionConnectionCallback callback,
        long threadId);

    /**
     * Finds an {@link AccessibilityNodeInfo} by View id. The search is performed
     * in the currently active window and starts from the root View in the window.
     *
     * @param id The id of the node.
>>>>>>> upstream/master
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
<<<<<<< HEAD
    float focusSearch(int accessibilityWindowId, long accessibilityNodeId, int direction,
        int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId);

    /**
     * Performs an accessibility action on an
     * {@link android.view.accessibility.AccessibilityNodeInfo}.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param action The action to perform.
     * @param arguments Optional action arguments.
=======
    float findAccessibilityNodeInfoByViewIdInActiveWindow(int viewId, int interactionId,
        IAccessibilityInteractionConnectionCallback callback, long threadId);

    /**
     * Performs an accessibility action on an {@link AccessibilityNodeInfo}.
     *
     * @param accessibilityWindowId The id of the window.
     * @param accessibilityViewId A unique View accessibility id.
     * @param action The action to perform.
>>>>>>> upstream/master
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return Whether the action was performed.
     */
<<<<<<< HEAD
    boolean performAccessibilityAction(int accessibilityWindowId, long accessibilityNodeId,
        int action, in Bundle arguments, int interactionId,
        IAccessibilityInteractionConnectionCallback callback, long threadId);

    /**
     * @return The associated accessibility service info.
     */
    AccessibilityServiceInfo getServiceInfo();

    /**
     * Performs a global action, such as going home, going back, etc.
     *
     * @param action The action to perform.
     * @return Whether the action was performed.
     */
    boolean performGlobalAction(int action);
=======
    boolean performAccessibilityAction(int accessibilityWindowId, int accessibilityViewId,
        int action, int interactionId, IAccessibilityInteractionConnectionCallback callback,
        long threadId);
>>>>>>> upstream/master
}
