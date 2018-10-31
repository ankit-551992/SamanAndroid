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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.Country;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Store;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.data.model.apis.PlaceOrderResponse;
import com.algorepublic.saman.data.model.apis.PromoVerify;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.country.CountriesActivity;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.algorepublic.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.algorepublic.saman.ui.activities.settings.SettingsActivity;
import com.algorepublic.saman.ui.adapters.BagCartAdapter;
import com.algorepublic.saman.ui.adapters.StoresAdapter;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;
import com.algorepublic.saman.utils.SamanApp;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PaymentActivity;
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

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.editText_promo)
    EditText promoEditText;

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

    //CARD
    @BindView(R.id.iv_card)
    ImageView cardImage;
    @BindView(R.id.tv_card_number)
    TextView cardNumberTextView;
    @BindView(R.id.tv_card_holder_name)
    TextView cardNameTextView;
    @BindView(R.id.tv_card_expiry)
    TextView cardExpiryTextView;
    //CARD

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
    float price;
    float deliveryCost = 0.0f;
    float priceToPay;
    float promoSaved = 0.0f;

    boolean promoApplied=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out));
        promoSavedTextView.setVisibility(View.GONE);
        price = (float) getIntent().getIntExtra("Price", 0);
        subtotalTextView.setText(getString(R.string.subtotal) + " " + price + " OMR");
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        setBag();

        for (int i = 0; i < GlobalValues.countries.size(); i++) {
            if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))) {
                selectedCountry = GlobalValues.countries.get(i);
                Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                countryName.setText(selectedCountry.getName());
            }
        }

        authenticatedUser = GlobalValues.getUser(ShoppingCartActivity.this);
        shipmentAddress.setText(authenticatedUser.getAddress().replace(" ", "\n\n"));

        cardNameTextView.setText("Cash On delivery");
        cardExpiryTextView.setVisibility(View.GONE);
        cardNumberTextView.setVisibility(View.GONE);
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(ShoppingCartActivity.this, CountriesActivity.class);
        startActivityForResult(intent, 1299);
    }


    @OnClick(R.id.button_apply)
    public void applyPromo() {

        if (promoApplied){
            Constants.showAlert("Apply Coupon","Already Applied","Close",ShoppingCartActivity.this);
            return;
        }

        if(promoEditText.getText().toString().equals("")){
            return;
        }
        Constants.showSpinner("Applying Coupon",ShoppingCartActivity.this);
        WebServicesHandler.instance.applyPromo(promoEditText.getText().toString(),new retrofit2.Callback<PromoVerify>() {
            @Override
            public void onResponse(Call<PromoVerify> call, Response<PromoVerify> response) {
                PromoVerify promoVerify = response.body();
                Constants.dismissSpinner();
                if (promoVerify != null) {
                    if (promoVerify.getSuccess() == 1) {
                        promoApplied=true;
                        promoSaved=(float)promoVerify.getResult().getDiscount();
                        promoSavedTextView.setVisibility(View.VISIBLE);
                        promoSavedTextView.setText("Promo Saved : "+promoSaved+" OMR");
                        priceToPay=priceToPay-promoSaved;
                        priceToPayTextView.setText(getString(R.string.price_to_pay) + " : " + priceToPay + " OMR");
                    }else {
                        Constants.showAlert("Apply Coupon","Invalid Coupon",getString(R.string.try_again),ShoppingCartActivity.this);
                    }
                }
            }
            @Override
            public void onFailure(Call<PromoVerify> call, Throwable t) {
                Constants.dismissSpinner();
            }
        });
    }

    @OnClick(R.id.button_change_payment)
    public void changePaymentMethod() {
        Intent intent = new Intent(ShoppingCartActivity.this, MyPaymentActivity.class);
        startActivityForResult(intent, 1309);
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_place_order)
    void placeOrder() {

        progressBar.setVisibility(View.VISIBLE);
        JSONArray array = new JSONArray();
        JSONObject obj;
        for (int p = 0; p < bagArrayList.size(); p++) {
            obj = new JSONObject();
            try {
                obj.put("ProductID", bagArrayList.get(p).getID());
                obj.put("AttributeID", bagArrayList.get(p).getCartAttributeID());
                obj.put("ProductQuantity", bagArrayList.get(p).getQuantity());
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
                        Intent intent = new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
                        intent.putExtra("Response", placeOrderResponse);
                        intent.putExtra("OrderTotal", priceToPay);
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
                for (int i = 0; i < GlobalValues.countries.size(); i++) {
                    if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))) {
                        selectedCountry = GlobalValues.countries.get(i);
                        Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                        countryName.setText(selectedCountry.getName());
                    }
                }
            }
        } else if (requestCode == 1309) {
            if (resultCode == RESULT_OK) {
                String d = data.getExtras().getString("DATA");

                if (d.equalsIgnoreCase("CASH")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cardImage.setImageDrawable(getDrawable(R.drawable.cash_delivery));
                        cardImage.getLayoutParams().height = 200;
                        cardImage.getLayoutParams().width = 200;
                    } else {
                        cardImage.getLayoutParams().height = 200;
                        cardImage.getLayoutParams().width = 200;
                        cardImage.setImageDrawable(getResources().getDrawable(R.drawable.cash_delivery));
                    }
                    cardNameTextView.setText("Cash On delivery");
                    cardExpiryTextView.setVisibility(View.GONE);
                    cardNumberTextView.setVisibility(View.GONE);
                } else {

                    ArrayList<CardDs> cardDs = new ArrayList<>();

                    Object obj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
                    }.getType());
                    if (obj != null) {
                        cardDs.addAll((ArrayList<CardDs>) obj);
                    }

                    for (int i=0;i<cardDs.size();i++){
                        if(cardDs.get(i).getCardNumber().equals(d)){

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                cardImage.setImageDrawable(getDrawable(R.drawable.visa_card));
                                cardImage.getLayoutParams().height = 200;
                                cardImage.getLayoutParams().width = 200;
                            } else {
                                cardImage.getLayoutParams().height = 200;
                                cardImage.getLayoutParams().width = 200;
                                cardImage.setImageDrawable(getResources().getDrawable(R.drawable.visa_card));
                            }
                            cardNameTextView.setText(cardDs.get(i).getCardHolder());
                            cardExpiryTextView.setText(cardDs.get(i).getExpireDate());
                            cardNumberTextView.setText(cardDs.get(i).getCardNumber());
                            cardNameTextView.setVisibility(View.VISIBLE);
                            cardExpiryTextView.setVisibility(View.VISIBLE);
                            cardNumberTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    private void setBag() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        layoutManager = new GridLayoutManager(this, 3);
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        bagArrayList = new ArrayList<>();
        bagCartAdapter = new BagCartAdapter(this, bagArrayList);
        bagRecyclerView.setAdapter(bagCartAdapter);
//        bagRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));

        if (SamanApp.localDB != null) {
            bagArrayList.addAll(SamanApp.localDB.getCartProducts());
            bagCartAdapter.notifyDataSetChanged();

            if (price < 35) {

                switch (bagArrayList.size()) {
                    case 1:
                        deliveryCost = 1.0f;
                        break;
                    case 2:
                        deliveryCost = 0.5f;
                        break;
                    case 3:
                        deliveryCost = 0.5f;
                        break;
                    case 4:
                        deliveryCost = 0.4f;
                        break;
                    case 5:
                        deliveryCost = 0.4f;
                        break;
                    default:
                        deliveryCost = 2.0f;
                        break;
                }

            }
            priceToPay = deliveryCost + price;
            priceToPay=priceToPay-promoSaved;
            deliveryCostTextView.setText(getString(R.string.delivery_cost) + " : " + deliveryCost + " OMR");
            priceToPayTextView.setText(getString(R.string.price_to_pay) + " : " + priceToPay + " OMR");
        }
    }
}
