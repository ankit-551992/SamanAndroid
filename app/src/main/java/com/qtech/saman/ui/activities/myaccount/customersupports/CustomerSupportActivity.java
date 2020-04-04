package com.qtech.saman.ui.activities.myaccount.customersupports;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qtech.saman.BuildConfig;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.CustomerSupport;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.adapters.CustomerSupportAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.GridSpacingItemDecoration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.tv_count)
    TextView countTextView;

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
    private int mCount = 300;
    private String orderId = "";
    Dialog dialog;
    Dialog dialog2;
    String selectedSubject = "";

    TextWatcher txwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (selectedSubject.equals("")) {
                Constants.showAlert(getString(R.string.customer_service), getString(R.string.subject_prompt), getString(R.string.okay), CustomerSupportActivity.this);
                return;
            }
            setCount(mCount - s.length());
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private void setCount(int count) {
        countTextView.setText(count + " " + getString(R.string.character));
    }

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
        orderId = getIntent().getStringExtra("order_id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        files = new ArrayList<>();
        files.add(null);
        if (orderId != null && !orderId.isEmpty()) {
            photos.setVisibility(View.GONE);
            subjectSelectionEditText.setText(getString(R.string.cancel_order_of) + " " + orderId + ".");
            selectedSubject = subjectSelectionEditText.getText().toString();

        } else {
            photos.setVisibility(View.VISIBLE);
            layoutManager = new GridLayoutManager(CustomerSupportActivity.this, 3);
            photos.setLayoutManager(layoutManager);
            photos.setNestedScrollingEnabled(false);
            customerSupportAdapter = new CustomerSupportAdapter(CustomerSupportActivity.this, files);
            photos.setAdapter(customerSupportAdapter);
            photos.addItemDecoration(new GridSpacingItemDecoration(2, 30, false, this));
            photos.requestFocus();
            messageSelectionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.e("FLAG000", "---hasFocus---" + hasFocus);
                    if (hasFocus) {
                        if (selectedSubject.equals("") && selectedSubject.isEmpty()) {
                            Constants.showAlert(getString(R.string.customer_service), getString(R.string.subject_prompt), getString(R.string.okay), CustomerSupportActivity.this);
                            return;
                        }
                    } else {
                    }
                }
            });
        }

        messageSelectionEditText.addTextChangedListener(txwatcher);
        setCount(mCount);

    }

    @OnClick(R.id.editText_message)
    public void textmessage() {
//        if (selectedSubject.equals("")) {
//            Constants.showAlert(getString(R.string.customer_service), getString(R.string.subject_prompt), getString(R.string.okay), CustomerSupportActivity.this);
//        } else {
//            messageSelectionEditText.addTextChangedListener(txwatcher);
//            setCount(mCount);
//        }
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
                    String path = GlobalValues.getRealPathFromURI(CustomerSupportActivity.this, data.getData());
                    if (path != null) {
                        file = new File(path);
                        if (file.exists()) {
                            files.add(file);
                        } else {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                if (selectedImageUri.getPath() != null) {
                                    file = new File(selectedImageUri.getPath());
                                    if (file.exists()) {
                                        files.add(file);
                                    }
                                }
                            }
                        }
                        customerSupportAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void removeImage(int position) {
        files.remove(position);
        customerSupportAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.button_upload)
    public void upload() {
        if ((orderId == null || orderId.isEmpty()) && selectedSubject.equals("") && selectedSubject.isEmpty()) {
            Constants.showAlert(getString(R.string.subject_prompt), "", getString(R.string.okay), CustomerSupportActivity.this);
            return;
        }
        if (messageSelectionEditText.getText().toString().equals("") && messageSelectionEditText.getText().toString().isEmpty()) {
            Constants.showAlert(getString(R.string.write_here), "", getString(R.string.okay), CustomerSupportActivity.this);
            return;
        }

        if (files.size() > 1) {
            List<File> uFiles = new ArrayList<>();
            for (int i = 1; i < files.size(); i++) {
                uFiles.add(files.get(i));
            }
            uploadToServer(authenticatedUser.getId(), selectedSubject, messageSelectionEditText.getText().toString(), uFiles);
        } else {
            List<File> uFiles = new ArrayList<>();
            if (orderId != null && !orderId.isEmpty()) {
                selectedSubject = subjectSelectionEditText.getText().toString();
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",    /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
//        finish();
        super.onBackPressed();
    }

    @OnClick(R.id.editText_subject)
    public void selectSubjectClick() {
        if (orderId == null || orderId.isEmpty()) {
            selectSubject();
        }
    }

    private void selectSubject() {
        dialog = new Dialog(CustomerSupportActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_subject_selection);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog.findViewById(R.id.iv_filer_close);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        RadioButton radioButton = null;
        if (getString(R.string.report_issue).equals(subjectSelectionEditText.getText().toString())) {
            radioButton = radioGroup.findViewById(R.id.radio_my_account);
        } else if (getString(R.string.ask_help).equals(subjectSelectionEditText.getText().toString())) {
            radioButton = radioGroup.findViewById(R.id.radio_report_prodcut);
        } else if (getString(R.string.report_prodcut).equals(subjectSelectionEditText.getText().toString())) {
            radioButton = radioGroup.findViewById(R.id.radio_report_store);
        } else if (getString(R.string.report_store).equals(subjectSelectionEditText.getText().toString())) {
            radioButton = radioGroup.findViewById(R.id.radio_general_inquires);
        }
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
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


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                if (radioButton.isChecked()) {
                    selectedSubject = radioButton.getText().toString();
                    subjectSelectionEditText.setText(radioButton.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(CustomerSupportActivity.this,
                R.anim.slide_bottom_to_top);

        ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog.show();
    }

    private void uploadToServer(int userID, String subject, String message, List<File> files) {
        Constants.showSpinner(getString(R.string.submit_request), CustomerSupportActivity.this);
        WebServicesHandler apiClient = WebServicesHandler.instance;
        apiClient.uploadToSupport(userID, subject, message, files, new Callback<CustomerSupport>() {
            @Override
            public void onResponse(Call<CustomerSupport> call, Response<CustomerSupport> response) {
                Constants.dismissSpinner();
                CustomerSupport customerSupport = response.body();
                Log.e("CUSTOMSUPPORT", "---000----CustomerSupport-----" + new Gson().toJson(response.body()));
                if (customerSupport != null) {
                    if (customerSupport.getSuccess() == 1) {
                        showPopUp();
//                    Constants.showAlertWithActivityFinish(getString(R.string.customer_service), getString(R.string.ticket_created_success), getString(R.string.okay), CustomerSupportActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSupport> call, Throwable t) {
                Constants.dismissSpinner();
            }
        });
    }

    private void showPopUp() {
        dialog2 = new Dialog(CustomerSupportActivity.this, R.style.CustomDialog);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_customer_support);
        dialog2.setCancelable(false);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = (ImageView) dialog2.findViewById(R.id.iv_pop_up_close);
        Button nextButton = (Button) dialog2.findViewById(R.id.button_pop_next);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                Intent intent = new Intent(CustomerSupportActivity.this, CustomerSupportListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Animation animation;
        animation = AnimationUtils.loadAnimation(CustomerSupportActivity.this, R.anim.fade_in);

        ((ViewGroup) dialog2.getWindow().getDecorView()).getChildAt(0).startAnimation(animation);
        dialog2.show();
    }
}
