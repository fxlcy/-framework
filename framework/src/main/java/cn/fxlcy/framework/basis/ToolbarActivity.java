package cn.fxlcy.framework.basis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import cn.fxlcy.framework.util.Compat;

public abstract class ToolbarActivity extends BaseActivity {
    private ToolbarHelper mToolBarHelper;
    private Toolbar mToolbar;
    private View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        mToolBarHelper = new ToolbarHelper(this, layoutResID);
        mToolbar = mToolBarHelper.getToolBar();
        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(mToolbar);
        /*自定义的一些操作*/
        onCreateCustomToolbar(mToolbar);
    }

    public void onCreateCustomToolbar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
        mToolBarHelper.mTitleView.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolBarHelper.mTitleView.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitleColor(int textColor) {
        setTitleTextColor(textColor);
    }

    public void setTitleTextColor(int textColor) {
        mToolBarHelper.mTitleView.setTextColor(textColor);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }


    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        ActionBar actionBar = super.getSupportActionBar();
        if (actionBar == null) {
            throw new NullPointerException("getSupportActionBar为null");
        }
        return actionBar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = super.onCreateLoadingView();
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLoadingView.getLayoutParams();

            mLoadingView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    params.topMargin += mToolbar.getHeight() + ((ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams()).topMargin;
                    Compat.removeOnGlobalLayoutListener(mLoadingView, this);
                }
            });
        }
        return mLoadingView;
    }
}