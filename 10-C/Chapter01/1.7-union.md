联合体
===

- 联合体和结构体区别：联合每次只能存储一个成员，联合体的长度由最宽成员类型决定。

```c
typedef struct {
    int type;
    union {
        int ivalue;
        long long lvalue;
    } value;
} data_t;
data_t d = { 0x8899, .value.lvalue = 0x1234LL };
data_t d2;
memcpy(&d2, &d, sizeof(d));
printf("type:%d, value:%lld\n", d2.type, d2.value.lvalue);
```

当然也可以用指针来实现上例功能，但 union 会将数据内嵌在结构体中，这对于进行 memcpy 等操作更加方便快捷，且不需进行指针类型转换。

- 可使用初始化器初始化联合体，如果没有指定成员修饰符，默认为第一个成员。

```c
union value_t {
    int ivalue;
    long long lvalue;
};
union value_t v1 = { 10 };
printf("%d\n", v1.ivalue);
union value_t v2 = { .lvalue = 20LL };
printf("%lld\n", v2.lvalue);
union value2_t { char c; int x; } v3 = { .x = 100 };
printf("%d\n", v3.x);
```

 - 一个常用的联合体用法。

```c
union {
    int x;
    struct {
        char a, b, c, d;
    } bytes;
} n = { 0x12345678 };
printf("%#x => %x, %x, %x, %x\n", n.x, n.bytes.a, n.bytes.b, n.bytes.c, n.bytes.d);

// Result:
0x12345678 => 78, 56, 34, 12
```

- 可把结构或联合的多个成员 "压缩存储" 在一个字段中，以节约内存。

```c
struct {
    unsigned int year : 22;    // 指定位域，即 year 长度为 22 bit
    unsigned int month : 4;
    unsigned int day : 5;
} d = { 2010, 4, 30 };
printf("size: %d\n", sizeof(d));
printf("year = %u, month = %u, day = %u\n", d.year, d.month, d.day);

// Result:
size: 4
year = 2010, month = 4, day = 30
```

