package com.algorepublic.saman.ui.activities.search;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_search)
    ImageView search;
    @BindView(R.id.editText_search)
    EditText searchEditText;
    @BindView(R.id.recyclerView)
    RecyclerView searchRecyclerView;
    RecyclerView.LayoutManager productLayoutManager;
    List<Product> originalData = new ArrayList<>();
    List<Product> displayData = new ArrayList<>();
    ProductAdapter productAdapter;
    Dialog dialog;

    int function=0;
    int storeID=0;
    String storeName="";
    String storeNameAr="";

    String[] d={"PHONE FINDER","SAMSUNG","APPLE","NOKIA","SONY","LG","MOTOROLA","GOOGLE","BLACKBERRY"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            function=bundle.getInt("Function");
            if(function==2){
                storeID=bundle.getInt("StoreID");
                storeName=bundle.getString("StoreName");
                storeNameAr=bundle.getString("StoreNameAr");
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(function==0){
            toolbarTitle.setText(getString(R.string.search));
        }else if (function==1){
            toolbarTitle.setText(getString(R.string.latest_product));
        }else if (function==2){
            toolbarTitle.setText(storeName);
        }

        toolbarTitle.setAllCaps(true);
        toolbarBack.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }


        getProducts();

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    displayData.clear();

                    for (int i=0;i<originalData.size();i++){
                        if(originalData.get(i).getProductName().toLowerCase().contains(searchEditText.getText().toString().toLowerCase())){
                            displayData.add(originalData.get(i));
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.iv_filer)
    void filter(){
        dialog = new Dialog(SearchActivity.this,R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);
        TextView done = (TextView) dialog.findViewById(R.id.tv_done);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(SearchActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup)dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);

        dialog.show();
    }

    private void getProducts() {
        productLayoutManager = new GridLayoutManager(this, 2);
        searchRecyclerView.setLayoutManager(productLayoutManager);
        searchRecyclerView.setNestedScrollingEnabled(false);
        originalData = new ArrayList<>();
        displayData = new ArrayList<>();
        productAdapter = new ProductAdapter(this, displayData);
        searchRecyclerView.setAdapter(productAdapter);
        searchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        for (int i = 0; i < d.length; i++) {
            Product product = new Product();
            product.setProductName(d[i]);
            originalData.add(product);
        }
    }
}
