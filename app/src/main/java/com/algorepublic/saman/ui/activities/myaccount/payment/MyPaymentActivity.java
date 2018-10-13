package com.algorepublic.saman.ui.activities.myaccount.payment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.ui.activities.search.SearchActivity;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.ui.adapters.PaymentAdapter;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPaymentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_settings)
    ImageView settings;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Payment> paymentList = new ArrayList<>();
    PaymentAdapter paymentAdapter;
    Bitmap icon;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.payment_method));
        toolbarBack.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setImageDrawable(getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        }else {
            settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        }

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        paymentList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(this,paymentList);
        mRecyclerView.setAdapter(paymentAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        getExistingPaymentMethods();

        icon = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_button_bg);
        SwipeHelper swipeHelper = new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        icon,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));
            }
        };


    }


    @OnClick(R.id.toolbar_settings)
    void addPayment(){

        dialog = new Dialog(MyPaymentActivity.this,R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_payment_method);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Animation animation;
        animation = AnimationUtils.loadAnimation(MyPaymentActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup)dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();

    }


    private void getExistingPaymentMethods(){
        for (int i = 0; i < 3; i++) {
            Payment payment = new Payment();
            paymentList.add(payment);
            paymentAdapter.notifyDataSetChanged();
        }
    }
}
