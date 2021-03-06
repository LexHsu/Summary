
变量
=========

- 计算机中内存以字节 byte 为单位存储，1 byte = 8 bit。

- 变量所占存储空间与变量类型及编译器环境有关：

| 类型       | 16位编译器(字节) | 32位编译器(字节) | 64位编译器(字节) |
|:----------:|:----------------:|:----------------:|:----------------:|
| char       | 1                | 1                | 1                |
| int        | 2                | 4                | 4                |
| float      | 4                | 4                | 4                |
| double     | 8                | 8                | 8                |

- 无布尔类型，如 Java 中有 boolean，C++ 中有 bool，c 中一般通过自定义宏处理。

- 查看变量地址

```c
#include <stdio.h>

int main() {
    int a = 10;
    printf("变量 a 地址：%p", &a); // & 为地址运算符，%p 用于格式化地址
    return 0;
}

// 输出
// 变量 a 地址：0x7fff5fbff8f8
```

- 任何数值在内存中都是以补码形式存储。

```
正数的补码与原码相同。如 9 的原码和补码都为 1001。
负数的补码等于它正数的原码取反后再 +1。如 -10 的补码计算过程如下：

1. 先算出10的二进制形式：0000 0000 0000 0000 0000 0000 0000 1010
2. 对10的二进制进行取反：1111 1111 1111 1111 1111 1111 1111 0101
3. 对取反后的结果+1：1111 1111 1111 1111 1111 1111 1111 0110

因此整数 -10 在内存中的二进制形式是：1111 1111 1111 1111 1111 1111 1111 0110
```

为何引入补码？

计算机里面所有数都以补码形式保存，逻辑加减运算都是补码之间的加法运算。

用计算机里，只有加法器，没有减法器，用补数代替原数，可把减法转变为加法。出现的进位就是模，此时的进位，就应该忽略不计。
二进制下，有多少位数参加运算，模就是在 1 的后面加上多少个 0。
补码就是按照这个要求来定义的：正数不变，负数即用模减去绝对值。

其实，补数和补码最高位的1、0 符号位是自然出现的，并不是人为来规定的。
的确，符号位在补码运算里面是“模”，本身并不带符号的意义。
因为计算机将加法转换成加上一个“负数”，而负数又以补码的形式表现。
补码比源码多一位，从这多出来的一位可以推断出原来数字的正负号，所以成为了符号位。
也可以这样认为，留出一位（不全部占满）的原因是要用“模”来表示正负数。

可见不是特意留出一个符号位，用1和0来表示正负号。
而是补码运算可以用最高位来表示正负，所以符号位诞生了。

为何 -128 的补码是 10000000 ？

-128 是一个负数，所以它的补码是它的“模”减去它的绝对值，即：
100000000 - 10000000 = 10000000

为何
负数补码等于源码的反码加一呢？可以这样推导：
100000000 - 10000000
= (11111111 + 00000001) - 10000000
= 11111111 - 10000000 + 1
= 01111111 + 1 // 反码加一
= 10000000

由此我们得知，在计算机里面所有的数字都以补码形式存储。
127 存成 01111111，-127 存成 11111111，算减法就变成算加法了，尽管看到的是`-`号。


- 字符常量默认为 int 型，但编译器可决定将其解释为 char 或 int。
```c
char c = 'a';
printf("%c, size(char)=%d, size('a')=%d;\n", c, sizeof(c), sizeof('a'));

// Result:
a, size(char)=1, size('a')=4;
```
