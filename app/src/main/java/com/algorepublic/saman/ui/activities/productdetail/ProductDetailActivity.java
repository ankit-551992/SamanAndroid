package com.algorepublic.saman.ui.activities.productdetail;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.viewpager)
    ViewPager mPager;

    ArrayList<String> urls;
    CustomPagerAdapter customPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setVisibility(View.GONE);
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        urls=new ArrayList<>();
        customPagerAdapter= new CustomPagerAdapter(this,urls);
        mPager.setAdapter(customPagerAdapter);

        setPagerData();
    }


    @OnClick(R.id.left_nav)
    void left(){
        mPager.arrowScroll(View.FOCUS_LEFT);
    }

    @OnClick(R.id.right_nav)
    void right(){
        mPager.arrowScroll(View.FOCUS_RIGHT);
    }



    private void setPagerData(){
        urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        customPagerAdapter.notifyDataSetChanged();
    }
}
