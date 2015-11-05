Service
===

### Service 分类

##### 1. 按所在进程

- 本地服务。依附于主线程，主进程被 kill 后，服务随着终止。如后台音乐播放器。

- 远程服务。独立进程，通过 android:process 指定，不受其他进程影响。一般用于系统服务。

##### 2. 按运行层级

- 前台服务。通过 startForeground() 指定。在通知栏显示 Notification，若服务被终止，Notification 也会消失。

- 后台服务。默认即为后台服务，若服务被终止，用户无感知。

##### 3. 按启动方式

- startService 启动的服务。通过 stopService() 停止服务

- bindService 启动的服务。通过 unbindService 解除绑定，或 Activity 被 finish 后，service 会自动终止并解除绑定。

- startService 与 bindService 共同启动的服务。需要 unbindService 与 stopService 同时调用，才能终止 service，顺序无要求。


### startService

##### 1. 自定义 Service

```java
public class MyService extends Service {

    private static final String TAG = "MyService";

    @Override
    public void onCreate() {    // 仅调用一次
        super.onCreate();
        Log.i(TAG, "onCreate called.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart called.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called.");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind called.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called.");
    }
}

```

##### 2. 在 AndroidManifest.xml 中注册 service

```xml
<service android:name=".MyService">
    <intent-filter>
        <action android:name="android.intent.action.MyService" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</service>
```
- 若服务只在本应用中使用，可去掉 `<intent-filter>` 属性。

##### 3. 在有 Context 的组件中启动及销毁，如 Activity。

```java
/**
 * 启动服务
 * @param view
 */
public void start(View view) {
    Intent intent = new Intent(this, MyService.class);
    startService(intent);
}

/**
 * 停止服务
 * @param view
 */
public void stop(View view) {
    Intent intent = new Intent(this, MyService.class);
    stopService(intent);
}
```

### bindService

##### 1. 自定义 Service，
- onBind() 方法需要返回一个实现了 IBinder 接口的实例，该接口描述了与远程对象进行交互的抽象协议。

```java
public class MyService extends Service {

    private static final String TAG = "MyService";

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called.");
        return new MyBinder();
    }

    /**
     * 绑定对象
     * @author user
     *
     */
    public class MyBinder extends Binder {

        /**
         * custom method
         *
         * @param name
         */
        public void greet(String name) {
        	Log.i(TAG, "hello, " + name);
        }
    }
}

```
##### 2. 在 AndroidManifest.xml 中注册 Service，与 startService 相同。

##### 3.与带有 Context 引用的组件绑定

- 在 Activity 中通过自定义 ServiceConnection 与 Service 通信。

```java
private ServiceConnection conn = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Log.i(TAG, "onServiceConnected called.");
    }

    /**
     * 	Called when a connection to the Service has been lost.
     *	This typically happens when the process hosting the service has crashed or been killed.
     *	This does not remove the ServiceConnection itself.
     *	this binding to the service will remain active,
     *	and you will receive a call to onServiceConnected when the Service is next running.
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
};

/**
 * 绑定服务
 * @param view
 */
public void bind(View view) {
    Intent intent = new Intent(this, MyService.class);
    bindService(intent, conn, Context.BIND_AUTO_CREATE);
}

/**
 * 解除绑定
 * @param view
 */
public void unbind(View view) {
    unbindService(conn);
}



public class MainActivity extends Activity {

    /**
     * 绑定对象实例
     */
    private MyService.MyBinder binder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接服务成功时被调用
            binder = (MyService.MyBinder) service;    //获取其实例
            binder.greet("scott");                    //调用其方法
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 异常中断，如服务崩溃或被杀死导致连接中断时被调用，若是自行解除绑定则不会被调用
        }
    };

    /**
     * 绑定服务
     * @param view
     */
    public void bind(View view) {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解除绑定
     * @param view
     */
    public void unbind(View view) {
        unbindService(conn);
    }
}
```

- 服务绑定是异步过程，绑定服务后操作的 binder 对象可能还为 null，注意将操作放到绑定成功之后。

### 其他

- 设备横竖屏切换时 Activity 自动旋转，Android 会重新创建，通过 bindService() 建立的连接便会断开。


### 权限

第一种场景： 谁有权收我的广播？

在这种情况下，可以在自己应用发广播时添加参数声明 Receiver 所需的权限。

首先，在 Androidmanifest.xml 中定义新的权限 RECV_XXX(自定义的权限都需要先声明)，例如：

<permission android:name = "com.android.permission.RECV.XXX"/>

然后，在 Sender app 发送广播时将此权限作为参数传入，如下：

sendBroadcast("com.andoird.XXX_ACTION", "com.android.permission.RECV_XXX");

这样做之后就使得只有具有 permission 权限的 Receiver 才能接收此广播要接收该广播，
在 Receiver 应用的 AndroidManifest.xml 中要添加对应的 RECV_XXX 权限。
例如：
<users-permission android:name = "com.android.permission.RECV.XXX"/>

第二种场景： 谁有权给我发广播？

在这种情况下，需要在 Receiver app 的 <receiver> tag中声明一下Sender app应该具有的权限。

首先同上，在AndroidManifest.xml中定义新的权限SEND_XXX，例如：

<permission android:name = "com.android.permission.SEND.XXX"/>


然后，在Receiver app的Androidmanifest.xml中的<receiver>tag里添加权限SEND_XXX的声明，如下：


<receiver android:name=".XXXReceiver"   
          android:permission="com.android.permission.SEND_XXX">   
    <intent-filter>  
         <action android:name="com.android.XXX_ACTION" />   
    </intent-filter>  
</receiver>

这样一来，该Receiver便只能接收来自具有该send_permission权限的应用发出的广播。
要发送这种广播，需要在Sender app的AndroidManifest.xml中也声明使用该权限即可，如下：

<users-permission android:name = "com.android.permission.SEND.XXX"/>
