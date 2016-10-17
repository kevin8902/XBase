package com.hemaapp.xaar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.xaar.R;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/13 14:58
 * 名称:SpannableTextView
 * 注释:textview中可以为其中文字设为其他颜色并带有下划线 并且添加点击事件
 *******************************/
public class SpannableTextView extends TextView {
    private String spannable_text;
    private int spannable_color;
    private boolean spannable_underline;


    public SpannableTextView(Context context) {
        this(context, null);
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpannableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpannableTextView);
        spannable_text = a.getString(R.styleable.SpannableTextView_spannable_text);
        spannable_color = a.getColor(R.styleable.SpannableTextView_spannable_cokor, Color.parseColor("#0000ff"));
        spannable_underline = a.getBoolean(R.styleable.SpannableTextView_spannable_underline, false);
        a.recycle();
        if (!TextUtils.isEmpty(getText().toString().trim()) && !TextUtils.isEmpty(spannable_text))
            setSpananble();
    }

    private void setSpananble() {
        String[] values;
        if (spannable_text.contains(","))
            values = spannable_text.split(",");
        else
            values = new String[]{spannable_text};
        SpannableString mSpannableString = new SpannableString(getText().toString().trim());
        for (int i = 0; i < values.length; i++) {
            final int pos = i;
            int start = getText().toString().trim().indexOf(values[i]);
            int end = start + values[i].length();
            mSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSpannableClick(pos);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(spannable_color);
                    ds.setUnderlineText(spannable_underline);
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setHighlightColor(Color.TRANSPARENT);//设置高亮背景色为透明,否则点击后有背景色
            setText(mSpannableString);
            setMovementMethod(LinkMovementMethod.getInstance());

        }
    }


    public void setSpannable_text(String text) {
        this.spannable_text = text;
    }

    public void setSpannable_color(int color) {
        this.spannable_color = color;
    }


    public void setSpannable_underline(boolean underline) {
        this.spannable_underline = underline;
    }


    public void setMainText(String text) {
        if (TextUtils.isEmpty(text))
            return;
        if (TextUtils.isEmpty(spannable_text))
            setText(text);
        else
            setSpananble();
    }

    public interface SpannableClickListener {
        void onSpannableClick(int position);
    }


    private SpannableClickListener listener;

    public void setOnSpannableClickListener(SpannableClickListener listener) {
        this.listener = listener;
    }
}
