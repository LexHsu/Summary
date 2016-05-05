Java 重写方法与初始化的隐患
===

### 问题

父类 SuperClass:

```
public class SuperClass {

    private int mSuperX;

    public SuperClass() {
        setX(99);
    }

    public void setX(int x) {
        mSuperX = x;
    }
}
```

子类 SubClass:

```
public class SubClass extends SuperClass {

    private int mSubX = 1;

    public SubClass() {}

    @Override
    public void setX(int x) {
        super.setX(x);
        mSubX = x;
        System.out.println("SubX is assigned " + x);
    }

    public void printX() {
        System.out.println("SubX = " + mSubX);
    }
}
```

main 调用:

```
public class Main {
    public static void main(String[] args) {
        SubClass sc = new SubClass();
        sc.printX();
    }
}
```

输出结果:

```
SubX is assigned 99
SubX = 99

然而真正运行后输出的是:

SubX is assigned 99
SubX = 1
```

### 分析

看Java字节码，Main字节码：

```
Compiled from "Main.java"
public class bugme.Main {
  ......
  public static void main(java.lang.String[]);
    Code:
       0: new           #2                  // class bugme/SubClass
       3: dup           
       4: invokespecial #3                  // Method bugme/SubClass."<init>":()V
       ......  
}
```

1. 首先new一个SubClass实例, 把引用入栈, 
2. dup是把栈顶复制一份入栈, 
3. invokespecial #3将栈顶元素出栈并调用它的某个方法, 这个方法具体是什么要看常量池里第3个条目是什么, 但是javap生成的字节码直接给我们写在旁边了, 即SubClass.<init>.

SubClass.<init> 里面并没有方法叫<init>, 是因为javap为了方便阅读, 直接把它改成类名bugme.SubClass, 顺便一提, bugme是包名. <init>方法并非通常意义上的构造方法, 这是Java帮我们合成的一个方法, 里面的指令会帮我们按顺序进行普通成员变量初始化, 也包括初始化块里的代码, 注意是按顺序执行, 这些都执行完了之后才轮到构造方法里代码生成的指令执行. 这里aload_0将局部变量表中下标为0的元素入栈, 其实就是Java中的this, 结合invokespecial #1, 是在调用父类的构造函数, 也就是我们常见的super().

所以我们再看SuperClass.<init>
```
public class bugme.SuperClass {
  public bugme.SuperClass();
    Code:
       0: aload_0       
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: aload_0       
       5: bipush        99
       7: invokevirtual #2                  // Method setX:(I)V
      10: return  

  ......     
}
```

同样是先调了父类Object的构造方法, 然后再将this, 99入栈, invokevirtual #2旁边注释了是调用setX, 参数分别是this和99也就是this.setX(99), 然而这个方法被重写了, 调用的是子类的方法, 所以我们再看SubClass.setX:
```
public class bugme.SubClass extends bugme.SuperClass {
  ......
  public void setX(int);
    Code:
       0: aload_0       
       1: iload_1       
       2: invokespecial #3                  // Method bugme/SuperClass.setX:(I)V
       ......
}
```
这里将局部变量表前两个元素都入栈, 第一个是this, 第二个是括号里的参数, 也就是99, invokespecial #3调用的是父类的setX, 也就是我们代码中写的super.setX(int)

SuperClass.setX就很简单了:
```
public class bugme.SuperClass {
  ......     
  public void setX(int);
    Code:
       0: aload_0       
       1: iload_1       
       2: putfield      #3                  // Field mSuperX:I
       5: return        
}
```
这里先把this入栈, 再把参数入栈, putfield      #3使得前两个入栈的元素全部出栈, 而成员mSuperX被赋值, 这四条指令只对应代码里的一句this.mSuperX = x;

接下来控制流回到子类的setX:
```
public class bugme.SubClass extends bugme.SuperClass {
  ......
  public void setX(int);
    Code:
       0: aload_0       
       1: iload_1       
       2: invokespecial #3  // Method bugme/SuperClass.setX:(I)V
     ->5: aload_0           // 即将执行这句
       6: iload_1       
       7: putfield      #2  // Field mSubX:I
      10: getstatic     #4  // Field java/lang/System.out:Ljava/io/PrintStream;
      13: new           #5  // class java/lang/StringBuilder
      16: dup           
      17: invokespecial #6  // Method java/lang/StringBuilder."<init>":()V
      20: ldc           #7  // String SubX is assigned 
      22: invokevirtual #8  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      25: iload_1       
      26: invokevirtual #9  // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
      29: invokevirtual #10 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      32: invokevirtual #11 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      35: return
}
```

从5处开始继续分析, 5,6,7将参数的值赋给mSubX, 此时mSubX是99了, 下面那一堆则是在执行System.out.println("SubX is assigned " + x);并返回, 还可以看到Java自动帮我们使用StringBuilder优化字符串拼接, 就不分析了.

说了这么多, 我们的代码才刚把下面箭头指着的这句执行完:
```java
public class bugme.SubClass extends bugme.SuperClass {
  public bugme.SubClass();
    Code:
       0: aload_0       
     ->1: invokespecial #1                  // Method bugme/SuperClass."<init>":()V
       4: aload_0       
       5: iconst_1      
       6: putfield      #2                  // Field mSubX:I
       9: return        

  ......      
}
```
此时mSubX已经是99了, 再执行下面的4,5,6, 这一部分是SubClass的初始化, 代码将把1赋给mSubX, 99被1覆盖了.
方法返回后, 相当于我们执行完了箭头指的这一句代码:
```
public class Main {
    public static void main(String[] args) {
      ->SubClass sc = new SubClass();
        sc.printX();
    }
}
```
接下来执行的代码将打印mSubX的值, 自然就是1了.

以前就听说过JVM是基于栈的, Dalvik是基于寄存器的, 现在看了Java字节码, 回想一下smali, 自然就能明白. 我在Android无需权限显示悬浮窗, 兼谈逆向分析app中有分析smali代码, smali里面经常看到类似v0, v1这类东西, 是在操作寄存器, 而刚才分析的bytecode, 指令常常伴随着入栈出栈.

### 总结

Java 中 new 一个子类, 初始化顺序:

1. 父类static成员
2. 子类static成员
3. 父类普通成员初始化和初始化块
4. 父类构造方法
5. 子类普通成员初始化和初始化块
6. 子类构造方法

上例中，父类构造方法中调用了一次 setX, 此时 mSubX 已是要跟踪的值, 但之后子类普通成员初始化将 mSubX 又初始化了一遍, 覆盖了前面的值。

Java 中, 在子类构造方法中唯一能安全调用的是基类中的 final 方法, 子类的 final 方法(子类的 private 方法自动 final), 如果类本身是 final 的, 自然就能安全调用自己所有的方法，完全遵守这个准则, 可以保证不会出上例的问题。

如果默认初始化代码如下:
```
public class SubClass extends SuperClass {
    private int mSubX;

    public SubClass() {}
    ......
}
```
则不会出现上例中的问题，可见区别就在于如下代码, 虽然效果一样, 但实际是有区别的.
```
private int mSubX;

private int mSubX = 0;
```

一般情况下, 这两句代码对程序没有任何影响(除非你遇到这个bug), 上面一句和下面一句的区别在于, 下面一句会导致<init>方法里面生成3条指令, 分别是aload_0, iconst_0, putfield  #**, 而上面一句则不会.
```
   4: aload_0       
   5: iconst_1      
   6: putfield      #2                  // Field mSubX:I
```
所谓的默认初始化, 其实是我们要实例化一个对象之前, 需要一块内存放我们的数据, 这块内存被全部置为0, 这就是默认初始化了。如果成员变量使用默认值初始化, 就没必要自己赋那个默认值, 而且还能省3条指令.
