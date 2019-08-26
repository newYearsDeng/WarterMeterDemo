package com.northmeter.wartermeterdemo.bean;

import java.util.List;

/**
 * @author zz
 * @time 2016/08/30 8:40
 * @des 查询所有用户信息bean
 */
public class SearchAllUserMsgBean {

    /**
     * RESCODE : 1
     * RESMSG : 成功
     * name : [{"CustomerID":"0000000000000026","CustomerName":"深圳","month":[{"OperationTime":"2016-7-1 0:00:00","detail":[{"BasisPrice":"1.0000","ClearingValueTotal":"5.00","ECMoneySumTotal":"5.0000","OperationTime":"2016-7-1 0:00:00","eDataItemValue":"20.00","sDataItemValue":"15.00"}]}]},{"CustomerID":"0000000000000006","CustomerName":"邹鹏飞2","month":[{"OperationTime":"2016-7-1 0:00:00","detail":[{"BasisPrice":"512.0000","ClearingValueTotal":"394.40","ECMoneySumTotal":"267.4600","OperationTime":"2016-7-1 0:00:00","eDataItemValue":"264.34","sDataItemValue":"246.73"}]},{"OperationTime":"2016-8-1 0:00:00","detail":[{"BasisPrice":"512.0000","ClearingValueTotal":"138.46","ECMoneySumTotal":"194.6450","OperationTime":"2016-8-1 0:00:00","eDataItemValue":"195.64","sDataItemValue":"134.30"}]}]},{"CustomerID":"0000000000000006","CustomerName":"邹鹏飞2","month":[{"OperationTime":"2016-7-1 0:00:00","detail":[{"BasisPrice":"512.0000","ClearingValueTotal":"394.40","ECMoneySumTotal":"267.4600","OperationTime":"2016-7-1 0:00:00","eDataItemValue":"264.34","sDataItemValue":"246.73"}]},{"OperationTime":"2016-8-1 0:00:00","detail":[{"BasisPrice":"512.0000","ClearingValueTotal":"138.46","ECMoneySumTotal":"194.6450","OperationTime":"2016-8-1 0:00:00","eDataItemValue":"195.64","sDataItemValue":"134.30"}]}]}]
     */

    private String RESCODE;//状态码
    private String RESMSG;//返回信息
    /**
     * CustomerID : 0000000000000026
     * CustomerName : 深圳
     * month : [{"OperationTime":"2016-7-1 0:00:00","detail":[{"BasisPrice":"1.0000","ClearingValueTotal":"5.00","ECMoneySumTotal":"5.0000","OperationTime":"2016-7-1 0:00:00","eDataItemValue":"20.00","sDataItemValue":"15.00"}]}]
     */

    private List<NameBean> name;

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

    public List<NameBean> getName() {
        return name;
    }

    public void setName(List<NameBean> name) {
        this.name = name;
    }

    public static class NameBean {
        private String CustomerID;//用户id
        private String CustomerName;//用户姓名
        /**
         * OperationTime : 2016-7-1 0:00:00
         * detail : [{"BasisPrice":"1.0000","ClearingValueTotal":"5.00","ECMoneySumTotal":"5.0000","OperationTime":"2016-7-1 0:00:00","eDataItemValue":"20.00","sDataItemValue":"15.00"}]
         */

        private List<MonthBean> month;

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

        public List<MonthBean> getMonth() {
            return month;
        }

        public void setMonth(List<MonthBean> month) {
            this.month = month;
        }

        public static class MonthBean {
            private String OperationTime;//时间
            /**
             * BasisPrice : 1.0000
             * ClearingValueTotal : 5.00
             * ECMoneySumTotal : 5.0000
             * OperationTime : 2016-7-1 0:00:00
             * eDataItemValue : 20.00
             * sDataItemValue : 15.00
             */

            private List<DetailBean> detail;

            public String getOperationTime() {
                return OperationTime;
            }

            public void setOperationTime(String operationTime) {
                OperationTime = operationTime;
            }

            public List<DetailBean> getDetail() {
                return detail;
            }

            public void setDetail(List<DetailBean> detail) {
                this.detail = detail;
            }

            public static class DetailBean {
                private String BasisPrice;//基础水价
                private String ClearingValueTotal;//本月使用量
                private String ECMoneySumTotal;//应缴水费
                private String eDataItemValue;//本月抄底
                private String sDataItemValue;//上月抄底
                private String CustomerAddress;//用户地址

                public String getCustomerAddress() {
                    return CustomerAddress;
                }

                public void setCustomerAddress(String customerAddress) {
                    CustomerAddress = customerAddress;
                }


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

                public String getECMoneySumTotal() {
                    return ECMoneySumTotal;
                }

                public void setECMoneySumTotal(String eCMoneySumTotal) {
                    ECMoneySumTotal = eCMoneySumTotal;
                }


                public String getEDataItemValue() {
                    return eDataItemValue;
                }

                public void setEDataItemValue(String eDataItemValue) {
                    this.eDataItemValue = eDataItemValue;
                }

                public String getSDataItemValue() {
                    return sDataItemValue;
                }

                public void setSDataItemValue(String sDataItemValue) {
                    this.sDataItemValue = sDataItemValue;
                }
            }
        }
    }
}
