
控制语句
=========

###IF
- 可省略条件表达式括号
- 支持初始化语句，可定义代码块局部变量
- 代码块左大括号必须在条件表达式尾部
- 不支持三元操作符 "a > b ? a : b"

```go
x := 0
// if x > 10           // Error: missing condition in if statement
// {
// }
if n := "abc"; x > 0 { // 初始化语句未必就是定义变量，如println("init")也可
    println(n[2])
} else if x < 0 {
    println(n[1])
} else {
    println(n[0])
}
```

###For
支持三种循环方法，包括类while语法

```go
s := "abc"
for i, n := 0, len(s); i < n; i++ { // 常见for循环，支持初始化语句
    println(s[i])
}

n := len(s)
for n > 0 {                         // 替代while(n > 0) {} 或 for (; n > 0;) {}
    println(s[n])
    n--
}

for {                               // 替代while (true) {} 或 for (;;) {}
    println(s)
}
```

###Range

- 类似迭代器操作,返回 (索引, 值) 或 (键, 值)。

```go
                        1st value           2nd value
------------------+-------------------+------------------+-------------------
string                index               s[index]           unicode, rune
array/slice           index               s[index]
map                   key                 m[key]
channel               element
```

- 可忽略不想要的返回值,包括⽤用 "_" 这个特殊变量。

```go
s := "abc"
for i := range s {                      // 忽略2nd value，支持string/array/slice/map
    println(s[i])
}
for _, c := range s {                   // 忽略index
    println(c)
}
m := map[string]int{"a": 1, "b": 2}
for k, v := range m {                   // 返回(key, value)
    println(k, v)
}
```

- 注意,range 会复制对象。

```go
a := [3]int{0, 1, 2}
for i, v := range a {               // index、value 都是从复制品中取出
    if i == 0 {                     // 在修改前,我们先修改原数组
        a[1], a[2] = 999, 999       // 确认修改有效,输出 [0, 999, 999]
        fmt.Println(a)
    }

    a[i] = v + 100                  // 使⽤用复制品中取出的 value 修改原数组
}
fmt.Println(a)                      // 输出 [100, 101, 102]
```

- 建议改⽤用引⽤用类型,其底层数据不会被复制

```go
s := []int{1, 2, 3, 4, 5}
for i, v := range s {               // 复制 struct slice { pointer, len, cap }。
    if i == 0 {
        s = s[:3]                   // 对 slice 的修改,不会影响 range。
        s[2] = 100                  // 对底层数据的修改。
    }
    println(i, v)
}

// 输出
0 1
1 2
2 100
3 4
4 5
```
- 另外两种引用类型map、channel是指针包装，而不像slice是struct。
