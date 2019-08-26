package com.northmeter.wartermeterdemo.bean;

import java.util.List;

/**
 * @author zz
 * @time 2016/6/17/0017 9:47
 * @des 下载批量任务
 */
public class DownloadTaskBean {
    private List<DownloadTaskBeanRESPONSEXMLBean> RESPONSEXML;

    public List<DownloadTaskBeanRESPONSEXMLBean> getRESPONSEXML() {
        return RESPONSEXML;
    }

    public void setRESPONSEXML(List<DownloadTaskBeanRESPONSEXMLBean> rESPONSEXML) {
        RESPONSEXML = rESPONSEXML;
    }

}
