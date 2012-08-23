LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

<<<<<<< HEAD
FrameworkCoreTests_BUILD_PACKAGE := $(LOCAL_PATH)/FrameworkCoreTests_apk.mk

=======
>>>>>>> upstream/master
# build sub packages
include $(call all-makefiles-under,$(LOCAL_PATH))
