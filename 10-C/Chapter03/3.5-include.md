include
===

### 为何需要 include

对于一些简单代码，可不使用 include 预编译命令，直接在调用之前添加函数声明即可：

```c
// a.c
void test_a() {
    return;
}

// b.c，函数声明
void test_a();

void test_b() {
    test_a();
}
```

对于较大的工程，若还是像上文这样，必然效率太低，且扩展性差。
如某个函数参数发生变化，其每一处的函数声明都需要修改。 于是便有了 `#include`。

```c
// a.c
void test_a() {
     return;
}

// a.h
void test_a();

// b.c
#include "a.h"

void test_b() {
    test_a();
}
```

编译时，遇到 `#include "a.h"`，会把整个 `a.h` 文件都拷贝到 `b.c` 的开头，如下：

```c
// b.c，预编译后的临时文件
void test_a();

void test_b() {
    test_a();
}
```

在 `Linux` 下，可用 `gcc -E b.c` 查看预编译 `b.c` 后的结果。

### static 关键词

1）函数重复定义。

```c
// a.c
void test() {
    return;
}

// b.c
void test() {
    return;
}
```

编译时不会报错，但是链接时报错：

```
multiple definition of `test'
```

因为在同一个工程里面出现了两个 test 函数的定义。

2）函数实现放在头文件里。

```c
// a.h
void test_a() {
   return;
}

// b.c
#include "a.h"

void test_b() {
    test_a();
}
```

预编译后，会发现，b.c 被修改为如下形式：

```c
// b.c 预编译后的临时文件
void test_a() {
   return;
}

void test_b() {
    test_a();
}
```

当然，这样目前是没有什么问题的，可以正常编译链接成功。但是，如果有一个 c.c 也包含的 a.h 的话，怎么办？

```c
// c.c
#include "a.h"

void test_c() {
    test_a();
}
```

同上，`c.c` 在预编译后如下代码：

```c
// c.c 预编译后的临时文件
void test_a() {
    return;
}

void test_c() {
    test_a();
}
```
链接时报错：`multiple definition of `test_a'`

这就是在头文件里写函数实现的弊端。
但有时会将一个函数设置为内联函数，且放在 `.h` 文件里。为解决上述问题，需要 static 关键字

### static

用 static 修饰函数，表明该函数只能在本文件中使用，当不同的文件中有相同的函数名被 static 修饰时，不会产生重复定义的错误。如：

```c
// a.c
static void test() {
    return;
}

void test_a() {
    test();
}

// b.c
static void test() {
    return;
}

void test_b() {
    test();
}
```

编译时不会报错，但是 test() 函数只能被 `a.c` 和 `b.c` 中的函数调用，不能被 `c.c` 等其他文件中的函数调用。
那么，用 static 修饰 `.h` 文件中定义的函数，会有什么效果呢？

```c
// a.h
static void test() {
    return;
}

// b.c
#include "a.h"

void test_b() {
    test();
}

// c.c
#include "a.h"

void test_c() {
    test();
}
```
预编译后 `b.c` 和 `c.c` 文件中，由于 `#include "a.h"` ，故在这两个文件开头都会定义 `static void test()` 函数，
因此 `test_b()` 和 `test_c()` 均调用的是自己文件中的 `static void test()` 函数 ， 因此不会产生重复定义的报错。

因此，在 `.h` 文件中定义函数的话，建议一定要加上 static 关键词修饰，这样在被多个文件包含时，才不会产生重复定义的错误。

### 防止头文件重复包含

为了防止头文件重复包含，头文件中一般会加上如下代码：

```c
#ifndef    XXX
#define    XXX  
......
#endif
```

其不是为了防止多个文件包含某一个头文件，而是为了防止一个头文件被同一个文件包含多次。具体说明如下：

```c
// a.h
static void test_a() {
    return;
}

// b.c
#include "a.h"

void test_b() {
    test_a();
}

// c.c
#include "a.h"

void test_c() {
    test_a();
}
```

这样是没有问题的，但下面这种情况就会有问题。


```c
// a.h
static void test_a() {
    return;
}

// b.h
#include "a.h"

// c.h
#include "a.h"

// main.c
#include "b.h"
#include "c.h"

void main() {
    test_a();
}
```

`b.h` 和 `c.h` 均包含了 `a.h`，那么，在预编译 `main.c` 文件时，会展开为如下形式：

```c
// main.c 预编译之后的临时文件
static void test_a() {
    return;
}

static void test_a() {
    return;
}

void main() {
    test_a();
}
```

在同一个 .c 里面，出现了两次 test_a() 的定义，因此，会出现重复定义的报错，修改如下：

例如，上面的 a.h 改为：
```c
//a.h 文件

#ifndef  A_H
#define  A_H

static void test_a() {
    return;
}

#endif
```

预编译展开main.c则会出现：

```c
// main.c 预编译后的临时文件
#ifndef A_H
#define A_H

static void test_a() {
    return;
}

#endif

#ifndef A_H
#define A_H

static void test_a() {
    return;
}

#endif

void main(){
    test_a();
}
```
在编译 `main.c` 时，当遇到第二个 `#ifndef  A_H` 时，由于已定义过 `A_H`，故此段代码被跳过，不会编译，
因此不会产生重复定义错误。
