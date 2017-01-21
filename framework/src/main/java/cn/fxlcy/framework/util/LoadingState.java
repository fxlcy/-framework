package cn.fxlcy.framework.util;

import android.support.annotation.IntDef;

import static cn.fxlcy.framework.util.LoadingState.bottom;
import static cn.fxlcy.framework.util.LoadingState.error;
import static cn.fxlcy.framework.util.LoadingState.loading;
import static cn.fxlcy.framework.util.LoadingState.normal;

/**
 * Created by fxlcy
 * on 2017/1/21.
 *
 * @author fxlcy
 * @version 1.0
 *          加载状态
 */

@IntDef({loading, normal, error, bottom})
public @interface LoadingState {
    /**
     * 加载中
     */
    int loading = 0;
    /**
     * 普通状态
     */
    int normal = 1;
    /**
     * 出错了
     */
    int error = 2;
    /**
     * 到底了
     */
    int bottom = 3;
}