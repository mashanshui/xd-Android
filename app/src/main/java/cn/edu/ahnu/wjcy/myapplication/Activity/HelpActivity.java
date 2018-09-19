package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Model.PayUrlResult;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.SettingStore;
import cn.edu.ahnu.wjcy.myapplication.util.ZXingUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.HttpRequestUtil;
import cn.edu.ahnu.wjcy.myapplication.util.requestNet.WebRequestInterface;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HelpActivity extends BaseActivity {
    public static final String TAG = "HelpActivity";
    private Context mContext;

    @Bind(R.id.qrcode_iv)
    ImageView qrCodeIV;//二维码
    @Bind(R.id.image_back)
    ImageView imageBack;

    //构建请求体
    private WebRequestInterface webRequestInterface;

    private SettingStore mSettingStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mContext = getApplicationContext();
        webRequestInterface = HttpRequestUtil.getRetrofitClien(WebRequestInterface.class.getName(), Constant.SERVICE_BOOK);
        ButterKnife.bind(this);
        mSettingStore = new SettingStore(mContext);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //生成二维码
        getHelpUrl();
    }

    /**
     * 获取帮助url
     */
    public void getHelpUrl() {
        webRequestInterface.getHelpUrl(mSettingStore.getStoreNum())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PayUrlResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(PayUrlResult payUrlResult) {
                        if(payUrlResult!=null&&!TextUtils.isEmpty(payUrlResult.getPayUrl())){
                            ZXingUtil.createQRCode(payUrlResult.getPayUrl(),qrCodeIV);
                        }
                    }
                });
    }
}
