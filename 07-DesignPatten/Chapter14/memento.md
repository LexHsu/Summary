备忘录模式
===

### 模式定义

备忘录模式是在不破坏封装的条件下，存储一个对象的状态，可在合适的时候把这个对象还原到之前的状态。备忘录模式常常与命令模式和迭代子模式一同使用。

### UML 类图



### 涉及角色

- 备忘录角色 Memento，用于存储另外一个对象内部状态的快照。

- 发起人角色 Originator，创建一个含有当前的内部状态的备忘录对象。使用备忘录对象存储其内部状态。

- 负责人角色 Caretaker，负责保存备忘录对象。

### 白箱备忘录模式

备忘录角色对任何对象都提供一个接口，即宽接口，备忘录角色的内部所存储的状态就对所有对象公开。因此这个实现又叫做“白箱实现”。
白箱实现将发起人角色的状态存储在一个大家都看得到的地方，因此是破坏封装性的。但是通过程序员自律，同样可以在一定程度上实现模式的大部分用意。因此白箱实现仍然是有意义的。

```java
public class Originator {

    private String state;

    /**
     * 工厂方法，返回一个新的备忘录对象
     */
    public Memento createMemento() {
        return new Memento(state);
    }

    /**
     * 将发起人恢复到备忘录对象所记载的状态
     */
    public void restoreMemento(Memento memento) {
        this.state = memento.getState();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        System.out.println("当前状态：" + this.state);
    }

}

public class Memento {

    private String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}

public class Caretaker {

    private Memento memento;
    /**
     * 备忘录的取值方法
     */
    public Memento retrieveMemento(){
        return this.memento;
    }
    /**
     * 备忘录的赋值方法
     */
    public void saveMemento(Memento memento){
        this.memento = memento;
    }
}

public class Client {

    public static void main(String[] args) {

        Originator o = new Originator();
        Caretaker c = new Caretaker();
        // 改变负责人对象的状态
        o.setState("On");
        // 创建备忘录对象，并将发起人对象的状态储存起来
        c.saveMemento(o.createMemento());
        // 修改发起人的状态
        o.setState("Off");
        // 恢复发起人对象的状态
        o.restoreMemento(c.retrieveMemento());

        System.out.println(o.getState());
    }

}
```

首先将发起人对象的状态设置成 On，并创建一个备忘录对象将这个状态存储起来；然后将发起人对象的状态改成 Off；
最后又将发起人对象恢复到备忘录对象所存储起来的状态，即 On 状态。

### 黑箱备忘录模式
