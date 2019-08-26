package com.northmeter.wartermeterdemo.bean;

import java.util.List;

/**
 * created by lht on 2016/7/13 15:21
 */
public class DownloadAd  {

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
        private String PhoteFile;
        private String ImgLinkAddress;

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

        public String getImgLinkAddress() {
            return ImgLinkAddress;
        }

        public void setImgLinkAddress(String imgLinkAddress) {
            ImgLinkAddress = imgLinkAddress;
        }

        public String getPhoteFile() {
            return PhoteFile;
        }

        public void setPhoteFile(String photeFile) {
            PhoteFile = photeFile;
        }
    }
}
