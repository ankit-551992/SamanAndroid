/*
 * Copyright (c) 2016 Mastercard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mastercard.gateway.android.sdk;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.Single;

/**
 * The public interface to the Gateway SDK.
 * <p>
 * Example set up:
 * <p>
 * <code>
 * Gateway gateway = new Gateway();
 * gateway.setMerchantId("your-merchant-id");
 * gateway.setRegion(Gateway.Region.NORTH_AMERICA);
 * </code>
 */
@SuppressWarnings("unused,WeakerAccess")
public class Gateway {

    /**
     * The available gateway regions
     */
    public enum Region {
        ASIA_PACIFIC("ap"),
        EUROPE("eu"),
        NORTH_AMERICA("na"),
        MTF("test");

        String prefix;

        Region(String prefix) {
            this.prefix = prefix;
        }

        String getPrefix() {
            return prefix;
        }
    }

    // internally supported request methods
    enum Method {
        PUT
    }


    static final int MIN_API_VERSION = 39;
    static final int CONNECTION_TIMEOUT = 15000;
    static final int READ_TIMEOUT = 60000;
    static final int REQUEST_3D_SECURE = 14137;
    static final String API_OPERATION = "UPDATE_PAYER_DATA";
    static final String USER_AGENT = "Gateway-Android-SDK/" + BuildConfig.VERSION_NAME;
    static final String INTERMEDIATE_CA = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFAzCCA+ugAwIBAgIEUdNg7jANBgkqhkiG9w0BAQsFADCBvjELMAkGA1UEBhMC\n" +
            "VVMxFjAUBgNVBAoTDUVudHJ1c3QsIEluYy4xKDAmBgNVBAsTH1NlZSB3d3cuZW50\n" +
            "cnVzdC5uZXQvbGVnYWwtdGVybXMxOTA3BgNVBAsTMChjKSAyMDA5IEVudHJ1c3Qs\n" +
            "IEluYy4gLSBmb3IgYXV0aG9yaXplZCB1c2Ugb25seTEyMDAGA1UEAxMpRW50cnVz\n" +
            "dCBSb290IENlcnRpZmljYXRpb24gQXV0aG9yaXR5IC0gRzIwHhcNMTQxMDIyMTcw\n" +
            "NTE0WhcNMjQxMDIzMDczMzIyWjCBujELMAkGA1UEBhMCVVMxFjAUBgNVBAoTDUVu\n" +
            "dHJ1c3QsIEluYy4xKDAmBgNVBAsTH1NlZSB3d3cuZW50cnVzdC5uZXQvbGVnYWwt\n" +
            "dGVybXMxOTA3BgNVBAsTMChjKSAyMDEyIEVudHJ1c3QsIEluYy4gLSBmb3IgYXV0\n" +
            "aG9yaXplZCB1c2Ugb25seTEuMCwGA1UEAxMlRW50cnVzdCBDZXJ0aWZpY2F0aW9u\n" +
            "IEF1dGhvcml0eSAtIEwxSzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "ANo/ltBNuS9E59s5XptQ7lylYdpBZ1MJqgCajld/KWvbx+EhJKo60I1HI9Ltchbw\n" +
            "kSHSXbe4S6iDj7eRMmjPziWTLLJ9l8j+wbQXugmeA5CTe3xJgyJoipveR8MxmHou\n" +
            "fUAL0u8+07KMqo9Iqf8A6ClYBve2k1qUcyYmrVgO5UK41epzeWRoUyW4hM+Ueq4G\n" +
            "RQyja03Qxr7qGKQ28JKyuhyIjzpSf/debYMcnfAf5cPW3aV4kj2wbSzqyc+UQRlx\n" +
            "RGi6RzwE6V26PvA19xW2nvIuFR4/R8jIOKdzRV1NsDuxjhcpN+rdBQEiu5Q2Ko1b\n" +
            "Nf5TGS8IRsEqsxpiHU4r2RsCAwEAAaOCAQkwggEFMA4GA1UdDwEB/wQEAwIBBjAP\n" +
            "BgNVHRMECDAGAQH/AgEAMDMGCCsGAQUFBwEBBCcwJTAjBggrBgEFBQcwAYYXaHR0\n" +
            "cDovL29jc3AuZW50cnVzdC5uZXQwMAYDVR0fBCkwJzAloCOgIYYfaHR0cDovL2Ny\n" +
            "bC5lbnRydXN0Lm5ldC9nMmNhLmNybDA7BgNVHSAENDAyMDAGBFUdIAAwKDAmBggr\n" +
            "BgEFBQcCARYaaHR0cDovL3d3dy5lbnRydXN0Lm5ldC9ycGEwHQYDVR0OBBYEFIKi\n" +
            "cHTdvFM/z3vU981/p2DGCky/MB8GA1UdIwQYMBaAFGpyJnrQHu995ztpUdRsjZ+Q\n" +
            "EmarMA0GCSqGSIb3DQEBCwUAA4IBAQA/HBpb/0AiHY81DC2qmSerwBEycNc2KGml\n" +
            "jbEnmUK+xJPrSFdDcSPE5U6trkNvknbFGe/KvG9CTBaahqkEOMdl8PUM4ErfovrO\n" +
            "GhGonGkvG9/q4jLzzky8RgzAiYDRh2uiz2vUf/31YFJnV6Bt0WRBFG00Yu0GbCTy\n" +
            "BrwoAq8DLcIzBfvLqhboZRBD9Wlc44FYmc1r07jHexlVyUDOeVW4c4npXEBmQxJ/\n" +
            "B7hlVtWNw6f1sbZlnsCDNn8WRTx0S5OKPPEr9TVwc3vnggSxGJgO1JxvGvz8pzOl\n" +
            "u7sY82t6XTKH920l5OJ2hiEeEUbNdg5vT6QhcQqEpy02qUgiUX6C\n" +
            "-----END CERTIFICATE-----\n";


    Logger logger = new BaseLogger();
    Gson gson = new Gson();
    String merchantId;
    Region region;


    /**
     * Constructs a new instance.
     */
    public Gateway() {
    }

    /**
     * Gets the current Merchant ID
     *
     * @return The current Merchant ID
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the current Merchant ID
     *
     * @param merchantId A valid Merchant ID
     * @return The <tt>Gateway</tt> instance
     * @throws IllegalArgumentException If the provided Merchant ID is null
     */
    public Gateway setMerchantId(String merchantId) {
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID may not be null");
        }

        this.merchantId = merchantId;

        return this;
    }

    /**
     * Gets the current {@link Region}
     *
     * @return The region
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Sets the current {@link Region} to target
     *
     * @param region The region
     * @return The <tt>Gateway</tt> instance
     * @throws IllegalArgumentException If the provided Merchant ID is null
     */
    public Gateway setRegion(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region may not be null");
        }

        this.region = region;

        return this;
    }

    /**
     * Updates a Mastercard Gateway session with the provided information.<br>
     * The API version number provided MUST match the version used when the session was created.
     * <p>
     * This will execute the necessary network request on a background thread
     * and return the response (or error) to the provided callback.
     *
     * @param sessionId  A session ID from the Mastercard Gateway
     * @param apiVersion The API version number used when the session was created
     * @param payload    A map of the request data
     * @param callback   A callback to handle success and error messages
     * @throws IllegalArgumentException If the provided session id is null
     */
    public void updateSession(String sessionId, String apiVersion, GatewayMap payload, GatewayCallback callback) {
        String url = getUpdateSessionUrl(sessionId, apiVersion);
        payload.put("apiOperation", API_OPERATION);
        payload.put("device.browser", USER_AGENT);
        runGatewayRequest(url, Method.PUT, payload, callback);
    }

    /**
     * Updates a Mastercard Gateway session with the provided information.
     * The API version number provided MUST match the version used when the session was created.
     * <p>
     * Does not adhere to any particular scheduler
     *
     * @param sessionId  A session ID from the Mastercard Gateway
     * @param apiVersion The API version number used when the session was created
     * @param payload    A map of the request data
     * @return A <tt>Single</tt> of the response map
     * @throws IllegalArgumentException If the provided session id is null
     * @see <a href="http://reactivex.io/RxJava/javadoc/io/reactivex/Single.html">RxJava: Single</a>
     */
    public Single<GatewayMap> updateSession(String sessionId, String apiVersion, GatewayMap payload) {
        String url = getUpdateSessionUrl(sessionId, apiVersion);
        payload.put("apiOperation", API_OPERATION);
        payload.put("device.browser", USER_AGENT);
        return runGatewayRequest(url, Method.PUT, payload);
    }

    /**
     * Starts the {@link Gateway3DSecureActivity} for result, initializing it with the provided html
     *
     * @param activity The calling activity context
     * @param html     The initial HTML to render in the web view
     */
    public static void start3DSecureActivity(Activity activity, String html) {
        start3DSecureActivity(activity, html, null);
    }

    /**
     * Starts the {@link Gateway3DSecureActivity} for result, initializing it with the provided html
     *
     * @param activity The calling activity context
     * @param html     The initial HTML to render in the web view
     * @param title    An optional title to render in the toolbar
     */
    public static void start3DSecureActivity(Activity activity, String html, String title) {
        Intent intent = new Intent(activity, Gateway3DSecureActivity.class);
        intent.putExtra(Gateway3DSecureActivity.EXTRA_HTML, html); // required

        if (title != null) {
            intent.putExtra(Gateway3DSecureActivity.EXTRA_TITLE, title);
        }

        activity.startActivityForResult(intent, REQUEST_3D_SECURE);
    }

    /**
     * A convenience method helper for handling activity result messages returned from {@link Gateway3DSecureActivity}.
     * This method should be called within the calling Activity's onActivityResult() lifecycle method.
     * This helper only works if the 3-D Secure Activity was launched using the
     * {@link Gateway#start3DSecureActivity(Activity, String, String)} method.
     *
     * @param requestCode The request code returning from the activity result
     * @param resultCode The result code returning from the activity result
     * @param data The intent data returning from the activity result
     * @param callback An implementation of {@link Gateway3DSecureCallback}
     * @return True if handled, False if not
     * @see Gateway#start3DSecureActivity(Activity, String)
     * @see Gateway#start3DSecureActivity(Activity, String, String)
     */
    public static boolean handle3DSecureResult(int requestCode, int resultCode, Intent data, Gateway3DSecureCallback callback) {
        if (data == null || callback == null) {
            return false;
        }

        if (requestCode == REQUEST_3D_SECURE) {
            if (resultCode == Activity.RESULT_OK) {
                // check for error
                String errorMessage = data.getStringExtra(Gateway3DSecureActivity.EXTRA_ERROR);
                if (errorMessage != null) {
                    callback.on3DSecureError(errorMessage);
                    return true;
                }

                // get the basic txn details
                String threeDSecureId = data.getStringExtra(Gateway3DSecureActivity.EXTRA_3D_SECURE_ID);
                String summaryStatus = data.getStringExtra(Gateway3DSecureActivity.EXTRA_SUMMARY_STATUS);

                callback.on3DSecureComplete(summaryStatus, threeDSecureId);
            } else {
                callback.on3DSecureCancel();
            }

            return true;
        }

        return false;
    }


    String getApiUrl(String apiVersion) {
        if (Integer.valueOf(apiVersion) < MIN_API_VERSION) {
            throw new IllegalArgumentException("API version must be >= " + MIN_API_VERSION);
        }

        if (region == null) {
            throw new IllegalStateException("You must initialize the the Gateway instance with a Region before use");
        }

        return "https://" + region.getPrefix() + "-gateway.mastercard.com/api/rest/version/" + apiVersion;
    }

    String getUpdateSessionUrl(String sessionId, String apiVersion) {
        if (sessionId == null) {
            throw new IllegalArgumentException("Session Id may not be null");
        }

        if (merchantId == null) {
            throw new IllegalStateException("You must initialize the the Gateway instance with a Merchant Id before use");
        }

        return getApiUrl(apiVersion) + "/merchant/" + merchantId + "/session/" + sessionId;
    }

    void runGatewayRequest(String url, Method method, GatewayMap payload, GatewayCallback callback) {
        // create handler on current thread
        Handler handler = new Handler(msg -> handleCallbackMessage(callback, msg.obj));

        new Thread(() -> {
            Message m = handler.obtainMessage();
            try {
                m.obj = executeGatewayRequest(url, method, payload);
            } catch (Exception e) {
                m.obj = e;
            }
            handler.sendMessage(m);
        }).start();
    }

    Single<GatewayMap> runGatewayRequest(String url, Method method, GatewayMap payload) {
        return Single.fromCallable(() -> executeGatewayRequest(url, method, payload));
    }

    // handler callback method when executing a request on a new thread
    @SuppressWarnings("unchecked")
    boolean handleCallbackMessage(GatewayCallback callback, Object arg) {
        if (callback != null) {
            if (arg instanceof Throwable) {
                callback.onError((Throwable) arg);
            } else {
                callback.onSuccess((GatewayMap) arg);
            }
        }
        return true;
    }

    GatewayMap executeGatewayRequest(String endpoint, Method method, GatewayMap payload) throws Exception {
        // init ssl context with limiting trust managers
        SSLContext context = createSslContext();

        // parse url
        URL url = new URL(endpoint);

        // init connection
        HttpsURLConnection c = createHttpsUrlConnection(url, context, method);

        // encode request data to json
        String requestData = gson.toJson(payload);

        // log request data
        logger.logRequest(c, requestData);

        // write request data
        if (requestData != null) {
            OutputStream os = c.getOutputStream();
            os.write(requestData.getBytes("UTF-8"));
            os.close();
        }

        // initiate the connection
        c.connect();

        String responseData = null;
        int statusCode = c.getResponseCode();
        boolean isStatusOk = (statusCode >= 200 && statusCode < 300);

        // if connection has output stream, get the data
        // socket time-out exceptions will be thrown here
        if (c.getDoInput()) {
            InputStream is = isStatusOk ? c.getInputStream() : c.getErrorStream();
            responseData = inputStreamToString(is);
            is.close();
        }

        c.disconnect();

        // log response
        logger.logResponse(c, responseData);

        // parse the response body
        GatewayMap response = new GatewayMap(responseData);

        // if response static is good, return response
        if (isStatusOk) {
            return response;
        }

        // otherwise, create a gateway exception and throw it
        String message = (String) response.get("error.explanation");
        if (message == null) {
            message = "An error occurred";
        }

        GatewayException exception = new GatewayException(message);
        exception.setStatusCode(statusCode);
        exception.setErrorResponse(response);

        throw exception;
    }

    SSLContext createSslContext() throws Exception {
        // create and initialize a KeyStore
        KeyStore keyStore = createSslKeyStore();

        // create a TrustManager that trusts the INTERMEDIATE_CA in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustManagers, null);

        return context;
    }

    KeyStore createSslKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);

        // add our trusted cert to the keystore
        keyStore.setCertificateEntry("gateway.mastercard.com", readCertificate(INTERMEDIATE_CA));

        return keyStore;
    }

    X509Certificate readCertificate(String cert) throws CertificateException {
        byte[] bytes = cert.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);

        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
    }

    HttpsURLConnection createHttpsUrlConnection(URL url, SSLContext context, Method method) throws IOException {
        HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
        c.setSSLSocketFactory(context.getSocketFactory());
        c.setConnectTimeout(CONNECTION_TIMEOUT);
        c.setReadTimeout(READ_TIMEOUT);
        c.setRequestMethod(method.name());
        c.setRequestProperty("User-Agent", USER_AGENT);
        c.setRequestProperty("Content-Type", "application/json");
        c.setDoOutput(true);

        return c;
    }


    boolean isStatusCodeOk(int statusCode) {
        return (statusCode >= 200 && statusCode < 300);
    }

    String inputStreamToString(InputStream is) throws IOException {
        // get buffered reader from stream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // read stream into string builder
        StringBuilder total = new StringBuilder();

        String line;
        while ((line = rd.readLine()) != null) {
            total.append(line);
        }

        return total.toString();
    }
}