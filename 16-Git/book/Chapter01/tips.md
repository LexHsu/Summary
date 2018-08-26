git tips
===

### 1. You have unstaged changes

执行git pull –-rebase，提示：
```
error: Cannot pull with rebase: You have unstaged changes. 
error: Additionally, your index contains uncommitted changes. 
```
原因：如果有未提交的更改，不能git pull

解决： 
```git
git stash #可用来暂存当前正在进行的工作
git pull –-rebase 
git stash pop #从Git栈中读取最近一次保存的内容
```
如果pop的时候冲突了，可通过git status查看冲突文件，编辑后重新add，commit。

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

撤销某次commit之前的提交，保留本地所有修改，注意是撤销到指定版本号之前的所有提交，不包含该提交
git reset commitId
```

### 4. git pull, git fetch

```
git pull <远程主机名> <远程分支名>:<本地分支名>
如取回远程主机origin的branch1分支与本地的master分支合并：
git pull origin branch1:master
如果本地分支和远程分支建立了追踪关系（git branch --set-upstream master origin/branch1），可省略为：
git pull origin

git fetch

git fetch <远程主机名>/<本地分支名>
如取回远程主机origin上的branch1分支
git fetch origin/branch1
如果需要在当前分支上合并fetch回来的分支：
git merge origin/branch1
or
git rebase orgin/branch1

可见区别在与：git pull是拉下更新后就自动合并本地分支，而git fetch是先吧更新拉下来，在用merge或rebase进行合并。
结果上可见简单理解git pull = git fetch + git merge，但实现上并非如此
```

### 5. Your change could not be merged due to a path conflict

```
git branch                               # 查看分支情况  
git checkout branch1                     # 选择分支dev   
git fetch origin/branch                  # 注意fetch与pull区别
git rebase origin/branch1                # 手工解决有冲突的地方
git add .                            
git rebase --continue
git push origin HEAD:refs/for/dev        # 或使用 repo upload .

```

### git大小写敏感
```
git config core.ignorecase false
```
