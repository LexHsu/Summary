Go 环境配置
===

### GOROOT

Go 语言默认的安装目录通常是 /usr/local/go，在windows下是 c:\Go。
若安装到其他目录需设置 GOROOT 环境变量。该变量指向自定义的安装目录。
如将 Go 安装到 home 目录，将如下命令添加至 $HOME/.profile 或 /etc/profile 文件：

```
export GOROOT=$HOME/go
export PATH=$PATH:$GOROOT/bin
```

### GOPATH

GOPATH 用于设置包加载路径。可设置多个目录，Ubuntu 下用冒号分隔(Win 下用分号)。每个目录应包含 src, pkg, bin 三个文件夹

```
export GOPATH=$HOME/Applications/go:$HOME/Work/go
```

go get 获取的开源包放入第一个 GOPATH 路径中，如：

```
go get github.com/smartystreets/goconvey
```

会将 goconvey 包下载至 Applications/go 目录下。
