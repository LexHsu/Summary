undefined reference
===

常见 undefined reference 错误的各种原因以及解决方法。

### 链接时目标文件（.o）缺失

```c
// main.c
int main() {
    test();
}

// test.c
#include <stdio.h>

void test() {
    printf("test.\n");
}
```

编译命令：

```
gcc -c test.c
gcc –c main.c

得到 main.o 与 test.o ，然后链接 .o 目标文件：
gcc -o main main.o
或
gcc -o main main.c

提示如下：
main.o: In function `main':  
main.c:(.text+0x7): undefined reference to `test'  
collect2: ld returned 1 exit status

正确方式
gcc -o main main.o test.o
或
gcc -o main main.c test.c
```

### 链接时缺少相关的库文件（.a/.so）

```c
// main.c
int main() {
    test();
}

// test.c
#include <stdio.h>

void test() {
    printf("test.\n");
}

// test.h
void test();
```

编译命令：

```
先把 test.c 编译成静态库(.a)文件
gcc -c test.c
ar -rc test.a test.o
得到 test.a 静态库文件后。开始编译main.c
gcc -c main.c

生成 main.o 文件后，再链接，希望得到可执行程序。
gcc -o main main.o

编译器报错：
/tmp/ccCPA13l.o: In function `main':  
main.c:(.text+0x7): undefined reference to `test'  
collect2: ld returned 1 exit status

找不到 test() 函数的实现文件，因为其实现在 test.a 这个静态库中。

链接命令修改为如下形式即可
gcc -o main main.o ./test.a
或
gcc -o main main.c ./test.a
```

### 链接的库文件使用了另一个库文件

```c
// func.h
int func();

// func.c
#include <stdio.h>

int func() {
    printf("call func.\n");
    return 0;
}

// test.h
void test();

// test.c
#include <stdio.h>
#include "func.h"

void test() {
    printf("enter test.\n");
    func();
}

// main.c
#include "test.h"

int main() {
    test();
}
```

编译命令：

```
先对 fun.c，test.c，main.c 进行编译，生成 .o 文件。
gcc -c func.c  
gcc -c test.c  
gcc -c main.c

然后，将 test.c 和 func.c 各自打包成为静态库文件。
ar –rc func.a func.o
ar –rc test.a test.o

将 main.o 链接为可执行程序：
gcc -o main main.o test.a

编译器报错：
test.a(test.o): In function `test':  
test.c:(.text+0x13): undefined reference to `func'  
collect2: ld returned 1 exit status

还需要将test.a所引用到的库文件也加进来才能成功链接。
gcc -o main main.o test.a func.a

如果库或者程序中引用了第三方库（如pthread.a）
则同样在链接的时候需要给出第三方库的路径和库文件。
```

### 多个库文件链接顺序问题

```
如果修改上文链接的库文件的顺序，如：

gcc -o main main.o func.a test.a

报错如下：
test.a(test.o): In function `test':  
test.c:(.text+0x13): undefined reference to `func'  
collect2: ld returned 1 exit status

因此，需要注意库之间的依赖顺序，依赖其他库的库要放到被依赖库的前面。
```

### 在 c++ 中链接 c 库

c++ 代码中链接 c 库中的函数时，也会遇到 undefined reference 错误，如下例。

```c
// test.h
void test();

// test.c
#include <stdio.h>

void test() {
    printf("test.\n")
}

// 打包为静态库
gcc -c test.c
ar -rc test.a test.o
```

```c
#include "test.h"

int main() {
    test();
    return 0;
}
```

编译 main.cpp 生成可执行程序：
```
g++ -o main main.cpp test.a

// 报错如下：
/tmp/ccJjiCoS.o: In function `main':
main.cpp:(.text+0x7): undefined reference to `test()'
collect2: ld returned 1 exit status

原因是 main.cpp 为 c++ 代码，调用了 c 语言库的函数，因此链接的时候找不到，
解决方法：即在 main.cpp 中，把与 c 语言库 test.a 相关的头文件包含添加一个extern "C"的声明即可。
```

修改后的 main.cpp 如下：

```c
extern "c" {
    #include "test.h"
}

int main() {
    test();
    return 0;
}
```

`g++ -o main main.cpp test.a` 编译 OK。
