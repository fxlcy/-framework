package cn.fxlcy.framework.widget;

/**
 * Created by fxlcy
 * on 2017/1/22
 *
 * @author fxlcy
 * @version 1.0
 */
public interface ToggleStateView {
    void showErrorView(Object arg);

    void showEmptyView(Object arg);

    void showLoadingView(Object arg);

    void showContentView(Object arg);
}
