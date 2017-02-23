package cn.fxlcy.framework.util;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.fxlcy.framework.manager.Log;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */

public class Encrypt {
    private static final String TAG = "Encrypt";

    private static final String ALGORITHM = "MD5";
    private final static String UTF_8 = "UTF-8";

    private static final char[] HEX = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private Encrypt() {
        throw new RuntimeException("Stub");
    }

    public static String MD5(String str) {
        try {
            return MD5(str.getBytes(UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "Md5 Fail");
            return null;
        }
    }

    public static String MD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] digest = md.digest(data);
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Md5 Fail");
        }
        return null;
    }

    private static String toHex(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (byte v : b) {
            builder.append(HEX[(0xF0 & v) >> 4]);
            builder.append(HEX[0x0F & v]);
        }
        return builder.toString();
    }

    public static byte[] encryptStr(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes(UTF_8);
            aes256_encrypt_ecb(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "encryptStr fail");
        }

        return bytes;
    }


    public static String decryptStr(byte[] bytes) {
        aes256_decrypt_ecb(bytes);
        return new String(bytes);
    }


    public native static void aes256_encrypt_ecb(byte[] bytes);

    public native static void aes256_decrypt_ecb(byte[] bytes);

    public native static void aes256_init_context(byte[] bytes);

    public native static void aes256_destroy_context();

    public native static void xorCrypt(byte[] bytes, @Nullable byte[] key);

    public static void xorCrypt(byte[] bytes) {
        xorCrypt(bytes, null);
    }

    static {
        System.loadLibrary("al");
        System.loadLibrary("fw-core");
    }
}
