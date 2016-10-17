package com.hemaapp.xaar.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;



/******************************
 * 作者:邢佩凯
 * 日期:2016/10/9 14:29
 * 名称:BasePresenterActivity
 * 注释:实现p的基类
 *******************************/
public abstract class BasePresenterActivity<T extends ViewDelegate, M extends BaseModel> extends XActivity implements Presenter, View.OnClickListener {
    protected T mViewDelegate;
    protected M mBaseModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mViewDelegate = getDelegateClass().newInstance();
            mBaseModel = getModelClass().newInstance();
            mViewDelegate.onCreate(getLayoutInflater(), null, savedInstanceState, this);//this是代表接口presenter
            mBaseModel.Create(mContext, this);//this代表接口presenter
            setContentView(mViewDelegate.getRootView());
            mViewDelegate.initWidget(getIntent());
            mViewDelegate.initEvent(this);//this代表view.onclicklistener
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewDelegate = null;
        mBaseModel = null;
    }

    //该方法为界面创建完毕之后要实现的方法
    protected abstract void init();


    protected abstract Class<T> getDelegateClass();

    protected abstract Class<M> getModelClass();


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
    public void finishActivity() {
        this.finish();
    }
}
