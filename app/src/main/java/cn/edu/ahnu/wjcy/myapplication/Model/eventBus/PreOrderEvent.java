package cn.edu.ahnu.wjcy.myapplication.Model.eventBus;


import cn.edu.ahnu.wjcy.myapplication.Model.PreOrder;

/**
 * Created by Cuibiming on 2017-10-08.
 */

public class PreOrderEvent extends BaseEvent {
    private PreOrder preOrder;

    public PreOrder getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(PreOrder preOrder) {
        this.preOrder = preOrder;
    }

    public PreOrderEvent(PreOrder preOrder) {
        this.preOrder = preOrder;
    }
}
