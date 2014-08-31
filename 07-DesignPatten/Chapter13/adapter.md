适配器模式
===

### 模式定义

将一个类的接口转换成客户希望的另一个接口，使得原本由于接口不兼容而无法一起工作的类可以一起工作。

### UML 类图


模式所涉及的角色有：

- 目标角色（Target）：这就是所期待得到的接口。注意：由于这里讨论的是类适配器模式，因此目标不可以是类。
- 源角色（Adaptee）：现在需要适配的接口。
- 适配器角色（Adapter）：适配器类是本模式的核心。适配器把源接口转换成目标接口。显然，这一角色不可以是接口，而必须是具体类。

### 代码示例

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
