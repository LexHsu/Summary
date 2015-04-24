Handler
===

### Handler的作用

在 Android 主线程进行耗时操作超过 5 秒，如网络请求或本地读取大文件，会出现 ANR。因此这些操作需放在子线程中处理，
而 Android 更新 UI 只能在主线程中，这时就需要通过 Handler 实现线程间通信。

1. 子线程 Handler 通过主线程的 Looper 间接得到主线程 Handler 引用（或直接调用主线程 Handler 引用），
发送到 MessageQueue 消息队列里。
2. 由于该 Looper 由主线程创建的，因此消息会在主线程处理，Looper 从 MessageQueue 中依次取出 Message。
3. 通过主线程的 Handler 的 dispatchMessage 函数进行消息的处理（可见消息的处理是Handler负责）。
4. 消息处理完毕后，Message 对象被放入 Message Pool 中。


### Message与Runnable

子线程可通过两种方式的 Handler 与主线程通信：

1. message 对象：传递参数，Handler 根据返回的参数做相应处理。
2. Runnable 对象：直接给出具体处理的方法。

这些队列中的内容可以立即执行，延迟一定时间执行或者指定某个时刻执行。

- Message常用方法

```java
// 发送一个空的Message对象，仅带有what值标识
// 如果Message对象被成功加入消息队列返回true，反之返回false
public final boolean sendEmptyMessage (int what)

// 推送一个Message对象至消息队列的末尾，将在相应handler的handleMessage中被接收到。
// 如果Message对象被成功加入消息队列返回true，反之返回false
public final boolean sendMessage (Message msg)

// 其与obtainMessage()相近。是从全局message池中返回一个Message对象，更高效的复用
// 参数返回的message对象可设置what, obj, arg1, arg2 值
public final Message obtainMessage (int what, int arg1, int arg2, Object obj)

// 清除消息队列中所有带有指定token的，等待执行的runnable对象
// r对象并没有销毁，只是将r的引用从消息队列里拿出来。
public final void removeCallbacks (Runnable r, Object token)

// 移除消息队列中，所有等待处理的Runnable对象r，r对象并没有销毁，只是将r的引用从消息队列里拿出来
public final void removeCallbacks (Runnable r)
```

- Runnable常用方法

```java
// 将Runnable对象r加入消息队列。该Runnable对象运行在handler所依附的线程中
// 如果成功加入了消息队列返回true，否则返回false
// 返回false经常是因为looper处理在消息中已经存在
public final boolean post (Runnable r)

// 将Runnable对象r加入消息队列，并在指定的时间运行
public final boolean postAtTime (Runnable r, long uptimeMillis)

// 将Runnable对象r加入消息队列，并在延迟指定时间之后运行
public final boolean postDelayed (Runnable r, long delayMillis)
```

两种方式本质上都是在Handler的队列中放入内容，一般使用Messge方式，因为它可实现更灵活的需求，如执行代码后进入相应回调。而Runnable在某些简单明确的方式中使用。


### Handler 传递 Message 对象

在 Android 中，线程分为有消息循环的线程和没有消息循环的线程，子线程默认没有消息循环。
主线程（UI线程）就是一个消息循环的线程，带有一个 Looper。
Handler 通过 Looper 向消息队列里面添加消息，因此 Handler 只能应用于有消息循环的线程。
每个线程可以有多个 Handler，但只能有一个 Looper，各个 Handler 可直接操作不同的 Looper，可通过如下方式得到Looper的句柄：

```java
// mHandler 对象控制 UI 主线程的 Looper 对象，即间接控制 MessageQueen。
// 将消息发到 UI 线程的 MessageQueue 中，由主线程的 Looper 自行处理 mHandler 的 handleMessage 回调
Handler mHandler = new Handler(Looper.getMainLooper);

// 就是控制当前线程(也可能是 UI 主线程)的 MessageQueen。
Handler mHandler = new Handler(Looper.myLooper);
```

子线程默认没有消息循环，如何创建 Looper？

1. 调用 Looper.prepare() 创建子线程的 Looper。
2. 调用 Looper.myLooper() 获得Looper对象句柄
3. 构造子线程 Handler 时传入该 Looper 进行绑定。

这样就可以在主线程调用 handler.sendMessage 把 message 发到子线程。实现主线程到子线程的通信。
注意，Looper 对象的执行一定要初始化 Looper.prepare 方法，退出时还要通过 Looper.release 释放资源。

Android 子线程中调用 Looper.prepare() 后，系统就会自动的为该线程建立一个消息队列，然后调用 Looper.loop() 之后就进入了消息循环，之后就可以发消息、取消息、和处理消息。

```java
class MyThread extends Thread{
    public void run() {
          Looper.prepare();
          mHandler = new Handler() {
              public void handleMessage(Message msg) {
                  // process incoming messages here
              }
          };
          Looper.loop();
      }
}
```

这个如何发送消息和如何处理消息可以再其他的线程中通过 Handler 来做，但前提是 Handle 知道该子线程的 Looper，
但是如果不是在子线程运行 Looper.myLooper()，一般是无法得到子线程的 looper。

```java
class MyThread extends Thread {
    private EHandler mHandler ;
    public void run() {
        Looper myLooper, mainLooper;
        myLooper = Looper.myLooper ();
        mainLooper = Looper.getMainLooper ();
        String obj;
        if (myLooper == null ) {
            mHandler = new EHandler(mainLooper) {
                public void handleMessage(Message msg) {
                    // process incoming messages here
                }
            };
            obj = “current thread has no looper!” ;
        } else {
            mHandler = new EHandler(myLooper);
            obj = “This is from current thread.” ;
        }
        mHandler.removeMessages(0);
        Message m = mHandler .obtainMessage(1, 1, 1, obj);
        // 同 m.sendToTarget();
        mHandler.sendMessage(m);
    }
  }
```

Handle 须要重载方法 `public void handleMessage(Message msg)`，用于处理接收的消息。
在需要发送消息的地方，先通过 obtainMessage 获取 Message 对象，如 myMessage = mHandler.obtainMessage();
任何一个 handler 发送消息都是到自身的 handleMessage 中，
然后通过 sendMessage 等发送消息，给 Handler 的处理函数 handleMessage(Message msg) 去处理。
handle 可根据 looper 绑定不同的线程，如主线程的 Handler 可绑定到子线程，子线程的 handler 可以绑定到主线程。

### Handler 传递 Runnable 对象

Runnable 对象的 run 方法可以立刻执行某个操作，也可以在指定时间后执行（预约执行）。
Handler 类主要可以使用如下方法来设置执行 Runnable 对象的时间(单位是毫秒)，执行的线程即为 Handler 所在的线程：

```java
public final boolean post(Runnable r);
//  立即执行 Runnable 对象
public final boolean postAtTime(Runnable r, long uptimeMillis);
//  在指定的时间（uptimeMillis）执行 Runnable 对象
public final boolean postDelayed(Runnable r, long delayMillis);
//  在指定的时间间隔（delayMillis）执行 Runnable 对象
```

每次调用 Runnable 对象只能运行一次。一般在 run 方法的最后再次执行 post、postAtTime 或 postDelayed 方法实现循环调用。

上述方法的第 1 个参数的类型都是 Runnable，
因此，在调用这 3 个方法之前，需要有一个实现 Runnable 接口的类，在 Runnable 接口中只有一个 run 方法，该方法为线程执行方法。
如果某个类实现了Runnable接口。可以使用如下代码指定在5秒后调用run方法：

```java
Handler handler = new Handler();
handler.postDelayed(this, 5000);
// 如果想在5秒内停止计时，可以使用如下代码：
handler.removeCallbacks(this);
// 除此之外，还可以使用postAtTime方法指定未来的某一个精确时间来执行Runnable对象，代码如下：
Handler handler = new Handler();
// 15秒后执行指定的Runnable对象
handler.postAtTime(runnableObject, SystemClock.uptimeMillis() + 15 * 1000);

// 注：
SystemClock.uptimeMillis()   // 从开机到现在的毫秒数（手机睡眠的时间不包括在内）
System.currentTimeMillis()   // 从1970年1月1日 UTC到现在的毫秒数，注意该值随手机时间更改后改变
```

### Looper

Looper 用于将一个普通线程变为 Looper 线程。所谓 Looper 线程就是循环工作的线程。在程序开发中（尤其是GUI开发中），
经常会需要一个线程不断循环，一旦有新任务则执行，执行完继续等待下一个任务，这就是 Looper 线程。Android 中使用 Looper 类创建 Looper 线程很简单：

```java
public class LooperThread extends Thread {
    @Override
    public void run() {
        // 将当前线程初始化为Looper线程
        Looper.prepare();

        // 其他处理，如实例化handler

        // 开始循环处理消息队列
        Looper.loop();
    }
}
```

Looper 类源码：

```java
public class Looper {
    // 每个线程中的 Looper 对象其实是一个 ThreadLocal，即线程本地存储(TLS)对象
    private static final ThreadLocal sThreadLocal = new ThreadLocal();
    // Looper 内的消息队列
    final MessageQueue mQueue;
    // 当前线程
    Thread mThread;
    // ...

    // 每个 Looper 对象中有它的消息队列，和它所属的线程
    private Looper() {
        mQueue = new MessageQueue();
        mRun = true;
        mThread = Thread.currentThread();
    }

    // 调用该方法会在调用线程的 TLS 中创建 Looper 对象
    public static final void prepare() {
        if (sThreadLocal.get() != null) {
            // 试图在有 Looper 的线程中再次创建 Looper 将抛出异常
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }
    // ...
}
```

```java
public static final void loop() {
    // 得到当前线程 Looper
    Looper me = myLooper();
    // 得到当前 looper 的 MQ
    MessageQueue queue = me.mQueue;

    Binder.clearCallingIdentity();
    final long ident = Binder.clearCallingIdentity();
    // 开始循环
    while (true) {
        // 取出message
        Message msg = queue.next();
        if (msg != null) {
            if (msg.target == null) {
                // message 没有 target 为结束信号，退出循环
                return;
            }
            // 日志
            if (me.mLogging!= null) me.mLogging.println(
                    ">>>>> Dispatching to " + msg.target + " "
                    + msg.callback + ": " + msg.what
                    );
            // 非常重要！将真正的处理工作交给 message 的 target，即 handler
            msg.target.dispatchMessage(msg);
            // 日志
            if (me.mLogging!= null) me.mLogging.println(
                    "<<<<< Finished to    " + msg.target + " "
                    + msg.callback);

            final long newIdent = Binder.clearCallingIdentity();
            if (ident != newIdent) {
                Log.wtf("Looper", "Thread identity changed from 0x"
                        + Long.toHexString(ident) + " to 0x"
                        + Long.toHexString(newIdent) + " while dispatching to "
                        + msg.target.getClass().getName() + " "
                        + msg.callback + " what=" + msg.what);
            }
            // 回收 message 资源
            msg.recycle();
        }
    }
}
```
