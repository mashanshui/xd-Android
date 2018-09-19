package cn.edu.ahnu.wjcy.myapplication.util.requestNet;

import cn.edu.ahnu.wjcy.myapplication.Model.CheckResult;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 创 建 人： cbm
 * 创建时间： 2017/5/27
 * 文件作用：web请求接口
 */

public interface CheckRequestInterface {
    /**
     * 核检重量
     */
    @POST("check_weight")
    Observable<CheckResult> checkWeight(@Query("weight") int weight);

    /**
     * 开启消磁
     */
    @GET("add_magnetism")
    Observable<CheckResult> addMagnetism();

    /**
     * 关闭消磁
     */
    @GET("remove_magnetism")
    Observable<CheckResult> removeMagnetism();
}

