strcpy strncpy
===

### strcpy

```
char *strcpy(char *dest, char *src);

功能：把 src 所指由 \0 结束的字符串复制到 dest 所指的数组中。
说明：src 和 dest 所指内存区域不可以重叠且dest必须有足够的空间来容纳src的字符串。
返回：指向dest的指针。

注意：当 src 串长度 > dest 串长度时，程序仍会将整个 src 串复制到 dest 区域，可是 dest 数组已发生溢出。
因此会导致 dest 栈空间溢出以致产生崩溃异常。如果不考虑 src 串的完整性，可以把 dest 数组最后一元素置为 NULL，从 dest 串长度处插入 NULL 截取字串。
```

### strncpy

```
char * strncpy(char *dest, char *src, size_t n);

功能：将字符串 src 中最多 n 个字符复制到字符数组 dest 中，strcpy 是遇到 NULL 才停止复制，strncpy 是等凑够 n 个字符才开始复制，
返回：指向 dest 的指针。
说明：如果 n > dest串长度，dest 栈空间溢出产生崩溃异常。
否则：
1）src 串长度 <= dest 串长度，这里的串长度包含串尾 NULL 字符。
如果 n = (0, src串长度)，src 的前n个字符复制到 dest 中。但是由于没有 NULL 字符，所以直接访问 dest 串会发生栈溢出的异常情况。
如果 n = src 串长度，与 strcpy 一致。
如果 n = dest 串长度，[0, src 串长度]处存放于 dest 字串，(src 串长度, dest 串长度]处存放 NULL。
2）src串长度>dest串长度
如果n = dest 串长度，则 dest 串没有 NULL 字符，会导致输出会有乱码。如果不考虑 src 串复制完整性，可以将 dest 最后一字符置为 NULL。
```


### 总结　

一般情况下，使用 strncpy 时，建议将 n 置为 dest 串长度（除非你将多个src串都复制到dest数组，并且从dest尾部反向操作)，
复制完毕后，为保险起见，将dest串最后一字符置NULL，避免发生在第2)种情况下的输出乱码问题。
```

###
第一种情况：
char* p="how are you ?";
char name[20]="ABCDEFGHIJKLMNOPQRS";
strcpy(name,p);                // name改变为"how are you ? " ====>正确！
strncpy(name,p, sizeof(name)); // name 改变为 "how are you ?" =====>正确！后续的字符将置为NULL
第二种情况：
char* p="how are you ?";
char name[10];
strcpy(name,p);                // 目标串长度小于源串,错误！
name[sizeof(name)-1]='\0';     // 和上一步组合，弥补结果，但是这种做法并不可取，因为上一步出错处理方式并不确定
strncpy(name,p,sizeof(name));  // 源串长度大于指定拷贝的长度 sizeof(name)，注意在这种情况下不会自动在目标串后面加 \0
name[sizeof(name)-1]='\0';     // 和上一步组合，弥补结果
