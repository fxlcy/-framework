package cn.fxlcy.framework.basis.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.fxlcy.framework.widget.HeaderFooterView;

/**
 * Created by fxlcy on 2016/10/25.
 */

public abstract class HeaderFooterRecyclerViewAdapter<D> extends CommonRecyclerViewAdapter<D>
        implements HeaderFooterView {
    private final static int VIEW_TYPE_FOOTER = -2;
    private final static int VIEW_TYPE_HEADER = -3;

    private HeaderFooterView mHF;


    public HeaderFooterRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public HeaderFooterRecyclerViewAdapter(Context context, List<D> data) {
        super(context, data);
        mHF = new RecyclerViewHeaderFooterViewImpl(context, this);
    }

    @Override
    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case VIEW_TYPE_FOOTER:
                view = mHF.getFooterView();
                break;
            case VIEW_TYPE_HEADER:
                view = mHF.getHeaderView();
                break;
            default:
                view = getConvertView(inflater, parent, viewType);
        }

        if (view != null) {
            return new ViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == 0 && hasHeaderView()) {
            return VIEW_TYPE_HEADER;
        } else if (position == getItemCount() - 1 && hasFooterView()) {
            return VIEW_TYPE_FOOTER;
        }

        return getNormalItemViewType(position);
    }

    public int getNormalItemViewType(int position) {
        return 0;
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                onBindHeaderViewHolder(holder);
                break;
            case VIEW_TYPE_FOOTER:
                onBindFooterViewHolder(holder);
                break;
            default:
                int ps = hasHeaderView() ? position - 1 : position;
                onBindNormalViewHolder(holder, ps);
                break;
        }
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    protected void onBindHeaderViewHolder(ViewHolder holder) {
    }

    protected void onBindFooterViewHolder(ViewHolder holder) {
    }

    @Override
    public BaseRecyclerViewAdapter addItem(D data) {
        return super.addItem(data);
    }

    protected abstract void onBindNormalViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (hasHeaderView()) {
            count += 1;
        }

        if (hasFooterView()) {
            count += 1;
        }

        return count;
    }

    @Override
    public void setHeaderView(@LayoutRes int layoutRes) {
        mHF.setHeaderView(layoutRes);
    }

    @Override
    public void setHeaderView(View view) {
        mHF.setHeaderView(view);
    }

    @Override
    public void setFooterView(@LayoutRes int layoutRes) {
        mHF.setFooterView(layoutRes);
    }

    @Override
    public void setFooterView(View view) {
        mHF.setFooterView(view);
    }

    @Override
    public View getHeaderView() {
        return mHF.getHeaderView();
    }

    @Override
    public View getFooterView() {
        return mHF.getFooterView();
    }

    @Override
    public View findHeaderViewById(@IdRes int viewId) {
        return mHF.findHeaderViewById(viewId);
    }

    @Override
    public <V extends View> View findHeaderViewById(@IdRes int viewId, Class<V> viewType) {
        return mHF.findHeaderViewById(viewId, viewType);
    }

    @Override
    public View findFooterViewById(@IdRes int viewId) {
        return mHF.findFooterViewById(viewId);
    }

    @Override
    public <V extends View> View findFooterViewById(@IdRes int viewId, Class<V> viewType) {
        return mHF.findFooterViewById(viewId, viewType);
    }

    @Override
    public int getDataOffset() {
        return hasHeaderView() ? 1 : 0;
    }

    @Override
    public void setParent(ViewGroup view) {
        mHF.setParent(view);
    }

    private boolean hasFooterView() {
        return mHF.getFooterView() != null;
    }

    private boolean hasHeaderView() {
        return mHF.getHeaderView() != null;
    }

    private static class RecyclerViewHeaderFooterViewImpl implements HeaderFooterView {
        View mHeaderView, mFooterView;
        SparseArray<View> mHeaderViews;
        SparseArray<View> mFooterViews;
        ViewGroup mParent;
        Context mContext;
        LayoutInflater mInflater;
        RecyclerView.Adapter mAdapter;

        RecyclerViewHeaderFooterViewImpl(@NonNull Context context, RecyclerView.Adapter adapter) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mAdapter = adapter;
        }

        @Override
        public void setParent(ViewGroup viewGroup) {
            mParent = viewGroup;
        }

        @Override
        public void setHeaderView(@LayoutRes int layoutRes) {
            setHeaderView(mInflater.inflate(layoutRes, mParent, false));
        }

        @Override
        public void setHeaderView(View view) {
            mHeaderView = view;
            notifyDataSetChanged();
        }

        @Override
        public void setFooterView(@LayoutRes int layoutRes) {
            setFooterView(mInflater.inflate(layoutRes, mParent, false));
        }

        @Override
        public void setFooterView(View view) {
            mFooterView = view;
            notifyDataSetChanged();
        }

        @Override
        public View getHeaderView() {
            return mHeaderView;
        }

        @Override
        public View getFooterView() {
            return mFooterView;
        }

        @Override
        public View findHeaderViewById(@IdRes int viewId) {
            return findViewById(mHeaderView, viewId);
        }

        @Override
        public <V extends View> View findHeaderViewById(@IdRes int viewId, Class<V> viewType) {
            View view = findHeaderViewById(viewId);
            return view == null ? null : viewType.cast(view);
        }

        @Override
        public View findFooterViewById(@IdRes int viewId) {
            return findViewById(mFooterView, viewId);
        }

        @Override
        public <V extends View> View findFooterViewById(@IdRes int viewId, Class<V> viewType) {
            View view = findFooterViewById(viewId);
            return view == null ? null : viewType.cast(view);
        }

        private View findViewById(View view, @IdRes int id) {
            if (view == null) {
                return null;
            }

            View v;

            if (view.equals(mHeaderView)) {
                if (mHeaderViews != null && mHeaderViews.indexOfKey(id) != -1) {
                    v = mHeaderViews.get(id);
                } else {
                    v = mHeaderView.findViewById(id);
                    if (v != null) {
                        if (mHeaderViews == null) {
                            mHeaderViews = new SparseArray<>();
                        }

                        mHeaderViews.append(id, v);
                    }
                }
            } else {
                if (mFooterViews != null && mFooterViews.indexOfKey(id) != -1) {
                    v = mFooterViews.get(id);
                } else {
                    v = mFooterView.findViewById(id);
                    if (v != null) {
                        if (mFooterViews == null) {
                            mFooterViews = new SparseArray<>();
                        }

                        mFooterViews.append(id, v);
                    }
                }
            }

            return v;
        }

        private void notifyDataSetChanged() {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
