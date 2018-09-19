package cn.edu.ahnu.wjcy.myapplication.Activity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/1 17:46，文件位于cn.edu.ahnu.wjcy.myapplication.Activity
 * 文件作用：
 */

public class BaseActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnPreparedListener{

    MediaPlayer mMediaPlayer;
    AssetManager mAssetManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        //初始化提示语音
        //mMediaPlayer = ((MyApplication)getApplication()).getMediaPlayer(Constant.VOICE_PAY_HELP);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnPreparedListener(this);

        mAssetManager = getAssets();
    }

    //缓冲完成
    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }

    //播放结束
    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    //进度跳转结束
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mMediaPlayer.start();
    }


    public void playVoice(String path){
        try {
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            AssetFileDescriptor afd = mAssetManager.openFd(path);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
        }
    }
}
