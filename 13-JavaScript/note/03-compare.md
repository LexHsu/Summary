比较运算符
===

比较运算符在逻辑语句中使用，以测定变量或值是否相等。

给定 x=5，下面的表格解释了比较运算符：


可以在条件语句中使用比较运算符对值进行比较，然后根据结果来采取行动：
if (age<18) document.write("Too young");
您将在本教程的下一节中学习更多有关条件语句的知识。

### 逻辑运算符

逻辑运算符用于测定变量或值之间的逻辑。
给定 x=6 以及 y=3，下表解释了逻辑运算符：
运算符	描述	例子
&&	and	(x < 10 && y > 1) 为 true
||	or	(x==5 || y==5) 为 false
!	not	!(x==y) 为 true

### 条件运算符

JavaScript 还包含了基于某些条件对变量进行赋值的条件运算符。

```js
variablename=(condition)?value1:value2
```

greeting=(visitor=="PRES")?"Dear President ":"Dear ";

如果变量 visitor 中的值是 "PRES"，则向变量 greeting 赋值 "Dear President "，否则赋值 "Dear"。
