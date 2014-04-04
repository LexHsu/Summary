一个汉字编码后所占字符
===

```java
String name="宋";
// gb2312, 2 byte
int a=name.getBytes("gb2312").length;
System.out.println(a);//a=2
// UTF-8, 3 byte
a=name.getBytes("utf-8").length;
System.out.println(a);//a=3
// GBK, 2 byte
a=name.getBytes("gbk").length;
System.out.println(a);//a=2
// ISO8859-1, 1 byte
a=name.getBytes("ISO8859-1").length;
System.out.println(a);//a=1
// UNICODE, 4 byte
a=name.getBytes("UNICODE").length;
System.out.println(a);//a=4
// 1 byte
a=name.length();//这句不要被忽悠，这只是指该String的长度，不是getBytes编码后
System.out.println(a);
```
