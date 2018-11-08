package com.algorepublic.saman.ui.activities.productdetail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.OptionValue;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.ProductAttribute;
import com.algorepublic.saman.data.model.ProductOption;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetProduct;
import com.algorepublic.saman.data.model.apis.GetStores;
import com.algorepublic.saman.db.MySQLiteHelper;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.fragments.home.HomeContractor;
import com.algorepublic.saman.ui.fragments.home.HomePresenter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ProductDetailActivity extends BaseActivity implements ProductContractor.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.viewpager)
    ViewPager mPager;
    @BindView(R.id.options_layout)
    LinearLayout optionsLinearLayout;

    //Product
    @BindView(R.id.tv_attributes)
    TextView attributes;
    @BindView(R.id.tv_product_name)
    TextView productName;
    @BindView(R.id.tv_product_description)
    TextView productDescription;
    @BindView(R.id.tv_product_price)
    TextView productPrice;
    @BindView(R.id.tv_product_count)
    TextView productCount;
    @BindView(R.id.iv_favorite)
    ImageView favoriteImageView;

    //Product

    int productID;
    ArrayList<String> urls;
    CustomPagerAdapter customPagerAdapter;

    Product product;
    User authenticatedUser;

    ProductContractor.Presenter presenter;
    @BindView(R.id.loading)
    RelativeLayout loading;

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        authenticatedUser = GlobalValues.getUser(this);
        productID = getIntent().getIntExtra("ProductID", 1);

        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        presenter=new ProductPresenter(this);


        toolbarTitle.setVisibility(View.GONE);
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        urls = new ArrayList<>();
        customPagerAdapter = new CustomPagerAdapter(this, urls);
        mPager.setAdapter(customPagerAdapter);

        presenter.getProductData(productID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.iv_favorite)
    public void favoriteButton() {
        if(product.getFavorite()){
            presenter.markUnFavorite(authenticatedUser.getId(),productID);
            product.setFavorite(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.ic_fav_b));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_b));
            }
        }else {
            presenter.markFavorite(authenticatedUser.getId(),productID);
            product.setFavorite(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        }
    }

    @OnClick(R.id.iv_add_to_cart)
    public void addToCart() {
        getOptionsData();
        if (SamanApp.localDB != null) {
            if (SamanApp.localDB.addToCart(product, getOptionsData(), Integer.parseInt(productCount.getText().toString()))) {
                Constants.showAlertWithActivityFinish("ADD TO BAG", "Product Added successfully, Check Your Bag", "Okay", ProductDetailActivity.this);
            }
        }
    }

    @OnClick(R.id.left_nav)
    void left() {
        mPager.arrowScroll(View.FOCUS_LEFT);
    }

    @OnClick(R.id.right_nav)
    void right() {
        mPager.arrowScroll(View.FOCUS_RIGHT);
    }


    @OnClick(R.id.iv_add_quantity)
    void addItem() {
        int current = Integer.parseInt(productCount.getText().toString());
        current++;
        productCount.setText(String.valueOf(current));
    }

    @OnClick(R.id.iv_remove_quantity)
    void removeItem() {
        int current = Integer.parseInt(productCount.getText().toString());
        if (current > 1) {
            current--;
        }
        productCount.setText(String.valueOf(current));
    }




    @Override
    public void showProgress() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void response(Product product) {
        this.product = product;
        productName.setText(product.getProductName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productDescription.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            productDescription.setText(Html.fromHtml(product.getDescription()));
        }

        String atributes="";
        for (int i=0;i<product.getProductAttributes().size();i++){

            if(atributes.equals("")){
                atributes=product.getProductAttributes().get(i).getTitle();
            }else {

                atributes=atributes+"\n"+product.getProductAttributes().get(i).getTitle();
            }
        }
        if (!atributes.equals("")) {
            attributes.setText(atributes);
        }else {
            attributes.setText(getString(R.string.no_specifications));
        }

        productPrice.setText(product.getPrice() + " OMR");

        if(product.getProductOptions()!=null){
            for (int p=0;p<product.getProductOptions().size();p++){
                ProductOption productOption=product.getProductOptions().get(p);
                View child = inflater.inflate(R.layout.item_options_row, null);
                TextView optionName = (TextView) child.findViewById(R.id.tv_option_name);
                Spinner optionValuesSpinner = (Spinner) child.findViewById(R.id.spinner_option_value);

                optionName.setText(productOption.getTitle());

                if(productOption.getOptionValues()!=null){
                    ArrayAdapter valuesAdapter = new ArrayAdapter(ProductDetailActivity.this, android.R.layout.simple_spinner_item, productOption.getOptionValues());
                    optionValuesSpinner.setAdapter(valuesAdapter);
                }
                optionsLinearLayout.addView(child);
            }
        }

        if(product.getFavorite()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        }
        for (int i = 0; i < product.getProductImagesURLs().size(); i++) {
            urls.add(product.getProductImagesURLs().get(i));
        }
        customPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(String message) {

    }

    @Override
    public void markFavoriteResponse(boolean success) {
        if(success){
            product.setFavorite(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        }else {
            product.setFavorite(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.ic_fav_b));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_b));
            }
        }
    }

    @Override
    public void markUnFavoriteResponse(boolean success) {
        if(!success){
            product.setFavorite(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        }else {
            product.setFavorite(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.ic_fav_b));
            }else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_b));
            }
        }
    }

    private String getOptionsData(){
        View v = null;
        OptionValue optionValue=null;
        String ids="";
        if(product.getProductOptions()!=null) {
            for (int i = 0; i < product.getProductOptions().size(); i++) {
                v = optionsLinearLayout.getChildAt(i);
                Spinner spinner = (Spinner) ((RelativeLayout) v).getChildAt(1);
                optionValue = (OptionValue) spinner.getSelectedItem();

                if (ids.equals("")) {
                    ids = "" + optionValue.getID();
                } else {
                    ids = "," + optionValue.getID();
                }
            }
        }

        return ids;
    }
}
