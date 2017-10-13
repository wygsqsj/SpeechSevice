package com.crfchina.service;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

/**
 * 录音工具.
 * @author xwh
 *
 */
public class MediaRecorderUtil {
	
	public static MediaRecorderUtil mediaUtil;
	private MediaRecorderUtil(){
		mediaRecorder  = new MediaRecorder();  
		// 第1步：设置音频来源（MIC表示麦克风）  
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
		//第2步：设置音频输出格式（默认的输出格式）  
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);  
		//第3步：设置音频编码方式（默认的编码方式）  
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
	}
	
	public static MediaRecorderUtil obtain(){
		 if (null == mediaUtil) {
	            synchronized (MediaRecorderUtil.class) {
	                if (null == mediaUtil) {
	                	mediaUtil = new MediaRecorderUtil();
	                }
	            }
	        }
	        return mediaUtil;
	}
	
	//创建一个临时的音频输出文件  
	private MediaRecorder mediaRecorder;
	private String path = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "XunFeiTest" + File.separator + "media.wav";
	private String TAG = "SERVICE";
	
	public void start(){
		if(mediaRecorder == null){
			return;
		}
		try {
			//第4步：指定音频输出文件  
			mediaRecorder.setOutputFile(path);  
			//第5步：调用prepare方法  
			mediaRecorder.prepare();  
			//第6步：调用start方法开始录音  
			mediaRecorder.start();  
			Log.i(TAG, "开始录音");
		} catch (IOException e) {
			Log.i(TAG, "录音出现错误"+e.getMessage());
			e.printStackTrace();
		}  
	}
	
	public void stop(){
		if(mediaRecorder == null){
			return;
		}
		mediaRecorder.stop(); 
	}
	
	public void cancel(){
		if(mediaRecorder == null){
			return;
		}
		mediaRecorder.stop(); 
		File media = new File(path);
		if(media.exists()){
			media.delete();
		}
	}
	
	public String getMediaPath(){
		return path;
	}
	
}
