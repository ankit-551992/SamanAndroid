package com.algorepublic.saman.ui.activities.myaccount.addresses;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddShippingAddressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_address)
    EditText address;
    @BindView(R.id.setDefault_checkBox)
    CheckBox setDefaultCheckBox;
    @BindView(R.id.button_add)
    Button addButton;

    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        type=getIntent().getIntExtra("Type",0);

        if(type==0){
            toolbarTitle.setText(getString(R.string.add_shipping_address));
        }else {
            toolbarTitle.setText(getString(R.string.edit_shipping_address));
            address.setText(getIntent().getStringExtra("Address"));
            addButton.setText(getString(R.string.update));
        }

        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_add)
    public void addAddress() {
        int v = setDefaultCheckBox.isChecked() ? 1 : 0;
    }

}
