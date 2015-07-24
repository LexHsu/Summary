ORDER BY
===

ORDER BY 语句默认按照升序对记录进行排序，通过追加 DESC 关键字对结果进行降序排序。

### 示例

"Orders"表：

| Company    | OrderNumber  |
|:-----------|:-------------|
| IBM        | 3532         |
| Google     | 2356         |
| Apple      | 4698         |
| Google     | 6953         |

以字母顺序显示公司名称：
`SELECT Company, OrderNumber FROM Orders ORDER BY Company`

| Company    | OrderNumber  |
|:-----------|:-------------|
| Apple      | 4698         |
| Google     | 6953         |
| Google     | 2356         |
| IBM        | 3532         |

以字母顺序显示公司名称（Company），并以数字顺序显示顺序号（OrderNumber）：
`SELECT Company, OrderNumber FROM Orders ORDER BY Company, OrderNumber`

| Company    | OrderNumber  |
|:-----------|:-------------|
| Apple      | 4698         |
| Google     | 2356         |
| Google     | 6953         |
| IBM        | 3532         |

以逆字母顺序显示公司名称：
`SELECT Company, OrderNumber FROM Orders ORDER BY Company DESC`

| Company    | OrderNumber  |
|:-----------|:-------------|
| IBM        | 3532         |
| Google     | 6953         |
| Google     | 2356         |
| Apple      | 4698         |

以逆字母顺序显示公司名称，并以数字顺序显示顺序号：
`SELECT Company, OrderNumber FROM Orders ORDER BY Company DESC, OrderNumber ASC`
| Company    | OrderNumber  |
|:-----------|:-------------|
| IBM        | 3532         |
| Google     | 2356         |
| Google     | 6953         |
| Apple      | 4698         |
