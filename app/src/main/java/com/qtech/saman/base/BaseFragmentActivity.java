package com.qtech.saman.base;

import android.support.v4.app.FragmentTransaction;

import com.qtech.saman.R;
import com.qtech.saman.listeners.FragmentChangeHandler;
import com.qtech.saman.ui.fragments.home.HomeFragment;
import com.qtech.saman.ui.fragments.store.StoreFragment;
import com.qtech.saman.utils.Constants;


public abstract class BaseFragmentActivity extends BaseActivity implements FragmentChangeHandler {

    protected BaseFragment currentFragment;

    //Fragment handling
    @Override
    public void changeFragment(Constants.Fragment fragment) {
        switch (fragment) {
            case Home:
                changeFragmentHelper(new HomeFragment());
                break;

            case Store:
                changeFragmentHelper(new StoreFragment());
                break;

            case Favorite:
                changeFragmentHelper(new HomeFragment());
                break;

            case Bag:
                changeFragmentHelper(new HomeFragment());
                break;

            case MyAccount:
                changeFragmentHelper(new HomeFragment());
                break;

        }

    }

    private void changeFragmentHelper(BaseFragment fragment) {

        currentFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.cont_content, fragment, fragment.getName());
        fragmentTransaction.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }
}
