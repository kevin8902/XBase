package com.hemaapp.xaar.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.xaar.R;


/*************************
 * 作者:邢佩凯
 * 时间:16/5/28 下午7:46
 * 文件名:XProgressDialog
 * 注释:自定义progressdialog
 *************************/
public class XProgressDialog {
    private Dialog mDialog;
    private TextView mTextView;

    public XProgressDialog(Context context, String text) {
        mDialog = new Dialog(context, R.style.myDialog);
        View view = View.inflate(context, R.layout.layout_progress_dialog, null);
        mTextView = (TextView) view.findViewById(R.id.base_progress_tv);
        if (TextUtils.isEmpty(text)) {
            mTextView.setText("请稍后...");
        }
        mTextView.setText(text);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            mTextView.setText("请稍后");
        } else {
            mTextView.setText(text);
        }

    }

    public void setText(int resId) {
        mTextView.setText(resId);
    }

    public void show() {
        mTextView.removeCallbacks(cancelRunnable);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void cancel() {
        if (mDialog.isShowing()) {
            mTextView.postDelayed(cancelRunnable, 500);
        }
    }


    public void cancelImmediately() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    private Runnable cancelRunnable = new Runnable() {

        @Override
        public void run() {
            if (mDialog.isShowing())
                mDialog.cancel();
        }
    };

}
