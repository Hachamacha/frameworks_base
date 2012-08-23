LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
<<<<<<< HEAD
	android_media_SoundPool.cpp
=======
	android_media_SoundPool.cpp \
	SoundPool.cpp \
	SoundPoolThread.cpp
>>>>>>> upstream/master

LOCAL_SHARED_LIBRARIES := \
	libcutils \
	libutils \
<<<<<<< HEAD
	libandroid_runtime \
	libnativehelper \
	libmedia \
	libmedia_native
=======
	libbinder \
	libandroid_runtime \
	libnativehelper \
	libmedia
>>>>>>> upstream/master

LOCAL_MODULE:= libsoundpool

include $(BUILD_SHARED_LIBRARY)
