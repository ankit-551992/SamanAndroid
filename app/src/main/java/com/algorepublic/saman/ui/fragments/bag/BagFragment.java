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
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.ui.activities.order.cart.ShoppingCartActivity;
import com.algorepublic.saman.ui.activities.productdetail.ProductDetailActivity;
import com.algorepublic.saman.ui.adapters.BagAdapter;
import com.algorepublic.saman.ui.adapters.FavoritesAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SamanApp;
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
    RecyclerView bagRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productArrayList = new ArrayList<>();
    BagAdapter bagAdapter;


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

    int grandTotal;
    User authenticatedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);
        ButterKnife.bind(this, view);


        layoutManager = new LinearLayoutManager(getActivity());
        bagRecyclerView.setLayoutManager(layoutManager);
        bagRecyclerView.setNestedScrollingEnabled(false);
        productArrayList = new ArrayList<>();
        authenticatedUser = GlobalValues.getUser(getContext());
        bagAdapter = new BagAdapter(getContext(), productArrayList,this);
        bagRecyclerView.setAdapter(bagAdapter);


        getData();


        new SwipeHelper(getContext(), bagRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(getContext(),R.drawable.ic_ddelete),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                Product p=productArrayList.get(pos);
                                if(SamanApp.localDB.deleteItemFromCart(p)){
                                    Constants.showAlert(getString(R.string.remove_from_bag),getString(R.string.removed_from_bag),getString(R.string.okay),getActivity());
                                    productArrayList.remove(p);
                                    bagAdapter.updateNotify();
                                }
                                quantity.setText(productArrayList.size()+ " " +getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));


                                if(productArrayList.size()>0){
                                    tv_empty_bag.setVisibility(View.GONE);
                                }else{
                                    tv_empty_bag.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.add_to_fav),
//                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_heart),
                        ResourceUtil.getBitmap(getContext(),R.drawable.ic_heart),
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: OnTransfer
                                GlobalValues.markFavourite(authenticatedUser.getId(),productArrayList.get(pos).getID());
                            }
                        }
                ));
            }
        };

        return view;
    }

    @OnClick(R.id.button_proceed_to_checkout)
    void proceedCheckout(){
        if(productArrayList.size()>0) {
            Intent intent = new Intent(getContext(), ShoppingCartActivity.class);
            intent.putExtra("Price", grandTotal);
            startActivity(intent);
        }else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getData(){

        if(SamanApp.localDB!=null){
            productArrayList.addAll(SamanApp.localDB.getCartProducts());
            bagAdapter.notifyDataSetChanged();

            if(productArrayList.size()>0){
                tv_empty_bag.setVisibility(View.GONE);
            }else {
                tv_empty_bag.setVisibility(View.VISIBLE);
            }
        }
        quantity.setText(productArrayList.size()+ " " +getActivity().getResources().getQuantityString(R.plurals.items, productArrayList.size()));
    }

    public void updateTotal(int total,int vat){
        grandTotal=total+vat;
        productsTotal.setText(getString(R.string.total)+" "+total+".0 OMR");
        productsSubTotal.setText(getString(R.string.subtotal)+" "+total+".0 OMR");
        productsVAT.setText(getString(R.string.VAT)+" "+vat+".0 OMR");
        productsGrandTotal.setText(getString(R.string.total)+" "+grandTotal+".0 OMR");
    }

    @Override
    public String getName() {
        return null;
    }
}
