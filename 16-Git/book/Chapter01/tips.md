git tips
===

### 1. You have unstaged changes

执行git pull –rebase，提示：
```
error: Cannot pull with rebase: You have unstaged changes. 
error: Additionally, your index contains uncommitted changes. 
```
原因：如果有未提交的更改，不能git pull

解决： 
git stash #可用来暂存当前正在进行的工作
git pull –-rebase 
git stash pop #从Git栈中读取最近一次保存的内容


### 2. 取消配置文件跟踪

通过.gitignore对文件忽略跟踪，如果该文件已经被跟踪（track），再把文件名添加到.gitignore中，无效。

虽然可以通过以下命令让.gitignore生效：

```git
git rm -rf --cache xxx.h
```

但执行完该命令，再commit的时候发现remote上的该文件被删除。

如果需要继续保留该文件，只是不提交当前的change，比如应用的一些配置文件，初始化的配置文件需要通过remote获取，但不希望个人配置push到remote：

```git
设置文件未做改变
git update-index --assume-unchanged config.ini

取消设置
git update-index --no-assume-unchanged config.ini

附：
git update-index -help
```

### 3. Your branch is ahead of XXX by X commits
```

在本地Commit之后，或者撤销修改后，如果输入git status，提示：

Git Your branch is ahead of 'origin/master' by X commits

如果需要提交到remote
git push origin
or
git push origin <local branch>:refs/for/<remote branch>

以remote端最新节点为准，取消本地所有修改
git reset --hard origin/master

以remote端最新节点为准，保留本地所有修改
git reset origin/master

撤销某次commit提交，取消本地所有修改
git reset --hard commitId

撤销某次commit提交，保留本地所有修改
git reset commitId
```
