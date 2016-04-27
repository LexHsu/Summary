lib工程
===

### 库工程（library project）

library project是作为jar包被其它android工程使用的，首先它也是普通的android工程。然后：

1. 右键指定工程，选择Properties
2. 选择Android，选中”Is Library“

这个android工程就成为了library工程。

注意：作为library工程可以引用外部jar包，但不能引用其它library工程，在library工程中不能使用aidl文件，不能引用raw、assets下资源
另外当试图运行一个library工程时，eclipse将报错："Android library projects cannot be launched."


### 主工程引用库工程

当引用lib工程后，主工程就可以调用library工程的相关类和方法。android工程引用其它library工程步骤：

1. 右键主工程选择Properties
2. 选择Android，点击Add按钮打开lib工程选择对话框

### 在主工程manifest.xml文件中声明lib工程的组件

主工程manifest.xml中必须添加：

1. library工程的所有Activity、Service、receiver、provider，
2. library工程的permission、uses-library等属性，

注意引用的组件要使用完全的包名，否则将报对应的NotFoundException。

### 库工程与主工程资源冲突问题

当运行有引用library工程的android工程时，android工具将会合并library工程与主工程的所有资源。如果一个资源ID将有可能在library工程之间或library工程、主工程之间都有定义，这时候优先级别高的资源ID将覆盖优先级别低的，使用资源时将使用有线级别最高的工程的资源。工程之间优先级别如何判定，请看下一条。

### 库工程之间以及主工程的资源使用上的优先级问题

![lib priority](img/lib.png)

优先级：主工程 > LibA > LibB，library工程之间也可以手动调整优先级。

6、库工程和主工程使用不同的android platform version问题

主工程打包时，android sdk版本使用的是主工程。所以library工程使用的android sdk版本要不高于主工程的sdk版本。如果library工程sdk版本高于主工程，将不能通过编译。
