package com.hemaapp.xaar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/******************************
 * 作者:邢佩凯
 * 日期:2016/5/27 11:08
 * 名称:XAllGridView
 * 注释:显示全部gridview,不收嵌套影响
 *******************************/
public class XAllGridView extends GridView {
    public XAllGridView(Context context) {
        super(context);
    }

    public XAllGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XAllGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
