ThreadLocal
===

### ThreadLocal作用

1. 在同一个线程内，可在完全不相关的两个段代码、方法之间共享一个变量
1. 在同一个线程内，可避免通过方法传参的方式传递成员变量。

ThreadLocal 不是用来解决共享对象的多线程访问问题的，其与synchronized完全没有可比性：

1. ThreadLocal避免通过参数传递来共享同一线程内的资源。
2. synchronized 解决的是多个线程间的资源共享问题


### ThreadLocal原理

1. 每个线程中都有一个自己的ThreadLocalMap类对象，可以将线程自己的对象保持到其中，互不干扰。
2. 将一个共用的ThreadLocal静态实例作为key，将不同对象的引用保存到不同线程的ThreadLocalMap中，
然后在线程执行的各处通过这个静态ThreadLocal实例的get()方法取得自己线程保存的那个对象，从而避免了参数的传递。

一般情况下，通过ThreadLocal.set() 到线程中的对象是该线程自己使用的对象，其他线程不需要访问。各个线程中访问的是不同的对象。


如果没有ThreadLocal，如何解决问题？

没有ThreadLocal，每个Thread中都有输入自己的一个本地变量，但是在整个Thread生命周期内，如果要穿梭很多class的很多method来使用这个本地变量的话，就要一直一直向下传送这个变量，显然很麻烦。
那么怎么才能在这个Thread的生命中，在任何地方都能够方便的访问到这个变量呢，这时候ThreadLocal就诞生了。
ThreadLocal就是这么个作用，除此之外和通常使用的本地变量没有任何区别。


### ThreadLocalMap

ThreadLocalMap是定以在ThreadLocal中的Hashmap，仅用于存放线程汇总的本地属性值，其引用却是在Thread类中。

###
```java
static class Entry extends WeakReference<ThreadLocal> {
    /** The value associated with this ThreadLocal. */
    Object value;

    Entry(ThreadLocal k, Object v) {
        super(k);
        value = v;
    }
}
```

可见该Map的key是ThreadLocal变量，value为用户的值，ThreadLocal工作如下：

1. Thread类中有一个成员变量叫做ThreadLocalMap，它是一个Map，他的Key是ThreadLocal类
2. 每个线程拥有自己的申明为ThreadLocal类型的变量,所以这个类的名字叫'ThreadLocal'：线程自己的（变量）
3. 此变量生命周期是由该线程决定的，开始于第一次初始（get或者set方法）
4. 由ThreadLocal的工作原理决定了：每个线程独自拥有一个变量，并非共享或者拷贝


### 源码分析

[ThreadLocal源码](https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/src/share/classes/java/lang/ThreadLocal.java)


首先，为了解释ThreadLocal类的工作原理，必须同时介绍与其工作甚密的其他几个类（内部类）

1. ThreadLocalMap
2. Thread

Thread类中的一行：

```java
ThreadLocal.ThreadLocalMap threadLocals = null;
```

可见，ThreadLocalMap的定义是在ThreadLocal类中，真正的引用却是在Thread类中

```java
/**
 * Get the map associated with a ThreadLocal. Overridden in
 * InheritableThreadLocal.
 *
 * @param  t the current thread
 * @return the map
 */
ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```
getMap返回当前Thread的ThreadLocalMap
每个Thread返回各自的ThreadLocalMap，所以各个线程中的ThreadLocal均为独立的

```java

/**
 * 关键方法，返回当前Thread的ThreadLocalMap
 * 每个Thread返回各自的ThreadLocalMap，所以各个线程中的ThreadLocal均为独立的
 */
ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

Threadlocal的get方法：

```java
public T get() {
    Thread t = Thread.currentThread();
    /**
     * 得到当前线程的ThreadLocalMap
     */
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        /**
         * 在此线程的ThreadLocalMap中查找key为当前ThreadLocal对象的entry
         */
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null)
            return (T)e.value;
    }
    return setInitialValue();
}
```

执行get()时首先获取当前的Thread，再获取Thread中的ThreadLocalMap - t.threadLocals，并以自身为key取出实际的value。于是可以看出，ThreadLocal的变量实际还是保存在Thread中的，容器是一个Map，Thread用到多少ThreadLocal变量，就会有多少以其为key的Entry。

```java
/**
 * 给当前thread初始ThreadlocalMap
 */
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

### 典型应用

ThreadLoacl变量的活动范围为某线程，是该线程“专有的，独自霸占”，对该变量的所有操作均有该线程完成！
也就是说，ThreadLocal不是用来解决共享，竞争问题的。典型的应用莫过于Spring，Hibernate等框架中对于多线程的处理：

```java
private static final ThreadLocal threadSession = new ThreadLocal();

public static Session getSession() throws InfrastructureException {
    Session s = (Session) threadSession.get();
    try {
        if (s == null) {
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
    } catch (HibernateException ex) {
        throw new InfrastructureException(ex);
    }
    return s;
}
```

可以看到在getSession()方法中，首先判断当前线程中有没有放进去session，如果还没有，那么通过sessionFactory().openSession()来创建一个session，再将session set到线程中，实际是放到当前线程的ThreadLocalMap这个map中，这时，对于这个session的唯一引用就是当前线程中的那个ThreadLocalMap（下面会讲到），而threadSession作为这个值的key，要取得这个session可以通过threadSession.get()来得到，里面执行的操作实际是先取得当前线程中的ThreadLocalMap，然后将threadSession作为key将对应的值取出。这个session相当于线程的私有变量，而不是public的。

显然，其他线程中是取不到这个session的，他们也只能取到自己的ThreadLocalMap中的东西。要是session是多个线程共享使用的，那还不乱套了。
试想如果不用ThreadLocal怎么来实现呢？可能就要在action中创建session，然后把session一个个传到service和dao中，这可够麻烦的。或者可以自己定义一个静态的map，将当前thread作为key，创建的session作为值，put到map中，应该也行，这也是一般人的想法，但事实上，ThreadLocal的实现刚好相反，它是在每个线程中有一个map，而将ThreadLocal实例作为key，这样每个map中的项数很少，而且当线程销毁时相应的东西也一起销毁了，不知道除了这些还有什么其他的好处。


这段代码，每个线程有自己的ThreadLocalMap，每个ThreadLocalMap中根据需要初始加载threadSession,这样的好处就是介于singleton与prototype之间，应用singleton无法解决线程，应用prototype开销又太大，有了ThreadLocal之后，对于需要线程“霸占”的变量用ThreadLocal，而该类实例的方法均可以共享。

### 总结


注意：

1. ThreadLocal使得各线程能够保持各自独立的一个对象，不是通过ThreadLocal.set()实现的，而是每个线程中的new 对象 的操作来创建的对象，每个线程创建一个，不是对象的拷贝或副本。通过ThreadLocal.set()将这个新创建的对象的引用保存到各线程的自己的一个ThreadLocalMap中，每个线程都有这样一个map，执行ThreadLocal.get()时，各线程从自己的map中取出放进去的对象，因此取出来的是各自自己线程中的对象，ThreadLocal实例是作为map的key来使用的。
2. 如果ThreadLocal.set()进去的对象本来就是多个线程共享的对象，那么多个线程的ThreadLocal.get()取得的还是这个共享对象本身，还是会有并发访问问题。
3. 如果一个类中定义了一个static的ThreadLocal，一个共享对象可以通过该ThreadLocal的set设置到多个线程的ThreadLocalMap中，但是这多个线程的ThreadLocalMap中存着的仅仅是该对象的引用，指向那个共享对象，而不是什么副本，通过ThreadLocal的get方法取到的是就是那个共享对象本身，共享访问安全问题还是要靠其他方法来解决。而实际中是不会这样使用的，很显然，这个共享变量是需要同步的(如果是线程之间的共享对象，那么其引用根本没有必要放在线程中，需要同步)
4. ThreadLocalMap在每个线程中有一个，而不是存在于ThreadLocal中，ThreadLocal更多是作为一个工具类，里面只包含一个int threadLocalHashCode，而不包含其他任何数据，数据是放在每个线程的ThreadLocalMap中的，里面存放的是通过ThreadLocal进行set和get的对象（引用），threadLocalHashCode相当于这个Map的key。
5. 如果一个类中定义了多个ThreadLocal，那么这些个ThreadLocal中的threadLocalHashCode值是不同的，也就是key不同，所以可以用来将不同的多个对象放到线程中。

6. 考虑一个类的多线程环境，对于该类中的static的某个ThreadLocal对象，在多个线程中是同一个对象，同一个threadLocalHashCode值，也就是同一个key，但是不同的是每个线程中的ThreadLocalMap，每个线程都有自己的ThreadLocalMap，所以相同的key可以对应不同的对象。

可见ThreadLocal的作用就是将经常要用到的对象的引用放到属于线程自己的一个存储空间中，在该线程的执行过程中，可以通过类的静态的ThreadLocal来方便的获取到这个对象，而不用通过参数的形式传来传去。

向ThreadLocal里面存东西就是向它里面的ThreadLocalMap存东西，然后ThreadLocal把这个Map挂到当前的线程底下，这样Map就只属于该线程。
