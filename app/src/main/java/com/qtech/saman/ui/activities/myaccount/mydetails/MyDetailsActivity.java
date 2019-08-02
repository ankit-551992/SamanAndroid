package com.qtech.saman.ui.activities.myaccount.mydetails;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

        if (GlobalValues.countries != null) {
            for (int i = 0; i < GlobalValues.countries.size(); i++) {
                if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(MyDetailsActivity.this))) {
                    selectedCountry = GlobalValues.countries.get(i);
                    countryName.setText(selectedCountry.getName());
                    Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                    if (selectedCountry.getName().equalsIgnoreCase("oman")) {
                        regionView.setVisibility(View.VISIBLE);
                        regionSelectionLinearLayout.setVisibility(View.VISIBLE);
                        if (authenticatedUser.getCountry() != null && !authenticatedUser.getCountry().isEmpty()) {
                            regionName.setText(authenticatedUser.getRegion());
                        }
                    } else {
                        regionSelectionLinearLayout.setVisibility(View.GONE);
                        regionView.setVisibility(View.GONE);
                    }
                }
            }
        }
        setProfile();

        showAlert = getIntent().getBooleanExtra("ShowAlert", false);
        if (showAlert) {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.profile_alert), getString(R.string.okay), this);
        }
    }


    private void setProfile() {
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
//            ccp.setEnabled(false);
//            phoneEditText.setEnabled(false);
        }

        genderText.setText(authenticatedUser.getGender());
        selectedGender = authenticatedUser.getGender();
//        countryName.setText(authenticatedUser.getCountry());
        if (authenticatedUser.getShippingAddress() != null) {

            if (authenticatedUser.getShippingAddress().getAddressLine1() != null) {

                if (authenticatedUser.getShippingAddress().getCity() != null) {

                    if (authenticatedUser.getShippingAddress().getCountry() != null) {
                        addressEditText.setText(authenticatedUser.getShippingAddress().getAddressLine1() + "," + authenticatedUser.getShippingAddress().getCity() + "," + authenticatedUser.getShippingAddress().getCountry());
                    } else {
                        addressEditText.setText(authenticatedUser.getShippingAddress().getAddressLine1() + "," + authenticatedUser.getShippingAddress().getCity());
                    }
                } else {
                    addressEditText.setText(authenticatedUser.getShippingAddress().getAddressLine1());
                }
            } else {
                if (authenticatedUser.getShippingAddress().getCity() != null) {

                    if (authenticatedUser.getShippingAddress().getCountry() != null) {
                        addressEditText.setText(authenticatedUser.getShippingAddress().getCity() + "," + authenticatedUser.getShippingAddress().getCountry());
                    } else {
                        addressEditText.setText(authenticatedUser.getShippingAddress().getCity());
                    }
                } else if (authenticatedUser.getShippingAddress().getCountry() != null) {
                    addressEditText.setText(authenticatedUser.getShippingAddress().getCountry());
                }
            }
        }
        Long datetimestamp = Long.parseLong(authenticatedUser.getDateOfBirth().replaceAll("\\D", ""));
        if (datetimestamp < Calendar.getInstance().getTimeInMillis()) {
            Date date = new Date(datetimestamp);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String dateFormatted = formatter.format(date);
            String sepDate[] = dateFormatted.split("/");
            if (!isAfterToday(Integer.valueOf(sepDate[0]), Integer.valueOf(sepDate[1]), Integer.valueOf(sepDate[2]))) {
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
//        dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
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

     /*   if (!dayEditText.getText().toString().equals("") && !TextUtils.isEmpty(dayEditText.getText().toString())) {
            choose_date = Integer.parseInt(dayEditText.getText().toString());
            day = String.valueOf(choose_date);
        }
        if (!monthEditText.getText().toString().equals("") && !TextUtils.isEmpty(monthEditText.getText().toString())) {
            mon = Integer.parseInt(monthEditText.getText().toString());
            month = String.valueOf(mon);
        }
        if (!yearEditText.getText().toString().equals("") && !TextUtils.isEmpty(yearEditText.getText().toString())) {
            yr = Integer.parseInt(yearEditText.getText().toString());
            year = String.valueOf(yr);
        }*/

        String day = dayEditText.getText().toString();
        String month = monthEditText.getText().toString();
        String year = yearEditText.getText().toString();
        String dob = month + "-" + day + "-" + year;

//        String day = String.valueOf(choose_date);
//        String month = String.valueOf(mon);
//        String year = String.valueOf(yr);


    /*    if (!month.equals("") && !day.equals("") && !year.equals("")) {

        } else {
            Constants.showAlert(getString(R.string.my_details), getString(R.string.dob_missing), getString(R.string.okay), MyDetailsActivity.this);
        }*/

        if (isDataValid(firstName, lastName, gender, address, day, month, year, phone, dob)) {
            phone = ccp.getText().toString() + "-" + phoneEditText.getText().toString();
            JSONObject jsonObject = new JSONObject();
            try {
                if (addressID == 0) {
                    jsonObject.put("ID", authenticatedUser.getShippingAddress().getiD());
                } else {
                    jsonObject.put("ID", addressID);
                }
                jsonObject.put("AddressLine1", address);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (countryName.getText().toString().equalsIgnoreCase("oman")) {
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
                countryName.setText(returnedResult);
                if (GlobalValues.countries != null) {
                    for (int i = 0; i < GlobalValues.countries.size(); i++) {
                        if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(MyDetailsActivity.this))) {
                            selectedCountry = GlobalValues.countries.get(i);
                            Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                            countryName.setText(selectedCountry.getName());
                        }
                    }
                }
                if (returnedResult.equalsIgnoreCase("oman")) {
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
                String d = data.getExtras().getString("DATA");
                addressID = data.getExtras().getInt("ID");
                addressEditText.setText(d);
            }
        } else if (requestCode == 2021) {
            if (resultCode == RESULT_OK) {
                String code = data.getExtras().getString("Code");
                ccp.setText("+" + code);
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

                if (radioButton.isChecked()) {
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
    public void updateResponse(boolean success) {
        if (success) {
            authenticatedUser.setFirstName(firstNameEditText.getText().toString());
            authenticatedUser.setLastName(lastNameEditText.getText().toString());
            authenticatedUser.setGender(genderText.getText().toString());
            authenticatedUser.setCountry(countryName.getText().toString());
            authenticatedUser.setRegion(regionName.getText().toString());
            authenticatedUser.setPhoneNumber(ccp.getText().toString() + "-" + phoneEditText.getText().toString());
            if (authenticatedUser.getShippingAddress() != null) {
                String arr[] = addressEditText.getText().toString().split(",");
                authenticatedUser.getShippingAddress().setAddressLine1(arr[0] + "," + arr[1]);
                if (arr.length > 2) {
                    authenticatedUser.getShippingAddress().setCity(arr[2]);
                }
                if (arr.length > 3) {
                    authenticatedUser.getShippingAddress().setCountry(arr[3]);
                }
                if (addressID != 0) {
                    authenticatedUser.getShippingAddress().setiD(addressID);
                    addressID = 0;
                }
            } else {
                ShippingAddress shippingAddress = new ShippingAddress();
                shippingAddress.setiD(addressID);
                String arr[] = addressEditText.getText().toString().split(",");
                shippingAddress.setAddressLine1(arr[0] + "," + arr[1]);
                if (arr.length > 2) {
                    shippingAddress.setCity(arr[2]);
                }
                if (arr.length > 3) {
                    shippingAddress.setCountry(arr[3]);
                }
                addressID = 0;
                authenticatedUser.setShippingAddress(shippingAddress);
            }
            if (selectedDateInMillis != 0) {
                authenticatedUser.setDateOfBirth(String.valueOf(selectedDateInMillis));
            }
            GlobalValues.saveUser(MyDetailsActivity.this, authenticatedUser);

            if (isRequest) {
                setResult(RESULT_OK);
                finish();
            } else {
                Constants.showAlertWithActivityFinish(getString(R.string.update_profile), getString(R.string.update_profile_success), getString(R.string.okay), MyDetailsActivity.this);
            }
        } else {
            if (isRequest) {
                Constants.showAlertWithActivityFinish(getString(R.string.update_profile), getString(R.string.update_profile_fail), getString(R.string.try_again), MyDetailsActivity.this);
            } else {
                Constants.showAlert(getString(R.string.update_profile), getString(R.string.update_profile_fail), getString(R.string.try_again), MyDetailsActivity.this);
            }
        }
    }
}