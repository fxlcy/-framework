#ifndef __LOG_H__
#define __LOG_H__

#include <android/log.h>
#include <string.h>
#include <sys/cdefs.h>
#include <stdio.h>

#ifndef CURRENT_FILENAME
#define CURRENT_FILENAME (strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__)
#endif

//默认的日志tag "NdkNative[Test.cpp(main):25]"
#ifndef TAG
#define TAG \
        CURRENT_FILENAME,__FUNCTION__,__LINE__
#endif


#ifndef LOG_TAG
#define  LOG_TAG    "androidJni"
#endif

#ifndef LOGV
#define LOGV(tag,...) __android_log_print(ANDROID_LOG_VERBOSE, tag, __VA_ARGS__)
#define  ALOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#endif

#ifndef LOGI
#define LOGI(tag,...) __android_log_print(ANDROID_LOG_INFO, tag, __VA_ARGS__)
#define  ALOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#endif


#ifndef LOGW
#define LOGW(tag,...) __android_log_print(ANDROID_LOG_WARN, tag, __VA_ARGS__)
#define  ALOGW(...)  LOGW(LOG_TAG,__VA_ARGS__)
#endif

#ifndef LOGE
#define LOGE(tag,...) __android_log_print(ANDROID_LOG_ERROR, tag, __VA_ARGS__)
#define  ALOGE(...)  LOGE(LOG_TAG,__VA_ARGS__)
#endif


#endif /* LOG_H_ */

