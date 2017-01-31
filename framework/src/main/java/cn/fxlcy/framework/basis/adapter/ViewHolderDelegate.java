package cn.fxlcy.framework.basis.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    public ViewHolderDelegate(View itemView) {
        mItemView = itemView;
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
        TextView tv = getView(viewId,TextView.class);
        
    }

    @Override
    public void setText(@IdRes int viewId, CharSequence text) {

    }

    @Override
    public void setImageResource(@IdRes int viewId, @DrawableRes int resId) {

    }


}
