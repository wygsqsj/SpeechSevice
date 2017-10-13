package com.crfchina.service;

import com.iflytek.cloud.SpeechError;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class XrfService extends WebView{

	public XrfService(Context context, AttributeSet attrs) {
		this(context, attrs,-1);
	}
	
	public XrfService(Context context) {
		this(context,null,-1);
	}

	public XrfService(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		getSettings().setJavaScriptEnabled(true);
		addJavascriptInterface(new WebOfHtml(context,listener), "jsObj");
	}
	
	private Context context;
	private CustomerServiceUtil.VoiceListener listener = new CustomerServiceUtil.VoiceListener() {
		@Override
		public void onResult(String msg) {
			//回传给服务器
			loadUrl("javascript:yuyin1 ('" + msg + "')");
			Log.i("Service","调用js："+msg);
		}

		@Override
		public void onError(SpeechError error) {
			//错误消息提示
			Toast.makeText(context, "语音获取错误！", Toast.LENGTH_LONG).show();
		}
	};

	

}
