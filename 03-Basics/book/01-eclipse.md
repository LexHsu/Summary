
Mac及Windows下Eclipse快捷键
=========

##Windows系统
    Ctrl+Shift+L: 查看快捷键
    Ctrl+1: 快速修复
    Ctrl+D: 删除当前行
    Ctrl+Alt+↓: 复制当前行到下一行(复制增加)
    Ctrl+Alt+↑: 复制当前行到上一行(复制增加)
    Alt+↓: 光标所在行和下一行交换位置
    Alt+↑: 光标所在行和上一行交换置
    Alt+←: 前一个编辑的页面
    Alt+→: 下一个编辑的页面(当然是针对上面那条来说了)
    Alt+/: 代码补全(调用方法，属性等等)
    Alt+Enter: 显示当前选择资源(工程,or 文件 or文件)的属性
    Shift+Enter: 在当前行的下一行插入空行(光标可在当前行任意位置,不需要在末尾)
    Shift+Ctrl+Enter: 在当前的上一行插入空行
    Ctrl+Q: 定位到最后编辑的地方
    Ctrl+L: 定位到指定行
    Ctrl+M: 最大化当前的Edit或View (再按则反之)
    Ctrl+/: 注释当前行,再按则取消注释(Ctrl+/为//~注释，Ctrl+Shift+/为/*~*/注释)
    Ctrl+O: 快速显示 OutLine
    Ctrl+T: 快速显示当前类的继承结构
    Ctrl+W: 关闭当前Editer
    Ctrl+J: 正向增量查找(按下Ctrl+J后,输入的每个字符编辑器都快速匹配定位到指定单词,
           如果没有,则在stutes line中显示没有找到,对于查一个Word时,特别实用，比Ctrl+F方便)
    Ctrl+Shift+J: 反向增量查找(和上条相同,只不过是从后往前查)
    Ctrl+K: 根据选中的Word快速定位到下一个
    Ctrl+E: 快速显示当前Editer的下拉列表(如果当前页面没有显示的用黑体表示)
    Ctrl+/(小键盘): 折叠展开当前类中的所有代码（与Shitf键，小键盘的+也可折叠代码）
    Ctrl+W: 关闭当前Editor（同Ctrl+F4，关闭所有Editor为Ctrl+Shift+W，同Ctrl+Shift+F4）
    Ctrl+S: 保存当前Editor（保存所有Editor，同Ctrl+Shift+S）
    Ctrl+N: 新建文件
    F3: 跳到声明或定义的地方
    F4: 打开类型层次结构
    Ctrl+F3: 打开结构
    F12: 激活编辑器
    
    Ctrl+Shift+R: 输入打开指定源文件
    Ctrl+Shift+E: 显示管理当前打开的所有的View的管理器(可以选择关闭,激活等操作)
    Ctrl+Shift+F4: 关闭所有打开的Editer
    Ctrl+Shift+X: 把当前选中的文本全部变为大写
    Ctrl+Shift+Y: 把当前选中的文本全部变为小写
    Ctrl+Shift+F: 格式化当前代码
    Ctrl+Shift+P: 定位到对于的匹配符(譬如{}) (从前面定位后面时,光标要在匹配符里面,后面到前面,则反之)
    Ctrl+Shift+F6: 上一个编辑器
    Ctrl+F6: 下一个编辑器
    Ctrl+O： 显示当前编辑源文件的大纲（光标焦点要在编辑器内）
    Ctrl+H: 打开搜索对话框
    Ctrl+G: 工作区中的声明
    Ctrl+Alt+H: 查找当前光标所指方法的调用情况
    Ctrl+Shift+G: 查找当前光标所指元素的所有引用
    Ctrl+Alt+J: 将选中行数的代码缩进成一行。
    Ctrl+Shift+H: 在层次结构中打开类型
    Ctrl+Shift+P: 转至匹配的括号
    Ctrl+Q: 转至上一个编辑位置 
    Ctrl+Shift+↑: 转至上一个成员
    Ctrl+Shift+↓: 转至下一个成员
    Ctrl+Shift+T: 打开类型
    
    常用重构快捷键(注:一般重构的快捷键都是Alt+Shift开头)
    Alt+Shift+R: 重命名 (是我自己最爱用的一个了,尤其是变量和类的Rename,比手工方法能节省很多劳动力)
    Alt+Shift+M: 抽取方法 (光标选中需要抽取的一段代码)
    Alt+Shift+C: 修改函数结构(比较实用,有N个函数调用了这个方法,修改一次搞定)
    Alt+Shift+L: 抽取本地变量( 可以直接把一些魔法数字和字符串抽取成一个变量,尤其是多处调用的时候)
    Alt+Shift+F: 把Class中的local变量变为field变量 (比较实用的功能)
    Alt+Shift+I: 合并变量(可能这样说有点不妥Inline)
    Alt+Shift+V: 移动函数和变量(不怎么常用)
    Alt+Shift+Z: 重构的后悔药(Undo)
    Alt+Shift+↓: 恢复上一个选择
    Alt+Shift+↑: 选择封装元素
    Alt+Shift+←: 选择上一个元素
    Alt+Shift+→: 选择下一个元素 
    
    调试
    F7: 单步返回, 即由函数内部返回到调用处
    F6: 单步跳过，即单步调试不进入函数内部
    F5: 单步跳入，即单步调试进入函数内部
    F8: 继续, 即跳转到下一个断点
    Shift+F5: 使用过滤器单步执行
    Ctrl+Shift+B: 添加/去除光标所在行的断点 
    F11: 调试上次启动的工程
    Ctrl+F11 运行上次启动 
    Ctrl+R: 执行多步，运行至光标所在行，调试很有用

##Mac系统
    Command + Shift + r: 查找类 
    Command + Shift + o: 自动加载（减）类路径
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
                 如果没有，则在Stutes Line中显示没有找到） 
    Command + Shift + J：反向增量查找 
    Command + Shift + W：关闭所有打开的Editer 
    Command + Shift + X：把当前选中的文本全部变为大写 
    Command + Shift + Y：把当前选中的文本全部变为小写 
    Command + Shift + F：格式化当前代码 
    Command + Shift + P：定位到对于的匹配符（譬如{}）（从前面定位后面时，光标要在匹配符里面，后面到前面，则反之） 
    Option + Command + R：重命名（尤其是变量和类的Rename效果比较明显） 
    Option + Shift + M：抽取方法（选中需要抽取的代码段） 
    Option + Command + C：修改函数结构（有N个函数调用了这个方法，修改一次就搞定） 
    Option + Command + L：抽取本地变量（可以直接把一些魔法数字和字符串抽取成一个变量，尤其是多处调用的时候） 
    Option + Shift + F：把Class中的Local变量变为Field变量（比较实用的功能） 
    Option + Command + H: 查找当前光标所指方法的调用情况
    Option + Command + Z：重构的后悔药（Undo）

##Tips
    右击窗口的左边框（即加断点的地方），选Show Line Numbers可以快速加行号。
    当鼠标放在一个标记处出现Tooltip时候按F2则把鼠标移开时Tooltip还会显示，即Show Tooltip Description。
    
    elipse快速生成getter和setter方法
    在指定类的内部，右击—>Source—>Generate Getters and  Setters
    快捷键Alt+shift+S
    
    设置eclipse Console控制台字体大小
    window -> preferences -> general -> appearance -> colors and fonts -> debug - console font
