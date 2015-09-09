GROUP BY
===

求和函数 (SUM) 常会见到 GROUP BY 语句。GROUP BY 语句即用于结合求和函数，根据一个或多个列对结果集进行分组。语法：
```
SELECT column_name, aggregate_function(column_name)
FROM table_name
WHERE column_name operator value
GROUP BY column_name
```

### 支持多个列

```
SELECT column_name1, column_name2, aggregate_function(column_name)
FROM tabe_name
WHERE column_name operator value
GROUP BY column_name1, column_name2
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
6 rows in set (0.00 sec)

mysql> SELECT id_people, SUM(number) FROM orders;
+-----------+-------------+
| id_people | SUM(number) |
+-----------+-------------+
|         3 |        2100 |
+-----------+-------------+
1 row in set (0.00 sec)

mysql> SELECT id_people, SUM(number) FROM orders GROUP BY id_people;
+-----------+-------------+
| id_people | SUM(number) |
+-----------+-------------+
|         1 |         700 |
|         3 |         300 |
|         8 |         600 |
|         9 |         500 |
+-----------+-------------+
4 rows in set (0.00 sec)

mysql> SELECT SUM(number) FROM orders GROUP BY id_people;
+-------------+
| SUM(number) |
+-------------+
|         700 |
|         300 |
|         600 |
|         500 |
+-------------+
4 rows in set (0.00 sec)

mysql> SELECT id_people, price, SUM(number) FROM orders GROUP BY id_people, price;
+-----------+-------+-------------+
| id_people | price | SUM(number) |
+-----------+-------+-------------+
|         1 |    30 |         700 |
|         3 |    20 |         300 |
|         8 |    60 |         600 |
|         9 |    30 |         500 |
+-----------+-------+-------------+
4 rows in set (0.00 sec)
```
