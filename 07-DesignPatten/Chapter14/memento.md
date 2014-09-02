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

Memento 对象给 Originator 角色对象提供一个宽接口，而为其他对象提供一个窄接口，即黑箱实现。

```java
public class Originator {

    private String state;

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
        System.out.println("赋值状态：" + state);
    }

    /**
     * 工厂方法，返还一个新的备忘录对象
     */
    public MementoIF createMemento(){
        return new Memento(state);
    }

    /**
     * 发起人恢复到备忘录对象记录的状态
     */
    public void restoreMemento(MementoIF memento) {
        this.setState(((Memento)memento).getState());
    }

    private class Memento implements MementoIF{

        private String state;
        /**
         * 构造方法
         */
        private Memento(String state){
            this.state = state;
        }

        private String getState() {
            return state;
        }
        private void setState(String state) {
            this.state = state;
        }
    }
}

public interface MementoIF {
}

public class Caretaker {

    private MementoIF memento;

    /**
     * 备忘录取值方法
     */
    public MementoIF retrieveMemento(){
        return memento;
    }

    /**
     * 备忘录赋值方法
     */
    public void saveMemento(MementoIF memento){
        this.memento = memento;
    }
}

public class Client {

    public static void main(String[] args) {
        Originator o = new Originator();
        Caretaker c = new Caretaker();
        // 改变负责人对象的状态
        o.setState("On");
        // 创建备忘录对象，并将发起人对象的状态存储起来
        c.saveMemento(o.createMemento());
        // 修改发起人对象的状态
        o.setState("Off");
        // 恢复发起人对象的状态
        o.restoreMemento(c.retrieveMemento());
    }

}
```

### 多重检查点

前面所给出的白箱和黑箱的示意性实现都是只存储一个状态的简单实现，也可以叫做只有一个检查点。常见的系统往往需要存储不止一个状态，而是需要存储多个状态，或者叫做有多个检查点。
备忘录模式可以将发起人对象的状态存储到备忘录对象里面，备忘录模式可以将发起人对象恢复到备忘录对象所存储的某一个检查点上。下面给出一个示意性的、有多重检查点的备忘录模式的实现。

```java
public class Originator {

    private List<String> states;
    //检查点指数
    private int index;
    /**
     * 构造函数
     */
    public Originator() {
        states = new ArrayList<String>();
        index = 0;
    }
    /**
     * 工厂方法，返还一个新的备忘录对象
     */
    public Memento createMemento() {
        return new Memento(states , index);
    }
    /**
     * 将发起人恢复到备忘录对象记录的状态上
     */
    public void restoreMemento(Memento memento) {
        states = memento.getStates();
        index = memento.getIndex();
    }
    /**
     * 状态的赋值方法
     */
    public void setState(String state) {
        states.add(state);
        index++;
    }
    /**
     * 辅助方法，打印所有状态
     */
    public void printStates() {

        for(String state : states) {
            System.out.println(state);
        }
    }
}

public class Memento {

    private List<String> states;
    private int index;
    /**
     * 构造函数
     */
    public Memento(List<String> states , int index) {
        this.states = new ArrayList<String>(states);
        this.index = index;
    }
    public List<String> getStates() {
        return states;
    }
    public int getIndex() {
        return index;
    }

}

public class Caretaker {

    private Originator o;
    private List<Memento> mementos = new ArrayList<Memento>();
    private int current;
    /**
     * 构造函数
     */
    public Caretaker(Originator o) {
        this.o = o;
        current = 0;
    }
    /**
     * 创建一个新的检查点
     */
    public int createMemento() {
        Memento memento = o.createMemento();
        mementos.add(memento);
        return current++;
    }
    /**
     * 将发起人恢复到某个检查点
     */
    public void restoreMemento(int index) {
        Memento memento = mementos.get(index);
        o.restoreMemento(memento);
    }
    /**
     * 将某个检查点删除
     */
    public void removeMemento(int index) {
        mementos.remove(index);
    }
}

public class Client {

    public static void main(String[] args) {

        Originator o = new Originator();
        Caretaker c = new Caretaker(o);
        // 改变状态
        o.setState("state 0");
        // 建立一个检查点
        c.createMemento();
        // 改变状态
        o.setState("state 1");
        // 建立一个检查点
        c.createMemento();
        // 改变状态
        o.setState("state 2");
        // 建立一个检查点
        c.createMemento();
        // 改变状态
        o.setState("state 3");
        // 建立一个检查点
        c.createMemento();
        // 打印出所有检查点
        o.printStates();
        System.out.println("-----------------恢复检查点-----------------");
        // 恢复到第二个检查点
        c.restoreMemento(2);
        // 打印出所有检查点
        o.printStates();
    }

}
```
