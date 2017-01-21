package cn.fxlcy.framework.res;

import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fxlcy on 2016/9/29.
 */

final class AssetInputStream extends InputStream {
    private InputStream mInputStream;
    private long mOffset;

    AssetInputStream(Resources res, String path) throws IOException {
        mInputStream = res.getAssets().open(path);
    }

    @Override
    public int read() throws IOException {
        return mInputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int readLen = mInputStream.read(b, off, len);
        if (readLen > 0) {
            int decodeOffset = (int) (mOffset % Assets.keyLength());
            mOffset += off + len;

            byte[] buffer = Assets.decode(b, decodeOffset, readLen);
            System.arraycopy(buffer, 0, b, 0, readLen);
        }

        return readLen;
    }

    @Override
    public long skip(long n) throws IOException {
        mOffset = n;
        return mInputStream.skip(mOffset);
    }

    @Override
    public int available() throws IOException {
        return mInputStream.available();
    }

    @Override
    public synchronized void reset() throws IOException {
        mOffset = 0;
        mInputStream.reset();
    }

    @Override
    public void close() throws IOException {
        mInputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        mInputStream.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return mInputStream.markSupported();
    }
}
