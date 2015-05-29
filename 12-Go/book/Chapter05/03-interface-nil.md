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
}
```

接口类型的变量底层是作为两个成员来实现：type，data。type 用于存储变量的动态类型，data 用于存储变量的具体数据。
在上面的例子中，第一条打印语句输出的是：int64。这是因为已经显示的将类型为 int64 的数据 58 赋值给了 interface 类型的变量 val，所以 val 的底层结构应该类似于：(int64, 58)。
我们暂且用这种二元组的方式来描述，二元组的第一个成员为 type，第二个成员为 data。第二条打印语句输出的是：int。
这是因为字面量的整数在 golang 中默认的类型是 int，所以这个时候 val 的底层结构就变成了：(int, 50)。


接下来说说 interface 类型的值和 nil 的比较问题。golang 中的一个坑：

```go
package main

import (
    "fmt"
)

func main() {
    var val interface{} = nil
    if val == nil {
        fmt.Println("val is nil")
    } else {
        fmt.Println("val is not nil")
    }
}
```

变量 val 是 interface 类型，它的底层结构必然是(type, data)。由于 nil 是 untyped(无类型)，而又将 nil 赋值给了变量 val，所以 val 实际上存储的是(nil, nil)。
因此很容易就知道 val 和 nil 的相等比较是为 true 的。
对于将任何其它有意义的值类型赋值给 val，都导致 val 持有一个有效的类型和数据。也就是说变量 val 的底层结构肯定不为(nil, nil)，因此它和 nil 的相等比较总是为 false。

上面的讨论都是在围绕值类型来进行的。在继续讨论之前，让我们来看一种特例：`(*interface{})(nil)`。将nil转成interface类型的指针，其实得到的结果仅仅是空接口类型指针并且它指向无效的地址。
注意是空接口类型指针而不是空指针，这两者的区别很大。

关于`(*interface{})(nil)`还有一些要注意的地方。这里仅仅是拿`(*interface{})(nil)`来举例，对于`(*int)(nil)、(*byte)(nil)`等等来说是一样的。
上面的代码定义了接口指针类型变量val，它指向无效的地址(0x0)，因此val持有无效的数据。但它是有类型的`(*interface{})`。所以val的底层结构应该是：
`(*interface{}, nil)`。有时候您会看到`(*interface{})(nil)`的应用，比如`var ptrIface = (*interface{})(nil)`，
如果您接下来将ptrIface指向其它类型的指针，将通不过编译。或者您这样赋值：`*ptrIface = 123`，那样的话编译是通过了，但在运行时还是会panic的，
这是因为ptrIface指向的是无效的内存地址。其实声明类似ptrIface这样的变量，是因为使用者只是关心指针的类型，而忽略它存储的值是什么。还是以例子来说明：

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

interface 类型的变量和 nil 的相等比较出现最多的地方应该是 error 接口类型的值与 nil 的比较。有时候您想自定义一个返回错误的函数来做这个事，可能会写出以下代码：

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

我们可以来分析一下。error 是一个接口类型，test 方法中返回的指针 p 虽然数据是 nil，但是由于它被返回成包装的 error 类型，也即它是有类型的。
所以它的底层结构应该是 `(*data, nil)`，很明显它是非 nil 的。

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
