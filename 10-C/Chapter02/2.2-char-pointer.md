字符数组与字符指针
===

### 字符数组

- 数组名是数组的首地址，是一个指针常量，确定存储位置后就不能改变。字符数组初始化如下。

```c
char a[10] = "hello";               // 方式一，定义候直接用字符串赋值
char a[10] = {'h','e','l','l','o'}; // 方式二，对数组中字符逐个赋值
char a[10];
strcpy(a, "hello");                 // 方式三，使用 string.h 中的 strcpy 库函数
a = "hello";                        // 错误，a 为常量指针，即 const *a，已指向堆栈中分配的 10 个字符空间，无法修改指向常量区
```

### 字符指针

- 而字符串指针变量是一个变量，该变量保存的字符串常量首地址是可以改变的，即字符串指针变量可以指向不同的字符串常量，字符指针初始化如下。

```c
char *p, a = '5';
p = &a;          // OK
p = "hello";     // OK，注意与字符数组初始化的区别
```

字符串赋值给字符指针，系统做了 3 件事：

1. 申请了空间(在静态常量区)，存放了字符串。
2. 在字符串尾加上了`'\0'`。
3. 返回这些字符存储的地址。

注意：
在指针运算时，编译器会自动识别类型，如 int 型指针要获取下一个地址，直接 p++ 即可，不需要 p + 4。
void 指针不可进行指针运算，应为 void 型编译器不能识别类型的长度（即指针所指对象的体积），如 `p++` 是非法的，
既不可进行数学运算，也不可使用 `*` 取值操作，想使用必须转换为具体指针类型。

### 字符串与字符数组与字符指针

编译器都会为字符串自动添加一个0作为结束符，如在代码中写 `"abc"`，编译器自动存储为 `"abc\0"`。

- 字符串赋值给字符数组。
```c
char str[] = "abc";  // 等价于 char str[4] = {'a','b','c','\0'};
```
- 字符串赋值给字符指针。

```c
char *p = "abc";     // 字符串 "abc\0" 分配在常量区，若在函数内，p 在栈上
ptr[0] = 'x';        // 编译可通过，但运行时异常，常量区中的数据不可修改
```
- 数组的类型是由该数组所存放的东西的类型以及数组本身的大小决定。

```c
char s1[3];    // s1 的类型就是 char[3]
char s2[4];    // s2 的类型就是 char[4]

```
可见，尽管 s1 和 s2 都是字符数组，但两者的类型却是不同的。"abc" 类型可认为是 const char[4]。

- sizeof 返回操作数占用内存空间大小，单位字节 (byte)。sizeof 返回值是 size_t 类型，操作数可以是类型和变量。不要用 int 代替 size_t，因为在 32 位和 64 位平台 size_t 长度不同。

```c
#include<stdio.h>

int main() {

    char a[] = "hello";
    char *p = "hello";
    printf("sizeof(a[]) = %d, sizeof(*p) = %d\n", sizeof(a), sizeof(p));

    int b[3]={1,2,3};
    printf("sizeof(b[]) = %d\n", sizeof b);
    char *s = a;
    char *x = "good";
    printf("sizeof(*s) = %d, sizeof(*x) = %d\n", sizeof s, sizeof x);

    int c = 4;
    printf("sizeof(c) = %d, sizeof(int) = %d", sizeof c, sizeof(int));
    return 0;
}

// Result:
sizeof(a[]) = 6, sizeof(*p) = 4
sizeof(b[]) = 12
sizeof(*s) = 4, sizeof(*x) = 4
sizeof(c) = 4, sizeof(int) = 4
```

### 内存结构

```
字符数组：
    +------------------------+
    | h | e | l | l | o |'\0'|
    +------------------------+
    |<--------  a  --------->|

字符指针：
       +---+
    |--| p |
    |  +---+
    |
    |  +------------------------+
    -->| h | e | l | l | o |'\0'|
       +------------------------+
```

执行 `char a[6] = "hello";` 时，系统便分配了长度为 6 字节的内存空间，名为 a，a 为常量指针，其指向的地址与自身的地址相同。
而字符指针 `char *p = "hello";` 时，内存分配了两段内存，一个名为 p，类型是一个字符指针，另外一段是一个字符串常量，且 p 的内容，即 p 的值为字符常量的首地址，无法通过 p 修改这段字符串，因为字符串分配在常量区。如下例：

- 字符数组实例。

```c
#include <stdio.h>

int main() {

    char s[6] = "hello";
    printf("&s = %x\n", &s);          // 指针变量 s 的内存地址，即 a 字符地址
    printf("&s[0] = %x\n", &s[0]);    // 数组中第一个变量的地址
    printf("s = %x\n", s);            // s 为 数组变量退化的指针，值即为数组首字节地址
    printf("\n");
    char *p = "world";
    printf("&p = %x\n", &p);          // 字符指针 p 的地址
    printf("&p[0] = %x\n", &p[0]);    // 字符串常量第一个字符的地址
    printf("p = %x\n", p);            // 指针 p 的值

    return 0;
}

// Result:
&s = ffdd3986
&s[0] = ffdd3986
s = ffdd3986

&p = ffdd3980
&p[0] = 8048645
p = 8048645
```

- `&s`：该字符指针的地址
- `&s[0]`：指针指向字符常量的首字符地址
- `s`：该字符指针的值，即字符常量的首地址

可见字符数组分配的是连续的内存空间，指针 s 的值与指向的内存地址即第一个字符地址是相同的。而字符指针 p 分配在栈上，字符串分配在常量区。

- 字符指针实例。

```c
#include <stdio.h>

int main() {
    char* p = "abcd";
    // 将指针 p 指向的内容格式化为 “字符串” 类型
    printf("p = %s\n", p);

    // 将指针 p 的值，即字符串地址转换成 int 类型, 不同时间结果可能不同，地址可能会发生变化
    printf("p = %d\n", p);

    // 将指针的值，即字符串常量的地址转换成字符型，不同时间运行结果可能不同，地址可能会发生变化
    printf("p = %c\n", p);

    // 将字符串常量 "abcd" 的地址所隐含的内容转换成字符型，*p 长度本身就是 1
    printf("*p = %c\n", *p);

    // 不可将字符转换为字符串
    // printf("*p = %s\n", *p);

    return 0;
}
// Result:
p = abcd
p = 134514152
p = �
*p = a
```

### 常见错误

```c
// Program 1
#include <stdio.h>

int main() {
    char a[5] = "abcd";
    a[3] = 'g';
    printf("a is %s\n", a);
    return 0;
}

// Result:
abcg

// Program 2
#include <stdio.h>

int main() {
    char *a = "abcd";
    a[3] = 'g';
    printf("a is %s\n", a);
    return 0;
}

// Result:
Segmentation fault (core dumped)

```
字符指针指向的字符串"abcd"是字符串常量，存储在文字常量区，常量的值不可修改。

```c
// Program 1
#include <stdio.h>

char *func() {
    char a[5] = "abcd";
    return a;
}

int main() {
    char *recv;
    recv = func();
    printf("recv is %s\n", recv);
    return 0;
}

// Result: 编译时会有警告：warning: function returns address of local variable
recv is recv is ���k

// Program 2
#include <stdio.h>

char *func() {
    char *a = "abcd";   // 这条语句就没有警告
    return a;
}

int main() {
    char *recv;
    recv = func();
    printf("recv is %s\n", recv);
    return 0;
}

// Result:
abcd
```
字符串数组变量在函数结束时便被回收，而字符串指针的字符串分配常量存储区。

```c
// Program 1
#include <stdio.h>
#include <string.h>   // strcpy

char *func(char a[]) {
    strcpy(a, "abcd");
    return a;
}

int main() {
    char recv[5];
    func(recv);
    printf("recv is %s\n", recv);
    return 0;
}

// Result:
recv is abcd

// Program 2
#include <stdio.h>

char *func(char *a) {
    a = "abcd";
    return a;
}

int main() {
    char *recv;
    func(recv);
    printf("recv is %s\n", recv);
    return 0;
}

// Result:
recv is ��;
```
第一个程序在 main 函数没有结束之前，字符数组 recv 一直可用。 改变了其中的值可以反映出来。
第二个错误是由于 c 语言中，参数的传递都是值传递，包括指针。因此 func 函数没有对 recv 做任何操作，只是对它的拷贝 a 进行了操作。
可用二级指针解决该问题：

```c
#include <stdio.h>

char *func(char **a) {
    *a = "abcd";
    return *a;
}

int main() {
    char *recv;
    func(&recv);
    printf("recv is %s\n", recv);
    return 0;
}

// Result:
recv is abcd
```
