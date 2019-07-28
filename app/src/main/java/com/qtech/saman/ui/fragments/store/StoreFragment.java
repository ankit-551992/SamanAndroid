package com.qtech.saman.ui.fragments.store;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class StoreFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    LockableViewPager viewPager;

    ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.bind(this, view);
        tab();
        return view;
    }

//    @OnClick(R.id.search)
//    void search(){
//        Intent intent=new Intent(getContext(), SearchActivity.class);
//        intent.putExtra("Function",0); //0 for Search Products
//        startActivity(intent);
//    }

    public void tab() {
        setupViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        setUpCustomTabs();
        viewPager.beginFakeDrag();
        viewPager.setSwipeable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            }
        }, 200);
    }



    private void setUpCustomTabs() {

        for (int i = 1; i < tabLayout.getTabCount(); i++) {
            View customTab = (View) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom_view, null);//get custom view
            TextView textView = (TextView) customTab.findViewById(R.id.tv_tab);
            ImageView imageView = (ImageView) customTab.findViewById(R.id.iv_tab);
            LinearLayout bg = (LinearLayout) customTab.findViewById(R.id.tab_layout);

            if (SamanApp.isEnglishVersion) {
                textView.setText(GlobalValues.storeCategories.get(i - 1).getTitle());
            } else {
                textView.setText(GlobalValues.storeCategories.get(i - 1).getTitleAR());
            }


            String url = Constants.URLS.BaseURLImages + GlobalValues.storeCategories.get(i - 1).getLogoURL();
            Picasso.get().load(url).into(imageView);

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
                tab.setCustomView(customTab);//set custom view
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
//        adapter.addFrag(new TabFragment(), "Home");
//        adapter.addFrag(new Tabs(), "Biz Forum");
//        adapter.addFrag(new TabFragment(), "Network");
//        adapter.addFrag(new TabFragment(), "Messages");
//        adapter.addFrag(new TabFragment(), "Offer");

        adapter.addFrag(new AllStores(), "");
        for (int i = 0; i < GlobalValues.storeCategories.size(); i++) {
            adapter.addFrag(
                    Tabs.newInstance(GlobalValues.storeCategories.get(i).getID()),
                    GlobalValues.storeCategories.get(i).getTitle());
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
