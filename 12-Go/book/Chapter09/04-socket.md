TCP Socket
===

### 一、模型

网络编程架构模型演化过程如下：

```
1. 每进程一个连接
     |
2. 每线程一个连接
     |
3. Non-Block + I/O 多路复用(linux epoll/windows iocp/freebsd darwin kqueue/solaris Event Port)
```

目前主流 Web Server 一般采用第三种模型，为降低复杂度，在这基础上抽象出框架如 libevent、libev、libuv。不过 Go 设计者认为依旧复杂，其巧妙地将复杂性隐藏在 Runtime 中，开发者无需关注 socket 是否阻塞，也无需注册回调，只需在每个 goroutine 中以 block I/O 的方式处理即可，大大降低了负责度。

一个典型的 Go server 端程序：

```go
//go-tcpsock/server.go
func handleConn(c net.Conn) {
    defer c.Close()
    for {
        // read from the connection
        // ... ...
        // write to the connection
        //... ...
    }
}

func main() {
    l, err := net.Listen("tcp", ":8888")
    if err != nil {
        fmt.Println("listen error:", err)
        return
    }

    for {
        c, err := l.Accept()
        if err != nil {
            fmt.Println("accept error:", err)
            break
        }
        // start a new goroutine to handle
        // the new connection.
        go handleConn(c)
    }
}
```

goroutine 中的 block socket 实现原理：

Go runtime 中的 netpoller 通过 Non-block socket + I/O 多路复用机制模拟出来的，
真实的 underlying socket 是 non-block 的，Go runtime 拦截了底层 socket 系统调用的错误码，并通过 netpoller 和 goroutine 调度让 goroutine 阻塞在用户层得到的 Socket fd 上。

举例：当用户层针对某个 socket fd 发起 read 操作时，如果该 socket fd 中尚无数据，那么 runtime 会将该 socket fd 加入到 netpoller 中监听，同时对应的 goroutine 被挂起，直到 runtime 收到 socket fd 数据 ready 的通知，runtime 才会重新唤醒等待在该 socket fd 上准备 read 的那个 Goroutine。而这个过程从 Goroutine 的视角来看，就像是 read 操作一直 block 在该 socket fd 上。

### 二、TCP 连接的建立

众所周知，TCP Socket 的连接的建立需要经历客户端和服务端的三次握手。连接建立过程中，服务端是一个标准的 Listen + Accept 的结构，而在客户端 Go 语言使用 net.Dial 或 DialTimeout 进行连接建立：

```go
// [1]. 阻塞 Dial：
conn, err := net.Dial("tcp", "google.com:80")
if err != nil {
    //handle error
}
// read or write on conn


// [2]. 带超时机制的Dial：
conn, err := net.DialTimeout("tcp", ":8080", 2 * time.Second)
if err != nil {
    //handle error
}
// read or write on conn
```

对于客户端而言，连接的建立会遇到如下几种情形：

1. 网络不可达或对方服务未启动
2. 对方服务的 listen backlog 满
3. 网络延迟较大，Dial 阻塞并超时

##### 1、网络不可达或对方服务未启动

如果传给 Dial 的 Addr 是可以立即判断出网络不可达，或者 Addr 中端口对应的服务没有启动，端口未被监听，Dial 会几乎立即返回错误，如：

```go
//go-tcpsock/conn_establish/client1.go
... ...
func main() {
    log.Println("begin dial...")
    conn, err := net.Dial("tcp", ":8888")
    if err != nil {
        log.Println("dial error:", err)
        return
    }
    defer conn.Close()
    log.Println("dial ok")
}
```

如果本机 8888 端口没有服务程序监听，那么执行上面程序，Dial会很快返回错误：

```
$go run client1.go
2015/11/16 14:37:41 begin dial...
2015/11/16 14:37:41 dial error: dial tcp :8888: getsockopt: connection refused
```

##### 2、对方服务的 listen backlog 满

对方服务器很忙，瞬间有大量 client 请求，server 端的 listen backlog 队列满，server accept不及时，将导致 client 端 Dial 阻塞。如下例：

```go
服务端代码：
//go-tcpsock/conn_establish/server2.go
... ...
func main() {
    l, err := net.Listen("tcp", ":8888")
    if err != nil {
        log.Println("error listen:", err)
        return
    }
    defer l.Close()
    log.Println("listen ok")

    var i int
    for {
        time.Sleep(time.Second * 10)
        if _, err := l.Accept(); err != nil {
            log.Println("accept error:", err)
            break
        }
        i++
        log.Printf("%d: accept a new connection\n", i)
    }
}


客户端代码：
//go-tcpsock/conn_establish/client2.go
... ...
func establishConn(i int) net.Conn {
    conn, err := net.Dial("tcp", ":8888")
    if err != nil {
        log.Printf("%d: dial error: %s", i, err)
        return nil
    }
    log.Println(i, ":connect to server ok")
    return conn
}

func main() {
    var sl []net.Conn
    for i := 1; i < 1000; i++ {
        conn := establishConn(i)
        if conn != nil {
            sl = append(sl, conn)
        }
    }

    time.Sleep(time.Second * 10000)
}
```

从程序可以看出，服务端在listen成功后，每隔10s钟accept一次。客户端则是串行的尝试建立连接。这两个程序在Darwin下的执行结果：
```
$go run server2.go
2015/11/16 21:55:41 listen ok
2015/11/16 21:55:51 1: accept a new connection
2015/11/16 21:56:01 2: accept a new connection
... ...

$go run client2.go
2015/11/16 21:55:44 1 :connect to server ok
2015/11/16 21:55:44 2 :connect to server ok
2015/11/16 21:55:44 3 :connect to server ok
... ...

2015/11/16 21:55:44 126 :connect to server ok
2015/11/16 21:55:44 127 :connect to server ok
2015/11/16 21:55:44 128 :connect to server ok

2015/11/16 21:55:52 129 :connect to server ok
2015/11/16 21:56:03 130 :connect to server ok
2015/11/16 21:56:14 131 :connect to server ok
... ...
```

可以看出 Client 初始时成功地一次性建立了 128 个连接，然后后续每阻塞近 10s 才能成功建立一条连接。也就是说在 server 端 backlog 满时(未及时 accept)，客户端将阻塞在 Dial 上，直到 server 端进行一次 accept。至于为什么是 128，这与darwin 下的默认设置有关：

```
$sysctl -a|grep kern.ipc.somaxconn
kern.ipc.somaxconn: 128
```

如果在 ubuntu 14.04 上运行上述程序，client 初始可以成功建立 499 条连接。
如果 server 一直不 accept，client 端会一直阻塞么？我们去掉 accept 后的结果是：在Darwin下，client 端会阻塞大 约 1 分多钟才会返回 timeout：

```
2015/11/16 22:03:31 128 :connect to server ok
2015/11/16 22:04:48 129: dial error: dial tcp :8888: getsockopt: operation timed out
```

而如果 server 运行在 ubuntu 14.04 上，client 似乎一直阻塞，等了 10 多分钟依旧没有返回。 阻塞与否看来与 server 端的网络实现和设置有关。

##### 3、网络延迟较大，Dial 阻塞并超时

如果网络延迟较大，TCP 握手过程将更加艰难坎坷（各种丢包），时间消耗的自然也会更长。Dial 这时会阻塞，如果长时间依旧无法建立连接，则 Dial 也会返回 getsockopt: operation timed out 错误。

对于严格控制连接时间的程序，使用 DialTimeout，下例将 Dial 最长阻塞时间限制在 2s 内，超时将返回 timeout error：

```go
//go-tcpsock/conn_establish/client3.go
... ...
func main() {
    log.Println("begin dial...")
    conn, err := net.DialTimeout("tcp", "104.236.176.96:80", 2 * time.Second)
    if err != nil {
        log.Println("dial error:", err)
        return
    }
    defer conn.Close()
    log.Println("dial ok")
}
```

执行结果如下（需要模拟一个延迟较大的网络环境）：

```
$go run client3.go
2015/11/17 09:28:34 begin dial...
2015/11/17 09:28:36 dial error: dial tcp 104.236.176.96:80: i/o timeout
```

### 三、Socket读写

Dial 连接成功后，方法返回一个 net.Conn 接口类型变量值，这个接口变量的动态类型为一个 `*TCPConn`：

```go
//$GOROOT/src/net/tcpsock_posix.go
type TCPConn struct {
    conn
}
```

TCPConn 内嵌了一个 unexported 类型：conn，因此TCPConn 继承 了 conn 的 Read 和 Write 方法，后续通过 Dial 返回值调用的 Write 和 Read 方法均是 net.conn 的方法：

```go
//$GOROOT/src/net/net.go
type conn struct {
    fd *netFD
}

func (c *conn) ok() bool { return c != nil && c.fd != nil }

// Implementation of the Conn interface.

// Read implements the Conn Read method.
func (c *conn) Read(b []byte) (int, error) {
    if !c.ok() {
        return 0, syscall.EINVAL
    }
    n, err := c.fd.Read(b)
    if err != nil && err != io.EOF {
        err = &OpError{Op: "read", Net: c.fd.net, Source: c.fd.laddr, Addr: c.fd.raddr, Err: err}
    }
    return n, err
}

// Write implements the Conn Write method.
func (c *conn) Write(b []byte) (int, error) {
    if !c.ok() {
        return 0, syscall.EINVAL
    }
    n, err := c.fd.Write(b)
    if err != nil {
        err = &OpError{Op: "write", Net: c.fd.net, Source: c.fd.laddr, Addr: c.fd.raddr, Err: err}
    }
    return n, err
}
```

#### conn.Read 使用场景

##### 1、Socket 中无数据

连接建立后，如果对方未发送数据到 Socket，执行该 Read 操作的 Goroutine 会被挂起。
runtime 会监视该 socket，直到其有数据才会重新
调度该 Socket 对应的 Goroutine 完成 Read。示例代码：go-tcpsock/read_write下 的 client1.go 和 server1.go。

##### 2、Socket中有部分数据

如果 Socket 中有部分数据，且长度小于一次 Read 操作所期望读出的数据长度 N，那么 Read 将会成功读出这部分数据并返回，而不是等待读取数据长度 N 之后再返回。

Client端：
```go
//go-tcpsock/read_write/client2.go
... ...
func main() {
    if len(os.Args) <= 1 {
        fmt.Println("usage: go run client2.go YOUR_CONTENT")
        return
    }
    log.Println("begin dial...")
    conn, err := net.Dial("tcp", ":8888")
    if err != nil {
        log.Println("dial error:", err)
        return
    }
    defer conn.Close()
    log.Println("dial ok")

    time.Sleep(time.Second * 2)
    data := os.Args[1]
    conn.Write([]byte(data))

    time.Sleep(time.Second * 10000)
}
```

Server端：

```go
//go-tcpsock/read_write/server2.go
... ...
func handleConn(c net.Conn) {
    defer c.Close()
    for {
        // read from the connection
        var buf = make([]byte, 10)
        log.Println("start to read from conn")
        n, err := c.Read(buf)
        if err != nil {
            log.Println("conn read error:", err)
            return
        }
        log.Printf("read %d bytes, content is %s\n", n, string(buf[:n]))
    }
}
... ...
```

通过 client2.go 发送 hi 到 Server 端，运行结果:
```
$go run client2.go hi
2015/11/17 13:30:53 begin dial...
2015/11/17 13:30:53 dial ok

$go run server2.go
2015/11/17 13:33:45 accept a new connection
2015/11/17 13:33:45 start to read from conn
2015/11/17 13:33:47 read 2 bytes, content is hi
...
```

Client向 Socket 中写入两个字节数据：hi ，Server 端创建一个len = 10 的 slice，等待 Read 将读取的数据放入 slice；Server 随后读取到那两个字符。Read 成功返回，n = 2 ，err = nil。

##### 3、Socket 中有足够数据

如果 Socket 中有数据，且长度大于等于一次 Read 操作所期望读出的数据长度，那么 Read 将会成功读出这部分数据并返回。这个情景是最符合我们对Read的期待的：Read 将用 Socket 中的数据将我们传入的 slice 填满后返回：n = 10, err = nil。

通过 client2.go 向 Server2 发送如下内容：abcdefghij12345，执行结果如下：

```
$go run client2.go abcdefghij12345
2015/11/17 13:38:00 begin dial...
2015/11/17 13:38:00 dial ok

$go run server2.go
2015/11/17 13:38:00 accept a new connection
2015/11/17 13:38:00 start to read from conn
2015/11/17 13:38:02 read 10 bytes, content is abcdefghij
2015/11/17 13:38:02 start to read from conn
2015/11/17 13:38:02 read 5 bytes, content is 12345
```

client 端发送的内容长度为 15 个字节，Server 端 Read buffer 的长度为 10，因此 Server Read 第一次返回时只会读取 10 个字节；Socket 中还剩余 5 个字节数据，Server 再次 Read 时会把剩余数据读出（如：情形 2）。

##### 4、Socket关闭

如果 Client 端主动关闭了 Socket，那么 Server 的 Read 将会读到什么呢？这里分为“有数据关闭”和“无数据关闭”。

“有数据关闭”是指在 Client 关闭时，Socket 中还有 Server 端未读取的数据，我们在 go-tcpsock/read_write/client3.go 和 server3.go 中模拟这种情况：

```
$go run client3.go hello
2015/11/17 13:50:57 begin dial...
2015/11/17 13:50:57 dial ok

$go run server3.go
2015/11/17 13:50:57 accept a new connection
2015/11/17 13:51:07 start to read from conn
2015/11/17 13:51:07 read 5 bytes, content is hello
2015/11/17 13:51:17 start to read from conn
2015/11/17 13:51:17 conn read error: EOF
```
可见，当 Client 端关闭 Socket 退出后，server3 依旧没有开始 Read，10s 后第一次 Read 成功读出了 5 个字节的数据，当第二次 Read 时，由于 Client 端 Socket 关闭，Read 返回 EOF error。

通过上面这个例子，可知“无数据关闭”情形下的结果，那就是 Read 直接返回 EOF error。

##### 5、读取操作超时

有些场合对Read的阻塞时间有严格限制，在这种情况下，Read的行为到底是什么样的呢？在返回超时错误时，是否也同时Read了一部分数据了呢？这个实验比较难于模拟，下面的测试结果也未必能反映出所有可能结果。我们编写了client4.go和server4.go来模拟这一情形。
```
//go-tcpsock/read_write/client4.go
... ...
func main() {
    log.Println("begin dial...")
    conn, err := net.Dial("tcp", ":8888")
    if err != nil {
        log.Println("dial error:", err)
        return
    }
    defer conn.Close()
    log.Println("dial ok")

    data := make([]byte, 65536)
    conn.Write(data)

    time.Sleep(time.Second * 10000)
}

//go-tcpsock/read_write/server4.go
... ...
func handleConn(c net.Conn) {
    defer c.Close()
    for {
        // read from the connection
        time.Sleep(10 * time.Second)
        var buf = make([]byte, 65536)
        log.Println("start to read from conn")
        c.SetReadDeadline(time.Now().Add(time.Microsecond * 10))
        n, err := c.Read(buf)
        if err != nil {
            log.Printf("conn read %d bytes,  error: %s", n, err)
            if nerr, ok := err.(net.Error); ok && nerr.Timeout() {
                continue
            }
            return
        }
        log.Printf("read %d bytes, content is %s\n", n, string(buf[:n]))
    }
}
```
在Server端我们通过Conn的SetReadDeadline方法设置了10微秒的读超时时间，Server的执行结果如下：
```
$go run server4.go

2015/11/17 14:21:17 accept a new connection
2015/11/17 14:21:27 start to read from conn
2015/11/17 14:21:27 conn read 0 bytes,  error: read tcp 127.0.0.1:8888->127.0.0.1:60970: i/o timeout
2015/11/17 14:21:37 start to read from conn
2015/11/17 14:21:37 read 65536 bytes, content is
```
虽然每次都是10微秒超时，但结果不同，第一次Read超时，读出数据长度为0；第二次读取所有数据成功，没有超时。反复执行了多次，没能出现“读出部分数据且返回超时错误”的情况。

和读相比，Write遇到的情形一样不少，我们也逐一看一下。

##### 1、成功写

前面例子着重于Read，client端在Write时并未判断Write的返回值。所谓“成功写”指的就是Write调用返回的n与预期要写入的数据长度相等，且error = nil。这是我们在调用Write时遇到的最常见的情形，这里不再举例了。

##### 2、写阻塞

TCP连接通信两端的OS都会为该连接保留数据缓冲，一端调用Write后，实际上数据是写入到OS的协议栈的数据缓冲的。TCP是全双工通信，因此每个方向都有独立的数据缓冲。当发送方将对方的接收缓冲区以及自身的发送缓冲区写满后，Write就会阻塞。我们来看一个例子：client5.go和server.go。
```
//go-tcpsock/read_write/client5.go
... ...
func main() {
    log.Println("begin dial...")
    conn, err := net.Dial("tcp", ":8888")
    if err != nil {
        log.Println("dial error:", err)
        return
    }
    defer conn.Close()
    log.Println("dial ok")

    data := make([]byte, 65536)
    var total int
    for {
        n, err := conn.Write(data)
        if err != nil {
            total += n
            log.Printf("write %d bytes, error:%s\n", n, err)
            break
        }
        total += n
        log.Printf("write %d bytes this time, %d bytes in total\n", n, total)
    }

    log.Printf("write %d bytes in total\n", total)
    time.Sleep(time.Second * 10000)
}

//go-tcpsock/read_write/server5.go
... ...
func handleConn(c net.Conn) {
    defer c.Close()
    time.Sleep(time.Second * 10)
    for {
        // read from the connection
        time.Sleep(5 * time.Second)
        var buf = make([]byte, 60000)
        log.Println("start to read from conn")
        n, err := c.Read(buf)
        if err != nil {
            log.Printf("conn read %d bytes,  error: %s", n, err)
            if nerr, ok := err.(net.Error); ok && nerr.Timeout() {
                continue
            }
        }

        log.Printf("read %d bytes, content is %s\n", n, string(buf[:n]))
    }
}
... ...
```
Server5在前10s中并不Read数据，因此当client5一直尝试写入时，写到一定量后就会发生阻塞：
```
$go run client5.go

2015/11/17 14:57:33 begin dial...
2015/11/17 14:57:33 dial ok
2015/11/17 14:57:33 write 65536 bytes this time, 65536 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 131072 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 196608 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 262144 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 327680 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 393216 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 458752 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 524288 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 589824 bytes in total
2015/11/17 14:57:33 write 65536 bytes this time, 655360 bytes in total
```
在Darwin上，这个size大约在679468bytes。后续当server5每隔5s进行Read时，OS socket缓冲区腾出了空间，client5就又可以写入了：
```
$go run server5.go
2015/11/17 15:07:01 accept a new connection
2015/11/17 15:07:16 start to read from conn
2015/11/17 15:07:16 read 60000 bytes, content is
2015/11/17 15:07:21 start to read from conn
2015/11/17 15:07:21 read 60000 bytes, content is
2015/11/17 15:07:26 start to read from conn
2015/11/17 15:07:26 read 60000 bytes, content is
....
```
client端：
```
2015/11/17 15:07:01 write 65536 bytes this time, 720896 bytes in total
2015/11/17 15:07:06 write 65536 bytes this time, 786432 bytes in total
2015/11/17 15:07:16 write 65536 bytes this time, 851968 bytes in total
2015/11/17 15:07:16 write 65536 bytes this time, 917504 bytes in total
2015/11/17 15:07:27 write 65536 bytes this time, 983040 bytes in total
2015/11/17 15:07:27 write 65536 bytes this time, 1048576 bytes in total
.... ...
```
##### 3、写入部分数据

Write操作存在写入部分数据的情况，比如上面例子中，当client端输出日志停留在“write 65536 bytes this time, 655360 bytes in total”时，我们杀掉server5，这时我们会看到client5输出以下日志：
```
...
2015/11/17 15:19:14 write 65536 bytes this time, 655360 bytes in total
2015/11/17 15:19:16 write 24108 bytes, error:write tcp 127.0.0.1:62245->127.0.0.1:8888: write: broken pipe
2015/11/17 15:19:16 write 679468 bytes in total
```
显然Write并非在655360这个地方阻塞的，而是后续又写入24108后发生了阻塞，server端socket关闭后，我们看到Wrote返回er != nil且n = 24108，程序需要对这部分写入的24108字节做特定处理。

##### 4、写入超时

如果非要给Write增加一个期限，那我们可以调用SetWriteDeadline方法。我们copy一份client5.go，形成client6.go，在client6.go的Write之前增加一行timeout设置代码：

conn.SetWriteDeadline(time.Now().Add(time.Microsecond * 10))
启动server6.go，启动client6.go，我们可以看到写入超时的情况下，Write的返回结果：
```
$go run client6.go
2015/11/17 15:26:34 begin dial...
2015/11/17 15:26:34 dial ok
2015/11/17 15:26:34 write 65536 bytes this time, 65536 bytes in total
... ...
2015/11/17 15:26:34 write 65536 bytes this time, 655360 bytes in total
2015/11/17 15:26:34 write 24108 bytes, error:write tcp 127.0.0.1:62325->127.0.0.1:8888: i/o timeout
2015/11/17 15:26:34 write 679468 bytes in total
```
可以看到在写入超时时，依旧存在部分数据写入的情况。

综上例子，虽然Go给我们提供了阻塞I/O的便利，但在调用Read和Write时依旧要综合需要方法返回的n和err的结果，以做出正确处理。net.conn实现了io.Reader和io.Writer接口，因此可以试用一些wrapper包进行socket读写，比如bufio包下面的Writer和Reader、io/ioutil下的函数等。

##### Goroutine safe

基于goroutine的网络架构模型，存在在不同goroutine间共享conn的情况，那么conn的读写是否是goroutine safe的呢？在深入这个问题之前，我们先从应用意义上来看read操作和write操作的goroutine-safe必要性。

对于read操作而言，由于TCP是面向字节流，conn.Read无法正确区分数据的业务边界，因此多个goroutine对同一个conn进行read的意义不大，goroutine读到不完整的业务包反倒是增加了业务处理的难度。对与Write操作而言，倒是有多个goroutine并发写的情况。不过conn读写是否goroutine-safe的测试不是很好做，我们先深入一下runtime代码，先从理论上给这个问题定个性：

net.conn只是*netFD的wrapper结构，最终Write和Read都会落在其中的fd上：
```
type conn struct {
    fd *netFD
}
```
netFD在不同平台上有着不同的实现，我们以net/fd_unix.go中的netFD为例：
```
// Network file descriptor.
type netFD struct {
    // locking/lifetime of sysfd + serialize access to Read and Write methods
    fdmu fdMutex

    // immutable until Close
    sysfd       int
    family      int
    sotype      int
    isConnected bool
    net         string
    laddr       Addr
    raddr       Addr

    // wait server
    pd pollDesc
}
```
我们看到netFD中包含了一个runtime实现的fdMutex类型字段，从注释上来看，该fdMutex用来串行化对该netFD对应的sysfd的Write和Read操作。从这个注释上来看，所有对conn的Read和Write操作都是有fdMutex互斥的，从netFD的Read和Write方法的实现也证实了这一点：
```
func (fd *netFD) Read(p []byte) (n int, err error) {
    if err := fd.readLock(); err != nil {
        return 0, err
    }
    defer fd.readUnlock()
    if err := fd.pd.PrepareRead(); err != nil {
        return 0, err
    }
    for {
        n, err = syscall.Read(fd.sysfd, p)
        if err != nil {
            n = 0
            if err == syscall.EAGAIN {
                if err = fd.pd.WaitRead(); err == nil {
                    continue
                }
            }
        }
        err = fd.eofError(n, err)
        break
    }
    if _, ok := err.(syscall.Errno); ok {
        err = os.NewSyscallError("read", err)
    }
    return
}

func (fd *netFD) Write(p []byte) (nn int, err error) {
    if err := fd.writeLock(); err != nil {
        return 0, err
    }
    defer fd.writeUnlock()
    if err := fd.pd.PrepareWrite(); err != nil {
        return 0, err
    }
    for {
        var n int
        n, err = syscall.Write(fd.sysfd, p[nn:])
        if n > 0 {
            nn += n
        }
        if nn == len(p) {
            break
        }
        if err == syscall.EAGAIN {
            if err = fd.pd.WaitWrite(); err == nil {
                continue
            }
        }
        if err != nil {
            break
        }
        if n == 0 {
            err = io.ErrUnexpectedEOF
            break
        }
    }
    if _, ok := err.(syscall.Errno); ok {
        err = os.NewSyscallError("write", err)
    }
    return nn, err
}
```
每次Write操作都是受lock保护，直到此次数据全部write完。因此在应用层面，要想保证多个goroutine在一个conn上write操作的Safe，需要一次write完整写入一个“业务包”；一旦将业务包的写入拆分为多次write，那就无法保证某个Goroutine的某“业务包”数据在conn发送的连续性。

同时也可以看出即便是Read操作，也是lock保护的。多个Goroutine对同一conn的并发读不会出现读出内容重叠的情况，但内容断点是依 runtime调度来随机确定的。存在一个业务包数据，1/3内容被goroutine-1读走，另外2/3被另外一个goroutine-2读 走的情况。比如一个完整包：world，当goroutine的read slice size < 5时，存在可能：一个goroutine读到 “worl”,另外一个goroutine读出”d”。

### 四、Socket 属性

原生 Socket API 提供了丰富的 sockopt 设置接口，但 Golang 有自己的网络架构模型，golang 提供的 socket options 接口也是基于上述模型的必要的属性设置。包括
```
SetKeepAlive
SetKeepAlivePeriod
SetLinger
SetNoDelay （默认no delay）
SetWriteBuffer
SetReadBuffer
```
不过上面的 Method 是 TCPConn 的，而不是 Conn 的，要使用上面的 Method 的，需要 type assertion：

```go
tcpConn, ok := c.(*TCPConn)
if !ok {
    //error handle
}

tcpConn.SetNoDelay(true)
```

对于 listener socket, golang 默认采用了 SO_REUSEADDR，这样当你重启 listener 程序时，不会因为 address in use 的错误而启动失败。而 listen backlog 的默认值是通过获取系统的设置值得到的。不同系统不同：mac 128, linux 512等。

### 五、关闭连接

由于 Socket 是全双工的，Client 和 Server 端在己方已关闭的 Socket 和对方关闭的 Socket 上操作的结果有不同。看下面例子：

```go
//go-tcpsock/conn_close/client1.go
... ...
func main() {
    log.Println("begin dial...")
    conn, err := net.Dial("tcp", ":8888")
    if err != nil {
        log.Println("dial error:", err)
        return
    }
    conn.Close()
    log.Println("close ok")

    var buf = make([]byte, 32)
    n, err := conn.Read(buf)
    if err != nil {
        log.Println("read error:", err)
    } else {
        log.Printf("read % bytes, content is %s\n", n, string(buf[:n]))
    }

    n, err = conn.Write(buf)
    if err != nil {
        log.Println("write error:", err)
    } else {
        log.Printf("write % bytes, content is %s\n", n, string(buf[:n]))
    }

    time.Sleep(time.Second * 1000)
}

//go-tcpsock/conn_close/server1.go
... ...
func handleConn(c net.Conn) {
    defer c.Close()

    // read from the connection
    var buf = make([]byte, 10)
    log.Println("start to read from conn")
    n, err := c.Read(buf)
    if err != nil {
        log.Println("conn read error:", err)
    } else {
        log.Printf("read %d bytes, content is %s\n", n, string(buf[:n]))
    }

    n, err = c.Write(buf)
    if err != nil {
        log.Println("conn write error:", err)
    } else {
        log.Printf("write %d bytes, content is %s\n", n, string(buf[:n]))
    }
}
... ...
```

上述例子的执行结果如下：

```
$go run server1.go
2015/11/17 17:00:51 accept a new connection
2015/11/17 17:00:51 start to read from conn
2015/11/17 17:00:51 conn read error: EOF
2015/11/17 17:00:51 write 10 bytes, content is

$go run client1.go
2015/11/17 17:00:51 begin dial...
2015/11/17 17:00:51 close ok
2015/11/17 17:00:51 read error: read tcp 127.0.0.1:64195->127.0.0.1:8888: use of closed network connection
2015/11/17 17:00:51 write error: write tcp 127.0.0.1:64195->127.0.0.1:8888: use of closed network connection
```

从 client1 的结果来看，在己方已经关闭的 socket 上再进行 read 和 write 操作，会报错：use of closed network connection。
从 server1 的结果来看，在对方关闭的 socket 上执行 read 操作会得到 EOF error，但 write 操作会成功，因为数据会成功写入己方的内核 socket 缓冲区中，即便最终发不到对方 socket 缓冲区了，因为己方 socket 并未关闭。因此当发现对方 socket 关闭后，己方应正确合理处理自己的 socket，再继续 write 已毫无意义。

### 六、附

本文代码实验环境：go 1.5.1 on Darwin amd64 以及部分在 ubuntu 14.04 amd64。

[demo代码](https://github.com/bigwhite/experiments/tree/master/go-tcpsock)
