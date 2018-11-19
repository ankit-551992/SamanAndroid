package com.algorepublic.saman.ui.fragments.favourite;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.HomeScreenData;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends BaseFragment implements FavoritesContractor.View {

    @BindView(R.id.loading)
    RelativeLayout loading;

    @BindView(R.id.tv_empty_bag)
    TextView tv_empty_bag;
    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.recycler)
    RecyclerView favoritesRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    FavoritesAdapter favoritesAdapter;

    FavoritesPresenter presenter;
    User authenticatedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);
        authenticatedUser = GlobalValues.getUser(getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(getContext(), productArrayList);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        presenter = new FavoritesPresenter(this);
        presenter.getFavoritesData(authenticatedUser.getId());

        new SwipeHelper(getContext(), favoritesRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(getContext(), R.drawable.ic_ddelete),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                GlobalValues.markUnFavourite(authenticatedUser.getId(), productArrayList.get(pos).getID());
                                productArrayList.remove(pos);
                                favoritesAdapter.notifyDataSetChanged();
                                ((DashboardActivity) getActivity()).updateFavCount(productArrayList.size());
                                quantity.setText(productArrayList.size() + " " + getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));
                                if (productArrayList.size() > 0) {
                                    tv_empty_bag.setVisibility(View.GONE);
                                } else {
                                    tv_empty_bag.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.add_to_cart),
                        ResourceUtil.getBitmap(getContext(), R.drawable.ic_app_logo),
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: OnTransfer
                                Intent intent=new Intent(getContext(), ProductDetailActivity.class);
                                intent.putExtra("ProductID",productArrayList.get(pos).getID());
                                getContext().startActivity(intent);
                            }
                        }
                ));
            }
        };

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void showProgress() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loading.setVisibility(View.GONE);
    }


    @Override
    public void response(List<Product> product) {
        productArrayList.addAll(product);
        favoritesAdapter.notifyDataSetChanged();
        quantity.setText(productArrayList.size() + " " + getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));
        if (productArrayList.size() > 0) {
            tv_empty_bag.setVisibility(View.GONE);
        } else {
            tv_empty_bag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void error(String message) {

    }
}
