/*
 * Copyright (C) 2008 The Android Open Source Project
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
//#define LOG_NDEBUG 0

#define LOG_TAG "AudioTrack-JNI"

#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <math.h>

<<<<<<< HEAD
#include <jni.h>
#include <JNIHelp.h>
#include <android_runtime/AndroidRuntime.h>

#include <utils/Log.h>
#include <media/AudioSystem.h>
#include <media/AudioTrack.h>
=======
#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"

#include "utils/Log.h"
#include "media/AudioSystem.h"
#include "media/AudioTrack.h"
>>>>>>> upstream/master

#include <binder/MemoryHeapBase.h>
#include <binder/MemoryBase.h>

#include <cutils/bitops.h>

#include <system/audio.h>

// ----------------------------------------------------------------------------

using namespace android;

// ----------------------------------------------------------------------------
static const char* const kClassPathName = "android/media/AudioTrack";

struct fields_t {
    // these fields provide access from C++ to the...
    jmethodID postNativeEventInJava; //... event post callback method
    int       PCM16;                 //...  format constants
    int       PCM8;                  //...  format constants
<<<<<<< HEAD
=======
    int       STREAM_VOICE_CALL;     //...  stream type constants
    int       STREAM_SYSTEM;         //...  stream type constants
    int       STREAM_RING;           //...  stream type constants
    int       STREAM_MUSIC;          //...  stream type constants
    int       STREAM_ALARM;          //...  stream type constants
    int       STREAM_NOTIFICATION;   //...  stream type constants
    int       STREAM_BLUETOOTH_SCO;  //...  stream type constants
    int       STREAM_DTMF;           //...  stream type constants
>>>>>>> upstream/master
    int       MODE_STREAM;           //...  memory mode
    int       MODE_STATIC;           //...  memory mode
    jfieldID  nativeTrackInJavaObj;  // stores in Java the native AudioTrack object
    jfieldID  jniData;      // stores in Java additional resources used by the native AudioTrack
};
static fields_t javaAudioTrackFields;

struct audiotrack_callback_cookie {
    jclass      audioTrack_class;
    jobject     audioTrack_ref;
<<<<<<< HEAD
    bool        busy;
    Condition   cond;
};
=======
 };
>>>>>>> upstream/master

// ----------------------------------------------------------------------------
class AudioTrackJniStorage {
    public:
        sp<MemoryHeapBase>         mMemHeap;
        sp<MemoryBase>             mMemBase;
        audiotrack_callback_cookie mCallbackData;
<<<<<<< HEAD
        audio_stream_type_t        mStreamType;
=======
        int                        mStreamType;
>>>>>>> upstream/master

    AudioTrackJniStorage() {
        mCallbackData.audioTrack_class = 0;
        mCallbackData.audioTrack_ref = 0;
        mStreamType = AUDIO_STREAM_DEFAULT;
    }

    ~AudioTrackJniStorage() {
        mMemBase.clear();
        mMemHeap.clear();
    }

    bool allocSharedMem(int sizeInBytes) {
        mMemHeap = new MemoryHeapBase(sizeInBytes, 0, "AudioTrack Heap Base");
        if (mMemHeap->getHeapID() < 0) {
            return false;
        }
        mMemBase = new MemoryBase(mMemHeap, 0, sizeInBytes);
        return true;
    }
};

<<<<<<< HEAD
static Mutex sLock;
static SortedVector <audiotrack_callback_cookie *> sAudioTrackCallBackCookies;

=======
>>>>>>> upstream/master
// ----------------------------------------------------------------------------
#define DEFAULT_OUTPUT_SAMPLE_RATE   44100

#define AUDIOTRACK_SUCCESS                         0
#define AUDIOTRACK_ERROR                           -1
#define AUDIOTRACK_ERROR_BAD_VALUE                 -2
#define AUDIOTRACK_ERROR_INVALID_OPERATION         -3
#define AUDIOTRACK_ERROR_SETUP_AUDIOSYSTEM         -16
<<<<<<< HEAD
#define AUDIOTRACK_ERROR_SETUP_INVALIDCHANNELMASK  -17
=======
#define AUDIOTRACK_ERROR_SETUP_INVALIDCHANNELMASK -17
>>>>>>> upstream/master
#define AUDIOTRACK_ERROR_SETUP_INVALIDFORMAT       -18
#define AUDIOTRACK_ERROR_SETUP_INVALIDSTREAMTYPE   -19
#define AUDIOTRACK_ERROR_SETUP_NATIVEINITFAILED    -20


jint android_media_translateErrorCode(int code) {
<<<<<<< HEAD
    switch (code) {
=======
    switch(code) {
>>>>>>> upstream/master
    case NO_ERROR:
        return AUDIOTRACK_SUCCESS;
    case BAD_VALUE:
        return AUDIOTRACK_ERROR_BAD_VALUE;
    case INVALID_OPERATION:
        return AUDIOTRACK_ERROR_INVALID_OPERATION;
    default:
        return AUDIOTRACK_ERROR;
<<<<<<< HEAD
    }
=======
    }   
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static void audioCallback(int event, void* user, void *info) {
<<<<<<< HEAD

    audiotrack_callback_cookie *callbackInfo = (audiotrack_callback_cookie *)user;
    {
        Mutex::Autolock l(sLock);
        if (sAudioTrackCallBackCookies.indexOf(callbackInfo) < 0) {
            return;
        }
        callbackInfo->busy = true;
    }

    if (event == AudioTrack::EVENT_MORE_DATA) {
        // set size to 0 to signal we're not using the callback to write more data
        AudioTrack::Buffer* pBuff = (AudioTrack::Buffer*)info;
        pBuff->size = 0;

    } else if (event == AudioTrack::EVENT_MARKER) {
        JNIEnv *env = AndroidRuntime::getJNIEnv();
        if (user && env) {
            env->CallStaticVoidMethod(
                callbackInfo->audioTrack_class,
=======
    if (event == AudioTrack::EVENT_MORE_DATA) {
        // set size to 0 to signal we're not using the callback to write more data
        AudioTrack::Buffer* pBuff = (AudioTrack::Buffer*)info;
        pBuff->size = 0;  
    
    } else if (event == AudioTrack::EVENT_MARKER) {
        audiotrack_callback_cookie *callbackInfo = (audiotrack_callback_cookie *)user;
        JNIEnv *env = AndroidRuntime::getJNIEnv();
        if (user && env) {
            env->CallStaticVoidMethod(
                callbackInfo->audioTrack_class, 
>>>>>>> upstream/master
                javaAudioTrackFields.postNativeEventInJava,
                callbackInfo->audioTrack_ref, event, 0,0, NULL);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
            }
        }

    } else if (event == AudioTrack::EVENT_NEW_POS) {
<<<<<<< HEAD
        JNIEnv *env = AndroidRuntime::getJNIEnv();
        if (user && env) {
            env->CallStaticVoidMethod(
                callbackInfo->audioTrack_class,
=======
        audiotrack_callback_cookie *callbackInfo = (audiotrack_callback_cookie *)user;
        JNIEnv *env = AndroidRuntime::getJNIEnv();
        if (user && env) {
            env->CallStaticVoidMethod(
                callbackInfo->audioTrack_class, 
>>>>>>> upstream/master
                javaAudioTrackFields.postNativeEventInJava,
                callbackInfo->audioTrack_ref, event, 0,0, NULL);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
            }
        }
    }
<<<<<<< HEAD
    {
        Mutex::Autolock l(sLock);
        callbackInfo->busy = false;
        callbackInfo->cond.broadcast();
    }
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
<<<<<<< HEAD
static sp<AudioTrack> getAudioTrack(JNIEnv* env, jobject thiz)
{
    Mutex::Autolock l(sLock);
    AudioTrack* const at =
            (AudioTrack*)env->GetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    return sp<AudioTrack>(at);
}

static sp<AudioTrack> setAudioTrack(JNIEnv* env, jobject thiz, const sp<AudioTrack>& at)
{
    Mutex::Autolock l(sLock);
    sp<AudioTrack> old =
            (AudioTrack*)env->GetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (at.get()) {
        at->incStrong(thiz);
    }
    if (old != 0) {
        old->decStrong(thiz);
    }
    env->SetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj, (int)at.get());
    return old;
}

// ----------------------------------------------------------------------------
=======
>>>>>>> upstream/master
static int
android_media_AudioTrack_native_setup(JNIEnv *env, jobject thiz, jobject weak_this,
        jint streamType, jint sampleRateInHertz, jint javaChannelMask,
        jint audioFormat, jint buffSizeInBytes, jint memoryMode, jintArray jSession)
{
<<<<<<< HEAD
    ALOGV("sampleRate=%d, audioFormat(from Java)=%d, channel mask=%x, buffSize=%d",
=======
    LOGV("sampleRate=%d, audioFormat(from Java)=%d, channel mask=%x, buffSize=%d",
>>>>>>> upstream/master
        sampleRateInHertz, audioFormat, javaChannelMask, buffSizeInBytes);
    int afSampleRate;
    int afFrameCount;

<<<<<<< HEAD
    if (AudioSystem::getOutputFrameCount(&afFrameCount, (audio_stream_type_t) streamType) != NO_ERROR) {
        ALOGE("Error creating AudioTrack: Could not get AudioSystem frame count.");
        return AUDIOTRACK_ERROR_SETUP_AUDIOSYSTEM;
    }
    if (AudioSystem::getOutputSamplingRate(&afSampleRate, (audio_stream_type_t) streamType) != NO_ERROR) {
        ALOGE("Error creating AudioTrack: Could not get AudioSystem sampling rate.");
=======
    if (AudioSystem::getOutputFrameCount(&afFrameCount, streamType) != NO_ERROR) {
        LOGE("Error creating AudioTrack: Could not get AudioSystem frame count.");
        return AUDIOTRACK_ERROR_SETUP_AUDIOSYSTEM;
    }
    if (AudioSystem::getOutputSamplingRate(&afSampleRate, streamType) != NO_ERROR) {
        LOGE("Error creating AudioTrack: Could not get AudioSystem sampling rate.");
>>>>>>> upstream/master
        return AUDIOTRACK_ERROR_SETUP_AUDIOSYSTEM;
    }

    // Java channel masks don't map directly to the native definition, but it's a simple shift
    // to skip the two deprecated channel configurations "default" and "mono".
    uint32_t nativeChannelMask = ((uint32_t)javaChannelMask) >> 2;

    if (!audio_is_output_channel(nativeChannelMask)) {
<<<<<<< HEAD
        ALOGE("Error creating AudioTrack: invalid channel mask.");
=======
        LOGE("Error creating AudioTrack: invalid channel mask.");
>>>>>>> upstream/master
        return AUDIOTRACK_ERROR_SETUP_INVALIDCHANNELMASK;
    }

    int nbChannels = popcount(nativeChannelMask);
<<<<<<< HEAD

    // check the stream type
    audio_stream_type_t atStreamType;
    switch (streamType) {
    case AUDIO_STREAM_VOICE_CALL:
    case AUDIO_STREAM_SYSTEM:
    case AUDIO_STREAM_RING:
    case AUDIO_STREAM_MUSIC:
    case AUDIO_STREAM_ALARM:
    case AUDIO_STREAM_NOTIFICATION:
    case AUDIO_STREAM_BLUETOOTH_SCO:
    case AUDIO_STREAM_DTMF:
        atStreamType = (audio_stream_type_t) streamType;
        break;
    default:
        ALOGE("Error creating AudioTrack: unknown stream type.");
=======
    
    // check the stream type
    audio_stream_type_t atStreamType;
    if (streamType == javaAudioTrackFields.STREAM_VOICE_CALL) {
        atStreamType = AUDIO_STREAM_VOICE_CALL;
    } else if (streamType == javaAudioTrackFields.STREAM_SYSTEM) {
        atStreamType = AUDIO_STREAM_SYSTEM;
    } else if (streamType == javaAudioTrackFields.STREAM_RING) {
        atStreamType = AUDIO_STREAM_RING;
    } else if (streamType == javaAudioTrackFields.STREAM_MUSIC) {
        atStreamType = AUDIO_STREAM_MUSIC;
    } else if (streamType == javaAudioTrackFields.STREAM_ALARM) {
        atStreamType = AUDIO_STREAM_ALARM;
    } else if (streamType == javaAudioTrackFields.STREAM_NOTIFICATION) {
        atStreamType = AUDIO_STREAM_NOTIFICATION;
    } else if (streamType == javaAudioTrackFields.STREAM_BLUETOOTH_SCO) {
        atStreamType = AUDIO_STREAM_BLUETOOTH_SCO;
    } else if (streamType == javaAudioTrackFields.STREAM_DTMF) {
        atStreamType = AUDIO_STREAM_DTMF;
    } else {
        LOGE("Error creating AudioTrack: unknown stream type.");
>>>>>>> upstream/master
        return AUDIOTRACK_ERROR_SETUP_INVALIDSTREAMTYPE;
    }

    // check the format.
    // This function was called from Java, so we compare the format against the Java constants
    if ((audioFormat != javaAudioTrackFields.PCM16) && (audioFormat != javaAudioTrackFields.PCM8)) {
<<<<<<< HEAD
        ALOGE("Error creating AudioTrack: unsupported audio format.");
=======
        LOGE("Error creating AudioTrack: unsupported audio format.");
>>>>>>> upstream/master
        return AUDIOTRACK_ERROR_SETUP_INVALIDFORMAT;
    }

    // for the moment 8bitPCM in MODE_STATIC is not supported natively in the AudioTrack C++ class
    // so we declare everything as 16bitPCM, the 8->16bit conversion for MODE_STATIC will be handled
<<<<<<< HEAD
    // in android_media_AudioTrack_native_write_byte()
    if ((audioFormat == javaAudioTrackFields.PCM8)
        && (memoryMode == javaAudioTrackFields.MODE_STATIC)) {
        ALOGV("android_media_AudioTrack_native_setup(): requesting MODE_STATIC for 8bit \
=======
    // in android_media_AudioTrack_native_write()
    if ((audioFormat == javaAudioTrackFields.PCM8) 
        && (memoryMode == javaAudioTrackFields.MODE_STATIC)) {
        LOGV("android_media_AudioTrack_native_setup(): requesting MODE_STATIC for 8bit \
>>>>>>> upstream/master
            buff size of %dbytes, switching to 16bit, buff size of %dbytes",
            buffSizeInBytes, 2*buffSizeInBytes);
        audioFormat = javaAudioTrackFields.PCM16;
        // we will need twice the memory to store the data
        buffSizeInBytes *= 2;
    }

    // compute the frame count
    int bytesPerSample = audioFormat == javaAudioTrackFields.PCM16 ? 2 : 1;
<<<<<<< HEAD
    audio_format_t format = audioFormat == javaAudioTrackFields.PCM16 ?
            AUDIO_FORMAT_PCM_16_BIT : AUDIO_FORMAT_PCM_8_BIT;
    int frameCount = buffSizeInBytes / (nbChannels * bytesPerSample);

    jclass clazz = env->GetObjectClass(thiz);
    if (clazz == NULL) {
        ALOGE("Can't find %s when setting up callback.", kClassPathName);
        return AUDIOTRACK_ERROR_SETUP_NATIVEINITFAILED;
    }

    if (jSession == NULL) {
        ALOGE("Error creating AudioTrack: invalid session ID pointer");
=======
    int format = audioFormat == javaAudioTrackFields.PCM16 ? 
            AUDIO_FORMAT_PCM_16_BIT : AUDIO_FORMAT_PCM_8_BIT;
    int frameCount = buffSizeInBytes / (nbChannels * bytesPerSample);
    
    AudioTrackJniStorage* lpJniStorage = new AudioTrackJniStorage();
    
    // initialize the callback information:
    // this data will be passed with every AudioTrack callback
    jclass clazz = env->GetObjectClass(thiz);
    if (clazz == NULL) {
        LOGE("Can't find %s when setting up callback.", kClassPathName);
        delete lpJniStorage;
        return AUDIOTRACK_ERROR_SETUP_NATIVEINITFAILED;
    }
    lpJniStorage->mCallbackData.audioTrack_class = (jclass)env->NewGlobalRef(clazz);
    // we use a weak reference so the AudioTrack object can be garbage collected.
    lpJniStorage->mCallbackData.audioTrack_ref = env->NewGlobalRef(weak_this);
    
    lpJniStorage->mStreamType = atStreamType;

    if (jSession == NULL) {
        LOGE("Error creating AudioTrack: invalid session ID pointer");
        delete lpJniStorage;
>>>>>>> upstream/master
        return AUDIOTRACK_ERROR;
    }

    jint* nSession = (jint *) env->GetPrimitiveArrayCritical(jSession, NULL);
    if (nSession == NULL) {
<<<<<<< HEAD
        ALOGE("Error creating AudioTrack: Error retrieving session id pointer");
=======
        LOGE("Error creating AudioTrack: Error retrieving session id pointer");
        delete lpJniStorage;
>>>>>>> upstream/master
        return AUDIOTRACK_ERROR;
    }
    int sessionId = nSession[0];
    env->ReleasePrimitiveArrayCritical(jSession, nSession, 0);
    nSession = NULL;

    // create the native AudioTrack object
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = new AudioTrack();
    if (lpTrack == NULL) {
        ALOGE("Error creating uninitialized AudioTrack");
        return AUDIOTRACK_ERROR_SETUP_NATIVEINITFAILED;
    }

    // initialize the callback information:
    // this data will be passed with every AudioTrack callback
    AudioTrackJniStorage* lpJniStorage = new AudioTrackJniStorage();
    lpJniStorage->mStreamType = atStreamType;
    lpJniStorage->mCallbackData.audioTrack_class = (jclass)env->NewGlobalRef(clazz);
    // we use a weak reference so the AudioTrack object can be garbage collected.
    lpJniStorage->mCallbackData.audioTrack_ref = env->NewGlobalRef(weak_this);
    lpJniStorage->mCallbackData.busy = false;

=======
    AudioTrack* lpTrack = new AudioTrack();
    if (lpTrack == NULL) {
        LOGE("Error creating uninitialized AudioTrack");
        goto native_track_failure;
    }
    
>>>>>>> upstream/master
    // initialize the native AudioTrack object
    if (memoryMode == javaAudioTrackFields.MODE_STREAM) {

        lpTrack->set(
            atStreamType,// stream type
            sampleRateInHertz,
            format,// word length, PCM
            nativeChannelMask,
            frameCount,
<<<<<<< HEAD
            AUDIO_OUTPUT_FLAG_NONE,
=======
            0,// flags
>>>>>>> upstream/master
            audioCallback, &(lpJniStorage->mCallbackData),//callback, callback data (user)
            0,// notificationFrames == 0 since not using EVENT_MORE_DATA to feed the AudioTrack
            0,// shared mem
            true,// thread can call Java
            sessionId);// audio session ID
<<<<<<< HEAD

    } else if (memoryMode == javaAudioTrackFields.MODE_STATIC) {
        // AudioTrack is using shared memory

        if (!lpJniStorage->allocSharedMem(buffSizeInBytes)) {
            ALOGE("Error creating AudioTrack in static mode: error creating mem heap base");
            goto native_init_failure;
        }

=======
            
    } else if (memoryMode == javaAudioTrackFields.MODE_STATIC) {
        // AudioTrack is using shared memory
        
        if (!lpJniStorage->allocSharedMem(buffSizeInBytes)) {
            LOGE("Error creating AudioTrack in static mode: error creating mem heap base");
            goto native_init_failure;
        }
        
>>>>>>> upstream/master
        lpTrack->set(
            atStreamType,// stream type
            sampleRateInHertz,
            format,// word length, PCM
            nativeChannelMask,
            frameCount,
<<<<<<< HEAD
            AUDIO_OUTPUT_FLAG_NONE,
            audioCallback, &(lpJniStorage->mCallbackData),//callback, callback data (user));
            0,// notificationFrames == 0 since not using EVENT_MORE_DATA to feed the AudioTrack
=======
            0,// flags
            audioCallback, &(lpJniStorage->mCallbackData),//callback, callback data (user));
            0,// notificationFrames == 0 since not using EVENT_MORE_DATA to feed the AudioTrack 
>>>>>>> upstream/master
            lpJniStorage->mMemBase,// shared mem
            true,// thread can call Java
            sessionId);// audio session ID
    }

    if (lpTrack->initCheck() != NO_ERROR) {
<<<<<<< HEAD
        ALOGE("Error initializing AudioTrack");
=======
        LOGE("Error initializing AudioTrack");
>>>>>>> upstream/master
        goto native_init_failure;
    }

    nSession = (jint *) env->GetPrimitiveArrayCritical(jSession, NULL);
    if (nSession == NULL) {
<<<<<<< HEAD
        ALOGE("Error creating AudioTrack: Error retrieving session id pointer");
=======
        LOGE("Error creating AudioTrack: Error retrieving session id pointer");
>>>>>>> upstream/master
        goto native_init_failure;
    }
    // read the audio session ID back from AudioTrack in case we create a new session
    nSession[0] = lpTrack->getSessionId();
    env->ReleasePrimitiveArrayCritical(jSession, nSession, 0);
    nSession = NULL;

<<<<<<< HEAD
    {   // scope for the lock
        Mutex::Autolock l(sLock);
        sAudioTrackCallBackCookies.add(&lpJniStorage->mCallbackData);
    }
    // save our newly created C++ AudioTrack in the "nativeTrackInJavaObj" field
    // of the Java object (in mNativeTrackInJavaObj)
    setAudioTrack(env, thiz, lpTrack);

    // save the JNI resources so we can free them later
    //ALOGV("storing lpJniStorage: %x\n", (int)lpJniStorage);
    env->SetIntField(thiz, javaAudioTrackFields.jniData, (int)lpJniStorage);

    return AUDIOTRACK_SUCCESS;

    // failures:
native_init_failure:
=======
    // save our newly created C++ AudioTrack in the "nativeTrackInJavaObj" field 
    // of the Java object (in mNativeTrackInJavaObj)
    env->SetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj, (int)lpTrack);
    
    // save the JNI resources so we can free them later
    //LOGV("storing lpJniStorage: %x\n", (int)lpJniStorage);
    env->SetIntField(thiz, javaAudioTrackFields.jniData, (int)lpJniStorage);

    return AUDIOTRACK_SUCCESS;
    
    // failures:
native_init_failure:
    delete lpTrack;
    env->SetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj, 0);
    
native_track_failure:
>>>>>>> upstream/master
    if (nSession != NULL) {
        env->ReleasePrimitiveArrayCritical(jSession, nSession, 0);
    }
    env->DeleteGlobalRef(lpJniStorage->mCallbackData.audioTrack_class);
    env->DeleteGlobalRef(lpJniStorage->mCallbackData.audioTrack_ref);
    delete lpJniStorage;
    env->SetIntField(thiz, javaAudioTrackFields.jniData, 0);
<<<<<<< HEAD

    return AUDIOTRACK_ERROR_SETUP_NATIVEINITFAILED;
=======
    return AUDIOTRACK_ERROR_SETUP_NATIVEINITFAILED;
    
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static void
android_media_AudioTrack_start(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (lpTrack == NULL ) {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for start()");
        return;
    }

    lpTrack->start();
}


// ----------------------------------------------------------------------------
static void
android_media_AudioTrack_stop(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (lpTrack == NULL ) {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for stop()");
        return;
    }

    lpTrack->stop();
}


// ----------------------------------------------------------------------------
static void
android_media_AudioTrack_pause(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (lpTrack == NULL ) {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for pause()");
        return;
    }

    lpTrack->pause();
}


// ----------------------------------------------------------------------------
static void
android_media_AudioTrack_flush(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (lpTrack == NULL ) {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for flush()");
        return;
    }

    lpTrack->flush();
}

// ----------------------------------------------------------------------------
static void
android_media_AudioTrack_set_volume(JNIEnv *env, jobject thiz, jfloat leftVol, jfloat rightVol )
{
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (lpTrack == NULL ) {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setVolume()");
        return;
    }

    lpTrack->setVolume(leftVol, rightVol);
}

// ----------------------------------------------------------------------------
<<<<<<< HEAD

#define CALLBACK_COND_WAIT_TIMEOUT_MS 1000
static void android_media_AudioTrack_native_release(JNIEnv *env,  jobject thiz) {
    sp<AudioTrack> lpTrack = setAudioTrack(env, thiz, 0);
    if (lpTrack == NULL) {
        return;
    }
    //ALOGV("deleting lpTrack: %x\n", (int)lpTrack);
    lpTrack->stop();

    // delete the JNI data
    AudioTrackJniStorage* pJniStorage = (AudioTrackJniStorage *)env->GetIntField(
        thiz, javaAudioTrackFields.jniData);
    // reset the native resources in the Java object so any attempt to access
    // them after a call to release fails.
    env->SetIntField(thiz, javaAudioTrackFields.jniData, 0);

    if (pJniStorage) {
        Mutex::Autolock l(sLock);
        audiotrack_callback_cookie *lpCookie = &pJniStorage->mCallbackData;
        //ALOGV("deleting pJniStorage: %x\n", (int)pJniStorage);
        while (lpCookie->busy) {
            if (lpCookie->cond.waitRelative(sLock,
                                            milliseconds(CALLBACK_COND_WAIT_TIMEOUT_MS)) !=
                                                    NO_ERROR) {
                break;
            }
        }
        sAudioTrackCallBackCookies.remove(lpCookie);
        // delete global refs created in native_setup
        env->DeleteGlobalRef(lpCookie->audioTrack_class);
        env->DeleteGlobalRef(lpCookie->audioTrack_ref);
=======
static void android_media_AudioTrack_native_finalize(JNIEnv *env,  jobject thiz) {
    //LOGV("android_media_AudioTrack_native_finalize jobject: %x\n", (int)thiz);
       
    // delete the AudioTrack object
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    if (lpTrack) {
        //LOGV("deleting lpTrack: %x\n", (int)lpTrack);
        lpTrack->stop();
        delete lpTrack;
    }
    
    // delete the JNI data
    AudioTrackJniStorage* pJniStorage = (AudioTrackJniStorage *)env->GetIntField(
        thiz, javaAudioTrackFields.jniData);
    if (pJniStorage) {
        // delete global refs created in native_setup
        env->DeleteGlobalRef(pJniStorage->mCallbackData.audioTrack_class);
        env->DeleteGlobalRef(pJniStorage->mCallbackData.audioTrack_ref);
        //LOGV("deleting pJniStorage: %x\n", (int)pJniStorage);
>>>>>>> upstream/master
        delete pJniStorage;
    }
}

<<<<<<< HEAD

// ----------------------------------------------------------------------------
static void android_media_AudioTrack_native_finalize(JNIEnv *env,  jobject thiz) {
    //ALOGV("android_media_AudioTrack_native_finalize jobject: %x\n", (int)thiz);
    android_media_AudioTrack_native_release(env, thiz);
}

// ----------------------------------------------------------------------------
jint writeToTrack(const sp<AudioTrack>& track, jint audioFormat, jbyte* data,
=======
// ----------------------------------------------------------------------------
static void android_media_AudioTrack_native_release(JNIEnv *env,  jobject thiz) {
       
    // do everything a call to finalize would
    android_media_AudioTrack_native_finalize(env, thiz);
    // + reset the native resources in the Java object so any attempt to access
    // them after a call to release fails.
    env->SetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj, 0);
    env->SetIntField(thiz, javaAudioTrackFields.jniData, 0);
}


// ----------------------------------------------------------------------------
jint writeToTrack(AudioTrack* pTrack, jint audioFormat, jbyte* data,
>>>>>>> upstream/master
                  jint offsetInBytes, jint sizeInBytes) {
    // give the data to the native AudioTrack object (the data starts at the offset)
    ssize_t written = 0;
    // regular write() or copy the data to the AudioTrack's shared memory?
<<<<<<< HEAD
    if (track->sharedBuffer() == 0) {
        written = track->write(data + offsetInBytes, sizeInBytes);
    } else {
        if (audioFormat == javaAudioTrackFields.PCM16) {
            // writing to shared memory, check for capacity
            if ((size_t)sizeInBytes > track->sharedBuffer()->size()) {
                sizeInBytes = track->sharedBuffer()->size();
            }
            memcpy(track->sharedBuffer()->pointer(), data + offsetInBytes, sizeInBytes);
=======
    if (pTrack->sharedBuffer() == 0) {
        written = pTrack->write(data + offsetInBytes, sizeInBytes);
    } else {
        if (audioFormat == javaAudioTrackFields.PCM16) {
            // writing to shared memory, check for capacity
            if ((size_t)sizeInBytes > pTrack->sharedBuffer()->size()) {
                sizeInBytes = pTrack->sharedBuffer()->size();
            }
            memcpy(pTrack->sharedBuffer()->pointer(), data + offsetInBytes, sizeInBytes);
>>>>>>> upstream/master
            written = sizeInBytes;
        } else if (audioFormat == javaAudioTrackFields.PCM8) {
            // data contains 8bit data we need to expand to 16bit before copying
            // to the shared memory
            // writing to shared memory, check for capacity,
            // note that input data will occupy 2X the input space due to 8 to 16bit conversion
<<<<<<< HEAD
            if (((size_t)sizeInBytes)*2 > track->sharedBuffer()->size()) {
                sizeInBytes = track->sharedBuffer()->size() / 2;
            }
            int count = sizeInBytes;
            int16_t *dst = (int16_t *)track->sharedBuffer()->pointer();
            const int8_t *src = (const int8_t *)(data + offsetInBytes);
            while (count--) {
=======
            if (((size_t)sizeInBytes)*2 > pTrack->sharedBuffer()->size()) {
                sizeInBytes = pTrack->sharedBuffer()->size() / 2;
            }
            int count = sizeInBytes;
            int16_t *dst = (int16_t *)pTrack->sharedBuffer()->pointer();
            const int8_t *src = (const int8_t *)(data + offsetInBytes);
            while(count--) {
>>>>>>> upstream/master
                *dst++ = (int16_t)(*src++^0x80) << 8;
            }
            // even though we wrote 2*sizeInBytes, we only report sizeInBytes as written to hide
            // the 8bit mixer restriction from the user of this function
            written = sizeInBytes;
        }
    }
    return written;

}

// ----------------------------------------------------------------------------
<<<<<<< HEAD
static jint android_media_AudioTrack_native_write_byte(JNIEnv *env,  jobject thiz,
                                                  jbyteArray javaAudioData,
                                                  jint offsetInBytes, jint sizeInBytes,
                                                  jint javaAudioFormat) {
    //ALOGV("android_media_AudioTrack_native_write_byte(offset=%d, sizeInBytes=%d) called",
    //    offsetInBytes, sizeInBytes);
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
=======
static jint android_media_AudioTrack_native_write(JNIEnv *env,  jobject thiz,
                                                  jbyteArray javaAudioData,
                                                  jint offsetInBytes, jint sizeInBytes,
                                                  jint javaAudioFormat) {
    jbyte* cAudioData = NULL;
    AudioTrack *lpTrack = NULL;
    //LOGV("android_media_AudioTrack_native_write(offset=%d, sizeInBytes=%d) called",
    //    offsetInBytes, sizeInBytes);
    
    // get the audio track to load with samples
    lpTrack = (AudioTrack *)env->GetIntField(thiz, javaAudioTrackFields.nativeTrackInJavaObj);
>>>>>>> upstream/master
    if (lpTrack == NULL) {
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for write()");
        return 0;
    }

    // get the pointer for the audio data from the java array
    // NOTE: We may use GetPrimitiveArrayCritical() when the JNI implementation changes in such
    // a way that it becomes much more efficient. When doing so, we will have to prevent the
    // AudioSystem callback to be called while in critical section (in case of media server
    // process crash for instance)
<<<<<<< HEAD
    jbyte* cAudioData = NULL;
    if (javaAudioData) {
        cAudioData = (jbyte *)env->GetByteArrayElements(javaAudioData, NULL);
        if (cAudioData == NULL) {
            ALOGE("Error retrieving source of audio data to play, can't play");
            return 0; // out of memory or no data to load
        }
    } else {
        ALOGE("NULL java array of audio data to play, can't play");
=======
    if (javaAudioData) {
        cAudioData = (jbyte *)env->GetByteArrayElements(javaAudioData, NULL);
        if (cAudioData == NULL) {
            LOGE("Error retrieving source of audio data to play, can't play");
            return 0; // out of memory or no data to load
        }
    } else {
        LOGE("NULL java array of audio data to play, can't play");
>>>>>>> upstream/master
        return 0;
    }

    jint written = writeToTrack(lpTrack, javaAudioFormat, cAudioData, offsetInBytes, sizeInBytes);

    env->ReleaseByteArrayElements(javaAudioData, cAudioData, 0);

<<<<<<< HEAD
    //ALOGV("write wrote %d (tried %d) bytes in the native AudioTrack with offset %d",
=======
    //LOGV("write wrote %d (tried %d) bytes in the native AudioTrack with offset %d",
>>>>>>> upstream/master
    //     (int)written, (int)(sizeInBytes), (int)offsetInBytes);
    return written;
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_native_write_short(JNIEnv *env,  jobject thiz,
                                                  jshortArray javaAudioData,
                                                  jint offsetInShorts, jint sizeInShorts,
                                                  jint javaAudioFormat) {
<<<<<<< HEAD
    return (android_media_AudioTrack_native_write_byte(env, thiz,
=======
    return (android_media_AudioTrack_native_write(env, thiz,
>>>>>>> upstream/master
                                                 (jbyteArray) javaAudioData,
                                                 offsetInShorts*2, sizeInShorts*2,
                                                 javaAudioFormat)
            / 2);
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_get_native_frame_count(JNIEnv *env,  jobject thiz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);

    if (lpTrack) {
        return lpTrack->frameCount();
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for frameCount()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD

    return lpTrack->frameCount();
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_set_playback_rate(JNIEnv *env,  jobject thiz,
        jint sampleRateInHz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);

    if (lpTrack) {
        return android_media_translateErrorCode(lpTrack->setSampleRate(sampleRateInHz));
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setSampleRate()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return android_media_translateErrorCode(lpTrack->setSampleRate(sampleRateInHz));
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_get_playback_rate(JNIEnv *env,  jobject thiz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);

    if (lpTrack) {
        return (jint) lpTrack->getSampleRate();   
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for getSampleRate()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return (jint) lpTrack->getSampleRate();
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
<<<<<<< HEAD
static jint android_media_AudioTrack_set_marker_pos(JNIEnv *env,  jobject thiz,
        jint markerPos) {
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
static jint android_media_AudioTrack_set_marker_pos(JNIEnv *env,  jobject thiz, 
        jint markerPos) {
            
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
                
    if (lpTrack) {
        return android_media_translateErrorCode( lpTrack->setMarkerPosition(markerPos) );   
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setMarkerPosition()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return android_media_translateErrorCode( lpTrack->setMarkerPosition(markerPos) );
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_get_marker_pos(JNIEnv *env,  jobject thiz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    uint32_t markerPos = 0;

    if (lpTrack == NULL) {
=======
    
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    uint32_t markerPos = 0;
                
    if (lpTrack) {
        lpTrack->getMarkerPosition(&markerPos);
        return (jint)markerPos;
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for getMarkerPosition()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    lpTrack->getMarkerPosition(&markerPos);
    return (jint)markerPos;
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_set_pos_update_period(JNIEnv *env,  jobject thiz,
        jint period) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setPositionUpdatePeriod()");
        return AUDIOTRACK_ERROR;
    }
    return android_media_translateErrorCode( lpTrack->setPositionUpdatePeriod(period) );
=======
            
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
                
    if (lpTrack) {
        return android_media_translateErrorCode( lpTrack->setPositionUpdatePeriod(period) );   
    } else {
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setPositionUpdatePeriod()");
        return AUDIOTRACK_ERROR;
    }            
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_get_pos_update_period(JNIEnv *env,  jobject thiz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    uint32_t period = 0;

    if (lpTrack == NULL) {
=======
    
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    uint32_t period = 0;
                
    if (lpTrack) {
        lpTrack->getPositionUpdatePeriod(&period);
        return (jint)period;
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for getPositionUpdatePeriod()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    lpTrack->getPositionUpdatePeriod(&period);
    return (jint)period;
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
<<<<<<< HEAD
static jint android_media_AudioTrack_set_position(JNIEnv *env,  jobject thiz,
        jint position) {
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======
static jint android_media_AudioTrack_set_position(JNIEnv *env,  jobject thiz, 
        jint position) {
            
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
                
    if (lpTrack) {
        return android_media_translateErrorCode( lpTrack->setPosition(position) );
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setPosition()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return android_media_translateErrorCode( lpTrack->setPosition(position) );
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_get_position(JNIEnv *env,  jobject thiz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    uint32_t position = 0;

    if (lpTrack == NULL) {
=======
    
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
    uint32_t position = 0;
                
    if (lpTrack) {
        lpTrack->getPosition(&position);
        return (jint)position;
    }  else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for getPosition()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    lpTrack->getPosition(&position);
    return (jint)position;
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_set_loop(JNIEnv *env,  jobject thiz,
        jint loopStart, jint loopEnd, jint loopCount) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======

     AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
     if (lpTrack) {
        return android_media_translateErrorCode( lpTrack->setLoop(loopStart, loopEnd, loopCount) );
     }  else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setLoop()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return android_media_translateErrorCode( lpTrack->setLoop(loopStart, loopEnd, loopCount) );
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_reload(JNIEnv *env,  jobject thiz) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======

     AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);
     if (lpTrack) {
        return android_media_translateErrorCode( lpTrack->reload() );
     } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for reload()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return android_media_translateErrorCode( lpTrack->reload() );
=======
>>>>>>> upstream/master
}


// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_get_output_sample_rate(JNIEnv *env,  jobject thiz,
        jint javaStreamType) {
    int afSamplingRate;
    // convert the stream type from Java to native value
    // FIXME: code duplication with android_media_AudioTrack_native_setup()
    audio_stream_type_t nativeStreamType;
<<<<<<< HEAD
    switch (javaStreamType) {
    case AUDIO_STREAM_VOICE_CALL:
    case AUDIO_STREAM_SYSTEM:
    case AUDIO_STREAM_RING:
    case AUDIO_STREAM_MUSIC:
    case AUDIO_STREAM_ALARM:
    case AUDIO_STREAM_NOTIFICATION:
    case AUDIO_STREAM_BLUETOOTH_SCO:
    case AUDIO_STREAM_DTMF:
        nativeStreamType = (audio_stream_type_t) javaStreamType;
        break;
    default:
        nativeStreamType = AUDIO_STREAM_DEFAULT;
        break;
    }

    if (AudioSystem::getOutputSamplingRate(&afSamplingRate, nativeStreamType) != NO_ERROR) {
        ALOGE("AudioSystem::getOutputSamplingRate() for stream type %d failed in AudioTrack JNI",
=======
    if (javaStreamType == javaAudioTrackFields.STREAM_VOICE_CALL) {
        nativeStreamType = AUDIO_STREAM_VOICE_CALL;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_SYSTEM) {
        nativeStreamType = AUDIO_STREAM_SYSTEM;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_RING) {
        nativeStreamType = AUDIO_STREAM_RING;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_MUSIC) {
        nativeStreamType = AUDIO_STREAM_MUSIC;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_ALARM) {
        nativeStreamType = AUDIO_STREAM_ALARM;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_NOTIFICATION) {
        nativeStreamType = AUDIO_STREAM_NOTIFICATION;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_BLUETOOTH_SCO) {
        nativeStreamType = AUDIO_STREAM_BLUETOOTH_SCO;
    } else if (javaStreamType == javaAudioTrackFields.STREAM_DTMF) {
        nativeStreamType = AUDIO_STREAM_DTMF;
    } else {
        nativeStreamType = AUDIO_STREAM_DEFAULT;
    }

    if (AudioSystem::getOutputSamplingRate(&afSamplingRate, nativeStreamType) != NO_ERROR) {
        LOGE("AudioSystem::getOutputSamplingRate() for stream type %d failed in AudioTrack JNI",
>>>>>>> upstream/master
            nativeStreamType);
        return DEFAULT_OUTPUT_SAMPLE_RATE;
    } else {
        return afSamplingRate;
    }
}


// ----------------------------------------------------------------------------
// returns the minimum required size for the successful creation of a streaming AudioTrack
// returns -1 if there was an error querying the hardware.
static jint android_media_AudioTrack_get_min_buff_size(JNIEnv *env,  jobject thiz,
    jint sampleRateInHertz, jint nbChannels, jint audioFormat) {

    int frameCount = 0;
    if (AudioTrack::getMinFrameCount(&frameCount, AUDIO_STREAM_DEFAULT,
            sampleRateInHertz) != NO_ERROR) {
        return -1;
    }
    return frameCount * nbChannels * (audioFormat == javaAudioTrackFields.PCM16 ? 2 : 1);
}

// ----------------------------------------------------------------------------
static void
android_media_AudioTrack_setAuxEffectSendLevel(JNIEnv *env, jobject thiz, jfloat level )
{
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
=======
    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
        thiz, javaAudioTrackFields.nativeTrackInJavaObj);
>>>>>>> upstream/master
    if (lpTrack == NULL ) {
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for setAuxEffectSendLevel()");
        return;
    }

    lpTrack->setAuxEffectSendLevel(level);
}

// ----------------------------------------------------------------------------
static jint android_media_AudioTrack_attachAuxEffect(JNIEnv *env,  jobject thiz,
        jint effectId) {
<<<<<<< HEAD
    sp<AudioTrack> lpTrack = getAudioTrack(env, thiz);
    if (lpTrack == NULL) {
=======

    AudioTrack *lpTrack = (AudioTrack *)env->GetIntField(
                thiz, javaAudioTrackFields.nativeTrackInJavaObj);

    if (lpTrack) {
        return android_media_translateErrorCode( lpTrack->attachAuxEffect(effectId) );
    } else {
>>>>>>> upstream/master
        jniThrowException(env, "java/lang/IllegalStateException",
            "Unable to retrieve AudioTrack pointer for attachAuxEffect()");
        return AUDIOTRACK_ERROR;
    }
<<<<<<< HEAD
    return android_media_translateErrorCode( lpTrack->attachAuxEffect(effectId) );
=======
>>>>>>> upstream/master
}

// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
static JNINativeMethod gMethods[] = {
    // name,              signature,     funcPtr
    {"native_start",         "()V",      (void *)android_media_AudioTrack_start},
    {"native_stop",          "()V",      (void *)android_media_AudioTrack_stop},
    {"native_pause",         "()V",      (void *)android_media_AudioTrack_pause},
    {"native_flush",         "()V",      (void *)android_media_AudioTrack_flush},
    {"native_setup",         "(Ljava/lang/Object;IIIIII[I)I",
                                         (void *)android_media_AudioTrack_native_setup},
    {"native_finalize",      "()V",      (void *)android_media_AudioTrack_native_finalize},
    {"native_release",       "()V",      (void *)android_media_AudioTrack_native_release},
<<<<<<< HEAD
    {"native_write_byte",    "([BIII)I", (void *)android_media_AudioTrack_native_write_byte},
=======
    {"native_write_byte",    "([BIII)I", (void *)android_media_AudioTrack_native_write},
>>>>>>> upstream/master
    {"native_write_short",   "([SIII)I", (void *)android_media_AudioTrack_native_write_short},
    {"native_setVolume",     "(FF)V",    (void *)android_media_AudioTrack_set_volume},
    {"native_get_native_frame_count",
                             "()I",      (void *)android_media_AudioTrack_get_native_frame_count},
    {"native_set_playback_rate",
                             "(I)I",     (void *)android_media_AudioTrack_set_playback_rate},
    {"native_get_playback_rate",
                             "()I",      (void *)android_media_AudioTrack_get_playback_rate},
    {"native_set_marker_pos","(I)I",     (void *)android_media_AudioTrack_set_marker_pos},
    {"native_get_marker_pos","()I",      (void *)android_media_AudioTrack_get_marker_pos},
    {"native_set_pos_update_period",
                             "(I)I",     (void *)android_media_AudioTrack_set_pos_update_period},
    {"native_get_pos_update_period",
                             "()I",      (void *)android_media_AudioTrack_get_pos_update_period},
    {"native_set_position",  "(I)I",     (void *)android_media_AudioTrack_set_position},
    {"native_get_position",  "()I",      (void *)android_media_AudioTrack_get_position},
    {"native_set_loop",      "(III)I",   (void *)android_media_AudioTrack_set_loop},
    {"native_reload_static", "()I",      (void *)android_media_AudioTrack_reload},
    {"native_get_output_sample_rate",
                             "(I)I",      (void *)android_media_AudioTrack_get_output_sample_rate},
    {"native_get_min_buff_size",
                             "(III)I",   (void *)android_media_AudioTrack_get_min_buff_size},
    {"native_setAuxEffectSendLevel",
                             "(F)V",     (void *)android_media_AudioTrack_setAuxEffectSendLevel},
    {"native_attachAuxEffect",
                             "(I)I",     (void *)android_media_AudioTrack_attachAuxEffect},
};


// field names found in android/media/AudioTrack.java
#define JAVA_POSTEVENT_CALLBACK_NAME                    "postEventFromNative"
#define JAVA_CONST_PCM16_NAME                           "ENCODING_PCM_16BIT"
#define JAVA_CONST_PCM8_NAME                            "ENCODING_PCM_8BIT"
#define JAVA_CONST_BUFFER_COUNT_NAME                    "BUFFER_COUNT"
#define JAVA_CONST_STREAM_VOICE_CALL_NAME               "STREAM_VOICE_CALL"
#define JAVA_CONST_STREAM_SYSTEM_NAME                   "STREAM_SYSTEM"
#define JAVA_CONST_STREAM_RING_NAME                     "STREAM_RING"
#define JAVA_CONST_STREAM_MUSIC_NAME                    "STREAM_MUSIC"
#define JAVA_CONST_STREAM_ALARM_NAME                    "STREAM_ALARM"
#define JAVA_CONST_STREAM_NOTIFICATION_NAME             "STREAM_NOTIFICATION"
#define JAVA_CONST_STREAM_BLUETOOTH_SCO_NAME            "STREAM_BLUETOOTH_SCO"
#define JAVA_CONST_STREAM_DTMF_NAME                     "STREAM_DTMF"
#define JAVA_CONST_MODE_STREAM_NAME                     "MODE_STREAM"
#define JAVA_CONST_MODE_STATIC_NAME                     "MODE_STATIC"
#define JAVA_NATIVETRACKINJAVAOBJ_FIELD_NAME            "mNativeTrackInJavaObj"
#define JAVA_JNIDATA_FIELD_NAME                         "mJniData"

#define JAVA_AUDIOFORMAT_CLASS_NAME             "android/media/AudioFormat"
#define JAVA_AUDIOMANAGER_CLASS_NAME            "android/media/AudioManager"

// ----------------------------------------------------------------------------
// preconditions:
//    theClass is valid
bool android_media_getIntConstantFromClass(JNIEnv* pEnv, jclass theClass, const char* className,
                             const char* constName, int* constVal) {
    jfieldID javaConst = NULL;
    javaConst = pEnv->GetStaticFieldID(theClass, constName, "I");
    if (javaConst != NULL) {
        *constVal = pEnv->GetStaticIntField(theClass, javaConst);
        return true;
    } else {
<<<<<<< HEAD
        ALOGE("Can't find %s.%s", className, constName);
=======
        LOGE("Can't find %s.%s", className, constName);
>>>>>>> upstream/master
        return false;
    }
}


// ----------------------------------------------------------------------------
int register_android_media_AudioTrack(JNIEnv *env)
{
    javaAudioTrackFields.nativeTrackInJavaObj = NULL;
    javaAudioTrackFields.postNativeEventInJava = NULL;

    // Get the AudioTrack class
    jclass audioTrackClass = env->FindClass(kClassPathName);
    if (audioTrackClass == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find %s", kClassPathName);
=======
        LOGE("Can't find %s", kClassPathName);
>>>>>>> upstream/master
        return -1;
    }

    // Get the postEvent method
    javaAudioTrackFields.postNativeEventInJava = env->GetStaticMethodID(
            audioTrackClass,
            JAVA_POSTEVENT_CALLBACK_NAME, "(Ljava/lang/Object;IIILjava/lang/Object;)V");
    if (javaAudioTrackFields.postNativeEventInJava == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find AudioTrack.%s", JAVA_POSTEVENT_CALLBACK_NAME);
=======
        LOGE("Can't find AudioTrack.%s", JAVA_POSTEVENT_CALLBACK_NAME);
>>>>>>> upstream/master
        return -1;
    }

    // Get the variables fields
    //      nativeTrackInJavaObj
    javaAudioTrackFields.nativeTrackInJavaObj = env->GetFieldID(
            audioTrackClass,
            JAVA_NATIVETRACKINJAVAOBJ_FIELD_NAME, "I");
    if (javaAudioTrackFields.nativeTrackInJavaObj == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find AudioTrack.%s", JAVA_NATIVETRACKINJAVAOBJ_FIELD_NAME);
=======
        LOGE("Can't find AudioTrack.%s", JAVA_NATIVETRACKINJAVAOBJ_FIELD_NAME);
>>>>>>> upstream/master
        return -1;
    }
    //      jniData;
    javaAudioTrackFields.jniData = env->GetFieldID(
            audioTrackClass,
            JAVA_JNIDATA_FIELD_NAME, "I");
    if (javaAudioTrackFields.jniData == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find AudioTrack.%s", JAVA_JNIDATA_FIELD_NAME);
=======
        LOGE("Can't find AudioTrack.%s", JAVA_JNIDATA_FIELD_NAME);
>>>>>>> upstream/master
        return -1;
    }

    // Get the memory mode constants
    if ( !android_media_getIntConstantFromClass(env, audioTrackClass,
<<<<<<< HEAD
               kClassPathName,
               JAVA_CONST_MODE_STATIC_NAME, &(javaAudioTrackFields.MODE_STATIC))
         || !android_media_getIntConstantFromClass(env, audioTrackClass,
               kClassPathName,
               JAVA_CONST_MODE_STREAM_NAME, &(javaAudioTrackFields.MODE_STREAM)) ) {
        // error log performed in android_media_getIntConstantFromClass()
=======
               kClassPathName, 
               JAVA_CONST_MODE_STATIC_NAME, &(javaAudioTrackFields.MODE_STATIC))
         || !android_media_getIntConstantFromClass(env, audioTrackClass,
               kClassPathName, 
               JAVA_CONST_MODE_STREAM_NAME, &(javaAudioTrackFields.MODE_STREAM)) ) {
        // error log performed in android_media_getIntConstantFromClass() 
>>>>>>> upstream/master
        return -1;
    }

    // Get the format constants from the AudioFormat class
    jclass audioFormatClass = NULL;
    audioFormatClass = env->FindClass(JAVA_AUDIOFORMAT_CLASS_NAME);
    if (audioFormatClass == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find %s", JAVA_AUDIOFORMAT_CLASS_NAME);
        return -1;
    }
    if ( !android_media_getIntConstantFromClass(env, audioFormatClass,
                JAVA_AUDIOFORMAT_CLASS_NAME,
                JAVA_CONST_PCM16_NAME, &(javaAudioTrackFields.PCM16))
           || !android_media_getIntConstantFromClass(env, audioFormatClass,
                JAVA_AUDIOFORMAT_CLASS_NAME,
                JAVA_CONST_PCM8_NAME, &(javaAudioTrackFields.PCM8)) ) {
        // error log performed in android_media_getIntConstantFromClass()
        return -1;
    }
=======
        LOGE("Can't find %s", JAVA_AUDIOFORMAT_CLASS_NAME);
        return -1;
    }
    if ( !android_media_getIntConstantFromClass(env, audioFormatClass, 
                JAVA_AUDIOFORMAT_CLASS_NAME, 
                JAVA_CONST_PCM16_NAME, &(javaAudioTrackFields.PCM16))
           || !android_media_getIntConstantFromClass(env, audioFormatClass, 
                JAVA_AUDIOFORMAT_CLASS_NAME, 
                JAVA_CONST_PCM8_NAME, &(javaAudioTrackFields.PCM8)) ) {
        // error log performed in android_media_getIntConstantFromClass() 
        return -1;
    }
 
    // Get the stream types from the AudioManager class
    jclass audioManagerClass = NULL;
    audioManagerClass = env->FindClass(JAVA_AUDIOMANAGER_CLASS_NAME);
    if (audioManagerClass == NULL) {
       LOGE("Can't find %s", JAVA_AUDIOMANAGER_CLASS_NAME);
       return -1;
    }
    if ( !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_VOICE_CALL_NAME, &(javaAudioTrackFields.STREAM_VOICE_CALL))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_MUSIC_NAME, &(javaAudioTrackFields.STREAM_MUSIC))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_SYSTEM_NAME, &(javaAudioTrackFields.STREAM_SYSTEM))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_RING_NAME, &(javaAudioTrackFields.STREAM_RING))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_ALARM_NAME, &(javaAudioTrackFields.STREAM_ALARM))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_NOTIFICATION_NAME, &(javaAudioTrackFields.STREAM_NOTIFICATION))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_BLUETOOTH_SCO_NAME, &(javaAudioTrackFields.STREAM_BLUETOOTH_SCO))
          || !android_media_getIntConstantFromClass(env, audioManagerClass,
               JAVA_AUDIOMANAGER_CLASS_NAME,
               JAVA_CONST_STREAM_DTMF_NAME, &(javaAudioTrackFields.STREAM_DTMF))) {
       // error log performed in android_media_getIntConstantFromClass()
       return -1;
    }
>>>>>>> upstream/master

    return AndroidRuntime::registerNativeMethods(env, kClassPathName, gMethods, NELEM(gMethods));
}


// ----------------------------------------------------------------------------
