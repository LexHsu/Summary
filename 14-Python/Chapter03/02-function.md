Function
===

### 定义函数
- python中的函数通过def关键字定义，以冒号结尾。

```python
#!/usr/bin/python
# Filename: func.py

def sayHello():
    print 'Hello World!' # block belonging to the function

sayHello() # call the function

# $ python func.py
Hello World!
```

### 参数
- 函数中的参数名称为`形参`, 提供给函数调用的值称为`实参`。

```python
#!/usr/bin/python
# Filename: func_param.py

def printMax(a, b):
    if a > b:
        print a, 'is maximum'
    else:
        print b, 'is maximum'

printMax(3, 4) # directly give literal values

x = 5
y = 7

printMax(x, y) # give variables as arguments

# $ python func_param.py
4 is maximum
7 is maximum
```

- 默认参数。可在函数定义的形参名后加上赋值运算符（=）和默认值，从而给形参指定默认参数值。只有最后一个形参可设置默认值。

```python
#!/usr/bin/python
# Filename: func_default.py

def say(message, times = 1):
    print message * times

say('Hello')
say('World', 5)

# $ python func_default.py
Hello
WorldWorldWorldWorldWorld
```

- 关键参数，即指定参数赋值。

```python
#!/usr/bin/python
# Filename: func_key.py

def func(a, b=5, c=10):
    print 'a is', a, 'and b is', b, 'and c is', c

func(3, 7)
func(25, c=24)
func(c=50, a=100)

# $ python func_key.py
a is 3 and b is 7 and c is 10
a is 25 and b is 5 and c is 24
a is 100 and b is 5 and c is 50
```

### 局部变量

```python
#!/usr/bin/python
# Filename: func_local.py

def func(x):
    print 'x is', x
    x = 2
    print 'Changed local x to', x

x = 50
func(x)
print 'x is still', x

# $ python func_local.py
x is 50
Changed local x to 2
x is still 50
```

### 全局变量

```python
#!/usr/bin/python
# Filename: func_global.py

def func():
    global x    # 声明为全局变量，表示x在外面的代码块定义

    print 'x is', x
    x = 2
    print 'Changed local x to', x

x = 50
func()
print 'Value of x is', x

# $ python func_global.py
x is 50
Changed global x to 2
Value of x is 2
```

### DocStrings
- 在函数的第一个逻辑行的字符串是这个函数的`文档字符串`。DocStrings也适用于模块和类。文档字符串的惯例是一个多行字符串，它的首行以大写字母开始，句号结尾。第二行是空行，从第三行开始是详细的描述。
```python
#!/usr/bin/python
# Filename: func_doc.py

def printMax(x, y):
    '''Prints the maximum of two numbers.

    The two values must be integers.'''
    x = int(x) # convert to integers, if possible
    y = int(y)

    if x > y:
        print x, 'is maximum'
    else:
        print y, 'is maximum'

printMax(3, 5)
print printMax.__doc__

# $ python func_doc.py
5 is maximum
Prints the maximum of two numbers.

        The two values must be integers.
```
