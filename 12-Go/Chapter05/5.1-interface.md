
Interface
=========

接口是一个或多个方法签名的集合，任何类型的方法集中只要拥有与之对应的全部方法，就表示它实现了该接口，无须在该类型上显式添加接口声明。

- 接口命名常以er结尾。
- 接口只有方法签名，没有实现
- 可在接口中嵌入其他接口
- 类型可实现多个接口

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
    var t Printer = &User{1, "Tom"} // *User方法集包含String, Print
    t.Print()
}

// 输出：
user 1, Tom
```


- 空接口interface{}没有任何方法签名，这意味着任何类型都实现了空接口。其作用类似Java中的Object基类。

```go
func Print(v interface{}) {
    fmt.Println("%T: %v\n", v, v)
}

func main() {
    Print(1)
    Print(Hello, world!")
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

- 可用switch做批量类型判断，不支持fallthrough。

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
