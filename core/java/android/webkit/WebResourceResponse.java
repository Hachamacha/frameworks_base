/*
 * Copyright (C) 2010 The Android Open Source Project
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

import android.net.http.Headers;

import java.io.InputStream;

/**
<<<<<<< HEAD
 * Encapsulates a resource response. Applications can return an instance of this
 * class from {@link WebViewClient#shouldInterceptRequest} to provide a custom
 * response when the WebView requests a particular resource.
 */
public class WebResourceResponse {
=======
 * A WebResourceResponse is return by
 * {@link WebViewClient#shouldInterceptRequest} and
 * contains the response information for a particular resource.
 */
public class WebResourceResponse {

    private class Loader extends StreamLoader {
        Loader(LoadListener loadListener) {
            super(loadListener);
            mDataStream = mInputStream;
        }
        @Override
        protected boolean setupStreamAndSendStatus() {
            mLoadListener.status(1, 1, mDataStream != null ? 200 : 404, "");
            return true;
        }
        @Override
        protected void buildHeaders(Headers headers) {
            headers.setContentType(mMimeType);
            headers.setContentEncoding(mEncoding);
        }
    }

>>>>>>> upstream/master
    // Accessed by jni, do not rename without modifying the jni code.
    private String mMimeType;
    private String mEncoding;
    private InputStream mInputStream;

    /**
<<<<<<< HEAD
     * Constructs a resource response with the given MIME type, encoding, and
     * input stream. Callers must implement
     * {@link InputStream#read(byte[]) InputStream.read(byte[])} for the input
     * stream.
     *
     * @param mimeType the resource response's MIME type, for example text/html
     * @param encoding the resource response's encoding
     * @param data the input stream that provides the resource response's data
=======
     * Construct a response with the given mime type, encoding, and data.
     * @param mimeType The mime type of the data (i.e. text/html).
     * @param encoding The encoding of the bytes read from data.
     * @param data An InputStream for reading custom data.  The implementation
     *             must implement {@link InputStream#read(byte[])}.
>>>>>>> upstream/master
     */
    public WebResourceResponse(String mimeType, String encoding,
            InputStream data) {
        mMimeType = mimeType;
        mEncoding = encoding;
        mInputStream = data;
    }

    /**
<<<<<<< HEAD
     * Sets the resource response's MIME type, for example text/html.
     *
     * @param mimeType the resource response's MIME type
=======
     * Set the mime type of the response data (i.e. text/html).
     * @param mimeType
>>>>>>> upstream/master
     */
    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    /**
<<<<<<< HEAD
     * Gets the resource response's MIME type.
     *
     * @return the resource response's MIME type
=======
     * @see #setMimeType
>>>>>>> upstream/master
     */
    public String getMimeType() {
        return mMimeType;
    }

    /**
<<<<<<< HEAD
     * Sets the resource response's encoding, for example UTF-8. This is used
     * to decode the data from the input stream.
     *
     * @param encoding the resource response's encoding
=======
     * Set the encoding of the response data (i.e. utf-8).  This will be used to
     * decode the raw bytes from the input stream.
     * @param encoding
>>>>>>> upstream/master
     */
    public void setEncoding(String encoding) {
        mEncoding = encoding;
    }

    /**
<<<<<<< HEAD
     * Gets the resource response's encoding.
     *
     * @return the resource response's encoding
=======
     * @see #setEncoding
>>>>>>> upstream/master
     */
    public String getEncoding() {
        return mEncoding;
    }

    /**
<<<<<<< HEAD
     * Sets the input stream that provides the resource respone's data. Callers
     * must implement {@link InputStream#read(byte[]) InputStream.read(byte[])}.
     *
     * @param data the input stream that provides the resource response's data
=======
     * Set the input stream containing the data for this resource.
     * @param data An InputStream for reading custom data.  The implementation
     *             must implement {@link InputStream#read(byte[])}.
>>>>>>> upstream/master
     */
    public void setData(InputStream data) {
        mInputStream = data;
    }

    /**
<<<<<<< HEAD
     * Gets the input stream that provides the resource respone's data.
     *
     * @return the input stream that provides the resource response's data
=======
     * @see #setData
>>>>>>> upstream/master
     */
    public InputStream getData() {
        return mInputStream;
    }
<<<<<<< HEAD
=======

    StreamLoader loader(LoadListener listener) {
        return new Loader(listener);
    }
>>>>>>> upstream/master
}
