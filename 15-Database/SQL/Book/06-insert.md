INSERT INTO
===

INSERT INTO 语句用于向表格中插入新的行，语法：

```
INSERT INTO 表名称 VALUES (值1, 值2,....)
```

也可以指定所要插入数据的列：
```
INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
```

### 示例：

"Persons" 表：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |

插入新行：
`INSERT INTO Persons VALUES (4, 'Gates', 'Bill', 'Changan Street', 'Beijing')` 结果：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |
|  4  | Gates    | Bill      | Changan Street | Beijing  |

在指定的列中插入数据
`INSERT INTO Persons (LastName, Address) VALUES ('Wilson', 'Champs-Elysees')` 结果：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |
|  4  | Gates    | Bill      | Changan Street | Beijing  |
|  5  | Wilson   |           | Champs-Elysees |          |
