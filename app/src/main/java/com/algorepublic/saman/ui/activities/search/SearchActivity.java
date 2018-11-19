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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.utils.GlobalValues;
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
    @BindView(R.id.search_bar)
    RelativeLayout searchBar;
    @BindView(R.id.recyclerView)
    RecyclerView searchRecyclerView;
    RecyclerView.LayoutManager productLayoutManager;
    List<Product> originalData = new ArrayList<>();
    List<Product> displayData = new ArrayList<>();
    ProductAdapter productAdapter;
    Dialog dialog;

    CheckBox highPrice;
    CheckBox lowPrice;
    CheckBox newIn;
    CheckBox bestSell;
    boolean isHighPrice;
    boolean isLowPrice;
    boolean isNewIn;
    boolean isBestSell;

    User authenticatedUser;

    String[] d={"PHONE FINDER","SAMSUNG","APPLE","NOKIA","SONY","LG","MOTOROLA","GOOGLE","BLACKBERRY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        authenticatedUser = GlobalValues.getUser(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        search.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.search));
        toolbarTitle.setAllCaps(true);
        toolbarBack.setVisibility(View.VISIBLE);

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

        highPrice = (CheckBox) dialog.findViewById(R.id.checkbox_high_price);
        lowPrice  = (CheckBox) dialog.findViewById(R.id.checkbox_low_price);
        newIn     = (CheckBox) dialog.findViewById(R.id.checkbox_new_in);
        bestSell  = (CheckBox) dialog.findViewById(R.id.checkbox_best_sell);


        highPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isHighPrice=b;
                    bestSell.setChecked(false);
                    lowPrice.setChecked(false);
                    newIn.setChecked(false);
                }
            }
        });

        lowPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isLowPrice=b;
                    bestSell.setChecked(false);
                    highPrice.setChecked(false);
                    newIn.setChecked(false);
                }
            }
        });

        newIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isNewIn=b;
                    highPrice.setChecked(false);
                    lowPrice.setChecked(false);
                    bestSell.setChecked(false);
                }
            }
        });

        bestSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isBestSell=b;
                    highPrice.setChecked(false);
                    lowPrice.setChecked(false);
                    newIn.setChecked(false);
                }
            }
        });

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
        productAdapter = new ProductAdapter(this, displayData,authenticatedUser.getId());
        searchRecyclerView.setAdapter(productAdapter);
        searchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));

        for (int i = 0; i < d.length; i++) {
            Product product = new Product();
            product.setProductName(d[i]);
            originalData.add(product);
        }
    }
}
