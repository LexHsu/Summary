WHERE
===

WHERE 子句用于有条件地从表中选取数据，语法：

```
SELECT 列名称 FROM 表名称 WHERE 列 运算符 值
```

下面的运算符可在 WHERE 子句中使用：

|  操作符  |   描述                                          |
|:---------|:-----------------------------------------------|
| =        | 等于                                            |
| <>       | 不等于,某些版本的 SQL 中，操作符 <> 可以写为 !=     |
| >        | 大于                                            |
| <        | 小于                                            |
| >=       | 大于等于                                         |
| <=       | 小于等于                                         |
| BETWEEN  | 在某个范围内                                     |
| LIKE     | 搜索某种模式                                     |


### 实例

Persons 表:

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |

选取居住在城市 "Beijing" 中的人，`SELECT * FROM Persons WHERE City='Beijing'`，结果：

| Id  | LastName | FirstName |     Address    |   City   |
|:----|:---------|:----------|:---------------|:---------|
|  3  | Carter   | Thomas    | Changan Street | Beijing  |

### 引号的使用

上例中的条件语句中使用单引号。因为 SQL 使用单引号来环绕文本值（大部分数据库系统也接受双引号）。如果是数值，则不使用引号。

##### 文本值：
```
正确：
SELECT * FROM Persons WHERE FirstName='Bush'

错误：
SELECT * FROM Persons WHERE FirstName=Bush
```

##### 数值：

```
正确：
SELECT * FROM Persons WHERE Year>1965

错误：
SELECT * FROM Persons WHERE Year>'1965'
```
