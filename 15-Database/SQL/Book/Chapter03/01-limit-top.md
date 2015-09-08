LIMIT OR TOP
===

有时需要指定要返回的记录的数目。MySQL 和 Oracle 使用 LIMIT，SQL Server 使用 TOP。
对于拥有数千条记录的大型表来说，TOP 子句是非常有用的。

```
SQL Server 语法：
SELECT TOP number|percent column_name(s) FROM table_name

MySQL 语法：
SELECT column_name(s) FROM table_name LIMIT number

Oracle 语法
SELECT column_name(s) FROM table_name WHERE ROWNUM <= number
```

### 示例

```
Persons 表:

1. 从 people 表中选取头两条记录
SQL Server：
SELECT TOP 2 * FROM people;

MySQL：
SELECT * FROM people LIMIT 2;

Oracle：
SELECT * FROM people WHERE id <= 2;

2. 在 people 表中选取 50% 的记录：
SQL Server：
SELECT TOP 50 PERCENT * FROM people;

MySQL：
SELECT first_name FROM people WHERE id <= (SELECT COUNT(first_name) FROM people) / 2;

Oracle：
SELECT first_name FROM people WHERE id <= (SELECT COUNT(first_name) FROM people) / 2;

3. 从第三条开始的 2 条记录：
SQL Server：
SELECT TOP 2 * FROM people WHERE id NOT IN (SELECT TOP 3 id from people);

MySQL：
SELECT * FROM people LIMIT 3, 2;

Oracle：
SELECT * FROM people WHERE id <= 5 AND id > 2;

```
