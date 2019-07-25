package com.algorepublic.saman.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Response;

public class PopupActivity extends BaseActivity {

    Dialog dialog;
    int orderID = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.acticvity_pop_up);
        orderID = getIntent().getIntExtra("orderID", 0);
        feedBackDialog(PopupActivity.this);
        Log.e("orderID", "" + orderID);
    }

    private void feedBackDialog(final Context mContext) {
        dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);
        dialog.setCancelable(true);

//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText editText = dialog.findViewById(R.id.editText_review);
        RatingBar ratingBar = dialog.findViewById(R.id.ratting);
        Button sendButton = dialog.findViewById(R.id.button_feedback);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Message", editText.getText().toString());

                if (ratingBar.getRating() > 0) {
                    updateOrderFeedback(orderID, ratingBar.getRating(), editText.getText().toString());
                    dialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(PopupActivity.this, getString(R.string.rating_required), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //Controlling width and height.
    }

    private void updateOrderFeedback(int orderID, float rating, String feedback) {
        WebServicesHandler.instance.updateOrderFeedback(orderID, rating, feedback, new retrofit2.Callback<SimpleSuccess>() {
            @Override
            public void onResponse(Call<SimpleSuccess> call, Response<SimpleSuccess> response) {
//                Log.e("RES",""+response.body().getSuccess());
            }

            @Override
            public void onFailure(Call<SimpleSuccess> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
