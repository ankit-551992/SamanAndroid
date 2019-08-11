package com.qtech.saman.ui.activities.imagesview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullScreenImagesActivity extends BaseActivity {

    @BindView(R.id.tv_count)
    TextView countTextView;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    FullScreenAdapter mAdapter;
    List<String> urls;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_full_screen_images);
        ButterKnife.bind(this);

        urls = new ArrayList<>();

        urls = getIntent().getStringArrayListExtra("urls");
        index = getIntent().getIntExtra("index", 0);

        mAdapter = new FullScreenAdapter(this, urls);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);
        countTextView.setText((index + 1) + "/" + urls.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                index = position;
                countTextView.setText((index + 1) + "/" + urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick(R.id.iv_close)
    public void close() {
        finish();
    }
}
