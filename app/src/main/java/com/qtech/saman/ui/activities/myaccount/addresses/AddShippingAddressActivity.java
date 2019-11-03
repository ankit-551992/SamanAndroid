package com.qtech.saman.ui.activities.myaccount.addresses;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.AddAddressApi;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.country.CountriesListingActivity;
import com.qtech.saman.ui.activities.map.GoogleMapActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;

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
    @BindView(R.id.toolbar_search)
    ImageView markerImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.editText_street_no)
    EditText streetEditText;
    @BindView(R.id.editText_building_no)
    EditText buildingEditText;
    @BindView(R.id.editText_landmark)
    EditText landmarkEditText;
    @BindView(R.id.editText_city)
    EditText cityEditText;
    @BindView(R.id.editText_country)
    TextView countryEditText;
    @BindView(R.id.setDefault_checkBox)
    CheckBox setDefaultCheckBox;
    @BindView(R.id.button_add)
    Button addButton;
    @BindView(R.id.ll_region)
    LinearLayout ll_region;

    User authenticatedUser;
    String state = "states";

    int type;
    ShippingAddress shippingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        authenticatedUser = GlobalValues.getUser(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        type = getIntent().getIntExtra("Type", 0);

        if (type == 0) {
            toolbarTitle.setText(getString(R.string.add_shipping_address));
            toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            Intent intent = new Intent(AddShippingAddressActivity.this, GoogleMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 1414);
        } else if (type == 2) {
            toolbarTitle.setText(getString(R.string.address));
            Intent intent = new Intent(AddShippingAddressActivity.this, GoogleMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 1414);
        } else {
            toolbarTitle.setText(getString(R.string.edit_shipping_address));
            toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            shippingAddress = (ShippingAddress) getIntent().getSerializableExtra("ShippingAddress");
            String addressLine1 = shippingAddress.getAddressLine1();
            String arr[] = addressLine1.split(",");
            streetEditText.setText(arr[0]);
            if (arr.length > 1) {
                buildingEditText.setText(arr[1]);
            }
            if (shippingAddress.getAddressLine2() != null) {
                landmarkEditText.setText(shippingAddress.getAddressLine2());
            }
            if (shippingAddress.getCity() != null) {
                cityEditText.setText(shippingAddress.getCity());
            }
            if (shippingAddress.getCountry() != null) {
                countryEditText.setText(shippingAddress.getCountry());
                if (shippingAddress.getCountry().equalsIgnoreCase(getResources().getString(R.string.oman))) {
                    ll_region.setVisibility(View.VISIBLE);
                } else {
                    ll_region.setVisibility(View.GONE);
                }
            }
            setDefaultCheckBox.setChecked(shippingAddress.isDefault());
            addButton.setText(getString(R.string.update));
        }

        toolbarBack.setVisibility(View.VISIBLE);
        markerImageView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            markerImageView.setImageDrawable(getDrawable(R.drawable.ic_location_white));
        } else {
            markerImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_white));
        }
    }


    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(AddShippingAddressActivity.this, CountriesListingActivity.class);
        startActivityForResult(intent, 1199);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1414) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                if (!returnedResult.equals("") && !returnedResult.isEmpty()) {
                    String arr[] = returnedResult.split(",");
                    cityEditText.setText(arr[0]);
                    if (arr.length > 2) {
                        state = arr[1];
                        countryEditText.setText(arr[2]);
                    } else if (arr.length > 1) {
                        countryEditText.setText(arr[1]);
                    }
                }
            }
        } else if (requestCode == 1199) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
//                countryName.setText(returnedResult);
//                if (GlobalValues.countries != null) {
//                    for (int i = 0; i < GlobalValues.countries.size(); i++) {
//                        if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(MyDetailsActivity.this))) {
//                            selectedCountry = GlobalValues.countries.get(i);
//                            Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
//                            if (SamanApp.isEnglishVersion) {
//                                countryName.setText(selectedCountry.getName());
//                            } else {
//                                countryName.setText(selectedCountry.getName_AR());
//                            }
//                        }
//                    }
//                }
                if (returnedResult != null && !returnedResult.isEmpty()) {
                    if (GlobalValues.countries != null) {
                        for (int i = 0; i < GlobalValues.countries.size(); i++) {
                            if (GlobalValues.countries.get(i).getName().equalsIgnoreCase(returnedResult)
                                    || GlobalValues.countries.get(i).getName_AR().equalsIgnoreCase(returnedResult)) {
                                if (SamanApp.isEnglishVersion) {
                                    countryEditText.setText(GlobalValues.countries.get(i).getName());
                                } else {
                                    countryEditText.setText(GlobalValues.countries.get(i).getName_AR());
                                }

                                if (GlobalValues.countries.get(i).getName().equalsIgnoreCase(getResources().getString(R.string.oman))) {
                                    ll_region.setVisibility(View.VISIBLE);
                                } else {
                                    ll_region.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OnClick(R.id.toolbar_search)
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
        Intent intent = new Intent(AddShippingAddressActivity.this, GoogleMapActivity.class);
        startActivityForResult(intent, 1414);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_add)
    public void addAddress() {

        boolean isChecked = setDefaultCheckBox.isChecked();

        if (streetEditText.getText() == null || streetEditText.getText().toString().equals("") || streetEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.street_no) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (buildingEditText.getText() == null || buildingEditText.getText().toString().equals("") || buildingEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.building_no) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (cityEditText.getText() == null || cityEditText.getText().toString().equals("") || cityEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.city) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (countryEditText.getText() == null || countryEditText.getText().toString().equals("") || countryEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.country) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }
        String addressLine = streetEditText.getText().toString() + "," + buildingEditText.getText().toString();
        String addressLine2 = landmarkEditText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if (type == 0) {

            WebServicesHandler.instance.addAddress(authenticatedUser.getId(), addressLine, addressLine2, cityEditText.getText().toString(), state, countryEditText.getText().toString(), isChecked, new retrofit2.Callback<AddAddressApi>() {
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

        } else if (type == 2) {
            progressBar.setVisibility(View.GONE);
            Gson gson = new Gson();
            ShippingAddress shippingAddress = new ShippingAddress();
            shippingAddress.setAddressLine1(addressLine);
            shippingAddress.setAddressLine2(addressLine2);
            shippingAddress.setCity(cityEditText.getText().toString());
            shippingAddress.setState(state);
            shippingAddress.setCountry(countryEditText.getText().toString());
            String addr = gson.toJson(shippingAddress);
            Intent data = new Intent();
            data.setData(Uri.parse(addr));
            setResult(RESULT_OK, data);
            finish();
        } else {
            WebServicesHandler.instance.updateAddress(authenticatedUser.getId(), shippingAddress.getiD(), addressLine, addressLine2, cityEditText.getText().toString(), state, countryEditText.getText().toString(), isChecked, new retrofit2.Callback<SimpleSuccess>() {
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
