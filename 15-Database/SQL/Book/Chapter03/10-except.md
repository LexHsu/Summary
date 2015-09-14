EXCEPT
===

EXCEPTn运算符通过包括所有在 table1 中但不在 table2 中的行并消除所有重复行而派生出一个结果表。当 ALL 随 EXCEPT 一起使用时 (EXCEPT ALL)，不消除重复行。

MySQL 中没有 EXCEPT 关键字，可用其他方式替代：

```
mysql> SELECT * FROM it;
+----+---------+--------+
| id | company | number |
+----+---------+--------+
|  1 | IBM     |      3 |
|  2 | Google  |      6 |
|  3 | Apple   |      4 |
|  4 | Google  |      2 |
+----+---------+--------+
4 rows in set (0.00 sec)

mysql> SELECT * FROM it2;
+----+---------+--------+
| id | company | number |
+----+---------+--------+
|  1 | IBM     |      1 |
|  2 | Google  |      6 |
|  3 | Apple   |      9 |
|  4 | Google  |      2 |
+----+---------+--------+
4 rows in set (0.00 sec)

mysql> SELECT number FROM it LEFT JOIN it2 USING(number);
+--------+
| number |
+--------+
|      3 |
|      6 |
|      4 |
|      2 |
+--------+
4 rows in set (0.00 sec)

mysql> SELECT number FROM it LEFT JOIN it2 USING(number) where it2.number is NULL;
+--------+
| number |
+--------+
|      3 |
|      4 |
+--------+
2 rows in set (0.00 sec)

```
