interface 与 nil
===

go 中，nil 只能赋值给指针、channel、func、interface、map 或 slice 类型的变量。否则会导致 panic。[官方说明](http://pkg.golang.org/pkg/builtin/#Type)

[英文原文](http://golang.org/doc/go_faq.html#nil_error)，
[中文版本](http://my.oschina.net/chai2010/blog/117923) 。

接下来通过编写测试代码和gdb来看看interface倒底是什么。会用到反射，关于 go 中的反射概念：
[英文原文](http://blog.golang.org/laws-of-reflection)，
[中文版本](http://mikespook.com/2011/09/%E5%8F%8D%E5%B0%84%E7%9A%84%E8%A7%84%E5%88%99/)。

代码如下：

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
    
    var val interface{} = nil
    if val == nil {
        fmt.Println("val is nil")
    } else {
        fmt.Println("val is not nil")
    }
}

// 输出
(int64, 58)
(int, 50)
```

接口类型的变量底层是作为两个成员来实现：type，data。

1. type 用于存储变量的动态类型。
2. data 用于存储变量的具体数据。

看一种特例：`(*interface{})(nil)`，其将 nil 转成 interface 类型的指针，一个空接口类型指针且指向无效地址。
注意是空接口类型指针而不是空指针，两者区别很大。同理`(*int)(nil)、(*byte)(nil)`。
上面的代码定义了接口指针类型变量val，它指向无效的地址(0x0)，因此val持有无效的数据。但它是有类型的`(*interface{})`。所以val的底层结构应该是：`(*interface{}, nil)`。

```go
package main

import (
    "fmt"
)

func main() {
    var val interface{} = (*interface{})(nil)
    // val = (*int)(nil)
    if val == nil {
        fmt.Println("val is nil")
    } else {
        fmt.Println("val is not nil")
    }
}
```

很显然，无论该指针的值是什么：`(*interface{}, nil)`，这样的接口值总是非nil的，即使在该指针的内部为nil。

```go
$ cd $GOPATH/src/interface_test
$ go build
$ ./interface_test
val is not nil
```

interface 类型的变量和 nil 的相等比较出现最多的地方应该是 error 接口类型的值与 nil 的比较。如自定义一个返回错误的函数：

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

但是很可惜，以上代码是有问题的。

```go
$ cd $GOPATH/src/interface_test
$ go build
$ ./interface_test
e is not nil
```

error 是一个接口类型，test 方法中返回的指针 p 虽然数据是 nil，但是由于它被返回成包装的 error 类型，也即它是有类型的。
所以它的底层结构应该是 `(*data, nil)`，很明显是非 nil 的。

可以打印观察下底层结构数据：

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

正确的做法应该是：

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
