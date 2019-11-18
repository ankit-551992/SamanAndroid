package com.qtech.saman.ui.activities.productdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.OptionValue;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.ProductAttribute;
import com.qtech.saman.data.model.ProductOption;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends BaseActivity implements ProductContractor.View {

    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar_back)
//    ImageView toolbarBack;
    @BindView(R.id.viewpager)
    ViewPager mPager;
    @BindView(R.id.options_layout)
    LinearLayout optionsLinearLayout;

    //Product
    @BindView(R.id.tv_product_name)
    TextView productName;
    @BindView(R.id.tv_store_name)
    TextView storeName;
    @BindView(R.id.tv_product_description)
    TextView productDescription;
    @BindView(R.id.tv_product_price)
    TextView productPrice;
    @BindView(R.id.tv_sale_price)
    TextView salePrice;
    @BindView(R.id.tv_product_count)
    TextView productCount;
    @BindView(R.id.iv_favorite)
    ImageView favoriteImageView;
    @BindView(R.id.iv_add_to_cart)
    ImageView addToCart;
    @BindView(R.id.iv_add_quantity)
    ImageView addQuantity;
    @BindView(R.id.iv_remove_quantity)
    ImageView removeQuantity;
    @BindView(R.id.tv_out_of_stock)
    TextView outOfStockTextView;

    @BindView(R.id.button_notify)
    Button button_notify;

    //Product
    int productID;
    ArrayList<String> urls;
    CustomPagerAdapter customPagerAdapter;

    Product product;
    User authenticatedUser;

    @BindView(R.id.layout_specifications_parent)
    LinearLayout specificationParentLayout;
    @BindView(R.id.layout_specifications)
    LinearLayout specificationsLayout;

    ProductContractor.Presenter presenter;
    @BindView(R.id.loading)
    RelativeLayout loading;

    LayoutInflater inflater;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5 * 1000; // time in milliseconds between successive task executions.

    boolean fromFavorite = false;

    String selectedOptions = "";
    String[] optionIDs;
    int selectedQuantity = -1;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        authenticatedUser = GlobalValues.getUser(this);
//        if (getIntent().hasExtra("ProductID1")) {
//            String productID = getIntent().getStringExtra("ProductID1");
//        }

        if (getIntent().hasExtra("ProductID")) {
            productID = getIntent().getIntExtra("ProductID", 1);
        }

        if (getIntent().hasExtra("Favorites")) {
            fromFavorite = getIntent().getBooleanExtra("Favorites", false);
        }

        if (getIntent().hasExtra("Quantity")) {
            selectedQuantity = getIntent().getIntExtra("Quantity", -1);
        }

        if (getIntent().hasExtra("Options")) {
            selectedOptions = getIntent().getStringExtra("Options");
            optionIDs = selectedOptions.split(",");

            for (int o = 0; o < optionIDs.length; o++) {
                Log.e(o + "-Options", optionIDs[o]);
            }
        }

        Intent intentShare = getIntent();
        String action = intentShare.getAction();
        Uri data = intentShare.getData();
        if (data != null) {
            String id = data.getQueryParameter("id");
            if (id != null) {
                productID = Integer.valueOf(id);
            } else {
                finish();
            }
        }
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        presenter = new ProductPresenter(this);

//        toolbarTitle.setVisibility(View.GONE);
//        toolbarBack.setVisibility(View.VISIBLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
//        } else {
//            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
//        }
        urls = new ArrayList<>();
        customPagerAdapter = new CustomPagerAdapter(this, urls);
        mPager.setAdapter(customPagerAdapter);
        presenter.getProductData(productID, authenticatedUser.getId());
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
        if (GlobalValues.getGuestLoginStatus(ProductDetailActivity.this)) {
            Constants.showLoginDialog(ProductDetailActivity.this);
            return;
        }

        if (!allOptionsSelected()) {
//          showPopUp();
            Constants.showErrorPopUp(ProductDetailActivity.this, getString(R.string.error), getString(R.string.missing_options), getString(R.string.okay));
            return;
        }

        authenticatedUser = GlobalValues.getUser(ProductDetailActivity.this);
        if (product.getFavorite()) {
//          showAlert(getString(R.string.ask_remove_from_fav), getString(R.string.remove_sure), 1);
            showPopUp(getString(R.string.ask_remove_from_fav), getString(R.string.remove_sure), getString(R.string.no),
                    getString(R.string.yes), 1);
        } else {
            String[] optionIDs = getOptionsData().split(",");
            presenter.markFavorite(authenticatedUser.getId(), productID, optionIDs, Integer.parseInt(productCount.getText().toString()));
            product.setFavorite(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            } else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
          /* showPopUp(getString(R.string.added_to_fav),
                    getString(R.string.item_added_message),
                    getString(R.string.continue_shopping),
                    getString(R.string.view_fav),
                    1);*/
            Constants.showCustomPopUp(ProductDetailActivity.this, getString(R.string.added_to_fav),
                    getString(R.string.item_added_message),
                    getString(R.string.continue_shopping),
                    getString(R.string.view_fav),
                    1);
        }
    }

    @OnClick(R.id.iv_add_to_cart)
    public void addToCart() {
        if (product == null) {
            return;
        }

        if (!allOptionsSelected()) {
//          showPopUp();
            Constants.showErrorPopUp(ProductDetailActivity.this, getString(R.string.error), getString(R.string.missing_options), getString(R.string.okay));
            return;
        }

        if (SamanApp.localDB != null) {
            if (product.getQuantity() != 0) {
                if (product.getQuantity() >= Integer.parseInt(productCount.getText().toString())) {
                    if (SamanApp.localDB.addToCart(product, getOptionsData(), getOptionsName(), getOptionsNameAR(), Integer.parseInt(productCount.getText().toString()))) {
                      /* showPopUp(getString(R.string.item_added_bag),
                                getString(R.string.item_added_message),
                                getString(R.string.continue_shopping),
                                getString(R.string.view_bag),
                                0);*/
                        Constants.showCustomPopUp(ProductDetailActivity.this, getString(R.string.item_added_bag),
                                getString(R.string.item_added_message),
                                getString(R.string.continue_shopping),
                                getString(R.string.view_bag),
                                0);
                    }
                } else {
                    String text = String.format(getString(R.string.items_available_count), product.getQuantity());

                    Constants.showAlert(getString(R.string.title_my_bag),
                            text,
                            getString(R.string.cancel),
                            this);
                }
            } else {
                Constants.showAlert(getString(R.string.title_my_bag),
                        getString(R.string.out_of_stock),
                        getString(R.string.cancel),
                        this);
            }
        }
    }

    @OnClick(R.id.button_notify)
    public void notifyItem() {
//        showAlert(getString(R.string.out_of_stock_title), getString(R.string.out_of_stock_message), 0);
        showPopUp(getString(R.string.out_of_stock_title), getString(R.string.out_of_stock_message), getString(R.string.no),
                getString(R.string.yes), 0);
    }

    @OnClick(R.id.iv_share)
    public void share() {
        inviteFriends();
//        Constants.showAlert(getString(R.string.sorry), getString(R.string.no_implemented), getString(R.string.close), ProductDetailActivity.this);
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
        Log.e("PRODCTID", "--getQuantity---" + product.getQuantity());
        int current = Integer.parseInt(productCount.getText().toString());
        if (product.getQuantity() > current) {
            current++;
            productCount.setText(String.valueOf(current));
        } else {
            String text = String.format(getString(R.string.items_available_count), product.getQuantity());
            Constants.showAlert(getString(R.string.title_my_bag),
                    text,
                    getString(R.string.cancel),
                    this);
        }
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
            }
        }, 1500);
    }

    @Override
    public void response(Product product) {
        this.product = product;
        if (selectedQuantity != -1) {
            //this.product.setQuantity(selectedQuantity);
            productCount.setText("" + selectedQuantity);
        }
        if (SamanApp.isEnglishVersion) {
            productName.setText(product.getProductName());
            storeName.setText(product.getStoreName());
        } else {
            productName.setText(product.getProductNameAR());
            storeName.setText(product.getStoreNameAR());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (SamanApp.isEnglishVersion) {
//                productDescription.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                productDescription.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            } else {
//              productDescription.setText(Html.fromHtml(product.getDescriptionAR(), Html.FROM_HTML_MODE_COMPACT));
                productDescription.setText(Html.fromHtml(product.getDescriptionAR(), Html.FROM_HTML_MODE_LEGACY));
            }
        } else {
            if (SamanApp.isEnglishVersion) {
                productDescription.setText(Html.fromHtml(product.getDescription()));
            } else {
                productDescription.setText(Html.fromHtml(product.getDescriptionAR()));
            }
        }

        String atributes = "";
        if (product.getProductAttributes() == null || product.getProductAttributes().size() < 1) {
            specificationParentLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < product.getProductAttributes().size(); i++) {
//            if (atributes.equals("")) {
//                if (SamanApp.isEnglishVersion) {
//                    atributes = product.getProductAttributes().get(i).getTitle()+" : "+product.getProductAttributes().get(i).getValue();
//                } else {
//                    atributes = product.getProductAttributes().get(i).getTitleAR()+" : "+product.getProductAttributes().get(i).getValueAR();
//                }
//            } else {
//                if (SamanApp.isEnglishVersion) {
//                    atributes = atributes + "\n" + product.getProductAttributes().get(i).getTitle()+" : "+product.getProductAttributes().get(i).getValue();
//                } else {
//                    atributes = atributes + "\n" + product.getProductAttributes().get(i).getTitleAR()+" : "+product.getProductAttributes().get(i).getValueAR();
//                }
//            }
            ProductAttribute productAttribute = product.getProductAttributes().get(i);
            View child = inflater.inflate(R.layout.item_specifications_row, null);
            TextView name = (TextView) child.findViewById(R.id.tv_attributeName);
            TextView value = (TextView) child.findViewById(R.id.tv_attributeValue);

            if (SamanApp.isEnglishVersion) {
                name.setText(productAttribute.getTitle());
                value.setText(productAttribute.getValue());
            } else {
                name.setText(productAttribute.getTitleAR());
                value.setText(productAttribute.getValueAR());
            }
            specificationsLayout.addView(child);
        }

//        if (!atributes.equals("")) {
//            attributes.setText(atributes);
//        } else {
//            attributes.setText(getString(R.string.no_specifications));
//        }
        if (product.getQuantity() <= 0) {
            // addToCart.setEnabled(false);
            removeQuantity.setEnabled(false);
            addQuantity.setEnabled(false);
            outOfStockTextView.setVisibility(View.VISIBLE);
            productCount.setText("0");

            if (product.getIsNotificationSubscribed().equals("true")) {
                button_notify.setVisibility(View.GONE);
            } else {
                button_notify.setVisibility(View.VISIBLE);
            }
        }

        if (product.getIsSaleProduct().equals("true")) {
            salePrice.setVisibility(View.VISIBLE);
            salePrice.setText(product.getSalePrice() + " " + getString(R.string.OMR));
            // productPrice.setText(product.getPrice() + " " + getString(R.string.OMR));
            productPrice.setText(product.getPrice() + " ");
            productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            salePrice.setVisibility(View.GONE);
            productPrice.setText(product.getPrice() + " " + getString(R.string.OMR));
        }
        //productPrice.setText(product.getPrice() + " " + getString(R.string.OMR));

        if (product.getProductOptions() != null) {
            for (int p = 0; p < product.getProductOptions().size(); p++) {
                ProductOption productOption = product.getProductOptions().get(p);
                View child = inflater.inflate(R.layout.item_options_row, null);
                TextView optionName = (TextView) child.findViewById(R.id.tv_option_name);
                Spinner optionValuesSpinner = (Spinner) child.findViewById(R.id.spinner_option_value);
                if (SamanApp.isEnglishVersion) {
                    optionName.setText(productOption.getTitle());
                } else {
                    optionName.setText(productOption.getTitleAR());
                }
                if (productOption.getOptionValues() != null) {
                    List<OptionValue> optionsList = productOption.getOptionValues();
                    OptionValue value = new OptionValue();
                    value.setID(-200);
                    value.setTitle(getString(R.string.select));
                    value.setTitleAR(getString(R.string.select));
                    optionsList.add(0, value);

                    ArrayAdapter valuesAdapter = new ArrayAdapter(ProductDetailActivity.this, android.R.layout.simple_spinner_item, optionsList);
                    optionValuesSpinner.setAdapter(valuesAdapter);
                }
                for (int i = 0; i < productOption.getOptionValues().size(); i++) {
                    if (optionIDs != null) {
                        if (optionIDs.length == product.getProductOptions().size()) {
                            if (productOption.getOptionValues().get(i).getID() == Integer.valueOf(optionIDs[p])) {
                                optionValuesSpinner.setSelection(i);
                            }
                        }
                    }
                }
//              optionValuesSpinner.setPrompt(getString(R.string.select));
                optionsLinearLayout.addView(child);
            }
        }

        if (product.getFavorite()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            } else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        }
        for (int i = 0; i < product.getProductImagesURLs().size(); i++) {
            urls.add(product.getProductImagesURLs().get(i));
        }

        if (urls.size() == 0) {
            urls.add("https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg?auto=compress&cs=tinysrgb&h=350");
        }
        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                mPager.setCurrentItem(currentPage, true);
//                currentPage++;
//                if (currentPage == urls.size()) {
//                    currentPage = 0;
//                }
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
        customPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(String message) {
    }

    @Override
    public void markFavoriteResponse(boolean success) {
        if (success) {
            product.setFavorite(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            } else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        } else {
            product.setFavorite(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.ic_fav_b));
            } else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_b));
            }
        }
    }

    @Override
    public void markUnFavoriteResponse(boolean success) {
        if (!success) {
            product.setFavorite(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.fav));
            } else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            }
        } else {
            product.setFavorite(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                favoriteImageView.setImageDrawable(getDrawable(R.drawable.ic_fav_b));
            } else {
                favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_b));
            }
        }
    }

    private String getOptionsData() {
        View v = null;
        OptionValue optionValue = null;
        String ids = "";
        if (product.getProductOptions() != null) {
            for (int i = 0; i < product.getProductOptions().size(); i++) {
                v = optionsLinearLayout.getChildAt(i);
                Spinner spinner = (Spinner) ((RelativeLayout) v).getChildAt(1);
                optionValue = (OptionValue) spinner.getSelectedItem();

                if (ids.equals("")) {
                    ids = "" + optionValue.getID();
                } else {
                    ids = ids + "," + optionValue.getID();
                }
            }
        }
        return ids;
    }

    private boolean allOptionsSelected() {
        boolean isAllSelected = true;
        View v = null;
        OptionValue optionValue = null;
        String ids = "";
        if (product.getProductOptions() != null) {
            for (int i = 0; i < product.getProductOptions().size(); i++) {
                v = optionsLinearLayout.getChildAt(i);
                Spinner spinner = (Spinner) ((RelativeLayout) v).getChildAt(1);
                optionValue = (OptionValue) spinner.getSelectedItem();
                if (optionValue.getID() == -200) {
                    isAllSelected = false;
                }
            }
        }
        return isAllSelected;
    }

    private String getOptionsName() {
        View v = null;
        OptionValue optionValue = null;
        String names = "";
        if (product.getProductOptions() != null) {
            for (int i = 0; i < product.getProductOptions().size(); i++) {
                v = optionsLinearLayout.getChildAt(i);
                Spinner spinner = (Spinner) ((RelativeLayout) v).getChildAt(1);
                optionValue = (OptionValue) spinner.getSelectedItem();

                if (names.equals("")) {
                    names = "" + optionValue.getTitle();
                } else {
                    names = names + "," + optionValue.getTitle();
                }
            }
        }
        return names;
    }

    private String getOptionsNameAR() {
        View v = null;
        OptionValue optionValue = null;
        String names = "";
        if (product.getProductOptions() != null) {
            for (int i = 0; i < product.getProductOptions().size(); i++) {
                v = optionsLinearLayout.getChildAt(i);
                Spinner spinner = (Spinner) ((RelativeLayout) v).getChildAt(1);
                optionValue = (OptionValue) spinner.getSelectedItem();

                if (names.equals("")) {
                    names = "" + optionValue.getTitleAR();
                } else {
                    names = names + "," + optionValue.getTitleAR();
                }
            }
        }
        return names;
    }

    private void dislike() {
//        String[] optionIDs = getOptionsData().split(",");
        presenter.markUnFavorite(authenticatedUser.getId(), productID, null);
        product.setFavorite(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            favoriteImageView.setImageDrawable(getDrawable(R.drawable.ic_fav_b));
        } else {
            favoriteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_b));
        }
    }

    private void inviteFriends() {
//        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Lets Enjoy on Saman");
        intent.putExtra(Intent.EXTRA_TEXT, "Lets Enjoy Saman " + "https://www.saman.om/productSharing?deviceType=2&id=" + productID);
        startActivity(Intent.createChooser(intent, "Share Saman With Friends"));
    }

    @Override
    public void addProductNotifyResponse(SimpleSuccess simpleSuccess) {
        if (simpleSuccess.getResult().equals(true)) {
            Toast.makeText(this, "" + simpleSuccess.getMessage(), Toast.LENGTH_SHORT).show();
            button_notify.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "" + simpleSuccess.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopUp(String title, String message, String closeButtonText, String nextButtonText, final int type) {
        dialog = new Dialog(ProductDetailActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_information_pop_up);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_pop_up_close);
        Button closePopUp = (Button) dialog.findViewById(R.id.button_close_pop_up);
        Button nextButton = (Button) dialog.findViewById(R.id.button_pop_next);
        TextView titleTextView = (TextView) dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.tv_pop_up_message);

        titleTextView.setText(title);
        messageTextView.setText(message);
        closePopUp.setText(closeButtonText);
        nextButton.setText(nextButtonText);

        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    // call api for out of stock product
                    presenter.addProduct(authenticatedUser.getId(), productID);
                    dialog.dismiss();
                } else {
                    dislike();
                    dialog.dismiss();
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(ProductDetailActivity.this,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }
}
