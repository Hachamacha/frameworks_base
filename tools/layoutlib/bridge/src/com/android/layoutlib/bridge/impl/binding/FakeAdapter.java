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

package com.android.layoutlib.bridge.impl.binding;

import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.DataBindingItem;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.ResourceReference;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fake adapter to do fake data binding in {@link AdapterView} objects for {@link ListAdapter}
 * and {@link SpinnerAdapter}.
 *
 */
public class FakeAdapter extends BaseAdapter implements ListAdapter, SpinnerAdapter {

    // don't use a set because the order is important.
    private final List<ResourceReference> mTypes = new ArrayList<ResourceReference>();

    public FakeAdapter(ResourceReference adapterRef, AdapterBinding binding,
            IProjectCallback callback) {
        super(adapterRef, binding, callback);

        final int repeatCount = getBinding().getRepeatCount();
        final int itemCount = getBinding().getItemCount();

        // Need an array to count for each type.
        // This is likely too big, but is the max it can be.
        int[] typeCount = new int[itemCount];

        // We put several repeating sets.
        for (int r = 0 ; r < repeatCount ; r++) {
            // loop on the type of list items, and add however many for each type.
            for (DataBindingItem dataBindingItem : getBinding()) {
                ResourceReference viewRef = dataBindingItem.getViewReference();
                int typeIndex = mTypes.indexOf(viewRef);
                if (typeIndex == -1) {
                    typeIndex = mTypes.size();
                    mTypes.add(viewRef);
                }

                int count = dataBindingItem.getCount();

                int index = typeCount[typeIndex];
                typeCount[typeIndex] += count;

                for (int k = 0 ; k < count ; k++) {
                    mItems.add(new AdapterItem(dataBindingItem, typeIndex, mItems.size(), index++));
                }
            }
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public boolean isEnabled(int position) {
        return true;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public int getCount() {
        return mItems.size();
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public Object getItem(int position) {
        return mItems.get(position);
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public long getItemId(int position) {
        return position;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public View getView(int position, View convertView, ViewGroup parent) {
        // we don't care about recycling here because we never scroll.
        AdapterItem item = mItems.get(position);
        return getView(item, null /*parentGroup*/, convertView, parent);
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public int getViewTypeCount() {
        return mTypes.size();
    }

    // ---- SpinnerAdapter

<<<<<<< HEAD
    @Override
=======
>>>>>>> upstream/master
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // pass
        return null;
    }
}
