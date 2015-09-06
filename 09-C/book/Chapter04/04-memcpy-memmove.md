memset memcpy memcpy
===

### memcpy

```
void *memcpy(void *dest, const void *src, int n);

头文件：#include <string.h>
功能：由 src 指向地址为起始地址的连续 n 个字节的数据复制到以 dest 指向地址为起始地址的空间内。
返回：函数返回一个指向dest的指针。
说明：
　　1. src 和 dest 所指内存区域不可重叠，函数返回指向 dest 的指针。
　　2. 与 strcpy 相比，memcpy 并不是遇到 \0 就结束，而是一定会拷贝完 n 个字节。
　　3. 如果目标数组 dest 本身已有数据，执行 memcpy 后，将覆盖原有数据（最多覆盖n）。
```

### memmove

```
void *memmove( void* dest, const void* src, size_t n );

头文件：#include <string.h>或 #include <memory.h>
功能：由 src 所指内存区域复制 n 个字节到 dest 所指内存区域。
说明：src 和 dest 所指内存区域可重叠，复制后 dest 内容会被更改。
返回：返回指向 dest 的指针。
```

### memset

```
void *memset(void *s, int ch, size_t n);

头文件：#include <string.h>或 #include <memory.h>
功能：将src 中前 n 个字节替换为 ch 并返回s；
说明：常用于内存空间的初始化。
```
