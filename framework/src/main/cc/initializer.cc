//
// Created by fxlcy on 2016/9/19.
//

#include "initializer.h"
#include "security.h"
#include "include/al.h"


static const JNINativeMethod securityMethods[] = {
        {"aes256_encrypt_ecb",     "([B)V",   (void *) security_aes256_encrypt_ecb},
        {"aes256_decrypt_ecb",     "([B)V",   (void *) security_aes256_decrypt_ecb},
        {"aes256_init_context",    "([B)V",   (void *) security_aes256_init},
        {"aes256_destroy_context", "()V",     (void *) security_aes256_destroy},
        {"xorCrypt",               "([B[B)V", (void *) security_xorCrypt}
};


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    ALOGE("jni_onload");

    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    } else {
        jclass encryptClazz = env->FindClass("cn/fxlcy/framework/util/Encrypt");

        if (encryptClazz == NULL) {
            return JNI_ERR;
        }

        int securityMethodLength;
        GET_ARRAY_LENGTH(securityMethods, securityMethodLength);
        ALOGE("length:%d", securityMethodLength);

        if (env->RegisterNatives(encryptClazz, securityMethods, securityMethodLength) != JNI_OK)
            //把本地函数和java类方法关联起来
        {
            return JNI_ERR;
        }
    }

    return JNI_VERSION_1_6;
}