package cn.fxlcy.framework.widget.recyclerview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xc on 2016/9/20.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mOrientation;

    public final static int HORIZONTAL = RecyclerView.HORIZONTAL;
    public final static int VERTICAL = RecyclerView.VERTICAL;

    public SpacesItemDecoration(int space) {
        this(space,VERTICAL);
    }

    public SpacesItemDecoration(int space,int orientation) {
        this.mSpace = space;
        this.mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildLayoutPosition(view) != 0) {
            if (mOrientation == VERTICAL) {
                outRect.top = mSpace;
            } else {
                outRect.left = mSpace;
            }
        }
    }

    protected int getSpace(){
        return mSpace;
    }

    protected int getOrientation(){
        return mOrientation;
    }
}