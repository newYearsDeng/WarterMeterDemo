package com.northmeter.wartermeterdemo.activity;

/**
 * @author lht
 *
 * 这个对象是我自己封装的.和微信支付的sdk没有关系
 */
/**
 {"sign_method":"SHA1",
 "timestamp":"1437640097",
 "noncestr":"2015072335521628162163",
 "partnerid":"1218554401",
 "app_signature":"3e978ec98cd9aefba2e2f94240b34252b9e67aad",
 "prepayid":"12010000001507237fba6bd1dc972271",
 "package1":"Sign=WXPay",
 "appid":"wx71d0b68f17483ced"}
 */
public class WXPayData {
    private String sign_method;
    //时间戳
    private String timestamp;
    //随机字符串
    private String noncestr;
    //商户号
    private String partnerid;
    private String app_signature;
    //预支付交易会话ID
    private String prepayid;
    //扩展字段
    private String package1;
    //应用id
    private String appid;

    public String getSign_method() {
        return sign_method;
    }

    public void setSign_method(String sign_method) {
        this.sign_method = sign_method;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getApp_signature() {
        return app_signature;
    }

    public void setApp_signature(String app_signature) {
        this.app_signature = app_signature;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackage1() {
        return package1;
    }

    public void setPackage1(String package1) {
        this.package1 = package1;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public WXPayData(String sign_method, String timestamp, String noncestr, String partnerid, String app_signature,
                     String prepayid, String package1, String appid) {
        super();
        this.sign_method = sign_method;
        this.timestamp = timestamp;
        this.noncestr = noncestr;
        this.partnerid = partnerid;
        this.app_signature = app_signature;
        this.prepayid = prepayid;
        this.package1 = package1;
        this.appid = appid;
    }

    @Override
    public String toString() {
        return "WXPayData [sign_method=" + sign_method + ", timestamp=" + timestamp + ", noncestr=" + noncestr
                + ", partnerid=" + partnerid + ", app_signature=" + app_signature + ", prepayid=" + prepayid
                + ", package1=" + package1 + ", appid=" + appid + "]";
    }

}
