inject 源码分析
===

依赖注入（Dependency Injection）和控制反转（Inversion of Control）为同一个概念。
在传统的程序设计过程中，调用者是自己来决定使用哪些被调用者实现的。
但是在依赖注入模式中，创建被调用者的工作不再由调用者来完成，因此称为控制反转；
创建被调用者实例的工作通常由注入器来完成，然后注入调用者，因此也称为依赖注入。

inject 是依赖注入的 golang 实现，其能在运行时注入参数，调用方法，是 Martini 框架的基础核心。

依赖注入有如下性质：

1. 由注入器注入属性。
2. 由注入器创建被调用者实例。

在 inject 中，被调用者为 func，因此注入属性就是对 func 注入实参，inject 也可以注入 struct，这样的话注入的属性就是 struct 中的已添加 tag 为 inject 的导出字段。

如下是普通的函数调用：

```go
package main

import (
    "fmt"
)

func Say(name, gender string, age int) {
    fmt.Printf("My name is %s, gender is %s, age is %d!\n", name, gender, age)
}

func main() {
    Say("Jack", "Man", 20)
}
```

上面的例子中，定义了方法 Say 并在 main 方法中手动调用。但有的场景下，在 web 开发中，注册路由，服务器接受请求，然后根据 request path 调用相应的 handler。这个 handler 不是由手动来调用的，而是由服务器端根据路由匹配来查找对应的 handler 并自动调用。inject 便由此而来，尝试用 inject 改写上面的代码：

```go
package main

import (
    "fmt"
    "github.com/codegangsta/inject"
)

type SpecialString interface{}

func Say(name string, gender SpecialString, age int) {
    fmt.Printf("My name is %s, gender is %s, age is %d!\n", name, gender, age)
}

func main() {
    inj := inject.New()
    inj.Map("Jack")
    inj.MapTo("Man", (*SpecialString)(nil))
    inj.Map(20)
    inj.Invoke(Say)
}

$ cd $GOPATH/src/injector_test
$ go build
$ ./injector_test
My name is Jack, gender is Man, age is 20!
```

看不懂？没关系，因为我们对于 inject 还没有足够的知识储备，一切从分析 inject 源码开始。
inject.go 短小精悍。定义了 4 个接口，包括一个父接口和三个子接口，这样的好处稍后介绍。


```go
type Injector interface {
    Applicator
    Invoker
    TypeMapper
    SetParent(Injector)
}

type Applicator interface {
    Apply(interface{}) error
}

type Invoker interface {
    Invoke(interface{}) ([]reflect.Value, error)
}

type TypeMapper interface {
    Map(interface{}) TypeMapper
    MapTo(interface{}, interface{}) TypeMapper
    Get(reflect.Type) reflect.Value
}
```

接口 Injector 是接口 Applicator、接口 Invoker、接口 TypeMapper 的父接口，所以实现了 Injector 接口的类型，
也必然实现了 Applicator 接口、Invoker 接口和 TypeMapper 接口。

Applicator 接口只规定了 Apply 成员，它用于注入 struct。

Invoker 接口只规定了 Invoke 成员，它用于执行被调用者。

TypeMapper 接口规定了三个成员，Map 和 MapTo 都用于注入参数，但它们有不同的用法。Get 用于调用时获取被注入的参数。

另外 Injector 还规定了 SetParent 行为，它用于设置父 Injector，其实它相当于查找继承。也即通过 Get 方法在获取被注入参数时会一直追溯到 parent，这是个递归过程，直到查找到参数或为 nil 终止。

```go
type injector struct {
    values map[reflect.Type]reflect.Value
    parent Injector
}

func InterfaceOf(value interface{}) reflect.Type {
    t := reflect.TypeOf(value)

    for t.Kind() == reflect.Ptr {
        t = t.Elem()
    }

    if t.Kind() != reflect.Interface {
        panic("Called inject.InterfaceOf with a value that is not a pointer to an interface. (*MyInterface)(nil)")
    }

    return t
}

func New() Injector {
    return &injector{
        values: make(map[reflect.Type]reflect.Value),
    }
}
```
injector 是 inject 包中唯一定义的 struct，所有的操作都是基于 injector struct 来进行的。
它有两个成员 values 和 parent。values 用于保存注入的参数，它是一个用 reflect.Type 当键、reflect.Value 为值的 map，这个很重要，理解这点将有助于理解 Map 和 MapTo。
New 方法用于初始化 injector struct，并返回一个指向 injector struct 的指针。但是请注意这个返回值被 Injector 接口包装了。

InterfaceOf 方法虽然只有几句实现代码，但它是 Injector 的核心。InterfaceOf 方法的参数必须是一个接口类型的指针，如果不是则引发 panic。
InterfaceOf 方法的返回类型是 reflect.Type，您应该还记得 injector 的成员 values 就是一个 reflect.Type 类型当键的 map。
这个方法的作用其实只是获取参数的类型，而不关心它的值。

```go
package main

import (
    "fmt"
    "github.com/codegangsta/inject"
)

type SpecialString interface{}

func main() {
    fmt.Println(inject.InterfaceOf((*interface{})(nil)))
    fmt.Println(inject.InterfaceOf((*SpecialString)(nil)))
}

$ cd $GOPATH/src/injector_test
$ go build
$ ./injector_test
interface {}
main.SpecialString
```

上面的输出一点也不奇怪。InterfaceOf方法就是用来得到参数类型，而不关心它具体存储的是什么值。值得一提的是，我们定义了一个SpecialString接口。我们在之前的代码也有定义SpecialString接口，用在Say方法的参数声明中，之后您就会知道为什么要这么做。当然您不一定非得命名为SpecialString。

```go
func (i *injector) Map(val interface{}) TypeMapper {
    i.values[reflect.TypeOf(val)] = reflect.ValueOf(val)
    return i
}

func (i *injector) MapTo(val interface{}, ifacePtr interface{}) TypeMapper {
    i.values[InterfaceOf(ifacePtr)] = reflect.ValueOf(val)
    return i
}

func (i *injector) Get(t reflect.Type) reflect.Value {
    val := i.values[t]
    if !val.IsValid() && i.parent != nil {
        val = i.parent.Get(t)
    }
    return val
}

func (i *injector) SetParent(parent Injector) {
    i.parent = parent
}
```
Map和MapTo方法都用于注入参数，保存于injector的成员values中。这两个方法的功能完全相同，唯一的区别就是Map方法用参数值本身的类型当键，而MapTo方法有一个额外的参数可以指定特定的类型当键。
但是MapTo方法的第二个参数ifacePtr必须是接口指针类型，因为最终ifacePtr会作为InterfaceOf方法的参数。

为什么需要有MapTo方法？因为注入的参数是存储在一个以类型为键的map中，可想而知，当一个函数中有一个以上的参数的类型是一样时，后执行Map进行注入的参数将会覆盖前一个通过Map注入的参数。

SetParent方法用于给某个Injector指定父Injector。Get方法通过reflect.Type从injector的values成员中取出对应的值，它可能会检查是否设置了parent，直到找到或返回无效的值，最后Get方法的返回值会经过IsValid方法的校验。举个例子来加深理解：

```go
package main

import (
    "fmt"
    "github.com/codegangsta/inject"
    "reflect"
)

type SpecialString interface{}

func main() {
    inj := inject.New()
    inj.Map("Jack")
    inj.MapTo("Man", (*SpecialString)(nil))
    inj.Map(20)
    fmt.Println("string is valid?", inj.Get(reflect.TypeOf("Jack")).IsValid())
    fmt.Println("SpecialString is valid?", inj.Get(inject.InterfaceOf((*SpecialString)(nil))).IsValid())
    fmt.Println("int is valid?", inj.Get(reflect.TypeOf(18)).IsValid())
    fmt.Println("[]byte is valid?", inj.Get(reflect.TypeOf([]byte("Golang"))).IsValid())
    inj2 := inject.New()
    inj2.Map([]byte("test"))
    inj.SetParent(inj2)
    fmt.Println("[]byte is valid?", inj.Get(reflect.TypeOf([]byte("Golang"))).IsValid())
}

$ cd $GOPATH/src/injector_test
$ go build
$ ./injector_test
string is valid? true
SpecialString is valid? true
int is valid? true
[]byte is valid? false
[]byte is valid? true
```
通过以上例子应该知道SetParent是什么样的行为。是不是很像面向对象中的查找链？

```go
func (inj *injector) Invoke(f interface{}) ([]reflect.Value, error) {
    t := reflect.TypeOf(f)

    var in = make([]reflect.Value, t.NumIn()) //Panic if t is not kind of Func
    for i := 0; i < t.NumIn(); i++ {
        argType := t.In(i)
        val := inj.Get(argType)
        if !val.IsValid() {
            return nil, fmt.Errorf("Value not found for type %v", argType)
        }

        in[i] = val
    }

    return reflect.ValueOf(f).Call(in), nil
}
```
Invoke方法用于动态执行函数，当然执行前可以通过Map或MapTo来注入参数，因为通过Invoke执行的函数会取出已注入的参数，然后通过reflect包中的Call方法来调用。Invoke接收的参数f是一个接口类型，但是f的底层类型必须为func，否则会panic。

```go
package main

import (
    "fmt"
    "github.com/codegangsta/inject"
)

type SpecialString interface{}

func Say(name string, gender SpecialString, age int) {
    fmt.Printf("My name is %s, gender is %s, age is %d!\n", name, gender, age)
}

func main() {
    inj := inject.New()
    inj.Map("Jack")
    inj.MapTo("Man", (*SpecialString)(nil))
    inj2 := inject.New()
    inj2.Map(20)
    inj.SetParent(inj2)
    inj.Invoke(Say)
}
```
上面的例子如果没有定义SpecialString接口作为gender参数的类型，而把name和gender都定义为string类型，那么gender会覆盖name的值。如果您还没有明白，建议您把这篇文章从头到尾再看几遍。

```go
func (inj *injector) Apply(val interface{}) error {
    v := reflect.ValueOf(val)

    for v.Kind() == reflect.Ptr {
        v = v.Elem()
    }

    if v.Kind() != reflect.Struct {
        return nil
    }

    t := v.Type()

    for i := 0; i < v.NumField(); i++ {
        f := v.Field(i)
        structField := t.Field(i)
        if f.CanSet() && structField.Tag == "inject" {
            ft := f.Type()
            v := inj.Get(ft)
            if !v.IsValid() {
                return fmt.Errorf("Value not found for type %v", ft)
            }

            f.Set(v)
        }

    }

    return nil
}
```
Apply方法是用于对struct的字段进行注入，参数为指向底层类型为结构体的指针。可注入的前提是：字段必须是导出的(也即字段名以大写字母开头)，并且此字段的tag设置为`inject`。以例子来说明：

```go
package main

import (
    "fmt"
    "github.com/codegangsta/inject"
)

type SpecialString interface{}
type TestStruct struct {
    Name   string `inject`
    Nick   []byte
    Gender SpecialString `inject`
    uid    int           `inject`
    Age    int           `inject`
}

func main() {
    s := TestStruct{}
    inj := inject.New()
    inj.Map("Jack")
    inj.MapTo("Man", (*SpecialString)(nil))
    inj2 := inject.New()
    inj2.Map(20)
    inj.SetParent(inj2)
    inj.Apply(&s)
    fmt.Println("s.Name =", s.Name)
    fmt.Println("s.Gender =", s.Gender)
    fmt.Println("s.Age =", s.Age)
}

$ cd $GOPATH/src/injector_test
$ go build
$ ./injector_test
s.Name = Jack
s.Gender = Man
s.Age = 20
```
