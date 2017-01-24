package cn.fxlcy.framework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.fxlcy.framework.basis.BaseActivity;
import cn.fxlcy.framework.component.DaggerTestComponent;
import cn.fxlcy.framework.mvp.dagger.MvpInjectTarget;
import cn.fxlcy.framework.mvp.dagger.module.ContextModule;
import cn.fxlcy.framework.example.R;
import cn.fxlcy.framework.module.TestModule;
import cn.fxlcy.framework.mvp.presenter.ITestPresenter;
import cn.fxlcy.framework.mvp.view.ITestView;
import cn.fxlcy.framework.util.Encrypt;

public class MainActivity extends BaseActivity implements ITestView {
    ITestPresenter mPresenter;
    private Button mBtn;

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
    public void onContentChanged() {
        super.onContentChanged();

        mBtn = (Button) findViewById(R.id.btn);
        registerForContextMenu(mBtn);
    }


    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
