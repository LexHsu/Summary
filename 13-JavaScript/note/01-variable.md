数据类型
===

JavaScript 中的类型包括：字符串、数字、布尔、数组、对象、Null、Undefined

### JavaScript 拥有动态类型

JavaScript 拥有动态类型。这意味着相同的变量可用作不同的类型：

```js
var x                // x 为 undefined
var x = 6;           // x 为数字
var x = "Bill";      // x 为字符串
```

### JavaScript 字符串

字符串可以是引号中的任意文本。您可以使用单引号或双引号：
```js
var carname = "Bill Gates";
var carname = 'Bill Gates';

可以在字符串中使用引号，只要不匹配包围字符串的引号即可：

var answer = "Nice to meet you!";
var answer = "He is called 'Bill'";
var answer = 'He is called "Bill"';
```

### JavaScript 数字

1. JavaScript 只有一种数字类型。数字可以带小数点，也可以不带，所有 JavaScript 数字均为 64 位。
2. JavaScript 不是类型语言。与许多其他编程语言不同，JavaScript 不定义不同类型的数字，比如整数、短、长、浮点等等。
3. JavaScript 中的所有数字都存储为根为 10 的 64 位（8 比特），浮点数。

```js
var x1 = 34.00;      //使用小数点来写
var x2 = 34;         //不使用小数点来写

极大或极小的数字可以通过科学（指数）计数法来书写：

var y = 123e5;      // 12300000
var z = 123e-5;     // 0.00123
```

##### 精度

整数（不使用小数点或指数计数法）最多为 15 位。小数的最大位数是 17，但是浮点运算并不总是 100% 准确：

```java
var x=0.2+0.1;
```

##### 八进制和十六进制

如果前缀为 0，则 JavaScript 会把数值常量解释为八进制数，如果前缀为 0 和 "x"，则解释为十六进制数。

```java
var y=0377;
var z=0xFF;
```

提示：不要在数字前面写零，除非需要进行八进制转换。

### JavaScript 布尔

布尔（逻辑）只能有两个值：true 或 false。
```js
var x = true
var y = false
```
##### Boolean 对象

可将 Boolean 对象理解为一个产生逻辑值的对象包装器。Boolean（逻辑）对象用于将非逻辑值转换为逻辑值（true 或者 false）。
使用关键词 new 来定义 Boolean 对象。下面的代码定义了一个名为 myBoolean 的逻辑对象：
```js
var myBoolean = ew Boolean()
```

下面的所有的代码行均会创建初始值为 false 的 Boolean 对象。

```js
var myBoolean=new Boolean();
var myBoolean=new Boolean(0);
var myBoolean=new Boolean(-0);
var myBoolean=new Boolean(null);
var myBoolean=new Boolean("");
var myBoolean=new Boolean(false);
var myBoolean=new Boolean(undefined);
var myBoolean=new Boolean(NaN);
```

下面的所有的代码行均会创初始值为 true 的 Boolean 对象：

```js
var myBoolean=new Boolean(1);
var myBoolean=new Boolean(true);
var myBoolean=new Boolean("true");
var myBoolean=new Boolean("false");
var myBoolean=new Boolean("Bill Gates");
```

### JavaScript 数组

下面的代码创建名为 cars 的数组：

```js
var cars = new Array();
cars[0] = "Audi";
cars[1] = "BMW";
cars[2] = "Volvo";

或者 (condensed array):
var cars = new Array("Audi","BMW","Volvo");

或者 (literal array):
var cars = ["Audi","BMW","Volvo"];
```
数组下标是基于零的，所以第一个项目是 [0]，第二个是 [1]，以此类推。

### JavaScript 对象

对象由花括号分隔。在括号内部，对象的属性以名称和值对的形式 (name : value) 来定义。属性由逗号分隔：

```js
var person = {firstname:"Bill", lastname:"Gates", id:5566};
```

上面例子中的对象 (person) 有三个属性：firstname、lastname 以及 id。
空格和折行无关紧要。声明可横跨多行：

```js
// 方式一：
var person = {
firstname : "Bill",
lastname  : "Gates",
id        :  5566
};

// 方式二：
person = new Object();
person.firstname = "Bill";
person.lastname = "Gates";
persion.id = "5566"

// 方式三：
function person(firstname, lastname, id) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.id = id;
}
var person = new person("Bill", "Gates", 5566);
```

对象属性有两种寻址方式：

```js
name = person.lastname;
name = person["lastname"];
```

注意：
JavaScript 是面向对象的语言，但 JavaScript 不使用类。
JavaScript 基于 prototype，而不是基于类的。
在 JavaScript 中，不会创建类，也不会通过类来创建对象。

### Undefined 和 Null

Undefined 这个值表示变量不含有值。
可以通过将变量的值设置为 null 来清空变量。

```js
cars = null;
person = null;
```

### 声明变量类型

可以使用关键词 new 来声明其类型：

```js
var carname = new String;
var x = new Number;
var y = new Boolean;
var cars = new Array;
var person = new Object;
```
JavaScript 变量均为对象。声明一个变量，就创建了一个新的对象。
