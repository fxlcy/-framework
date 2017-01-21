//
// Created by fxlcy on 2017/1/6.
//
#include <jni.h>
#include <stdint.h>


#ifndef ABC_SECURITY_H
#define ABC_SECURITY_H


#ifdef __cplusplus
extern "C" {
#endif


void security_aes256_encrypt_or_decrypt_ecb(uint8_t source[], const int len, bool encrypt);

void security_aes256_encrypt_ecb(JNIEnv *env, jclass clazz, jbyteArray bytes);

void security_aes256_decrypt_ecb(JNIEnv *env, jclass clazz, jbyteArray bytes);

void security_aes256_init(JNIEnv *env, jclass clazz, jbyteArray key);

void security_aes256_destroy(JNIEnv *env, jclass clazz);

void security_xorCrypt(JNIEnv *env, jclass clazz, jbyteArray array,jbyteArray key);


#ifdef __cplusplus
}
#endif

#endif //FLZX2_SECURITY_H
