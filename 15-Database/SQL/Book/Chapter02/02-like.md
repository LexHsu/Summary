LIKE
===

LIKE 操作符用于在 WHERE 子句中搜索列中的指定模式。语法：
```
SELECT column_name(s) FROM table_name WHERE column_name LIKE pattern
```

### 通配符

|  操作符  |   描述                                          |
|:------------|:---------------------------------------------|
| %           | 替代一个或多个字符                           |
| _           | 仅替代一个字符                               |
| [charlist]  | 字符列中的任何单一字符                       |
| [^charlist] | 同 [!charlist]，不在字符列中的任何单一字符   |

### 示例

```
从 people 表中选取居住在以 "N" 开始的城市里的人：

SELECT * FROM people WHERE City LIKE 'N%'
提示："%" 可用于定义通配符（模式中缺少的字母）。

从 people 表中选取居住在以 "g" 结尾的城市里的人：
SELECT * FROM Persons WHERE City LIKE '%g'

从 people 表中选取居住在包含 "lon" 的城市里的人：
SELECT * FROM Persons WHERE City LIKE '%lon%'

使用 NOT 关键字，可以从表中选取居住在不包含 "lon" 的城市里的人：
SELECT * FROM Persons WHERE City NOT LIKE '%lon%'
```
