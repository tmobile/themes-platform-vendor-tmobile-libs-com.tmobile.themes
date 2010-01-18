LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
         $(call all-subdir-java-files)

LOCAL_MODULE_TAGS := user eng

LOCAL_MODULE := com.tmobile.themes

include $(BUILD_STATIC_JAVA_LIBRARY)
