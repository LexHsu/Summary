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
import com.braincol.aidl.service.Beauty;
import com.braincol.aidl.service.RemoteBeauty;

public class ClientActivity extends Activity implements OnClickListener {
	private final static String TAG="ClientActivity";
	TextView textView ;
	Button btn_bind ;
	Button btn_getName;
	Button btn_getAge;
	Button btn_getAllInfo;
	String actionName = "com.braincol.aidl.remote";
	RemoteBeauty remoteBeauty=null;
	Beauty beauty = null;
	String allInfo = null;
	boolean isBinded=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView);
		btn_bind = (Button) findViewById(R.id.btn_bind);
		btn_getName = (Button)findViewById(R.id.btn_getName);
		btn_getAge = (Button)findViewById(R.id.btn_getAge);
		btn_getAllInfo = (Button)findViewById(R.id.btn_allinfo);

		btn_getName.setEnabled(false);
		btn_getAge.setEnabled(false);
		btn_getAllInfo.setEnabled(false);

		btn_bind.setOnClickListener(this);
		btn_getName.setOnClickListener(this);
		btn_getAge.setOnClickListener(this);
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
			remoteBeauty = RemoteBeauty.Stub.asInterface(service);
			if(remoteBeauty==null){
				textView.setText("bind service failed!");	
				return;
			}
			try {
				isBinded=true;
				btn_bind.setText("�Ͽ�");
				textView.setText("������!");
				beauty = remoteBeauty.getBeauty();
				allInfo = remoteBeauty.getAllInfo();
				btn_getName.setEnabled(true);
				btn_getAge.setEnabled(true);
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
				btn_getName.setEnabled(false);
				btn_getAge.setEnabled(false);
				btn_getAllInfo.setEnabled(false);	
				btn_bind.setText("����");
				isBinded = false;
				textView.setText("�ѶϿ�����!");
			}
		}else if(v==this.btn_getName){
			textView.setText("��Ů  ������"+beauty.getName());
		}else if(v==this.btn_getAge){
			textView.setText("��Ů  ���䣺"+beauty.getAge());		
		}else if(v==this.btn_getAllInfo){
			textView.setText(allInfo);
		}

	}
}