Intent IntentFilter
===

### 关系

- Intent: 根据特定的条件找到匹配的组件，继而对该组件执行一些操作。Intent在后，根据特定信息，找到之前以及注册过的组件。
 
- IntentFilter: 为某个组件向系统注册一些特性(可注册多个 IntentFilter)，以便Intent找到对应
的组件。IntentFilter在前，任何一个组件必须先通过IntentFilter注册。

### 源码分析
 
Intent类源码(部分) 路径位于：\frameworks\base\core\java\android\content\Intent.java

```java
public class Intent implements Parcelable, Cloneable {  
  
    private String mAction;           // action值  
    private Uri mData;                // uri  
    private String mType;             // MimeType  
    private String mPackage;          // 所在包名  
    private ComponentName mComponent; // 组件信息  
    private int mFlags;               // Flag标志位  
    private HashSet<String> mCategories; // Category值  
    private Bundle mExtras;           // 附加值信息  
    //...  
}
```

IntentFilter类源码(部分) 路径位于：\frameworks\base\core\java\android\content\IntentFilter.java

```java
public class IntentFilter implements Parcelable {  
    //...  
    // 保存了所有action字段的值  
    private final ArrayList<String> mActions;  
    // 保存了所有Category的值  
    private ArrayList<String> mCategories = null;  
    // 保存了所有Schema(模式)的值  
    private ArrayList<String> mDataSchemes = null;  
    // 保存了所有Authority字段的值  
    private ArrayList<AuthorityEntry> mDataAuthorities = null;  
    // 保存了所有Path的值  
    private ArrayList<PatternMatcher> mDataPaths = null;  
    // 保存了所有MimeType的值  
    private ArrayList<String> mDataTypes = null;  
    //...  
}
```

可见，Intent中属性类型基本上都是单个类型的，而IntentFilter属性都是集合类型的。
