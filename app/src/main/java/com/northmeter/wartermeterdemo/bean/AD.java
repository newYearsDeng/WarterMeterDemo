package com.northmeter.wartermeterdemo.bean;

import org.litepal.crud.DataSupport;

import java.util.Arrays;

/**
 * created by lht on 2016/7/13 15:19
 */
public class AD extends DataSupport {
    private String url;
    private byte[] pic;

    public AD(byte[] pic, String url) {
        this.pic = pic;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "AD{" +
                "pic=" + Arrays.toString(pic) +
                ", url='" + url + '\'' +
                '}';
    }
}
