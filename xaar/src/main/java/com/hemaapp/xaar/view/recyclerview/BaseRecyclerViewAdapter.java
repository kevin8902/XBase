package com.hemaapp.xaar.view.recyclerview;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.hemaapp.xaar.view.recyclerview.animation.AlphaInAnimation;
import com.hemaapp.xaar.view.recyclerview.animation.BaseAnimation;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected List<T> mDatas;
    protected Context mContext;
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private int mLastPosition = -1;
    private boolean mFirstOnlyEnable = true;
    private boolean mOpenAnimationEnable = false;
    private DataChangeListener mDataChangeListener;
    private int initNum = 0;

    public BaseRecyclerViewAdapter(Context mContext, List<T> data) {
        this.mContext = mContext;
        this.mDatas = data;
        this.initNum = data.size();
    }


    public int getInitNum() {
        return initNum;
    }

    //重设数据
    public void resetData(List<T> data) {
        if (mDatas == null)
            mDatas = new ArrayList<>();
        else
            mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
        if (mDataChangeListener != null) {
            mDataChangeListener.addDataListener(data.size());
        }
    }

    //删除数据
    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    //添加数据
    public void add(int position, T item) {
        mDatas.add(position, item);
        notifyItemInserted(position);
    }

    //添加数据
    public void add(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
        if (mDataChangeListener != null) {
            mDataChangeListener.addDataListener(data.size());
        }
    }

    public void setOnDataChangeListener(DataChangeListener mDataChangeListener) {
        this.mDataChangeListener = mDataChangeListener;
    }


    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }


    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    protected void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }


    /**
     * 子控件的点击事件
     */
    private OnRecyclerViewItemChildClickListener mChildClickListener;


    public void setOnRecyclerViewItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public interface OnRecyclerViewItemChildClickListener {
        void onItemChildClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

    public class OnItemChildClickListener implements View.OnClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public void onClick(View v) {
            if (mChildClickListener != null)
                mChildClickListener.onItemChildClick(BaseRecyclerViewAdapter.this, v, mViewHolder.getLayoutPosition());
        }
    }
}
