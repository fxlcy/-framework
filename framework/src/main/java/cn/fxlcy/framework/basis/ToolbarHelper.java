package cn.fxlcy.framework.basis;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.StyleableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.fxlcy.framework.R;
import cn.fxlcy.framework.util.ScreenUtils;

final class ToolbarHelper {
    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    /*base view*/
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    private ViewGroup mToolbarContainer;

    /*toolbar*/
    private Toolbar mToolBar;

    /*视图构造器*/
    private LayoutInflater mInflater;

    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * 3、状态栏是否透明
    * */
    private final static int[] ATTRS;

    private final static
    @StyleableRes
    int STYLEABLE_WINDOWACTIONBAROVERLAY = 0;
    private final static
    @StyleableRes
    int STYLEABLE_ACTIONBARSIZE = 1;
    private final static
    @StyleableRes
    int STYLEABLE_WINDOWTRANSLUCENTSTATUS = 2;

    TextView mTitleView;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ATTRS = new int[]{
                    R.attr.windowActionBarOverlay,
                    R.attr.actionBarSize,
                    android.R.attr.windowTranslucentStatus
            };
        } else {
            ATTRS = new int[]{
                    R.attr.windowActionBarOverlay,
                    R.attr.actionBarSize
            };
        }
    }

    ToolbarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        /*初始化整个内容*/
        initContentView();
        /*初始化toolbar*/
        initToolBar();
        /*初始化用户定义的布局*/
        initView(layoutId);
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        mToolbarContainer = (ViewGroup) mInflater.inflate(R.layout.toolbar, mContentView);
        mToolBar = (Toolbar) mToolbarContainer.findViewById(R.id.toolbar);
        mTitleView = (TextView) mToolBar.findViewById(R.id.toolbar_title);
    }

    private void initView(int id) {
        mUserView = mInflater.inflate(id, mContentView, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUserView.getLayoutParams();
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);

        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(STYLEABLE_WINDOWACTIONBAROVERLAY, false);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) typedArray.getDimension(STYLEABLE_ACTIONBARSIZE, (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));

        boolean isTran = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isTran = typedArray.getBoolean(STYLEABLE_WINDOWTRANSLUCENTSTATUS, false);
        }

        typedArray.recycle();

        int topMargin = toolBarSize + params.topMargin;

        if (isTran) {
            int statusHeight = ScreenUtils.getStatusHeight(mContext);
            topMargin += statusHeight;
            ((ViewGroup.MarginLayoutParams) mToolBar.getLayoutParams()).topMargin += statusHeight;
            View statusView = new View(mContext);
            statusView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight));
            ViewCompat.setBackground(statusView, mToolBar.getBackground());

            mToolbarContainer.addView(statusView, 0);
        }

        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = overly ? params.topMargin : topMargin;
        mContentView.addView(mUserView, params);

    }

    FrameLayout getContentView() {
        return mContentView;
    }

    Toolbar getToolBar() {
        return mToolBar;
    }
}
