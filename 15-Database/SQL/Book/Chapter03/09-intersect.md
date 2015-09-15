INTERSECT
===

INTERSECT 运算符通过只包括 table1 和 table2 中都有的行并消除所有重复行而派生出一个结果表。当 ALL 随 INTERSECT 一起使用时 (INTERSECT ALL)，不消除重复行。

```
SELECT member_id, name FROM table1
INTERSECT
SELECT member_id, name FROM table2;
```

MySQL　不支持 INTERSECT，可用如下语句代替：

```
SELECT member_id, name
FROM table1 INNER JOIN table2
USING (member_id, name)
```
