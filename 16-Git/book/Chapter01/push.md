git push
===
```
git push <远程主机名> <本地分支名>  <远程分支名> 

如将本地master分支推送到远程主机origin上的master分支
git push origin master:refs/for/master 
或者
git push origin master:master 

如果remote repository不存在名字为master的branch，则新建一个

git push origin HEAD:refs/for/mybranch 
HEAD指向当前工作的branch，master不一定指向当前工作的branch，个人常用HEAD

git push origin master
如果远程分支被省略，如上则表示将本地分支推送到与之存在追踪关系的远程分支（通常两者同名），如果该远程分支不存在，则会被新建，等价于“git push origin master:master”

it push origin ：refs/for/master 
如果省略本地分支名，则表示删除指定的远程分支，因为这等同于推送一个空的本地分支到远程分支，等同于 git push origin --delete master

git push origin
如果当前分支与远程分支存在追踪关系，则本地分支和远程分支都可以省略，将当前分支推送到origin主机的对应分支 

git push
如果当前分支只有一个远程分支，那么主机名都可以省略，形如 git push，可以使用git branch -r查看远程的分支名


git push 的其他命令

git push -u origin master 
如果当前分支与多个主机存在追踪关系，则可以使用 -u 参数指定一个默认主机，
这样后面就可以不加任何参数使用git push，不带任何参数的git push，默认只推送当前分支，这叫做simple方式，
还有一种matching方式，会推送所有有对应的远程分支的本地分支， 
Git 2.0之前默认使用matching，现在改为simple方式
如果想更改设置，可以使用git config命令
git config --global push.default matching OR git config --global push.default simple；
可以使用git config -l 查看配置

git push --all origin
当遇到这种情况就是不管是否存在对应的远程分支，将本地的所有分支都推送到远程主机，这时需要 -all 选项

git push --force origin
git push的时候需要本地先git pull更新到跟服务器版本一致，如果本地版本库比远程服务器上的低，那么一般会提示你git pull更新，如果一定要提交，那么可以使用这个命令。

git push origin --tags
git push 的时候不会推送分支，如果一定要推送标签的话那么可以使用这个命令

git push origin master:refs/changes/123456
之前有push记录，但没合入，可本地修改该changes
```
