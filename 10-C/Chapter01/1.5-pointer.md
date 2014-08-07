指针
===

### void 指针

`void*` 指针又称万能指针，可代表任意对象的地址，但无该对象的类型，即转型后才能进行对象操作。`void*` 指针可与其他任何类型指针进行隐式转换。

```c
void test(void* p, size_t len) {
    unsigned char* cp = p;
    for (int i = 0; i < len; i++) {
        printf("%02x ", *(cp + i));
    }
    printf("\n");
}

int main() {
    int x = 0x00112233;
    test(&x, sizeof(x));
    return 0;
}

// Result:
33 22 11 00
```

### 初始化指针

可以用初始化器初始化指针。

1. 空指针常量 NULL。
2. 相同类型的指针，或者指向限定符较少的相同类型指针。
3. void 指针。

非自动周期指针变量或静态生存期指针变量必须用编译期常量表达式初始化，如函数名。

```c
char s[] = "abc";
char* sp = s;
int x = 5;
int* xp = &x;

void test() {}
typedef void(*test_t)();

int main() {
    static int* sx = &x;
    static test_t t = test;
    return 0;
}
```

### 指针运算

- 对指针进行相等或不等运算来判断是否指向同一对象。

```c
int x = 1;
int *a, *b;
a = &x;
b = &x;
printf("%d\n", a == b);
```

- 对指针进行加法运算获取数组第 n 个元素指针。

```c
int x[] = { 1, 2, 3 };
int* p = x;
printf("%d, %d\n", x[1], *(p + 1));
```

- 对指针进行减法运算，以获取指针所在元素的数组索引序号。

```c
int x[] = { 1, 2, 3 };
int* p = x;
p++;
p++;
int index = p - x;
printf("x[%d] = %d\n", index, x[index]);

//Result:
x[2] = 3;
```

- 对指针进行大小比较运算，相当于判断数组索引序号大小。

```c
int x[] = { 1, 2, 3 };
int* p1 = x;
int* p2 = x;
p1++;
p2++;
p2++;
printf("p1 < p2? %s\n", p1 < p2 ? "Y" : "N");

// Result:
p1 < p2? Y
```

- 可直接用 `&x[i]` 获取指定序号元素的指针。

```c
int x[] = { 1, 2, 3 };
int* p = &x[1];
*p += 10;
printf("%d\n", x[1]);
// [] 优先级比 & 高，* 运算符优先级比算术运算符高。
```

### 数组指针

- 默认情况下，数组名为指向该数组第一个元素的指针常量。

```c
int x[] = { 1, 2, 3, 4 };
int* p = x;
for (int i = 0; i < 4; i++) {
    printf("%d, %d, %d\n", x[i], *(x + i), , *p++);
}
```
尽管可用 `*(x + 1)` 访问数组元素，但不能执行 x++ / ++x 操作。
但 "数组的指针" 和数组名并不是一个类型，数组指针将整个数组当做一个对象，而不是其中的成员(元素)。

注意下面代码的区别：

```c
int x[] = { 1, 2, 3, 4 };
int* p1 = x;             // 指向整数的指针
int (*p2)[] = &x;        // 指向数组的指针
for(int i = 0; i < 4; i++) {
    printf("%d, %d\n", *p1++, (*p2)[i]);
}
```
p1 的类型是 `int*`，也就是说它指向一个整数类型。数组名默认指向数组中的第一个元素，因此 x 默认也是 `int*` 类型。

p2 的含义是指向一个 "数组类型" 的指针，注意是 "数组类型" 而不是 "数组元素类型"，这有本质上的区别。

数组指针把数组当做一个整体，因为从类型角度来说，数组类型和数组元素类型是两个概念。因此 `p2 = &x` 中 x 代表的是数组本本身，不是数组的第一个元素地址，`&x` 取的是数组指针，而不是"第一个元素指针的指针"。

- 数组指针操作一维数组：

```c
void array1() {
    int x[] = {1,2,3,4,5,6};
    int (*p)[] = &x;             // 指针 p 指向数组
    for(int i = 0; i < 6; i++) {
        printf("%d\n", (*p)[i]); // *p 返回该数组, (*p)[i] 相当于 x[i]
    }
}
```

`p = &x` 使得指针 p 存储了该数组的指针，`*p` 自然就是获取该数组。那么 (*p)[i] 也就等于 x[i]。
注意: p 的目标类型是数组，因此 p++ 指向的不是数组下一个元素，而是 "整个数组之后" 的位置 (EA + SizeOf(x))，已超出数组范围。

- 数组指针操作二维数据：

```c
void array2() {
    int x[][4] = {{1, 2, 3, 4}, {11, 22, 33, 44}};
    int (*p)[4] = x;          // 相当于p = &x[0]
        for(int i = 0; i < 2; i++) {
            for (int c = 0; c < 4; c++) {
                    printf("[%d, %d] = %d\n", i, c, (*p)[c]);
                }
            p++;
        }
}
```

x 是一个二维数组，x 默认指向该数组的第一个元素，即 `{1, 2, 3, 4}`。注意第一个元素不是 int，而是一个 int[]，x 实际上是 `int(*)[]` 指针。因此 `p = x` 而不是 `p = &x`，否则 p 就指向 `int (*)[][]` 了。

既然 p 指向第一个元素，那么 `*p` 自然也就是第一行数组，即 {1, 2, 3, 4}，`(*p)[2]` 的含义就是第一行的第三个元素。p++ 的结果自然也就是指向下一行。还可以直接用 `*(p + 1)` 来访问 x[1]。

```c
void array2() {
    int x[][4] = {{1, 2, 3, 4}, {11, 22, 33, 44}};
    int (*p)[4] = x;
    printf("[1, 3] = %d\n", (*(p + 1))[3]);
}

//  int (*)[][] 示例
void array3() {
    int x[][4] = {{1, 2, 3, 4}, {11, 22, 33, 44}};
    int (*p)[][4] = &x;
    for(int i = 0; i < 2; i++) {
        for (int c = 0; c < 4; c++) {
            printf("[%d, %d] = %d\n", i, c, (*p)[i][c]);
        }
    }
}
```

这里 `p = &x`，也就是说把整个二维数组当成一个整体，因此 `*p` 返回的是整个二维数组，因此 p++ 也无法使用。

附: 在附有初始化的数组声明语句中，只有第一维度可省略。

- 将数组指针当做函数参数传递。

```c
#include<stdio.h>
void test1(p,len)
    int(*p)[];
    int len;
{
    int i;
    for(i = 0; i < len; i++) {
        printf("%d\n", (*p)[i]);
    }
}

void test2(void* p, int len) {
    int(*pa)[] = p;
    int i;
    for(i = 0; i < len; i++) {
        printf("%d\n", (*pa)[i]);
    }
}

int main () {
    int x[] = {1,2,3};
    test1(&x, 3);
    test2(&x, 3);
    return 0;
}
```

由于数组指针类型中有括号，因此 test1 的参数定义看着有些古怪。

### 指针数组

元素是指针的数组，通常用于表示字符串数组或交错数组。数组元素是目标对象 (可以是数组或其他对象) 的指针，而非实际嵌入内容。

```c
int* x[3] = {};
x[0] = (int[]){ 1 };
x[1] = (int[]){ 2, 22 };
x[2] = (int[]){ 3, 33, 33 };
int* x1 = *(x + 1);
for (int i = 0; i < 2; i++) {
    printf("%d\n", x1[i]);
    printf("%d\n", *(*(x + 1) + i));
}

// Result:
2
2
22
22
```

指针数组 x 是三个指向目标对象(数组)的指针，`*(x + 1)` 获取目标对象，即 x[1]。

指针数组是指元素为指针类型的数组，通常用来处理 "交错数组"，又称为数组的数组。 和二维数组不同，指针数组的元素只是一个指针，因此在初始化的时候，每个元素只占用 4 字节内存空间，远比二维数组节省。同时，每个元素数组的长度可以不同，这也是交错数组的说法。

```c
int main() {
    int x[] = {1,2,3};
    int y[] = {4,5};
    int z[] = {6,7,8,9};
    int* ints[] = { NULL, NULL, NULL };
    ints[0] = x;
    ints[1] = y;
    ints[2] = z;
    printf("%d\n", ints[2][2]);
    for(int i = 0; i < 4; i++) {
        printf("[2,%d] = %d\n", i, ints[2][i]);
    }
    return 0;
}

// Result:
8
[2,0] = 6
[2,1] = 7
[2,2] = 8
[2,3] = 9
```

指针数组存储的都是目标元素的指针。

默认情况下 ints 是哪种类型的指针呢？按规则来说，数组名默认是指向第一个元素的指针，那么第一个元素是什么呢？数组？当然不是，而是一个 `int*` 的指针而已。注意 `ints[0] = x;` 这条语句，实际上 x 返回的是 `&x[0]` 的指针 `int*`，而非 `&a` 这样的数组指针 `int (*)[]`。

继续，`*ints` 取出第一个元素内容 `0xbf880fdc`，这个内容又是一个指针，因此 ints 隐式成为一个指针的指针 `int**`，就交错数组而言，它默认指向 `ints[0][0]`。

```c
int main() {
    int x[] = {1,2,3};
    int y[] = {4,5};
    int z[] = {6,7,8,9};
    int* ints[] = { NULL, NULL, NULL };
    ints[0] = x;
    ints[1] = y;
    ints[2] = z;
    printf("%d\n", **ints);
    printf("%d\n", *(*ints + 1));
    printf("%d\n", **(ints + 1));
    return 0;
}

// Result:
1
2
4
```
第一个 printf 语句验证了上面的说法。我们继续分析后面两个看上去有些复杂的 printf 语句。

(1) `*(*ints + 1)`
首先 `*ints` 取出了第一个元素，即 ints[0][0] 的指针。 `*ints + 1` 就是向后移动一次指针，即指向 ints[0][1] 的指针。`*(*ints + 1)` 的结果也就是取出 ints[0][1] 的值。

(2) `**(ints + 1)`
ints 指向第一个元素 (0xbf880fdc)，`ints + 1` 指向第二个元素(0xbf880fe8)。"*(ints + 1)" 取出 ints[1] 的内容，这个内容是另外一只指针，因此 `**(ints + 1)` 就是取出 ints[1][0] 的内容。下面这种写法，看上去更容易理解：

```c
int main() {
    int x[] = {1,2,3};
    int y[] = {4,5};
    int z[] = {6,7,8,9};
    int* ints[] = { NULL, NULL, NULL };
    ints[0] = x;
    ints[1] = y;
    ints[2] = z;
    int** p = ints;

    // -----------------------------------------------

    // *p 取出ints[0] 存储的指针(&ints[0][0])
    // **p 取出ints[0][0] 值
    printf("%d\n", **p);

    // -----------------------------------------------

    // p 指向ints[1]
    p++;

    // *p 取出ints[1] 存储的指针(&ints[1][0])
    // **p 取出ints[1][0] 的值(= 4)
    printf("%d\n", **p);

    // -----------------------------------------------

    // p 指向ints[2]
    p++;

    // *p 取出ints[2] 存储的指针(&ints[2][0])
    // *p + 1 返回所取出指针的后⼀个位置
    // *(*p + 1) 取出ints[2][0 + 1] 的值(= 7)
    printf("%d\n", *(*p + 1));

    return 0;
}
```

指针数组经常出现在操作字符串数组的场合。

```c
int main () {
    char* strings[] = { "Ubuntu", "C", "C#", "NASM" };
    for (int i = 0; i < 4; i++) {
        printf("%s\n", strings[i]);
    }
    printf("------------------\n");
    printf("[2,1] = '%c'\n", strings[2][1]);
    strings[2] = "CSharp";
    printf("%s\n", strings[2]);
    printf("------------------\n");
    char** p = strings;
    printf("%s\n", *(p + 2));
    return 0;
}

// Result:
Ubuntu
C
C#
NASM
------------------[2,1] = '#'
CSharp
------------------CSharp
```

main 参数的两种写法。

```c
int main(int argc, char* argv[]) {
    for (int i = 0; i < argc; i++)
    {
        printf("%s\n", argv[i]);
    }
    return 0;
}
int main(int argc, char** argv) {
    for (int i = 0; i < argc; i++)
    {
        printf("%s\n", *(argv + i));
    }
    return 0;
}
```

当然，指针数组不仅仅用来处理数组。

```c
int main (int args, char* argv[]) {
    int* ints[] = { NULL, NULL, NULL, NULL };
    int a = 1;
    int b = 2;
    ints[2] = &a;
    ints[3] = &b;
    for(int i = 0; i < 4; i++) {
        int* p = ints[i];
        printf("%d = %d\n", i, p == NULL ? 0 : *p);
    }
    return 0;
}
```

### 函数指针

默认情况下，函数名就是指向该函数的指针常量。

```c
void inc(int* x) {
    *x += 1;
}

int main(void) {
    void (*f)(int*) = inc;
    int i = 100;
    f(&i);
    printf("%d\n", i);
    return 0;
}
```

可像 C# 委托那样定义一个函数指针类型。

```c
typedef void (*inc_t)(int*);
int main(void) {
    inc_t f = inc;
    // ... ...
}
```

有了 typedef，下面的代码更易阅读和理解。

```c
inc_t getFunc() {
    return inc;
}

int main(void) {
    inc_t inc = getFunc();
    // ... ...
}
```

注意:

- 定义函数指针类型: `typedef void (*inc_t)(int*)`
- 定义函数类型: `typedef void (inc_t)(int*)`

```c
void test() {
    printf("test");
}
typedef void(func_t)();
typedef void(*func_ptr_t)();
int main(int argc, char* argv[]) {
    func_t* f = test;
    func_ptr_t p = test;
    f();
    p();
    return 0;
}
```

### 指针常量

指针常量意指 "类型为指针的常量"，初始化后不能被修改，固定指向某个内存地址。
我们无法修改指针自身的值，但可以修改指针所指目标的内容。

```c
int x[] = { 1, 2, 3, 4 };
int* const p = x;
for (int i = 0; i < 4; i++) {
    int v = *(p + i);
    *(p + i) = ++v;
    printf("%d\n", v);
    // p++; // Compile Error!
}
```
上例中的指针 p 始终指向数组 x 的第一个元素，和数组名 x 作用相同。由于指针本身是常量，自然无法执行 p++、++p 之类的操作，否则会导致编译错误。

### 常量指针

常量指针是 "指向常量数据的指针"，指针目标被当做常量处理 (尽管原目标不一定是常量)，不可通过指针做赋值处理。指针自身并非常量，可以指向其他位置，但依然不能做赋值操作。

```c
int x = 1, y = 2;
int const* p = &x;
// *p = 100;     // Compile Error!
p = &y;
printf("%d\n", *p);
// *p = 100;     // Compile Error!

// 建议常量指针将 const 写在前面更易识别。
const int* p = &x;
```

几种特殊情况：

(1) 下面的代码在 VC 下无法编译，但 GCC 可以。

```c
const int x = 1;
int* p = &x;
printf("%d\n", *p);
*p = 1234;
printf("%d\n", *p);
```
(2) const int* p 指向 const int 虽然没问题，但不能通过指针做出修改。

```c
const int x = 1;
const int* p = &x;
printf("%d\n", *p);
*p = 1234;        // Compile Error!
```

(3) 声明指向常量的指针常量，这很罕见，但也好理解。

```c
int x = 10;
const int* const p = &i;
p++;         // Compile Error!
*p = 20;     // Compile Error!
```

### 区别指针常量和常量指针

看 const 修饰的是谁，即 `*` 在 const 的左边还是右边。

```c
int* const p       // const 修饰指针变量 p，指针是常量
int const *p       // const 修饰指针所指向的内容 *p，是常量的指针。或写成 const int *p
const int* const p // 指向常量的指针常量。右 const 修饰 p 常量，左 const 表明 *p 为常量
```

### 二级指针

指针本身也是内存区的一个数据变量，自然也可以用其他的指针来指向它。

```c
int x = 10;
int* p = &x;
int** p2 = &p; // p2 存储的是指针 p 的地址
printf("p = %p, *p = %d\n", p, *p);
printf("p2 = %p, *p2 = %x\n", p2, *p2);
printf("x = %d, %d\n",*p, **p2);

// Result:
p = 0xbfba3e5c, *p = 10
p2 = 0xbfba3e58, *p2 = bfba3e5c
x = 10, 10
```


