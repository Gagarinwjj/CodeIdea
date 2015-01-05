/**
 * Project Name:  TestAndroid
 * File Name:     CrashHandler.java
 * Package Name:  com.wjj.utils
 * @Date:         2015年1月4日
 * Copyright (c)  2015, wulian All Rights Reserved.
 */

package com.wulian.icam.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * @ClassName: CrashHandler
 * @Function: 崩溃处理类
 * @Date: 2015年1月4日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private static CrashHandler crashHandler;
	private Context context;
	private HashMap<String, String> crashInfos;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (crashHandler == null) {
			crashHandler = new CrashHandler();
		}
		return crashHandler;
	}

	public void init(Context context) {
		this.context = context;
		crashInfos = new HashMap<String, String>();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * @Function 自定义处理
	 * @author Wangjj
	 * @date 2015年1月4日
	 * @param ex
	 */
	public boolean myHandleException(Throwable ex) {

		if (ex == null) {
			return false;
		}
		collectDeviceInfos();
		saveCrashInfo2File(ex);
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "Sorry ,app is crashed",
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		return true;
	}

	/**
	 * @Function 手机设备信息
	 * @author Wangjj
	 * @date 2015年1月4日
	 */
	private void collectDeviceInfos() {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			crashInfos.put("versionName", packageInfo.versionName);
			crashInfos.put("versionCode", packageInfo.versionCode + "");

		} catch (NameNotFoundException e) {
			Log.e(TAG, "collectDeviceInfos error:NameNotFoundException");
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				crashInfos.put(field.getName(), field.get(null).toString());
				// Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 
	 * @Function 保存崩溃信息到文件
	 * @author Wangjj
	 * @date 2015年1月4日
	 * @param ex
	 * @return
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		writer.append("\n-------feng-ge-xian-------\n\n");

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			writer.append("\n-------feng-ge-xian-------\n\n");
			cause = cause.getCause();
		}
		printWriter.close();

		// 通过StringBuffer处理信息
		StringBuffer sb = new StringBuffer();
		// 设备信息
		for (Map.Entry<String, String> entry : crashInfos.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		// 异常信息
		// sb.append(printWriter.toString());
		sb.append(writer.toString());
		// 保存到文件
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS",
				Locale.CHINA);
		String dateStr = sdf.format(new Date());
		String fileName = dateStr + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/iCam/Crash/");// crash后面的/加不加都一样,dir都不含"/"
			if (!dir.exists()) {
				dir.mkdirs();
				Log.i(TAG, dir.toString());
			}
			try {
				FileOutputStream fos = new FileOutputStream(dir + "/"
						+ fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
				return fileName;
			} catch (IOException e) {
				Log.e(TAG, "an error occured while writing file...", e);
			}

		}
		return null;

	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!myHandleException(ex)) {
			UncaughtExceptionHandler defaultExceptionHandler = Thread
					.getDefaultUncaughtExceptionHandler();
			defaultExceptionHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000); // 等待,避免闪退
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}

	}

}
