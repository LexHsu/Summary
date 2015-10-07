AIDL
===

本文实现一个稍微复杂一点的场景：

Service 实现一个控制中心（如一个多人游戏），客户端可随时加入或退出，每个客户端都可以获取当前参与进来的成员列表。

根据需求，在上一篇文章的代码的基础上，可以很容易申明如下接口：

```java
// IRemoteService.aidl
package com.race604.servicelib;
interface IRemoteService {  
    ...

    void join(String userName);
    void leave(String userName);
    List<String> getParticipators();
}
```

Service 的实现：
```java
// RemoteService.java

package com.race604.remoteservice;
import ...
public class RemoteService extends Service {  
private List<String> mClients = new ArrayList<>();

private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
    @Override
    public void join(String name) throws RemoteException {
        mClients.add(name);
    }
    
    @Override
    public void leave(String name) throws RemoteException {
        mClients.remove(name);
    }
    
    @Override
    public List<String> getParticipators() throws RemoteException {
        return mClients;
    }
};
```
需要考虑，客户端意外退出（如 crash）没有机会调用 leave()。这样 Service 中的 mClients 中的还保持这个客户端的信息，得不到释放，会造成资源浪费。
幸运的是，Binder 有可以让对端的进程得到意外退出通知的机制：Link-To-Death。我这里以我们这里Service被通知Client意外退出的情况为例，实现的方法如下：

Client 传递一个 Binder 对象给 Service，此 Binder 对象与 Client 的进程关联；

在 Sevice 中接受到这个 Binder 对象，并且使用 binder.linkToDeath()，注册一个 DeathRecipient 回调；

实现 DeathRecipient。当 Client 意外退出的时候，DeathRecipient.binderDied()将被回调，可以在这里释放相关资源。

具体实现，修改AIDL的定义如下：
```java
// IRemoteService.aidl
package com.race604.servicelib;
interface IRemoteService {  
    ...

    void join(IBinder token, String name);
    void leave(IBinder token);
    List<String> getParticipators();
}
```
注意到这里接口中传入了一个 IBinder 对象 token，此就是客户端的唯一标示。
接下来重点看一下 Service 的实现。我们首先定义个类来保存 Client 的信息，如下：
```java
private final class Client implements IBinder.DeathRecipient {  
    public final IBinder mToken;
    public final String mName;

    public Client(IBinder token, String name) {
        mToken = token;
        mName = name;
    }

    @Override
    public void binderDied() {
        // 客户端死掉，执行此回调
        int index = mClients.indexOf(this);
        if (index < 0) {
            return;
        }

        Log.d(TAG, "client died: " + mName);
        mClients.remove(this);
    }
}
```

这里为了方便，因为每个 IBinder 都需要注册一个 IBinder.DeathRecipient 回调，我们就直接让 Client 实现此接口。

Service 中保存客户端的信息也做如下修改：
```java
private List<Client> mClients = new ArrayList<>();// 通过IBinder查找Client
private int findClient(IBinder token) {  
    for (int i = 0; i < mClients.size(); i++) {
        if (mClients.get(i).mToken == token) {
            return i;
        }
    }
    return -1;
}
```

然后修改 join() 的实现如下：

```java
@Override
public void join(IBinder token, String name) throws RemoteException {  
    int idx = findClient(token);
    if (idx >= 0) {
        Log.d(TAG, "already joined");
        return;
    }

    Client client = new Client(token, name);
    // 注册客户端死掉的通知
    token.linkToDeath(client, 0);
    mClients.add(client);
}
```

注意到这里的 token.linkToDeath(client, 0);，表示的含义就是与 token（IBinder对象）关联的客户端，如果意外退出，就会回调 client.binderDied() 方法。

同理 leave() 的实现：
```java
@Override
public void leave(IBinder token) throws RemoteException {  
    int idx = findClient(token);
    if (idx < 0) {
        Log.d(TAG, "already left");
        return;
    }

    Client client = mClients.get(idx);
    mClients.remove(client);

    // 取消注册    client.mToken.unlinkToDeath(client, 0);
}
```
当调用 leave 的时候，释放相关资源，取消 IBinder.DeathRecipient 回调，即 client.mToken.unlinkToDeath(client, 0);。

客户端主要代码如下：
```java
package com.race604.client;
import ...
public class MainActivity extends ActionBarActivity {  
    ...
    private IBinder mToken = new Binder();
    private boolean mIsJoin = false;

    private void toggleJoin() {
        if (!isServiceReady()) {
            return;
        }

        try {
            if (!mIsJoin) {
                String name = "Client:" + mRand.nextInt(10);
                mService.join(mToken, name);
                mJoinBtn.setText(R.string.leave);
                mIsJoin = true;
            } else {
                mService.leave(mToken);
                mJoinBtn.setText(R.string.join);
                mIsJoin = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
```

在客户端 join() 后，在最近任务列表中删除 client 应用，可以看到 service 端打印信息：
```
02-04 14:01:23.627: D/RemoteService(29969): client died: Client:6
```

可见 Kill 掉客户端，回调到了这里：

```java
private final class Client implements IBinder.DeathRecipient {  
    ...
    @Override    public void binderDied() {
        // 客户端死掉，执行此回调        ...
        Log.d(TAG, "client died: " + mName);
        ...
    }
}
```

### [源码地址](https://github.com/race604/AIDLService-sample/commit/58052056c052eb0f01554c18d2f224638e836450)
