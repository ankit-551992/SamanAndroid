package com.algorepublic.saman.ui.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.onboarding.WelcomeActivity;
import com.algorepublic.saman.ui.activities.password.ForgotPasswordActivity;
import com.algorepublic.saman.ui.activities.register.RegisterActivity;
import com.algorepublic.saman.utils.AsteriskPasswordTransformationMethod;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView,GoogleApiClient.OnConnectionFailedListener  {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_email)
    EditText emailEditText;
    @BindView(R.id.editText_password)
    EditText passwordEditText;
    @BindView(R.id.iv_password_visible)
    ImageView passwordVisibilityImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    boolean isShowing=false;

    //Social Login
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;

    TwitterAuthClient mTwitterAuthClient;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";

    // Social Login

    LoginPresenter mPresenter;
    boolean isGuestTry=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        isGuestTry=getIntent().getBooleanExtra("GuestTry",false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPresenter=new LoginPresenterImpl(this,new LoginDataInteractor());

        // Social Login
        mTwitterAuthClient= new TwitterAuthClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,mFacebookCallback);
        //Social Login
    }

    @OnClick(R.id.toolbar_back)
    public void back(){
        super.onBackPressed();
    }

    @OnClick(R.id.button_signUp)
    public void buttonSignUp(){
        Intent mainIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        mainIntent.putExtra("GuestTry",isGuestTry);
        startActivity(mainIntent);
        if(isGuestTry){
            finish();
        }
    }

    @OnClick(R.id.tv_forgotPassword)
    public void forgotPassword(){
        Intent mainIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(mainIntent);
    }

    @OnClick(R.id.button_guest_user)
    public void guestLogin(){
        User user=new User();
        Random rand = new Random();
        int n = rand.nextInt(50000) + 1;
        user.setId(0);
        user.setFirstName("Guest");
        user.setLastName("User");
        user.setEmail("guest"+n+"@saman.com");
        GlobalValues.saveUser(LoginActivity.this,user);
        GlobalValues.setGuestLoginStatus(LoginActivity.this,true);
        Intent mainIntent = new Intent(LoginActivity.this,DashboardActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @OnClick(R.id.button_login)
    public void buttonLogin(){
        String userName=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        if(isDataValid(userName,password)) {
            mPresenter.loginUser(userName, password,GlobalValues.getUserToken(LoginActivity.this));
        }
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


    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @OnClick(R.id.iv_email_cross)
    public void emailCross(){
        emailEditText.setText("");
    }

    @OnClick(R.id.iv_password_visible)
    public void setPasswordVisibility(){
        if(!isShowing){
            isShowing=true;
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setTransformationMethod(null);
            passwordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        }else {
            isShowing=false;
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            passwordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
        }
        passwordEditText.setSelection(passwordEditText.length());
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
    public void loginResponse(User user) {

        if(isGuestTry){
            GlobalValues.saveUser(LoginActivity.this, user);
            GlobalValues.setUserLoginStatus(LoginActivity.this, true);
            GlobalValues.setGuestLoginStatus(LoginActivity.this, false);
            finish();
        }else {
            GlobalValues.saveUser(LoginActivity.this, user);
            GlobalValues.setUserLoginStatus(LoginActivity.this, true);
            GlobalValues.setGuestLoginStatus(LoginActivity.this, false);
            Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(mainIntent);
            finish();
        }

    }

    @Override
    public void loginError(String message) {
        Constants.showAlert(getString(R.string.login_failed),message,getString(R.string.try_again),LoginActivity.this);
    }

    private boolean isDataValid(String userName, String password) {
        if (TextUtils.isEmpty(userName)) {
            Constants.showAlert(getString(R.string.Login),getString(R.string.email_required),getString(R.string.okay),LoginActivity.this);
            return false;
        }
//        else if (!isValidEmailId(userName)) {
//            Constants.showAlert(getString(R.string.sign_up), getString(R.string.email_invalid), getString(R.string.okay), RegisterActivity.this);
//            return false;
//        }
        else if (TextUtils.isEmpty(password)) {
            Constants.showAlert(getString(R.string.Login),getString(R.string.password_required),getString(R.string.okay),LoginActivity.this);
            return false;
        }
        else {
            return true;
        }
    }


    //Social Login

    // Google Plus Region

    @OnClick(R.id.google_signIn)
    public void googleSignIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = "";
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            String email = acct.getEmail();
            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

//            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//            startActivity(intent);
        }
    }

    // Google Plus Region End


    //Twitter Region

    @OnClick(R.id.twitter_signIn)
    public void twitter() {

//        mTwitterAuthClient.authorize(LoginActivity.this,new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                //If login succeeds passing the Calling the login method and passing Result object
//                login(result);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                //If failure occurs while login handle it here
//                Log.d("TwitterKit", "Login with Twitter failure", exception);
//            }
//        });
    }
    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {

        //Creating a ic_twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        twitterApiClient.getAccountService().verifyCredentials(true, false,false).enqueue(new Callback<com.twitter.sdk.android.core.models.User>(){
            @Override
            public void failure(TwitterException e) {
            }

            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> userResult) {
                com.twitter.sdk.android.core.models.User user = userResult.data;
                String name = userResult.data.name;
                //Allow email from admin panel
                String email = userResult.data.email;


                // _normal (48x48px) | _bigger (73x73px) | _mini (24x24px)
                String photoUrlNormalSize = userResult.data.profileImageUrl;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");

                Log.e(name, photoUrlNormalSize);
            }
        });

        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                Log.e("Email", result.toString());
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    //Twitter Region End

    //Facebook Region

    @OnClick(R.id.facebook_signIn)
    public void facebookSignIn(){

//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

//        LoginManager.getInstance().logInWithReadPermissions(
//                this,
//                Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
//        );
    }

    private FacebookCallback<LoginResult> mFacebookCallback= new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
//                                    String email = object.getString("email");
                                String name = object.getString("name");
//                                    String birthday = object.getString("birthday"); // 01/31/1980 format

                                Log.e("LoginResult",name);
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

    //Facebook Region End

    //Social Login
}
