CREATE INDEX
===

CREATE INDEX 语句用于在表中创建索引。
在不读取整个表的情况下，索引使数据库应用程序可以更快地查找数据。

可以在表中创建索引，以便更加快速高效地查询数据。用户无法看到索引，它们只能被用来加速搜索/查询。
注：更新一个包含索引的表需要比更新一个没有索引的表更多的时间，这是由于索引本身也需要更新。因此，理想的做法是仅仅在常常被搜索的列（以及表）上面创建索引。

```
SQL CREATE INDEX 语法
在表上创建一个简单的索引。允许使用重复的值：
CREATE INDEX index_name
ON table_name (column_name)
注释："column_name" 规定需要索引的列。

SQL CREATE UNIQUE INDEX 语法
在表上创建一个唯一的索引。唯一的索引意味着两个行不能拥有相同的索引值。
CREATE UNIQUE INDEX index_name
ON table_name (column_name)
```

### 示例

```
创建一个简单的索引，名为 people_index，在 Person 表的 last_name 列：

CREATE INDEX people_index
ON Person (last_name)

如希望以降序索引某个列中的值，您可以在列名称之后添加保留字 DESC：
CREATE INDEX PersonIndex
ON Person (last_name DESC)

如希望索引不止一个列，您可以在括号中列出这些列的名称，用逗号隔开：
CREATE INDEX people_index
ON Person (last_name, first_name)
```
