package cn.fxlcy.framework.mvp;

import android.content.Context;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */

public class BaseModel {
    @Inject
    WeakReference<Context> mContext;

    public BaseModel(Context context) {
        mContext = new WeakReference<>(context);
    }

    public BaseModel() {
    }

    protected Context getContext() {
        return mContext.get();
    }
}
