//
// Created by fxlcy on 2017/1/6.
//

#include <malloc.h>
#include "security.h"
#include "include/aes256.h"

#define AES256_LEN 18

aes256_context *ctx;

void init_default_aes256Ctx() {
    security_aes256_destroy(NULL, NULL);
    uint8_t key[32] = {21, 32, 56, 68, 32, 134, 67, 233,
                       45, 135, 90, 34, 111, 56, 124, 67,
                       245, 234, 41, 134, 65, 93, 113, 45,
                       222, 111, 222, 31, 232, 54, 231, 51};//默认key
    ctx = new aes256_context;
    aes256_init(ctx, key);
}

void security_aes256_encrypt_or_decrypt_ecb(uint8_t source[], const int len, bool encrypt) {
    bool isTmp = false;
    if (ctx == NULL) {
        init_default_aes256Ctx();
        isTmp = true;
    }


    if (len > AES256_LEN) {
        uint8_t buffer[AES256_LEN];

        int pos = 0;

        while (pos < len) {
            int count = len - pos;
            bool isOuOf = count > AES256_LEN;

            if (isOuOf) {
                count = AES256_LEN;
            }

            for (int i = 0; i < count; i++) {
                buffer[i] = source[i + pos];
            }

            if (!isOuOf) {
                uint8_t _buffer[count];
                for (int i = 0; i < count; i++) {
                    _buffer[i] = buffer[i];
                }

                if (encrypt) {
                    aes256_encrypt_ecb(ctx, _buffer);
                } else {
                    aes256_decrypt_ecb(ctx, _buffer);
                }

                for (int i = 0; i < count; i++) {
                    source[i + pos] = _buffer[i];
                }
            } else {
                if (encrypt) {
                    aes256_encrypt_ecb(ctx, buffer);
                } else {
                    aes256_decrypt_ecb(ctx, buffer);
                }

                for (int i = 0; i < count; i++) {
                    source[i + pos] = buffer[i];
                }
            }

            pos += AES256_LEN;
        }
    } else {
        if (encrypt) {
            aes256_encrypt_ecb(ctx, source);
        } else {
            aes256_decrypt_ecb(ctx, source);
        }
    }

    if (isTmp) {
        security_aes256_destroy(NULL, NULL);
    }
}


jbyteArray security_aes256_encrypt_ecb(JNIEnv *env, jclass clazz, jbyteArray bytes) {
    int len = env->GetArrayLength(bytes);
    uint8_t *source = (uint8_t *) env->GetByteArrayElements(bytes, JNI_FALSE);

    security_aes256_encrypt_or_decrypt_ecb(source, len, true);

    jbyteArray newbytes = env->NewByteArray(len);
    env->SetByteArrayRegion(newbytes, 0, len, (const jbyte *) source);

    env->ReleaseByteArrayElements(bytes, (jbyte *) source, 0);

    return newbytes;
}

jbyteArray security_aes256_decrypt_ecb(JNIEnv *env, jclass clazz, jbyteArray bytes) {
    int len = env->GetArrayLength(bytes);
    uint8_t *source = (uint8_t *) env->GetByteArrayElements(bytes, JNI_FALSE);

    security_aes256_encrypt_or_decrypt_ecb(source, len, false);

    jbyteArray newbytes = env->NewByteArray(len);
    env->SetByteArrayRegion(newbytes, 0, len, (const jbyte *) source);

    env->ReleaseByteArrayElements(bytes, (jbyte *) source, 0);

    return newbytes;
}


void security_aes256_init(JNIEnv *env, jclass clazz, jbyteArray keyBa) {
    security_aes256_destroy(NULL, NULL);

    int len = env->GetArrayLength(keyBa);
    len = len > 32 ? 32 : len;

    jbyte *keyTmp = env->GetByteArrayElements(keyBa, JNI_FALSE);
    uint8_t key[32];

    for (int i = 0; i < len; i++) {
        key[i] = (unsigned char) keyTmp[i];
    }


    env->ReleaseByteArrayElements(keyBa, keyTmp, JNI_FALSE);

    ctx = new aes256_context;
    aes256_init(ctx, key);
}

void security_aes256_destroy(JNIEnv *env, jclass clazz) {
    if (ctx != NULL) {
        aes256_done(ctx);
        ctx = NULL;
    }
}