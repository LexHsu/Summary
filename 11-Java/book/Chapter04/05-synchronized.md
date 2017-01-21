synchronized
===

synchronized可以用于修饰类的实例方法、静态方法和代码块，我们分别来看下。

### 一、实例方法
上节计数的例子，当多个线程并发执行counter++的时候，由于该语句不是原子操作，出现了意料之外的结果，这个问题可以用synchronized解决。

```java
public class Counter {
    private int count;

    public synchronized void incr(){
        count ++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

Counter是一个简单的计数器类，incr方法和getCount方法都加了synchronized修饰。加了synchronized后，方法内的代码就变成了原子操作，当多个线程并发更新同一个Counter对象的时候，也不会出现问题，我们看使用的代码：

```java
public class CounterThread extends Thread {
    Counter counter;

    public CounterThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((int) (Math.random() * 10));
        } catch (InterruptedException e) {
        }
        counter.incr();
    }

    public static void main(String[] args) throws InterruptedException {
        int num = 100;
        Counter counter = new Counter();
        Thread[] threads = new Thread[num];
        for (int i = 0; i < num; i++) {
            threads[i] = new CounterThread(counter);
            threads[i].start();
        }
        for (int i = 0; i < num; i++) {
            threads[i].join();
        }
        System.out.println(counter.getCount());
    }
}
```

与上节类似，我们创建了100个线程，传递了相同的counter对象，每个线程主要就是调用Counter的incr方法，main线程等待子线程结束后输出counter的值，这次，不论运行多少次，结果都是正确的100。

这里，synchronized到底做了什么呢？看上去，synchronized使得同时只能有一个线程执行实例方法，但这个理解是不确切的。多个线程是可以同时执行同一个synchronized实例方法的，只要它们访问的对象是不同的，比如说：

```java
Counter counter1 = new Counter();
Counter counter2 = new Counter();
Thread t1 = new CounterThread(counter1);
Thread t2 = new CounterThread(counter2);
t1.start();
t2.start();
```

这里，t1和t2两个线程是可以同时执行Counter的incr方法的，因为它们访问的是不同的Counter对象，一个是counter1，另一个是counter2。

所以，`synchronized实例方法实际保护的是同一个对象的方法调用`，确保同时只能有一个线程执行。再具体来说，synchronized实例方法保护的是当前实例对象，即this，this对象有一个锁和一个等待队列，锁只能被一个线程持有，其他试图获得同样锁的线程需要等待，执行synchronized实例方法的过程大概如下：

1. 尝试获得锁，如果能够获得锁，继续下一步，否则加入等待队列，阻塞并等待唤醒
2. 执行实例方法体代码
3. 释放锁，如果等待队列上有等待的线程，从中取一个并唤醒，如果有多个等待的线程，唤醒哪一个是不一定的，不保证公平性

synchronized的实际执行过程比这要复杂的多，而且Java虚拟机采用了多种优化方式以提高性能，但从概念上，我们可以这么简单理解。

当前线程不能获得锁的时候，它会加入等待队列等待，线程的状态会变为BLOCKED。

我们再强调下，synchronized保护的是对象而非代码，只要访问的是同一个对象的synchronized方法，即使是不同的代码，也会被同步顺序访问，比如，对于Counter中的两个实例方法getCount和incr，对同一个Counter对象，一个线程执行getCount，另一个执行incr，它们是不能同时执行的，会被synchronized同步顺序执行。

此外，需要说明的，`synchronized方法不能防止非synchronized方法被同时执行`，比如，如果给Counter类增加一个非synchronized方法：

```java
public void decr() {
    count--;
}
```

decr方法可以和synchronized的incr方法同时执行，这通常会出现非期望的结果，所以，一般在保护变量时，需要在所有访问该变量的方法上加上synchronized。

### 二、静态方法

synchronized同样可以用于静态方法，比如：

```java
public class StaticCounter {
    private static int count = 0;

    public static synchronized void incr() {
        count++;
    }

    public static synchronized int getCount() {
        return count;
    }
}
```

前面我们说，synchronized保护的是对象，对实例方法，保护的是当前实例对象this，对静态方法，保护的是哪个对象呢？是类对象，这里是StaticCounter.class，实际上，每个对象都有一个锁和一个等待队列，类对象也不例外。

synchronized静态方法和synchronized实例方法保护的是不同的对象，不同的两个线程，可以同时，一个执行synchronized静态方法，另一个执行synchronized实例方法。

### 三、代码块

除了用于修饰方法外，synchronized还可以用于修饰代码块，比如对于前面的Counter类，等价的代码可以为：

```java
public class Counter {
    private int count;

    public void incr(){
        synchronized(this){
            count ++;    
        }
    }
    
    public int getCount() {
        synchronized(this){
            return count;
        }
    }
}
```

synchronized括号里面的就是保护的对象，对于实例方法，就是this，{}里面是同步执行的代码。

对于前面的StaticCounter类，等价的代码为：

```java
public class StaticCounter {
    private static int count = 0;

    public static void incr() {
        synchronized(StaticCounter.class){
            count++;    
        }    
    }

    public static int getCount() {
        synchronized(StaticCounter.class){
            return count;    
        }
    }
}
```

synchronized同步的对象可以是任意对象，任意对象都有一个锁和等待队列，或者说，任何对象都可以作为锁对象。比如说，Counter的等价代码还可以为：

```java
public class Counter {
    private int count;
    private Object lock = new Object();
    
    public void incr(){
        synchronized(lock){
            count ++;    
        }
    }
    
    public int getCount() {
        synchronized(lock){
            return count;
        }
    }
}
```

### 四、理解synchronized

介绍了synchronized的基本用法和原理，我们再从下面几个角度来进一步理解一下synchronized：

1. 可重入性
2. 内存可见性
3. 死锁

#### 1. 可重入性

synchronized有一个重要的特征，它是可重入的，也就是说，对同一个执行线程，它在获得了锁之后，在调用其他需要同样锁的代码时，可以直接调用，比如说，在一个synchronized实例方法内，可以直接调用其他synchronized实例方法。可重入是一个非常自然的属性，应该是很容易理解的，之所以强调，是因为并不是所有锁都是可重入的(后续章节介绍)。

可重入是通过记录锁的持有线程和持有数量来实现的，当调用被synchronized保护的代码时，检查对象是否已被锁，如果是，再检查是否被当前线程锁定，如果是，增加持有数量，如果不是被当前线程锁定，才加入等待队列，当释放锁时，减少持有数量，当数量变为0时才释放整个锁。

#### 2. 内存可见性

对于复杂一些的操作，synchronized可以实现原子操作，避免出现竞态条件，但对于明显的本来就是原子的操作方法，也需要加synchronized吗？比如说，对于下面的开关类Switcher，它只有一个boolean变量on和对应的setter/getter方法：

```java
public class Switcher {
    private boolean on;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
```

当多线程同时访问同一个Switcher对象时，会有问题吗？没有竞态条件问题，但正如上节所说，有内存可见性问题，而加上synchronized可以解决这个问题。

synchronized除了保证原子操作外，它还有一个重要的作用，就是保证内存可见性，在释放锁时，所有写入都会写回内存，而获得锁后，都会从内存中读最新数据。

不过，如果只是为了保证内存可见性，使用synchronzied的成本有点高，有一个更轻量级的方式，那就是给变量加修饰符volatile，如下所示：

```java
public class Switcher {
    private volatile boolean on;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
```

加了volatile之后，Java会在操作对应变量时插入特殊的指令，保证读写到内存最新值，而非缓存的值。

#### 3. 死锁

使用synchronized或者其他锁，要注意死锁，所谓死锁就是类似这种现象，比如， 有a, b两个线程，a持有锁A，在等待锁B，而b持有锁B，在等待锁A，a,b陷入了互相等待，最后谁都执行不下去。示例代码如下所示：

```java
public class DeadLockDemo {
    private static Object lockA = new Object();
    private static Object lockB = new Object();

    private static void startThreadA() {
        Thread aThread = new Thread() {

            @Override
            public void run() {
                synchronized (lockA) {
                    try {
                        Thread.sleep(1000); // 设置等待时间构造死锁
                    } catch (InterruptedException e) {
                    }
                    synchronized (lockB) {
                    }
                }
            }
        };
        aThread.start();
    }

    private static void startThreadB() {
        Thread bThread = new Thread() {
            @Override
            public void run() {
                synchronized (lockB) {
                    try {
                        Thread.sleep(1000); // 设置等待时间构造死锁
                    } catch (InterruptedException e) {
                    }
                    synchronized (lockA) {
                    }
                }
            }
        };
        bThread.start();
    }

    public static void main(String[] args) {
        startThreadA();
        startThreadB();
    }
}
```

1. startThreadA(): 线程A获取lockA锁，等待1秒
2. startThreadB(): 线程B获取lockB锁，等待1秒
3. 线程A等待时间结束，尝试获取lockB锁，而此时lockB锁已被占用，等待
4. 线程B等待时间结束，尝试获取lockA锁，而此时lockA锁已被占用，等待

运行后aThread和bThread陷入了相互等待。怎么解决呢？首先，应该尽量避免在持有一个锁的同时去申请另一个锁，如果确实需要多个锁，所有代码都应该按照相同的顺序去申请锁，比如，对于上面的例子，可以约定都先申请lockA，再申请lockB。

不过，在复杂的项目代码中，这种约定可能难以做到。还有一种方法是使用后续章节介绍的显式锁接口Lock，它支持尝试获取锁(tryLock)和带时间限制的获取锁方法，使用这些方法可以在获取不到锁的时候释放已经持有的锁，然后再次尝试获取锁或干脆放弃，以避免死锁。

如果还是出现了死锁，怎么办呢？Java不会主动处理，不过，借助一些工具，我们可以发现运行中的死锁，比如，Java自带的jstack命令会报告发现的死锁，对于上面的程序，在我的电脑上，jstack会有如下报告：

```
Found one Java-level deadlock:
...
```


### 五、同步容器及其注意事项

#### 1. 同步容器

我们在54节介绍过Collection的一些方法，它们可以返回线程安全的同步容器，比如：

```java
public static <T> Collection<T> synchronizedCollection(Collection<T> c)
public static <T> List<T> synchronizedList(List<T> list) 
public static <K,V> Map<K,V> synchronizedMap(Map<K,V> m)
```

它们是给所有容器方法都加上synchronized来实现安全的，比如SynchronizedCollection，其部分代码如下所示：

```java
static class SynchronizedCollection<E> implements Collection<E> {
    final Collection<E> c;  // Backing Collection
    final Object mutex;     // Object on which to synchronize

    SynchronizedCollection(Collection<E> c) {
        if (c==null)
            throw new NullPointerException();
        this.c = c;
        mutex = this;
    }
    public int size() {
        synchronized (mutex) {return c.size();}
    }
    public boolean add(E e) {
        synchronized (mutex) {return c.add(e);}
    }
    public boolean remove(Object o) {
        synchronized (mutex) {return c.remove(o);}
    }
    //....
}
```

这里线程安全针对的是容器对象，指的是当多个线程并发访问同一个容器对象时，不需要额外的同步操作，也不会出现错误的结果。

加了synchronized，所有方法调用变成了原子操作，客户端在调用时，是不是就绝对安全了呢？不是的，至少有以下情况需要注意：

1. 复合操作，比如先检查再更新
2. 伪同步
3. 迭代

复合操作
先来看复合操作，我们看段代码：
public class EnhancedMap <K, V> {
    Map<K, V> map;
    
    public EnhancedMap(Map<K,V> map){
        this.map = Collections.synchronizedMap(map);
    }
    
    public V putIfAbsent(K key, V value){
         V old = map.get(key);
         if(old!=null){
             return old;
         }
         map.put(key, value);
         return null;
     }
    
    public void put(K key, V value){
        map.put(key, value);
    }
    
    //... 其他代码
}

EnhancedMap是一个装饰类，接受一个Map对象，调用synchronizedMap转换为了同步容器对象map，增加了一个方法putIfAbsent，该方法只有在原Map中没有对应键的时候才添加。

map的每个方法都是安全的，但这个复合方法putIfAbsent是安全的吗？显然是否定的，这是一个检查然后再更新的复合操作，在多线程的情况下，可能有多个线程都执行完了检查这一步，都发现Map中没有对应的键，然后就会都调用put，而这就破坏了putIfAbsent方法期望保持的语义。

### 2. 伪同步

那给该方法加上synchronized就能实现安全吗？如下所示：

```java
public synchronized V putIfAbsent(K key, V value){
    V old = map.get(key);
    if(old!=null){
        return old;
    }
    map.put(key, value);
    return null;
}
```

答案是否定的！为什么呢？同步错对象了。putIfAbsent同步使用的是EnhancedMap对象，而其他方法(如代码中的put方法)使用的是Collections.synchronizedMap返回的对象map，两者是不同的对象。要解决这个问题，所有方法必须使用相同的锁，可以使用EnhancedMap的对象锁，也可以使用map。使用EnhancedMap对象作为锁，则EnhancedMap中的所有方法都需要加上synchronized。使用map作为锁，putIfAbsent方法可以改为：

```java
public V putIfAbsent(K key, V value){
    synchronized(map){
        V old = map.get(key);
         if(old!=null){
             return old;
         }
         map.put(key, value);
         return null;    
    }
}
```


### 3. 迭代

对于同步容器对象，虽然单个操作是安全的，但迭代并不是。我们看个例子，创建一个同步List对象，一个线程修改List，另一个遍历，看看会发生什么，代码为：

```java
private static void startModifyThread(final List<String> list) {
    Thread modifyThread = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                list.add("item " + i);
                try {
                    Thread.sleep((int) (Math.random() * 10));
                } catch (InterruptedException e) {
                }
            }
        }
    });
    modifyThread.start();
}

private static void startIteratorThread(final List<String> list) {
    Thread iteratorThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                for (String str : list) {
                }
            }
        }
    });
    iteratorThread.start();
}

public static void main(String[] args) {
    final List<String> list = Collections
            .synchronizedList(new ArrayList<String>());
    startIteratorThread(list);
    startModifyThread(list);
}
```

运行该程序，程序抛出并发修改异常：

```java
Exception in thread "Thread-0" java.util.ConcurrentModificationException
    at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:859)
    at java.util.ArrayList$Itr.next(ArrayList.java:831)
```

我们之前介绍过这个异常，如果在遍历的同时容器发生了结构性变化，就会抛出该异常，同步容器并没有解决这个问题，如果要避免这个异常，需要在遍历的时候给整个容器对象加锁，比如，上面的代码，startIteratorThread可以改为：

```java
private static void startIteratorThread(final List<String> list) {
    Thread iteratorThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                synchronized(list){
                    for (String str : list) {
                    }    
                }
            }
        }
    });
    iteratorThread.start();
}
```

### 3. 并发容器
除了以上这些注意事项，同步容器的性能也是比较低的，当并发访问量比较大的时候性能很差。所幸的是，Java中还有很多专为并发设计的容器类，比如：

```java
CopyOnWriteArrayList
ConcurrentHashMap
ConcurrentLinkedQueue
ConcurrentSkipListSet
```

这些容器类都是线程安全的，但都没有使用synchronized、没有迭代问题、直接支持一些复合操作、性能也高得多，它们能解决什么问题？怎么使用？实现原理是什么？我们留待后续章节介绍。

### 六、总结

本节详细介绍了synchronized的用法和实现原理，为进一步理解synchronized，介绍了可重入性、内存可见性、死锁等，最后，介绍了同步容器及其注意事项如复合操作、伪同步、迭代异常、并发容器等。

Synchronized修饰分为类级别和对象级别，分别对应着类锁和对象锁。

##### 1. 修饰类对象

用于修饰静态方法，或者静态方法内的代码块，同一时刻只有一个线程可访问该方法，无论有多少实例。

```java
synchronized static void foo() {
    // ...
}
// 等价于
static void foo() {
    synchronized (TestClass.class) {
        // ...
    }
}
```

##### 2. 修饰实例对象

用于修饰成员方法，或者成员方法内的代码块。

```java
synchronized void foo() {
    // ...
}
// 等价于
void foo() {
    synchronized (this) {
        // ...
    }
}
```

1. 无论 synchronized 关键字加在方法上还是对象上，最终取得的锁都是对象。
2. synchronized 关键字不能继承。
3. 零长度的 byte 数组作为对象锁，`private byte[] lock = new byte[0];`。
生成零长度的 byte[] 只需 3 条操作码，而 `new Object()` 需要 7 行操作码。
4. 两个线程访问同一对象中的同步方法时，
同一时间内只能有一个线程得到执行。另一个线程必须等待当前线程执行完该代码块以后才能执行。
5. 一个线程访问 Object 的 synchronized(this) 同步代码块时，
其他线程对该 Object 的其它 synchronized(this) 同步代码块的访问也被阻塞。
但可访问该 object 中的非 synchronized(this) 同步代码块。
6. 多个线程可同时调用不同实例对象的同步方法，如上文中的foo()方法。

多线程之间除了竞争访问同一个资源外，也经常需要相互协作，怎么协作呢？下节介绍协作的基本机制wait/notify。
