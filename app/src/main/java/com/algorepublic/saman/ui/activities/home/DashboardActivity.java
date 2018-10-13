package com.algorepublic.saman.ui.activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.login.LoginActivity;
import com.algorepublic.saman.ui.activities.settings.SettingsActivity;
import com.algorepublic.saman.ui.fragments.bag.BagFragment;
import com.algorepublic.saman.ui.fragments.favourite.FavoritesFragment;
import com.algorepublic.saman.ui.fragments.home.HomeFragment;
import com.algorepublic.saman.ui.fragments.store.StoreFragment;
import com.algorepublic.saman.ui.fragments.useraccount.MyAccountFragment;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity implements DashboardContractor.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_settings)
    ImageView settings;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtName, txtWebsite;
    public static boolean isAppRunning;
    String title="Home";

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private Handler mHandler;

    DashboardPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);
        ButterKnife.bind(this);
        mHandler = new Handler();
        mPresenter = new DashboardPresenter(this);
        mPresenter.getUserData();
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.toolbar_settings)
    void settingButton(){
        if(navItemIndex==4){
            Intent intent=new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                navItemIndex = 0;
                break;
            case R.id.nav_store:
                navItemIndex = 1;
                break;
            case R.id.nav_favorite:
                navItemIndex = 2;
                break;
            case R.id.nav_bag:
                navItemIndex = 3;
                break;
            case R.id.nav_my_account:
                navItemIndex = 4;
                break;
            case R.id.nav_settings:
                Intent intent=new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(intent);
                navItemIndex = -1;
                break;
            case R.id.nav_logout:
                show_logout_dialog();
                navItemIndex = -1;
                break;
            default:
                navItemIndex = 0;
        }
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();
        if(navItemIndex==4){
            settings.setVisibility(View.VISIBLE);
        }else {
            settings.setVisibility(View.GONE);
        }
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        if (navItemIndex != -1) {
            //Checking if the item is in checked state or not, if not make it in checked state
            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            // set item as selected to persist highlight
            item.setChecked(true);
            loadFragment();
        }
        return true;
    }

    @Override
    public void setupNavigationDrawer() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_cross);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.email);
        imgProfile = (ImageView) navHeader.findViewById(R.id.iv_profile);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        // showing dot next to notifications label
        MenuItem element = navigationView.getMenu().findItem(R.id.nav_bag);
        String before = element.getTitle().toString();
        String counter = Integer.toString(5);
        String s = before + "   " + counter + " ";
        SpannableString sColored = new SpannableString(s);

        sColored.setSpan(new BackgroundColorSpan(Color.YELLOW), s.length() - (counter.length() + 2), s.length(), 0);
        sColored.setSpan(new ForegroundColorSpan(Color.WHITE), s.length() - (counter.length() + 2), s.length(), 0);


        element.setTitle(sColored);

        navItemIndex = 0;
        loadFragment();
    }

    @Override
    public void updateUserDetails() {
        User authenticatedUser = GlobalValues.getUser(DashboardActivity.this);
        txtName.setText(authenticatedUser.getFirstName() + " " + authenticatedUser.getLastName());
        txtWebsite.setText(authenticatedUser.getEmail());
        if (authenticatedUser.getProfileImagePath() != null) {
            Picasso.get()
                    .load(authenticatedUser.getProfileImagePath())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.ic_profile)
                    .into(imgProfile);
        } else {
            Picasso.get()
                    .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.ic_profile)
                    .into(imgProfile);
        }
    }

    private void loadFragment() {
//        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        setToolbarTitle();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getSelectedFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        mDrawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getSelectedFragment() {
        Fragment fragment;
        switch (navItemIndex) {
            case 0:
                fragment = new HomeFragment();
                title=getString(R.string.title_home);
                break;
            case 1:
                fragment = new StoreFragment();
                title=getString(R.string.title_store);
                break;
            case 2:
                fragment = new FavoritesFragment();
                title=getString(R.string.title_favorite);
                break;
            case 3:
                fragment = new BagFragment();
                title=getString(R.string.title_bag);
                break;
            case 4:
                fragment = new MyAccountFragment();
                title=getString(R.string.title_my_account);
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        setToolbarTitle();
        return fragment;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        finish();
//        super.onBackPressed();
    }

    private void setToolbarTitle() {
        toolbarTitle.setText(title);
    }

    private void show_logout_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.logout_message));
        builder.setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                presenter.logoutUser();
                GlobalValues.setUserLoginStatus(DashboardActivity.this, false);
                Intent mainIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
