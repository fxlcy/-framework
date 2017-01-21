package cn.fxlcy.framework.widget;

import android.view.View;

/**
 * Created by fxlcy on 2016/8/10.
 * 不同状态的视图
 */
public interface CreateStateView {

    View onCreateErrorView();

    View onCreateEmptyView();

    View onCreateLoadingView();

    void showErrorView(Object arg);

    void showEmptyView(Object arg);

    void showLoadingView(Object arg);

    void showContentView(Object arg);
}
