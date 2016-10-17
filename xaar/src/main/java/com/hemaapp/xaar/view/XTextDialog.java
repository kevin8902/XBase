package com.hemaapp.xaar.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.xaar.R;


/*************************
 * 作者:邢佩凯
 * 时间:16/5/28 下午8:11
 * 文件名:XTextDialog
 * 注释:显示提示信息
 *************************/
public class XTextDialog {

    private Dialog mDialog;
    private TextView mTextView;

    public XTextDialog(Context context, String text) {
        mDialog = new Dialog(context, R.style.myDialog);
        View view = View.inflate(context, R.layout.layout_text_dialog, null);
        mTextView = (TextView) view.findViewById(R.id.base_textdialog_tv);
        mTextView.setText(text);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
    }


    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setText(int resId) {
        mTextView.setText(resId);
    }

    public void show() {
        mDialog.show();
        mTextView.postDelayed(cancelRunnable, 1500);
    }


    public void cancel() {
        mDialog.dismiss();
    }

    private Runnable cancelRunnable = new Runnable() {

        @Override
        public void run() {
            cancel();
        }
    };

}
