package com.qtech.saman.ui.activities.myaccount.addresses;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import com.qtech.saman.ui.activities.country.RegionListingActivity;
import com.qtech.saman.ui.activities.map.GoogleMapActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

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
    @BindView(R.id.layout_regionSelection)
    LinearLayout layout_regionSelection;
    @BindView(R.id.tv_region_name)
    TextView region_name;

    @BindView(R.id.edit_building_floor)
    EditText buildingFloor;
    @BindView(R.id.edit_building_apt)
    EditText buildingApt;

    User authenticatedUser;
    String state = "states";

    int type;
    ShippingAddress shippingAddress;
    String latitude = "";
    String longitude = "";
    boolean is_first = true;

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
            if (is_first) {
                GlobalValues.setUserLat(AddShippingAddressActivity.this, "");
                GlobalValues.setUserLng(AddShippingAddressActivity.this, "");
                is_first = false;
            }
            Intent intent = new Intent(AddShippingAddressActivity.this, GoogleMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 1414);

        } else if (type == 2) {
            toolbarTitle.setText(getString(R.string.address));
            if (is_first) {
                GlobalValues.setUserLat(AddShippingAddressActivity.this, "");
                GlobalValues.setUserLng(AddShippingAddressActivity.this, "");
                is_first = false;
            }
            Intent intent = new Intent(AddShippingAddressActivity.this, GoogleMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 1414);
        } else {
            toolbarTitle.setText(getString(R.string.edit_shipping_address));
            toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            shippingAddress = (ShippingAddress) getIntent().getSerializableExtra("ShippingAddress");
            Log.e("SHIPPID00", "----shipping--add--" + new Gson().toJson(shippingAddress));

            if (shippingAddress.getLatitude() != null && shippingAddress.getLongitude() != null) {
                latitude = shippingAddress.getLatitude();
                longitude = shippingAddress.getLongitude();

                GlobalValues.setUserLat(AddShippingAddressActivity.this, "" + latitude);
                GlobalValues.setUserLng(AddShippingAddressActivity.this, "" + longitude);
            } else {
                GlobalValues.setUserLat(AddShippingAddressActivity.this, "");
                GlobalValues.setUserLng(AddShippingAddressActivity.this, "");
            }

            String addressLine1 = shippingAddress.getAddressLine1();
            String arr[] = addressLine1.split(",");
            streetEditText.setText(arr[0]);
            if (arr.length > 1) {
                buildingEditText.setText(arr[1]);
            }
//            if (arr.length >2){
//                buildingFloor.setText(arr[2]);
//            }
//            if (arr.length >3){
//                buildingApt.setText(arr[3]);
//            }
            if (shippingAddress.getFloor() != null) {
                buildingFloor.setText(shippingAddress.getFloor());
            }
            if (shippingAddress.getApt() != null) {
                buildingApt.setText(shippingAddress.getApt());
            }
            if (shippingAddress.getAddressLine2() != null) {
                landmarkEditText.setText(shippingAddress.getAddressLine2());
            }
            if (shippingAddress.getCity() != null) {
                cityEditText.setText(shippingAddress.getCity());
            }
            if (shippingAddress.getCountry() != null) {
                countryEditText.setText(shippingAddress.getCountry());
                region_name.setText(shippingAddress.getRegion());
                if (shippingAddress.getCountry().contains("oman")) {
                    ll_region.setVisibility(View.VISIBLE);
                    if (shippingAddress.getRegion() != null) {
                        region_name.setText(shippingAddress.getRegion());
                    }
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

    @OnClick(R.id.layout_regionSelection)
    public void regionSelection() {
        Intent intent = new Intent(AddShippingAddressActivity.this, RegionListingActivity.class);
        startActivityForResult(intent, 1299);
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(AddShippingAddressActivity.this, CountriesListingActivity.class);
        intent.putExtra("cartlist", 1);
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
//                        state = arr[1];
                        Log.e("LAT0LNG0", "--22--latitude---" + arr[1]);
                        Log.e("LAT0LNG0", "----longitude---" + arr[2]);
                        latitude = arr[1];
                        longitude = arr[2];
//                      countryEditText.setText(arr[2]);
                    } else if (arr.length > 0) {
//                      countryEditText.setText(arr[1]);
                        Log.e("LAT0LNG0", "----latitude-----" + arr[1]);
                        Log.e("LAT0LNG0", "---000-longitude---" + arr[2]);
                    }
                }
            }
        } else if (requestCode == 1199) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Log.e("LAT0LNG0", "---000-returnedResult---" + returnedResult);

                if (returnedResult != null && !returnedResult.isEmpty()) {
                    countryEditText.setText(returnedResult);
                    if (countryEditText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.oman)) || countryEditText.getText().toString().equalsIgnoreCase("oman")) {
                        ll_region.setVisibility(View.VISIBLE);
                    } else {
                        ll_region.setVisibility(View.GONE);
                    }
                }
            }
        } else if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                region_name.setText(returnedResult);
            }
        }
    }

    @OnClick(R.id.toolbar_search)
    public void userAddress() {

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
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.building) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (buildingFloor.getText() == null || buildingFloor.getText().toString().equals("") || buildingFloor.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.building_floor) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (buildingApt.getText() == null || buildingApt.getText().toString().equals("") || buildingApt.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.building_apartment) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (cityEditText.getText() == null || cityEditText.getText().toString().equals("") || cityEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.city) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }

        if (countryEditText.getText() == null || countryEditText.getText().toString().equals("") || countryEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.country) + " " + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
            return;
        }else {
            if (countryEditText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.Oman))) {
                if (region_name.getText() == null || region_name.getText().toString().equals("") ||
                        region_name.getText().toString().isEmpty()) {
                    Constants.showAlert(getString(R.string.add_shipping_address), getString(R.string.region) + " "
                            + getString(R.string.required), getString(R.string.okay), AddShippingAddressActivity.this);
                    return;
                }
            }
        }

        String addressLine = streetEditText.getText().toString() + "," + buildingEditText.getText().toString();

        String floor = buildingFloor.getText().toString();
        String apartment = buildingApt.getText().toString();

        String addressLine2 = landmarkEditText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if (type == 0) {
            Log.e("LATLNG00", "-latitude-longitude---" + latitude + longitude);
            if (latitude.equals("") && longitude.equals("")) {
                latitude = GlobalValues.getUserLat(AddShippingAddressActivity.this);
                longitude = GlobalValues.getUserLng(AddShippingAddressActivity.this);
            }

            WebServicesHandler.instance.addAddress(authenticatedUser.getId(), addressLine, floor, apartment, addressLine2,
                    cityEditText.getText().toString(), state, countryEditText.getText().toString(), region_name.getText().toString(),
                    latitude, longitude, isChecked, new retrofit2.Callback<AddAddressApi>() {
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

            if (latitude.equals("") && longitude.equals("")) {
                latitude = GlobalValues.getUserLat(AddShippingAddressActivity.this);
                longitude = GlobalValues.getUserLng(AddShippingAddressActivity.this);
            }

            WebServicesHandler.instance.updateAddress(authenticatedUser.getId(), shippingAddress.getiD(), addressLine, addressLine2,
                    floor, apartment, cityEditText.getText().toString(), state, countryEditText.getText().toString(), region_name.getText().toString(), latitude, longitude, isChecked, new retrofit2.Callback<SimpleSuccess>() {
                        @Override
                        public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                            progressBar.setVisibility(View.GONE);
                            SimpleSuccess simpleSuccess = response.body();
                            if (simpleSuccess != null) {
                                Log.e("UPDATEADD00", "--simpleSuccess--" + new Gson().toJson(simpleSuccess));
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
