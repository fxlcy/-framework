package cn.fxlcy.framework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import cn.fxlcy.framework.basis.BaseActivity;
import cn.fxlcy.framework.component.DaggerTestComponent;
import cn.fxlcy.framework.manager.Cache;
import cn.fxlcy.framework.mvp.dagger.MvpInjectTarget;
import cn.fxlcy.framework.mvp.dagger.module.ContextModule;
import cn.fxlcy.framework.example.R;
import cn.fxlcy.framework.module.TestModule;
import cn.fxlcy.framework.mvp.presenter.ITestPresenter;
import cn.fxlcy.framework.mvp.view.ITestView;
import cn.fxlcy.framework.util.Encrypt;
import cn.fxlcy.framework.util.SdcardUtils;

public class MainActivity extends BaseActivity implements ITestView {
    ITestPresenter mPresenter;
    private Cache mCache;

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


        mCache = Cache.create(SdcardUtils.getDiskCacheDir(getApplicationContext(), "cache"), 1024 * 1024 * 100);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId();
                switch (id) {
                    case R.id.write:
                        mCache.edit().put("aaa", "涉及到佛世界的佛教哦哦");
                        break;
                    case R.id.read:
                        String str = mCache.snapshot().getString("aaa");
                        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViewById(R.id.read).setOnClickListener(clickListener);
        findViewById(R.id.write).setOnClickListener(clickListener);
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
