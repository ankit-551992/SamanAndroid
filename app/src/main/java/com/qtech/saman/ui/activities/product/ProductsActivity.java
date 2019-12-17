package com.qtech.saman.ui.activities.product;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.StoreCategory;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetCategoriesList;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.search.SearchActivity;
import com.qtech.saman.ui.fragments.product.ProductsCategoryFragment;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.LockableViewPager;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.qtech.saman.utils.GlobalValues.CATEGORYID;
import static com.qtech.saman.utils.GlobalValues.FLAG_SEARCH_PRODUCT;

public class ProductsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_search)
    ImageView toolbarSearch;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    LockableViewPager viewPager;
    @BindView(R.id.search)
    RelativeLayout rl_search;
    @BindView(R.id.search_product)
    EditText search_product;
    String search = "";

    ViewPagerAdapter adapter;
    int categoryID, bannerID, categoryBannerID;
    boolean isBannerProduct, isCategoryProduct;
    public static List<StoreCategory> banner_storeCategories;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
//
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("BannerID")) {
            bannerID = bundle.getInt("BannerID");
        }
        if (getIntent().hasExtra("IsBannerProduct")) {
            isBannerProduct = bundle.getBoolean("IsBannerProduct");
        }
        if (getIntent().hasExtra("CategoryBannerID")) {
            categoryBannerID = bundle.getInt("CategoryBannerID");
        }
        if (getIntent().hasExtra("IsCategoryProduct")) {
            isCategoryProduct = bundle.getBoolean("IsCategoryProduct");
        }

        if (getIntent().hasExtra("CategoryID")) {
            categoryID = bundle.getInt("CategoryID");
//          categoryID = 4;
            ProductsCategoryFragment.newInstance(categoryID, "", false);
        }

        User user = GlobalValues.getUser(ProductsActivity.this);
        String userId = String.valueOf(user.getId());
        if (isCategoryProduct) {
            getProductCategory(categoryBannerID, userId, 0, 30);
        } else {
            tab();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(getString(R.string.products));
        toolbarBack.setVisibility(View.VISIBLE);
        toolbarSearch.setVisibility(View.VISIBLE);
        rl_search.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
    }

    private void getProductCategory(int bannerID, String userId, int pageIndex, int pageSize) {

        WebServicesHandler.instance.getBannerProductCategory(bannerID, userId, pageIndex, pageSize, new retrofit2.Callback<GetCategoriesList>() {
            @Override
            public void onResponse(Call<GetCategoriesList> call, Response<GetCategoriesList> response) {

                GetCategoriesList getCategoriesList = response.body();
                if (getCategoriesList != null) {
                    if (getCategoriesList.getSuccess() == 1) {
                        if (getCategoriesList.getCategories() != null) {
                            banner_storeCategories = getCategoriesList.getCategories();
                            tab();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCategoriesList> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        FLAG_SEARCH_PRODUCT = false;
        search = "";
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_search)
    void search() {
        Intent intent = new Intent(ProductsActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.search_product)
    void searchCategotyProduct() {
        searchTextListner();
    }

    private void searchTextListner() {
        search_product.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (search_product.getText() != null && !search_product.getText().toString().isEmpty() &&
                            search_product.getText().length() > 0) {
                        tabLayout.setVisibility(View.VISIBLE);
                        FLAG_SEARCH_PRODUCT = true;
                        search = search_product.getText().toString();
                        tab();
                        adapter.notifyDataSetChanged();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        return true;
                    } else {
                        FLAG_SEARCH_PRODUCT = false;
                        tabLayout.setVisibility(View.VISIBLE);
                        search = "";
                        tab();
                        adapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
    }

    public void tab() {
        setupViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager, false);
        setUpCustomTabs();
        viewPager.beginFakeDrag();
        viewPager.setSwipeable(false);
    }

    private void setUpCustomTabs() {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View customTab = (View) LayoutInflater.from(this).inflate(R.layout.tab_custom_view, null);//get custom view
            TextView textView = (TextView) customTab.findViewById(R.id.tv_tab);
            ImageView imageView = (ImageView) customTab.findViewById(R.id.iv_tab);
            LinearLayout bg = (LinearLayout) customTab.findViewById(R.id.tab_layout);

            if (isCategoryProduct) {
                isCategoryProduct = false;
                if (SamanApp.isEnglishVersion) {
                    textView.setText(banner_storeCategories.get(i).getTitle());
                } else {
                    textView.setText(banner_storeCategories.get(i).getTitleAR());
                }
                String url = Constants.URLS.BaseURLImages + banner_storeCategories.get(i).getLogoURL();
                Picasso.get().load(url).into(imageView);
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(customTab);//set custom view
            } else {
                if (i == 0) {
                    if (SamanApp.isEnglishVersion) {
                        textView.setText(getString(R.string.all));
                    } else {
                        textView.setText(getString(R.string.all));
                    }
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_app_logo));

                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                    if (tab != null)
                        tab.setCustomView(customTab);//set custom view
                } else if (i == 1) {
                    textView.setText(getString(R.string.new_in));
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_app_logo));

                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    if (tab != null)
                        tab.setCustomView(customTab);//set custom view
                } else {
                    if (SamanApp.isEnglishVersion) {
                        textView.setText(GlobalValues.storeCategories.get(i - 2).getTitle());
                    } else {
                        textView.setText(GlobalValues.storeCategories.get(i - 2).getTitleAR());
                    }
                    String url = Constants.URLS.BaseURLImages + GlobalValues.storeCategories.get(i - 2).getLogoURL();
                    Picasso.get().load(url).into(imageView);
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null)
                        tab.setCustomView(customTab);//set custom view
                }
            }
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (FLAG_SEARCH_PRODUCT) {
            adapter.addFrag(ProductsCategoryFragment.newInstance(CATEGORYID, search, false), getString(R.string.all));

            for (int i = 0; i < GlobalValues.storeCategories.size(); i++) {
                adapter.addFrag(
                        ProductsCategoryFragment.newInstance(GlobalValues.storeCategories.get(i).getID(), "", false),
                        GlobalValues.storeCategories.get(i).getTitle());
            }
        } else if (isBannerProduct) {
            isBannerProduct = false;
            tabLayout.setVisibility(View.GONE);
            adapter.addFrag(ProductsCategoryFragment.newInstance(bannerID, "", true), getString(R.string.all));
        } else if (isCategoryProduct) {
            for (int i = 0; i < banner_storeCategories.size(); i++) {
                adapter.addFrag(
                        ProductsCategoryFragment.newInstance(banner_storeCategories.get(i).getID(), "", false),
                        banner_storeCategories.get(i).getTitle());
            }
        } else {
//          adapter.addFrag(AllProductsFragment.newInstance(false), getString(R.string.all));
            adapter.addFrag(ProductsCategoryFragment.newInstance(0, "", false), getString(R.string.all));
            adapter.addFrag(ProductsCategoryFragment.newInstance(1, "", false), getString(R.string.new_in));
            for (int i = 0; i < GlobalValues.storeCategories.size(); i++) {
                adapter.addFrag(
                        ProductsCategoryFragment.newInstance(GlobalValues.storeCategories.get(i).getID(), "", false),
                        GlobalValues.storeCategories.get(i).getTitle());
            }
        }
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mfragmentlist = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mfragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return mfragmentlist.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mfragmentlist.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }
}
