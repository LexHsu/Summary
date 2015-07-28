DISTINCT
===


表中可能会包含重复值。有时希望仅列出不同的值。关键词 DISTINCT 即用于返回唯一不同的值。语法：

```
SELECT DISTINCT 列名称 FROM 表名称
```

### 实例

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

从 Company 列中选取所有的值：

mysql> SELECT company FROM it;
+---------+
| company |
+---------+
| IBM     |
| Google  |
| Apple   |
| Google  |
+---------+

从 Company 列中仅选取唯一不同的值：

mysql> SELECT DISTINCT company FROM it;
+---------+
| company |
+---------+
| IBM     |
| Google  |
| Apple   |
+---------+
```
