单例模式
===

### 模式定义

保证一个类仅有一个实例，并提供一个访问它的全局访问点。

### UML 类图

![单例模式](img/17-singleton.png)

### 饿汉式

在类初始化时就创建实例。

```java
public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {

        return instance;
    }

}
```

饿汉式是典型的空间换时间，如果该Singleton实例的创建非常消耗系统资源，而业务运行到最后才会使用Singleton实例，使用懒汉式更佳。

### 懒汉式(惰性加载)

在首次调用时才创建实例。

```java
public class Singleton {
    private static Singleton instance = null;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

}
```
上述代码在单线程中没有问题，上述代码问题在于，多线程环境下，可能会产生多个 Singleton 实例。
假设两个线程 A 和 B 同时执行了 getInstance()：

1. 线程 A 进入 if 判断，此时变量 instance 为 null，因此进入 if 语句内部；
2. 线程 B 同时进入 if 判断，但此时线程 A 还没有完成变量 instance 的创建，因此 instance 的值仍为 null，因此线程 B 也进入 if 语句内部；
3. 线程 A 创建了一个变量 instance 并返回；
4. 线程 B 也创建了一个变量 instance 并返回。

此时问题出现了，单例被创建了两次，而这并不是我们所期望的。

### 1. 懒汉式-Class级别同步锁

```java
public class Singleton {
    private static Singleton instance = null;
    private Singleton(){}

    public synchronized static Singleton getInstance() {
       if(instance == null) {
           instance = new Singleton();
       }
       return instance;
    }
}
```

在这个版本中，每次调用 getInstance 都需要取得 Singleton.class 上的锁，然而该锁只是在开始构建 Singleton 对象的时候才是必要的，后续的多线程访问，效率会降低，于是有了接下来的版本：

```java
public class Singleton {
    private static Singleton instance = null;
    private Singleton(){}

    public static Singleton getInstance() {
       if (instance == null) {
           synchronized(Singleton.class) {
              if (instance == null) {
                  instance = new Singleton();    // 语句1
              }
           }
       }
       return instance;
    }
}
```

但是该方案也未能解决问题之根本：
原因在于：初始化 Singleton  和 将对象地址写到 instance 字段的顺序是不确定的。

```
instance = new Singleton();这一行代码可以分解为如下的三行伪代码：

memory = allocate();   // 1：分配对象的内存空间
ctorInstance(memory);  // 2：初始化对象
instance = memory;     // 3：设置instance指向刚分配的内存地址

但在一些JIT编译上，语句2，3可能会被重排序：

memory = allocate();   // 1：分配对象的内存空间
instance = memory;     // 3：设置instance指向刚分配的内存地址，此时对象还没有被初始化！
ctorInstance(memory);  // 2：初始化对象
```

上面三行伪代码的2和3之间虽然被重排序了，但这个重排序并不会改变单线程程序的执行结果：
![reorder](http://cdn.infoqstatic.com/statics_s2_20160322-0135u1/resource/articles/double-checked-locking-with-delay-initialization/zh/resources/1008100.png)

如上图所示，只要保证2排在4的前面，即使2和3之间重排序了，也不会违反intra-thread semantics。

下面，再让我们看看多线程并发执行的时候的情况。请看下面的示意图：

![reorder](http://cdn.infoqstatic.com/statics_s2_20160322-0135u1/resource/articles/double-checked-locking-with-delay-initialization/zh/resources/1008101.png)

当线程A和B按上图的时序执行时，B线程将看到一个还没有被初始化的对象。


再详细点说，对于 JVM 而言，其执行的是一个个 Java 指令。在 Java 指令中创建对象和赋值操作是分开进行的，
也就是说 instance = new Singleton(); 语句是分两步执行的。但是JVM并不保证这两个操作的先后顺序，
也就是说有可能JVM会为新的Singleton实例分配空间，然后直接赋值给instance成员，然后再去初始化这个Singleton实例。
这样就使出错成为了可能，仍然以 A、B 两个线程为例：

1. A、B 线程同时进入了第一个 if 判断。
2. 线程 A 首先进入 synchronized 块，由于 instance 为 null，所以它执行 instance = new Singleton();
3. 由于 JVM 内部的优化机制，JVM 先分配内存给Singleton实例，
并赋值给 instance 变量（注意此时 JVM 没有开始初始化这个实例），然后线程 A 离开了 synchronized 块。
4. 线程 B 进入 synchronized 块，由于 instance 此时不是 null ，因此它马上离开了 synchronized 块并将结果返回给调用该方法的程序。
5. 此时 B 线程打算使用 Singleton 实例，却发现它没有被初始化，于是发生错误。

鉴于以上原因，有人可能提出下列解决方案：

```java
public class Singleton {
    private static Singleton instance = null;
    private Singleton(){}

    public static Singleton getInstance() {
       if (instance == null) {
           Singleton temp;
           synchronized(Singleton.class) {
              temp = instance;
              if (temp == null) {
                  synchronized(Singleton.class) {
                     temp = new Singleton();
                  }
                  instance = temp;
              }
           }
       }
       return instance;
    }
}
```

该方案将 Singleton 对象的构造置于最里面的同步块，这种思想是在退出该同步块时设置一个内存屏障，以阻止初始化 Singleton 和将对象地址写到 instance 字段的重新排序。

不幸的是，这种想法也是错误的，同步的规则不是这样的。
退出监视器（退出同步）的规则是：所以在退出监视器前面的动作都必须在释放监视器之前完成。
然而，并没有规定说退出监视器之后的动作不能放到退出监视器之前完成。
也就是说同步块里的代码必须在退出同步时完成，而同步块后面的代码则可以被编译器或运行时环境移到同步块中执行。

编译器可以合法的，也是合理的，将 instance = temp 移动到最里层的同步块内，这样就出现了上个版本同样的问题。

### 2. 懒汉式-双重检查

双重检查锁定在延迟初始化的单例模式中见得比较多，在 `JDK1.5` 及后续版本中，扩充了 volatile 语义，系统将不允许对写入一个 volatile 变量的操作与其之前的任何读写操作重新排序，也不允许将 读取一个 volatile 变量的操作与其之后的任何读写操作重新排序。

在 `jdk1.5` 及其后的版本中，可以将 instance 设置成 volatile 以让双重检查锁定生效，如下：

```java
public class Singleton {
    private static volatile Singleton instance = null;
    private Singleton(){}

    public static Singleton getInstance() {
       if(instance == null) {
           synchronized(Singleton.class) {
              if(instance == null) {
                  instance = new Singleton();
              }
           }
       }
       return instance;
    }
}
```

注意：在 `JDK1.4` 以及之前的版本中，该方式仍然有问题。

### 3. 懒汉式-Lazy initialization holder class方式

```java
class Singleton {
    private Singleton() {
    }

    private static class Holder {
        private static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.instance;
    }
}
```

JVM 机制能够保证当一个类被加载的时候，这个类的加载过程是线程互斥的。这样第一次调用 getInstance() 时，
JVM 能保证 instance  只被创建一次，并且会保证把赋值给 instance 的内存初始化完毕，
此外该方法也只会在第一次调用的时候使用互斥机制，这样就解决了Class级别同步锁方式的低效问题。
最后 instance 是在第一次加载 Holder 类时被创建的，
而 Holder 类则在调用 getInstance 方法的时候才会被加载，因此也实现了惰性加载。

该思路的执行流程：

1. JVM 加载 Singleton 类进行初始化，由于没有任何静态变量，初始化过程很快完成。
2. 只有 Singleton 中的静态方法getInstance() 被调用时，LazySingleton 才会初始化。
3. JVM 第一次加载并初始化 LazySingleton 时，静态变量 instance 通过执行外部类 Singleton 的私有构造函数而初始化。
由于在 JLS（Java Language Specification）中定义内部类初始化阶段是线性的、非并发的(serial, non-concurrent)，所以无需再在静态的 getInstance() 方法中指定任何 synchronized 锁。
4. 由于在类的初始化阶段，是以一种线性操作方式来写(而非无序访问)静态变量 singleton，
所有对 getInstance() 后续的并发调用，将返回同样正确初始化的 instance，而不会导致任何额外的同步负担。

什么是类级内部类？

类级内部类可简单理解为有 static 修饰的成员式内部类。如果没有 static 修饰的成员式内部类被称为对象级内部类。
类级内部类相当于其外部类的 static 成分，它的对象与外部类对象间不存在依赖关系，因此可直接创建。而对象级内部类的实例，是绑定在外部对象实例中的。
类级内部类中，可以定义静态的方法。在静态方法中只能够引用外部类中的静态成员方法或者成员变量。
类级内部类相当于其外部类的成员，只有在第一次被使用的时候才被会装载。

在多线程开发中，为了解决并发问题，主要是通过使用 synchronized 来加互斥锁进行同步控制。
但是在某些情况中，JVM 已经隐含地执行了同步，这些情况包括：

1. 由静态初始化器（在静态字段上或 static{} 块中的初始化器）初始化数据时
2. 访问 final 字段时
3. 在创建线程之前创建对象时
4. 线程可以看见它将要处理的对象时

### 枚举单例

使用枚举来实现单实例控制更加简洁，而且无偿地提供了序列化机制，并由 JVM 从根本上提供保障，防止多次实例化，是最佳的单例实现方式。

- 写法简洁

默认枚举实例的创建是线程安全的，但枚举中的其他任何方法需自行负责。

```java
public enum Singleton {
    /**
     * 定义一个枚举的元素，它就代表了Singleton的一个实例
     */

    INSTANCE;

    public void operation() {
        // 功能处理
    }
}
```
可通过 Singleton.INSTANCE 访问，简洁优雅。

- 枚举自己处理序列化

枚举无偿地提供了序列化机制，而传统单例存在的问题是一旦实现了序列化接口，就不再保持单例了，因为 readObject() 方法返回一个新的对象，要使用 readResolve() 方法来规避：

```java
// readResolve to prevent another instance of Singleton
private Object readResolve(){
    return INSTANCE;
}
```

- 枚举实例创建是线程安全的

创建枚举默认就是线程安全的，不需要担心 double checked locking。
