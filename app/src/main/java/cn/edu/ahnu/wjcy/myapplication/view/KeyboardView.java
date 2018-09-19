package cn.edu.ahnu.wjcy.myapplication.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.edu.ahnu.wjcy.myapplication.R;

/**
 * Created by Cuibiming on 2017-11-15.
 */

public class KeyboardView extends LinearLayout implements View.OnClickListener {
    private Button input1BTN;
    private Button input2BTN;
    private Button input3BTN;
    private Button input4BTN;
    private Button input5BTN;
    private Button input6BTN;
    private Button input7BTN;
    private Button input8BTN;
    private Button input9BTN;
    private Button input0BTN;
    private Button backSpaceBTN;

    public KeyboardView(Context context) {
        super(context);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     *  初始化子控件
     *  @param context
     */
    private void initView(Context context) {
        inflate(context, R.layout.keyboard_layout, this);
        input1BTN= (Button) findViewById(R.id.input_1_btn);
        input2BTN= (Button) findViewById(R.id.input_2_btn);
        input3BTN= (Button) findViewById(R.id.input_3_btn);
        input4BTN= (Button) findViewById(R.id.input_4_btn);
        input5BTN= (Button) findViewById(R.id.input_5_btn);
        input6BTN= (Button) findViewById(R.id.input_6_btn);
        input7BTN= (Button) findViewById(R.id.input_7_btn);
        input8BTN= (Button) findViewById(R.id.input_8_btn);
        input9BTN= (Button) findViewById(R.id.input_9_btn);
        input0BTN= (Button) findViewById(R.id.input_0_btn);
        backSpaceBTN= (Button) findViewById(R.id.backspace_btn);

        input1BTN.setOnClickListener(this);
        input2BTN.setOnClickListener(this);
        input3BTN.setOnClickListener(this);
        input4BTN.setOnClickListener(this);
        input5BTN.setOnClickListener(this);
        input6BTN.setOnClickListener(this);
        input7BTN.setOnClickListener(this);
        input8BTN.setOnClickListener(this);
        input9BTN.setOnClickListener(this);
        input0BTN.setOnClickListener(this);
        backSpaceBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.input_1_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("1");
                }
                break;
            case R.id.input_2_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("2");
                }
                break;
            case R.id.input_3_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("3");
                }
                break;
            case R.id.input_4_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("4");
                }
                break;
            case R.id.input_5_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("5");
                }
                break;
            case R.id.input_6_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("6");
                }
                break;
            case R.id.input_7_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("7");
                }
                break;
            case R.id.input_8_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("8");
                }
                break;
            case R.id.input_9_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("9");
                }
                break;
            case R.id.input_0_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onInput("0");
                }
                break;
            case R.id.backspace_btn:
                if(keyboardCallback!=null){
                    keyboardCallback.onBackspace();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 键盘输出回调接口
     */
    public interface KeyboardCallback{
        /**
         * 按键输入
         * @param input
         */
        void onInput(String input);
        /**
         * 退格键输入
         */
        void onBackspace();
    }

    private KeyboardCallback keyboardCallback;

    public void setKeyboardCallback(KeyboardCallback keyboardCallback) {
        this.keyboardCallback = keyboardCallback;
    }
}
