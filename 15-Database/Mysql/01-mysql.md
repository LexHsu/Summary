MySQL
===

### MySQL 数据类型

MySQL 有三大类数据类型：数字、日期、字符串,细分如下:

##### 1.数字类型

- 整数: tinyint、smallint、mediumint、int、bigint
- 浮点数: float、double、real、decimal

##### 2.日期和时间

- date、time、datetime、timestamp、year

##### 3.字符串类型

- 字符串: char、varchar
- 文本: tinytext、text、mediumtext、longtext
- 二进制(可存储图片、音乐等): tinyblob、blob、mediumblob、longblob

详细介绍参见: [MySQL数据类型](http://www.cnblogs.com/zbseoag/archive/2013/03/19/2970004.html)

### 使用 MySQL

##### 1.登录 MySQL

命令行输入如下命令：

```
mysql -h 主机名 -u 用户名 -p

-h : MySQL 主机名, 若登录当前机器该参数可以省略;
-u : 要登录的用户名;
-p : 若登录的用户名密码为空, 可以忽略此选项。

例：
mysql -u root -p 
按回车确认，提示如下：
Enter password:
若密码存在, 输入密码登录, 不存在则直接按回车登录。
```

##### 2.创建数据库

```
create database 数据库名 [其他选项];

例:
create database samp_db character set gbk;

Query OK, 1 row affected(0.02 sec)
```

1. MySQL 语句以分号作为语句结束。
2. 可使用 `show databases;` 已经创建的数据库。

##### 3.选择所要操作的数据库

要对操作一个数据库, 必须先选择该数据库, 否则会提示错误:
`ERROR 1046(3D000): No database selected`。

```
选择数据库有两种方式，语句结尾可不加分号:

1. 在登录数据库时指定: 
mysql -D 数据库名 -h 主机名 -u 用户名 -p;
mysql -D mydb -u root -p;

2. 在登录后使用 use 语句指定: 
use 数据库名;
use mydb;
```

##### 4.创建表

```
create table 表名称(列声明);
```

括号内声明列的名称以及该列的数据类型，列与列之间用逗号隔开。示例：

```
create table students
(
    id int unsigned not null auto_increment primary key,
    name char(8) not null,
    sex char(4) not null,
    age tinyint unsigned not null,
    tel char(13) null default "-"
);
```
创建 students 表, 表中存放学号(id)、姓名(name)、性别(sex)、年龄(age)、联系电话(tel):
`id int unsigned not null auto_increment primary key` 表示：

1. id：列的名称;
2. int：指定该列的类型为 int(取值范围为 -8388608到8388607), 在后面我们又用 "unsigned" 加以修饰, 表示该类型为无符号型, 此时该列的取值范围为 0 到 16777215;
3. not null：说明该列的值不能为空, 必须要填, 如果不指定该属性, 默认可为空;
4. auto_increment：需在整数列中使用, 其作用是在插入数据时若该列为 NULL, MySQL 将自动产生一个比现存值更大的唯一标识符值。在每张表中仅能有一个这样的值且所在列必须为索引列。
5. primary key：表示该列是表的主键, 本列的值必须唯一, MySQL 将自动索引该列。

char(8) 表示存储的字符长度为 8, tinyint 的取值范围为 -127 到 128, default 属性指定当该列值为空时的默认值。

### 执行 SQL 文件

上述语句也可保存为 createtable.sql 的文件，在命令行输入:

```
mysql -D samp_db -u root -p < createtable.sql
```

注意：

1. 如果连接远程主机，加上 -h 指令。
2. createtable.sql 文件若不在当前工作目录下需指定文件的完整路径。
3. 使用 show tables; 命令可查看已创建了表的名称。
4. 使用 describe 表名; 命令可查看已创建的表的详细信息。


### 操作 MySQL 数据库

##### 向表中插入数据

insert 语句可以用来将一行或多行数据插到数据库表中, 使用的一般形式如下:

```
insert [into] 表名 [(列名1, 列名2, 列名3, ...)] values (值1, 值2, 值3, ...);

[] 中的内容可选, 如给 samp_db 数据库中的 students 表插入一条记录:

insert into students values(NULL, "ligang", "男", 20, "13811371377");
```

按回车键确认后若提示 Query Ok, 1 row affected (0.05 sec) 表示数据插入成功。
若插入失败请检查是否已选择需要操作的数据库。有时只需要插入部分数据, 或者不按照列的顺序进行插入:

```
insert into students (name, sex, age) values("孙丽华", "女", 21);
```

##### 查询表中的数据

select 语句常用来根据一定的查询规则到数据库中获取数据, 其基本的用法为:

select 列名称 from 表名称 [查询条件];

例如要查询 students 表中所有学生的名字和年龄, 输入语句 select name, age from students; 执行结果如下:

```
mysql> select name, age from students;
+--------+-----+
| name   | age |
+--------+-----+
| 王刚   |  20 |
| 孙丽华 |  21 |
| 王永恒 |  23 |
| 郑俊杰 |  19 |
| 陈芳   |  22 |
| 张伟朋 |  21 |
+--------+-----+
6 rows in set (0.00 sec)

mysql>
```

也可以使用通配符 `*` 查询表中所有的内容, 语句: `select * from students;`

##### 按特定条件查询

where 关键词用于指定查询条件, 用法形式为: select 列名称 from 表名称 where 条件;

以查询所有性别为女的信息为例, 输入查询语句: `select * from students where sex = "女";`

where 子句不仅仅支持 "where 列名 = 值" 这种名等于值的查询形式, 对一般的比较运算的运算符都是支持的,
例如 `=、>、<、>=、<、!=` 以及一些扩展运算符 is [not] null、in、like 等等。
还可以对查询条件使用 or 和 and 进行组合查询。示例:

```
查询年龄在21岁以上的所有人信息:
select * from students where age > 21;

查询名字中带有 "王" 字的所有人信息:
select * from students where name like "%王%";

查询id小于 5 且年龄大于20的所有人信息:
select * from students where id < 5 and age > 20;
```

##### 更新表中的数据

update 语句可用来修改表中的数据, 基本的使用形式为:

update 表名称 set 列名称=新值 where 更新条件;

示例：

```
将 id 为 5 的手机号改为默认的"-":
update students set tel=default where id=5;

将所有人的年龄增加 1:
update students set age=age+1;

将手机号为 13288097888 的姓名改为 "张伟鹏", 年龄改为 19:
update students set name="张伟鹏", age = 19 where tel = "13288097888";
```

##### 删除表中的数据

delete 语句用于删除表中的数据, 基本用法为:

delete from 表名称 where 删除条件;

示例:

```
删除 id 为 2 的行: delete from students where id=2;

删除所有年龄小于 21 岁的数据: delete from students where age<20;

删除表中的所有数据: delete from students;
```

### 创建后表的修改

##### 1.添加列

```
基本形式: alter table 表名 add 列名 列数据类型 [after 插入位置];

示例:

在表的最后追加列 address: alter table students add address char(60);

在名为 age 的列后插入列 birthday: alter table students add birthday date after age;
```

##### 2.修改列

```
基本形式: alter table 表名 change 列名称 列新名称 新数据类型;

示例:

将表 tel 列改名为 telphone: alter table students change tel telphone char(13) default "-";

将 name 列的数据类型改为 char(16): alter table students change name name char(16) not null;
```

##### 3.删除列

```
基本形式: alter table 表名 drop 列名称;

示例:

删除 birthday 列: alter table students drop birthday;
```

##### 4.重命名表

```
基本形式: alter table 表名 rename 新表名;

示例:

重命名 students 表为 workmates: alter table students rename workmates;
```

##### 5.删除整张表

```
基本形式: drop table 表名;

示例: 删除 workmates 表: drop table workmates;
```

##### 6.删除整个数据库

```
基本形式: drop database 数据库名;

示例: 删除 samp_db 数据库: drop database samp_db;
```
