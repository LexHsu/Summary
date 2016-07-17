
FileReader InputStreamReader BufferedReader区别
=========

ava.io下面有两个抽象类：InputStream和Reader
- InputStream是表示字节输入流的所有类的超类
- Reader是用于读取字符流的抽象类
- InputStream提供的是字节流的读取，而非文本读取，这是和Reader类的根本区别。

即用Reader读取出来的是char数组或者String ，使用InputStream读取出来的是byte数组。
弄清了两个超类的根本区别，再来看他们底下子类的使用，这里只对最常用的几个说明.
###FileInputStream
从文件系统中的某个文件中获得输入字节。
```
InputStream 
   |____FileInputStream 

构造方法
FileInputStream (File  file) 
    通过打开一个到实际文件的连接来创建一个 FileInputStream ，该文件通过文件系统中的 File 对象 file 指定。 
FileInputStream (FileDescriptor  fdObj) 
    通过使用文件描述符 fdObj 创建一个 FileInputStream ，该文件描述符表示到文件系统中某个实际文件的现有连接。 
FileInputStream (String  name) 
    通过打开一个到实际文件的连接来创建一个 FileInputStream ，该文件通过文件系统中的路径名 name 指定。 
 ```

###BufferedReader 
从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。
```
Reader
    |____BufferedReader 
    |____InputStreamReader 
        |____FileReader 

构造方法
BufferedReader (Reader  in) 
    创建一个使用默认大小输入缓冲区的缓冲字符输入流。 
BufferedReader (Reader  in, int sz) 
    创建一个使用指定大小输入缓冲区的缓冲字符输入流。 

BufferedReader的最大特点就是缓冲区的设置。
通常Reader 所作的每个读取请求都会导致对底层字符或字节流进行相应的读取请求，
如果没有缓冲，则每次调用 read() 都会导致从文件中读取字节，并将其转换为字符后返回，而这是极其低效的。 

使用BufferedReader可指定缓冲区的大小，或使用默认大小。
因此，建议用 BufferedReader 包装所有其 read() 操作可能开销很高的 Reader（如 FileReader 和InputStreamReader）: 
BufferedReader in = new BufferedReader(new FileReader("foo.in"));
将缓冲指定文件的输入。 
```

###InputStreamReader
```
字节流通向字符流的桥梁，它使用指定的 charset读取字节并将其解码为字符。
它使用的字符集可以由名称指定或显式给定，或者可以接受平台默认的字符集。

构造方法
InputStreamReader (InputStream  in) 
    创建一个使用默认字符集的 InputStreamReader。 
InputStreamReader (InputStream  in, Charset  cs) 
    创建使用给定字符集的 InputStreamReader。 
InputStreamReader (InputStream  in, CharsetDecoder  dec) 
    创建使用给定字符集解码器的 InputStreamReader。 
InputStreamReader (InputStream  in, String  charsetName) 
    创建使用指定字符集的 InputStreamReader。 
每次调用 InputStreamReader 中的一个 read() 方法都会导致从底层输入流读取一个或多个字节。
要启用从字节到字符的有效转换，可以提前从底层流读取更多的字节，使其超过满足当前读取操作所需的字节。 
为了达到最高效率，可要考虑在 BufferedReader 内包装 InputStreamReader。例如： 
BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
InputStreamReader最大的特点是可以指转换的定编码格式，这是其他类所不能的，从构造方法就可看出，
这一点在读取中文字符时非常有用。
```

###FileReader
```
InputStreamReader类的子类，所有方法（read()等）都从父类InputStreamReader中继承而来；
构造方法摘要  
FileReader (File  file) 
          在给定从中读取数据的 File 的情况下创建一个新 FileReader 。 
FileReader (FileDescriptor  fd) 
          在给定从中读取数据的 FileDescriptor 的情况下创建一个新 FileReader 。 
FileReader (String  fileName) 
          在给定从中读取数据的文件名的情况下创建一个新 FileReader  

该类与它的父类InputStreamReader的主要不同在于构造函数，主要区别也就在于构造函数！
从InputStreamReader的构造函数中看到，参数为InputStream和编码方式，可以看出，
当要指定编码方式时，必须使用InputStreamReader类；
而FileReader构造函数的参数与FileInputStream同，为File对象或表示path的String，
可以看出，当要根据File对象或者String读取一个文件时，用FileReader；

```
###总结
####1. 字符与字节： 
FileInputStream 类以二进制输入/输出，I/O速度快且效率搞，但是它的read（）方法读到的是一个字节（二进制数据），很不利于阅读，而且无法直接对文件中的字符进行操作，比如替换，查找（必须以字节形式操作）；而Reader类弥补了这个缺陷，可以以文本格式输入/输出，非常方便；比如可以使用while((ch = filereader.read())!=-1 )循环来读取文件；可以使用BufferedReader的readLine()方法一行一行的读取文本。
####2. 编码
InputStreamReader ，它是字节转换为字符的桥梁。 你可以在构造器重指定编码的方式，如果不指定的话将采用底层操作系统的默认编码方式，例如GBK等。 
FileReader与InputStreamReader 涉及编码转换(指定编码方式或者采用os默认编码)，可能在不同的平台上出现乱码现象！而FileInputStream 以二进制方式处理，不会出现乱码现象. 因此要指定编码方式时，必须使用InputStreamReader 类，所以说它是字节转换为字符的桥梁；
####3. 缓存区
BufferReader类用来包装所有其 read() 操作可能开销很高的 Reader（如 FileReader 和InputStreamReader）。
####4. 规范用法
总结以上内容，得出比较好的规范用法：

```java
// 推荐，指定了字符编码
File file = new File ("hello.txt"); 
FileInputStream in = new FileInputStream (file); 
InputStreamReader inReader = new InputStreamReader (in, "UTF-8"); 
BufferedReader bufReader = new BufferedReader(inReader); 

File file = new File ("hello.txt"); 
FileInputStream in = new FileInputStream(file); 

File file = new File ("hello.txt"); 
FileReader fileReader = new FileReader(file); 
BufferedReader bufReader = new BufferedReader(fileReader);
```
