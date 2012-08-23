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

#include <stdio.h>

//#define LOG_NDEBUG 0
#define LOG_TAG "SoundPool-JNI"

#include <utils/Log.h>
#include <nativehelper/jni.h>
#include <nativehelper/JNIHelp.h>
#include <android_runtime/AndroidRuntime.h>
<<<<<<< HEAD
#include <media/SoundPool.h>
=======
#include "SoundPool.h"
>>>>>>> upstream/master

using namespace android;

static struct fields_t {
    jfieldID    mNativeContext;
    jmethodID   mPostEvent;
    jclass      mSoundPoolClass;
} fields;

static inline SoundPool* MusterSoundPool(JNIEnv *env, jobject thiz) {
    return (SoundPool*)env->GetIntField(thiz, fields.mNativeContext);
}

// ----------------------------------------------------------------------------
static int
android_media_SoundPool_load_URL(JNIEnv *env, jobject thiz, jstring path, jint priority)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_load_URL");
=======
    LOGV("android_media_SoundPool_load_URL");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (path == NULL) {
        jniThrowException(env, "java/lang/IllegalArgumentException", NULL);
        return 0;
    }
    const char* s = env->GetStringUTFChars(path, NULL);
    int id = ap->load(s, priority);
    env->ReleaseStringUTFChars(path, s);
    return id;
}

static int
android_media_SoundPool_load_FD(JNIEnv *env, jobject thiz, jobject fileDescriptor,
        jlong offset, jlong length, jint priority)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_load_FD");
=======
    LOGV("android_media_SoundPool_load_FD");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->load(jniGetFDFromFileDescriptor(env, fileDescriptor),
            int64_t(offset), int64_t(length), int(priority));
}

static bool
android_media_SoundPool_unload(JNIEnv *env, jobject thiz, jint sampleID) {
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_unload\n");
=======
    LOGV("android_media_SoundPool_unload\n");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->unload(sampleID);
}

static int
android_media_SoundPool_play(JNIEnv *env, jobject thiz, jint sampleID,
        jfloat leftVolume, jfloat rightVolume, jint priority, jint loop,
        jfloat rate)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_play\n");
=======
    LOGV("android_media_SoundPool_play\n");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return 0;
    return ap->play(sampleID, leftVolume, rightVolume, priority, loop, rate);
}

static void
android_media_SoundPool_pause(JNIEnv *env, jobject thiz, jint channelID)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_pause");
=======
    LOGV("android_media_SoundPool_pause");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->pause(channelID);
}

static void
android_media_SoundPool_resume(JNIEnv *env, jobject thiz, jint channelID)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_resume");
=======
    LOGV("android_media_SoundPool_resume");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->resume(channelID);
}

static void
android_media_SoundPool_autoPause(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_autoPause");
=======
    LOGV("android_media_SoundPool_autoPause");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->autoPause();
}

static void
android_media_SoundPool_autoResume(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_autoResume");
=======
    LOGV("android_media_SoundPool_autoResume");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->autoResume();
}

static void
android_media_SoundPool_stop(JNIEnv *env, jobject thiz, jint channelID)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_stop");
=======
    LOGV("android_media_SoundPool_stop");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->stop(channelID);
}

static void
android_media_SoundPool_setVolume(JNIEnv *env, jobject thiz, jint channelID,
        float leftVolume, float rightVolume)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setVolume");
=======
    LOGV("android_media_SoundPool_setVolume");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setVolume(channelID, leftVolume, rightVolume);
}

static void
android_media_SoundPool_setPriority(JNIEnv *env, jobject thiz, jint channelID,
        int priority)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setPriority");
=======
    LOGV("android_media_SoundPool_setPriority");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setPriority(channelID, priority);
}

static void
android_media_SoundPool_setLoop(JNIEnv *env, jobject thiz, jint channelID,
        int loop)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setLoop");
=======
    LOGV("android_media_SoundPool_setLoop");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setLoop(channelID, loop);
}

static void
android_media_SoundPool_setRate(JNIEnv *env, jobject thiz, jint channelID,
        float rate)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_setRate");
=======
    LOGV("android_media_SoundPool_setRate");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap == NULL) return;
    ap->setRate(channelID, rate);
}

static void android_media_callback(SoundPoolEvent event, SoundPool* soundPool, void* user)
{
<<<<<<< HEAD
    ALOGV("callback: (%d, %d, %d, %p, %p)", event.mMsg, event.mArg1, event.mArg2, soundPool, user);
=======
    LOGV("callback: (%d, %d, %d, %p, %p)", event.mMsg, event.mArg1, event.mArg2, soundPool, user);
>>>>>>> upstream/master
    JNIEnv *env = AndroidRuntime::getJNIEnv();
    env->CallStaticVoidMethod(fields.mSoundPoolClass, fields.mPostEvent, user, event.mMsg, event.mArg1, event.mArg2, NULL);
}

static jint
android_media_SoundPool_native_setup(JNIEnv *env, jobject thiz, jobject weakRef, jint maxChannels, jint streamType, jint srcQuality)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_native_setup");
    SoundPool *ap = new SoundPool(maxChannels, (audio_stream_type_t) streamType, srcQuality);
=======
    LOGV("android_media_SoundPool_native_setup");
    SoundPool *ap = new SoundPool(maxChannels, streamType, srcQuality);
>>>>>>> upstream/master
    if (ap == NULL) {
        return -1;
    }

    // save pointer to SoundPool C++ object in opaque field in Java object
    env->SetIntField(thiz, fields.mNativeContext, (int)ap);

    // set callback with weak reference
    jobject globalWeakRef = env->NewGlobalRef(weakRef);
    ap->setCallback(android_media_callback, globalWeakRef);
    return 0;
}

static void
android_media_SoundPool_release(JNIEnv *env, jobject thiz)
{
<<<<<<< HEAD
    ALOGV("android_media_SoundPool_release");
=======
    LOGV("android_media_SoundPool_release");
>>>>>>> upstream/master
    SoundPool *ap = MusterSoundPool(env, thiz);
    if (ap != NULL) {

        // release weak reference
        jobject weakRef = (jobject) ap->getUserData();
        if (weakRef != NULL) {
            env->DeleteGlobalRef(weakRef);
        }

        // clear callback and native context
        ap->setCallback(NULL, NULL);
        env->SetIntField(thiz, fields.mNativeContext, 0);
        delete ap;
    }
}

// ----------------------------------------------------------------------------

// Dalvik VM type signatures
static JNINativeMethod gMethods[] = {
    {   "_load",
        "(Ljava/lang/String;I)I",
        (void *)android_media_SoundPool_load_URL
    },
    {   "_load",
        "(Ljava/io/FileDescriptor;JJI)I",
        (void *)android_media_SoundPool_load_FD
    },
    {   "unload",
        "(I)Z",
        (void *)android_media_SoundPool_unload
    },
    {   "play",
        "(IFFIIF)I",
        (void *)android_media_SoundPool_play
    },
    {   "pause",
        "(I)V",
        (void *)android_media_SoundPool_pause
    },
    {   "resume",
        "(I)V",
        (void *)android_media_SoundPool_resume
    },
    {   "autoPause",
        "()V",
        (void *)android_media_SoundPool_autoPause
    },
    {   "autoResume",
        "()V",
        (void *)android_media_SoundPool_autoResume
    },
    {   "stop",
        "(I)V",
        (void *)android_media_SoundPool_stop
    },
    {   "setVolume",
        "(IFF)V",
        (void *)android_media_SoundPool_setVolume
    },
    {   "setPriority",
        "(II)V",
        (void *)android_media_SoundPool_setPriority
    },
    {   "setLoop",
        "(II)V",
        (void *)android_media_SoundPool_setLoop
    },
    {   "setRate",
        "(IF)V",
        (void *)android_media_SoundPool_setRate
    },
    {   "native_setup",
        "(Ljava/lang/Object;III)I",
        (void*)android_media_SoundPool_native_setup
    },
    {   "release",
        "()V",
        (void*)android_media_SoundPool_release
    }
};

static const char* const kClassPathName = "android/media/SoundPool";

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;
    jclass clazz;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
<<<<<<< HEAD
        ALOGE("ERROR: GetEnv failed\n");
=======
        LOGE("ERROR: GetEnv failed\n");
>>>>>>> upstream/master
        goto bail;
    }
    assert(env != NULL);

    clazz = env->FindClass(kClassPathName);
    if (clazz == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find %s", kClassPathName);
=======
        LOGE("Can't find %s", kClassPathName);
>>>>>>> upstream/master
        goto bail;
    }

    fields.mNativeContext = env->GetFieldID(clazz, "mNativeContext", "I");
    if (fields.mNativeContext == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find SoundPool.mNativeContext");
=======
        LOGE("Can't find SoundPool.mNativeContext");
>>>>>>> upstream/master
        goto bail;
    }

    fields.mPostEvent = env->GetStaticMethodID(clazz, "postEventFromNative",
                                               "(Ljava/lang/Object;IIILjava/lang/Object;)V");
    if (fields.mPostEvent == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/media/SoundPool.postEventFromNative");
=======
        LOGE("Can't find android/media/SoundPool.postEventFromNative");
>>>>>>> upstream/master
        goto bail;
    }

    // create a reference to class. Technically, we're leaking this reference
    // since it's a static object.
    fields.mSoundPoolClass = (jclass) env->NewGlobalRef(clazz);

    if (AndroidRuntime::registerNativeMethods(env, kClassPathName, gMethods, NELEM(gMethods)) < 0)
        goto bail;

    /* success -- return valid version number */
    result = JNI_VERSION_1_4;

bail:
    return result;
}
