package com.braincol.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.braincol.aidl.service.RemoteWebPage;

public class ClientActivity extends Activity implements OnClickListener {
	private final static String TAG="ClientActivity";
	TextView textView ;
	Button btn_bind ;
	Button btn_getAllInfo;
	String actionName = "com.braincol.aidl.remote.webpage";
	RemoteWebPage remoteWebPage=null;
	String allInfo = null;
	boolean isBinded=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView);
		btn_bind = (Button) findViewById(R.id.btn_bind);
		btn_getAllInfo = (Button)findViewById(R.id.btn_allinfo);

		btn_getAllInfo.setEnabled(false);

		btn_bind.setOnClickListener(this);
		btn_getAllInfo.setOnClickListener(this);
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.d(TAG,"onPause");
		if(isBinded){
			Log.d(TAG,"unbind");
			unbindService(connection);	
		}
	}
	private class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "��������...");
			remoteWebPage = RemoteWebPage.Stub.asInterface(service);
			if(remoteWebPage==null){
				textView.setText("bind service failed!");	
				return;
			}
			try {
				isBinded=true;
				btn_bind.setText("�Ͽ�");
				textView.setText("������!");
				allInfo = remoteWebPage.getCurrentPageUrl();
				btn_getAllInfo.setEnabled(true);	
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "onServiceDisconnected...");
		}

	}
	MyServiceConnection connection = new MyServiceConnection();

	@Override
	public void onClick(View v) {
		if(v==this.btn_bind){
			if(!isBinded){
				Intent intent  = new Intent(actionName);
				bindService(intent, connection, Context.BIND_AUTO_CREATE);				
			}else{
				Log.i(TAG, "�Ͽ�����...");
				unbindService(connection);
				btn_getAllInfo.setEnabled(false);	
				btn_bind.setText("����");
				isBinded = false;
				textView.setText("�ѶϿ�����!");
			}
		}else if(v==this.btn_getAllInfo){
			textView.setText(allInfo);
		}

	}
}