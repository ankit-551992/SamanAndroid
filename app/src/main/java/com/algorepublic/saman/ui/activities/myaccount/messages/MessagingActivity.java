package com.algorepublic.saman.ui.activities.myaccount.messages;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.Message;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.data.model.apis.GetConversationApi;
import com.algorepublic.saman.data.model.apis.GetConversationsApi;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.ChatAdapter;
import com.algorepublic.saman.ui.adapters.PaymentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class MessagingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_order_name)
    TextView orderName;
    @BindView(R.id.tv_store_name)
    TextView storeName;
    @BindView(R.id.tv_product_name)
    TextView productName;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_settings)
    ImageView settings;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Message> messages = new ArrayList<>();
    ChatAdapter chatAdapter;

    int conversationID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        conversationID = getIntent().getIntExtra("ConversationID", 1);
        toolbarTitle.setText(getString(R.string.title_store));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setImageDrawable(getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        } else {
            settings.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
        }

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages);
        mRecyclerView.setAdapter(chatAdapter);

        getConversation();
    }

    private void getConversation() {
        WebServicesHandler.instance.getConversation(conversationID, new retrofit2.Callback<GetConversationApi>() {
            @Override
            public void onResponse(Call<GetConversationApi> call, Response<GetConversationApi> response) {
                GetConversationApi getConversationApi = response.body();
                if (getConversationApi != null) {
                    if (getConversationApi.getResult() != null) {

                        orderName.setText(getConversationApi.getResult().getTitle());
                        storeName.setText(getConversationApi.getResult().getStoreName());
                        productName.setText(getConversationApi.getResult().getProductName());

                        if(getConversationApi.getResult().getMessages()!=null) {
                            messages.addAll(getConversationApi.getResult().getMessages());
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetConversationApi> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

}
