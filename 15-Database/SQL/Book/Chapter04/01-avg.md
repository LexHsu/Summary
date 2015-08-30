AVG()
===


AVG 函数返回数值列的平均值。NULL 值不包括在计算中。语法：

```
SELECT AVG(column_name) FROM table_name
```

### 示例

```
mysql> select * FROM orders;
+----+--------+-----------+
| id | number | id_people |
+----+--------+-----------+
|  1 |    100 |         3 |
|  2 |    200 |         3 |
|  3 |    300 |         1 |
|  4 |    400 |         1 |
|  5 |    500 |         9 |
+----+--------+-----------+
5 rows in set (0.00 sec)

mysql> SELECT AVG(number) FROM orders;
+-------------+
| AVG(number) |
+-------------+
|    300.0000 |
+-------------+
1 row in set (0.00 sec)

mysql> SELECT number FROM orders WHERE number > (SELECT AVG(number) FROM orders);
+--------+
| number |
+--------+
|    400 |
|    500 |
+--------+
2 rows in set (0.01 sec)
```
