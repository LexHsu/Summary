refs
===

通过gerrit 进行code review时，常会遇到refs：

### 1. refs/for 和 refs/heads

refs/for表示提交代码需要经过code review之后才能merge，而refs/heads不需要。

```
$git push origin HEAD:refs/for/mybranch

refs/for/mybranch需要经过code review之后才可以提交；

refs/heads/mybranch不需要code review。
```

### 2.refs/for 和 refs/changes
```
向refs/for/<branch-name>命名空间下推送新的提交会分配一个review-id，
并为该review-id的访问建立如下格式的引用refs/changes/nn/<review-id>/m，其中： 

review-id是 Gerrit为评审任务顺序而分配的全局唯一的号码。

nn为review-id的后两位数，位数不足用零补齐。即nn为review-id除以100的余数。

m为修订号，该review-id的首次提交修订号为1，如果该修订被打回，重新提交修订号会自增。
```

### Gerrit server上的project 下的refs目录内容：

```
$ ls refs/

changes  heads  meta  notes  tags  users

$ ls refs/changes/

02  05  06  07  08  09  10  11  12  13  14  15  16  17  18  19  20  21  22  23  60

查看review-id

$ cat refs/changes/02/2/1 

a29ef399448a1f762506964a52ef5bff7012c39c

这里review-id为2，其nn为后两位数，因为不足用0补齐，所以为02，修订号m为1

$ cat  refs/changes/60/60/1 

28bf746fe8a4e2c2d03380ffa111a42dc4959b2e

 这里review-id为60，其nn为后两位数，无需补齐，所以为60，修订号m为1



其中a29ef399448a1f762506964a52ef5bff7012c39c为已经 abandoned提交，28bf746fe8a4e2c2d03380ffa111a42dc4959b2e为最近一次提交，在网页上都可以查到。

3.refs/tags

这个是打tag用的，当然提交tag之前需要给用户组配置tag权限，啰嗦两句：

1）如果需要推送轻量级标签(不带注释), 给refs/tags/*命名空间赋予Create Reference权限, 轻量级标签就像Git中的分支一样；

2）如果需要删除或者重写标签, 给refs/tags/*命名空间赋予带force选项的Push权限,删除标签需要和删除分支一样的权限；

3）标签的email一定会与提交者的邮箱进行验证，如果推送其他人的标签需要同时赋予Push Annotated Tag和Forge Committer Identity权限.

添加一个tag

$ git tag -a v0.1 -m " version 0.1" // 添加一个带注释的tag

$ git tag                           // 查看tag

v0.1

$ git push  origin --tags           // 将tag提交到远程

$ git show v0.1                     // 查看tag 信息

tag v0.1

Tagger: wang_chunai <wang_chunai@*******.com>

Date:   Mon Jun 19 10:28:50 2017 +0800

 

0.1版本

服务器上和本地的tag目录下查看commit信息，信息相同

服务器上：
$ cat refs/tags/v0.1 
ef64aed96a0a6cfa2bf9d6dcf01de3fee7d5c312
本地仓库：
$ cat .git/refs/tags/v0.1
ef64aed96a0a6cfa2bf9d6dcf01de3fee7d5c312

本地查看远程commit
$ git ls-remote origin

28bf746fe8a4e2c2d03380ffa111a42dc4959b2e        HEAD

a29ef399448a1f762506964a52ef5bff7012c39c        refs/changes/02/2/1

33e87492a8b6bd6733ddbff033dc7cba7277f05a        refs/changes/05/5/1

42aa02e8b390433ae6e905a23128c7c0f3037d9c        refs/changes/06/6/1

ffdbec854e3aa03e9b1144e5832e5f9258f40e05        refs/changes/07/7/1

a84cada069126991a2dacdce8b60de5c029152e2        refs/changes/08/8/1

3e006bdbf60075b8bb2cacb8f46521d0593c9025        refs/changes/09/9/1

7740f34e71ccee8ffe3bc1a56a9301c42c19f80e        refs/changes/10/10/1

47b1bda1c00c8ec0280932efc54096f3a11abeb4        refs/changes/11/11/1

4e4f5e0482c85f46f4a2c6fa4083da26c4b6e571        refs/changes/12/12/1

96a42295059c685bf710bf6fc8e28757fe22b79b        refs/changes/13/13/1

bf06e38e5b0fc2ceaa3ed5a9b3ac8f64a0e78605        refs/changes/14/14/1

59039ea9c6b1223e0b85162ccaa5deb43fe55ce9        refs/changes/15/15/1

14c7051a6eb2b0a670450840191569f0582b374b        refs/changes/16/16/1

64593454f9fd8450c753abc69ffb8268b3c429b7        refs/changes/16/16/2

55e00044539447ed2cb15a50ee92ef054399cbdc        refs/changes/17/17/1

8b0493c94694bf96b944dc95c86201e91129bd97        refs/changes/18/18/1

c8614cad9950244bdfd29f75a3a47dffadbf0709        refs/changes/19/19/1

f443a551b184bc80fc6c45cd6a2edaf4ad042297        refs/changes/20/20/1

cdc97ca1984ca26ab3e99123818612aa72c669a1        refs/changes/21/21/1

7cb096695faed2dfdf7a00366c06f6cc4b2ef690        refs/changes/22/22/1

c31f1afaed312d8c13868823558b5f15aad7f816        refs/changes/23/23/1

28bf746fe8a4e2c2d03380ffa111a42dc4959b2e        refs/changes/60/60/1

28bf746fe8a4e2c2d03380ffa111a42dc4959b2e        refs/heads/dev

33e87492a8b6bd6733ddbff033dc7cba7277f05a        refs/heads/master

7740f34e71ccee8ffe3bc1a56a9301c42c19f80e        refs/heads/release

cdb225b3f3c7f23e2b9cf2f81e19e941ea53ad41        refs/notes/review

ef64aed96a0a6cfa2bf9d6dcf01de3fee7d5c312        refs/tags/v0.1

c31f1afaed312d8c13868823558b5f15aad7f816        refs/tags/v0.1^{}

从最后两条记录可以看出，tag v0.1的commit 为ef64aed96a0a6cfa2bf9d6dcf01de3fee7d5c312       ，是基于commitc31f1afaed312d8c13868823558b5f15aad7f816    上所打的tag
```