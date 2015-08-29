SELECT INTO
===

SELECT INTO 语句从一个表中选取数据，然后把数据插入另一个表中。常用于创建表的备份复件或者用于对记录进行存档。语法：

```
可以把所有的列插入新表：
SELECT *
INTO new_table_name [IN externaldatabase]
FROM old_tablename

或者只把希望的列插入新表：
SELECT column_name(s)
INTO new_table_name [IN externaldatabase]
FROM old_tablename
```

注：MySQL 不支持 SELECT INTO 语句，替代方法如下：
```
MYSQL不支持:
SELECT * INTO new_table_name FROM old_table_name;
替代方法:
CREATE TABLE new_table_name (SELECT * FROM old_table_name);
```

### 示例

```
1. 制作 people 表的备份：
mysql> CREATE TABLE people_bak (SELECT * FROM people);
Query OK, 3 rows affected (0.07 sec)
Records: 3  Duplicates: 0  Warnings: 0
mysql> show tables;
+----------------+
| Tables_in_mydb |
+----------------+
| orders         |
| people         |
| people_bak     |
| students       |
| table1         |
| table2         |
+----------------+
6 rows in set (0.00 sec)
```
