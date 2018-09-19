package cn.edu.ahnu.wjcy.myapplication.util;

import java.util.List;
import cn.edu.ahnu.wjcy.myapplication.Model.ComOrder;
import cn.edu.ahnu.wjcy.myapplication.greendao.gen.ComOrderDao;

/**
 * Created by Cuibiming on 2017-10-12.
 */

public class ComOrderDbUtil {
    private ComOrderDao mComOrderDao;

    public ComOrderDbUtil() {
        mComOrderDao = GreenDaoHelper.getDaoSession().getComOrderDao();
    }

    /**
     * 获取订单列表
     * @return
     */
    public List<ComOrder> getComOrderList(){
        return  mComOrderDao.queryBuilder().build().list();
    }

    /**
     * 插入一条支付订单
     * @return
     */
    public void insertComOrder(ComOrder comOrder){
        mComOrderDao.insertOrReplace(comOrder);
    }

    /**
     * 删除一条订单
     * @return
     */
    public void deleteComOrder(ComOrder comOrder){
        mComOrderDao.delete(comOrder);
    }
}
