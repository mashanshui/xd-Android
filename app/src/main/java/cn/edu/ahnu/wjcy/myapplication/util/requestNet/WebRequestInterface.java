package cn.edu.ahnu.wjcy.myapplication.util.requestNet;
import cn.edu.ahnu.wjcy.myapplication.Model.PayUrlResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CheckMerberResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CheckStatusResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.ScanResultSources;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.UploadOrderResult;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 创 建 人： cbm
 * 创建时间： 2017/5/27
 * 文件作用：web请求接口
 */

public interface WebRequestInterface {
    /**
     * 根据ISBNh获取图书信息
     */
    @GET("sale/search")//api/book/getOneByIsbn
    Observable<ScanResultSources> getBookInformationByISBN(@Query("barcode") String isbn);//barcode//isbn

    /**
     * 根据storeNum获取支付url
     */
    @GET("index/getpayurl")
    Observable<PayUrlResult> getPayUrl(@Query("storenum") String storenum);

    /**
     * 获取紧急求助url
     */
    @GET("index/gethelpurl")
    Observable<PayUrlResult> getHelpUrl(@Query("storenum") String storenum);

    /**
     * 支付成功
     */
    @Headers({"Content-Type: application/json"})
    @POST("sale/upload")
    Observable<UploadOrderResult> postPayOk(@Body RequestBody route);

    /**
     * 检查该手机号是否注册
     */
    @GET("merber/existMerber")
    Observable<CheckMerberResult> hasMerber(@Query("phonenum") String phonenum);
}

