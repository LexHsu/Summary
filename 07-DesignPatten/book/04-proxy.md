代理模式
===

### 模式定义

代理模式是为其他对象提供一种代理以控制这个对象的访问。 在某些情况下，一个客户不愿或不能直接引用另一个对象，而代理对象可在客户端和目标对象之间起到中介作用。

### UML 类图

![Alt text](img/proxy.png)

```java
// Subject类，定义了 RealSubject 和 Proxy 的共用接口，这样就在任何使用 RealSubject 的地方都可以使用 Proxy
public interface Subject
{
    public void request();
}
// RealSubject类，定义Proxy所代表的真实实体。
public class RealSubject implements Subject
{
    public void request()
    {
        System.out.println("真实的请求");
    }
}
// Proxy 类，保存一个引用使用代理可以访问实体，并提供一个与 Subject 的接口相同的接口，这样的代理可以用替代实体
public class Proxy implements Subject
{
    RealSubject    realSubject;

    public void request()
    {
        if (null == realSubject)
        {
            realSubject = new RealSubject();
        }
        realSubject.request();
    }
}
// 客户端代码
public class Test
{
    public static void main(String[] args)
    {
        Proxy proxy = new Proxy();
        proxy.request();
    }
}

```

### 代理模式应用

第一种，远程代理，也就是为一个对象在不同的地址空间提供局部代表。可以隐藏一个对象存在于不同地址空间的事实。WebService在.NET中的应用就是
远程代理，在应用程序的项目中加入一个 Web 引用，引用一个 WebService，此时会在项目中生成一个 WebReference 的文件夹和一些文件，其实它们就是代理，这就使得客户端程序调用代理就可以解决远程访问的问题。

第二种应用是虚拟代理，是根据需要创建开销很大的对象。通过它来存放实例化需要很长时间的真实对象。这样就可以达到性能的最优化，比如说你打开一个很大的HTML网页时，里面可能有很多的文字和图片，但你还是可以很快地打开它，此时你所看到的所有文字，但图片却是一张一张地下载后，才能看到的。那些未打开的图片框，就是通过虚拟代理来替代了真实的图片，此时代理存储了真实图片的路径和尺寸。

第三种应用是安全代理，用来控制真实对象访问时的权限。一般用于对象应该有不同的访问权限的时候。

第四种是智能指引，是指当调用真实对象时，代理处理另外一些事。如计算真实对象的引用次数，这样当该对象没有引用时，可以自动释放它；或当第一次引用一个持久对象时，将它装入内存；或在访问一个实际对象前，检查是否已经锁定它，以确保其他对象不能改变它。它们都是通过代理在访问一个对象时附加一些内务处理。

### 实例讲解

以房东找中介出租房屋为例。代理模式涉及具体角色如下：

- 抽象角色(租房子)

声明真实对象和代理对象的共同接口。

- 代理角色（中介）

代理对象角色内部含有对真实对象的引用，从而可以操作真实对象，同时代理对象提供与真实对象相同的接口以便在任何时刻都能代替真实对象。同时，代理对象可在执行真实对象操作时附加其他操作，相当于对真实对象进行封装。

- 真实角色（房东）

代理角色所代表的真实对象，是最终要引用的对象。

### 实例代码

```java
/**
 * 租房接口
 */
public interface Rentable {
    public void rent();
}

```

代理对象和真实对象必须均实现该接口。

```java
/**
 * 房东
 */
public class Landlord implements Rentable {

    @Override
    public void rent() {
        System.out.println("i have a room for rent.");
    }

}
```

中介代理人。

```java
public class Agent implements Rentable {

    private Landlord mLandlord;

    @Override
    public void rent() {
        if (mLandlord == null) {
            mLandlord = new Landlord();
        }

        mLandlord.rent();
        // do other things
    }
}
```

代理对象持有真实对象的引用，除操作真实对象外，也可添加一些自身的业务逻辑，如收取佣金等。Client 调用代理对象如下：

```java
public class Client {

    public static void main(String[] args) {
        Rentable agent = new Agent();
        agent.rent();
    }
}
```
