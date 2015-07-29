AND OR
===

AND 和 OR 可在 WHERE 子语句中把两个或多个条件结合起来。

### 示例

```
表名：people

+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+

使用 AND 显示所有姓为 Ada 并且名为 John 的人：

mysql> SELECT * FROM people WHERE first_name = 'John' AND last_name = 'Ada';
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
+----+-----------+------------+----------------+----------+


使用 OR 显示所有姓为 Carter 或者名为 Thomas 的人：

mysql> SELECT * FROM people WHERE first_name = 'Thomas' OR last_name = 'Bush';
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+


结合 AND 和 OR 运算符

SELECT * FROM people WHERE (first_name = 'Thomas' OR first_name = 'William') 
                           AND last_name = 'Carter';
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+
```
