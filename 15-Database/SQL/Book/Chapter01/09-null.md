NULL
===

默认表的列可存放 NULL 值。

### SQL NULL 值
如果表中的某个列是可选的，那么我们可以在不向该列添加值的情况下插入新记录或更新已有的记录。这意味着该字段将以 NULL 值保存。
NULL 值的处理方式与其他值不同。
NULL 用作未知的或不适用的值的占位符。
注释：无法比较 NULL 和 0；它们是不等价的。
SQL 的 NULL 值处理

```
mysql> SELECT * FROM people;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       |                | London   |
|  2 | Bush      | George     | Fifth Avenue   | New York |
|  3 | Carter    | Thomas     |                | Beijing  |
+----+-----------+------------+----------------+----------+
```

无法使用比较运算符来测试 NULL 值，比如 =, <, 或者 <>。必须使用 IS NULL 和 IS NOT NULL 操作符。

### SQL IS NULL
如果要仅选取在 address 列中带有 NULL 值的记录，必须使用 IS NULL 操作符：
```
mysql> SELECT last_name, first_name, address FROM people WHERE address IS NULL;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  1 | Ada       | John       |                | London   |
|  3 | Carter    | Thomas     |                | Beijing  |
+----+-----------+------------+----------------+----------+
```
提示：请始终使用 IS NULL 来查找 NULL 值。

### SQL IS NOT NULL
选取在 address 列中不带有 NULL 值的记录，必须使用 IS NOT NULL 操作符：
```
mysql> SELECT last_name, first_name, address FROM people WHERE address IS NOT NULL;
+----+-----------+------------+----------------+----------+
| id | last_name | first_name | address        | city     |
+----+-----------+------------+----------------+----------+
|  2 | Bush      | George     | Fifth Avenue   | New York |
+----+-----------+------------+----------------+----------+
```

### IFNULL() 函数

微软的 ISNULL() 函数用于规定如何处理 NULL 值。
NVL(), IFNULL() 和 COALESCE() 函数也可以达到相同的结果。
在这里，我们希望 NULL 值为 0。
有时候，对于某个字段值是 NULL 的情况，则不利于计算，因此 MySQL 中如果值是 NULL 则 IFNULL() 返回 0。相当于
SQL Server的 ISNULL()，Oracle 的 NVL()，这里以 MySQL 为例：

```
mysql> SELECT name, price * (param1 + IFNULL(param2, 0));
```
