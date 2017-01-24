package cn.fxlcy.framework.basis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import cn.fxlcy.framework.R;
import cn.fxlcy.framework.widget.DefaultStateViewImpl;
import cn.fxlcy.framework.widget.IViewGroup;
import cn.fxlcy.framework.widget.StateView;

/**
 * Created by fxlcy
 * on 2017/1/21.
 *
 * @author fxlcy
 * @version 1.0
 */
public abstract class BaseActivity extends AppCompatActivity implements StateView {
    private StateView mStateView;
    private ViewGroup mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        GlobalBase.getGlobal().addActivity(this);
        super.onCreate(savedInstanceState);

        BaseApplication app = getBaseApp();
        if (app != null) {
            app.dispatchActivityCreated(this, savedInstanceState);
        }

        mStateView = new DefaultStateViewImpl(this, this, new IViewGroup() {
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
        }, R.layout.layout_loading, R.layout.layout_error, R.layout.layout_loading);
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
        return mStateView.onCreateErrorView();
    }

    @Override
    public View onCreateEmptyView() {
        return mStateView.onCreateEmptyView();
    }

    @Override
    public View onCreateLoadingView() {
        return mStateView.onCreateLoadingView();
    }

    @Override
    public void showErrorView(Object arg) {
        mStateView.showErrorView(arg);
    }

    @Override
    public void showEmptyView(Object arg) {
        mStateView.showEmptyView(arg);
    }

    @Override
    public void showLoadingView(Object arg) {
        mStateView.showLoadingView(arg);
    }

    @Override
    public void showContentView(Object arg) {
        mStateView.showContentView(arg);
    }

    public ViewGroup getContentView() {
        if (mContentView == null) {
            mContentView = (ViewGroup) findViewById(android.R.id.content);
        }
        return mContentView;
    }
}
