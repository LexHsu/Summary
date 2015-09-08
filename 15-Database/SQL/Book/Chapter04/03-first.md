FIRST()
===

FIRST() 函数返回指定的字段中第一个记录的值。可使用 ORDER BY 语句对记录进行排序。语法:

```
SELECT FIRST(column_name) FROM table_name
```

MySQL 不支持 FIRST()函数，可通过如下方式：

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
5 rows in set (0.00 sec)

mysql> SELECT number FROM orders LIMIT 1;
+--------+
| number |
+--------+
|    100 |
+--------+
1 row in set (0.00 sec)

mysql> SELECT number FROM orders WHERE id = 1;
+--------+
| number |
+--------+
|    100 |
+--------+
1 row in set (0.00 sec)

```

