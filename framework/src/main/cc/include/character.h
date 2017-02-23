//
// Created by fxlcy on 2016/11/23.
//

#ifndef FLZX_CHARACTER_H
#define FLZX_CHARACTER_H

#include <stdint.h>

int enc_unicode_to_utf8_one(unsigned long unic, uint8_t *pOutput,
                            int outSize);

int unicodeToUtf8(const char*input,uint8_t *output);


#endif //FLZX_CHARACTER_H
