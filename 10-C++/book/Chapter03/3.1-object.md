创建对象方式
===

在C++里，有两种方法创建对象：
方法一：

```c++
ClassName object(param);
```

这样就声明了一个ClassName类型的object对象，C++会为它分配足够的存放对象所有成员的存储空间。
注意：为节省存储空间，C++创建对象时仅分配用于保存数据成员的空间，而类中定义的成员函数则被分配到存储空间中的一个公用区域，由该类的所有对象共享。
例如，我定义了一个这样的类：

```c++
class Rec
{
   public:
       Rec(int width, int height);
       ~Rec();
       int getArea();

   private:
       int Rwidth;
       int Rheight;
};
```
Rec myRec(5,5);
这种方法创建的对象，内存分配是分配到栈中的，由C++缺省创建和撤销，自动调用构造函数和析构函数
因为myRec中有2个int类型的数据成员，一个int成员占4个字节，所以myRec对象占8个字节。

注：该方法创建的对象调用类方法时，必须用`.`，而不能用`->`，如myRec.getArea();

方法二：

```c++
ClassName *object = new ClassName(param);
delete object;
```
在堆上分配内存来创建对象的；区别在于用new创建对象时返回的是一个对象指针，object指向一个ClassName的对象，C++分配给object的仅仅是存放指针值的空间。且用new动态创建的对象必须用delete撤销该对象。只有delete对象才会调用其析构函数。

注：new创建的对象用运算符`->`访问成员方法;

```c++
Rec *rec=new Rec(3,4);
rec->getArea();
delete rec;
```
