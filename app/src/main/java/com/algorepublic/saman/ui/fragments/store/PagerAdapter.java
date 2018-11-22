package com.algorepublic.saman.ui.fragments.store;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.algorepublic.saman.ui.fragments.store.Tab.Tabs;


public class PagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tabs tab1 = new Tabs();
                return tab1;
            case 1:
                Tabs tab2 = new Tabs();
                return tab2;
            case 2:
                Tabs tab3 = new Tabs();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
