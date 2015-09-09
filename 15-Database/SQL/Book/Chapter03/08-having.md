HAVING
===

WHERE 关键字无法与求和函数搭配使用，需要 HAVING 子句来搭配。语法：

```
SELECT column_name, aggregate_function(column_name)
FROM table_name
WHERE column_name operator value
GROUP BY column_name
HAVING aggregate_function(column_name) operator value
```

### 示例

```
mysql> SELECT * FROM orders;
+----+--------+-----------+-------+
| id | number | id_people | price |
+----+--------+-----------+-------+
|  1 |    100 |         3 |    20 |
|  2 |    200 |         3 |    20 |
|  3 |    300 |         1 |    30 |
|  4 |    400 |         1 |    30 |
|  5 |    500 |         9 |    30 |
|  6 |    600 |         8 |    60 |
+----+--------+-----------+-------+
6 rows in set (0.01 sec)

mysql> SELECT id_people, SUM(number) FROM orders GROUP BY id_people
    -> HAVING SUM(number) < 600;
+-----------+-------------+
| id_people | SUM(number) |
+-----------+-------------+
|         3 |         300 |
|         9 |         500 |
+-----------+-------------+
2 rows in set (0.00 sec)

mysql> SELECT id_people, SUM(number) FROM orders
    -> WHERE id_people = 3 OR id_people = 9
    -> GROUP BY id_people
    -> HAVING SUM(number) < 500;
+-----------+-------------+
| id_people | SUM(number) |
+-----------+-------------+
|         3 |         300 |
+-----------+-------------+
1 row in set (0.00 sec)

```
