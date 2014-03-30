
Java String.split()用法小结
=========

@[split|java]

JDK文档中split的方法如下：

    public String[] split(String regex)
        Splits this string around matches of the given regular expression. 
注意参数regex是一个正规则表达式(regular-expression)，不是简单的字符串(String)，因此，对特殊字符的使用需要注意，比如用竖线|分隔字符串：

    public static void main(String[] args) {
        String str = "aa|bb|cc";
        String[] strArray = str.split("|");
        // String[] strArray = str.split("."); // strArray为一个空数组[]
        // String[] strArray = str.split("\\."); // strArray相当于str未变，可见对于特殊字符分隔需要注意，防止意外
        // String[] strArray = str.split("d"); // strArray还是相当于str
        for (String s : strArray) {
            System.out.println(s);
        }
    }
    // 输出等同于str.split("")：
    a
    a
    |
    b
    b
    |
    c
    c

    // 不是我们想要的结果，正确方式是：
    public static void main(String[] args) {
        String str = "aa|bb|cc";
        String[] strArray = str.split("|");
        for (String s : strArray) {
            System.out.println(s);
        }
    }
    // 输出结果：
    aa
    bb
    cc

    // 注意“+”与“*”等符号不是有效的模式匹配规则表达式，分隔字符串运行将抛出java.util.regex.PatternSyntaxException异常，也需用"\\*" "\\+"转义。
    // 如果想在串中使用"\"字符，则也需要转义.首先要表达"aaaa\bbbb"这个串就应该用"aaaa\\bbbb",
    // 如果要分隔就应该这样才能得到正确结果：String[] aa = "aaa\\bbb\\bccc".split("\\\\");

    // 若在一个字符串中有多个分隔符，可以用“|”作为连字符，比如：“acount=? and uu =? orn=?”,
    // 把三个都分隔出来，可以用String.split("and|or");
    public static void main(String[] args) {
        String str = "acount=?anduu=?orn=?";
        String[] strArray = str.split("and|or");
        for(String s : strArray) {
            System.out.println(s);
        }
    }
    结果为
    acount=?
    uu=?
    n=?
    
    // 若字符串含有多个空格及tab键，可用split("\\s+"),按空白部分进行拆分，不管是几个空格，也不管是空格还是tab键，如下：
    public static void main(String[] args) {
        String str = "acount=?   and   		  uu =?   or   n=?";
        String[] strArray = str.split("\\s+");
        for(String s : strArray) {
            System.out.println(s);
        }
    }
    例子中的空白部分既有空格，也有tab键，结果为
    acount=?
    and
    uu
    =?
    or
    n=?


