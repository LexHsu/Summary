AIDL
===

接着上文的例子实现有用户加入(join())或者离开(leave())时，能够通知客户端。

### 注册回调

客户端注册一个回调到 Service 中，当 Service 有用户加入或者离开的时候，就调用此回调。
因为普通的 interface 对象不能通过 AIDL 注册到 Service 中，需要定义一个 AIDL 接口：

```java
// IParticipateCallback.aidl
package com.race604.servicelib;

interface IParticipateCallback {
    // 用户加入或者离开的回调
    void onParticipate(String name, boolean joinOrLeave);
}
```

同时，在 IRemoteService.aidl 中添加两个方法如下：

```java
// IRemoteService.aidl
package com.race604.servicelib;

// 注意这里需要import
import com.race604.servicelib.IParticipateCallback;

interface IRemoteService {
    ...
    
    void registerParticipateCallback(IParticipateCallback cb);
    void unregisterParticipateCallback(IParticipateCallback cb);
}
```

这里需要注意的是，需要 import com.race604.servicelib.IParticipateCallback;。

我们可以先设想一下，需要在Service的实现中，用一个 List 来保存注册进来的 IParticipateCallback 实例。
你肯定很快就想到上一篇中说的问题，如果客户端意外退出的话，需要从 List 列表中删掉对应的实例。
否则不仅浪费资源，而且在回调的时候，会出现 DeadObjectException。
当然，我们可以使用上一篇文章中的方法，传入一个 IBinder 对象，使用 Link-To-Death 回调。

幸运的是，这是一个典型的应用场景，Android SDK 提供一个封装好的对象：RemoteCallbackList，
帮我自动处理了 Link-To-Death 的问题，这就帮我们剩下了很多代码了。RemoteService 实现：

```java
package com.race604.remoteservice;

import ...

public class RemoteService extends Service {
    ...
    private RemoteCallbackList<IParticipateCallback> mCallbacks = new RemoteCallbackList<>();

    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        ...
        @Override
        public void registerParticipateCallback(IParticipateCallback cb) throws RemoteException {
            mCallbacks.register(cb);
        }

        @Override
        public void unregisterParticipateCallback(IParticipateCallback cb) throws RemoteException {
            mCallbacks.unregister(cb);
        }
        
        @Override
        public void join(IBinder token, String name) throws RemoteException {
            ...
            // 通知client加入
            notifyParticipate(client.mName, true);
        }

        @Override
        public void leave(IBinder token) throws RemoteException {
        	...
            // 通知client离开
            notifyParticipate(client.mName, false);
        }
    };

}
```

我们在 Service 中用 mCallbacks 来保存回调列表，在注册和反注册 IParticipateCallback 回调的时候，
只要调用 mCallbacks.register(cb) 和 mCallbacks.unregister(cb) 即可。

怎么在用户加入或者退出的时候，怎么通知回调？

上面的代码中，在 join() 和 leave() 方法中分别调用了 notifyParticipate() 函数，我们来看它的实现：

```java
private void notifyParticipate(String name, boolean joinOrLeave) {
    final int len = mCallbacks.beginBroadcast();
    for (int i = 0; i < len; i++) {
        try {
        	  // 通知回调
            mCallbacks.getBroadcastItem(i).onParticipate(name, joinOrLeave);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    mCallbacks.finishBroadcast();
}
```

这里我们也是使用一个循环，获取每个 callback，然后调用 onParticipate()。循环开始前，使用 mCallbacks.beginBroadcast()
来准备开始通知 Callbacks，此函数返回值是 mCallbacks 中回调对象个数。循环结束的时候，调用 mCallbacks.finishBroadcast();
来宣告完成。

另外，在 Service 销毁的时候，需要清除掉 mCallbacks 中的所有的对象，如下：

```java
@Override
public void onDestroy() {
    super.onDestroy();
    // 取消掉所有的回调
    mCallbacks.kill();
}
```

客户端使用 IParticipateCallback 的方法，只要实现 IParticipateCallback.Stub 即可，如下：

```java
package com.race604.client;

import ...
public class MainActivity extends ActionBarActivity {
	...
	private ArrayAdapter<String> mAdapter;
    private IParticipateCallback mParticipateCallback = new IParticipateCallback.Stub() {

        @Override
        public void onParticipate(String name, boolean joinOrLeave) throws RemoteException {
            if (joinOrLeave) {
                mAdapter.add(name);
            } else {
                mAdapter.remove(name);
            }
        }
    };
}
```
