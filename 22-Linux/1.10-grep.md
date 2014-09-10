grep
===

### 命令功能

grep全称Global Regular Expression Print，是强大的正则表达式文本搜索工具，并将匹配的行打印出来，其使用权限是所有用户。

grep 可用于 shell 脚本，因为 grep 通过返回一个状态值来说明搜索的状态：

- 如果模板搜索成功，返回 0。
- 如果搜索不成功，返回 1。
- 如果搜索的文件不存在，返回 2。

因此可进行一些自动化的文本处理工作。

### 命令格式

```
grep [option] pattern file
```

### 命令参数

```
-a   --text   #不要忽略二进制的数据。
-A<显示行数>   --after-context=<显示行数>   #除了显示符合范本样式的那一列之外，并显示该行之后的内容。
-b   --byte-offset   #在显示符合样式的那一行之前，标示出该行第一个字符的编号。
-B<显示行数>   --before-context=<显示行数>   #除了显示符合样式的那一行之外，并显示该行之前的内容。
-c    --count   #计算符合样式的列数。
-C<显示行数>    --context=<显示行数>或-<显示行数>   #除了显示符合样式的那一行之外，并显示该行之前后的内容。
-d <动作>      --directories=<动作>   #当指定要查找的是目录而非文件时，必须使用这项参数，否则grep指令将回报信息并停止动作。
-e<范本样式>  --regexp=<范本样式>   #指定字符串做为查找文件内容的样式。
-E      --extended-regexp   #将样式为延伸的普通表示法来使用。
-f<规则文件>  --file=<规则文件>   #指定规则文件，其内容含有一个或多个规则样式，让grep查找符合规则条件的文件内容，格式为每行一个规则样式。
-F   --fixed-regexp   #将样式视为固定字符串的列表。
-G   --basic-regexp   #将样式视为普通的表示法来使用。
-h   --no-filename   #在显示符合样式的那一行之前，不标示该行所属的文件名称。
-H   --with-filename   #在显示符合样式的那一行之前，表示该行所属的文件名称。
-i    --ignore-case   #忽略字符大小写的差别。
-l    --file-with-matches   #列出文件内容符合指定的样式的文件名称。
-L   --files-without-match   #列出文件内容不符合指定的样式的文件名称。
-n   --line-number   #在显示符合样式的那一行之前，标示出该行的列数编号。
-q   --quiet或--silent   #不显示任何信息。
-r   --recursive   #此参数的效果和指定 “-d recurse” 参数相同。
-s   --no-messages   #不显示错误信息。
-v   --revert-match   #显示不包含匹配文本的所有行。
-V   --version   #显示版本信息。
-w   --word-regexp   #只显示全字符合的列。
-x    --line-regexp   #只显示全列符合的列。
-y   #此参数的效果和指定“-i”参数相同。
```

### 规则表达式

```
^  #锚定行的开始 如：'^grep' 匹配所有以 grep 开头的行。
$  #锚定行的结束 如：'grep$' 匹配所有以 grep 结尾的行。
.  #匹配一个非换行符的字符 如：'gr.p' 匹配 gr 后接一个任意字符，然后是 p。
*  #匹配零个或多个先前字符 如：'*grep' 匹配所有一个或多个空格后紧跟 grep 的行。
.*   #一起用代表任意字符。
[]   #匹配一个指定范围内的字符，如 '[Gg]rep' 匹配 Grep 和 grep。
[^]  #匹配一个不在指定范围内的字符，如：'[^A-FH-Z]rep' 匹配不包含 A-R 和 T-Z 的一个字母开头，紧跟 rep 的行。
\(..\)  #标记匹配字符，如'\(love\)'，love被标记为1。
\<      #锚定单词的开始，如: '\<grep' 匹配包含以 grep 开头的单词的行。
\>      #锚定单词的结束，如'grep\>'匹配包含以 grep 结尾的单词的行。
x\{m\}  #重复字符 x，m 次，如：'0\{5\}'匹配包含 5 个 o 的行。
x\{m,\}  #重复字符 x,至少 m 次，如：'o\{5,\}'匹配至少有 5 个 o 的行。
x\{m,n\}  #重复字符 x，至少 m 次，不多于n次，如：'o\{5,10\}'匹配 5-10 个 o 的行。
\w    #匹配文字和数字字符，也就是 [A-Za-z0-9]，如：'G\w*p' 匹配以 G 后跟零个或多个文字或数字字符，然后是 p。
\W    #\w 的反置形式，匹配一个或多个非单词字符，如点号句号等。
\b    #单词锁定符，如: '\bgrep\b' 只匹配 grep。  
POSIX 字符:
为了在不同国家的字符编码中保持一至，POSIX(The Portable Operating System Interface)增加了特殊的字符类，
如 [:alnum:] 是 [A-Za-z0-9] 的另一个写法。要把它们放到 [] 号内才能成为正则表达式，如 [A- Za-z0-9] 或 [[:alnum:]]。
在 linux 下的 grep 除 fgrep 外，都支持 POSIX 的字符类。

[:alnum:]    #文字数字字符
[:alpha:]    #文字字符
[:digit:]    #数字字符
[:graph:]    #非空字符（非空格、控制字符）
[:lower:]    #小写字符
[:cntrl:]    #控制字符
[:print:]    #非空字符（包括空格）
[:punct:]    #标点符号
[:space:]    #所有空白字符（新行，空格，制表符）
[:upper:]    #大写字符
[:xdigit:]   #十六进制数字（0-9，a-f，A-F）
```

### 使用示例

```
查找指定进程
ps -ef|grep svn
第一条记录是查找出的进程；第二条结果是 grep 进程本身，并非真正要找的进程。

查找指定进程个数
ps -ef|grep svn -c
或
ps -ef|grep -c svn

从文件中读取关键词进行搜索
cat test.txt | grep -f test2.txt
输出 test.txt 文件中含有从 test2.txt 文件中读取出的关键词的内容行

从文件中读取关键词进行搜索 且显示行号
cat test.txt | grep -nf test2.txt
输出 test.txt 文件中含有从 test2.txt 文件中读取出的关键词的内容行，并显示每一行的行号

从文件中查找关键词
grep 'linux' test.txt

从多个文件中查找关键词
grep 'linux' test.txt test2.txt
多文件时，输出查询到的信息内容行时，会把文件的命名在行最前面输出并且加上":"作为标示符

grep 不显示本身进程
ps aux|grep \[s]sh
ps aux | grep ssh | grep -v "grep"

找出以 u 开头的行内容
cat test.txt |grep ^u

输出非u开头的行内容
cat test.txt |grep ^[^u]

输出以hat结尾的行内容
cat test.txt |grep hat$

显示包含ed或者at字符的内容行
cat test.txt |grep -E "ed|at"

显示当前目录下面以.txt 结尾的文件中的所有包含每个字符串至少有7个连续小写字符的字符串的行
grep '[a-z]\{7\}' *.txt
```
