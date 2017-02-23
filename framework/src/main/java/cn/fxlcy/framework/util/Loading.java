package cn.fxlcy.framework.util;

/**
 * Created by fxlcy
 * on 2017/1/5.
 *
 * @author fxlcy
 * @version 1.0
 */

public interface Loading {
    void setLoadingState(@LoadingState int state);

    @LoadingState
    int loadingState();
}
