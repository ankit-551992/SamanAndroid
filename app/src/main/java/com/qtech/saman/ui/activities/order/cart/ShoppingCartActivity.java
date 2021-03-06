package com.qtech.saman.ui.activities.order.cart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mastercard.gateway.android.sdk.Gateway;
import com.mastercard.gateway.android.sdk.Gateway3DSecureCallback;
import com.mastercard.gateway.android.sdk.GatewayCallback;
import com.mastercard.gateway.android.sdk.GatewayMap;
import com.qtech.saman.BuildConfig;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.CardDs;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.data.model.Coupon;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.ShippingAddress;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.PlaceOrderResponse;
import com.qtech.saman.data.model.apis.PromoVerify;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.data.model.paymentgateway.PaymentGateWay;
import com.qtech.saman.network.ApiController;
import com.qtech.saman.network.OmanNetServiceHandler;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.PoliciesActivity;
import com.qtech.saman.ui.activities.country.CountriesListingActivity;
import com.qtech.saman.ui.activities.myaccount.addresses.ShippingAddressActivity;
import com.qtech.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.qtech.saman.ui.activities.myaccount.payment.OmanNetCardDetailActivity;
import com.qtech.saman.ui.activities.order.checkout.CheckoutOrderActivity;
import com.qtech.saman.ui.adapters.BagCartAdapter;
import com.qtech.saman.ui.adapters.TagsAdapter;
import com.qtech.saman.utils.CircleTransform;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.GridSpacingItemDecoration;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends BaseActivity implements Gateway3DSecureCallback {

    public String sId, apiVer;
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
    @BindView(R.id.tv_agreement_order)
    TextView agreementOrder;
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
    //Tags
    @BindView(R.id.tags_recyclerView)
    RecyclerView tagRecyclerView;
    RecyclerView.LayoutManager tagsLayoutManager;
    List<String> tagsList = new ArrayList<>();
    List<Integer> appliedProducts = new ArrayList<>();
    TagsAdapter tagsAdapter;
    Country selectedCountry;
    double price = 0.0f;
    double subTotal = 0.0f;
    float deliveryCost = 0.0f;
    double priceToPay;
    double promoSaved = 0.0f;
    double promoTotalSaved = 0.0f;
    String couponCode = "";
    int addressID = -1;
    PlaceOrderResponse placeOrderResponse;
    ApiController apiController = ApiController.getInstance();
    boolean isCOD = true;
    boolean isOmanNet = false;
    Gateway paymentGateway;
    String orderId;
    String transactionId;
    String amount;
    String currency;
    String paymentType = "COD";
    boolean requestAgain = false;
    boolean isGeneralApplied = false;
    String discount_couponId = "";
    String discount_price = "";
    float final_displayprice = 0.0f;
    float dis_product = 0.0f;
    double couponDiscount_price = 0.0f;
    String setaddress = "";
    String AddressLine1, floor, apt, city, usercountry, landmark;
    String userregion = "";
    String latitude, longitude;
    private CardDs selectedCard = null;
    private boolean isSelect = false;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out));
        price = getIntent().getFloatExtra("Price", 0);
        decimalFormat = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        subtotalTextView.setText(String.format("%s %s %s", getString(R.string.subtotal), decimalFormat.format(price), getString(R.string.OMR)));
        appliedProducts = new ArrayList<>();
        subTotal = price;
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
        setBag();
        setTagView();

        currency = "OMR";
        //random 3ds/order/txn IDs for example purposes
        orderId = UUID.randomUUID().toString();
        orderId = orderId.substring(0, orderId.indexOf('-'));
        transactionId = UUID.randomUUID().toString();
        transactionId = transactionId.substring(0, transactionId.indexOf('-'));

        GlobalValues.countries = new ArrayList<>();
        getCountriesAPI();

        authenticatedUser = GlobalValues.getUser(ShoppingCartActivity.this);

        if (authenticatedUser.getShippingAddress() != null) {
            addressID = authenticatedUser.getShippingAddress().getiD();
            setShippingAddress(authenticatedUser.getShippingAddress());
        }

        cardNameTextView.setText(getString(R.string.card_delivery));
        cardExpiryTextView.setVisibility(View.GONE);
        cardNumberTextView.setVisibility(View.GONE);

        customTextView(agreementOrder);

        apiController.setMerchantServerUrl(BuildConfig.MERCHANT_SERVER_URL);

        paymentGateway = new Gateway();
        paymentGateway.setMerchantId(BuildConfig.GATEWAY_MERCHANT_ID);

        try {
            Gateway.Region region = Gateway.Region.valueOf(BuildConfig.GATEWAY_REGION);
            paymentGateway.setRegion(region);
        } catch (Exception e) {
            e.printStackTrace();
        }
        apiController.createSession(new CreateSessionCallback());

        promoSavedTextView.setVisibility(View.VISIBLE);
        promoSavedTextView.setText(String.format("%s: %s %s", getString(R.string.promo_saved), decimalFormat.format(promoTotalSaved), getString(R.string.OMR)));
    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getString(R.string.agreement_order));
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary));
        spanTxt.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanTxt.length(), 0);
        spanTxt.append(getString(R.string.term));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                view.setTextColor(ContextCompat.getColor(ShoppingCartActivity.this, R.color.colorPrimary));
                Intent intent = new Intent(ShoppingCartActivity.this, PoliciesActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        }, spanTxt.length() - getString(R.string.term).length(), spanTxt.length(), 0);
        spanTxt.setSpan(foregroundSpan, spanTxt.length() - getString(R.string.term).length(), spanTxt.length(), 0);
        spanTxt.append(getString(R.string.and));
        spanTxt.setSpan(new ForegroundColorSpan(Color.GRAY), spanTxt.length() - getString(R.string.and).length(), spanTxt.length(), 0);
        spanTxt.append(getString(R.string.privacy));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(ShoppingCartActivity.this, PoliciesActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        }, spanTxt.length() - getString(R.string.privacy).length(), spanTxt.length(), 0);
        spanTxt.setSpan(foregroundSpan, spanTxt.length() - getString(R.string.privacy).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    @OnClick(R.id.layout_countrySelection)
    public void countrySelection() {
        Intent intent = new Intent(ShoppingCartActivity.this, CountriesListingActivity.class);
        intent.putExtra("cartlist", 1);
        startActivityForResult(intent, 1299);
    }

    @OnClick(R.id.button_apply)
    public void applyPromo() {
        if (promoEditText.getText().toString().equals("")) {
            return;
        }

        boolean isNewPromo = true;
        for (int t = 0; t < tagsList.size(); t++) {
            if (!promoEditText.getText().toString().isEmpty() && promoEditText.getText().toString().equalsIgnoreCase(tagsList.get(t))) {
                isNewPromo = false;
                break;
            }
        }
        isSelect = tagsList.size() > 0;
        if (!isNewPromo) {
            Constants.showAlert("", getString(R.string.already_apply),
                    getString(R.string.close), ShoppingCartActivity.this);
            return;
        }

        if (isSelect) {
            Constants.showAlert("", getString(R.string.only_one_time_apply),
                    getString(R.string.close), ShoppingCartActivity.this);
            return;
        }

        Constants.showSpinner(getString(R.string.apply_coupon), ShoppingCartActivity.this);
        WebServicesHandler.instance.applyPromo(promoEditText.getText().toString(), new Callback<PromoVerify>() {
            @Override
            public void onResponse(Call<PromoVerify> call, Response<PromoVerify> response) {
                PromoVerify promoVerify = response.body();
                Constants.dismissSpinner();
                if (promoVerify != null) {
                    if (promoVerify.getSuccess() == 1) {
                        if (promoVerify.getResult().getCouponType() == 1) {
                            if (!isGeneralApplied) {
                                isGeneralApplied = true;
                                setPromoDiscountWithPrice(promoVerify.getResult(), 1);
                            } else {
                                Constants.showAlert("", getString(R.string.already_apply),
                                        getString(R.string.close), ShoppingCartActivity.this);
                            }
                        } else {
                            setPromoDiscountWithPrice(promoVerify.getResult(), 2);
                        }
                        if (promoSaved > 0) {
                            tagsList.add(promoEditText.getText().toString());
                            setTagData();
                            String msg = getString(R.string.coupon_discount_is_) + " " + decimalFormat.format(promoSaved) + " " + getString(R.string.OMR);
                            Constants.showAlert(getString(R.string.coupon_discount), msg, getString(R.string.Okay), ShoppingCartActivity.this);
                        } else {
                            isGeneralApplied = false;
                            Constants.showAlert(getString(R.string.coupon_discount), getString(R.string.invalid_coupon), getString(R.string.okay), ShoppingCartActivity.this);
                        }
                        discount_couponId = String.valueOf(promoVerify.getResult().getID());
                        discount_price = String.valueOf(promoSaved);
                    } else {
                        Constants.showAlert(getString(R.string.apply_coupon), promoVerify.getMessage(), getString(R.string.try_again), ShoppingCartActivity.this);
                    }
                }
                promoEditText.setText("");
                promoEditText.clearFocus();
            }

            @Override
            public void onFailure(Call<PromoVerify> call, Throwable t) {
                Constants.dismissSpinner();
            }
        });
    }


    private void setPromoDiscountWithPrice(Coupon coupon, int type) {

        if (type == 2) {
            if (coupon.getDiscountType() == 1) {
                //Percentage


                double newPromoAmount = 0;
                for (int i = 0; i < coupon.getProductID().size(); i++) {
                    for (Product product : bagArrayList) {
                        if (product.getID().equals(coupon.getProductID().get(i))) {
                            float price = 0;
                            if (product.getIsSaleProduct().equals("true")) {
                                if (product.getSaleDiscountedType().equals("1")) {
                                    product.setProductDiscountPrice(product.getSalePrice());
                                    price = product.getPrice() - product.getSalePrice();
                                } else if (product.getSaleDiscountedType().equals("2")) {
                                    float calculateDiscount = product.getPrice() / 100.0f;
                                    float dis = calculateDiscount * product.getSalePrice();
                                    product.setProductDiscountPrice(dis);
                                    price = product.getPrice() - dis;
                                } else {
                                    price = product.getPrice();
                                }
                            } else {
                                price = product.getPrice();
                            }
                            price = price * product.getQuantity();
                            promoSaved += (price * coupon.getDiscount()) / 100;
                        }
                    }
                }


            } else if (coupon.getDiscountType() == 2) {
                //Price
                ArrayList<Integer> exist = new ArrayList<>();
                double exist1 = 0;

                for (int i = 0; i < coupon.getProductID().size(); i++) {
                    for (Product product : bagArrayList) {
                        if (product.getID().equals(coupon.getProductID().get(i))) {
                            exist.add(product.getID());
                        }
                    }
                }
                promoSaved = coupon.getDiscount() * exist.size();

            }
        } else {
            if (coupon.getDiscountType() == 1) {
                //Percentage
                double calculateDiscount = subTotal / 100.0f;
                double dis = calculateDiscount * ((float) coupon.getDiscount());
                promoSaved = promoSaved + dis;
            } else if (coupon.getDiscountType() == 2) {
                float dis = (float) coupon.getDiscount();
                promoSaved = promoSaved + dis;
            }

        }

        promoSaved = Double.parseDouble(decimalFormat.format(promoSaved));

        promoSavedTextView.setVisibility(View.VISIBLE);
        promoTotalSaved = promoTotalSaved + promoSaved;
        promoSavedTextView.setText(String.format("%s: %s %s", getString(R.string.promo_saved), decimalFormat.format(promoTotalSaved), getString(R.string.OMR)));
        price = subTotal - promoSaved;
        priceToPay = price + deliveryCost;
        priceToPayTextView.setText(String.format("%s : %s %s", getString(R.string.price_to_pay), decimalFormat.format(priceToPay), getString(R.string.OMR)));

        couponCode = coupon.getCouponCode();
    }

    @OnClick({R.id.tv_delivery_address_change, R.id.iv_delivery_address_change})
    public void changeDeliveryAddress() {
        Intent intent = new Intent(ShoppingCartActivity.this, ShippingAddressActivity.class);
        intent.putExtra("Request", true);
        startActivityForResult(intent, 1204);
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
        if (addressID == -1) {
            Constants.showAlert(getString(R.string.check_out), getString(R.string.add_shipping_address), getString(R.string.okay), ShoppingCartActivity.this);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        JSONArray array = new JSONArray();
        JSONObject obj;
        for (int p = 0; p < bagArrayList.size(); p++) {

            int product_quantity = bagArrayList.get(p).getQuantity();

            if (bagArrayList.get(p).getIsSaleProduct().equals("true")) {
                if (bagArrayList.get(p).getSaleDiscountedType().equals("1")) {
                    dis_product = bagArrayList.get(p).getSalePrice();
                    final_displayprice = bagArrayList.get(p).getPrice() - bagArrayList.get(p).getSalePrice();
                } else if (bagArrayList.get(p).getSaleDiscountedType().equals("2")) {
                    float calculateDiscount = bagArrayList.get(p).getPrice() / 100.0f;
                    dis_product = calculateDiscount * bagArrayList.get(p).getSalePrice();
                    final_displayprice = bagArrayList.get(p).getPrice() - dis_product;
                } else {
                    dis_product = 0.0f;
                    final_displayprice = bagArrayList.get(p).getPrice();
                }
            } else {
                dis_product = 0.0f;
                final_displayprice = bagArrayList.get(p).getPrice();
            }

            float product_quantity_price = final_displayprice * product_quantity;
            float p1 = product_quantity_price * 100;
            couponDiscount_price = p1 / subTotal;
            couponDiscount_price = couponDiscount_price * promoSaved / 100;
            couponDiscount_price = Double.parseDouble(decimalFormat.format(couponDiscount_price));

            obj = new JSONObject();
            try {
                obj.put("ProductID", bagArrayList.get(p).getID());
                obj.put("ProductQuantity", bagArrayList.get(p).getQuantity());
                obj.put("ProductPrice", bagArrayList.get(p).getPrice());
                obj.put("Discount", dis_product);
                obj.put("couponDiscount", couponDiscount_price);

                JSONArray optionsArray = new JSONArray();
                JSONObject optionsObj;
                String[] optionIDs = bagArrayList.get(p).getOptionValues().split(",");
                for (int o = 0; o < optionIDs.length; o++) {
                    if (!optionIDs[o].equals("")) {
                        optionsObj = new JSONObject();
                        optionsObj.put("ID", Integer.valueOf(optionIDs[o]));
                        optionsArray.put(optionsObj);
                    }
                }
                obj.put("OrderOptionValue", optionsArray);
                array.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WebServicesHandler apiClient = WebServicesHandler.instance;
        apiClient.placeOrder(authenticatedUser.getId(),
                addressID,
                addressID,
                deliveryCost,
                priceToPay,
                paymentType,
                discount_couponId,
                discount_price,
                couponCode,
                array,
                new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        placeOrderResponse = response.body();
                        if (placeOrderResponse != null) {
                            if (placeOrderResponse.getSuccess() == 1) {
                                if (isCOD) {
                                    Intent intent = new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
                                    intent.putExtra("Response", placeOrderResponse);
                                    String v1 = decimalFormat.format(priceToPay);
                                    intent.putExtra("OrderTotal", v1);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (selectedCard.getType() == 0) {
                                        orderId = placeOrderResponse.getResult().getOrderNumber();
                                        doCheck3DSEnrollment();
                                    } else {
                                        omanNetPaymentVerification(placeOrderResponse.getResult().getId());
                                    }
                                }
                            } else if (placeOrderResponse.getSuccess() == 0) {
                                progressBar.setVisibility(View.GONE);
                                if (placeOrderResponse.getMessage() != null) {
                                    Constants.showErrorPopUp(ShoppingCartActivity.this, getResources().getString(R.string.error),
                                            placeOrderResponse.getMessage(), getResources().getString(R.string.okay));
                                } else {
                                    Constants.showErrorPopUp(ShoppingCartActivity.this, getResources().getString(R.string.error),
                                            getResources().getString(R.string.order_fail_msg), getResources().getString(R.string.okay));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                        Constants.showErrorPopUp(ShoppingCartActivity.this, getResources().getString(R.string.error),
                                getResources().getString(R.string.order_fail_msg), getResources().getString(R.string.okay));
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Gateway.handle3DSecureResult(requestCode, resultCode, data, this)) {
            return;
        }

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

                assert d != null;
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
                    cardNameTextView.setText(getString(R.string.card_delivery));
                    cardExpiryTextView.setVisibility(View.GONE);
                    cardNumberTextView.setVisibility(View.GONE);
                    isCOD = true;
                } else if (d.equalsIgnoreCase("OMANNET")) {

                    isCOD = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cardImage.setImageDrawable(getDrawable(R.drawable.ic_oman_net));
                        cardImage.getLayoutParams().height = 200;
                        cardImage.getLayoutParams().width = 200;
                    } else {
                        cardImage.getLayoutParams().height = 200;
                        cardImage.getLayoutParams().width = 200;
                        cardImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_oman_net));
                    }
                    cardNameTextView.setText(getString(R.string.omannet));
                    cardExpiryTextView.setVisibility(View.GONE);
                    cardNumberTextView.setVisibility(View.GONE);

                    isOmanNet = true;
                    paymentType = "OmanNet";
                    selectedCard = new CardDs();
                    selectedCard.setCardNumber("OMANNET");
                    selectedCard.setCardHolder("CardHolderName");
                    selectedCard.setExpireDate("02/2222");
                    selectedCard.setMonth(1);
                    selectedCard.setCvc("CVV");
                    selectedCard.setType(1);

                } else {
                    paymentType = "MasterCard";
                    isCOD = false;
                    ArrayList<CardDs> cardDs = new ArrayList<>();

                    Object obj = GlobalValues.fromJson(SamanApp.db.getString(Constants.CARD_LIST), new TypeToken<ArrayList<CardDs>>() {
                    }.getType());
                    if (obj != null) {
                        cardDs.addAll((ArrayList<CardDs>) obj);
                    }

                    for (int i = 0; i < cardDs.size(); i++) {
                        if (cardDs.get(i).getCardNumber().equals(d)) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                cardImage.setImageDrawable(getDrawable(R.drawable.ic_visa_));
                                cardImage.getLayoutParams().height = 200;
                                cardImage.getLayoutParams().width = 200;
                            } else {
                                cardImage.getLayoutParams().height = 200;
                                cardImage.getLayoutParams().width = 200;
                                cardImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa_));
                            }

                            selectedCard = cardDs.get(i);
                            cardNameTextView.setText(cardDs.get(i).getCardHolder());
                            cardExpiryTextView.setText(cardDs.get(i).getExpireDate());
                            cardNumberTextView.setText(maskedCardNumber(cardDs.get(i).getCardNumber()));
                            cardNameTextView.setVisibility(View.VISIBLE);
                            cardExpiryTextView.setVisibility(View.VISIBLE);
                            cardNumberTextView.setVisibility(View.VISIBLE);

                            if (sId != null) {
                                updateCardOnGateway();
                            } else {
                                apiController.createSession(new CreateSessionCallback());
                                requestAgain = true;
                            }
                        }
                    }
                }
            }
        } else if (requestCode == 1204) {
            if (resultCode == RESULT_OK) {
                addressID = data.getExtras().getInt("ID");
                ShippingAddress shippingAddress = (ShippingAddress) data.getExtras().getSerializable("DATA");
                if (shippingAddress != null) {
                    setShippingAddress(shippingAddress);
                }
            }
        } else if (requestCode == 2019) {
            if (resultCode == RESULT_OK) {
                isPaymentSuccessFull(placeOrderResponse.getResult().getId());
            } else if (resultCode == RESULT_CANCELED) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    private void setShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress.getAddressLine1() != null) {
            setaddress = shippingAddress.getAddressLine1();
            AddressLine1 = shippingAddress.getAddressLine1();
        }
        if (shippingAddress.getFloor() != null) {
            setaddress = setaddress + ", " + shippingAddress.getFloor();
            floor = shippingAddress.getFloor();
        }
        if (shippingAddress.getApt() != null) {
            setaddress = setaddress + ", " + shippingAddress.getApt();
            apt = shippingAddress.getApt();
        }
        if (shippingAddress.getAddressLine2() != null) {
            setaddress = setaddress + ", " + shippingAddress.getAddressLine2();
            landmark = shippingAddress.getAddressLine2();
        }
        if (shippingAddress.getCity() != null) {
            setaddress = setaddress + ", " + shippingAddress.getCity();
            city = shippingAddress.getCity();
        }
        if (shippingAddress.getCountry() != null) {
            setaddress = setaddress + ", " + shippingAddress.getCountry();
            usercountry = shippingAddress.getCountry();
        }
        if (shippingAddress.getRegion() != null) {
            setaddress = setaddress + ", " + shippingAddress.getRegion();
            userregion = shippingAddress.getRegion();
        }
        if (shippingAddress.getLatitude() != null && shippingAddress.getLongitude() != null) {
            latitude = shippingAddress.getLatitude();
            longitude = shippingAddress.getLongitude();
        }
        shipmentAddress.setText(setaddress);
        setShipmentCost();
    }

    private void setTagView() {
        tagsList = new ArrayList<>();
        tagsLayoutManager = new GridLayoutManager(this, 3);
        tagRecyclerView.setLayoutManager(tagsLayoutManager);
        tagRecyclerView.setNestedScrollingEnabled(false);
        tagsAdapter = new TagsAdapter(this, tagsList);
        tagRecyclerView.setAdapter(tagsAdapter);
        tagRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 5, false, this));
    }

    private void setTagData() {
        tagRecyclerView.setVisibility(View.VISIBLE);
        tagsAdapter.notifyDataSetChanged();
    }

    private void setBag() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//      layoutManager = new GridLayoutManager(this, 3);
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        bagArrayList = new ArrayList<>();
        bagCartAdapter = new BagCartAdapter(this, bagArrayList);
        bagRecyclerView.setAdapter(bagCartAdapter);

        if (SamanApp.localDB != null) {
            bagArrayList.addAll(SamanApp.localDB.getCartProducts());
            bagCartAdapter.notifyDataSetChanged();
        }
    }

    private void setShipmentCost() {
        if (SamanApp.localDB != null) {
            if (userregion.contains("Governorate of Muscat") || userregion.contains("محافظة مسقط") || userregion.contains("Muscat")) {    // Insideside of Muscat
                if (price < 35) {
                    switch (SamanApp.localDB.getCartAllProductsCount()) {
                        case 1:
                            deliveryCost = 1.0f;
                            break;
                        case 2:
                        case 3:
                            deliveryCost = 0.5f;
                            break;
                        case 4:
                        case 5:
                            deliveryCost = 0.4f;
                            break;
                        default:
                            deliveryCost = 2.0f;
                            break;
                    }
                } else {
                    deliveryCost = 0.0f;
                }
            } else {
                if (price < 35) {
                    switch (SamanApp.localDB.getCartAllProductsCount()) {    // Outside of Muscat
                        case 1:
                            deliveryCost = 1.3f;
                            break;
                        case 2:
                        case 3:
                            deliveryCost = 0.65f;
                            break;
                        case 4:
                        case 5:
                            deliveryCost = 0.55f;
                            break;
                        default:
                            deliveryCost = 2.7f;
                            break;
                    }
                } else {
                    deliveryCost = 0.0f;
                }
            }
            priceToPay =SamanApp.localDB.getCartAllProductsCount()*deliveryCost + price;
            priceToPay = priceToPay - promoSaved;

            deliveryCostTextView.setText(String.format("%s : %s %s", getString(R.string.delivery_cost),
                    decimalFormat.format(SamanApp.localDB.getCartAllProductsCount()*deliveryCost), getString(R.string.OMR)));
            priceToPayTextView.setText(String.format("%s : %s %s", getString(R.string.price_to_pay),
                    decimalFormat.format(priceToPay), getString(R.string.OMR)));

        }
    }

    String maskedCardNumber(String cardNumber) {
        int maskLen = cardNumber.length() - 4;
        char[] mask = new char[maskLen];
        Arrays.fill(mask, '*');
        return new String(mask) + cardNumber.substring(maskLen);
    }

    private void updateCardOnGateway() {
        // build the gateway request
        String y = String.valueOf(selectedCard.getYear());
        String year = y.substring(Math.max(y.length() - 2, 0));
        GatewayMap request = new GatewayMap()
                .set("sourceOfFunds.provided.card.nameOnCard", selectedCard.getCardHolder())
                .set("sourceOfFunds.provided.card.number", selectedCard.getCardNumber())
                .set("sourceOfFunds.provided.card.securityCode", selectedCard.getCvc())
                .set("sourceOfFunds.provided.card.expiry.month", selectedCard.getMonth())
                .set("sourceOfFunds.provided.card.expiry.year", year);

        paymentGateway.updateSession(sId, apiVer, request, new UpdateSessionCallback());
    }

    @Override
    public void on3DSecureCancel() {
        Toast.makeText(ShoppingCartActivity.this, "3DSecureCancel", Toast.LENGTH_LONG).show();
    }

    @Override
    public void on3DSecureError(String errorMessage) {
        Toast.makeText(ShoppingCartActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void on3DSecureComplete(String summaryStatus, String threeDSecureId) {
        doConfirm(threeDSecureId);
    }

    void doCheck3DSEnrollment() {
        amount = String.valueOf(priceToPay);
        // generate a random 3DSecureId for testing
        String threeDSId = UUID.randomUUID().toString();
        threeDSId = threeDSId.substring(0, threeDSId.indexOf('-'));
        apiController.check3DSecureEnrollment(sId, amount, currency, threeDSId, new Check3DSecureEnrollmentCallback());
    }

    void doConfirm() {
        doConfirm(null);
    }

    void doConfirm(String threeDSecureId) {
        amount = String.valueOf(priceToPay);
        apiController.completeSession(sId, orderId, transactionId, amount, currency, threeDSecureId, new CompleteSessionCallback());
    }

    private void updatePaymentStatus(int orderID, int paymentStatus, final boolean isPaid) {
        Constants.showSpinner(getString(R.string.completing_process), ShoppingCartActivity.this);
        WebServicesHandler apiClient = WebServicesHandler.instance;
        apiClient.updatePaymentStatus(orderID, paymentStatus, new Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                Constants.dismissSpinner();
                if (isPaid) {
                    Intent intent = new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
                    intent.putExtra("Response", placeOrderResponse);
                    String v1 = decimalFormat.format(priceToPay);
                    intent.putExtra("OrderTotal", v1);
                    startActivity(intent);
                    finish();
                } else {
                    Constants.showAlert(getString(R.string.failed), getString(R.string.server_error), getString(R.string.try_again), ShoppingCartActivity.this);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {

                Constants.dismissSpinner();
                progressBar.setVisibility(View.GONE);
                Constants.showAlert(getString(R.string.failed), getString(R.string.server_error), getString(R.string.try_again), ShoppingCartActivity.this);
            }
        });
    }

    private void isPaymentSuccessFull(int orderID) {
        Constants.showSpinner(getString(R.string.completing_process), ShoppingCartActivity.this);
        WebServicesHandler apiClient = WebServicesHandler.instance;

        apiClient.isPaymentSuccessful(orderID, new Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {

                Constants.dismissSpinner();
                SimpleSuccess simpleSuccess = response.body();

                if (simpleSuccess != null) {
                    if (simpleSuccess.getSuccess() == 1) {
                        Intent intent = new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
                        intent.putExtra("Response", placeOrderResponse);
                        String v1 = decimalFormat.format(priceToPay);
                        intent.putExtra("OrderTotal", v1);
                        startActivity(intent);
                        finish();
                    } else if (simpleSuccess.getSuccess() == 0) {
                        Constants.showAlert(getString(R.string.failed), getString(R.string.server_error), getString(R.string.try_again), ShoppingCartActivity.this);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                Constants.dismissSpinner();

                progressBar.setVisibility(View.GONE);
                Constants.showAlert(getString(R.string.failed), getString(R.string.server_error), getString(R.string.try_again), ShoppingCartActivity.this);
            }
        });
    }

    private void omanNetPaymentVerification(int orderID) {

        OmanNetServiceHandler.instance.invoice(orderID, selectedCard.getCardNumber()
                , selectedCard.getExpireDate().split("/")[0]
                , selectedCard.getExpireDate().split("/")[1]
                , selectedCard.getCardHolder()
                , selectedCard.getCvc()
                , new Callback<PaymentGateWay>() {
                    @Override
                    public void onResponse(Call<PaymentGateWay> call, Response<PaymentGateWay> response) {
                        try {
                            PaymentGateWay paymentGateWay = response.body();
                            if (paymentGateWay != null) {
                                if (paymentGateWay.getResult() != null) {
                                    if (paymentGateWay.getResult().getStatus() != null && paymentGateWay.getResult().getStatus().equalsIgnoreCase("success")) {
                                        if (paymentGateWay.getResult().getMessage() != null && paymentGateWay.getResult().getMessage().getPayUrl() != null && !paymentGateWay.getResult().getMessage().getPayUrl().isEmpty()) {
                                            Intent intent = new Intent(ShoppingCartActivity.this, OmanNetCardDetailActivity.class);
                                            intent.putExtra("PayURL", paymentGateWay.getResult().getMessage().getPayUrl());
                                            startActivityForResult(intent, 2019);
                                        }
                                    }
                                }

                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentGateWay> call, Throwable t) {
                    }
                });
    }

    private void getCountriesAPI() {
        WebServicesHandler.instance.getCountries(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject JObject = new JSONObject(response.body().string());
                    int status = 0;
                    status = JObject.getInt("success");
                    if (status == 0) {
                        getCountriesAPI();
                    } else if (status == 1) {
                        if (JObject.has("result")) {
                            JSONArray jsonArray = JObject.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Country country = new Country();

                                country.setId(jsonObject.getInt("ID"));
                                String flagURL = jsonObject.getString("FlagURL");

                                String[] array = flagURL.split("/");
                                String[] array2 = array[array.length - 1].split("\\.");
                                String[] array3 = array2[0].split("_");
                                String shortNameCode = array3[array3.length - 1];

                                country.setSortname(shortNameCode);
                                country.setName(jsonObject.getString("CountryName"));
                                country.setFlag(Constants.URLS.BaseURLImages + flagURL);
                                country.setPhoneCode(jsonObject.getString("CountryCode"));
                                GlobalValues.countries.add(country);
                            }
                        }
                    }
                    if (GlobalValues.countries != null) {
                        selectedCountry = GlobalValues.countries.get(0);
                        Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                        countryName.setText(selectedCountry.getName());
                        for (int i = 0; i < GlobalValues.countries.size(); i++) {
                        }
                    }
                    // countriesAdapter.notifyDataSetChanged();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    class CreateSessionCallback implements ApiController.CreateSessionCallback {
        @Override
        public void onSuccess(String sessionId, String apiVersion) {
            sId = sessionId;
            apiVer = apiVersion;

            if (requestAgain) {
                updateCardOnGateway();
                requestAgain = false;
            }
        }

        @Override
        public void onError(Throwable throwable) {
            Toast.makeText(ShoppingCartActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class UpdateSessionCallback implements GatewayCallback {

        @Override
        public void onSuccess(GatewayMap response) {
            Log.i(ShoppingCartActivity.class.getSimpleName(), "Successful pay");
        }

        @Override
        public void onError(Throwable throwable) {
            Toast.makeText(ShoppingCartActivity.this, R.string.pay_error_could_not_update_session, Toast.LENGTH_SHORT).show();
        }
    }

    class Check3DSecureEnrollmentCallback implements ApiController.Check3DSecureEnrollmentCallback {
        @Override
        public void onSuccess(String summaryStatus, String threeDSecureId, String html) {
            if ("CARD_ENROLLED".equalsIgnoreCase(summaryStatus)) {
                Gateway.start3DSecureActivity(ShoppingCartActivity.this, html);
            } else if ("CARD_NOT_ENROLLED".equalsIgnoreCase(summaryStatus) || "AUTHENTICATION_NOT_AVAILABLE".equalsIgnoreCase(summaryStatus)) {
                // for these 2 cases, you still provide the 3DSecureId with the pay operation
                doConfirm(threeDSecureId);
            } else {
                doConfirm();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    class CompleteSessionCallback implements ApiController.CompleteSessionCallback {
        @Override
        public void onSuccess(String result) {
            updatePaymentStatus(placeOrderResponse.getResult().getId(), 1, true);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            updatePaymentStatus(placeOrderResponse.getResult().getId(), 2, false);
        }
    }
}
