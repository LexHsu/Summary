strcat strncat
===

### strcat 函数
```
char *strcat(char *dest,char *src);

头文件：#include <string.h>
功能  ：把 src 所指字符串添加到 dest 结尾处(覆盖dest结尾处的'\0')并添加'\0'。
返回  ：指向 dest 的指针。
说明  ：src 和 dest 所指内存区域不可以重叠且 dest 必须有足够的空间来容纳 src 的字符串。
```
### strncat 函数
```
char *strncat(char *dest,char *src,int n);

头文件：#include <string.h>
功能  ：把 src 所指字符串的前 n 个字符添加到 dest 结尾处。覆盖 dest 结尾处的 '\0'，并添加'\0'
返回  ：指向 dest 的指针。
说明  ：src 和 dest 所指内存区域不可以重叠且 dest 必须有足够的空间来容纳 src 的字符串。
```

### 示例
```c
#include<stdlib.h>
#include<string.h>

int main() {

    char a[20]="abc";
    char *p="abcd";
    strncat(a, p, 3);
    printf("%s\n", a);

    char b[20] = "abc";
    char *q = "ab";
    strcat(b, q);
    printf("%s\n", b);
    return 0;
}

// result:
abcabc
abcab
```
