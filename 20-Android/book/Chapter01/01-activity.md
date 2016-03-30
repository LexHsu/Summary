Activity
===

### Activity启动模式

一般而言是先创建一个Activity对象，然后把Activity放入一个Task堆栈里进行管理（包括切换、关闭等）。
那么问题来了，如果该堆栈里已经有这个Activity对象呢，我还要不要创建一个新的对象呢？Android提供了四种选择方案：

1. 不管堆栈里有没有，都创建一个新的activity放在堆栈顶部：standard模式
2. 如果我要打开的activity正好在堆栈顶部，那就直接用它；否则也创建新的： singleTop模式
3. 这个比较霸道。即如果activity已经存在在堆栈里，那么当要打开这个activity时，就算栈的上层还有其他activity，也统统移除，成为栈顶。而如果没有，就也要创建。：singleTask模式
4. 这个更加霸道。即打开这个activity时，直接创建一个堆栈，并创建这个activity放入新堆栈中，不去和别人挤一个堆栈。以后再要打开这个activity时，直接跳转到这个新堆栈里去用。：singleInstance模式（即只创建一个实例，单独放在一个task堆栈里给别的task栈共享）

![activity](http://ww1.sinaimg.cn/large/005SiNxyjw1f25pnc5z5lj30m10f50ur.jpg)


### 对象序列化机制

组件间通信时会先用bundle对数据打包，然后交给信使intent。

一种是原型数据，如int、string等，可以直接打包并传递。打包方式如下：

```java
bundle.putInt("key1", 20);
bundle.putString("key2", "hello");
```
另一种是java对象，这是不能直接给bundle的，而要通过序列化后再给bundle，然后对象会以二进制流形式传输，直到目标组件接受到bundle后，要对该对象二进制数据反序列化才能获取真实的的对象。

那么如何对对象进行序列化呢？
两种方法。

基于java语言的Serializable序列化方法：对象类要实现Serializable接口；
基于android系统提出的Parcel序列化方法：实现Parcelable接口。
只要类实现了这两个接口，就可以把对象存入bundle中：

```java
bundle.putSerializable(Key, Object);
bundle.putParcelable(Key, Object);
```

那么

##### 这两种机制区别

JAVA中的Serialize机制是将对象转化为字节流存储在外部设备，在需要时重新生成对象(采用java反射机制)。主要用于外部设备保存对象状态，网络传输对象等场景。缺点是产生很多中间对象及造成一定的GC(垃圾回收)，简而言之Serialize更慢；
Android提供的Parcel机制是针对移动设备的轻量级高效对象序列化机制。整个过程均在内存进行，不涉及外部设备，反序列化时读取的就是原对象，而不会创建新对象。简单来说Parcel更快；不过它使用复杂。
