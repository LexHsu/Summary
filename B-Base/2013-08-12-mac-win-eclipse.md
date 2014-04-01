ndows和Mac下Eclipse快捷键
=========

@(H-Base)[eclipse|Windows|Mac]

##【Windows系统】
###常用快捷键
    Ctrl+1 快速修复(最经典的快捷键,就不用多说了)
    Ctrl+D: 删除当前行
    Ctrl+Alt+↓ 复制当前行到下一行(复制增加)
    Ctrl+Alt+↑ 复制当前行到上一行(复制增加)
    Alt+↓ 当前行和下面一行交互位置(特别实用,可以省去先剪切,再粘贴了)
    Alt+↑ 当前行和上面一行交互位置(同上)
    Alt+← 前一个编辑的页面
    Alt+→ 下一个编辑的页面(当然是针对上面那条来说了)
    Alt+Enter 显示当前选择资源(工程,or 文件 or文件)的属性
    Shift+Enter 在当前行的下一行插入空行(这时鼠标可以在当前行的任一位置,不一定是最后)
    Shift+Ctrl+Enter 在当前行插入空行(原理同上条)
    Ctrl+Q 定位到最后编辑的地方
    Ctrl+L 定位在某行 (对于程序超过100的人就有福音了)
    Ctrl+M 最大化当前的Edit或View (再按则反之)
    Ctrl+/ 注释当前行,再按则取消注释(Ctrl+/为//~注释，Ctrl+Shift+/为/*~*/注释)
    Ctrl+O 快速显示 OutLine
    Ctrl+T 快速显示当前类的继承结构
    Ctrl+W 关闭当前Editer
    Ctrl+K 参照选中的Word快速定位到下一个
    Ctrl+E 快速显示当前Editer的下拉列表(如果当前页面没有显示的用黑体表示)
    Ctrl+/(小键盘) 折叠当前类中的所有代码
    Ctrl+×(小键盘) 展开当前类中的所有代码
    Ctrl+Space 代码助手完成一些代码的插入(但一般和输入法有冲突,可以修改输入法的热键,也可以暂用Alt+/来代替)
    Ctrl+Shift+E 显示管理当前打开的所有的View的管理器(可以选择关闭,激活等操作)
    Ctrl+J 正向增量查找(按下Ctrl+J后,你所输入的每个字母编辑器都提供快速匹配定位到某个单词,
           如果没有,则在stutes line中显示没有找到了,查一个单词时,特别实用,这个功能Idea两年前就有了)
    Ctrl+Shift+J 反向增量查找(和上条相同,只不过是从后往前查)
    Ctrl+Shift+F4 关闭所有打开的Editer
    Ctrl+Shift+X 把当前选中的文本全部变味小写
    Ctrl+Shift+Y 把当前选中的文本全部变为小写
    Ctrl+Shift+F 格式化当前代码
    Ctrl+Shift+P 定位到对于的匹配符(譬如{}) (从前面定位后面时,光标要在匹配符里面,后面到前面,则反之)
    
    常用重构快捷键(注:一般重构的快捷键都是Alt+Shift开头)
    Alt+Shift+R 重命名 (是我自己最爱用的一个了,尤其是变量和类的Rename,比手工方法能节省很多劳动力)
    Alt+Shift+M 抽取方法 (这是重构里面最常用的方法之一了,尤其是对一大堆泥团代码有用)
    Alt+Shift+C 修改函数结构(比较实用,有N个函数调用了这个方法,修改一次搞定)
    Alt+Shift+L 抽取本地变量( 可以直接把一些魔法数字和字符串抽取成一个变量,尤其是多处调用的时候)
    Alt+Shift+F 把Class中的local变量变为field变量 (比较实用的功能)
    Alt+Shift+I 合并变量(可能这样说有点不妥Inline)
    Alt+Shift+V 移动函数和变量(不怎么常用)
    Alt+Shift+Z 重构的后悔药(Undo)

###编辑
    作用域 功能 快捷键
    全局 查找并替换 Ctrl+F
    文本编辑器 查找上一个 Ctrl+Shift+K
    文本编辑器 查找下一个 Ctrl+K
    全局 撤销 Ctrl+Z
    全局 复制 Ctrl+C
    全局 恢复上一个选择 Alt+Shift+↓
    全局 剪切 Ctrl+X
    全局 快速修正 Ctrl1+1
    全局 内容辅助 Alt+/
    全局 全部选中 Ctrl+A
    全局 删除 Delete
    全局 上下文信息 Alt+？
    Alt+Shift+?
    Ctrl+Shift+Space
    Java编辑器 显示工具提示描述 F2
    Java编辑器 选择封装元素 Alt+Shift+↑
    Java编辑器 选择上一个元素 Alt+Shift+←
    Java编辑器 选择下一个元素 Alt+Shift+→
    文本编辑器 增量查找 Ctrl+J
    文本编辑器 增量逆向查找 Ctrl+Shift+J
    全局 粘贴 Ctrl+V
    全局 重做 Ctrl+Y

###查看
    作用域 功能 快捷键
    全局 放大 Ctrl+=
    全局 缩小 Ctrl+-

###窗口
    作用域   功 能           快捷键
    全局     激活编辑器         F12
    全局     切换编辑器         Ctrl+Shift+W
    全局     上一个编辑器     Ctrl+Shift+F6
    全局     上一个视图         Ctrl+Shift+F7
    全局     上一个透视图     Ctrl+Shift+F8
    全局     下一个编辑器     Ctrl+F6
    全局     下一个视图         Ctrl+F7
    全局     下一个透视图     Ctrl+F8
    文本    编辑器 显示标尺上下文菜单 Ctrl+W
    全局     显示视图菜单     Ctrl+F10
    全局     显示系统菜单     Alt+-

###导航
    作用域 功能 快捷键
    Java编辑器 打开结构 Ctrl+F3
    全局 打开类型 Ctrl+Shift+T
    全局 打开类型层次结构 F4
    全局 打开声明 F3，即跳到声明或定义的地方。
    全局 打开外部javadoc Shift+F2
    全局 打开资源 Ctrl+Shift+R
    全局 后退历史记录 Alt+←
    全局 前进历史记录 Alt+→
    全局 上一个 Ctrl+,
    全局 下一个 Ctrl+.
    Java编辑器 显示大纲 Ctrl+O
    全局 在层次结构中打开类型 Ctrl+Shift+H
    全局 转至匹配的括号 Ctrl+Shift+P
    全局 转至上一个编辑位置 Ctrl+Q
    Java编辑器 转至上一个成员 Ctrl+Shift+↑
    Java编辑器 转至下一个成员 Ctrl+Shift+↓
    文本编辑器 转至行 Ctrl+L

###搜索
    作用域 功能 快捷键
    全局 出现在文件中 Ctrl+Shift+U
    全局 打开搜索对话框 Ctrl+H
    全局 工作区中的声明 Ctrl+G
    全局 工作区中的引用 Ctrl+Shift+G

###文本编辑
    作用域 功能 快捷键
    文本编辑器 改写切换 Insert
    文本编辑器 上滚行 Ctrl+↑
    文本编辑器 下滚行 Ctrl+↓

###文件
    作用域 功能 快捷键
    全局 保存 Ctrl+X
    Ctrl+S
    全局 打印 Ctrl+P
    全局 关闭 Ctrl+F4
    全局 全部保存 Ctrl+Shift+S
    全局 全部关闭 Ctrl+Shift+F4
    全局 属性 Alt+Enter
    全局 新建 Ctrl+N

###项目
    作用域 功能 快捷键
    全局 全部构建 Ctrl+B

###源代码
    作用域 功能 快捷键
    Java编辑器 格式化 Ctrl+Shift+F
    Java编辑器 取消注释 Ctrl+\
    Java编辑器 注释 Ctrl+/
    Java编辑器 添加导入 Ctrl+Shift+M
    Java编辑器 组织导入 Ctrl+Shift+O
    Java编辑器 使用try/catch块来包围 未设置，太常用了，所以在这里列出,建议自己设置。
    也可以使用Ctrl+1自动修正。

###调试
    作用域 功能 快捷键
    全局 单步返回 F7，即由函数内部返回到调用处
    全局 单步跳过 F6，即单步调试不进入函数内部
    全局 单步跳入 F5，即单步调试进入函数内部。
    全局 单步跳入选择 Ctrl+F5
    全局 调试上次启动 F11
    全局 继续 F8，即一直执行到下一个断点。
    全局 使用过滤器单步执行 Shift+F5
    全局 添加/去除断点 Ctrl+Shift+B
    全局 显示 Ctrl+D
    全局 运行上次启动 Ctrl+F11
    全局 运行至行 Ctrl+R
    全局 执行 Ctrl+U

###重构
    作用域 功能 快捷键
    全局 撤销重构 Alt+Shift+Z
    全局 抽取方法 Alt+Shift+M
    全局 抽取局部变量 Alt+Shift+L
    全局 内联 Alt+Shift+I
    全局 移动 Alt+Shift+V
    全局 重命名 Alt+Shift+R
    全局 重做 Alt+Shift+Y

###其他
    右击窗口的左边框（即加断点的地方），选Show Line Numbers可以快速加行号。
    当鼠标放在一个标记处出现Tooltip时候按F2则把鼠标移开时Tooltip还会显示即Show Tooltip Description。
    
    elipse快速生成getter和setter方法
    在指定类的内部，右击—>Source—>Generate Getters and  Setters
    快捷键alt+shift+s
    
    设置eclipse Console控制台字体大小
    window -> preferences -> general -> appearance -> colors and fonts -> debug - console font

##【Mac系统】
    Command + Shift + r 查找类 
    Command + Shift + o 自动加载（减）类路径
    Command + O：显示大纲 
    Command + 1：快速修复 
    Command + D：删除当前行 
    Command + Option + ↓：复制当前行到下一行 
    Command + Option + ↑：复制当前行到上一行 
    Option + ↓：当前行和下面一行交互位置 
    Option + ↑：当前行和上面一行交互位置 
    Option + ←：前一个编辑的页面 
    Option + →：下一个编辑的页面 
    Option + Return：显示当前选择资源的属性 
    Shift + Return：在当前行的下一行插入空行 
    Shift + Control + Return：在当前行插入空行 
    Control + Q：定位到最后编辑的地方 
    Control + M：最大化当前的Edit或View（再按则最小化） 
    Control + /：注释当前行，再按则取消注释 
    Command + T：快速显示当前类的继承结构 
    Command + W：关闭当前Editer 
    Command + K：参照当前选中的Word快速定位到下一个 
    Command + E：快速显示当前Editer的下拉列表（如果当前页面没有显示的用黑体表示） 
    Option + /：代码助手完成一些代码的插入（俗称“智能提示”） 
    Command + Shift + E：显示管理当前打开的所有的View的管理器 
    Command + J：正向增量查找（按下Command + J后，你所输入的每个字母编辑器都提供快速匹配定位到某个单词，
                 如果没有，则在Stutes Line中显示没有找到了） 
    Command + Shift + J：反向增量查找 
    Command + Shift + W：关闭所有打开的Editer 
    Command + Shift + X：把当前选中的文本全部变为大写 
    Command + Shift + Y：把当前选中的文本全部变为小写 
    Command + Shift + F：格式化当前代码 
    Command + Shift + P：定位到对于的匹配符（譬如{}）（从前面定位后面时，光标要在匹配符里面，后面到前面，则反之） 
    Option + Command + R：重命名（尤其是变量和类的Rename效果比较明显） 
    Option + Shift + M：抽取方法（这是重构里面最常用的方法之一了，尤其是对一大堆泥团代码有用） 
    Option + Command + C：修改函数结构（有N个函数调用了这个方法，修改一次就搞定） 
    Option + Command + L：抽取本地变量（可以直接把一些魔法数字和字符串抽取成一个变量，尤其是多处调用的时候） 
    Option + Shift + F：把Class中的Local变量变为Field变量（比较实用的功能） 
    Option + Command + Z：重构的后悔药（Undo）

