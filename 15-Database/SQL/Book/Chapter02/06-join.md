JOIN
===

JOIN 用于根据两个或多个表中的列之间的关系，从这些表中查询数据。

### 示例

```
people 表中的 id 映射在 orders 表中的 id_people。

mysql> SELECT * FROM people;
+----+-----------+------------+------------+----------+
| id | last_name | first_name | address    | city     |
+----+-----------+------------+------------+----------+
|  1 | Ada       | John       | Oxford Str | London   |
|  2 | Bush      | George     | Fifth Aven | New York |
|  3 | Carter    | Thomas     | Changan St | Beijing  |
+----+-----------+------------+------------+----------+
3 rows in set (0.00 sec)

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

mysql> SELECT people.last_name, people.first_name, orders.number FROM people, orders WHERE people.id = orders.id_people;
+-----------+------------+--------+
| last_name | first_name | number |
+-----------+------------+--------+
| Carter    | Thomas     |    100 |
| Carter    | Thomas     |    200 |
| Ada       | John       |    300 |
| Ada       | John       |    400 |
+-----------+------------+--------+
4 rows in set (0.00 sec)

mysql> SELECT people.last_name, people.first_name, orders.number FROM people INNER JOIN orders ON people.id = orders.id_people ORDER BY people.last_name;
+-----------+------------+--------+
| last_name | first_name | number |
+-----------+------------+--------+
| Ada       | John       |    300 |
| Ada       | John       |    400 |
| Carter    | Thomas     |    100 |
| Carter    | Thomas     |    200 |
+-----------+------------+--------+
4 rows in set (0.01 sec)
```

### 不同的 SQL JOIN
除了上例子中使用的 INNER JOIN（内连接），还可以使用其他几种连接。
1. JOIN: 如果表中有至少一个匹配，则返回行
2. LEFT JOIN: 即使右表中没有匹配，也从左表返回所有的行
3. RIGHT JOIN: 即使左表中没有匹配，也从右表返回所有的行
4. FULL JOIN: 只要其中一个表中存在匹配，就返回行
