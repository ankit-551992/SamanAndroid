package com.algorepublic.saman.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.Slider;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.activities.store.StoreDetailActivity;
import com.algorepublic.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BestSellerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Slider> sliderList;
    private LayoutInflater inflater;

    public BestSellerPagerAdapter(Context mContext,List<Slider> sliderList) {
        this.mContext = mContext;
        this.sliderList = sliderList;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_image_view, collection, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView)imageLayout.findViewById(R.id.imageView);

        Picasso.get().load(Constants.URLS.BaseURLImages+ sliderList.get(position).getBannerURL()).fit().centerCrop().into(imageView);
        collection.addView(imageLayout, 0);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Store = 3, Product = 4
                if(sliderList.get(position).getType()==3){
                    Intent intent=new Intent(mContext, StoreDetailActivity.class);
                    intent.putExtra("Function",2); //2 for Store Products
                    intent.putExtra("StoreID", sliderList.get(position).getiD());
                    mContext.startActivity(intent);
                }else if(sliderList.get(position).getType()==4){
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID", sliderList.get(position).getiD());
                    mContext.startActivity(intent);
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
