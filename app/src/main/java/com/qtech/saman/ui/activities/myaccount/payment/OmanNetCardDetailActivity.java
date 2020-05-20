package com.qtech.saman.ui.activities.myaccount.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OmanNetCardDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.webView)
    WebView webView;

    String url = "";
//    String url="https://www.saman.om/api/Order/Invoice?";
//    String url="https://www.saman.om/api/Order/Invoice?orderID=305&CreditCardNumber=4017518900000300349&ExpiryMonth=03&ExpiryYear=21&CardHolderName=Ahmed&CVV=200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oman_net_card_details);
        url = getIntent().getStringExtra("PayURL");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.payment_method));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (url.equalsIgnoreCase("saman://backtoapp")) {
                Intent data = new Intent();
                data.putExtra("ID", -30);
                setResult(RESULT_OK, data);
                finish();
            }else if(url.contains("https://certepayments.omannet.om/PG/paymentcancel")){
                Intent data = new Intent();
                data.putExtra("ID", -40);
                setResult(RESULT_CANCELED, data);
                finish();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.e("onPageFinished", url);
            if (url.equalsIgnoreCase("saman://backtoapp")) {
                Intent data = new Intent();
                data.putExtra("ID", -30);
                setResult(RESULT_OK, data);
                finish();
            }else if(url.contains("https://certepayments.omannet.om/PG/paymentcancel")){
                Intent data = new Intent();
                data.putExtra("ID", -40);
                setResult(RESULT_CANCELED, data);
                finish();
            }
            super.onPageFinished(view, url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            Log.e("onPageStarted", url);
            if (url.equalsIgnoreCase("saman://backtoapp")) {
                Intent data = new Intent();
                data.putExtra("ID", -30);
                setResult(RESULT_OK, data);
                finish();
            }else if(url.contains("https://certepayments.omannet.om/PG/paymentcancel")){
                Intent data = new Intent();
                data.putExtra("ID", -40);
                setResult(RESULT_CANCELED, data);
                finish();
            }
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
