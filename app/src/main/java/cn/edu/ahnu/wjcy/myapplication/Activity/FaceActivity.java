package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.zxy.tiny.Tiny;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.BaseEvent;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.FaceEvent;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.SettingStore;
import cn.edu.ahnu.wjcy.myapplication.view.camera.CameraContainer;

/**
 * 相机主页
 */
public class FaceActivity extends BaseActivity implements View.OnClickListener {
    public final static String TAG = "FaceActivity";

    @Bind(R.id.container)
    CameraContainer mContainer;//相机
    @Bind(R.id.cancel_btn)
    Button cancelBTN;//取消按钮

    private SettingStore mSettingStore;

    public static boolean isTest = true;

    private boolean hasFouse = false;//是否在前台

    public static final int MSG_BACK = 1;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BACK:
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_face);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mSettingStore = new SettingStore(this);
        initViews();

        Tiny.getInstance().init(getApplication());
        Tiny.getInstance().debug(true);
        mHandler.sendEmptyMessageDelayed(MSG_BACK,30000);
    }

    private void initViews() {
        cancelBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                finish();
                break;
        }
    }

    @Subscribe
    public void onEvent(BaseEvent event){
        if(event instanceof FaceEvent){
            if(hasFouse){//在前台
                mSettingStore.setUserPhonenum(((FaceEvent) event).getPhonenum());
                startActivity(new Intent(FaceActivity.this, ScanActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTest=false;
        hasFouse = false;

        mHandler.removeMessages(MSG_BACK);
    }
    @Override
    protected void onResume() {
        super.onResume();
        isTest=true;
        hasFouse = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
