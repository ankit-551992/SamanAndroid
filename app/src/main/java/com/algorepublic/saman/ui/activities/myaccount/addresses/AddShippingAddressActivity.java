package com.algorepublic.saman.ui.activities.myaccount.addresses;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.ShippingAddress;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.AddAddressApi;
import com.algorepublic.saman.data.model.apis.GetAddressApi;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class AddShippingAddressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.editText_address)
    EditText address;
    @BindView(R.id.setDefault_checkBox)
    CheckBox setDefaultCheckBox;
    @BindView(R.id.button_add)
    Button addButton;


    User authenticatedUser;

    int type;
    ShippingAddress shippingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        authenticatedUser=GlobalValues.getUser(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        type=getIntent().getIntExtra("Type",0);

        if(type==0){
            toolbarTitle.setText(getString(R.string.add_shipping_address));
        }else {
            toolbarTitle.setText(getString(R.string.edit_shipping_address));
            shippingAddress=(ShippingAddress)getIntent().getSerializableExtra("ShippingAddress");
            address.setText(shippingAddress.getAddressLine1());
            setDefaultCheckBox.setChecked(shippingAddress.isDefault());
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
        progressBar.setVisibility(View.VISIBLE);
        boolean isChecked = setDefaultCheckBox.isChecked();

        if(type==0) {
            WebServicesHandler.instance.addAddress(authenticatedUser.getId(), address.getText().toString(), isChecked, new retrofit2.Callback<AddAddressApi>() {
                @Override
                public void onResponse(Call<AddAddressApi> call, Response<AddAddressApi> response) {
                    progressBar.setVisibility(View.GONE);
                    AddAddressApi addAddressApi = response.body();
                    if (addAddressApi != null) {
                        if (addAddressApi.getSuccess() == 1) {
                            Constants.showAlertWithActivityFinish(getString(R.string.shipping_address), getString(R.string.address_added), getString(R.string.okay), AddShippingAddressActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddAddressApi> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            WebServicesHandler.instance.updateAddress(shippingAddress.getID(), address.getText().toString(), isChecked, new retrofit2.Callback<SimpleSuccess>() {
                @Override
                public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                    progressBar.setVisibility(View.GONE);
                    SimpleSuccess simpleSuccess = response.body();
                    if (simpleSuccess != null) {
                        if (simpleSuccess.getSuccess() == 1) {
                            Constants.showAlertWithActivityFinish(getString(R.string.shipping_address), getString(R.string.address_edit), getString(R.string.okay), AddShippingAddressActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

}
