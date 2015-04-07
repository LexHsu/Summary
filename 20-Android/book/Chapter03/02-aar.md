Android studio 中配置 jar 与 aar
===

Android Studio 中对一个库进行生成操作时将会同时生成 `*.jar` 与 `*.aar` 文件。

### 存储位置

```
*.jar：库/build/intermediates/bundles/debug(release)/classes.jar
*.aar：库/build/outputs/aar/libraryname.aar
```

### 两者区别

```
*.jar：只包含 class 文件与清单文件，不包含资源文件，如图片等 res 文件
*.aar：包含所有资源，class 以及 res 资源文件
```

如果是简单的类库，使用 `*.jar` 文件即可；
如果是一个 UI 库，包含布局文件及字体等资源文件，只能使用 `*.aar` 文件。

### jar 使用方式

将 `*.jar` 拷贝到 libs 目录，Android Studio 项目中添加：

```
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
}
```

重新编译项目即可。

### `xxx.aar` 使用方式

aar 有两种方式，分别为本地加载以及网络加载，这里介绍本地加载方式：

1. 如拷贝 genius.aar 到 libs 目录
2. 修改 build.gradle 配置文件

```
repositories {
    flatDir {
        dirs 'libs'
    }
}
dependencies {
    compile(name:'genius', ext:'aar')
}
```

重新编译即可。

这时打开项目地址 `\build\intermediates\exploded-aar\` 你会发现下面多了一个文件夹 `genius`
打开后能看见里边包含了一个 `classes.jar` 文件与一些资源文件和 `R.txt` 文件。
