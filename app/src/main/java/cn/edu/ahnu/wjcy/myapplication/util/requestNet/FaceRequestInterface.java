package cn.edu.ahnu.wjcy.myapplication.util.requestNet;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 创 建 人： cbm
 * 创建时间： 2017/5/27
 * 文件作用：人脸识别接口
 */

public interface FaceRequestInterface {
    @Multipart
    @POST("facepp/v3/search") //URL，可以为空
    Observable<String> search(
            @Part("api_key") String  api_key,
            @Part("api_secret") String api_secret,
            @Part("outer_id") String outer_id,
            @Part("image_file\"; filename=\"image.png\"")  RequestBody  ...image_file
    );
}

