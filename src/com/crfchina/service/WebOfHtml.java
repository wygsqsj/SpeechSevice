package com.crfchina.service;

import com.crfchina.service.CustomerServiceUtil.VoiceListener;
import com.iflytek.cloud.SpeechError;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebOfHtml {
	private Context context;
	private CustomerServiceUtil customerUtil;
	private MediaRecorderUtil mediaUtil;
	private VoiceListener listener;
	
	public WebOfHtml(Context context,VoiceListener listener){
		this.context = context;
		this.listener = listener;
	}
	
	@JavascriptInterface
	public void getSpeechMsg(){
		Log.i("Service","收到请求");
		customerUtil = CustomerServiceUtil.obtain().build(context);
		customerUtil.setListener(listener);
	}
	
	@JavascriptInterface
	public void startRecorder(){
		Log.i("Service","收到请求");
		customerUtil = CustomerServiceUtil.obtain().build(context);
		customerUtil.setListener(listener);
	}
	
	@JavascriptInterface
	public void stopRecorder(){
		Log.i("Service","收到结束请求");
		customerUtil.stopListening();
		
		Log.i("Service","收到录音结束请求");
		MediaRecorderUtil.obtain().stop();
		customerUtil = CustomerServiceUtil.obtain().build(context);
		customerUtil.setListener(listener);
	}
	
	@JavascriptInterface
	public void cancelRecorder(){
		Log.i("Service","收到取消请求");
		customerUtil.stopListening();
	}
	
	
	

}
