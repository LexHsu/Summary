
字节流与字符流
=========

###概念
Java中IO流分为字符流与字节流。

- 字符流处理的单元为2个字节的Unicode字符，分别操作字符、字符数组或字符串。如操作文本文件，常用字符流。
- 字节流处理单元为1个字节，操作字节和字节数组。如果是音频文件、图片、歌曲，常用字节流。
- 所有文件（包括文本文件）的储存是都是字节（byte）储存，文本文件也不是字符存储，而是先把字符编码成字节后再储存到磁盘。
- 在读取文件（特别是文本文件）时，也是一个字节一个字节地读取以形成字节序列，对于文本文件Java虚拟机可转换为字符。
- 字节流可用于任何类型的对象，包括二进制对象，而字符流只能处理字符或者字符串。
- 字节流提供了处理任何类型的IO操作的功能，但它不能直接处理Unicode字符，而字符流就可以。


###IO流
- 字节流抽象类 InputStream OutputStream，字节流实现类 FileInputStream FileOutputStream 

```
InputStream
    |____FileInputStream
OutputStream
    |____FileOutputStream
```
- 字符流抽象类 Reader Writer，字符流实现类 FileReader FileWriter，带缓冲区的字符流BufferdReader BufferedWriter
- OutputStreamWriter：Writer的子类，将输出的字符流变为字节流，即将一个字符流的输出对象变为字节流输出对象。
- InputStreamReader：Reader的子类，将输入的字节流变为字符流，即将一个字节流的输入对象变为字符流的输入对象。

```
Writer
    |____OutputStreamWriter
        |____FileWriter
Reader
    |____InputStreamReader
        |____FileReader
以文件操作为例，内存中的字符数据需要通过OutputStreamWriter变为字节流才能保存在文件中，
读取时需要将读入的字节流通过InputStreamReader变为字符流，转换步骤如下：
写入数据：程序---->字符流---->OutputStreamWriter---->字节流---->文件
读取数据：文件---->字节流---->InputStreamReader---->字符流---->程序
```

###转换
字节流转化为字符流，实际上是byte[]转化为String
```java
public String(byte bytes[], String charsetName) // 通常省略参数字符集编码，默认用操作系统的编码方式
```
字符流转化为字节流，实际上是String转化为byte[]
```java
byte[] String.getBytes(String charsetName)
```

###IO流使用原则
```
原则一、按数据来源（去向）分类：
1.是文件： FileInputStream, FileOutputStream, ( 字节流 )FileReader, FileWriter( 字符流)
2.是 byte[] ： ByteArrayInputStream, ByteArrayOutputStream( 字节流 )
3.是 Char[]: CharArrayReader, CharArrayWriter( 字符流 )
4.是 String: StringBufferInputStream, StringBufferOuputStream ( 字节流 )StringReader, StringWriter( 字符流 )
5.网络数据流： InputStream, OutputStream,( 字节流 ) Reader, Writer( 字符流 )

原则二、按是否格式化输出分：
1.要格式化输出： PrintStream, PrintWriter

原则三、按是否要缓冲分：
1.要缓冲： BufferedInputStream, BufferedOutputStream,( 字节流 ) BufferedReader, BufferedWriter( 字符流 )

原则四、按数据格式分：
1.二进制格式（只要不能确定是纯文本的） : InputStream, OutputStream 及其所有带 Stream 结束的子类
2.纯文本格式（含纯英文与汉字或其他编码方式）； Reader, Writer 及其所有带 Reader, Writer 的子类

原则五、按输入输出分：
1.输入： Reader, InputStream 类型的子类
2.输出： Writer, OutputStream 类型的子类

原则六、特殊需要：
1.从 Stream 到 Reader,Writer 的转换类： InputStreamReader, OutputStreamWriter
2.对象输入输出： ObjectInputStream, ObjectOutputStream
3.进程间通信： PipeInputStream, PipeOutputStream, PipeReader, PipeWriter
4.合并输入： SequenceInputStream
5.更特殊的需要： PushbackInputStream, PushbackReader, LineNumberInputStream, LineNumberReader

通常使用原则如下：
1.考虑最原始的数据格式是什么： 原则四
2.是输入还是输出：原则五
3.是否需要转换流：原则六第1条
4.数据来源（去向）是什么：原则一
5.是否要缓冲：原则三 （特别注明：一定要注意的是 readLine() 是否有定义，有什么比 read, write 更特殊的输入或输出方法）
6.是否要格式化输出：原则二
```

###示例
1.读取文件，从字节流输入到字符流输入
```java
try {
    // 定义一个指向d:/text.txt的字节流 
    FileInputStream fileInputStream = new FileInputStream("d:" + File.separator + "test.txt");
    // 字节流转换成InputStreamReader 
    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream); 
    // InputStreamReader转换为带缓存的bufferedReader 
    BufferedReader bufferedReader = new BufferedReader(inputSteamReader); 
    String s = null; 
    while ((s = bufferedReader.readLine()) != null) { 
        System.out.println(s);
    }
} catch (IOException e) {
    e.printStackTrace();
} finally {
    fileInputStream.close();
}
```
2.写入文件 从字节流输出到字符流输出 
```java
try {
    // 定义一个指向d:/text.txt文件的字节流
    FileOutputStream fileOutputStream = new FileOutputStream("d:" + File.separator + "test.txt");
    // 字节流转换为OutputStreamReader
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream); 
    // OutputStreamReader转换为带缓存的bufferedReader 
    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter); 
    bufferedWriter.write(s); 
} catch (IOException e) {
    e.printStackTrace();
} finally {
    bufferedWriter.close(); 
    outputStreamWriter.close(); 
    fileOutputStream.close();
}
```

###字节流
1.输出流FileOutputStream
```java
public static void main(String[] args) throws IOException {
    File f = new File("d:" + File.separator + "test.txt");
    OutputStream out=new FileOutputStream(f); // 如果文件不存在会自动创建，覆盖原文件内容
    // OutputStream out=new FileOutputStream(f,true); // 追加内容
    String str="Hello World";
    // 因为是字节流，所以要转化成字节数组进行输出
    byte[] b = str.getBytes();
    out.write(b);
    out.close();
}

    // 也可以单字节输出
    public static void main(String[] args) throws IOException {
    File f = new File("d:" + File.separator + "test.txt");
    OutputStream out=new FileOutputStream(f); // 如果文件不存在会自动创建，覆盖原文件内容
    // OutputStream out=new FileOutputStream(f,true); // 追加内容
    String str="Hello World";
    byte[] b=str.getBytes();
    for(int i = 0; i < b.length; i++){
        out.write(b[i]);
    }
    out.close();
}
```
2.输入流FileInputStream
```java
public static void main(String[] args) throws IOException {
    File f = new File("d:" + File.separator + "test.txt");
    InputStream in=new FileInputStream(f);
    byte[] b=new byte[1024];
    int len=in.read(b);
    in.close();
    System.out.println(new String(b,0,len));
}

// 也可以单字节输入
public static void main(String[] args) throws IOException {
File f = new File("d:" + File.separator + "test.txt");
InputStream in=new FileInputStream(f);
byte[] b=new byte[1024];
int temp=0;
int len=0;
while((temp=in.read())!=-1){//-1为文件读完的标志
    b[len]=(byte) temp;
    len++;
}
in.close();
System.out.println(new String(b,0,len));
}
```
###字符流
1.输出流FileWriter
```java
    public static void main(String[] args) throws IOException {
        File f = new File("d:" + File.separator+"test.txt");
        Writer out=new FileWriter(f);
        // Writer out=new FileWriter(f,true); // 追加内容
        String str="Hello World";
        out.write(str);
        out.close();
    }
```
2.输入流FileReader
```java
public static void main(String[] args) throws IOException {
    File f = new File("d:" + File.separator+"test.txt");
    Reader input=new FileReader(f);
    char[] c=new char[1024];
    int len=input.read(c);
    input.close();
    System.out.println(new String(c,0,len));
}

// 也可用循环读取，判断是否读到底
public static void main(String[] args) throws IOException {
    File f = new File("d:" + File.separator+"test.txt");
    Reader input=new FileReader(f);
    char[] c=new char[1024];
    int temp=0;
    int len=0;
    while((temp=input.read())!=-1){
        c[len]=(char) temp;
        len++;
    }
    input.close();
    System.out.println(new String(c,0,len));
}
```
###字节流与字符流区别
字节流和字符流使用是非常相似的，那么除了操作代码的不同之外，还有哪些不同呢？

1.使用字节流不关闭代码如下：
```java
public static void main(String[] args) throws Exception {
     File f = new File("d:" + File.separator + "test.txt");
     OutputStream out = new FileOutputStream(f);    
     String str = "Hello World!!!";    
     byte b[] = str.getBytes();        
    // 字符串转byte数组  
     out.write(b);                    
    // 此时没有关闭  
    // out.close();                
}

```
结果显示，test.txt文件中也依然存在了输出的内容，证明字节流是直接操作文件本身的。

2.使用字符流不执行关闭操作
```java
public static void main(String[] args) throws Exception {
     File f = new File("d:" + File.separator + "test.txt");
     Writer writer = new FileWriter(f);
     String str = "Hello World!!!";    
    // 字符串转byte数组  
     writer.write(b);                    
    // 此时没有flush  
    // writer.flush(); 
    // 此时没有close
    // writer.close();                
}
```
结果显示，test.txt文件没有内容，放开flush或close就有内容。

可见，字节流在操作的时候本身是不会用到缓冲区（内存）的，是与文件本身直接操作的，而字符流在操作的时候是使用到缓冲区的。字节流在操作文件时，即使不关闭资源（close方法），文件也能输出，但是如果字符流不使用close方法活flush方法的话，则不会输出任何内容，说明字符流用的是缓冲区，并且可以使用flush方法强制清空缓冲区，这时才能在不close的情况下输出内容。
- flush() 刷新该流的缓冲，将缓冲区的数据刷到目的地。
- close() 关闭流资源，但是关闭之前会刷新一次内部的缓冲中的数据，将数据刷到目的地中。
- 区别：flush刷新后流可以继续使用，close刷新后会将流关闭，无法再调用flush。

```
字节流输出：程序---->字节流---->文件
字符流输出：程序---->字符流---->缓冲区---->文件
```
