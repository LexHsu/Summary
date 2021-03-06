
Slice
=========

由于数组长度不可变，在特定场景不适用，Go 提供灵活，功能强悍的内置类型 Slice，
slice 是引用类型，自身是结构体，值拷贝传递，并不是数组或者数组指针。
与数组相比切片的长度是不固定的，其通过内部指针和相关属性引用数组片段，以实现变长方案。

```c
// runtime.h源码
struct Slice
{                           // must not move anything
    byte*    array;         // actual data
    uintgo   len;           // number of elements
    uintgo   cap;           // allocated number of elements
};

```

1. 属性 len 表示可用元素数量，读写操作不能超过该限制。
2. 属性 cap 表示最大扩张容量，不能超出数组限制。
3. 如果 slice == nil，那么 len，cap 都为 0。
4. 创建表达式使用的是元素索引号，而非数量。

```go
s :=[]int {1, 2, 3}
// 直接初始化切片，[] 表示是切片类型，该 slice 初始化值依次为 1, 2, 3。其 cap = len = 3

arr := [...]int {1, 2, 3}
s := arr[:]
// 初始化切片 s，为数组 arr 的引用

s := arr[startIndex:endIndex]
// 将 arr 中取索引为 startIndex 到 endIndex - 1 的元素创建为一个新的切片

s := arr[startIndex:]
// 缺省 endIndex 时将表示一直到 arr 的最后一个元素

s := arr[:endIndex]
// 缺省 startIndex 时将表示从 arr 的第一个元素开始，到 endIndex - 1 结束

s1 := s[startIndex:endIndex]
// 通过切片 s 初始化切片 s1

s :=make([]int, len, cap)
// 通过内置函数 make() 初始化切片 s，[]int 标识为其元素类型为 int 的切片

// 【例1】
data := [...]int{0, 1, 2, 3, 4, 5, 6}     // 注意 slice 与数组区别，slice无...
slice := data[1:4:5]                      // [startIndex : endIndex : capIndex]

      +---+---+---+---+---+---+---+                +---------+---------+---------+
data  | 0 | 1 | 2 | 3 | 4 | 5 | 6 |         slice  | pointer | len = 3 | cap = 4 |
      +---+---+---+---+---+---+---+                +---------+---------+---------+
          |<-- len -->|   |                        | len = endIndex - startIndex
          |<---- cap ---->|                        | cap = capIndex - startIndex
          |<---------------------------------------+


// 【例2】
data := [...]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}

expression   slice                    len    cap     comments
------------+----------------------+------+-------+-----------------------------
data[:6:8]   [0 1 2 3 4 5]            6       8      // 省略 startIndex
data[5:]     [5 6 7 8 9]              5       5      // 省略 endIndex，capIndex
data[:3]     [0 1 2]                  3       10     // 省略 startIndex，capIndex
data[:]      [0 1 2 3 4 5 6 7 8 9]    10      10     // 全部省略，即为数组 data 的引用
```

- 读写操作实际目标是底层数组，只需注意索引号的差别。

```go
data := [...]int{0, 1, 2, 3, 4, 5}
s := data[2:4]
s[0] += 100
s[1] += 200
fmt.Println(s)
fmt.Println(data)

// 输出：
[102 203]
[0 1 102 203 4 5]
```

- 可直接创建slice对象，自动分配底层数组。

```go
s1 := []int{0, 1, 2, 3, 8: 100}     // 通过初始化表达式构造，可使用索引号
fmt.Println(s1, len(s1), cap(s1))
s2 := make([]int, 6, 8)             // 使用 make 创建，指定 len 和 cap 值
fmt.Println(s2, len(s2), cap(s2))
s3 := make([]int, 6)                // 省略 cap，相当于 cap = len
fmt.Println(s3, len(s3), cap(s3))

// 输出：
[0 1 2 3 0 0 0 0 100]  9 9
[0 0 0 0 0 0]          6 8
[0 0 0 0 0 0]          6 6
```

- 使用 make 动态创建 slice，避免了数组必须用常量做长度的麻烦。还可以用指针直接访问底层数组，退化成普通数组操作。

```go
s := []int{0, 1, 2, 3}
p := &s[2] // *int, 获取底层数组元素指针。
*p += 100

fmt.Println(s)

// 输出：
[0 1 102 3]
```

- [][]T，是指元素类型为[]T。

```go
data := [][]int{
    []int{1, 2, 3},
    []int{100, 200},
    []int{11, 22, 33, 44},
}
```

- 当 slice 超出 array 的长度时，Go 会隐式对 array 做拷贝，并让 slice 内部指向了新数组。

```go
array := [5]int{1, 2, 3, 4, 5}

// 1. 创建一个 slice，切出数组的中间3个值：
slice := array[1:4]
// 输出 slice：
[2 3 4]

// 2. 对 slice 执行 append：
slice = append(slice, -5)
fmt.Println(slice1, arr)
// 输出 slice，array：
[2 3 4 -5] [1 2 3 4 -5]

// 3. 修改 slice[0]：
slice[0] = 99
// 输出 array：
[1 99 3 4 -5]
// 可见此时没有拷贝，slice 在操作原数组

// 4. 再对 slice 进行 append (注意 slice 长度超出了 array)

slice = append(slice, 100)
slice[0] = 99

// 输出 array：
[1 2 3 4 -5]

// 因此大批量添加数据时，应该避免使用 append，其频繁创建 slice 对象会影响性能。
// 可一次性分配 len 足够大的 slice，及时释放不再使用的 slice 对象。避免持有过期数组，GC 无法回收。
```

- 基于已有 slice 创建新的 slice 对象，依旧是指向原底层数组

```go
s := []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}

s1 := s[2:5]        // [2 3 4]
s2 := s1[2:6:7]     // [4 5 6 7]
s3 := s2[3:6]       // Error

      +---+---+---+---+---+---+---+---+---+---+
slice | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
      +---+---+---+---+---+---+---+---+---+---+

              +---+---+---+---+---+---+---+---+
s1            | 2 | 3 | 4 |   |   |   |   |   |    len = 3, cap = 8
              +---+---+---+---+---+---+---+---+

                      +---+---+---+---+---+
s2                    | 4 | 5 | 6 | 7 |   |        len = 4, cap = 5
                      +---+---+---+---+---+

                                  +---+---+---+
s3                                | 7 | 8 | X |     error: slice bounds out of range
                                  +---+---+---+

s := []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
s1 := s[2:5]       // [2 3 4]
s1[2] = 100

s2 := s1[2:6]      // [100 5 6 7]
s2[3] = 200

fmt.Println(s)

// 输出：
[0 1 2 3 100 5 6 200 8 9]
```

- 函数copy在两个slice间复制数据，复制长度以len小的为准。两个slice可指向同一底层数据，允许元素区间重叠。

```go
data := [...]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}

s := data[8:]
s2 := data[:5]

copy(s2, s)          // dst:s2, src:s

fmt.Println(s2)
fmt.Println(data)

// 输出：
[8 9 2 3 4]
[8 9 2 3 4 5 6 7 8 9]

// 应及时将所需数据 copy 到较小的 slice，以便释放大号底层数据内存。
```

- 在函数间传递 slice 是很廉价的，因为 slice 相当于是指向底层数组的指针。

```go
slice := make([]int, 1e6)
// 64 位机器，slice 需要 24 字节内存，指针部分需要 8 字节，长度和容量分别需要 8 字节
slice = foo(slice)

func foo(slice []int) []int {
    ...
    return slice
}
```
