package com.algorepublic.saman.ui.activities.productdetail;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.ProductAttribute;
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
    @BindView(R.id.iv_favorite)
    ImageView favoriteImageView;

    ArrayAdapter attributesAdapter;
    ProductAttribute selectedAttribute;
    //Product

    int productID;
    ArrayList<String> urls;
    CustomPagerAdapter customPagerAdapter;

    Product product;
    User authenticatedUser;

    ProductContractor.Presenter presenter;
    @BindView(R.id.loading)
    RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        authenticatedUser = GlobalValues.getUser(this);
        productID = getIntent().getIntExtra("ProductID", 1);

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
        selectedAttribute = (ProductAttribute) attributesSpinner.getSelectedItem();
        if (SamanApp.localDB != null) {
            if (SamanApp.localDB.addToCart(product, 1, selectedAttribute.getID(), 1, 1, Integer.parseInt(productCount.getText().toString()))) {
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

        productPrice.setText(product.getPrice() + " OMR");
        if (product.getProductAttributes() != null) {
            attributesAdapter = new ArrayAdapter(ProductDetailActivity.this, android.R.layout.simple_spinner_item, product.getProductAttributes());
            attributesSpinner.setAdapter(attributesAdapter);
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
}
