Future
===

在 Java 中，如果需要设定代码执行的最长时间,即超时，可以用 Java 线程池 ExecutorService 类配合 Future 异步接口实现。

Future模式可以这样来描述：一个任务，提交给了 Future 之后。可以继续做其他业务。
一段时间之后，可从 Future 接口取出结果。就相当于下了一张订货单，一段时间后可以拿着提订单来提货，这期间可以干别的任何事情。
其中 Future 接口就是订货单，真正处理订单的是 Executor 类。

Future接口是一个泛型接口，严格的格式应该是Future<V>，其中V代表了Future执行的任务返回值的类型。 Future接口的方法介绍如下：

```java
// 取消任务的执行。参数指定是否立即中断任务执行，或者等等任务结束
boolean cancel (boolean mayInterruptIfRunning)

//任务是否已经取消，任务正常完成前将其取消，则返回 true
boolean isCancelled ()

// 任务是否已经完成。需要注意的是如果任务正常终止、异常或取消，都将返回true
boolean isDone ()

// 等待任务执行结束，然后获得V类型的结果
// InterruptedException 线程被中断异常
// ExecutionException 任务执行异常
// CancellationException 任务被取消
V get () throws InterruptedException, ExecutionException

// 参数 timeout 指定超时时间，uint 指定时间的单位，在枚举类 TimeUnit 中有相关的定义。
// 如果计算超时，将抛出 TimeoutException
V get (long timeout, TimeUnit unit) throws
    InterruptedException, ExecutionException, TimeoutException
```

常使用 FutureTask 来处理耗时任务。FutureTask 类实现了 Runnable接口，因此可直接提交给 Executor 执行。如下例：

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
FutureTask<String> future =
       new FutureTask<String>(new Callable<String>() {
         public String call() {
           // 执行具体任务，这里返回值 String，可以为任意类型
       }});
executor.execute(future);
// 这里可继续处理其他业务
try {
    // 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
    result = future.get(5000, TimeUnit.MILLISECONDS);
} catch (InterruptedException e) {
    future.cancel(true);
} catch (ExecutionException e) {
    future.cancel(true);
} catch (TimeoutException e) {
    future.cancel(true);
} finally {
    executor.shutdown();
}
```

也可以使用 ExecutorService.submit() 方法来获得 Future 对象，submit 方法既支持 Callable 接口，也支持 Runnable 接口，示例如下：

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
FutureTask<String> future =　executor.submit(
   new Callable<String>() {
       public String call() {
      // 耗时任务
   }});
```
