package cn.fxlcy.framework.widget;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fxlcy on 2016/10/25.
 */

public interface HeaderFooterView {
    void setHeaderView(@LayoutRes int layoutRes);

    void setHeaderView(View view);

    void setFooterView(@LayoutRes int layoutRes);

    void setFooterView(View view);

    View getHeaderView();

    View getFooterView();

    View findHeaderViewById(@IdRes int viewId);

    <V extends View> View findHeaderViewById(@IdRes int viewId, Class<V> viewType);

    View findFooterViewById(@IdRes int viewId);

    <V extends View> View findFooterViewById(@IdRes int viewId, Class<V> viewType);

    void setParent(ViewGroup viewGroup);
}
