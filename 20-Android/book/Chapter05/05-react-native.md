React Native
===

2015 年 9 月 15 号，React Native for Android 发布。至此，React 基本完成了对多端的支持。基于 React / React Native 可以：

- H5, Android, iOS 多端代码复用
- 实时热部署

在接下来的时间，我会通过一系列文章来介绍 React Native。本文介绍环境配置以及如何建立一个简单的项目。本篇文章 iOS 和 Android 开发都适用。

目前使用 React Native 开发只能在 Mac 系统 上进行。写 iOS 的同学，应该都是 Mac （除了听说网易有些部门写 iOS 给黑苹果之外，哈哈哈哈）。 开发 Android 的同学, 如果公司配发的不是 Mac 的话，建议自己入手一个，能显著提高生产力，就当投资自己吧。我大阿里对 Android 开发也是不给 Mac 的（不知道公司什么思路，现在倒是可以申请 Mac air了，air的性能……），所以我也是自己买的。

### 环境配置

需要安装的有:

- Homebrew

Mac 中的一个包管理器。[安装地址](http://brew.sh/)，我的版本如下:

```
mac-2:~ srain$ brew -v
Homebrew 0.9.5 (git revision ac9a7; last commit 2015-09-21)
```

版本过低将会导致无法安装后续几个组件。可用 brew update 升级。

```
mac-2:react-native srain$ brew update
Updated Homebrew from ac9a71c8 to 4257c3da.
```

- Node.js 和 npm

Node.js 需要 4.0 及其以上版本。安装好之后，npm 也有了。

通过 [nvm](https://github.com/creationix/nvm#installation) 安装 Node.js

nvm 是 Node.js 的版本管理器，可以轻松安装各个版本的 Node.js 版本。
```
安装 nvm 可以通过 Homebrew 安装:

brew install nvm
或者 按照这里的方法 安装。

然后安装 Node.js：

nvm install node && nvm alias default node
也可以直接下载安装 Node.js： https://nodejs.org/en/download/

安装好之后，如下：

mac-2:react-native srain$ node -v
v4.0.0
mac-2:react-native srain$ npm -v
2.14.2
```

- watchman 和 flow

这两个包分别是监控文件变化和类型检查的。安装如下：
```
brew install watchman
brew install flow
```

### 安装 React-Native

```
通过 npm 安装即可：
npm install -g react-native-cli
```

### APP 开发环境设置

- iOS

XCode 6.3 及其以上即可。

- Android

```
1. 设置环境变量：ANDROID_HOME

export ANDROID_HOME=/usr/local/opt/android-sdk

2. SDK Manager 安装以下包：

Android SDK Build-tools version 23.0.1
Android 6.0 (API 23)
Android Support Repository
```

### 初始化一个项目

文档提到：
```
react-native init AwesomeProject
```

初始化一个项目，其中 AwesomeProject 是项目名字，这个随意。等待一段时间之后（具体视网络情况而定），项目初始化完成。

进入到项目目录：

```
cd AwesomeProject
mac-2:AwesomeProject srain$ ls -l
total 24
drwxr-xr-x  14 srain  staff   476 Sep 21 09:52 android
-rw-r--r--   1 srain  staff  1023 Sep 21 11:47 index.android.js
-rw-r--r--   1 srain  staff  1065 Sep 20 11:58 index.ios.js
drwxr-xr-x   6 srain  staff   204 Sep 20 11:58 ios
drwxr-xr-x   5 srain  staff   170 Sep 21 10:31 node_modules
-rw-r--r--   1 srain  staff   209 Sep 20 11:58 package.json
```

其中 android 和 ios 中分别为两个平台的项目文件。index.android.js 和 index.ios.js 为两个页面对应的 js 文件。

### 运行项目

不管是 iOS 还是 Android，在开发调试阶段，都需要在 Mac 上启动一个 HTTP 服务，称为`Debug Server`，默认运行在 8081 端口，APP 通 Debug Server 加载 js。

iOS 和 Android 的模拟器，连接 Mac 本机的服务都很方便。但是通过 USB 或者 Wifi 连接调试，就稍微麻烦一些了。

- iOS

还是非常简单，XCode 打开项目，点击运行就好。修改 index.ios.js, 在模拟器中 ⌘ + R 重新载入 js 即可看到相应的变化。

iOS 真机调试也简单，修改HTTP地址即可。

jsCodeLocation = [NSURL URLWithString:@"http://localhost:8081/index.ios.bundle"];

- Android

按照官方文档，需要一个模拟器（Genymotion模拟器也可以）。但是不像 iOS，Android 开发平时更多是直接用真机进行开发和调试，如何运行部署到真机，下面会提到。

运行命令

```
react-native run-android
```

然后就会部署到模拟器，修改 index.android.js ，调出模拟器菜单键，选择重新载入 js 即可看到变化。

- Android 真机调试

示例 APP 直接部署到真机，红色界面报错，无法连接到 Debug Server。

如果是 5.0 或者以上机型，可通过 adb 反向代理端口，将 Mac 端口反向代理到测试机上。

```
adb reverse tcp:8081 tcp:8081
```

如果 5.0 以下机器，应用安装到测试机上之后，摇动设备，在弹出菜单中选择 Dev Setting > Debug Server host for device，然后填入 Mac 的 IP 地址（ifconfig 命令可查看本机 IP）

关于修改 DevHelper 来进行和 iOS 一样的开发调试，后续关于热部署时，我会介绍到。

- 在 Android Studio 中调试开发

我们可能希望在 Android Studio 打开项目，然后编译部署到真机。

这个时候，在命令行启动 Debug Server 即可：

```
react-native start
```

### 结论和后续

本篇文章，iOS 和 Android 都适用。至此，环境配置和示例项目运行应该都好了。后续我会继续发几篇文章介绍：

- [原始文档](https://github.com/liaohuqiu/liaohuqiu.github.io/edit/docs/_posts/blog/2015/2015-09-23-react-native-1.cn.md)
- [翻译文档](http://www.liaohuqiu.net/cn/posts/react-native-1/)
- [官方文档](https://facebook.github.io/react-native/docs/getting-started.html#content)
