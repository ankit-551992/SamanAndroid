package com.algorepublic.saman.ui.activities.myaccount.payment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.ui.adapters.PaymentAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SamanApp;
import com.algorepublic.saman.utils.SwipeHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
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
    List<CardDs> cards = new ArrayList<>();
    PaymentAdapter paymentAdapter;
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
        cards = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(this,cards);
        mRecyclerView.setAdapter(paymentAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        cards.add(codCard());
        getCards();

        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(MyPaymentActivity.this,R.drawable.ic_delete),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                if (pos!=0) {
                                    deleteCard(pos);
                                }else {
                                    Constants.showAlert(getString(R.string.payment_method),getString(R.string.default_card_msg),getString(R.string.close),MyPaymentActivity.this);
                                }
                            }
                        }
                ));
            }
        };

    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
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

        Button add=(Button) dialog.findViewById(R.id.button_add_card);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyPaymentActivity.this,AddCardActivity.class);
                startActivityForResult(intent,1010);
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


    void getCards() {
        Object obj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
        }.getType());
        if (obj != null) {
            cards.clear();
            cards.add(codCard());
            cards.addAll((ArrayList<CardDs>) obj);
        }

        paymentAdapter.notifyDataSetChanged();
    }

    public void updateCards() {
        Object obj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
        }.getType());
        cards.clear();
        cards.add(codCard());
        cards.addAll((Collection<? extends CardDs>) obj);

        paymentAdapter.notifyDataSetChanged();
    }

    public void deleteCard(int postion) {
        cards.remove(postion);
        if (paymentAdapter != null) {
            paymentAdapter.notifyDataSetChanged();
        }
        SamanApp.db.putString(Constants.CARD_LIST, GlobalValues.convertListToString(cards));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010) {
            if (resultCode == RESULT_OK) {
                updateCards();
            }
        }
    }

    private CardDs codCard(){
        CardDs cardDs=new CardDs();
        cardDs.setCardNumber(getString(R.string.card_delivery));
        return cardDs;
    }
}
