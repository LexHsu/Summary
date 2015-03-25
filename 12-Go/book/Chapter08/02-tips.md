Tips
===

### 使用单一的 GOPATH

有多个 GOPATH 会导致某些副作用，如可能使用了给定的库的不同的版本。可能在某个地方升级了它，但其他地方却没有升级。
因此除非项目很大且极为重要，否则不要为每个项目使用不同的 GOPATH。

### 将 for-select 封装到函数中

在某个条件下需要从 for-select 中退出，就需要使用标签。如：

```go
func main() {

    L:
    for {
        select {
            case <-time.After(time.Second):
            fmt.Println("hello")
            default:
            break L
        }
    }

    fmt.Println("ending")
}
```

可见，需要联合break使用标签。这有其用途，不过我不喜欢。
这个例子中的 for 循环看起来很小，但是通常它们会更大，而判断break的条件也更为冗长。
建议将 for-select 封装到函数中：

```go
func main() {
    foo()
    fmt.Println("ending")
}

func foo() {
    for {
        select {
            case <-time.After(time.Second):
            fmt.Println("hello")
            default:
            return
        }
    }
}
```

这样还可返回错误：

```go
// 阻塞
if err := foo(); err != nil {
    // 处理 err
}
```

### 在初始化结构体时使用带有标签的语法

这是一个无标签语法的例子：

```go
type T struct {
    Foo string
    Bar int
}

func main() {
    t := T{"example", 123} // 无标签语法
    fmt.Printf("t %+v\n", t)
}
```

那么如果你添加一个新的字段到T结构体，代码会编译失败：

```go
type T struct {
    Foo string
    Bar int
    Qux string
}

func main() {
    t := T{"example", 123} // 无法编译
    fmt.Printf("t %+v\n", t)
}
```

如果使用了标签语法，[Go 的兼容性规则](http://golang.org/doc/go1compat)会处理代码。
如[在向net包的类型添加Zone字段](http://golang.org/doc/go1.1#library)。
回到例子，使用标签语法：

```go
type T struct {
    Foo string
    Bar int
    Qux string
}

func main() {
    t := T{Foo: "example", Qux: 123}
    fmt.Printf("t %+v\n", t)
}
```

这个编译起来没问题，而且弹性也好。不论你如何添加其他字段到T结构体。你的代码总是能编译，并且在以后的 Go 的版本也可以保证这一点。只要在代码集中执行go vet，就可以发现所有的无标签的语法。

### 将结构体的初始化拆分到多行

如果有两个以上的字段，那么就用多行。它会让你的代码更加容易阅读，也就是说不要：

```go
T{Foo: "example", Bar:someLongVariable, Qux:anotherLongVariable, B: forgetToAddThisToo}
```
而是：

```go
T{
    Foo: "example",
    Bar: someLongVariable,
    Qux: anotherLongVariable,
    B: forgetToAddThisToo,
}
```

这有许多好处，首先它容易阅读，其次它使得允许或屏蔽字段初始化变得容易（只要注释或删除它们），最后添加其他字段也更容易（只要添加一行）。

### 为整数常量添加 String() 方法

如果你利用 iota 来使用自定义的整数枚举类型，务必要为其添加 String() 方法。例如，像这样：

```go
type State int

const (
    Running State = iota
    Stopped
    Rebooting
    Terminated
    )
```

如果你创建了这个类型的一个变量，然后输出，会得到一个[整数](http://play.golang.org/p/V5VVFB05HB)：

```go
func main() {
    state := Running

    // print: "state 0"
    fmt.Println("state ", state)
}
```

除非你回顾常量定义，否则这里的0看起来毫无意义。
只需要为State类型添加String()方法就可以修复该[问题](http://play.golang.org/p/ewMKl6K302)：

```go
func (s State) String() string {
    switch s {
        case Running:
        return "Running"
        case Stopped:
        return "Stopped"
        case Rebooting:
        return "Rebooting"
        case Terminated:
        return "Terminated"
        default:
        return "Unknown"
    }
}
```

新的输出是：state: Running。显然现在看起来可读性好了很多。在你调试程序的时候，这会带来更多的便利。同时还可以在实现 MarshalJSON()、UnmarshalJSON() 这类方法的时候使用同样的手段。

### 让 iota 从 a +1 开始增量

在前面的例子中同时也产生了一个我已经遇到过许多次的 bug。假设你有一个新的结构体，有一个State字段：

```go
type T struct {
    Name  string
    Port  int
    State State
}
```

现在如果基于 T 创建一个新的变量，然后输出，会得到[奇怪的结果](http://play.golang.org/p/LPG2RF3y39)：

```go
func main() {
    t := T{Name: "example", Port: 6666}

    // prints: "t {Name:example Port:6666 State:Running}"
    fmt.Printf("t %+v\n", t)
}
```

看到 bug 了吗？State字段没有初始化，Go 默认使用对应类型的零值进行填充。由于State是一个整数，零值也就是0，但在我们的例子中它表示Running。

那么如何知道 State 被初始化了？还是它真得是在Running模式？
没有办法区分它们，那么这就会产生未知的、不可预测的 bug。
不过，修复这个很容易，只要让 iota [从 +1 开始](http://play.golang.org/p/VyAq-3OItv)：

```go
const (
    Running State = iota + 1
    Stopped
    Rebooting
    Terminated
    )
```

现在t变量将默认输出Unknown：

```go
func main() {
    t := T{Name: "example", Port: 6666}

    // 输出： "t {Name:example Port:6666 State:Unknown}"
    fmt.Printf("t %+v\n", t)
}
```

不过让 iota 从零值开始也是一种解决办法。例如，你可以引入一个新的状态叫做Unknown，将其修改为：

```go
const (
    Unknown State = iota
    Running
    Stopped
    Rebooting
    Terminated
    )
```

### 返回函数调用

我已经看过很多代码例如（http://play.golang.org/p/8Rz1EJwFTZ）：

```go
func bar() (string, error) {
    v, err := foo()
    if err != nil {
        return "", err
    }

    return v, nil
}
```

```go
func bar() (string, error) {
    return foo()
}
```

### 把 slice、map 等定义为自定义类型

将 slice 或 map 定义成自定义类型可以让代码维护起来更加容易。假设有一个Server类型和一个返回服务器列表的函数：

```go
type Server struct {
    Name string
}

func ListServers() []Server {
    return []Server{
        {Name: "Server1"},
        {Name: "Server2"},
        {Name: "Foo1"},
        {Name: "Foo2"},
    }
}
```

现在假设需要获取某些特定名字的服务器。需要对 ListServers() 做一些改动，增加筛选条件：

```go
// ListServers 返回服务器列表。只会返回包含 name 的服务器。空的 name 将会返回所有服务器。
func ListServers(name string) []Server {
    servers := []Server{
        {Name: "Server1"},
        {Name: "Server2"},
        {Name: "Foo1"},
        {Name: "Foo2"},
    }

    // 返回所有服务器
    if name == "" {
        return servers
    }

    // 返回过滤后的结果
    filtered := make([]Server, 0)

    for _, server := range servers {
        if strings.Contains(server.Name, name) {
            filtered = append(filtered, server)
        }
    }

    return filtered
}
```

现在可以用这个来筛选有字符串Foo的服务器：

```go
func main() {
    servers := ListServers("Foo")

    // 输出：“servers [{Name:Foo1} {Name:Foo2}]”
    fmt.Printf("servers %+v\n", servers)
}
```

显然这个函数能够正常工作。不过它的弹性并不好。如果你想对服务器集合引入其他逻辑的话会如何呢？例如检查所有服务器的状态，为每个服务器创建一个数据库记录，用其他字段进行筛选等等……

现在引入一个叫做Servers的新类型，并且修改原始版本的 ListServers() 返回这个新类型：

```go
type Servers []Server

// ListServers 返回服务器列表
func ListServers() Servers {
    return []Server{
        {Name: "Server1"},
        {Name: "Server2"},
        {Name: "Foo1"},
        {Name: "Foo2"},
    }
}
```

现在需要做的是只要为Servers类型添加一个新的Filter()方法：

```go
// Filter 返回包含 name 的服务器。空的 name 将会返回所有服务器。
func (s Servers) Filter(name string) Servers {
    filtered := make(Servers, 0)

    for _, server := range s {
        if strings.Contains(server.Name, name) {
            filtered = append(filtered, server)
        }

    }

    return filtered
}
```

现在可以针对字符串Foo筛选服务器：

```go
func main() {
    servers := ListServers()
    servers = servers.Filter("Foo")
    fmt.Printf("servers %+v\n", servers)
}
```

还想对服务器的状态进行检查？或者为每个服务器添加一条数据库记录？没问题，添加以下新方法即可：

```go
func (s Servers) Check()
func (s Servers) AddRecord()
func (s Servers) Len()
```

### withContext 封装函数

有时对于函数会有一些重复劳动，例如锁/解锁，初始化一个新的局部上下文，准备初始化变量等等……这里有一个例子：

```go
func foo() {
    mu.Lock()
    defer mu.Unlock()

    // foo 相关的工作
}

func bar() {
    mu.Lock()
    defer mu.Unlock()

    // bar 相关的工作
}

func qux() {
    mu.Lock()
    defer mu.Unlock()

    // qux 相关的工作
}
```

如果你想要修改某个内容，你需要对所有的都进行修改。如果它是一个常见的任务，那么最好创建一个叫做withContext的函数。这个函数的输入参数是另一个函数，并用调用者提供的上下文来调用它：

```go
func withLockContext(fn func()) {
    mu.Lock
    defer mu.Unlock()

    fn()
}
```

只需要将之前的函数用这个进行封装：

```go
func foo() {
    withLockContext(func() {
        // foo 相关工作
        })
    }

    func bar() {
        withLockContext(func() {
            // bar 相关工作
            })
        }

        func qux() {
            withLockContext(func() {
                // qux 相关工作
                })
            }
```

不要光想着加锁的情形。对此来说最好的用例是数据库链接。现在对 withContext 函数作一些小小的改动：

```go
func withDBContext(fn func(db DB) error) error {
    // 从连接池获取一个数据库连接
    dbConn := NewDB()

    return fn(dbConn)
}
```

如你所见，它获取一个连接，然后传递给提供的参数，并且在调用函数的时候返回错误。你需要做的只是：

```go
func foo() {
    withDBContext(func(db *DB) error {
        // foo 相关工作
        })
    }

    func bar() {
        withDBContext(func(db *DB) error {
            // bar 相关工作
            })
        }

        func qux() {
            withDBContext(func(db *DB) error {
                // qux 相关工作
                })
            }
```

你在考虑一个不同的场景，例如作一些预初始化？没问题，只需要将它们加到withDBContext就可以了。这对于测试也同样有效。

这个方法有个缺陷，它增加了缩进并且更难阅读。再次提示，永远寻找最简单的解决方案。

### 为访问 map 增加 setter，getters

如果你重度使用 map 读写数据，那么就为其添加 getter 和 setter 吧。通过 getter 和 setter 你可以将逻辑封分别装到函数里。这里最常见的错误就是并发访问。如果你在某个 goroutein 里有这样的代码：

```go
m["foo"] = bar
// 还有
delete(m, "foo")
```

会发生什么？你们中的大多数应当已经非常熟悉这样的竞态了。简单来说这个竞态是由于 map 默认并非线程安全。不过你可以用互斥量来保护它们：
```go
mu.Lock()
m["foo"] = "bar"
mu.Unlock()
// 以及：
mu.Lock()
delete(m, "foo")
mu.Unlock()
```

假设你在其他地方也使用这个 map。你必须把互斥量放得到处都是！然而通过 getter 和 setter 函数就可以很容易的避免这个问题：

```go
func Put(key, value string) {
    mu.Lock()
    m[key] = value
    mu.Unlock()
}
func Delete(key string) {
    mu.Lock()
    delete(m, key)
    mu.Unlock()
}
```

使用接口可以对这一过程做进一步的改进。你可以将实现完全隐藏起来。只使用一个简单的、设计良好的接口，然后让包的用户使用它们：

```go
type Storage interface {
    Delete(key string)
    Get(key string) string
    Put(key, value string)
}
```

这只是个例子，不过你应该能体会到。对于底层的实现使用什么都没关系。不光是使用接口本身很简单，而且还解决了暴露内部数据结构带来的大量的问题。

但是得承认，有时只是为了同时对若干个变量加锁就使用接口会有些过分。理解你的程序，并且在你需要的时候使用这些改进。
