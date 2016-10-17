package com.hemaapp.xaar.adapter.lv;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/31 10:07
 * 名称:LvBaseViewHolder
 * 注释:listview的viewholder
 *******************************/
public class LvBaseViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private int mPosition;

    public LvBaseViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static LvBaseViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            LvBaseViewHolder holder = new LvBaseViewHolder(context, parent, layoutId, position);
            return holder;
        } else {
            LvBaseViewHolder holder = (LvBaseViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public LvBaseViewHolder setOnClickListener(int viewId, LvBaseAdapter.OnItemChildClickListener listener) {
        View view = getView(viewId);
        listener.mViewHolder = this;
        view.setOnClickListener(listener);
        return this;
    }


    public int getmPosition() {
        return mPosition;
    }

    //通过viewId获取view
    public <T extends View> T getView(int resId) {
        View view = mViews.get(resId);
        if (view == null) {
            view = mConvertView.findViewById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }


    public View getConvertView() {
        return mConvertView;
    }


}
