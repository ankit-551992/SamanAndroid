package com.algorepublic.saman.ui.activities.myaccount.customersupports;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.algorepublic.saman.BuildConfig;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseActivity;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.CustomerSupport;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.settings.SettingsActivity;
import com.algorepublic.saman.ui.adapters.CustomerSupportAdapter;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.GridSpacingItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSupportActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.editText_subject)
    EditText subjectSelectionEditText;
    @BindView(R.id.editText_message)
    EditText messageSelectionEditText;

    @BindView(R.id.photos)
    RecyclerView photos;
    RecyclerView.LayoutManager layoutManager;
    CustomerSupportAdapter customerSupportAdapter;
    private List<File> files;

    User authenticatedUser;

    File file = null;
    String mCurrentPhotoPath;
    private int REQUEST_TAKE_PHOTO = 1;
    private int REQUEST_CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support);
        ButterKnife.bind(this);
        authenticatedUser = GlobalValues.getUser(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.customer_service));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        }else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        files=new ArrayList<>();
        files.add(null);
        layoutManager = new GridLayoutManager(CustomerSupportActivity.this, 3);
        photos.setLayoutManager(layoutManager);
        photos.setNestedScrollingEnabled(false);
        customerSupportAdapter = new CustomerSupportAdapter(CustomerSupportActivity.this, files);
        photos.setAdapter(customerSupportAdapter);
        photos.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                // Show the thumbnail on ImageView
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                if (imageUri != null) {
                    if (imageUri.getPath() != null) {
                        file = new File(imageUri.getPath());
                        files.add(file);
                        customerSupportAdapter.notifyDataSetChanged();
                    }
                }
            } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        if (selectedImageUri.getPath() != null) {
                            file = new File(selectedImageUri.getPath());
                            files.add(file);
                            customerSupportAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    public void removeImage(int position){
        files.remove(position);
        customerSupportAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.button_upload)
    public void upload(){
        if(files.size()>1) {
            List<File> uFiles=new ArrayList<>();
            for (int i=1;i<files.size();i++) {
                uFiles.add(files.get(i));
            }
            uploadToServer(authenticatedUser.getId(), selectedSubject, messageSelectionEditText.getText().toString(), uFiles);
        }
    }

    public void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(CustomerSupportActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(CustomerSupportActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.editText_subject)
    public void selectSubjectClick() {
        selectSubject();
    }


    Dialog dialog;
    String selectedSubject = "";
    private void selectSubject(){
        dialog = new Dialog(CustomerSupportActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_subject_selection);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView done = (TextView) dialog.findViewById(R.id.tv_done);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                if(radioButton.isChecked()) {
                    selectedSubject = radioButton.getText().toString();
                    subjectSelectionEditText.setText(radioButton.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(CustomerSupportActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(animation);
        dialog.show();
    }



    private void uploadToServer(int userID,String subject,String message,List<File> files) {
        Constants.showSpinner(getString(R.string.submit_request), CustomerSupportActivity.this);
        WebServicesHandler apiClient = WebServicesHandler.instance;
        apiClient.uploadToSupport(userID,subject,message,files, new Callback<CustomerSupport>() {
            @Override
            public void onResponse(Call<CustomerSupport> call, Response<CustomerSupport> response) {
                Constants.dismissSpinner();
                CustomerSupport customerSupport=response.body();
                if(customerSupport.getSuccess()==1){
                    Constants.showAlertWithActivityFinish(getString(R.string.customer_service),getString(R.string.request_sent),getString(R.string.close),CustomerSupportActivity.this);
                }
            }

            @Override
            public void onFailure(Call<CustomerSupport> call, Throwable t) {
                Constants.dismissSpinner();
            }
        });
    }
}
