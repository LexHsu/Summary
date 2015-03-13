BroadcastReceiver
===

### 创建自定义广播接收器

```java
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        Log.i(TAG, msg);
    }

}

```

### 静态注册
- 静态注册在 AndroidManifest.xml 中配置。

- 静态注册是常驻型的，即使应用关闭后，若接受到指定广播，MyReceiver 便会自动运行。

```xml
<receiver android:name=".MyReceiver">
    <intent-filter>
        <!-- 添加要接收的广播Action-->
        <action android:name="android.intent.action.MY_BROADCAST"/>
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</receiver>
```

### 动态注册

- 在代码中指定广播地址并注册，通常在 Activity 或 Service 中注册。

- 动态注册方式与静态注册相反，不是常驻型的，即跟随程序的生命周期。

```java
MyReceiver receiver = new MyReceiver();
IntentFilter filter = new IntentFilter();
filter.addAction("android.intent.action.MY_BROADCAST");
registerReceiver(receiver, filter);

// registerReceiver 是 android.content.ContextWrapper 类中的方法
// Activity 和 Service 均继承了 ContextWrapper，因此可直接调用
// 当 Activity 或 Service 被销毁时如果没有解除注册，系统会报异常，因此需要销毁前解除注册

@Override
protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
}
```

### 发送广播

- sendBroadcast 也是 android.content.ContextWrapper 类中的方法，其可将指定地址和携带参数的 Intent 对象广播出去。
```java
public void send(View view) {
    Intent intent = new Intent("android.intent.action.MY_BROADCAST");
    intent.putExtra("msg", "hello receiver.");
    sendBroadcast(intent);
}
```

### 普通广播 NormalBroadcast

- 普通广播对于多个接收者来说是完全异步的，每个接收者都无需等待即可以接收到广播，接收者相互之间不会有影响。对于这种广播，接收者无法终止广播，即无法阻止其他接收者的接收动作。

### 有序广播 OrderedBroadcast

- 有序广播先发送到优先级较高的接收者那里，然后由优先级高的接受者处理后，再传播到优先级低的接收者那里，优先级高的接收者有能力截获该广播（abortBroadcast），让其他广播接收器无法收到该广播。

- 优先级通过 android:priority 属性定义，数值越大优先级别越高，取值范围：-1000到1000。

- 为 sendOrderedBroadcast 发送有序广播设置权限，

```xml
    <!-- 在 AndroidMainfest.xml 中定义一个权限 -->
    <permission android:protectionLevel="normal"
        android:name="scott.permission.MY_BROADCAST_PERMISSION" />

    <!-- 然后声明使用了此权限 -->
    <uses-permission android:name="scott.permission.MY_BROADCAST_PERMISSION" />

```

### 粘性广播 StickyBroadcast

- 如果普通广播接收器在指定广播发送后才注册，便无法接收到刚才的广播，而 StickyBroadcast 可接收到最后一条指定 Action 的广播，如系统网络状态的改变发送的广播就是粘性广播。

- 粘性广播需要添加权限`<uses-permission android:name="android.permission.BROADCAST_STICKY"/>`
