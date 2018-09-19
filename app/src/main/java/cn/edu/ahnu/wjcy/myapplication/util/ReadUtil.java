package cn.edu.ahnu.wjcy.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.FaceEvent;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.FaceRequestInterface;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.RequestWebSite;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cbm on 2017-06-24.
 */

public class ReadUtil {
    public static final String TAG = "ReadUtil";
    private static FaceRequestInterface faceRequestInterface;//网络请求工具

    static {
        faceRequestInterface = HttpRequestUtil.getRetrofitClien(FaceRequestInterface.class.getName(), Constant.SERVICE_FACE);
    }

    /**
     * 保存到SD卡
     * @param bt
     */
    public static void saveBitmapToSD(Bitmap bt) {
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        //设置
        options.size = 200f;
        options.isKeepSampling = true;
        Tiny.getInstance().source(bt).asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                if (isSuccess) {
                    Log.d(TAG, "callback: 文件压缩完成 ，开始检索图片信息"+outfile);
                    upLoad(outfile);
                }
            }
        });
    }

    public static void upLoad(String outfile) {
        RequestBody requestImgFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(outfile));
        MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("image_file", outfile, requestImgFile);
        final File file = new File(outfile);
        faceRequestInterface.search(RequestWebSite.FACE_API_KEY,
                                    RequestWebSite.FACE_API_SECRET,
                                    RequestWebSite.FACE_OUTER_ID,
                                    requestImgFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        //如果文件存在，则删除文件
                        if (file.exists()) {
                            file.delete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //如果防止文件出现异常的时候导致无法删除的问题
                        if (file.exists()) {
                            file.delete();
                        }
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(String searchForFacesetBean) {
                        // Log.e(TAG, "onNext: " );
                        JSONObject jsonObj = JSONObject.parseObject(searchForFacesetBean);
                        JSONArray resultsList = jsonObj.getJSONArray("results");
                        //获取脸部宽度和高度，以用来判断距离
                        JSONArray facesList = jsonObj.getJSONArray("faces");

                        //循环遍历，找出最大的人脸信息
                        int widthMaxFace = 0;
                        int heightMaxFace = 0;
                        if (facesList != null && facesList.size() > 0) {
                            for (int i = 0; i < facesList.size(); i++) {
                                int width = facesList.getJSONObject(i).getJSONObject("face_rectangle").getInteger("width");
                                int height = facesList.getJSONObject(i).getJSONObject("face_rectangle").getInteger("height");
                                widthMaxFace = Math.max(width, widthMaxFace);
                                heightMaxFace = Math.max(height, heightMaxFace);
                            }
                        }
                        //如果检测室姐结果并且有数据的话
                        if (resultsList != null && resultsList.size() > 0) {
                            JSONObject jsonObject = resultsList.getJSONObject(0);
                            if (jsonObject != null) {
                                BigDecimal confidence = (BigDecimal) jsonObject.get("confidence");
                                String faceToken = (String) jsonObject.get("face_token");
                                //判断脸部评分是否大于阈值
                                if (confidence.floatValue() > 80 && SizeUtils.faceSizeGravel(widthMaxFace,heightMaxFace)) {
                                    EventBus.getDefault().post(new FaceEvent(jsonObject.getString("user_id")));
                                }
                            }
                        }
                    }
                });


    }


    /**
     * 将预览图生成Bitmap数据
     *
     * @param b
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] b, Camera.Size size) {
        YuvImage yuvimage = new YuvImage(b, ImageFormat.NV21, size.width, size.height, null); //20、20分别是图的宽度与高度
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);//80--JPG图片的质量[0-100],100最高
        byte[] jdata = baos.toByteArray();


        if (jdata.length != 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
            return bitmap;
        } else {
            return null;
        }
    }

}
