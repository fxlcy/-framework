package cn.fxlcy.framework.widget;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


/**
 * Created fxlcy xc on 2016/8/10.
 */
public class DefaultCreateStateViewImpl implements CreateStateView, ComponentCallbacks {
    private final static int VIEW_CONTENT = 1;
    private final static int VIEW_ERROR = 2;
    private final static int VIEW_EMPTY = 3;
    private final static int VIEW_LOADING = 4;

    private int mCurrentView = VIEW_CONTENT;

    private AlphaAnimation mHideAnim;
    private AlphaAnimation mShowAnim;

    private View mErrorView;
    private View mEmptyView;
    private View mLoadingView;

    private Context mContext;
    private CreateStateView mCreateStateView;
    private IViewGroup mContentView;


    private boolean mHideAnimIsRun = false;
    private Animation.AnimationListener mHideAnimListener;

    private int[] mStateViewLayout;

    public DefaultCreateStateViewImpl(Context context, CreateStateView impl, IViewGroup contentView,
                                      @LayoutRes int loadingLayout, @LayoutRes int errorLayout, @LayoutRes int emptyLayout) {
        mContext = context;
        mCreateStateView = impl;
        mContentView = contentView;
        mStateViewLayout = new int[]{
                loadingLayout, errorLayout, emptyLayout
        };
    }

    /**
     * 创建错误视图(请求错误时显示)
     */

    @Override
    public View onCreateErrorView() {
        if (mErrorView != null) {
            return mErrorView;
        } else {
            mErrorView = LayoutInflater.from(mContext).inflate(mStateViewLayout[1],
                    mContentView.getParent(), false);
            return mErrorView;
        }
    }

    /**
     * 创建空白视图(请求内容为空时显示)
     */

    @Override
    public View onCreateEmptyView() {
        if (mEmptyView != null) {
            return mEmptyView;
        } else {
            mEmptyView = LayoutInflater.from(mContext).inflate(mStateViewLayout[2]
                    , mContentView.getParent(), false);
            return mEmptyView;
        }
    }


    @Override
    public View onCreateLoadingView() {
        if (mLoadingView != null) {
            return mLoadingView;
        } else {
            mLoadingView = LayoutInflater.from(mContext).inflate(mStateViewLayout[0]
                    , mContentView.getParent(), false);

            return mLoadingView;
        }
    }

    @Override
    public void showErrorView(Object arg) {
        if (mCurrentView != VIEW_ERROR && mCreateStateView != null) {
            innerHide(VIEW_ERROR);
            View errorView = mCreateStateView.onCreateErrorView();

            if (mContentView.indexOfChild(errorView) == -1) {
                innerShow(errorView);
            }
        }
    }

    @Override
    public void showEmptyView(Object arg) {
        if (mCurrentView != VIEW_EMPTY && mCreateStateView != null) {
            innerHide(VIEW_EMPTY);
            View emptyView = mCreateStateView.onCreateEmptyView();
            if (mContentView.indexOfChild(emptyView) == -1) {
                innerShow(emptyView);
            }
        }
    }

    @Override
    public void showLoadingView(Object arg) {
        if (mCurrentView != VIEW_LOADING && mCreateStateView != null) {
            innerHide(VIEW_LOADING);
            View loadingView = mCreateStateView.onCreateLoadingView();
            if (mContentView.indexOfChild(loadingView) == -1) {
                innerShow(loadingView);
            }
        }
    }

    @Override
    public void showContentView(Object arg) {
        if (mCurrentView != VIEW_CONTENT) {
            innerHide(VIEW_CONTENT);
        }
    }

    private void innerShow(final View view) {
        if (view == null) {
            return;
        }

        if (mShowAnim == null) {
            mShowAnim = new AlphaAnimation(0, 1);
            mShowAnim.setDuration(300);
        }

        mContentView.addView(view);
        view.startAnimation(mShowAnim);
    }

    private void innerHide(int view) {
        View _view = null;
        switch (mCurrentView) {
            case VIEW_CONTENT:

                break;
            case VIEW_ERROR:
                _view = mCreateStateView.onCreateErrorView();
                break;
            case VIEW_EMPTY:
                _view = mCreateStateView.onCreateEmptyView();
                break;
            case VIEW_LOADING:
                _view = mCreateStateView.onCreateLoadingView();
                break;
        }

        mCurrentView = view;

        if (_view != null) {
            if (mHideAnim == null) {
                mHideAnim = new AlphaAnimation(1, 0);
                mHideAnim.setDuration(300);
            }

            final View final_view = _view;

            if (mHideAnimIsRun) {
                mHideAnimListener.onAnimationEnd(null);
                mHideAnim.cancel();
            }

            mHideAnimListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mHideAnimIsRun = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mHideAnimIsRun = false;
                    mContentView.removeView(final_view);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
            mHideAnim.setAnimationListener(mHideAnimListener);
            _view.startAnimation(mHideAnim);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {
        if (mCurrentView != VIEW_EMPTY) {
            mEmptyView = null;
        }

        if (mCurrentView != VIEW_ERROR) {
            mErrorView = null;
        }

        if (mCurrentView != VIEW_LOADING) {
            mLoadingView = null;
        }
    }
}
