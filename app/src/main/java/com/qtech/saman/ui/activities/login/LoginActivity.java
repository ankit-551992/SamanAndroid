package com.qtech.saman.ui.activities.login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.ChangeLanguage;
import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.SplashActivity;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.password.ForgotPasswordActivity;
import com.qtech.saman.ui.activities.register.RegisterActivity;
import com.qtech.saman.utils.AsteriskPasswordTransformationMethod;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.qtech.saman.utils.Constants.dialog;

public class LoginActivity extends BaseActivity implements LoginView, GoogleApiClient.OnConnectionFailedListener {

    //Social Login
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private static final String EMAIL = "email";

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
    @BindView(R.id.tv_language)
    TextView languageTextView;
    boolean isShowing = false;
    TwitterAuthClient mTwitterAuthClient;
    CallbackManager callbackManager;
    // Social Login
    String socialName = "";
    String socialEmail = "";
    String socialPhotoUrl = "";
    LoginPresenter mPresenter;
    String selectedLanguage = "";

    boolean isGuestTry = false;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient googleApiClient;
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    (object, response) -> {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        isGuestTry = getIntent().getBooleanExtra("GuestTry", false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPresenter = new LoginPresenterImpl(this, new LoginDataInteractor());

        // Social Login
        mTwitterAuthClient = new TwitterAuthClient();
//      setGlobals();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, mFacebookCallback);
        //Social Login
        initializeGoogle();
//      getInvoice();

        if (GlobalValues.getAppLanguage(this).equals("ar")) {
            languageTextView.setText(getString(R.string.arabic));
        } else {
            languageTextView.setText(getString(R.string.english));
        }
    }
    @OnClick(R.id.layout_language)
    public void languageSelection() {
        selectLanguage();
    }
    private void setGlobals() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

//        user = (User) ConstantMethod.getPreferenceObjectJson(this, ConstantMethod.PREF_USER_OBJECT, User.class);
//
//        if (user != null && user.getGoogleId() != null && user.getGoogleId().length() > 0) {
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestEmail()
//                    .build();
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
//            mGoogleApiClient.connect();
//        }
    }

    private void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestServerAuthCode("160419179473-f7364ji4t7b3aj9ptlcc676tihots5la.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_signUp)
    public void buttonSignUp() {
        Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        mainIntent.putExtra("GuestTry", isGuestTry);
        startActivity(mainIntent);
        if (isGuestTry) {
            finish();
        }
    }

    @OnClick(R.id.tv_forgotPassword)
    public void forgotPassword() {
        Intent mainIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(mainIntent);
    }

    @OnClick(R.id.button_guest_user)
    public void guestLogin() {
        User user = new User();
        Random rand = new Random();
        int n = rand.nextInt(50000) + 1;
        user.setId(0);
        user.setFirstName("Guest");
        user.setLastName("User");
        user.setEmail("guest" + n + "@saman.com");
        GlobalValues.saveUser(LoginActivity.this, user);
        GlobalValues.setGuestLoginStatus(LoginActivity.this, true);
        Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @OnClick(R.id.button_login)
    public void buttonLogin() {
        String userName = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (isDataValid(userName, password)) {
            mPresenter.loginUser(userName, password, GlobalValues.getUserToken(LoginActivity.this));
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



    @OnClick(R.id.iv_email_cross)
    public void emailCross() {
        emailEditText.setText("");
    }

    @OnClick(R.id.iv_password_visible)
    public void setPasswordVisibility() {
        if (!isShowing) {
            isShowing = true;
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setTransformationMethod(null);
            passwordVisibilityImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view));
        } else {
            isShowing = false;
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
        if (isGuestTry) {
            GlobalValues.saveUser(LoginActivity.this, user);
            GlobalValues.setUserLoginStatus(LoginActivity.this, true);
            GlobalValues.setGuestLoginStatus(LoginActivity.this, false);
            finish();
        } else {
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
        Constants.showAlert(getString(R.string.login_failed), message, getString(R.string.try_again), LoginActivity.this);
    }

    //Social Login
    // Google Plus Region

    private boolean isDataValid(String userName, String password) {
        if (TextUtils.isEmpty(userName)) {
            Constants.showAlert(getString(R.string.Login), getString(R.string.email_required), getString(R.string.okay), LoginActivity.this);
            return false;
        }
//        else if (!isValidEmailId(userName)) {
//            Constants.showAlert(getString(R.string.sign_up), getString(R.string.email_invalid), getString(R.string.okay), RegisterActivity.this);
//            return false;
//        }
        else if (TextUtils.isEmpty(password)) {
            Constants.showAlert(getString(R.string.Login), getString(R.string.password_required), getString(R.string.okay), LoginActivity.this);
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.google_signIn)
    public void googleSignIn() {
//      Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Intent signInIntent = googleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();

        setGlobals();
    /*    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
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
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            socialName = acct.getDisplayName();

            if (acct.getPhotoUrl() != null) {
                socialPhotoUrl = acct.getPhotoUrl().toString();
            }
            socialEmail = acct.getEmail();

            socialLogin(1);
//          Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//          startActivity(intent);
        }
    }

    // Google Plus Region End

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            socialName = acct.getDisplayName();
            if (acct != null) {
                if (acct.getPhotoUrl() != null) {
                    socialPhotoUrl = acct.getPhotoUrl().toString();
                }
            }
            socialEmail = acct.getEmail();
            socialLogin(1);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    //Twitter Region
    @OnClick(R.id.twitter_signIn)
    public void twitter() {
        mTwitterAuthClient.authorize(LoginActivity.this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
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
                //_normal (48x48px) | _bigger (73x73px) | _mini (24x24px)
                socialPhotoUrl = userResult.data.profileImageUrl;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");
                if (tryAgain) {
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
        apiClient.socialLogin(names[0], lastName, socialEmail, GlobalValues.getUserToken(LoginActivity.this), socialPhotoUrl, new retrofit2.Callback<UserResponse>() {
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
                                GlobalValues.saveUser(LoginActivity.this, user);
                                GlobalValues.setUserLoginStatus(LoginActivity.this, true);
                                GlobalValues.setGuestLoginStatus(LoginActivity.this, false);
                                finish();
                            } else {
                                user.setSocialID(socialID);
                                GlobalValues.saveUser(LoginActivity.this, user);
                                GlobalValues.setUserLoginStatus(LoginActivity.this, true);
                                GlobalValues.setGuestLoginStatus(LoginActivity.this, false);
                                Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                if (user.getPhoneNumber() == null || user.getShippingAddress().getAddressLine1() == null || user.getShippingAddress().getState() == null || user.getShippingAddress().getFloor() == null || user.getCountry() == null) {
                                    mainIntent.putExtra("NavItem", 0);
                                    mainIntent.putExtra("OpenDetails", true);
                                } else if (user.getPhoneNumber().isEmpty() || user.getShippingAddress().getAddressLine1().isEmpty() || user.getShippingAddress().getState().isEmpty() || user.getShippingAddress().getFloor().isEmpty() || user.getCountry().isEmpty()
                                        || user.getPhoneNumber().equalsIgnoreCase("") || user.getShippingAddress().getAddressLine1().equalsIgnoreCase("") || user.getCountry().equalsIgnoreCase("")) {
                                    mainIntent.putExtra("NavItem", 0);
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

//    private void getInvoice() {
//        OmanNetServiceHandler.instance.invoice(new retrofit2.Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    JSONObject JObject = new JSONObject(response.body().string());
//                    Log.e("Invoice", JObject.toString());
//                    if (JObject != null) {
//                        JSONObject result = JObject.getJSONObject("result");
//                        if (result != null) {
//                            if (result.has("status")) {
//                                if (result.getString("status").equalsIgnoreCase("success")) {
//                                    JSONObject message = result.getJSONObject("message");
//                                    if (message != null) {
//                                        if (message.has("PayUrl")) {
//                                            String payURL = message.getString("PayUrl");
//                                            if (payURL != null && !payURL.equals("")) {
//                                                Intent intent = new Intent(LoginActivity.this, OmanNetCardDetailActivity.class);
//                                                intent.putExtra("PayURL", payURL);
//                                                startActivity(intent);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//            }
//        });
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.stopAutoManage(LoginActivity.this);
            mGoogleApiClient.disconnect();
        }
    }

    private void selectLanguage() {
        dialog = new Dialog(LoginActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_language_selection);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView done =  dialog.findViewById(R.id.tv_done);

        RadioButton rbEnglish = dialog.findViewById(R.id.radio_english);
        RadioButton rbArabic = dialog.findViewById(R.id.radio_arabic);

        if (SamanApp.isEnglishVersion) {
            rbEnglish.setChecked(true);
            rbArabic.setChecked(false);
        } else {
            rbArabic.setChecked(true);
            rbEnglish.setChecked(false);
        }

        Log.e("isEnglishVersion", "selectLanguage: " + SamanApp.isEnglishVersion);

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

                    changeLanguage(radioButton);
//                    Constants.showAlert(getString(R.string.title_settings), getString(R.string.app_language), getString(R.string.okay), SettingsActivity.this);
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }
    private void changeLanguage(RadioButton radioButton) {

        WebServicesHandler apiClient = WebServicesHandler.instance;
        int type = 1;
        if (radioButton.getId() == R.id.radio_arabic) {
            type = 2;
        }
        User user = GlobalValues.getUser(this);
        Constants.showSpinner(getString(R.string.language), this);
        apiClient.getChangeLanguage(String.valueOf(58), type, new retrofit2.Callback<ChangeLanguage>() {
            @Override
            public void onResponse(Call<ChangeLanguage> call, Response<ChangeLanguage> response) {
                Constants.dismissSpinner();
                ChangeLanguage changeLanguage = response.body();
                if (changeLanguage != null && changeLanguage.result) {
                    showAlertLanguage(getString(R.string.title_settings), getString(R.string.app_language), getString(R.string.okay), LoginActivity.this);
                    if (radioButton.getId() == R.id.radio_arabic) {

                        GlobalValues.setAppLanguage(getApplicationContext(), "ar");
                        SamanApp.isEnglishVersion = false;
                    } else {

                        GlobalValues.setAppLanguage(getApplicationContext(), "en");
                        SamanApp.isEnglishVersion = true;
                    }
                    selectedLanguage = radioButton.getText().toString();
                    languageTextView.setText(radioButton.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Please try again...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangeLanguage> call, Throwable t) {
                Constants.dismissSpinner();
                Toast.makeText(LoginActivity.this, "Please try again...!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showAlertLanguage(String title, String message, String buttonText, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        TextView textView = new TextView(this);

        Typeface face = Typeface.createFromAsset(getAssets(), "font/neo_sans.ttf");
        textView.setTypeface(face);
        textView.setText(title);
        if (SamanApp.isEnglishVersion) {
            textView.setPadding(20, 10, 0, 0);
        } else {
            textView.setPadding(0, 10, 20, 0);
        }
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        alertDialog.setCustomTitle(textView);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent refresh = new Intent(context, SplashActivity.class);

                        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //refresh.putExtra(currentLang, localeName);
                        startActivity(refresh);
                        finishAffinity();
                    }
                });
        alertDialog.show();
    }

}
