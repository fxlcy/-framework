package cn.fxlcy.framework.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;

import cn.fxlcy.framework.R;
import cn.fxlcy.framework.basis.adapter.HeaderFooterRecyclerViewAdapter;
import cn.fxlcy.framework.util.Loading;
import cn.fxlcy.framework.util.LoadingState;
import cn.fxlcy.framework.widget.recyclerview.decoration.HorizontalDividerItemDecoration;
import cn.fxlcy.framework.widget.recyclerview.itemanimation.SlideInOutBottomItemAnimator;

/**
 * Created by fxlcy on 2016/3/11.
 */
public class ObservableRecyclerView extends RecyclerView {
    private final ViewConfiguration mViewConfiguration;
    private ArrayList<cn.fxlcy.framework.widget.OnScrollChangeListener> mOnScrollChangeListeners;
    private OnUpPullLoadingListener mOnUpPullLoadingListener;
    private HorizontalDividerItemDecoration mItemDecoration;
    private ItemAnimator mAnimator;
    private VelocityTracker mVelocityTracker;


    private float mOldX, mOldY;

    public ObservableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mAnimator = new SlideInOutBottomItemAnimator(this);
        mItemDecoration = new HorizontalDividerItemDecoration.Builder(getContext()).build();

        this.setLayoutManager(manager);
        this.setItemAnimator(mAnimator);
        this.addItemDecoration(mItemDecoration);

        mViewConfiguration = ViewConfiguration.get(context);
    }

    public ObservableRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.recyclerViewStyle);
    }

    public ObservableRecyclerView(Context context) {
        this(context, null);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mOnScrollChangeListeners != null) {
            for (cn.fxlcy.framework.widget.OnScrollChangeListener scrollChangeListener : mOnScrollChangeListeners) {
                scrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
            }
        }

        if (mOnUpPullLoadingListener != null && isBottom()) {
            Adapter adapter = getAdapter();
            if (adapter instanceof Loading && ((Loading) adapter).loadingState() != LoadingState.bottom) {
                mOnUpPullLoadingListener.onUpPull(this);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mOnUpPullLoadingListener != null) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(e);

            int action = e.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mOldX = e.getX();
                    mOldY = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float x = e.getX() - mOldX;
                    float y = e.getY() - mOldY;

                    if (y < 0 && Math.abs(y) > Math.abs(x)) {
                        mVelocityTracker.computeCurrentVelocity(1000, mViewConfiguration.getScaledMaximumFlingVelocity());
                        float velocityY = mVelocityTracker.getYVelocity();
                        if (Math.abs(velocityY) > mViewConfiguration.getScaledMinimumFlingVelocity() && isBottom()) {
                            mOnUpPullLoadingListener.onUpPull(this);
                        }
                    }

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    break;
            }
        }

        return super.onTouchEvent(e);
    }

    public void addOnScrollChangeListener(cn.fxlcy.framework.widget.OnScrollChangeListener listener) {
        if (mOnScrollChangeListeners == null) {
            mOnScrollChangeListeners = new ArrayList<>();
        }

        mOnScrollChangeListeners.add(listener);
    }

    public void removeOnScrollChangeListener(cn.fxlcy.framework.widget.OnScrollChangeListener listener) {
        if (mOnScrollChangeListeners != null) {
            mOnScrollChangeListeners.remove(listener);
        }
    }

    public void removeOnScrollChangeListener(int index) {
        if (mOnScrollChangeListeners != null) {
            mOnScrollChangeListeners.remove(index);
        }
    }

    @Override
    public void setItemAnimator(ItemAnimator animator) {
        super.setItemAnimator(animator);
        mAnimator = animator;
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        super.addItemDecoration(decor);

        if (mItemDecoration != null) {
            removeItemDecoration(mItemDecoration);
            mItemDecoration = null;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof HeaderFooterRecyclerViewAdapter) {
            ((HeaderFooterRecyclerViewAdapter) adapter).setParent(this);
        }

        super.setAdapter(adapter);
    }

    public boolean isTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.getItemCount() == 0) {
                return true;
            }
            if (layoutManager.findFirstVisibleItemPosition() == 0) {
                View view = layoutManager.findViewByPosition(0);

                if (view != null) {
                    RecyclerView.LayoutParams params = (LayoutParams) view.getLayoutParams();
                    return view.getTop() == params.topMargin;
                }
            }

            return false;
        }
        throw new RuntimeException("请在使用LinearLayoutManager时使用此方法");
    }

    public boolean isBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager != null) {
            int index = this.getAdapter().getItemCount() - 1;
            return this.getChildCount() == 0 || layoutManager.findLastCompletelyVisibleItemPosition() == index;
        }


        throw new RuntimeException("请在使用LinearLayoutManager时使用此方法");
    }

    public void setOnUpPullLoadingListener(OnUpPullLoadingListener loadingListener) {
        mOnUpPullLoadingListener = loadingListener;
    }

    public interface OnUpPullLoadingListener {
        void onUpPull(View view);
    }
}
