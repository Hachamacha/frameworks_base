LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

# This builds "SmokeTestApp"
LOCAL_SRC_FILES := $(call all-java-files-under, src)

<<<<<<< HEAD
LOCAL_PACKAGE_NAME := SmokeTestApp

LOCAL_SDK_VERSION := 8
=======
LOCAL_JAVA_LIBRARIES := android.test.runner

LOCAL_PACKAGE_NAME := SmokeTestApp
>>>>>>> upstream/master

include $(BUILD_PACKAGE)

# This builds "SmokeTest"
<<<<<<< HEAD
include $(call all-makefiles-under,$(LOCAL_PATH))
=======
include $(call all-makefiles-under,$(LOCAL_PATH))
>>>>>>> upstream/master
