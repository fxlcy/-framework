#include <jni.h>
#include <android/log.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "md5.h"
#include "io.h"
#include "aes256.h"
#include "utils.h"
#include "log.h"

#define NULL 0

//取得数组长度
#define GET_ARRAY_LENGTH(array,len) {len = (sizeof(array) / sizeof(array[0]));}

#ifndef AL_H
#define AL_H

jstring getSignMd5(JNIEnv *env,jobject context);
jstring getPackageName(JNIEnv *env,jobject context);
AAsset *getAssetStream(JNIEnv *env,jobject assetsManager,const char *fileName);
char *getMd5(const char *str);

#endif // AL_H
