package com.northmeter.wartermeterdemo.bean;

import java.util.List;

/**
 * @author zz
 * @time 2016/5/25 15:27
 * @des ${TODO}
 */
public class GetCustomerInfoBean {


    private List<RESPONSEXMLBean> RESPONSEXML;

    public List<RESPONSEXMLBean> getRESPONSEXML() {
        return RESPONSEXML;
    }

    public void setRESPONSEXML(List<RESPONSEXMLBean> rESPONSEXML) {
        RESPONSEXML = rESPONSEXML;
    }

    public static class RESPONSEXMLBean {
        private String RESCODE;//返回码
        private String RESMSG;//返回码对应的信息
        private String CUSTOMER_ADDR;//客户地址
        private String CUSTOMER_GASID;//客户气表编号
        private String CUSTOMER_ID;//客户编号
        private String CUSTOMER_NAME;//客户姓名
        private String CUSTOMER_WATERID;//客户水表编号
        private String STATE_CODE;//手机号码状态码
        private String WATER_CUSTOMERID;//客户水表子用户号
        private String USEMONEY;//结算金额
        private String USEVALUE;//本月实际使用值
        private String BASISPRICE;//本月结算时的基础单价
        private String USEDATE;//结算月份
        private String ENDVALUE;//本月抄表数
        private String STARTVALUE;//上月抄表数
        private String OPER_TYPE;//操作员类型


        public String getOPER_TYPE() {
            return OPER_TYPE;
        }

        public void setOPER_TYPE(String OPER_TYPE) {
            this.OPER_TYPE = OPER_TYPE;
        }


        public String getUSEMONEY() {
            return USEMONEY;
        }

        public void setUSEMONEY(String USEMONEY) {
            this.USEMONEY = USEMONEY;
        }

        public String getUSEVALUE() {
            return USEVALUE;
        }

        public void setUSEVALUE(String USEVALUE) {
            this.USEVALUE = USEVALUE;
        }

        public String getBASISPRICE() {
            return BASISPRICE;
        }

        public void setBASISPRICE(String BASISPRICE) {
            this.BASISPRICE = BASISPRICE;
        }

        public String getUSEDATE() {
            return USEDATE;
        }

        public void setUSEDATE(String USEDATE) {
            this.USEDATE = USEDATE;
        }

        public String getENDVALUE() {
            return ENDVALUE;
        }

        public void setENDVALUE(String ENDVALUE) {
            this.ENDVALUE = ENDVALUE;
        }

        public String getSTARTVALUE() {
            return STARTVALUE;
        }

        public void setSTARTVALUE(String STARTVALUE) {
            this.STARTVALUE = STARTVALUE;
        }

        public String getWATER_CUSTOMERID() {
            return WATER_CUSTOMERID;
        }

        public void setWATER_CUSTOMERID(String WATER_CUSTOMERID) {
            this.WATER_CUSTOMERID = WATER_CUSTOMERID;
        }


        public String getCUSTOMER_ADDR() {
            return CUSTOMER_ADDR;
        }

        public void setCUSTOMER_ADDR(String CUSTOMER_ADDR) {
            this.CUSTOMER_ADDR = CUSTOMER_ADDR;
        }

        public String getCUSTOMER_GASID() {
            return CUSTOMER_GASID;
        }

        public void setCUSTOMER_GASID(String CUSTOMER_GASID) {
            this.CUSTOMER_GASID = CUSTOMER_GASID;
        }

        public String getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(String CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public String getCUSTOMER_NAME() {
            return CUSTOMER_NAME;
        }

        public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
            this.CUSTOMER_NAME = CUSTOMER_NAME;
        }

        public String getCUSTOMER_WATERID() {
            return CUSTOMER_WATERID;
        }

        public void setCUSTOMER_WATERID(String CUSTOMER_WATERID) {
            this.CUSTOMER_WATERID = CUSTOMER_WATERID;
        }

        public String getSTATE_CODE() {
            return STATE_CODE;
        }

        public void setSTATE_CODE(String STATE_CODE) {
            this.STATE_CODE = STATE_CODE;
        }

        public String getRESCODE() {
            return RESCODE;
        }

        public void setRESCODE(String rESCODE) {
            RESCODE = rESCODE;
        }

        public String getRESMSG() {
            return RESMSG;
        }

        public void setRESMSG(String rESMSG) {
            RESMSG = rESMSG;
        }
    }
}
