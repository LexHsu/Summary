
字符串
=========

### 字符串

- 字符串内部有一指针，一长度属性，指针指向 UTF-8 字节数组。

```c
// Go 中，string 源码：
struct String
{
    byte*   str; // byte 而不是 rune，因为 byte 使用更频繁
    int32   len;
};

// 字符串长度判断：
// byte：len                     // len(str) 的值便是字符串的字节长度
// rune：utf8.RuneCountInString  // unicode 字符个数
```
- go 中字符串为基本类型，C/C++ 中的“字符串”其实是字符数组。
- 字符串默认值为空串 ""。
- 用索引号访问某字节，如 s[i]。不能用序号获取某字节地址，如 &s[i] 非法。
- go 字节数组尾部不包含 '\0'。
- go 中字符串有两种表示方法，如下：

```go
// 1. 使用双引号，可以使用转义字符，不可跨行赋值
func main() {
    s := "ab\nc"
    println(s)
	println(s[0] == '\x61', s[1] == 'b', s[2] == '\n', s[3] == 0x63)
}

// Result:
ab
c
true true true true


// 2. 使用反引号(不是单引号)，转义字符不会做转义处理，可跨行赋值
s := `a
b\r\n\x00
c`
println(s)

// 输出：
a
b\r\n\x00
c

// 连接跨行字符串时，"+" 必须在上一行末尾，否则会导致编译错误。
s := "Hello, " +
     "World!"
s2 := "Hello, "
    + "World!"    // Error: invalid operation: + untyped string

```

- Go 语言中的字符串以 UTF-8 格式编码并存储：

```go
s := "Hello 世界！"
```

变量 s 中存放的是该字符串的 UTF-8 编码，使用 len(s) 函数获取字符串的 UTF-8 字节长度。对 Win 系统中的 ANSI 编码而言，存储一个 ASCII 字符需要一个字节，存储一个非 ASCII 字符需要两个字节，而在 Go 中，用 UTF-8 编码存放一个 ASCII 字符依然只需要一个字节，但存放一个非 ASCII 字符，可能需要 2 个或 3 个甚至 4 个字节，是不固定的。

既然 Go 中的字符串存放的是 UTF-8 编码，那么使用 s[0] 这样的下标方式获取到的内容就是 UTF-8 编码中的一个字节。对于非 ASCII 字符而言，这样的一个字节没有实际的意义，除非想编码或解码 UTF-8 字节流。

- 可通过两个索引号返回子串。子串依然指向原字节数组，仅修改了指针和长度属性。

```go
s := "Hello, World!"
s1 := s[:5]         // Hello
s2 := s[7:]         // World!
s3 := s[1:5]        // ello
```

### 字符

- Go 的字符类型是 rune 类型，rune 其实是 int32 的别名，Rune 类型可完整表示全部 Unicode 字符。
之所以用 rune 表示字符类型，只是为了方便与整型值区分而已。

```go
c1 := 'A'              // 单引号，可以使用转义字符
c2 := rune(65)         // 码点值，格式必须是 rune
```

- 字符支持 \uFFFF、\U7FFFFFFF、\xFF 表示方式。

```go
func main() {
    fmt.Printf("%T\n", 'a')
    var c1, c2 rune = '\u6211', '们'
    println(c1 == '我', string(c2) == "\xe4\xbb\xac")
}

// Result:
int32       // 可见 rune 是 int32 的别名
true true
```

- Go语言中的转义字符：

```
\a           // 匹配响铃符    （相当于 \x07）
\b           // 匹配退格符    （相当与 \x08）
\t           // 匹配制表符    （相当于 \x09）
\n           // 匹配换行符    （相当于 \x0A）
\v           // 匹配纵向制表符（相当于 \x0B）
\f           // 匹配换页符    （相当于 \x0C）
\r           // 匹配回车符    （相当于 \x0D）
\377         // 匹配 8  进制字符（长度固定为 3，最大值 377   ）
\xFF         // 匹配 16 进制字符（长度固定为 2，最大值 FF    ）
\uFFFF       // 匹配 16 进制字符（长度固定为 4，最大值 FFFF  ）
\U0010FFFF   // 匹配 16 进制字符（长度固定为 8，最大值 10FFFF）
```

### 修改字符串

- go 中字符串为不可变值类型，不可直接修改字符元素。要修改字符串，可先转换成 []rune 或 []byte，修改后再转回 string。

1. 对字符串中的字节进行修改，则转换为 []byte 格式。
2. 对字符串中的字符进行修改，则转换为 []rune 格式。

注意：无论哪种转换，都会重新分配内存，并复制字节数组。

```go
// 1. 修改字符串中的字节（[]byte）：
func main() {
	s := "Hello 世界"
	b := []byte(s)        // 自动复制数据，并转换为 []byte
	b[5] = ','            // 修改 []byte
	fmt.Printf("%s\n", s) // s 内容不变
	fmt.Printf("%s\n", b) // 修改后的数据
}
// Result:
Hello 世界
Hello,世界

// 2. 修改字符串中的字符（[]rune）：
func main() {
	s := "Hello 世界"
	r := []rune(s)         // 自动复制数据，并转换为 []rune
	r[6] = '中'            // 修改 []rune，注意索引
	r[7] = '国'            // 修改 []rune，注意索引
	fmt.Println(s)         // s 内容不变
	fmt.Println(string(r)) // 转换为字符串，又一次复制数据
}
// Result:
Hello 世界
Hello 中国

// 若想在 []byte 中处理 Rune 字符，需要用到 utf-8 包中的解码函数
package main
import "fmt"
import "unicode/utf8"

func main() {
	b := []byte("hello 世界")
	for len(b) > 0 {
		r, n := utf8.DecodeRune(b) // 解码 b 中的第一个字符
		fmt.Printf("%c\n", r)      // 显示读出的字符
		b = b[n:]                  // 丢弃已读取的字符
	}
}

// 输出
h
e
l
l
o

世
界

```

### 字符串遍历

- 用 for 循环遍历字符串时，有 byte 和 rune 两种方式。

```go
// 1. 遍历字符串中的字节，byte方式（for 语句）：
func main() {
	s := "Hello 世界！"
	for i, l := 0, len(s); i < l; i++ {
		fmt.Printf("%2v = %v, %c\n", i, s[i], s[i]) // 输出单个字节值
	}
}

// 输出
 0 = 72, H
 1 = 101, e
 2 = 108, l
 3 = 108, l
 4 = 111, o
 5 = 32,  
 6 = 228, ä
 7 = 184, ¸
 8 = 150, 
 9 = 231, ç
10 = 149, 
11 = 140, 
12 = 239, ï
13 = 188, ¼
14 = 129, 

// 可见，该字符串长度为15。虽然直观上是9个字符。因为每个中文字符在 UTF-8 中占 3 个字节，而不是 1 个字节

// 2. 遍历字符串中的 Unicode 字符，rune 方式（使用 for range 语句）：
func main() {
	s := "Hello 世界！"
	for i, v := range s {                 // i 是字符的字节位置，v 是字符的拷贝
		fmt.Printf("%2v = %c\n", i, v)    // 输出单个字符
	}
}

// 输出
 0 = H
 1 = e
 2 = l
 3 = l
 4 = o
 5 =  
 6 = 世
 9 = 界
 12 = ！
// 每个字符的类型是 rune，而不是 byte。注意索引 6，9，12
```
