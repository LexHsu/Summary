snprintf
===

### 函数原型

```c
int snprintf(char *str, size_t size, const char *format, ...)
```

### 所需头文件

```c
#include <stdio.h>
```

### 功能
将可变个参数(...)按照 format 格式化成字符串，然后将其复制到 str 中

1. 如果格式化后的字符串长度 < size，则将此字符串全部复制到 str 中，并给其后添加一个字符串结束符 `\0`；
2. 如果格式化后的字符串长度 >= size，则只将其中的 (size-1) 个字符复制到 str 中，并给其后添加一个字符串结束符 `\0`；

注意：返回值为欲写入的字符串长度。

### 示例

```c
#include<stdio.h>

int main() {

    char a[3] = {0};
    int len = 0;
    len = snprintf(a, sizeof(a), "%d", 12345);
    printf("len = %d, a = %s\n", len, a);

    char s[5] = {0};
    len = snprintf(s, sizeof(s), "%.*s", 3, "abcd");
    printf("len = %d, a = %s\n", len, s);

    char str[5] = {0};  
    char *data = "abcd";  
    len = snprintf(str, sizeof(str) - 1, "str:%s", data);  
    printf("len = %d, a = %s\n", len, str);

    char x[5] = {0};  
    char *data2 = "abcd";  
    len = snprintf(x, sizeof(x) - 1, "xis:%s", data2);  
    printf("len = %d, a = %s\n", len, x);

    return 0;
}

// 结果:
len = 5, a = 12
len = 3, a = abc
len = 8, a = str
len = 8, a = xis
```
