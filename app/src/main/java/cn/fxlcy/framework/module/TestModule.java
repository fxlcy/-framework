package cn.fxlcy.framework.module;

import cn.fxlcy.framework.mvp.dagger.module.BaseMvpModule;
import cn.fxlcy.framework.mvp.model.ITestModel;
import cn.fxlcy.framework.mvp.model.impl.TestModel;
import cn.fxlcy.framework.mvp.view.ITestView;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */
@Module
public class TestModule implements BaseMvpModule<ITestModel, ITestView> {
    private ITestView mView;

    public TestModule(ITestView view) {
        this.mView = view;
    }

    @Provides
    @Override
    public ITestModel provideModel() {
        return new TestModel();
    }

    @Provides
    @Override
    public ITestView provideView() {
        return mView;
    }
}
