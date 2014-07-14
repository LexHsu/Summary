类型转换与类型断言
===

### 各类型默认值

```go
package main

import "fmt"

type myStruct struct {
    name   bool
    userid int64
}

var structZero myStruct
var intZero int
var int32Zero int32
var int64Zero int64
var uintZero uint
var uint8Zero uint8
var uint32Zero uint32
var uint64Zero uint64
var byteZero byte
var boolZero bool
var float32Zero float32
var float64Zero float64
var stringZero string
var funcZero func(int) int
var byteArrayZero [5]byte
var boolArrayZero [5]bool
var byteSliceZero []byte
var boolSliceZero []bool
var mapZero map[string]bool
var interfaceZero interface{}
var chanZero chan int
var pointerZero *int

func main() {
    fmt.Println("structZero: ", structZero)
    fmt.Println("intZero: ", intZero)
    fmt.Println("int32Zero: ", int32Zero)
    fmt.Println("int64Zero: ", int64Zero)
    fmt.Println("uintZero: ", uintZero)
    fmt.Println("uint8Zero: ", uint8Zero)
    fmt.Println("uint32Zero: ", uint32Zero)
    fmt.Println("uint64Zero: ", uint64Zero)
    fmt.Println("byteZero: ", byteZero)
    fmt.Println("boolZero: ", boolZero)
    fmt.Println("float32Zero: ", float32Zero)
    fmt.Println("float64Zero: ", float64Zero)
    fmt.Println("stringZero: ", stringZero)
    fmt.Println("funcZero: ", funcZero)
    fmt.Println("funcZero == nil?", funcZero == nil)
    fmt.Println("byteArrayZero: ", byteArrayZero)
    fmt.Println("boolArrayZero: ", boolArrayZero)
    fmt.Println("byteSliceZero: ", byteSliceZero)
    fmt.Println("byteSliceZero's len?", len(byteSliceZero))
    fmt.Println("byteSliceZero's cap?", cap(byteSliceZero))
    fmt.Println("byteSliceZero == nil?", byteSliceZero == nil)
    fmt.Println("boolSliceZero: ", boolSliceZero)
    fmt.Println("mapZero: ", mapZero)
    fmt.Println("mapZero's len?", len(mapZero))
    fmt.Println("mapZero == nil?", mapZero == nil)
    fmt.Println("interfaceZero: ", interfaceZero)
    fmt.Println("interfaceZero == nil?", interfaceZero == nil)
    fmt.Println("chanZero: ", chanZero)
    fmt.Println("chanZero == nil?", chanZero == nil)
    fmt.Println("pointerZero: ", pointerZero)
    fmt.Println("pointerZero == nil?", pointerZero == nil)
}
```

上述代码除了看到了各类型的默认值，也知道了 nil 只能赋值给指针、channel、func、interface、map 或 slice 类型的变量。

- Go 仅对字面量会自动隐式转换，其他需要手动转换。

```go
package main

import "fmt"

func main() {
    var i int32 = 5    // 不必写成 int32(5)
    var f float64 = 0  // 不必写成 float64(0)
    f = float64(i)
    fmt.Println(i)
    fmt.Println(f)
}
```

Go 中的类型区分静态类型和底层类型。即使两个类型的底层类型相同，在相互赋值时还是需要强制类型转换。
可以用 reflect 包中的 Kind 方法获取相应类型的底层类型。

```go
package main
import "fmt"

type MyInt32 int32    // MyInt32 为静态类型，int32 为底层类型

func main() {
    var uid int32   = 12345
    var gid MyInt32 = MyInt32(uid)
    fmt.Printf("uid=%d, gid=%d\n", uid, gid)
}
```

### 类型截断

这里只考虑具有相同底层类型之间的类型转换。小类型(这里指存储空间)向大类型转换时，通常都是安全的。下面是一个大类型向小类型转换的示例：

```go
package main

import "fmt"

func main() {
    var gid int32 = 0x12345678
    var uid int8  = int8(gid)
    fmt.Printf("uid = %#x, gid = %#x\n", uid, gid)
}
```
在上面的代码中，gid 类型为 int32，即占 4 个字节，值分别是：0x12, 0x34, 0x56, 0x78。
但事实不总是如此，这跟cpu架构有关。在内存中的存储方式分为两种：大端序和小端序。
大端序的存储方式是高位字节存储在低地址上；gid 的存储序列：0x78, 0x56, 0x34, 0x12。
小端序的存储方式是高位字节存储在高地址上，gid 的存储序列：0x12, 0x34, 0x56, 0x78。
对于强制转换后的uid，肯定是产生了截断行为。因为uid只占1个字节，转换后的结果必然会丢弃掉多余的3个字节。
截断的规则是：保留低地址上的数据，丢弃多余的高地址上的数据。来看下测试结果：

uid = 0x12, gid = 0x12345678

可见，可通过改程序判断机器是大端序还是小端序

```go
package main

import "fmt"

func IsBigEndian() bool {

    var i int32 = 0x12345678
    var b byte  = byte(i)
    if b == 0x12 {
        return true
    }
    return false
}

func main() {

    if IsBigEndian() {
        fmt.Println("大端序")
    } else {
        fmt.Println("小端序")
    }
}
```

### 接口转换规则

- 普通类型向接口类型的转换是隐式的。

```go
package main

import "fmt"

func main() {

    // hello 作为 string 类型存储在 interface{} 类型的变量 val 中
    var val interface{} = "hello"
    fmt.Println(val)

    // []byte{'a', 'b', 'c'} 作为 slice 存储在 interface{} 类型的变量 val中
    val = []byte{'a', 'b', 'c'}
    fmt.Println(val)
}
```

- 接口类型向普通类型转换需要类型断言。

接口类型向普通类型转换有两种方式：Comma-ok 断言和 switch 测试。任何实现了接口 I 的类型都可以赋值给这个接口类型变量。
由于 interface{} 包含了 0 个方法，所以任何类型都实现了 interface{} 接口，这就是为什么可以将任意类型值赋值给 interface{} 类型的变量，包括 nil。

此外注意一个接口问题，`*T` 包含了定义在 `T` 和 `*T` 上的所有方法，而 `T` 只包含定义在 `T` 上的方法：

#### Comma-ok 断言

语法为： `value, ok := element.(T)`。

element 必须为接口类型的变量，T 可为任意类型。如果断言失败，ok 为 false，否则 ok 为 true 且 value 为变量的值：

```go
package main

import "fmt"

type Html []interface{}

func main() {
    html := make(Html, 5)
    html[0] = "div"
    html[1] = "span"
    html[2] = []byte("script")
    html[3] = "style"
    html[4] = "head"
    for index, element := range html {
        if value, ok := element.(string); ok {
            fmt.Printf("html[%d] is a string and its value is %s\n", index, value)
        } else if value, ok := element.([]byte); ok {
            fmt.Printf("html[%d] is a []byte and its value is %s\n", index, string(value))
        }
    }
}

```

Comma-ok 断言还支持另一种简化使用的方式：`value := element.(T)`。
但不建议使用，因为一旦 element.(T) 断言失败，则会产生运行时错误。

```go
package main

import "fmt"

func main() {
    var val interface{} = "good"
    fmt.Println(val.(string))
    // fmt.Println(val.(int))  // error
}
```

#### switch 测试

仅用于 switch 语句中。

```go
package main

import "fmt"

type Html []interface{}

func main() {
    html := make(Html, 5)
    html[0] = "div"
    html[1] = "span"
    html[2] = []byte("script")
    html[3] = "style"
    html[4] = "head"
    for index, element := range html {
        switch value := element.(type) {
        case string:
            fmt.Printf("html[%d] is a string and its value is %s\n", index, value)
        case []byte:
            fmt.Printf("html[%d] is a []byte and its value is %s\n", index, string(value))
        case int:
            fmt.Printf("invalid type\n")
        default:
            fmt.Printf("unknown type\n")
        }
    }
}

```
