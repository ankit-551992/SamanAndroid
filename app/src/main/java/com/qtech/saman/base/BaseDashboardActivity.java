package com.qtech.saman.base;


import android.widget.ImageView;
import android.widget.TextView;


import com.qtech.saman.R;
import com.qtech.saman.utils.Constants;

import butterknife.BindView;



public abstract class BaseDashboardActivity extends BaseFragmentActivity {

    @BindView(R.id.iv_nav_home)
    ImageView ivNavHome;
    @BindView(R.id.tv_nav_home)
    TextView tvNavHome;

    @BindView(R.id.iv_nav_store)
    ImageView ivNavStore;
    @BindView(R.id.tv_nav_store)
    TextView tvNavStore;

    @BindView(R.id.iv_nav_favorite)
    ImageView ivNavFavorite;
    @BindView(R.id.tv_nav_favorite)
    TextView tvNavFavorite;

    @BindView(R.id.iv_nav_bag)
    ImageView ivNavBag;
    @BindView(R.id.tv_nav_bag)
    TextView tvNavBag;

    @BindView(R.id.iv_nav_my_account)
    ImageView ivNavMyAccount;
    @BindView(R.id.tv_nav_my_account)
    TextView tvNavMyAccount;


    //Fragment handling
    @Override
    public void changeFragment(Constants.Fragment fragment) {

        super.changeFragment(fragment);

        switch (fragment) {
            case Home:
                adjustNavBar(ivNavHome);
                break;
            case Store:
                adjustNavBar(ivNavStore);
                break;

            case Favorite:
                adjustNavBar(ivNavFavorite);
                break;

            case Bag:
                adjustNavBar(ivNavBag);
                break;

            case MyAccount:
                adjustNavBar(ivNavMyAccount);
                break;

            default:
                adjustNavBar(null);
                break;
        }
    }


    //Nav bar
    private void adjustNavBar(ImageView ivSelected) {

        int normalColor = getResources().getColor(R.color.grey);
        int selectedColor = getResources().getColor(R.color.colorPrimary);

        if (ivSelected == ivNavHome) {
            ivNavHome.setImageResource(R.drawable.ic_home);
            tvNavHome.setTextColor(selectedColor);
        }else {
            ivNavHome.setImageResource(R.drawable.ic_home);
            tvNavHome.setTextColor(normalColor);
        }

        if (ivSelected == ivNavStore) {
            ivNavStore.setImageResource(R.drawable.ic_home);
            tvNavStore.setTextColor(selectedColor);
        }else {
            ivNavStore.setImageResource(R.drawable.ic_home);
            tvNavStore.setTextColor(normalColor);
        }

        if (ivSelected == ivNavFavorite) {
            ivNavFavorite.setImageResource(R.drawable.ic_home);
            tvNavFavorite.setTextColor(selectedColor);
        }else {
            ivNavFavorite.setImageResource(R.drawable.ic_home);
            tvNavFavorite.setTextColor(normalColor);
        }

        if (ivSelected == ivNavBag) {
            ivNavBag.setImageResource(R.drawable.ic_home);
            tvNavBag.setTextColor(selectedColor);
        }else {
            ivNavBag.setImageResource(R.drawable.ic_home);
            tvNavBag.setTextColor(normalColor);
        }

        if (ivSelected == ivNavMyAccount) {
            ivNavMyAccount.setImageResource(R.drawable.ic_home);
            tvNavMyAccount.setTextColor(selectedColor);
        }else {
            ivNavMyAccount.setImageResource(R.drawable.ic_home);
            tvNavMyAccount.setTextColor(normalColor);
        }

    }
}
