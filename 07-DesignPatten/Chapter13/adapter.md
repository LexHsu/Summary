适配器模式
===

### 模式定义

将一个类的接口转换成客户希望的另一个接口，使得原本由于接口不兼容而无法一起工作的类可以一起工作。

适配器模式有类的适配器模式和对象的适配器模式两种不同的形式。

### 类适配器模式

- UML 类图

在上图中可以看出，Adaptee 类并没有 sampleOperation2() 方法，而客户端则期待这个方法。
为使客户端能够使用 Adaptee 类，提供一个中间环节，即类 Adapter，把 Adaptee 的 API 与 Target 类的 API 衔接起来。
Adapter 与 Adaptee 是继承关系，这决定了这个适配器模式是类的。

模式所涉及的角色有：

- 目标角色（Target）：这就是所期待得到的接口。注意：由于这里讨论的是类适配器模式，因此目标不可以是类。
- 源角色（Adaptee）：现在需要适配的接口。
- 适配器角色（Adapter）：适配器类是本模式的核心。适配器把源接口转换成目标接口。显然，这一角色不可以是接口，而必须是具体类。

### 对象适配器模式

与类的适配器模式一样，对象的适配器模式把被适配的类的 API 转换成为目标类的API，与类的适配器模式不同的是，
对象的适配器模式不是使用继承关系连接到 Adaptee 类，而是使用委派关系连接到Adaptee类。

```java

// Target 类，客户所期待的接口，目标可以是具体的或抽象的类，也可以是接口
public class Target {
    public void request() {
        System.out.println("普通请求！");
    }
}
// Adaptee 类，需要适配的类
public class Adaptee extends Target {
    public void specificRequest() {
        System.out.println("特殊请求！");
    }
}
// Adapter 类，在内部包装一个 Adaptee 对象，把源接口转换成目标接口
public class Adapter extends Target {
    private Adaptee adaptee = new Adaptee();

    public void request() {
        adaptee.specificRequest();
    }
}

public class Client {
    public static void main(String[] args) {
        Target target = new Adapter();
        target.request();
    }
}
```
