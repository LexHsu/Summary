AIDL调用及回调实现
===

### 一、服务端实现步骤

##### 1. 编写 AndroidManifest.xml

```java
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.braincol.aidl.service"
      android:versionCode="1"
      android:versionName="1.0">
    <uses-sdk android:minSdkVersion="8" />
 
    <application android:icon="@drawable/icon" android:label="@string/app_name">
 
    <service android:name="RemoteService">
        <intent-filter>
            <action android:name="com.braincol.aidl.remote.webpage"/>
        </intent-filter>
    </service>
 
    </application>
</manifest>
```

##### 2. 创建 RemoteWebPage.aidl 和 RemoteCallback.aidl

在包 com.braincol.aidl.service 下创建 RemoteWebPage.aidl 文件：
```java
package com.braincol.aidl.service;

interface RemoteWebPage {
    String getCurrentPageUrl();
    boolean registerListener(RemoteCallback callback);
    boolean unregisterListener(RemoteCallback callback);
}
```

创建RemoteCallback.aidl
```java
package com.braincol.aidl.service;

interface RemoteCallback {
    void onResult(String event);     
}
```

编译工程会在 gen/ 目录下自动生成 RemoteWebPage.java 文件和 RemoteCallback.java 文件。
接口内声明了两个 aidl 文件描述的接口。

##### 4. 编写 RemoteService.java

为了实现 AIDL 通信，必须在 RemoteService 类中实现 RemoteWebPage.Stub 接口，
然后实现 RemoteWebPage.Stub 内的相关方法：
```java
public class RemoteService extends Service {
    private final static String TAG = "RemoteService";
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "OnBind");
        return new MyBinder();
    }
 
    private class MyBinder extends RemoteWebPage.Stub {
        private RemoteCallbackList<RemoteCallback> mCallback = new RemoteCallbackList<RemoteCallback>();
        @Override
        public String getCurrentPageUrl() throws RemoteException {
            return "http://www.google.com";
        }
        
        @Override
        public String registerListener() throws RemoteException {
            if (mCallback != null) {
                  mCallback.registerCallback();
            }
        }
        
        @Override
        public String unregisterListener() throws RemoteException {
            if (mCallback != null) {
                  mCallback.unregisterCallback();
            }
        }
    }
}
```

### 二、客户端实现步骤

##### 客户端流程：

1. 在 src 目录下包含和服务端相同的 aidl 文件。
2. 创建 IBinder 接口（工程根据 aidl 文件生成的）实例，如 RemoteWebPage。
3. 实现 ServiceConnection。
4. 调用 Context.bindService()绑定 ServiceConnection 实现类的对象（即远程服务端）。
5. 在 onServiceConnected()方法中会接收到 IBinder 对象，即 InterfaceName.Stub.asInterface(service)。
6. 调用接口中定义的方法，并且应该总是捕获连接被打断时抛出的 RemoteException 异常。
7. 调用 unbindService 方法断开连接。

```java
public class ClientActivity extends Activity implements OnClickListener {
    private MyServiceConnection connection = null;
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
        connection = new MyServiceConnection();
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
    private class MyServiceConnection implements ServiceConnection {
 
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "connecting...");
            remoteWebPage = RemoteWebPage.Stub.asInterface(service);
            if(remoteWebPage==null){
                textView.setText("bind service failed!");    
                return;
            }
            try {
                isBinded=true;
                btn_bind.setText("disconnected");
                textView.setText("connected!");
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
 
    @Override
    public void onClick(View v) {
        if(v==this.btn_bind){
            if(!isBinded){
                Intent intent  = new Intent(actionName);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);                
            }else{
                Log.i(TAG, "disconnecting..");
                unbindService(connection);
                btn_getAllInfo.setEnabled(false);    
                btn_bind.setText("connect");
                isBinded = false;
                textView.setText("disconnected!");
            }
        }else if(v==this.btn_getAllInfo){
            textView.setText(allInfo);
        }
 
    }
}
```
