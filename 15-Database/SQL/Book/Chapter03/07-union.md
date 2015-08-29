UNION
===

UNION 操作符用于合并两个或多个 SELECT 语句的结果集。
注意，UNION 内部的 SELECT 语句必须拥有相同数量的列。列也必须拥有相似的数据类型。同时，每条 SELECT 语句中的列的顺序必须相同。语法：

```
SELECT column_name(s) FROM table_name1
UNION
SELECT column_name(s) FROM table_name2
```

默认情况下，UNION 操作符选取不同的值。如果允许重复的值，使用 UNION ALL。SQL UNION ALL 语法：

```
SELECT column_name(s) FROM table_name1
UNION ALL
SELECT column_name(s) FROM table_name2
```

UNION 结果集中的列名总是等于 UNION 中第一个 SELECT 语句中的列名。

### 示例

```
mysql> SELECT * FROM table1;
+----+-----------+
| id | name      |
+----+-----------+
|  1 | Zhang Hua |
|  2 | Li Li     |
|  3 | Xu Liang  |
+----+-----------+
3 rows in set (0.00 sec)

mysql> SELECT * FROM table2;
+----+-----------+
| id | name      |
+----+-----------+
|  1 | Jiang Yue |
|  2 | Wang Min  |
|  3 | Li Li     |
+----+-----------+
3 rows in set (0.00 sec)

mysql> SELECT name FROM table1 UNION SELECT name FROM table2;
+-----------+
| name      |
+-----------+
| Zhang Hua |
| Li Li     |
| Xu Liang  |
| Jiang Yue |
| Wang Min  |
+-----------+
5 rows in set (0.01 sec)

mysql> SELECT name FROM table1 UNION ALL SELECT name FROM table2;
+-----------+
| name      |
+-----------+
| Zhang Hua |
| Li Li     |
| Xu Liang  |
| Jiang Yue |
| Wang Min  |
| Li Li     |
+-----------+
6 rows in set (0.00 sec)
```
