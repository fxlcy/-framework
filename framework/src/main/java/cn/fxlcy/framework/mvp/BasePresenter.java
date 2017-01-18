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

public class BasePresenter<M, V> {
    private M mModel;
    private V mView;
    @Inject
    WeakReference<Context> mContext;

    public BasePresenter(M model, V view) {
        this.mModel = model;
        this.mView = view;
    }

    public BasePresenter(Context context, M model, V view) {
        this(model, view);
        this.mContext = new WeakReference<>(context);
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        return mModel;
    }

    protected Context getContext() {
        return mContext.get();
    }
}
