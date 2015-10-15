AIDL(Android Interface Definition Language)
===

### 使用场景

1. 仅在 A 应用程序(客户端)访问 B 应用程序(服务端)来实现 IPC 通信，且在 A 多线程（或多客户端）访问 B 的情况下使用 AIDL。
2. 如果不需要使用进程间 IPC 通信，通过 Binder 接口实现更合适，
3. 如果需要实现进程间的 IPC 通信，但不需要处理多线程（多客户端），通过 Messager 接口实现更合适。


同进程的线程调用和其他进程调用 AIDL 接口之间是有所区别的：

在同进程中调用 AIDL 接口，AIDL 接口代码的执行将在调用该 AIDL 接口的线程中完成，如果在主 UI 线程中调用 AIDL 接口，该调用将会在主 UI 线程中完成。

远程进程调用 AIDL 接口时，将会在 AIDL 所属的进程的线程池中分派一个线程来执行该 AIDL 代码，所以编写 AIDL 时，必须准备好可能有未知线程访问、同一时间可能有多个调用发生（多个线程的访问），所以 ADIL 接口的实现必须是线程安全的。

可以用关键字 oneway 来标明远程调用的行为属性，不会租塞远程线程的运行，那么远程调用将仅仅是调用所需的数据传输过来并立即返回，而不会等待结果的返回。
AIDL 接口将最终将获得一个从 Binder 线程池中产生的调用（和普通的远程调用类似）。如果关键字 oneway 在本地调用中被使用，将不会对函数调用有任何影响。

### 定义 AIDL 接口

AIDL 接口使用 java 语法编写，文件后缀名为 .aidl，该文件保存在 src/目录下。编译时，
Android sdk 工具将会为 src/目录下的 .aidl 文件在 gen/ 目录下产生一个 IBinder 接口的 .java 文件。
被调用方必须相应的实现该IBinder接口。调用方可以绑定该服务、调用其中的方法实现 IPC 通信。

创建一个用AIDL实现的服务端，需要以下几个步骤：

1. 创建.aidl文件：该文件（YourInterface.aidl）定义了客户端可用的方法和数据的接口
2. 实现该接口：Android SDK 将会根据 .aidl 文件产生 AIDL 接口。生成的接口包含一个名为 Stub 的抽象内部类，该类声明了所有 .aidl 中描述的方法，在代码里继承该 Stub 类并且实现 .aidl 中定义的方法。
3.向客户端公开服务端的接口：实现一个Service，并且在 onBinder 方法中返回第 2 步中实现的 Stub 类的子类（实现类）。

注意：服务端 AIDL 的任何修改都必须的同步到所有的客户端，否则客户端调用服务端得接口可能会导致程序异常（因为此时客户端此时可能会调用到服务端已不再支持的接口）。

### 创建 AIDL 文件

AIDL使用简单的语法来声明接口，描述其方法以及方法的参数和返回值。这些参数和返回值可以是任何类型，甚至是其他AIDL生成的接口。重要的是必须导入所有非内置类型，哪怕是这些类型是在与接口相同的包中。

默认的AIDL支持一下的数据类型（这些类型不需要通过import导入）：

- java语言的原始数据类型（包括 int, long, char, boolen 等等）
- String
- CharSequence：该类是被TextView和其他控件对象使用的字符序列
- List：列表中的所有元素必须是在此列出的类型，包括其他AIDL生成的接口和可打包类型。List可以像一般的类（例如List<String>）那样使用，另一边接收的具体类一般是一个ArrayList，这些方法会使用List接口
- Map：Map中的所有元素必须是在此列出的类型，包括其他AIDL生成的接口和可打包类型。一般的maps（例如Map<String,Integer>）不被支持，另一边接收的具体类一般是一个HashMap，这些方法会使用Map接口。

对于其他的类型，在 aidl 中必须使用 import 导入，即使该类型和 aidl 处于同一包内。

定义一个服务端接口时，注意一下几点：

- 方法可以有 0 个或多个参数，可以使空返回值也可以返回所需的数据。
- 所有非原始数据类型的参数必须指定参数方向（是传入参数，还是传出参数），传入参数使用 in 关键字标记，传出参数使用 out，传入传出参数使用 inout。如果没有显示的指定，那么将缺省使用 in。
- 在 aidl 文件中所有的注释都将会包含在生成的 IBinder 接口中（在 Import 和 pacakge 语句之上的注释除外）。
- aidl 中只支持成员方法，不支持成员变量。

注意：限定参数的传输方向非常有必要，因为编组（序列化）参数的代价非常昂贵。


### 实现接口

将 .aidl 文件保存在工程目录中的 src/ 目录下，当编译生成 apk 时，sdk 工具将会在 gen/ 目录下生成一个对应的 IBiner 接口的 .java 文件。生成的接口包含一个名为 Stub 的抽象的内部类，该类声明了所有 .aidl 中描述的方法，

Stub 还定义了少量的辅助方法，尤其是 asInterface()，通过它或以获得 IBinder（当 applicationContext.bindService() 成功调用时传递到客户端的 onServiceConnected()）并且返回用于调用 IPC 方法的接口实例。

更多细节参见[Calling an IPC Method](http://developer.android.com/guide/developing/tools/aidl.html#calling)。

要实现自己的接口，就要继承 YourInterface.Stub类，然后实现相关方法，注意：

- 不能保证所有对 aidl 接口的调用都在主线程中执行，所以必须考虑多线程调用的情况，也就是必须考虑线程安全。
- 默认 IPC 调用是同步的。如果已知 IPC 服务端会花费很多毫秒才能完成，那就不要在 UI 线程调用，否则会引起应用程序挂起
- 不会将异常返回给调用方

### 向客户端暴露接口

在完成了接口的实现后需要向客户端暴露接口，也就是发布服务，实现的方法是继承 Service，然后实现以Service.onBind(Intent)返回一个实现了接口的类对象。下面的代码表示了暴露IRemoteService接口给客户端的方式。
现在，如果客户端（比如一个Activity）调用bindService()来连接该服务端（RemoteService） ，客户端的onServiceConnected()回调函数将会获得从服务端（RemoteService ）的onBind（）返回的mBinder对象。

客户端同样得访问该接口类（这里指IRemoteService），所以，如果服务端和客户端不在同一进程（应用程序）中，那么客户端也必须在 src/ 目录下拥有和服务端同样的一份.aidl文件的拷贝（同样是指，包名、类名、内容完全一模一样），客户端将会通过这个.aidl文件生成android.os.Binder接口——以此来实现客户端访问AIDL中的方法。

当客户端在onServiceConnected()回调方法中获得IBinder对象后，必须通过调用YourServiceInterface.Stub.asInterface(service)将其转化成为YourServiceInterface类型
