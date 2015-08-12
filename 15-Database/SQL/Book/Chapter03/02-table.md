table
===

##### 创建表

```
CREATE TABLE 表名称(列声明);
```

括号内声明列的名称以及该列的数据类型，列与列之间用逗号隔开。示例：

```
CREATE TABLE students
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

##### 2.5 通过 SQL 文件执行

上述语句也可保存为 createtable.sql 的文件，在命令行输入:

```
mysql -D samp_db -u root -p < createtable.sql
```

注意：

1. 如果连接远程主机，加上 -h 指令。
2. createtable.sql 文件若不在当前工作目录下需指定文件的完整路径。
3. 使用 show tables; 命令可查看已创建了表的名称。
4. 使用 describe 表名; 命令可查看已创建的表的详细信息。
