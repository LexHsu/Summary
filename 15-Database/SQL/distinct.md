DISTINCT
===


在表中，可能会包含重复值。，有时希望仅列出不同（distinct）的值。关键词 DISTINCT 即用于返回唯一不同的值。语法：

```
SELECT DISTINCT 列名称 FROM 表名称
```

### 实例

"Orders"表：

| Company    | OrderNumber  |
|:-----------|:-------------|
| IBM        | 3532         |
| W3School   | 2356         |
| Apple      | 4698         |
| W3School   | 6953         |

从 "Company" 列中选取所有的值，`SELECT Company FROM Orders`，结果：

| Company    |
|:-----------|
| IBM        |
| W3School   |
| Apple      |
| W3School   |

请注意，在结果集中，W3School 被列出了两次。
如需从 "Company" 列中仅选取唯一不同的值，`SELECT DISTINCT Company FROM Orders`，结果：

| Company    |
|:-----------|
| IBM        |
| W3School   |
| Apple      |

"W3School" 仅被列出了一次。
