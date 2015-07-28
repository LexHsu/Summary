SELECT
===

SELECT 语句用于从表中选取数据。结果被存储在一个结果表中（称为结果集）。

### SELECT 语法

```
SELECT 列名称 FROM 表名称
或
SELECT * FROM 表名称
```

### 实例

```
表名：people

+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+

查看 last_name 和 first_name 列：

mysql> SELECT last_name, first_name FROM people;
+-----------+------------+
| last_name | first_name |
+-----------+------------+
| Ada       | John       |
| Bush      | George     |
| Carter    | Thomas     |
+-----------+------------+
```
