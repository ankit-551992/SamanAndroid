package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.fragments.favourite.FavoritesFragment;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwipeFavoritesAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Product> productArrayList = new ArrayList<>();
    private Context mContext;
    private User authenticatedUser;
    FavoritesFragment favoritesFragment;


    public SwipeFavoritesAdapter(Context mContext, List<Product> productArrayList, FavoritesFragment favoritesFragment){
        this.productArrayList=productArrayList;
        this.mContext=mContext;
        this.favoritesFragment=favoritesFragment;
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


            favoritesViewHolder.name.setText(productArrayList.get(position).getProductName());
            favoritesViewHolder.price.setText(productArrayList.get(position).getPrice()+" OMR");

            if(productArrayList.get(position).getLogoURL()!=null && !productArrayList.get(position).getLogoURL().isEmpty()) {
                Picasso.get().load(Constants.URLS.BaseURLImages + productArrayList.get(position).getLogoURL())
                        .placeholder(R.drawable.dummy_mobile)
                        .error(R.drawable.dummy_mobile)
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
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID",productArrayList.get(position).getID());
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
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID",productArrayList.get(position).getID());
                    mContext.startActivity(intent);
                }
            });

            favoritesViewHolder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalValues.markUnFavourite(authenticatedUser.getId(), productArrayList.get(position).getID());
                    productArrayList.remove(position);
                    notifyDataSetChanged();
                    ((DashboardActivity) mContext).updateFavCount(productArrayList.size());
                    favoritesFragment.updateCount(productArrayList.size());
                }
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

        @BindView(R.id.tv_product_name) TextView name;
        @BindView(R.id.tv_price) TextView price;
        @BindView(R.id.iv_product) ImageView productImageView;
        public FavoritesViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.native_progress_bar);
        }
    }
}
