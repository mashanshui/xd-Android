package cn.edu.ahnu.wjcy.myapplication.util;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * 功能性函数扩展类
 */
public class FucUtil {
	/**
	 * 读取asset目录下文件。
	 * @return content
	 */
	public static String readFile(Context mContext, String file, String code)
	{
		int len = 0;
		byte []buf = null;
		String result = "";
		try {
			InputStream in = mContext.getAssets().open(file);
			len  = in.available();
			buf = new byte[len];
			in.read(buf, 0, len);
			
			result = new String(buf,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取asset目录下音频文件。
	 * 
	 * @return 二进制文件数据
	 */
	public static byte[] readAudioFile(Context context, String filename) {
		try {
			InputStream ins = context.getAssets().open(filename);
			byte[] data = new byte[ins.available()];
			
			ins.read(data);
			ins.close();
			
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 获取语音提示目录
	 * @return
	 */
	public static String getVoiceFilePath(String type){
		String voicePath = "";
		switch (type){
			case Constant.VOICE_AL:
				voicePath = "ali.wav";
				break;
			case Constant.VOICE_CHOOSE:
				voicePath = "choose.wav";
				break;
			case Constant.VOICE_FAIL:
				voicePath = "fail.wav";
				break;
			case Constant.VOICE_OK:
				voicePath = "ok.wav";
				break;
			case Constant.VOICE_QUICK_PAY:
				voicePath = "quick_pay.wav";
				break;
			case Constant.VOICE_SCAN_HELP:
				voicePath = "scan_help.wav";
				break;
			case Constant.VOICE_START_PAY:
				voicePath = "start_pay.wav";
				break;
			case Constant.VOICE_WX:
				voicePath = "weixin.wav";
				break;
			case Constant.VOICE_HELLO_BEAN:
				voicePath = "hello_bean.wav";
				break;
			case Constant.VOICE_CHECK_FAIL:
				voicePath = "check_fail.wav";
				break;
			case Constant.VOICE_CHECK_ERROR:
				voicePath = "check_error.wav";
				break;
			case Constant.VOICE_SCAN_VOICE:
				voicePath = "scan_voice.wav";
				break;
		}

		return "voice"+File.separator+voicePath;
	}

	/**
	 * 获取广告页语音帮助目录
	 * @return
	 */
	public static String getHelpFilePath(int id){
		String voicePath = "";
		switch (id){
			case 1:
				voicePath = "help_1.wav";
				break;
			case 2:
				voicePath = "help_2.wav";
				break;
			case 3:
				voicePath = "help_3.wav";
				break;
			case 4:
				voicePath = "help_4.wav";
				break;
			case 5:
				voicePath = "help_5.wav";
				break;
			case 6:
				voicePath = "help_6.wav";
				break;
		}

		return "voice"+File.separator+voicePath;
	}


	public static String[] noSpeechAnswer = {"你好像没有说话哦",
			"大点声","我没有听清"};

	public static String getNoSpeechAnswer(){
		return noSpeechAnswer[new Random().nextInt(3)];
	}


	public static String[] unknowAnswer = {"这个问题我还在学习中哟",
			"我没学过这个问题，你可以告诉我吗",
			"我还小，这个问题我不懂",
			"床前明月光，疑是地上霜。",
			"少年强，则中国强！我会继续努力弥补不足的",
			"我还是祖国的花朵，还需要灌输更多的知识呢",
			"我没听清你在说什么,你能再说一遍么",
			"举头望明月，低头思故乡。",
			"我累了，想休息一会",
			"这个问题我不想回答",
			"你猜我的答案是什么呢",
			"你想知道什么答案呢",
			"锄禾日当午，汗滴禾下土。",
			"让我想一想，待会再告诉你",
			"我想当一名少先队员，你能帮帮我么",
			"不要为难人家么",
			"离离原上草，一岁一枯荣。",
			"你让我说什么好呢",
			"不要嫌我话太多",
			"我在听你们说话学习呢"};

	public static String getUnknowAnswer(){
		return unknowAnswer[new Random().nextInt(20)];
	}

	public static String[] helloAnswer = {"你在叫我么",
			"谁在呼唤我","我在呢，干嘛呀"};

	public static String getHelloAnswer(){
		return helloAnswer[new Random().nextInt(3)];
	}

	public static String[] overAnswer = {"再见"};

	public static String getOverAnswer(){
		return overAnswer[0];
	}
	
}
