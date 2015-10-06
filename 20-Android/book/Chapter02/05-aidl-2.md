AIDL
===

本文实现一个稍微复杂一点的场景：

Service 实现一个控制中心（例如一个多人游戏），客户端可以随时加入，或者退出，每个客户端都可以获取当前参与进来的成员列表。

根据需求，在上一篇文章的代码的基础上，可以很容易申明如下接口：

```java
// IRemoteService.aidlpackage com.race604.servicelib;
interface IRemoteService {  
    ...

    void join(String userName);
    void leave(String userName);
    List<String> getParticipators();
}
```

Service的实现也很简单，大致如下：
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
这里的实现非常简单，看起来也没有问题。

客户端的实现我这里就不写了。我们期望Client调用join()和leave()成对出现。在离开的时候，注意调用leave()。

但是，考虑一个情况：客户端意外退出。例如客户端因为错误应用Crash，或者被Kill掉了。没有机会调用到leave()。这样Service中的mClients中的还保持这个客户端的信息，得不到释放。这里还是一个简单的例子，但是如果Service中如果为Client申请了一些资源，客户端意外退出以后，Service中资源得不到释放，会造成资源浪费。

幸运的是，Binder有可以让对端的进程得到意外退出通知的机制：Link-To-Death。我这里以我们这里Service被通知Client意外退出的情况为例，实现的方法如下：

Client传递一个Binder对象给Service，此Binder对象与Client的进程关联；

在Sevice中接受到这个Binder对象，并且使用binder.linkToDeath()，注册一个DeathRecipient回调；

实现DeathRecipient。当Client意外退出的时候，DeathRecipient.binderDied()将被回调，我们可以在这里释放相关的资源。

具体实现如下： 修改AIDL的定义如下：
```java
// IRemoteService.aidlpackage com.race604.servicelib;
interface IRemoteService {  
    ...

    void join(IBinder token, String name);
    void leave(IBinder token);
    List<String> getParticipators();
}
```
注意到这里接口中传入了一个IBinder对象token，此就是客户端的唯一标示。

接下来重点看一下Service的实现。我们首先定义个类来保存Client的信息，如下：
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

这里为了方便，因为每个IBinder都需要注册一个IBinder.DeathRecipient回调，我们就直接让Client实现此接口。

Service中保存客户端的信息也做如下修改：
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

然后修改join()的实现如下：

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

注意到这里的token.linkToDeath(client, 0);，表示的含义就是与token（IBinder对象）关联的客户端，如果意外退出，就会回调client.binderDied()方法。

同理leave()的实现如下：
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
当调用leave的时候，释放相关资源，取消IBinder.DeathRecipient回调，即client.mToken.unlinkToDeath(client, 0);。

客户端调用就比较简单了，主要代码如下：
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
至此，核心代码都实现了。完整的代码请参考这个Commit。

我们做如下测试，在客户端 join()后，然后在最近任务列表中，删除 client 应用，看到 service 端打印信息：
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
