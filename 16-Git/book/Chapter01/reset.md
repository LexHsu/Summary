git reset
===

```
git reset --mixed：
此为默认方式，不带任何参数的git reset，即时这种方式，它回退到某个版本，只保留源码，回退commit和add信息，需要重新add，commit

git reset --soft：
回退到某个版本，只回退了commit的信息。如果还要提交，直接commit即可

git reset  --hard：
彻底回退到某个版本，之前修改文件也会清除，本地源码变为上一个版本的内容，

例：
以remote端最新节点为准，取消本地所有修改
git reset --hard origin/master

以remote端最新节点为准，保留本地所有修改
git reset origin/master



HEAD 最近的提交，同HEAD~0
HEAD^ 上一次提交，同HEAD~1
HEAD^ ^ 上上次的提交（倒数第三次），同HEAD~2
HEAD^^^ 倒数第四次提交，同HEAD~3
```