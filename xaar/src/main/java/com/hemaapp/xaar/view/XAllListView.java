package com.hemaapp.xaar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/******************************
 * 作者:邢佩凯
 * 日期:2016/5/27 11:07
 * 名称:XAllListView
 * 注释:listview全部显示,不是嵌套影响
 *******************************/
public class XAllListView extends ListView {
    public XAllListView(Context context) {
        super(context);
    }

    public XAllListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XAllListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
