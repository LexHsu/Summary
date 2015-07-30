DELETE
===

DELETE 语句用于删除表中的行。语法:
```
删除某一行的数据：
DELETE FROM table_name WHERE column_name = value;

删除所有行的数据，表的结构、属性和索引不会删除。
DELETE FROM table_name;
或：
DELETE * FROM table_name;
```

### 示例

```
删除某行的数据
mysql> SELECT * FROM people;
+----+-----------+------------+----------------+---------+
| id | last_name | first_name | address        | city    |
+----+-----------+------------+----------------+---------+
|  1 | Fred      | John       | Oxford Street  | London  |
|  2 | Bush      | George     | Zhongshan Road | Nanjing |
+----+-----------+------------+----------------+---------+
2 rows in set (0.00 sec)

mysql> DELETE FROM people WHERE first_name = 'John';
Query OK, 1 row affected (0.07 sec)

mysql> SELECT * FROM people;
+----+-----------+------------+----------------+---------+
| id | last_name | first_name | address        | city    |
+----+-----------+------------+----------------+---------+
|  2 | Bush      | George     | Zhongshan Road | Nanjing |
+----+-----------+------------+----------------+---------+


删除所有行的数据
mysql> DELETE FROM people;
Query OK, 1 row affected (0.05 sec)

mysql> SELECT * FROM people;
Empty set (0.00 sec)

```
