友元
===

C++中有时需要定义一些函数，其不是类的一部分，但又需要访问类的数据成员，这时可以将这些函数定义为该函数的友元函数。友元提高了程序的运行效率（即减少了类型检查和安全性检查等都需要时间开销），但它破坏了类的封装性和隐藏性，使得非成员函数可以访问类的私有成员。

友元（包括友元函数，友元类，友元成员函数）可放在类的public,private,protected的任意位置，分为三种：


### 友元函数

- 友元函数和类的成员函数的区别

1. 成员函数有this指针，而友元函数没有。
2. 友元函数不能被继承，就像父亲的朋友未必是儿子的朋友。

可以将单独一函数作为一个类的友元函数，使这个函数具有访问后者私有成员和保护成员的能力。

1. 友元函数的声明可以放在类的私有部分，也可以放在公有部分，没有区别。
2. 一个函数可以是多个类的友元函数，需要在各个类中分别声明。

- 内部定义友元函数（可不用声明，直接定义）

```c++
class Point  {

public:
    Point(double a, double b)
    {
        x = a;
        y = b;
    }

    friend void Getxy(Point &a)
    {
        cout << a.x << "," << a.y << endl;
    }

private:
    double x, y;
};
```

- 外部定义友元函数，需要先声明

```c++
class Point  {

public:
    Point(double a, double b)
    {
        x = a;
        y = b;
    }

    friend void Getxy(Point &a);

private:
    double x, y;
};

friend void Getxy(Point &a)
{
    cout << a.x << "," << a.y << endl;
}
```


### 友元类

将一个类B声明为类A的友元类，B就可以访问A的私有成员和保护成员。
使用场景：如电视机与遥控的关系。不是has-a的关系，因为一台遥控可以控制多个电视。
也不是 a kind of的关系。但是遥控可以改变电视机的状态，
此种情况用友元实现更合适，即让遥控类作为电视机类的友元，可以更改其私有成员。

```c++
class Tv
{
private:
    int channel;
    int mode;
protected:
    int vol;
public:
    void SetMode();
    friend class Remote;
};

class Remote
{
private:
    int mode;
public:
    void SetVol(int c, Tv&t) {t.vol= c;}
    void SetChan(int c, Tv&t) {t.channel = c;}
};
```

### 友元成员函数

```c++
#include <iostream>
#include <string>
using namespace std;

// 必须先声明A
class A;

// 定义B
class B
{
    int age;
public:
    B(int x)
    {
        age = x;
    }
    void GetAge(A &a);
};

// 定义A
class A
{
    int age;
public:
    A(int x)
    {
        age = x;
    }
    // 声明友元成员，要加上成员函数所在类的类名和作用域运算符：：
    friend void B::GetAge(A &a);
};

// 定义友元成员，这里不能直接放在B类后面，否则会报错
void B::GetAge(A &a)
{
    cout<<"B age: "<<age<<endl;
    cout<<"A age: "<<a.age<<endl;
}

int main()
{
    B b(22);
    A a(11);
    b.GetAge(a);
    return 0;
}
```

可见，类B的成员函数作为类A的友元函数时，必须先定义成员函数所在的类，即B，再定义A，否则编译器无法找到成员函数的声明而报错。
