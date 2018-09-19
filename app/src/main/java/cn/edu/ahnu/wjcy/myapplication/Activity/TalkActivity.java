package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.util.ResourceUtil;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Model.aiui.AnswerAIUI;
import cn.edu.ahnu.wjcy.myapplication.Model.aiui.BaseAIUI;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.FucUtil;

public class TalkActivity extends AppCompatActivity{
    public static final String TAG = "TalkActivity";

    @Bind(R.id.image_back)
    ImageView imageBack;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 语义理解对象（语音到语义）。
    private SpeechUnderstander mSpeechUnderstander;

    //是否准备退出
    private boolean isOver = false;

    private Context mContext;

    public static final int MSG_UNDERSTANDER = 1;
    public static final int MSG_CLOSE = 2;
    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==MSG_UNDERSTANDER){
                Log.d(TAG,"MSG_UNDERSTANDER");
                if(mSpeechUnderstander.isUnderstanding()){
                    mSpeechUnderstander.cancel();
                }
                mSpeechUnderstander.startUnderstanding(speechUnderstandListener);
            }else if(msg.what==MSG_CLOSE){
                isOver = true;
                speech(FucUtil.getOverAnswer());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);

        mContext = TalkActivity.this;

        initMsc();
        initViews();
    }

    private void initViews() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isOver = true;
                //speech(FucUtil.getOverAnswer());
                finish();
            }
        });
    }

    /**
     * 初始化语音相关对象
     */
    private void initMsc(){
        Log.d(TAG,"init msc");
        // 初始化语义理解对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(this, speechUnderstanderListener);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHander.removeMessages(MSG_CLOSE);
        mHander.removeMessages(MSG_UNDERSTANDER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHander.sendEmptyMessageDelayed(MSG_CLOSE,120000);
    }

    /**
     * 语音合成初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "TtsInitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "初始化失败,错误码：" + code);
            } else {
                // 设置语音合成参数
                setTtsParam();
                //播放欢迎语句
                speech(FucUtil.getHelloAnswer());
            }
        }
    };

    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener speechUnderstanderListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                setSpeechUndersanderListener();
            }
        }
    };



    private void parseOrder(String result){
        if(!TextUtils.isEmpty(result)){
            // 解析得到语义结果
            JSONObject resultJSON = null;
            try {
                resultJSON = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(resultJSON!=null){
                Log.d( TAG,"resultJSON: "+ resultJSON.toString());
                String service = resultJSON.optString("service");
                BaseAIUI resultAIUI = null;
                switch (service){
                    case BaseAIUI.SERVICE_OPENQA:case BaseAIUI.SERVICE_BAIKE:
                    case BaseAIUI.SERVICE_POETRY:case BaseAIUI.SERVICE_DATETIME:
                    case BaseAIUI.SERVICE_WEATHER:case BaseAIUI.SERVICE_CALC://对话
                        resultAIUI = JSON.parseObject(result, AnswerAIUI.class);
                        if(resultAIUI!=null){
                            speech(((AnswerAIUI) resultAIUI).getAnswer().getText());
                            mHander.removeMessages(MSG_CLOSE);
                            mHander.sendEmptyMessageDelayed(MSG_CLOSE,120000);
                        }
                        return;
                    case BaseAIUI.SERVICE_BEAN_OUT://结束
                        isOver = true;
                        speech(FucUtil.getOverAnswer());
                        return;
                    default:
                        break;
                }
            }
        }

        speech(FucUtil.getUnknowAnswer());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }

        if( null != mSpeechUnderstander ){
            // 退出时释放连接
            mSpeechUnderstander.cancel();
            mSpeechUnderstander.destroy();
        }
    }


    //语音合成
    /**
     * 语音合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "开始播放");
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.d(TAG, "播放完成");
            } else if (error != null) {
                Log.d(TAG, error.getPlainDescription(true));
            }
            if(isOver){//退出命令
                finish();
            }else{//继续监听说话
                mHander.sendEmptyMessage(MSG_UNDERSTANDER);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    /**
     * 语音合成参数设置
     *
     * @return
     */
    private void setTtsParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        /*//设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getTtsResourcePath("nannan"));
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");*/

        //设置使用云端引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME,"nannan");

        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    //获取发音人资源路径
    private String getTtsResourcePath(String name) {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + name + ".jet"));
        return tempBuffer.toString();
    }

    /**
     * 合成音频
     * @param text
     */
    private void speech(String text) {
        // 合成的音频格式：只支持pcm格式
        mTts.stopSpeaking();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            Log.d(TAG, "语音合成失败,错误码: " + code);
        }
    }


    //语音识别
    /**
     * 参数设置
     * @return
     */
    public void setSpeechUndersanderListener(){
        // 设置语言
        mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mSpeechUnderstander.setParameter(SpeechConstant.ACCENT,"mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号，默认：1（有标点）
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/sud.wav");

        //通过此参数，设置开放语义协议版本号。
        mSpeechUnderstander.setParameter(SpeechConstant.NLP_VERSION, "3.0");
        // 设置语义情景
        mSpeechUnderstander.setParameter(SpeechConstant.SCENE, "main");
    }

    /**
     * 识别回调。
     */
    private SpeechUnderstanderListener speechUnderstandListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            // 显示
            parseOrder(result.getResultString());
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG,"结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG,"开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            if( error.getErrorCode() == ErrorCode.MSP_ERROR_NO_DATA ){//没有说话
                Log.d(TAG,error.getPlainDescription(true));
                speech(FucUtil.getNoSpeechAnswer());
            }else{//出错了
                Log.d(TAG,error.getPlainDescription(true)+", "+error.toString());
                speech(FucUtil.getUnknowAnswer());
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
}
