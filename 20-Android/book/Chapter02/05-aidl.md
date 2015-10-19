AIDL
===

一、编写服务端代码

1. 编写 AndroidManifest.xml文件：

```java
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.braincol.aidl.service"
      android:versionCode="1"
      android:versionName="1.0">
    <uses-sdk android:minSdkVersion="8" />
 
    <application android:icon="@drawable/icon" android:label="@string/app_name">
 
    <service android:name="RemoteService">
        <intent-filter>
            <action android:name="com.braincol.aidl.remote.webpage"/>
        </intent-filter>
    </service>
 
    </application>
</manifest>
```

2. 创建RemoteWebPage.aidl文件

在包com.braincol.aidl.service下创建RemoteWebPage.aidl文件：
```java
package com.braincol.aidl.service;

interface RemoteWebPage {
    String getCurrentPageUrl();     
}
```

3. 生成RemoteWebPage.java文件

保存并编译该工程（在eclipse中编译）会看到 gen/ 目录下的com.braincol.aidl.service包下出现了一个 RemoteWebPage.java文件：
