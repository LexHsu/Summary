Go搭建Http(s)服务器
===

HTTPS证书生成
```
openssl genrsa -out key.pem 2048
openssl req -new -x509 -key key.pem -out cert.pem -days 1095
```
源码如下：
```go
package main

import (
    "fmt"
    "net/http"
    "strings"
    "log"
)

func handleRoutle(w http.ResponseWriter, r *http.Request) {

    //解析参数，默认不解析
    r.ParseForm()

    //这些信息是输出到服务器端的打印信息
    fmt.Println(r.Form)

    for k, v := range r.Form {
        fmt.Println("key:", k)
        fmt.Println("val:", strings.Join(v, ""))
    }
    // 忽略大小写比较字符串
    if (strings.equalFold(r.FormValue("method"), "method")) {
        w.Write([]byte("Hello"))
        return
    }
    fmt.Fprintf(w, "world")
}

func main() {
    // 设置访问的路由
    http.HandleFunc("/", handleRoute)
    // 设置HTTP监听的端口
    // err := http.ListenAndServe(":8080", nil)
    // 设置HTTPS监听的端口
    err := http.ListenAndServeTLS(":8080", "cert.pem", "key.pem", nil)
    if err != nil {
        log.Fatal("ListenAndServe: ", err)
    }
}
```
