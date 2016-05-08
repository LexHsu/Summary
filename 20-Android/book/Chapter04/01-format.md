Android代码风格
===

Google对Android的编程规范在Code Style Guidelines for Contributors中做了描述，并在Android源码中release了import和Java的配置文件android.importorder与android-formatting.xml。本文分析这些配置文件在Eclipse环境下格式化Android编码规范都做了什么，在Java和XML文件中如何具体体现。

Android源码目录<android_src_root>/development/ide/eclipse/下有文件android.importorder和android-formatting.xml，可以在Eclipse中导入import次序及Java编码风格：

1. Window > Preferences -> Java -> Code Style；
2. Organizer Imports -> Imports -> android.importorder；
3. Formatter -> Imports -> android-formatting.xml。

下面讲解这些配置都做了什么，在代码中如何具体体现的。


### 一、Import的次序

Google推荐的AndroidJava文件开头import的次序（按照先后）是：

- com
- org
- android
- java
- javax

排列原则：

- 这个次序也是根据看import语句的重要性来排定的：首先希望看到用了android里的哪些类；然后是其他第三方的；最后才关注标准Java库里的。
- 不同的import分组之间是有一个空白行，在5.2.1 4)中描述。
- 同一import分组内部按照字母次序排列。

### 二、缩进（Indentation）

2.1 总则

缩进只用空格，不用制表符（TAB）。缩进用4个空格，按下TAB键用4个空格代替。

2.2 示例代码

```java
/**
 * Indentation
 */  
class Example {  
    int[] myArray = {  
            1, 2, 3, 4, 5, 6  
    };  

    int theInt = 1;  

    String someString = "Hello";  

    double aDouble = 3.0;  

    void foo(int a, int b, int c, int d, int e, int f) {  
        switch (a) {  
            case 0:  
                Other.doFoo();  
                break;  
            default:  
                Other.doBaz();  
        }  
    }  

    void bar(List v) {  
        for (int i = 0; i <10; i++) {  
            v.add(new Integer(i));  
        }  
    }  
}  

enum MyEnum {  
    UNDEFINED(0) {  
        void foo() {  
        }  
    }  
}  

@interface MyAnnotation {  
    int count() default 1;  
}  
```

2.3 规则说明

1. 域不用对齐：若对齐的话，则myArray,theInt, someString和aDouble都在同一列上对齐。
2. 类体内部的声明全都缩进：Class Example内的定义[#5 ~ #29]相对class Example[#4]都有缩进
3. 枚举的声明要缩进：UNDEFINED(0) [#33]前面有缩进
4. 枚举内的常量要缩进：void foo() [#34]前面有缩进
5. 注解的声明要缩进：int count()[#39]前面有缩进
6. 方法/构造体内的语句要缩进：方法foo和bar内的语句[#16 ~ #22, #26 ~ #28]都有缩进
7. 程序块内的语句要缩进：for循环内的v.add(new Integer(i))[#27]有缩进
8. switch内的语句要缩进：switch内的语句[#17 ~ #21]相对switch有缩进
9. case内的语句要缩进：Other.doFoo()[#18]相对于case；Other.doBaz()[#21]相对于default都有缩进
10. break语句要缩进：break[#19]相对于case有缩进
11. 空白行不用缩进：域和方法之间的空白行[#8, #10, #12, #14, #24]是没有缩进的


### 三、大括号（Braces）的位置

3.1 示例代码

```java
/**                                             //  1
 * Braces                                       //  2
 */                                             //  3
interface Empty {                               //  4
}                                               //  5
                                                //  6
enum MyEnum {                                   //  7
    UNDEFINED(0) {                              //  8
        void foo() {                            //  9
        }                                       // 10
    }                                           // 11
}                                               // 12
                                                // 13
@interfaceSomeAnnotationType {                  // 14
}                                               // 15
                                                // 16
class Example {                                 // 17
    SomeClass fField = new SomeClass() {        // 18
    };                                          // 19
                                                // 20
    int[] myArray = {                           // 21
            1, 2, 3, 4, 5, 6                    // 22
    };                                          // 23
                                                // 24
    int[] emptyArray = new int[] {};            // 25
                                                // 26
    Example() {                                 // 27
    }                                           // 28
                                                // 29
    void bar(int p) {                           // 30
        for (int i = 0; i <10; i++) {           // 31
        }                                       // 32
        switch (p) {                            // 33
            case 0:                             // 34
                fField.set(0);                  // 35
                break;                          // 36
            case 1: {                           // 37
                break;                          // 38
            }                                   // 39
            default:                            // 40
                fField.reset();                 // 41
        }                                       // 42
    }                                           // 43
}                                               // 44
```

3.2  规则说明

1. 类或接口的声明跟左大括号在同一行上：#4 Empty以及 #17 Example后面{的位置
2. 匿名类的声明跟左大括号在同一行上：#18 SomeClass后面{的位置
3. 构造体的声明跟左大括号在同一行上：#27 Example()后面{的位置
4. 方法的声明跟左大括号在同一行上：#9 foo和#30 bar后面{的位置
5. 枚举的声明跟左大括号在同一行上：#7 MyEnum 后面{的位置
6. 枚举常量体跟左大括号在同一行上：#8 UNDEFINED(0) 后面{的位置
7. 注解类型的声明跟左大括号在同一行上：#14 SomeAnnotationType后面{的位置
8. 程序块跟左大括号在同一行上：#31 for后面{的位置
9. case语句中的程序块跟左大括号在同一行上：#37 case 1后面{的位置
10. switch语句跟左大括号在同一行上：#33 switch后面{的位置
11. 数组的初始化常量跟左大括号在同一行上：#21和#25 {的位置


四、空格（White Space）

4.1 声明

4.1.1 类
[java] view plain copy
class MyClass implements I0, I1, I2 {  
}  

AnonClass = new AnonClass() {  
    void foo(Some s) {  
    }  
};  
·        类的左大括号的前面加空格；
·        匿名类的左大括号的前面加空格；
·        implements语句中逗号的前面，不加空格；
·        implements语句中逗号的后面，加上空格；

4.1.2 域
[java] view plain copy
int a = 0, b = 1, c= 2, d = 3;  
·        多个域声明中逗号的前面，不加空格；
·        多个域声明中逗号的后面，加上空格。

4.1.3 临时变量
[java] view plain copy
int a = 0, b = 1, c= 2, d = 3;  
·        多个临时变量声明中逗号的前面，不加空格；
·        多个临时变量声明中逗号的后面，加上空格；

4.1.4 构造体
[java] view plain copy
MyClass() throws E0, E1 {  
    this(0, 0, 0);  
}  

MyClass(int x, int y, int z) throws E0, E1 {  
    super(x, y, z, true);  
}  

·        左小括号的前面，不加空格；
·        左小括号的后面，不加空格；
·        右小括号的前面，不加空格；
·        小括号内为空，则它们之间不加空格；
·        左大括号前面，加上空格；
·        参数列表中逗号之前，不加空格；
·        参数列表中逗号之后，加上空格；
·        throws语句中逗号之前，不加空格；
·        throws语句中逗号之后，加上空格。

4.1.5 方法
[java] view plain copy
void foo() throws E0, E1 {  
};  

void bar(int x, int y) throws E0, E1 {  
}  

void format(Strings, Object... args) {  
}  
·        左小括号的前面，不加空格；
·        左小括号的后面，不加空格；
·        右小括号的前面，不加空格；
·        小括号内为空，则它们之间不加空格；
·        左大括号前面，加上空格；
·        参数列表中逗号之前，不加空格；
·        参数列表中逗号之后，加上空格；
·        可变参数列表省略号之前，不加空格；
·        可变参数列表省略号之后，加上空格；
·        throws语句中逗号之前，不加空格；
·        throws语句中逗号之后，加上空格。

4.1.6 标号
[java] view plain copy
label: for (int i = 0; i < list.length; i++) {  
    for (int j = 0; j < list[i].length; j++)  
        continue label;  
}  
·        冒号之前，不加空格；
·        冒号之后，加上空格

4.1.7 注解/Annotation
[java] view plain copy
@Annot(x = 23, y = -3)  
public class A {  
}  
·        ‘@’之后，不加空格；
·        左小括号的前面，不加空格；
·        左小括号的后面，不加空格；
·        逗号前面，不加空格；
·        逗号后面，加上空格；
·        右小括号的前面，不加空格

4.1.8 枚举/Enumtypes
[java] view plain copy
enum MyEnum {  
    GREEN(0, 1), RED() {  
        void process() {  
        }  
    }  
}  
·        声明中左大括号的前面[#1]，加上空格；
·        常量之间的逗号[#2 RED前]前面，不加空格；
·        常量之间的逗号[#2 RED前]后面，加上空格；
·        常量参数的左小括号[#2 GREEN后]前面，不加空格；
·        常量参数的左小括号[#2 GREEN后]后面，不加空格；
·        常量参数的小括号[#2 RED后]中间为空，括号之间不加空格；
·        常量参数之间的逗号[#2 GREEN()里面]前面，不加空格；
·        常量参数之间的逗号[#2 GREEN()里面]后面，加上空格；
·        常量参数的右小括号[#2 GREEN()后]前面，不加空格；
·        常量体左大括号[#2 RED后]前面，加上空格。

4.1.9 注解类型/Annotationtypes
[java] view plain copy
@interface MyAnnotation {  
    String value();  
}  

@interface OtherAnnotation {  
}  
·        ‘@’之前，不加空格；
·        ‘@’之后，不加空格
·        左大括号的前面，加上空格；
·        注解类型成员的左小括号的前面，不加空格；
·        注解类型成员的小括号的之间，不加空格；

4.2 控制语句

4.2.1 程序块
[java] view plain copy
if (true) {  
    return 1;  
} else {  
    return 2;  
}  
·        左大括号前面，加上空格；
·        右大括号后面，加上空格。

4.2.2 if else语句
[java] view plain copy
if (condition) {  
    return foo;  
} else {  
    return bar;  
}  
·        左小括号前加上空格；
·        左小括号后不加空格；
·        右小括号前不加空格【左大括号前的空格是规则#4.2.1】

4.2.3 for语句
[java] view plain copy
for (int i = 0, j = array.length; i < array.length; i++, j--) {  
}  
for (String s : names) {  
}  
·        左小括号前加上空格；
·        左小括号后不加空格；
·        右小括号前不加空格【左大括号前的空格是规则#4.2.1】
·        初始化语句的逗号前不加空格；
·        初始化语句的逗号后加上空格
·        增量语句的逗号前不加空格；
·        增量语句的逗号后加上空格
·        语句之间的分号前不加空格；
·        语句之间的分号后加上空格；
·        冒号前面加上空格；
·        冒号后面加上空格。

4.2.4 switch语句
[java] view plain copy
switch (number) {  
    case RED:  
        return GREEN;  
    case GREEN:  
        return BLUE;  
    case BLUE:  
        return RED;  
    default:  
        return BLACK;  
}  
·        case和default的冒号（‘:’）前不加空格；
·        左括号（‘(’）和左大括号（‘{’）前都加上空格；
·        左括号（‘(’）后和右括号（‘)’）前都不加空格。

4.2.5 while和dowhile语句
[java] view plain copy
while (condition) {  
}  
;  
do {  
} while (condition);  
·        左括号前加上空格；
·        左括号后不加空格；
·        右括号前不加空格【#1左大括号前的空格是规则#4.2.1】

4.2.6 同步（synchronized）语句
[java] view plain copy
synchronized (list) {  
    list.add(element);  
}  
·        左括号前加上空格；
·        左括号后不加空格；
·        右括号前不加空格【左大括号前的空格是规则#4.2.1】

4.2.7 catch语句
[java] view plain copy
try {  
    number = Integer.parseInt(value);  
} catch (NumberFormatException e) {  
}  
·        左括号前加上空格；
·        左括号后不加空格；
·        右括号前不加空格【左大括号前的空格是规则#4.2.1】

4.2.8 assert语句
[java] view plain copy
assert condition : reportError();  
冒号前后都加上空格

4.2.9 return语句
[java] view plain copy
return (o);  
括号表达式前加上空格

4.2.10 throw语句
[java] view plain copy
throw (e);  
括号表达式前加上空格

4.3 表达式

4.2.1 函数调用
[java] view plain copy
foo();  
bar(x, y);  

String str = new String();  
Point point = new Point(x, y);  

MyClass() throws E0, E1 {  
    this(0, 0, 0);  
}  

MyClass(int x, int y, int z) throws E0, E1 {  
    super(x, y, z, true);  
}  
·        左括号的前后都不加空格；
·        右括号前不加空格；
·        空的参数的左右括号之间不加空格；
·        方法调用时多个参数之间分割的逗号前面不加空格，逗号后面加空格；
·        对象申请时多个参数之间分割的逗号前面不加空格，逗号后面加空格；
·        显示调用构造函数时多个参数之间分割的逗号前面不加空格，逗号后面加空格；

4.3.2 赋值
[java] view plain copy
List list = new ArrayList();  
int a = -4 + -9;  
b = a++ / --number;  
c += 4;  
boolean value = true && false;  
赋值操作（=）前后都加上空格。【注意：‘+=’是一个操作数】

4.3.3 操作数
[java] view plain copy
List list = new ArrayList();  
int a = -4 + -9;  
b = a++ / --number;  
c += 4;  
boolean value = true && false;  
·        二元操作（#2的‘+’；#3的‘/’；#5的‘&&’）前后都加上空格；
·        一元操作（#2 ‘4’和‘9’前面的‘-’）前后都不加空格【示例中‘-’前的空格不是这个规则里的】；
·        前置操作的（#3的‘--number’）前后都不加空格【示例中‘--’前的空格不是这个规则里的】；
·        后置操作的（#3的‘a++’）前后都不加空格【示例中‘++’后的空格不是这个规则里的】。

4.3.4 加括号的表达式
[java] view plain copy
result = (a * (b + c + d) * (e + f));  
左括号前，左括号后和右括号前都不加空格【示例中左括号前的空格不是这个规则里的】

4.3.5 类型转换
[java] view plain copy
String s = ((String) object);  
·        右括号后加上空格；
·        左括号后和右括号前都不加空格。

4.3.6 三元条件表达式
[java] view plain copy
String value = condition ? TRUE : FALSE;  
问号前，问号后，冒号前，冒号后都要加上空格

4.4 数组

[java] view plain copy
int[] array0 = new int[] {};  
int[] array1 = new int[] {  
        1, 2, 3  
};  
int[] array2 = new int[3];  

array[i].foo();  

4.4.1 声明
·        左中括号前不加空格；
·        左右中括号中间不加空格
示例中：arrayX前的‘[]’

4.4.2 申请
·        左中括号前后都不加空格；
·        右中括号前不加空格；
·        空的左右中括号中间不加空格；
示例中：等号‘=’后面的‘[]’

4.4.3 初始化体
·        左大括号前后都加上空格；
·        右大括号前加上空格；
·        逗号前不加空格；
·        逗号后加上空格；
·        空的大括号中间不加空格

4.4.4 数组元素的访问
·        左中括号前后都不加空格；
·        右中括号前面不加空格

4.5 泛型
[java] view plain copy
Map<String, Element> map = newHashMap<String, Element>();  

x.<String, Element> foo();  

classMyGenericType<S, T extends Element & List> {  
}  

Map<X<?>, Y<? extendsK, ? super V>> t;  


五、空白行（BlankLines）

5.1 示例代码
[java] view plain copy
/**
 * Blank Lines
 */  

package foo.bar.baz;  

import java.util.List;  
import java.util.Vector;  

import java.net.Socket;  

public class Another {  
}  

public class Example {  
    public static class Pair {  
        public String first;  

        public String second;  
        // Between here...  

        // ...and here are 10 blank lines  
    };  

    private LinkedList fList;  

    public int counter;  

    publicExample(LinkedList list) {  
        fList = list;  
        counter = 0;  
    }  

    public void push(Pair p) {  
        fList.add(p);  
        ++counter;  
    }  

    public Object pop() {  
        --counter;  
        return (Pair)fList.getLast();  
    }  
}  


5.2 规则说明

5.2.1 编译单元之间的空白行
1)     包声明之前有一空白行[#4]
2)     包声明之后有一空白行[#6]
3)     import声明之前有一空白行[#6]
4)     import各个分组之间有一空白行[#9]
5)     import声明之后有一空白行[#11]
6)     类声明之间有一空白行[#14]

5.2.2 类内部的空白行
1)     第一个声明之间无空白行[#15 & #16之间]
2)     相同分类声明之前有一空白行[#24, #28]
3)     成员类声明之前有一空白行
4)     域声明之前有一空白行[#18, #24, #26]
5)      方法声明之前有一空白行[#28,#33, #38]          
6)      方法内的开始处没有空白行[#29和#30之间；#34与#35之间；#39与#40之间]


六、插入新行（NewLines）

6.1 示例代码
[java] view plain copy
/**
 * New Lines
 */  
public class Empty {  
}  

class Example {  
    static int[] fArray = {  
            1, 2, 3, 4, 5  
    };  

    Listener fListener = new Listener() {  
    };  

    @Deprecated  
    @Override  
    public void bar(@SuppressWarnings("unused") int i) {  
        @SuppressWarnings("unused")  
        int k;  
    }  

    void foo() {  
        ;  
        ;  
        label: do {  
        } while (false);  
        for (;;) {  
        }  
    }  
}  

enum MyEnum {  
    UNDEFINED(0) {  
    }  
}  

enum EmptyEnum {  
}  

@interface EmptyAnnotation{  
}  


6.2 规则说明

6.2.1 插入新行
1)     类体内为空，插入新行[#5是另起的一行]
2)     匿名类体内为空，插入新行[#13是另起的一行]
3)     方法内为空，插入新行
4)     空的程序块，插入新行[#26是另起的一行；#28是另起的一行]
5)     标号后面不插入新行[#25 do与label在同一行]
6)     在空的枚举声明中，插入新行[#38是另起的一行]
7)     在空的枚举常量体中，插入新行[#34是另起的一行]
8)     在空的注解体中，插入新行[#41是另起的一行]
9)     在文件结尾，插入新行[#42是另起的一行]

6.2.2 数组初始化
1)     数组初始化体的左大括号后，插入新行[#9是另起的一行]
2)     数组初始化体的右大括号前，插入新行[#10是另起的一行]

6.2.3 空的语句
空的语句放在新行上[#24是另起的一行]

6.2.4 注解
1)     对成员的注解之后，插入新行[#16 & #17都是另起的一行]
2)     对参数的注解之后，不插入新行[#17 int i与@SuppressWarnings("unused")在同一行]
3)     对临时变量的注解之后，插入新行[#19是另起的一行]


七、控制语句（ControlStatements）

7.1 示例代码
[java] view plain copy
/**
 * If...else
 */  
class Example {  
    void bar() {  
        do {  
        } while (true);  
        try {  
        } catch (Exception e) {  
        } finally {  
        }  
    }  

    void foo2() {  
        if (true) {  
            return;  
        }  
        if (true) {  
            return;  
        } else if (false) {  
            return;  
        } else {  
            return;  
        }  
    }  

    void foo(int state) {  
        if (true)  
            return;  
        if (true)  
            return;  
        else if (false)  
            return;  
        else  
            return;  
    }  
}  


7.2 规则说明

1)     if语句的else之前，不插入新行[#20& #22的else与‘}’在同一行]
2)     try语句的catch之前，不插入新行[#9的catch与‘}’在同一行]
3)     try语句的finally之前，不插入新行[#10的finally与‘}’在同一行]
4)     do语句的while之前，不插入新行[#7的while与‘}’在同一行]
5)     #29的‘then’语句与#28的if在不同行，#31的‘then’语句与#30的if在不同行；
6)     #35的else语句与#34的else在不同行；
7)     #20和#32的else if中‘else与‘if’在同一行；
8)     ‘return’或‘throw’语句不需要在一行上[#16与#15在两行上]


八、换行（LineWrapping）

8.1 总则
 每行最多100个字符；
 超过100个字符的行要换行，新行缺省缩进2个缩进单位。一个缩进单位是4个空格，所以这里总共缩进8个空格。
 缺省数组初始化值缺省缩进2个缩进单位。

8.2 注解（Annotation）
[java] view plain copy
/**
 * Element-value pairs
 */  
@MyAnnotation(value1 = "this isan example", value2 = "of an annotation", value3 = "withseveral arguments", value4 = "by Haili TIAN (haili.tian@gmail.com)")  
class Example {  
}  
注解不换行：value1、value2、value3和value4都在同一行上。

8.3 类声明
[java] view plain copy
/**
 * 'extends' clause
 */  
class Example extends  
        OtherClass {  
}  

/**
 * 'implements' clause
 */  
class Example implements I1,  
        I2, I3 {  
}  

 extends子句在需要换行的地方，用缺省换行方式换行：#5OtherClass处换行，且OtherClass相对class缩进了8个空格；
 implements子句在需要换行的地方，用缺省换行方式换行：#12I2处换行，且I2相对class缩进了8个空格。

8.4 构造体声明
[java] view plain copy
/**
 * Parameters
 */  
class Example {  
    Example(int arg1, int arg2,  
            int arg3, int arg4,  
            int arg5, int arg6) {  
        this();  
    }  

    Example() {  
    }  
}  

/**
 * 'throws' clause
 */  
class Example {  
    Example() throws FirstException,  
            SecondException,  
            ThirdException {  
        returnOther.doSomething();  
    }  
}  

 构造体的参数在需要换行的地方，用缺省换行方式换行：参数[#6 &#7]相对Example[#5]缩进了8个空格；
 构造体throws子句在需要换行的地方，用缺省换行方式换行：子句[#20& #21]相对Example[#19]缩进了8个空格。

8.5 方法声明
[java] view plain copy
/**
 * Declaration
 */  
class Example {  
    public final synchronizedjava.lang.String a_method_with_a_long_name() {  
    }  
}  

/**
 * Parameters
 */  
class Example {  
    void foo(int arg1, int arg2,  
            int arg3, int arg4,  
            int arg5, int arg6) {  
    }  
}  

/**
 * 'throws' clause
 */  
class Example {  
    int foo() throws FirstException,  
            SecondException,  
            ThirdException {  
        returnOther.doSomething();  
    }  
}  

 方法声明处不换行：#5 很长，但不分行；
 方法声明处的参数在需要换行的地方，用缺省换行方式换行：参数[#14& #15]相对voidfoo [#13]缩进了8个空格；
 方法声明处的throws子句在需要换行的地方，用缺省换行方式换行：子句[#24& #25]相对intfoo [#23]缩进了8个空格。

8.6 枚举声明
[java] view plain copy
/**
 * Constants
 */  
enum Example {  
    CANCELLED, RUNNING, WAITING, FINISHED  
}  

enum Example {  
    GREEN(0, 255, 0), RED(  
            255, 0, 0)  
}  

/**
 * 'implements' clause
 */  
enum Example implements A, B,  
        C {  
}  

/**
 * Constant arguments
 */  
enum Example {  
    GREEN(0, 255, 0), RED(  
            255, 0, 0)  
}  

 枚举常量定义的地方，不换行：#5不换行；
 implements子句在需要换行的地方，用缺省换行方式换行：#17 C处换行，且C相对enum缩进了8个空格；
 常量参数在需要换行的地方，用缺省换行方式换行：#10 和#25处换行，且相对GREEN缩进了8个空格。

8.7 函数调用
[java] view plain copy
/**
 * Arguments
 */  
class Example {  
    void foo() {  
        Other.bar(  
                100,  
                nested(200, 300, 400,  
                        500, 600, 700,  
                        800, 900));  
    }  
}  

/**
 * Qualified invocations
 */  
class Example {  
    int foo(Some a) {  
        return a.getFirst();  
    }  
}  

/**
 * Explicit constructor invocations
 */  
class Example extends AnotherClass {  
    Example() {  
        super(100, 200, 300,400, 500,  
                600, 700);  
    }  
}  

/**
 * Object allocation arguments
 */  
class Example {  
    SomeClass foo() {  
        return new SomeClass(100,200,  
                300, 400, 500, 600,  
                700, 800, 900);  
    }  
}  

/**
 * Qualified object allocation arguments
 */  
class Example {  
    SomeClass foo() {  
        return SomeOtherClass.new SomeClass(  
                100, 200, 300, 400, 500);  
    }  
}  

 函数调用参数处，在需要换行的地方，用缺省换行方式换行：#7, #8和#9, #10处换行，且#7,#8相对Other.bar缩进了8个空格， #9, #10相对nested缩进了8个空格；
 对象的方法调用处，在需要换行的地方，用缺省换行方式换行：#19这个例子不太好，如果这句很长，可以在a和getFirst之间换行，且.放在getFirst前；
 显示的构造函数调用处，在需要换行的地方，用缺省换行方式换行：#29处换行，且相对#30的super缩进8个空格；
 对象创建参数处，在需要换行的地方，用缺省换行方式换行：#39, #40处换行，且相对#38的return缩进8个空格；
 对象方法构造对象参数处，在需要换行的地方，用缺省换行方式换行：#50处换行，且相对#49的return缩进8个空格.

8.8 表达式
[java] view plain copy
/**
 * Binary expressions
 */  
class Example extends AnotherClass {  
    int foo() {  
        int sum = 100 + 200 + 300 + 400  
                + 500 + 600 + 700 + 800;  
        int product = 1 * 2 * 3 * 4 * 5  
                * 6 * 7 * 8 * 9 * 10;  
        boolean val = true && false  
                && true && false  
                && true;  
        return product / sum;  
    }  
}  

/**
 * Conditionals
 */  
class Example extends AnotherClass {  
    int Example(boolean Argument) {  
        return argument ?100000  
                : 200000;  
    }  
}  

/**
 * Array initializers
 */  
class Example {  
    int[] fArray = {  
            1, 2, 3, 4, 5, 6, 7, 8, 9,  
            10, 11, 12  
    };  
}  

/**
 * Assignments
 */  
class Example {  
    private static final String string ="TextTextText";  

    void foo() {  
        for (int i = 0; i <10; i++) {  
        }  
        String s;  
        s = "TextTextText";  
    }  
}  

 二元表达式，在需要换行的地方，用缺省换行方式换行：#7, #9和#11,#12处换行，且#7相对int sum缩进了8个空格，且#9相对int product缩进了8个空格，且#11& #12相对boolean val缩进了8个空格；
 三元条件表达式，除了第一个元素，用缺省换行方式换行所有其他元素：#22& #23；
 数组初始化体，在需要换行的地方，用缺省换行方式换行：#32 &#33处换行，且#32& #33相对int[] fArray缩进了8个空格；
 赋值语句，不需要换行：#41行很长也不需要换行。

8.9 语句
[java] view plain copy
/**
 * Compact 'if else'
 */  
class Example {  
    int foo(int argument) {  
        if (argument == 0)  
            return 0;  
        if (argument == 1)  
            return 42;  
        else  
            return 43;  
    }  
}  

配置起什么用，没看懂！


九、注释（Comments）

[java] view plain copy

[java] view plain copy
/**
 * An example for comment formatting. This example is meant to illustrate the various possibilities offered by <i>Haili TIAN</i> in order to format comments.
 */  

package mypackage;  

/**
 * This is the comment for the example interface.
 */  
interface Example {  
    // This is a long comment with white space that should be split in multiple  
    // line comments in case the linecomment formatting is enabled  
    int foo3();  

//    void commented() {  
//        System.out.println("indented");  
//    }  

    // void indentedCommented() {  
    // System.out.println("indented");  
    // }  

    /* block comment on first column */  
    int bar();  

    /*
     * These possibilities include:<ul><li>Formatting of header
     * comments.</li><li>Formatting of Javadoc tags</li></ul>
     */  
    int bar2(); // This is along comment that should be split in multiple line  
                // comments in case the linecomment formatting is enabled  

    /**
     * The following is some sample code whichillustrates source formatting
     * within javadoc comments:
     *
     * <pre>
     * public class Example {
     *    final int a = 1;
     *
     *    final boolean b = true;
     * }
     * </pre>
     *
     * Descriptions of parameters and returnvalues are best appended at end of
     * the javadoc comment.
     *
     * @param a The first parameter. For an optimum result, this should be an
     *            odd number between 0 and 100.
     * @param b The second parameter.
     * @return The result of the foo operation, usually within 0 and 1000.
     */  
    int foo(int a, int b);  
}  
 注释每行长度是80个字符；
 开启对Javadoc注释的格式化；
  用html的TAG；
  开启对‘pre’TAG里的Java代码片段进行格式化；
#37 ~ #43
  Javadoc TAG前面插入空白行；
#47是插入的行
  Javadoc Tag缩进
§ ‘@param’TAG后面的描述缩进
#49的odd相对#48的a缩进了4个空格。
  不对‘@param’TAG插入新行；
#48和#50的描述与参数在同一行。
  ‘/**’和‘*/’在不同的行；
  移除空白行；
Javadoc注释形式见#33~ #52
 开启对块注释的格式化；
  ‘/*’和‘*/’在不同的行；
  移除空白行；
块注释形式见#26~ #29
 开启对行注释的格式化；
  注释在行的第一列
#15, #16 和#17是行注释，且‘//’与原程序之间的空格仍旧保持。
 关闭对头注释的格式化；
#1, #2和#3的头注释保持不变。
 关闭对块注释缩进到第一列；
#23是块注释，与程序有相同的缩进，不是缩进到第一列。
 关闭对行注释缩进到第一列；

注：笔者对行注释验证，发现Eclipse中无论如何设置，基本不会改变其行为。


十、Android中XML文件的格式化

前面讲了那么多都是针对Java程序的，Android中有大量的XML文件。对XML的格式也要进行排版格式化。
打开Window> Preferences，可以通过两个地方对XML文件进行格式化：
1)     XML > XML Files > Editor
对其中的各项设置进行配置
2)     Android > Editors
对其中的各项设置进行配置


总结

        本文分析了Android编码风格在Eclipse环境中具体实施，通过对Eclipse环境的配置可以方便的格式化Android程序：Java文件和XML文件，特别是Java文件，其配置是基于JDT实现的，所以对Java的方方面面的配置都覆盖了。
        但是，只有这些还不够，对一般的Java设计编码原则和Android中推荐的风格还不能完全自动执行，还需要人的参与，依赖于团队风格规范的制定，成员的设计能力和领悟执行能力。下面是这些工具所不能解决的风格和原则：
 对Exception的处理；
 写短小的方法；
 域的命名规则；
 程序块内的逻辑分组；
 等等。
