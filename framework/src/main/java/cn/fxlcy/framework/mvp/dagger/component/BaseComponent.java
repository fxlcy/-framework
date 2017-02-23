package cn.fxlcy.framework.mvp.dagger.component;

import cn.fxlcy.framework.mvp.dagger.MvpInjectTarget;
import cn.fxlcy.framework.mvp.BasePresenter;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */
public interface BaseComponent<P extends BasePresenter> {
    void inject(MvpInjectTarget<P> target);
}
