package cn.fxlcy.framework.mvp.dagger;

import android.content.Context;

import javax.inject.Inject;

import cn.fxlcy.framework.mvp.BaseModel;
import cn.fxlcy.framework.mvp.BasePresenter;
import cn.fxlcy.framework.mvp.dagger.component.BaseComponent;
import cn.fxlcy.framework.mvp.dagger.component.DaggerModelComponent;
import cn.fxlcy.framework.mvp.dagger.component.ModelComponent;
import cn.fxlcy.framework.mvp.dagger.module.ContextModule;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */
public final class MvpInjectTarget<P extends BasePresenter> {
    @Inject
    P mPresenter;
    private BaseComponent<P> mComponent;
    private ModelComponent mModelComponent;

    public MvpInjectTarget(BaseComponent<P> component) {
        mComponent = component;
    }

    public MvpInjectTarget<P> injectContext(Context context) {
        mModelComponent = DaggerModelComponent.builder().contextModule(new ContextModule(context))
                .build();

        return this;
    }

    public P inject() {
        mComponent.inject(this);
        Object model = mPresenter.getModel();
        if (mModelComponent != null && model instanceof BaseModel) {
            mModelComponent.inject((BaseModel) mPresenter.getModel());
        }

        return mPresenter;
    }

    public P getPresenter() {
        return mPresenter;
    }
}
