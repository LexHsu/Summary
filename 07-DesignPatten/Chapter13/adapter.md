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

### 类适配器和对象适配器的权衡

- 类适配器使用对象继承的方式，是静态的定义方式。
- 对象适配器使用对象组合的方式，是动态组合的方式。

- 对于类适配器，由于适配器直接继承了 Adaptee，使得适配器不能和 Adaptee 的子类一起工作，因为继承是静态的关系，当适配器继承了 Adaptee 后，就不可能再去处理 Adaptee 的子类了。
- 对于对象适配器，一个适配器可以把多种不同的源适配到同一个目标。换言之，同一个适配器可以把源类和它的子类都适配到目标接口。因为对象适配器采用的是对象组合的关系，只要对象类型正确，是不是子类都无所谓。

- 对于类适配器，适配器可以重定义Adaptee的部分行为，相当于子类覆盖父类的部分实现方法。
- 对于对象适配器，要重定义Adaptee的行为比较困难，这种情况下，需要定义Adaptee的子类来实现重定义，然后让适配器组合子类。虽然重定义Adaptee的行为比较困难，但是想要增加一些新的行为则方便的很，而且新增加的行为可同时适用于所有的源。

- 对于类适配器，仅仅引入了一个对象，并不需要额外的引用来间接得到Adaptee。
- 对于对象适配器，需要额外的引用来间接得到Adaptee。

建议尽量使用对象适配器的实现方式，多用合成/聚合、少用继承。当然，具体问题具体分析，根据需要来选用实现方式，最适合的才是最好的。

### 适配器模式的优点

- 更好的复用性

系统需要使用现有的类，而此类的接口不符合系统的需要。那么通过适配器模式就可以让这些功能得到更好的复用。

- 更好的扩展性

在实现适配器功能的时候，可以调用自己开发的功能，从而自然地扩展系统的功能。
