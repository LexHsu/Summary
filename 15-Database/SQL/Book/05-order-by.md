ORDER BY
===

ORDER BY 语句默认按照升序对记录进行排序，通过追加 DESC 关键字对结果进行降序排序。

### 示例

```
表名: it

mysql> SELECT * FROM it;
+----+---------+--------+
| id | company | number |
+----+---------+--------+
|  1 | IBM     |      3 |
|  2 | Google  |      6 |
|  3 | Apple   |      4 |
|  4 | Google  |      2 |
+----+---------+--------+

1. 以字母顺序显示公司名称。对于公司名相同，其他列值不同的情况，按照从数据库取出的顺序，不会排序：

mysql> SELECT company, number FROM it ORDER BY company;
+---------+--------+
| company | number |
+---------+--------+
| Apple   |      4 |
| Google  |      6 |
| Google  |      2 |
| IBM     |      3 |
+---------+--------+



2. 以字母顺序显示公司名称（company），并以数字顺序显示顺序号（number）：

mysql> SELECT company, number FROM it ORDER BY company, number;
+---------+--------+
| company | number |
+---------+--------+
| Apple   |      4 |
| Google  |      2 |
| Google  |      6 |
| IBM     |      3 |
+---------+--------+


3. 以逆字母顺序显示公司名称：

mysql> SELECT company, number FROM it ORDER BY company DESC;
+---------+--------+
| company | number |
+---------+--------+
| IBM     |      3 |
| Google  |      6 |
| Google  |      2 |
| Apple   |      4 |
+---------+--------+

4. 以逆字母顺序显示公司名称，并以数字顺序显示顺序号：

mysql> SELECT company, number FROM it ORDER BY company DESC, number ASC;
+---------+--------+
| company | number |
+---------+--------+
| IBM     |      3 |
| Google  |      2 |
| Google  |      6 |
| Apple   |      4 |
+---------+--------+
```
