package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;
import cn.edu.ahnu.wjcy.myapplication.util.FucUtil;

public class ChooseActivity extends BaseActivity {

    @Bind(R.id.img_wx_pay)
    ImageView imgWxPay;
    @Bind(R.id.img_ali_pay)
    ImageView imgAliPay;
    @Bind(R.id.image_back)
    ImageView imageBack;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        ButterKnife.bind(this);

        final float sumPay = getIntent().getFloatExtra("sum", 0f);

        if (sumPay - 0f < 0.00001F) {
            Toast.makeText(this, "获取支付金额失败，请重试", Toast.LENGTH_SHORT).show();
            finish();
        }
        imgAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, QrccodeActivity.class);
                intent.putExtra("sum", sumPay);
                intent.putExtra("style", "ali");
                startActivity(intent);
            }
        });

        imgWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, QrccodeActivity.class);
                intent.putExtra("sum", sumPay);
                intent.putExtra("style", "wx");
                startActivity(intent);
            }

        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //注册广播，接收到广播后关闭界面
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.andly.bro");
        registerReceiver(receiver, intentFilter);
        playVoice(FucUtil.getVoiceFilePath(Constant.VOICE_CHOOSE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消关联
        unregisterReceiver(receiver);
    }
}
