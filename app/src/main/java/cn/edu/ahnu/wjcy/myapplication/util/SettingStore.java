package cn.edu.ahnu.wjcy.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

import java.util.Set;

import cn.edu.ahnu.wjcy.myapplication.Model.OrderInfo;
import cn.edu.ahnu.wjcy.myapplication.Model.UserInfo;

/**
 * Created by cbm on 2017-10-08.
 * 店铺设置SP文件工具类
 */

public class SettingStore {
    private static final String FILE_NAME = "store_data";
    private static final String STORE_NUM = "store_num";
    private static final String MACHINE_CODE = "machine_code";
    private static final String USER_PHONENUM = "user_phonenum";
    private static final String ORDER_INFO = "order_info";

    private SharedPreferences sp;

    private Context mContext;
    public SettingStore(Context context) {
        mContext = context;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取店铺编号
     * @return
     */
    public String getStoreNum(){
        return sp.getString(STORE_NUM, "");
    }

    /**
     * 设置店铺编号
     */
    public void setStoreNum(String num){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(STORE_NUM, num);
        editor.commit();
    }

    /**
     * 获取机器码
     * @return
     */
    public String getMachineCode(){
        return sp.getString(MACHINE_CODE, "");
    }

    /**
     * 设置机器码
     */
    public void setMachineCode(String machineCode){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MACHINE_CODE, machineCode);
        editor.commit();
    }

    /**
     * 设置用户手机号
     */
    public void setUserPhonenum(String phonenum){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_PHONENUM, phonenum);
        editor.commit();
    }

    /**
     * 获取用户手机号
     */
    public String getUserPhonenum(){
        return sp.getString(USER_PHONENUM, "");
    }


    /**
     * 获取订单信息
     * @return
     */
    public OrderInfo getOrderInfo(){
        String orderInfo = sp.getString(ORDER_INFO, "");
        return JSON.parseObject(orderInfo,OrderInfo.class);
    }

    /**
     * 设置订单信息
     */
    public void setOrderInfo(OrderInfo orderInfo){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ORDER_INFO, orderInfo.toJson());
        editor.commit();
    }
}
