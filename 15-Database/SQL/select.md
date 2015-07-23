SQL SELECT 语句
===

SELECT 语句用于从表中选取数据。结果被存储在一个结果表中（称为结果集）。

### SELECT 语法

```
SELECT 列名称 FROM 表名称
或
SELECT * FROM 表名称
```

### 实例

##### Persons 表:

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |


##### `SELECT LastName,FirstName FROM Persons`, 结果如下:

| Id  | LastName | FirstName |
|:----|:---------|:----------|
|  1  | Adams    | John      |
|  2  | Bush     | George    |
|  3  | Carter   | Thomas    |

##### `SELECT * FROM Persons`, 结果如下:

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |
