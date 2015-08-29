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

### NOT NULL 约束
约束强制列不接受 NULL 值，如果不向字段添加值，就无法插入新记录或者更新记录。
下面的 SQL 语句强制 id 列和 "last_name" 列不接受 NULL 值：

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

### UNIQUE 约束
UNIQUE 约束唯一标识数据库表中的每条记录。UNIQUE 和 PRIMARY KEY 约束均为列或列集合提供了唯一性的保证。区别在于：

- PRIMARY KEY 拥有自动定义的 UNIQUE 约束。
- 每个表可以有多个 UNIQUE 约束，但是每个表只能有一个 PRIMARY KEY 约束。

下面的 SQL 在 people 表创建时在 id 列创建 UNIQUE 约束：

```
MySQL:
CREATE TABLE people
(
    id int NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255),
    address varchar(255),
    city varchar(255),
    UNIQUE (id)
);

SQL Server / Oracle / MS Access:
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

```
MySQL / SQL Server / Oracle / MS Access:
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



当表已被创建时，如需在 id 列创建 UNIQUE 约束，请使用下列 SQL：

```
SQL UNIQUE Constraint on ALTER TABLE

MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE people
ADD UNIQUE (id)
```

如需命名 UNIQUE 约束，并定义多个列的 UNIQUE 约束，请使用下面的 SQL 语法：

```
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE people
ADD CONSTRAINT uc_people UNIQUE (id, last_name)
```

撤销 UNIQUE 约束

```
MySQL:
ALTER TABLE people
DROP INDEX uc_people

SQL Server / Oracle / MS Access:
ALTER TABLE people
DROP CONSTRAINT uc_people
```

### PRIMARY KEY 约束
PRIMARY KEY 约束唯一标识数据库表中的每条记录。
主键必须包含唯一的值。
主键列不能包含 NULL 值。
每个表都应该有一个主键，并且每个表只能有一个主键。
```
SQL PRIMARY KEY Constraint on CREATE TABLE
```
下面的 SQL 在 people 表创建时在 id 列创建 PRIMARY KEY 约束：

```
MySQL:
CREATE TABLE people
(
id int NOT NULL,
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255),
PRIMARY KEY (id)
)
SQL Server / Oracle / MS Access:
CREATE TABLE people
(
id int NOT NULL PRIMARY KEY,
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255)
)
```
如果需要命名 PRIMARY KEY 约束，以及为多个列定义 PRIMARY KEY 约束，请使用下面的 SQL 语法：
```
MySQL / SQL Server / Oracle / MS Access:
CREATE TABLE people
(
id int NOT NULL,
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255),
CONSTRAINT pk_PersonID PRIMARY KEY (id,last_name)
)
SQL PRIMARY KEY Constraint on ALTER TABLE
如果在表已存在的情况下为 id 列创建 PRIMARY KEY 约束，请使用下面的 SQL：
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE people
ADD PRIMARY KEY (id)
如果需要命名 PRIMARY KEY 约束，以及为多个列定义 PRIMARY KEY 约束，请使用下面的 SQL 语法：
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE people
ADD CONSTRAINT pk_PersonID PRIMARY KEY (id,last_name)
注释：如果使用 ALTER TABLE 语句添加主键，必须把主键列声明为不包含 NULL 值（在表首次创建时）。
撤销 PRIMARY KEY 约束
如需撤销 PRIMARY KEY 约束，请使用下面的 SQL：
MySQL:
ALTER TABLE people
DROP PRIMARY KEY
SQL Server / Oracle / MS Access:
ALTER TABLE people
DROP CONSTRAINT pk_PersonID
```

### FOREIGN KEY 约束
一个表中的 FOREIGN KEY 指向另一个表中的 PRIMARY KEY。
FOREIGN KEY 约束用于预防破坏表之间连接的动作。
FOREIGN KEY 约束也能防止非法数据插入外键列，因为它必须是它指向的那个表中的值之一。
SQL FOREIGN KEY Constraint on CREATE TABLE

下表 orders 中的 ideople 列指向 people 表中的 id 列。

```
mysql> SELECT * FROM people;
+----+-----------+------------+------------+----------+
| id | last_name | first_name | address    | city     |
+----+-----------+------------+------------+----------+
|  1 | Ada       | John       | Oxford Str | London   |
|  2 | Bush      | George     | Fifth Aven | New York |
|  3 | Carter    | Thomas     | Changan St | Beijing  |
+----+-----------+------------+------------+----------+
3 rows in set (0.00 sec)

mysql> SELECT * FROM orders;
+----+--------+-----------+
| id | number | ideople |
+----+--------+-----------+
|  1 |    100 |         3 |
|  2 |    200 |         3 |
|  3 |    300 |         1 |
|  4 |    400 |         1 |
|  5 |    500 |         9 |
+----+--------+-----------+
5 rows in set (0.00 sec)
```

- people 表中的 id 列是 people 表中的 PRIMARY KEY。
- orders 表中的 id 列是 orders 表中的 FOREIGN KEY。

下面的 SQL 在 orders 表创建时为 id 列创建 FOREIGN KEY：
```
MySQL:
CREATE TABLE Orders
(
id int NOT NULL,
OrderNo int NOT NULL,
id int,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES Persons(id)
)

SQL Server / Oracle / MS Access:
CREATE TABLE Orders
(
id int NOT NULL PRIMARY KEY,
OrderNo int NOT NULL,
id int FOREIGN KEY REFERENCES Persons(id)
)

如果需要命名 FOREIGN KEY 约束，以及为多个列定义 FOREIGN KEY 约束，请使用下面的 SQL 语法：
MySQL / SQL Server / Oracle / MS Access:
CREATE TABLE Orders
(
id int NOT NULL,
OrderNo int NOT NULL,
id int,
PRIMARY KEY (id),
CONSTRAINT fk_PerOrders FOREIGN KEY (id)
REFERENCES Persons(id)
)
SQL FOREIGN KEY Constraint on ALTER TABLE

如果在 orders 表已存在的情况下为 id 列创建 FOREIGN KEY 约束，请使用下面的 SQL：
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE Orders
ADD FOREIGN KEY (id)
REFERENCES Persons(id)

如果需要命名 FOREIGN KEY 约束，以及为多个列定义 FOREIGN KEY 约束，请使用下面的 SQL 语法：
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE Orders
ADD CONSTRAINT fk_PerOrders
FOREIGN KEY (id)
REFERENCES Persons(id)
撤销 FOREIGN KEY 约束

如需撤销 FOREIGN KEY 约束，请使用下面的 SQL：
MySQL:
ALTER TABLE Orders
DROP FOREIGN KEY fk_PerOrders

SQL Server / Oracle / MS Access:
ALTER TABLE Orders
DROP CONSTRAINT fk_PerOrders
```

### CHECK 约束
CHECK 约束用于限制列中的值的范围。
如果对单个列定义 CHECK 约束，那么该列只允许特定的值。
如果对一个表定义 CHECK 约束，那么此约束会在特定的列中对值进行限制。
SQL CHECK Constraint on CREATE TABLE
下面的 SQL 在 people 表创建时为 id 列创建 CHECK 约束。CHECK 约束规定 id 列必须只包含大于 0 的整数。
```
My SQL:
CREATE TABLE Persons
(
id int NOT NULL,
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255),
CHECK (id>0)
)

SQL Server / Oracle / MS Access:
CREATE TABLE Persons
(
id int NOT NULL CHECK (id>0),
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255)
)


如果需要命名 CHECK 约束，以及为多个列定义 CHECK 约束，请使用下面的 SQL 语法：
MySQL / SQL Server / Oracle / MS Access:
CREATE TABLE Persons
(
id int NOT NULL,
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255),
CONSTRAINT chk_Person CHECK (id>0 AND city = 'Sandnes')
)


SQL CHECK Constraint on ALTER TABLE
如果在表已存在的情况下为 id 列创建 CHECK 约束，请使用下面的 SQL：
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE Persons
ADD CHECK (id>0)


如果需要命名 CHECK 约束，以及为多个列定义 CHECK 约束，请使用下面的 SQL 语法：
MySQL / SQL Server / Oracle / MS Access:
ALTER TABLE Persons
ADD CONSTRAINT chk_Person CHECK (id>0 AND city = 'Sandnes')


撤销 CHECK 约束
SQL Server / Oracle / MS Access:
ALTER TABLE Persons
DROP CONSTRAINT chk_Person

MySQL:
ALTER TABLE Persons
DROP CHECK chk_Person
```

### DEFAULT

DEFAULT 约束用于向列中插入默认值。
如果没有规定其他的值，那么会将默认值添加到所有的新记录。
SQL DEFAULT Constraint on CREATE TABLE

```
下面的 SQL 在 people 表创建时为 city 列创建 DEFAULT 约束：
My SQL / SQL Server / Oracle / MS Access:
CREATE TABLE Persons
(
id int NOT NULL,
last_name varchar(255) NOT NULL,
first_name varchar(255),
address varchar(255),
city varchar(255) DEFAULT 'Sandnes'
)
```

通过使用类似 GETDATE() 这样的函数，DEFAULT 约束也可以用于插入系统值：
```
CREATE TABLE Orders
(
id int NOT NULL,
OrderNo int NOT NULL,
id_people int,
OrderDate date DEFAULT GETDATE()
)
```

如果在表已存在的情况下为 city 列创建 DEFAULT 约束，请使用下面的 SQL：
```
SQL DEFAULT Constraint on ALTER TABLE
MySQL:
ALTER TABLE Persons
ALTER city SET DEFAULT 'SANDNES'

SQL Server / Oracle / MS Access:
ALTER TABLE Persons
ALTER COLUMN city SET DEFAULT 'SANDNES'
```

撤销 DEFAULT 约束：
```
MySQL:
ALTER TABLE Persons
ALTER city DROP DEFAULT

SQL Server / Oracle / MS Access:
ALTER TABLE Persons
ALTER COLUMN city DROP DEFAULT
```
