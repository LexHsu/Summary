whereis
===

### 命令格式

```
whereis [-bmsu] [BMS 目录名 -f ] 文件名
```

### 命令功能
whereis 命令是定位可执行文件、源代码文件、帮助文件在文件系统中的位置。这些文件的属性应属于原始代码，二进制文件，或是帮助文件。
此外还可搜索源代码、指定备用搜索路径和搜索不寻常项。

whereis 命令只能搜索程序名，且只搜索二进制文件（-b）、man 说明文件（-m）和源代码文件（-s）。若省略参数，则返回所有信息。
和 find 相比，whereis 查找速度非常快，因为 linux 系统会将系统内的所有文件都记录在一个数据库文件中，
当使用 whereis 和 locate 时，会从数据库中查找数据，而不是像 find 命令那样，通过遍历硬盘查找。
但是该数据库文件并不是实时更新，默认情况下时一星期更新一次，因此，在用 whereis 和 locate 查找文件时，
有时会找到已经被删除的数据，或者刚刚建立文件，却无法查找到，原因就是因为数据库文件没有被更新。

### 命令参数

```
-b   定位可执行文件。
-m   定位帮助文件。
-s   定位源代码文件。
-u   搜索默认路径下除可执行文件、源代码文件、帮助文件以外的其它文件。
-B   指定搜索可执行文件的路径。
-M   指定搜索帮助文件的路径。
-S   指定搜索源代码文件的路径。
```

### 使用示例

```
仅查找 svn 二进制文件
whereis -b svn
[root@localhost ~]# whereis -b svn
svn: /usr/bin/svn /usr/local/svn

查找 svn 说明文档路径
[root@localhost ~]# whereis -m svn
svn: /usr/share/man/man1/svn.1.gz

查找 source 源文件
[root@localhost ~]# whereis -s svn
```
