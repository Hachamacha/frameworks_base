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

package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.CancellationSignal;
import android.os.DeadObjectException;
import android.os.ICancellationSignal;
=======
>>>>>>> upstream/master
import android.os.RemoteException;
import android.os.ParcelFileDescriptor;
import android.content.res.AssetFileDescriptor;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * The public interface object used to interact with a {@link ContentProvider}. This is obtained by
 * calling {@link ContentResolver#acquireContentProviderClient}. This object must be released
 * using {@link #release} in order to indicate to the system that the {@link ContentProvider} is
 * no longer needed and can be killed to free up resources.
<<<<<<< HEAD
 *
 * <p>Note that you should generally create a new ContentProviderClient instance
 * for each thread that will be performing operations.  Unlike
 * {@link ContentResolver}, the methods here such as {@link #query} and
 * {@link #openFile} are not thread safe -- you must not call
 * {@link #release()} on the ContentProviderClient those calls are made from
 * until you are finished with the data they have returned.
=======
>>>>>>> upstream/master
 */
public class ContentProviderClient {
    private final IContentProvider mContentProvider;
    private final ContentResolver mContentResolver;
<<<<<<< HEAD
    private final boolean mStable;
    private boolean mReleased;
=======
>>>>>>> upstream/master

    /**
     * @hide
     */
<<<<<<< HEAD
    ContentProviderClient(ContentResolver contentResolver,
            IContentProvider contentProvider, boolean stable) {
        mContentProvider = contentProvider;
        mContentResolver = contentResolver;
        mStable = stable;
=======
    ContentProviderClient(ContentResolver contentResolver, IContentProvider contentProvider) {
        mContentProvider = contentProvider;
        mContentResolver = contentResolver;
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#query ContentProvider.query} */
    public Cursor query(Uri url, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) throws RemoteException {
<<<<<<< HEAD
        try {
            return query(url, projection, selection,  selectionArgs, sortOrder, null);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
    }

    /** See {@link ContentProvider#query ContentProvider.query} */
    public Cursor query(Uri url, String[] projection, String selection,
            String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal)
                    throws RemoteException {
        ICancellationSignal remoteCancellationSignal = null;
        if (cancellationSignal != null) {
            remoteCancellationSignal = mContentProvider.createCancellationSignal();
            cancellationSignal.setRemote(remoteCancellationSignal);
        }
        try {
            return mContentProvider.query(url, projection, selection,  selectionArgs, sortOrder,
                    remoteCancellationSignal);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.query(url, projection, selection,  selectionArgs, sortOrder);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#getType ContentProvider.getType} */
    public String getType(Uri url) throws RemoteException {
<<<<<<< HEAD
        try {
            return mContentProvider.getType(url);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.getType(url);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#getStreamTypes ContentProvider.getStreamTypes} */
    public String[] getStreamTypes(Uri url, String mimeTypeFilter) throws RemoteException {
<<<<<<< HEAD
        try {
            return mContentProvider.getStreamTypes(url, mimeTypeFilter);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.getStreamTypes(url, mimeTypeFilter);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#insert ContentProvider.insert} */
    public Uri insert(Uri url, ContentValues initialValues)
            throws RemoteException {
<<<<<<< HEAD
        try {
            return mContentProvider.insert(url, initialValues);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.insert(url, initialValues);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#bulkInsert ContentProvider.bulkInsert} */
    public int bulkInsert(Uri url, ContentValues[] initialValues) throws RemoteException {
<<<<<<< HEAD
        try {
            return mContentProvider.bulkInsert(url, initialValues);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.bulkInsert(url, initialValues);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#delete ContentProvider.delete} */
    public int delete(Uri url, String selection, String[] selectionArgs)
            throws RemoteException {
<<<<<<< HEAD
        try {
            return mContentProvider.delete(url, selection, selectionArgs);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.delete(url, selection, selectionArgs);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#update ContentProvider.update} */
    public int update(Uri url, ContentValues values, String selection,
            String[] selectionArgs) throws RemoteException {
<<<<<<< HEAD
        try {
            return mContentProvider.update(url, values, selection, selectionArgs);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.update(url, values, selection, selectionArgs);
>>>>>>> upstream/master
    }

    /**
     * See {@link ContentProvider#openFile ContentProvider.openFile}.  Note that
     * this <em>does not</em>
     * take care of non-content: URIs such as file:.  It is strongly recommended
     * you use the {@link ContentResolver#openFileDescriptor
     * ContentResolver.openFileDescriptor} API instead.
     */
    public ParcelFileDescriptor openFile(Uri url, String mode)
            throws RemoteException, FileNotFoundException {
<<<<<<< HEAD
        try {
            return mContentProvider.openFile(url, mode);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.openFile(url, mode);
>>>>>>> upstream/master
    }

    /**
     * See {@link ContentProvider#openAssetFile ContentProvider.openAssetFile}.
     * Note that this <em>does not</em>
     * take care of non-content: URIs such as file:.  It is strongly recommended
     * you use the {@link ContentResolver#openAssetFileDescriptor
     * ContentResolver.openAssetFileDescriptor} API instead.
     */
    public AssetFileDescriptor openAssetFile(Uri url, String mode)
            throws RemoteException, FileNotFoundException {
<<<<<<< HEAD
        try {
            return mContentProvider.openAssetFile(url, mode);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.openAssetFile(url, mode);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#openTypedAssetFile ContentProvider.openTypedAssetFile} */
    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri,
            String mimeType, Bundle opts)
            throws RemoteException, FileNotFoundException {
<<<<<<< HEAD
        try {
            return mContentProvider.openTypedAssetFile(uri, mimeType, opts);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.openTypedAssetFile(uri, mimeType, opts);
>>>>>>> upstream/master
    }

    /** See {@link ContentProvider#applyBatch ContentProvider.applyBatch} */
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws RemoteException, OperationApplicationException {
<<<<<<< HEAD
        try {
            return mContentProvider.applyBatch(operations);
        } catch (DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        }
=======
        return mContentProvider.applyBatch(operations);
>>>>>>> upstream/master
    }

    /**
     * Call this to indicate to the system that the associated {@link ContentProvider} is no
     * longer needed by this {@link ContentProviderClient}.
     * @return true if this was release, false if it was already released
     */
    public boolean release() {
<<<<<<< HEAD
        synchronized (this) {
            if (mReleased) {
                throw new IllegalStateException("Already released");
            }
            mReleased = true;
            if (mStable) {
                return mContentResolver.releaseProvider(mContentProvider);
            } else {
                return mContentResolver.releaseUnstableProvider(mContentProvider);
            }
        }
=======
        return mContentResolver.releaseProvider(mContentProvider);
>>>>>>> upstream/master
    }

    /**
     * Get a reference to the {@link ContentProvider} that is associated with this
     * client. If the {@link ContentProvider} is running in a different process then
     * null will be returned. This can be used if you know you are running in the same
     * process as a provider, and want to get direct access to its implementation details.
     *
     * @return If the associated {@link ContentProvider} is local, returns it.
     * Otherwise returns null.
     */
    public ContentProvider getLocalContentProvider() {
        return ContentProvider.coerceToLocalContentProvider(mContentProvider);
    }
}
