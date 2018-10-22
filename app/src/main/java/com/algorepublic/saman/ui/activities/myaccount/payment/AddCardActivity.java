package com.algorepublic.saman.ui.activities.myaccount.payment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import static com.algorepublic.saman.utils.GlobalValues.convertListToString;

public class AddCardActivity extends BaseActivity{

    EditText cardHolderEt, cardNumberEt, cvcEt;
    TextView expireTv;
    Button addBt;
    ArrayList<CardDs> cardlist = new ArrayList<>();
    CardDs obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        init();
    }

    void init() {
        addBt = findViewById(R.id.addBt);
        cardHolderEt = findViewById(R.id.cardHolderEt);
        cardNumberEt =findViewById(R.id.cardNumberEt);
        cvcEt = findViewById(R.id.cvcEt);
        expireTv = findViewById(R.id.expireTv);
        if (obj != null) {
            cardNumberEt.setEnabled(false);
        } else {
            cardNumberEt.setEnabled(true);
        }

        cardNumberEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    cvcEt.requestFocus();
                    return true;
                }
                return false;
            }
        });


        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    CardDs cardDs = new CardDs(cardHolderEt.getText().toString(), cardNumberEt.getText().toString(), expireTv.getText().toString(), cvcEt.getText().toString(), 10, 2021);
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
        });


        expireTv.setText("10/2021");

        if (obj != null) {
            updateData(obj);
        }
    }

    void updateData(CardDs obj) {
        cardHolderEt.setText(obj.getCardHolder());
        cardNumberEt.setText(obj.getCardNumber());
        cvcEt.setText(obj.getCvc());
        expireTv.setText(obj.getExpireDate());
    }

    public boolean validate() {
        boolean valid = true;

        if (cardHolderEt.getText().toString().isEmpty()) {
            cardHolderEt.setError("Please enter card holder name!");
            valid = false;
        }

        if (cardNumberEt.getText().toString().isEmpty()) {
            cardNumberEt.setError("Please enter card number!");
            valid = false;
        }

        if (cardNumberEt.getText().toString().length() < 16) {
            cardNumberEt.setError("Please enter valid card number!");
            valid = false;
        }

        if (cvcEt.getText().toString().isEmpty()) {
            cardNumberEt.setError("Please enter CVC!");
            valid = false;
        }

        if (expireTv.getText().toString().isEmpty()) {
            cardNumberEt.setError("Please add card expire date!");
            valid = false;
        }

        return valid;
    }
}
