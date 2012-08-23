LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

<<<<<<< HEAD
LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := install_uses_feature

include $(FrameworkCoreTests_BUILD_PACKAGE)
=======
LOCAL_MODULE_TAGS := tests

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := FrameworkCoreTests_install_uses_feature

include $(BUILD_PACKAGE)

>>>>>>> upstream/master
