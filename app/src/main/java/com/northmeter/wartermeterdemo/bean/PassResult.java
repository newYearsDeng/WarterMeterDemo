package com.northmeter.wartermeterdemo.bean;

import java.util.List;

/**
 * webService 接口访问返回的信息
 * created by lht on 2016/5/30 16:48
 */
public class PassResult {

    /**
     * RESCODE : 1
     * RESMSG : 成功
     */

    private List<RESPONSEXMLBean> RESPONSEXML;

    public List<RESPONSEXMLBean> getRESPONSEXML() {
        return RESPONSEXML;
    }

    public void setRESPONSEXML(List<RESPONSEXMLBean> RESPONSEXML) {
        this.RESPONSEXML = RESPONSEXML;
    }

    public static class RESPONSEXMLBean {
        private String RESCODE;
        private String RESMSG;
        private String VersionAppName;
        private String VersionNumber;

        public String getVersionAppName() {
            return VersionAppName;
        }

        public void setVersionAppName(String versionAppName) {
            VersionAppName = versionAppName;
        }

        public String getVersionNumber() {
            return VersionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            VersionNumber = versionNumber;
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
    }
}
