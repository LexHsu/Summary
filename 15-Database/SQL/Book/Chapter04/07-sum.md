SUM()
===

SUM 函数返回数值列的总数（总额）。语法：
```
SELECT SUM(column_name) FROM table_name
```

### 示例

```
mysql> SELECT * FROM orders;
+----+--------+-----------+
| id | number | id_people |
+----+--------+-----------+
|  1 |    100 |         3 |
|  2 |    200 |         3 |
|  3 |    300 |         1 |
|  4 |    400 |         1 |
|  5 |    500 |         9 |
+----+--------+-----------+
5 rows in set (0.04 sec)

mysql> SELECT SUM(number) FROM orders;
+-------------+
| SUM(number) |
+-------------+
|        1500 |
+-------------+
1 row in set (0.00 sec)

```
