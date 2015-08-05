BETWEEN
===

操作符 BETWEEN ... AND 会选取介于两个值之间的数据范围。这些值可以是数值、文本或者日期。语法：

```
SELECT column_name(s) FROM table_name WHERE column_name BETWEEN value1 AND value2;
```

### 示例

以字母顺序显示介于 "Adams"（包括）和 "Carter"（不包括）之间的人：



注：不同的数据库对 BETWEEN...AND 操作符的处理方式是有差异的。
某些数据库会列出介于 Adams 和 Carter 之间的人，但不包括 Ada 和 Carter ；
某些数据库会列出介于 Adams 和 Carter 之间并包括 Ada 和 "Carter" 的人；
某些数据库会列出介于 Adams 和 Carter 之间的人，包括 Ada，但不包括 Carter 。


显示上述范围之外的人，使用 NOT 操作符：
