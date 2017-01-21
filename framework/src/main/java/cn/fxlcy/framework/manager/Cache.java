package cn.fxlcy.framework.manager;

import android.support.v4.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import cn.fxlcy.framework.util.Encrypt;

/**
 * Created by fxlcy on 2016/12/23.
 * 缓存类(file+memory)
 */
public class Cache implements Closeable, Flushable {

    private final static String TAG = Cache.class.getSimpleName();

    private final static int ENTRY_COUNT = 2;

    public final static int UNLIMITED_TIME = -1;

    private int mDiskCacheMaxSize = 1024 * 1024 * 20;
    private final int mMemoryCacheMaxSize = 1024 * 1024 * 4;
    private static final int sWriteBufferSize = 1024 * 100;


    private File mDiskDir;
    private DiskLruCache mCache;
    private MemoryCache mMemoryCache;
    private DiskLruCache.Editor mEditor;


    private Cache(File cacheDir, int version) {
        this(cacheDir, 0, version);
    }

    private Cache(File cacheDir, int cacheMaxSize, int version) {
        if (cacheMaxSize > 0) {
            mDiskCacheMaxSize = cacheMaxSize;
        }

        if (!cacheDir.isDirectory()) {
            throw new IllegalArgumentException("cacheDir.isDirectory");
        } else {
            mDiskDir = cacheDir;
        }

        try {
            mCache = DiskLruCache.open(cacheDir, version, ENTRY_COUNT, mDiskCacheMaxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMemoryCache = new MemoryCache(this, mMemoryCacheMaxSize);
    }


    public Editor edit() {
        return new Editor();
    }

    @Override
    public void close() throws IOException {
        mCache.close();
    }

    @Override
    public void flush() throws IOException {
        mCache.flush();
    }

    public int maxDiskCacheSize() {
        return mDiskCacheMaxSize;
    }

    public int maxMemoryCacheSize() {
        return mMemoryCacheMaxSize;
    }

    public File getDiskDir() {
        return mDiskDir;
    }

    public final class Editor {
        Editor() {
            synchronized (Cache.class) {
            }
        }

        private int outTimeMillis = UNLIMITED_TIME;
        private DiskLruCache.Snapshot snapshot;

        public Editor setOutTimeMillis(int outTimeMillis) {
            this.outTimeMillis = outTimeMillis;
            return this;
        }


        public void put(String key, String value) {
            put(key, value, "utf-8");
        }

        public void put(String key, String value, String charset) {
            byte[] bytes = value.getBytes(Charset.forName(charset));
            put(key, bytes);
        }

        public void put(String key, byte[] value) {
            put(key, value, 0, value.length);
        }

        public void put(String key, byte[] value, int off, int len) {
            key = key(key);
            long currentTimeMillis = System.currentTimeMillis();

            mMemoryCache.put(key, new MemoryEntry(currentTimeMillis, outTimeMillis, value));

            if (putDisk(key, value, off, len)) {
                if (putTimeDisk(new Entry(currentTimeMillis, outTimeMillis))) {
                    completeEdit(false);
                }
            }
        }

        public void put(String key, InputStream stream) {
            key = key(key);
            if (putDisk(key, stream)) {
                if (putTimeDisk(new Entry(System.currentTimeMillis(), outTimeMillis))) {
                    completeEdit(false);
                }
            }
        }

        public InputStream get(String key) {
            return get(key, null);
        }

        public String getString(String key, String charset) {
            String _key = key(key);
            MemoryEntry memoryEntry = mMemoryCache.get(_key);
            if (memoryEntry != null) {
                boolean b = verifyTimeout(_key, memoryEntry);
                if (!b) {
                    Log.i(TAG, key + ":的数据过期了");
                    return null;
                } else {
                    try {
                        return new String(memoryEntry.data, charset);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            Entry entry = new Entry();
            InputStream is = get(key, entry);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[100];

            if (is == null) {
                return null;
            }
            try {
                InputStreamReader reader = new InputStreamReader(is, charset);
                while (true) {
                    int readCount = reader.read(buffer);
                    if (readCount <= 0) {
                        break;
                    }

                    sb.append(buffer, 0, readCount);
                }

                byte[] bytes = sb.toString().getBytes(charset);
                mMemoryCache.put(_key, new MemoryEntry(entry.cacheTimeMillis, entry.outTimeMillis, bytes));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                releaseSnapshot();
            }

            return sb.toString();
        }

        public boolean remove(String key) {
            try {
                return mCache.remove(key) || mMemoryCache.remove(key) != null;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void releaseSnapshot() {
            if (snapshot != null) {
                snapshot.close();
                snapshot = null;
            }
        }


        private InputStream get(String key, Entry outEntry) {
            String _key = key(key);

            if (snapshot != null) {
                throw new RuntimeException("请先调用releaseSnapshot（）");
            }

            try {
                snapshot = mCache.get(_key);
                if (snapshot == null) {
                    return null;
                } else {
                    Entry entry = new Entry(snapshot.getInputStream(1));
                    if (outEntry != null) {
                        outEntry.cacheTimeMillis = entry.cacheTimeMillis;
                        outEntry.outTimeMillis = entry.outTimeMillis;
                    }
                    boolean b = verifyTimeout(_key, entry);
                    if (!b) {
                        Log.i(TAG, key + ":的数据过期了");
                        return null;
                    }

                    return snapshot.getInputStream(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    private DiskLruCache.Editor editor(String key) {
        if (mEditor != null) {
            throw new RuntimeException("请先执行releaseEditor()");
        }

        if (key == null) {
            throw new NullPointerException("key == null");
        }

        try {
            mEditor = mCache.edit(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mEditor;
    }

    private void completeEdit(boolean isAbort) {
        if (mEditor != null) {
            try {
                if (isAbort) {
                    mEditor.abort();
                } else {
                    mEditor.commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mEditor = null;
            }
        }
    }

    private boolean putDisk(String key, byte[] value, int off, int len) {
        OutputStream os;

        try {
            os = editor(key)
                    .newOutputStream(0);
            os.write(value, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            completeEdit(true);
            return false;
        }
        return true;
    }

    private boolean putDisk(String key, InputStream stream) {
        OutputStream os;

        byte[] buffer = new byte[sWriteBufferSize];
        try {
            DiskLruCache.Editor editor = editor(key);
            os = editor.newOutputStream(0);
            while (true) {
                int readCount = stream.read(buffer, 0, buffer.length);
                if (readCount <= 0) {
                    break;
                }

                os.write(buffer, 0, readCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
            completeEdit(true);
            return false;
        }

        return true;
    }

    private boolean putTimeDisk(Entry entry) {
        if (mEditor != null) {
            OutputStream os;
            try {
                os = mEditor.newOutputStream(1);
                OutputStreamWriter writer = new OutputStreamWriter(os, "utf-8");
                writer.write(entry.cacheTimeMillis + "\n");
                writer.write(entry.outTimeMillis);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                completeEdit(true);
                return false;
            }
        }

        return false;
    }


    private String key(String key) {
        return Encrypt.MD5(key);
    }


    private boolean verifyTimeout(String key, Entry entry) {
        if (entry.outTimeMillis != UNLIMITED_TIME
                && (entry.outTimeMillis == 0
                || System.currentTimeMillis() - entry.cacheTimeMillis > entry.outTimeMillis)) {
            mMemoryCache.remove(key);
            try {
                mCache.remove(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    private static class MemoryCache extends LruCache<String, MemoryEntry> {
        Cache mCache;

        MemoryCache(Cache cache, int maxSize) {
            super(maxSize);
            mCache = cache;
        }
    }


    private static class MemoryEntry extends Entry {
        byte[] data;

        MemoryEntry(long cacheTimeMillis, int outTimeMillis, byte[] data) {
            super(cacheTimeMillis, outTimeMillis);
            this.data = data;
        }
    }

    private static class Entry {
        Entry(InputStream inputStream) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            long cacheTimeMillis = 0;
            int outTimeMillis = 0;

            try {
                cacheTimeMillis = Long.parseLong(br.readLine());
                outTimeMillis = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.cacheTimeMillis = cacheTimeMillis;
            this.outTimeMillis = outTimeMillis;
        }

        Entry(long cacheTimeMillis, int outTimeMillis) {
            this.cacheTimeMillis = cacheTimeMillis;
            this.outTimeMillis = outTimeMillis;
        }

        Entry() {
        }

        /**
         * 缓存时间
         */
        long cacheTimeMillis;
        /**
         * 超时时间
         */
        int outTimeMillis = UNLIMITED_TIME;

    }


    public static Cache create(File cacheDir, int cacheMaxSize) {
        return new Cache(cacheDir, cacheMaxSize);
    }
}
