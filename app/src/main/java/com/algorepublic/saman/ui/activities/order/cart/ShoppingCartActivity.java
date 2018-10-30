package com.algorepublic.saman.ui.activities.order.cart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.PlaceOrderResponse;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.country.CountriesActivity;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.algorepublic.saman.ui.activities.settings.SettingsActivity;
import com.algorepublic.saman.ui.adapters.BagCartAdapter;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;
import com.algorepublic.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;

    @BindView(R.id.iv_country_flag)
    ImageView countryFlag;
    @BindView(R.id.tv_country_name)
    TextView countryName;
    @BindView(R.id.tv_shipment_address)
    TextView shipmentAddress;
    @BindView(R.id.tv_products_subtotal)
    TextView subtotalTextView;
    @BindView(R.id.tv_delivery_cost)
    TextView deliveryCostTextView;
    @BindView(R.id.tv_price_to_pay)
    TextView priceToPayTextView;
    @BindView(R.id.tv_promo_saved)
    TextView promoSavedTextView;

    //Bag
    @BindView(R.id.bag_recyclerView)
    RecyclerView bagRecyclerView;
    @BindView(R.id.tv_bag_see_all)
    TextView bagSeeAllTextView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> bagArrayList = new ArrayList<>();
    BagCartAdapter bagCartAdapter;
    //Bag
    User authenticatedUser;

    Country selectedCountry;
    int price;
    int deliveryCost=20;
    int priceToPay;
    int promoSaved=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out));
        promoSavedTextView.setVisibility(View.GONE);
        price=getIntent().getIntExtra("Price",0);
        subtotalTextView.setText(getString(R.string.subtotal)+" "+price+ ".0 OMR");
        deliveryCostTextView.setText(getString(R.string.delivery_cost)+" : "+deliveryCost+ ".0 OMR");
        priceToPay=deliveryCost+price;
        priceToPayTextView.setText(getString(R.string.price_to_pay)+" : "+priceToPay+ ".0 OMR");
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        setBag();

        for (int i = 0; i<GlobalValues.countries.size(); i++){
            if(GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))){
                selectedCountry=GlobalValues.countries.get(i);
                Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                countryName.setText(selectedCountry.getName());
            }
        }

        authenticatedUser = GlobalValues.getUser(ShoppingCartActivity.this);
        shipmentAddress.setText(authenticatedUser.getAddress().replace(" ","\n\n"));
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent=new Intent(ShoppingCartActivity.this,CountriesActivity.class);
        startActivityForResult(intent,1299);
    }



    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_place_order)
    void placeOrder(){

        JSONArray array=new JSONArray();
        JSONObject obj;
        for (int p=0;p<bagArrayList.size();p++){
            obj=new JSONObject();
            try {
                obj.put("ProductID",bagArrayList.get(p).getID());
                obj.put("AttributeID",bagArrayList.get(p).getCartAttributeID());
                obj.put("ProductQuantity",bagArrayList.get(p).getQuantity());
                array.put(obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.placeOrder(String.valueOf(authenticatedUser.getId()),
                String.valueOf(1),
                String.valueOf(1),
                String.valueOf(deliveryCost),
                String.valueOf(priceToPay),
                "COD",
                array,
                new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                PlaceOrderResponse placeOrderResponse = response.body();
                Intent intent=new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
                intent.putExtra("Response",placeOrderResponse);
                intent.putExtra("OrderTotal",priceToPay);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1299) {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i<GlobalValues.countries.size(); i++){
                    if(GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))){
                        selectedCountry=GlobalValues.countries.get(i);
                        Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                        countryName.setText(selectedCountry.getName());
                    }
                }
            }
        }
    }

    private void setBag() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
//        layoutManager = new GridLayoutManager(this, 3);
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        bagArrayList = new ArrayList<>();
        bagCartAdapter = new BagCartAdapter(this, bagArrayList);
        bagRecyclerView.setAdapter(bagCartAdapter);
//        bagRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));

        if(SamanApp.localDB!=null){
            bagArrayList.addAll(SamanApp.localDB.getCartProducts());
            bagCartAdapter.notifyDataSetChanged();
        }
    }


}
