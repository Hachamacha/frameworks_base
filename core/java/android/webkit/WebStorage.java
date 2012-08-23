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

package android.webkit;

<<<<<<< HEAD
import java.util.Map;

/**
 * This class is used to manage the JavaScript storage APIs provided by the
 * {@link WebView}. It manages the Application Cache API, the Web SQL Database
 * API and the HTML5 Web Storage API.
 *
 * The Web SQL Database API provides storage which is private to a given
 * origin, where an origin comprises the host, scheme and port of a URI.
 * Similarly, use of the Application Cache API can be attributed to an origin.
 * This class provides access to the storage use and quotas for these APIs for
 * a given origin. Origins are represented using {@link WebStorage.Origin}.
 */
public class WebStorage {

    /**
     * Encapsulates a callback function which is used to provide a new quota
     * for a JavaScript storage API. See
     * {@link WebChromeClient#onExceededDatabaseQuota} and
     * {@link WebChromeClient#onReachedMaxAppCacheSize}.
     */
    // We primarily want this to allow us to call back the sleeping WebCore
    // thread from outside the WebViewCore class (as the native call is
    // private). It is imperative that the setDatabaseQuota method is
    // executed after a decision to either allow or deny new quota is made,
    // otherwise the WebCore thread will remain asleep.
    public interface QuotaUpdater {
        /**
         * Provides a new quota, specified in bytes.
         *
         * @param newQuota the new quota, in bytes
         */
        public void updateQuota(long newQuota);
    };

    /**
     * This class encapsulates information about the amount of storage
     * currently used by an origin for the JavaScript storage APIs.
     * See {@link WebStorage} for details.
=======
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Functionality for manipulating the webstorage databases.
 */
public final class WebStorage {

    /**
     * Encapsulates a callback function to be executed when a new quota is made
     * available. We primarily want this to allow us to call back the sleeping
     * WebCore thread from outside the WebViewCore class (as the native call
     * is private). It is imperative that this the setDatabaseQuota method is
     * executed once a decision to either allow or deny new quota is made,
     * otherwise the WebCore thread will remain asleep.
     */
    public interface QuotaUpdater {
        public void updateQuota(long newQuota);
    };

    // Log tag
    private static final String TAG = "webstorage";

    // Global instance of a WebStorage
    private static WebStorage sWebStorage;

    // Message ids
    static final int UPDATE = 0;
    static final int SET_QUOTA_ORIGIN = 1;
    static final int DELETE_ORIGIN = 2;
    static final int DELETE_ALL = 3;
    static final int GET_ORIGINS = 4;
    static final int GET_USAGE_ORIGIN = 5;
    static final int GET_QUOTA_ORIGIN = 6;

    // Message ids on the UI thread
    static final int RETURN_ORIGINS = 0;
    static final int RETURN_USAGE_ORIGIN = 1;
    static final int RETURN_QUOTA_ORIGIN = 2;

    private static final String ORIGINS = "origins";
    private static final String ORIGIN = "origin";
    private static final String CALLBACK = "callback";
    private static final String USAGE = "usage";
    private static final String QUOTA = "quota";

    private Map <String, Origin> mOrigins;

    private Handler mHandler = null;
    private Handler mUIHandler = null;

    /**
     * Class containing the HTML5 database quota and usage for an origin.
>>>>>>> upstream/master
     */
    public static class Origin {
        private String mOrigin = null;
        private long mQuota = 0;
        private long mUsage = 0;

<<<<<<< HEAD
        /** @hide */
        protected Origin(String origin, long quota, long usage) {
=======
        private Origin(String origin, long quota, long usage) {
>>>>>>> upstream/master
            mOrigin = origin;
            mQuota = quota;
            mUsage = usage;
        }

<<<<<<< HEAD
        /** @hide */
        protected Origin(String origin, long quota) {
=======
        private Origin(String origin, long quota) {
>>>>>>> upstream/master
            mOrigin = origin;
            mQuota = quota;
        }

<<<<<<< HEAD
        /** @hide */
        protected Origin(String origin) {
=======
        private Origin(String origin) {
>>>>>>> upstream/master
            mOrigin = origin;
        }

        /**
<<<<<<< HEAD
         * Gets the string representation of this origin.
         *
         * @return the string representation of this origin
         */
        // An origin string is created using WebCore::SecurityOrigin::toString().
        // Note that WebCore::SecurityOrigin uses 0 (which is not printed) for
        // the port if the port is the default for the protocol. Eg
        // http://www.google.com and http://www.google.com:80 both record a port
        // of 0 and hence toString() == 'http://www.google.com' for both.
=======
         * An origin string is created using WebCore::SecurityOrigin::toString().
         * Note that WebCore::SecurityOrigin uses 0 (which is not printed) for
         * the port if the port is the default for the protocol. Eg
         * http://www.google.com and http://www.google.com:80 both record a port
         * of 0 and hence toString() == 'http://www.google.com' for both.
         * @return The origin string.
         */
>>>>>>> upstream/master
        public String getOrigin() {
            return mOrigin;
        }

        /**
<<<<<<< HEAD
         * Gets the quota for this origin, for the Web SQL Database API, in
         * bytes. If this origin does not use the Web SQL Database API, this
         * quota will be set to zero.
         *
         * @return the quota, in bytes
=======
         * Returns the quota for this origin's HTML5 database.
         * @return The quota in bytes.
>>>>>>> upstream/master
         */
        public long getQuota() {
            return mQuota;
        }

        /**
<<<<<<< HEAD
         * Gets the total amount of storage currently being used by this origin,
         * for all JavaScript storage APIs, in bytes.
         *
         * @return the total amount of storage, in bytes
=======
         * Returns the usage for this origin's HTML5 database.
         * @return The usage in bytes.
>>>>>>> upstream/master
         */
        public long getUsage() {
            return mUsage;
        }
    }

<<<<<<< HEAD
    /*
     * When calling getOrigins(), getUsageForOrigin() and getQuotaForOrigin(),
     * we need to get the values from WebCore, but we cannot block while doing so
     * as we used to do, as this could result in a full deadlock (other WebCore
     * messages received while we are still blocked here, see http://b/2127737).
     *
     * We have to do everything asynchronously, by providing a callback function.
     * We post a message on the WebCore thread (mHandler) that will get the result
     * from WebCore, and we post it back on the UI thread (using mUIHandler).
=======
    /**
     * @hide
     * Message handler, UI side
     */
    public void createUIHandler() {
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case RETURN_ORIGINS: {
                            Map values = (Map) msg.obj;
                            Map origins = (Map) values.get(ORIGINS);
                            ValueCallback<Map> callback = (ValueCallback<Map>) values.get(CALLBACK);
                            callback.onReceiveValue(origins);
                            } break;

                        case RETURN_USAGE_ORIGIN: {
                            Map values = (Map) msg.obj;
                            ValueCallback<Long> callback = (ValueCallback<Long>) values.get(CALLBACK);
                            callback.onReceiveValue((Long)values.get(USAGE));
                            } break;

                        case RETURN_QUOTA_ORIGIN: {
                            Map values = (Map) msg.obj;
                            ValueCallback<Long> callback = (ValueCallback<Long>) values.get(CALLBACK);
                            callback.onReceiveValue((Long)values.get(QUOTA));
                            } break;
                    }
                }
            };
        }
    }

    /**
     * @hide
     * Message handler, webcore side
     */
    public synchronized void createHandler() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case SET_QUOTA_ORIGIN: {
                            Origin website = (Origin) msg.obj;
                            nativeSetQuotaForOrigin(website.getOrigin(),
                                                    website.getQuota());
                            } break;

                        case DELETE_ORIGIN: {
                            Origin website = (Origin) msg.obj;
                            nativeDeleteOrigin(website.getOrigin());
                            } break;

                        case DELETE_ALL:
                            nativeDeleteAllData();
                            break;

                        case GET_ORIGINS: {
                            syncValues();
                            ValueCallback callback = (ValueCallback) msg.obj;
                            Map origins = new HashMap(mOrigins);
                            Map values = new HashMap<String, Object>();
                            values.put(CALLBACK, callback);
                            values.put(ORIGINS, origins);
                            postUIMessage(Message.obtain(null, RETURN_ORIGINS, values));
                            } break;

                        case GET_USAGE_ORIGIN: {
                            syncValues();
                            Map values = (Map) msg.obj;
                            String origin = (String) values.get(ORIGIN);
                            ValueCallback callback = (ValueCallback) values.get(CALLBACK);
                            Origin website = mOrigins.get(origin);
                            Map retValues = new HashMap<String, Object>();
                            retValues.put(CALLBACK, callback);
                            if (website != null) {
                                long usage = website.getUsage();
                                retValues.put(USAGE, new Long(usage));
                            }
                            postUIMessage(Message.obtain(null, RETURN_USAGE_ORIGIN, retValues));
                            } break;

                        case GET_QUOTA_ORIGIN: {
                            syncValues();
                            Map values = (Map) msg.obj;
                            String origin = (String) values.get(ORIGIN);
                            ValueCallback callback = (ValueCallback) values.get(CALLBACK);
                            Origin website = mOrigins.get(origin);
                            Map retValues = new HashMap<String, Object>();
                            retValues.put(CALLBACK, callback);
                            if (website != null) {
                                long quota = website.getQuota();
                                retValues.put(QUOTA, new Long(quota));
                            }
                            postUIMessage(Message.obtain(null, RETURN_QUOTA_ORIGIN, retValues));
                            } break;

                        case UPDATE:
                            syncValues();
                            break;
                    }
                }
            };
        }
    }

    /*
     * When calling getOrigins(), getUsageForOrigin() and getQuotaForOrigin(),
     * we need to get the values from webcore, but we cannot block while doing so
     * as we used to do, as this could result in a full deadlock (other webcore
     * messages received while we are still blocked here, see http://b/2127737).
     *
     * We have to do everything asynchronously, by providing a callback function.
     * We post a message on the webcore thread (mHandler) that will get the result
     * from webcore, and we post it back on the UI thread (using mUIHandler).
>>>>>>> upstream/master
     * We can then use the callback function to return the value.
     */

    /**
<<<<<<< HEAD
     * Gets the origins currently using either the Application Cache or Web SQL
     * Database APIs. This method operates asynchronously, with the result
     * being provided via a {@link ValueCallback}. The origins are provided as
     * a map, of type {@code Map<String, WebStorage.Origin>}, from the string
     * representation of the origin to a {@link WebStorage.Origin} object.
     */
    public void getOrigins(ValueCallback<Map> callback) {
        // Must be a no-op for backward compatibility: see the hidden constructor for reason.
    }

    /**
     * Gets the amount of storage currently being used by both the Application
     * Cache and Web SQL Database APIs by the given origin. The amount is given
     * in bytes and the origin is specified using its string representation.
     * This method operates asynchronously, with the result being provided via
     * a {@link ValueCallback}.
     */
    public void getUsageForOrigin(String origin, ValueCallback<Long> callback) {
        // Must be a no-op for backward compatibility: see the hidden constructor for reason.
    }

    /**
     * Gets the storage quota for the Web SQL Database API for the given origin.
     * The quota is given in bytes and the origin is specified using its string
     * representation. This method operates asynchronously, with the result
     * being provided via a {@link ValueCallback}. Note that a quota is not
     * enforced on a per-origin basis for the Application Cache API.
     */
    public void getQuotaForOrigin(String origin, ValueCallback<Long> callback) {
        // Must be a no-op for backward compatibility: see the hidden constructor for reason.
    }

    /**
     * Sets the storage quota for the Web SQL Database API for the given origin.
     * The quota is specified in bytes and the origin is specified using its string
     * representation. Note that a quota is not enforced on a per-origin basis
     * for the Application Cache API.
     */
    public void setQuotaForOrigin(String origin, long quota) {
        // Must be a no-op for backward compatibility: see the hidden constructor for reason.
    }

    /**
     * Clears the storage currently being used by both the Application Cache and
     * Web SQL Database APIs by the given origin. The origin is specified using
     * its string representation.
     */
    public void deleteOrigin(String origin) {
        // Must be a no-op for backward compatibility: see the hidden constructor for reason.
    }

    /**
     * Clears all storage currently being used by the JavaScript storage APIs.
     * This includes the Application Cache, Web SQL Database and the HTML5 Web
     * Storage APIs.
     */
    public void deleteAllData() {
        // Must be a no-op for backward compatibility: see the hidden constructor for reason.
    }

    /**
     * Gets the singleton instance of this class.
     *
     * @return the singleton {@link WebStorage} instance
     */
    public static WebStorage getInstance() {
      return WebViewFactory.getProvider().getWebStorage();
    }

    /**
     * This class should not be instantiated directly, applications must only use
     * {@link #getInstance()} to obtain the instance.
     * Note this constructor was erroneously public and published in SDK levels prior to 16, but
     * applications using it would receive a non-functional instance of this class (there was no
     * way to call createHandler() and createUIHandler(), so it would not work).
     * @hide
     */
    public WebStorage() {}
=======
     * Returns a list of origins having a database. The Map is of type
     * Map<String, Origin>.
     */
    public void getOrigins(ValueCallback<Map> callback) {
        if (callback != null) {
            if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
                syncValues();
                callback.onReceiveValue(mOrigins);
            } else {
                postMessage(Message.obtain(null, GET_ORIGINS, callback));
            }
        }
    }

    /**
     * Returns a list of origins having a database
     * should only be called from WebViewCore.
     */
    Collection<Origin> getOriginsSync() {
        if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
            update();
            return mOrigins.values();
        }
        return null;
    }

    /**
     * Returns the use for a given origin
     */
    public void getUsageForOrigin(String origin, ValueCallback<Long> callback) {
        if (callback == null) {
            return;
        }
        if (origin == null) {
            callback.onReceiveValue(null);
            return;
        }
        if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
            syncValues();
            Origin website = mOrigins.get(origin);
            callback.onReceiveValue(new Long(website.getUsage()));
        } else {
            HashMap values = new HashMap<String, Object>();
            values.put(ORIGIN, origin);
            values.put(CALLBACK, callback);
            postMessage(Message.obtain(null, GET_USAGE_ORIGIN, values));
        }
    }

    /**
     * Returns the quota for a given origin
     */
    public void getQuotaForOrigin(String origin, ValueCallback<Long> callback) {
        if (callback == null) {
            return;
        }
        if (origin == null) {
            callback.onReceiveValue(null);
            return;
        }
        if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
            syncValues();
            Origin website = mOrigins.get(origin);
            callback.onReceiveValue(new Long(website.getUsage()));
        } else {
            HashMap values = new HashMap<String, Object>();
            values.put(ORIGIN, origin);
            values.put(CALLBACK, callback);
            postMessage(Message.obtain(null, GET_QUOTA_ORIGIN, values));
        }
    }

    /**
     * Set the quota for a given origin
     */
    public void setQuotaForOrigin(String origin, long quota) {
        if (origin != null) {
            if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
                nativeSetQuotaForOrigin(origin, quota);
            } else {
                postMessage(Message.obtain(null, SET_QUOTA_ORIGIN,
                    new Origin(origin, quota)));
            }
        }
    }

    /**
     * Delete a given origin
     */
    public void deleteOrigin(String origin) {
        if (origin != null) {
            if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
                nativeDeleteOrigin(origin);
            } else {
                postMessage(Message.obtain(null, DELETE_ORIGIN,
                    new Origin(origin)));
            }
        }
    }

    /**
     * Delete all databases
     */
    public void deleteAllData() {
        if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
            nativeDeleteAllData();
        } else {
            postMessage(Message.obtain(null, DELETE_ALL));
        }
    }

    /**
     * Sets the maximum size of the ApplicationCache.
     * This should only ever be called on the WebKit thread.
     * @hide Pending API council approval
     */
    public void setAppCacheMaximumSize(long size) {
        nativeSetAppCacheMaximumSize(size);
    }

    /**
     * Utility function to send a message to our handler
     */
    private synchronized void postMessage(Message msg) {
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Utility function to send a message to the handler on the UI thread
     */
    private void postUIMessage(Message msg) {
        if (mUIHandler != null) {
            mUIHandler.sendMessage(msg);
        }
    }

    /**
     * Get the global instance of WebStorage.
     * @return A single instance of WebStorage.
     */
    public static WebStorage getInstance() {
      if (sWebStorage == null) {
          sWebStorage = new WebStorage();
      }
      return sWebStorage;
    }

    /**
     * @hide
     * Post a Sync request
     */
    public void update() {
        if (WebViewCore.THREAD_NAME.equals(Thread.currentThread().getName())) {
            syncValues();
        } else {
            postMessage(Message.obtain(null, UPDATE));
        }
    }

    /**
     * Run on the webcore thread
     * set the local values with the current ones
     */
    private void syncValues() {
        Set<String> tmp = nativeGetOrigins();
        mOrigins = new HashMap<String, Origin>();
        for (String origin : tmp) {
            Origin website = new Origin(origin,
                                 nativeGetQuotaForOrigin(origin),
                                 nativeGetUsageForOrigin(origin));
            mOrigins.put(origin, website);
        }
    }

    // Native functions
    private static native Set nativeGetOrigins();
    private static native long nativeGetUsageForOrigin(String origin);
    private static native long nativeGetQuotaForOrigin(String origin);
    private static native void nativeSetQuotaForOrigin(String origin, long quota);
    private static native void nativeDeleteOrigin(String origin);
    private static native void nativeDeleteAllData();
    private static native void nativeSetAppCacheMaximumSize(long size);
>>>>>>> upstream/master
}
