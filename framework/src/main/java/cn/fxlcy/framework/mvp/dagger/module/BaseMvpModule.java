package cn.fxlcy.framework.mvp.dagger.module;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */
public interface BaseMvpModule<M, V> {
    M provideModel();

    V provideView();
}
