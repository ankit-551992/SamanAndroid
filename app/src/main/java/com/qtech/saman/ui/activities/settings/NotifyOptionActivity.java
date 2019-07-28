package com.qtech.saman.ui.activities.settings;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.utils.GlobalValues;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifyOptionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.switch_order)
    SwitchCompat switch_order;
    @BindView(R.id.switch_msg)
    SwitchCompat switch_msg;
    @BindView(R.id.switch_promo_sales)
    SwitchCompat switch_promo_sales;
    @BindView(R.id.switch_feedback)
    SwitchCompat switch_feedback;
    @BindView(R.id.switch_itemback)
    SwitchCompat switch_itemback;
    Context mcontext;

    String order_notify = "ORDER_NOTIFY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_option);

        ButterKnife.bind(this);
        mcontext = NotifyOptionActivity.this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.notifications));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        //switch_order.setChecked(GlobalValues.getNotificationOnOff(this));

        switch_order.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GlobalValues.setNotificationtypeOnOff(mcontext, order_notify, true);
                } else {
                    GlobalValues.setNotificationtypeOnOff(mcontext, order_notify, false);
                }
            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }
}
