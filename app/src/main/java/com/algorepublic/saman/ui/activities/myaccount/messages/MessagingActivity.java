package com.algorepublic.saman.ui.activities.myaccount.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.CardDs;
import com.algorepublic.saman.data.model.Message;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetConversationApi;
import com.algorepublic.saman.data.model.apis.GetConversationsApi;
import com.algorepublic.saman.data.model.apis.SendMessageApi;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.adapters.ChatAdapter;
import com.algorepublic.saman.ui.adapters.PaymentAdapter;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.editText_message_box)
    EditText writeMessage;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.orderImage)
    ImageView orderImage;
    RecyclerView.LayoutManager layoutManager;
    List<Message> messages;
    ChatAdapter chatAdapter;

    @BindView(R.id.chat_room_footer)
    LinearLayout chatLayout;
    @BindView(R.id.bottom_info_layout)
    LinearLayout bottomInfoLayout;
    @BindView(R.id.tv_product_quantity)
    TextView productQuantityTextView;
    @BindView(R.id.tv_product_price)
    TextView productPriceTextView;
    @BindView(R.id.tv_order_total)
    TextView orderTotalTextView;

    User authenticatedUser;
    int conversationID = 0;
    int recipientID = 0;

    int productQuantity=1;
    float productPrice=0.0f;
    float orderTotal=0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        SamanApp.isScreenOpen=true;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        conversationID = getIntent().getIntExtra("ConversationID", 1);
        authenticatedUser = GlobalValues.getUser(this);
        toolbarTitle.setText(getString(R.string.messages));
        messages = new ArrayList<>();
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

        writeMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
        writeMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(writeMessage.getWindowToken(), 0);
//                    sendMessage(authenticatedUser.getId(),0,conversationID,"SAMAN",writeMessage.getText().toString());
                    return true;
                }
                return false;
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("messageReceived"));
    }

    @OnClick(R.id.tv_send_button)
    public void send(){
        if(!writeMessage.getText().toString().isEmpty()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(writeMessage.getWindowToken(), 0);
            sendMessage(authenticatedUser.getId(), recipientID, conversationID, "SAMAN", writeMessage.getText().toString());
            writeMessage.setText("");
        }
    }


    private void getConversation() {
        WebServicesHandler.instance.getConversation(conversationID, new retrofit2.Callback<GetConversationApi>() {
            @Override
            public void onResponse(Call<GetConversationApi> call, Response<GetConversationApi> response) {
                GetConversationApi getConversationApi = response.body();
                if (getConversationApi != null) {
                    if (getConversationApi.getResult() != null) {
                        if(getConversationApi.getResult().getID()!=0) {
                            recipientID = getConversationApi.getResult().getCreateBy();
                            orderName.setText(getConversationApi.getResult().getTitle());
                            storeName.setText(getConversationApi.getResult().getStoreName());
                            productName.setText(getConversationApi.getResult().getProductName());

                            productQuantity = getConversationApi.getResult().getProductQuantity();
                            productPrice = getConversationApi.getResult().getProductPrice();
                            orderTotal = (float) productQuantity * productPrice;

//                            bottomInfoLayout.setVisibility(View.GONE);
                            productQuantityTextView.setText(getString(R.string.quantity)+productQuantity);
                            productPriceTextView.setText(getString(R.string.price)+productPrice);
                            orderTotalTextView.setText(getString(R.string.total)+orderTotal);

                            if(getConversationApi.getResult().getStatus()==4
                              || getConversationApi.getResult().getStatus()==5
                              || getConversationApi.getResult().getStatus()==8){

                                chatLayout.setVisibility(View.GONE);
                            }

                            if (!getConversationApi.getResult().getImage().equals("")) {
                                Picasso.get().load(Constants.URLS.BaseURLImages + getConversationApi.getResult().getImage())
                                        .into(orderImage);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    orderImage.setImageDrawable(getDrawable(R.drawable.app_icon));
                                } else {
                                    orderImage.setImageDrawable(getResources().getDrawable(R.drawable.app_icon));
                                }
                            }

                            if (getConversationApi.getResult().getMessages() != null) {
                                messages.addAll(getConversationApi.getResult().getMessages());
                            }
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();

                if(messages.size()>0) {
                    mRecyclerView.scrollToPosition(messages.size() - 1);
                }

                updateMessageStatus(conversationID);

            }

            @Override
            public void onFailure(Call<GetConversationApi> call, Throwable t) {

            }
        });
    }

    private void sendMessage(int userID, int recipientID, int conversationID, String title, final String message){
        WebServicesHandler.instance.sendMessage(userID,recipientID,conversationID,title,message, new retrofit2.Callback<SendMessageApi>() {
            @Override
            public void onResponse(Call<SendMessageApi> call, Response<SendMessageApi> response) {
                SendMessageApi sendMessageApi=response.body();
                if(sendMessageApi!=null){
                    if(sendMessageApi.getSuccess()==1){
                        if(sendMessageApi.getResult()!=null){
                            Message message1=sendMessageApi.getResult();
                            if(message1.getSender()!=null){
                                messages.add(message1);
                            }else {
                                message1.setSender(authenticatedUser);
                                messages.add(message1);
                            }
                        }
                    }
                }

                chatAdapter.notifyDataSetChanged();

                if(messages.size()>0) {
                    mRecyclerView.scrollToPosition(messages.size() - 1);
                }
            }
            @Override
            public void onFailure(Call<SendMessageApi> call, Throwable t) {

            }
        });
    }

    private void updateMessageStatus(int conversationID){
        WebServicesHandler.instance.updateMessageStatus(conversationID, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {

            }
            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }


    // Our handler for received Intents. This will be called whenever an Intent
   // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            int id = intent.getIntExtra("id",0);
            if(conversationID==id){
//                getConversation();
                Message otherMessage=null;
                for (int i=0;i<messages.size();i++){
                    if(messages.get(i).getSender().getId()!=authenticatedUser.getId()){
                        otherMessage=messages.get(i);
                        break;
                    }
                }

                if(otherMessage!=null){
                    otherMessage.setMessageBody(message);
                    messages.add(otherMessage);
                    chatAdapter.notifyDataSetChanged();

                    if(messages.size()>0) {
                        mRecyclerView.scrollToPosition(messages.size() - 1);
                    }
                }else {
                    getConversation();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        SamanApp.isScreenOpen=false;
    }


}
