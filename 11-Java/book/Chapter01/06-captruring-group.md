
捕获组与非捕获组
=========

###捕获组(capturing group)
Capturing group是用来对付作为一个整体出现的多个字符。可通过使用()建立一个group。输入字符串中和capturing group相匹配的部分将保存在内存里，并且可以通过使用Backreference调用。可以使用matcher.groupCount方法获得一个正则pattern中capturing groups的数目。
例如((a)(bc))包含3个capturing groups; ((a)(bc)), (a) 和 (bc)。

你可以使用在正则表达式中使用Backreference，一个反斜杠(\)接要调用的group号码。

Capturing groups和Backreferences可能很令人困惑，所以我们通过一个例子来理解。
```java
System.out.println(Pattern.matches("(\\w\\d)\\1", "a2a2")); //true
System.out.println(Pattern.matches("(\\w\\d)\\1", "a2b2")); //false
System.out.println(Pattern.matches("(AB)(B\\d)\\2\\1", "ABB2B2AB")); //true
System.out.println(Pattern.matches("(AB)(B\\d)\\2\\1", "ABB2B3AB")); //false
```
在第一个例子里，运行的时候第一个capturing group是(\w\d)，在和输入字符串“a2a2″匹配的时候获取“a2″并保存到内存里。因此\1是”a2”的引用，因此第一个参数的匹配表达式其实是a2a2，所以结果返回true。其他例子原理相同。

###非捕获组(non-capturing)
####一、 先从(?:)非捕获组说起。

下面由一个例子引出非捕获组。

有两个金额：8899￥ 和 6688$ 。显然，前一个是8899元的人民币，后一个是6688元的美元。我现在需要一个正则，要求提炼出它们的货币金额和货币种类。
```java 
// 正则可以这写：(\\d)+([￥$])$  (在java中测试，所以多了转义字符'\')
Pattern p = Pattern.compile("(\\d+)([￥$])$");  
String str = "8899￥";  
Matcher m = p.matcher(str);  
if (m.matches()) {  
    System.out.println("货币金额: " + m.group(1));  
    System.out.println("货币种类: " + m.group(2));  
}  

// 输出结果为：
// 货币金额: 8899
// 货币种类: ￥

// 这里的正则分成了两个组，一个是(\\d+)，一个是([￥$])，前一个组匹配货币金额，后一个组匹配货币种类。
```

现在，我需要这个正则可以匹配浮点数。如8899.56￥。我们都知道，现在少于一元钱基本上买不到东西了，所以我希望忽略小数部分，正则还是提炼出 8899 和 ￥。
那么正则如下：
```
(\\d+)(\\.?)(\\d+)([￥$])$
```
这里用括号分了四组，所以要输出货币金额的整数部分和货币种类，要分别输了group(1),group(4)了。如果输出部分和正则是分开的，我希望只修改正则而不去修改输出部分的代码，也就是还是用group(1)，group(2)作为输出。由此可以引出非捕获组(?:)。
把前面的正则修改为：
```
(\\d+)(?:\\.?)(?:\\d+)([￥$])$
```
这样，还是用group(1),group(2)做为输出，同样输出了 8899 和 ￥
这个正则的中间两个组用到的就是非捕获组（?:），它可以理解为只分组而不捕获。
####二、(?=)和(?<=)
有的资料把它们叫做肯定式向前查找和肯定式向后查找；
有的资料也叫做肯定顺序环视和肯定逆序环视。

1、(?=)

看下面的例子：
```java
Pattern p = Pattern.compile("[0-9a-z]{2}(?=aa)");  
String str = "12332aa438aaf";  
   
Matcher m = p.matcher(str);  
while (m.find()) {  
    System.out.println(m.group());  
}  

// 输出32 38
```
这个正则的意思是：匹配这么一个字符串，它要满足：是两位字符（数字，或字母），且`后面`紧跟着两个a。

分析一下，32aa  这个子串 满足这个条件，所以可以匹配到，又因为 (?=) 的部分是不捕获的，所以输出的只是 32，不包括aa。同理 38aa 也匹配这个正则，而输出仅是 38。再深入看一下,当str第一次匹配成功输出 32 后，程序要继续向后查找是否还有匹配的其它子串。那么这时应该从 32aa 的后一位开始向后查找，还是从 32 的后一位呢？也就是从索引 5 开始还是从 7 开始呢？有人可能想到是从 32aa 的下一位开始往后找，因为 32aa 匹配了正则，所以下一位当然是它的后面也就是从 4 开始。但实际上是从 32 的后一位也就是第一个 a 开始往后找。原因还是 (?=) 是非捕获的。

查阅API文档是这么注释的：(?=X) X, via zero-width positive lookahead。可见zero-width（零宽度）说的就是这个意思。

现在，把字符串写的更有意思些：str = "aaaaaaaa";
看一下它的输出： aa aa aa
分析一下：
这个字符串一共有8个a。
1. 第一次匹配比较容易找到，那就是前四个：aaaa ,当然第三和第四个 a 是不捕获的，所以输出是第一和第二个a；
2. 接着继续查找，这时是从第三个a开始，三到六，这4个a区配到了，所以输出第三和第四个a；
3. 接着继续查找，这时是从第五个a开始，五到八，这4个a区配到了，所以输出第五和第六个a；
4. 接着往后查找，这时是从第七个a开始，显然，第七和第八个a,不满足正则的匹配条件，查找结束。

我们再延伸一下，刚说的情况的是(?=)放在捕获的字符串后面，它如果放在前面又是什么结果呢？
例子换成：
```java
Pattern p = Pattern.compile("(?=hopeful)hope");  
String str = "hopeful";  
Matcher m = p.matcher(str);  
while(m.find()){  
    System.out.println(m.group());  
}  

// 它的输出是hope。
// 正则的意思是：是否能匹配hopeful,如果能，则捕获hopeful中的hope。当然继续向后查找匹配的子串，是从f开始。
// 比较一下可以看出，(?=hopeful)hope 和 hope(?=ful),两个正则的效果其实是一样的。
```

2、(?<=)

把正则改一下，
```java
Pattern p = Pattern.compile("(?<=aa)[0-9a-z]{2}");
// 字符串还是str = "12332aa438aaf";
// 它的输出：43。
```
这个正则的意思是：匹配这么一个字符串，它要满足：是两位字符（数字或字母），且`前面`紧跟的是两个字母 a 。
同样，深入一下，把str换成str = "aaaaaaaa";看一下输出是什么，同样也是：aa aa aa
分析一下：
1. 第一次匹配不用说，是前四个a，输出的是第三和第四个a;
2. 继续向后查找，从第五个a开始，程序发现，第五个和第六个a满足，因为是两位字符，且满足前面紧跟着两个a(第三和第四个a)。所以匹配成功，输出第五个和第六个a;
3. 继续向后查找，从第七个a开始，程序发现，第七个和第八个a满足，因为是两位字符，且满足前面紧跟着两个a(第五和第六个a)。所以匹配成功，输出第七和第八个a。查找结束。

####三、(?!)和(?<!)
从外观上看，和前面一组很相似，区别就是把 ‘=’ 换成了 ‘!’
那么意义刚好也是相反的。
[0-9a-z]{2}(?!aa)    意思是：匹配两个字符，且后面紧跟着的不是aa
(?<=aa)[0-9a-z]{2}  意思是：匹配两个字符，且前面紧跟着的不是aa
用法和前面讲的差不多，这里不再详述。

###Pattern和Matcher类中一些重要的方法。

可以创建一个带有标志的Pattern对象。例如
1. Pattern.CASE_INSENSITIVE可以进行大小写不敏感的匹配。
2. Pattern类同样提供了和String类相似的split(String) 方法
3. Pattern类toString()方法返回被编译成这个pattern的正则表达式字符串。
4. Matcher类有start()和end()索引方法，可以显示从输入字符串中匹配到的准确位置。
5. Matcher类同样提供了字符串操作方法replaceAll(String replacement)和replaceFirst(String replacement)。
6. Matcher.groupCount不是计算一个字符串中满足匹配表达式的子串的个数，而是正则表达式捕获组的个数

例：
```java
package com.journaldev.util;
 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class RegexExamples {
 
    public static void main(String[] args) {
        // using pattern with flags
        Pattern pattern = Pattern.compile("ab", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("ABcabdAb");
        // using Matcher find(), group(), start() and end() methods
        while (matcher.find()) {
            System.out.println("Found the text \"" + matcher.group()
                    + "\" starting at " + matcher.start()
                    + " index and ending at index " + matcher.end());
        }
 
        // using Pattern split() method
        pattern = Pattern.compile("\\W");
        String[] words = pattern.split("one@two#three:four$five");
        for (String s : words) {
            System.out.println("Split using Pattern.split(): " + s);
        }
 
        // using Matcher.replaceFirst() and replaceAll() methods
        pattern = Pattern.compile("1*2");
        matcher = pattern.matcher("11234512678");
        System.out.println("Using replaceAll: " + matcher.replaceAll("_"));
        System.out.println("Using replaceFirst: " + matcher.replaceFirst("_"));
    }
 
}

// 输出：
Found the text "AB" starting at 0 index and ending at index 2
Found the text "ab" starting at 3 index and ending at index 5
Found the text "Ab" starting at 6 index and ending at index 8
Split using Pattern.split(): one
Split using Pattern.split(): two
Split using Pattern.split(): three
Split using Pattern.split(): four
Split using Pattern.split(): five
Using replaceAll: _345_678
Using replaceFirst: _34512678

```

###Pattern.split与String.split区别
其实String.split的实现最终还是调用Pattern.split方法，如果需要频繁的split，如循环内split，基于性能上的考虑，还是新建Pattern，然后使用Pattern.split。
