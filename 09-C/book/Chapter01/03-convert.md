类型转换
===

### 隐式转换

- 编译器默认隐式转换: `char -> int -> long -> long long -> float -> double -> long double`。
浮点数的等级比任何类型的整数等级都高。有符号整数和其等价的无符号类型等级相同。

- 在表达式中，可能会将 char、short 当做默认 int (unsigned int) 类型操作数，但 float 并不会自动转换为默认的 double 类型。

```c
char a = 'a';
char c = 'c';
printf("%d\n", sizeof(c - a));
printf("%d\n", sizeof(1.5F - 1));

// Result:
4
4
```

- 当包含无符号操作数时，需要注意提升后类型是否能容纳无符号类型的所有值。

```c
long a = -1L;
unsigned int b = 100;
printf("%ld\n", a > b ? a : b);

// Result:
-1
```
输出结果让人费解。尽管 long 等级比 unsigned int 高，但在 32 位系统中，它们都是 32 位整数，且 long 并不足以容纳 unsigned int 的所有值，因此编译器会将这两个操作数都转换为 unsigned long，也就是高等级的无符号版本，如此 (unsigned long)a 的结果就变成了一个很大的整数。

```c
long a = -1L;
unsigned int b = 100;
printf("%lu\n", (unsigned long)a);
printf("%ld\n", a > b ? a : b);

// Result:
4294967295
-1
```

其他隐式转换还包括：

1. 赋值和初始化时，右操作数总是被转换成左操作数类型。
2. 函数调用时，总是将实参转换为形参类型。
3. 将 return 表达式结果转换为函数返回类型。
4. 将宽类型转换为窄类型时，编译器会尝试丢弃高位或者四舍五入等手段返回一个 "近似值"。
5. 任何指针都可以隐式转换为 void 指针，反之亦然。
6. 任何指针都可以隐式转换为类型更明确的指针 (包含 const、volatile、restrict 等限定符)。

```c
int x = 123, *p = &x;
const int* p2 = p;
```
- NULL 可以被隐式转换为任何类型指针。

### 强制类型转换

- 可以显式将指针转换成任何其他类型指针。

```c
int x = 123, *p = &x;
char* c = (char*)x;
```
- 可以显式将指针转换为整数，反向转换亦可。

```c
int x = 123, *p = &x;
int px = (int)p;
printf("%p, %x, %d\n", p, px, *(int*)px);

// Result:
0xbfc1389c, bfc1389c, 123
```
