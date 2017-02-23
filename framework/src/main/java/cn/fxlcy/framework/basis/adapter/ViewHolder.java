package cn.fxlcy.framework.basis.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    public void setBackground(@IdRes int viewId, @DrawableRes int resId) {
        mDelegate.setBackground(viewId, resId);
    }

    @Override
    public void setBackground(@IdRes int viewId, Drawable drawable) {
        mDelegate.setBackground(viewId, drawable);
    }

    @Override
    public void setBackgroundColor(@IdRes int viewId, int color) {
        mDelegate.setBackgroundColor(viewId, color);
    }

    @Override
    public void setTextColorResource(@IdRes int viewId, @ColorRes int colorRes) {
        mDelegate.setTextColorResource(viewId, colorRes);
    }

    @Override
    public void setTextColor(@IdRes int viewId, ColorStateList colorStateList) {
        mDelegate.setTextColor(viewId, colorStateList);
    }

    @Override
    public void setTextColor(@IdRes int viewId, int color) {
        mDelegate.setTextColor(viewId, color);
    }

    @Override
    public void setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        mDelegate.setImageResource(viewId, resId);
    }

    @Override
    public void setChecked(@IdRes int viewId, boolean isChecked) {
        mDelegate.setChecked(viewId, isChecked);
    }

    @Override
    public boolean isChecked(@IdRes int viewId) {
        return mDelegate.isChecked(viewId);
    }
}
