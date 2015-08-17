SQL 约束
===

约束用于限制加入表的数据的类型。
可以在创建表时规定约束（通过 CREATE TABLE 语句），或者在表创建之后也可以（通过 ALTER TABLE 语句）。
这里将主要探讨以下几种约束：

- NOT NULL
- UNIQUE
- PRIMARY KEY
- FOREIGN KEY
- CHECK
- DEFAULT

### NOT NULL
约束强制列不接受 NULL 值。
NOT NULL 约束强制字段始终包含值。这意味着，如果不向字段添加值，就无法插入新记录或者更新记录。
下面的 SQL 语句强制 "Id_P" 列和 "LastName" 列不接受 NULL 值：

```
CREATE TABLE people
(
    id int NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255),
    address varchar(255),
    city varchar(255)
);
```

### UNIQUE

UNIQUE 约束唯一标识数据库表中的每条记录。UNIQUE 和 PRIMARY KEY 约束均为列或列集合提供了唯一性的保证。PRIMARY KEY 拥有自动定义的 UNIQUE 约束。
每个表可以有多个 UNIQUE 约束，但是每个表只能有一个 PRIMARY KEY 约束。

下面的 SQL 在 people 表创建时在 id 列创建 UNIQUE 约束：

MySQL:

```
CREATE TABLE people
(
    id int NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255),
    address varchar(255),
    city varchar(255),
    UNIQUE (id)
);
```

SQL Server / Oracle / MS Access:

```
CREATE TABLE people
(
    id int NOT NULL UNIQUE,
    last_name varchar(255) NOT NULL,
    first_name varchar(255),
    address varchar(255),
    city varchar(255),
);
```

如果需要命名 UNIQUE 约束，以及为多个列定义 UNIQUE 约束，请使用下面的 SQL 语法：

MySQL / SQL Server / Oracle / MS Access:

```
CREATE TABLE people
(
    id int NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255),
    address varchar(255),
    city varchar(255),
    CONSTRAINT uc_people UNIQUE (id, last_name)
);
```

SQL UNIQUE Constraint on ALTER TABLE

当表已被创建时，如需在 id 列创建 UNIQUE 约束，请使用下列 SQL：

MySQL / SQL Server / Oracle / MS Access:
```
ALTER TABLE people
ADD UNIQUE (id)
```

如需命名 UNIQUE 约束，并定义多个列的 UNIQUE 约束，请使用下面的 SQL 语法：

MySQL / SQL Server / Oracle / MS Access:
```
ALTER TABLE people
ADD CONSTRAINT uc_people UNIQUE (id, last_name)
```

撤销 UNIQUE 约束

MySQL:

```
ALTER TABLE people
DROP INDEX uc_people
```

SQL Server / Oracle / MS Access:

```
ALTER TABLE people
DROP CONSTRAINT uc_people
```
