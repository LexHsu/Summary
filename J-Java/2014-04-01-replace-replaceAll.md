replace与replaceAll区别
=========

@(J-Java)[replace|replaceAll]

replace和replaceAll是JAVA中常用的替换字符的方法。不要被字面意思混淆，两者都是全部替换,即把源字符串中的某一字符或字符串全部换成指定的字符或字符串。

- replace的参数是`char或CharSequence`,即可以支持字符的替换,也支持字符串的替换。
- replaceAll的参数是regex,即`正规则表达式`,比如,可以通过replaceAll("\\d", "*")把一个字符串所有的数字字符都换成星号;
- 如果只想替换第一次出现的,可以使用replaceFirst(),这个方法也是基于规则表达式的替换。
- 如果replaceAll()和replaceFirst()所用的参数据不是基于规则表达式的,则与replace()替换字符串的效果是一样的,即这两者也支持字符串的操作;
- 还有一点注意:执行了替换操作后,源字符串的内容是没有发生改变的.

举例如下:
        
    String src = new String("aa33bb22cc44");

    System.out.println(src.replace("3","f"));        // aaffbb22cc44
    System.out.println(src.replace('3','f'));        // aaffbb22cc44
    System.out.println(src.replaceAll("\\d","f"));   // aaffbbffccff
    System.out.println(src.replaceAll("a","f"));     // ff33bb22cc44
    System.out.println(src.replaceFirst("\\d","f")); // aaf3bb22cc44
    System.out.println(src.replaceFirst("4","h"));   // aa33bb22cch4

    
    // 将多个连续的空格替换为一个空格
    String s1 = "Hey!  I    have    many    many     whitespaces.";
    String s2 = s1.replaceAll(" +", " ");
    // 如果既有空格也有tab键，可以这样，把所有空白字符转换为一个空格
    String s3 = s1.replaceAll("\\s+", " ");
