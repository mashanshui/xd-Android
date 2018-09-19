package cn.edu.ahnu.wjcy.myapplication.Model.eventBus;

/**
 * Created by Cuibiming on 2017-10-08.
 */

public class FaceEvent extends BaseEvent {
    private String phonenum;

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public FaceEvent(String phonenum) {
        this.phonenum = phonenum;
    }
}
