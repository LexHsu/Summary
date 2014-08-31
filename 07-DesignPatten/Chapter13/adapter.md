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

```java
public interface Target {

    public void sampleOperation1();

    public void sampleOperation2();
}
```
上面给出的是目标角色的源代码，这个角色是以接口的形式实现，声明了两个方法。
而源角色 Adaptee 是一个具体类，它有一个 sampleOperation1() 方法，但是没有 sampleOperation2() 方法。

```java
public class Adaptee {
    public void sampleOperation1(){}
}
```

适配器角色 Adapter 扩展了 Adaptee，同时又实现了目标 Target 接口。
由于 Adaptee 没有提供 sampleOperation2() 方法，而目标接口又要求这个方法，因此适配器角色 Adapter 实现了这个方法。

```java
public class Adapter extends Adaptee implements Target {
    /**
     * 由于源类 Adaptee 没有方法 sampleOperation2()
     * 因此适配器补充上该方法
     */
    @Override
    public void sampleOperation2() {
        // 相关代码
    }

}
```

### 对象适配器模式

与类的适配器模式一样，对象的适配器模式把被适配的类的 API 转换成为目标类的API，与类的适配器模式不同的是，
对象的适配器模式不是使用继承关系连接到 Adaptee 类，而是使用委派关系连接到Adaptee类。

- UML 类图




```java
public interface Target {

    public void sampleOperation1();

    public void sampleOperation2();
}

public class Adaptee {

    public void sampleOperation1(){}

}

public class Adapter {
    private Adaptee adaptee;

    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }
    /**
     * 源类 Adaptee 有方法 sampleOperation1
     * 因此适配器类直接委派即可
     */
    public void sampleOperation1(){
        this.adaptee.sampleOperation1();
    }
    /**
     * 源类 Adaptee 没有方法 sampleOperation2
     * 因此由适配器类需要补充此方法
     */
    public void sampleOperation2(){
        // 相关代码
    }
}

```
