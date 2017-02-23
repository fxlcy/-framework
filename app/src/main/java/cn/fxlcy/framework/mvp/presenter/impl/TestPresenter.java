package cn.fxlcy.framework.mvp.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

import cn.fxlcy.framework.mvp.BasePresenter;
import cn.fxlcy.framework.mvp.model.ITestModel;
import cn.fxlcy.framework.mvp.presenter.ITestPresenter;
import cn.fxlcy.framework.mvp.view.ITestView;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */

public class TestPresenter extends BasePresenter<ITestModel, ITestView> implements ITestPresenter {
    @Inject
    public TestPresenter(ITestModel model, ITestView view) {
        super(model, view);
    }

    @Override
    public void toast() {
        getView().showLoadingView(null);
        Toast.makeText(getContext(), "context ！= null", Toast.LENGTH_SHORT).show();
        getView().toast(getModel().getTestText());
    }
}
