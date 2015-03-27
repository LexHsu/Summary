
Interface
=========

- Go 是静态类型语言。每个变量都有一个静态类型对应，在编译时确定，接口也是如此。尽管接口类型的变量在运行时值会发生改变，但其并不是动态类型，本质上还是静态类型。
- 接口是一个或多个方法签名的集合，任何类型的方法集中只要拥有与之对应的全部方法，就表示它实现了该接口，无须在该类型上显式添加接口声明。
- 接口命名常以 er 结尾。
- 接口只有方法签名，没有实现 。
- 可在接口中嵌入其他接口。
- 类型可实现多个接口。

```go
type Stringer interface {
    String() string
}

type Printer interface {
    Stringer
    Print()
}

type User struct {
    id int
    name string
}

func (self *User) String() string {
    return fmt.Println("user %d, %s", self.id, self.name)
}

func (self *User) Print() {
    fmt.Println(self.String())
}

func main() {
    var t Printer = &User{1, "Tom"} // *User 方法集包含 String, Print
    t.Print()
}

// 输出：
user 1, Tom
```

一个 interface 的值存储了一个赋给变量的具体值和这个值类型的描述。

```go
var r io.Reader
tty, err := os.OpenFile("/dev/tty", os.O_RDWR,0)
if err != nil {
    return nil,err
}
r = tty
```
上述代码中，r 包含了一个 `(value, type)` 对，即`(tty, *os.File)`，虽然 `*os.File` 实现了 Read，Write 等很多方法，但是 io.Reader 的接口只允许访问 Read 方法，若要访问 write 方法，可通过类型断言（type assertion）：

```go
var w io.Writer
w = r.(io.Writer)
```

- 接口是是一个确定的的方法集合。一个接口变量可存储任何实现了该接口方法的具体值(除了接口本身)。典型例子是 io.Reader 和 io.Writer：

```go
// Reader is the interface that wraps the basic Read method.
type Reader interface {
Read(p []byte) (n int, err error)
}

// Writer is the interface that wraps the basic Write method.
type Writer interface {
Write(p []byte) (n int, err error)
}
```
如果一个类型声明实现了 Reade 或 Write 方法，便实现了 io.Reader或 io.Writer 接口。如一个 io.Reader 的变量可以持有任何一个实现了 Read 方法的的类型的值。

```go
var r io.Reader
r = os.Stdin
r = bufio.NewReader(r)
r = new(bytes.Buffer)
// and so on
```
注意，无论变量 r 的具体值是什么，r 的类型永远是 io.Reader，因为 Go 是静态类型的，r 的静态类型就是 io.Reader。

- 空接口 interface{} 没有任何方法签名，这意味着任何类型都实现了空接口。其作用类似 Java 中的 Object 基类。

```go
func Print(v interface{}) {
    fmt.Println("%T: %v\n", v, v)
}

func main() {
    Print(1)
    Print("Hello, world!")
}

// 输出：
int: 1
string: Hello, world!
```

- 匿名接口可用作变量类型，或结构成员。

```go
type Tester struct {
    s interface {
        String() string
    }
}

type User struct {
    id   int
    name string
}

func (self *User) String() string {
    return fmt.Sprintf("user %d, %s", self.id, self.name)
}

func main() {
    t := Tester{&User{1, "Tom"}}
    fmt.Println(t.s.String())
}

// 输出：
user 1, Tom
```

- 利用类型推断，可判断接口对象是否为某个具体的接口或类型。

```go
type User struct {
    id   int
    name string
}

func (self *User) String() string {
    return fmt.Sprintf("%d, %s", self.id, self.name)
}

func main() {
    var o interface{} = &User{1, "Tom"}
    if i, ok := o.(fmt.Stringer); ok {      // ok-idiom
        fmt.Println(i)
    }

    u := o.(*User)
    // u := o.(User)            // panic: interface is *main.User, not main.User
    fmt.Println(u)
}
```

- 可用 switch 做批量类型判断，不支持 fallthrough。

```go
func main() {
    var o interface{} = &User{1, "Tom"}

    switch v := o.(type) {
    case nil:                                   // o == nil
        fmt.Println("nil")
    case fmt.Stringer:                          // interface
        fmt.Println(v)
    case func() string:                         // func
        fmt.Println(v())
    case *User:                                 // *struct
        fmt.Printf("%d, %s\n", v.id, v.name)
    default:
        fmt.Println("unknown")
    }
}
```

- 超集接口对象可转换为子集接口，反之出错。

```go
type Stringer interface {
    String() string
}

type Printer interface {
    String() string
    Print()
}

type User struct {
    id   int
    name string
}

func (self *User) String() string {
    return fmt.Sprintf("%d, %v", self.id, self.name)
}

func (self *User) Print() {
    fmt.Println(self.String())
}

func main() {
    var o Printer = &User{1, "Tom"}
    var s Stringer = o
    fmt.Println(s.String())
}
```

- 接口技巧，让编译器检查，以确保某个类型实现接口。

```go
var _ fmt.Stringer = (*Data)(nil)
```

- 某些时候，让函数直接实现接口更方便。

```go
type Tester interface {
    Do()
}

type FuncDo func()
func (self FuncDo) Do() { self() }

func main() {
    var t Tester = FuncDo(func() { println("Hello, World!") })
    t.Do()
}
```
