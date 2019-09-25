package com.qtech.saman.listeners;

import android.content.Context;

public interface DialogOnClick {

//    public void onDialogClick(Context context,String y,String n);
    public void onDialogClick(Context context,String title,String message,String closebuttonText,String nextbuttonText,int type);


    interface OnDialogResponse{
        void OnResponseDialogClick(Context context,String response);
    }
}
