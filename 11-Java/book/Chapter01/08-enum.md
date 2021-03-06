枚举
===

JDK1.5 引入了 Emun，使用 Emun 定义常量有诸多优点。

### 通常定义常量方法如下：

```java
public class Light {
    // 红灯
    public final static int RED = 1;
    // 绿灯
    public final static int GREEN = 3;
    // 黄灯
    public final static int YELLOW = 2;
}
```

缺点如下：

1. 非类型安全：必须确保是 int，还要确认值的范围。
2. 无意义的打印：如打印 YELLOW 显示 5。

### 枚举类型定义常量方法：

```java
public enum Light {
       RED, GREEN, YELLOW ;
}
```
上述代码的红灯、绿灯和黄灯的具体值无法表示。可通过构造函数和覆写 toString 方法实现。
首先给Light 枚举类型增加构造方法，然后每个枚举类型的值通过构造函数传入对应的参数，同时覆写 toString 方法，
在该方法中返回从构造函数中传入的参数：

```java
public enum Light {
    // 利用构造函数传参
    RED(1), GREEN(3), YELLOW(2);

    // 定义私有变量
    private int nCode ;

    // 构造函数，枚举类型只能为私有
    private Light( int code) {
        this.nCode = code;
    }

    @Override
    public String toString() {
        return String.valueOf( this.nCode );
    }
}
```

- 枚举为隐式的为static final。

```java
Currency.PENNY = Currency.DIME;
error： The final field EnumExamples.Currency.PENNY cannot be re assigned.
```


- 枚举可用`==`比较。

```java
Currency usCoin = Currency.DIME;
if (usCoin == Currency.DIME) {
    System.out.println("enum in java can be compared using ==");
}
```
对象不建议用“==”，推荐用 equals() 或者 compareTo() 方法。

- Values()方法可迭代枚举常量。

```java
for (Currency coin : Currency.values()) {
    System.out.println("coin: " + coin);
}

And it will print:
coin: PENNY
coin: NICKLE
coin: DIME
coin: QUARTER

Notice the order its exactly same with defined order in enums.
```

- 枚举类可重写某些方法。

```java
public enum Currency {
  @Override
  public String toString() {
       switch (this) {
         case PENNY:
              System.out.println("Penny: " + value);
              break;
         case NICKLE:
              System.out.println("Nickle: " + value);
              break;
         case DIME:
              System.out.println("Dime: " + value);
              break;
         case QUARTER:
              System.out.println("Quarter: " + value);
        }
  return super.toString();
 }
};
And here is how it looks like when displayed:
Currency usCoin = Currency.DIME;
System.out.println(usCoin);

output:
Dime: 10
```

- 枚举类也可实现接口（枚举本身隐式实现了Serializable 和 Comparable 接口）。

```java
public enum Currency implements Runnable{
    PENNY(1), NICKLE(5), DIME(10), QUARTER(25);
    private int value;
    // ............

    @Override
    public void run() {
        System.out.println("Enum in Java implement interfaces");
    }
}
```

- 枚举也可定义抽象方法。

```java
public enum Currency implements Runnable{
    PENNY(1) {
        @Override
        public String color() {
            return "copper";
        }
    },
    NICKLE(5) {
        @Override
        public String color() {
            return "bronze";
        }
    },
    DIME(10) {
        @Override
        public String color() {
            return "silver";
        }
    },
    QUARTER(25) {
        @Override
        public String color() {
            return "silver";
        }
    };
    private int value;

    public abstract String color();

    private Currency(int value) {
        this.value = value;
    }
}

// 调用
System.out.println("Color: " + Currency.DIME.color());
```

### 其他

- 可以创建一个enum 类，把它看做一个普通的类。除了它不能继承其他类，但可实现接口
可以添加其他方法，覆盖它本身的方法
- enum可用于 switch 语句。
- values() 方法是编译器插入到enum 定义中的static 方法，所以，当你将enum 实例向上转型为父类Enum 时，values() 就不可访问了。解决办法：在Class 中有一个getEnumConstants() 方法，所以即便Enum 接口中没有values() 方法，我们仍然可以通过Class 对象取得所有的enum 实例
- 无法从 enum 继承子类，如果需要扩展 enum 中的元素，在一个接口的内部，创建实现该接口的枚举，以此将元素进行分组。达到将枚举元素进行分组。
- 使用EnumSet 代替标志。enum 要求其成员都是唯一的，但是enum 中不能删除添加元素。
- EnumMap 的 key 是 enum，value 是任何其他 Object 对象。
- 可为 eunm 实例编写方法。所以可以为每个 enum 实例赋予各自不同的行为。
- enum 的职责链(Chain of Responsibility)， 这个关系到设计模式的职责链模式。以多种不同的方法来解决一个问题。然后将他们链接在一起。当一个请求到来时，遍历这个链，直到链中的某个解决方案能够处理该请求。
- 使用enum 的状态机。
- 使用enum 多路分发。


### 完整示例

```java
public class LightTest {

    // 1. 定义枚举类型
    public enum Light {
       // 利用构造函数传参
       RED (1), GREEN (3), YELLOW (2);

       // 定义私有变量
       private int nCode ;

       // 构造函数，枚举类型只能为私有
       private Light( int _nCode) {
           this . nCode = _nCode;
       }

       @Override
       public String toString() {
           return String.valueOf ( this . nCode );
       }
    }

    /**
      * @param args
      */
    public static void main(String[] args ) {

        // 1. 遍历枚举类型
        System. out .println( " 演示枚举类型的遍历 ......" );
        testTraversalEnum ();

        // 2. 演示 EnumMap 对象的使用
        System. out .println( " 演示 EnmuMap 对象的使用和遍历 ....." );
        testEnumMap ();

        // 3. 演示 EnmuSet 的使用
        System. out .println( " 演示 EnmuSet 对象的使用和遍历 ....." );
        testEnumSet ();
    }

    /**
      * 演示枚举类型的遍历
      */
    private static void testTraversalEnum() {
        Light[] allLight = Light.values ();
        for (Light aLight : allLight) {
            System. out .println( " 当前灯 name ： " + aLight.name());
            System. out .println( " 当前灯 ordinal ： " + aLight.ordinal());
            System. out .println( " 当前灯： " + aLight);
        }
    }

    /**
      * 演示 EnumMap 的使用， EnumMap 跟 HashMap 的使用相似，但 key 为枚举类型
      */
    private static void testEnumMap() {
        // 1. 定义 EnumMap 对象，EnumMap 对象的构造函数需要参数传入 , 默认是 key 的类的类型
        EnumMap<Light, String> currEnumMap = new EnumMap<Light, String>(
               Light. class );
        currEnumMap.put(Light. RED , " 红灯 " );
        currEnumMap.put(Light. GREEN , " 绿灯 " );
        currEnumMap.put(Light. YELLOW , " 黄灯 " );

        // 2. 遍历对象
        for (Light aLight : Light.values ()) {
            System. out .println( "[key=" + aLight.name() + ",value="
                   + currEnumMap.get(aLight) + "]" );
        }
    }

    /**
      * 演示 EnumSet 如何使用， EnumSet 是一个抽象类，获取一个类型的枚举类型内容 <BR/>
      * 可以使用 allOf 方法
      */
    private static void testEnumSet() {
        EnumSet<Light> currEnumSet = EnumSet.allOf (Light. class );
        for (Light aLightSetElement : currEnumSet) {
            System. out .println( " 当前 EnumSet 中数据为： " + aLightSetElement);
        }
    }
}

```
