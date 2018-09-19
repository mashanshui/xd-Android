package cn.edu.ahnu.wjcy.myapplication.Model.requesBean;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import cn.edu.ahnu.wjcy.myapplication.md5.MD5;

/**
 * Created by Cuibiming on 2017-10-09.
 */

public class CloseOrderParam {

    /**
     * sign : XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     * orderId : 20160818164813221850281
     * mallCode : 100000
     * posCode : 1000001100000100010001
     * payTypeCode : 21
     */
    private String orderId;
    private String mallCode;
    private String posCode;
    private int payTypeCode;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMallCode() {
        return mallCode;
    }

    public void setMallCode(String mallCode) {
        this.mallCode = mallCode;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public int getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(int payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public String toMDSignString() {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("orderId",orderId);
        hashMap.put("mallCode",mallCode);
        hashMap.put("posCode",posCode);
        hashMap.put("payTypeCode",payTypeCode);
        hashMap.put("sign", MD5.getMd5Key(hashMap));
        return new JSONObject(hashMap).toString();
    }
}
