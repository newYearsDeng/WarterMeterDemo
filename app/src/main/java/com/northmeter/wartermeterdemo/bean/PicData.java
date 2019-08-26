package com.northmeter.wartermeterdemo.bean;


import java.io.Serializable;

/**
 * 图像数字识别返回的信息
 * created by lht on 2016/5/10 14:33
 */
public class PicData implements Serializable {

    /**
     * Engine : mobile
     * Code : 0
     * NumArea : 88,248,416,87
     * Message : 检测成功
     * Circle : -2,-2,-2
     * filename : 20170320091628_9c846f03529942a390160b4b0b14beea.jpg
     * Pos : 214,268,374,267,374,312,215,313
     * Value : 00510.0,7
     * ProcessTime : 328
     */

    private String Engine;
    private String Code;
    private String NumArea;
    private String Message;
    private String Circle;
    private String filename;
    private String Pos;
    private String Value;
    private String ProcessTime;

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String Engine) {
        this.Engine = Engine;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getNumArea() {
        return NumArea;
    }

    public void setNumArea(String NumArea) {
        this.NumArea = NumArea;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getCircle() {
        return Circle;
    }

    public void setCircle(String Circle) {
        this.Circle = Circle;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPos() {
        return Pos;
    }

    public void setPos(String Pos) {
        this.Pos = Pos;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getProcessTime() {
        return ProcessTime;
    }

    public void setProcessTime(String ProcessTime) {
        this.ProcessTime = ProcessTime;
    }
}
