package com.northmeter.wartermeterdemo.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * created by lht on 2016/8/5 13:50
 */
public class PayInfo {

    /**
     * RESCODE : 1
     * RESMSG : 成功
     */

    private static final char TOP_LEFT_CORNER = '╔';
    private static final char TOP_RIGHT_CORNER = '╗';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char BOTTOM_RIGHT_CORNER = '╝';
    private static final char MIDDLE_LEFT_CORNER = '╟';
    private static final char MIDDLE_RIGHT_CORNER = '╢';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "══════════════";
    private static final String SINGLE_DIVIDER = "───────────────";
    private static final String TOP_BORDER = SINGLE_DIVIDER;
    private static final String BOTTOM_BORDER = SINGLE_DIVIDER;
    private static final String MIDDLE_BORDER = SINGLE_DIVIDER;
    private List<RESPONSEXMLBean> RESPONSEXML;

    public List<RESPONSEXMLBean> getRESPONSEXML() {
        return RESPONSEXML;
    }

    public void setRESPONSEXML(List<RESPONSEXMLBean> RESPONSEXML) {
        this.RESPONSEXML = RESPONSEXML;
    }

    public static class RESPONSEXMLBean implements Serializable {
        private String RESCODE;
        private String RESMSG;
        //客户名称
        private String CustomerName;
        //用户编号
        private String CustomerID;
        //用户地址
        private String CustomerAddress;
        //上期表底
        private String sDataItemValue;
        //本期表底
        private String eDataItemValue;
        //本期用水量
        private String ClearingValueTotal;
        //水价 （元/t）
        private String BasisPrice;
        //用水金额（元）
        private String ECMoneySumTotal;

        public String getBasisPrice() {
            return BasisPrice;
        }

        public void setBasisPrice(String basisPrice) {
            BasisPrice = basisPrice;
        }

        public String getClearingValueTotal() {
            return ClearingValueTotal;
        }

        public void setClearingValueTotal(String clearingValueTotal) {
            ClearingValueTotal = clearingValueTotal;
        }

        public String getCustomerAddress() {
            return CustomerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            CustomerAddress = customerAddress;
        }

        public String getCustomerID() {
            return CustomerID;
        }

        public void setCustomerID(String customerID) {
            CustomerID = customerID;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public String getECMoneySumTotal() {
            return ECMoneySumTotal;
        }

        public void setECMoneySumTotal(String ECMoneySumTotal) {
            this.ECMoneySumTotal = ECMoneySumTotal;
        }

        public String geteDataItemValue() {
            return eDataItemValue;
        }

        public void seteDataItemValue(String eDataItemValue) {
            this.eDataItemValue = eDataItemValue;
        }

        public String getsDataItemValue() {
            return sDataItemValue;
        }

        public void setsDataItemValue(String sDataItemValue) {
            this.sDataItemValue = sDataItemValue;
        }

        public String getRESCODE() {
            return RESCODE;
        }

        public void setRESCODE(String RESCODE) {
            this.RESCODE = RESCODE;
        }

        public String getRESMSG() {
            return RESMSG;
        }

        public void setRESMSG(String RESMSG) {
            this.RESMSG = RESMSG;
        }

        public RESPONSEXMLBean(String basisPrice, String clearingValueTotal, String customerAddress, String customerID, String customerName, String ECMoneySumTotal, String eDataItemValue, String RESCODE, String RESMSG, String sDataItemValue) {
            BasisPrice = basisPrice;
            ClearingValueTotal = clearingValueTotal;
            CustomerAddress = customerAddress;
            CustomerID = customerID;
            CustomerName = customerName;
            this.ECMoneySumTotal = ECMoneySumTotal;
            this.eDataItemValue = eDataItemValue;
            this.RESCODE = RESCODE;
            this.RESMSG = RESMSG;
            this.sDataItemValue = sDataItemValue;
        }
        @Override
        public String toString() {
            String timeStamp = new SimpleDateFormat("yyyy年MM月").format(new Date());
            return "        "+timeStamp+"水量缴费信息" + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "客户名称      " + CustomerName + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "用户编号      " + CustomerID + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "用户地址      " + CustomerAddress + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "上期表底      " + sDataItemValue + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "本期表底      " + eDataItemValue + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "月使用量（t）    " + ClearingValueTotal + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "水价（元/t）  " + BasisPrice + "元" + "\n" +
                    MIDDLE_BORDER + "\n" +
                    "总金额（元）  " + ECMoneySumTotal + "元" + "\n" +
                    BOTTOM_BORDER;
        }
    }
}
