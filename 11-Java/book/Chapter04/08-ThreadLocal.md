ThreadLocal
===

ThreadLocal是用于存放线程局部变量的容器。

一个序列号生成器的程序，可能同时会有多个线程并发访问它，要保证每个线程得到的序列号都是自增的，而不能相互干扰。

先定义一个接口：

```java
public interface Sequence {

    int getNumber();
}
```

每次调用 getNumber() 方法可获取一个序列号，下次再调用时，序列号会自增。

再做一个线程类：

```java
public class ClientThread extends Thread {

    private Sequence sequence;

    public ClientThread(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " => " + sequence.getNumber());
        }
    }
}
```

在线程中连续输出三次线程名与其对应的序列号。

我们先不用 ThreadLocal，来做一个实现类吧。

```java
public class SequenceA implements Sequence {

    private static int number = 0;

    public int getNumber() {
        number = number + 1;
        return number;
    }

    public static void main(String[] args) {
        Sequence sequence = new SequenceA();

        ClientThread thread1 = new ClientThread(sequence);
        ClientThread thread2 = new ClientThread(sequence);
        ClientThread thread3 = new ClientThread(sequence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

// 序列号初始值是0，在 main() 方法中模拟了三个线程，运行后结果如下：

Thread-0 => 1
Thread-0 => 2
Thread-0 => 3
Thread-2 => 4
Thread-2 => 5
Thread-2 => 6
Thread-1 => 7
Thread-1 => 8
Thread-1 => 9
```

由于线程启动顺序是随机的，所以并不是0、1、2这样的顺序，这个好理解。为什么当 Thread-0 输出了1、2、3之后，而 Thread-2 却输出了4、5、6呢？线程之间竟然共享了 static 变量！这就是所谓的“非线程安全”问题了。

那么如何来保证“线程安全”呢？对应于这个案例，就是说不同的线程可拥有自己的 static 变量，如何实现呢？下面看看另外一个实现吧。

```java
public class SequenceB implements Sequence {

    private static ThreadLocal<Integer> numberContainer = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public int getNumber() {
        numberContainer.set(numberContainer.get() + 1);
        return numberContainer.get();
    }

    public static void main(String[] args) {
        Sequence sequence = new SequenceB();

        ClientThread thread1 = new ClientThread(sequence);
        ClientThread thread2 = new ClientThread(sequence);
        ClientThread thread3 = new ClientThread(sequence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```

通过 ThreadLocal 封装了一个 Integer 类型的 numberContainer 静态成员变量，并且初始值是0。再看 getNumber() 方法，首先从 numberContainer 中 get 出当前的值，加1，随后 set 到 numberContainer 中，最后将 numberContainer 中 get 出当前的值并返回。

是不是很恶心？但是很强大！确实稍微饶了一下，我们不妨把 ThreadLocal 看成是一个容器，这样理解就简单了。所以，这里故意用 Container 这个单词作为后缀来命名 ThreadLocal 变量。

运行结果：

```java
Thread-0 => 1
Thread-0 => 2
Thread-0 => 3
Thread-2 => 1
Thread-2 => 2
Thread-2 => 3
Thread-1 => 1
Thread-1 => 2
Thread-1 => 3
```

每个线程相互独立了，同样是 static 变量，对于不同的线程而言，它没有被共享，而是每个线程各一份，这样也就保证了线程安全。 也就是说，TheadLocal 为每一个线程提供了一个独立的副本！

搞清楚 ThreadLocal 的原理之后，有必要总结一下 ThreadLocal 的 API，其实很简单。

```
public void set(T value)：将值放入线程局部变量中
public T get()：从线程局部变量中获取值
public void remove()：从线程局部变量中移除值（有助于 JVM 垃圾回收）
protected T initialValue()：返回线程局部变量中的初始值（默认为 null）
```

为什么 initialValue() 方法是 protected 的呢？就是为了提醒程序员们，这个方法是要你们来实现的，请给这个线程局部变量一个初始值吧。

了解了原理与这些 API，其实想想 ThreadLocal 里面不就是封装了一个 Map 吗？自己都可以写一个 ThreadLocal 了，尝试一下吧。

```java
public class MyThreadLocal<T> {

    private Map<Thread, T> container = Collections.synchronizedMap(new HashMap<Thread, T>());

    public void set(T value) {
        container.put(Thread.currentThread(), value);
    }

    public T get() {
        Thread thread = Thread.currentThread();
        T value = container.get(thread);
        if (value == null && !container.containsKey(thread)) {
            value = initialValue();
            container.put(thread, value);
        }
        return value;
    }

    public void remove() {
        container.remove(Thread.currentThread());
    }

    protected T initialValue() {
        return null;
    }
}
```

以上完全山寨了一个 ThreadLocal，其中中定义了一个同步 Map（为什么要这样？请读者自行思考），代码应该非常容易读懂。

下面用这 MyThreadLocal 再来实现一把看看。

```java
public class SequenceC implements Sequence {

    private static MyThreadLocal<Integer> numberContainer = new MyThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public int getNumber() {
        numberContainer.set(numberContainer.get() + 1);
        return numberContainer.get();
    }

    public static void main(String[] args) {
        Sequence sequence = new SequenceC();

        ClientThread thread1 = new ClientThread(sequence);
        ClientThread thread2 = new ClientThread(sequence);
        ClientThread thread3 = new ClientThread(sequence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```

### ThreadLocal 使用案例

通过 ThreadLocal 存放 JDBC Connection，以达到事务控制的能力。如用户提出一个需求：当修改产品价格的时候，需要记录操作日志，什么时候做了什么事情。

数据库里就两张表：product 与 log，用两条 SQL 语句应该可以解决问题：

```
update product set price = ? where id = ?
insert into log (created, description) values (?, ?)
```

要确保这两条 SQL 语句必须在同一个事务里进行提交，否则有可能 update 提交了，但 insert 却没有提交。就会导致：“为什么产品价格改了，却看不到什么时候改的呢？”。

解决思路，首先，写一个 DBUtil 的工具类，封装了数据库的常用操作：

```java
public class DBUtil {
    // 数据库配置
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/demo";
    private static final String username = "root";
    private static final String password = "root";

    // 定义一个数据库连接
    private static Connection conn = null;

    // 获取连接
    public static Connection getConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭连接
    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

里面搞了一个 static 的 Connection，然后定义了一个接口，用于给逻辑层来调用：

```java
public interface ProductService {

    void updateProductPrice(long productId, int price);
}
根据用户提出的需求，我想这个接口完全够用了。根据 productId 去更新对应 Product 的 price，然后再插入一条数据到 log 表中。

其实业务逻辑也不太复杂，于是我快速地完成了 ProductService 接口的实现类：

public class ProductServiceImpl implements ProductService {

    private static final String UPDATE_PRODUCT_SQL = "update product set price = ? where id = ?";
    private static final String INSERT_LOG_SQL = "insert into log (created, description) values (?, ?)";

    public void updateProductPrice(long productId, int price) {
        try {
            // 获取连接
            Connection conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 关闭自动提交事务（开启事务）

            // 执行操作
            updateProduct(conn, UPDATE_PRODUCT_SQL, productId, price); // 更新产品
            insertLog(conn, INSERT_LOG_SQL, "Create product."); // 插入日志

            // 提交事务
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            DBUtil.closeConnection();
        }
    }

    private void updateProduct(Connection conn, String updateProductSQL, long productId, int productPrice) throws Exception {
        PreparedStatement pstmt = conn.prepareStatement(updateProductSQL);
        pstmt.setInt(1, productPrice);
        pstmt.setLong(2, productId);
        int rows = pstmt.executeUpdate();
        if (rows != 0) {
            System.out.println("Update product success!");
        }
    }

    private void insertLog(Connection conn, String insertLogSQL, String logDescription) throws Exception {
        PreparedStatement pstmt = conn.prepareStatement(insertLogSQL);
        pstmt.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
        pstmt.setString(2, logDescription);
        int rows = pstmt.executeUpdate();
        if (rows != 0) {
            System.out.println("Insert log success!");
        }
    }
}
```

代码的可读性还算不错吧？这里我用到了 JDBC 的高级特性 Transaction 了。暗自庆幸了一番之后，我想是不是有必要写一个客户端，来测试一下执行结果是不是我想要的呢？ 于是我偷懒，直接在 ProductServiceImpl 中增加了一个 main() 方法：

```java
public static void main(String[] args) {
    ProductService productService = new ProductServiceImpl();
    productService.updateProductPrice(1, 3000);
}
```

我想让 productId 为 1 的产品的价格修改为 3000。于是我把程序跑了一遍，控制台输出：
```
Update product success!
Insert log success!
```
应该是对了。作为一名专业的程序员，为了万无一失，我一定要到数据库里在看看。没错！product 表对应的记录更新了，log 表也插入了一条记录。这样就可以将 ProductService 接口交付给别人来调用了。

几个小时过去了，QA 妹妹开始骂我：“我靠！我才模拟了 10 个请求，你这个接口怎么就挂了？说是数据库连接关闭了！”。

听到这样的叫声，让我浑身打颤，立马中断了我的小视频，赶紧打开 IDE，找到了这个 ProductServiceImpl 这个实现类。好像没有 Bug 吧？但我现在不敢给她任何回应，我确实有点怕她的。

我突然想起，她是用工具模拟的，也就是模拟多个线程了！那我自己也可以模拟啊，于是我写了一个线程类：
```java
public class ClientThread extends Thread {

    private ProductService productService;

    public ClientThread(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        productService.updateProductPrice(1, 3000);
    }
}
```

我用这线程去调用 ProduceService 的方法，看看是不是有问题。此时，我还要再修改一下 main() 方法：
```java
// public static void main(String[] args) {
//     ProductService productService = new ProductServiceImpl();
//     productService.updateProductPrice(1, 3000);
// }

public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
        ProductService productService = new ProductServiceImpl();
        ClientThread thread = new ClientThread(productService);
        thread.start();
    }
}

// 模拟 10 个线程，我就不信那个邪了！运行异常：

Thread-1
Thread-3
Thread-5
Thread-7
Thread-9
Thread-0
Thread-2
Thread-4
Thread-6
Thread-8
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No operations allowed after connection closed.
at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
at com.mysql.jdbc.Util.handleNewInstance(Util.java:411)
at com.mysql.jdbc.Util.getInstance(Util.java:386)
at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:1015)
at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:989)
at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:975)
at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:920)
at com.mysql.jdbc.ConnectionImpl.throwConnectionClosedException(ConnectionImpl.java:1304)
at com.mysql.jdbc.ConnectionImpl.checkClosed(ConnectionImpl.java:1296)
at com.mysql.jdbc.ConnectionImpl.commit(ConnectionImpl.java:1699)
at com.smart.sample.test.transaction.solution1.ProductServiceImpl.updateProductPrice(ProductServiceImpl.java:25)
at com.smart.sample.test.transaction.ClientThread.run(ClientThread.java:18)
```

我靠！竟然在多线程的环境下报错了，果然是数据库连接关闭了。怎么回事呢？我陷入了沉思中。于是我 Copy 了一把那句报错信息，在百度、Google，还有 OSC 里都找了，解答实在是千奇百怪。

我突然想起，既然是跟 Connection 有关系，那我就将主要精力放在检查 Connection 相关的代码上吧。是不是 Connection 不应该是 static 的呢？我当初设计成 static 的主要是为了让 DBUtil 的 static 方法访问起来更加方便，用 static 变量来存放 Connection 也提高了性能啊。怎么搞呢？

于是我看到了 OSC 上非常火爆的一篇文章《ThreadLocal 那点事儿》，终于才让我明白了！原来要使每个线程都拥有自己的连接，而不是共享同一个连接，否则线程1有可能会关闭线程2的连接，所以线程2就报错了。一定是这样！

我赶紧将 DBUtil 给重构了：

```java
public class DBUtil {
    // 数据库配置
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/demo";
    private static final String username = "root";
    private static final String password = "root";

    // 定义一个用于放置数据库连接的局部线程变量（使每个线程都拥有自己的连接）
    private static ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();

    // 获取连接
    public static Connection getConnection() {
        Connection conn = connContainer.get();
        try {
            if (conn == null) {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connContainer.set(conn);
        }
        return conn;
    }

    // 关闭连接
    public static void closeConnection() {
        Connection conn = connContainer.get();
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connContainer.remove();
        }
    }
}
```

我把 Connection 放到了 ThreadLocal 中，这样每个线程之间就隔离了，不会相互干扰了。

此外，在 getConnection() 方法中，首先从 ThreadLocal 中（也就是 connContainer 中） 获取 Connection，如果没有，就通过 JDBC 来创建连接，最后再把创建好的连接放入这个 ThreadLocal 中。可以把 ThreadLocal 看做是一个容器，一点不假。

同样，我也对 closeConnection() 方法做了重构，先从容器中获取 Connection，拿到了就 close 掉，最后从容器中将其 remove 掉，以保持容器的清洁。

这下应该行了吧？我再次运行 main() 方法：
```java
Thread-0
Thread-2
Thread-4
Thread-6
Thread-8
Thread-1
Thread-3
Thread-5
Thread-7
Thread-9
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
Update product success!
Insert log success!
```java
