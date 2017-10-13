package com.crfchina.service;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechError;

public class MainActivity extends Activity {

	private TextView tvResult;
	private CustomerServiceUtil util;

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

		tvResult = (TextView) findViewById(R.id.activity_main_tv_xunfeitest);
		Button btnXunFei = (Button) findViewById(R.id.activity_main_btn_xunfeitest);

		util = CustomerServiceUtil.obtain().build(MainActivity.this);

		btnXunFei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				util.setListener(listener);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		util.destory();
	}
}
