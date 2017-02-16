根据.x509.pem.pk8¥生成keystore
===

一、背景

Goolge源码中提供了4个标准的key，以签名测试程序:

```
testkey -- a generic key for packages that do not otherwise specify a key.
platform -- a test key for packages that are part of the core platform.
shared -- a test key for things that are shared in the home/contacts process.
media -- a test key for packages that are part of the media/download system.
```

位于Android源码目录

```
android\build\target\product\security

注意,这些key只是用于工程版的Android系统，在编译android源码时：
使用eng选项即表示编译生成工程版的Android系统，
使用user选项时表示编译用户版（即正式版）的Android系统。
```

二、工具

```
1. openssl  (cryptography and SSL/TLS toolkit)
Mac安装很简单
Win地址：http://slproweb.com/products/Win32OpenSSL.html
（Win64 OpenSSL v1.1.0d），安装后，将bin目录配置到环境变量中

2. keytool
```

三、生成keystore文件

```
以platform.pk8，platform.x509.pem为例

1. 生成platform.pem文件
openssl pkcs8 -inform DER -nocrypt -in platform.pk8 -out platform.pem

2. 生成platform.p12文件
openssl pkcs12 -export -in  platform.x509.pem -out platform.p12 -inkey  platform.pem -password pass:android -name androiddebugkey
执行该命令，将在目录下生成platform.p12文件，它本质上应该就是一个数字证书。

3. 生成platform.jks文件，或platform.keystore文件，或debug.keystore
keytool -importkeystore -deststorepass android -destkeystore ./platform.jks -srckeystore ./platform.p12 -srcstoretype PKCS12 -srcstorepass android

keytool -importkeystore -deststorepass android -destkeystore ./platform.keystore -srckeystore ./platform.p12 -srcstoretype PKCS12 -srcstorepass android

keytool -importkeystore -deststorepass android -destkeystore ./debug.keystore -srckeystore ./platform.p12 -srcstoretype PKCS12 -srcstorepass android
```