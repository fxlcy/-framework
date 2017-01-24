package cn.fxlcy.framework.basis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.fxlcy.framework.util.ItemList;

/**
 * Created by fxlcy on 2016/8/17.
 */
public abstract class BaseRecyclerViewAdapter<D, VH extends ViewHolder> extends RecyclerView.Adapter<VH> implements ItemList<D, BaseRecyclerViewAdapter> {
    private Context mContext;
    private List<D> mData;
    private LayoutInflater mInflater;

    public BaseRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public BaseRecyclerViewAdapter(Context context, List<D> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setData(data);
    }

    public Context getContext() {
        return mContext;
    }

    public void setData(Collection<D> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (data instanceof List) {
            mData = (List<D>) data;
        } else {
            mData = new ArrayList<>(data);
        }

        notifyDataSetChanged();
        onDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public D getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public BaseRecyclerViewAdapter addItem(D data) {
        if (mData.add(data)) {
            this.notifyItemInserted(mData.indexOf(data) + getDataOffset());
            onDataSetChanged();
        }
        return this;
    }

    @Override
    public BaseRecyclerViewAdapter addItem(int position, D item) {
        mData.add(position, item);
        this.notifyItemInserted(position + getDataOffset());
        onDataSetChanged();
        return this;
    }

    @Override
    public BaseRecyclerViewAdapter addItemAll(Collection<D> coll) {
        int start = mData.size();
        mData.addAll(coll);
        this.notifyItemRangeInserted(start + getDataOffset(), coll.size());
        onDataSetChanged();
        return this;
    }

    @Override
    public BaseRecyclerViewAdapter addItemAll(int position, Collection<D> coll) {
        mData.addAll(position, coll);
        this.notifyItemRangeInserted(position + getDataOffset(), coll.size());
        onDataSetChanged();
        return this;
    }

    @Override
    public D removeItem(int position) {
        D d = mData.remove(position);
        this.notifyItemRemoved(position + getDataOffset());
        onDataSetChanged();
        return d;
    }

    @Override
    public boolean removeItem(D item) {
        int index = mData.indexOf(item);
        if (mData.remove(item)) {
            this.notifyItemRemoved(index + getDataOffset());
            onDataSetChanged();
            return true;
        }
        return false;
    }

    public List<D> getData() {
        return mData;
    }

    protected void onDataSetChanged() {
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(mInflater, parent, viewType);
    }

    public abstract VH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    /**
     * 数据和View的偏移量
     */
    public int getDataOffset() {
        return 0;
    }
}
