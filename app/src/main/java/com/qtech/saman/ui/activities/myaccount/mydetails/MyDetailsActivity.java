package com.qtech.saman.ui.activities.myaccount.mydetails;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.User;
import com.qtech.saman.ui.activities.country.CountriesListingActivity;
import com.qtech.saman.ui.activities.country.RegionListingActivity;
import com.qtech.saman.ui.activities.myaccount.addresses.ShippingAddressActivity;
import com.qtech.saman.utils.CircleTransform;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDetailsActivity extends BaseActivity implements DetailContractor.View {

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
    @BindView(R.id.ccp)
    EditText ccp;
    @BindView(R.id.editText_phone)
    EditText phoneEditText;
    @BindView(R.id.tv_gender)
    TextView genderText;
    @BindView(R.id.tv_country_name)
    TextView countryName;
    @BindView(R.id.editText_address)
    EditText addressEditText;
    @BindView(R.id.iv_country_flag)
    ImageView countryFlag;
    @BindView(R.id.iv_code_flag)
    ImageView iv_code_flag;
    @BindView(R.id.editText_day)
    EditText dayEditText;
    @BindView(R.id.editText_month)
    EditText monthEditText;
    @BindView(R.id.editText_year)
    EditText yearEditText;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @BindView(R.id.tv_region_name)
    TextView regionName;
    @BindView(R.id.layout_regionSelection)
    LinearLayout regionSelectionLinearLayout;
    @BindView(R.id.view_region)
    View regionView;

    Calendar myCalendar;
    Country selectedCountry;

    int addressID = 0;
    User authenticatedUser;
    MyDetailsPresenter presenter;
    boolean isRequest = false;
    long selectedDateInMillis = 0;

    boolean showAlert = false;
    String codeflag;
    Dialog dialog;
    String selectedGender = "";
    String setaddress = "";
    String AddressLine1, floor, apt, city, usercountry, userregion, landmark;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        ButterKnife.bind(this);
        presenter = new MyDetailsPresenter(this);
        setSupportActionBar(toolbar);
        isRequest = getIntent().getBooleanExtra("Request", false);
        authenticatedUser = GlobalValues.getUser(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.my_details));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        myCalendar = Calendar.getInstance();

        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                firstNameEditText.setHint("firstName");
            }
        });
        setProfile();
        showAlert = getIntent().getBooleanExtra("ShowAlert", false);
        if (showAlert) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.profile_alert), getString(R.string.okay), this);
        }
    }

    private void setProfile() {

        authenticatedUser = GlobalValues.getUser(this);
        Log.e("PROFILEDETAIL", "--authenticatedUser--" + authenticatedUser);
        firstNameEditText.setText(authenticatedUser.getFirstName());
        lastNameEditText.setText(authenticatedUser.getLastName());
        emailEditText.setText(authenticatedUser.getEmail());
        emailEditText.setEnabled(false);

        if (authenticatedUser.getPhoneNumber() != null) {
            String phoneNumber = authenticatedUser.getPhoneNumber();

            String[] number = phoneNumber.split("-");
            if (number.length > 1) {
                ccp.setText(phoneNumber.split("-")[0]);
                phoneEditText.setText(phoneNumber.split("-")[1]);
            } else {
                phoneEditText.setText(phoneNumber);
            }
//           ccp.setEnabled(false);
//           phoneEditText.setEnabled(false);
        }
        codeflag = GlobalValues.getSelectedCodeFlag(MyDetailsActivity.this);
        Log.e("FLAG000", "--codeflag--" + codeflag);
        if (!codeflag.equals("") && !TextUtils.isEmpty(codeflag)) {
            iv_code_flag.setVisibility(View.VISIBLE);
            Picasso.get().load(codeflag).transform(new CircleTransform()).into(iv_code_flag);
        } else {
            iv_code_flag.setVisibility(View.GONE);
        }

//      genderText.setText(authenticatedUser.getGender());
        selectedGender = authenticatedUser.getGender();

        if (SamanApp.isEnglishVersion) {
            Log.e("FLAG000", "--isEnglishVersion--getGender-" + selectedGender);
            if (selectedGender != null && selectedGender.equals(getResources().getString(R.string.female))) {
                genderText.setText(getResources().getString(R.string.female));
            } else {
                genderText.setText(getResources().getString(R.string.male));
            }
//          genderText.setText(selectedGender);
        } else {
            Log.e("FLAG000", "--else---getGender--" + selectedGender);
            if (selectedGender != null && selectedGender.equals(getResources().getString(R.string.female))) {
                genderText.setText(getResources().getString(R.string.female));
            } else {
                genderText.setText(getResources().getString(R.string.male));
            }
        }
        Log.e("FLAG000", "--getCountry--" + authenticatedUser.getCountry());
        if (authenticatedUser.getCountry() != null && !authenticatedUser.getCountry().isEmpty()) {
            if (GlobalValues.countries != null) {
                for (int i = 0; i < GlobalValues.countries.size(); i++) {
                    if (GlobalValues.countries.get(i).getName().equals(authenticatedUser.getCountry())) {
                        if (SamanApp.isEnglishVersion) {
                            countryName.setText(GlobalValues.countries.get(i).getName());
                        } else {
                            countryName.setText(GlobalValues.countries.get(i).getName_AR());
                        }
                        GlobalValues.setSelectedCountry(this, GlobalValues.countries.get(i).getSortname());
                    }

                    Picasso.get().load(GlobalValues.countries.get(i).getFlag()).transform(new CircleTransform()).into(countryFlag);
                }
            }
            if (authenticatedUser.getCountry().equalsIgnoreCase("oman")) {
                regionView.setVisibility(View.VISIBLE);
                regionSelectionLinearLayout.setVisibility(View.VISIBLE);
                regionName.setText(authenticatedUser.getRegion());
                GlobalValues.setSelectedRegion(this, authenticatedUser.getRegion());
            } else {
                regionSelectionLinearLayout.setVisibility(View.GONE);
                regionView.setVisibility(View.GONE);
            }
        }

        Log.e("FLAG000", "--getShippingAddress--" + authenticatedUser.getShippingAddress());
        if (authenticatedUser.getShippingAddress() != null) {
            setShippingAddress(authenticatedUser.getShippingAddress());
        }

        long datetimestamp = Long.parseLong(authenticatedUser.getDateOfBirth().replaceAll("\\D", ""));
        if (datetimestamp < Calendar.getInstance().getTimeInMillis()) {
            Date date = new Date(datetimestamp);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String dateFormatted = formatter.format(date);
            String[] sepDate = dateFormatted.split("/");
            if (!isAfterToday(Integer.parseInt(sepDate[0]), Integer.parseInt(sepDate[1]), Integer.parseInt(sepDate[2]))) {
                dayEditText.setText(sepDate[0]);
                monthEditText.setText(sepDate[1]);
                yearEditText.setText(sepDate[2]);
            }
        }
    }

    @OnClick(R.id.layout_GenderSelection)
    public void selectGenderClick() {
        selectGender();
    }

    @OnClick({R.id.button_change_address, R.id.editText_address})
    public void ChangeAddress() {
        Intent intent = new Intent(MyDetailsActivity.this, ShippingAddressActivity.class);
        startActivityForResult(intent, 1414);
    }

    @OnClick(R.id.iv_pin)
    public void userAddress() {
//        try {
//            Intent intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                            .build(this);
//            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        } catch (GooglePlayServicesRepairableException e) {
//            // TODO: Handle the error.
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
        Intent intent = new Intent(MyDetailsActivity.this, ShippingAddressActivity.class);
        startActivityForResult(intent, 1414);
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(MyDetailsActivity.this, CountriesListingActivity.class);
        startActivityForResult(intent, 1299);
    }

    @OnClick(R.id.layout_regionSelection)
    public void regionSelection() {
        Intent intent = new Intent(MyDetailsActivity.this, RegionListingActivity.class);
        startActivityForResult(intent, 1309);
    }

    @OnClick(R.id.ccp)
    public void phoneCode() {
        Intent intent = new Intent(MyDetailsActivity.this, CountriesListingActivity.class);
        intent.putExtra("GetCode", true);
        startActivityForResult(intent, 2021);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick({R.id.editText_day, R.id.editText_month, R.id.editText_year})
    void setDOB() {
//        DatePickerDialog dialog = new DatePickerDialog(MyDetailsActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                0);

        DatePickerDialog dialog = new DatePickerDialog(MyDetailsActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (!isAfterToday(year, monthOfYear, dayOfMonth)) {
                selectedDateInMillis = myCalendar.getTimeInMillis();
                updateLabel();
            } else {
                Constants.showAlert(getString(R.string.my_details), getString(R.string.invalid_date), getString(R.string.okay), MyDetailsActivity.this);
            }
        }
    };

    public boolean isAfterToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();
        myDate.set(year, month, day);
        return myDate.after(today);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String dateSelected = sdf.format(myCalendar.getTime());
        String sepDate[] = dateSelected.split("/");
        dayEditText.setText(sepDate[0]);
        monthEditText.setText(sepDate[1]);
        yearEditText.setText(sepDate[2]);
    }
//    int choose_date, mon, yr;
//    String day, month, year;
//    String dob;

    @OnClick(R.id.button_update)
    public void registerButton() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String gender = selectedGender;
        String country = countryName.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = ccp.getText().toString() + phoneEditText.getText().toString();
        String region = regionName.getText().toString();

        String day = dayEditText.getText().toString();
        String month = monthEditText.getText().toString();
        String year = yearEditText.getText().toString();
        String dob = month + "-" + day + "-" + year;

        if (isDataValid(firstName, lastName, gender, address, day, month, year, phone, dob)) {
            phone = ccp.getText().toString() + "-" + phoneEditText.getText().toString();

            JSONObject jsonObject = new JSONObject();
            try {
                if (addressID == 0) {
                    jsonObject.put("ID", authenticatedUser.getShippingAddress().getiD());
                } else {
                    jsonObject.put("ID", addressID);
                }
                jsonObject.put("AddressLine1", AddressLine1);
                jsonObject.put("Floor", floor);
                jsonObject.put("Apt", apt);
                jsonObject.put("AddressLine2", landmark);
                jsonObject.put("City", city);
                jsonObject.put("UserCountry", usercountry);
                jsonObject.put("UserRegion", userregion);
                jsonObject.put("Latitude", latitude);
                jsonObject.put("Longitude", longitude);
                jsonObject.put("isDefault", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (countryName.getText().toString().equalsIgnoreCase("oman")) {
            if (countryName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.oman))) {
                if (region == null || TextUtils.isEmpty(region)) {
                    Constants.showAlert(getString(R.string.my_details), getString(R.string.region_required), getString(R.string.okay), MyDetailsActivity.this);
                    return;
                }
            } else {
                region = "";
            }
            presenter.updateUser(authenticatedUser.getId(), firstName, lastName, gender, country, jsonObject, dob, phone, region);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();

                if (returnedResult != null && !returnedResult.isEmpty()) {
                    if (GlobalValues.countries != null) {
                        for (int i = 0; i < GlobalValues.countries.size(); i++) {
                            if (GlobalValues.countries.get(i).getName().equalsIgnoreCase(returnedResult)
                                    || GlobalValues.countries.get(i).getName_AR().equalsIgnoreCase(returnedResult)) {
                                if (SamanApp.isEnglishVersion) {
                                    countryName.setText(GlobalValues.countries.get(i).getName());
                                } else {
                                    countryName.setText(GlobalValues.countries.get(i).getName_AR());
                                }
                                Picasso.get().load(GlobalValues.countries.get(i).getFlag()).transform(new CircleTransform()).into(countryFlag);
                            }
                        }
                    }
                }
                if (returnedResult.equalsIgnoreCase("oman")) {
                    GlobalValues.setSelectedRegion(this,"");
                    regionView.setVisibility(View.VISIBLE);
                    regionSelectionLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    regionView.setVisibility(View.GONE);
                    regionSelectionLinearLayout.setVisibility(View.GONE);
                }
            }
        } else if (requestCode == 1309) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                regionName.setText(returnedResult);
            }
        } else if (requestCode == 1414) {
            if (resultCode == RESULT_OK) {
//              String d = data.getExtras().getString("DATA");
                addressID = data.getExtras().getInt("ID");
//              addressEditText.setText(d);
                ShippingAddress shippingAddress = (ShippingAddress) data.getExtras().getSerializable("DATA");
                Log.e("SHIPPINGADD00", "----shipping--add--" + new Gson().toJson(shippingAddress));

                if (shippingAddress != null) {
                    setShippingAddress(shippingAddress);
                }
            }
        } else if (requestCode == 2021) {
            if (resultCode == RESULT_OK) {
                String code = data.getExtras().getString("Code");
                codeflag = data.getExtras().getString("Flag");
                if (!codeflag.equals("")) {
                    iv_code_flag.setVisibility(View.VISIBLE);
                    Picasso.get().load(codeflag).transform(new CircleTransform()).into(iv_code_flag);
                }
                ccp.setText(code);
            }
        }
    }

    private void setShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress.getAddressLine1() != null) {
            setaddress = shippingAddress.getAddressLine1();
            AddressLine1 = shippingAddress.getAddressLine1();
        }
        if (shippingAddress.getFloor() != null) {
            setaddress = setaddress + ", " + shippingAddress.getFloor();
            floor = shippingAddress.getFloor();
        }
        if (shippingAddress.getApt() != null) {
            setaddress = setaddress + ", " + shippingAddress.getApt();
            apt = shippingAddress.getApt();
        }
        if (shippingAddress.getAddressLine2() != null) {
            setaddress = setaddress + ", " + shippingAddress.getAddressLine2();
            landmark = shippingAddress.getAddressLine2();
        }
        if (shippingAddress.getCity() != null) {
            setaddress = setaddress + ", " + shippingAddress.getCity();
            city = shippingAddress.getCity();
        }
        if (shippingAddress.getCountry() != null) {
            setaddress = setaddress + ", " + shippingAddress.getCountry();
            usercountry = shippingAddress.getCountry();
        }
        if (shippingAddress.getRegion() != null) {
            setaddress = setaddress + ", " + shippingAddress.getRegion();
            userregion = shippingAddress.getRegion();
        }
        if (shippingAddress.getLatitude() != null && shippingAddress.getLongitude() != null) {
            latitude = shippingAddress.getLatitude();
            longitude = shippingAddress.getLongitude();
        }
        addressEditText.setText(setaddress);
    }

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

                if (radioButton.isChecked()) {
                    selectedGender = radioButton.getText().toString();
                    genderText.setText(radioButton.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(MyDetailsActivity.this, R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    private boolean isDataValid(String fName, String lName, String gender, String address, String day, String month, String year, String phone, String dob) {
        if (TextUtils.isEmpty(fName)) {
            //firstNameEditText.setError(getString(R.string.first_name_required));
            Constants.showAlert(getString(R.string.my_details), getString(R.string.first_name_required), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (TextUtils.isEmpty(lName)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.last_name_required), getString(R.string.okay), MyDetailsActivity.this);
            //lastNameEditText.setError(getString(R.string.last_name_required));
            return false;
        } else if (TextUtils.isEmpty(gender)) {
            // genderText.setError(getString(R.string.gender_prompt));
            Constants.showAlert(getString(R.string.my_details), getString(R.string.gender_prompt), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.Phone_Number_Required), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (phone.length() < 10) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.Not_Valid), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (TextUtils.isEmpty(day)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.day_missing), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (TextUtils.isEmpty(month)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.month_missing), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (TextUtils.isEmpty(year)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.year_missing), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else if (TextUtils.isEmpty(address)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.address_req), getString(R.string.okay), MyDetailsActivity.this);
            // addressEditText.setError(getString(R.string.address_req));
            return false;
        } else if (TextUtils.isEmpty(dob)) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.dob_missing), getString(R.string.okay), MyDetailsActivity.this);
            return false;
        } else {
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
    public void updateResponse(boolean success, User user) {
        if (success) {
            GlobalValues.saveUser(MyDetailsActivity.this, user);
            setProfile();
//          ((DashboardActivity)activity).updateUserDetails();

            GlobalValues.setSelectedCodeFlag(MyDetailsActivity.this, codeflag);
            if (isRequest) {
                setResult(RESULT_OK);
                finish();
            } else {
                Constants.showAlertWithActivityFinish("", getString(R.string.update_profile_success), getString(R.string.okay), MyDetailsActivity.this);
            }
        } else {
            if (isRequest) {
                Constants.showAlertWithActivityFinish("", getString(R.string.update_profile_fail), getString(R.string.try_again), MyDetailsActivity.this);
            } else {
                Constants.showAlert("", getString(R.string.update_profile_fail), getString(R.string.try_again), MyDetailsActivity.this);
            }
        }
    }

    @Override
    public void updateResponseFail(boolean success) {
        if (isRequest) {
            Constants.showAlertWithActivityFinish("", getString(R.string.update_profile_fail), getString(R.string.try_again), MyDetailsActivity.this);
        } else {
            Constants.showAlert("", getString(R.string.update_profile_fail), getString(R.string.try_again), MyDetailsActivity.this);
        }

    }
}
