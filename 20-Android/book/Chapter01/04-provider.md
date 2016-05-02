ContentProvider
===

ContentProvider 用于对外共享数据，其他应用可通过 ContentProvider 对该应用中的数据进行添删改查。其优点在于统一了数据的访问方式。
如采用 xml 文件对外共享数据，需要进行 xml 解析才能读取数据；
采用 sharedpreferences 共享数据，需要使用 sharedpreferences API 读取数据。

### 自定义 ContentProvider

```java
public class MyProvider extends ContentProvider {
    public boolean onCreate()
    public Uri insert(Uri uri, ContentValues values)
    public int delete(Uri uri, String selection, String[] selectionArgs)
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
        String sortOrder)
    public String getType(Uri uri) // 返回当前 Uri 所代表数据的 MIME 类型
}
```


- 如果操作的数据属于集合类型，那么 MIME 类型字符串应以 `vnd.android.cursor.dir/` 开头。

如：要得到所有 person 记录的 Uri 为 `content://com.android.lex/person`，
那么返回的 MIME 类型字符串应该为：`"vnd.android.cursor.dir/person"`。

- 如果要操作的数据属于非集合类型数据，那么 MIME 类型字符串应该以 `vnd.android.cursor.item/` 开头。

如：得到 id 为 10 的 person 记录，Uri 为 `content://com.android.lex/person/10`，
那么返回的 MIME 类型字符串为：`"vnd.android.cursor.item/person"`。

### 在AndroidManifest.xml 中 配置 ContentProvider

```xml
<application android:icon="@drawable/icon" android:label="@string/app_name">
<provider
    android:name=".PersonContentProvider"
    android:authorities="com.ljq.providers.personprovider"/>
</application>
```

### 通过 Resolver 访问 ContentProvider

```java
public boolean updateArticle(Article article) {
        resolver = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(Articles.CONTENT_URI, article.getId());  

        ContentValues values = new ContentValues();  
        values.put(Articles.TITLE, article.getTitle());  
        values.put(Articles.ABSTRACT, article.getAbstract());  
        values.put(Articles.URL, article.getUrl());  

        int count = resolver.update(uri, values, null, null);  

        return count > 0;  
}
```

### ContentUris

ContentUris 类用于操作 Uri 路径后面的 ID 部分，它有两个常用方法：

##### 1. withAppendedId(uri, id) 用于为路径加上 ID 部分：

```java
Uri uri = Uri.parse("content://com.android.lex/person")
Uri resultUri = ContentUris.withAppendedId(uri, 10);
// 生成后的Uri为：content://com.android.lex/person/10
```

##### 2. parseId(uri) 用于从路径中获取 ID 部分：

```java
Uri uri = Uri.parse("content://com.android.lex/person/10")
long personid = ContentUris.parseId(uri); // 获取的结果为10
```

### 监听 ContentProvider 中数据变化。

- ContentProvider 发生数据变化时调用 notifyChange 方法通知注册在此 URI 上的访问者：

```java
public class PersonContentProvider extends ContentProvider {

    public Uri insert(Uri uri, ContentValues values) {
        db.insert("person", "personid", values);
        // 发送数据变化通知
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
```
- 通过 ContentObserver 对 URI 表示的数据进行监听。

```java
getContentResolver().registerContentObserver(
    Uri.parse("content://com.ljq.providers.personprovider/person"),
    true,
    new PersonObserver(new Handler()));

public class PersonObserver extends ContentObserver {

    public PersonObserver(Handler handler) {
        super(handler);
    }

    public void onChange(boolean selfChange) {
        // 当监听到数据变化通知时，回调到这里
    }
}
```

- Content Provider负责存取数据并暴露给其他应用。每个ContentProvider都只有一个实例（单例模式），但可以与多个在不同应用或进程中的Content Resovler进行通信。
- 其他应用通过Content Resovler间接操作该应用的数据。
- URI标识了ContentResovler与哪个Content Provider对话(talk)，并将其哪一张table作为目标（target)
- 持有多个数据集（多张表）的ContentProvider可以为每个数据集提供一个独立的URI。
- 定义一个Content Provider，最好也定义一个常量作为其URI，可以简化客户端的代码，并在以后的更新中保持简洁。
