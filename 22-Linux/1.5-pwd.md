pwd
===

pwd 用于查看当前工作目录的完整路径。



目录连接链接时，pwd -P  显示出实际路径，而非使用连接（link）路径；pwd显示的是连接路径

```
-L 目录连接链接时，输出连接路径
-P 输出物理路径

[root@localhost init.d]# /bin/pwd
/etc/rc.d/init.d
[root@localhost init.d]# /bin/pwd --help
[root@localhost init.d]# /bin/pwd -P
/etc/rc.d/init.d
[root@localhost init.d]# /bin/pwd -L
/etc/init.d
```
