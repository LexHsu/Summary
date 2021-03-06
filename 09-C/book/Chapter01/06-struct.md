结构体
===

### 声明

- 显式声明。

```
struct 结构体名 {
    成员列表;        // 注意分号
};                  // 注意分号
```

- 声明并定义。

```
struct 结构体名 {
    成员列表;
}结构体变量名列表;

struct student {
    int num;
    char name[20];
}stu1, stu2;
```

- 声明定义并初始化。

```
struct student {
    int num;
    char name[20];
    char sex;
}stu = {111, "bill", 'M'};
```

- 直接定义匿名结构体类型变量。

```
struct {
    成员列表
}结构体变量名列表;

struct {
   int i, j;
}point;
```
声明一个匿名结构体并定义一个结构体变量 point，分配相应内存空间。

- 不同的匿名结构体变量，类型不同

```c
struct {
   int i,j;
}p1,p2;

struct {
   int i,j;
}p3;

p1 = p2   // ok
p1 = p3   // error: incompatible types when assigning to type ‘struct <anonymous>’ from type ‘struct <anonymous>’
```
可见两者的实际类型是不一样的。

- 用 typedef 简化结构体的写法。

```c
typedef struct {
   int i,j;
}node;

node n1;
```

### 定义

- 结构体在定义的时候分配内存，全局的结构体变量有默认值，定义结构类型有多种灵活的方式。

```c
int main(int argc, char* argv[]) {

    /* 直接定义结构类型和变量 */
    struct { int x; short y; } a = { 1, 2 }, a2 = {};
    printf("a.x = %d, a.y = %d\n", a.x, a.y);

    /* 函数内部也可以定义结构类型 */
    struct data { int x; short y; };
    struct data b = { .y = 3 };
    printf("b.x = %d, b.y = %d\n", b.x, b.y);

    /* 复合字面值 */
    struct data* c = &(struct data){ 1, 2 };
    printf("c.x = %d, c.y = %d\n", c->x, c->y);

    /* 也可以直接将结构体类型定义放在复合字面值中 */
    void* p = &(struct data2 { int x; short y; }){ 11, 22 };

    /* 相同内存布局的结构体可以直接转换 */
    struct data* d = (struct data*)p;
    printf("d.x = %d, d.y = %d\n", d->x, d->y);
    return 0;
}

// Result:
a.x = 1, a.y = 2
b.x = 0, b.y = 3
c.x = 1, c.y = 2
d.x = 11, d.y = 22
```

### 初始化

- 结构体的初始化和数组一样简洁方便，包括使用初始化器初始化特定的某些成员。未被初始化器初始化的成员将默认设置为 0。

```c
typedef struct {
    int x;
    short y[3];
    long long z;
} data_t;

int main(int argc, char* argv[]) {
    data_t d = {};
    data_t d1 = { 1, { 11, 22, 33 }, 2LL };
    data_t d2 = { .z = 3LL, .y[2] = 2 };
    return 0;
}

// Result:
d = {x = 0, y = {0, 0, 0}, z = 0}
d1 = {x = 1, y = {11, 22, 33}, z = 2}
d2 = {x = 0, y = {0, 0, 2}, z = 3}
```

- 再看一例：

```c
typedef struct {
   char *p;
   int i;
   char ch[256];
}mystr;

mystr str; // 定义一个变量,此时已为之分配了空间

// 如 str 变量定义在全局作用域，则默认：
// str.p = NULL, str.i = 0, str.ch 数组都是 '\0'
// 否则，所有值都是野值

mystr str2={"abc", 2, "def"};
mystr str3={.p = "abc", .ch="def"};  // 初始化结构体中的某些变量
mystr str4={.ch[256] = "def"};       // Error: array index in initializer exceeds array bounds
mystr str5={.ch[10] = "def"};        // OK
```
注意 str4 和 str5。
对于数组，如 char a[size]，传递给常量字符指针，可以是 `"a"`，也可以是 `"a[n]"`，其中`0 <= n < size`，编译器会忽略掉 n。
但不能是`"a[size]"`，编译器报 "array index in initializer exceeds array bounds" 错误。

- 结构类型无法把自身作为成员类型。

```c
typedef struct {
   int i,j;
   Node n1;
}node;

// 上述代码错误之处：
// 1. 结构体自身相互包含嵌套，编译器无法知道结构体的空间大小，无法通过编译
// 2. typedef 还没有将结构体命名为 Node，在结构体中也无法使用 Node

// 可包含指向自身的指针
typedef struct node{
   int i,j;
   struct node *n1;
}node;
```

- 虽然结构类型无法把自身作为成员类型，但可以包含指向自身类型的指针成员。

```c
struct list_node {
    struct list_node* prev;
    struct list_node* next;
    void* value;
};
```

- 定义不完整结构类型，只能使用小标签，像下例的 typedef 类型名称是错误的。

```c
typedef struct {
    list_node* prev;
    list_node* next;
    void* value;
} list_node;

// 编译出错:
// $ make
gcc -Wall -g -c -std=c99 -o main.o main.c
main.c:15: error: expected specifier-qualifier-list before ‘list_node’

// 正确用法如下
typedef struct {
    struct list_node* prev;
    struct list_node* next;
    void* value;
} list_node;
```

- 通常用法。

```c
typedef struct node_t
{
    struct node_t* prev;
    struct node_t* next;
    void* value;
} list_node;

// 标签可与 typedef 定义的类型名相同。
typedef struct node_t
{
    struct node_t* prev;
    struct node_t* next;
    void* value;
} node_t;
```

### 匿名结构体

```c
typedef struct {
    struct {
        int length;
        char chars[100];
    } s;
    int x;
} data_t;

int main(int argc, char * argv[]) {
    data_t d = { .s.length = 100, .s.chars = "abcd", .x = 1234 };
    printf("%d\n%s\n%d\n", d.s.length, d.s.chars, d.x);
    return 0;
}
```

- 也可直接定义一个匿名变量。

```c
int main(int argc, char * argv[])
{
    struct { int a; char b[100]; } d = { .a = 100, .b = "abcd" };
    printf("%d\n%s\n", d.a, d.b);
    return EXIT_SUCCESS;
}
```

### 不定长结构体

- 不定长结构体也称为弹性结构体，即在结构体尾部声明一个未指定长度的数组。 通过 sizeof 运算符时，该数组未计入结果。

```c
typedef struct string {
    int length;
    char chars[];
} string;

int main(int argc, char * argv[]) {
    int len = sizeof(string) + 10; // 计算存储一个10 字节长度的字符串（包括\0）所需的长度
    char buf[len];                 // 从栈上分配所需的内存空间
    string *s = (string*)buf;      // 转换成 struct string 指针
    s->length = 9;
    strcpy(s->chars, "123456789");
    printf("%d\n%s\n", s->length, s->chars);
    return 0;
}

// Result:
9
123456789
```

考虑到不同编译器和 ANSI C 标准的问题，也用 char chars[1] 或 char chars[0] 来代替。对这类结构体进行拷贝的时候，尾部结构成员不会被复制，且不能直接对弹性成员进行初始化。

```c
typedef struct string {
    int length;
    char chars[];
} string;

int main(int argc, char * argv[]) {
    int len = sizeof(string) + 10;
    char buf[len];
    string *s = (string*)buf;
    s->length = 10;
    strcpy(s->chars, "123456789");
    string s2 = *s;                            // 复制 struct string s
    printf("s2.length:%d\n", s2.length);       // s2.length 正常
    printf("s2.chars:%s\n", s2.chars);         // s2.chars 错误，下文输出结果为空串
    return 0;
}

// Result:
10

```

### 结构体偏移量

- 通过 stddef.h 中的 offsetof 宏可获取结构成员的偏移量。

```c
#include<stdio.h>
#include<stddef.h>

typedef struct {
    int x;
    short y[3];
    long long z;
} data_t;

int main(int argc, char* argv[]) {
    printf("x %d\n", offsetof(data_t, x));
    printf("y %d\n", offsetof(data_t, y));
    printf("y[1] %d\n", offsetof(data_t, y[1]));
    printf("z %d\n", offsetof(data_t, z));
    return 0;
}

// Result: 注意输出结果有字节对齐
x 0
y 4
y[1] 6
z 12
```

### 结构体指针

一个结构体变量的指针就是该变量所占据的内存段的起始地址。

```c
struct student {
    int num;
    char name[20];
}
struct student stu1;
struct student *p;
p=&stu1;

// 以下三种形式等价
stu1.num
(*p).num
p->num
```

- 用结构体变量和指向结构体的指针做函数参数。

1. 用结构体变量的成员作参数－－值传递。
2. 用结构体变量作实参－－值传递，复制结构体。
3. 用指向结构体变量（和数组）的指针作实参－－将结构体变量（或数组）的地址传递给形参。
4. 用指针作函数的参数较好，可提高执行效率。

- 用scanf给结构体赋值。

```c
scanf("%d%s",&stu.num,stu.name);
// 输入:123 bill（用空格隔开）
```

- 动态生成结构体变量。

```c
mystr * pstr = (mystr*)malloc(sizeof(mystr)); // 注意用完后释放掉(free)
pstr->p = "abc";

// 如果结构体内部也有动态生成的成员变量，在释放结构体之前要先释放掉其内部的内存空间
pstr->p = (char*)malloc(sizeof(char) * 256);
free(pstr->p);
free(pstr);
```
