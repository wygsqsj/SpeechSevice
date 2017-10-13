package com.crfchina.service;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechError;

public class MainActivity extends Activity {

	private TextView tvResult;
	private XrfService mWebView;

	private CustomerServiceUtil.VoiceListener listener = new CustomerServiceUtil.VoiceListener() {
		@Override
		public void onResult(String msg) {
			tvResult.setText(msg);
		}

		@Override
		public void onError(SpeechError error) {
			Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mWebView = (XrfService) findViewById(R.id.main_web);
		
	    mWebView.loadUrl("http://192.168.10.61:8080/crf_intell/dialogWeiXin.jsp?cdsUserName=admin&capacityUserName=zhangwj&robotNo=xinerfu&userSource=cds");

		/*util = CustomerServiceUtil.obtain().build(MainActivity.this);
		btnXunFei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				util.setListener(listener);
			}
		});*/
	}

	
}
