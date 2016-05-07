ThreadPoolExecutor
===

### 接口介绍
```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)
// 参数介绍
corePoolSize         // 线程池维护线程的最少数量

maximumPoolSize      // 线程池维护线程的最大数量

keepAliveTime        // 线程池维护线程所允许的空闲时间

unit                 // 线程池维护线程所允许的空闲时间的单位
                     // TimeUnit.NANOSECONDS
                     // TimeUnit.MICROSECONDS
                     // TimeUnit.MILLISECONDS
                     // TimeUnit.SECONDS

workQueue            // 线程池所使用的缓冲队列
                     // SynchronousQueue
                     // LinkedBlockingQueue
                     // ArrayBlockingQueue

handler              // 线程池对拒绝任务的处理策略，默认值 ThreadPoolExecutor.AbortPolicy()
ThreadPoolExecutor.AbortPolicy()            // 抛出 RejectedExecutionException 异常
ThreadPoolExecutor.CallerRunsPolicy()       // 重试添加当前的任务，自动重复调用 execute() 方法
ThreadPoolExecutor.DiscardOldestPolicy()    // 抛弃旧的任务
ThreadPoolExecutor.DiscardPolicy()          // 抛弃当前的任务
```

### 方法调用
一个任务通过 execute(Runnable) 方法被添加到线程池，任务就是一个 Runnable 类型的对象，任务的执行方法就是 Runnable 类型对象的 run() 方法。

### 处理机制

##### 通过 execute(Runnable) 方法欲添加单任务到线程池时：

1. 如果此时线程池中的数量小于 corePoolSize，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务。
2. 如果此时线程池中的数量等于 corePoolSize，但是缓冲队列 workQueue 未满，那么任务被放入缓冲队列。
3. 如果此时线程池中的数量大于 corePoolSize，缓冲队列 workQueue 满，并且线程池中的数量小于 maximumPoolSize，建新的线程来处理被添加的任务。
4. 如果此时线程池中的数量大于 corePoolSize，缓冲队列 workQueue 满，并且线程池中的数量等于 maximumPoolSize，那么通过 handler 所指定的策略来处理此任务。

##### 处理任务的多时：

核心线程 corePoolSize、任务队列 workQueue、最大线程 maximumPoolSize，如果三者都满了，使用 handler 处理被拒绝的任务。
处理任务的少时：
当线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过 keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。

### 创建线程池的一些方法：
```java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads,
                                  nThreads,
                                  0L,
                                  TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>());
}

public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0,
                                  Integer.MAX_VALUE,
                                  60L,
                                  TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());
}

public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService(
        new ThreadPoolExecutor(1,
                               1,
                               0L,
                               TimeUnit.MILLISECONDS,
                               new LinkedBlockingQueue<Runnable>()));
}
```
