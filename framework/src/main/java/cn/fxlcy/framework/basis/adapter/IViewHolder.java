package cn.fxlcy.framework.basis.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by fxlcy
 * on 2017/1/31
 *
 * @author fxlcy
 * @version 1.0
 */
public interface IViewHolder {
    View getView(@IdRes int viewId);

    <V extends View> V getView(@IdRes int viewId, Class<V> clazz);

    View getConvertView();

    <V extends View> V getConvertView(Class<V> clazz);

    void setText(@IdRes int viewId, @StringRes int resId);

    void setText(@IdRes int viewId, CharSequence text);

    void setBackground(@IdRes int viewId, @DrawableRes int resId);

    void setBackground(@IdRes int viewId, Drawable drawable);

    void setBackgroundColor(@IdRes int viewId, int color);

    void setTextColorResource(@IdRes int viewId, @ColorRes int colorRes);

    void setTextColor(@IdRes int viewId, ColorStateList colorStateList);

    void setTextColor(@IdRes int viewId, int color);

    void setImageResource(@IdRes int viewId, @DrawableRes int resId);

    void setChecked(@IdRes int viewId, boolean isChecked);

    boolean isChecked(@IdRes int viewId);
}
