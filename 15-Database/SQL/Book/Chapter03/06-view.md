VIEW
===

在 SQL 中，视图是基于 SQL 语句的结果集的可视化的表。视图包含行和列，就像一个真实的表。视图中的字段就是来自一个或多个数据库中的真实的表中的字段。
可以向视图添加 SQL 函数、WHERE 以及 JOIN 语句，也可以提交数据，就像这些来自于某个单一的表。
注：数据库的设计和结构不会受到视图中的函数、where 或 join 语句的影响。

### SQL 创建视图
```
CREATE VIEW view_name AS
SELECT column_name(s)
FROM table_name
WHERE condition

注：视图总是显示最近的数据。每当用户查询视图时，数据库引擎通过使用 SQL 语句来重建数据。

示例，创建一个名为 address_view 的视图，包括 address，city 两列：

mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+
3 rows in set (0.00 sec)

mysql> CREATE VIEW address_view AS SELECT address, city FROM people;
Query OK, 0 rows affected (0.06 sec)

mysql> SELECT * FROM address_view;
+----------------+----------+
| address        | city     |
+----------------+----------+
| Oxford Street  | London   |
| Fifth Avenue   | New York |
| Changan Street | Beijing  |
+----------------+----------+
3 rows in set (0.02 sec)


```

### SQL 更新视图

您可以使用下面的语法来更新视图：
```
CREATE OR REPLACE VIEW view_name AS
SELECT column_name(s)
FROM table_name
WHERE condition

注：开始误认为CREATE 和 REPLACE 只要使用任意一个关键字即可，其实是需要同时使用。

示例，向 address_view 视图添加  first_name 列：
mysql> CREATE OR REPLACE VIEW address_view AS SELECT address, city, first_name FROM people;
Query OK, 0 rows affected (0.04 sec)

mysql> SELECT * FROM address_view;
+----------------+----------+------------+
| address        | city     | first_name |
+----------------+----------+------------+
| Oxford Street  | London   | John       |
| Fifth Avenue   | New York | George     |
| Changan Street | Beijing  | Thomas     |
+----------------+----------+------------+
3 rows in set (0.00 sec)

```

### SQL 撤销视图

可通过 DROP VIEW 命令删除视图。
```
DROP VIEW view_name
```
