package cn.edu.ahnu.wjcy.myapplication.Model.aiui;

/**
 * Created by Cuibiming on 2017-08-21.
 * AIUI返回结果基类
 */

public class BaseAIUI {
    public static final String SERVICE_BEAN_OUT = "BEAN.OUT";
    public static final String SERVICE_OPENQA = "openQA";
    public static final String SERVICE_CALC = "calc";
    public static final String SERVICE_BAIKE = "baike";
    public static final String SERVICE_WEATHER = "weather";
    public static final String SERVICE_POETRY = "poetry";
    public static final String SERVICE_DATETIME = "datetime";
    private String sid;
    private String uuid;
    private String text;
    private String service;
    private String rc;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }
}
