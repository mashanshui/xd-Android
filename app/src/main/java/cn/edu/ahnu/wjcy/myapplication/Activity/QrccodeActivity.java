package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Model.CheckResult;
import cn.edu.ahnu.wjcy.myapplication.Model.ComOrder;
import cn.edu.ahnu.wjcy.myapplication.Model.OrderInfo;
import cn.edu.ahnu.wjcy.myapplication.Model.PreOrder;
import cn.edu.ahnu.wjcy.myapplication.Model.ResponseCode;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.ScanResultSources;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.PreOrderEvent;
import cn.edu.ahnu.wjcy.myapplication.Model.requesBean.CloseOrderParam;
import cn.edu.ahnu.wjcy.myapplication.Model.requesBean.PayResult;
import cn.edu.ahnu.wjcy.myapplication.Model.requesBean.Precreate;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CheckStatusResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CloseOrderResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.PrecreateResponceNet;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.UploadOrderResult;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.ComOrderDbUtil;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.FucUtil;
import cn.edu.ahnu.wjcy.myapplication.util.PreOrderDbUtil;
import cn.edu.ahnu.wjcy.myapplication.util.SettingStore;
import cn.edu.ahnu.wjcy.myapplication.util.ZXingUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.CheckRequestInterface;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.PayServicesInterface;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.RequestWebSite;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.UUIDTools;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QrccodeActivity extends BaseActivity {
    public static final String TAG = "QrccodeActivity";
    private Context mContext;

    @Bind(R.id.imgQrcode)
    ImageView imgQrcode;
    @Bind(R.id.payTipes)
    TextView payTipes;
    @Bind(R.id.payTipes_en)
    TextView payTipesEn;
    @Bind(R.id.timeToFinish)
    TextView timeToFinish;
    @Bind(R.id.image_back)
    ImageView imageBack;
    @Bind(R.id.pay_bg_rl)
    RelativeLayout payBgRL;

    private float sumPrice = 0f;
    private String payStyle;
    private String orderId = null;

    private SettingStore mSettingStore;
    private PreOrderDbUtil mPreOrderDbUtil;
    private ComOrderDbUtil mComOrderDbUtil;

    //构建请求体
    private WebRequestInterface webRequestInterface;
    private PayServicesInterface payServicesInterface;
    private CheckRequestInterface checkRequestInterface;
    private boolean isPlay = false;

    public static final int MSG_VOICE = 2;//提示尽快支付
    public static final int MSG_BACK = 3;//关闭页面
    public static final int MSG_STATE = 4;//查询订单状态
    public static final int MSG_TIME = 5;//倒计时

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME:
                    timeToFinish.setText(Html.fromHtml("剩余时间：<font color='#FF0000'>" + msg.obj + "</font>S"));
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TIME,(int)msg.obj-1),1000);
                    break;
                case MSG_VOICE:
                    playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_QUICK_PAY));
                    break;
                case MSG_BACK:
                    playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_FAIL));
                    sendCloseBroadCast();
                    finish();
                    break;
                case MSG_STATE:
                    checkOrderSate(orderId);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrccode);
        ButterKnife.bind(this);

        mContext = getApplicationContext();

        mSettingStore = new SettingStore(mContext);
        mPreOrderDbUtil = new PreOrderDbUtil();
        mComOrderDbUtil = new ComOrderDbUtil();
        payServicesInterface = HttpRequestUtil.getRetrofitClien(PayServicesInterface.class.getName(), Constant.SERVICE_PAY);
        webRequestInterface = HttpRequestUtil.getRetrofitClien(WebRequestInterface.class.getName(), Constant.SERVICE_BOOK);
        checkRequestInterface = HttpRequestUtil.getRetrofitClien(CheckRequestInterface.class.getName(), Constant.SERVICE_CHECK);
        sumPrice = getIntent().getFloatExtra("sum", 0f);
        sumPrice = (float) (Math.round(sumPrice * 100)) / 100;
        payStyle = getIntent().getStringExtra("style");

        if (sumPrice - 0f < 0.00001F) {
            Toast.makeText(mContext, "获取支付金额失败，请重试", Toast.LENGTH_SHORT).show();

            finish();
        }
        if (payStyle == null) {
            Toast.makeText(mContext, "获取支付方式失败，请重试", Toast.LENGTH_SHORT).show();
            finish();
        }
        payTipes.setText(Html.fromHtml("需要支付<font color='#FF0000'>" + sumPrice + "</font>元"));
        payTipesEn.setText(Html.fromHtml("Need to pay <font color='#FF0000'>" + sumPrice + "</font>RMB"));
        payBgRL.setBackgroundResource("wx".equals(payStyle) ? R.mipmap.wechat_pay_bg : R.mipmap.ali_pay_bg);
        getQrcode(payStyle);

        //消磁
        addMagnetism();

        mHandler.sendMessage(mHandler.obtainMessage(MSG_TIME,600));//开启倒计时
        mHandler.sendEmptyMessageDelayed(MSG_BACK,600000);//600秒后关闭页面
        mHandler.sendEmptyMessageDelayed(MSG_VOICE,20000);//20秒后提示支付
        mHandler.sendEmptyMessageDelayed(MSG_VOICE,40000);//40秒后提示支付

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 获取微信二维码
     */
    private void getQrcode(final String payStyle) {
        Precreate precreate = new Precreate();
        precreate.setOrderId("");
        precreate.setPayTypeCode("wx".equals(payStyle) ? 21 : 22);//,21指的是微信，22指的是支付宝
        precreate.setPosFlow(UUIDTools.getUUID());
        precreate.setPosCode(RequestWebSite.POS_CODE);
        precreate.setShopCode(RequestWebSite.SHOP_CODE);
        precreate.setMallCode(RequestWebSite.MALL_CODE);
        precreate.setTotalAmount(sumPrice + "");
        String jsonData = precreate.toString();


        payServicesInterface.GetPrecreate(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PrecreateResponceNet>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "");
                    }

                    @Override
                    public void onNext(PrecreateResponceNet precreateResponceNet) {
                        if ("000".equals(precreateResponceNet.getStatusCode())) {
                            PrecreateResponceNet.DataBean data = precreateResponceNet.getData();
                            //Glide.with(QrccodeActivity.this).load("http://qr.liantu.com/api.php?text=" + data.getQrCodeUrl()).asBitmap().into(imgQrcode);
                            ZXingUtil.createQRCode(data.getQrCodeUrl(),imgQrcode);
                            MediaPlayer mediaPlayer01;
                            if ("wx".equals(payStyle)){
                                playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_WX));
                            }else{
                                playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_AL));
                            }
                            orderId = precreateResponceNet.getData().getOrderId();
                            /*开始检查订单状态信息*/
                            checkOrderSate(precreateResponceNet.getData().getOrderId());
                        }

                    }
                });
    }

    //检查订单状态，如果是已经支付，则返回主界面
    private void checkOrderSate(String orderId) {
        PayResult payResult = new PayResult();
        payResult.setMallCode(RequestWebSite.MALL_CODE);
        payResult.setPosCode(RequestWebSite.POS_CODE);
        payResult.setOrderId(orderId);
        final String jsonData = payResult.toString();
        getStatus(jsonData);

        mHandler.sendEmptyMessageDelayed(MSG_STATE,5000);
    }

    //获取订单状态
    private void getStatus(String jsonData) {

        payServicesInterface.GetPayResult(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CheckStatusResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "");
                    }

                    @Override
                    public void onNext(CheckStatusResult checkStatusResult) {
                        if ("000".equals(checkStatusResult.getStatusCode())) {
                            CheckStatusResult.DataBean data = checkStatusResult.getData();
                            if (data.getPayStatus() == 1) {
                                if (isPlay) {
                                    return;
                                }
                                isPlay = true;
                                //上传订单
                                postPayOk();
                                //跳转到支付成功页面
                                goPayOk();
                            }
                        }

                    }
                });
    }


    /**
     * 关闭订单
     */
    private void closeOrder(){
        if(TextUtils.isEmpty(orderId) || isPlay){
            return;
        }

        //加入数据库
        mPreOrderDbUtil.insertPreOrder(getPreOrder());

        CloseOrderParam closeOrderParam = new CloseOrderParam();
        closeOrderParam.setOrderId(orderId);
        closeOrderParam.setMallCode(RequestWebSite.MALL_CODE);
        closeOrderParam.setPosCode(RequestWebSite.POS_CODE);
        closeOrderParam.setPayTypeCode("wx".equals(payStyle) ? 21 : 22);

        payServicesInterface.closeOrder(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), closeOrderParam.toMDSignString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CloseOrderResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "");
                    }

                    @Override
                    public void onNext(CloseOrderResult closeOrderResult) {
                        if ("000".equals(closeOrderResult.getStatusCode())) {//请求成功
                            CloseOrderResult.DataBean data = closeOrderResult.getData();
                            if (data.getPayStatus() == 1) {//支付成功
                                Log.d(TAG,"订单已支付");
                                if(!isPlay){//订单未上传
                                    isPlay = true;
                                    //上传订单
                                    postPayOk();
                                }
                                //删除数据库
                                mPreOrderDbUtil.deletePreOrder(getPreOrder());
                                //跳转到支付成功页
                                goPayOk();
                            }else if(data.getPayStatus() == 0){//支付未成功
                                //加入队列继续轮询
                                Log.d(TAG,"加入队列=="+orderId);
                                EventBus.getDefault().post(new PreOrderEvent(getPreOrder()));
                            }else if(data.getPayStatus() == -1){//订单取消
                                Log.d(TAG,"订单已取消");
                                //删除数据库
                                mPreOrderDbUtil.deletePreOrder(getPreOrder());
                            }
                        }else{//请求失败
                            Log.d(TAG,"加入队列=="+orderId);
                            EventBus.getDefault().post(new PreOrderEvent(getPreOrder()));
                        }

                    }
                });
    }

    private PreOrder getPreOrder(){
        PreOrder preOrder = new PreOrder();
        OrderInfo orderInfo = mSettingStore.getOrderInfo();
        preOrder.setOrderId(orderId);
        preOrder.setPayStyle(payStyle);
        preOrder.setSumPrice(sumPrice);
        preOrder.setCreatTime(System.currentTimeMillis());
        preOrder.setPhoneNum(orderInfo.getPhoneNum());
        preOrder.setMachineCode(orderInfo.getMachineCode());
        preOrder.setStoreNum(orderInfo.getStoreNum());
        preOrder.setData(JSON.toJSONString(orderInfo.getData()));

        Log.d(TAG,"preorder==="+preOrder.toString());

        return preOrder;
    }

    private void postPayOk(){
        OrderInfo orderInfo = mSettingStore.getOrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setPayStyle(payStyle);
        orderInfo.setSumPrice(sumPrice);
        webRequestInterface.postPayOk(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), orderInfo.toJson()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<UploadOrderResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "");
                        mComOrderDbUtil.insertComOrder(new ComOrder(getPreOrder(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
                    }

                    @Override
                    public void onNext(UploadOrderResult uploadOrderResult) {
                        //上传失败，存到本地，定时上传
                        if(ResponseCode.FAILED.equals(uploadOrderResult)){
                            mComOrderDbUtil.insertComOrder(new ComOrder(getPreOrder(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
                        }
                    }
                });
    }

    /**
     * 跳转到支付成功页面
     */
    private void goPayOk(){
        sendCloseBroadCast();
        //跳转到支付成功页面
        startActivity(new Intent(mContext,PayOkActivity.class));
        finish();
    }

    /**
     * 开启消磁
     */
    private void addMagnetism(){
        checkRequestInterface.addMagnetism()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CheckResult>() {
                    @Override
                    public void onCompleted() {
                        //Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.e("", "");
                    }

                    @Override
                    public void onNext(CheckResult checkResult) {
                        if (ResponseCode.SUCCESS.equals(checkResult.getStatus())) {
                            //消磁成功
                        }else{
                            //核检失败
                        }

                    }
                });
    }

    /**
     * 关闭消磁
     */
    private void removeMagnetism(){
        checkRequestInterface.removeMagnetism()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CheckResult>() {
                    @Override
                    public void onCompleted() {
                        //Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.e("", "");
                    }

                    @Override
                    public void onNext(CheckResult checkResult) {
                        if (ResponseCode.SUCCESS.equals(checkResult.getStatus())) {
                            //消磁成功
                        }else{
                            //核检失败
                        }

                    }
                });
    }

    /**
     * 发送关闭Activity广播
     */
    private void sendCloseBroadCast(){
        Intent intent = new Intent();
        intent.setAction("com.andly.bro");
        sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHandler.removeMessages(MSG_TIME);
        mHandler.removeMessages(MSG_VOICE);
        mHandler.removeMessages(MSG_STATE);
        mHandler.removeMessages(MSG_BACK);

        closeOrder();

        //如果支付失败，关闭消磁，如果支付成功，跳转到支付成功页再关闭消磁
        if(!isPlay){
            removeMagnetism();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
