package com.northmeter.wartermeterdemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.bean.AD;
import com.northmeter.wartermeterdemo.bean.DownloadAd;
import com.northmeter.wartermeterdemo.utils.NetUtils;

import org.litepal.crud.DataSupport;

import java.io.InputStream;

/**
 * created by lht on 2016/7/6 10:59
 */
public class LoadAdService extends IntentService {
    private  String LoadAdURL="http://218.17.157.121:9147/Service1.asmx/RandomImg? ";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */

    public LoadAdService() {
        super("");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String s = NetUtils.loadStringFromURL(LoadAdURL);
        if(TextUtils.isEmpty(s)){
            return;
        }
        String result = Html.fromHtml(s).toString();
        DownloadAd downloadAd = new Gson().fromJson(result, DownloadAd.class);
        if(downloadAd.getRESPONSEXML().get(0).getRESCODE().equals("1")){
            DownloadAd.RESPONSEXMLBean responsexmlBean = downloadAd.getRESPONSEXML().get(1);
            String photeFile = responsexmlBean.getPhoteFile();
            String imgLinkAddress = responsexmlBean.getImgLinkAddress();
            Log.i("LHT","photeFile "+photeFile);
            Log.i("LHT","imgLinkAddress "+imgLinkAddress);
            byte[] bytes = NetUtils.loadStreamFromURL(photeFile);
            AD ad=new AD(bytes,imgLinkAddress);
            //先把所有数据删除掉
            DataSupport.deleteAll(AD.class);
            //然后再保存
            boolean save = ad.save();
            Log.i("LHT","issava "+save);

        }
    }
}
