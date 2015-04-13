Martini源码解析
===

[什么是依赖注入 Dependency Injection，DI](http://nerdyang.com/post/yi-lai-zhu-ru)

### 使用场景

Martini 中，每一个 web 请求进来之后，都有一个特定的 handler 来处理它，如下：

```go
func Price(r *render.Render, db *sql.DB, w http.ResponseWriter, req *http.Request) {
}
```

把它传到 router 的时候如下：

```go
router.Get("/price", web.Price)
```

那么 r， db， w, req 这些参数从哪里来呢，最直接的，当然从调用它的对象那里来。那么显然，router 会提供这些参数的实例。而 runter 又是由 martini 实例化的，参数最终掌握在 martini 手中。

```go
router := martini.NewRouter()
```

分析可知，martini 必须要管理这些参数的实例化工作。当然，它不会自己管理，这时候依赖注入器老兄就要闪亮登场了。

### 依赖注入器

依赖注入器想要完成上述工作，即为 http handler 提供参数，它必须知道以下两点:

1. handler 有哪些参数
2. 如何给出这些参数的实例。

第 1 2 条都得 go 语言的反射机制提供帮助，而第 2 条则是依赖注入器的本质工作，反射只能告诉依赖注入器函数有哪些参数，分别是什么类型，但如何实例化，则是由依赖注入器负责。

所以，一个依赖注入器一般有以下功能：

1. 存储和管理（类型-值）映射
2. 将依赖注入到函数的参数中，即调用函数。


### inject代码解析

Martini 的作者写了一个非常优雅的依赖注入器，源代码见[这里](https://github.com/codegangsta/inject)

首先是它的接口定义，我们知道，接口是对象的行为。

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
看起来很复杂，其实是接口的组合，这也是 go 语言的灵活和优雅之处。Injector 接口主要包含三个：

1. TypeMapper，管理类型和值的映射
2. Applicator，函数调用
3. Invoker，这个接口则是把 injector 管理的映射注入到一个 struct 中，额外的功能。

怎么样，注入器所需要的功能一应俱全了吧，那么（类型-值）映射存储在哪里呢，请看下面struct的定义：

```go
type injector struct {
    values map[reflect.Type]reflect.Value
    parent Injector
}
```
没错，就存储在 values 中。parent 则是在 values 中找不到实例化某个 type 的映射，上溯到父注入器那里找，都找不到再返回 error。

另外，我 fork 一个 inject 的源码，加了些中文注释，详情见[这里](https://github.com/rnoldo/inject)
