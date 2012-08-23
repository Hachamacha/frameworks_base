<<<<<<< HEAD
/*
=======
/* //device/libs/android_runtime/android_media_AudioSystem.cpp
>>>>>>> upstream/master
 **
 ** Copyright 2008, The Android Open Source Project
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 **
 **     http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 */

#define LOG_TAG "ToneGenerator"

#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>

<<<<<<< HEAD
#include <jni.h>
#include <JNIHelp.h>
#include <android_runtime/AndroidRuntime.h>

#include <utils/Log.h>
#include <media/AudioSystem.h>
#include <media/ToneGenerator.h>
=======
#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"

#include "utils/Log.h"
#include "media/AudioSystem.h"
#include "media/ToneGenerator.h"
>>>>>>> upstream/master

// ----------------------------------------------------------------------------

using namespace android;

struct fields_t {
    jfieldID context;
};
static fields_t fields;

static jboolean android_media_ToneGenerator_startTone(JNIEnv *env, jobject thiz, jint toneType, jint durationMs) {
<<<<<<< HEAD
    ALOGV("android_media_ToneGenerator_startTone: %x", (int)thiz);
=======
    LOGV("android_media_ToneGenerator_startTone: %x\n", (int)thiz);
>>>>>>> upstream/master

    ToneGenerator *lpToneGen = (ToneGenerator *)env->GetIntField(thiz,
            fields.context);
    if (lpToneGen == NULL) {
        jniThrowRuntimeException(env, "Method called after release()");
        return false;
    }

<<<<<<< HEAD
    return lpToneGen->startTone((ToneGenerator::tone_type) toneType, durationMs);
}

static void android_media_ToneGenerator_stopTone(JNIEnv *env, jobject thiz) {
    ALOGV("android_media_ToneGenerator_stopTone: %x", (int)thiz);
=======
    return lpToneGen->startTone(toneType, durationMs);
}

static void android_media_ToneGenerator_stopTone(JNIEnv *env, jobject thiz) {
    LOGV("android_media_ToneGenerator_stopTone: %x\n", (int)thiz);
>>>>>>> upstream/master

    ToneGenerator *lpToneGen = (ToneGenerator *)env->GetIntField(thiz,
            fields.context);

<<<<<<< HEAD
    ALOGV("ToneGenerator lpToneGen: %x", (unsigned int)lpToneGen);
=======
    LOGV("ToneGenerator lpToneGen: %x\n", (unsigned int)lpToneGen);
>>>>>>> upstream/master
    if (lpToneGen == NULL) {
        jniThrowRuntimeException(env, "Method called after release()");
        return;
    }
    lpToneGen->stopTone();
}

<<<<<<< HEAD
static jint android_media_ToneGenerator_getAudioSessionId(JNIEnv *env, jobject thiz) {
    ToneGenerator *lpToneGen = (ToneGenerator *)env->GetIntField(thiz,
            fields.context);
    if (lpToneGen == NULL) {
        jniThrowRuntimeException(env, "Method called after release()");
        return 0;
    }
    return lpToneGen->getSessionId();
}

static void android_media_ToneGenerator_release(JNIEnv *env, jobject thiz) {
    ToneGenerator *lpToneGen = (ToneGenerator *)env->GetIntField(thiz,
            fields.context);
    ALOGV("android_media_ToneGenerator_release lpToneGen: %x", (int)lpToneGen);

    env->SetIntField(thiz, fields.context, 0);

    delete lpToneGen;
=======
static void android_media_ToneGenerator_release(JNIEnv *env, jobject thiz) {
    ToneGenerator *lpToneGen = (ToneGenerator *)env->GetIntField(thiz,
            fields.context);
    LOGV("android_media_ToneGenerator_release lpToneGen: %x\n", (int)lpToneGen);

    env->SetIntField(thiz, fields.context, 0);

    if (lpToneGen) {
        delete lpToneGen;
    }
>>>>>>> upstream/master
}

static void android_media_ToneGenerator_native_setup(JNIEnv *env, jobject thiz,
        jint streamType, jint volume) {
<<<<<<< HEAD
    ToneGenerator *lpToneGen = new ToneGenerator((audio_stream_type_t) streamType, AudioSystem::linearToLog(volume), true);

    env->SetIntField(thiz, fields.context, 0);

    ALOGV("android_media_ToneGenerator_native_setup jobject: %x", (int)thiz);

    if (lpToneGen == NULL) {
        ALOGE("ToneGenerator creation failed");
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }
    ALOGV("ToneGenerator lpToneGen: %x", (unsigned int)lpToneGen);

    if (!lpToneGen->isInited()) {
        ALOGE("ToneGenerator init failed");
=======
    ToneGenerator *lpToneGen = new ToneGenerator(streamType, AudioSystem::linearToLog(volume), true);

    env->SetIntField(thiz, fields.context, 0);

    LOGV("android_media_ToneGenerator_native_setup jobject: %x\n", (int)thiz);

    if (lpToneGen == NULL) {
        LOGE("ToneGenerator creation failed \n");
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }
    LOGV("ToneGenerator lpToneGen: %x\n", (unsigned int)lpToneGen);

    if (!lpToneGen->isInited()) {
        LOGE("ToneGenerator init failed \n");
>>>>>>> upstream/master
        jniThrowRuntimeException(env, "Init failed");
        return;
    }

    // Stow our new C++ ToneGenerator in an opaque field in the Java object.
    env->SetIntField(thiz, fields.context, (int)lpToneGen);

<<<<<<< HEAD
    ALOGV("ToneGenerator fields.context: %x", env->GetIntField(thiz, fields.context));
=======
    LOGV("ToneGenerator fields.context: %x\n", env->GetIntField(thiz, fields.context));
>>>>>>> upstream/master
}

static void android_media_ToneGenerator_native_finalize(JNIEnv *env,
        jobject thiz) {
<<<<<<< HEAD
    ALOGV("android_media_ToneGenerator_native_finalize jobject: %x", (int)thiz);
=======
    LOGV("android_media_ToneGenerator_native_finalize jobject: %x\n", (int)thiz);
>>>>>>> upstream/master

    ToneGenerator *lpToneGen = (ToneGenerator *)env->GetIntField(thiz,
            fields.context);

<<<<<<< HEAD
    if (lpToneGen != NULL) {
        ALOGV("delete lpToneGen: %p", lpToneGen);
=======
    if (lpToneGen) {
        LOGV("delete lpToneGen: %x\n", (int)lpToneGen);
>>>>>>> upstream/master
        delete lpToneGen;
    }
}

// ----------------------------------------------------------------------------

static JNINativeMethod gMethods[] = {
    { "startTone", "(II)Z", (void *)android_media_ToneGenerator_startTone },
    { "stopTone", "()V", (void *)android_media_ToneGenerator_stopTone },
<<<<<<< HEAD
    { "getAudioSessionId", "()I", (void *)android_media_ToneGenerator_getAudioSessionId},
=======
>>>>>>> upstream/master
    { "release", "()V", (void *)android_media_ToneGenerator_release },
    { "native_setup", "(II)V", (void *)android_media_ToneGenerator_native_setup },
    { "native_finalize", "()V", (void *)android_media_ToneGenerator_native_finalize }
};


int register_android_media_ToneGenerator(JNIEnv *env) {
    jclass clazz;

    clazz = env->FindClass("android/media/ToneGenerator");
    if (clazz == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find %s", "android/media/ToneGenerator");
=======
        LOGE("Can't find %s", "android/media/ToneGenerator");
>>>>>>> upstream/master
        return -1;
    }

    fields.context = env->GetFieldID(clazz, "mNativeContext", "I");
    if (fields.context == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find ToneGenerator.mNativeContext");
        return -1;
    }
    ALOGV("register_android_media_ToneGenerator ToneGenerator fields.context: %x", (unsigned int)fields.context);
=======
        LOGE("Can't find ToneGenerator.mNativeContext");
        return -1;
    }
    LOGV("register_android_media_ToneGenerator ToneGenerator fields.context: %x", (unsigned int)fields.context);
>>>>>>> upstream/master

    return AndroidRuntime::registerNativeMethods(env,
            "android/media/ToneGenerator", gMethods, NELEM(gMethods));
}
