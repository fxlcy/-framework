package cn.fxlcy.framework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cn.fxlcy.framework.component.DaggerTestComponent;
import cn.fxlcy.framework.mvp.dagger.MvpInjectTarget;
import cn.fxlcy.framework.mvp.dagger.module.ContextModule;
import cn.fxlcy.framework.example.R;
import cn.fxlcy.framework.module.TestModule;
import cn.fxlcy.framework.mvp.presenter.ITestPresenter;
import cn.fxlcy.framework.mvp.view.ITestView;
import cn.fxlcy.framework.util.Encrypt;

public class MainActivity extends AppCompatActivity implements ITestView {
    ITestPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPresenter = new MvpInjectTarget<>(DaggerTestComponent.builder().contextModule(new ContextModule(this))
                .testModule(new TestModule(this))
                .build())
                .injectContext(this)
                .inject();
        mPresenter.toast();


        Encrypt.encryptStr("哈哈哈哈哈哈或或");
    }

    @Override
    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
