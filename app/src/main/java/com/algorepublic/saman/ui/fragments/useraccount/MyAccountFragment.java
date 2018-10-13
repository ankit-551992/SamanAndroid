package com.algorepublic.saman.ui.fragments.useraccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.ui.activities.myaccount.customersupports.CustomerSupportActivity;
import com.algorepublic.saman.ui.activities.myaccount.messages.MessagesListActivity;
import com.algorepublic.saman.ui.activities.myaccount.mydetails.MyDetailsActivity;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.my_details)
    void myDetails() {
        Intent intent = new Intent(getActivity(), MyDetailsActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.payment_method)
    void paymentMethods() {
        Intent intent = new Intent(getActivity(), MyPaymentActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.messages_layout)
    void messages() {
        Intent intent = new Intent(getActivity(), MessagesListActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.customer_support)
    void customerSupport() {
        Intent intent = new Intent(getActivity(), CustomerSupportActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public String getName() {
        return null;
    }
}
