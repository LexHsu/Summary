snprintf
===

### sprintf 与 snprintf

```
int sprintf( char *str, const char *format, [argument]...)

头文件：#include <stdio.h>
功能  ：将可变个参数(...)按照 format 格式化成字符串，然后将其复制到 str 中
返回  ：返回值为欲写入的字符串长度，即 format 长度
说明  ：容易导致缓冲区溢出


int snprintf(char *str, size_t size, const char *format, [argument]...)

头文件：#include <stdio.h>
功能  ：将可变个参数(...)按照 format 格式化成字符串，然后将其复制到 str 中
返回  ：返回值为欲写入的字符串长度，即 format 长度
说明  ：1. 如果格式化后的字符串长度 < size，则将此字符串全部复制到 str 中，并给其后添加一个字符串结束符 `\0`
       2. 如果格式化后的字符串长度 >= size，则只将其中的 (size-1) 个字符复制到 str 中，并给其后添加一个字符串结束符 `\0`

```
### 示例

```
#include<stdio.h>

int main() {

    // sprintf example
    char *p1 = "abcdefghijklmn";
    char buf[11] = {0};
    //sprintf(buf, "%10s", p); // p 长度大于 10 个字符，sprintf 写操作会越过 buf 边界，产生缓冲区溢出
    printf("%sn",buf);

    // snprintf example
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

### 总结

1. strcpy 函数操作的对象是字符串，完成从源字符串到目的字符串的拷贝功能。
2. snprintf 函数操作的对象不限于字符串，主要用来实现字符串或基本数据类型向字符串的转换功能。
3. memcpy 函数顾名思义就是 内存拷贝，实现将一个内存块的内容复制到另一个内存块。memcpy 的操作对象不局限于某一类数据类型。
对于基本数据类型，有赋值运算符可以方便且高效地拷贝，memcpy 几乎不被使用。对结构或者数组的拷贝

对于字符串拷贝，用上述三个函数都可以实现，区别在于：

1. strcpy 无疑是最合适的选择：效率高且调用方便。
2. snprintf 要额外指定格式符并且进行格式转化，麻烦且效率不高。
3. memcpy 虽然高效，但是需要额外提供拷贝的内存长度这一参数，易错且使用不便；如果长度指定过大（最优长度是源字符串长度 + 1），还会带来性能的下降。

strcpy 和 memcpy 区别：

```
const char *str1="abc\0def";
char str2[7];

strcpy示例：
strcpy（str2, str1）

结果:
str2＝"abc"; // strcpy是以 \0 为结束标志

memcpy示例：
memset(str2, 7);
memcpy(str2, str1, 7);

结果:
str2="abc\0def"; // memcpy直接对内存进行复制
```
