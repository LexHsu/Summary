git clean
===

clean用来从工作目录中删除所有#没有tracked过#的文件。注意只影响untracked文件。

```
git clean -n
是一次clean的提醒, 告诉你哪些文件会被删除. 不会真正的删除文件

git clean -f
删除当前目录下所有没有track过的文件。不会删除.gitignore文件里面指定的文件夹和文件, 不管这些文件有没有被track过

git clean -f <path>
删除指定路径下的没有被track过的文件

git clean -df
删除当前目录下没有被track过的文件和文件夹

git clean -xf
删除当前目录下所有没有track过的文件. 不管他是否是.gitignore文件里面指定的文件夹和文件

例
清除编译后生成的临时文件（untracked）及有修改的（modified）文件
git reset --hard
git clean -fd
```
