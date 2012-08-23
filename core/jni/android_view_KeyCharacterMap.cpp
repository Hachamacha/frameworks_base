/*
 * Copyright 2006, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
*/

<<<<<<< HEAD
#include <android_runtime/AndroidRuntime.h>

#include <androidfw/KeyCharacterMap.h>
#include <androidfw/Input.h>
#include <binder/Parcel.h>

#include <nativehelper/jni.h>
#include <nativehelper/JNIHelp.h>

#include "android_os_Parcel.h"
=======
#include <ui/KeyCharacterMap.h>
#include <ui/Input.h>

#include <android_runtime/AndroidRuntime.h>
#include <nativehelper/jni.h>
#include <nativehelper/JNIHelp.h>

>>>>>>> upstream/master
#include "android_view_KeyEvent.h"

namespace android {

static struct {
    jclass clazz;
<<<<<<< HEAD
    jmethodID ctor;
} gKeyCharacterMapClassInfo;

static struct {
    jclass clazz;
=======
>>>>>>> upstream/master
} gKeyEventClassInfo;

static struct {
    jfieldID keyCode;
    jfieldID metaState;
} gFallbackActionClassInfo;


<<<<<<< HEAD
class NativeKeyCharacterMap {
public:
    NativeKeyCharacterMap(int32_t deviceId, const sp<KeyCharacterMap>& map) :
        mDeviceId(deviceId), mMap(map) {
    }

    ~NativeKeyCharacterMap() {
    }

    inline int32_t getDeviceId() const {
        return mDeviceId;
    }

    inline const sp<KeyCharacterMap>& getMap() const {
        return mMap;
    }

private:
    int32_t mDeviceId;
    sp<KeyCharacterMap> mMap;
};


jobject android_view_KeyCharacterMap_create(JNIEnv* env, int32_t deviceId,
        const sp<KeyCharacterMap>& kcm) {
    NativeKeyCharacterMap* map = new NativeKeyCharacterMap(deviceId,
            kcm.get() ? kcm : KeyCharacterMap::empty());
    if (!map) {
        return NULL;
    }

    return env->NewObject(gKeyCharacterMapClassInfo.clazz, gKeyCharacterMapClassInfo.ctor,
            reinterpret_cast<jint>(map));
}

static jint nativeReadFromParcel(JNIEnv *env, jobject clazz, jobject parcelObj) {
    Parcel* parcel = parcelForJavaObject(env, parcelObj);
    if (!parcel) {
        return 0;
    }

    int32_t deviceId = parcel->readInt32();
    if (parcel->errorCheck()) {
        return 0;
    }

    sp<KeyCharacterMap> kcm = KeyCharacterMap::readFromParcel(parcel);
    if (!kcm.get()) {
        return 0;
    }

    NativeKeyCharacterMap* map = new NativeKeyCharacterMap(deviceId, kcm);
    return reinterpret_cast<jint>(map);
}

static void nativeWriteToParcel(JNIEnv* env, jobject clazz, jint ptr, jobject parcelObj) {
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
    Parcel* parcel = parcelForJavaObject(env, parcelObj);
    if (parcel) {
        parcel->writeInt32(map->getDeviceId());
        map->getMap()->writeToParcel(parcel);
    }
}

static void nativeDispose(JNIEnv *env, jobject clazz, jint ptr) {
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
=======
static jint nativeLoad(JNIEnv *env, jobject clazz, jstring fileStr) {
    const char* file = env->GetStringUTFChars(fileStr, NULL);

    KeyCharacterMap* map;
    status_t status = KeyCharacterMap::load(String8(file), &map);
    jint result;
    if (status) {
        String8 msg;
        msg.appendFormat("Could not load key character map '%s' due to error %d.  "
                "Refer to the log for details.", file, status);
        jniThrowException(env, "android/view/KeyCharacterMap$KeyCharacterMapUnavailableException",
                msg.string());
        result = 0;
    } else {
        result = reinterpret_cast<jint>(map);
    }

    env->ReleaseStringUTFChars(fileStr, file);
    return result;
}

static void nativeDispose(JNIEnv *env, jobject clazz, jint ptr) {
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
>>>>>>> upstream/master
    delete map;
}

static jchar nativeGetCharacter(JNIEnv *env, jobject clazz, jint ptr,
        jint keyCode, jint metaState) {
<<<<<<< HEAD
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
    return map->getMap()->getCharacter(keyCode, metaState);
=======
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
    return map->getCharacter(keyCode, metaState);
>>>>>>> upstream/master
}

static jboolean nativeGetFallbackAction(JNIEnv *env, jobject clazz, jint ptr, jint keyCode,
        jint metaState, jobject fallbackActionObj) {
<<<<<<< HEAD
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
    KeyCharacterMap::FallbackAction fallbackAction;

    bool result = map->getMap()->getFallbackAction(keyCode, metaState, &fallbackAction);
=======
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
    KeyCharacterMap::FallbackAction fallbackAction;

    bool result = map->getFallbackAction(keyCode, metaState, &fallbackAction);
>>>>>>> upstream/master
    if (result) {
        env->SetIntField(fallbackActionObj, gFallbackActionClassInfo.keyCode,
                fallbackAction.keyCode);
        env->SetIntField(fallbackActionObj, gFallbackActionClassInfo.metaState,
                fallbackAction.metaState);
    }
    return result;
}

static jchar nativeGetNumber(JNIEnv *env, jobject clazz, jint ptr, jint keyCode) {
<<<<<<< HEAD
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
    return map->getMap()->getNumber(keyCode);
=======
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
    return map->getNumber(keyCode);
>>>>>>> upstream/master
}

static jchar nativeGetMatch(JNIEnv *env, jobject clazz, jint ptr, jint keyCode,
        jcharArray charsArray, jint metaState) {
<<<<<<< HEAD
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
=======
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
>>>>>>> upstream/master

    jsize numChars = env->GetArrayLength(charsArray);
    jchar* chars = static_cast<jchar*>(env->GetPrimitiveArrayCritical(charsArray, NULL));
    if (!chars) {
        return 0;
    }

<<<<<<< HEAD
    char16_t result = map->getMap()->getMatch(keyCode, chars, size_t(numChars), metaState);
=======
    char16_t result = map->getMatch(keyCode, chars, size_t(numChars), metaState);
>>>>>>> upstream/master

    env->ReleasePrimitiveArrayCritical(charsArray, chars, JNI_ABORT);
    return result;
}

static jchar nativeGetDisplayLabel(JNIEnv *env, jobject clazz, jint ptr, jint keyCode) {
<<<<<<< HEAD
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
    return map->getMap()->getDisplayLabel(keyCode);
}

static jint nativeGetKeyboardType(JNIEnv *env, jobject clazz, jint ptr) {
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
    return map->getMap()->getKeyboardType();
}

static jobjectArray nativeGetEvents(JNIEnv *env, jobject clazz, jint ptr,
        jcharArray charsArray) {
    NativeKeyCharacterMap* map = reinterpret_cast<NativeKeyCharacterMap*>(ptr);
=======
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
    return map->getDisplayLabel(keyCode);
}

static jint nativeGetKeyboardType(JNIEnv *env, jobject clazz, jint ptr) {
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
    return map->getKeyboardType();
}

static jobjectArray nativeGetEvents(JNIEnv *env, jobject clazz, jint ptr, jint deviceId,
        jcharArray charsArray) {
    KeyCharacterMap* map = reinterpret_cast<KeyCharacterMap*>(ptr);
>>>>>>> upstream/master

    jchar* chars = env->GetCharArrayElements(charsArray, NULL);
    if (!chars) {
        return NULL;
    }
    jsize numChars = env->GetArrayLength(charsArray);

    Vector<KeyEvent> events;
    jobjectArray result = NULL;
<<<<<<< HEAD
    if (map->getMap()->getEvents(map->getDeviceId(), chars, size_t(numChars), events)) {
=======
    if (map->getEvents(deviceId, chars, size_t(numChars), events)) {
>>>>>>> upstream/master
        result = env->NewObjectArray(jsize(events.size()), gKeyEventClassInfo.clazz, NULL);
        if (result) {
            for (size_t i = 0; i < events.size(); i++) {
                jobject keyEventObj = android_view_KeyEvent_fromNative(env, &events.itemAt(i));
                if (!keyEventObj) break; // threw OOM exception
                env->SetObjectArrayElement(result, jsize(i), keyEventObj);
                env->DeleteLocalRef(keyEventObj);
            }
        }
    }

    env->ReleaseCharArrayElements(charsArray, chars, JNI_ABORT);
    return result;
}


/*
 * JNI registration.
 */

static JNINativeMethod g_methods[] = {
    /* name, signature, funcPtr */
<<<<<<< HEAD
    { "nativeReadFromParcel", "(Landroid/os/Parcel;)I",
            (void*)nativeReadFromParcel },
    { "nativeWriteToParcel", "(ILandroid/os/Parcel;)V",
            (void*)nativeWriteToParcel },
=======
    { "nativeLoad", "(Ljava/lang/String;)I",
            (void*)nativeLoad },
>>>>>>> upstream/master
    { "nativeDispose", "(I)V",
            (void*)nativeDispose },
    { "nativeGetCharacter", "(III)C",
            (void*)nativeGetCharacter },
    { "nativeGetFallbackAction", "(IIILandroid/view/KeyCharacterMap$FallbackAction;)Z",
            (void*)nativeGetFallbackAction },
    { "nativeGetNumber", "(II)C",
            (void*)nativeGetNumber },
    { "nativeGetMatch", "(II[CI)C",
            (void*)nativeGetMatch },
    { "nativeGetDisplayLabel", "(II)C",
            (void*)nativeGetDisplayLabel },
    { "nativeGetKeyboardType", "(I)I",
            (void*)nativeGetKeyboardType },
<<<<<<< HEAD
    { "nativeGetEvents", "(I[C)[Landroid/view/KeyEvent;",
=======
    { "nativeGetEvents", "(II[C)[Landroid/view/KeyEvent;",
>>>>>>> upstream/master
            (void*)nativeGetEvents },
};

#define FIND_CLASS(var, className) \
        var = env->FindClass(className); \
        LOG_FATAL_IF(! var, "Unable to find class " className);

<<<<<<< HEAD
#define GET_METHOD_ID(var, clazz, methodName, methodDescriptor) \
        var = env->GetMethodID(clazz, methodName, methodDescriptor); \
        LOG_FATAL_IF(! var, "Unable to find method " methodName);

=======
>>>>>>> upstream/master
#define GET_FIELD_ID(var, clazz, fieldName, fieldDescriptor) \
        var = env->GetFieldID(clazz, fieldName, fieldDescriptor); \
        LOG_FATAL_IF(! var, "Unable to find field " fieldName);

<<<<<<< HEAD
int register_android_view_KeyCharacterMap(JNIEnv* env)
{
    FIND_CLASS(gKeyCharacterMapClassInfo.clazz, "android/view/KeyCharacterMap");
    gKeyCharacterMapClassInfo.clazz = jclass(env->NewGlobalRef(gKeyCharacterMapClassInfo.clazz));

    GET_METHOD_ID(gKeyCharacterMapClassInfo.ctor, gKeyCharacterMapClassInfo.clazz,
            "<init>", "(I)V");

=======
int register_android_text_KeyCharacterMap(JNIEnv* env)
{
>>>>>>> upstream/master
    FIND_CLASS(gKeyEventClassInfo.clazz, "android/view/KeyEvent");
    gKeyEventClassInfo.clazz = jclass(env->NewGlobalRef(gKeyEventClassInfo.clazz));

    jclass clazz;
    FIND_CLASS(clazz, "android/view/KeyCharacterMap$FallbackAction");

    GET_FIELD_ID(gFallbackActionClassInfo.keyCode, clazz,
            "keyCode", "I");

    GET_FIELD_ID(gFallbackActionClassInfo.metaState, clazz,
            "metaState", "I");

    return AndroidRuntime::registerNativeMethods(env,
            "android/view/KeyCharacterMap", g_methods, NELEM(g_methods));
}

}; // namespace android
