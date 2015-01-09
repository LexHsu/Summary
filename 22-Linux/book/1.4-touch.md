touch
===

常用于 make 时修改文件时间戳，或新建一个文件。

### 命令参数

```
-a   或 --time=atime 或--time=access 或 --time=use 只更改存取时间。
-c   或 --no-create 不建立任何文档。
-d 　使用指定的日期时间，而非现在的时间。
-f 　此参数将忽略不予处理，仅负责解决 BSD 版本 touch 指令的兼容性问题。
-m   或 --time=mtime 或 --time=modify 只更改变动时间。
-r 　把指定文档或目录的日期时间，统统设成和参考文档或目录的日期时间相同。
-t 　使用指定的日期时间，而非现在的时间。[YYYY]MMDDhhmm[.SS]，表示年月日时分秒
```

### 命令功能

touch 命令参数可更改文档或目录的日期时间，包括存取时间和更改时间。


```
创建新文件
touch log1.log log2.log

更新 log.log 时间戳，如 log.log 不存在，则不创建文件
touch -c log.log

更新 log.log 的时间和 log2.log 时间戳相同
touch -r log.log log2.log

指定文件时间戳
touch -t 201211142234.50 log.log
```
