package com.algorepublic.saman.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseDashboardActivity;
import com.algorepublic.saman.ui.activities.onboarding.WelcomeActivity;
import com.algorepublic.saman.utils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import butterknife.ButterKnife;

public class DummyActivity extends BaseDashboardActivity implements  GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient mGoogleApiClient;
    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        changeFragment(Constants.Fragment.Home);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public void onNavItemClick(View view) {
        switch (view.getId()) {
            case R.id.cont_nav_home:
                changeFragment(Constants.Fragment.Home);
                break;

            case R.id.cont_nav_store:
                changeFragment(Constants.Fragment.Store);
                break;

            case R.id.cont_nav_favorite:
                changeFragment(Constants.Fragment.Favorite);
                break;

            case R.id.cont_nav_bag:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Intent intent=new Intent(DummyActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            }
                        });
//                changeFragment(Constants.Fragment.Bag);
                break;

            case R.id.cont_nav_my_account:

//                changeFragment(Constants.Fragment.MyAccount);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }
}
