package cn.fxlcy.framework.basis.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.List;

import cn.fxlcy.framework.R;
import cn.fxlcy.framework.util.Loading;
import cn.fxlcy.framework.util.LoadingState;
import cn.fxlcy.framework.widget.LoadingButton;

import static cn.fxlcy.framework.util.LoadingState.bottom;
import static cn.fxlcy.framework.util.LoadingState.error;
import static cn.fxlcy.framework.util.LoadingState.loading;
import static cn.fxlcy.framework.util.LoadingState.normal;

/**
 * Created by fxlcy on 2016/10/25.
 */

public abstract class BottomLoadingRecyclerViewAdapter<D> extends HeaderFooterRecyclerViewAdapter<D>
        implements Loading {
    private int mLoadingState = normal;
    private LoadingButton mLoadingButton;
    private Animation mDoBottomAnim;

    public BottomLoadingRecyclerViewAdapter(Context context) {
        super(context);
    }

    public BottomLoadingRecyclerViewAdapter(Context context, List<D> data) {
        super(context, data);
    }

    @Override
    public void setParent(ViewGroup view) {
        super.setParent(view);

        setFooterView(R.layout.layout_loading_footer);
        mLoadingButton = (LoadingButton) getFooterView();
        mLoadingButton.loading();

        setLoadingState(mLoadingState);
    }


    @Override
    public void setLoadingState(@LoadingState int state) {
        mLoadingState = state;

        if (mLoadingButton != null) {
            switch (mLoadingState) {
                case loading:
                    mLoadingButton.loading();
                    break;
                case normal:
                    mLoadingButton.loaded();
                    break;
                case error:
                    mLoadingButton.loaded(getContext().getString(R.string.loading_error));
                    break;
                case bottom:
                    if (mDoBottomAnim == null) {
                        mDoBottomAnim = new AlphaAnimation(1, 0);
                        mDoBottomAnim.setDuration(2000);
                        mDoBottomAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mLoadingButton.loaded(null);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }


                    mLoadingButton.loaded(getContext().getString(R.string.do_bottom));
                    mLoadingButton.startAnimation(mDoBottomAnim);
                    break;
            }
        }
    }

    @Override
    public
    @LoadingState
    int loadingState() {
        return mLoadingState;
    }

}
