package cn.fxlcy.framework.widget;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fxlcy on 2016/8/10.
 */
public interface IViewGroup {
    void addView(View view);

    void removeView(View view);

    ViewGroup getParent();

    int indexOfChild(View view);
}
