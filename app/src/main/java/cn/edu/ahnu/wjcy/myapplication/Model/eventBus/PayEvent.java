package cn.edu.ahnu.wjcy.myapplication.Model.eventBus;

import cn.edu.ahnu.wjcy.myapplication.Model.UserInfo;

/**
 * Created by Cuibiming on 2017-10-08.
 */

public class PayEvent extends BaseEvent {
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public PayEvent() {
    }

    public PayEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
