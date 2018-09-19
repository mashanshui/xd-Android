package cn.edu.ahnu.wjcy.myapplication.Model;

/**
 * Created by Cuibiming on 2017-10-08.
 */

public class UserInfo {
    private String openid;
    private String phonenum;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public UserInfo() {
    }

    public UserInfo(String openid, String phonenum) {
        this.openid = openid;
        this.phonenum = phonenum;
    }
}
