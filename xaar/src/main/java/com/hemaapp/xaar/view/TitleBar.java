package com.hemaapp.xaar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.xaar.R;


public class TitleBar extends FrameLayout {
    private RelativeLayout titleView;
    private TextView title;
    private FrameLayout left_fr;
    private TextView left_tv;
    private ImageView left_im;
    private FrameLayout right_fr;
    private TextView right_tv;
    private ImageView right_im;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        titleView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_title, null);
        //设置标题内容
        title = (TextView) titleView.findViewById(R.id.activity_title_center_tv);
        //设置左边
        left_fr = (FrameLayout) titleView.findViewById(R.id.activity_title_lf);
        left_tv = (TextView) titleView.findViewById(R.id.activity_title_lf_tv);
        left_im = (ImageView) titleView.findViewById(R.id.activity_title_lf_im);
        //设置右边
        right_fr = (FrameLayout) titleView.findViewById(R.id.activity_title_rg);
        right_tv = (TextView) titleView.findViewById(R.id.activity_title_rg_tv);
        right_im = (ImageView) titleView.findViewById(R.id.activity_title_rg_im);
        addView(titleView);
    }


    public void showLeftIm(int imageResource) {
        left_fr.setVisibility(VISIBLE);
        left_im.setVisibility(VISIBLE);
        left_tv.setVisibility(GONE);
        left_im.setImageResource(imageResource);
    }


    public void showLeftTv(String text) {
        left_fr.setVisibility(VISIBLE);
        left_im.setVisibility(GONE);
        left_tv.setVisibility(VISIBLE);
        left_tv.setText(text);
    }


    public void hideLeft() {
        left_fr.setVisibility(GONE);
    }


    public void showRightIm(int imageResource) {
        right_fr.setVisibility(VISIBLE);
        right_im.setVisibility(VISIBLE);
        right_tv.setVisibility(GONE);
        right_im.setImageResource(imageResource);
    }


    public void showRightTv(String text) {
        right_fr.setVisibility(VISIBLE);
        right_im.setVisibility(GONE);
        right_tv.setVisibility(VISIBLE);
        right_tv.setText(text);
    }

    public void hideRight() {
        right_fr.setVisibility(GONE);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setOnLeftListener(OnClickListener listener) {
        left_fr.setOnClickListener(listener);
    }


    public void setOnRightListener(OnClickListener listener) {
        right_fr.setOnClickListener(listener);
    }


}
