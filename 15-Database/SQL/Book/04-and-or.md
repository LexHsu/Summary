AND OR
===

AND 和 OR 可在 WHERE 子语句中把两个或多个条件结合起来。

Persons 表：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |


### 实例

使用 AND 来显示所有姓为 "Carter" 并且名为 "Thomas" 的人：

`SELECT * FROM Persons WHERE FirstName='John' AND LastName='Adams'`

结果：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  1  | Adams    | John      | Oxford Street  | London   |


使用 OR 来显示所有姓为 "Carter" 或者名为 "Thomas" 的人：

`SELECT * FROM Persons WHERE firstname='Thomas' OR lastname='Bush'`

结果：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  2  | Bush     | George    | Fifth Avenue   | New York |
|  3  | Carter   | Thomas    | Changan Street | Beijing  |


结合 AND 和 OR 运算符

`SELECT * FROM Persons WHERE (FirstName='Thomas' OR FirstName='William') AND LastName='Carter'``

结果：

| Id  | LastName | FirstName |     Address    |   city   |
|:----|:---------|:----------|:---------------|:---------|
|  3  | Carter   | Thomas    | Changan Street | Beijing  |
