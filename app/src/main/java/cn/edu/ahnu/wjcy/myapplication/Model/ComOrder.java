package cn.edu.ahnu.wjcy.myapplication.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cuibiming on 2017-11-15.
 * 用来记录已完成但上传失败的订单
 */

@Entity
public class ComOrder {
    @Id
    private String orderId;
    private String phoneNum;
    private String payStyle;
    private float sumPrice;
    private String machineCode;
    private String storeNum;
    private String data;
    private String creatTime;

    @Generated(hash = 1998002220)
    public ComOrder(String orderId, String phoneNum, String payStyle,
            float sumPrice, String machineCode, String storeNum, String data,
            String creatTime) {
        this.orderId = orderId;
        this.phoneNum = phoneNum;
        this.payStyle = payStyle;
        this.sumPrice = sumPrice;
        this.machineCode = machineCode;
        this.storeNum = storeNum;
        this.data = data;
        this.creatTime = creatTime;
    }

    @Generated(hash = 1455839135)
    public ComOrder() {
    }

    public ComOrder(PreOrder preOrder, SimpleDateFormat simpleDateFormat) {
        this.orderId = preOrder.getOrderId();
        this.phoneNum = preOrder.getPhoneNum();
        this.payStyle = preOrder.getPayStyle();
        this.sumPrice = preOrder.getSumPrice();
        this.machineCode = preOrder.getMachineCode();
        this.storeNum = preOrder.getStoreNum();
        this.data = preOrder.getData();
        this.creatTime = simpleDateFormat.format(new Date(preOrder.getCreatTime()));
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }
}
