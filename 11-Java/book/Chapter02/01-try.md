try中的return语句
===

对于Java中的try语句，会先执行finally中的语句，再执行try中的return。
如果finally里面也有return语句，则try代码块中的return被屏蔽。

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(new Test().test());
    }

    public String test() {
        try {
            System.out.println("in try");
            return "return try";
        } catch (Exception e) {
            return "catch";
        } finally {
            System.out.println("in finally");
            return "return finally";
        }
    }
}

// 结果
in try
in finally
return finally
```

如果finally语句中有修改返回值，但是没有return，在try中return，不会反应到最终结果上。

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(new Test().test());
    }

    public String test() {
        String result = "";
        try {
            result = "return try";
            System.out.println("in try");
            return result;
        } catch (Exception e) {
            return result;
        } finally {
            result = "return finally";
            System.out.println("in finally");
        }
    }
}

// 结果
in try                                                                              
in finally                                                                          
return try 
```


可见，应避免在try或者finally中处理return语句。会增加程序复杂度且存在隐藏错误。return语句应放在try语句之外。
与return语句相似，System.exit(0)或Runtime.getRuntime().exit(0)出现在异常代码块中也常会出现错误。

正确代码示例：

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(new Test().test());
    }

    public String test() {
        String result = "";
        try {
            result = "return try";
            System.out.println("in try");
        } catch (Exception e) {
            return result;
        } finally {
            result = "return finally";
            System.out.println("in finally");
        }
        return result;
    }
}

// 结果
in try                                                                              
in finally                                                                          
return finally
```


