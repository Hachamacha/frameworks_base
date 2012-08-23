/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.accessibilityservice;

<<<<<<< HEAD
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_FOCUS;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_FOCUS;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_SELECT;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_SELECTION;

import android.graphics.Rect;
=======
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_FOCUS;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_SELECTION;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_FOCUS;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_SELECT;

import android.content.Context;
import android.graphics.Rect;
import android.os.ServiceManager;
>>>>>>> upstream/master
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
<<<<<<< HEAD
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.frameworks.coretests.R;
import com.android.internal.util.Predicate;
=======
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityManager;

import com.android.frameworks.coretests.R;
>>>>>>> upstream/master

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Activity for testing the accessibility APIs for "interrogation" of
 * the screen content. These APIs allow exploring the screen and
 * requesting an action to be performed on a given view from an
 * AccessiiblityService.
 */
public class InterrogationActivityTest
        extends ActivityInstrumentationTestCase2<InterrogationActivity> {
<<<<<<< HEAD
    private static final boolean DEBUG = false;

    private static String LOG_TAG = "InterrogationActivityTest";

    // Timeout for the accessibility state of an Activity to be fully initialized.
    private static final int TIMEOUT_PROPAGATE_ACCESSIBILITY_EVENT_MILLIS = 5000;

    // Timeout for which non getting accessibility events considers the app idle.
    private static final long IDLE_EVENT_TIME_DELTA_MILLIS = 200;

    // Timeout in which to wait for idle device.
    private static final long GLOBAL_IDLE_DETECTION_TIMEOUT_MILLIS = 1000;

    // Handle to a connection to the AccessibilityManagerService
    private UiTestAutomationBridge mUiTestAutomationBridge;
=======
    private static final boolean DEBUG = true;

    private static String LOG_TAG = "InterrogationActivityTest";

    // Timeout before give up wait for the system to process an accessibility setting change.       
    private static final int TIMEOUT_PROPAGATE_ACCESSIBLITY_SETTING = 2000;

    // Timeout for the accessibility state of an Activity to be fully initialized.
    private static final int TIMEOUT_ACCESSIBLITY_STATE_INITIALIZED_MILLIS = 100;

    // Handle to a connection to the AccessibilityManagerService
    private static int sConnectionId = View.NO_ID;

    // The last received accessibility event
    private volatile AccessibilityEvent mLastAccessibilityEvent;
>>>>>>> upstream/master

    public InterrogationActivityTest() {
        super(InterrogationActivity.class);
    }

    @Override
    public void setUp() throws Exception {
<<<<<<< HEAD
        super.setUp();
        mUiTestAutomationBridge = new UiTestAutomationBridge();
        mUiTestAutomationBridge.connect();
        mUiTestAutomationBridge.waitForIdle(IDLE_EVENT_TIME_DELTA_MILLIS,
                GLOBAL_IDLE_DETECTION_TIMEOUT_MILLIS);
        mUiTestAutomationBridge.executeCommandAndWaitForAccessibilityEvent(new Runnable() {
                // wait for the first accessibility event
                @Override
                public void run() {
                    // bring up the activity
                    getActivity();
                }
            },
            new Predicate<AccessibilityEvent>() {
                @Override
                public boolean apply(AccessibilityEvent event) {
                    return (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                            && event.getPackageName().equals(getActivity().getPackageName()));
                }
            },
            TIMEOUT_PROPAGATE_ACCESSIBILITY_EVENT_MILLIS);
    }

    @Override
    public void tearDown() throws Exception {
        mUiTestAutomationBridge.disconnect();
        super.tearDown();
=======
        ensureConnection();
        bringUpActivityWithInitalizedAccessbility();
>>>>>>> upstream/master
    }

    @LargeTest
    public void testFindAccessibilityNodeInfoByViewId() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
<<<<<<< HEAD
            AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertNotNull(button);
            assertEquals(0, button.getChildCount());

            // bounds
            Rect bounds = new Rect();
            button.getBoundsInParent(bounds);
            assertEquals(0, bounds.left);
            assertEquals(0, bounds.top);
            assertEquals(160, bounds.right);
            assertEquals(100, bounds.bottom);

            // char sequence attributes
            assertEquals("com.android.frameworks.coretests", button.getPackageName());
            assertEquals("android.widget.Button", button.getClassName());
            assertEquals("Button5", button.getText());
            assertNull(button.getContentDescription());

            // boolean attributes
            assertTrue(button.isFocusable());
            assertTrue(button.isClickable());
            assertTrue(button.isEnabled());
            assertFalse(button.isFocused());
            assertTrue(button.isClickable());
            assertFalse(button.isPassword());
            assertFalse(button.isSelected());
            assertFalse(button.isCheckable());
            assertFalse(button.isChecked());

            // actions
            assertEquals(ACTION_FOCUS | ACTION_SELECT | ACTION_CLEAR_SELECTION,
                button.getActions());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testFindAccessibilityNodeInfoByViewId: "
                        + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testFindAccessibilityNodeInfoByViewText() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view by text
<<<<<<< HEAD
            List<AccessibilityNodeInfo> buttons = mUiTestAutomationBridge
                .findAccessibilityNodeInfosByTextInActiveWindow("butto");
=======
            List<AccessibilityNodeInfo> buttons = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfosByViewTextInActiveWindow(sConnectionId, "butto");
>>>>>>> upstream/master
            assertEquals(9, buttons.size());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testFindAccessibilityNodeInfoByViewText: "
                        + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testFindAccessibilityNodeInfoByViewTextContentDescription() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
<<<<<<< HEAD
            // find a view by text
            List<AccessibilityNodeInfo> buttons = mUiTestAutomationBridge
                .findAccessibilityNodeInfosByTextInActiveWindow("contentDescription");
=======
            bringUpActivityWithInitalizedAccessbility();

            // find a view by text
            List<AccessibilityNodeInfo> buttons = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfosByViewTextInActiveWindow(sConnectionId,
                        "contentDescription");
>>>>>>> upstream/master
            assertEquals(1, buttons.size());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testFindAccessibilityNodeInfoByViewTextContentDescription: "
                        + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testTraverseAllViews() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // make list of expected nodes
            List<String> classNameAndTextList = new ArrayList<String>();
            classNameAndTextList.add("android.widget.LinearLayout");
            classNameAndTextList.add("android.widget.LinearLayout");
            classNameAndTextList.add("android.widget.LinearLayout");
            classNameAndTextList.add("android.widget.LinearLayout");
            classNameAndTextList.add("android.widget.ButtonButton1");
            classNameAndTextList.add("android.widget.ButtonButton2");
            classNameAndTextList.add("android.widget.ButtonButton3");
            classNameAndTextList.add("android.widget.ButtonButton4");
            classNameAndTextList.add("android.widget.ButtonButton5");
            classNameAndTextList.add("android.widget.ButtonButton6");
            classNameAndTextList.add("android.widget.ButtonButton7");
            classNameAndTextList.add("android.widget.ButtonButton8");
            classNameAndTextList.add("android.widget.ButtonButton9");

<<<<<<< HEAD
            AccessibilityNodeInfo root = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.root);
=======
            AccessibilityNodeInfo root = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.root);
>>>>>>> upstream/master
            assertNotNull("We must find the existing root.", root);

            Queue<AccessibilityNodeInfo> fringe = new LinkedList<AccessibilityNodeInfo>();
            fringe.add(root);

            // do a BFS traversal and check nodes
            while (!fringe.isEmpty()) {
                AccessibilityNodeInfo current = fringe.poll();

                CharSequence className = current.getClassName();
                CharSequence text = current.getText();
                String receivedClassNameAndText = className.toString()
                   + ((text != null) ? text.toString() : "");
                String expectedClassNameAndText = classNameAndTextList.remove(0);

                assertEquals("Did not get the expected node info",
                        expectedClassNameAndText, receivedClassNameAndText);

                final int childCount = current.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    AccessibilityNodeInfo child = current.getChild(i);
                    fringe.add(child);
                }
            }
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testTraverseAllViews: " + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testPerformAccessibilityActionFocus() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view and make sure it is not focused
<<<<<<< HEAD
            AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertFalse(button.isFocused());

            // focus the view
            assertTrue(button.performAction(ACTION_FOCUS));

            // find the view again and make sure it is focused
<<<<<<< HEAD
            button = mUiTestAutomationBridge
                    .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertTrue(button.isFocused());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testPerformAccessibilityActionFocus: " + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testPerformAccessibilityActionClearFocus() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view and make sure it is not focused
<<<<<<< HEAD
            AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertFalse(button.isFocused());

            // focus the view
            assertTrue(button.performAction(ACTION_FOCUS));

            // find the view again and make sure it is focused
<<<<<<< HEAD
            button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertTrue(button.isFocused());

            // unfocus the view
            assertTrue(button.performAction(ACTION_CLEAR_FOCUS));

            // find the view again and make sure it is not focused
<<<<<<< HEAD
            button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertFalse(button.isFocused());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testPerformAccessibilityActionClearFocus: "
                        + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testPerformAccessibilityActionSelect() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view and make sure it is not selected
<<<<<<< HEAD
            AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertFalse(button.isSelected());

            // select the view
            assertTrue(button.performAction(ACTION_SELECT));

            // find the view again and make sure it is selected
<<<<<<< HEAD
            button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertTrue(button.isSelected());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testPerformAccessibilityActionSelect: " + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testPerformAccessibilityActionClearSelection() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view and make sure it is not selected
<<<<<<< HEAD
            AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertFalse(button.isSelected());

            // select the view
            assertTrue(button.performAction(ACTION_SELECT));

            // find the view again and make sure it is selected
<<<<<<< HEAD
            button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertTrue(button.isSelected());

            // unselect the view
            assertTrue(button.performAction(ACTION_CLEAR_SELECTION));

            // find the view again and make sure it is not selected
<<<<<<< HEAD
            button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
=======
            button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            assertFalse(button.isSelected());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testPerformAccessibilityActionClearSelection: "
                        + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testAccessibilityEventGetSource() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view and make sure it is not focused
<<<<<<< HEAD
            final AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
            assertFalse(button.isFocused());

            AccessibilityEvent event = mUiTestAutomationBridge
                    .executeCommandAndWaitForAccessibilityEvent(new Runnable() {
                @Override
                public void run() {
                    // focus the view
                    assertTrue(button.performAction(ACTION_FOCUS));
                }
            },
            new Predicate<AccessibilityEvent>() {
                @Override
                public boolean apply(AccessibilityEvent event) {
                    return (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED
                            && event.getPackageName().equals(getActivity().getPackageName())
                            && event.getText().get(0).equals(button.getText()));
                }
            },
            TIMEOUT_PROPAGATE_ACCESSIBILITY_EVENT_MILLIS);

            // check the last event
            assertNotNull(event);

            // check that last event source
            AccessibilityNodeInfo source = event.getSource();
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
            assertFalse(button.isSelected());

            // focus the view
            assertTrue(button.performAction(ACTION_FOCUS));

            synchronized (this) {
                try {
                    wait(TIMEOUT_ACCESSIBLITY_STATE_INITIALIZED_MILLIS);
                } catch (InterruptedException ie) {
                    /* ignore */
                }
            }

            // check that last event source
            AccessibilityNodeInfo source = mLastAccessibilityEvent.getSource();
>>>>>>> upstream/master
            assertNotNull(source);

            // bounds
            Rect buttonBounds = new Rect();
            button.getBoundsInParent(buttonBounds);
            Rect sourceBounds = new Rect();
            source.getBoundsInParent(sourceBounds);

            assertEquals(buttonBounds.left, sourceBounds.left);
            assertEquals(buttonBounds.right, sourceBounds.right);
            assertEquals(buttonBounds.top, sourceBounds.top);
            assertEquals(buttonBounds.bottom, sourceBounds.bottom);

            // char sequence attributes
            assertEquals(button.getPackageName(), source.getPackageName());
            assertEquals(button.getClassName(), source.getClassName());
            assertEquals(button.getText(), source.getText());
            assertSame(button.getContentDescription(), source.getContentDescription());

            // boolean attributes
            assertSame(button.isFocusable(), source.isFocusable());
            assertSame(button.isClickable(), source.isClickable());
            assertSame(button.isEnabled(), source.isEnabled());
            assertNotSame(button.isFocused(), source.isFocused());
            assertSame(button.isLongClickable(), source.isLongClickable());
            assertSame(button.isPassword(), source.isPassword());
            assertSame(button.isSelected(), source.isSelected());
            assertSame(button.isCheckable(), source.isCheckable());
            assertSame(button.isChecked(), source.isChecked());
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testAccessibilityEventGetSource: " + elapsedTimeMillis + "ms");
            }
        }
    }

    @LargeTest
    public void testObjectContract() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // find a view and make sure it is not focused
<<<<<<< HEAD
            AccessibilityNodeInfo button = mUiTestAutomationBridge
                .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.button5);
            assertNotNull(button);
=======
            AccessibilityNodeInfo button = AccessibilityInteractionClient.getInstance()
                .findAccessibilityNodeInfoByViewIdInActiveWindow(sConnectionId, R.id.button5);
>>>>>>> upstream/master
            AccessibilityNodeInfo parent = button.getParent();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = parent.getChild(i);
                assertNotNull(child);
                if (child.equals(button)) {
                    assertEquals("Equal objects must have same hasCode.", button.hashCode(),
                            child.hashCode());
                    return;
                }
            }
            fail("Parent's children do not have the info whose parent is the parent.");
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testObjectContract: " + elapsedTimeMillis + "ms");
            }
        }
    }

<<<<<<< HEAD
    @LargeTest
    public void testGetRootAccessibilityNodeInfoInActiveWindow() throws Exception {
        final long startTimeMillis = SystemClock.uptimeMillis();
        try {
            // get the root via the designated API
            AccessibilityNodeInfo fetched = mUiTestAutomationBridge
                    .getRootAccessibilityNodeInfoInActiveWindow();
            assertNotNull(fetched);

            // get the root via traversal
            AccessibilityNodeInfo expected = mUiTestAutomationBridge
                    .findAccessibilityNodeInfoByViewIdInActiveWindow(R.id.root);
            while (true) {
                AccessibilityNodeInfo parent = expected.getParent();
                if (parent == null) {
                    break;
                }
                expected = parent;
            }
            assertNotNull(expected);

            assertEquals("The node with id \"root\" should be the root.", expected, fetched);
        } finally {
            if (DEBUG) {
                final long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
                Log.i(LOG_TAG, "testGetRootAccessibilityNodeInfoInActiveWindow: "
                        + elapsedTimeMillis + "ms");
=======
    private void bringUpActivityWithInitalizedAccessbility() {
        mLastAccessibilityEvent = null;
        // bring up the activity
        getActivity();

        final long startTimeMillis = SystemClock.uptimeMillis();
        while (true) {
            if (mLastAccessibilityEvent != null) {
                final int eventType = mLastAccessibilityEvent.getEventType();
                if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    return;
                }
            }
            final long remainingTimeMillis = TIMEOUT_ACCESSIBLITY_STATE_INITIALIZED_MILLIS
                    - (SystemClock.uptimeMillis() - startTimeMillis);
            if (remainingTimeMillis <= 0) {
                return;
            }
            synchronized (this) {
                try {
                    wait(remainingTimeMillis);
                } catch (InterruptedException e) {
                    /* ignore */
                }
            }
        }
    }

    private void ensureConnection() throws Exception {
        if (sConnectionId == View.NO_ID) {
            IEventListener listener = new IEventListener.Stub() {
                public void setConnection(IAccessibilityServiceConnection connection,
                        int connectionId) {
                    sConnectionId = connectionId;
                    if (connection != null) {
                        AccessibilityInteractionClient.getInstance().addConnection(connectionId,
                                connection);
                    } else {
                        AccessibilityInteractionClient.getInstance().removeConnection(connectionId);
                    }
                    synchronized (this) {
                        notifyAll();
                    }
                }

                public void onInterrupt() {}

                public void onAccessibilityEvent(AccessibilityEvent event) {
                    mLastAccessibilityEvent = AccessibilityEvent.obtain(event);
                    synchronized (this) {
                        notifyAll();
                    }
                }
            };

            AccessibilityManager accessibilityManager =
                AccessibilityManager.getInstance(getInstrumentation().getContext());

            synchronized (this) {
                IAccessibilityManager manager = IAccessibilityManager.Stub.asInterface(
                        ServiceManager.getService(Context.ACCESSIBILITY_SERVICE));
                manager.registerEventListener(listener);
                wait(TIMEOUT_PROPAGATE_ACCESSIBLITY_SETTING);
>>>>>>> upstream/master
            }
        }
    }
}
