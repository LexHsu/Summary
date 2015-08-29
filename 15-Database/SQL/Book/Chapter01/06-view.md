VIEW
===

在 SQL 中，视图是基于 SQL 语句的结果集的可视化的虚表，包含行和列，其字段来自真实的表中的字段。
可以向视图添加 SQL 函数、WHERE 以及 JOIN 语句，也可以提交数据，就像这些来自于某个单一的表。

注：数据库的设计和结构不会受到视图中的函数、where 或 join 语句的影响。

### 视图作用

1. 可以定制特定数据：如销售公司的采购人员，可能只需要采购数据，其他数据不需要，可专门为采购人员创建采购数据视图，以后其查询数据，只需 `select * from view_purchase` 即可。
2. 可以简化数据操作：在查询时，常需要使用聚合函数，还会显示其它字段信息，也可能还会关联到其它表，这时写的语句可能会很长。可以创建视图，查询只需要 `select * from view_simple` 即可。
3. 提高安全性：因为视图是虚拟的，只是存储了数据的集合，可隐藏重要字段信息；且用户对视图，不可以随意更改和删除。
4. 可以合并分离的数据：如大公司有很多分公司，为了管理方便，需要统一表的结构，定期查看各公司业务情况，可使用 union 关键字，将各分公司的数据合并为一个视图。

注：实际上很多公司都使用视图来查询数据。

### SQL 创建视图
```
1. 语法：

CREATE VIEW view_name AS
SELECT column_name(s)
FROM table_name
WHERE condition

注：视图总是显示最近的数据。每当用户查询视图时，数据库引擎通过使用 SQL 语句来重建数据。

2. 示例：
创建一个名为 address_view 的视图，包括 address，city 两列：
mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       | Oxford Street  | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     | Changan Street | Beijing  |
+----+-----------+------------+----------------+----------+
3 rows in set (0.00 sec)

mysql> CREATE VIEW address_view AS SELECT address, city FROM people;
Query OK, 0 rows affected (0.06 sec)

mysql> SELECT * FROM address_view;
+----------------+----------+
| address        | city     |
+----------------+----------+
| Oxford Street  | London   |
| Fifth Avenue   | New York |
| Changan Street | Beijing  |
+----------------+----------+
3 rows in set (0.02 sec)
```

### SQL 更新视图
```
1. 语法：
CREATE OR REPLACE VIEW view_name AS
SELECT column_name(s)
FROM table_name
WHERE condition

注：开始误认为CREATE 和 REPLACE 只要使用任意一个关键字即可，其实是需要同时使用。

2. 示例：
向 address_view 视图添加  first_name 列：
mysql> CREATE OR REPLACE VIEW address_view AS SELECT address, city, first_name FROM people;
Query OK, 0 rows affected (0.04 sec)

mysql> SELECT * FROM address_view;
+----------------+----------+------------+
| address        | city     | first_name |
+----------------+----------+------------+
| Oxford Street  | London   | John       |
| Fifth Avenue   | New York | George     |
| Changan Street | Beijing  | Thomas     |
+----------------+----------+------------+
3 rows in set (0.00 sec)
```

### SQL 撤销视图
可通过 DROP VIEW 命令删除视图。
```
DROP VIEW view_name;
```
