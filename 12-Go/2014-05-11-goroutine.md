
Goroutine
=========

- Go在语言层面提供goroutine协程支持，让并发程序编写变得简单。只要在函数调用语句前添加go关键字，即可创建并在后台运行goroutine。

```go
go func() {
    println("Hello, World!")
}()
```

- 调度器不能保证goroutine执行次序，且进程退出时不会等待goroutine结束。
- Go程序启动后默认仅允许一个系统线程服务于 goroutine （即使有多个goroutine也是运行在一个线程里，主函数main也是一个goroutine）。如果当前goroutine发生阻塞（如Sleep，channel读取阻塞等），才会让出CPU时间给其他同线程的goroutine。可通过runtime.GOMAXPROCS自行修改，让调度器用多个线程实现多核并行，而不仅仅是并发。

### 并发与并行
并发和并行的区别就是一个处理器同时处理多个任务和多个处理器或者是多核的处理器同时处理多个不同的任务。
前者是逻辑上的同时发生（simultaneous），而后者是物理上的同时发生．

并发性(concurrency)，又称共行性，是指能处理多个同时性活动的能力，并发事件之间不一定要同一时刻发生。

并行(parallelism)是指同时发生的两个并发事件，具有并发的含义，而并发则不一定并行。

![Alt text](../99-Images/concurrency_parallelism.jpg)

```go
package main

import (
    "fmt"
)

func say(s string) {
    for i := 0; i < 5; i++ {
        fmt.Println(s)
    }
}

func main() {
    go say("world") //开一个新的Goroutines执行
    for {
    }
}

// 按道理应该打印5次 world ，但上述代码什么也没有打印
// 因为这里Go仍然在使用单核，for死循环占据了单核CPU所有的资源
// 而main和say两个goroutine默认在一个线程里面， 所以say没有机会执行。解决方案有两个：
// 1. 允许Go使用多核(runtime.GOMAXPROCS)

```go
package main

import (
    "fmt"
    "runtime"
)

func say(s string) {
    for i := 0; i < 5; i++ {

        fmt.Println(s)
    }
}

func main() {
    runtime.GOMAXPROCS(2) // 最多同时使用2个核
    go say("world") //开一个新的Goroutines执行
    for {
    }
}


// 2. 手动显式调动(runtime.Gosched)

package main

import (
    "fmt"
    "runtime"
)

func say(s string) {
    for i := 0; i < 5; i++ {
        fmt.Println(s)
    }
}

func main() {
    go say("world") //开一个新的Goroutines执行
    for {
        runtime.Gosched() // 显式地让出CPU时间给其他goroutine
    }
}
// 这种主动让出CPU时间的方式仍然是在单核里跑。但手工地切换goroutine导致了看上去的“并行”。
```

### runtime 调度器
在同一个原生线程里，如果当前 goroutine 不发生阻塞，它是不会让出CPU时间给其他同线程的goroutines的，这是Go运行时对goroutine的调度，我们也可以使用runtime包来手工调度。关于runtime包，有几个常用函数:

- `Gosched` 让出 CPU 时间给其他 goroutine

- `NumCPU` 返回当前系统的 CPU 核数量

- `GOMAXPROCS` 设置最大的可同时使用的CPU核数

- `Goexit` 退出当前goroutine(但是defer语句会照常执行)

```go
func sum(id int) {
    var x int64
    for i := 0; i < math.MaxUint32; i++ {
        x += int64(i)
    }

    println(id, x)
}

func main() {
    wg := new(sync.WaitGroup)
    wg.Add(2)

    for i := 0; i < 2; i++ {
        go func(id int) {
            defer wg.Done()
            sum(id)
        }(i)
    }

    wg.Wait()
}

// 输出：
$ go build -o test

$ time -p ./test

0 9223372030412324865
1 9223372030412324865

real   7.70                     // 程序开始到结束时间差(非CPU时间)
user   7.66                     // 用户态所使用CPU时间片(多核累加)
sys    0.01                     // 内核态所使用CPU时间片

$ GOMAXPROCS=2 time -p ./test

0 9223372030412324865
1 9223372030412324865

real 4.18
user 7.61           // 虽然总时间差不多，但由于2个核并行，real时间少了许多
sys 0.02
```

- 调用runtime.Goexit将终止当前goroutine执行，但调度器会确保所有已注册defer延迟调用被执行。

```go
func main() {
    wg := new(sync.WaitGroup)
    wg.Add(1)

    go func() {
        defer wg.Done()
        defer println("A.defer")

        func() {
            defer println("B.defer")
            runtime.Goexit()
            println("B")
        }()

        println("A")
    }()

    wg.Wait()
}

// 输出：
B.defer
A.defer
```

- Gosched让出底层线程，将当前goroutine暂停，放回队列等待下次被调度执行。

```go
func main() {
    wg := new(sync.WaitGroup)
    wg.Add(2)

    go func() {
        defer wg.Done()
        for i := 0; i < 6; i++ {
            println(i)
            if i == 3 { runtime.Gosched() }
        }
    }()

    go func() {
        defer wg.Done()
        println("Hello, World!")
    }()

    wg.Wait()
}

// 输出：
$ go run main.go
0
1
2
3
Hello, World!
4
5
```

### 同步
go 中有两种方式同步，一种使用 channel 来同步 goroutine，另一种使用锁机制 sync.WaitGroup，一种较为简单的同步方法集。

sync.WaitGroup 只有3个方法：
- `Add()` 添加计数
- `Done()` Add(-1)的别名，一个计数
- `Wait()` 计数不为0, 阻塞 Wait() 所在goroutine的运行

注意：应在运行main函数的goroutine里运行 Add() 函数，在其他 goroutine 里面运行 Done() 函数。

```go
package main

import (
    "fmt"
    "sync"
)

func main() {
    var wg sync.WaitGroup
    for i := 0; i < 5; i++ {
        fmt.Println("add", i)
        wg.Add(1)
    }

    for i := 0; i < 5; i++ {
        fmt.Println("subtract", i)
        go wg.Done()
    }
    fmt.Println("exit")
    wg.Wait()
}
```
