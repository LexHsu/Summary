包
===

- 用 package 保留字定义一个包。文件名不需要与包名一致。包名的约定是使用小写字符。

- 一个包可由多个文件组成，使用相同的 package <name> 声明。

- 一个包如果有多个不同文件，相互之间可以直接引用变量和函数，不论是否导出，因此不能有相同的全局变量和函数，init() 函数除外。

- 包名与所在目录名可以不同，但建议保持相同，防止歧义。因为引入包的时候，import 时采用目录名，在代码中使用时，使用包名。

- 在 `$GOPATH` 或 `$GOROOT` 下建立一个目录，该目录名就是包名。每个子目录中只能存在一个 package，否则编译时会报错。

- 以大写字母起始的方法或函数是可导出的，可以在包的外部调用，反之是私有的。

示例，自定义包 even.go。

```go
package even

func Even(i int) bool {
    return i % 2 == 0
}

func odd(i int) bool {
    return i % 2 == 1
}
```

在 myeven.go 中 引用 even 包。

```go
package main

import "even"
import "fmt"

func main() {
    i := 5
    fmt.Printf("Is%deven?%v\n",i,even.Even(i))
}
```

### 测试包

- 编写测试需要包含 testing 包和程序 go test。所有测试文件都声明 test 包。go test 只用来执行测试函数。

- testing 包中的文件常命名为 `*_test.go`。每个测试函数名均以 Test 开头 `func TestXxx(t *testing.T)`。

- 执行 go test 成功则直接返回，当测试失败可以用下面的函数标记。

```go
// Fail 标记测试函数失败但仍继续执行
func (t *T) Fail()

// FailNow标记测试函数失败，当前文件的其他所有测试也都被跳过，执行下一个测试文件。
func (t *T) FailNow()

// Log 用默认格式对其参数进行格式化，与 Print() 类似，并记录到错误日志
func (t *T) Log(args ...interface{})

// Fatal 等价于 Log() 后跟随 FailNow()。
func (t *T) Fatal(args ...interface{})
```

- 示例，even_test.go。

```go
package even

import "testing"

func TestEven(t *testing.T) {
    if !Even(2) {
        t.Log("2 should be even!")
        t.Fail()
    }
}
```

### init 函数

一个 package 可有多个 init 函数，每个 init 都会被调用，顺序如下：

1. 对同一个 go 文件的 init() 调用顺序为从上到下。
2. 对同一个 package 中不同文件是按文件名字符串比较“从小到大”顺序调用各文件中的 init() 函数。
3. 对不同的 package，如果不相互依赖，按照 main 包中“先 import 的后调用”原则调用其包中的init()
4. 如果 package 存在依赖，则先调用最早被依赖的 package 中的 init() 函数。

第一行使用了 package even，测试使用与被测试的包使用相同的名字空间。这不仅方便，也允许了测试未导出的函数和结构。
然后导入 testing 包，测试函数检查了 Even 函数是否工作正常。
