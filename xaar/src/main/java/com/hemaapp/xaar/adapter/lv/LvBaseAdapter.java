package com.hemaapp.xaar.adapter.lv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class LvBaseAdapter<T> extends BaseAdapter implements LvDataChangeListener<T> {

    private ArrayList<T> mDatas;
    private Context mContext;


    public LvBaseAdapter(Context mContext, ArrayList<T> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        if (getMax() != 0 && mDatas.size() > getMax())
            return getMax();
        return mDatas.size();

    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LvBaseViewHolder holder = LvBaseViewHolder.getViewHolder(mContext, view, viewGroup, itemViewId(), i);
        setData(holder, mDatas.get(i), i);
        return holder.getConvertView();
    }


    private OnLvItemChildClickListener mChildClickListener;


    public void setOnLvItemChildClickListener(OnLvItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public interface OnLvItemChildClickListener {
        void onItemChildClick(LvBaseAdapter adapter, View view, int position);
    }


    public class OnItemChildClickListener implements View.OnClickListener {
        public LvBaseViewHolder mViewHolder;

        @Override
        public void onClick(View v) {
            if (mChildClickListener != null)
                mChildClickListener.onItemChildClick(LvBaseAdapter.this, v, mViewHolder.getmPosition());
        }
    }

    @Override
    public void refresh(ArrayList<T> datas) {
        if (datas == null && mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
            return;
        }
        if (mDatas == null) {
            mDatas = datas;
            notifyDataSetChanged();
        } else {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }


    @Override
    public void addAll(ArrayList<T> datas) {
        if (datas == null)
            return;
        if (mDatas == null) {
            mDatas = datas;
            notifyDataSetChanged();
        } else {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }


    @Override
    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public void remove(int position) {
        if (mDatas != null && position < getCount()) {
            mDatas.remove(position);
            notifyDataSetChanged();
        }
    }


    @Override
    public void add(T item) {
        if (item == null) {
            return;
        }

        if (mDatas == null) {
            mDatas = new ArrayList<>();
            mDatas.add(item);
            notifyDataSetChanged();
        }
    }

    //获取显示最多条数
    public abstract int getMax();

    //获取布局资源id
    public abstract int itemViewId();

    //设置数值
    public abstract void setData(LvBaseViewHolder holder, T object, int position);
}
