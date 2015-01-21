数组
===

### 一维数组初始化
```java
// 1. 静态初始化
int intArray[]={1,2,3,4};
String stringArray[]={"abc", "How", "you"};

// 2. 动态初始化
// 2.1 简单类型的数组
int intArray[];
intArray = new int[5];

// 2.2 复合类型的数组
String stringArray[ ];
String stringArray = new String[3]; // 为数组中每个元素开辟引用空间(32位)
stringArray[0]= new String("How");  // 为第一个数组元素开辟空间
stringArray[1]= new String("are");  // 为第二个数组元素开辟空间
stringArray[2]= new String("you");  // 为第三个数组元素开辟空间
```


### 二维数组的初始化

```java
// 1. 静态初始化
int intArray[ ][ ]={{1,2},{2,3},{3,4,5}};
// Java中，由于把二维数组看作是数组的数组，数组空间不是连续分配的，所以不要求二维数组每一维的大小相同。

// 2. 动态初始化
// 2.1 直接为每一维分配空间，格式如下：
arrayName = new type[arrayLength1][arrayLength2];
int a[ ][ ] = new int[2][3]；

// 2.2 从最高维开始，分别为每一维分配空间：
arrayName = new type[arrayLength1][ ];
arrayName[0] = new type[arrayLength20];
arrayName[1] = new type[arrayLength21];
// ......
arrayName[arrayLength1-1] = new type[arrayLength2n];

// 3) 例：
// 二维简单数据类型数组的动态初始化如下:
int a[ ][ ] = new int[2][ ]；
a[0] = new int[3];
a[1] = new int[5];

// 对二维复合数据类型的数组，必须首先为最高维分配引用空间，然后再顺次为低维分配空间。
// 且必须为每个数组元素单独分配空间。如：
String s[ ][ ] = new String[2][ ];
s[0]= new String[2];            // 为最高维分配引用空间
s[1]= new String[2];            // 为最高维分配引用空间
s[0][0]= new String("Good");    // 为每个数组元素单独分配空间
s[0][1]= new String("Luck");    // 为每个数组元素单独分配空间
s[1][0]= new String("to");      // 为每个数组元素单独分配空间
s[1][1]= new String("You");     // 为每个数组元素单独分配空间
```

### 示例

```java
// 一维数组示例
int[] a;                                            // 声明，没有初始化

int[] a = new int[5];                               // 动态初始化，默认为 0
// 等价于
int[] a;
a=new int[5];

int[] a = {1, 2, 3, 4, 5};                          // 初始化为给定值
int[] a = new int[]{1,2,3,4,5};                     // 同上

int[] a = new int[5]{1, 2, 3, 4, 5};                // 错误，如果提供了数组初始化操作，则不能定义维表达式

int[] a;
a = {1,2,3,4,5};                                    // 错误，数组常量只能在初始化操作中使用

int a[];
a[0]=1;                                             // 错误，因为数组没有初始化,不能赋值
a[1]=2;


// 二维数组示例
int[][] a;                                          // 声明，没有初始化

int[][] a = new int[2][3];                          // 初始化为默认值 0

int[][] a = {{1, 2}, {2, 3}, {3, 4}};               // 初始化为给定值
int[][] a = {{1, 2}, {2, 3}, {3, 4, 5}};            // 正确，数组空间不是连续分配，不要求每一维大小相同
int[][] a = new int[][]{{1, 2}, {2, 3}, {3, 4, 5}}; // 同上

int[][] a = new int[2][];
a[0] = new int[3];                                  // a[0]其实就是一个数组
a[1] = new int[4];                                  // 每一维的大小可以不一样


int[] a = new int[5]{{1, 2}, {2, 3}, {3, 4, 5}};    // 错误，如果提供了数组初始化操作，则不能定义维表达式
int[][] a = new int[2][];
a[0] = {1, 2, 3, 4, 5};                             // 错误，数组常量只能在初始化操作中使用

int[][] a = new int[2][];
a[0][1] = 1;                                        // 错误，第二维没有初始化,空指针异常
```
