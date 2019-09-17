package com.qtech.saman.ui.activities.CustomerSupport;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.apis.CustomerSupportListApi;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.CustomerSupportImageAdapter;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.GridSpacingItemDecoration;
import com.qtech.saman.utils.SamanApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;

    @BindView(R.id.tv_customer_id)
    TextView customerIdTextView;
    @BindView(R.id.tv_customer_subject)
    TextView customerSubjectTextView;
    @BindView(R.id.tv_message)
    TextView messageTextView;
    @BindView(R.id.tv_reply)
    TextView replyTextView;
    @BindView(R.id.tv_ticket_status)
    TextView tv_ticket_status;
    @BindView(R.id.ll_image)
    LinearLayout ll_image;
    @BindView(R.id.support_item_recyclerView)
    RecyclerView supportImageRecyclerview;
    RecyclerView.LayoutManager layoutManager;
    CustomerSupportImageAdapter customerSupportImageAdapter;

    CustomerSupportListApi.Result usersupport_details;
    ArrayList<String> imagelist = new ArrayList<>();
    private List<CustomerSupportListApi.Result> customerSupportList;
    int ticketId;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_details);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mcontext = SupportDetailsActivity.this;
        SamanApp.isScreenOpen = true;
        if (getIntent().hasExtra("UserSupportDetail")) {
            usersupport_details = (CustomerSupportListApi.Result) getIntent().getSerializableExtra("UserSupportDetail");
            Log.e("SUPPORTDETAILS", "-----usersupport_details---" + usersupport_details);
            setUserSupportDetailes();
        }

        if (getIntent().hasExtra("TicketId")) {
            ticketId = getIntent().getIntExtra("TicketId", 1);
            Log.e("SUPPORTDETAILS", "-----ticketId---" + ticketId);
            //getInvoiceDetailes(ticketId);
            getTicketDetailes(ticketId);
            if (GlobalValues.getBadgeCount(mcontext) != 0) {
                int newCount = GlobalValues.getBadgeCount(mcontext) - 1;
                GlobalValues.setBadgeCount(mcontext, newCount);
                ShortcutBadger.applyCount(mcontext, newCount);
            }
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.customer_service));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
    }

    private void setUserSupportDetailes() {
        if (usersupport_details != null) {
            if (usersupport_details.getID() != null) {
                customerIdTextView.setText(usersupport_details.getID());
            }

            if (usersupport_details.getSubject() != null) {
                customerSubjectTextView.setText(usersupport_details.getSubject());
            }

            if (usersupport_details.getMessage() != null) {
                messageTextView.setText(usersupport_details.getMessage());
            }

            if (usersupport_details.getReplay() != null) {
                replyTextView.setText(usersupport_details.getReplay());
            }

            if (usersupport_details.getIsResolved().equals("true")) {
                tv_ticket_status.setText(mcontext.getResources().getString(R.string.ticket_resolve));
            } else {
                tv_ticket_status.setText(mcontext.getResources().getString(R.string.ticket_pending));
            }

            if (usersupport_details.getSupportImageURLs().size() != 0) {
                ll_image.setVisibility(View.VISIBLE);
                imagelist.addAll(usersupport_details.getSupportImageURLs());
                setRecyclerView();
            } else {
                ll_image.setVisibility(View.GONE);
            }
        }
    }


    private void getTicketDetailes(int ticketId) {
        WebServicesHandler apiClient = WebServicesHandler.instance;
        Log.e("TICKETID", "--custom-ticketId--id--" + ticketId);

        apiClient.getTicketByID(ticketId, new Callback<CustomerSupportListApi>() {
            @Override
            public void onResponse(Call<CustomerSupportListApi> call, Response<CustomerSupportListApi> response) {

                Log.e("TICKETID", "--custom-response--getTicketByID--" + response.body());
                CustomerSupportListApi customerSupportListApi = response.body();
                if (customerSupportListApi != null) {
                    if (customerSupportListApi.getResult() != null) {
                        customerSupportList = new ArrayList<>();
                        customerSupportList.addAll(customerSupportListApi.getResult());
                        usersupport_details = customerSupportList.get(0);
                        setUserSupportDetailes();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSupportListApi> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }

    private void setRecyclerView() {
        layoutManager = new GridLayoutManager(SupportDetailsActivity.this, 3);
        supportImageRecyclerview.setLayoutManager(layoutManager);
        supportImageRecyclerview.setNestedScrollingEnabled(false);
        customerSupportImageAdapter = new CustomerSupportImageAdapter(SupportDetailsActivity.this, imagelist);
        supportImageRecyclerview.setAdapter(customerSupportImageAdapter);
        supportImageRecyclerview.addItemDecoration(new GridSpacingItemDecoration(2, 30, false, this));
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SamanApp.isScreenOpen = false;
    }
}
