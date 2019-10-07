package com.qtech.saman.ui.activities.password;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.apis.PhoneCodeResponse;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class NumberVerificationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_code)
    EditText codeEditText;

    String number = null;
    PhoneCodeResponse phoneCodeResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        number = getIntent().getStringExtra("Number");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.number_verification));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (number != null) {
            verifyNumber(number);
        }
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_verification)
    public void verificationButton() {
        String code = codeEditText.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Constants.showAlert(getString(R.string.number_verification), getString(R.string.verification_code_required), getString(R.string.okay), NumberVerificationActivity.this);
            return;
        } else {
            if (phoneCodeResponse != null) {
                if (phoneCodeResponse.getSuccess() == 1) {
                    if (code.equals(phoneCodeResponse.getResult())) {
                        Intent data = new Intent();
                        data.putExtra("ID", -30);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
            }
        }
    }

    private void verifyNumber(String number) {
        WebServicesHandler.instance.sendVerificationCode(number, new retrofit2.Callback<PhoneCodeResponse>() {
            @Override
            public void onResponse(Call<PhoneCodeResponse> call, Response<PhoneCodeResponse> response) {
                phoneCodeResponse = response.body();
                Log.e("RESPNSE", "--phoneCodeResponse--" + response.body());
                Log.e("RESPNSE", "--phoneCodeResponse-0000-" + phoneCodeResponse);
                if (phoneCodeResponse != null) {
                    if (phoneCodeResponse.getSuccess() == 1) {
                        Log.e("Code", phoneCodeResponse.getResult());
                        Toast.makeText(NumberVerificationActivity.this, phoneCodeResponse.getResult(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneCodeResponse> call, Throwable t) {
            }
        });
    }
}
