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

import java.io.File;

/**
 * Created by xwh on 2017/10/10.
 */
public class CustomerServiceUtil {

    private SpeechRecognizer mIat;
    private RecognizerDialog iatDialog;
    private String textResult;
    private RecognizerDialog mIatDialog;
    private VoiceListener listener;

    private static CustomerServiceUtil cusSerUtil;

    private CustomerServiceUtil() {
    }

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
    private RecognizerListener recognizerDialogListener =
            new RecognizerListener() {
                @Override
                public void onVolumeChanged(int i, byte[] bytes) {
//                    Log.i(TAG, "onVolumeChanged,i  = "+i);

                }

                @Override
                public void onBeginOfSpeech() {
//                    Log.i(TAG, "onBeginOfSpeech");
                }

                @Override
                public void onEndOfSpeech() {
//                    Log.i(TAG, "onBeginOfSpeech");
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
//                    Log.i(TAG, "i:" + i + "i1:" + i1 + " i2:" + i2 );
                }
            };

    /*private RecognizerDialogListener recognizerDialogListener =
            new RecognizerDialogListener() {

                @Override
                public void onResult(RecognizerResult recognizerResult, boolean b) {
                    // TODO Auto-generated method stub
                    String result = JsonParser.parseIatResult(recognizerResult.getResultString());
                    listener.onResult(result);
                    Log.i(TAG, "onResult:" + result + "  boolen:" + b);
                }

                @Override
                public void onError(SpeechError speechError) {
                    // TODO Auto-generated method stub
                    listener.onError(speechError);
                    Log.i(TAG, "onError:" + speechError.toString());

                }
            };*/


    public CustomerServiceUtil build(Context context) {

        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mIatDialog = new RecognizerDialog(context, mInitListener);

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
        String path = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "XunFeiTest" + File.separator + "wavaudio.pcm";
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, path);

        return cusSerUtil;
    }

    public void setListener(VoiceListener listener) {
        this.listener = listener;
        mIat.startListening(recognizerDialogListener);
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

    public interface VoiceListener {
        void onResult(String msg);

        void onError(SpeechError error);
    }
}
