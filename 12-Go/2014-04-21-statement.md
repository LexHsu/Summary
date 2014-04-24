
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
- 常见有三种形式

```go
// 1. 最常见的for循环，支持初始化语句
s := "abc"
for i, n := 0, len(s); i < n; i++ {
    println(s[i])
}

// 2. 替代while(n > 0) {} 或 for (; n > 0;) {}
n := len(s)
for n > 0 {
    println(s[n])
    n--
}

// 3. 替代while (true) {} 或 for (;;) {}
for {
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

- 使用"_"忽略不想要的返回值。

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

- range中的index和value是拷贝值。

```go
a := [3]int{0, 1, 2}
for i, v := range a {               // index、value都是原数组的拷贝
    if i == 0 {                     // 在修改前,我们先修改原数组
        a[1], a[2] = 999, 999       // 确认修改有效,输出 [0, 999, 999]
        fmt.Println(a)
    }

    a[i] = v + 100                  // 使用拷贝的value修改原数组
}
fmt.Println(a)                      // 输出 [100, 101, 102]
```

- 使用引用类型slice时，其底层数据不会被复制

```go
s := []int{1, 2, 3, 4, 5}
for i, v := range s {               // 复制struct slice { pointer, len, cap }
    if i == 0 {
        s = s[:3]                   // 对slice的修改,不会影响range
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

- 另外两种引用类型map、channel是指针包装，而slice是struct。

###Switch

- 分支表达式可以是任意类型，不限于常量。可省略break，默认自动终止。

```go
x := []int{1, 2, 3}
i := 2
switch i {
    case x[1]:
        println("a")
    case 1, 3:
        println("b")
    default:
        println("c")
}

// 输出
a
b

// 省略条件表达式,可当 if...else if...else使用

switch {
    case x[1] > 0:
        println("a")
    case x[1] < 0:
        println("b")
    default:
        println("c")
}
switch i := x[2]; {   // 带初始化语句
    case i > 0:
        println("a")
    case i < 0:
        println("b")
    default:
        println("c")
}
```

###Goto, Break, Continue

- 支持在函数内goto跳转。标签名区分大小写,未使用标签会引发错误。

```go
func main() {
    var i int
    for {
        println(i)
        i++
        if i > 2 { goto BREAK }
    }
BREAK:
    println("break")
EXIT:                    // Error: label EXIT defined and not used
}
```

- 配合标签,break 和 continue 可在多级嵌套循环中跳出。

```go
func main() {
L1:
    for x := 0; x < 3; x++ {
L2:
        for y := 0; y < 5; y++ {
            if y > 2 { continue L2 }
            if x > 1 { break L1 }
            print(x, ":", y, " ")
        }
        println()
    }
}

// 输出
0:0 0:1 0:2
1:0 1:1 1:2
```

- break可用于for、switch、select，而continue仅用于for循环。

```go
x := 100
switch {
case x >= 0:
    if x == 0 { break }
    println(x) }
```
