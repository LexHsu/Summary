TABLE
===

##### 创建表

##### MySQL 命令行创建表

```
CREATE TABLE 表名称(列声明);
```

括号内声明列的名称以及该列的数据类型，列与列之间用逗号隔开。示例：

```
mysql> CREATE TABLE students
    -> (
    -> id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -> name char(8) NOT NULL,
    -> age tinyint unsigned NOT NULL,
    -> tel char(13) NULL DEFAULT "-"
    -> );
Query OK, 0 rows affected (0.12 sec)

```

##### 通过 SQL 文件创建表

```
CREATE TABLE students
(
    id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name char(8) NOT NULL,
    age tinyint unsigned NOT NULL,
    tel char(13) NULL DEFAULT "-"
);
```

上述语句保存为 tbl.sql：

```
1. 在系统命令行输入:
mysql -D samp_db -u root -p < tbl.sql

2. 在 mysql 命令行输入：
source tbl.sql;
```

注意：

1. 如果连接远程主机，加上 -h 指令。
2. tbl.sql 文件若不在当前工作目录下需指定文件的完整路径。
3. 使用 show tables; 命令可查看已创建了表的名称。
4. 使用 describe 表名; 命令可查看已创建的表的详细信息。

### 添加列

```
ALTER TABLE 表名 ADD 列名 列数据类型 [AFTER 插入位置];

在表的最后追加列 address:
ALTER TABLE students ADD address char(60);

在名为 age 的列后插入列 birthday:
ALTER TABLE students ADD birthday date AFTER age;
```

### 修改列

```
ALTER TABLE 表名 CHANGE 列名称 列新名称 新数据类型;

将表 tel 列改名为 telphone:
ALTER TABLE students CHANGE tel telphone char(13) default "-";

将 name 列的数据类型改为 char(16):
ALTER TABLE students CHANGE name name char(16) not null;
```

### 删除列

```
ALTER TABLE 表名 DROP 列名称;

删除 birthday 列:
ALTER TABLE students DROP birthday;
```

### 重命名表

```
ALTER TABLE 表名 RENAME 新表名;

重命名 students 表为 workmates:
ALTER TABLE students RENAME workmates;
```

##### 删除整张表

```
DROP TABLE 表名;

删除 workmates 表:
DROP TABLE workmates;
```
