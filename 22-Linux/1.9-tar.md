tar
===

Linux 中区分两个概念：打包和压缩。
- 打包是指将一大堆文件或目录变成一个总的文件。
- 压缩则是将一个大的文件通过一些压缩算法变成一个小文件。
为什么要区分这两个概念？这源于 Linux 中很多压缩程序只能针对一个文件进行压缩，要压缩一大堆文件时，
需先将这一大堆文件先打成一个包（tar 命令），然后再用压缩程序进行压缩（gzip bzip2 命令）。
linux 下最常用的打包程序就是 tar，使用 tar 程序打出来的包常称为 tar 包，常以 `.tar` 结尾。
生成 tar 包后，可用其它程序进行压缩。

### 常见压缩文件格式

```
*.Z       compress 程序压缩的档案
*.bz2     bzip2 程序压缩的档案
*.gz      gzip 程序压缩的档案
*.tar     tar 程序打包的数据，并没有压缩过
*.tar.gz  tar 程序打包的档案，其中并且经过 gzip 的压缩
*.zip     zip 程序压缩文件
*.rar     rar 程序压缩文件
```

### tar命令格式

```
tar [必要参数] [可选参数] [文件]
```

### 命令功能

用于压缩和解压文件。tar 本身不具有压缩功能，而是调用相应压缩功能实现。

### 命令参数

```
-A 新增压缩文件到已存在的压缩
-B 设置区块大小
-c 建立新的压缩文件
-d 记录文件的差别
-r 添加文件到已经压缩的文件
-u 添加改变了和现有的文件到已经存在的压缩文件
-x 从压缩的文件中提取文件
-t 显示压缩文件的内容
-z 支持 gzip 解压文件
-j 支持 bzip2 解压文件
-Z 支持 compress 解压文件
-v 显示操作过程
-l 文件系统边界设置
-k 保留原有文件不覆盖
-m 保留文件不被覆盖
-W 确认压缩文件的正确性
可选参数如下：
-b 设置区块数目
-C 切换到指定目录
-f 指定压缩文件，注意 f 之后要立即接档名，不可再加参数
-N 比后面接的日期(yyyy/mm/dd)还要新的才会被打包进新建的档案中
--help 显示帮助信息
--version 显示版本信息
--exclude FILE 在压缩的过程中，不要将 FILE 打包
```

### Gzip 命令参数

```
参数说明：
-d 解压缩的参数！
-r 递归处理，将指定目录下的所有文件及子目录一并处理
-# 压缩等级， 1 最不好， 9 最好， 6 是默认值！
```

### 常见打包或者压缩命令

```
tar
解包：tar xvf FileName.tar
打包：tar cvf FileName.tar DirName （注：tar 是打包，不是压缩）
打包并gzip压缩：tar -zcvf FileName.tar.gz DirName
打包并bzip2压缩：tar -jcvf FileName.tar.gz DirName

.gz
解压1：gunzip FileName.gz
解压2：gzip -d FileName.gz
压缩：gzip FileName
最大压缩：gzip -9 FileName

.tar.gz 和 .tgz
因为压缩通过z，即gzip压缩的，因此解压也需要z参数
解压：tar zxvf FileName.tar.gz
压缩：tar zcvf FileName.tar.gz DirName

.bz2
解压1：bzip2 -d FileName.bz2
解压2：bunzip2 FileName.bz2
压缩： bzip2 -z FileName
查看压缩文件内容：bzcat FileName

.tar.bz2
解压：tar jxvf FileName.tar.bz2
压缩：tar jcvf FileName.tar.bz2 DirName

.bz
解压1：bzip2 -d FileName.bz
解压2：bunzip2 FileName.bz
压缩：未知

.tar.bz
解压：tar jxvf FileName.tar.bz
压缩：未知

.Z
解压1：uncompress FileName.Z
解压2：compress -d FileName.Z
压缩：compress FileName

.tar.Z
解压：tar Zxvf FileName.tar.Z
压缩：tar Zcvf FileName.tar.Z DirName

.zip
解压：unzip FileName.zip
压缩：zip FileName.zip DirName

.rar
解压：rar x FileName.rar
压缩：rar a FileName.rar DirName
```

### 使用示例

```
将文件全部打包成 tar 包
tar -cvf log.tar log2012.log       仅打包，不压缩！
tar -zcvf log.tar.gz log2012.log   打包后，以 gzip 压缩
tar -zcvf log.tar.bz2 log2012.log  打包后，以 bzip2 压缩
如果加 z 参数，则以 .tar.gz 或 .tgz 来代表 gzip 压缩过的 tar包
如果加 j 参数，则以 .tar.bz2 来作为tar包名。

查阅上述 tar 包内有哪些文件
tar -ztvf log.tar.gz
由于使用 gzip 压缩的 log.tar.gz，所以要查阅 log.tar.gz 包内的文件时，需加上 z 参数。

将 tar 包解压缩
tar -zxvf /opt/soft/test/log.tar.gz

只将 /tar 内的 部分文件解压出来
tar -zxvf /opt/soft/test/log30.tar.gz log2013.log
可通过 tar -ztvf 查阅 tar 包内的文件名称，如果单只要一个文件，可通过该方式解压部分文件

文件备份下来，并且保存其权限
tar -zcvpf log31.tar.gz log2014.log log2015.log log2016.log
这个 -p 的属性是很重要的，尤其是保留原本文件的属性时

在test文件夹当中，比2012.11.13日期新的文件才打包压缩
tar -N "2012/11/13" -zcvf log17.tar.gz test

备份文件夹内容，但排除部分文件
tar --exclude scf/service -zcvf scf.tar.gz scf/*

tar -zcvf host.tar.gz / --exclude /mnt --exclude /proc
将根目录的所有数据都打包进 host.tar.gz 中，是 /mnt 及 /proc 则不打包！

tar -cvf - /home | tar -xvf -
将 /home 打包之后，直接解压缩在 /root下！
优点在于不需建立一次中间档案！该语法最好使用『绝对路径』，相对路径可能会有问题

将file1、file2以及 /usr/work 目录的内容压缩起来，放入 filename.bz2 文件中
gzip -r filename.gz file1 file2 /usr/work/

将当前目录下的所有文件和文件夹全部压缩成myfile.zip文件,－r表示递归压缩子目录下所有文件
zip -r myfile.zip ./*  

删除压缩文件中smart.txt文件
zip -d myfile.zip smart.txt

向压缩文件中myfile.zip中添加rpm_info.txt文件
zip -m myfile.zip ./rpm_info.txt

把myfile.zip文件解压到 /home/sunny/
unzip -o -d /home/sunny myfile.zip
