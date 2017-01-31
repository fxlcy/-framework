package cn.fxlcy.framework.basis.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by fxlcy on 2016/8/17.
 */
public class ViewHolder extends RecyclerView.ViewHolder implements IViewHolder {
    private ViewHolderDelegate mDelegate;

    public ViewHolder(View itemView) {
        super(itemView);
        mDelegate = new ViewHolderDelegate(itemView);
    }

    @Override
    public View getView(@IdRes int viewId) {
        return mDelegate.getView(viewId);
    }

    @Override
    public <V extends View> V getView(@IdRes int viewId, Class<V> clazz) {
        return mDelegate.getView(viewId, clazz);
    }

    @Override
    public View getConvertView() {
        return mDelegate.getConvertView();
    }

    @Override
    public <V extends View> V getConvertView(Class<V> clazz) {
        return mDelegate.getConvertView(clazz);
    }

    @Override
    public void setText(@IdRes int viewId, @StringRes int resId) {
        mDelegate.setText(viewId, resId);
    }

    @Override
    public void setText(@IdRes int viewId, CharSequence text) {
        mDelegate.setText(viewId, text);
    }

    @Override
    public void setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        mDelegate.setImageResource(viewId, resId);
    }

}
