package cn.fxlcy.framework.basis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import cn.fxlcy.framework.widget.CreateStateView;
import cn.fxlcy.framework.widget.DefaultCreateStateViewImpl;
import cn.fxlcy.framework.widget.IViewGroup;

/**
 * Created by fxlcy
 * on 2017/1/21.
 *
 * @author fxlcy
 * @version 1.0
 */
public abstract class BaseActivity extends AppCompatActivity implements CreateStateView {
    private CreateStateView mCreateStateView;
    private ViewGroup mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        GlobalBase.getGlobal().addActivity(this);
        super.onCreate(savedInstanceState);

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityCreated(this, savedInstanceState);
        }

        mCreateStateView = new DefaultCreateStateViewImpl(this, this, new IViewGroup() {
            @Override
            public void addView(View view) {
                getContentView().addView(view);
            }

            @Override
            public void removeView(View view) {
                getContentView().removeView(view);
            }

            @Override
            public ViewGroup getParent() {
                return getContentView();
            }

            @Override
            public int indexOfChild(View view) {
                return getContentView().indexOfChild(view);
            }
        }, 0, 0, 0);
    }


    @Override
    protected void onStart() {
        super.onRestart();

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityStarted(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityResumed(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityPaused(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityStopped(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBase.getGlobal().removeActivity(this);

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityDestroyed(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivitySaveInstanceState(this, outState);
        }
    }

    private BaseApplication getBaseApp() {
        return GlobalBase.getGlobal().getApplication(BaseApplication.class);
    }

    @Override
    public View onCreateErrorView() {
        return mCreateStateView.onCreateErrorView();
    }

    @Override
    public View onCreateEmptyView() {
        return mCreateStateView.onCreateEmptyView();
    }

    @Override
    public View onCreateLoadingView() {
        return mCreateStateView.onCreateLoadingView();
    }

    @Override
    public void showErrorView(Object arg) {
        mCreateStateView.showErrorView(arg);
    }

    @Override
    public void showEmptyView(Object arg) {
        mCreateStateView.showEmptyView(arg);
    }

    @Override
    public void showLoadingView(Object arg) {
        mCreateStateView.showLoadingView(arg);
    }

    @Override
    public void showContentView(Object arg) {
        mCreateStateView.showContentView(arg);
    }

    public ViewGroup getContentView() {
        if (mContentView == null) {
            mContentView = (ViewGroup) findViewById(android.R.id.content);
        }
        return mContentView;
    }
}
