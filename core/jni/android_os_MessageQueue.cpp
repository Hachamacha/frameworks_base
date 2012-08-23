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

#define LOG_TAG "MessageQueue-JNI"

#include "JNIHelp.h"
<<<<<<< HEAD
#include <android_runtime/AndroidRuntime.h>
=======
>>>>>>> upstream/master

#include <utils/Looper.h>
#include <utils/Log.h>
#include "android_os_MessageQueue.h"

namespace android {

<<<<<<< HEAD
=======
// ----------------------------------------------------------------------------

>>>>>>> upstream/master
static struct {
    jfieldID mPtr;   // native object attached to the DVM MessageQueue
} gMessageQueueClassInfo;

<<<<<<< HEAD

class NativeMessageQueue : public MessageQueue {
public:
    NativeMessageQueue();
    virtual ~NativeMessageQueue();

    virtual void raiseException(JNIEnv* env, const char* msg, jthrowable exceptionObj);

    void pollOnce(JNIEnv* env, int timeoutMillis);

    void wake();

private:
    bool mInCallback;
    jthrowable mExceptionObj;
};


MessageQueue::MessageQueue() {
}

MessageQueue::~MessageQueue() {
}

bool MessageQueue::raiseAndClearException(JNIEnv* env, const char* msg) {
    jthrowable exceptionObj = env->ExceptionOccurred();
    if (exceptionObj) {
        env->ExceptionClear();
        raiseException(env, msg, exceptionObj);
        env->DeleteLocalRef(exceptionObj);
        return true;
    }
    return false;
}

NativeMessageQueue::NativeMessageQueue() : mInCallback(false), mExceptionObj(NULL) {
=======
// ----------------------------------------------------------------------------

class NativeMessageQueue {
public:
    NativeMessageQueue();
    ~NativeMessageQueue();

    inline sp<Looper> getLooper() { return mLooper; }

    void pollOnce(int timeoutMillis);
    void wake();

private:
    sp<Looper> mLooper;
};

// ----------------------------------------------------------------------------

NativeMessageQueue::NativeMessageQueue() {
>>>>>>> upstream/master
    mLooper = Looper::getForThread();
    if (mLooper == NULL) {
        mLooper = new Looper(false);
        Looper::setForThread(mLooper);
    }
}

NativeMessageQueue::~NativeMessageQueue() {
}

<<<<<<< HEAD
void NativeMessageQueue::raiseException(JNIEnv* env, const char* msg, jthrowable exceptionObj) {
    if (exceptionObj) {
        if (mInCallback) {
            if (mExceptionObj) {
                env->DeleteLocalRef(mExceptionObj);
            }
            mExceptionObj = jthrowable(env->NewLocalRef(exceptionObj));
            ALOGE("Exception in MessageQueue callback: %s", msg);
            jniLogException(env, ANDROID_LOG_ERROR, LOG_TAG, exceptionObj);
        } else {
            ALOGE("Exception: %s", msg);
            jniLogException(env, ANDROID_LOG_ERROR, LOG_TAG, exceptionObj);
            LOG_ALWAYS_FATAL("raiseException() was called when not in a callback, exiting.");
        }
    }
}

void NativeMessageQueue::pollOnce(JNIEnv* env, int timeoutMillis) {
    mInCallback = true;
    mLooper->pollOnce(timeoutMillis);
    mInCallback = false;
    if (mExceptionObj) {
        env->Throw(mExceptionObj);
        env->DeleteLocalRef(mExceptionObj);
        mExceptionObj = NULL;
    }
=======
void NativeMessageQueue::pollOnce(int timeoutMillis) {
    mLooper->pollOnce(timeoutMillis);
>>>>>>> upstream/master
}

void NativeMessageQueue::wake() {
    mLooper->wake();
}

// ----------------------------------------------------------------------------

static NativeMessageQueue* android_os_MessageQueue_getNativeMessageQueue(JNIEnv* env,
        jobject messageQueueObj) {
    jint intPtr = env->GetIntField(messageQueueObj, gMessageQueueClassInfo.mPtr);
    return reinterpret_cast<NativeMessageQueue*>(intPtr);
}

static void android_os_MessageQueue_setNativeMessageQueue(JNIEnv* env, jobject messageQueueObj,
        NativeMessageQueue* nativeMessageQueue) {
    env->SetIntField(messageQueueObj, gMessageQueueClassInfo.mPtr,
             reinterpret_cast<jint>(nativeMessageQueue));
}

<<<<<<< HEAD
sp<MessageQueue> android_os_MessageQueue_getMessageQueue(JNIEnv* env, jobject messageQueueObj) {
    NativeMessageQueue* nativeMessageQueue =
            android_os_MessageQueue_getNativeMessageQueue(env, messageQueueObj);
    return nativeMessageQueue;
=======
sp<Looper> android_os_MessageQueue_getLooper(JNIEnv* env, jobject messageQueueObj) {
    NativeMessageQueue* nativeMessageQueue =
            android_os_MessageQueue_getNativeMessageQueue(env, messageQueueObj);
    return nativeMessageQueue != NULL ? nativeMessageQueue->getLooper() : NULL;
>>>>>>> upstream/master
}

static void android_os_MessageQueue_nativeInit(JNIEnv* env, jobject obj) {
    NativeMessageQueue* nativeMessageQueue = new NativeMessageQueue();
<<<<<<< HEAD
    if (!nativeMessageQueue) {
=======
    if (! nativeMessageQueue) {
>>>>>>> upstream/master
        jniThrowRuntimeException(env, "Unable to allocate native queue");
        return;
    }

<<<<<<< HEAD
    nativeMessageQueue->incStrong(env);
=======
>>>>>>> upstream/master
    android_os_MessageQueue_setNativeMessageQueue(env, obj, nativeMessageQueue);
}

static void android_os_MessageQueue_nativeDestroy(JNIEnv* env, jobject obj) {
    NativeMessageQueue* nativeMessageQueue =
            android_os_MessageQueue_getNativeMessageQueue(env, obj);
    if (nativeMessageQueue) {
        android_os_MessageQueue_setNativeMessageQueue(env, obj, NULL);
<<<<<<< HEAD
        nativeMessageQueue->decStrong(env);
=======
        delete nativeMessageQueue;
>>>>>>> upstream/master
    }
}

static void throwQueueNotInitialized(JNIEnv* env) {
    jniThrowException(env, "java/lang/IllegalStateException", "Message queue not initialized");
}

static void android_os_MessageQueue_nativePollOnce(JNIEnv* env, jobject obj,
        jint ptr, jint timeoutMillis) {
    NativeMessageQueue* nativeMessageQueue = reinterpret_cast<NativeMessageQueue*>(ptr);
<<<<<<< HEAD
    nativeMessageQueue->pollOnce(env, timeoutMillis);
=======
    nativeMessageQueue->pollOnce(timeoutMillis);
>>>>>>> upstream/master
}

static void android_os_MessageQueue_nativeWake(JNIEnv* env, jobject obj, jint ptr) {
    NativeMessageQueue* nativeMessageQueue = reinterpret_cast<NativeMessageQueue*>(ptr);
    return nativeMessageQueue->wake();
}

// ----------------------------------------------------------------------------

static JNINativeMethod gMessageQueueMethods[] = {
    /* name, signature, funcPtr */
    { "nativeInit", "()V", (void*)android_os_MessageQueue_nativeInit },
    { "nativeDestroy", "()V", (void*)android_os_MessageQueue_nativeDestroy },
    { "nativePollOnce", "(II)V", (void*)android_os_MessageQueue_nativePollOnce },
    { "nativeWake", "(I)V", (void*)android_os_MessageQueue_nativeWake }
};

#define FIND_CLASS(var, className) \
        var = env->FindClass(className); \
        LOG_FATAL_IF(! var, "Unable to find class " className);

#define GET_FIELD_ID(var, clazz, fieldName, fieldDescriptor) \
        var = env->GetFieldID(clazz, fieldName, fieldDescriptor); \
        LOG_FATAL_IF(! var, "Unable to find field " fieldName);

int register_android_os_MessageQueue(JNIEnv* env) {
    int res = jniRegisterNativeMethods(env, "android/os/MessageQueue",
            gMessageQueueMethods, NELEM(gMessageQueueMethods));
    LOG_FATAL_IF(res < 0, "Unable to register native methods.");

    jclass clazz;
    FIND_CLASS(clazz, "android/os/MessageQueue");

    GET_FIELD_ID(gMessageQueueClassInfo.mPtr, clazz,
            "mPtr", "I");
    
    return 0;
}

} // namespace android
