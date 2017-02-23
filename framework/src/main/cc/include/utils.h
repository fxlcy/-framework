#include <stdint.h>
#include "character.h"

#ifndef FXLCY_UTILS_H
#define FXLCY_UTILS_H


#ifdef __cplusplus
extern "C" {
#endif

void xorCrypt(uint8_t source[],int len,uint8_t key[],int keyLen);

#ifdef __cplusplus
}
#endif


#endif //end  FXLCY_AES256_H