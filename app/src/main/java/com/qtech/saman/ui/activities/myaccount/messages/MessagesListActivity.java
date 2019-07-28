package com.qtech.saman.ui.activities.myaccount.messages;

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

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.Conversation;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.GetConversationsApi;
import com.qtech.saman.data.model.apis.SimpleSuccess;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.ConversationsAdapter;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.ResourceUtil;
import com.qtech.saman.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

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
    List<Conversation> conversationArrayList = new ArrayList<>();
    ConversationsAdapter conversationsAdapter;


    User authenticatedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        authenticatedUser = GlobalValues.getUser(this);
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
        conversationArrayList = new ArrayList<>();
        conversationsAdapter = new ConversationsAdapter(this,conversationArrayList);
        mRecyclerView.setAdapter(conversationsAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        getConversation();

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
                                deleteConversation(conversationArrayList.get(pos).getID());
                                conversationArrayList.remove(pos);
                                conversationsAdapter.notifyDataSetChanged();
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

    private void getConversation(){
        WebServicesHandler.instance.getConversationList(authenticatedUser.getId(), new retrofit2.Callback<GetConversationsApi>() {
            @Override
            public void onResponse(Call<GetConversationsApi> call, Response<GetConversationsApi> response) {
                GetConversationsApi getConversationsApi = response.body();
                if(getConversationsApi!=null) {
                    if(getConversationsApi.getResult()!=null) {
                        conversationArrayList.addAll(getConversationsApi.getResult());
                    }
                }

                if(conversationArrayList.size()<1){
                    empty.setVisibility(View.VISIBLE);
                }else {
                    empty.setVisibility(View.GONE);
                }

                Collections.sort(conversationArrayList, new Comparator<Conversation>() {
                    @Override
                    public int compare(Conversation lhs, Conversation rhs) {
                        return getDate(rhs.getUpdatedAt()).compareTo(getDate(lhs.getUpdatedAt()));
                    }
                });
                conversationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetConversationsApi> call, Throwable t) {
                empty.setVisibility(View.VISIBLE);
            }
        });
    }


    private Date getDate(String date){
        return new Date(Long.parseLong(date.replaceAll("\\D", "")));
    }

    private void deleteConversation(int conversationID){
        WebServicesHandler.instance.deleteConversation(conversationID, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
            }
            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
            }
        });
    }
}
