package cn.edu.ahnu.wjcy.myapplication.util;

import java.util.List;

import cn.edu.ahnu.wjcy.myapplication.Model.PreOrder;
import cn.edu.ahnu.wjcy.myapplication.greendao.gen.PreOrderDao;

/**
 * Created by Cuibiming on 2017-10-12.
 */

public class PreOrderDbUtil {
    private PreOrderDao mPreOrderDao;

    public PreOrderDbUtil() {
        mPreOrderDao = GreenDaoHelper.getDaoSession().getPreOrderDao();
    }

    /**
     * 获取预支付订单列表
     * @return
     */
    public List<PreOrder> getPreOrderList(){
        return  mPreOrderDao.queryBuilder().build().list();
    }

    /**
     * 插入一条预支付订单
     * @return
     */
    public void insertPreOrder(PreOrder preOrder){
        mPreOrderDao.insertOrReplace(preOrder);
    }

    /**
     * 删除一条预支付订单
     * @return
     */
    public void deletePreOrder(PreOrder preOrder){
        mPreOrderDao.delete(preOrder);
    }
}
