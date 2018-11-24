package com.algorepublic.saman.ui.activities.search;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetProducts;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.ProductAdapter;
import com.algorepublic.saman.ui.fragments.store.OnLoadMoreListener;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_empty)
    TextView empty;
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
    @BindView(R.id.native_progress_bar)
    ProgressBar progressBar;
    RecyclerView.LayoutManager productLayoutManager;
    List<Product> displayData = new ArrayList<>();
    ProductAdapter productAdapter;
    Dialog dialog;

    CheckBox highPrice;
    CheckBox lowPrice;
    CheckBox newIn;
    CheckBox bestSell;
    boolean isHighPrice=true;
    boolean isLowPrice;
    boolean isNewIn;
    boolean isBestSell;

    User authenticatedUser;

    String[] d={"PHONE FINDER","SAMSUNG","APPLE","NOKIA","SONY","LG","MOTOROLA","GOOGLE","BLACKBERRY"};

    int currentPage = 0;
    int pageSize = 20;
    int sortType = 1;
    boolean isGetAll = false;
    String query;

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
        setProductAdapter();
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    if(searchEditText.getText()!=null && !searchEditText.getText().toString().isEmpty()) {
                        progressBar.setVisibility(View.VISIBLE);
                        currentPage=0;
                        isGetAll=false;
                        displayData.clear();
                        query = searchEditText.getText().toString();
                        searchProduct(query, currentPage, pageSize,sortType);
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        return true;
                    }else {
                        Constants.showAlert(getString(R.string.search),getString(R.string.search_query),getString(R.string.okay),SearchActivity.this);
                    }
                }
                return false;
            }
        });
    }

    private void searchProduct(String query,int pageIndex,int pageSize,int sortType) {
        WebServicesHandler.instance.getSearchProducts(authenticatedUser.getId(),query,sortType,pageIndex,pageSize,new retrofit2.Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                progressBar.setVisibility(View.GONE);
                GetProducts getProducts = response.body();
                if (getProducts != null) {
                    if (getProducts.getSuccess() == 1){
                        if (displayData.size() > 0 && displayData.get(displayData.size() - 1)==null) {
                            displayData.remove(displayData.size() - 1);
                            productAdapter.notifyItemRemoved(displayData.size());
                        }
                        if(getProducts.getProduct()!=null && getProducts.getProduct().size()>0) {
                            displayData.addAll(getProducts.getProduct());
                            productAdapter.notifyDataSetChanged();
                        }else {
                            isGetAll=true;
                        }
                        isLoading = false;
                    }
                }
                searchRecyclerView.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);

                if(displayData.size()>0){
                    empty.setVisibility(View.GONE);
                }else {
                    empty.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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

        if(isHighPrice) {
            highPrice.setChecked(true);
        }else if (isLowPrice){
            lowPrice.setChecked(true);
        }else if (isNewIn){
            newIn.setChecked(true);
        }else if (isBestSell){
            bestSell.setChecked(true);
        }

        highPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isHighPrice=true;
                    isNewIn=false;
                    isBestSell=false;
                    isLowPrice=false;
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
                    isHighPrice=false;
                    isNewIn=false;
                    isBestSell=false;
                    isLowPrice=true;
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
                    isHighPrice=false;
                    isNewIn=true;
                    isBestSell=false;
                    isLowPrice=false;
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
                    isHighPrice=false;
                    isNewIn=false;
                    isBestSell=true;
                    isLowPrice=false;
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
                if(isHighPrice){
                    sortType=1;
                }else if (isLowPrice){
                    sortType=2;
                }else if (isNewIn){
                    sortType=3;
                }else if (isBestSell){
                    sortType=4;
                }
                progressBar.setVisibility(View.VISIBLE);
                displayData.clear();
                isLoading = true;
                currentPage=0;
                isGetAll=false;
                productAdapter.notifyDataSetChanged();
                searchProduct(query,currentPage,pageSize,sortType);
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

    private void setProductAdapter() {
        productLayoutManager = new GridLayoutManager(this, 2);
        searchRecyclerView.setLayoutManager(productLayoutManager);
        searchRecyclerView.setNestedScrollingEnabled(false);
        displayData = new ArrayList<>();
        productAdapter = new ProductAdapter(this, displayData,authenticatedUser.getId());
        searchRecyclerView.setAdapter(productAdapter);
        searchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        searchRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private boolean isLoading;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int visibleThreshold = 2;
            int lastVisibleItem, totalItemCount;
            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

            if (!isGetAll && !isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                displayData.add(null);
                productAdapter.notifyItemInserted(displayData.size() - 1);
                isLoading = true;
                currentPage++;
                searchProduct(query,currentPage,pageSize,sortType);
            }
        }
    };

//    HighToLow = 1,
//    LowToHigh = 2,
//    Latest = 3,
//    BestSell = 4
}
