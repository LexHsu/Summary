UCASE() LCASE()
===

### UCASE() 函数

UCASE 函数把字段的值转换为大写。语法：

```
SELECT UCASE(column_name) FROM table_name
```

### LCASE() 函数
LCASE 函数把字段的值转换为小写。语法：

```
SELECT LCASE(column_name) FROM table_name
```

###示例

```
mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+
3 rows in set (0.07 sec)

mysql> SELECT UCASE(last_name) FROM people;
+------------------+
| UCASE(last_name) |
+------------------+
| ADA              |
| BUSH             |
| CARTER           |
+------------------+
3 rows in set (0.03 sec)

mysql> SELECT lcase(address) FROM people;
+----------------+
| lcase(address) |
+----------------+
| oxford street  |
| fifth avenue   |
| changan street |
+----------------+
3 rows in set (0.00 sec)

mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+
3 rows in set (0.00 sec)
```
