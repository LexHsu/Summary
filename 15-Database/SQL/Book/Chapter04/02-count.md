COUNT()
===

COUNT() 函数返回匹配指定条件的行数。语法：

```
1. COUNT(column_name) 函数返回指定列的值的数目（NULL 不计入）：
SELECT COUNT(column_name) FROM table_name

2. COUNT(*) 函数返回表中的记录数：
SELECT COUNT(*) FROM table_name

3. COUNT(DISTINCT column_name) 函数返回指定列的不同值的数目：
SELECT COUNT(DISTINCT column_name) FROM table_name

注：COUNT(DISTINCT) 适用于 MySQL，ORACLE 和 Microsoft SQL Server，但是无法用于 Microsoft Access。
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
5 rows in set (0.00 sec)

mysql> SELECT COUNT(number) AS orderNumber FROM orders WHERE id_people = 3;
+-------------+
| orderNumber |
+-------------+
|           2 |
+-------------+
1 row in set (0.01 sec)

mysql> SELECT COUNT(*) AS orderNumber FROM orders;
+-------------+
| orderNumber |
+-------------+
|           5 |
+-------------+
1 row in set (0.00 sec)

mysql> SELECT COUNT(DISTINCT id_people) AS order_id_people FROM orders;
+-----------------+
| order_id_people |
+-----------------+
|               3 |
+-----------------+
1 row in set (0.00 sec)
```
