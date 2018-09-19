package cn.edu.ahnu.wjcy.myapplication.Model;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by Cuibiming on 2017-10-09.
 */

public class OrderInfo {
    private String orderId;
    private String phoneNum;
    private String payStyle;
    private float sumPrice;
    private String machineCode;
    private String storeNum;
    private String creatTime;
    private List<ScanResult> data;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(String payStyle) {
        this.payStyle = payStyle;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public List<ScanResult> getData() {
        return data;
    }

    public void setData(List<ScanResult> data) {
        this.data = data;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public OrderInfo() {
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
