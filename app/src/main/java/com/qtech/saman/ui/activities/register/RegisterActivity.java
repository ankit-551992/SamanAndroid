package com.qtech.saman.ui.activities.register;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.PoliciesActivity;
import com.qtech.saman.ui.activities.country.CountriesListingActivity;
import com.qtech.saman.ui.activities.country.RegionListingActivity;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.login.LoginActivity;
import com.qtech.saman.ui.activities.myaccount.addresses.AddShippingAddressActivity;
import com.qtech.saman.ui.activities.password.NumberVerificationActivity;
import com.qtech.saman.utils.AsteriskPasswordTransformationMethod;
import com.qtech.saman.utils.CircleTransform;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements RegisterView, GoogleApiClient.OnConnectionFailedListener {

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
    @BindView(R.id.editText_password)
    EditText passwordEditText;
    @BindView(R.id.editText_confirmPassword)
    EditText confirmPasswordEditText;
    @BindView(R.id.iv_password_visible)
    ImageView passwordVisibilityImageView;
    @BindView(R.id.iv_confirm_password_visible)
    ImageView confirmPasswordVisibilityImageView;
    @BindView(R.id.tv_gender)
    TextView genderText;
    @BindView(R.id.tv_country_name)
    TextView countryName;
    @BindView(R.id.tv_region_name)
    TextView regionName;
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
    @BindView(R.id.tv_terms_policies)
    TextView termPolicy;
    @BindView(R.id.ccp)
    EditText ccp;
    @BindView(R.id.editText_phone)
    EditText phoneEditText;

    @BindView(R.id.layout_regionSelection)
    LinearLayout regionSelectionLinearLayout;
    @BindView(R.id.view_region)
    View regionView;
    @BindView(R.id.iv_country_flag)
    ImageView countryFlag;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    boolean isShowing = false;
    boolean isConfirmShowing = false;

    RegisterPresenterImpl mPresenter;
    ArrayAdapter<String> arrayAdapter;

    //Social Login
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;

    TwitterAuthClient mTwitterAuthClient;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    //Social Login

    String socialName = "";
    String socialEmail = "";
    String socialPhotoUrl = "";

    Calendar myCalendar;
    String returnedResult;
    boolean isGuestTry = false;
    boolean isNumberVerified = false;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.sign_up));
        isGuestTry = getIntent().getBooleanExtra("GuestTry", false);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        confirmPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPresenter = new RegisterPresenterImpl(this, new RegisterDataInteractor());
        mPresenter.getCountries();
//        ccp.setDefaultCountryUsingNameCode("OM");
//        ccp.resetToDefaultCountry();
//        ccp.hideNameCode(true);
//        ccp.showFlag(false);
//        Social Login

        mTwitterAuthClient = new TwitterAuthClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, mFacebookCallback);
        //Social Login
        myCalendar = Calendar.getInstance();

        customTextView(termPolicy);
//        addSpinner();

        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String phone = ccp.getText().toString();
                if (hasFocus) {
                    if (TextUtils.isEmpty(phone)) {
                          chooseCountryCode();
//                        Constants.showAlert(getString(R.string.sign_up), getString(R.string.enter_countrycode), getString(R.string.okay), RegisterActivity.this);
                    } else {
                    }
                } else {
                }
            }
        });
    }

    @OnClick(R.id.editText_phone)
    void phoneText() {
        String phone = ccp.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            chooseCountryCode();
//          Constants.showAlert(getString(R.string.sign_up), getString(R.string.enter_countrycode),
//          getString(R.string.okay), RegisterActivity.this);
        } else {
        }
    }

    @OnClick({R.id.editText_day, R.id.editText_month, R.id.editText_year})
    void setDOB() {
        DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
//      dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }

    @OnClick(R.id.tv_numberVerify)
    void numberVerifyButton() {
        String phone = ccp.getText().toString() + phoneEditText.getText().toString();
        phone = phone.replace("+", "");

        if (TextUtils.isEmpty(phone)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.Phone_Number_Required), getString(R.string.okay),
                    RegisterActivity.this);
            return;
        } else if (phone.length() < 10) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.Not_Valid), getString(R.string.okay), RegisterActivity.this);
            return;
        }
        Intent intent = new Intent(RegisterActivity.this, NumberVerificationActivity.class);
        intent.putExtra("Number", phone);
        startActivityForResult(intent, 2025);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//            selectedDateInMillis=myCalendar.getTimeInMillis();
            if (!isAfterToday(year, monthOfYear, dayOfMonth)) {
                updateLabel();
            } else {
                Constants.showAlert(getString(R.string.my_details), getString(R.string.invalid_date),
                        getString(R.string.okay), RegisterActivity.this);
            }
        }
    };

    public boolean isAfterToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();
        myDate.set(year, month, day);
        if (myDate.before(today)) {
            return false;
        }
        return true;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String dateSelected = sdf.format(myCalendar.getTime());
        String sepDate[] = dateSelected.split("/");
        dayEditText.setText(sepDate[0]);
        monthEditText.setText(sepDate[1]);
        yearEditText.setText(sepDate[2]);
    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getString(R.string.sign_up_agreement));
        spanTxt.append(getString(R.string.term_of_use));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, PoliciesActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        }, spanTxt.length() - getString(R.string.term_of_use).length(), spanTxt.length(), 0);
        spanTxt.append(" & ");
        spanTxt.append(getString(R.string.privacy_policy));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, PoliciesActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        }, spanTxt.length() - getString(R.string.privacy_policy).length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    Dialog dialog;
    String selectedGender = "";

    private void selectGender() {
        dialog = new Dialog(RegisterActivity.this, R.style.CustomDialog);
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
        animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @OnClick(R.id.iv_pin)
    public void userAddress() {
//        try {
//          Intent intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                            .build(this);
//            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        } catch (GooglePlayServicesRepairableException e) {
//
//        } catch (GooglePlayServicesNotAvailableException e) {
//       }
        Intent intent = new Intent(RegisterActivity.this, AddShippingAddressActivity.class);
        intent.putExtra("Type", 2);
        startActivityForResult(intent, 1414);

//        Intent intent = new Intent(mContext, AddShippingAddressActivity.class);
//        intent.putExtra("ShippingAddress", shippingAddresses.get(position));
//        intent.putExtra("Type", 1);
//        mContext.startActivity(intent);
    }

    @OnClick(R.id.button_signIn)
    public void buttonSignUp() {
        if (isGuestTry) {
            Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            mainIntent.putExtra("GuestTry", true);
            startActivity(mainIntent);
        } else {
            finish();
        }
    }

    @OnClick(R.id.layout_GenderSelection)
    public void selectGenderClick() {
        selectGender();
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(RegisterActivity.this, CountriesListingActivity.class);
        startActivityForResult(intent, 1299);
    }

    @OnClick(R.id.layout_regionSelection)
    public void regionSelection() {
        Intent intent = new Intent(RegisterActivity.this, RegionListingActivity.class);
        startActivityForResult(intent, 1309);
    }

    @OnClick(R.id.button_register)
    public void registerButton() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String gender = selectedGender;
        String country = countryName.getText().toString();
        String phone = ccp.getText().toString() + phoneEditText.getText().toString();
        String region = regionName.getText().toString();

        int date;
        int mon;
        int yr;
        String day = "";
        String month = "";
        String year = "";
        if (dayEditText.getText() != null && !dayEditText.getText().toString().equals("")) {
            date = Integer.parseInt(dayEditText.getText().toString());
            day = String.valueOf(date);
        }

        if (monthEditText.getText() != null && !monthEditText.getText().toString().equals("")) {
            mon = Integer.parseInt(monthEditText.getText().toString());
            month = String.valueOf(mon);
        }
        if (yearEditText.getText() != null && !yearEditText.getText().toString().equals("")) {
            yr = Integer.parseInt(yearEditText.getText().toString());
            year = String.valueOf(yr);
        }
//        isNumberVerified=true;
        String address = addressEditText.getText().toString();
        if (isDataValid(firstName, lastName, email, password, confirmPassword, gender, country, address, day, month, year, phone)) {
            if (countryName.getText().toString().equalsIgnoreCase("oman")) {
                if (region == null || TextUtils.isEmpty(region)) {
                    Constants.showAlert(getString(R.string.sign_up), getString(R.string.region_required),
                            getString(R.string.okay), RegisterActivity.this);
                    return;
                }
            } else {
                region = "";
            }
            String dob = day + "-" + month + "-" + year;
            Intent intent = new Intent(RegisterActivity.this, PoliciesActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("checkRequired", true);
            startActivityForResult(intent, 1401);
        }
    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.iv_email_cross)
    public void emailCross() {
        emailEditText.setText("");
    }

    @OnClick(R.id.iv_password_visible)
    public void setPasswordVisibility() {
        if (!isShowing) {
            isShowing = true;
            passwordEditText.setTransformationMethod(null);
            passwordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        } else {
            isShowing = false;
            passwordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            passwordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        passwordEditText.setSelection(passwordEditText.length());
    }

    @OnClick(R.id.iv_confirm_password_visible)
    public void setConfirmPasswordVisibility() {
        if (!isConfirmShowing) {
            isConfirmShowing = true;
            confirmPasswordEditText.setTransformationMethod(null);
            confirmPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        } else {
            isConfirmShowing = false;
            confirmPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            confirmPasswordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        confirmPasswordEditText.setSelection(confirmPasswordEditText.length());
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
    public void registerResponse(User user) {
        if (isGuestTry) {
            GlobalValues.saveUser(RegisterActivity.this, user);
            GlobalValues.setGuestLoginStatus(RegisterActivity.this, false);
            GlobalValues.setUserLoginStatus(RegisterActivity.this, true);
            finish();
        } else {
            GlobalValues.saveUser(RegisterActivity.this, user);
            GlobalValues.setGuestLoginStatus(RegisterActivity.this, false);
            GlobalValues.setUserLoginStatus(RegisterActivity.this, true);
            Intent mainIntent = new Intent(RegisterActivity.this, DashboardActivity.class);
            startActivity(mainIntent);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }
    }

    @Override
    public void registerError(String message) {
        Constants.showAlert(getString(R.string.sign_up), message, getString(R.string.try_again), RegisterActivity.this);
    }

    @Override
    public void countries(ArrayList<String> countriesList) {

        if (GlobalValues.countries == null || GlobalValues.countries.size() == 0) {
            GlobalValues.countries = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(Constants.loadJSONFromAsset(getApplicationContext()));
                JSONArray jsonArray = obj.getJSONArray("countries");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Country country = new Country();
                    country.setId(jsonObject.getInt("id"));
                    country.setSortname(jsonObject.getString("sortname"));
                    country.setName(jsonObject.getString("name"));
                    country.setName_AR(jsonObject.getString("name_AR"));
                    country.setFlag("https://www.saman.om/Flags/flag_" + jsonObject.getString("sortname").toLowerCase() + ".png");
                    country.setPhoneCode("" + jsonObject.getInt("phoneCode"));
                    GlobalValues.countries.add(country);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            WebServicesHandler.instance.getCountries(new retrofit2.Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        JSONObject JObject = new JSONObject(response.body().string());
//                        int status = 0;
//                        status = JObject.getInt("success");
//                        if (status==0) {
//
//                        } else if (status==1) {
//                            if (JObject.has("result")) {
//                                JSONArray jsonArray=JObject.getJSONArray("result");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Country country = new Country();
//
//                                    country.setId(jsonObject.getInt("ID"));
//                                    String flagURL=jsonObject.getString("FlagURL");
//
//                                    String[] array= flagURL.split("/");
//                                    String[] array2=array[array.length-1].split("\\.");
//                                    String[] array3=array2[0].split("_");
//                                    String shortNameCode=array3[array3.length-1];
//
//                                    country.setSortname(shortNameCode);
//                                    country.setName(jsonObject.getString("CountryName"));
//                                    country.setFlag(Constants.URLS.BaseURLImages+flagURL);
//                                    country.setPhoneCode(jsonObject.getString("CountryCode"));
//
//                                    GlobalValues.countries.add(country);
//                                }
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                }
//            });
        }
    }


    @OnClick(R.id.ccp)
    public void phoneCode() {
        chooseCountryCode();
    }

    private void chooseCountryCode() {
        Intent intent = new Intent(RegisterActivity.this, CountriesListingActivity.class);
        intent.putExtra("GetCode", true);
        startActivityForResult(intent, 2021);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                countryName.setText(returnedResult);
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
        } else if (requestCode == 1401) {
            if (resultCode == RESULT_OK) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String gender = selectedGender;
                String country = countryName.getText().toString();
                String phone = ccp.getText().toString() + "-" + phoneEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String region = regionName.getText().toString();

                if (region == null && region.isEmpty()) {
                    region = "";
                }

                int date = Integer.parseInt(dayEditText.getText().toString());
                int mon = Integer.parseInt(monthEditText.getText().toString());
                int yr = Integer.parseInt(yearEditText.getText().toString());

                String day = String.valueOf(date);
                String month = String.valueOf(mon);
                String year = String.valueOf(yr);

                String dob = month + "-" + day + "-" + year;
                if (!flag.equals("")) {
                    GlobalValues.setSelectedCodeFlag(RegisterActivity.this, flag);
                }
                mPresenter.registerUser(firstName, lastName, email, password, GlobalValues.getUserToken(RegisterActivity.this), gender, country, returnedResult, dob, phone, region);
            }
        } else if (requestCode == 1414) {
            if (resultCode == RESULT_OK) {
                returnedResult = data.getData().toString();
                ShippingAddress obj = new Gson().fromJson(returnedResult, ShippingAddress.class);
                addressEditText.setText(obj.getAddressLine1() + "," + obj.getCity() + "," + obj.getCountry());
            }
        } else if (requestCode == 2021) {
            if (resultCode == RESULT_OK) {
                String code = data.getExtras().getString("Code");
                flag = data.getExtras().getString("Flag");
                Log.e("FLAG000", "--flag---" + flag + "---code---" + code);
//              ccp.setText("+" + code);
                ccp.setText(code);
                if (!flag.equals("")) {
                    Picasso.get().load(flag).transform(new CircleTransform()).into(countryFlag);
                }
            }
        } else if (requestCode == 2025) {
            if (resultCode == RESULT_OK) {
                isNumberVerified = true;
                phoneEditText.setEnabled(false);
                ccp.setEnabled(false);
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                addressEditText.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isDataValid(String fName, String lName, String email, String password, String confrim, String gender, String country, String address, String day, String month, String year, String phone) {
        if (TextUtils.isEmpty(fName)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.first_name_required),
                    getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(lName)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.last_name_required),
                    getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.email_required), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (!isValidEmailId(email)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.email_invalid), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.Phone_Number_Required), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (phone.length() < 10) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.Not_Valid), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.password_required), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (password.length() < 6) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.password_short), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(confrim)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.confirm_password_required), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (!confrim.equals(password)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.not_matched), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(gender)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.gender_prompt), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(country)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.select_country_req), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(day)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.day_missing), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(month)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.month_missing), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(year)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.year_missing), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (TextUtils.isEmpty(address)) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.address_req), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else if (!isNumberVerified) {
            Constants.showAlert(getString(R.string.sign_up), getString(R.string.number_verification_required), getString(R.string.okay), RegisterActivity.this);
            return false;
        } else {
            return true;
        }
    }


    //Social Login


    // Google Plus Region

    @OnClick(R.id.google_signIn)
    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.e(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            socialName = acct.getDisplayName();

            if (acct.getPhotoUrl() != null) {
                socialPhotoUrl = acct.getPhotoUrl().toString();
            }
            socialEmail = acct.getEmail();
            socialLogin(1);
//            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//            startActivity(intent);
        }
    }


    // Google Plus Region End


    //Twitter Region

    @OnClick(R.id.twitter_signIn)
    public void twitter() {

        mTwitterAuthClient.authorize(RegisterActivity.this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {

        //Creating a ic_twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();

        getTwitterData(false);

        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                socialEmail = result.data;
                if (socialName.equalsIgnoreCase("")) {
                    getTwitterData(true);
                } else {
//                    Log.e("Twitter", socialName + "\n" + socialEmail + "\n" + socialPhotoUrl);
                    socialLogin(2);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    private void getTwitterData(boolean tryAgain) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        twitterApiClient.getAccountService().verifyCredentials(true, false, false).enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void failure(TwitterException e) {
            }

            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> userResult) {
                com.twitter.sdk.android.core.models.User user = userResult.data;
                socialName = userResult.data.name;
                // _normal (48x48px) | _bigger (73x73px) | _mini (24x24px)
                socialPhotoUrl = userResult.data.profileImageUrl;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");
                if (tryAgain) {
//                    Log.e("TTwitter", socialName + "\n" + socialEmail + "\n" + socialPhotoUrl);
                    socialLogin(2);
                }
            }
        });
    }

    //Twitter Region End

    //Facebook Region

    @OnClick(R.id.facebook_signIn)
    public void facebookSignIn() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
        );
    }

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            // Application code
                            try {
                                String fbId;
                                String location;
                                socialName = object.getString("name");
                                String birthday; // 01/31/1980 format


                                try {
                                    fbId = object.getString("id");
                                } catch (Exception e) {
                                    fbId = "";
                                    e.printStackTrace();

                                }


                                try {
                                    socialEmail = object.getString("email");
                                } catch (Exception e) {
                                    socialEmail = fbId + "@facebook.com";
                                    e.printStackTrace();
                                }

                                try {
                                    birthday = object.getString("birthday");
                                } catch (Exception e) {
                                    birthday = "";
                                    e.printStackTrace();
                                }

                                try {
                                    JSONObject jsonobject_location = object.getJSONObject("location");
                                    location = jsonobject_location.getString("name");

                                } catch (Exception e) {
                                    location = "";
                                    e.printStackTrace();
                                }

                                fbProfileImage();
//
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
        }
    };

    private void fbProfileImage() {

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            socialPhotoUrl = (String) response.getJSONObject().getJSONObject("data").get("url");
                            socialLogin(3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }

    //Facebook Region End

    //socialID ----> 1 for G+,2 for Twitter, 3 for Facebook
    private void socialLogin(final int socialID) {

        showProgress();
        WebServicesHandler apiClient = WebServicesHandler.instance;

        String[] names = socialName.split(" ");
        String lastName = "";
        if (names.length > 1) {
            lastName = names[1];
        }

        apiClient.socialLogin(names[0], lastName, socialEmail, GlobalValues.getUserToken(RegisterActivity.this), socialPhotoUrl, new retrofit2.Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();
                User user = null;
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if (userResponse.getSuccess() == 1) {
                        user = userResponse.getUser();
                        if (user != null) {
                            if (isGuestTry) {
                                GlobalValues.saveUser(RegisterActivity.this, user);
                                GlobalValues.setUserLoginStatus(RegisterActivity.this, true);
                                GlobalValues.setGuestLoginStatus(RegisterActivity.this, false);
                                finish();
                            } else {
                                user.setSocialID(socialID);
                                user.setProfileImagePath(socialPhotoUrl);
                                GlobalValues.saveUser(RegisterActivity.this, user);
                                GlobalValues.setUserLoginStatus(RegisterActivity.this, true);
                                GlobalValues.setGuestLoginStatus(RegisterActivity.this, false);
                                Intent mainIntent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                if (user.getPhoneNumber() == null || user.getShippingAddress().getAddressLine1() == null || user.getShippingAddress().getState() == null || user.getShippingAddress().getFloor() == null || user.getCountry() == null) {
                                    mainIntent.putExtra("NavItem", 4);
                                    mainIntent.putExtra("OpenDetails", true);
                                } else if (user.getPhoneNumber().isEmpty() || user.getShippingAddress().getAddressLine1().isEmpty() || user.getShippingAddress().getState().isEmpty() || user.getShippingAddress().getFloor().isEmpty() || user.getCountry().isEmpty()
                                        || user.getPhoneNumber().equalsIgnoreCase("") || user.getShippingAddress().getAddressLine1().equalsIgnoreCase("") || user.getCountry().equalsIgnoreCase("")) {
                                    mainIntent.putExtra("NavItem", 4);
                                    mainIntent.putExtra("OpenDetails", true);
                                }
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                hideProgress();
            }
        });
    }

    //Social Login

    void addSpinner() {
//        ArrayList<String> yearList = new ArrayList<>();
//
//        for (int i = 0; i <= 9; i++) {
//            yearList.add("190" + i);
//        }
//
//        for (int i = 10; i <= 99; i++) {
//            yearList.add("19" + i);
//        }
//        for (int i = 0; i <= 9; i++) {
//            yearList.add("200" + i);
//        }
//
//        for (int i = 10; i <= 99; i++) {
//            yearList.add("20" + i);
//        }
//
//        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, yearList);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        yearEditText.setAdapter(dataAdapter1);
//
//        yearEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//        ArrayList<String> monthList = new ArrayList<>();
//        monthList.add("Jan");
//        monthList.add("Feb");
//        monthList.add("Mar");
//        monthList.add("Apr");
//        monthList.add("May");
//        monthList.add("Jun");
//        monthList.add("Jul");
//        monthList.add("Aug");
//        monthList.add("Sep");
//        monthList.add("Oct");
//        monthList.add("Nov");
//        monthList.add("Dec");
//
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, monthList);
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        monthEditText.setAdapter(dataAdapter2);
//
//        monthEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                yearEditText.performClick();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//        ArrayList<String> dayList = new ArrayList<>();
//        for (int i = 1; i <= 31; i++) {
//            dayList.add("" + i);
//        }
//
//        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, dayList);
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dayEditText.setAdapter(dataAdapter3);
//
//        dayEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    monthEditText.performClick();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
}
