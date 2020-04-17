package com.qtech.saman.ui.activities.invoice;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.qtech.saman.data.model.OrderHistory;
import com.qtech.saman.data.model.OrderItem;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.OrderHistoryAPI;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.TrackingActivity;
import com.qtech.saman.ui.adapters.InvoiceAdapter;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
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

public class InvoiceActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;

    @BindView(R.id.tv_order_total)
    TextView orderTotalTextView;
    @BindView(R.id.tv_order_number)
    TextView orderNumberTextView;
    @BindView(R.id.tv_order_status)
    TextView orderStatusTextView;
    @BindView(R.id.tv_delivery_date)
    TextView deliveryDateTextView;
    @BindView(R.id.tv_order_date)
    TextView orderDateTextView;
    @BindView(R.id.tv_shipment_address)
    TextView shippingAddress;
    @BindView(R.id.tv_shipping_fee)
    TextView shippingFee;

    @BindView(R.id.tv_quantity)
    TextView quantity;
    @BindView(R.id.cart_item_recyclerView)
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<OrderItem> orderItems = new ArrayList<>();
    InvoiceAdapter invoiceAdapter;

    OrderHistory orderHistory;
    User authenticatedUser;
    int orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        authenticatedUser = GlobalValues.getUser(this);
        if (getIntent().hasExtra("OrderId")) {
            orderID = getIntent().getIntExtra("OrderId", 1);
            getInvoiceDetailes(orderID);
        }
        if (getIntent().hasExtra("Obj")) {
            orderHistory = (OrderHistory) getIntent().getSerializableExtra("Obj");
            setOrderDetails();
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getString(R.string.invoice));
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }
    }

    private void setOrderDetails() {
        Log.e("USERID", "--orderHistory--id--setOrderDetails--" + new Gson().toJson(orderHistory));
        if (orderHistory != null) {
            orderTotalTextView.setText(String.valueOf(orderHistory.getTotalPrice()) + " " + getString(R.string.OMR));
            if (orderHistory.getOrderNumber() != null) {
                orderNumberTextView.setText(orderHistory.getOrderNumber());
            }

            if (orderHistory.getStatus() != null) {
                if (!orderHistory.getStatus().equals("")) {
                    if (orderHistory.getStatus().equalsIgnoreCase("Denied")) {
                        orderStatusTextView.setText(getResources().getString(R.string.pending));
                    } else {
                        orderStatusTextView.setText(orderHistory.getStatus());
                    }
                }
            }

            if (orderHistory.getShippingTotal() != 0) {
                shippingFee.setText(String.valueOf(orderHistory.getShippingTotal()) + " " + getString(R.string.OMR));
            }

            Long orderDateStamp = Long.parseLong(orderHistory.getCreatedAt().replaceAll("\\D", ""));
            Date orderDate = new Date(orderDateStamp);
            DateFormat orderFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            String orderDateFormatted = orderFormatter.format(orderDate);
            orderDateTextView.setText(orderDateFormatted.toString());

            Long dateTimeStamp = Long.parseLong(orderHistory.getDeliveryDate().replaceAll("\\D", ""));
            Date date = new Date(dateTimeStamp);
//            DateFormat formatter = new SimpleDateFormat("EEEE, d MMM, yyyy",Locale.ENGLISH);
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy ");
            String dateFormatted = formatter.format(date);
            deliveryDateTextView.setText(dateFormatted.toString());

            if (orderHistory.getShippingAddress() != null) {
                String address = orderHistory.getShippingAddress().getAddressLine1().replace(",", ", ");
                address = address + ", " + orderHistory.getShippingAddress().getCity();
                address = address + ", " + orderHistory.getShippingAddress().getCountry();
                shippingAddress.setText(address);
            } else {
                shippingAddress.setText("Muscat" + ", " + "Oman");
            }
        }
        setBag();
    }

    List<OrderHistory> orderHistoryList = new ArrayList<>();

    private void getInvoiceDetailes(int orderID) {

        WebServicesHandler apiClient = WebServicesHandler.instance;
        Log.e("USERID", "--order--id--authenticatedUser--id--" + orderID);
        apiClient.getOrderIdDetailes(orderID, new Callback<OrderHistoryAPI>() {
            @Override
            public void onResponse(Call<OrderHistoryAPI> call, Response<OrderHistoryAPI> response) {

                Log.e("USERID", "--order--id--response----" + response.body());
                OrderHistoryAPI orderHistoryAPI = response.body();
                if (orderHistoryAPI != null) {
                    if (orderHistoryAPI.getResult() != null) {
                        Log.e("USERID", "--order--id--response----" + orderHistoryAPI.getResult());

                        orderHistoryList = new ArrayList<>();
                        orderHistoryList.addAll(orderHistoryAPI.getResult());
                        orderHistory = orderHistoryList.get(0);
                        setOrderDetails();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryAPI> call, Throwable t) {
                Log.e("onFailure", "" + t.getMessage());
            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_download_invoice)
    public void downloadInvoice() {
        if (ContextCompat.checkSelfPermission(InvoiceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(InvoiceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) InvoiceActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1111);
        } else {
            String downloadURL = Constants.URLS.Invoice_url + orderHistory.getID();

//            new DownloadFile().execute(downloadURL);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(downloadURL));
            startActivity(intent);
        }
//        Intent intent=new Intent(InvoiceActivity.this, ViewInvoice.class);
//        intent.putExtra("OrderID",orderHistory.getID());
//        startActivity(intent);
    }

    @OnClick(R.id.button_track_order)
    public void trackOrder() {
        Intent intent = new Intent(InvoiceActivity.this, TrackingActivity.class);
        intent.putExtra("Obj", orderHistory);
        startActivity(intent);
    }

    private void setBag() {
        layoutManager = new LinearLayoutManager(InvoiceActivity.this);
        cartRecyclerView.setLayoutManager(layoutManager);
        cartRecyclerView.setNestedScrollingEnabled(false);
        orderItems = new ArrayList<>();
        orderItems.addAll(orderHistory.getOrderItems());
        invoiceAdapter = new InvoiceAdapter(InvoiceActivity.this, orderItems);
        cartRecyclerView.setAdapter(invoiceAdapter);

        quantity.setText(orderItems.size() + " " + getResources().getQuantityString(R.plurals.items, orderItems.size()));
    }

    private void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }


    private void down(String fileURL) {
        try {
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            String PATH = Environment.getExternalStorageDirectory().toString() + "/load";
            Log.e("PATH", PATH);
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(String.valueOf(file));
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[4096];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
            Toast.makeText(this, " A new file is downloaded successfully", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(InvoiceActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
//                fileName = timestamp + "_" + fileName + "_invoice.pdf";
                fileName = timestamp + "_" + fileName + "_invoice.doc";

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Saman/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
//                  Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return folder + fileName;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
            if (message != null) {
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File file = new File(message);
                    Uri uri = FileProvider.getUriForFile(InvoiceActivity.this, getPackageName() + ".provider", file);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(message), "application/pdf");
                    intent = Intent.createChooser(intent, "Open File");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            // Display File path after downloading
            Toast.makeText(getApplicationContext(), getString(R.string.downloaded_invoice), Toast.LENGTH_LONG).show();
        }
    }
}
