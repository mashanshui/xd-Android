package cn.edu.ahnu.wjcy.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.edu.ahnu.wjcy.myapplication.Model.ComOrder;
import cn.edu.ahnu.wjcy.myapplication.Model.OrderInfo;
import cn.edu.ahnu.wjcy.myapplication.Model.OrderQueue;
import cn.edu.ahnu.wjcy.myapplication.Model.PreOrder;
import cn.edu.ahnu.wjcy.myapplication.Model.ResponseCode;
import cn.edu.ahnu.wjcy.myapplication.Model.ScanResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.ScanResultSources;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.BaseEvent;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.PreOrderEvent;
import cn.edu.ahnu.wjcy.myapplication.Model.requesBean.CloseOrderParam;
import cn.edu.ahnu.wjcy.myapplication.Model.requesBean.PayResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CheckStatusResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CloseOrderResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.UploadOrderResult;
import cn.edu.ahnu.wjcy.myapplication.util.ComOrderDbUtil;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.PreOrderDbUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.PayServicesInterface;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.RequestWebSite;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PreOrderService extends Service {
    public static final String TAG = "PreOrderService";
    private PreOrderDbUtil mPreOrderDbUtil;//预支付订单
    private ComOrderDbUtil mComOrderDbUtil;//上传失败的已完成订单
    private OrderQueue mOrderQueue;
    private Subscription mPreOrderMs;
    private Subscription mComOrderMs;

    private SimpleDateFormat simpleDateFormat;

    //构建请求体
    private WebRequestInterface webRequestInterface;
    private PayServicesInterface payServicesInterface;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"PreOrderService start");
        EventBus.getDefault().register(this);

        payServicesInterface = HttpRequestUtil.getRetrofitClien(PayServicesInterface.class.getName(), Constant.SERVICE_PAY);
        webRequestInterface = HttpRequestUtil.getRetrofitClien(WebRequestInterface.class.getName(), Constant.SERVICE_BOOK);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mPreOrderDbUtil = new PreOrderDbUtil();
        mOrderQueue = new OrderQueue();

        mComOrderDbUtil = new ComOrderDbUtil();

        //查询预支付订单
        startPreOrder();
        //查询已完成订单
        startComOrder();
    }

    /**
      *开启预支付订单查询
      */
    private void startPreOrder() {
        List<PreOrder> preOrderList = mPreOrderDbUtil.getPreOrderList();
        for(PreOrder preOrder:preOrderList){
            if(System.currentTimeMillis()<preOrder.getCreatTime()+10*60*1000){//10分钟内订单处理
                mOrderQueue.enQueue(preOrder);
            }
        }

        //开始轮询队列
        mPreOrderMs = Observable.interval(0,5, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                //Log.d(TAG,"do interval");
                //Log.d(TAG,"mOrderQueue size =="+mOrderQueue.QueueLength());
                PreOrder preOrder = mOrderQueue.deQueue();
                if(preOrder!=null){
                    if(System.currentTimeMillis()<preOrder.getCreatTime()+60*1000){//1分钟内订单查询
                        checkOrderSate(preOrder);
                    }else{//超过1分钟订单关闭
                        closeOrder(preOrder);
                    }

                }
            }
        });
    }

    /**
     * 开启已支付订单查询
     */
    private void startComOrder(){
        postComOrder();

        //开启每天定时上传
        long nowTimeLong = Calendar.getInstance().getTimeInMillis();

        //每天开始时间
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,4);
        startTime.set(Calendar.MINUTE,0);
        startTime.set(Calendar.SECOND,0);
        startTime.set(Calendar.MILLISECOND,0);

        long startTimeLong = startTime.getTimeInMillis();

        long delayTime = 0;

        //如果今天已经过了每天开始时间，则第二天再开始
        if(startTimeLong>nowTimeLong){
            delayTime = startTimeLong - nowTimeLong;
        }else{
            delayTime = startTimeLong+24*60*60*1000-nowTimeLong;
        }

        mComOrderMs = Observable.interval(delayTime,24*60*60, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext: postComOrder");
                postComOrder();
            }
        });
    }

    @Subscribe
    public void onEvent(BaseEvent event){
        if(event instanceof PreOrderEvent){
            Log.d(TAG,"add new preorder");
            //先加入数据库保存
            mPreOrderDbUtil.insertPreOrder(((PreOrderEvent) event).getPreOrder());
            //加入队列轮询
            mOrderQueue.enQueue(((PreOrderEvent) event).getPreOrder());
        }
    }


    //检查订单状态，如果是已经支付，上传订单列表
    private void checkOrderSate(final PreOrder preOrder) {
        PayResult payResult = new PayResult();
        payResult.setMallCode(RequestWebSite.MALL_CODE);
        payResult.setPosCode(RequestWebSite.POS_CODE);
        payResult.setOrderId(preOrder.getOrderId());
        final String jsonData = payResult.toString();

        payServicesInterface.GetPayResult(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CheckStatusResult>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: http结束");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: 没有返回");
                    }

                    @Override
                    public void onNext(CheckStatusResult checkStatusResult) {
                        if ("000".equals(checkStatusResult.getStatusCode())) {//查询成功
                            CheckStatusResult.DataBean data = checkStatusResult.getData();
                            if (data.getPayStatus() == 1) {//支付成功
                                Log.d(TAG,"支付成功");
                                //上传订单
                                postPayOk(preOrder);
                                //删除该订单
                                mPreOrderDbUtil.deletePreOrder(preOrder);
                            }else if(data.getPayStatus() == 0){//支付未成功
                                Log.d(TAG,"支付未成功");
                                //1分钟内的订单加入队列继续轮询
                                if(System.currentTimeMillis()<preOrder.getCreatTime()+60*1000){
                                    mOrderQueue.enQueue(preOrder);
                                }else if(System.currentTimeMillis()<preOrder.getCreatTime()+10*60*1000){//大于1分钟小于10分钟取消该订单
                                    closeOrder(preOrder);
                                }
                            }else if(data.getPayStatus() == -1){//订单取消
                                Log.d(TAG,"订单取消");
                                //删除该订单
                                mPreOrderDbUtil.deletePreOrder(preOrder);
                            }
                        }else{//查询失败
                            Log.d(TAG,"查询失败");
                            //1分钟内的订单加入队列继续轮询
                            if(System.currentTimeMillis()<preOrder.getCreatTime()+60*1000){
                                mOrderQueue.enQueue(preOrder);
                            }else if(System.currentTimeMillis()<preOrder.getCreatTime()+10*60*1000){//大于1分钟小于10分钟取消该订单
                                closeOrder(preOrder);
                            }
                        }

                    }
                });
    }

    /**
     * 关闭订单
     */
    private void closeOrder(final PreOrder preOrder){
        CloseOrderParam closeOrderParam = new CloseOrderParam();
        closeOrderParam.setOrderId(preOrder.getOrderId());
        closeOrderParam.setMallCode(RequestWebSite.MALL_CODE);
        closeOrderParam.setPosCode(RequestWebSite.POS_CODE);
        closeOrderParam.setPayTypeCode("wx".equals(preOrder.getPayStyle()) ? 21 : 22);

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
                                //上传订单
                                postPayOk(preOrder);
                                //删除该订单
                                mPreOrderDbUtil.deletePreOrder(preOrder);
                            }else if(data.getPayStatus() == 0){//支付未成功
                                Log.d(TAG,"支付未成功");
                                //10分钟内加入队列继续取消
                                if(System.currentTimeMillis()<preOrder.getCreatTime()+10*60*1000){
                                    mOrderQueue.enQueue(preOrder);
                                }
                            }else if(data.getPayStatus() == -1){//订单取消
                                Log.d(TAG,"订单取消");
                                //删除该订单
                                mPreOrderDbUtil.deletePreOrder(preOrder);
                            }
                        }else{//请求失败
                            Log.d(TAG,"查询失败");
                            //10分钟内加入队列继续取消
                            if(System.currentTimeMillis()<preOrder.getCreatTime()+10*60*1000){
                                mOrderQueue.enQueue(preOrder);
                            }
                        }

                    }
                });
    }

    /**
     * 上传已完成订单
     */
    private void postComOrder(){
        List<ComOrder> comOrderList = mComOrderDbUtil.getComOrderList();
        if(comOrderList!=null&&comOrderList.size()>0){
            for(ComOrder comOrder:comOrderList){
                postPayOk(comOrder);
            }
        }
    }

    /**
     * 上传已完成订单
     * @param preOrder
     */
    private void postPayOk(final PreOrder preOrder){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(preOrder.getOrderId());
        orderInfo.setPayStyle(preOrder.getPayStyle());
        orderInfo.setSumPrice(preOrder.getSumPrice());
        orderInfo.setStoreNum(preOrder.getStoreNum());
        orderInfo.setMachineCode(preOrder.getMachineCode());
        orderInfo.setPhoneNum(preOrder.getPhoneNum());
        orderInfo.setData(JSON.parseArray(preOrder.getData(), ScanResult.class));
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
                    }

                    @Override
                    public void onNext(UploadOrderResult uploadOrderResult) {
                        //上传失败，存到本地，定时上传
                        if(ResponseCode.FAILED.equals(uploadOrderResult)){
                            mComOrderDbUtil.insertComOrder(new ComOrder(preOrder,simpleDateFormat));
                        }
                    }
                });
    }

    /**
     * 上传已完成订单
     * @param comOrder
     */
    private void postPayOk(final ComOrder comOrder){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(comOrder.getOrderId());
        orderInfo.setPayStyle(comOrder.getPayStyle());
        orderInfo.setSumPrice(comOrder.getSumPrice());
        orderInfo.setStoreNum(comOrder.getStoreNum());
        orderInfo.setMachineCode(comOrder.getMachineCode());
        orderInfo.setPhoneNum(comOrder.getPhoneNum());
        orderInfo.setCreatTime(comOrder.getCreatTime());
        orderInfo.setData(JSON.parseArray(comOrder.getData(), ScanResult.class));
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
                    }

                    @Override
                    public void onNext(UploadOrderResult uploadOrderResult) {
                        //上传成功，删除本地
                        if(ResponseCode.SUCCESS.equals(uploadOrderResult)){
                            mComOrderDbUtil.deleteComOrder(comOrder);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //取消轮询
        if(mPreOrderMs!=null){
            mPreOrderMs.unsubscribe();
        }

        if(mComOrderMs!=null){
            mComOrderMs.unsubscribe();
        }

        //更新preorder
        while (!mOrderQueue.QueueEmpty()){
            PreOrder preOrder = mOrderQueue.deQueue();
            mPreOrderDbUtil.insertPreOrder(preOrder);
        }

        //释放队列
        if(mOrderQueue!=null){
            mOrderQueue.clear();
        }

        Log.d(TAG,"PreOrderService destroy");
    }
}
