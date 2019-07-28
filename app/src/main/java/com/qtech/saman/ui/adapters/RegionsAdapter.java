package com.qtech.saman.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.qtech.saman.R;
import com.qtech.saman.data.model.Country;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.app.Activity.RESULT_OK;

public class RegionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Country> regions = new ArrayList<>();
    private Context mContext;



    public RegionsAdapter(Context mContext, List<Country> regions) {
        this.regions = regions;
        this.mContext = mContext;
    }


    @Override
    public int getItemViewType(int position) {
        return regions.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_country, parent, false);
            return new RegionViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RegionViewHolder) {
            RegionViewHolder regionViewHolder = (RegionViewHolder) holder;
            regionViewHolder.countryName.setText(regions.get(position).getName());
            regionViewHolder.countryCode.setVisibility(View.GONE);
            regionViewHolder.flag.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data = new Intent();
                    String text = regions.get(holder.getAdapterPosition()).getName();
                    data.setData(Uri.parse(text));
                    ((Activity) mContext).setResult(RESULT_OK, data);
                    ((Activity) mContext).finish();
                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return regions == null ? 0 : regions.size();
    }


    static class RegionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtViewCountryName)
        TextView countryName;
        @BindView(R.id.txtViewCountryCode)
        TextView countryCode;
        @BindView(R.id.imgViewFlag)
        ImageView flag;

        public RegionViewHolder(View v) {
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
