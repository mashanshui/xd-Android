package cn.edu.ahnu.wjcy.myapplication.util.requestNet;

import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CheckStatusResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CloseOrderResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.PrecreateResponceNet;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 创 建 人： cbm
 * 创建时间： 2017/5/27
 * 文件作用：支付服务接口
 */

public interface PayServicesInterface {

    /**
     * 获取支付编码
     * @param route
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("java/rest/payment/trade/precreate")
    Observable<PrecreateResponceNet> GetPrecreate(@Body RequestBody route);

    /**
     * 取消订单
     * @param route
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("java/rest/payment/trade/closeOrder")
    Observable<CloseOrderResult> closeOrder(@Body RequestBody route);


    /**
     * 查询支付信息
     * @param route
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("java/rest/payment/trade/payresult")
    Observable<CheckStatusResult> GetPayResult(@Body RequestBody route);


}

