package com.northmeter.wartermeterdemo.utils;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 *@author  Lht
 *@time    2017/1/18 15:27
 *@des:    网络请求管理类
 */
public class NetUtils {
    private static final String TAG =NetUtils.class.getSimpleName() ;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    static {
        okHttpClient.setConnectTimeout(50, TimeUnit.SECONDS);
    }
    public static String doPostSubmit(String urlString, Map<String, String> map) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = map.get(key);
                builder.add(key, value);
            }
        }
        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(urlString)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 作用：实现网络访问文件，将获取到数据储存在文件流中
     *
     * @param urlString ：访问网络的url地址
     * @return InputStream
     */
    public static byte[] loadStreamFromURL(String urlString) {
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadStringFromURL(String urlString) {
        Request request = new Request.Builder()
                .url(urlString)
                .tag("")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 仅用于图像数字识别
     *
     * @param imagPath 图片的保存路径
     * @param numArea  图片中，矩形框区域
     */
    public static String doPostPic(final String imagPath, final String numArea) {
        String macFromWifi = GetPhoneInfo.getMacFromWifi();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Log.i(TAG,"--------参数----------");
        builder.add("ClientType", "Android");
        builder.add("UserID", "SZBD2016");
        builder.add("DeviceID", GetPhoneInfo.getIMEI());
        Log.i(TAG,"DeviceID= "+GetPhoneInfo.getIMEI());
        builder.add("MacAddress", macFromWifi);
        Log.i(TAG,"MacAddress= "+macFromWifi);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss-0SSS");//设置日期格式
        String timestamp = df.format(new Date());
        builder.add("Datetime", timestamp);
        Log.i(TAG,"Datetime= "+timestamp);
        builder.add("Province", "guangdong");
        builder.add("City", "shenzhen");
        String encoded = ImageUtil.picPathToStr(imagPath);
        builder.add("Image", encoded);
        Log.i(TAG,"encoded= "+encoded);
        builder.add("NumArea", numArea);
        Log.i(TAG,"NumArea= "+numArea);
        Log.i(TAG,"------------------");
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(Constants.ANALYSISNUM)
                .tag("LHT")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("LHT","netException "+e.toString());
        }
        return null;
    }

}

