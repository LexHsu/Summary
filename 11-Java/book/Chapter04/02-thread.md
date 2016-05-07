主线程等待子线程结束
===

### join 方式

```java
// 创建一个线程，假设完成需要 5 秒
public class TestThread extends Thread {
    public void run() {
        System.out.println(this.getName() + "子线程开始");
        try {  
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + "子线程结束");
    }  
}  


public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        for(int i = 0; i < 3; i++) {
            Thread thread = new TestThread();
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {  
                e.printStackTrace();
            }  
        }

        long end = System.currentTimeMillis();
        System.out.println("子线程执行时长：" + (end - start));
    }
}

// 执行结果：

// Thread-0子线程开始
// Thread-0子线程结束
// Thread-1子线程开始
// Thread-1子线程结束
// Thread-2子线程开始
// Thread-2子线程结束
// 子线程执行时长：15000
```

上述三个子线程并不是并发执行，若要子线程之间并发执行，那么需要在所有子线程 start() 后，再执行所有子线程的 join() 方法。

```java
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<Thread> list = new ArrayList<Thread>();
        for(int i = 0; i < 3; i++) {
            Thread thread = new TestThread();
            thread.start();
            list.add(thread);
        }

        try {
            for(Thread thread : list) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("子线程执行时长：" + (end - start));
    }
}

// 执行结果：

// Thread-0子线程开始
// Thread-1子线程开始
// Thread-2子线程开始
// Thread-0子线程结束
// Thread-2子线程结束
// Thread-1子线程结束
// 子线程执行时长：5000
```

### CountDownLatch 方式

在主线程中，创建一个初始值为 3 的 CountDownLatch，并传给每个子线程，在每个子线程最后调用 countDown() 方法对计数器减 1，
主线程调用 await() 方法等待 3 个子线程执行完成。

```java
public class TestThread extends Thread {
    private CountDownLatch countDownLatch;
    public TestThread(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        System.out.println(this.getName() + "子线程开始");
        try {
            // 子线程休眠五秒
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(this.getName() + "子线程结束");
            // 计数器减1
            countDownLatch.countDown();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 创建一个初始值为5的倒数计数器  
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for(int i = 0; i < 3; i++) {
            Thread thread = new TestThread(countDownLatch);
            thread.start();
        }

        try {
            // 阻塞当前线程，直到倒数计数器倒数到0
            countDownLatch.await();  
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("子线程执行时长：" + (end - start));
    }
}

// 执行结果：
// Thread-0子线程开始
// Thread-2子线程开始
// Thread-1子线程开始
// Thread-2子线程结束
// Thread-1子线程结束
// Thread-0子线程结束
// 子线程执行时长：5000
```

await() 方法还有一个实用的重载方法：`public boolean await(long timeout, TimeUnit unit)`，设置超时时间。
如要设置超时时间为 10 秒，到了 10 秒无论是否倒数完成到 0，都会不再阻塞主线程。返回值是 boolean 类型，如果是超时返回 false，如果计数到达 0 没有超时返回 true。

```java
boolean timeoutFlag = countDownLatch.await(10, TimeUnit.SECONDS);  
if (timeoutFlag) {
    System.out.println("所有子线程执行完成");
} else {
    System.out.println("超时");
}
```

### 线程池

ExecutorService中的 `boolean awaitTermination(long timeout, TimeUnit unit)`，
即阻塞主线程，等待线程池的所有线程执行完成，参数设置一个超时时间，如果超时返回 false，否则返回 true。
与 CountDownLatch 的 `public boolean await(long timeout,TimeUnit unit)` 类似，
ExecutorService 没有类似 CountDownLatch 的无参 await() 方法，只可通过 awaitTermination 实现主线程等待线程池。

```java
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 创建一个同时允许两个线程并发执行的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 3; i++) {
            Thread thread = new TestThread();
            executor.execute(thread);
        }
        executor.shutdown();

        try {
            // 本例中，每隔10秒循环一次，直到执行完成
            while (!executor.awaitTermination(10, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("子线程执行时长：" + (end - start));
    }
}

// 执行结果：
// Thread-0子线程开始
// Thread-1子线程开始
// Thread-0子线程结束
// Thread-2子线程开始
// Thread-1子线程结束
// Thread-2子线程结束
// 子线程执行时长：5000
```
