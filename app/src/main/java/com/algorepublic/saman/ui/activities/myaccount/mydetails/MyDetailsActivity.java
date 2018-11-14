package com.algorepublic.saman.ui.activities.myaccount.mydetails;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.country.CountriesActivity;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDetailsActivity extends BaseActivity implements DetailContractor.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_firstName)
    EditText firstNameEditText;
    @BindView(R.id.editText_lastName)
    EditText lastNameEditText;
    @BindView(R.id.editText_email)
    EditText emailEditText;
    @BindView(R.id.tv_gender)
    TextView genderText;
    @BindView(R.id.tv_country_name)
    TextView countryName;
    @BindView(R.id.editText_address)
    EditText addressEditText;
    @BindView(R.id.editText_day)
    EditText dayEditText;
    @BindView(R.id.editText_month)
    EditText monthEditText;
    @BindView(R.id.editText_year)
    EditText yearEditText;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Calendar myCalendar;
    Country selectedCountry;

    User authenticatedUser;
    MyDetailsPresenter presenter;
    boolean isRequest=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        ButterKnife.bind(this);
        presenter=new MyDetailsPresenter(this);
        setSupportActionBar(toolbar);
        isRequest=getIntent().getBooleanExtra("Request",false);
        authenticatedUser = GlobalValues.getUser(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.my_details));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        myCalendar= Calendar.getInstance();

        for (int i=0;i<GlobalValues.countries.size();i++){
            if(GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(MyDetailsActivity.this))){
                selectedCountry=GlobalValues.countries.get(i);
                countryName.setText(selectedCountry.getName());
            }
        }
        setProfile();
    }


    private void setProfile(){
        firstNameEditText.setText(authenticatedUser.getFirstName());
        lastNameEditText.setText(authenticatedUser.getLastName());
        emailEditText.setText(authenticatedUser.getEmail());
        emailEditText.setEnabled(false);
        genderText.setText(authenticatedUser.getGender());
        selectedGender=authenticatedUser.getGender();
        countryName.setText(authenticatedUser.getCountry());
        addressEditText.setText(authenticatedUser.getShippingAddress().getAddressLine1());
    }

    @OnClick(R.id.layout_GenderSelection)
    public void selectGenderClick() {
        selectGender();
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent=new Intent(MyDetailsActivity.this,CountriesActivity.class);
        startActivityForResult(intent,1299);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/YYYY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    }

    @OnClick(R.id.button_update)
    public void registerButton() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String gender = selectedGender;
        String country = countryName.getText().toString();
        String address = addressEditText.getText().toString();
        if (isDataValid(firstName, lastName, gender, address)) {
            presenter.updateUser(authenticatedUser.getId(),firstName,lastName,gender,country,address);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                countryName.setText(returnedResult);
            }
        }
    }


    Dialog dialog;
    String selectedGender = "";

    private void selectGender() {
        dialog = new Dialog(MyDetailsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gender_selection);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView done = (TextView) dialog.findViewById(R.id.tv_done);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                if(radioButton.isChecked()) {
                    selectedGender = radioButton.getText().toString();
                    genderText.setText(radioButton.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(MyDetailsActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }



    private boolean isDataValid(String fName, String lName, String gender, String address) {
        if (TextUtils.isEmpty(fName)) {
            firstNameEditText.setError(getString(R.string.first_name_required));
            return false;
        } else if (TextUtils.isEmpty(lName)) {
            lastNameEditText.setError(getString(R.string.last_name_required));
            return false;
        } else if (TextUtils.isEmpty(gender)) {
            genderText.setError(getString(R.string.gender_prompt));
            return false;
        }
        else if (TextUtils.isEmpty(address)) {
            addressEditText.setError(getString(R.string.address_req));
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateResponse(boolean success) {
        if(success){
            authenticatedUser.setFirstName(firstNameEditText.getText().toString());
            authenticatedUser.setLastName(lastNameEditText.getText().toString());
            authenticatedUser.setGender(genderText.getText().toString());
            authenticatedUser.setCountry(countryName.getText().toString());
            authenticatedUser.getShippingAddress().setAddressLine1(addressEditText.getText().toString());

            GlobalValues.saveUser(MyDetailsActivity.this, authenticatedUser);

            if(isRequest){
                setResult(RESULT_OK);
                finish();
            }else {
                Constants.showAlert(getString(R.string.update_profile),getString(R.string.update_profile_success),getString(R.string.okay),MyDetailsActivity.this);
            }
        }else {

            if(isRequest){
                Constants.showAlertWithActivityFinish(getString(R.string.update_profile),getString(R.string.update_profile_fail),getString(R.string.try_again),MyDetailsActivity.this);
            }else {
                Constants.showAlert(getString(R.string.update_profile),getString(R.string.update_profile_fail),getString(R.string.try_again),MyDetailsActivity.this);
            }
        }
    }
}
