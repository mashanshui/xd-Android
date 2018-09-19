package cn.edu.ahnu.wjcy.myapplication;

import android.app.Application;
import android.content.Intent;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import cn.edu.ahnu.wjcy.myapplication.service.PreOrderService;
import cn.edu.ahnu.wjcy.myapplication.util.GreenDaoHelper;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by cbm on 2017-07-19.
 */

public class MyApplication extends TinkerApplication {

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "cn.edu.ahnu.wjcy.myapplication.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

}
