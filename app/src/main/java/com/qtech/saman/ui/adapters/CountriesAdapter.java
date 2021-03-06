package com.qtech.saman.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.data.model.Country;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Country> countries = new ArrayList<>();
    private Context mContext;
    int selectedPosition = -1;
    private int focusedItem = 0;

    boolean getCode;

    public CountriesAdapter(Context mContext, List<Country> countries, boolean getCode) {
        this.countries = countries;
        this.mContext = mContext;
        this.getCode = getCode;
    }

    @Override
    public int getItemViewType(int position) {
        return countries.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_country, parent, false);
            return new CountryViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CountryViewHolder) {
            CountryViewHolder countryViewHolder = (CountryViewHolder) holder;

            if (SamanApp.isEnglishVersion) {
                countryViewHolder.countryName.setText(countries.get(position).getName());
            } else {
                countryViewHolder.countryName.setText(countries.get(position).getName_AR());
            }

            if (countries.get(position).getSortname().equalsIgnoreCase(GlobalValues.getSelectedCountry(mContext))) {
                countryViewHolder.iv_selected_country.setVisibility(View.VISIBLE);
                countryViewHolder.countryName.setText(countries.get(position).getName());
                countryViewHolder.countryName.setTextColor(Color.parseColor("#000000"));
            } else {
                countryViewHolder.iv_selected_country.setVisibility(View.GONE);
            }
            countryViewHolder.countryCode.setText("(+" + countries.get(position).getPhoneCode() + ")");

            Picasso.get().load(countries.get(position).getFlag()).into(countryViewHolder.flag);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getCode) {
                        Intent data = new Intent();
                        String code = countries.get(holder.getAdapterPosition()).getPhoneCode();
                        data.putExtra("Code", code);
                        data.putExtra("Flag", countries.get(holder.getAdapterPosition()).getFlag());
                        ((Activity) mContext).setResult(RESULT_OK, data);
                        ((Activity) mContext).finish();
                    } else {
                        Intent data = new Intent();
                        String text = countries.get(holder.getAdapterPosition()).getName();
                        data.setData(Uri.parse(text));
                        GlobalValues.setSelectedCountry(mContext, countries.get(holder.getAdapterPosition()).getSortname());
                        ((Activity) mContext).setResult(RESULT_OK, data);
                        ((Activity) mContext).finish();
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
        return countries == null ? 0 : countries.size();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtViewCountryName)
        TextView countryName;
        @BindView(R.id.txtViewCountryCode)
        TextView countryCode;
        @BindView(R.id.imgViewFlag)
        ImageView flag;
        @BindView(R.id.ll_country)
        LinearLayout ll_country;
        @BindView(R.id.iv_selected_country)
        ImageView iv_selected_country;

        public CountryViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.native_progress_bar);
        }
    }

}
