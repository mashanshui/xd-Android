package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Model.CheckResult;
import cn.edu.ahnu.wjcy.myapplication.Model.ResponseCode;
import cn.edu.ahnu.wjcy.myapplication.MyApplication;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.FucUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.CheckRequestInterface;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayOkActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.home_btn)
    Button homeBTN;

    @Bind(R.id.state_iv)
    ImageView stateIV;

    private CheckRequestInterface checkRequestInterface;

    public static final int MSG_TIME = 1;//倒计时

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME:
                    close();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ok);
        ButterKnife.bind(this);
        initViews();

        checkRequestInterface = HttpRequestUtil.getRetrofitClien(CheckRequestInterface.class.getName(), Constant.SERVICE_CHECK);

        playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_OK));
        mHandler.sendEmptyMessageDelayed(MSG_TIME,5000);
    }

    private void initViews() {
        homeBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_btn:
                close();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void close(){
        removeMagnetism();
        //发送关闭Activity广播
        Intent intent = new Intent();
        intent.setAction("com.andly.bro");
        sendBroadcast(intent);

        startActivity(new Intent(PayOkActivity.this,MainActivity.class));
        finish();
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
                            //成功
                        }else{
                            //失败
                        }

                    }
                });
    }
}
