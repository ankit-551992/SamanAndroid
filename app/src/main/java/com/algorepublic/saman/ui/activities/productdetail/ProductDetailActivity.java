package com.algorepublic.saman.ui.activities.productdetail;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.ProductAttribute;
import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.db.MySQLiteHelper;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.SamanApp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.viewpager)
    ViewPager mPager;

    //Product
    @BindView(R.id.tv_product_name)
    TextView productName;
    @BindView(R.id.tv_product_description)
    TextView productDescription;
    @BindView(R.id.tv_product_price)
    TextView productPrice;
    @BindView(R.id.tv_product_count)
    TextView productCount;
    @BindView(R.id.spinner_attributes)
    Spinner attributesSpinner;

    ArrayAdapter attributesAdapter;
    ProductAttribute selectedAttribute;
    //Product

    int productID;
    ArrayList<String> urls;
    CustomPagerAdapter customPagerAdapter;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        productID=getIntent().getIntExtra("ProductID",1);

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
        getProductDetail();



        attributesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                productCount.setText("1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }





    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.iv_add_to_cart)
    public void addToCart() {
        selectedAttribute = (ProductAttribute) attributesSpinner.getSelectedItem();
        if(SamanApp.localDB!=null){
            if(SamanApp.localDB.addToCart(product,1,selectedAttribute.getID(),1,1,Integer.parseInt(productCount.getText().toString()))){
                Constants.showAlertWithActivityFinish("ADD TO BAG","Product Added successfully, Check Your Bag","Okay",ProductDetailActivity.this);
            }
        }
    }

    @OnClick(R.id.left_nav)
    void left(){
        mPager.arrowScroll(View.FOCUS_LEFT);
    }

    @OnClick(R.id.right_nav)
    void right(){
        mPager.arrowScroll(View.FOCUS_RIGHT);
    }


    @OnClick(R.id.iv_add_quantity)
    void addItem() {
        int current=Integer.parseInt(productCount.getText().toString());
        current++;
        productCount.setText(String.valueOf(current));
    }

    @OnClick(R.id.iv_remove_quantity)
    void removeItem() {
        int current=Integer.parseInt(productCount.getText().toString());
        if(current>1) {
            current--;
        }
        productCount.setText(String.valueOf(current));
    }

    private void setPagerData(){
        urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYzcXT8JvYjG5IEYf-rzzklrzvOqG66atU-oyXPWlCZX7_luqU");
        customPagerAdapter.notifyDataSetChanged();
    }

    private void getProductDetail() {

        WebServicesHandler.instance.getProductDetail(String.valueOf(productID), new retrofit2.Callback<GetProduct>() {
            @Override
            public void onResponse(Call<GetProduct> call, Response<GetProduct> response) {
                GetProduct getProduct = response.body();
                if(getProduct!=null){
                    if(getProduct.getSuccess()==1){
                        product=getProduct.getProduct();
                        productName.setText(product.getProductName());
                        productDescription.setText(product.getDescription());
                        productPrice.setText(product.getPrice()+" OMR");
                        if(product.getProductAttributes()!=null) {
                            attributesAdapter = new ArrayAdapter(ProductDetailActivity.this, android.R.layout.simple_spinner_item, product.getProductAttributes());
                            attributesSpinner.setAdapter(attributesAdapter);
                        }
                        for (int i=0;i<product.getProductImagesURLs().size();i++) {
//                            urls.add(product.getProductImagesURLs().get(i));
                        }
//                        customPagerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProduct> call, Throwable t) {
            }
        });
    }
}
