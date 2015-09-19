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
