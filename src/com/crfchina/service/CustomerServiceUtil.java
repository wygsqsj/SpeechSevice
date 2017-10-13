package com.crfchina.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 讯飞语音读取本地语音转换成文字.
 * Created by xwh on 2017/10/10.
 */
public class CustomerServiceUtil {

    private SpeechRecognizer mIat;
    private VoiceListener listener;

    private static CustomerServiceUtil cusSerUtil;

    private CustomerServiceUtil() {}

    public static CustomerServiceUtil obtain() {
        if (null == cusSerUtil) {
            synchronized (CustomerServiceUtil.class) {
                if (null == cusSerUtil) {
                    cusSerUtil = new CustomerServiceUtil();
                }
            }
        }
        return cusSerUtil;
    }

    private String TAG = "CustomerServiceUtil";
    private RecognizerListener recognizerListener =
            new RecognizerListener() {
                @Override
                public void onVolumeChanged(int i, byte[] bytes) {
                	Log.d(TAG, "返回音频数据："+bytes.length);
                }

                @Override
                public void onBeginOfSpeech() {
                    Log.i(TAG, "开始说话");
                }

                @Override
                public void onEndOfSpeech() {
                    Log.i(TAG, "结束说话");
                }

                @Override
                public void onResult(RecognizerResult recognizerResult, boolean b) {
                    String result = JsonParser.parseIatResult(recognizerResult.getResultString());
                    listener.onResult(result);
                    Log.i(TAG, "onResult:" + result + "  boolen:" + b);
                }

                @Override
                public void onError(SpeechError speechError) {
                    listener.onError(speechError);
                    Log.i(TAG, "onError:" + speechError.toString());
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {
                	// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        			// 若使用本地能力，会话id为null
        			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
        			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
        			//		Log.d(TAG, "session id =" + sid);
        			//	}
                }
            };

    public CustomerServiceUtil build(Context context) {
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        
        // 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言，中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域，普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点，不说话达到4秒，表示没有说话
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点，说完停顿1秒，表示停止说话
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号，1有标点；0无标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        String path = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "XunFeiTest" + File.separator + "iat.pcm";
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, path);
        return cusSerUtil;
    }

    public void setListener(VoiceListener listener) {
        this.listener = listener;
        int ret = mIat.startListening(recognizerListener);
    }

    /**
     * 初始化监听器
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("Main", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d("Main", "初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 退出时释放资源.
     */
    public void destory() {
        if (mIat != null) {
            mIat.cancel();
            mIat.destroy();
        }
    }
    
    public void stopListening(){
    	 if (mIat != null) {
    			mIat.stopListening();
    	 }
    }
    

    public interface VoiceListener {
        void onResult(String msg);
        void onError(SpeechError error);
    }
}
