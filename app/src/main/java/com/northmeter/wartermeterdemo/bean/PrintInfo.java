package com.northmeter.wartermeterdemo.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by lht on 2016/7/6 15:45
 */
public class PrintInfo {
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

    public PrintInfo(String basisPrice, String clearingValueTotal, String customerAddress, String customerID, String customerName, String ECMoneySumTotal, String eDataItemValue, String sDataItemValue) {
        BasisPrice = basisPrice;
        ClearingValueTotal = clearingValueTotal;
        CustomerAddress = customerAddress;
        CustomerID = customerID;
        CustomerName = customerName;
        this.ECMoneySumTotal = ECMoneySumTotal;
        this.eDataItemValue = eDataItemValue;
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
                "本月使用量（t）    " + ClearingValueTotal + "\n" +
                MIDDLE_BORDER + "\n" +
                "水价（元/t）  " + BasisPrice + "元" + "\n" +
                MIDDLE_BORDER + "\n" +
                "总金额（元）  " + ECMoneySumTotal + "元" + "\n" +
                BOTTOM_BORDER;
    }
}
