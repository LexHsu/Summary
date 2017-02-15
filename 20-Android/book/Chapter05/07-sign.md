Andorid自定义签名
===

Android开发中，申请使用地图SDK接口时，需要应用打包发布的数字签名（eystore的SHA指纹），调试时使用默认的debug.keystore是无法直接使用的，如果每次调试都打包效率太低，可自定义Keystore解决。

使用命令行查看Release签名(keystore或jks)的具体信息（最后一位参数为store密码，也可直接输入`lex$ keytool -list -v -keystore lex.jks`，由shell提示输入密码）

```
lex$ keytool -list -v -keystore lex.jks -storepass android

密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: lexkeyalias
创建日期: Feb 15, 2017
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=Lex, OU=MobileDept, O=lex.com, L=San Francisco, ST=California, C=United States
发布者: CN=Lex, OU=MobileDept, O=lex.com, L=San Francisco, ST=California, C=United States
序列号: 5ce9ca71
有效期开始日期: Wed Feb 15 23:29:17 CST 2017, 截止日期: Sun Feb 09 23:29:17 CST 2042
证书指纹:
	 MD5: B7:8A:BC:B0:08:8B:81:69:6E:18:4E:E8:FB:4E:D2:31
	 SHA1: CD:1C:2D:2F:DF:3B:AC:AE:47:A6:1C:E6:20:BC:E6:90:99:5D:44:78
	 SHA256: 80:FE:69:A9:82:41:80:C5:95:AD:56:CC:7E:00:C6:07:46:03:B2:00:F7:00:42:FB:37:B9:96:33:17:70:EB:A2
	 签名算法名称: SHA256withRSA
	 版本: 3

扩展: 

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 8E 25 7E 53 0B 19 4A 9F   D1 BC C6 D6 C5 BE DB 5C  .%.S..J........\
0010: 65 0E 74 AE                                        e.t.
]
]



*******************************************
*******************************************
```

注意：必须保持自定义keystore的密码，别名，别名密码和debug keystore一致，均为android，步骤：


一. 修改签名alias为androiddebugkey

```
lex$ keytool -changealias -keystore lex.jks -alias lexkeyalias -destalias androiddebugkey
输入密钥库口令:  
```

注：

- 需要输入原来签名的密码
- `lexkeyalias` 为原来签名的别名
- `androiddebugkey`为目标签名的别名
- keystore文件与jks文件规则一致

二. 修改key密码

```
lex$ keytool -keypasswd -keystore lex.jks -alias androiddebugkey
输入密钥库口令:  
新<androiddebugkey> 的密钥口令: 
口令不能相同
新<androiddebugkey> 的密钥口令: 
口令太短 - 至少必须为 6 个字符
```

注：口令不可相同

三. 修改store密码

```
lex$ keytool -storepasswd -keystore lex.jks
输入密钥库口令:  
新keystore password: 
口令不能相同
新keystore password: 
口令太短 - 至少必须为 6 个字符
```
注：口令不可相同

修改后的签名信息：

```
lex$ keytool -list -v -keystore lex.jks -storepass android

密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: androiddebugkey
创建日期: Feb 16, 2017
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=Lex, OU=MobileDept, O=lex.com, L=San Francisco, ST=California, C=United States
发布者: CN=Lex, OU=MobileDept, O=lex.com, L=San Francisco, ST=California, C=United States
序列号: 5ce9ca71
有效期开始日期: Wed Feb 15 23:29:17 CST 2017, 截止日期: Sun Feb 09 23:29:17 CST 2042
证书指纹:
	 MD5: B7:8A:BC:B0:08:8B:81:69:6E:18:4E:E8:FB:4E:D2:31
	 SHA1: CD:1C:2D:2F:DF:3B:AC:AE:47:A6:1C:E6:20:BC:E6:90:99:5D:44:78
	 SHA256: 80:FE:69:A9:82:41:80:C5:95:AD:56:CC:7E:00:C6:07:46:03:B2:00:F7:00:42:FB:37:B9:96:33:17:70:EB:A2
	 签名算法名称: SHA256withRSA
	 版本: 3

扩展: 

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 8E 25 7E 53 0B 19 4A 9F   D1 BC C6 D6 C5 BE DB 5C  .%.S..J........\
0010: 65 0E 74 AE                                        e.t.
]
]



*******************************************
*******************************************


```