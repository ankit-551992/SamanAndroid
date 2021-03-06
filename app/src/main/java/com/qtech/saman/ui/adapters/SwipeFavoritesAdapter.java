package com.qtech.saman.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.qtech.saman.R;
import com.qtech.saman.data.model.OptionValue;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.productdetail.ProductDetailActivity;
import com.qtech.saman.ui.fragments.favourite.FavoritesFragment;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class SwipeFavoritesAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Product> productArrayList = new ArrayList<>();
    FavoritesFragment favoritesFragment;
    Dialog dialog;
    private Context mContext;
    private User authenticatedUser;
    private Product cartProduct;

    public SwipeFavoritesAdapter(Context mContext, List<Product> productArrayList, FavoritesFragment favoritesFragment) {
        this.productArrayList = productArrayList;
        this.mContext = mContext;
        this.favoritesFragment = favoritesFragment;
        authenticatedUser = GlobalValues.getUser(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return productArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_fav_swipe_row, parent, false);
            return new FavoritesViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FavoritesViewHolder) {
            FavoritesViewHolder favoritesViewHolder = (FavoritesViewHolder) holder;

            if (SamanApp.isEnglishVersion) {
                favoritesViewHolder.name.setText(productArrayList.get(position).getProductName() + " x " + productArrayList.get(position).getUserQuantity());
                favoritesViewHolder.storeName.setText(productArrayList.get(position).getStoreName());
            } else {
                favoritesViewHolder.name.setText(productArrayList.get(position).getProductNameAR() + " x " + productArrayList.get(position).getUserQuantity());
                favoritesViewHolder.storeName.setText(productArrayList.get(position).getStoreNameAR());
            }
//            favoritesViewHolder.price.setText(productArrayList.get(position).getPrice() + " OMR");

            if (productArrayList.get(position).getProductOptions() != null) {
                if (SamanApp.isEnglishVersion) {
                    favoritesViewHolder.price.setText(getOptionsName(productArrayList.get(position)));
                } else {
                    favoritesViewHolder.price.setText(getOptionsNameAR(productArrayList.get(position)));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    favoritesViewHolder.price.setText(Html.fromHtml(productArrayList.get(position).getDescription(),
                            Html.FROM_HTML_MODE_COMPACT));
                } else {
                    favoritesViewHolder.price.setText(Html.fromHtml(productArrayList.get(position).getDescription()));
                }
            }

            if (productArrayList.get(position).getLogoURL() != null && !productArrayList.get(position).getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + productArrayList.get(position).getLogoURL())
                        .into(favoritesViewHolder.productImageView);
            }

            // Drag From Right
            favoritesViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, favoritesViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
            favoritesViewHolder.favIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_apps));
            favoritesViewHolder.textView1.setText(mContext.getString(R.string.add_to_cart));
            favoritesViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            favoritesViewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID", productArrayList.get(position).getID());
                    intent.putExtra("Favorites", true);
                    intent.putExtra("Options", getOptionsData(productArrayList.get(position)));
                    intent.putExtra("Quantity", productArrayList.get(position).getUserQuantity());
                    mContext.startActivity(intent);
                }
            });

            favoritesViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                }

                @Override
                public void onOpen(SwipeLayout layout) {

                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
            favoritesViewHolder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 /*  if (productArrayList.get(position).getAvailableQuantity() > 0) {
                        String[] optionIDs = getOptionsData(productArrayList.get(position)).split(",");
                        getProductDetails(productArrayList.get(position));
                        markUnFavourite(authenticatedUser.getId(), productArrayList.get(position).getID(), optionIDs);
                        productArrayList.remove(position);
                        ((DashboardActivity) mContext).updateFavCount(productArrayList.size());
                        favoritesFragment.updateCount(productArrayList.size());
                        mItemManger.closeAllItems();
                    } else {
                        Constants.showAlert(mContext.getResources().getString(R.string.title_fav),
                                mContext.getResources().getString(R.string.out_of_stock),
                                mContext.getResources().getString(R.string.cancel),
                                mContext);
                        mItemManger.closeAllItems();
                    }*/
                    boolean isOutOfStock = false;
                    int count = 0;
                    if (SamanApp.localDB != null) {
                        ArrayList<Product> arrayLst = new ArrayList<>();
                        arrayLst = SamanApp.localDB.getCartProducts();
                        for (int i = 0; i < arrayLst.size(); i++) {
                            Product p = arrayLst.get(i);
                            if (p.getID().equals(productArrayList.get(position).getID())) {
                                count += p.getQuantity();
                                isOutOfStock = p.getQuantity() + productArrayList.get(position).getUserQuantity() > productArrayList.get(position).getQuantity();
                            }
                        }
                    }
                    if (productArrayList.get(position).getQuantity() >= productArrayList.get(position).getUserQuantity() + count) {
                        String[] optionIDs = getOptionsData(productArrayList.get(position)).split(",");
                        getProductDetails(productArrayList.get(position));
                        markUnFavourite(authenticatedUser.getId(), productArrayList.get(position).getID(), optionIDs);
                        productArrayList.remove(position);
                        ((DashboardActivity) mContext).updateFavCount(productArrayList.size());
                        favoritesFragment.updateCount(productArrayList.size());
                        mItemManger.closeAllItems();
                    } else {
//                        Constants.showAlert(mContext.getResources().getString(R.string.title_fav),
//                                mContext.getResources().getString(R.string.out_of_stock),
//                                mContext.getResources().getString(R.string.cancel),
//                                mContext);
                        Constants.showErrorPopUp(mContext, mContext.getResources().getString(R.string.title_fav), mContext.getResources().getString(R.string.out_of_stock), mContext.getResources().getString(R.string.okay));
                    }
                }
            });

            favoritesViewHolder.layout2.setOnClickListener(view -> {

//                    String[] optionIDs = getOptionsData(productArrayList.get(position)).split(",");
//                    markUnFavourite(authenticatedUser.getId(), productArrayList.get(position).getID(), optionIDs);
//                    productArrayList.remove(position);
//                    notifyDataSetChanged();
//                    ((DashboardActivity) mContext).updateFavCount(productArrayList.size());
//                    favoritesFragment.updateCount(productArrayList.size());
//                    mItemManger.closeAllItems();

//                    showPopUp(mContext.getString(R.string.removed_from_fav),
//                            mContext. getString(R.string.item_added_message),
//                            mContext.getString(R.string.okay),
//                            mContext.getString(R.string.view_fav),
//                            1,position);
                showPopUp(mContext.getString(R.string.removed_from_fav),
                        "",
                        mContext.getString(R.string.no),
                        mContext.getString(R.string.yes),
                        1, position);
                mItemManger.closeAllItems();
            });
            // mItemManger is member in RecyclerSwipeAdapter Class
            mItemManger.bindView(favoritesViewHolder.itemView, position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return productArrayList == null ? 0 : productArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void showPopUp(String title, String message, String closeButtonText, String nextButtonText, final int type, int position) {
        dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_information_pop_up);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.iv_pop_up_close);
        Button closePopUp = dialog.findViewById(R.id.button_close_pop_up);
        Button nextButton = dialog.findViewById(R.id.button_pop_next);
        TextView titleTextView = dialog.findViewById(R.id.tv_pop_up_title);
        TextView messageTextView = dialog.findViewById(R.id.tv_pop_up_message);

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

        nextButton.setOnClickListener(view -> {
            String[] optionIDs = getOptionsData(productArrayList.get(position)).split(",");
            markUnFavourite(authenticatedUser.getId(), productArrayList.get(position).getID(), optionIDs);
            productArrayList.remove(position);
            notifyDataSetChanged();
            ((DashboardActivity) mContext).updateFavCount(productArrayList.size());
            favoritesFragment.updateCount(productArrayList.size());
//                mItemManger.closeAllItems();
            dialog.dismiss();
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(mContext,
                R.anim.fade_in);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    private void getProductDetails(Product product1) {
        cartProduct = product1;
        if (SamanApp.localDB != null) {

            if (cartProduct.getQuantity() != 0) {
                if (cartProduct.getQuantity() >= product1.getUserQuantity()) {
                    if (SamanApp.localDB.addToCart(cartProduct, getOptionsData(), getOptionsName(), getOptionsNameAR(), product1.getUserQuantity(), product1.getProductDiscountPrice())) {
                        Constants.showCustomPopUp(mContext, mContext.getString(R.string.item_added_bag),
                                mContext.getString(R.string.item_added_message),
                                mContext.getString(R.string.continue_shopping),
                                mContext.getString(R.string.view_bag),
                                0, 0);
                    }
                }
            }
        }
        ((DashboardActivity) mContext).updateBagCount();
        notifyDataSetChanged();
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

    private String getOptionsData(Product product1) {
        View v = null;
        OptionValue optionValue = null;
        String ids = "";
        if (product1.getProductOptions() != null) {
            for (int i = 0; i < product1.getProductOptions().size(); i++) {
                optionValue = product1.getProductOptions().get(i).getOptionValues().get(0);
                if (ids.equals("")) {
                    ids = "" + optionValue.getID();
                } else {
                    ids = ids + "," + optionValue.getID();
                }
            }
        }
        return ids;
    }

    private String getOptionsName(Product product1) {
        View v = null;
        OptionValue optionValue = null;
        String names = "";
        if (product1.getProductOptions() != null) {
            for (int i = 0; i < product1.getProductOptions().size(); i++) {
                optionValue = product1.getProductOptions().get(i).getOptionValues().get(0);
                if (names.equals("")) {
                    names = "" + optionValue.getTitle();
                } else {
                    names = names + "," + optionValue.getTitle();
                }
            }
        }
        return names;
    }

    private String getOptionsNameAR(Product product1) {
        View v = null;
        OptionValue optionValue = null;
        String names = "";
        if (product1.getProductOptions() != null) {
            for (int i = 0; i < product1.getProductOptions().size(); i++) {
                optionValue = product1.getProductOptions().get(i).getOptionValues().get(0);
                if (names.equals("")) {
                    names = "" + optionValue.getTitleAR();
                } else {
                    names = names + "," + optionValue.getTitleAR();
                }
            }
        }
        return names;
    }

    private void markUnFavourite(int userID, int productId, String[] optionIds) {
        WebServicesHandler.instance.markUnFavourite(userID, productId, optionIds, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
                if (response != null) {
                }
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {

            }
        });
    }

    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.swipe)
        SwipeLayout swipeLayout;

        @BindView(R.id.layout1)
        LinearLayout layout1;

        @BindView(R.id.layout2)
        LinearLayout layout2;

        @BindView(R.id.iv_icon1)
        ImageView favIcon;

        @BindView(R.id.tv_1)
        TextView textView1;

        @BindView(R.id.tv_product_name)
        TextView name;
        @BindView(R.id.tv_store_name)
        TextView storeName;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.iv_product)
        ImageView productImageView;

        public FavoritesViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.native_progress_bar);
        }
    }
}
