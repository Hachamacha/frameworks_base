/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.speech.tts;

import android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher;
import android.util.Log;

/**
 * Speech synthesis request that plays the audio as it is received.
 */
class PlaybackSynthesisCallback extends AbstractSynthesisCallback {

    private static final String TAG = "PlaybackSynthesisRequest";
    private static final boolean DBG = false;

    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;

    /**
     * Audio stream type. Must be one of the STREAM_ contants defined in
     * {@link android.media.AudioManager}.
     */
    private final int mStreamType;

    /**
     * Volume, in the range [0.0f, 1.0f]. The default value is
     * {@link TextToSpeech.Engine#DEFAULT_VOLUME} (1.0f).
     */
    private final float mVolume;

    /**
     * Left/right position of the audio, in the range [-1.0f, 1.0f].
     * The default value is {@link TextToSpeech.Engine#DEFAULT_PAN} (0.0f).
     */
    private final float mPan;

    /**
<<<<<<< HEAD
     * Guards {@link #mAudioTrackHandler}, {@link #mItem} and {@link #mStopped}.
=======
     * Guards {@link #mAudioTrackHandler}, {@link #mToken} and {@link #mStopped}.
>>>>>>> upstream/master
     */
    private final Object mStateLock = new Object();

    // Handler associated with a thread that plays back audio requests.
    private final AudioPlaybackHandler mAudioTrackHandler;
    // A request "token", which will be non null after start() has been called.
<<<<<<< HEAD
    private SynthesisPlaybackQueueItem mItem = null;
    // Whether this request has been stopped. This is useful for keeping
    // track whether stop() has been called before start(). In all other cases,
    // a non-null value of mItem will provide the same information.
=======
    private SynthesisMessageParams mToken = null;
    // Whether this request has been stopped. This is useful for keeping
    // track whether stop() has been called before start(). In all other cases,
    // a non-null value of mToken will provide the same information.
>>>>>>> upstream/master
    private boolean mStopped = false;

    private volatile boolean mDone = false;

    private final UtteranceProgressDispatcher mDispatcher;
<<<<<<< HEAD
    private final Object mCallerIdentity;
=======
    private final String mCallingApp;
>>>>>>> upstream/master
    private final EventLogger mLogger;

    PlaybackSynthesisCallback(int streamType, float volume, float pan,
            AudioPlaybackHandler audioTrackHandler, UtteranceProgressDispatcher dispatcher,
<<<<<<< HEAD
            Object callerIdentity, EventLogger logger) {
=======
            String callingApp, EventLogger logger) {
>>>>>>> upstream/master
        mStreamType = streamType;
        mVolume = volume;
        mPan = pan;
        mAudioTrackHandler = audioTrackHandler;
        mDispatcher = dispatcher;
<<<<<<< HEAD
        mCallerIdentity = callerIdentity;
=======
        mCallingApp = callingApp;
>>>>>>> upstream/master
        mLogger = logger;
    }

    @Override
    void stop() {
        stopImpl(false);
    }

    void stopImpl(boolean wasError) {
        if (DBG) Log.d(TAG, "stop()");

        // Note that mLogger.mError might be true too at this point.
        mLogger.onStopped();

<<<<<<< HEAD
        SynthesisPlaybackQueueItem item;
=======
        SynthesisMessageParams token;
>>>>>>> upstream/master
        synchronized (mStateLock) {
            if (mStopped) {
                Log.w(TAG, "stop() called twice");
                return;
            }

<<<<<<< HEAD
            item = mItem;
            mStopped = true;
        }

        if (item != null) {
            // This might result in the synthesis thread being woken up, at which
            // point it will write an additional buffer to the item - but we
            // won't worry about that because the audio playback queue will be cleared
            // soon after (see SynthHandler#stop(String).
            item.stop(wasError);
=======
            token = mToken;
            mStopped = true;
        }

        if (token != null) {
            // This might result in the synthesis thread being woken up, at which
            // point it will write an additional buffer to the token - but we
            // won't worry about that because the audio playback queue will be cleared
            // soon after (see SynthHandler#stop(String).
            token.setIsError(wasError);
            token.clearBuffers();
            if (wasError) {
                // Also clean up the audio track if an error occurs.
                mAudioTrackHandler.enqueueSynthesisDone(token);
            }
>>>>>>> upstream/master
        } else {
            // This happens when stop() or error() were called before start() was.

            // In all other cases, mAudioTrackHandler.stop() will
            // result in onSynthesisDone being called, and we will
            // write data there.
            mLogger.onWriteData();

            if (wasError) {
                // We have to dispatch the error ourselves.
                mDispatcher.dispatchOnError();
            }
        }
    }

    @Override
    public int getMaxBufferSize() {
        // The AudioTrack buffer will be at least MIN_AUDIO_BUFFER_SIZE, so that should always be
        // a safe buffer size to pass in.
        return MIN_AUDIO_BUFFER_SIZE;
    }

    @Override
    boolean isDone() {
        return mDone;
    }

    @Override
    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        if (DBG) {
            Log.d(TAG, "start(" + sampleRateInHz + "," + audioFormat
                    + "," + channelCount + ")");
        }

<<<<<<< HEAD
        int channelConfig = BlockingAudioTrack.getChannelConfig(channelCount);
=======
        int channelConfig = AudioPlaybackHandler.getChannelConfig(channelCount);
>>>>>>> upstream/master
        if (channelConfig == 0) {
            Log.e(TAG, "Unsupported number of channels :" + channelCount);
            return TextToSpeech.ERROR;
        }

        synchronized (mStateLock) {
            if (mStopped) {
                if (DBG) Log.d(TAG, "stop() called before start(), returning.");
                return TextToSpeech.ERROR;
            }
<<<<<<< HEAD
            SynthesisPlaybackQueueItem item = new SynthesisPlaybackQueueItem(
                    mStreamType, sampleRateInHz, audioFormat, channelCount, mVolume, mPan,
                    mDispatcher, mCallerIdentity, mLogger);
            mAudioTrackHandler.enqueue(item);
            mItem = item;
=======
            SynthesisMessageParams params = new SynthesisMessageParams(
                    mStreamType, sampleRateInHz, audioFormat, channelCount, mVolume, mPan,
                    mDispatcher, mCallingApp, mLogger);
            mAudioTrackHandler.enqueueSynthesisStart(params);

            mToken = params;
>>>>>>> upstream/master
        }

        return TextToSpeech.SUCCESS;
    }


    @Override
    public int audioAvailable(byte[] buffer, int offset, int length) {
        if (DBG) {
            Log.d(TAG, "audioAvailable(byte[" + buffer.length + "],"
                    + offset + "," + length + ")");
        }
        if (length > getMaxBufferSize() || length <= 0) {
            throw new IllegalArgumentException("buffer is too large or of zero length (" +
                    + length + " bytes)");
        }

<<<<<<< HEAD
        SynthesisPlaybackQueueItem item = null;
        synchronized (mStateLock) {
            if (mItem == null || mStopped) {
                return TextToSpeech.ERROR;
            }
            item = mItem;
=======
        SynthesisMessageParams token = null;
        synchronized (mStateLock) {
            if (mToken == null || mStopped) {
                return TextToSpeech.ERROR;
            }
            token = mToken;
>>>>>>> upstream/master
        }

        // Sigh, another copy.
        final byte[] bufferCopy = new byte[length];
        System.arraycopy(buffer, offset, bufferCopy, 0, length);
<<<<<<< HEAD

        // Might block on mItem.this, if there are too many buffers waiting to
        // be consumed.
        try {
            item.put(bufferCopy);
        } catch (InterruptedException ie) {
            return TextToSpeech.ERROR;
        }
=======
        // Might block on mToken.this, if there are too many buffers waiting to
        // be consumed.
        token.addBuffer(bufferCopy);
        mAudioTrackHandler.enqueueSynthesisDataAvailable(token);
>>>>>>> upstream/master

        mLogger.onEngineDataReceived();

        return TextToSpeech.SUCCESS;
    }

    @Override
    public int done() {
        if (DBG) Log.d(TAG, "done()");

<<<<<<< HEAD
        SynthesisPlaybackQueueItem item = null;
=======
        SynthesisMessageParams token = null;
>>>>>>> upstream/master
        synchronized (mStateLock) {
            if (mDone) {
                Log.w(TAG, "Duplicate call to done()");
                return TextToSpeech.ERROR;
            }

            mDone = true;

<<<<<<< HEAD
            if (mItem == null) {
                return TextToSpeech.ERROR;
            }

            item = mItem;
        }

        item.done();
=======
            if (mToken == null) {
                return TextToSpeech.ERROR;
            }

            token = mToken;
        }

        mAudioTrackHandler.enqueueSynthesisDone(token);
>>>>>>> upstream/master
        mLogger.onEngineComplete();

        return TextToSpeech.SUCCESS;
    }

    @Override
    public void error() {
        if (DBG) Log.d(TAG, "error() [will call stop]");
        // Currently, this call will not be logged if error( ) is called
        // before start.
        mLogger.onError();
        stopImpl(true);
    }

}
