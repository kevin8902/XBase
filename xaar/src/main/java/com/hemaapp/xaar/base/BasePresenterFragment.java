package com.hemaapp.xaar.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BasePresenterFragment<T extends ViewDelegate, M extends BaseModel> extends Fragment implements Presenter, View.OnClickListener {
    protected T mViewDelegate;
    protected Context mContext;
    protected M mBaseModel;

    //该方法为界面创建完毕之后要实现的方法
    protected abstract void init();


    protected abstract Class<T> getDelegateClass();

    protected abstract Class<M> getModelClass();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mViewDelegate = getDelegateClass().newInstance();
            mBaseModel = getModelClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDelegate.onCreate(inflater, container, savedInstanceState, this);
        return mViewDelegate.getRootView();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseModel.Create(mContext, this);
        mViewDelegate.initWidget(null);
        mViewDelegate.initEvent(this);
        init();
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mViewDelegate == null) {
            try {
                mViewDelegate = getDelegateClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mBaseModel == null) {
            try {
                mBaseModel = getModelClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewDelegate = null;
        mBaseModel = null;
    }
}
