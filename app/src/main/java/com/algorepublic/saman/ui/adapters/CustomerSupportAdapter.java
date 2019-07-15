package com.algorepublic.saman.ui.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algorepublic.saman.R;
import com.algorepublic.saman.ui.activities.myaccount.customersupports.CustomerSupportActivity;
import com.algorepublic.saman.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerSupportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_ADDING = 1;
    private List<File> files;
    private Context mContext;

    public CustomerSupportAdapter(Context mContext, List<File> files) {
        this.files = files;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return files.get(position) == null ? VIEW_ADDING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);

            return new RowViewHolder(view);
        } else if (viewType == VIEW_ADDING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_photos, parent, false);
            return new AddingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            Picasso.get().load(files.get(position)).fit().into(rowViewHolder.imageView);
            rowViewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CustomerSupportActivity)mContext).removeImage(position);
                }
            });
        } else if (holder instanceof AddingViewHolder) {
            AddingViewHolder loadingViewHolder = (AddingViewHolder) holder;
            loadingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(files.size()<6) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1111);
                        } else {
                            choose();
                        }
                    }else{
                        Constants.showAlert(mContext.getString(R.string.customer_service),mContext.getString(R.string.images_limit),mContext.getString(R.string.close),mContext);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.imageView_delete)
        ImageView deleteImageView;
        RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AddingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout)
        RelativeLayout layout;
        AddingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void choose() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.CAMERA},
                                    1112);
                        } else {
                            ((CustomerSupportActivity)mContext).dispatchTakePictureIntent();
                        }
                    } catch (IOException e) {
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    ((Activity)mContext).startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}


//    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//    getIntent.setType("image/*");
//
//            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            pickIntent.setType("image/*");
//
//            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
//
//            startActivityForResult(chooserIntent, PICK_IMAGE);