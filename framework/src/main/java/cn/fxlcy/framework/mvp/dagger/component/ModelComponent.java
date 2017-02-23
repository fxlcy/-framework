package cn.fxlcy.framework.mvp.dagger.component;

import cn.fxlcy.framework.mvp.dagger.module.ContextModule;
import cn.fxlcy.framework.mvp.BaseModel;
import dagger.Component;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */
@Component(modules = ContextModule.class)
public interface ModelComponent {
    void inject(BaseModel model);
}
