LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := TVXmlDemo

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_USE_AAPT2 := true

LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_SDK_VERSION := current

LOCAL_STATIC_ANDROID_LIBRARIES := \
    androidx.appcompat_appcompat \
    androidx.cardview_cardview \
    com.google.android.material_material \
    androidx.core_core-ktx

LOCAL_STATIC_JAVA_LIBRARIES := \
    kotlin-stdlib

LOCAL_MODULE_TAGS := optional

LOCAL_KOTLIN_VERSION := 1.9

LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)
