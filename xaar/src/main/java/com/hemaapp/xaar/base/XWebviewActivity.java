package com.hemaapp.xaar.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hemaapp.xaar.R;
import com.hemaapp.xaar.XApplication;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/11 17:45
 * 名称:XWebviewActivity
 * 注释:webview
 *******************************/
public class XWebviewActivity extends XActivity {
    private WebView mWebView;
    private LinearLayout mLinearLayout;
    private String url;
    private String title;


    public static void webviewActivityStart(Context context, String url, String title) {
        Intent intent = new Intent(context, XWebviewActivity.class);
        intent.putExtra("webview_url", url);
        intent.putExtra("webview_title", title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwebview);
        Intent intent = getIntent();
        url = intent.getStringExtra("webview_url");
        title = intent.getStringExtra("webview_title");
        mLinearLayout = (LinearLayout) findViewById(R.id.webview_root);
        mWebView = new WebView(getApplicationContext());
        mLinearLayout.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        mWebView.loadUrl("http://www.hemaapp.com");

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }


}
