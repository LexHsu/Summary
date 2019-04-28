Settings
===
```
Editor->Color Schema -> General

->Code
Identifier under caret(write)光标聚焦的 834781
Identifier under caret光标关联的 834781

->Editor
selection background 4D5083

Keymap->Inspect Code with Editor Settings 检视指定类的代码问题
Ctrl + Alt + N

Android Studio中变量的 Ctrl + K，需要先 Ctrl + F

ToolBar -> Quick Definition 快捷键 Ctrl + Shift + I 改为 Ctrl + Alt + I

Settings -> Keymap -> Quick Evaluate Expression 增加 Ctrl + Shift + I

修改打印日志字体大小
Settings->Editor->Color & Fonts->Console Font
size: 13

修改LogCat颜色

Settings->Editor->Color & Fonts->Android Logcat
去掉勾选 Use inherited attributes
Info: 2D8333 Bright: 124
Debug: 3B7393 Bright: 108
Warning: 837A32 Bright: 124
Error: C35D5A Bright 60
Assert: C35D5A Bright 60

打开分屏

在代码编辑区顶部，右击需要分屏的文件名称，选择Split Vertically或者Split Horizontally
可自定义快捷键，Preferences → Keymap 找到Split Vertically。然后打开上下文菜单并单击 Add Keyboard Shortcut。
对于垂直拆分视图，我增加了control + alt + v


无干扰模式(Distraction Free Mode)
View → Enter Distraction Free Mode来开启无干扰模式。编辑器占用整个Studio版面，而没有任何编辑器选项卡和工具按钮，代码按中心对齐。


导入代码样式模板

依次点击Preferences → Code Style → Java，在Scheme import自定义代码样式模板。

有2款风格特别值得一提：
Square Java Code Styles with Android
Google Java Code Style


Search for command：cmd(ctrl) + shift + a (Windows / Linux：ctrl + shift + a)。

想关闭当前选项卡，只需键入：close，会得到一个正确的快捷方式/命令。

从最近的复制/粘贴中选择(管理剪贴簿)：
cmd(ctrl) + shift + v(Windows / Linux：ctrl + shift + v)。
默认情况下，最后有5个复制/粘贴项目。

启用多光标(multiple cursor)功能：
control + g(Windows / Linux：alt + j)，如果设置了不同类型的快捷方式，命令不同，可从Search for command中搜索multiple cursor，如我的mac是 alt + y
```
