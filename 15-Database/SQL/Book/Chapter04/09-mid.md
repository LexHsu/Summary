MID()
===


MID 函数用于从文本字段中提取字符。语法：

```
SELECT MID(column_name,start[,length]) FROM table_name
```
|        参数      |   描述                                     |
|:-----------------|:-------------------------------------------|
| column_name      | 必需。要提取字符的字段。
| start            | 必需。规定开始位置（起始值是 1）。
| length           | 可选。要返回的字符数。如果省略，则 MID() 函数返回剩余文本。

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

mysql> SELECT MID(city, 1, 3) as small_city FROM people;
+------------+
| small_city |
+------------+
| Lon        |
| New        |
| Bei        |
+------------+
3 rows in set (0.00 sec)

mysql> SELECT MID(city, 2) as small_city FROM people;
+------------+
| small_city |
+------------+
| ondon      |
| ew York    |
| eijing     |
+------------+
3 rows in set (0.00 sec)
```
