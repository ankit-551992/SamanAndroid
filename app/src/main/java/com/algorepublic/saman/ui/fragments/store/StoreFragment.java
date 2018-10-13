package com.algorepublic.saman.ui.fragments.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.ui.activities.search.SearchActivity;
import com.algorepublic.saman.ui.fragments.store.Tab.TabFragment;
import com.algorepublic.saman.ui.fragments.store.Tab.Tabs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;

    ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.bind(this, view);
        tab();
        return view;
    }

    @OnClick(R.id.search)
    void search(){
        Intent intent=new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    public void tab() {
        setupViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        setUpCustomTabs();
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.beginFakeDrag();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab != null) {
                    View customView = tab.getCustomView();
                    if (customView != null) {
                        LinearLayout bg = (LinearLayout)customView.findViewById(R.id.tab_layout);
                        bg.setBackground(getActivity().getResources().getDrawable(R.drawable.tab_selector));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab != null) {
                    View customView = tab.getCustomView();
                    if (customView != null) {
                        LinearLayout bg = (LinearLayout)customView.findViewById(R.id.tab_layout);
                        bg.setBackground(null);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_facebook);
        tabLayout.getTabAt(1).setCustomView(R.layout.tab_custom_view);
        tabLayout.getTabAt(2).setCustomView(R.layout.tab_custom_view);
        tabLayout.getTabAt(3).setCustomView(R.layout.tab_custom_view);
        tabLayout.getTabAt(4).setCustomView(R.layout.tab_custom_view);
    }

    private void setUpCustomTabs() {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View customTab = (View) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom_view, null);//get custom view
            TextView textView=(TextView)customTab.findViewById(R.id.tv_tab);
            ImageView imageView=(ImageView) customTab.findViewById(R.id.iv_tab);
            LinearLayout bg = (LinearLayout)customTab.findViewById(R.id.tab_layout);
            textView.setText(adapter.mFragmentTitleList.get(i));
            if(i==0){
                bg.setBackground(getActivity().getResources().getDrawable(R.drawable.tab_selector));
            }

            switch (i){
                case 0:
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_settings_white));
                    break;
                case 1:
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_logo));
                    break;
                case 2:
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_logo));
                    break;
                case 3:
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_settings_white));
                    break;
                case 4:
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_logo));
                    break;
            }

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setCustomView(customTab);//set custom view
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new TabFragment(), "Home");
        adapter.addFrag(new Tabs(), "Biz Forum");
        adapter.addFrag(new TabFragment(), "Network");
        adapter.addFrag(new TabFragment(), "Messages");
        adapter.addFrag(new TabFragment(), "Offer");
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
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
