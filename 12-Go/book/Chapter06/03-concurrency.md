Go 并发模型
===

### 生成器

非并发做法：

```go
// 函数 rand_generator_1 ，返回 int
func rand_generator_1() int {
    return rand.Int()
}
```

并发做法：

```go
// 函数 rand_generator_2，返回 通道(Channel)
func rand_generator_2() chan int {
    // 创建通道
    out := make(chan int)
    // 创建协程
    go func() {
        for {
            // 向通道内写入数据，如果无人读取会等待
            out <- rand.Int()
        }
    }()
    return out
}

func main() {
    // 生成随机数作为一个服务
    rand_service_handler := rand_generator_2()
    // 从服务中读取随机数并打印
    fmt.Printf("%dn", <-rand_service_handler)
}
```

调用生成器，可以返回一个“服务”。可以用在持续获取数据的场合。用途很广泛，读取数据，生成ID，甚至定时器。这是一种非常简洁的思路，将程序并发化。
如果需要大量访问，可采用多路复用技术，启动若干生成器，再将其整合成一个更大的服务。

### 多路复用

多路复用是让一次处理多个队列的技术。下例为并发执行的随机数生成器。

```go
// 函数 rand_generator_3 ，返回通道(Channel)
func rand_generator_3() chan int {
    // 创建两个随机数生成器服务
    rand_generator_1 := rand_generator_2()
    rand_generator_2 := rand_generator_2()

    // 创建通道
    out := make(chan int)

    // 创建协程
    go func() {
        for {
            // 读取生成器1中的数据，整合
            out <- <-rand_generator_1
        }
    }()
    go func() {
        for {
            // 读取生成器2中的数据，整合
            out <- <-rand_generator_2
        }
    }()
    return out
}
```

多路复用技术可以用来整合多个通道。提升性能和操作的便捷。
