面向对象设计原则
===

通过剖析 Android 网络框架 Volley，了解六大面向对象设计原则，Volley 相关资料：

- [Volley 源码](http://codekk.com/open-source-project-analysis/detail/Android/grumoon/Volley%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90)
- [Volley 源码剖析](https://github.com/mcxiaoke/android-volley)
- [Android 网络框架系列](http://blog.csdn.net/column/details/simple-net.html)


### 单一职责原则 Single Responsibility Principle

就一个类而言，应该仅有一个引起它变化的原因。简单来说一个类只做一件事。类中方法所做的工作必须高度相关，即高内聚。
这个设计原则备受争议，毕竟界定类的职责无法标准化。
在 Volley 中，能够体现 SRP 原则的就是 HttpStack 类。HttpStack 定义了一个执行网络请求的接口，代码如下 :

```java
/**
* An HTTP stack abstraction.
*/
public interface HttpStack {
    /**
    * 执行Http请求,并且返回一个HttpResponse
    */
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
    throws IOException, AuthFailureError;

}
```

优点：

1. 复杂性低
3. 可维护性高


### 里氏替换原则 Liskov Substitution Principle

简述

面向对象的语言的三大特点是继承、封装、多态，里氏替换原则就是依赖于继承、多态这两大特性。里氏替换原则简单来说就是所有引用基类的地方必须能透明地使用其子类的对象。
通俗点讲，只要父类能出现的地方子类就可以出现，而且替换为子类也不会产生任何错误或异常，使用者可能根本就不需要知道是父类还是子类。

以 HttpStack 为例，Volley 定义了 HttpStack 来表示执行网络请求这个抽象概念。在执行网络请求时，
只需要定义一个 HttpStack 对象，然后调用 performRequest 即可。至于 HttpStack 的具体实现由更高层的调用者给出:

```java
public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

String userAgent = "volley/0";

// 1、构造HttpStack对象
    if (stack == null) {
        if (Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
        }
    }
    // 2、将HttpStack对象传递给Network对象
    Network network = new BasicNetwork(stack);
    // 3、将network对象传递给网络请求队列
    RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
    queue.start();

    return queue;
}

// BasicNetwork的代码如下:

/**
* A network performing Volley requests over an {@link HttpStack}.
*/
public class BasicNetwork implements Network {
    // HttpStack 抽象对象
    protected final HttpStack mHttpStack;

    protected final ByteArrayPool mPool;

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }


    public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
        mHttpStack = httpStack;
        mPool = pool;
    }
}
```

BasicNetwork 构造函数依赖的是 HttpStack 抽象接口，任何实现了 HttpStack 接口的类型都可以作为参数传递给 BasicNetwork 用以执行网络请求。
这就是所谓的里氏替换原则，任何父类出现的地方子类都可以出现，保证了可扩展性。任何实现 HttpStack 接口的类的对象都可以传递给 BasicNetwork 实现网络请求的功能，
这样 Volley 执行网络请求的方法就有很多种可能性，而不是只有 HttpClient 和 HttpURLConnection。

优点

1. 代码共享，减少创建类的工作量，每个子类都拥有父类的方法和属性；
2. 提高代码的重用性；
3. 提高代码的可扩展性，实现父类的方法就可以“为所欲为”了，很多开源框架的扩展接口都是通过继承父类来完成的；

缺点

1. 继承是侵入性的。只要继承，就必须拥有父类的所有属性和方法；
2. 降低代码的灵活性。子类必须拥有父类的属性和方法，让子类自由的世界中多了些约束；
3. 增强了耦合性。当父类的常量、变量和方法被修改时，必需要考虑子类的修改，而且在缺乏规范的环境下，这种修改可能带来非常糟糕的结果——大片的代码需要重构。

### 依赖倒转（置）原则 Dependence Inversion Principle

1. 高层模块不应该依赖低层模块，两者都应该依赖其抽象；
2. 抽象不应该依赖细节，细节应该依赖抽象。

Java 中，抽象就是指接口或抽象类，两者都是不能直接被实例化的；细节就是实现类，实现接口或继承抽象类而产生的类就是细节，其特点就是可以直接被实例化。
依赖倒置原则在 Java 语言中的表现就是：模块间的依赖通过抽象发生，实现类之间不发生直接的依赖关系，其依赖关系是通过接口或抽象类产生的。
即面向接口编程，或者说是面向抽象编程，这里的抽象指的是接口或者抽象类。面向接口编程是面向对象精髓之一。

上例中的 BasicNetwork 实现类依赖于 HttpStack 接口（抽象），而不依赖于 HttpClientStack 与 HurlStack 实现类（细节） ，这就是典型的依赖倒置原则的体现。
如果 BasicNetwork 直接依赖 HttpClientStack，那么 HurlStack 就无法传递，除非 HurlStack 继承自 HttpClientStack。但这么设计明显不符合逻辑，HurlStack 与 HttpClientStack 并没有任何的 is-a 的关系，而且即使有也不能这么设计，因为 HttpClientStack 是一个具体类而不是抽象，如果 HttpClientStack 作为 BasicNetwork 构造函数的参数，那么以为这后续的扩展都需要继承自 HttpClientStack，明显是糟糕的设计。

优点

1. 可扩展性好；
2. 耦合度低；

### 开闭原则 Open-Close Principle

简述

开闭原则是指一个软件实体，如类、模块和函数应该对扩展开放，对修改关闭。
在软件的生命周期内，因为变化、升级和维护等原因需要对软件原有代码进行修改时，可能会给旧代码中引入错误。
因此当软件需要变化时，我们应该尽量通过扩展的方式来实现变化，而不是通过修改已有的代码来实现。

Volley 中，开闭原则体现得比较好的是 Request 类族的设计。在开发 C/S 应用时，服务器返回的数据格式多种多样，有字符串类型、xml、json 等。
而解析服务器返回的 Response 的原始数据类型则是通过 Request 类来实现的，这样就使得 Request 类对于服务器返回的数据格式有良好的扩展性，即 Request 的可变性太大。
如返回的数据格式为 Json，那么使用 JsonObjectRequest 请求来获取数据，其会将结果转成 JsonObject 对象，JsonObjectRequest 的核心实现如下：

```java
/**
* A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
* optional {@link JSONObject} to be passed in as part of the request body.
*/
public class JsonObjectRequest extends JsonRequest<JSONObject> {

    // 代码省略
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
            HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
```

JsonObjectRequest 通过实现 Request 抽象类的 parseNetworkResponse 解析服务器返回的结果，这里将结果转换为 JSONObject，并且封装到 Response 类中。
如 Volley 添加对图片请求的支持，即 ImageLoader( 已内置 )。这时请求返回的数据是 Bitmap 图片。
因此需要在该类型的 Request 得到的结果是 Request，但支持一种数据格式不能通过修改源码的形式，这样可能会为旧代码引入错误。
但此时我们的开闭原则就很重要了，对扩展开放，对修改关闭。看看 Volley 是如何做的。

```java
/**
* A canned request for getting an image at a given URL and calling
* back with a decoded Bitmap.
*/
public class ImageRequest extends Request<Bitmap> {
    // 代码省略

    // 将结果解析成Bitmap，并且封装套Response对象中
    @Override
    protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
        // Serialize all decode on a global lock to reduce concurrent heap usage.
        synchronized (sDecodeLock) {
            try {
                return doParse(response);
            } catch (OutOfMemoryError e) {
                VolleyLog.e("Caught OOM for %d byte image, url=%s", response.data.length, getUrl());
                return Response.error(new ParseError(e));
            }
        }
    }

    /**
    * The real guts of parseNetworkResponse. Broken out for readability.
    */
    private Response<Bitmap> doParse(NetworkResponse response) {
        byte[] data = response.data;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
        // 解析Bitmap的相关代码,在此省略

        if (bitmap == null) {
            return Response.error(new ParseError(response));
        } else {
            return Response.success(bitmap, HttpHeaderParser.parseCacheHeaders(response));
        }
    }
}
```

需要添加某种数据格式的 Request 时，只需要继承自 Request 类，并且实现相应的方法即可。这样通过扩展的形式来应对软件的变化或者说用户需求的多样性，即避免了破坏原有系统，又保证了软件系统的可扩展性。

优点

1. 增加稳定性
2. 可扩展性高

### 接口隔离原则 Interface Segregation Principle

客户端不应该依赖它不需要的接口；一个类对另一个类的依赖应该建立在最小的接口上。根据接口隔离原则，当一个接口太大时，需分割成一些更细小的接口，使用该接口的客户端仅需知道与之相关的方法即可。

在 Volley 的网络队列中会对请求进行排序。其使用 PriorityBlockingQueue 来维护网络请求队列，PriorityBlockingQueue 需要调用 Request 类的 compareTo 方法进行排序。
PriorityBlockingQueue 其实只需要调用 Request 类的排序接口 compareTo，其他接口不需要。

```java
public abstract class Request<T> implements Comparable<Request<T>> {

    /**
    * 排序方法,PriorityBlockingQueue只需要调用元素的compareTo即可进行排序
    */
    @Override
    public int compareTo(Request<T> other) {
        Priority left = this.getPriority();
        Priority right = other.getPriority();

        return left == right ?
        this.mSequence - other.mSequence :
        right.ordinal() - left.ordinal();
    }
}
```

PriorityBlockingQueue 类相关代码 :

```java
public class PriorityBlockingQueue<E> extends AbstractQueue<E>
implements BlockingQueue<E>, java.io.Serializable {

    // 代码省略

    // 添加元素的时候进行排序
    public boolean offer(E e) {
        if (e == null)
        throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        int n, cap;
        Object[] array;
        while ((n = size) >= (cap = (array = queue).length))
        tryGrow(array, cap);
        try {
            Comparator<? super E> cmp = comparator;
            // 没有设置 Comparator 则使用元素本身的 compareTo 方法进行排序
            if (cmp == null)
            siftUpComparable(n, e, array);
            else
            siftUpUsingComparator(n, e, array, cmp);
            size = n + 1;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
        return true;
    }

    private static <T> void siftUpComparable(int k, T x, Object[] array) {
        Comparable<? super T> key = (Comparable<? super T>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = array[parent];
            // 调用元素的 compareTo 方法进行排序
            if (key.compareTo((T) e) >= 0)
            break;
            array[k] = e;
            k = parent;
        }
        array[k] = key;
    }
}
```

在元素排序时，PriorityBlockingQueue 只需要知道元素是个 Comparable 对象即可，不需要知道这个对象是不是 Request 类以及这个类的其他接口。
它只需要排序，因此我只要知道它是实现了 Comparable 接口的对象即可，Comparable 就是它的最小接口，也是通过 Comparable 隔离了 PriorityBlockingQueue 类对 Request 类的其他方法的可见性。

优点

1. 降低耦合性
2. 提升代码的可读性
3. 隐藏实现细节


### 迪米特原则 Law of Demeter

迪米特法则也称为最少知识原则（Least Knowledge Principle），指一个对象应该对其他对象有最少的了解。
通俗地讲，一个类应该对自己需要耦合或调用的类知道得最少，这有点类似接口隔离原则中的最小接口的概念。
类的内部如何实现、如何复杂都与调用者或者依赖者没关系，调用者或者依赖者只需要知道他需要的方法即可。
类与类之间的关系越密切，耦合度越大，当一个类发生改变时，对另一个类的影响也越大。

迪米特法则还有一个英文解释是: Only talk to your immedate friends（ 只与直接的朋友通信。）什么叫做直接的朋友呢？每个对象都必然会与其他对象有耦合关系，两个对象之间的耦合就成为朋友关系，这种关系的类型有很多，例如组合、聚合、依赖等。

Volley 中的 Response 缓存接口的设计。

```java
/**
* An interface for a cache keyed by a String with a byte array as data.
*/
public interface Cache {
    /**
    * 获取缓存
    */
    public Entry get(String key);

    /**
    * 添加一个缓存元素
    */
    public void put(String key, Entry entry);

    /**
    * 初始化缓存
    */
    public void initialize();

    /**
    * 标识某个缓存过期
    */
    public void invalidate(String key, boolean fullExpire);

    /**
    * 移除缓存
    */
    public void remove(String key);

    /**
    * 清空缓存
    */
    public void clear();

}
```

Cache 接口定义了缓存类需要实现的最小接口，依赖缓存类的对象只需要知道这些接口即可。
例如缓存的具体实现类 DiskBasedCache，该缓存类将 Response 序列化到本地,这就需要操作 File 以及相关的类。代码如下 :


```java
public class DiskBasedCache implements Cache {

    /** Map of the Key, CacheHeader pairs */
    private final Map<String, CacheHeader> mEntries =
    new LinkedHashMap<String, CacheHeader>(16, .75f, true);

    /** The root directory to use for the cache. */
    private final File mRootDirectory;

    public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        mRootDirectory = rootDirectory;
        mMaxCacheSizeInBytes = maxCacheSizeInBytes;
    }

    public DiskBasedCache(File rootDirectory) {
        this(rootDirectory, DEFAULT_DISK_USAGE_BYTES);
    }

    // 代码省略
}
```
在这里，Volley 的直接朋友就是 DiskBasedCache，间接朋友就是 mRootDirectory、mEntries 等。
Volley 只需要直接和 Cache 类交互即可，并不需要知道 File、mEntries 等对象的存在。这就是迪米特原则，尽量少的知道对象的信息，只与直接的朋友交互。

优点

1. 降低复杂度
2. 降低耦合度
3. 增加稳定性


### 依赖注入

依赖注入有个经典的解释：5分钱的东西，取了一个25美元的名字。

其实很简单，当一个对象依赖另外一个对象的时候，不自己创建，也不调用工厂创建。而是通过构造函数参数，或通过 setter 传入，即为依赖注入。

```java
// 自己构造实例
public class Person {
   //arm = new Arm();
   arm = Factory.getArm();
}

// 依赖反转
public class Person {
    public Person(Arm a) {
        this.arm = a;  
    }

}
```

更加实际一点的案例。你的网站需要返回图书列表，需要通过一个数据库连接来获取。

```java
// 依赖注入方式
m.Get("/booklist", func(db DB) string {
    return db.getBookList().String()
})

// 自己创建方式
m.Get("/booklist", func() string {
    db = new DB(url, port, usr, password)
    return db.getBookList().String()
})
```

依赖注入还有一个 250 美元的名字“依赖反转”。其实也一样，本来是自上而下的依赖关系，越上层的组件对下层越是依赖严重。改为不依赖下层，而是依赖一个控制中心。

##### 1. 依赖

如果在 Class A 中，有 Class B 的实例，则称 Class A 对 Class B 有一个依赖。例如下面类 Human 中用到一个 Father 对象，我们就说类 Human 对类 Father 有一个依赖。

```java
public class Human {
    ...
    Father father;
    ...
    public Human() {
        father = new Father();
    }
}
```
仔细看这段代码我们会发现存在一些问题：

1. 如果现在要改变 father 生成方式，如需要用`new Father(String name)`初始化 father，需要修改 Human 代码；
2. 如果想测试不同 Father 对象对 Human 的影响很困难，因为 father 的初始化被写死在了 Human 的构造函数中；
3. 如果`new Father()`过程非常缓慢，单测时我们希望用已经初始化好的 father 对象 Mock 掉这个过程也很困难。

##### 2. 依赖注入

上面将依赖在构造函数中直接初始化是一种 Hard init 方式，弊端在于两个类不够独立，不方便测试。我们还有另外一种 Init 方式，如下：

```java
public class Human {
    ...
    Father father;
    ...
    public Human(Father father) {
        this.father = father;
    }
}
```

上面代码中，我们将 father 对象作为构造函数的一个参数传入。在调用 Human 的构造方法之前外部就已经初始化好了 Father 对象。**像这种非自己主动初始化依赖，而通过外部来传入依赖的方式，我们就称为依赖注入。**  
现在我们发现上面 1 中存在的两个问题都很好解决了，简单的说依赖注入主要有两个好处：
1. 解耦，将依赖之间解耦。
2. 因为已经解耦，所以方便做单元测试，尤其是 Mock 测试。

##### 3. Java 中的依赖注入

依赖注入的实现有多种途径，而在 Java 中，使用注解是最常用的。通过在字段的声明前添加 @Inject 注解进行标记，来实现依赖对象的自动注入。

```java
public class Human {
    ...
    @Inject Father father;
    ...
    public Human() {
    }
}
```

上面这段代码看起来很神奇：只是增加了一个注解，Father 对象就能自动注入了？这个注入过程是怎么完成的？

实质上，如果只是写了一个 @Inject 注解，Father 并不会被自动注入。还需要使用一个依赖注入框架，并进行简单的配置。现在 Java 语言中较流行的依赖注入框架有 [Google Guice](https://github.com/google/guice)、[Spring](http://projects.spring.io/spring-framework/) 等，而在 Android 上比较流行的有 [RoboGuice](https://github.com/roboguice/roboguice)、[Dagger](http://square.github.io/dagger/) 等。

【附】[Dagger 实现原理解析](https://github.com/android-cn/android-open-project-analysis/tree/master/dagger) 。
