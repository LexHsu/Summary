数组
===

### 初始化

- x[i] 相当于 `*(x + i)`，数组名为指向第一个元素的指针。

```c
int x[] = { 1, 2, 3, 4 };
x[1] = 10;
printf("%d\n", *(x + 1));
*(x + 2) = 20;
printf("%d\n", x[2]);
```

C 不会对数组下标索引进行范围检查，注意过界检查，而 &x[i] 则返回 `int*` 类型指针，指向目标序号元素。

- 除了使用下标初始化外，还可以直接用初始化器。

```c
int x[] = { 1, 2, 3 };
int y[5] = { 1, 2 };
int a[3] = {};
int z[][2] = {
    { 1, 1 },
    { 2, 1 },
    { 3, 1 },
};
```

初始化规则:

1. 如果数组为静态生存周期，那么初始化器必须是常量表达式。
2. 如果提供初始化器，那么可以不提供数组长度，由初始化器的最后一个元素决定。
3. 如果同时提供长度和初始化器，那么没有提供初始值的元素都被初始化为 0 或 NULL。

- 还可以在初始化器中初始化特定的元素。

```c
int x[] = { 1, 2, [6] = 10, 11 };
int len = sizeof(x) / sizeof(int);
for (int i = 0; i < len; i++) {
printf("x[%d] = %d\n", i, x[i]);
}

// Result:
x[0] = 1
x[1] = 2
x[2] = 0
x[3] = 0
x[4] = 0
x[5] = 0
x[6] = 10
x[7] = 11
```

- 字符串是以 '\0' 结尾的 char 数组。

```c
char s[10] = "abc";
char x[] = "abc";
printf("s, size=%d, len=%d\n", sizeof(s), strlen(s));
printf("x, size=%d, len=%d\n", sizeof(x), strlen(x));

// Result:
s, size=10, len=3
x, size=4, len=3
```

### 多维数组

多维数组其实是“元素为数组”的数组，注意元素是数组并不是数组指针。
多维数组的第一个维度下标可以不指定。

```c
int x[][2] = {
    { 1, 11 },
    { 2, 22 },
    { 3, 33 }
};
int col = 2, row = sizeof(x) / sizeof(int) / col;
for (int r = 0; r < row; r++) {
    for (int c = 0; c < col; c++) {
        printf("x[%d][%d] = %d\n", r, c, x[r][c]);
    }
}

// Result:
x[0][0] = 1
x[0][1] = 11
x[1][0] = 2
x[1][1] = 22
x[2][0] = 3
x[2][1] = 33
```

二维数组通常也被称为 "矩阵 (matrix)"，相当于一个 row * column 的表格。
如 x[3][2] 相当于三行两列表格。多维数组的元素是连续排列的，这也是区别指针数组的一个重要特征。

```c
int x[][2] = {
    { 1, 11 },
    { 2, 22 },
    { 3, 33 }
};
int len = sizeof(x) / sizeof(int);
int* p = (int*)x;
for (int i = 0; i < len; i++) {
    printf("x[%d] = %d\n", i, p[i]);
}

// Result:
x[0] = 1
x[1] = 11
x[2] = 2
x[3] = 22
x[4] = 3
x[5] = 33
```

- 多维数组同样可以初始化特定的元素。

```c
int x[][2] = {
    { 1, 11 },
    { 2, 22 },
    { 3, 33 },
    [4][1] = 100,
    { 6, 66 },
    [7] = { 9, 99 }
};
int col = 2, row = sizeof(x) / sizeof(int) / col;
for (int r = 0; r < row; r++) {
    for (int c = 0; c < col; c++) {
        printf("x[%d][%d] = %d\n", r, c, x[r][c]);
    }
}

// Result:
x[0][0] = 1
x[0][1] = 11
x[1][0] = 2
x[1][1] = 22
x[2][0] = 0
x[2][1] = 0
x[3][0] = 0
x[3][1] = 0
x[4][0] = 0
x[4][1] = 100
x[5][0] = 6
x[5][1] = 66
x[6][0] = 0
x[6][1] = 0
x[7][0] = 9
x[7][1] = 99
```

### 数组参数

当数组作为函数参数时，总是被隐式转换为指向数组第一个元素的指针，即无法用 sizeof 获得数组的长度。

```c
void test(int x[]) {
    printf("%d\n", sizeof(x));
}

void test2(int* x) {
    printf("%d\n", sizeof(x));
}

int main() {
    int x[] = { 1, 2, 3 };
    printf("%d\n", sizeof(x));
    test(x);
    test2(x);
    return 0;
}

// Result:
12
4
4
```
test 和 test2 中的 sizeof(x) 实际效果是 sizeof(int*)。需要显式传递数组长度，或者是一个以特定标记结尾的数组 (NULL)。

- C99 支持长度可变数组作为函数函数。传递数组参数时，可能的写法包括：

```c
/* 数组名默认指向第一个元素，同 test2 */
void test1(int len, int x[]) {
    int i;
    for (i = 0; i < len; i++) {
        printf("x[%d] = %d; ", i, x[i]);
    }
    printf("\n");
}

/* 直接传入数组第一元素指针*/
void test2(int len, int* x) {
    for (int i = 0; i < len; i++) {
        printf("x[%d] = %d; ", i, *(x + i));
    }
    printf("\n");
}

/* 数组指针: 数组名默认指向第一元素指针，&array 则是获得整个数组指针*/
void test3(int len, int(*x)[len]) {
    for (int i = 0; i < len; i++) {
        printf("x[%d] = %d; ", i, (*x)[i]);
    }
    printf("\n");
}

/* 多维数组: 数组名默认指向第一元素指针，也即是int(*)[] */
void test4(int r, int c, int y[][c]) {
    for (int a = 0; a < r; a++) {
        for (int b = 0; b < c; b++) {
            printf("y[%d][%d] = %d; ", a, b, y[a][b]);
        }
    }
    printf("\n");
}

/* 多维数组: 传递第一个元素的指针*/
void test5(int r, int c, int (*y)[c]) {
    for (int a = 0; a < r; a++) {
        for (int b = 0; b < c; b++) {
            printf("y[%d][%d] = %d; ", a, b, (*y)[b]);
        }
        y++;
    }
    printf("\n");
}

/* 多维数组*/
void test6(int r, int c, int (*y)[][c]) {
    for (int a = 0; a < r; a++) {
        for (int b = 0; b < c; b++) {
            printf("y[%d][%d] = %d; ", a, b, (*y)[a][b]);
        }
    }
    printf("\n");
}

/* 元素为指针的指针数组，相当于test8 */
void test7(int count, char** s) {
    for (int i = 0; i < count; i++) {
        printf("%s; ", *(s++));
    }
    printf("\n");
}

void test8(int count, char* s[count]) {
    for (int i = 0; i < count; i++) {
        printf("%s; ", s[i]);
    }
    printf("\n");
}

/* 以NULL 结尾的指针数组*/
void test9(int** x) {
    int* p;
    while ((p = *x) != NULL) {
        printf("%d; ", *p);
        x++;
    }
    printf("\n");
}

int main(int argc, char* argv[]) {
    int x[] = { 1, 2, 3 };
    int len = sizeof(x) / sizeof(int);
    test1(len, x);
    test2(len, x);
    test3(len, &x);
    int y[][2] = {
        {10, 11},
        {20, 21},
        {30, 31}
    };
    int a = sizeof(y) / (sizeof(int) * 2);
    int b = 2;
    test4(a, b, y);
    test5(a, b, y);
    test6(a, b, &y);
    char* s[] = { "aaa", "bbb", "ccc" };
    test7(sizeof(s) / sizeof(char*), s);
    test8(sizeof(s) / sizeof(char*), s);
    int* xx[] = { &(int){111}, &(int){222}, &(int){333}, NULL };
    test9(xx);
    return EXIT_SUCCESS;
}
```

