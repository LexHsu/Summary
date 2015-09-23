
内存管理
=========

### 内存泄露与野指针

- 指针销毁了，所指的空间没有释放，即内存泄露。
- 内存被释放后，指针没有置 NULL，那块内存数据是未知的，该指针即为野指针。

### 内存常见分配方式

- 静态区，分为BSS段(BSS Segment)和数据段(Data Segment)。BSS段存放程序中未初始化的全局变量和静态变量，数据段存放已经初始化的全局变量和静态变量。程序结束后由系统释放。
- 栈，一般存放函数的参数值，局部变量(不包括static声明的变量)。其操作方式类似于数据结构中的栈。函数结束后自动释放。
- 堆，一般由手动分配，使用 malloc() 申请指定大小的内存，使用结束后要手动调用 free() 函数手动释放，其与数据结构中的堆是两种概念，分配方式类似于链表。
- 文字常量区，常量字符串就是放在这里的。程序结束后由系统释放。
- 程序代码区，存放函数体二进制代码，内存区域通常属于只读，有些架构可写。

![memory](img/memory.jpg)


### 内存使用规则

- 使用  malloc() 申请空间时，要检查是否分配成功，分配之后判断指针是否为 NULL 即可。
- 申请成功后最好使用 memset() 将内存清空为 0，否则可能存在垃圾数据或乱码。
- 记得为数组和动态内存赋初值，避免未初始化的内存作为右值使用。
- 防止数组或指针内存越界。
- 动态内存使用结束后要释放，系统不会自动释放手动分配的内存。
- 内存释放后，指针还是指向那块地址，不过该指针已经是野指针，要让该指针要指向 NULL。

### 给指针分配内存

1. 动态在堆上分配一块内存，让指针指向这块内存。
2. 指针指向栈上的一块内存，一般是定义一个非指针变量，让指针指向该变量。

```c
#include <stdio.h>
#include <malloc.h>

int a = 0;                   // 全局初始化区
char *p1;                    // 全局未初始化区

int main() {  
    int b;                   // 栈，由系统自动在栈上分配一块内存，不需要用户管理
    char s[] = "abc";        // 栈
    char *p2;                // 栈
    char *p3 = "123456";     // 123456\\0在常量区，p3在栈上。
    static int c =0;         // 全局（静态）初始化区
    p1 = (char *)malloc(10);
    p2 = (char *)malloc(20); // 分配得来得10和20字节的区域就在堆区。
    strcpy(p1, "123456");    // 123456\\0放在常量区，编译器可能会将它与p3所指向的"123456"优化成一个地方。
    return 0;
}
```

### 示例

```c
char* test1() {
    char *s = "abc";
    return s;
}  

char* test2() {  
    char s[] = "abc";
    return s;
}
int main() {
    printf("%s\n", test1());  // abc
    printf("%s\n", test2());  // 乱码
    return 0;
}
```

test1中，指针s为局部变量，被分配在栈空间，作用域是函数内部。在函数返回s指向的地址后，s即被销毁，但s指向的内容"abc"是常量，被分配在程序的常量区。直到整个程序结束才被销毁。因此打印正确。
而test2中s为数组，分配到栈空间，"abc"分别以单个字符放到数组中，一旦函数退出，栈中这块内存就被释放。虽然返回一个地址，但结果未知。

### 堆栈

- 申请方式

```
stack: 由系统自动分配。 例如，声明在函数中一个局部变量 int b; 系统自动在栈中为b开辟空间
heap: 需要程序员自己申请，并指明大小，在c中malloc函数
如p1 = (char *)malloc(10);
在C++中用new运算符
如p2 = (char *)malloc(10);
但是注意p1、p2本身是在栈中的。
```

- 申请后系统响应

栈：只要栈的剩余空间大于所申请空间，系统将为程序提供内存，否则将报异常提示栈溢出。

堆：首先操作系统有一个记录空闲内存地址的链表，当系统收到程序的申请时， 会遍历该链表，寻找第一个空间大于所申请空间的堆结点，然后将该结点从空闲结点链表中删除，并将该结点的空间分配给程序，另外，对于大多数系统，会在这块内存空间中的首地址处记录本次分配的大小，这样，代码中的delete语句才能正确的释放本内存空间。另外，由于找到的堆结点的大小不一定正好等于申请的大小，系统会自动的将多余的那部分重新放入空闲链表中。

- 申请效率

栈由系统自动分配，速度较快。但程序员是无法控制的。

堆是由new分配的内存，一般速度比较慢，而且容易产生内存碎片,但使用方便。

2.5堆和栈中的存储内容
栈： 在函数调用时，第一个进栈的是主函数中后的下一条指令（函数调用语句的下一条可执行语句）的地址，然后是函数的各个参数，在大多数的C编译器中，参数是由右往左入栈的，然后是函数中的局部变量。注意静态变量不入栈。
当本次函数调用结束后，局部变量先出栈，然后是参数，最后栈顶指针指向最开始存的地址，也就是主函数中的下一条指令，程序由该点继续运行。

堆：一般是在堆的头部用一个字节存放堆的大小。堆中的具体内容有程序员安排。

- 存取效率

```c
char s1[] = "abc";      // 运行时才赋值
char *s2 = "def";       // 编译时确定
// 但在以后的存取中，栈上的数组比指针所指向的字符串(例如堆)快。
```



### 指针与数组

数组可修改，但指针指向的常量字符串不可修改，虽然编译器编译时不会保存，但执行时会出错。

```c
int main() {
    char str[] = "hello"; // 存储了6个字符 "hello\0"
    char *p = "word";     // 指针指向了常量字符串，常量字符串不可修改
    str[0] = 'x';         // 正确
    // p[0] = 'x';        // 编译器不会报错，但执行会出错
    printf("%s\n", str); // xello
    printf("%c\n", *p);   // w
    printf("%c\n", p[0]); // w
    printf("%s\n", p);    // word
    return 0;
}
```

- 内容复制使用 strcpy() 函数，内容比较使用 strcmp() 函数。

```c
// 数组例子
char a[] = "hello";
char b[10];
strcpy(b, a);            // 不可用 b = a;
if (strcmp(b, a) == 0)   // 不可用 if (b == a)

// 指针例子
int len = strlen(a);
char *p = (char *)malloc(sizeof(char) * (len + 1));
strcpy(p, a);            // 不可用 p = a;
if (strcmp(p, a) == 0)   // 不可用 if (p == a)
```

- 当数组作为函数的参数进行传递时，自动退化为同类型的指针，如sizeof(a) 退化为 sizeof(char *)。

```c
void Func(char a[100]) {
    printf("%s\n", sizeof(a));  // 4，不是 100
}
```

### 指针的内存的传递

- 如果函数的参数是指针，不可使用该参数申请内存空间，这样函数结束时无法释放内存，导致内存泄露。

```c
#include <stdio.h>

void GetMemory(char *p) {
    p = NULL;
    p = (char *)malloc(sizeof(char));
    memset(p, 0, sizeof(char));
    if (p) {
        printf("p 申请内存成功\n");
    }
}

int main() {
    char *p1 = NULL;
    GetMemory(p1);
    if (p1) {
        printf("p1 申请内存成功\n");
    } else {
        printf("p1 申请内存不成功\n");
    }
    return 0;
}

// result:
// p 申请内存成功
// p1 申请内存不成功
```
这是因为传入函数 GetMemory 内的是指针p的副本，因此是其副本新分配了空间，而 p 未变。

- 解决方法一，可传入指向 p 的指针，即二级指针。

```c
void GetMemory(char **p) {
    *p = NULL;
    *p = (char *)malloc(sizeof(char));
    memset(p, 0, sizeof(char));
    if (*p) {
        printf("p 申请内存成功\n");
    }
}

int main() {
    char *p1 = NULL;
    GetMemory(&p1); // 这里是 &p1
    if (p1) {
        printf("p1 申请内存成功\n");
    } else {
        printf("p申请内存不成功\n");
    }
    return 0;
}
// result:
// p 申请内存成功
// p1 申请内存成功
```

- 解决方法二，返回内存地址。

```c
char * GetMemory() {
    char *p = NULL;
    p = (char *)malloc(sizeof(char)); // 在堆上动态分配
    memset(p, 0, sizeof(char));
    return p;
}

int main() {
    char *p1 = NULL;
    p1 = GetMemory();
    if (p1) {
        printf("p1 申请内存成功\n");
    } else {
        printf("p1 申请内存不成功\n");
    }
    return 0;
}
// result:
// p1 申请内存成功
```

注意，使用返回内存地址的方式要通过 malloc 分配，否则容易出错。
上面例子是通过 malloc 动态分配在堆上，GetMemory 函数调用结束不会释放。
而如果是在栈上分配的，如函数内的数组，当 GetMemory() 函数结束时栈内存也被释放，因此"栈内存"的指针便是乱码，如下：

```c
// 返回栈内存地址
char *GetMemory() {
    char p[] = "hello world";      // 栈上分配
    return p;
}

int main() {
    char *p1 = NULL;
    p1 = GetMemory();
    if (p1) {
        printf("p1 申请内存成功\n");
        printf("%s\n", p1);        // 输出了乱码
    } else {
        printf("p1 申请内存不成功\n");
    }
    return 0;
}

// result:
// p1 申请内存成功
// @%%%@~
```

- 注意指向常量字符串的指针。

```c
char *getString(void) {
    char *p = "hello world";
    return p;
}

int main() {
    char *str = NULL;
    str = getString2();
    printf("%s\n", str);
    return 0;
}
```

虽然运行结果是正确的，但是函数 GetString 的实现思路却是错误的。因为 GetString 内的 “hello world”是常量字符串，位于静态存储区，
其在程序生命期内恒定不变。无论什么时候调用getString，返回的始终是同一个“只读”的内存块。
