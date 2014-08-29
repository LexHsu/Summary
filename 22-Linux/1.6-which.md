which
===

linux 中查找某个文件位置，常会用到如下命令：

- which    查看`可执行文件`的位置。
- whereis  查看`文件`的位置。
- locate   配合数据库查看`文件`位置。
- find     搜索硬盘查询`文件`名称。



### 命令格式
which 可执行文件名称

### 命令功能
本文主要讨论 which，其作用是在 PATH 变量指定的路径中，搜索某个系统命令的位置，并且返回第一个搜索结果。
也就是说，使用 which 命令，就可以看到某个系统命令是否存在，以及执行的到底是哪一个位置的命令。

### 命令参数

-n 　指定文件名长度，指定的长度必须大于或等于所有文件中最长的文件名。
-p 　与 `-n` 参数相同，但此处的包括了文件的路径。
-w 　指定输出时栏位的宽度。
-V 　显示版本信息

### 使用实例

```
查找文件、显示命令路径
[root@localhost ~]# which pwd
/bin/pwd
注：which 根据用户配置的 PATH 变量内的目录进行查找！因此不同的 PATH 配置找到的命令不同！

用 which 去找出 which
[root@localhost ~]# which which
alias which='alias | /usr/bin/which --tty-only --read-alias --show-dot 	--show-tilde'
     /usr/bin/which
注：有两个 which ，其中一个是 alias 别名，意思是输入 which 会等於后面接的那串命令！

找出 cd 命令
[root@localhost ~]# which cd
注：因为 cd 是 bash 内建命令！而 which 默认是找 PATH 内所规范的目录，所以没有找到！
```
