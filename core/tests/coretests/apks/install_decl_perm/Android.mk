LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

<<<<<<< HEAD
LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := install_decl_perm

include $(FrameworkCoreTests_BUILD_PACKAGE)
=======
LOCAL_MODULE_TAGS := tests

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := FrameworkCoreTests_install_decl_perm

include $(BUILD_PACKAGE)

>>>>>>> upstream/master
