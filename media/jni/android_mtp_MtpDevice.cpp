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

// #define LOG_NDEBUG 0

#define LOG_TAG "MtpDeviceJNI"
#include "utils/Log.h"

#include <stdio.h>
#include <assert.h>
#include <limits.h>
#include <unistd.h>
#include <fcntl.h>

#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"
#include "private/android_filesystem_config.h"

#include "MtpTypes.h"
#include "MtpDevice.h"
#include "MtpDeviceInfo.h"
#include "MtpStorageInfo.h"
#include "MtpObjectInfo.h"

using namespace android;

// ----------------------------------------------------------------------------

static jfieldID field_context;

jclass clazz_deviceInfo;
jclass clazz_storageInfo;
jclass clazz_objectInfo;

jmethodID constructor_deviceInfo;
jmethodID constructor_storageInfo;
jmethodID constructor_objectInfo;

// MtpDeviceInfo fields
static jfieldID field_deviceInfo_manufacturer;
static jfieldID field_deviceInfo_model;
static jfieldID field_deviceInfo_version;
static jfieldID field_deviceInfo_serialNumber;

// MtpStorageInfo fields
static jfieldID field_storageInfo_storageId;
static jfieldID field_storageInfo_maxCapacity;
static jfieldID field_storageInfo_freeSpace;
static jfieldID field_storageInfo_description;
static jfieldID field_storageInfo_volumeIdentifier;

// MtpObjectInfo fields
static jfieldID field_objectInfo_handle;
static jfieldID field_objectInfo_storageId;
static jfieldID field_objectInfo_format;
static jfieldID field_objectInfo_protectionStatus;
static jfieldID field_objectInfo_compressedSize;
static jfieldID field_objectInfo_thumbFormat;
static jfieldID field_objectInfo_thumbCompressedSize;
static jfieldID field_objectInfo_thumbPixWidth;
static jfieldID field_objectInfo_thumbPixHeight;
static jfieldID field_objectInfo_imagePixWidth;
static jfieldID field_objectInfo_imagePixHeight;
static jfieldID field_objectInfo_imagePixDepth;
static jfieldID field_objectInfo_parent;
static jfieldID field_objectInfo_associationType;
static jfieldID field_objectInfo_associationDesc;
static jfieldID field_objectInfo_sequenceNumber;
static jfieldID field_objectInfo_name;
static jfieldID field_objectInfo_dateCreated;
static jfieldID field_objectInfo_dateModified;
static jfieldID field_objectInfo_keywords;

MtpDevice* get_device_from_object(JNIEnv* env, jobject javaDevice)
{
    return (MtpDevice*)env->GetIntField(javaDevice, field_context);
}

static void checkAndClearExceptionFromCallback(JNIEnv* env, const char* methodName) {
    if (env->ExceptionCheck()) {
<<<<<<< HEAD
        ALOGE("An exception was thrown by callback '%s'.", methodName);
=======
        LOGE("An exception was thrown by callback '%s'.", methodName);
>>>>>>> upstream/master
        LOGE_EX(env);
        env->ExceptionClear();
    }
}

// ----------------------------------------------------------------------------

static jboolean
android_mtp_MtpDevice_open(JNIEnv *env, jobject thiz, jstring deviceName, jint fd)
{
    const char *deviceNameStr = env->GetStringUTFChars(deviceName, NULL);
    if (deviceNameStr == NULL) {
        return false;
    }

    MtpDevice* device = MtpDevice::open(deviceNameStr, fd);
    env->ReleaseStringUTFChars(deviceName, deviceNameStr);

    if (device)
        env->SetIntField(thiz, field_context, (int)device);
    return (device != NULL);
}

static void
android_mtp_MtpDevice_close(JNIEnv *env, jobject thiz)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (device) {
        device->close();
        delete device;
        env->SetIntField(thiz, field_context, 0);
    }
}

static jobject
android_mtp_MtpDevice_get_device_info(JNIEnv *env, jobject thiz)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device) {
<<<<<<< HEAD
        ALOGD("android_mtp_MtpDevice_get_device_info device is null");
=======
        LOGD("android_mtp_MtpDevice_get_device_info device is null");
>>>>>>> upstream/master
        return NULL;
    }
    MtpDeviceInfo* deviceInfo = device->getDeviceInfo();
    if (!deviceInfo) {
<<<<<<< HEAD
        ALOGD("android_mtp_MtpDevice_get_device_info deviceInfo is null");
=======
        LOGD("android_mtp_MtpDevice_get_device_info deviceInfo is null");
>>>>>>> upstream/master
        return NULL;
    }
    jobject info = env->NewObject(clazz_deviceInfo, constructor_deviceInfo);
    if (info == NULL) {
<<<<<<< HEAD
        ALOGE("Could not create a MtpDeviceInfo object");
=======
        LOGE("Could not create a MtpDeviceInfo object");
>>>>>>> upstream/master
        delete deviceInfo;
        return NULL;
    }

    if (deviceInfo->mManufacturer)
        env->SetObjectField(info, field_deviceInfo_manufacturer,
            env->NewStringUTF(deviceInfo->mManufacturer));
    if (deviceInfo->mModel)
        env->SetObjectField(info, field_deviceInfo_model,
            env->NewStringUTF(deviceInfo->mModel));
    if (deviceInfo->mVersion)
        env->SetObjectField(info, field_deviceInfo_version,
            env->NewStringUTF(deviceInfo->mVersion));
    if (deviceInfo->mSerial)
        env->SetObjectField(info, field_deviceInfo_serialNumber,
            env->NewStringUTF(deviceInfo->mSerial));

    delete deviceInfo;
    return info;
}

static jintArray
android_mtp_MtpDevice_get_storage_ids(JNIEnv *env, jobject thiz)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device)
        return NULL;
    MtpStorageIDList* storageIDs = device->getStorageIDs();
    if (!storageIDs)
        return NULL;

    int length = storageIDs->size();
    jintArray array = env->NewIntArray(length);
    // FIXME is this cast safe?
    env->SetIntArrayRegion(array, 0, length, (const jint *)storageIDs->array());

    delete storageIDs;
    return array;
}

static jobject
android_mtp_MtpDevice_get_storage_info(JNIEnv *env, jobject thiz, jint storageID)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device)
        return NULL;
    MtpStorageInfo* storageInfo = device->getStorageInfo(storageID);
    if (!storageInfo)
        return NULL;

    jobject info = env->NewObject(clazz_storageInfo, constructor_storageInfo);
    if (info == NULL) {
<<<<<<< HEAD
        ALOGE("Could not create a MtpStorageInfo object");
=======
        LOGE("Could not create a MtpStorageInfo object");
>>>>>>> upstream/master
        delete storageInfo;
        return NULL;
    }

    if (storageInfo->mStorageID)
        env->SetIntField(info, field_storageInfo_storageId, storageInfo->mStorageID);
    if (storageInfo->mMaxCapacity)
        env->SetLongField(info, field_storageInfo_maxCapacity, storageInfo->mMaxCapacity);
    if (storageInfo->mFreeSpaceBytes)
        env->SetLongField(info, field_storageInfo_freeSpace, storageInfo->mFreeSpaceBytes);
    if (storageInfo->mStorageDescription)
        env->SetObjectField(info, field_storageInfo_description,
            env->NewStringUTF(storageInfo->mStorageDescription));
    if (storageInfo->mVolumeIdentifier)
        env->SetObjectField(info, field_storageInfo_volumeIdentifier,
            env->NewStringUTF(storageInfo->mVolumeIdentifier));

    delete storageInfo;
    return info;
}

static jintArray
android_mtp_MtpDevice_get_object_handles(JNIEnv *env, jobject thiz,
        jint storageID, jint format, jint objectID)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device)
        return NULL;
    MtpObjectHandleList* handles = device->getObjectHandles(storageID, format, objectID);
    if (!handles)
        return NULL;

    int length = handles->size();
    jintArray array = env->NewIntArray(length);
    // FIXME is this cast safe?
    env->SetIntArrayRegion(array, 0, length, (const jint *)handles->array());

    delete handles;
    return array;
}

static jobject
android_mtp_MtpDevice_get_object_info(JNIEnv *env, jobject thiz, jint objectID)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device)
        return NULL;
    MtpObjectInfo* objectInfo = device->getObjectInfo(objectID);
    if (!objectInfo)
        return NULL;
    jobject info = env->NewObject(clazz_objectInfo, constructor_objectInfo);
    if (info == NULL) {
<<<<<<< HEAD
        ALOGE("Could not create a MtpObjectInfo object");
=======
        LOGE("Could not create a MtpObjectInfo object");
>>>>>>> upstream/master
        delete objectInfo;
        return NULL;
    }

    if (objectInfo->mHandle)
        env->SetIntField(info, field_objectInfo_handle, objectInfo->mHandle);
    if (objectInfo->mStorageID)
        env->SetIntField(info, field_objectInfo_storageId, objectInfo->mStorageID);
    if (objectInfo->mFormat)
        env->SetIntField(info, field_objectInfo_format, objectInfo->mFormat);
    if (objectInfo->mProtectionStatus)
        env->SetIntField(info, field_objectInfo_protectionStatus, objectInfo->mProtectionStatus);
    if (objectInfo->mCompressedSize)
        env->SetIntField(info, field_objectInfo_compressedSize, objectInfo->mCompressedSize);
    if (objectInfo->mThumbFormat)
        env->SetIntField(info, field_objectInfo_thumbFormat, objectInfo->mThumbFormat);
    if (objectInfo->mThumbCompressedSize)
        env->SetIntField(info, field_objectInfo_thumbCompressedSize, objectInfo->mThumbCompressedSize);
    if (objectInfo->mThumbPixWidth)
        env->SetIntField(info, field_objectInfo_thumbPixWidth, objectInfo->mThumbPixWidth);
    if (objectInfo->mThumbPixHeight)
        env->SetIntField(info, field_objectInfo_thumbPixHeight, objectInfo->mThumbPixHeight);
    if (objectInfo->mImagePixWidth)
        env->SetIntField(info, field_objectInfo_imagePixWidth, objectInfo->mImagePixWidth);
    if (objectInfo->mImagePixHeight)
        env->SetIntField(info, field_objectInfo_imagePixHeight, objectInfo->mImagePixHeight);
    if (objectInfo->mImagePixDepth)
        env->SetIntField(info, field_objectInfo_imagePixDepth, objectInfo->mImagePixDepth);
    if (objectInfo->mParent)
        env->SetIntField(info, field_objectInfo_parent, objectInfo->mParent);
    if (objectInfo->mAssociationType)
        env->SetIntField(info, field_objectInfo_associationType, objectInfo->mAssociationType);
    if (objectInfo->mAssociationDesc)
        env->SetIntField(info, field_objectInfo_associationDesc, objectInfo->mAssociationDesc);
    if (objectInfo->mSequenceNumber)
        env->SetIntField(info, field_objectInfo_sequenceNumber, objectInfo->mSequenceNumber);
    if (objectInfo->mName)
        env->SetObjectField(info, field_objectInfo_name, env->NewStringUTF(objectInfo->mName));
    if (objectInfo->mDateCreated)
        env->SetLongField(info, field_objectInfo_dateCreated, objectInfo->mDateCreated * 1000LL);
    if (objectInfo->mDateModified)
        env->SetLongField(info, field_objectInfo_dateModified, objectInfo->mDateModified * 1000LL);
    if (objectInfo->mKeywords)
        env->SetObjectField(info, field_objectInfo_keywords,
            env->NewStringUTF(objectInfo->mKeywords));

    delete objectInfo;
    return info;
}

struct get_object_callback_data {
    JNIEnv *env;
    jbyteArray array;
};

static bool get_object_callback(void* data, int offset, int length, void* clientData)
{
    get_object_callback_data* cbData = (get_object_callback_data *)clientData;
    cbData->env->SetByteArrayRegion(cbData->array, offset, length, (jbyte *)data);
    return true;
}

static jbyteArray
android_mtp_MtpDevice_get_object(JNIEnv *env, jobject thiz, jint objectID, jint objectSize)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device)
        return NULL;

    jbyteArray array = env->NewByteArray(objectSize);
    if (!array) {
        jniThrowException(env, "java/lang/OutOfMemoryError", NULL);
        return NULL;
    }

    get_object_callback_data data;
    data.env = env;
    data.array = array;

    if (device->readObject(objectID, get_object_callback, objectSize, &data))
        return array;
    return NULL;
}

static jbyteArray
android_mtp_MtpDevice_get_thumbnail(JNIEnv *env, jobject thiz, jint objectID)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (!device)
        return NULL;

    int length;
    void* thumbnail = device->getThumbnail(objectID, length);
    if (! thumbnail)
        return NULL;
    jbyteArray array = env->NewByteArray(length);
    env->SetByteArrayRegion(array, 0, length, (const jbyte *)thumbnail);

    free(thumbnail);
    return array;
}

static jboolean
android_mtp_MtpDevice_delete_object(JNIEnv *env, jobject thiz, jint object_id)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (device)
        return device->deleteObject(object_id);
    else
        return NULL;
}

static jlong
android_mtp_MtpDevice_get_parent(JNIEnv *env, jobject thiz, jint object_id)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (device)
        return device->getParent(object_id);
    else
        return -1;
}

static jlong
android_mtp_MtpDevice_get_storage_id(JNIEnv *env, jobject thiz, jint object_id)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (device)
        return device->getStorageID(object_id);
    else
        return -1;
}

static jboolean
android_mtp_MtpDevice_import_file(JNIEnv *env, jobject thiz, jint object_id, jstring dest_path)
{
    MtpDevice* device = get_device_from_object(env, thiz);
    if (device) {
        const char *destPathStr = env->GetStringUTFChars(dest_path, NULL);
        if (destPathStr == NULL) {
            return false;
        }

        bool result = device->readObject(object_id, destPathStr, AID_SDCARD_RW, 0664);
        env->ReleaseStringUTFChars(dest_path, destPathStr);
        return result;
    }

    return false;
}

// ----------------------------------------------------------------------------

static JNINativeMethod gMethods[] = {
    {"native_open",             "(Ljava/lang/String;I)Z",
                                        (void *)android_mtp_MtpDevice_open},
    {"native_close",            "()V",  (void *)android_mtp_MtpDevice_close},
    {"native_get_device_info",  "()Landroid/mtp/MtpDeviceInfo;",
                                        (void *)android_mtp_MtpDevice_get_device_info},
    {"native_get_storage_ids",  "()[I", (void *)android_mtp_MtpDevice_get_storage_ids},
    {"native_get_storage_info", "(I)Landroid/mtp/MtpStorageInfo;",
                                        (void *)android_mtp_MtpDevice_get_storage_info},
    {"native_get_object_handles","(III)[I",
                                        (void *)android_mtp_MtpDevice_get_object_handles},
    {"native_get_object_info",  "(I)Landroid/mtp/MtpObjectInfo;",
                                        (void *)android_mtp_MtpDevice_get_object_info},
    {"native_get_object",       "(II)[B",(void *)android_mtp_MtpDevice_get_object},
    {"native_get_thumbnail",    "(I)[B",(void *)android_mtp_MtpDevice_get_thumbnail},
    {"native_delete_object",    "(I)Z", (void *)android_mtp_MtpDevice_delete_object},
    {"native_get_parent",       "(I)J", (void *)android_mtp_MtpDevice_get_parent},
    {"native_get_storage_id",   "(I)J", (void *)android_mtp_MtpDevice_get_storage_id},
    {"native_import_file",     "(ILjava/lang/String;)Z",
                                        (void *)android_mtp_MtpDevice_import_file},
};

static const char* const kClassPathName = "android/mtp/MtpDevice";

int register_android_mtp_MtpDevice(JNIEnv *env)
{
    jclass clazz;

<<<<<<< HEAD
    ALOGD("register_android_mtp_MtpDevice\n");

    clazz = env->FindClass("android/mtp/MtpDeviceInfo");
    if (clazz == NULL) {
        ALOGE("Can't find android/mtp/MtpDeviceInfo");
=======
    LOGD("register_android_mtp_MtpDevice\n");

    clazz = env->FindClass("android/mtp/MtpDeviceInfo");
    if (clazz == NULL) {
        LOGE("Can't find android/mtp/MtpDeviceInfo");
>>>>>>> upstream/master
        return -1;
    }
    constructor_deviceInfo = env->GetMethodID(clazz, "<init>", "()V");
    if (constructor_deviceInfo == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/mtp/MtpDeviceInfo constructor");
=======
        LOGE("Can't find android/mtp/MtpDeviceInfo constructor");
>>>>>>> upstream/master
        return -1;
    }
    field_deviceInfo_manufacturer = env->GetFieldID(clazz, "mManufacturer", "Ljava/lang/String;");
    if (field_deviceInfo_manufacturer == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpDeviceInfo.mManufacturer");
=======
        LOGE("Can't find MtpDeviceInfo.mManufacturer");
>>>>>>> upstream/master
        return -1;
    }
    field_deviceInfo_model = env->GetFieldID(clazz, "mModel", "Ljava/lang/String;");
    if (field_deviceInfo_model == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpDeviceInfo.mModel");
=======
        LOGE("Can't find MtpDeviceInfo.mModel");
>>>>>>> upstream/master
        return -1;
    }
    field_deviceInfo_version = env->GetFieldID(clazz, "mVersion", "Ljava/lang/String;");
    if (field_deviceInfo_version == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpDeviceInfo.mVersion");
=======
        LOGE("Can't find MtpDeviceInfo.mVersion");
>>>>>>> upstream/master
        return -1;
    }
    field_deviceInfo_serialNumber = env->GetFieldID(clazz, "mSerialNumber", "Ljava/lang/String;");
    if (field_deviceInfo_serialNumber == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpDeviceInfo.mSerialNumber");
=======
        LOGE("Can't find MtpDeviceInfo.mSerialNumber");
>>>>>>> upstream/master
        return -1;
    }
    clazz_deviceInfo = (jclass)env->NewGlobalRef(clazz);

    clazz = env->FindClass("android/mtp/MtpStorageInfo");
    if (clazz == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/mtp/MtpStorageInfo");
=======
        LOGE("Can't find android/mtp/MtpStorageInfo");
>>>>>>> upstream/master
        return -1;
    }
    constructor_storageInfo = env->GetMethodID(clazz, "<init>", "()V");
    if (constructor_storageInfo == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/mtp/MtpStorageInfo constructor");
=======
        LOGE("Can't find android/mtp/MtpStorageInfo constructor");
>>>>>>> upstream/master
        return -1;
    }
    field_storageInfo_storageId = env->GetFieldID(clazz, "mStorageId", "I");
    if (field_storageInfo_storageId == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpStorageInfo.mStorageId");
=======
        LOGE("Can't find MtpStorageInfo.mStorageId");
>>>>>>> upstream/master
        return -1;
    }
    field_storageInfo_maxCapacity = env->GetFieldID(clazz, "mMaxCapacity", "J");
    if (field_storageInfo_maxCapacity == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpStorageInfo.mMaxCapacity");
=======
        LOGE("Can't find MtpStorageInfo.mMaxCapacity");
>>>>>>> upstream/master
        return -1;
    }
    field_storageInfo_freeSpace = env->GetFieldID(clazz, "mFreeSpace", "J");
    if (field_storageInfo_freeSpace == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpStorageInfo.mFreeSpace");
=======
        LOGE("Can't find MtpStorageInfo.mFreeSpace");
>>>>>>> upstream/master
        return -1;
    }
    field_storageInfo_description = env->GetFieldID(clazz, "mDescription", "Ljava/lang/String;");
    if (field_storageInfo_description == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpStorageInfo.mDescription");
=======
        LOGE("Can't find MtpStorageInfo.mDescription");
>>>>>>> upstream/master
        return -1;
    }
    field_storageInfo_volumeIdentifier = env->GetFieldID(clazz, "mVolumeIdentifier", "Ljava/lang/String;");
    if (field_storageInfo_volumeIdentifier == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpStorageInfo.mVolumeIdentifier");
=======
        LOGE("Can't find MtpStorageInfo.mVolumeIdentifier");
>>>>>>> upstream/master
        return -1;
    }
    clazz_storageInfo = (jclass)env->NewGlobalRef(clazz);

    clazz = env->FindClass("android/mtp/MtpObjectInfo");
    if (clazz == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/mtp/MtpObjectInfo");
=======
        LOGE("Can't find android/mtp/MtpObjectInfo");
>>>>>>> upstream/master
        return -1;
    }
    constructor_objectInfo = env->GetMethodID(clazz, "<init>", "()V");
    if (constructor_objectInfo == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/mtp/MtpObjectInfo constructor");
=======
        LOGE("Can't find android/mtp/MtpObjectInfo constructor");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_handle = env->GetFieldID(clazz, "mHandle", "I");
    if (field_objectInfo_handle == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mHandle");
=======
        LOGE("Can't find MtpObjectInfo.mHandle");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_storageId = env->GetFieldID(clazz, "mStorageId", "I");
    if (field_objectInfo_storageId == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mStorageId");
=======
        LOGE("Can't find MtpObjectInfo.mStorageId");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_format = env->GetFieldID(clazz, "mFormat", "I");
    if (field_objectInfo_format == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mFormat");
=======
        LOGE("Can't find MtpObjectInfo.mFormat");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_protectionStatus = env->GetFieldID(clazz, "mProtectionStatus", "I");
    if (field_objectInfo_protectionStatus == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mProtectionStatus");
=======
        LOGE("Can't find MtpObjectInfo.mProtectionStatus");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_compressedSize = env->GetFieldID(clazz, "mCompressedSize", "I");
    if (field_objectInfo_compressedSize == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mCompressedSize");
=======
        LOGE("Can't find MtpObjectInfo.mCompressedSize");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_thumbFormat = env->GetFieldID(clazz, "mThumbFormat", "I");
    if (field_objectInfo_thumbFormat == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mThumbFormat");
=======
        LOGE("Can't find MtpObjectInfo.mThumbFormat");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_thumbCompressedSize = env->GetFieldID(clazz, "mThumbCompressedSize", "I");
    if (field_objectInfo_thumbCompressedSize == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mThumbCompressedSize");
=======
        LOGE("Can't find MtpObjectInfo.mThumbCompressedSize");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_thumbPixWidth = env->GetFieldID(clazz, "mThumbPixWidth", "I");
    if (field_objectInfo_thumbPixWidth == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mThumbPixWidth");
=======
        LOGE("Can't find MtpObjectInfo.mThumbPixWidth");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_thumbPixHeight = env->GetFieldID(clazz, "mThumbPixHeight", "I");
    if (field_objectInfo_thumbPixHeight == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mThumbPixHeight");
=======
        LOGE("Can't find MtpObjectInfo.mThumbPixHeight");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_imagePixWidth = env->GetFieldID(clazz, "mImagePixWidth", "I");
    if (field_objectInfo_imagePixWidth == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mImagePixWidth");
=======
        LOGE("Can't find MtpObjectInfo.mImagePixWidth");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_imagePixHeight = env->GetFieldID(clazz, "mImagePixHeight", "I");
    if (field_objectInfo_imagePixHeight == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mImagePixHeight");
=======
        LOGE("Can't find MtpObjectInfo.mImagePixHeight");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_imagePixDepth = env->GetFieldID(clazz, "mImagePixDepth", "I");
    if (field_objectInfo_imagePixDepth == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mImagePixDepth");
=======
        LOGE("Can't find MtpObjectInfo.mImagePixDepth");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_parent = env->GetFieldID(clazz, "mParent", "I");
    if (field_objectInfo_parent == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mParent");
=======
        LOGE("Can't find MtpObjectInfo.mParent");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_associationType = env->GetFieldID(clazz, "mAssociationType", "I");
    if (field_objectInfo_associationType == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mAssociationType");
=======
        LOGE("Can't find MtpObjectInfo.mAssociationType");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_associationDesc = env->GetFieldID(clazz, "mAssociationDesc", "I");
    if (field_objectInfo_associationDesc == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mAssociationDesc");
=======
        LOGE("Can't find MtpObjectInfo.mAssociationDesc");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_sequenceNumber = env->GetFieldID(clazz, "mSequenceNumber", "I");
    if (field_objectInfo_sequenceNumber == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mSequenceNumber");
=======
        LOGE("Can't find MtpObjectInfo.mSequenceNumber");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_name = env->GetFieldID(clazz, "mName", "Ljava/lang/String;");
    if (field_objectInfo_name == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mName");
=======
        LOGE("Can't find MtpObjectInfo.mName");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_dateCreated = env->GetFieldID(clazz, "mDateCreated", "J");
    if (field_objectInfo_dateCreated == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mDateCreated");
=======
        LOGE("Can't find MtpObjectInfo.mDateCreated");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_dateModified = env->GetFieldID(clazz, "mDateModified", "J");
    if (field_objectInfo_dateModified == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mDateModified");
=======
        LOGE("Can't find MtpObjectInfo.mDateModified");
>>>>>>> upstream/master
        return -1;
    }
    field_objectInfo_keywords = env->GetFieldID(clazz, "mKeywords", "Ljava/lang/String;");
    if (field_objectInfo_keywords == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpObjectInfo.mKeywords");
=======
        LOGE("Can't find MtpObjectInfo.mKeywords");
>>>>>>> upstream/master
        return -1;
    }
    clazz_objectInfo = (jclass)env->NewGlobalRef(clazz);

    clazz = env->FindClass("android/mtp/MtpDevice");
    if (clazz == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find android/mtp/MtpDevice");
=======
        LOGE("Can't find android/mtp/MtpDevice");
>>>>>>> upstream/master
        return -1;
    }
    field_context = env->GetFieldID(clazz, "mNativeContext", "I");
    if (field_context == NULL) {
<<<<<<< HEAD
        ALOGE("Can't find MtpDevice.mNativeContext");
=======
        LOGE("Can't find MtpDevice.mNativeContext");
>>>>>>> upstream/master
        return -1;
    }

    return AndroidRuntime::registerNativeMethods(env,
                "android/mtp/MtpDevice", gMethods, NELEM(gMethods));
}
