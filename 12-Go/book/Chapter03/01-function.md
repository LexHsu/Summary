
函数
=========

### 函数定义
不支持嵌套、重载和默认参数。
- 无需声明原型。
- 支持不定长变参。
- 支持多返回值。
- 支持命名返回参数。
- 支持匿名函数和闭包。

- 函数关键字 func，左大括号不能另起一行。

```go
func test(x, y int, s string) (int, string) {   // 类型相同的相邻参数可合并
    n := x + y                                  // 多返回值必须用括号
    return n, fmt.Sprintf(s, n)
}
```

- 函数为第一类对象，可作为参数传递，复杂函数作为参数可先自定义类型。

```go
func test(fn func() int) int {
    return fn()
}
type FormatFunc func(s string, x, y int) string     // 定义函数类型
func format(fn FormatFunc, s string, x, y int) string {
    return fn(s, x, y)
}
func main() {
    s1 := test(func() int { return 100 })           // 直接将匿名函数当参数
    s2 := format(func(s string, x, y int) string {
        return fmt.Sprintf(s, x, y)
    }, "%d, %d", 10, 20)
    println(s1, s2)
}
```

- 有返回值的函数，必须有明确的终止语句，否则会引发编译错误。

### 变参
- 变参本质上就是 slice，只能有一个，且必须放在最后。

```go
func test(s string, n ...int) string {
    var x int
    for _, i := range n {
        x += i
}
    return fmt.Sprintf(s, x)
}
func main() {
    println(test("sum: %d", 1, 2, 3))
}
```

- 使用 slice 对象作为变参时，必须展开。

```go
func main() {
    s := []int{1, 2, 3}
    println(test("sum: %d", s...))
}
```

### 返回值
- 不能用容器对象接收多返回值，只能用多个变量，或 "_" 忽略。

```go
func test() (int, int) {
    return 1, 2
}

func main() {
    // s := make([]int, 2)
    // s = test()  // Error: multiple-value test() in single-value context
    x, _ := test()
    println(x)
}
```

- 多返回值可直接作为其他函数调用实参。

```go
func test() (int, int) {
    return 1, 2
}
func add(x, y int) int {
    return x + y
}
func sum(n ...int) int {
    var x int
    for _, i := range n {
        x += i
    }
    return x
}
func main() {
    println(add(test()))
    println(sum(test()))
}
```

- 命名返回参数可看做与形参类似的局部变量，最后由 return 隐式返回。

```go
func add(x, y int) (z int) {
    z=x+y
    return
}
func main() {
    println(add(1, 2))
}
```

- 命名返回参数可被同名局部变量遮蔽，此时需要显式返回。

```go
func add(x, y int) (z int) {
    {                   // 不能在一个级别，会引发 "z redeclared in this block" 错误
        var z = x + y
        // return       // Error: z is shadowed during return
        return z        // 必须显式返回
    }
}
```

- 命名返回参数允许 defer 延迟调⽤用通过闭包读取和修改。

```go
func add(x, y int) (z int) {
    defer func() {
        z += 100
    }()
    z=x+y
    return
}

func main() {
    println(add(1, 2))
}

// 输出:
103
```

- 显式 return 前，会先修改命名返回参数。

```go
func add(x, y int) (z int) {
    defer func() {
        println(z)          // 输出: 203
    }()

    z=x+y
    return z + 200          // 执行顺序: (z = z + 200) -> (call defer) -> (ret)
}
func main() {
    println(add(1, 2))      // 输出: 203
}
```

### 匿名函数

- 匿名函数可以赋值给变量，作为结构字段，或者在 channel 里传送。

```go
// function variable
fn := func() { println("Hello, World!") }
fn()

// function collection
fns := [](func(x int) int){
    func(x int) int { return x + 1 },
    func(x int) int { return x + 2 },
}
println(fns[0](100))

// function as field
d := struct {
    fn func() string
}{
    fn: func() string { return "Hello, World!" },
}
println(d.fn())

// channel of function
fc := make(chan func() string, 2)
fc <- func() string { return "Hello, World!" }
println((<-fc)())
```

- 闭包复制的是原对象指针，这就很容易解释延迟引用现象。

```go
func test() func() {
    x := 100
    fmt.Printf("x (%p) = %d\n", &x, x)
    return func() {
        fmt.Printf("x (%p) = %d\n", &x, x)
    }
}

func main() {
    f := test()
    f()
}

// 输出：
x (0x2101ef018) = 100
x (0x2101ef018) = 100
```
- 在汇编层面，test 实际返回的是 FuncVal 对象，其中包含了匿名函数地址、闭包对象指针。
只需将返回对象地址保存到某个寄存器，就可让匿名函数获取相关闭包对象指针。

```go
FuncVal { func_address, closure_var_pointer ... }
```

### defer延迟调用

- defer用于注册延迟调用，这些调用直到 ret 前才被执行，通常用于释放资源或错误处理。

```go
func test() error {
    f, err := os.Create("test.txt")
    if err != nil { return err }

    defer f.Close()         // 注册调用，而不是注册函数。必须提供参数，即使是 void 也要提供

    f.WriteString("Hello, World!")
    return nil
}
```

- 多个 defer 语句按 FILO 次序执行，若某个 defer 发生错误，错误信息会放到最后，其他 defer 还是依次执行。

```go
func test(x int) {
    defer println("a")
    defer println("b")
    defer func() {
        println(100 / x)
    }()

    defer println("c")
}

func main() {
    test(0)
}

// 输出：
c
b
a
panic: runtime error: integer divide by zero
```

- 延迟调用参数在注册时求值或复制，可用指针或闭包延迟读取。

```go
func test() {
    x, y := 10, 20
    defer func(i int) {
        println("defer:", i, y)     // y 闭包引⽤用
    }(x)                            // x 被复制

    x += 10
    y += 100
    println("x =", x, "y =", y)
}

// 输出：
x = 20 y = 120
defer: 10 120
```

### 错误处理
- 没有结构化异常，使用 panic 抛出错误，recover 捕获错误。

```go
func test() {
    defer func() {
        if err := recover(); err != nil {
            println(err.(string))       // 将 interface{} 转换为具体类型
        }
    }()

    panic("panic error!")
}
```

- 由于panic，recover参数类型为 interface{}，因此可以抛出任意类型对象。

```go
func panic(v interface{})
func recover() interface{}
```

- 延迟调用中引发的错误，可以被后续延迟调用捕获，但仅最后一个错误可被捕获。

```go
func test() {
    defer func() {
        fmt.Println(recover())
    }()

    defer func() {
        panic("defer panic")
    }()

    panic("test panic")
}

func main() {
    test()
}

// 输出：
defer panic
```

- 捕获函数 revocer() 只有在延迟调用(defer)内直接调用才会终止错误，否则总是返回 nil，任何未捕获的错误都会调用堆栈传递。

```go
func test() {
    defer recover()                 // 无效
    defer fmt.Println(recover())    // 无效
    defer func() {
        func() {
            println("defer inner")
            recover()               // 无效
        }()
    }()

    panic("test panic")
}

func main() {
    test()
}

// 输出：
defer inner
<nil>
panic: test panic
```

- 使用延迟匿名函数或下面这样都有效。

```go
func except() {
    recover()
}

func test() {
    defer except()
    panic("test panic")
}
```

- 有时候需要在每个函数内分别处理，如 locking / unlocking，可分别封装。

```go
func foo() {
    mu.Lock()
    defer mu.Unlock()

    // foo related stuff
}

func bar() {
    mu.Lock()
    defer mu.Unlock()

    // bar related stuff
}

// 封装为
func withLockContext(fn func()) {
    mu.Lock
    defer mu.Unlock()

    fn()
}

// 原来的函数重构为
func foo() {
    withLockContext(func() {
        // foo related stuff
    })
}

func bar() {
    withLockContext(func() {
        // bar related stuff
    })
}
```

该封装也常用于数据库连接。

```go
func withDBContext(fn func(db DB)) error {
    // get a db connection from the connection pool
    dbConn := NewDB()

    return fn(dbConn)
}
```

现在得到了一个连接，可传给相应函数。

```go
func foo() {
    withDBContext(func(db *DB) error {
        // foo related stuff
    })
}

func bar() {
    withDBContext(func(db *DB) error {
        // bar related stuff
    })
}
```

- 如果要保护代码片段，可将代码块重构成匿名函数，确保后续代码被执行。

```go
func test(x, y int) {
    var z int
    func() {
        defer func() {
            if recover() != nil { z = 0 }
        }()

        z=x/y
        return
    }()

    println("x / y =", z)
}
```

- 除用 panic 中断错误外，也可返回 error 类型错误对象来表示函数调用状态。

```go
type error interface {
    Error() string
}
```

- 标准库 error.New 和 fmt.Errorf 函数用于创建实现 error 接口的错误对象。通过判断错误对象实例来确定具体错误类型。

```go
var ErrDivByZero = errors.New("division by zero")

func div(x, y int) (int, error) {
    if y == 0 { return 0, ErrDivByZero }
    return x / y, nil
}

func main() {
    switch z, err := div(10, 0); err {
    case nil:
        println(z)
    case ErrDivByZero:
        panic(err)
    }
}
```

- panic 和 error 方式使用区别：通常，包内部使用 panic，对外 API 使用 error 返回值。
