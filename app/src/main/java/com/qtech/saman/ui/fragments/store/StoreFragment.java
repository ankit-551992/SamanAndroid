package com.qtech.saman.ui.fragments.store;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseFragment;
import com.qtech.saman.ui.fragments.store.Tab.AllStores;
import com.qtech.saman.ui.fragments.store.Tab.Tabs;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.qtech.saman.utils.GlobalValues.FLAG_SEARCH;


public class StoreFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    LockableViewPager viewPager;
    ViewPagerAdapter adapter;
    @BindView(R.id.search_store)
    EditText search_store;
    String search = "";
    //  @BindView(R.id.toolbar_search)
//  ImageView toolbar_search;
    int bannerStoreID;
    boolean banner_store;

//    public static StoreFragment newInstance() {
//
//        StoreFragment fragment = new StoreFragment();
//        return fragment;
//    }

    public static StoreFragment newInstance(Integer getiD, boolean b) {
        Bundle bundle = new Bundle();
        StoreFragment fragment = new StoreFragment();
        bundle.putInt("BannerID", getiD);
        bundle.putBoolean("BannerStore", b);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            bannerStoreID = bundle.getInt("BannerID", 0);
            banner_store = bundle.getBoolean("BannerStore", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        GlobalValues.isFromHome = false;
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        tab();
        return view;
    }

    private void searchTextListner() {
        search_store.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (search_store.getText() != null && !search_store.getText().toString().isEmpty() &&
                        search_store.getText().length() > 0) {
                    tabLayout.setVisibility(View.GONE);
                    FLAG_SEARCH = true;
                    search = search_store.getText().toString();
                    tab();
                    adapter.notifyDataSetChanged();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    return true;
                } else {
                    FLAG_SEARCH = false;
                    tabLayout.setVisibility(View.VISIBLE);
                    tab();
                    adapter.notifyDataSetChanged();
                }
            }
            return false;
        });
    }


    @OnClick(R.id.search_store)
    void search() {
        searchTextListner();
    }

    public void tab() {
        setupViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
            View customTab = LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom_view, null, false);//get custom view
            TextView textView =  customTab.findViewById(R.id.tv_tab);
            ImageView imageView = customTab.findViewById(R.id.iv_tab);
            LinearLayout bg = customTab.findViewById(R.id.tab_layout);

            if (i == 0) {
                textView.setText(getString(R.string.all));
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_app_logo));

                TabLayout.Tab tab = tabLayout.getTabAt(0);
                if (tab != null)
                    tab.setCustomView(customTab); //set custom view
            } else {
                if (SamanApp.isEnglishVersion) {
                    textView.setText(GlobalValues.storeCategories.get(i - 1).getTitle());
                } else {
                    textView.setText(GlobalValues.storeCategories.get(i - 1).getTitleAR());
                }
                String url = Constants.URLS.BaseURLImages + GlobalValues.storeCategories.get(i - 1).getLogoURL();
                Picasso.get().load(url).into(imageView);
            }

//            switch (i){
//                case 0:
//                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_settings_white));
//                    break;
//                case 1:
//                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_logo));
//                    break;
//                case 2:
//                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_logo));
//                    break;
//                case 3:
//                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_settings_white));
//                    break;
//                case 4:
//                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_logo));
//                    break;
//            }
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setCustomView(customTab); //set custom view
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        if (FLAG_SEARCH) {
//          adapter.addFrag(new AllStores(), "");
//          adapter.addFrag(AllStores.newInstance(search), "All");
            adapter.addFrag(AllStores.newInstance(search, 0, false), getString(R.string.all));
        } else if (banner_store) {
            tabLayout.setVisibility(View.GONE);
            adapter.addFrag(AllStores.newInstance("", bannerStoreID, true), getString(R.string.all));
        } else {
            adapter.addFrag(new AllStores(), getString(R.string.all));
            for (int i = 0; i < GlobalValues.storeCategories.size(); i++) {
                adapter.addFrag(
                        Tabs.newInstance(GlobalValues.storeCategories.get(i).getID()),
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

    @Override
    public String getName() {
        return null;
    }
}
