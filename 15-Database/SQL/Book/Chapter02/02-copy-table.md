MySQL 拷贝表操作
===

- 拷贝表结构到新表 new_table 中，不拷贝表中的数据

```
CREATE TABLE new_table LIKE origin_table  
```

- 拷贝数据到新表中。
```
CREATE TABLE new_table AS   
(   
SELECT *   
FROM origin_table   
)
```

注意：这个语句其实只是把select语句的结果建一个表。所以new_table这个表不会有主键，索引。

- 复制一个表。包括表结构，数据。
```
CREATE TABLE new_table LIKE origin_table;   
INSERT INTO new_table SELECT * FROM origin_table;  
```

- 复制不同数据库中的表。

```
CREATE TABLE new_table LIKE shop.origin_table;   
CREATE TABLE newshop.new_table LIKE shop.origin_table;  
```

- 拷贝一个表中的某些些字段。

```
CREATE TABLE new_table AS   
(   
SELECT username, password FROM origin_table   
)  
```

- 可将新建表的字段改名。

```
CREATE TABLE new_table AS   
(   
SELECT id, username AS uname, password AS pass FROM origin_table   
)
```

- 我们也可以拷贝一部分数据。

```
CREATE TABLE new_table AS   
(   
SELECT * FROM origin_table WHERE LEFT(username,1) = 's'   
)  
```

- 我们也可以在创建表的同时定义表中的字段信息。

```
CREATE TABLE new_table   
(   
id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY   
)   
AS   
(   
SELECT * FROM origin_table   
)
```
