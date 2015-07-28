UPDATE
===

Update 语句用于修改表中的数据。语法：
```
UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
```

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

修改某一行的某一列：

mysql> UPDATE people SET last_name = 'Fred' WHERE first_name = 'John';
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Fred      | John       | Oxford Street  | London   |
+----+-----------+------------+----------------+----------+

修改某一行的若干列：
mysql> UPDATE people SET address = 'Zhongshan Road', city = 'Nanjing' WHERE last_name = 'Bush';
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  2 | Bush      | George     | Zhongshan Road | Nanjing  |
+----+-----------+------------+----------------+----------+
```
