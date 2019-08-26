package com.northmeter.wartermeterdemo.bean;

import java.util.List;

/**
 * @author zz
 * @time 2016/09/09 14:58
 * @des 忘记密码的bean
 */
public class ForgetPwdBean {

    /**
     * RESCODE : 1
     * RESMSG : 成功
     */

    private List<RESPONSEXMLBean> RESPONSEXML;

    public List<RESPONSEXMLBean> getRESPONSEXML() {
        return RESPONSEXML;
    }

    public void setRESPONSEXML(List<RESPONSEXMLBean> rESPONSEXML) {
        RESPONSEXML = rESPONSEXML;
    }

    public static class RESPONSEXMLBean {
        private String RESCODE;
        private String RESMSG;
        private String PasswordProblem1;
        private String PasswordProblem2;
        private String PasswordProblem3;

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

        public String getPasswordProblem1() {
            return PasswordProblem1;
        }

        public void setPasswordProblem1(String passwordProblem1) {
            PasswordProblem1 = passwordProblem1;
        }

        public String getPasswordProblem2() {
            return PasswordProblem2;
        }

        public void setPasswordProblem2(String passwordProblem2) {
            PasswordProblem2 = passwordProblem2;
        }

        public String getPasswordProblem3() {
            return PasswordProblem3;
        }

        public void setPasswordProblem3(String passwordProblem3) {
            PasswordProblem3 = passwordProblem3;
        }
    }
}
