package com.algorepublic.saman.ui.activities.myaccount.payment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.algorepublic.saman.utils.GlobalValues.convertListToString;

public class AddCardActivity extends BaseActivity{


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;

    @BindView(R.id.cardNumberEt)
    EditText cardNumberEt;
    @BindView(R.id.editText_month)
    EditText month;
    @BindView(R.id.editText_year)
    EditText year;
    @BindView(R.id.cardHolderEt)
    EditText cardHolderEt;
    @BindView(R.id.cvcEt)
    EditText cvcEt;
    @BindView(R.id.et_otp)
    EditText otpEt;
    @BindView(R.id.addBt)
    Button addBt;

    ArrayList<CardDs> cardlist = new ArrayList<>();
    CardDs obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ButterKnife.bind(this);
        obj=(CardDs)getIntent().getSerializableExtra("Obj");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Card");
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        init();

    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    void init() {
        if (obj != null) {
            cardNumberEt.setEnabled(false);
        } else {
            cardNumberEt.setEnabled(true);
        }


        if (obj != null) {
            updateData(obj);
        }
    }

    @OnClick(R.id.addBt)
    public void addPayment(){
        if (validate()) {
            String expire=month.getText().toString()+"/"+year.getText().toString();
            CardDs cardDs = new CardDs(cardHolderEt.getText().toString(), cardNumberEt.getText().toString(),expire, cvcEt.getText().toString(),otpEt.getText().toString(),Integer.parseInt(month.getText().toString()), Integer.parseInt(year.getText().toString()));
            Object odj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
            }.getType());

            if (obj == null) {

                if (odj == null) {
                    cardlist.add(cardDs);
                } else {
                    cardlist.addAll((ArrayList<CardDs>) odj);
                    cardlist.add(cardDs);
                }

            } else {
                cardlist.addAll((ArrayList<CardDs>) odj);
                for (int i = 0; i < cardlist.size(); i++) {
                    if (cardlist.get(i).getCardNumber().equals(cardDs.getCardNumber())) {
                        cardlist.set(i, cardDs);
                    }
                }
            }
            SamanApp.db.putString(Constants.CARD_LIST, convertListToString(cardlist));
            setResult(RESULT_OK);
            finish();
        }
    }

    void updateData(CardDs obj) {
        cardHolderEt.setText(obj.getCardHolder());
        cardNumberEt.setText(obj.getCardNumber());
        cvcEt.setText(obj.getCvc());
        otpEt.setText(obj.getOtp());
        month.setText(String.valueOf(obj.getMonth()));
        year.setText(String.valueOf(obj.getYear()));
        addBt.setText("Update Card");
    }

    public boolean validate() {
        boolean valid = true;



        if (cardNumberEt.getText().toString().isEmpty()) {
            cardNumberEt.setError("Please enter card number!");
            valid = false;
        }

        if (cardNumberEt.getText().toString().length() < 16) {
            cardNumberEt.setError("Please enter valid card number!");
            valid = false;
        }

        if (month.getText().toString().isEmpty()) {
            month.setError("Please add card expire month!");
            valid = false;
        }

        if (year.getText().toString().isEmpty()) {
            year.setError("Please add card expire year!");
            valid = false;
        }

        if (cardHolderEt.getText().toString().isEmpty()) {
            cardHolderEt.setError("Please enter card holder name!");
            valid = false;
        }

        if (cvcEt.getText().toString().isEmpty()) {
            cvcEt.setError("Please enter CVC!");
            valid = false;
        }

        if (otpEt.getText().toString().isEmpty()) {
            otpEt.setError("Please enter OTP!");
            valid = false;
        }



        return valid;
    }
}
