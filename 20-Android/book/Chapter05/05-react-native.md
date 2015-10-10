React Native
===

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

### 独立发布 Android APK

做了 React Native Android 开发的话，就会知道，开发的时候必须启动个 JS Server，然后要让手机连接这个 Server，否者会出现那个”吓人”的红色屏幕。这个我在第一篇 React Native 文章中就提到过。
如果要发布一个 React Native 写的 Android 应用，不可能要别人来连接这个 JS Server。可不可以不要连接这个 Server 就能运行呢？在网上找了一圈，发现资料很少，官方文档上也没有说支持。这篇文章就来讨论一种实现方案。
原理探究
我们来看一下 RN 应用连接这个 JS Server 干了啥事情？第一次运行应用的时候，会提示正在加载 JS 文件。应该就是连接这个 JS Server 下载 JS 文件吧，我们同时看到 JS Server 的窗口中打印出如下的 log：
```
Running packager on port 8081.
...
transforming [========================================] 100% 330/330
[19:45:44] <START> request:/index.android.bundle?platform=android
[19:45:44] <START> find dependencies
[19:45:44] <END>   find dependencies (127ms)
[19:45:44] <START> transform
[19:45:45] <END>   transform (490ms)
[19:45:45] <END>   request:/index.android.bundle?platform=android (630ms)
::ffff:10.237.208.110 - - [Thu, 24 Sep 2015 11:45:50 GMT] "GET /flow/ HTTP/1.1" 404 - "-" "okhttp/2.4.0"
```

可以看到一些信息，这是在访问 /index.android.bundle?platform=android 这个连接，服务器端口号是 8081。在浏览器中打开这个连接：http://localhost:8081/index.android.bundle?platform=android ，果然能加载一个文件，里面都是我们写的 React Native 的 JS 代码。这个时候我们在手机上看一下 APP 的应用数据，以我们这个知乎日报的客户端为例，应用数据路径在手机上的 /data/data/com.rctzhihudaily/ 目录（手机需要 Root 才能看到这个路径）。在这目录下 files 文件夹下找到一个 ReactNativeDevBundle.js 文件，文件内容就是我们刚才在浏览器打开那个连接得到的内容。
哈，我们快接近真相了。我尝试重新安装一个新的 APK，然后把这个文件 push 到对应的目录下，修改一下权限，果然，不需要连接 JS Server 就能打开了。
总结一下，第一次打开 React Native 应用的时候，会连接 JS Server 下载一个 ReactNativeDevBundle.js 文件，然后放到应用数据的 files 目录下，就能运行这个 JS 文件了。到这里我们也就找到解决方案了。
解决方案
知道原理了，我们就很好解决了，只要我们把这个 ReactNativeDevBundle.js 文件提前打包到 APK 中，然后 APK 运行的时候，把这个文件释放到 files 目录中。
我们可以把 ReactNativeDevBundle.js 先保存下来，放在 Android 工程的 assets 目录中，这会自动打包到 APK 中。在 APP 第一次运行的时候，把文件拷贝到目的位置，代码如下：
```
public class MainActivity extends Activity {

    private static final String JSBUNDLE_FILE = "ReactNativeDevBundle.js";

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private void prepareJSBundle() {
        File targetFile = new File(getFilesDir(), JSBUNDLE_FILE);
        if (!targetFile.exists()) {
            try {
                OutputStream out = new FileOutputStream(targetFile);
                InputStream in = getAssets().open(JSBUNDLE_FILE);

                copyFile(in, out);
                out.close();
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareJSBundle();
        ...
    }
}
```
使用实例可以参考这里：MainActivity.java。
当然，这样已经可行了，但是还不够好，如果每次都要手动去添加这个 ReactNativeDevBundle.js 文件，难免会出错，或者遗漏。我这里在 build.gradle 中添加一个 task，让它每次打包的是自动访问 http://localhost:8081/index.android.bundle?platform=android 下载文件到 app/src/main/assets/ 目录中，脚本如下：
```
final def TARGET_BUNDLE_DIR = 'app/src/main/assets/'
final def TARGET_BUNDLE_FILE = 'ReactNativeDevBundle.js'
final def DOWNLOAD_URL = 'http://localhost:8081/index.android.bundle?platform=android'

task downloadJSBundle << {
    def dir = new File(TARGET_BUNDLE_DIR)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    def f = new File(TARGET_BUNDLE_DIR + TARGET_BUNDLE_FILE)
    if (f.exists()) {
        f.delete()
    }
    new URL(DOWNLOAD_URL).withInputStream{ i -> f.withOutputStream{ it << i }}
}
// 保证每次编译之前，先下载 JS 文件
preBuild.dependsOn downloadJSBundle
```

这样，每次打包的时候，都会先帮我们下载好 JS 文件到指定位置了。当然，打包的时候，要保证你的 JS Server 是开着的。完整代码可以参考：build.gradle。
到这里，我们就实现了一个可行的方案了，可以独立发布 APK 了。

##### 思考和改进

上面我们已经实现了一个可行方案了。这里我们也看到了一个更大潜在的特性——在线更新。通过上面的实现，我们可以看到，只要通过网络下载并替换这个 JS 文件，就可以实现 APP 的更新。不需要下载 APK 包更新，也不需要市场发布，只要后台上线一个新的 JS，客户端就能立即更新了。这就绕过了应用市场，解决了应用更新困难的问题，修复 BUG 一秒上线，新 Feature 一秒到达用户，这个特性是不是很颠覆？！！！
最后，值得注意的是，我这里只是一个简陋的解决方案，这里有些问题需要改进。首先，我们的 JS 文件都是明文的，基本上就是你的源代码，用在生产环境的话，做混淆是必须的。相信官方很快也会出标准的解决方案，毕竟 iOS 已经支持了。另外，如果要做在线更新的话，需要保证你更新 JS 的服务器的安全，因为这些 JS 代码可以直接运行到用户手机上。
