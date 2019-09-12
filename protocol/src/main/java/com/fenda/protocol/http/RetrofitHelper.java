package com.fenda.protocol.http;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.fenda.protocol.AppApplicaiton;
import com.fenda.protocol.util.DeviceIdUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 辅助类
 *
 * @author wzq
 * @date 19/08/27
 */

public class RetrofitHelper {

    private static String TAG = "RetrofitHelper";
    private long CONNECT_TIMEOUT = 20L;
    private long READ_TIMEOUT = 20L;
    private long WRITE_TIMEOUT = 20L;
    private static RetrofitHelper mInstance = null;
    private Retrofit mRetrofit = null;
    private static final String DEVICE_ID = DeviceIdUtil.getDeviceId();

    private static final String HTTP_APIKEY = "fendar03smartsound";
    private static final String HTTP_APISECRET = "sLR8ZC-855550-77536756780035706";

    public static RetrofitHelper getInstance(String baseUrl) {
        synchronized (RetrofitHelper.class) {
            if (mInstance == null) {
                mInstance = new RetrofitHelper(baseUrl);
            }
        }
        return mInstance;
    }

    private RetrofitHelper(String baseUrl) {
        init(baseUrl);
    }

    private void init(String baseUrl) {
        resetApp(baseUrl);
    }

    private void resetApp(String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build();
    }

    /**
     * 获取OkHttpClient实例
     *
     * @return
     */

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //这里可以选择拦截级别
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置 Debug Log 模式
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient okHttpClient = builder.retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new RqInterceptor())
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new AddSignInterceptor())
                .build();
        return okHttpClient;
    }


    private class AddSignInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            TreeMap allArgsTreeMap = new TreeMap();
            Set<String> formArgKeySet = request.url().queryParameterNames();

            ArrayList<String> nameList = new ArrayList<>();
            nameList.addAll(formArgKeySet);

            for (int i = 0; i < nameList.size(); i++) {

                allArgsTreeMap.put(nameList.get(i), request.url().queryParameterValues(nameList.get(i)).get(0));
            }


            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();

                for (int i = 0; i < formBody.size(); i++) {
                    allArgsTreeMap.put(formBody.encodedName(i), formBody.value(i));

//                    Log.e(TAG, "encodedValue " + formBody.encodedValue(i) + " value " + formBody.value(i));
                }
                //               bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
            } else {
                String tBodyStr = requestBodyToString(request.body());
                if (tBodyStr.length() > 0) {
                    Gson gson = new Gson();
                    try {
                        TreeMap argsMap = gson.fromJson(tBodyStr, TreeMap.class);
                        allArgsTreeMap.putAll(argsMap);
                    } catch (Exception ex) {
                        Log.e(TAG, "ex = " + ex + "+" + tBodyStr);
                    }
                }
            }

            String nonce = getRandomString(8);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

            allArgsTreeMap.put("apikey", HTTP_APIKEY);
            allArgsTreeMap.put("apisecret", HTTP_APISECRET);
            allArgsTreeMap.put("nonce", nonce);
            allArgsTreeMap.put("timestamp", timestamp);

            StringBuffer stringBuffer = new StringBuffer();
            Iterator<Map.Entry<String, String>> it = allArgsTreeMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String tWillMd5 = stringBuffer.substring(0, stringBuffer.length() - 1);

            //         String tWillMd5 = "apikey="+apiKey + "&apisecret=" + apisecret + "&timestamp=" + timestamp + "&nonce=" + nonce + "&mobile=15989349055" + "&password=123456";

            String tDoneMd5 = md5Decode(tWillMd5).toUpperCase();

            HttpUrl httpUrl = request.url().newBuilder().addQueryParameter("apikey", HTTP_APIKEY)
                    .addQueryParameter("nonce", nonce)
                    .addQueryParameter("timestamp", timestamp)
                    .addQueryParameter("sign", tDoneMd5).build();


            Log.d(TAG, "AddSignInterceptor = GET Param " + tWillMd5 + " " + tDoneMd5);

            request = request.newBuilder().url(httpUrl).build();

            return chain.proceed(request);
        }
    }


    public String md5Decode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public String getRandomString(int length) {

        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        return sb.toString();
    }

    /**
     * 请求拦截器
     */
    private class RqInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("X-APP-TYPE", "android")
                    .build();
            Response response = chain.proceed(request);
            return response;
        }
    }


    private class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            builder.addHeader("south-device-access-token", DeviceIdUtil.getDeviceId());
            builder.addHeader("version", getVerName(AppApplicaiton.getContext()) + "");

            Log.d(TAG, "south-device-access-token = " + DeviceIdUtil.getDeviceId());
            Log.d(TAG, "version = " + getVerName(AppApplicaiton.getContext()) + "");

            return chain.proceed(builder.build());
        }
    }

    /**
     * 获取版本号名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    private String requestBodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }

            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


    public <T> T getServer(Class<T> service) {
        return mRetrofit.create(service);
    }
}
