
Goroutine
=========

- Go在语言层面提供goroutine协程支持，让并发程序编写变得简单。只要在函数调用语句前添加go关键字，即可创建并在后台运行goroutine。

```go
go func() {
    println("Hello, World!")
}()
```

- 调度器不能保证goroutine执行次序，且进程退出时不会等待goroutine结束。
- Go程序启动后默认仅允许一个系统线程服务于goroutine。可通过runtime.GOMAXPROCS自行修改，让调度器用多个线程实现多核并行，而不仅仅是并发。

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

- 调用runtime.Goexit将终止当前goroutine执行，调度器确保所有已注册defer延迟调用被执行。

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
