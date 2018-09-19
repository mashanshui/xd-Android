package cn.edu.ahnu.wjcy.myapplication.util.requestNet;

import retrofit2.Retrofit;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao@xyz.com
 * 创建时间： 2017/5/20
 * 文件作用：
 */

/**
 * 请求总类
 */
public class HttpRequestUtil {
    public static <T> T getRetrofitClien(String interfaceName,String type) {
        RetrofitConfig<Object> retrofitObject = new RetrofitConfig<>();
        Retrofit requestClient = retrofitObject.getRetrofit(RetrofitConfig.getDefaultRequestConfit(),type);
        T t = null;
        try {
            t = (T) requestClient.create(Class.forName(interfaceName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到这个类文件,请检查类名是否正确", e);
        } finally {
            return t;
        }
    }
}





