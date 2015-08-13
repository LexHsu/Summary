TABLE
===

##### 创建表

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

##### 通过 SQL 文件执行

```
mysql> CREATE TABLE students
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
