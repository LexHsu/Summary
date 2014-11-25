Tcpdump抓包
===

### 条件

1. Root
2. [配置Android SDK](http://developer.android.com/sdk/index.html)
3. [配置Tcpdump](http://www.strazzere.com/android/tcpdump)

### 过程

```
adb push ~/Downloads/tcpdump /data/local/
adb shell
chmod 777 /data/local/tcpdump
/data/local/tcpdump -p -vv -s 0 -w /sdcard/p.pcap
```

### Tcpdump命令

```
命令格式：
tcpdump [ -adeflnNOpqStvx ] [ -c 数量 ] [ -F 文件名 ]
[ -i 网络接口 ] [ -r 文件名] [ -s snaplen ]
[ -T 类型 ] [ -w 文件名 ] [表达式 ]

-a     将网络地址和广播地址转变成名字
-d     将匹配信息包的代码以人们能够理解的汇编格式给出
-dd    将匹配信息包的代码以c语言程序段的格式给出
-ddd   将匹配信息包的代码以十进制的形式给出
-e     在输出行打印出数据链路层的头部信息
-f     将外部的Internet地址以数字的形式打印出来
-l     使标准输出变为缓冲行形式
-n     不把网络地址转换成名字
-t     在输出的每一行不打印时间戳
-v     输出一个稍微详细的信息，例如在ip包中可以包括ttl和服务类型的信息
-vv    输出详细的报文信息
-c     在收到指定的包的数目后，tcpdump就会停止
-F     从指定的文件中读取表达式,忽略其它的表达式
-i     指定监听的网络接口
-r     从指定的文件中读取包(这些包一般通过-w选项产生)
-w     直接将包写入文件中，并不分析和打印出来
-T     将监听到的包直接解释为指定的类型的报文，常见的类型有rpc(远程过程调用)和snmp(简单网络管理协议)
```
