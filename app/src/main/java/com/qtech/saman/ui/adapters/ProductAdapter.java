package com.qtech.saman.ui.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.OptionValue;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetProduct;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.productdetail.ProductDetailActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Product> productArrayList = new ArrayList<>();
    private Context mContext;
    private int userID;
    private boolean isHome;
    Dialog dialog;
    private Product cartProduct;

    public ProductAdapter(Context mContext, List<Product> productArrayList, int userID, boolean isHome) {
        this.productArrayList = productArrayList;
        this.mContext = mContext;
        this.userID = userID;
        this.isHome = isHome;
    }

    @Override
    public int getItemViewType(int position) {
        return productArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_row, parent, false);
            return new ProductViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductViewHolder) {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = productArrayList.get(position);
            if (SamanApp.isEnglishVersion) {
                productViewHolder.productDescription.setText(product.getProductName());
                productViewHolder.storeName.setText(product.getStoreName());
            } else {
                productViewHolder.productDescription.setText(product.getProductNameAR());
                productViewHolder.storeName.setText(product.getStoreNameAR());
            }
            productViewHolder.productPrice.setText(product.getPrice() + " "+mContext.getString(R.string.OMR));

            if (product.getLogoURL() != null && !product.getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + product.getLogoURL())
                        .into(productViewHolder.productImageView);
            }
            else {
                Picasso.get().load(Constants.URLS.BaseURLImages).placeholder(R.drawable.no_image)
                        .into(productViewHolder.productImageView);
            }
//            else if(product.getProductImagesURLs().size()>0 && product.getProductImagesURLs().get(0)!=null && !product.getProductImagesURLs().get(0).isEmpty()){
//                Picasso.get().load(Constants.URLS.BaseURLImages + product.getProductImagesURLs().get(0))
//                        .into(productViewHolder.productImageView);
//            }

            productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID", productArrayList.get(productViewHolder.getAdapterPosition()).getID());
                    mContext.startActivity(intent);
                }
            });

            productViewHolder.cartImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (productArrayList.get(productViewHolder.getAdapterPosition()).getQuantity() != 0) {
                        getProductDetails(productArrayList.get(productViewHolder.getAdapterPosition()).getID());
                    } else {
                        Constants.showAlert(mContext.getString(R.string.title_my_bag),
                                mContext.getString(R.string.out_of_stock),
                                mContext.getString(R.string.cancel),
                                mContext);
                    }
                }
            });


            if (productArrayList.get(position).getFavorite()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    productViewHolder.favoriteImageView.setImageDrawable(mContext.getDrawable(R.drawable.fav));
                } else {
                    productViewHolder.favoriteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fav));
                }
            }

            productViewHolder.favoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (GlobalValues.getGuestLoginStatus(mContext)) {
                        Constants.showLoginDialog(mContext);
                        return;
                    }
                    User authenticatedUser = GlobalValues.getUser(mContext);
                    userID = authenticatedUser.getId();

                    if (productArrayList.get(productViewHolder.getAdapterPosition()).getFavorite()) {

                        showAlert(mContext.getString(R.string.ask_remove_from_fav), mContext.getString(R.string.remove_sure), productViewHolder.favoriteImageView, productViewHolder.getAdapterPosition());

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            productViewHolder.favoriteImageView.setImageDrawable(mContext.getDrawable(R.drawable.fav));
                        } else {
                            productViewHolder.favoriteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fav));
                        }
                        GlobalValues.markFavourite(userID, productArrayList.get(productViewHolder.getAdapterPosition()).getID(), null,1);
                        productArrayList.get(productViewHolder.getAdapterPosition()).setFavorite(true);

                        showPopUp(mContext.getString(R.string.added_to_fav),
                                mContext.getString(R.string.item_added_message),
                                mContext.getString(R.string.continue_shopping),
                                mContext.getString(R.string.view_fav),
                                1);
                    }
                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return productArrayList == null ? 0 : productArrayList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productDescription;
        private TextView productPrice;
        private TextView storeName;
        private ImageView productImageView;
        private ImageView favoriteImageView;
        private ImageView cartImageView;

        public ProductViewHolder(View v) {
            super(v);
            productImageView = (ImageView) v.findViewById(R.id.iv_product);
            productDescription = (TextView) v.findViewById(R.id.tv_product_description);
            storeName = (TextView) v.findViewById(R.id.tv_store_name);
            productPrice = (TextView) v.findViewById(R.id.tv_product_price);
            favoriteImageView = (ImageView) v.findViewById(R.id.iv_favorite);
            cartImageView = (ImageView) v.findViewById(R.id.iv_add_to_cart);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.native_progress_bar);
        }
    }

    private void showPopUp(String title, String message, String closeButtonText, String nextButtonText, final int type) {
        dialog = new Dialog(mContext, R.style.CustomDialog);
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
                    dialog.dismiss();
                    Constants.viewBag = true;
                    Intent intent = new Intent(mContext, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("NavItem", 3);
                    ((Activity) mContext).startActivity(intent);
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("NavItem", 2);
                    ((Activity) mContext).startActivity(intent);
                }
            }
        });


        Animation animation;
        animation = AnimationUtils.loadAnimation(mContext,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }


    private void getProductDetails(int productID) {
        WebServicesHandler.instance.getProductDetail(String.valueOf(productID), String.valueOf(userID), new retrofit2.Callback<GetProduct>() {
            @Override
            public void onResponse(Call<GetProduct> call, Response<GetProduct> response) {
                GetProduct getProduct = response.body();
                if (getProduct != null) {
                    if (getProduct.getSuccess() == 1) {
                        if (getProduct.getProduct() != null) {
                            cartProduct = getProduct.getProduct();
                            Log.e("DefaultOptions", getOptionsData());
                            if (SamanApp.localDB != null) {
                                if (cartProduct.getQuantity() != 0) {
                                    if (SamanApp.localDB.addToCart(cartProduct, getOptionsData(), getOptionsName(), getOptionsNameAR(), 1)) {
                                        showPopUp(mContext.getString(R.string.item_added_bag),
                                                mContext.getString(R.string.item_added_message),
                                                mContext.getString(R.string.continue_shopping),
                                                mContext.getString(R.string.view_bag),
                                                0);
                                    }
                                } else {
                                    Constants.showAlert(mContext.getString(R.string.title_my_bag),
                                            mContext.getString(R.string.out_of_stock),
                                            mContext.getString(R.string.cancel),
                                            mContext);
                                }
                            }
                            if (isHome) {
                                ((DashboardActivity) mContext).updateBagCount();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProduct> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }

    private String getOptionsData() {
        View v = null;
        OptionValue optionValue = null;
        String ids = "";
        if (cartProduct.getProductOptions() != null) {
            for (int i = 0; i < cartProduct.getProductOptions().size(); i++) {
                optionValue = cartProduct.getProductOptions().get(i).getOptionValues().get(0);
                if (ids.equals("")) {
                    ids = "" + optionValue.getID();
                } else {
                    ids = ids + "," + optionValue.getID();
                }
            }
        }
        return ids;
    }


    private String getOptionsName() {
        View v = null;
        OptionValue optionValue = null;
        String names = "";
        if (cartProduct.getProductOptions() != null) {
            for (int i = 0; i < cartProduct.getProductOptions().size(); i++) {
                optionValue = cartProduct.getProductOptions().get(i).getOptionValues().get(0);
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
        if (cartProduct.getProductOptions() != null) {
            for (int i = 0; i < cartProduct.getProductOptions().size(); i++) {
                optionValue = cartProduct.getProductOptions().get(i).getOptionValues().get(0);
                if (names.equals("")) {
                    names = "" + optionValue.getTitleAR();
                } else {
                    names = names + "," + optionValue.getTitleAR();
                }
            }
        }
        return names;
    }

    private void showAlert(String title, String message, final ImageView favoriteImageView, final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dislike(favoriteImageView, position);
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void dislike(ImageView favoriteImageView, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            favoriteImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_fav_b));
        } else {
            favoriteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_b));
        }
        GlobalValues.markUnFavourite(userID, productArrayList.get(position).getID());
        productArrayList.get(position).setFavorite(false);
//        showPopUp(mContext.getString(R.string.removed_from_fav),
//                mContext.getString(R.string.item_added_message),
//                mContext.getString(R.string.continue_shopping),
//                mContext.getString(R.string.view_fav),
//                1);
    }
}
