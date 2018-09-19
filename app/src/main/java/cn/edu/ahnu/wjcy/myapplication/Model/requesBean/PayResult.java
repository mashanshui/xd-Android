package cn.edu.ahnu.wjcy.myapplication.Model.requesBean;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import cn.edu.ahnu.wjcy.myapplication.md5.MD5;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/4 16:31，文件位于cn.edu.ahnu.wjcy.myapplication.Model.requesBean
 * 文件作用：
 */

public class PayResult {

    /**
     * mallCode : 100000
     * posCode : 1000001100000100010001
     * orderId : 20171004160343598481922
     */

    private String mallCode;
    private String posCode;
    private String orderId;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("mallCode",mallCode);
        hashMap.put("posCode",posCode);
        hashMap.put("orderId",orderId);
        hashMap.put("sign", MD5.getMd5Key(hashMap));
        return new JSONObject(hashMap).toString();
    }
}
