package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.AppUtil;
import cn.edu.ahnu.wjcy.myapplication.util.SettingStore;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class StoreSettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StoreSettingActivity";
    @Bind(R.id.num_et)
    EditText numET;//店铺编号
    @Bind(R.id.num_btn)
    Button numBTN;//提交按钮
    @Bind(R.id.code_et)
    EditText codeET;//机器码
    @Bind(R.id.code_btn)
    Button codeBTN;//提交按钮
    @Bind(R.id.close_btn)
    Button closeBTN;//关闭按钮

    private String newAlias = "";
    private SettingStore mSettingStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);

        ButterKnife.bind(this);

        mSettingStore = new SettingStore(this);
        initViews();
    }

    private void initViews() {
        numET.setText(""+mSettingStore.getStoreNum());
        codeET.setText(""+mSettingStore.getMachineCode());

        numBTN.setOnClickListener(this);
        codeBTN.setOnClickListener(this);
        closeBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.num_btn:
                String newalias = numET.getText().toString().trim();
                if(TextUtils.isEmpty(newalias)){
                    Toast.makeText(this,"店铺编号不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(newalias.equals(mSettingStore.getStoreNum())){
                    Toast.makeText(this,"店铺编号不能和以前相同",Toast.LENGTH_SHORT).show();
                    break;
                }
                newAlias = newalias;
                setAlias(newalias);
                break;
            case R.id.code_btn:
                String code = codeET.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(this,"机器码不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(code.equals(mSettingStore.getMachineCode())){
                    Toast.makeText(this,"机器码不能和以前相同",Toast.LENGTH_SHORT).show();
                    break;
                }
                mSettingStore.setMachineCode(code);
                break;
            case R.id.close_btn:
                this.finish();
                break;
        }
    }

    private void setAlias(String alias) {
        JPushInterface.setAlias(this,alias,mAliasCallback);
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    mSettingStore.setStoreNum(newAlias);
                    AppUtil.showToast("店铺编号设置成功", StoreSettingActivity.this);
                    break;
                case 6002:
                    AppUtil.showToast("店铺编号设置超时，请重试", StoreSettingActivity.this);
                    break;
                default:
                    AppUtil.showToast("店铺编号设置失败，请重试", StoreSettingActivity.this);
            }
        }
    };
}
