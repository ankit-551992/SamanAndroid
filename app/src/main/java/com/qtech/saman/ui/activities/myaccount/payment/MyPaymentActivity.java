package com.qtech.saman.ui.activities.myaccount.payment;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.CardDs;
import com.qtech.saman.ui.adapters.PaymentAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.ResourceUtil;
import com.qtech.saman.utils.SamanApp;
import com.qtech.saman.utils.SwipeHelper;
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
    boolean isOmanNetSelected = false;
    boolean isMasterCardSelected = false;

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
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setImageDrawable(getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        } else {
            settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        }

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        cards = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(this, cards);
        mRecyclerView.setAdapter(paymentAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        cards.add(codCard());
        cards.add(omanNetCard());
        getCards();

        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(MyPaymentActivity.this, R.drawable.ic_delete_ic),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                if (pos != 0 && pos != 1) {
                                    deleteCard(pos);
                                } else {
                                    Constants.showAlert(getString(R.string.payment_method), getString(R.string.default_card_msg), getString(R.string.close), MyPaymentActivity.this);
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
        Intent intent = new Intent(MyPaymentActivity.this, AddCardActivity.class);
        intent.putExtra("Type",0);
        startActivityForResult(intent, 1010);
    }

    void addPayments() {


        dialog = new Dialog(MyPaymentActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_payment_method);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);
        final LinearLayout layoutCard = (LinearLayout) dialog.findViewById(R.id.layout_card);
        final LinearLayout layoutCash = (LinearLayout) dialog.findViewById(R.id.layout_cash);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button add = (Button) dialog.findViewById(R.id.button_add_card);

        layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOmanNetSelected = false;
                layoutCash.setBackground(getResources().getDrawable(R.drawable.favorites_background));
                if (!isMasterCardSelected) {
                    isMasterCardSelected = true;
                    layoutCard.setBackground(getResources().getDrawable(R.drawable.selected_bg));
                } else {
                    isMasterCardSelected = false;
                    layoutCard.setBackground(getResources().getDrawable(R.drawable.favorites_background));
                }
            }
        });

        layoutCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMasterCardSelected = false;
                layoutCard.setBackground(getResources().getDrawable(R.drawable.favorites_background));
                if (!isOmanNetSelected) {
                    isOmanNetSelected = true;
                    layoutCash.setBackground(getResources().getDrawable(R.drawable.selected_bg));
                } else {
                    isOmanNetSelected = false;
                    layoutCash.setBackground(getResources().getDrawable(R.drawable.favorites_background));
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMasterCardSelected) {
                    Intent intent = new Intent(MyPaymentActivity.this, AddCardActivity.class);
                    intent.putExtra("Type",0);
                    startActivityForResult(intent, 1010);
                    dialog.dismiss();
                    isMasterCardSelected = false;
                } else if (isOmanNetSelected) {
//                    Intent intent = new Intent(MyPaymentActivity.this, AddCardActivity.class);
//                    intent.putExtra("Type",1);
//                    startActivityForResult(intent, 1010);
                    Intent data = new Intent();
                    String text = "OMANNET";
                    data.putExtra("DATA",text);
                    setResult(RESULT_OK, data);
                    finish();
                    dialog.dismiss();
                    isOmanNetSelected = false;
                } else {
                    Constants.showAlert(getString(R.string.payment_method), getString(R.string.select_method), getString(R.string.okay), MyPaymentActivity.this);
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(MyPaymentActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }


    void getCards() {
        Object obj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
        }.getType());
        if (obj != null) {
            cards.clear();
            cards.add(codCard());
            cards.add(omanNetCard());
            cards.addAll((ArrayList<CardDs>) obj);
        }
        paymentAdapter.notifyDataSetChanged();
    }

    public void updateCards() {
        Object obj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
        }.getType());
        cards.clear();
        cards.add(codCard());
        cards.add(omanNetCard());
        cards.addAll((Collection<? extends CardDs>) obj);

        paymentAdapter.notifyDataSetChanged();
    }

    public void deleteCard(int postion) {
        cards.remove(postion);
        if (paymentAdapter != null) {
            paymentAdapter.notifyDataSetChanged();
        }
        if (cards.size() != 1) {
            SamanApp.db.putString(Constants.CARD_LIST, GlobalValues.convertListToString(cards));
        } else {
            SamanApp.db.putString(Constants.CARD_LIST, "");
        }
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

    private CardDs codCard() {
        CardDs cardDs = new CardDs();
        cardDs.setCardNumber(getString(R.string.card_delivery));
        return cardDs;
    }

    private CardDs omanNetCard() {
        CardDs cardDs = new CardDs();
        cardDs.setCardNumber(getString(R.string.omannet));
        return cardDs;
    }

    private void payment(){

    }
}
