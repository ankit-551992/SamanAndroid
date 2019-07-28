package com.qtech.saman.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;

public class InvoiceViewerActivity  extends BaseActivity {

    private WebView wv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv1=(WebView)findViewById(R.id.webView);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setLoadWithOverviewMode(true);
        wv1.getSettings().setUseWideViewPort(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.setWebViewClient(new WebViewClient());
        String pdf="https://www.saman.om/FileUploadsManager/invoice.pdf";
        wv1.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+pdf);
    }
}
