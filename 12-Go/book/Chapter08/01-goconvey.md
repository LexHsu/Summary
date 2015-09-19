GoConvey
===

### 库简介

[GoConvey](https://github.com/smartystreets/goconvey)可实现优雅地书写单元测试，简洁的语法和舒适的界面能够让一个不爱书写单元测试的开发人员从此爱上单元测试。

### 安装

可以通过以下两种方式下载安装 GoConvey：

```
gopm get github.com/smartystreets/goconvey
或
go get github.com/smartystreets/goconvey
```

### API 文档

[Go Walke](http://gowalker.org/github.com/smartystreets/goconvey)

### 示例

```go
package goconvey

import (
    "errors"
)

func Add(a, b int) int {
    return a + b
}

func Subtract(a, b int) int {
    return a - b
}

func Multiply(a, b int) int {
    return a * b
}

func Divide(a, b int) (int, error) {
    if b == 0 {
        return 0, errors.New("被除数不能为 0")
    }
    return a / b, nil
}
```

下面为这 4 个函数分别书写单元测试：

```go
package goconvey

import (
    "testing"
    . "github.com/smartystreets/goconvey/convey"
)

func TestAdd(t *testing.T) {
    Convey("将两数相加", t, func() {
        So(Add(1, 2), ShouldEqual, 3)
    })
}

func TestSubtract(t *testing.T) {
    Convey("将两数相减", t, func() {
        So(Subtract(1, 2), ShouldEqual, -1)
    })
}

func TestMultiply(t *testing.T) {
    Convey("将两数相乘", t, func() {
        So(Multiply(3, 2), ShouldEqual, 6)
    })
}

func TestDivide(t *testing.T) {
    Convey("将两数相除", t, func() {

        Convey("除以非 0 数", func() {
            num, err := Division(10, 2)
            So(err, ShouldBeNil)
            So(num, ShouldEqual, 5)
        })

        Convey("除以 0", func() {
            _, err := Division(10, 0)
            So(err, ShouldNotBeNil)
        })
    })
}

```

首先，使用官方推荐的方式导入 GoConvey 的辅助包以减少冗余代码：
```
. "github.com/smartystreets/goconvey/convey"。
```

其次，每个测试用例使用 Convey 函数包裹：

1. 第一个参数为 string 类型的描述
2. 第二个参数一般为 *testing.T，即本例中的变量 t
3. 第三个参数为不接收任何参数也不返回任何值的函数（常用闭包形式）。

Convey 语句同样可以无限嵌套，以体现各个测试用例之间的关系，如 TestDivision。
注意，只有最外层的 Convey 需要传入变量 t，内层的嵌套均不需要传入。

最后，使用 So 语句对条件进行判断。在本例中使用了 3 个不同类型的条件判断：ShouldBeNil、ShouldEqual 和 ShouldNotBeNil，分别表示值应该为 nil、值应该相等和值不应该为 nil。
更详细的条件列表，可以参见[官方文档](https://github.com/smartystreets/goconvey/wiki/Assertions)。

###　执行单元测试

三种方式，在需要执行的测试文件目录命令行下执行：

1. go test      (命令行统计)
2. go test -v   (详细的命令行统计)
3. goconvey     (Web页面统计)

注：使用 goconvey 需要先将$GOPATH/bin　目录添加到 $PATH 路径下。
