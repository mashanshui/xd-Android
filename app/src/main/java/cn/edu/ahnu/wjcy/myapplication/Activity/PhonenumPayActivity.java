package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Model.ResponseCode;
import cn.edu.ahnu.wjcy.myapplication.Model.responBean.CheckMerberResult;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.SettingStore;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import cn.edu.ahnu.wjcy.myapplication.view.KeyboardView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PhonenumPayActivity extends BaseActivity implements View.OnClickListener, KeyboardView.KeyboardCallback {
    public static final String TAG = "PhonenumPayActivity";

    @Bind(R.id.phonenum_tv)
    TextView phonenumTV;//输入的手机号
    @Bind(R.id.start_pay_iv)
    ImageView startPayIV;//开始支付
    @Bind(R.id.keyboard_kv)
    KeyboardView keyboardKV;//键盘
    @Bind(R.id.cancel_btn)
    Button cancelBTN;//取消按钮

    private String phonenum = "";

    private boolean isChecking = false;
    private SettingStore mSettingStore;
    private boolean hasFouse = false;//是否在前台

    //构建请求体
    private WebRequestInterface webRequestInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_phonenum_pay);

        webRequestInterface = HttpRequestUtil.getRetrofitClien(WebRequestInterface.class.getName(), Constant.SERVICE_BOOK);
        mSettingStore = new SettingStore(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        startPayIV.setOnClickListener(this);
        cancelBTN.setOnClickListener(this);

        keyboardKV.setKeyboardCallback(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_pay_iv:
                checkPhonenum();
                break;
            case R.id.cancel_btn:
                finish();
                break;
            default:
                break;
        }
    }

    private void checkPhonenum(){
        if(isChecking){
            return;
        }

        //输入的phoneNum合法
        if(!TextUtils.isEmpty(phonenum)&&phonenum.length()==11&&phonenum.startsWith("1")){
            isChecking = true;
            webRequestInterface.hasMerber(phonenum)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<CheckMerberResult>() {
                        @Override
                        public void onCompleted() {
                            isChecking = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            isChecking = false;
                        }

                        @Override
                        public void onNext(CheckMerberResult checkMerberResult) {
                            isChecking = false;
                            //用户存在，跳转到结账页面
                            if(checkMerberResult!=null&&ResponseCode.SUCCESS.equals(checkMerberResult.getCode())){
                                if(hasFouse){//在前台
                                    mSettingStore.setUserPhonenum(checkMerberResult.getData().getMemberInformation().getPhonenum());
                                    startActivity(new Intent(PhonenumPayActivity.this, ScanActivity.class));
                                    finish();
                                }
                            }else{
                                if(hasFouse) {//在前台
                                    Toast.makeText(PhonenumPayActivity.this,"该手机号尚未注册",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }else{
            Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 键盘输入回调
     * @param input
     */
    @Override
    public void onInput(String input) {
        if(phonenum.length()<11){
            phonenum += input;
            phonenumTV.setText(phonenum);
        }

        if(phonenum.length()==11){
            startPayIV.setClickable(true);
            startPayIV.setImageResource(R.mipmap.btn_start_pay_normal);
        }
    }

    /**
     * 键盘退格回调
     */
    @Override
    public void onBackspace() {
        if(!TextUtils.isEmpty(phonenum)){
            phonenum = phonenum.substring(0,phonenum.length()-1);
            phonenumTV.setText(phonenum);
        }

        if(phonenum.length()<11){
            startPayIV.setClickable(false);
            startPayIV.setImageResource(R.mipmap.btn_start_pay_unclick);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasFouse = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hasFouse = false;
    }
}
