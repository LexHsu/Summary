AIDL
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

##### 2. 创建 RemoteWebPage.aidl

在包com.braincol.aidl.service下创建RemoteWebPage.aidl文件：
```java
package com.braincol.aidl.service;

interface RemoteWebPage {
    String getCurrentPageUrl();     
}
```

编译工程会在 gen/ 目录下自动生成 RemoteWebPage.java 文件。
接口内包含了一个名为Stub的抽象的内部类，该类声明了RemoteWebPage.aidl中描述的方法。
Stub 还定义了少量的辅助方法，尤其是 asInterface()，以获取 RemotewebPage 实例引用。

##### 4. 编写RemoteService.java

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
 
    private class MyBinder extends RemoteWebPage.Stub{
        @Override
        public String getCurrentPageUrl() throws RemoteException{
            return "http://www.google.com";
        }
    }
}
```

### 二、客户端实现步骤

##### 客户端流程：

1. 在 src/ 目录下包含服务端 .adil 文件拷贝。
2. 声明一个 IBinder 接口（通过 .aidl 文件生成的）的实例。
3. 实现 ServiceConnection.
4. 调用 Context.bindService()绑定 ServiceConnection 实现类的对象（也就是远程服务端）。
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
                Log.i(TAG, "断开连接...");
                unbindService(connection);
                btn_getAllInfo.setEnabled(false);    
                btn_bind.setText("连接");
                isBinded = false;
                textView.setText("已断开连接!");
            }
        }else if(v==this.btn_getAllInfo){
            textView.setText(allInfo);
        }
 
    }
}
```

客户端通过 bindService() 方法绑定远程服务端，以及之前在manifest 文件中定义的 Service 的 action
通过unbindService()断开连接。连接客户端的相关的代码为：

客户端就是通过actionName（com.braincol.aidl.remote.webpage）来找到服务端。
通过 RemoteWebPage.Stub.asInterface(service) 获取到服务端的 RemoteWebPage 接口对象


