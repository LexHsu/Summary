MIN()
===

MIN 函数返回一列中的最小值。NULL 值不包括在计算中。语法：
```
SELECT MIN(column_name) FROM table_name
```

注：MIN 和 MAX 也可用于文本列，以获得按字母顺序排列的最高或最低值。

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

mysql> SELECT MIN(number) FROM orders;
+-------------+
| MIN(number) |
+-------------+
|         100 |
+-------------+
1 row in set (0.01 sec)

```
