package com.qtech.saman.ui.fragments.bag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseFragment;
import com.qtech.saman.data.model.Product;
import com.qtech.saman.data.model.User;
import com.qtech.saman.ui.activities.order.cart.ShoppingCartActivity;
import com.qtech.saman.ui.adapters.SwipeBagAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BagFragment extends BaseFragment {

    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.recycler)
    RecyclerView bagRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    SwipeBagAdapter bagAdapter;

    //Total
    @BindView(R.id.tv_empty_bag)
    TextView tv_empty_bag;
    @BindView(R.id.tv_products_total)
    TextView productsTotal;
    @BindView(R.id.tv_products_subtotal)
    TextView productsSubTotal;
    @BindView(R.id.tv_vat)
    TextView productsVAT;
    @BindView(R.id.tv_grand_total)
    TextView productsGrandTotal;
    //Total

    float grandTotal;
    User authenticatedUser;
    Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        authenticatedUser = GlobalValues.getUser(getContext());
        bagAdapter = new SwipeBagAdapter(getContext(), productArrayList, this);
        bagRecyclerView.setAdapter(bagAdapter);

        getData();
        return view;
    }

    @OnClick(R.id.button_proceed_to_checkout)
    void proceedCheckout() {
        if (GlobalValues.getGuestLoginStatus(getContext())) {
            Constants.showLoginDialog(getContext());
            return;
        }

        authenticatedUser = GlobalValues.getUser(getContext());

        if (productArrayList.size() > 0) {
            Intent intent = new Intent(getContext(), ShoppingCartActivity.class);
            intent.putExtra("Price", grandTotal);
            startActivity(intent);
        } else {
            //
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getData() {
        if (SamanApp.localDB != null) {
            productArrayList.addAll(SamanApp.localDB.getCartProducts());
            bagAdapter.notifyDataSetChanged();

            if (productArrayList.size() > 0) {
                tv_empty_bag.setVisibility(View.GONE);
            } else {
                tv_empty_bag.setVisibility(View.VISIBLE);
            }
            quantity.setText(SamanApp.localDB.getCartAllProductsCounting() + " " + getActivity().getResources().getQuantityString(R.plurals.bag_wish_items, productArrayList.size()));
        }
    }

    public void updateTotal(float total, float vat) {
        grandTotal = total + vat;
        productsTotal.setText(getString(R.string.total) + " " + total + " " + getString(R.string.currency_omr));
        productsSubTotal.setText(getString(R.string.subtotal) + " " + total + " " + getString(R.string.currency_omr));
        productsVAT.setText(getString(R.string.VAT) + " " + vat + " " + getString(R.string.currency_omr));
        productsGrandTotal.setText(getString(R.string.total) + " " + grandTotal + " " + getString(R.string.currency_omr));
    }

    public void updateCount(int size) {
        if (size > 0) {
            tv_empty_bag.setVisibility(View.GONE);
        } else {
            tv_empty_bag.setVisibility(View.VISIBLE);
        }
        quantity.setText(SamanApp.localDB.getCartAllProductsCounting() + " " + getActivity().getResources().getQuantityString(R.plurals.bag_wish_items, productArrayList.size()));

    }

    public void updateQuantity() {
        if (SamanApp.localDB != null) {
            quantity.setText(SamanApp.localDB.getCartAllProductsCounting() + " " + getActivity().getResources().getQuantityString(R.plurals.bag_wish_items, productArrayList.size()));
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
