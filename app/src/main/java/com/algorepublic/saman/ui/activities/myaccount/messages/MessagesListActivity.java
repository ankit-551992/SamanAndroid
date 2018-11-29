package com.algorepublic.saman.ui.activities.myaccount.messages;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.Message;
import com.algorepublic.saman.data.model.Payment;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.algorepublic.saman.ui.adapters.MessagesAdapter;
import com.algorepublic.saman.ui.adapters.PaymentAdapter;
import com.algorepublic.saman.utils.ResourceUtil;
import com.algorepublic.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessagesListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_empty)
    TextView empty;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Message> messageArrayList = new ArrayList<>();
    MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.messages));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        messageArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this,messageArrayList);
        mRecyclerView.setAdapter(messagesAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        getMessages();


        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        ResourceUtil.getBitmap(MessagesListActivity.this,R.drawable.ic_delete_ic),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));
            }
        };
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    private void getMessages(){
        messageArrayList.add(new Message());

        if(messageArrayList.size()==0){
            empty.setVisibility(View.VISIBLE);
        }else {
            empty.setVisibility(View.GONE);
        }
    }
}
