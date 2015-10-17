package com.braincol.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
/**
 * 
 * @author chenzheng_java
 * @description 提供服务的service
 *
 */
public class RemoteService extends Service {
	private final static String TAG = "RemoteService";
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "执行了OnBind");
		return new MyBinder();
	}

	private class MyBinder extends RemoteWebPage.Stub{
		@Override
		public String getCurrentPageUrl() throws RemoteException{
			return "http://www.cnblogs.com/hibraincol/";
		}
	}
}
