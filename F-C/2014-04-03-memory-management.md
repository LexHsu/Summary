
内存管理
=========

###动态内存释放与野指针
当使用free()和delete释放一块内存时,指针还是指向原来的地址,不过这时候的指针时野指针,那块内存数据是未知的:
- 指针销毁了,所指的空间没有释放，即内存泄露
- 内存被释放了,指针没有置为NULL，即野指针

###内存的常见分配方式

- 从静态区分配,一般是全局变量和static类型变量
- 从栈区分配内存,一般是局部的变量,会随着所在函数的结束而自动释放
- 从堆中分配,一般是使用手动分配,使用malloc()函数和new来申请任意大小空间,不过要手动释放空间,相应的使用free()函数和delete释放,如果不释放该空间,而且指向该空间的指针指向了别的空间.则该空间就无法释放,造成内存泄露,造成了内存浪费

###内存的使用规则
- 在使用malloc()或new申请空间时,要检查有没有分配空间成功,判断方法是判断指针是否为NULL,如申请一块很大的内存而没有这么大的内存则分配内存会失败，因此分配完空间后，要判断指针是否为NULL。
- 申请成功后最好是将该内存清空,使用memset()后ZeroMemory()清空,不然存在垃圾而造成有时候输出很大乱码
- 不要忘记为数组和动态内存赋初值。防止将未被初始化的内存作为右值使用。
- 要防止数组或指针内存越界,
- 申请内存成功后,使用结束后要释放,系统不会自动释放手动分配的内存
- 内存释放后,指针还是指向那块地址,不过这指针已经是"野指针"了,所以释放内存后指针要指向NULL,不然很危险,容易出错。

给指针分配内存主要有两种：一种是在堆上分配一块内存(动态分配内存),让指针指向这块内存.第二种是指针指向栈上的一块内存(一般是定义一个非指针变量,让指针指向这个变量);这两种方法通过以下代码实现:
```c
#include <stdio.h>
#include <malloc.h>
int main()
{
	//第一种
	int *p1 = (int*)malloc(sizeof(int)); //动态的在堆上分配一块内存(手动分配)
	scanf("%d",p1);
    printf("%d/n",*p1);
    free(p1);  //释放内存

	//第二种
	int  i;           //由系统自动在栈上分配一块内存
	int  *p2 = NULL;  
	scanf("%d",&i);
	p2 = &i;          //将p2指向栈上i的内存
	printf("%d/n",*p2);
  return 0;  
}
```
为什么没有在第二种方法后也加free(p2)?如果加了free(p2)编译器会报内存错误,因为p2指向的是i内存,i的内存是在
栈上,栈上的内存都是由系统管理,如分配和释放,不需要手动管理,这里使用free(p2)是在释放i的内存,这块内存不需要你管,你却使用free()管理当然会报错。

###指针和数组
首先，数组里的数据可以单个修改,但指针指向的数据不可修改,如我的例子,char str[] = "hello",数组的大小有6个字符(注意\0),可以通过str[0] = 'X'修改了的个字符,而指针
char *p = "Word",p是指向了一串常量的字符串,常量字符串是不可修改的,如 p[0] = 'X',编译器编译时不会保存,但执行时会出错

```c
#include<iosteam>
using namespace std;

int main() {
    char str[] = "hello"; // 存储了6个字符"hello\0"
    char *p = "word"; // 指针指向了常量字符串，常量字符串不可修改
    str[0] = 'x'; // 正确
    // p[0] = 'x'; // 编译器不会报错，但执行会出错
    cout << str << endl; // 输出xello
    cout << *p << endl; // 输出首字符w
    cout << p[0] << endl; // 输出首字符w
    cout << p << endl; // 输出word
    return 0;
}
```
其次，内容的复制要使用strcpy()函数,不要使用赋值符"=",内容的比较也是不要使用比较符号"<,>,==",使用strcmp()函数

    // 数组例子
    char a[] = "hello";
    char b[10];
    strcpy(b, a);           // 不能用 b = a;
    if(strcmp(b, a) == 0)   // 不能用 if (b == a)

    // 指针例子
    int len = strlen(a);
    char *p = (char *)malloc(sizeof(char)*(len+1));
    strcpy(p,a);            // 不要用 p = a;
    if(strcmp(p, a) == 0)   // 不要用 if (p == a)

然后，对于计算空间的大小，对数组的计算是使用sizeof()函数,该函数会按照内存对齐的方式4的倍数计算,而指针的空间大小没法计算,只能记住在申请空间时的空间大小。注意当数组作为函数的参数进行传递时，该数组自动退化为同类型的指针,不论数组a的容量是多少，sizeof(a)始终等于sizeof(char *)
```c
void Func(char a[100]) {
    cout<< sizeof(a) << endl;   // 4字节而不是100字节
}
```
###指针的内存的传递
如果函数的参数是指针,则不要使用该参数来申请内存空间,这样没有实际的用处,而且这样当函数结束时还得不到释放内存而造成内存泄露
```c
#include<iostream>
using namespace std;

void GetMemory(char *p) {
    p = NULL;
    p = (char *)malloc(sizeof(char));
    memset(p, 0, sizeof(char));
    if (p) {
        count << "p申请内存成功" << endl;
    }
}

int main() {
    char *p1 = NULL;
    GetMemory(p1);
    if (p1) {
        cout << "p1申请内存成功" << endl;
    } else {
        cout << "p1申请内存不成功" << endl;
    }
    return 0;
}

result:
p申请内存成功
p1申请内存不成功
可以看到p1值为0X00000000
```
    这是因为传入函数GetMemory内的是指针p的副本，因此是其副本新分配了空间，而p未变。要想改变p的值只能传入指向p的指针，即二级指针。
    
解决方法一，指针的指针：
 ```c   
#include<iostream>
using namespace std;

// 指针的指针方式
void GetMemory(char **p) {
    *p = NULL;
    *p = (char *)malloc(sizeof(char));
    memset(p, 0, sizeof(char));
    if (*p) {
        count << "p申请内存成功" << endl;
    }
}

int main() {
    char *p1 = NULL;
    GetMemory(&p1); // 这里是&p1
    if (p1) {
        cout << "p1申请内存成功" << endl;
    } else {
        cout << "p1申请内存不成功" << endl;
    }
    return 0;
}
result:
p申请内存成功
p1申请内存成功
可以看到p1值类似于ox005e26a0
```

解决方法二，返回内存地址：
```c
#include<iostream>
using namespace std;

// 返回地址
char * GetMemory() {
    char *p = NULL;
    p = (char *)malloc(sizeof(char)); // 在堆上动态分配
    memset(p, 0, sizeof(char));
    return p;
}

int main() {
    char *p1 = NULL;
    p1 = GetMemory();
    if (p1) {
        cout << "p1申请内存成功" << endl;
    } else {
        cout << "p1申请内存不成功" << endl;
    }
    return 0;
}
result:
p1申请内存成功
可以看到p1值类似于ox003e26a0
```

但是，使用返回内存地址的方式容易出错，上面例子是通过malloc动态分配在堆上，GetMemory函数调用结束不会释放，而如果是在栈上分配的，如函数内的数组，当GetMemory()函数结束时栈内存也被释放，因此"栈内存"的指针便是乱码
```c
#include<iostream>
using namespace std;

// 返回栈内存地址，// 运行不成功，有问题？
char * GetMemory() {
    char p[] = "hello world"; // 栈上分配
    return p;
}

int main() {
    char *p1 = NULL;
    p1 = GetMemory();
    if (p1) {
        cout << "p1申请内存成功" << endl;
        cout << p1 << endl; // 输出乱码
    } else {
        cout << "p1申请内存不成功" << endl;
    }
    return 0;
}

result:
p1申请内存成功
@%%%@~
可以看到p1值类似于ox0012fee4"烫烫?@"

而如下的函数

// 运行不成功，有问题？
char *GetString2(void) {
    char *p = "hello world";
    return p;
}
 
void Test5(void) {
    char *str = NULL;
    str = GetString2();
    cout<< str << endl;
}
 ```
函数Test5运行虽然不会出错，但是函数GetString2的设计概念却是错误的。因为GetString2内的“hello world”是常量字符串，位于静态存储区，
它在程序生命期内恒定不变。无论什么时候调用GetString2，它返回的始终是同一个“只读”的内存块。







