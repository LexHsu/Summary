JOIN
===

JOIN 用于根据两个或多个表中的列之间的关系，从这些表中查询数据。

### SQL JOIN 分类

1. INNER JOIN: 等同 JOIN，如果表中有至少一个匹配，则返回行
2. LEFT JOIN: 即使右表中没有匹配，也从左表返回所有的行
3. RIGHT JOIN: 即使左表中没有匹配，也从右表返回所有的行
4. FULL JOIN: 只要其中一个表中存在匹配，就返回行

### INNER JOIN 示例

语法：

```
SELECT column_name(s)
FROM table_name1
INNER JOIN table_name2
ON table_name1.column_name=table_name2.column_name
```
INNER JOIN 关键字在表中存在至少一个匹配时返回行。如果 people 表中的行在 orders 表中没有匹配，就不会列出这些行。

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

mysql> SELECT people.last_name, people.first_name, orders.number
    -> FROM people, orders
    -> WHERE people.id = orders.id_people;
+-----------+------------+--------+
| last_name | first_name | number |
+-----------+------------+--------+
| Carter    | Thomas     |    100 |
| Carter    | Thomas     |    200 |
| Ada       | John       |    300 |
| Ada       | John       |    400 |
+-----------+------------+--------+
4 rows in set (0.00 sec)

mysql> SELECT people.last_name, people.first_name, orders.number
    -> FROM people
    -> INNER JOIN orders
    -> ON people.id = orders.id_people
    -> ORDER BY people.last_name;
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

### LEFT JOIN 示例

语法：

```
SELECT column_name(s)
FROM table_name1
LEFT JOIN table_name2
ON table_name1.column_name=table_name2.column_name
```

注释：在某些数据库中， LEFT JOIN 称为 LEFT OUTER JOIN。

列出所有的人，以及他们的订购:

```
mysql> SELECT people.last_name, people.first_name, orders.number
    -> FROM people
    -> LEFT JOIN orders
    -> ON people.id = orders.id_people
    -> ORDER BY people.last_name;
+-----------+------------+--------+
| last_name | first_name | number |
+-----------+------------+--------+
| Ada       | John       |    300 |
| Ada       | John       |    400 |
| Bush      | George     |   NULL |
| Carter    | Thomas     |    100 |
| Carter    | Thomas     |    200 |
+-----------+------------+--------+
5 rows in set (0.05 sec)
```

### RIGHT JOIN 示例

RIGHT JOIN 关键字会右表 (table_name2) 那里返回所有的行，即使在左表 (table_name1) 中没有匹配的行。

语法：

```
SELECT column_name(s)
FROM table_name1
RIGHT JOIN table_name2
ON table_name1.column_name=table_name2.column_name
```

注释：在某些数据库中， RIGHT JOIN 称为 RIGHT OUTER JOIN。

列出所有的定单，以及定购它们的人

```
mysql> SELECT people.last_name, people.first_name, orders.number
    -> FROM people
    -> RIGHT JOIN orders
    -> ON people.id = orders.id_people
    -> ORDER BY people.last_name;
+-----------+------------+--------+
| last_name | first_name | number |
+-----------+------------+--------+
| NULL      | NULL       |    500 |
| Ada       | John       |    300 |
| Ada       | John       |    400 |
| Carter    | Thomas     |    100 |
| Carter    | Thomas     |    200 |
+-----------+------------+--------+
5 rows in set (0.01 sec)
```

### FULL JOIN 示例

只要其中某个表存在匹配，FULL JOIN 关键字就会返回行。语法：
FULL JOIN 关键字会从左表 (people) 和右表 (orders) 那里返回所有的行。
如果 people 中的行在表 orders 中没有匹配，或者如果 orders 中的行在表 people 中没有匹配，这些行同样会列出。

```
SELECT column_name(s)
FROM table_name1
FULL JOIN table_name2
ON table_name1.column_name = table_name2.column_name
```

注释：在某些数据库中， FULL JOIN 称为 FULL OUTER JOIN。

列出所有的人，以及他们的定单，以及所有的定单，以及定购它们的人:

```
mysql> SELECT people.last_name, people.first_name, orders.number
    -> FROM people
    -> FULL JOIN orders
    -> ON people.id = orders.id_people
    -> ORDER BY people.last_name;
ERROR 1054 (42S22): Unknown column 'people.last_name' in 'field list'

注：MySQL 不支持 FULL JOIN，替代方法如下：

mysql> SELECT people.last_name, people.first_name, orders.number
    -> FROM people
    -> LEFT JOIN orders
    -> ON people.id = orders.id_people
    -> UNION
    -> SELECT people.last_name, people.first_name, orders.number
    -> FROM people
    -> RIGHT JOIN orders
    -> ON people.id = orders.id_people
+-----------+------------+--------+
| last_name | first_name | number |
+-----------+------------+--------+
| Ada       | John       |    300 |
| Ada       | John       |    400 |
| Bush      | George     |   NULL |
| Carter    | Thomas     |    100 |
| Carter    | Thomas     |    200 |
| NULL      | NULL       |    500 |
+-----------+------------+--------+
6 rows in set (0.02 sec)
```
