package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Adapter.ScanAdapter;
import cn.edu.ahnu.wjcy.myapplication.Model.CheckResult;
import cn.edu.ahnu.wjcy.myapplication.Model.OrderInfo;
import cn.edu.ahnu.wjcy.myapplication.Model.ResponseCode;
import cn.edu.ahnu.wjcy.myapplication.Model.ScanResult;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.ScanResultSources;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.BaseEvent;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.ScanDeleteEvent;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.FucUtil;
import cn.edu.ahnu.wjcy.myapplication.util.SettingStore;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.CheckRequestInterface;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScanActivity extends BaseActivity {
    private Context mContext;
    @Bind(R.id.listPay)
    ListView listPay;

    @Bind(R.id.progress_searching)
    ProgressBar progressSearching;

    @Bind(R.id.cancel_btn)
    Button btnCancle;

    @Bind(R.id.btnChoosePay)
    Button btnChoosePay;

    @Bind(R.id.sum_number_tv)
    TextView sumNumberTV;
    @Bind(R.id.sum_number_en_tv)
    TextView sumNumberEnTV;
    @Bind(R.id.sum_price_tv)
    TextView sumPriceTV;
    @Bind(R.id.sum_price_en_tv)
    TextView sumPriceEnTV;

    private SettingStore mSettingStore;

    private StringBuffer stringBuffer;

    //创建适配器
    private ScanAdapter scanAdapter;
    //存放数据
    private ArrayList<ScanResult> arrayList;
    //构建请求体
    private WebRequestInterface webRequestInterface;
    private CheckRequestInterface checkRequestInterface;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private boolean isChecking = false;

    public static final int MSG_VOICE = 1;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_VOICE:
                    playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_SCAN_HELP));
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        EventBus.getDefault().register(this);
        mContext = getApplicationContext();

        ButterKnife.bind(this);
        //注册eventbus事件
        arrayList = new ArrayList<>();
        scanAdapter = new ScanAdapter(arrayList, mContext);
        listPay.setAdapter(scanAdapter);
        stringBuffer = new StringBuffer();
        webRequestInterface = HttpRequestUtil.getRetrofitClien(WebRequestInterface.class.getName(), Constant.SERVICE_BOOK);
        checkRequestInterface = HttpRequestUtil.getRetrofitClien(CheckRequestInterface.class.getName(), Constant.SERVICE_CHECK);
        mSettingStore = new SettingStore(mContext);
        initView();
        //注册广播，接收到广播后关闭界面
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.andly.bro");
        registerReceiver(receiver, intentFilter);

        playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_SCAN_HELP));
        mHandler.sendEmptyMessageDelayed(MSG_VOICE,30000);
    }

    /**
     * 初始化VIew
     */
    private void initView() {
        btnChoosePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent1 = new Intent(ScanActivity.this, ChooseActivity.class);
                intent1.putExtra("sum", 0.01f);
                //保存当前订单
                OrderInfo orderInfo1 = new OrderInfo();
                arrayList.add(new ScanResult("123456","123456789","test",0.01f));
                orderInfo1.setData(arrayList);
                orderInfo1.setPhoneNum(mSettingStore.getUserPhonenum());
                orderInfo1.setMachineCode(mSettingStore.getMachineCode());
                orderInfo1.setStoreNum(mSettingStore.getStoreNum());
                mSettingStore.setOrderInfo(orderInfo1);

                startActivity(intent1);*/


                if(isChecking){
                    //如果正在请求,直接返回
                    return;
                }
                //携带请求参数
                float sumPay = 0f;
                int sumWeight = 0;
                boolean needCheck = true;
                for (int i = 0; i < arrayList.size(); i++) {
                    sumPay += arrayList.get(i).getItemSalePrice();
                    sumWeight += arrayList.get(i).getItemWeight();
                    if(arrayList.get(i).getItemWeight()==0){
                        needCheck = false;
                    }
                }
                sumPay = (float) (Math.round(sumPay * 100)) / 100;
                if (sumPay - 0f < 0.00001F) {
                    Toast.makeText(ScanActivity.this, "当前金额为0元，无需支付", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(needCheck){
                    checkWeight(sumPay,sumWeight);
                }else{
                    checkOk(sumPay);
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Subscribe
    public void onEvent(BaseEvent event){
        if(event instanceof ScanDeleteEvent){
            setSumInfo();
        }
    }

    private void setSumInfo(){
        float sumPay = 0f;
        int num = arrayList.size();
        for (int i = 0; i < num; i++) {
            sumPay += arrayList.get(i).getItemSalePrice();
        }
        sumPay = (float) (Math.round(sumPay * 100)) / 100;

        sumNumberTV.setText("总数："+num);
        sumNumberEnTV.setText("Total："+num);
        sumPriceTV.setText("总价："+sumPay+"元");
        sumPriceEnTV.setText("Total："+sumPay+"RMB");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册广播
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() != 66){
                stringBuffer.append(event.getKeyCode() - 7);
            }else{
                playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_SCAN_VOICE));
                progressSearching.setVisibility(View.VISIBLE);
                getNetDataByIsbn(stringBuffer.toString());
                stringBuffer.setLength(0);
                //扫描成功后30秒无后续操作继续语音提示
                mHandler.removeMessages(MSG_VOICE);
                mHandler.sendEmptyMessageDelayed(MSG_VOICE,30000);
            }
        }
        return true;
    }

    /**
     * 根据ISBN获取图书信息
     *
     * @param isbn
     */
    public void getNetDataByIsbn(String isbn) {
        webRequestInterface.getBookInformationByISBN(isbn)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ScanResultSources>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressSearching.setVisibility(View.GONE);
                        Log.e("", "");
                    }

                    @Override
                    public void onNext(ScanResultSources scanResultSources) {
                        progressSearching.setVisibility(View.GONE);
                        if (ResponseCode.SUCCESS.equals(scanResultSources.getCode())) {
                            ScanResultSources.DataBean.AttributeBean attributes = scanResultSources.getData().getAttribute();
                            arrayList.add(new ScanResult(attributes.getItemId(),attributes.getItemBarcode(),attributes.getItemName(),
                                    attributes.getItemPrice(), attributes.getItemWeight(), attributes.getItemSaleDiscount()
                                    , attributes.getItemSaleDiscountStart(), attributes.getItemSaleDiscountEnd()));

                            setSumInfo();
                            scanAdapter.notifyDataSetChanged();
                        }

                    }
                });

    }

    /**
     * 核检商品重量是否正确
     * 成功后才可以进行付款
     * @param sumPay
     */
    private void checkWeight(final float sumPay, int sumWeight){
        isChecking = true;
        checkRequestInterface.checkWeight(sumWeight)
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
                        isChecking = false;
                        playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_CHECK_ERROR));
                    }

                    @Override
                    public void onNext(CheckResult checkResult) {
                        isChecking = false;
                        if (ResponseCode.SUCCESS.equals(checkResult.getStatus())) {
                            checkOk(sumPay);
                        }else{
                            //核检失败
                            playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_CHECK_FAIL));
                        }

                    }
                });
    }

    /**
     * 核检通过
     * @param sumPay
     */
    private void checkOk(float sumPay){
        //重量核检通过
        Intent intent = new Intent(ScanActivity.this, ChooseActivity.class);
        intent.putExtra("sum", sumPay);

        //保存当前订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setData(arrayList);
        orderInfo.setPhoneNum(mSettingStore.getUserPhonenum());
        orderInfo.setMachineCode(mSettingStore.getMachineCode());
        orderInfo.setStoreNum(mSettingStore.getStoreNum());
        mSettingStore.setOrderInfo(orderInfo);

        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MSG_VOICE);
    }
}
