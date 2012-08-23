BASE_PATH := $(call my-dir)
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

# our source files
#
LOCAL_SRC_FILES:= \
    asset_manager.cpp \
    configuration.cpp \
    input.cpp \
    looper.cpp \
    native_activity.cpp \
    native_window.cpp \
    obb.cpp \
    sensor.cpp \
    storage_manager.cpp

LOCAL_SHARED_LIBRARIES := \
    libcutils \
<<<<<<< HEAD
    libandroidfw \
=======
>>>>>>> upstream/master
    libutils \
    libbinder \
    libui \
    libgui \
    libandroid_runtime

LOCAL_STATIC_LIBRARIES := \
    libstorage

LOCAL_C_INCLUDES += \
    frameworks/base/native/include \
<<<<<<< HEAD
    frameworks/base/core/jni/android
=======
    frameworks/base/core/jni/android \
    dalvik/libnativehelper/include/nativehelper
>>>>>>> upstream/master

LOCAL_MODULE:= libandroid

include $(BUILD_SHARED_LIBRARY)
