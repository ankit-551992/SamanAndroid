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
import android.util.Log;
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
import com.qtech.saman.listeners.DialogOnClick;
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
    int categoryID;
    DialogOnClick dialogOnClick;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
//
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("CategoryID")) {
            categoryID = bundle.getInt("CategoryID");
//            categoryID = 4;
            ProductsCategoryFragment.newInstance(categoryID, "");
        }
        tab();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(getString(R.string.products));
        toolbarBack.setVisibility(View.VISIBLE);
        toolbarSearch.setVisibility(View.GONE);
        rl_search.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        FLAG_SEARCH_PRODUCT = false;
        tabLayout.setVisibility(View.VISIBLE);
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
                        tabLayout.setVisibility(View.GONE);
                        FLAG_SEARCH_PRODUCT = true;
                        search = search_product.getText().toString();
                        tab();
                        adapter.notifyDataSetChanged();
                        Log.e("SEARCH000", "--search--onTextChanged---store---" + search);
//                        if (dialogOnClick != null) {
//                            dialogOnClick.searchProduct(true, search);
//                        }
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        return true;
                    } else {
                        FLAG_SEARCH_PRODUCT = false;
                        tabLayout.setVisibility(View.VISIBLE);
                        search = "";
                        tab();
//                        if (dialogOnClick != null) {
//                            dialogOnClick.searchProduct(false, "");
//                        }
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
//        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager, false);
        setUpCustomTabs();
        viewPager.beginFakeDrag();
        viewPager.setSwipeable(false);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
//            }
//        }, 200);
    }

    private void setUpCustomTabs() {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View customTab = (View) LayoutInflater.from(this).inflate(R.layout.tab_custom_view, null);//get custom view
            TextView textView = (TextView) customTab.findViewById(R.id.tv_tab);
            ImageView imageView = (ImageView) customTab.findViewById(R.id.iv_tab);
            LinearLayout bg = (LinearLayout) customTab.findViewById(R.id.tab_layout);

            if (i == 0) {
                textView.setText(getString(R.string.all));
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

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (FLAG_SEARCH_PRODUCT) {
            adapter.addFrag(ProductsCategoryFragment.newInstance(CATEGORYID, search), getString(R.string.all));
        } else {
//           adapter.addFrag(AllProductsFragment.newInstance(false), getString(R.string.all));
            adapter.addFrag(ProductsCategoryFragment.newInstance(0, ""), getString(R.string.all));
            adapter.addFrag(ProductsCategoryFragment.newInstance(1, ""), getString(R.string.new_in));
            for (int i = 0; i < GlobalValues.storeCategories.size(); i++) {
                adapter.addFrag(
                        ProductsCategoryFragment.newInstance(GlobalValues.storeCategories.get(i).getID(), ""),
                        GlobalValues.storeCategories.get(i).getTitle());
                Log.e("SEARCH000", "---GlobalValue---getID()---" + GlobalValues.storeCategories.get(i).getID());
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
