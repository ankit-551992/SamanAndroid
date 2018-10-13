package com.algorepublic.saman.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.algorepublic.saman.R;


public abstract class BaseActivity extends AppCompatActivity {


    public void startActivity(Intent intent, boolean withAnimation) {
        startActivity(intent);
        if (withAnimation) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.stable);
        }
    }

    @Override
    public void onBackPressed() {
        onBackPressed(true);
    }

    public void onBackPressed(boolean withAnimation) {
        super.onBackPressed();
        if (withAnimation) {
            overridePendingTransition(R.anim.stable, R.anim.slide_to_left);
        }
    }
}
