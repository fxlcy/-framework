package cn.fxlcy.framework.basis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import cn.fxlcy.framework.net.NetworkState;
import cn.fxlcy.framework.net.OnNetStateChangedListener;
import cn.fxlcy.framework.receiver.NetworkStateBroadcastReceiver;
import cn.fxlcy.framework.util.Encrypt;
import cn.fxlcy.framework.util.NetUtils;

/**
 * Created by fxlcy on 2016/12/1.
 * 保存全局的参数（伴随整个Application生命周期）
 */

public abstract class GlobalBase implements Serializable, ActivityList, OnNetStateChangedListener {
    private transient final static String TAG = GlobalBase.class.getSimpleName();
    private static final long serialVersionUID = 4116933511386539517L;


    @SuppressLint("StaticFieldLeak")
    private transient static GlobalBase sGlobal;
    private transient Application mApp;
    private final transient Stack<Activity> mActivitys = new Stack<>();
    private transient
    @NetworkState
    int mNetworkState = -1;

    /**
     * 序列化路径
     */
    private transient static File sSerializableFile;


    protected GlobalBase(Application app) {
        if (sGlobal == null) {
            mApp = app;
        } else {
            throw new RuntimeException("single instance");
        }

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

    public <T extends Application> T getApplication(Class<T> clazz) {
        return clazz.cast(mApp);
    }

    /**
     * 初始化
     */
    public void initialize() {
        NetworkStateBroadcastReceiver.register(getApplication());//注册监听网络广播的receiver
    }


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


    public List<Activity> getActivitys() {
        return mActivitys;
    }


    public int getNetState() {
        return mNetworkState;
    }

    @Override
    public void addActivity(Activity activity) {
        mActivitys.add(activity);
    }

    @Override
    public Activity getCurrentActivity() {
        return mActivitys.peek();
    }

    @Override
    public boolean removeActivity(Activity object) {
        return mActivitys.remove(object);
    }

    @Override
    public Activity removeActivity(int position) {
        return mActivitys.remove(position);
    }

    @Override
    public boolean removeActivity(Class<? extends Activity> clazz) {
        if (mActivitys.size() == 0) {
            return false;
        }

        ListIterator<Activity> iterator = mActivitys.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass() == clazz) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public Activity getActivity(int position) {
        return mActivitys.get(position);
    }

    @Override
    public <A extends Activity> A getActivity(int position, Class<A> clazz) {
        Activity activity = getActivity(position);
        if (activity != null) {
            return clazz.cast(activity);
        }
        return null;
    }

    @Override
    public void clearActivity() {
        mActivitys.clear();
    }

    @Override
    public void finishActivity(Activity activity) {
        if (mActivitys.remove(activity)) {
            activity.finish();
        }
    }

    @Override
    public void finishActivity(int position) {
        Activity activity = mActivitys.get(position);
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void finishActivity(Class<? extends Activity> clazz) {
        Iterator<Activity> iterator = mActivitys.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass() == clazz) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    @Override
    public void finishAllActivity() {
        Iterator<Activity> iterator = mActivitys.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            iterator.remove();
            activity.finish();
        }
    }

    @Override
    public boolean containsActivity(Activity activity) {
        return mActivitys.contains(activity);
    }

    @Override
    public int activitySize() {
        return mActivitys.size();
    }


    @Override
    public void onNetStateChanged(@NetworkState int networkState) {
        for (Activity activity : mActivitys) {
            if (activity instanceof OnNetStateChangedListener) {
                ((OnNetStateChangedListener) activity).onNetStateChanged(networkState);
            }
        }
    }

    /**
     * 更新网络状态
     */
    public int updateNetworkState() {
        @NetworkState int networkState = NetUtils.getNetworkState(getApplication().getApplicationContext());
        if (mNetworkState != networkState) {
            mNetworkState = networkState;
            onNetStateChanged(networkState);
        }

        return networkState;
    }
}
