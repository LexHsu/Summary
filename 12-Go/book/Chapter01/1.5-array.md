
Array
=========

- 数组长度必须是常量，数组的类型同时包括数组的长度和可以被存储的元素类型。`[2]int` 和 `[3]int` 为不同类型。
- 支持 `==`，`!=` 操作符，因为内存总是被初始化过的。
- 指针数组 `[n]*T`，数组指针 `*[n]T`。
- 可用复合语句初始化。

```go
a := [3]int{1, 2}               // 未初始化元素值为0
b := [...]int{1, 2, 3, 4}       // 通过初始化值确定数组长度
c := [5]int{2: 100, 4:200}      // 初始化索引为 2 和 4 的元素，其他为 0

d := [...]struct {
    name string
    age uint8
}{
    {"user1", 10},              // 可省略元素类型
    {"user2", 20},              // 别忘了最后一行的逗号
}

array := [5]*int{0: new(int), 1: new(int)} // 指针数组
*array[0] = 7                              // 为索引为 0 的元素赋值
*array[1] = 77                             // 为索引为 1 的元素赋值
```

- 与其他语言的数组区别较大，go 数组为值类型，赋值和传参会复制整个数组，而不是指针。

```go
var array1 [5]string
array2 := [5]string{"Red", "Blue", "Green", "Yellow", "Pink"}
array1 = array2
```

- 数组类型完全相同才可以互相赋值

```go
var array1 [4]string
array2 := [5]string{"Red", "Blue", "Green", "Yellow", "Pink"}
array1 = array2
// 编译器会报错
Compiler Error:
cannot use array2 (type [5]string) as type [4]string in assignment

```

- 在函数中传递数组非常昂贵，因为在函数之间传递变量永远是传递值，所以如果变量是数组，那么意味着传递整个数组，
因此函数中常传递指向数组的指针。

```go
var array [1e6]int

func foo(array [1e6]int) {
    // ...
}

foo(array)      // bad
foo(&array)     // good
```

- 拷贝一个指针数组实际上是拷贝指针值，而不是指针指向的值。

```go
var array1 [3]*string
array2 := [3]*string{new(string), new(string), new(string)}
*array2[0] = "Red"
*array2[1] = "Blue"
*array2[2] = "Green"
array1 = array2
// 赋值完成后，两组指针数组指向同一字符串
```


- 支持多维数组。

```go
a := [2][3]int{{1, 2, 3}, {4, 5, 6}}
b := [...][2]int{{1, 1}, {2, 2}, {3, 3}} // 第 2 纬度不能用 ...
```

- 值拷贝会造成性能问题，通常会建议使用slice，或数组指针。

```go
func test(x [2]int) {
    fmt.Printf("x: %p\n", &x)
    x[1] = 1000
}
func main() {
    a := [2]int{}
    fmt.Printf("a: %p\n", &a)
    test(a)
    fmt.Println(a)
}

// 输出：
a: 0x2101f9150
x: 0x2101f9170
[0 0]
```

- 内置函数len和cap都返回数组长度（元素个数）。

```go
a := [2]int{}
println(len(a), cap(a))   // 2, 2
```
