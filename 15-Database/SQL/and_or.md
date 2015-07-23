AND OR
===

AND 和 OR 运算符用于基于一个以上的条件对记录进行过滤。

AND 和 OR 可在 WHERE 子语句中把两个或多个条件结合起来。
- 如果第一个条件和第二个条件都成立，则 AND 运算符显示一条记录。
- 如果第一个条件和第二个条件中只要有一个成立，则 OR 运算符显示一条记录。

| Id  |  LastName    |   FirstName   | Address   |
|:----|:-------------|:--------------|:----------|
|  1  | Adams John   | Oxford Street | London    |
|  2  | Bush George  | Fifth Avenue  | New York  |
|  3  | Carter Thomas| Changan Street| Beijing   |


### 使用 AND 来显示所有姓为 "Carter" 并且名为 "Thomas" 的人：
`SELECT * FROM Persons WHERE FirstName='Oxford Street' AND LastName='Adams John'`
结果：

|  LastName    |   FirstName   |
|:-------------|:--------------|
| Adams John   | Oxford Street |


### OR 运算符实例

使用 OR 来显示所有姓为 "Carter" 或者名为 "Thomas" 的人：
`SELECT * FROM Persons WHERE firstname='Thomas' OR lastname='Carter'`

结果：

|  LastName    |   FirstName   |
|:-------------|:--------------|
| Adams John   | Oxford Street |
| Bush George  | Fifth Avenue  |
| Carter Thomas| Changan Street|

### 结合 AND 和 OR 运算符
`SELECT * FROM Persons WHERE (FirstName='Thomas' OR FirstName='William') AND LastName='Carter'``

结果：

|  LastName    |   FirstName   |
|:-------------|:--------------|
| Adams John   | Oxford Street |
| Bush George  | Fifth Avenue  |
| Carter Thomas| Changan Street|
