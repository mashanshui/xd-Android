package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.FucUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import cn.edu.ahnu.wjcy.myapplication.view.PasswordWindow;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = "MainActivity";
    private Context mContext;

    @Bind(R.id.main_rl)
    RelativeLayout mainRL;//主页
    @Bind(R.id.cover_rl)
    RelativeLayout coverRL;//广告页
    @Bind(R.id.face_pay_iv)
    ImageView facePayIV;//刷脸支付
    @Bind(R.id.phonenum_pay_iv)
    ImageView phonenumPayIV;//输入手机号支付
    @Bind(R.id.talk_iv)
    ImageView talkIV;//二维码
    @Bind(R.id.setting_iv)
    ImageView settingIV;//设置页
    @Bind(R.id.store_iv)
    ImageView storeIV;//店铺配置页
    @Bind(R.id.help_iv)
    ImageView helpIV;//紧急求助按钮

    //构建请求体
    private WebRequestInterface webRequestInterface;

    //二维码是否已经加载
    private boolean hasQRCode = false;
    //二维码是否正在加载
    private boolean isGetting = false;

    // 语音唤醒对象
    private VoiceWakeuper mIvw;

    //记录点击次数  点击20次后进入设置页面
    private int clickTimes = 0;
    //当前页面是否在前台
    private boolean isFront = false;

    //输入密码弹出框
    private PasswordWindow passwordWindow;

    public static final int MSG_COVER = 1;
    public static final int MSG_VOICE = 2;
    public static final int MSG_HELP = 3;
    public static final int MSG_LISTEN = 4;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_COVER:
                    mainRL.setVisibility(View.GONE);
                    coverRL.setVisibility(View.VISIBLE);
                    //进入广告页后开始播放帮助语音
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_HELP,"1"));
                    break;
                case MSG_VOICE:
                    playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_START_PAY));
                    break;
                case MSG_HELP:
                    int pos = Integer.valueOf((String) msg.obj);
                    playVoice(FucUtil.getHelpFilePath(pos));
                    if(pos==6){//一组播完等30秒后重新播放
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_HELP,"1"),300000);
                    }else{//等3秒后重新播放下一条
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_HELP,""+(pos+1)),8000);
                    }
                    break;
                case MSG_LISTEN:
                    mIvw.startListening(mWakeuperListener);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        webRequestInterface = HttpRequestUtil.getRetrofitClien(WebRequestInterface.class.getName(), Constant.SERVICE_BOOK);
        ButterKnife.bind(this);
        initView();
        initMsc();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {

            }
        });
    }

    private void initMsc() {
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(mContext, null);
        mIvw = VoiceWakeuper.getWakeuper();
        // 设置语音合成参数
        setIvwParam();
    }

    /**
     * 初始化View
     */
    private void initView() {
        coverRL.setOnClickListener(this);
        settingIV.setOnClickListener(this);
        storeIV.setOnClickListener(this);
        facePayIV.setOnClickListener(this);
        phonenumPayIV.setOnClickListener(this);
        talkIV.setOnClickListener(this);
        helpIV.setOnClickListener(this);

        passwordWindow = new PasswordWindow(mContext);
        passwordWindow.setPasswordCallback(new PasswordWindow.PasswordCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(MainActivity.this,StoreSettingActivity.class);
                startActivity(intent);
            }
        });

        //QRCodeIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cover_rl:
                mainRL.setVisibility(View.VISIBLE);
                coverRL.setVisibility(View.GONE);
                //播放语音提示
                playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_START_PAY));

                mHandler.removeMessages(MSG_HELP);
                //隔30秒再播放一次
                mHandler.removeMessages(MSG_VOICE);
                mHandler.sendEmptyMessageDelayed(MSG_VOICE,30000);
                //倒计时60秒重新进入广告页
                mHandler.removeMessages(MSG_COVER);
                mHandler.sendEmptyMessageDelayed(MSG_COVER,60000);
                break;
            case R.id.setting_iv:
                //TODO 启动设置接界面
                clickTimes ++ ;
                if(clickTimes == 10){
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    clickTimes = 0;
                }
                break;
            case R.id.store_iv:
                //点击进入店铺配置
                clickTimes ++ ;
                if(clickTimes == 5){
                    clickTimes = 0;
                    if(passwordWindow!=null&&!passwordWindow.isShowing()){
                        passwordWindow.reset();
                        passwordWindow.showAtLocation(mainRL, Gravity.CENTER,0,0);
                    }
                }
                break;
            case R.id.face_pay_iv:
                startActivity(new Intent(MainActivity.this, FaceActivity.class));
                break;
            case R.id.phonenum_pay_iv:
                startActivity(new Intent(MainActivity.this, PhonenumPayActivity.class));
                break;
            case R.id.talk_iv:
                //startActivity(new Intent(MainActivity.this, TalkActivity.class));
                mIvw.stopListening();
                playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_HELLO_BEAN));
                mHandler.sendEmptyMessageDelayed(MSG_LISTEN,4000);
                break;
            case R.id.help_iv:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;

        mHandler.removeMessages(MSG_VOICE);
        mHandler.removeMessages(MSG_COVER);
        mHandler.removeMessages(MSG_HELP);
        mHandler.removeMessages(MSG_LISTEN);

        //结束唤醒监听
        mIvw.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;

        mainRL.setVisibility(View.VISIBLE);
        coverRL.setVisibility(View.GONE);

        mHandler.removeMessages(MSG_HELP);

        playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_START_PAY));

        //隔30秒再播放一次
        mHandler.removeMessages(MSG_VOICE);
        mHandler.sendEmptyMessageDelayed(MSG_VOICE,30000);
        //倒计时60秒重新进入广告页
        mHandler.removeMessages(MSG_COVER);
        mHandler.sendEmptyMessageDelayed(MSG_COVER,60000);

        clickTimes = 0;

        if(passwordWindow!=null&&passwordWindow.isShowing()){
            passwordWindow.dismiss();
        }

        // 启动唤醒
        int i = mIvw.startListening(mWakeuperListener);
        Log.d(TAG,"启动唤醒=="+i);
    }


    /**
     * 设置语音唤醒参数
     */
    private void setIvwParam(){
        // 清空参数
        mIvw.setParameter(SpeechConstant.PARAMS, null);
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:10");
        // 设置唤醒模式
        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
        // 设置持续进行唤醒
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "0");
        // 设置闭环优化网络模式
        mIvw.setParameter(SpeechConstant.IVW_NET_MODE, "0");
        // 设置唤醒资源路径
        mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getIvwResource());
        // 设置唤醒录音保存路径，保存最近一分钟的音频
        mIvw.setParameter( SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath()+"/msc/ivw.wav" );
        mIvw.setParameter( SpeechConstant.AUDIO_FORMAT, "wav" );
        // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
        //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );
        Log.d(TAG,"setIvwParam");
    }

    private String getIvwResource() {
        final String resPath = ResourceUtil.generateResourcePath(MainActivity.this, ResourceUtil.RESOURCE_TYPE.assets, "ivw/"+getString(R.string.app_id)+".jet");
        Log.d( TAG, "resPath: "+resPath );
        return resPath;
    }


    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onWakeuperResult=="+result.getResultString());

            startActivity(new Intent(MainActivity.this, TalkActivity.class));
        }

        @Override
        public void onError(SpeechError error) {
            Log.d(TAG,error.getPlainDescription(true));
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch( eventType ){
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray( SpeechEvent.KEY_EVENT_RECORD_DATA );
                    Log.i( TAG, "ivw audio length: "+audio.length );
                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
            Log.d(TAG, "onVolumeChanged=="+volume);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
    }
}
