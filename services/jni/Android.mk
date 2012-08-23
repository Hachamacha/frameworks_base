LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
    com_android_server_AlarmManagerService.cpp \
    com_android_server_BatteryService.cpp \
<<<<<<< HEAD
    com_android_server_input_InputApplicationHandle.cpp \
    com_android_server_input_InputManagerService.cpp \
    com_android_server_input_InputWindowHandle.cpp \
    com_android_server_LightsService.cpp \
    com_android_server_PowerManagerService.cpp \
    com_android_server_SerialService.cpp \
=======
    com_android_server_InputApplicationHandle.cpp \
    com_android_server_InputManager.cpp \
    com_android_server_InputWindowHandle.cpp \
    com_android_server_LightsService.cpp \
    com_android_server_PowerManagerService.cpp \
>>>>>>> upstream/master
    com_android_server_SystemServer.cpp \
    com_android_server_UsbDeviceManager.cpp \
    com_android_server_UsbHostManager.cpp \
    com_android_server_VibratorService.cpp \
    com_android_server_location_GpsLocationProvider.cpp \
    com_android_server_connectivity_Vpn.cpp \
    onload.cpp

LOCAL_C_INCLUDES += \
    $(JNI_H_INCLUDE) \
    frameworks/base/services \
    frameworks/base/core/jni \
<<<<<<< HEAD
    external/skia/include/core \
    libcore/include \
    libcore/include/libsuspend \
	$(call include-path-for, libhardware)/hardware \
	$(call include-path-for, libhardware_legacy)/hardware_legacy \

LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libandroidfw \
=======
    external/skia/include/core

LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
>>>>>>> upstream/master
    libcutils \
    libhardware \
    libhardware_legacy \
    libnativehelper \
    libsystem_server \
    libutils \
    libui \
    libinput \
    libskia \
    libgui \
<<<<<<< HEAD
    libusbhost \
    libsuspend
=======
    libusbhost
>>>>>>> upstream/master

ifeq ($(WITH_MALLOC_LEAK_CHECK),true)
    LOCAL_CFLAGS += -DMALLOC_LEAK_CHECK
endif

LOCAL_MODULE:= libandroid_servers

include $(BUILD_SHARED_LIBRARY)
