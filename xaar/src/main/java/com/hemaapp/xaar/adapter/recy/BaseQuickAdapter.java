package com.hemaapp.xaar.adapter.recy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hemaapp.xaar.R;
import com.hemaapp.xaar.view.recyclerview.BaseRecyclerViewAdapter;
import com.hemaapp.xaar.view.recyclerview.BaseViewHolder;
import com.hemaapp.xaar.view.recyclerview.OnItemClickLitener;
import com.hemaapp.xaar.view.recyclerview.OnItemLongClickLitener;

import java.util.List;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/10 9:52
 * 名称:BaseQuickAdapter
 * 注释:recyclerAdapter
 *******************************/
public abstract class BaseQuickAdapter<T> extends BaseRecyclerViewAdapter {
    private int resId;//布局文件
    private OnItemClickLitener itemClickLitener;
    private OnItemLongClickLitener itemLongClickLitener;


    public BaseQuickAdapter(Context mContext, List data, int resId) {
        super(mContext, data);
        this.resId = resId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(mContext).inflate(resId, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        convert((BaseViewHolder) holder, (T) mDatas.get(position));
        holder.itemView.setTag(R.id.tag_holder, position + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickLitener != null) {
                    itemClickLitener.onItemClick(holder.itemView, Integer.parseInt(holder.itemView.getTag(R.id.tag_holder).toString()));
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemLongClickLitener != null) {
                    itemLongClickLitener.onItemLongClick(holder.itemView, Integer.parseInt(holder.itemView.getTag(R.id.tag_holder).toString()));
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    protected abstract void convert(BaseViewHolder helper, T item);


    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.itemClickLitener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickLitener listener) {
        this.itemLongClickLitener = listener;
    }

}
