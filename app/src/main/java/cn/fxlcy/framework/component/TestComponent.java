package cn.fxlcy.framework.component;

import cn.fxlcy.framework.mvp.dagger.component.BaseComponent;
import cn.fxlcy.framework.mvp.dagger.module.ContextModule;
import cn.fxlcy.framework.module.TestModule;
import cn.fxlcy.framework.mvp.presenter.impl.TestPresenter;
import dagger.Component;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */
@Component(modules = {TestModule.class, ContextModule.class})
public interface TestComponent extends BaseComponent<TestPresenter> {
}
