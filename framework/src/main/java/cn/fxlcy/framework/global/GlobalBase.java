package cn.fxlcy.framework.global;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;

import cn.fxlcy.framework.util.Encrypt;

/**
 * Created by fxlcy on 2016/12/1.
 * 保存全局的参数（伴随整个Application生命周期）
 */

public abstract class GlobalBase implements Serializable {
    private transient final static String TAG = GlobalBase.class.getSimpleName();
    private static final long serialVersionUID = -8400435889624924293L;

    @SuppressLint("StaticFieldLeak")
    private transient static GlobalBase sGlobal;
    private transient Application mApp;
    /**
     * 序列化路径
     */
    private transient static File sSerializableFile;

    protected GlobalBase(Application app) {
        mApp = app;
    }

    static void initGlobal(String className, Application application) {
        Class<?> type = null;
        try {
            type = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (type == null) {
            throw new RuntimeException("Global class没找到");
        }

        initGlobal(className, application);
    }

    static void initGlobal(Class<?> type, Application application) {
        restore(application);

        if (sGlobal == null) {
            Constructor<?> constructor = null;

            try {
                constructor = type.getDeclaredConstructor(Application.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (constructor == null) {
                throw new RuntimeException("请设置<init>(Application)");
            }

            try {
                constructor.setAccessible(true);
                sGlobal = (GlobalBase) constructor.newInstance(application);
                sGlobal.initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T extends GlobalBase> T getGlobal(Class<T> type) {
        return type.cast(sGlobal);
    }

    public static GlobalBase getGlobal() {
        return sGlobal;
    }

    public Application getApplication() {
        return mApp;
    }

    /**
     * 初始化
     */
    public abstract void initialize();

    private static void restore(Application application) {
        sSerializableFile = new File(application.getCacheDir().getAbsolutePath() + File.separator + Encrypt.MD5("global"));
        if (sSerializableFile.exists() && sGlobal == null) {
            ObjectInputStream ois = null;
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(sSerializableFile);
                ois = new ObjectInputStream(fis);
                sGlobal = (GlobalBase) ois.readObject();
                sGlobal.mApp = application;
                sGlobal.initialize();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, e.toString());
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public abstract boolean isChange();

    protected void onSave() {
        FileOutputStream baos = null;
        ObjectOutputStream oos = null;

        try {
            baos = new FileOutputStream(sSerializableFile);
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.toString());
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 序列号保存
     */
    public final void save() {
        if (isChange()) {
            onSave();
        }
    }
}
