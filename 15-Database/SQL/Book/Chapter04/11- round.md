ROUND()
===

ROUND 函数用于把数值字段舍入为指定的小数位数。语法：

```
SELECT ROUND(column_name,decimals) FROM table_name
```

|        参数      |   描述                                     |
|:-----------------|:-------------------------------------------|
| column_name      | 必需。要四舍五入字段。
| decimals         | 必需。规定要返回的小数位数。

###示例

```
mysql> SELECT * FROM price;
+----+--------+-------+
| id | name   | price |
+----+--------+-------+
|  1 | apple  |   3.5 |
|  2 | banana |   4.7 |
|  3 | grape  |   5.2 |
+----+--------+-------+
3 rows in set (0.00 sec)

mysql> SELECT name, ROUND(price) as round_price FROM price;
+--------+-------------+
| name   | round_price |
+--------+-------------+
| apple  |           4 |
| banana |           5 |
| grape  |           5 |
+--------+-------------+
3 rows in set (0.01 sec)
```
