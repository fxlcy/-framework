//
// Created by Xiong on 2016/9/19.
//
#include "assets.h"
#include "include/al.h"

static const jbyte key[] = {253, 67, 82, 95, 79};

jbyteArray JNICALL assets_decode(JNIEnv *env, jclass clazz, jbyteArray bytes) {
    int len = env->GetArrayLength(bytes);
    return assets_decodeOffset(env, clazz, bytes, 0, len);
}


jbyteArray JNICALL assets_decodeOffset(JNIEnv *env, jclass clazz, jbyteArray bytes, jint offset,
                                       jint length) {
    const int keyLen = assets_keyLength(env, clazz);

    jbyte *jb = env->GetByteArrayElements(bytes, JNI_FALSE);

    for (int i = 0; i < length; i++) {
        jb[i] ^= key[(i + offset) % keyLen];
    }

    env->ReleaseByteArrayElements(bytes, jb, JNI_FALSE);

    return bytes;
}


jint JNICALL assets_keyLength(JNIEnv *env, jclass clazz) {
    int len;
    GET_ARRAY_LENGTH(key, len);
    return len;
}
