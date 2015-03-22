new 与 make 区别
===

### new

new 是内建函数，[官方文档](http://golang.org/pkg/builtin/#new)，定义如下：

```go
func new(Type) *Type
```

内建函数 new 用来分配内存，它的第一个参数是一个类型，不是一个值，它的返回值是一个指向新分配类型零值的指针。
根据这段描述，可自行实现类似 new 的功能：

```go
func newInt() *int {
    var i int
    return &i
}
someInt := newInt()     // 同 someInt := new(int)
```

### make

make 也是内建函数，[官方文档](http://golang.org/pkg/builtin/#make)，定义如下：

```go
func make(Type, size IntegerType) Type
```

内建函数 make 用来为 slice，map 或 chan 类型分配内存和初始化一个对象（注意：只能用在这三种类型上），
跟 new 类似，第一个参数也是一个类型而不是一个值。区别在于，make 返回类型的引用而不是指针，而返回值也依赖于具体传入的类型，具体说明如下：

```
Slice
size 指定了它的长度，它的容量和长度相同，如 make([]int, 5)
也可传入第三个参数来指定不同的容量值，但不能小于长度值，如 make([]int, 0, 10)

Map
根据 size 大小来初始化分配内存，不过分配后的 map 长度为 0，
如果 size 被忽略了，那么会在初始化分配内存时分配一个小尺寸的内存

Channel
管道缓冲区依据缓冲区容量被初始化。如果容量为 0 或忽略容量，则没有缓冲区
```

### 总结

new 用于初始化一个指向类型的指针 `*T`，make 用于为 slice，map 或 chan 初始化并返回引用 `T`。
