AIDL
===

经常有这样的应用场景，Client 端调用 Service 完成一些事情，Service 也可以通过回调通知客户端。本文主要实现一个基本的远程AIDL调用。

常见的与 Service 的通信方式有：

1. 通过 BroadCastReceiver：这种方式是最简单的，只能用来交换简单的数据；
2. 通过 Messager：
这种方式是通过一个传递一个 Messager 给对方，通过这个它来发送 Message 对象。这种方式只能单向传递数据。可以是 Service 到 Activity，也可以是从 Activity 发送数据给 Service。一个 Messeger 不能同时双向发送；
3. 通过 Binder 来实现远程调用(IPC)：这种方式是Android的最大特色之一，让你调用远程Service的接口，就像调用本地对象一样，实现非常灵活，写起来也相对复杂。

本文使用 AIDL 实现 Service 端和 Client 端的双向通信（或者叫"调用"）。

### 定义 AIDL 接口

```java
// IRemoteService.aidl
package com.race604.servicelib;

interface IRemoteService {
    int someOperate(int a, int b);
}
```

这里只定义了一个简单的接口 someOperate()，输入参数 a 和 b，返回一个 int 值。

### Service 实现
```java
// RemoteService.java
package com.race604.remoteservice;

import ...

public class RemoteService extends Service {
    private static final String TAG = RemoteService.class.getSimpleName();
    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
        public int someOperate(int a, int b) throws RemoteException {
            Log.d(TAG, "called RemoteService someOperate()");
            return a + b;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder; // 注意这里返回binder
    }
}
```
RemoteService 里面实现了一个 IRemoteService.Stub 接口的 Binder，且在 onBind() 中返回此 Binder 对象。

### Service 声明
在AndroidManifest.xml对RemoteService的申明如下:

```xml
<service  
  android:name=".RemoteService"
  android:enabled="true"
  android:exported="true" >

  <intent-filter>
      <action android:name="com.race604.servicelib.IRemoteService" />
  </intent-filter>
</service>
```

`android:exported="true"` 表示可以让其他进程绑定
`<action android:name="com.race604.servicelib.IRemoteService" />` 是为了让后面的 Client 通过此 Action 绑定。

### Client 调用

```java
package com.race604.client;

import ...

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private IRemoteService mService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_SHORT).show();
            mService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service disconnected", Toast.LENGTH_SHORT).show();
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bind).setOnClickListener(this);
        findViewById(R.id.unbind).setOnClickListener(this);
        findViewById(R.id.call).setOnClickListener(this);
    }

    private void callRemote() {

        if (mService != null) {
            try {
                int result = mService.someOperate(1, 2);
                Toast.makeText(this, "Remote call return: " + result, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(this, "Remote call error!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Service is not available yet!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind:
                Intent intent = new Intent(IRemoteService.class.getName());
                bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                unbindService(mServiceConnection);
                break;
            case R.id.call:
                callRemote();
                break;
        }
    }
}
```

在客户端，通过 Context.bindService() 绑定到远程的 Service。注意到这里的
Intent intent = new Intent(IRemoteService.class.getName());，
和上面的 Service 申明的 Action 一致。
BIND_AUTO_CREATE：表示如果Bind的时候，如果还没有Service的实例，就自动创建。

注意：Android 5.0 以后，不允许使用非特定的 Intent 来绑定 Service，需要使用如下方法：

```java
Intent intent = new Intent(IRemoteService.class.getName());  
intent.setClassName("com.race604.remoteservice", "com.race604.remoteservice.RemoteService");  
// 或者setPackage()
// intent.setPackage("com.race604.remoteservice");
bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
```

[源码地址](https://github.com/race604/AIDLService-sample/commit/58052056c052eb0f01554c18d2f224638e836450)
