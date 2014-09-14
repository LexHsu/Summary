桥接模式
===

### 模式定义

将抽象化(Abstraction)与实现化(Implementation)脱耦，使得二者可以独立地变化。

### UML 类图

![Alt text](img/18bridge.png)

### 代码示例

```java
abstract class Implementor {
    public abstract void operation();
}

class ConcreteImplementorA extends Implementor {

    @Override
    public void operation() {
        System.out.println("A 具体实现");
    }
}

class ConcreteImplementorB extends Implementor {

    @Override
    public void operation() {
        System.out.println("B 具体实现");
    }
}

class Abstraction {
    protected Implementor implementor;

    public Implementor setImplementor(Implementor imp) {
        this.implementor = imp;
    }

    public void operation() {
        implementor.operation();
    }
}

class RefinedAbstraction extends Abstraction {

    @Override
    public void operation() {
        super.operation();
    }

}

public class Client {

    public static void main(String[] args) {
        Abstraction mAbstraction = new RefinedAbstraction();

        mAbstraction.setImplementor(new ConcreteImplementorA());
        mAbstraction.operation();

        mAbstraction.setImplementor(new ConcreteImplementorB());
        mAbstraction.operation();
    }

}
```
