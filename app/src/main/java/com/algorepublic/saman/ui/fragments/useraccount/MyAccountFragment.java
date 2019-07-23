package com.algorepublic.saman.ui.fragments.useraccount;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.saman.BuildConfig;
import com.algorepublic.saman.R;
import com.algorepublic.saman.base.BaseFragment;
import com.algorepublic.saman.data.model.Message;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.GetConversationsApi;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.login.LoginActivity;
import com.algorepublic.saman.ui.activities.myaccount.addresses.ShippingAddressActivity;
import com.algorepublic.saman.ui.activities.myaccount.customersupports.CustomerSupportActivity;
import com.algorepublic.saman.ui.activities.myaccount.messages.MessagesListActivity;
import com.algorepublic.saman.ui.activities.myaccount.mydetails.MyDetailsActivity;
import com.algorepublic.saman.ui.activities.myaccount.myorders.MyOrdersActivity;
import com.algorepublic.saman.ui.activities.myaccount.payment.MyPaymentActivity;
import com.algorepublic.saman.ui.fragments.product.ProductsCategoryFragment;
import com.algorepublic.saman.utils.CircleTransform;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.thefinestartist.utils.content.ContextUtil.getContentResolver;

public class MyAccountFragment extends BaseFragment {

    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_email)
    TextView userEmail;
    @BindView(R.id.tv_unread_message_count)
    TextView unreadMessageCount;
    @BindView(R.id.iv_profile)
    ImageView profile;

    User authenticatedUser;

    File file = null;
    String mCurrentPhotoPath;
    private int REQUEST_TAKE_PHOTO = 1;
    private int REQUEST_CHOOSE_PHOTO = 2;

    int unread;

    public static MyAccountFragment newInstance(int unread) {
        Bundle bundle = new Bundle();
        bundle.putInt("unread", unread);

        MyAccountFragment fragment = new MyAccountFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            unread = bundle.getInt("unread");
            if (unread == 0) {
                unreadMessageCount.setVisibility(View.GONE);
            } else {
                unreadMessageCount.setVisibility(View.VISIBLE);
                unreadMessageCount.setText("" + unread);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        authenticatedUser = GlobalValues.getUser(getContext());
        userName.setText(getContext().getString(R.string.Welcome) + "," + authenticatedUser.getFirstName());
        userEmail.setText(authenticatedUser.getEmail());

        if (authenticatedUser.getProfileImagePath() != null && !authenticatedUser.getProfileImagePath().isEmpty() && !authenticatedUser.getProfileImagePath().equalsIgnoreCase("path")) {
            if (authenticatedUser.getSocialID() != 0) {
                if (!authenticatedUser.getProfileImagePath().isEmpty()) {
                    Picasso.get()
                            .load(authenticatedUser.getProfileImagePath())
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.ic_profile)
                            .into(profile);
                } else {
                    Picasso.get()
                            .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.ic_profile)
                            .into(profile);
                }
            } else {
                Picasso.get()
                        .load(Constants.URLS.BaseURLImages + authenticatedUser.getProfileImagePath())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.ic_profile)
                        .into(profile);
            }
        } else {
            Picasso.get()
                    .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.ic_profile)
                    .into(profile);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        authenticatedUser = GlobalValues.getUser(getContext());
        userName.setText(getContext().getString(R.string.Welcome) + "," + authenticatedUser.getFirstName());
        getConversation();
    }


    private void getConversation() {
        unread = 0;
        WebServicesHandler.instance.getConversationList(authenticatedUser.getId(), new retrofit2.Callback<GetConversationsApi>() {
            @Override
            public void onResponse(Call<GetConversationsApi> call, Response<GetConversationsApi> response) {
                GetConversationsApi getConversationsApi = response.body();
                if (getConversationsApi != null) {
                    if (getConversationsApi.getResult() != null) {
                        for (int i = 0; i < getConversationsApi.getResult().size(); i++) {
                            for (int j = 0; j < getConversationsApi.getResult().get(i).getMessages().size(); j++) {
                                Message message = getConversationsApi.getResult().get(i).getMessages().get(j);
                                if (!message.getIsRead() && message.getSender().getId() != authenticatedUser.getId()) {
                                    unread++;
                                }
                            }
                        }

                        if (unread == 0) {
                            unreadMessageCount.setVisibility(View.GONE);
                        } else {
                            unreadMessageCount.setVisibility(View.VISIBLE);
                            unreadMessageCount.setText("" + unread);
                        }
                        ((DashboardActivity) getActivity()).updateMessagesCount(unread);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetConversationsApi> call, Throwable t) {
            }
        });
    }

    @OnClick(R.id.iv_profile)
    void selectImage() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1111);
        } else {
            choose();
        }
    }

    @OnClick(R.id.my_details)
    void myDetails() {
        Intent intent = new Intent(getActivity(), MyDetailsActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.shipping_address)
    void shippingAddress() {
        Intent intent = new Intent(getActivity(), ShippingAddressActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.layout_wishlist)
    void wishlist() {
        ((DashboardActivity) getActivity()).callFavNav();
    }

    @OnClick(R.id.my_orders)
    void myOrders() {
        Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.payment_method)
    void paymentMethods() {
        Intent intent = new Intent(getActivity(), MyPaymentActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.messages_layout)
    void messages() {
        Intent intent = new Intent(getActivity(), MessagesListActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.customer_support)
    void customerSupport() {
        Intent intent = new Intent(getActivity(), CustomerSupportActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public String getName() {
        return null;
    }

    private void choose() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    1112);
                        } else {
                            dispatchTakePictureIntent();
                        }
                    } catch (IOException e) {
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(getContext(),
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
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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
                        Picasso.get()
                                .load(file)
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.ic_profile)
                                .into(profile);
                        uploadToServer(file);
                    }
                }
            } else if (requestCode == 2) {
                if (data != null) {
                    String path = GlobalValues.getRealPathFromURI(getContext(), data.getData());
                    if (path != null) {
                        file = new File(path);
                        if (file.exists()) {
                            Picasso.get()
                                    .load(file)
                                    .transform(new CircleTransform())
                                    .placeholder(R.drawable.ic_profile)
                                    .into(profile);
                            uploadToServer(file);
                        } else {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                if (selectedImageUri.getPath() != null) {
                                    file = new File(selectedImageUri.getPath());
                                    if (file.exists()) {
                                        Picasso.get()
                                                .load(file)
                                                .transform(new CircleTransform())
                                                .placeholder(R.drawable.ic_profile)
                                                .into(profile);
                                        uploadToServer(file);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    String filePath(Uri uri) {

        String id = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            id = DocumentsContract.getDocumentId(uri);
        }
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(getActivity().getCacheDir().getAbsolutePath() + "/" + id);
        writeFile(inputStream, file);
        String filePath = file.getAbsolutePath();
        return filePath;
    }

    void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadToServer(File file) {
        Constants.showSpinner("Picture Uploading", getContext());
        WebServicesHandler apiClient = WebServicesHandler.instance;
        apiClient.uploadImage(authenticatedUser.getId(), file, new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Constants.dismissSpinner();
                UserResponse userResponse = response.body();

                if (userResponse != null) {
                    if (userResponse.getSuccess() == 1) {
                        if (userResponse.getUser() != null) {
                            GlobalValues.saveUser(getContext(), userResponse.getUser());
                            authenticatedUser = GlobalValues.getUser(getContext());
                            if (authenticatedUser.getProfileImagePath() != null && !authenticatedUser.getProfileImagePath().isEmpty()) {
                                Picasso.get()
                                        .load(Constants.URLS.BaseURLImages + authenticatedUser.getProfileImagePath())
                                        .transform(new CircleTransform())
                                        .placeholder(R.drawable.ic_profile)
                                        .into(profile);
                            } else {
                                Picasso.get()
                                        .load("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100&ssl=1")
                                        .transform(new CircleTransform())
                                        .placeholder(R.drawable.ic_profile)
                                        .into(profile);
                            }
                            ((DashboardActivity) getActivity()).updateUserDetails();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Constants.dismissSpinner();
            }
        });
    }


    private String convertImage() {

        String convert = null;
        if (file != null) {
            Bitmap bitmap = decodeFile(file.toString());
            if (bitmap != null) {
                ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                convert = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
            }
        }
        return convert;
    }


    public Bitmap decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
//        Bitmap b = ExifUtils.rotateBitmap(filePath, b1);
        return bitmap;
    }
}
