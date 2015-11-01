线程间协作
===

多线程协作常见场景：如果条件不满足，则等待；当条件满足时，等待该条件的线程将被唤醒。
在 Java 中，该同步机制的实现有如下几种方式：

### wait 与 notify

```
wait()
该线程阻塞，该对象上的锁被释放，synchronied 中的代码不会往下执行。当线程被唤醒时，才有机会获取对象锁。

notify()
唤醒一个等待该对象同步锁的线程，若有多个等待的线程，由 JVM 决定。
同时使用 notify 和 wait 方法，必须先调用notify，因为 wait 后代码块阻塞。

notifyAll()
唤醒所有在 notify 之前 wait 的线程，但只有一个线程获得同步锁。
```

### 示例

```java
synchronized(obj) {
    while (!condition) {
        obj.wait();
    }
    obj.doSomething();
}
　　
当线程 A 获得了obj 锁后，满足 condition 进入 wait，直到线程 B 变更 condition 之后唤醒线程 A。
　　
synchronized(obj) {
    condition = true;
    obj.notify();
}
```

注意：

1. 调用 obj 的 wait(), notify() 方法前，必须获得 obj 锁，即必须写在 `synchronized(obj){...}` 代码段内。
否则虽然可编译通过，但会抛出IllegalMonitorStateException异常。
2. 调用 obj.wait() 后，线程 A 就释放了 obj 的锁，否则线程 B 无法获得 obj 锁，也就无法在`synchronized(obj){...}` 代码段内唤醒 A。
3. 线程 B notify 之后，线程 A 需要再次获得obj锁，才能继续执行。
4. 如果 A1, A2, A3 都在 obj.wait()，则 B 调用 obj.notify() 只能唤醒 A1, A2, A3 中的一个（由 JVM 决定）。
obj.notifyAll() 则能全部唤醒 A1, A2, A3，但是要继续执行 obj.wait() 的下一条语句，必须获得 obj 锁，
因此，A1, A2, A3 只有一个有机会获得锁继续执行，例如 A1，其余的需要等待A1释放obj锁之后才能继续执行。
当 B notify 的时候，B 正持有 obj 锁，因此，A1, A2, A3 虽被唤醒，但是仍无法获得 obj 锁。直到 B 退出 synchronized 块，释放 obj 锁后，A1, A2, A3 中的一个才有机会获得锁继续执行。


```java
public class Depository {

    public synchronized void addLast(Product p) {
        this.notifyAll();
        if (condition) {
            Thread.currentThread().wait();  // 应使用 this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
```
上述代码会抛 `java.lang.IllegalMonitorStateException: current thread not owner`，
因为 wait()，notify() 是对象的方法，不是线程的方法。
public synchronized void addLast(Product p) 表示该锁是 Depository 对象，即当前对象。
