

读写锁是针对于读写操作的互斥锁。遵循两大原则：

1. 可以随便读。多个 goroutin 同时读。
2. 写的时候，啥都不能干。不能读，也不能写。


RWMutex 提供四个方法：

```
func (*RWMutex) Lock     // 写锁定
func (*RWMutex) Unlock   // 写解锁
func (*RWMutex) RLock    // 读锁定
func (*RWMutex) RUnlock  // 读解锁
```
 

### 示例

1、可以随便读：

```go
package main

import (
"sync"
"time"
)

var m *sync.RWMutex

func main() {
    m = new(sync.RWMutex)
    //可以多个同时读
    go read(1)
    go read(2)
    time.Sleep(2 * time.Second)
}

 

func read(i int) {
    println(i, "read start")
    m.RLock()
    println(i, "reading")
    time.Sleep(1 * time.Second)
    m.RUnlock()
    println(i, "read end")
}
```
 

运行结果：
```
1 read start
1 reading
2 read start
2 reading
1 read end
2 read end
```
可以看到 1 读还没结束的时候，2 已经在读了。


2、写的时候啥也不能干：
```go
package main

import (
"sync"
"time"
)

var m *sync.RWMutex

func main() {
    m = new(sync.RWMutex)
    // 写的时候啥都不能干
    go write(1)
    go read(2)
    go write(3)
    time.Sleep(4 * time.Second)
}

 

func read(i int) {
    println(i, "read start")
    m.RLock()
    println(i, "reading")
    time.Sleep(1 * time.Second)
    m.RUnlock()
    println(i, "read end")
}

func write(i int) {
    println(i, "write start")
    m.Lock()
    println(i, "writing")
    time.Sleep(1 * time.Second)
    m.Unlock()
    println(i, "write end")
}

// console print:
1 write start
1 writing
2 read start
3 write start
1 write end
2 reading
2 read end
3 writing
3 write end
```

可见：

- 1 write end 结束之后，2 才能 reading
- 2 read end 结束之后，3 才能 writing
