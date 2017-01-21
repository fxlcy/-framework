package cn.fxlcy.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import cn.fxlcy.framework.R;
import cn.fxlcy.framework.util.DensityUtils;


/**
 * Created by xc on 2016/8/11.
 */
public class LoadingButton extends LinearLayout {
    private ProgressBar mPbLoading;
    private Button mButton;
    private int mTenDp;
    private boolean mIsLoading = false;
    private CharSequence mText;
    private CharSequence mLoadingText;

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTenDp = DensityUtils.dp2px(getContext(), 10);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        setClickable(true);
        initView(context, attrs, defStyleAttr);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context) {
        this(context, null);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mPbLoading = new ProgressBar(context);
        mPbLoading.setLayoutParams(new ViewGroup.LayoutParams(mTenDp * 2, mTenDp * 2));
        mPbLoading.setVisibility(GONE);
        mPbLoading.setIndeterminate(true);

        mButton = new Button(context);
        ViewCompat.setBackground(mButton, null);
        mButton.setPadding(mTenDp / 2, 0, mTenDp / 2, 0);
        mButton.setClickable(false);
        mButton.setGravity(Gravity.CENTER);

        addView(mPbLoading, 0);
        addView(mButton, 1);

        mButton.setMinWidth(0);
        mButton.setMinimumWidth(0);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton,
                R.attr.loadingButtonStyle, 0);
        Drawable drawable = ta.getDrawable(R.styleable.LoadingButton_loadingSrc);
        CharSequence text = ta.getText(R.styleable.LoadingButton_text);
        mLoadingText = ta.getText(R.styleable.LoadingButton_loadingText);
        int textColor = ta.getColor(R.styleable.LoadingButton_android_textColor, 0xFFFFFFFF);
        int size = ta.getDimensionPixelSize(R.styleable.LoadingButton_android_textSize, -1);
        int textSize = size == -1 ? 18 : DensityUtils.px2sp(getContext(), size);

        setEnabled(ta.getBoolean(R.styleable.LoadingButton_android_enabled, true));

        ta.recycle();

        if (drawable != null) {
            setLoadingDrawable(drawable);
        }
        setText(text);
        setTextColor(textColor);
        setTextSize(textSize);

    }

    public void setText(@StringRes int resId) {
        setText(getResources().getText(resId));
    }

    public void setText(CharSequence text) {
        mButton.setText(text);

        mText = text;
    }

    public void setLoadingText(CharSequence text) {
        mLoadingText = text;
        if (mIsLoading) {
            mButton.setText(text);
        }
    }

    public void setLoadingText(@StringRes int resId) {
        setLoadingText(getResources().getText(resId));
    }

    public void setTextColor(int color) {
        mButton.setTextColor(color);
    }

    public void setTextColorResource(@ColorRes int resId) {
        setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    public void setTextSize(float size) {
        mButton.setTextSize(size);
    }

    public void setTextSizeResource(@DimenRes int resId) {
        mButton.setTextSize(getResources().getDimensionPixelOffset(resId));
    }

    public void setLoadingDrawable(Drawable drawable) {
        mPbLoading.setIndeterminateDrawable(drawable);
    }

    public void setLoadingDrawableResource(@DrawableRes int resId) {
        setLoadingDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            return;
        }
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLoading) {
                    return;
                }
                l.onClick(view);
            }
        });
    }

    public void loading() {
        mIsLoading = true;
        mPbLoading.setVisibility(VISIBLE);
        mButton.setText(mLoadingText == null ? mText : mLoadingText);
        setEnabled(false);
    }

    public void loaded(CharSequence text) {
        mIsLoading = false;
        mPbLoading.setVisibility(GONE);
        mButton.setText(text);
        setEnabled(true);
    }

    public void loaded() {
        loaded(mText);
    }

    public boolean isLoading() {
        return mIsLoading;
    }

}
