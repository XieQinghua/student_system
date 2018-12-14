package com.stu.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mlayoutId;

    private int itemHeight = -1;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this(context,datas,layoutId,-1);
    }

    public CommonAdapter(Context context, List<T> datas, int layoutId, int itemHeight) {
        this.mContext = context;
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        this.itemHeight = itemHeight;
        mInflater = LayoutInflater.from(context);
    }

    public CommonAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.mlayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mDatas != null) {
            return position;
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mlayoutId, position);
        convert(holder, getItem(position), position);
        View v = holder.getConvertView();
        if (itemHeight != -1){
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.height = itemHeight;
            v.setLayoutParams(layoutParams);
        }
        return v;
    }

    public abstract void convert(ViewHolder holder, T t, int position);
}
