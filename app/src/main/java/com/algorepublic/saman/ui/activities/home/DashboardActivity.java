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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.login.LoginActivity;
import com.algorepublic.saman.ui.activities.search.SearchActivity;
import com.algorepublic.saman.ui.activities.settings.SettingsActivity;
import com.algorepublic.saman.ui.fragments.bag.BagFragment;
import com.algorepublic.saman.ui.fragments.favourite.FavoritesFragment;
import com.algorepublic.saman.ui.fragments.home.HomeFragment;
import com.algorepublic.saman.ui.fragments.store.StoreFragment;
import com.algorepublic.saman.ui.fragments.useraccount.MyAccountFragment;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity implements DashboardContractor.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_logo)
    ImageView homeLogo;
    @BindView(R.id.toolbar_settings)
    ImageView settings;
    @BindView(R.id.toolbar_search)
    ImageView search;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtName, txtWebsite;
    public static boolean isAppRunning;
    String title = "Saman";

    User authenticatedUser;
    private boolean doubleBackToExitPressedOnce = false;

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private Handler mHandler;

    DashboardPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);
        ButterKnife.bind(this);
        authenticatedUser = GlobalValues.getUser(this);
        search.setVisibility(View.GONE);
        GlobalValues.storeCategories = new ArrayList<>();
        navItemIndex=getIntent().getIntExtra("NavItem",0);
        mHandler = new Handler();
        mPresenter = new DashboardPresenter(this);
        mPresenter.getUserData();
    }

    @Override
    protected void onResume() {
        if (GlobalValues.orderPlaced) {
            onNavigationItemSelected(navigationView.getMenu().getItem(1));
            GlobalValues.orderPlaced = false;
        }
        updateBagCount();
        super.onResume();
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

    @OnClick(R.id.toolbar_search)
    void search() {
        if (navItemIndex == 1 || navItemIndex == 0) {
            Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
            startActivity(intent);
        }

        if (navItemIndex == 2) {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    @OnClick(R.id.toolbar_settings)
    void settingButton() {
        if (navItemIndex == 4) {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else {
            show_logout_dialog();
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
                Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(intent);
                navItemIndex = -1;
                break;
            case R.id.nav_logout:
                show_logout_dialog();
                navItemIndex = -1;
                break;
        }
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();
        if (navItemIndex == 4) {
            settings.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
        } else if (navItemIndex == 1 || navItemIndex == 2 || navItemIndex == 0) {
            search.setVisibility(View.VISIBLE);
            settings.setVisibility(View.GONE);
            if (navItemIndex == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    search.setImageDrawable(getDrawable(R.drawable.ic_cross));
                } else {
                    search.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    search.setImageDrawable(getDrawable(R.drawable.ic_search_image));
                } else {
                    search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_image));
                }
            }
        } else {
            search.setVisibility(View.GONE);
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
//        actionbar.setHomeButtonEnabled(true);
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
        onNavigationItemSelected(navigationView.getMenu().getItem(navItemIndex));

        loadFragment();
    }

    @Override
    public void updateUserDetails() {
        User authenticatedUser = GlobalValues.getUser(DashboardActivity.this);
        txtName.setText(authenticatedUser.getFirstName() + " " + authenticatedUser.getLastName());
        txtWebsite.setText(authenticatedUser.getEmail());
        if (authenticatedUser.getProfileImagePath() != null) {
            Picasso.get()
                    .load(Constants.URLS.BaseURLImages+authenticatedUser.getProfileImagePath())
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
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left);
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
                title = getString(R.string.app_name);
                break;
            case 1:
                fragment = new StoreFragment();
                title = getString(R.string.title_store);
                break;
            case 2:
                fragment = new FavoritesFragment();
                title = getString(R.string.title_fav);
                break;
            case 3:
                fragment = new BagFragment();
                title = getString(R.string.shopping_cart);
                break;
            case 4:
                fragment = new MyAccountFragment();
                title = getString(R.string.title_my_account);
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

        if(navItemIndex!=0){
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            return;
        }

        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_again), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setToolbarTitle() {
        if (navItemIndex == 0) {
            toolbarTitle.setVisibility(View.GONE);
            homeLogo.setVisibility(View.VISIBLE);
        } else {
            toolbarTitle.setVisibility(View.VISIBLE);
            homeLogo.setVisibility(View.GONE);
        }
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
                SamanApp.db.putString(Constants.CARD_LIST, "");
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

    public void callFavNav() {
        onNavigationItemSelected(navigationView.getMenu().getItem(2));
    }

    public void callStoreNav() {
        onNavigationItemSelected(navigationView.getMenu().getItem(1));
    }

    public void updateBagCount() {
        if(SamanApp.localDB.getCartAllProductsCount()>0) {
            // showing dot next to notifications label
            MenuItem element = navigationView.getMenu().findItem(R.id.nav_bag);
            String before = getString(R.string.shopping_cart);
            String counter = Integer.toString(SamanApp.localDB.getCartAllProductsCount());
            String s = before + "   " + counter + " ";
            SpannableString sColored = new SpannableString(s);

            sColored.setSpan(new BackgroundColorSpan(Color.GRAY), s.length() - (counter.length() + 2), s.length(), 0);
            sColored.setSpan(new ForegroundColorSpan(Color.WHITE), s.length() - (counter.length() + 2), s.length(), 0);

            element.setTitle(sColored);
        }else {
            MenuItem element = navigationView.getMenu().findItem(R.id.nav_bag);
            String before = getString(R.string.shopping_cart);
            element.setTitle(before);
        }
    }

    public void updateFavCount(int count) {
        if(count>0) {
            // showing dot next to notifications label
            MenuItem element = navigationView.getMenu().findItem(R.id.nav_favorite);
            String before = getString(R.string.title_fav);
            String counter = Integer.toString(count);
            String s = before + "   " + counter + " ";
            SpannableString sColored = new SpannableString(s);
            sColored.setSpan(new BackgroundColorSpan(Color.GRAY), s.length() - (counter.length() + 2), s.length(), 0);
            sColored.setSpan(new ForegroundColorSpan(Color.WHITE), s.length() - (counter.length() + 2), s.length(), 0);

            element.setTitle(sColored);
        }else {
            MenuItem element = navigationView.getMenu().findItem(R.id.nav_favorite);
            String before = getString(R.string.title_fav);
            element.setTitle(before);
        }
    }


}
