package cn.fxlcy.framework.basis.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.fxlcy.framework.util.ObjectUtils;

/**
 * Created by fxlcy
 * on 2017/1/31
 *
 * @author fxlcy
 * @version 1.0
 */
final class ViewHolderDelegate implements IViewHolder {
    private View mItemView;

    private SparseArray<View> mViews;
    private Context mContext;

    ViewHolderDelegate(View itemView) {
        mItemView = itemView;
        mContext = itemView.getContext();
    }


    @Override
    public View getView(@IdRes int viewId) {
        View view = mViews == null ? null : mViews.get(viewId);
        if (view != null) {
            return view;
        } else {
            view = mItemView.findViewById(viewId);
            if (view != null) {
                if (mViews == null) {
                    mViews = new SparseArray<>();
                }

                mViews.append(viewId, view);
            }

            return view;
        }
    }

    @Override
    public <V extends View> V getView(@IdRes int viewId, Class<V> clazz) {
        View view = getView(viewId);
        return view == null ? null : clazz.cast(view);
    }

    @Override
    public View getConvertView() {
        return mItemView;
    }

    @Override
    public <V extends View> V getConvertView(Class<V> clazz) {
        return mItemView == null ? null : clazz.cast(mItemView);
    }

    @Override
    public void setText(@IdRes int viewId, @StringRes int resId) {
        TextView tv = getView(viewId, TextView.class);
        ObjectUtils.requireNonNull(tv, "viewId:" + viewId + "== Null");

        tv.setText(resId);
    }

    @Override
    public void setText(@IdRes int viewId, CharSequence text) {
        TextView tv = getView(viewId, TextView.class);
        requireViewNonNull(viewId, tv);

        tv.setText(text);
    }

    @Override
    public void setBackground(@IdRes int viewId, @DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        setBackground(viewId, drawable);
    }

    @Override
    public void setBackground(@IdRes int viewId, Drawable drawable) {
        View v = getView(viewId);
        requireViewNonNull(viewId, v);

        ViewCompat.setBackground(v, drawable);
    }

    @Override
    public void setBackgroundColor(@IdRes int viewId, int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        setBackground(viewId, drawable);
    }

    @Override
    public void setTextColorResource(@IdRes int viewId, @ColorRes int colorRes) {
        ColorStateList color = ContextCompat.getColorStateList(mContext, colorRes);
        setTextColor(viewId, color);
    }

    @Override
    public void setTextColor(@IdRes int viewId, ColorStateList colorStateList) {
        TextView tv = getView(viewId, TextView.class);
        requireViewNonNull(viewId, tv);

        tv.setTextColor(colorStateList);
    }

    @Override
    public void setTextColor(@IdRes int viewId, int color) {
        setTextColor(viewId, ColorStateList.valueOf(color));
    }

    @Override
    public void setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView iv = getView(viewId, ImageView.class);
        requireViewNonNull(viewId, iv);

        iv.setImageResource(resId);
    }

    @Override
    public void setChecked(@IdRes int viewId, boolean isChecked) {
        CompoundButton cb = getView(viewId, CompoundButton.class);
        requireViewNonNull(viewId, cb);

        cb.setChecked(isChecked);
    }

    @Override
    public boolean isChecked(@IdRes int viewId) {
        CompoundButton cb = getView(viewId, CompoundButton.class);
        requireViewNonNull(viewId, cb);

        return cb.isChecked();
    }


    private void requireViewNonNull(@IdRes int viewId, View view) {
        ObjectUtils.requireNonNull(view, "viewId:" + viewId + "== Null");
    }
}
