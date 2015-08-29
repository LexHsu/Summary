SELECT
===

SELECT 语句用于从表中选取出数据，存储在一个结果表（结果集）中。

### SELECT 语法

常见语法：
```
SELECT 列名称 FROM 表名称
或
SELECT * FROM 表名称
```

更复杂的语法如下，后续会详细介绍：
```
SELECT 列名或表达式
FROM 表名或视图名
[WHERE 行条件表达式]
[GROUP BY 列名]
[HAVING 组条件表达式]
[ORDER BY 列名 [ASC | DESC]]
```


### 实例

```
表名：people

mysql> SELECT * FROM people;
Empty set (0.05 sec)

mysql> INSERT INTO people VALUeS (1, "Ada", "John", "Oxford Street", "London");
Query OK, 1 row affected (0.07 sec)

mysql> INSERT INTO people VALUeS (2, "Bush", "George", "Fifth Avenue", "New York");
Query OK, 1 row affected (0.04 sec)

mysql> INSERT INTO people VALUeS (3, "Carter", "Thomas", "Changan Street", "Beijing");
Query OK, 1 row affected (0.04 sec)

mysql> SELECT * FROM people;
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
