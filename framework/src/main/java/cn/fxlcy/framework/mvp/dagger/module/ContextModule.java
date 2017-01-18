package cn.fxlcy.framework.mvp.dagger.module;

import android.content.Context;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */

@Module
public class ContextModule {
    private WeakReference<Context> mContext;

    public ContextModule(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Provides
    WeakReference<Context> provideContext() {
        return mContext;
    }
}
