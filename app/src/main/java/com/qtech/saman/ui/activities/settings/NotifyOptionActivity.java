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

import static com.qtech.saman.utils.GlobalValues.Itemback_notify;
import static com.qtech.saman.utils.GlobalValues.feedback_notify;
import static com.qtech.saman.utils.GlobalValues.msg_notify;
import static com.qtech.saman.utils.GlobalValues.order_notify;
import static com.qtech.saman.utils.GlobalValues.promo_sales_notify;

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

        switch_order.setChecked(GlobalValues.getTypesNotificationOnOff(mcontext, order_notify));
        switch_msg.setChecked(GlobalValues.getTypesNotificationOnOff(mcontext, msg_notify));
        switch_promo_sales.setChecked(GlobalValues.getTypesNotificationOnOff(mcontext, promo_sales_notify));
        switch_feedback.setChecked(GlobalValues.getTypesNotificationOnOff(mcontext, feedback_notify));
        switch_itemback.setChecked(GlobalValues.getTypesNotificationOnOff(mcontext, Itemback_notify));

        onClickTypeOfSwitch();
        //switch_order.setChecked(GlobalValues.getNotificationOnOff(this));
    }

    private void onClickTypeOfSwitch() {

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
        switch_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GlobalValues.setNotificationtypeOnOff(mcontext, msg_notify, true);
                } else {
                    GlobalValues.setNotificationtypeOnOff(mcontext, msg_notify, false);
                }
            }
        });

        switch_promo_sales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GlobalValues.setNotificationtypeOnOff(mcontext, promo_sales_notify, true);
                } else {
                    GlobalValues.setNotificationtypeOnOff(mcontext, promo_sales_notify, false);
                }
            }
        });

        switch_feedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GlobalValues.setNotificationtypeOnOff(mcontext, feedback_notify, true);
                } else {
                    GlobalValues.setNotificationtypeOnOff(mcontext, feedback_notify, false);
                }
            }
        });

        switch_itemback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GlobalValues.setNotificationtypeOnOff(mcontext, Itemback_notify, true);
                } else {
                    GlobalValues.setNotificationtypeOnOff(mcontext, Itemback_notify, false);
                }
            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }
}
