USING
===

USING 用于表连接时给定连接条件（可以理解为JOIN的简写形式）。


###　示例

示例一：

```
SELECT column_name
FROM table1
INNER JOIN table2
ON table1.column_name = table2.column_name

等价于

SELECT column_name
FROM table1
INNER JOIN table2
USING(column_name)
```

示例二：

```
SELECT * FROM table1   
JOIN table2
ON table1.id = table2.id   

等价于

SELECT * FROM table1   
JOIN table2
USING(id)  
```

