package cn.edu.ahnu.wjcy.myapplication.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.Constant;

/**
 * Created by LH on 2017/5/15.
 */

public class PasswordWindow extends PopupWindow implements View.OnClickListener{
    private View view;
    private Context context;

    //密码框
    private TextView passwordTV1;
    private TextView passwordTV2;
    private TextView passwordTV3;
    private TextView passwordTV4;
    private TextView passwordTV5;
    private TextView passwordTV6;
    //输入框
    private TextView inputTV0;
    private TextView inputTV1;
    private TextView inputTV2;
    private TextView inputTV3;
    private TextView inputTV4;
    private TextView inputTV5;
    private TextView inputTV6;
    private TextView inputTV7;
    private TextView inputTV8;
    private TextView inputTV9;

    private Button sureBTN;
    private Button cancelBTN;

    private int pos = 1;
    private String password = "";

    public PasswordWindow(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.password_pop, null);
        this.context = context;
        initViews();
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(context.getResources().getDimensionPixelSize(R.dimen.pop_width));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(context.getResources().getDimensionPixelSize(R.dimen.pop_width));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //添加动画
        this.setAnimationStyle(R.style.popup_anim);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                reset();
            }
        });
    }

    /**
     * 初始View、适配器以及数据
     */
    private void initViews() {
        passwordTV1 = (TextView) view.findViewById(R.id.password_tv_1);
        passwordTV2 = (TextView) view.findViewById(R.id.password_tv_2);
        passwordTV3 = (TextView) view.findViewById(R.id.password_tv_3);
        passwordTV4 = (TextView) view.findViewById(R.id.password_tv_4);
        passwordTV5 = (TextView) view.findViewById(R.id.password_tv_5);
        passwordTV6 = (TextView) view.findViewById(R.id.password_tv_6);

        inputTV0 = (TextView) view.findViewById(R.id.input_tv_0);
        inputTV1 = (TextView) view.findViewById(R.id.input_tv_1);
        inputTV2 = (TextView) view.findViewById(R.id.input_tv_2);
        inputTV3 = (TextView) view.findViewById(R.id.input_tv_3);
        inputTV4 = (TextView) view.findViewById(R.id.input_tv_4);
        inputTV5 = (TextView) view.findViewById(R.id.input_tv_5);
        inputTV6 = (TextView) view.findViewById(R.id.input_tv_6);
        inputTV7 = (TextView) view.findViewById(R.id.input_tv_7);
        inputTV8 = (TextView) view.findViewById(R.id.input_tv_8);
        inputTV9 = (TextView) view.findViewById(R.id.input_tv_9);

        sureBTN = (Button) view.findViewById(R.id.sure_btn);
        cancelBTN = (Button) view.findViewById(R.id.cancel_btn);

        inputTV0.setOnClickListener(this);
        inputTV1.setOnClickListener(this);
        inputTV2.setOnClickListener(this);
        inputTV3.setOnClickListener(this);
        inputTV4.setOnClickListener(this);
        inputTV5.setOnClickListener(this);
        inputTV6.setOnClickListener(this);
        inputTV7.setOnClickListener(this);
        inputTV8.setOnClickListener(this);
        inputTV9.setOnClickListener(this);

        sureBTN.setOnClickListener(this);
        cancelBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_tv_0:
                inputPassword(0);
                break;
            case R.id.input_tv_1:
                inputPassword(1);
                break;
            case R.id.input_tv_2:
                inputPassword(2);
                break;
            case R.id.input_tv_3:
                inputPassword(3);
                break;
            case R.id.input_tv_4:
                inputPassword(4);
                break;
            case R.id.input_tv_5:
                inputPassword(5);
                break;
            case R.id.input_tv_6:
                inputPassword(6);
                break;
            case R.id.input_tv_7:
                inputPassword(7);
                break;
            case R.id.input_tv_8:
                inputPassword(8);
                break;
            case R.id.input_tv_9:
                inputPassword(9);
                break;
            case R.id.sure_btn:
                if(Constant.ADMIN_PASSWORD.equals(password)){
                    //密码正确
                    if(passwordCallback!=null){
                        passwordCallback.onSuccess();
                    }
                }else{
                    //密码错误
                    reset();
                    Toast.makeText(context,"输入的密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_btn:
                if(this.isShowing()){
                    this.dismiss();
                }
                break;
        }
    }

    /**
     * 根据点击输入密码
     * @param pwd
     */
    private void inputPassword(int pwd) {
        password += pwd;
        switch (pos){
            case 1:
                passwordTV1.setText("*");
                break;
            case 2:
                passwordTV2.setText("*");
                break;
            case 3:
                passwordTV3.setText("*");
                break;
            case 4:
                passwordTV4.setText("*");
                break;
            case 5:
                passwordTV5.setText("*");
                break;
            case 6:
                passwordTV6.setText("*");
                break;
        }

        pos++;
    }

    /**
     * 初始化数据
     */
    public void reset(){
        password = "";
        pos = 1;
        passwordTV1.setText("");
        passwordTV2.setText("");
        passwordTV3.setText("");
        passwordTV4.setText("");
        passwordTV5.setText("");
        passwordTV6.setText("");
    }

    public interface PasswordCallback{
        void onSuccess();
    }

    private PasswordCallback passwordCallback;

    public void setPasswordCallback(PasswordCallback passwordCallback) {
        this.passwordCallback = passwordCallback;
    }
}
