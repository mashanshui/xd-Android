package cn.edu.ahnu.wjcy.myapplication.Model.requesBean;


import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import cn.edu.ahnu.wjcy.myapplication.md5.MD5;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/4 16:24，文件位于cn.edu.ahnu.wjcy.myapplication.Model.requesBean
 * 文件作用：
 */

public class Precreate {

    /**
     * sign : XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     * mallCode : 100000
     * shopCode : 100000110000010001
     * posCode : 1000001100000100010001
     * orderId :
     * totalAmount : 0.01
     * payTypeCode : 21
     * posFlow : 999999
     */

    private String mallCode;
    private String shopCode;
    private String posCode;
    private String orderId;
    private String totalAmount;
    private int payTypeCode;
    private String posFlow;

    public String getMallCode() {
        return mallCode;
    }

    public void setMallCode(String mallCode) {
        this.mallCode = mallCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(int payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public String getPosFlow() {
        return posFlow;
    }

    public void setPosFlow(String posFlow) {
        this.posFlow = posFlow;
    }

    @Override
    public String toString() {


        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("mallCode",mallCode);
        hashMap.put("shopCode",shopCode);
        hashMap.put("posCode",posCode);
        hashMap.put("orderId",orderId);
        hashMap.put("totalAmount",totalAmount);
        hashMap.put("payTypeCode",payTypeCode);
        hashMap.put("posFlow",posFlow);
        hashMap.put("sign", MD5.getMd5Key(hashMap));
        return new JSONObject(hashMap).toString();

    }
}
