package com.algorepublic.saman.ui.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.fragments.bag.BagFragment;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwipeBagAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Product> productArrayList;
    private Context mContext;
    private BagFragment bagFragment;
    private float grandTotal = 0;

    private User authenticatedUser;

    public SwipeBagAdapter(Context mContext, List<Product> productArrayList, BagFragment bagFragment) {
        this.productArrayList = productArrayList;
        this.mContext = mContext;
        this.bagFragment = bagFragment;
        authenticatedUser = GlobalValues.getUser(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return productArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_bag_swipe_row, parent, false);

            return new BagViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BagViewHolder) {
            BagViewHolder bagViewHolder = (BagViewHolder) holder;

            bagViewHolder.getPosition = holder.getAdapterPosition();
            Product product = productArrayList.get(bagViewHolder.getPosition);
            if(SamanApp.isEnglishVersion) {
                bagViewHolder.name.setText(product.getProductName());
                bagViewHolder.storeName.setText(product.getStoreName());
            }else {
                bagViewHolder.name.setText(product.getProductNameAR());
                bagViewHolder.storeName.setText(product.getStoreNameAR());
            }
            if (product.getOptions() != null && !product.getOptions().isEmpty() && !product.getOptions().equals("")) {
                if(SamanApp.isEnglishVersion) {
                    bagViewHolder.description.setText(product.getOptions());
                }else {
//                    Log.e("DES",product.getOptionsAR());
//                    product.setOptionsAR(product.getOptionsAR().replaceAll("،","U+060C"));
                    bagViewHolder.description.setText(product.getOptionsAR());
                }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    bagViewHolder.description.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    bagViewHolder.description.setText(Html.fromHtml(product.getDescription()));
                }
            }
            if (product.getLogoURL() != null && !product.getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + product.getLogoURL())
                        .into(bagViewHolder.productImageView);
            }


            bagViewHolder.price.setText(product.getPrice() + " OMR");
            float total = product.getPrice() * product.getQuantity();
            grandTotal = grandTotal + total;
            bagViewHolder.total.setText(total + " OMR");
            bagViewHolder.quantity.setText(String.valueOf(product.getQuantity()));


            bagFragment.updateTotal(grandTotal, 0);


            // Drag From Right
            bagViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bagViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));

            bagViewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID", productArrayList.get(position).getID());
                    mContext.startActivity(intent);
                }
            });

            bagViewHolder.favIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border));
            bagViewHolder.textView1.setText(mContext.getString(R.string.add_to_fav));
            bagViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            bagViewHolder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (GlobalValues.getGuestLoginStatus(mContext)) {
                        Constants.showLoginDialog(mContext);
                        return;
                    }
                    authenticatedUser = GlobalValues.getUser(mContext);
                    Log.e("Values " + position, productArrayList.get(position).getOptionValues());
                    String[] optionIDs = productArrayList.get(position).getOptionValues().split(",");
                    GlobalValues.markFavourite(authenticatedUser.getId(), productArrayList.get(position).getID(), optionIDs,Integer.valueOf(bagViewHolder.quantity.getText().toString()  ));
                    Product p = productArrayList.get(position);
                    if (SamanApp.localDB.deleteItemFromCart(p)) {
                        productArrayList.remove(p);
                        updateNotify();
                        ((DashboardActivity) mContext).updateBagCount();
                    }
                    bagFragment.updateCount(productArrayList.size());
                    showPopUp(mContext.getString(R.string.added_to_fav),
                            mContext.getString(R.string.item_added_message),
                            mContext.getString(R.string.continue_shopping),
                            mContext.getString(R.string.view_fav),
                            1);
                    mItemManger.closeAllItems();
                }
            });

            bagViewHolder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product p = productArrayList.get(position);
                    if (SamanApp.localDB.deleteItemFromCart(p)) {

//                        Constants.showAlert(mContext.getString(R.string.remove_from_bag),mContext.getString(R.string.removed_from_bag),mContext.getString(R.string.okay),mContext);

                        showPopUp(mContext.getString(R.string.remove_from_bag),
                                mContext.getString(R.string.item_added_message),
                                mContext.getString(R.string.continue_shopping),
                                mContext.getString(R.string.close),
                                0);
                        productArrayList.remove(p);
                        updateNotify();
                        ((DashboardActivity) mContext).updateBagCount();
                    }
                    bagFragment.updateCount(productArrayList.size());

                    mItemManger.closeAllItems();
                }
            });

            // mItemManger is member in RecyclerSwipeAdapter Class
            mItemManger.bindView(bagViewHolder.itemView, position);

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


    class BagViewHolder extends RecyclerView.ViewHolder {

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

        int getPosition;

        @BindView(R.id.tv_quantity)
        TextView quantity;
        @BindView(R.id.tv_product_name)
        TextView name;
        @BindView(R.id.tv_store_name)
        TextView storeName;
        @BindView(R.id.tv_product_description)
        TextView description;
        @BindView(R.id.tv_product_price)
        TextView price;
        @BindView(R.id.tv_product_total)
        TextView total;
        @BindView(R.id.iv_product)
        ImageView productImageView;

        BagViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.iv_add_quantity)
        void addItem() {
            SamanApp.localDB.addToCart(
                    productArrayList.get(getPosition),
                    productArrayList.get(getPosition).getOptionValues(),
                    productArrayList.get(getPosition).getOptions(),
                    productArrayList.get(getPosition).getOptions(),
                    1);


            productArrayList.get(getPosition).setQuantity(productArrayList.get(getPosition).getQuantity() + 1);
            updateNotify();
        }

        @OnClick(R.id.iv_remove_quantity)
        void removeItem() {
            int current = Integer.parseInt(quantity.getText().toString());
            if (current > 1) {
                current--;

                SamanApp.localDB.addToCart(
                        productArrayList.get(getPosition),
                        productArrayList.get(getPosition).getOptionValues(),
                        productArrayList.get(getPosition).getOptions(),
                        productArrayList.get(getPosition).getOptions(),
                        -1);
                productArrayList.get(getPosition).setQuantity(productArrayList.get(getPosition).getQuantity() - 1);
            }
            updateNotify();
        }
    }

    public void updateNotify() {
        bagFragment.updateQuantity();
        grandTotal = 0;
        notifyDataSetChanged();

    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.native_progress_bar)
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    Dialog dialog;

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

        if (type == 0) {
            nextButton.setVisibility(View.GONE);
        }

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
                    Constants.viewBag=true;
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
}
