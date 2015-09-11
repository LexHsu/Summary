LENGTH()
===

LENGTH() 函数返回文本字段中值的长度。语法：

```
SELECT LEN(column_name) FROM table_name
```

### 示例

```
mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+
3 rows in set (0.00 sec)

mysql> SELECT LENGTH(city) FROM people;
+--------------+
| LENGTH(city) |
+--------------+
|            6 |
|            8 |
|            7 |
+--------------+
3 rows in set (0.00 sec)
```
