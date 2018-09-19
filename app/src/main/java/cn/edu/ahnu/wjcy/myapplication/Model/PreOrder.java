package cn.edu.ahnu.wjcy.myapplication.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Cuibiming on 2017-10-12.
 * 预支付订单
 */

@Entity
public class PreOrder {
    @Id
    private String orderId;
    private String phoneNum;
    private String payStyle;
    private float sumPrice;
    private String machineCode;
    private String storeNum;
    private String data;
    private long creatTime;

    @Generated(hash = 2031844468)
    public PreOrder(String orderId, String phoneNum, String payStyle,
            float sumPrice, String machineCode, String storeNum, String data,
            long creatTime) {
        this.orderId = orderId;
        this.phoneNum = phoneNum;
        this.payStyle = payStyle;
        this.sumPrice = sumPrice;
        this.machineCode = machineCode;
        this.storeNum = storeNum;
        this.data = data;
        this.creatTime = creatTime;
    }

    @Generated(hash = 1658771706)
    public PreOrder() {
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

    public long getCreatTime() {
        return this.creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "PreOrder{" +
                "orderId='" + orderId + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", payStyle='" + payStyle + '\'' +
                ", sumPrice=" + sumPrice +
                ", machineCode='" + machineCode + '\'' +
                ", storeNum='" + storeNum + '\'' +
                ", data='" + data + '\'' +
                ", creatTime=" + creatTime +
                '}';
    }
}
