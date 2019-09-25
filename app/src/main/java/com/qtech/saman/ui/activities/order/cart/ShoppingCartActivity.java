package com.qtech.saman.ui.activities.order.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.PlaceOrderResponse;
import com.qtech.saman.data.model.apis.PromoVerify;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.listeners.DialogOnClick;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends BaseActivity implements Gateway3DSecureCallback, DialogOnClick.OnDialogResponse {

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
    @BindView(R.id.tv_agreement_order)
    TextView agreementOrder;
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

    //Tags
    @BindView(R.id.tags_recyclerView)
    RecyclerView tagRecyclerView;
    RecyclerView.LayoutManager tagsLayoutManager;
    List<String> tagsList = new ArrayList<>();
    List<Integer> appliedProducts = new ArrayList<>();
    TagsAdapter tagsAdapter;

    Country selectedCountry;
    float price;
    float subTotal;
    float deliveryCost = 0.0f;
    float priceToPay;
    float promoSaved = 0.0f;
    float promoTotalSaved = 0.0f;

    int addressID = -1;

    PlaceOrderResponse placeOrderResponse;
    ApiController apiController = ApiController.getInstance();
    public String sId, apiVer;
    private CardDs selectedCard = null;
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
    DecimalFormat df = new DecimalFormat("#.#");
//    DecimalFormat df = new DecimalFormat("#.##");
//    DecimalFormat df = new DecimalFormat("#00.0#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.check_out));
        promoSavedTextView.setVisibility(View.GONE);
        price = (float) getIntent().getFloatExtra("Price", 0);
        subtotalTextView.setText(getString(R.string.subtotal) + " " + price + " " + getString(R.string.OMR));
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
        // random 3ds/order/txn IDs for example purposes
        orderId = UUID.randomUUID().toString();
        orderId = orderId.substring(0, orderId.indexOf('-'));
        transactionId = UUID.randomUUID().toString();
        transactionId = transactionId.substring(0, transactionId.indexOf('-'));

        GlobalValues.countries = new ArrayList<>();
        getCountriesAPI();

      /* for (int i = 0; i < GlobalValues.countries.size(); i++) {
            Log.e("COUNTRY", "---GlobalValues.countries----size---" + GlobalValues.countries.size());
            if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))) {
                selectedCountry = GlobalValues.countries.get(i);
                Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                countryName.setText(selectedCountry.getName());
            }
        }
*/
        authenticatedUser = GlobalValues.getUser(ShoppingCartActivity.this);

        if (authenticatedUser.getShippingAddress() != null) {

            Log.e("121212", "---" + authenticatedUser.getShippingAddress());
//            String address = authenticatedUser.getShippingAddress().getAddressLine1().replace(",", "\n\n");
            String address = authenticatedUser.getShippingAddress().getAddressLine1().replace(",", ", ");
//            address = address + "\n\n" + authenticatedUser.getShippingAddress().getCity();
            // address = address + "\n\n" + authenticatedUser.getShippingAddress().getCountry();
            address = address + ", " + authenticatedUser.getShippingAddress().getCity();

            address = address + ", " + authenticatedUser.getShippingAddress().getCountry();
            shipmentAddress.setText(address);
            addressID = authenticatedUser.getShippingAddress().getiD();
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
            Log.e(ShoppingCartActivity.class.getSimpleName(), "Invalid Gateway region value provided", e);
        }
        apiController.createSession(new CreateSessionCallback());
    }


    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getString(R.string.agreement_order));
        spanTxt.append(getString(R.string.term));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(ShoppingCartActivity.this, PoliciesActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        }, spanTxt.length() - getString(R.string.term).length(), spanTxt.length(), 0);
        spanTxt.append(" & ");
        spanTxt.append(getString(R.string.privacy));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(ShoppingCartActivity.this, PoliciesActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        }, spanTxt.length() - getString(R.string.privacy).length(), spanTxt.length(), 0);
        spanTxt.append(" " + getString(R.string.term_message));
        spanTxt.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanTxt.length(), 0);
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
            if (promoEditText.getText().toString().equalsIgnoreCase(tagsList.get(t))) {
                isNewPromo = false;
                break;
            }
        }

        if (!isNewPromo || isGeneralApplied) {
            Constants.showAlert(getString(R.string.apply_coupon), getString(R.string.already_apply), getString(R.string.close), ShoppingCartActivity.this);
            return;
        }

        Constants.showSpinner(getString(R.string.apply_coupon), ShoppingCartActivity.this);
        WebServicesHandler.instance.applyPromo(promoEditText.getText().toString(), new retrofit2.Callback<PromoVerify>() {
            @Override
            public void onResponse(Call<PromoVerify> call, Response<PromoVerify> response) {
                PromoVerify promoVerify = response.body();
                Log.e("PRODUCT888", "--promoVerify--" + promoVerify);
                Constants.dismissSpinner();
                if (promoVerify != null) {
                    if (promoVerify.getSuccess() == 1) {
                        Log.e("PRODUCT888", "--promoVerify--getSuccess-");
                        if (promoVerify.getResult().getCouponType() == 1) {

                            if (!isGeneralApplied) {
                                isGeneralApplied = true;

                                float promoAmount = 0.0f;
                                for (int p = 0; p < bagArrayList.size(); p++) {
                                    int productId = bagArrayList.get(p).getID();
                                    if (!appliedProducts.contains(productId)) {
                                        promoAmount = promoAmount + bagArrayList.get(p).getPrice();
                                    }
                                }

                                if (promoVerify.getResult().getDiscountType() == 1) {
                                    //Percentage
                                    float calculateDiscount = promoAmount / 100.0f;
                                    promoSaved = calculateDiscount * ((float) promoVerify.getResult().getDiscount());
                                } else if (promoVerify.getResult().getDiscountType() == 2) {
                                    //Price
                                    promoSaved = (float) promoVerify.getResult().getDiscount();
                                }
//                              promoSaved = Math.round(promoSaved);
                                Log.e("NUMBER000", "---promoSaved---" + promoSaved);
                                promoSaved = Float.valueOf(df.format(promoSaved));
                                Log.e("NUMBER000", "--format---promoSaved---" + promoSaved);

                                promoSavedTextView.setVisibility(View.VISIBLE);
                                promoTotalSaved = promoTotalSaved + promoSaved;
                                promoSavedTextView.setText(getString(R.string.promo_saved) + ": " + promoTotalSaved + " " + getString(R.string.OMR));
                                price = price - promoSaved;
                                priceToPay = price + deliveryCost;
                                priceToPayTextView.setText(getString(R.string.price_to_pay) + " : " + priceToPay + " " + getString(R.string.OMR));
                            } else {
                                Constants.showAlert(getString(R.string.apply_coupon), getString(R.string.already_apply), getString(R.string.close), ShoppingCartActivity.this);
                            }
                        } else {

                            for (int p = 0; p < bagArrayList.size(); p++) {

                                int productId = bagArrayList.get(p).getID();

                                if (promoVerify.getResult().getProductID().contains(productId)) {

                                    if (!appliedProducts.contains(productId)) {

                                        if (promoVerify.getResult().getDiscountType() == 1) {
                                            //Percentage
                                            float calculateDiscount = bagArrayList.get(p).getPrice() / 100.0f;
                                            float dis = calculateDiscount * ((float) promoVerify.getResult().getDiscount());
                                            Log.e("Dis", bagArrayList.get(p).getProductName() + " " + dis);
                                            promoSaved = promoSaved + dis;

                                        } else if (promoVerify.getResult().getDiscountType() == 2) {
                                            //Price
                                            float dis = (float) promoVerify.getResult().getDiscount();
                                            promoSaved = promoSaved + dis;
                                        }
                                        appliedProducts.add(productId);
                                    } else {
                                        Constants.showAlert(getString(R.string.apply_coupon), getString(R.string.already_apply_on_same_product), getString(R.string.close), ShoppingCartActivity.this);
                                    }
                                }
                            }

//                            promoSaved = Math.round(promoSaved);
                            promoSaved = Float.valueOf(df.format(promoSaved));

                            promoSavedTextView.setVisibility(View.VISIBLE);
                            promoTotalSaved = promoTotalSaved + promoSaved;
                            promoSavedTextView.setText(getString(R.string.promo_saved) + ": " + promoTotalSaved + " " + getString(R.string.OMR));
                            price = subTotal - promoSaved;
                            priceToPay = price + deliveryCost;
                            priceToPayTextView.setText(getString(R.string.price_to_pay) + " : " + priceToPay + " " + getString(R.string.OMR));
                        }
                        if (promoSaved > 0) {
                            tagsList.add(promoEditText.getText().toString());
                            setTagData();
                            String msg = getString(R.string.coupon_discount_is_) + " " + promoSaved + " " + getString(R.string.OMR);
                            Constants.showAlert(getString(R.string.coupon_discount), msg, getString(R.string.Okay), ShoppingCartActivity.this);
                        }

                    } else {

                        Constants.showAlert(getString(R.string.apply_coupon), promoVerify.getMessage(), getString(R.string.try_again), ShoppingCartActivity.this);
//                        Constants.showErrorPopUp(ShoppingCartActivity.this, getString(R.string.apply_coupon), promoVerify.getMessage(), getString(R.string.try_again));
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
            obj = new JSONObject();
            try {
                obj.put("ProductID", bagArrayList.get(p).getID());
//                obj.put("AttributeID", bagArrayList.get(p).getCartAttributeID());
                obj.put("ProductQuantity", bagArrayList.get(p).getQuantity());
                obj.put("ProductPrice", bagArrayList.get(p).getPrice());

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
                array,
                new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        placeOrderResponse = response.body();
                        if (placeOrderResponse != null) {
                            Log.e("ORDERPLACE", "--placeOrderResponse----" + placeOrderResponse);
                            Log.e("ORDERPLACE", "--placeOrder----response----" + response.body());
                            if (placeOrderResponse.getSuccess() == 1) {
                                if (isCOD) {
                                    Intent intent = new Intent(ShoppingCartActivity.this, CheckoutOrderActivity.class);
                                    intent.putExtra("Response", placeOrderResponse);
                                    intent.putExtra("OrderTotal", priceToPay);
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
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                        Log.e("onFailure", "" + t.getMessage());
                    }
                });
    }

    @Override
    public void OnResponseDialogClick(Context context, String response) {
        Log.e("2222", "--OnResponseDialogClick--response-" + response);
//        if (response.equals("nextclick")){

//        }
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
                String d = data.getExtras().getString("DATA");
                addressID = data.getExtras().getInt("ID");
                shipmentAddress.setText(d.replace(",", "\n\n"));
            }
        } else if (requestCode == 2019) {
            if (resultCode == RESULT_OK) {
                isPaymentSuccessFull(placeOrderResponse.getResult().getId());
            } else if (resultCode == RESULT_CANCELED) {
                progressBar.setVisibility(View.GONE);
            }
        }
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
//        layoutManager = new GridLayoutManager(this, 3);
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        bagArrayList = new ArrayList<>();
        bagCartAdapter = new BagCartAdapter(this, bagArrayList);
        bagRecyclerView.setAdapter(bagCartAdapter);
//      bagRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));

        if (SamanApp.localDB != null) {
            bagArrayList.addAll(SamanApp.localDB.getCartProducts());
            bagCartAdapter.notifyDataSetChanged();
//            Log.e("C",""+SamanApp.localDB.getCartAllProductsCounting());
            if (price < 35) {
//                switch (bagArrayList.size()) {
                switch (SamanApp.localDB.getCartAllProductsCounting()) {
                    case 1:
                        deliveryCost = 1.0f;
                        break;
                    case 2:
                        deliveryCost = 1.0f;
                        break;
                    case 3:
                        deliveryCost = 1.5f;
                        break;
                    case 4:
                        deliveryCost = 1.6f;
                        break;
                    default:
                        deliveryCost = 2.0f;
                        break;
                }

            }
            priceToPay = deliveryCost + price;
            priceToPay = priceToPay - promoSaved;
            deliveryCostTextView.setText(getString(R.string.delivery_cost) + " : " + deliveryCost + " " + getString(R.string.OMR));
            priceToPayTextView.setText(getString(R.string.price_to_pay) + " : " + priceToPay + " " + getString(R.string.OMR));
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

        Log.e("threeDSId", threeDSId);

        apiController.check3DSecureEnrollment(sId, amount, currency, threeDSId, new Check3DSecureEnrollmentCallback());
    }

    void doConfirm() {
        doConfirm(null);
    }

    void doConfirm(String threeDSecureId) {
        amount = String.valueOf(priceToPay);
        apiController.completeSession(sId, orderId, transactionId, amount, currency, threeDSecureId, new CompleteSessionCallback());
    }

    class CreateSessionCallback implements ApiController.CreateSessionCallback {
        @Override
        public void onSuccess(String sessionId, String apiVersion) {

            Log.i("CreateSessionTask", "Session established");
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
//            Toast.makeText(ShoppingCartActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }
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
                    intent.putExtra("OrderTotal", priceToPay);
                    startActivity(intent);
                    finish();
                } else {
                    Constants.showAlert(getString(R.string.failed), getString(R.string.server_error), getString(R.string.try_again), ShoppingCartActivity.this);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
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
                        intent.putExtra("OrderTotal", priceToPay);
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
                Log.e("onFailure", "" + t.getMessage());
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
                , new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject JObject = new JSONObject(response.body().string());
                            Log.e("Invoice", JObject.toString());
                            if (JObject != null) {
                                JSONObject result = JObject.getJSONObject("result");
                                if (result != null) {
                                    if (result.has("status")) {
                                        if (result.getString("status").equalsIgnoreCase("success")) {
                                            JSONObject message = result.getJSONObject("message");
                                            if (message != null) {
                                                if (message.has("PayUrl")) {
                                                    String payURL = message.getString("PayUrl");
                                                    if (payURL != null && !payURL.equals("")) {
                                                        Intent intent = new Intent(ShoppingCartActivity.this, OmanNetCardDetailActivity.class);
                                                        intent.putExtra("PayURL", payURL);
                                                        startActivityForResult(intent, 2019);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
    }

    private void getCountriesAPI() {
        Log.e("COUNTRYAPI", "--country---api---");
        WebServicesHandler.instance.getCountries(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject JObject = new JSONObject(response.body().string());
                    //   JSONObject JObject = new JSONObject(response.body().toString());
                    int status = 0;
                    status = JObject.getInt("success");

                    if (status == 0) {
                        getCountriesAPI();
                    } else if (status == 1) {
                        Log.e("COUNTRYAPI", "--country---api-status--" + status);
                        if (JObject.has("result")) {
                            JSONArray jsonArray = JObject.getJSONArray("result");
                            Log.e("COUNTRYAPI", "--country---api-jsonArray--" + jsonArray);

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
                        for (int i = 0; i < GlobalValues.countries.size(); i++) {
                            Log.e("COUNTRY", "---GlobalValues.countries----size---" + GlobalValues.countries.size());
                            selectedCountry = GlobalValues.countries.get(i);
                            Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                            countryName.setText(selectedCountry.getName());
                           /* if (GlobalValues.countries.get(i).getFlag().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))) {
                                selectedCountry = GlobalValues.countries.get(i);
                                Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                                countryName.setText(selectedCountry.getName());
                            }*/
                        }
                    }
                    Log.e("COUNTRYAPI", "-- GlobalValues.countries---api---" + GlobalValues.countries);
                    //   countriesAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void setCountry() {

        if (GlobalValues.countries != null) {
            for (int i = 0; i < GlobalValues.countries.size(); i++) {
                if (GlobalValues.countries.get(i).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(ShoppingCartActivity.this))) {
                    selectedCountry = GlobalValues.countries.get(i);
                    Picasso.get().load(selectedCountry.getFlag()).transform(new CircleTransform()).into(countryFlag);
                    countryName.setText(selectedCountry.getName());
                }
            }
        }
    }


}
