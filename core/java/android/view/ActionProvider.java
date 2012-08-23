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

package android.view;

import android.content.Context;
<<<<<<< HEAD
import android.util.Log;

/**
 * An ActionProvider defines rich menu interaction in a single component.
 * ActionProvider can generate action views for use in the action bar,
 * dynamically populate submenus of a MenuItem, and handle default menu
 * item invocations.
 *
 * <p>An ActionProvider can be optionally specified for a {@link MenuItem} and will be
 * responsible for creating the action view that appears in the {@link android.app.ActionBar}
 * in place of a simple button in the bar. When the menu item is presented in a way that
 * does not allow custom action views, (e.g. in an overflow menu,) the ActionProvider
 * can perform a default action.</p>
 *
 * <p>There are two ways to use an action provider:
 * <ul>
 * <li>
 * Set the action provider on a {@link MenuItem} directly by calling
 * {@link MenuItem#setActionProvider(ActionProvider)}.
 * </li>
 * <li>
 * Declare the action provider in an XML menu resource. For example:
=======

/**
 * This class is a mediator for accomplishing a given task, for example sharing a file.
 * It is responsible for creating a view that performs an action that accomplishes the task.
 * This class also implements other functions such a performing a default action.
 * <p>
 * An ActionProvider can be optionally specified for a {@link MenuItem} and in such a
 * case it will be responsible for creating the action view that appears in the
 * {@link android.app.ActionBar} as a substitute for the menu item when the item is
 * displayed as an action item. Also the provider is responsible for performing a
 * default action if a menu item placed on the overflow menu of the ActionBar is
 * selected and none of the menu item callbacks has handled the selection. For this
 * case the provider can also optionally provide a sub-menu for accomplishing the
 * task at hand.
 * </p>
 * <p>
 * There are two ways for using an action provider for creating and handling of action views:
 * <ul>
 * <li>
 * Setting the action provider on a {@link MenuItem} directly by calling
 * {@link MenuItem#setActionProvider(ActionProvider)}.
 * </li>
 * <li>
 * Declaring the action provider in the menu XML resource. For example:
>>>>>>> upstream/master
 * <pre>
 * <code>
 *   &lt;item android:id="@+id/my_menu_item"
 *     android:title="Title"
 *     android:icon="@drawable/my_menu_item_icon"
 *     android:showAsAction="ifRoom"
 *     android:actionProviderClass="foo.bar.SomeActionProvider" /&gt;
 * </code>
 * </pre>
 * </li>
 * </ul>
 * </p>
 *
 * @see MenuItem#setActionProvider(ActionProvider)
 * @see MenuItem#getActionProvider()
 */
public abstract class ActionProvider {
<<<<<<< HEAD
    private static final String TAG = "ActionProvider";
    private SubUiVisibilityListener mSubUiVisibilityListener;
    private VisibilityListener mVisibilityListener;

    /**
     * Creates a new instance. ActionProvider classes should always implement a
     * constructor that takes a single Context parameter for inflating from menu XML.
=======
    private SubUiVisibilityListener mSubUiVisibilityListener;

    /**
     * Creates a new instance.
>>>>>>> upstream/master
     *
     * @param context Context for accessing resources.
     */
    public ActionProvider(Context context) {
    }

    /**
<<<<<<< HEAD
     * Factory method called by the Android framework to create new action views.
     *
     * <p>This method has been deprecated in favor of {@link #onCreateActionView(MenuItem)}.
     * Newer apps that wish to support platform versions prior to API 16 should also
     * implement this method to return a valid action view.</p>
     *
     * @return A new action view.
     *
     * @deprecated use {@link #onCreateActionView(MenuItem)}
=======
     * Factory method for creating new action views.
     *
     * @return A new action view.
>>>>>>> upstream/master
     */
    public abstract View onCreateActionView();

    /**
<<<<<<< HEAD
     * Factory method called by the Android framework to create new action views.
     * This method returns a new action view for the given MenuItem.
     *
     * <p>If your ActionProvider implementation overrides the deprecated no-argument overload
     * {@link #onCreateActionView()}, overriding this method for devices running API 16 or later
     * is recommended but optional. The default implementation calls {@link #onCreateActionView()}
     * for compatibility with applications written for older platform versions.</p>
     *
     * @param forItem MenuItem to create the action view for
     * @return the new action view
     */
    public View onCreateActionView(MenuItem forItem) {
        return onCreateActionView();
    }

    /**
     * The result of this method determines whether or not {@link #isVisible()} will be used
     * by the {@link MenuItem} this ActionProvider is bound to help determine its visibility.
     *
     * @return true if this ActionProvider overrides the visibility of the MenuItem
     *         it is bound to, false otherwise. The default implementation returns false.
     * @see #isVisible()
     */
    public boolean overridesItemVisibility() {
        return false;
    }

    /**
     * If {@link #overridesItemVisibility()} returns true, the return value of this method
     * will help determine the visibility of the {@link MenuItem} this ActionProvider is bound to.
     *
     * <p>If the MenuItem's visibility is explicitly set to false by the application,
     * the MenuItem will not be shown, even if this method returns true.</p>
     *
     * @return true if the MenuItem this ActionProvider is bound to is visible, false if
     *         it is invisible. The default implementation returns true.
     */
    public boolean isVisible() {
        return true;
    }

    /**
     * If this ActionProvider is associated with an item in a menu,
     * refresh the visibility of the item based on {@link #overridesItemVisibility()} and
     * {@link #isVisible()}. If {@link #overridesItemVisibility()} returns false, this call
     * will have no effect.
     */
    public void refreshVisibility() {
        if (mVisibilityListener != null && overridesItemVisibility()) {
            mVisibilityListener.onActionProviderVisibilityChanged(isVisible());
        }
    }

    /**
=======
>>>>>>> upstream/master
     * Performs an optional default action.
     * <p>
     * For the case of an action provider placed in a menu item not shown as an action this
     * method is invoked if previous callbacks for processing menu selection has handled
     * the event.
     * </p>
     * <p>
     * A menu item selection is processed in the following order:
     * <ul>
     * <li>
     * Receiving a call to {@link MenuItem.OnMenuItemClickListener#onMenuItemClick
     *  MenuItem.OnMenuItemClickListener.onMenuItemClick}.
     * </li>
     * <li>
     * Receiving a call to {@link android.app.Activity#onOptionsItemSelected(MenuItem)
     *  Activity.onOptionsItemSelected(MenuItem)}
     * </li>
     * <li>
     * Receiving a call to {@link android.app.Fragment#onOptionsItemSelected(MenuItem)
     *  Fragment.onOptionsItemSelected(MenuItem)}
     * </li>
     * <li>
     * Launching the {@link android.content.Intent} set via
     * {@link MenuItem#setIntent(android.content.Intent) MenuItem.setIntent(android.content.Intent)}
     * </li>
     * <li>
     * Invoking this method.
     * </li>
     * </ul>
     * </p>
     * <p>
     * The default implementation does not perform any action and returns false.
     * </p>
     */
    public boolean onPerformDefaultAction() {
        return false;
    }

    /**
     * Determines if this ActionProvider has a submenu associated with it.
     *
     * <p>Associated submenus will be shown when an action view is not. This
     * provider instance will receive a call to {@link #onPrepareSubMenu(SubMenu)}
     * after the call to {@link #onPerformDefaultAction()} and before a submenu is
     * displayed to the user.
     *
     * @return true if the item backed by this provider should have an associated submenu
     */
    public boolean hasSubMenu() {
        return false;
    }

    /**
     * Called to prepare an associated submenu for the menu item backed by this ActionProvider.
     *
     * <p>if {@link #hasSubMenu()} returns true, this method will be called when the
     * menu item is selected to prepare the submenu for presentation to the user. Apps
     * may use this to create or alter submenu content right before display.
     *
     * @param subMenu Submenu that will be displayed
     */
    public void onPrepareSubMenu(SubMenu subMenu) {
    }

    /**
     * Notify the system that the visibility of an action view's sub-UI such as
     * an anchored popup has changed. This will affect how other system
     * visibility notifications occur.
     *
     * @hide Pending future API approval
     */
    public void subUiVisibilityChanged(boolean isVisible) {
        if (mSubUiVisibilityListener != null) {
            mSubUiVisibilityListener.onSubUiVisibilityChanged(isVisible);
        }
    }

    /**
     * @hide Internal use only
     */
    public void setSubUiVisibilityListener(SubUiVisibilityListener listener) {
        mSubUiVisibilityListener = listener;
    }

    /**
<<<<<<< HEAD
     * Set a listener to be notified when this ActionProvider's overridden visibility changes.
     * This should only be used by MenuItem implementations.
     *
     * @param listener listener to set
     */
    public void setVisibilityListener(VisibilityListener listener) {
        if (mVisibilityListener != null) {
            Log.w(TAG, "setVisibilityListener: Setting a new ActionProvider.VisibilityListener " +
                    "when one is already set. Are you reusing this " + getClass().getSimpleName() +
                    " instance while it is still in use somewhere else?");
        }
        mVisibilityListener = listener;
    }

    /**
=======
>>>>>>> upstream/master
     * @hide Internal use only
     */
    public interface SubUiVisibilityListener {
        public void onSubUiVisibilityChanged(boolean isVisible);
    }
<<<<<<< HEAD

    /**
     * Listens to changes in visibility as reported by {@link ActionProvider#refreshVisibility()}.
     *
     * @see ActionProvider#overridesItemVisibility()
     * @see ActionProvider#isVisible()
     */
    public interface VisibilityListener {
        public void onActionProviderVisibilityChanged(boolean isVisible);
    }
=======
>>>>>>> upstream/master
}
