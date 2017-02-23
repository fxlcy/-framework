package cn.fxlcy.framework.res;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fxlcy on 2016/9/19.
 */
public final class Assets {
    private final static int BUFFER_SIZE = 1024 * 4;

    private Assets() {
        throw new RuntimeException("Stub");
    }

    static {
        System.loadLibrary("al");
        System.loadLibrary("fw-core");
    }

    public native static int keyLength();

    public native static byte[] decode(byte[] bytes);

    public native static byte[] decode(byte[] bytes, int decodeOffset, int lenght);

    public static byte[] getResourceByteArray(Context context, String fileName) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            is = getInputStream(context, fileName);
            int len;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }


    public static InputStream getInputStream(Context context, String fileName) throws IOException {
        return new AssetInputStream(context.getResources(), fileName);
    }

    public static void exportAsset(Context context, String fileName, String outPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPath);
            is = context.getAssets().open(fileName);
            int len;
            byte[] buffer = new byte[1024 * 100];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
