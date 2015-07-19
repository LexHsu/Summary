SQL
===

### 一、基础

```
1、创建数据库
CREATE DATABASE dbname

2、删除数据库
DROP DATABASE dbname

3、备份数据库
备份 MySQL 数据库，不带删除表的格式
mysqldump -h hostname -u username -p password dbname > bak.sql

备份 MySQL 数据库，带删除表的格式，还原时直接覆盖已有数据库，不需要再手动删除原有数据库
mysqldump -–add-drop-table -u username -p password dbname > bak.sql

直接将 MySQL 数据库压缩备份
mysqldump -h hostname -u username -p password dbname | gzip > bak.sql.gz

备份 MySQL 数据库某个(些)表
mysqldump -h hostname -u username -p password dbname table1 table2 > bak.sql

同时备份多个 MySQL 数据库
mysqldump -h hostname -u username -p password –databases dbname1 dbname2 dbname3 > bak.sql

仅仅备份数据库结构
mysqldump –no-data –databases dbname1 dbname2 dbname3 > bak.sql

备份服务器上所有数据库
mysqldump –all-databases > bak.sql

还原 MySQL 数据库的命令
mysql -h hostname -u username -p password dbname < bak.sql

还原压缩的 MySQL 数据库
gunzip < bak.sql.gz | mysql -u username -p password dbname

将数据库转移到新服务器
mysqldump -u username -p password dbname | mysql –host=*.*.*.* -C dbname

4、创建新表
create table tabname(col1 type1 [not null] [primary key],col2 type2 [not null],..)

根据已有的表创建新表：
A：create table newtable like oldtable (使用旧表创建新表)
B：create table newtable as SELECT col1,col2… FROM oldtable definition only

5、删除新表
drop table tabname

6、增加一个列
Alter table tabname add column col type
注：列增加后将不能删除。DB2 中列加上后数据类型也不能改变，唯一能改变的是增加 varchar 类型的长度

7、添加主键： Alter table tabname add primary key(col)
删除主键： Alter table tabname drop primary key(col)

8、创建索引：create [unique] index idxname on tabname(col….)
删除索引：drop index idxname
注：索引是不可更改的，想更改必须删除重新建

9、创建视图：create view viewname as SELECT statement
删除视图：drop view viewname

10、几个简单的基本的 SQL 语句
选择：SELECT * FROM table1 WHERE 范围
插入：INSERT INTO table1(field1,field2) values(value1,value2)
删除：DELETE FROM table1 WHERE 范围
更新：UPDATE table1 SET field1=value1 WHERE 范围
查找：SELECT * FROM table1 WHERE field1 like ’%value1%’ ---like的语法很精妙，查资料!
排序：SELECT * FROM table1 ORDER BY field1,field2 [desc]
总数：SELECT COUNT AS totalcount FROM table1
求和：SELECT SUM(field1) AS sumvalue FROM table1
平均：SELECT AVG(field1) AS avgvalue FROM table1

最大：SELECT MAX(field1) AS maxvalue FROM table1
最小：SELECT MIN(field1) AS minvalue FROM table1

11、几个高级查询运算词
A： UNION 运算符
UNION 运算符通过组合其他两个结果表（例如 TABLE1 和 TABLE2）并消去表中任何重复行而派生出一个结果表。
当 ALL 随 UNION 一起使用时（即 UNION ALL），不消除重复行。两种情况下，派生表的每一行不是来自 table1 就是来自 table2
SELECT column_name(s) FROM table1 UNION SELECT column_name(s) FROM table2

B： EXCEPT 运算符
EXCEPT运算符通过包括所有在 TABLE1 中但不在 TABLE2 中的行并消除所有重复行而派生出一个结果表。当 ALL 随 EXCEPT 一起使用时 (EXCEPT ALL)，不消除重复行。

C： INTERSECT 运算符
INTERSECT运算符通过只包括 TABLE1 和 TABLE2 中都有的行并消除所有重复行而派生出一个结果表。当 ALL随 INTERSECT 一起使用时 (INTERSECT ALL)，不消除重复行。

注：使用运算词的几个查询结果行必须是一致的。

13、对数据库进行操作：
   分离数据库： sp_detach_db;附加数据库：sp_attach_db 后接表明，附加需要完整的路径名

14.如何修改数据库的名称:
sp_renamedb 'old_name', 'new_name'

1、复制表
将 mydb 数据库中的 oldtable 表复制为 newtable：

CREATE TABLE newtable LIKE mydb.oldtable;
INSERT newtable SELECT * FROM mydb.oldtable;

复制整个表
CREATE TABLE new_table SELECT * FROM old_table;

复制,不复制数据
CREATE TABLE new_table SELECT * FROM old_table where 0;
注意：本方案其实只是把 select 语句的结果建一个表，所以 newtable 这个表不会有主键、索引。


8、between的用法,between限制查询数据范围时包括了边界值,not between不包括
select * from table1 where time between time1 and time2
select a,b,c, from table1 where a not between 数值1 and 数值2

9、in 的使用方法
select * from table1 where a [not] in (‘value1’,’value2’,’value3,’value4’)

20、列出数据库里所有的表名
select name from sysobjects where type='U' // U代表用户

21、列出表里的所有的列名
select name from syscolumns where id=object_id('TableName')


```
