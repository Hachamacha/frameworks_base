/*
 * Copyright (C) 2007 The Android Open Source Project
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

package android.database;

/**
<<<<<<< HEAD
 * A specialization of {@link Observable} for {@link DataSetObserver}
 * that provides methods for sending notifications to a list of
 * {@link DataSetObserver} objects.
 */
public class DataSetObservable extends Observable<DataSetObserver> {
    /**
     * Invokes {@link DataSetObserver#onChanged} on each observer.
     * Called when the contents of the data set have changed.  The recipient
     * will obtain the new contents the next time it queries the data set.
=======
 * A specialization of Observable for DataSetObserver that provides methods for
 * invoking the various callback methods of DataSetObserver.
 */
public class DataSetObservable extends Observable<DataSetObserver> {
    /**
     * Invokes onChanged on each observer. Called when the data set being observed has
     * changed, and which when read contains the new state of the data.
>>>>>>> upstream/master
     */
    public void notifyChanged() {
        synchronized(mObservers) {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    /**
<<<<<<< HEAD
     * Invokes {@link DataSetObserver#onInvalidated} on each observer.
     * Called when the data set is no longer valid and cannot be queried again,
     * such as when the data set has been closed.
=======
     * Invokes onInvalidated on each observer. Called when the data set being monitored
     * has changed such that it is no longer valid.
>>>>>>> upstream/master
     */
    public void notifyInvalidated() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onInvalidated();
            }
        }
    }
}
