package com.hemaapp.xaar.base;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAppDelegate<P extends Presenter> implements ViewDelegate<P> {

    protected View mRootView;
    private SparseArray<View> mViews = new SparseArray<>();
    protected P mPresenter;


    @Override
    public void onCreate(LayoutInflater inflater, ViewGroup contanier, Bundle savedInstanceState, P presenter) {
        int rootId = getRootId();
        mRootView = inflater.inflate(rootId, contanier, false);
        this.mPresenter = presenter;
    }

    protected abstract int getRootId();


    @Override
    public View getRootView() {
        return mRootView;
    }

    public <T extends View> T getView(int resId) {
        T view = (T) mViews.get(resId);
        if (view == null) {
            view = (T) mRootView.findViewById(resId);
            mViews.put(resId, view);
        }
        return view;
    }

    //设置点击事件
    protected void setOnClickListener(View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            getView(id).setOnClickListener(listener);
        }
    }
}
