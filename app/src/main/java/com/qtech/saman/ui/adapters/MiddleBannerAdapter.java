package com.qtech.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.Slider;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.product.ProductsActivity;
import com.qtech.saman.ui.activities.productdetail.ProductDetailActivity;
import com.qtech.saman.ui.activities.store.StoreDetailActivity;
import com.qtech.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MiddleBannerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Slider> sliderList;
    private LayoutInflater inflater;

    public MiddleBannerAdapter(Context mContext, List<Slider> sliderList) {
        this.mContext = mContext;
        this.sliderList = sliderList;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_image_view, collection, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);

//      Picasso.get().load(Constants.URLS.BaseURLImages+ sliderList.get(position).getBannerURL()).fit().centerCrop().into(imageView);
//      Picasso.get().load(Constants.URLS.BaseURLImages+ sliderList.get(position).getBannerURL()).fit().centerInside().into(imageView);
        Picasso.get().load(Constants.URLS.BaseURLImages + sliderList.get(position).getBannerURL()).fit().into(imageView);
        collection.addView(imageLayout, 0);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               Product = 4 , Sales_category = 9
                if (sliderList.get(position).getType() == 6) {   // middle banner products Category
                    Intent intent = new Intent(mContext, ProductsActivity.class);
                    intent.putExtra("CategoryBannerID", sliderList.get(position).getiD());
                    intent.putExtra("IsCategoryProduct", true);
                    intent.putExtra("BannerID", sliderList.get(position).getiD());
                    mContext.startActivity(intent);
                } else if (sliderList.get(position).getType() == 7) {   // middle banner  open Store
                    if (sliderList.get(position).getProductIDlist().size() == 1) {
                        Intent intent = new Intent(mContext, StoreDetailActivity.class);
                        intent.putExtra("StoreID", Integer.parseInt(sliderList.get(position).getProductIDlist().get(0)));
                        intent.putExtra("BannerID", sliderList.get(position).getiD());
                        mContext.startActivity(intent);
                    } else {
                        ((DashboardActivity) mContext).callStoreNav(true, sliderList.get(position).getiD());
                    }
                } else if (sliderList.get(position).getType() == 8) {   // middle banner  open Product
                    if (sliderList.get(position).getProductIDlist().size() == 1) {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("ProductID", Integer.parseInt(sliderList.get(position).getProductIDlist().get(0)));
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ProductsActivity.class);
                        intent.putExtra("BannerID", sliderList.get(position).getiD());
                        intent.putExtra("IsBannerProduct", true);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
