反射规则
===

reflect 包实现了运行时反射，允许程序通过抽象类型操作对象。
通常用于处理静态类型 interface{} 的值，并通过 Typeof 解析出其动态类型信息，通常会返回一个有接口类型 Type 的对象。

### 从接口到反射对象

一个接口类型的变量包括变量的的具体值和该值的类型，即 `(Type, Value)`。
而反射就是一个检查接口变量中的 `(Type, Value)` 组合的机制。

可通过 reflect 包中的 reflect.TypeOf 函数和 reflect.ValueOf 函数获取 reflect.Type 和 reflect.Value。

```go
package main

import (
    "fmt"
    "reflect"
)

func main() {
    var x float = 3.14
    fmt.Println("type: ", reflect.TpyeOf(x))
    fmt.Println("value: ", reflect.ValueOf(x))
}

// result:
type: float64
value: <float64 Value>
```

- Type 和 Value 都有一系列方法。如 Value 获取被存储的值（float64）。

```go
var x float64 = 3.14
v := reflect.ValueOf(x)
fmt.Println("type: ", v.Type())  // 通过 reflect.Value 可得到 reflect.Type
fmt.Println("kind is floate64: ", v.Kind() == reflect.Float64)
fmt.Println("value: ", v.Float())

// result:
type: float64
kind is float64: true
value: 3.14
```

- Type 和 Value 都有 Kind 方法，其返回底层类型，非静态类型。

```go
type MyInt int
var x MyInt = 7            // x 为静态类型 MyInt
v := reflect.ValueOf(x)    // v 为底层类型 reflect.int
```

### 从反射对象到接口

Go 中的反射可逆，reflect.Value 可通过 interface 方法转换为接口，再通过类型断言转换为具体类型。

```go
// Interface returns v's value as an interface{}.
func (v Value) Interface() interface{}
```

举个例子

```go
y := v.Interface().(float64) // y will have type float64.
fmt.Println(y)
```
简而言之，Interface 方法就是 ValueOf 方法的逆，除非 ValueOf 所得结果的类型是 interface{}。

### 修改反射对象，值必须可设置

可设置（settability）是 reflect.Value 的特性之一，可通过 reflect.Value 的 Set 方法更改反射对象的值，但并非所有的 Value 都是可设置的。

```go
var x float64 = 3.14
v := reflect.ValueOf(x)
fmt.Println("settability of v:",v.CanSet())  // prints: settability of v: false
v.SetFloat(2.8)    // panic
```
因为上述代码传给 reflect.ValueOf 的是 x 的一个副本，而不是 x 自身，因此 x 并没有被 Set，可以通过传给函数一个指向参数的指针达到修改参数本身的目的，修改代码：

```go
var x float64 = 3.14
p := reflect.ValueOf(&x)  // Note: take the address of x.
fmt.Println("type of p:", p.Type())
fmt.Println("settability of p:", p.CanSet()) // 判断该 Value 是否可设置

// Result:
type of p: *float64
settability of p: false
```
上述代码 p 的类型已经为 `*float64` ，为何还是不能 Set ？
因为 p 是一个指针，并不是要 Set 该指针的值，而是要 Set p 所指地址的内容（也就是 `*p`），所以 p 依然不可 Set，
可通过 Value 的 Elem 方法来获取指向 Value 的地址。

```go
v := p.Elem()
fmt.Println("settability of v:", v.CanSet())  // prints: settabiliyty of v : true

v.SetFloat(2.8)
fmt.Println(v.Interface())  // prints: 2.8
fmt.Println(x)              // prints: 2.8
```

### 其他

为了保持API的简洁，Value 的 Getter 和 Setter 方法是用最大的类型去操作数据：例如让所有的整型都使用 int64 表示。
所以，Value 的 Int 方法返回一个 int64 的值，SetInt 需要传入 int64 参数；将数值转换成它的实际类型在某些时候是有必要的.

```go
var x uint8 = 'x'
v := reflect.ValueOf(x)
fmt.Println("type:", v.Type())                            // uint8.
fmt.Println("kind is uint8: ", v.Kind() == reflect.Uint8) // true.
x = uint8(v.Uint())                                       // v.Uint returns a uint64.
```

反射 struct。

```go
type T struct {
  A int
  B string
}

t := T{23, "helloworld"}
s := reflect.ValueOf(&t).Elem()
typeOfT := s.Type()
for i := 0; i < s.NumField(); i++ {
  f := s.Field(i)
  fmt.Printf("%d: %s %s = %v\n", i, typeOfT.Field(i).Name, f.Type(), f.Interface())
}

// Result：
0: A int = 23
1: B string = helloworld

// 因为 s 包含了一个可设置的反射对象，也可通过如下方法修改 T 的值
s.Field(0).SetInt(2)
s.Field(1).SetString("hello")
fmt.Println("t is now", t)

// Result:
t is now {2 hello}
```
- T 的字段名是大写（字段可导出/公共字段）的原因在于，结构体中只有可导出的的字段是“可设置”的。
- 如果修改程序让 s 由 t（而不是 &t）创建，程序调用 SetInt 和 SetString 就会失败，因为 t 字段是不可设置的。


### 总结

再次列出反射法则：

1. 反射从接口值到反射对象中 (Reflection goes from interface value to reflection object.)
2. 反射从反射对象到接口值中 (Reflection goes from reflection object to interface value.)
3. 要修改反射对象，值必须是“可设置”的 (To modify a reflection object, the value must be settable.)
