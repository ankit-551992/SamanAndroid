package com.qtech.saman.ui.activities.imagesview;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qtech.saman.R;
import com.qtech.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullScreenAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> urls;
    private LayoutInflater inflater;

    public FullScreenAdapter(Context mContext,List<String> urls) {
        this.mContext = mContext;
        this.urls = urls;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_image_view, collection, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);

        Picasso.get().load(Constants.URLS.BaseURLImages+urls.get(position))
                .placeholder(R.drawable.ic_account_img)
                .error(R.drawable.ic_account_img)
                .into(imageView);
        collection.addView(imageLayout, 0);
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
