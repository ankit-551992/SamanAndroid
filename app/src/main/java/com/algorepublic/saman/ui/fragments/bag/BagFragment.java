package com.algorepublic.saman.ui.fragments.bag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.ui.activities.order.cart.ShoppingCartActivity;
import com.algorepublic.saman.ui.adapters.BagAdapter;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BagFragment extends BaseFragment {

    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.recycler)
    RecyclerView favoritesRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    BagAdapter bagAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);
        ButterKnife.bind(this, view);


        layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        bagAdapter = new BagAdapter(getContext(), productArrayList);
        favoritesRecyclerView.setAdapter(bagAdapter);


        getData();


        SwipeHelper swipeHelper = new SwipeHelper(getContext(), favoritesRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(getContext(),R.drawable.ic_delete),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.add_to_fav),
//                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_heart),
                        ResourceUtil.getBitmap(getContext(),R.drawable.ic_favorite_border),
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: OnTransfer
                            }
                        }
                ));
            }
        };

        return view;
    }

    @OnClick(R.id.button_proceed_to_checkout)
    void proceedCheckout(){
        Intent intent=new Intent(getContext(), ShoppingCartActivity.class);
        startActivity(intent);
    }

    private void getData(){
        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            productArrayList.add(product);
            bagAdapter.notifyDataSetChanged();
        }

        quantity.setText(productArrayList.size()+ " " +getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));
    }

    @Override
    public String getName() {
        return null;
    }
}
