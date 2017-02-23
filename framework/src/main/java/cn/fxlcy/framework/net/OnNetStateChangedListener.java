package cn.fxlcy.framework.net;

/**
 * Created by fxlcy
 * on 2017/1/21.
 *
 * @author fxlcy
 * @version 1.0
 *          网络状态改变事件
 */
public interface OnNetStateChangedListener {
    /**
     * @param networkState {@link NetworkState}
     */
    void onNetStateChanged(@NetworkState int networkState);
}
