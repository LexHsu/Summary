extern
===

### extern 修饰变量

extern 修饰变量的声明。举例来说，如果文件 a.c 需要引用 b.c 中变量 int v，可在 a.c 中声明 extern int v 即可引用变量 v。注意被引用的变量 v 的链接属性必须是外链接（external）的，即 a.c 要能引用到 v。这涉及到c语言的另外一个话题－－变量的作用域。能够被其他模块以extern修饰符引用到的变量通常是全局变量。

此外 extern int v 可放在 a.c 中的任何地方，如在 a.c 中某一函数内声明 extern int v，值是作用域仅在该函数中，即变量作用域的问题。


### extern 修饰函数

本质变量和函数没有区别。函数名是指向函数二进制块开头处的指针。如果文件 a.c 需要引用 b.c 中的函数，在 b.c 中原型是 int fun(int mu)，可在 a.c 中声明 extern int fun(int mu)即可，该声明可以放在 a.c 文件任何地方，其实最常用的方法是包含这个函数声明的头文件。但是与 extern 的区别在于 extern 是直接指定引用的函数想，可加速程序的编译（确切的说是预处理）的过程。在大型C程序编译过程中，这种差异是非常明显的。

### extern "c"

常在cpp的代码之中看到这样的代码（在 c 文件中也会出现。最终该 c 文件会被 cpp 文件 include）；

```c
#ifdef __cplusplus
extern "C" {
#endif

// code here...

#ifdef __cplusplus
}
#endif
```

__cplusplus 是 cpp 中的自定义宏。上述代码作用是使 C++ 使用 c 方式调用 C 库文件的函数。在 c++ 中，为了支持重载机制，在编译生成的汇编码中，要对函数的名字进行一些处理，如加上函数的返回类型等。而在 C 中只是函数名，不会加入其他信息。可见 C++ 和 C 编译生成的函数名不同。而 extern "C" 便是在 C++ 中支持 C 程序的一种策略。

实例讲解：写一个 c 函数，文件名为 f1.c。

```c
extern "C" {
    void f1() {
        return;
    }
}
```
编译：gcc -c f1.c -o f1.o，产生 f1.o 库文件。再写一段 C++ 代码调用该 f1 函数。

```c
// 链接的时候还是需要链接上原来的库文件
extern void f1();
int main() {
    f1();
    return 0;
}
```

编译：gcc -c test.cxx -o test.o 产生test.o 文件。

使用 gcc test.o f1.o 来链接两个文件，出错：
undefine reference to 'f1()'

修改代码如下即可：

```c

extern "C" {
    extern void f1();
}
int main() {
    f1();
    return 0;
}
```

此外，一个 C 库文件，其头文件是 f.h，产生的 lib 文件是 f.lib，如要在 C++ 中使用该库文件：

```c
extern "C" {
#include "f.h"
}
```
