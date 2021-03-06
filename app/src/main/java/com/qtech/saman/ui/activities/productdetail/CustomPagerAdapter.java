package com.qtech.saman.ui.activities.productdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qtech.saman.R;
import com.qtech.saman.ui.activities.imagesview.FullScreenImagesActivity;
import com.qtech.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> urls;
    private LayoutInflater inflater;

    public CustomPagerAdapter(Context mContext, List<String> urls) {
        this.mContext = mContext;
        this.urls = urls;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_image_view, collection, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);

        //Picasso.get().load(Constants.URLS.BaseURLImages+urls.get(position)).fit().centerCrop().into(imageView);
        Picasso.get().load(Constants.URLS.BaseURLImages + urls.get(position)).fit().into(imageView);
        collection.addView(imageLayout, 0);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FullScreenImagesActivity.class);
                intent.putStringArrayListExtra("urls", (ArrayList<String>) urls);
                intent.putExtra("index", position);
                ((Activity) mContext).startActivity(intent);

                /*if(sliderList.get(position).getType()==5){
                    Intent intent=new Intent(mContext, SalesProductActivity.class);
                    mContext.startActivity(intent);
                }else if(sliderList.get(position).getType()==3){
                    Intent intent=new Intent(mContext, StoreDetailActivity.class);
                    intent.putExtra("Function",2); //2 for Store Products
                    intent.putExtra("StoreID", sliderList.get(position).getiD());
                    mContext.startActivity(intent);
                }else if(sliderList.get(position).getType()==4){
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("ProductID", sliderList.get(position).getiD());
                    mContext.startActivity(intent);
                }*/
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
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return urls.get(position);
    }
}
