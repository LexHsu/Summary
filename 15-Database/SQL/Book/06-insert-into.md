INSERT INTO
===

INSERT INTO 语句用于向表格中插入新的行，语法：

```
INSERT INTO table_name VALUES (value1, value2, ...)
```

也可以指定所要插入数据的列：
```
INSERT INTO table_name (co1, col2,...) VALUES (value1, value2, ...)
```

### 示例：

```
表名：people

+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+


插入新行：
mysql> INSERT INTO people VALUES (4, "Gates", "Bill", "Changan Street", "Beijing");
Query OK, 1 row affected (0.04 sec)

mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
|  4 | Gates     | Bill       | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+


在指定的列中插入数据
mysql> INSERT INTO people (last_name, address) VALUES ("Willson", "London");
Query OK, 1 row affected (0.05 sec)

mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
|  4 | Gates     | Bill       | Changan Street | Beijing  |
|  5 | Willson   | NULL       | London         | NULL     |
+----+-----------+------------+----------------+----------+

```
