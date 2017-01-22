package cn.fxlcy.framework.widget;

import android.view.View;

/**
 * Created by fxlcy on 2016/8/10.
 * 不同状态的视图
 */
public interface StateView {

    View onCreateErrorView();

    View onCreateEmptyView();

    View onCreateLoadingView();


}
