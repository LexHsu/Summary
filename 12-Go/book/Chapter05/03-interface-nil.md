interface 与 nil
===

1. go 语言中，nil 只能赋值给指针、channel、func、interface、map 或 slice 类型的变量，否则会导致 panic。
2. 接口类型的变量底层是作为两个成员来实现：type，data。type 用于存储变量的动态类型，data 用于存储变量的具体数据。
下例子中 val 接口可看成一个二元组(int64, 58)。

```go
package main

import (
    "fmt"
    "reflect"
)

func main() {
    var val interface{} = int64(58)
    fmt.Println(reflect.TypeOf(val))
    val = 50
    fmt.Println(reflect.TypeOf(val))
    
    var val2 interface{} = nil
    if val2 == nil {
        fmt.Println("val2 is nil")
    } else {
        fmt.Println("val2 is not nil")
    }
    
    // 可将 nil 转成 interface 类型的指针，一个空接口类型指针且指向无效地址
    // 注意是空接口类型指针而不是空指针，两者区别很大。
    // 同理，也可转换为 (*int)(nil) 或 (*byte)(nil)
    var val3 interface{} = (*interface{})(nil)
    // val3 = (*int)(nil)
    if val3 == nil {
        fmt.Println("val3 is nil")
    } else {
        fmt.Println("val3 is not nil")
    }
}

// 输出
int64
int
val2 is nil
val3 is not nil
```

interface 类型变量和 nil 常出现于 error 接口变量与 nil 的比较。如自定义一个返回错误的函数：

```go
package main

import (
    "fmt"
)

type data struct{}

func (this *data) Error() string { return "" }

func test() error {
    var p *data = nil
    return p
}

func main() {
    var e error = test()
    if e == nil {
        fmt.Println("e is nil")
    } else {
        fmt.Println("e is not nil")
    }
}
```

以上代码是有问题的。

```go
$ cd $GOPATH/src/interface_test
$ go build
$ ./interface_test
e is not nil
```

error 是一个接口类型，test 方法中返回的指针 p 虽然数据是 nil，但是由于它被返回成包装的 error 类型，也即它是有类型的。它的底层结构应该是 `(*data, nil)`，是非 nil 的。可以打印观察下底层结构数据：

```go
package main

import (
    "fmt"
    "unsafe"
)

type data struct{}

func (this *data) Error() string { return "" }

func test() error {
    var p *data = nil
    return p
}

func main() {
    var e error = test()

    d := (*struct {
        itab uintptr
        data uintptr
    })(unsafe.Pointer(&e))

    fmt.Println(d)
}
```

```
$ cd $GOPATH/src/interface_test
$ go build
$ ./interface_test
&{3078907912 0}
```

正确做法如下：

```go
package main

import (
    "fmt"
)

type data struct{}

func (this *data) Error() string { return "" }

func bad() bool {
    return true
}

func test() error {
    var p *data = nil
    if bad() {
        return p
    }
    return nil
}

func main() {
    var e error = test()
    if e == nil {
        fmt.Println("e is nil")
    } else {
        fmt.Println("e is not nil")
    }
}
```
