package cn.fxlcy.framework.basis;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import cn.fxlcy.framework.manager.UncaughtExceptionHandler;

/**
 * Created by fxlcy
 * on 2017/1/21.
 *
 * @author fxlcy
 * @version 1.0
 */

public abstract class BaseApplication extends Application {
    private final ArrayList<ComponentCallbacks> mComponentCallbacks = new ArrayList<>();
    private final ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks =
            new ArrayList<>();

    private final static boolean sIsCompatComponentCallback = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    private Config mConfig;


    protected
    @NonNull
    abstract Config obtainConfig();

    private Config getConfig() {
        if (mConfig == null) {
            mConfig = obtainConfig();
        }

        return mConfig;
    }

    @Override
    protected void attachBaseContext(Context base) {
        UncaughtExceptionHandler.getInstance(this).register();
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        GlobalBase.initGlobal(getConfig().mGlobalClass, this);
    }

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.remove(callback);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        if (sIsCompatComponentCallback) {
            super.registerComponentCallbacks(callback);
        } else {
            synchronized (mComponentCallbacks) {
                mComponentCallbacks.add(callback);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        if (sIsCompatComponentCallback) {
            super.unregisterComponentCallbacks(callback);
        } else {
            synchronized (mComponentCallbacks) {
                mComponentCallbacks.remove(callback);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!sIsCompatComponentCallback) {
            ComponentCallbacks[] callbackses = collectComponentCallbacks();
            if (callbackses != null) {
                for (ComponentCallbacks callback : callbackses) {
                    callback.onConfigurationChanged(newConfig);
                }
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!sIsCompatComponentCallback) {
            ComponentCallbacks[] callbackses = collectComponentCallbacks();
            if (callbackses != null) {
                for (ComponentCallbacks callback : callbackses) {
                    callback.onLowMemory();
                }
            }
        }
    }


    void dispatchActivityCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacks) callback).onActivityCreated(activity,
                        savedInstanceState);
            }
        }
    }

    void dispatchActivityStarted(Activity activity) {
        ActivityLifecycleCallbacks[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (ActivityLifecycleCallbacks callback : callbacks) {
                (callback).onActivityStarted(activity);
            }
        }
    }

    void dispatchActivityResumed(Activity activity) {
        ActivityLifecycleCallbacks[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (ActivityLifecycleCallbacks callback : callbacks) {
                (callback).onActivityResumed(activity);
            }
        }
    }

    void dispatchActivityPaused(Activity activity) {
        ActivityLifecycleCallbacks[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (ActivityLifecycleCallbacks callback : callbacks) {
                (callback).onActivityPaused(activity);
            }
        }
    }

    void dispatchActivityStopped(Activity activity) {
        ActivityLifecycleCallbacks[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (ActivityLifecycleCallbacks callback : callbacks) {
                (callback).onActivityStopped(activity);
            }
        }
    }

    void dispatchActivitySaveInstanceState(Activity activity, Bundle outState) {
        ActivityLifecycleCallbacks[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (ActivityLifecycleCallbacks callback : callbacks) {
                (callback).onActivitySaveInstanceState(activity,
                        outState);
            }
        }
    }

    void dispatchActivityDestroyed(Activity activity) {
        ActivityLifecycleCallbacks[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (ActivityLifecycleCallbacks callback : callbacks) {
                (callback).onActivityDestroyed(activity);
            }
        }
    }

    private ActivityLifecycleCallbacks[] collectActivityLifecycleCallbacks() {
        ActivityLifecycleCallbacks[] callbacks = null;
        synchronized (mActivityLifecycleCallbacks) {
            if (mActivityLifecycleCallbacks.size() > 0) {
                callbacks = (ActivityLifecycleCallbacks[]) mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    private ComponentCallbacks[] collectComponentCallbacks() {
        ComponentCallbacks[] callbacks = null;
        synchronized (mComponentCallbacks) {
            if (mComponentCallbacks.size() > 0) {
                callbacks = (ComponentCallbacks[]) mComponentCallbacks.toArray();
            }
        }
        return callbacks;
    }


    public static class Config {
        private Class<? extends GlobalBase> mGlobalClass;

        public void setGlobalClass(Class<? extends GlobalBase> globalClass) {
            mGlobalClass = globalClass;
        }
    }


    public interface ActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity, Bundle savedInstanceState);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle outState);

        void onActivityDestroyed(Activity activity);
    }
}
