package com.hemaapp.xaar.view.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.xaar.R;


/*************************
 * 作者:邢佩凯
 * 时间:16/5/30 下午4:36
 * 文件名:XRefreshLayout
 * 注释:下拉刷新和上拉加载
 *************************/
public class XRefreshLoadLayout extends XBaseRefreshLayout {

    private boolean isLoading;
    private boolean isRefreshing;

    public XRefreshLoadLayout(Context context) {
        this(context, null);
    }

    public XRefreshLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRefreshLoadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setRefreshView(R.layout.refresh_normal, new RefeshListener());
        setLoadmoreView(R.layout.loadmore_normal, new LoadmoreListener());
        setAnimationDuration(300);
    }


    private class RefeshListener implements RefreshViewListener {
        private final int ROTATE_ANIM_DURATION = 180;
        private Animation mRotateUpAnim;
        private Animation mRotateDownAnim;

        private ImageView arrowView;
        private TextView textView;
        private ProgressBar progressBar;

        private boolean pull_min1 = true;
        private boolean pull_max1 = false;

        private RefeshListener() {
            mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateUpAnim.setFillAfter(true);
            mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateDownAnim.setFillAfter(true);
        }

        @Override
        public void onPulling(View refreshView, float percent) {
            findView(refreshView);
            if (percent < 1) {
                if (pull_max1) {
                    textView.setText("下拉刷新");
                    arrowView.startAnimation(mRotateDownAnim);
                }
                pull_min1 = true;
                pull_max1 = false;
            } else {
                if (pull_min1) {
                    textView.setText("松开刷新");
                    arrowView.startAnimation(mRotateUpAnim);
                }
                pull_min1 = false;
                pull_max1 = true;
            }
        }

        @Override
        public void onReset(View refreshView) {
            findView(refreshView);
            arrowView.clearAnimation();
            arrowView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("下拉刷新");

        }

        @Override
        public void onRefresh(View refreshView) {
            findView(refreshView);
            arrowView.clearAnimation();
            arrowView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textView.setText("正在刷新");
            isRefreshing = true;
        }

        @Override
        public void onSuccess(View refreshView) {
            findView(refreshView);
            arrowView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            textView.setText("刷新成功");
            isRefreshing = false;
        }

        @Override
        public void onFailed(View refreshView) {
            findView(refreshView);
            arrowView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            textView.setText("刷新失败");
            isRefreshing = false;
        }

        private void findView(View fartherView) {
            if (arrowView == null || textView == null || progressBar == null) {
                arrowView = (ImageView) fartherView
                        .findViewById(R.id.refresh_arrow);
                textView = (TextView) fartherView
                        .findViewById(R.id.refresh_textview);
                progressBar = (ProgressBar) fartherView
                        .findViewById(R.id.refresh_progressbar);
            }
        }

    }

    private class LoadmoreListener implements LoadmoreViewListener {
        private TextView textView;
        private ProgressBar progressBar;

        @Override
        public void onPulling(View loadmoreView, float percent) {
            findView(loadmoreView);
            if (percent < 1) {
                textView.setText("上拉加载");
            } else {
                textView.setText("松开加载");
            }

        }

        @Override
        public void onReset(View loadmoreView) {
            findView(loadmoreView);
            progressBar.setVisibility(View.GONE);
            textView.setText("上拉加载");

        }

        @Override
        public void onLoadmore(View loadmoreView) {
            findView(loadmoreView);
            progressBar.setVisibility(View.VISIBLE);
            textView.setText("正在加载");
            isLoading = true;
        }

        @Override
        public void onSuccess(View loadmoreView) {
            findView(loadmoreView);
            progressBar.setVisibility(View.GONE);
            textView.setText("加载成功");
            isLoading = false;
        }

        @Override
        public void onFailed(View loadmoreView) {
            findView(loadmoreView);
            progressBar.setVisibility(View.GONE);
            textView.setText("加载失败");
            isLoading = false;
        }

        private void findView(View fartherView) {
            if (textView == null || progressBar == null) {
                textView = (TextView) fartherView
                        .findViewById(R.id.loadmore_textview);
                progressBar = (ProgressBar) fartherView
                        .findViewById(R.id.loadmore_progressbar);
            }
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }
}
