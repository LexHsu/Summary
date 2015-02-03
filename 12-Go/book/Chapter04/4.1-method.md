
Method
=========

- 方法总是绑定对象实例，并隐式将实例作为第一实参（receiver）。

- 只能为当前包内命名类型定义方法。

- 参数 receiver 可任意命名，若方法中未曾使用，可省略参数名。

- 参数 receiver 类型可以是 T 或者 `*T`。基类型 T 不能是接口或者指针。

- 不支持方法重载，receiver 只是参数签名的组成部分。

- 可用实例 value 或者 pointer 调用全部方法，编译器自动转换。

- 无构造方法，析构方法，通常用简单工厂模式返回对象实例。

```go
type Queue struct {
    elements []interface{}
}

func NewQueue() *Queue {                    // 创建对象实例
    return &Queue{make([]interface{}, 10)}
}

func (*Queue) Push(e interface{}) error {   // 省略 receiver 参数名
    panic("not implemented")
}

// func (Queue) Push(e int) error {         // Error: method redeclared: Queue.Push
//     panic("not implemented")
// }

func (self *Queue) length() int {           // receiver 参数名可以是 self，this 或其他
    return len(self.elements)
}
```

- 方法不过是一种特殊的函数，只需将其还原，就知道 receiver T 和 `*T` 的区别。

```go
type Data struct{
    x int
}

func (self Data) ValueTest() {                // func ValueTest(self Data);
    fmt.Printf("Value: %p\n", &self)
}

func (self *Data) PointerTest() {             // func PointerTest(self *Data);
    fmt.Printf("Pointer: %p\n", self)
}

func main() {
    d := Data{}
    p := &d
    fmt.Printf("Data: %p\n", p)
    d.ValueTest()                               // ValueTest(d)
    d.PointerTest()                             // PointerTest(&d)
    p.ValueTest()                               // ValueTest(*p)
    p.PointerTest()                             // PointerTest(p)
}

// 输出：
Data   : 0x2101ef018
Value  : 0x2101ef028
Pointer: 0x2101ef018
Value  : 0x2101ef030
Pointer: 0x2101ef018
```

- 通过结构体匿名字段，以及编译器从外向内查找的规则，可实现类似重载特性。

```go
type User struct {
    id   int
    name string
}

type Manager struct {
    User            // Manager得到了匿名字段User的所有字段及其方法
    title string
}

func (self *User) ToString() string {           // receiver = &(Manager.User)
    return fmt.Sprintf("User: %p, %v", self, self)
}

func (self *Manager) ToString() string {
    return fmt.Sprintf("Manager: %p, %v", self, self)
}

func main() {
    m := Manager{User{1, "Tom"}, "Administrator"}
    fmt.Println(m.ToString())   // Manager的ToString方法覆盖了User的ToString，类似重载
    fmt.Println(m.User.ToString())
}

// 若Manager无ToString方法，m.ToString()自动向内查找到User的ToString方法，

// 输出：
Manager: 0x2102271b0, &{{1 Tom} Administrator}
User   : 0x2102271b0, &{1 Tom}
```

- 方法集

每个类型都有与之关联的方法集，这会影响到接口实现规则。

```
1. 类型 T 方法集合包含全部 receiver T 方法，不包含全部 receiver *T 方法。
2. 类型 *T 方法集包含全部 receiver T + *T 方法。
3. 如类型 S 包含匿名字段 T，则 S 方法集包含 T 方法。
4. 如类型 S 包含匿名字段 *T，则 S 方法集包含 T + *T 方法。
5. 不管嵌入 T 或者 *T，*S 方法集总是包含 T + *T 方法。
```
用实例 value 和 pointer 调用方法(含匿名字段)不受方法集约束，编译器总是查找全部方法，并自动转换 receiver 实参。

- 表达式

根据调用者不同，方法分为两种表现形式：

```
instance.method(args...)    ---->    <type>.func(instance, args...)
前者称为 method value，后者称为 method expression。
两者都可像普通函数那样赋值和传参，区别在于：
前者绑定实例，后者需要显式传参。
```

```go
type User struct {
    id   int
    name string
}

func (self *User) Test() {
    fmt.Printf("%p, %v\n", self, self)
}

func main() {
    u := User{1, "Tom"}
    u.Test()

    mValue := u.Test
    mValue()                        // 隐式传递 receiver

    mExpression := (*User).Test
    mExpression(&u)                 // 显式传递 receiver
}

// 输出：
0x210230000, &{1 Tom}
0x210230000, &{1 Tom}
0x210230000, &{1 Tom}
```

- method value 会复制 receiver

```go
type User struct {
    id   int
    name string
}

func (self User) Test() {
    fmt.Println(self)
}

func main() {
    u := User{1, "Tom"}
    mValue := u.Test    // 立即复制 receiver，因为不是指针类型，不受后续修改影响
    u.id, u.name = 2, "Jack"
    u.Test()
    mValue()
}

// 输出：
{2 Jack}
{1 Tom}
```

- 可依据方法集转换 method expression，注意 receiver 类型的差异。

```go
type User struct {
    id   int
    name string
}

func (self *User) TestPointer() {
    fmt.Printf("TestPointer: %p, %v\n", self, self)
}

func (self User) TestValue() {
    fmt.Printf("TestValue: %p, %v\n", &self, self)
}

func main() {
    u := User{1, "Tom"}
    fmt.Printf("User: %p, %v\n", &u, u)

    mv := User.TestValue
    mv(u)

    mp := (*User).TestPointer
    mp(&u)

    mp2 := (*User).TestValue        // *User 方法集包含 TestValue，签名变为
    mp2(&u)                         // func TestValue(self *User)
                                    // 实际依然是 receiver value copy
}

// 输出：
User       : 0x210231000, {1 Tom}
TestValue  : 0x210231060, {1 Tom}
TestPointer: 0x210231000, &{1 Tom}
TestValue  : 0x2102310c0, {1 Tom}
```

- 将方法“还原”为函数，更容易理解。

```go
type Data struct{}

func (Data) TestValue()    {}
func (*Data) TestPointer() {}

func main() {
    var p *Data = nil
    p.TestPointer()
    (*Data)(nil).TestPointer()  // method value
    (*Data).TestPointer(nil)    // method expression

    // p.TestValue()            // invalid memory address or nil pointer dereference
    // (Data)(nil).TestValue()  // cannot convert nil to type Data
    // Data.TestValue(nil)      // cannot use nil as type Data in function argument
}
```

### 值接收者与指针接收者

- 何时使用值类型

1. 如果接受者是一个 map，func 或者 chan，使用值类型，因为它们本身就是引用类型。
2. 如果接受者是一个 slice，并且方法不执行 reslice 操作，也不重新分配内存给 slice，使用值类型。
3. 如果接受者是一个小的数组或者原生的值类型结构体类型(比如 time.Time 类型)，而且没有可修改的字段和指针，又或者接受者是一个简单地基本类型像是 int 和 string，使用值类型就好了。
4. 一个值类型的接受者可以减少一定数量的垃圾生成，如果一个值被传入一个值类型接受者的方法，一个栈上的拷贝会替代在堆上分配内存(但不是保证一定成功)，所以在没搞明白代码想干什么之前，别因为这个原因而选择值类型接受者。

- 何时使用指针类型

1. 如果方法需要修改接受者，接受者必须是指针类型。
2. 如果接受者是一个包含了 sync.Mutex 或者类似同步字段的结构体，接受者必须是指针，这样可以避免拷贝。
3. 如果接受者是一个大的结构体或者数组，那么指针类型接受者更有效率。
4. 如果接受者是一个结构体，数组或 slice，它们中任意一个元素是指针类型而且可能被修改，建议使用指针类型接受者，增加程序的可读性。
5. 其他情况，使用指针接受者。
