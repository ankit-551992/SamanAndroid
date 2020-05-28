package com.qtech.saman.ui.activities.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.network.GeoLocationHandler;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GPSTracker;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class GoogleMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnIndoorStateChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_search)
    ImageView search;

    SupportMapFragment mapFragment;
    String address = "";
    private FusedLocationProviderClient mFusedLocationClient;

    private GoogleMap gmap;
    LatLng markedLocation;
    String selected_country = "";
    private static final int REQUEST_LOCATION_CODE = 1042;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        ButterKnife.bind(this);

        MapsInitializer.initialize(SamanApp.getInstance().getApplicationContext());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.select_location));
        toolbarBack.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            search.setImageDrawable(getDrawable(R.drawable.ic_tick));
        } else {
            search.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
        }
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_search)
    public void done() {
        if (markedLocation != null) {
            GlobalValues.setUserLat(GoogleMapActivity.this, "" + markedLocation.latitude);
            GlobalValues.setUserLng(GoogleMapActivity.this, "" + markedLocation.longitude);
            address = getAddressFromLatLong(markedLocation);
            if (!address.isEmpty() && !address.equals("")) {
                saveSelectedAddress(address);
            } else {
                getLocationAddress(markedLocation);
            }
        }
    }

    private String getCompleteAddressString(LatLng point) {

        List<Address> addresses;
        String strAdd = "";
        Geocoder geocoder = new Geocoder(GoogleMapActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    private String getAddressFromLatLong(LatLng mLatLong) {
        Geocoder geocoder;
        List<Address> addresses;
        String address = "";

        geocoder = new Geocoder(GoogleMapActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mLatLong.latitude, mLatLong.longitude, 1);
            try {
//              address = addresses.get(0).getAddressLine(0);
                address += addresses.get(0).getLocality() + ",";
                // address += addresses.get(0).getAdminArea() + ",";
                // address += addresses.get(0).getPostalCode() + ",";
//              address += addresses.get(0).getCountryName();
                address += addresses.get(0).getLatitude() + ",";
                address += addresses.get(0).getLongitude() + ",";
                address += addresses.get(0).getCountryName();
//              selected_country = addresses.get(0).getCountryName();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//      https://developers.google.com/maps/documentation/android-sdk/map
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.setIndoorEnabled(true);
        gmap.getUiSettings().setIndoorLevelPickerEnabled(true);
//      Log.e("Level",""+gmap.getFocusedBuilding().getLevels().size());
        moveCameraToCurrent();
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                search.setVisibility(View.VISIBLE);
                gmap.clear();
                gmap.addMarker(new MarkerOptions().position(point));
                markedLocation = point;
            }
        });
        if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gmap.setMyLocationEnabled(true);
        }

//        Destiny USA, Destiny USA Drive, Syracuse, NY, USA
//        LatLng latLng = new LatLng(43.068242,-76.1738881);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//        gmap.addMarker(new MarkerOptions().position(new LatLng(43.068242,-76.1738881)));
//        gmap.animateCamera(cameraUpdate);

//        Gallagher Convention Centre
//        LatLng latLng = new LatLng(-26.0017037,28.1277542);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//        gmap.addMarker(new MarkerOptions().position(new LatLng(-26.0017037,28.1277542)));
//        gmap.animateCamera(cameraUpdate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                if (grantResults.length > 0 && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkGPSEnable();
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                GlobalValues.setUserLat(GoogleMapActivity.this, "" + location.getLatitude());
                                GlobalValues.setUserLng(GoogleMapActivity.this, "" + location.getLongitude());
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    gmap.setMyLocationEnabled(true);
                                }
                                gmap.animateCamera(cameraUpdate);
                                search.setVisibility(View.VISIBLE);
                                markedLocation = latLng;
                            }
                        }
                    });
                }
            }
        }
    }

    private void locationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_CODE);
        } else {
            checkGPSEnable();
        }
    }

    private void checkGPSEnable() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableGps(GoogleMapActivity.this);
            }
        }
    }

    private void enableGps(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(locationSettingsResult -> {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i("AlgoRepublic", "All location settings are satisfied.");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i("AlgoRepublic", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                    try {
                        status.startResolutionForResult(GoogleMapActivity.this, 0x1);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i("AlgoRepublic", "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i("AlgoRepublic", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
    }

    public void moveCameraToCurrent() {
        GPSTracker gpsTracker = new GPSTracker(GoogleMapActivity.this);

        if (GlobalValues.getUserLat(GoogleMapActivity.this) != null &&
                GlobalValues.getUserLng(GoogleMapActivity.this) != null
                && GlobalValues.getUserLat(GoogleMapActivity.this) != "" &&
                GlobalValues.getUserLng(GoogleMapActivity.this) != "") {
            double lat = Double.parseDouble(GlobalValues.getUserLat(GoogleMapActivity.this));
            double lng = Double.parseDouble(GlobalValues.getUserLng(GoogleMapActivity.this));
            LatLng latLng = new LatLng(lat, lng);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gmap.setMyLocationEnabled(true);
            }
            gmap.addMarker(new MarkerOptions().position(latLng));
            gmap.animateCamera(cameraUpdate);
            search.setVisibility(View.VISIBLE);
            markedLocation = latLng;
        } else {
            if (gpsTracker.getLongitude() != 0.0 && gpsTracker.getLatitude() != 0.0) {
                LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                GlobalValues.setUserLat(GoogleMapActivity.this, "" + gpsTracker.getLatitude());
                GlobalValues.setUserLng(GoogleMapActivity.this, "" + gpsTracker.getLongitude());

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gmap.setMyLocationEnabled(true);
                }
                gmap.addMarker(new MarkerOptions().position(latLng));
                gmap.animateCamera(cameraUpdate);
                search.setVisibility(View.VISIBLE);
                markedLocation = latLng;
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
//                          Logic to handle location object
//                          GlobalValues.setUserLat(GoogleMapActivity.this, "" + location.getLatitude());
//                          GlobalValues.setUserLng(GoogleMapActivity.this, "" + location.getLongitude());
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                gmap.setMyLocationEnabled(true);
                            }
                            gmap.animateCamera(cameraUpdate);
                            search.setVisibility(View.VISIBLE);
                            markedLocation = latLng;
                        } else {
                            if (GlobalValues.getUserLat(GoogleMapActivity.this) != null &&
                                    GlobalValues.getUserLng(GoogleMapActivity.this) != null
                                    && !GlobalValues.getUserLat(GoogleMapActivity.this).equals("") &&
                                    !GlobalValues.getUserLng(GoogleMapActivity.this).equals("")) {
                                double lati = Double.parseDouble(GlobalValues.getUserLat(GoogleMapActivity.this));
                                double lngi = Double.parseDouble(GlobalValues.getUserLng(GoogleMapActivity.this));
                                LatLng latLng = new LatLng(lati, lngi);
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    gmap.setMyLocationEnabled(true);
                                }
                                gmap.animateCamera(cameraUpdate);
                                search.setVisibility(View.VISIBLE);
                                markedLocation = latLng;
                            }
                        }
                    }
                });
            }
        }
    }

    private void getLocationAddress(LatLng latLng) {
        Constants.showSpinner(getString(R.string.select_location) + " " + getString(R.string.pending), GoogleMapActivity.this);
        GeoLocationHandler.instance.getLocation(latLng, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.dismissSpinner();
                String adress = null;
                try {
                    JSONObject googleMapResponse = new JSONObject(response.body().string());
                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        if (result.has("types")) {
                            JSONArray types = result.getJSONArray("types");
//                          search for locality or sub locality
//                          sub locality is preferred
                            for (int k = 0; k < types.length(); k++) {
                                if ("locality".equals(types.getString(k))) {
                                    if (result.has("formatted_address")) {
                                        adress = result.getString("formatted_address");
                                    }
                                }

                            }
                        }
                    }
                    if (adress != null) {
                        address = adress;
                        saveSelectedAddress(address);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Intent data = new Intent();
                data.setData(Uri.parse("Muscat Oman"));
                setResult(RESULT_OK, data);
                finish();
                Constants.dismissSpinner();
            }
        });
    }

    private void saveSelectedAddress(String address) {

        String[] arr = address.split(",");

        if (arr.length >= 4 && !arr[3].isEmpty() && arr[3].equals(getResources().getString(R.string.Oman))) {
            Intent data = new Intent();
            data.setData(Uri.parse(address));
            setResult(RESULT_OK, data);
            finish();
        } else {
            Constants.showErrorPopUp(GoogleMapActivity.this, "", getResources().getString(R.string.map_dialog_msg), getResources().getString(R.string.okay));
        }

    }

    @Override
    public void onIndoorBuildingFocused() {

        IndoorBuilding building = gmap.getFocusedBuilding();
        if (building != null) {
            List<IndoorLevel> levels = building.getLevels();
            //active the level you want to display on the map
            levels.get(1).activate();
        }
    }

    @Override
    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
        Log.e("Levels", "" + indoorBuilding.getLevels().size());
    }
}
