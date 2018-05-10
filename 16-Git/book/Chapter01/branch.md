branch
===

```
查看本地所有分支
git branch

查看远程所有分支
git branch -r

查看本地和远程所有分支
git branch -a

新建分支，但不切换到新建分支
git branch [-f] <branchName>

切换到其他分支
git branch branchName

新建分支并切换到指定分支，-B表示如果新建分支时，已存在同名分支，则强制创建新分支并覆盖原来分支
git checkout (-b | -B) <branch name>

新建分支并切换到指定分支，且映射远程分支
git checkout -b <branch name> remote/branch
例：
git checkout -b branch1 origin/branch1

删除本地分支
git branch (-d | -D) <branch name>

删除远程分支
git branch -d -r <remote branch name>

重命名本地分支
git branch (-m | -M) <oldbranch> <new branch>

重命名远程分支
1. 删除远程待修改分支
2. push 本地新分支到远程

附：
-d
--delete：删除
-D
--delete --force的快捷键
-f
--force：强制
-m
--move：移动或重命名
-M
--move --force的快捷键
-r
--remote：远程
-a
--all：所有
```
