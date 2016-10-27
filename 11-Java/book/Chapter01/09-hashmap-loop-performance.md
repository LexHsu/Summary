HashMap循环遍历方式及其性能对比
===

### 1. Map的四种遍历方式

下面只是简单介绍各种遍历示例(以HashMap为例)，各自优劣会在本文后面进行分析给出结论。

(1) for each map.entrySet()

```java
Map<String, String> map = new HashMap<String, String>();
for (Entry<String, String> entry : map.entrySet()) {
    entry.getKey();
    entry.getValue();
}
```

(2) 显示调用map.entrySet()的集合迭代器

```java
Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
while (iterator.hasNext()) {
    Map.Entry<String, String> entry = iterator.next();
    entry.getKey();
    entry.getValue();
}
```

(3) for each map.keySet()，再调用get获取

```java
Map<String, String> map = new HashMap<String, String>();
for (String key : map.keySet()) {
    map.get(key);
}
```

(4) for each map.entrySet()，用临时变量保存map.entrySet()

```java
Set<Entry<String, String>> entrySet = map.entrySet();
for (Entry<String, String> entry : entrySet) {
    entry.getKey();
    entry.getValue();
}
```


### 2、HashMap四种遍历方式的性能测试及对比


PS：如果运行报异常in thread “main” java.lang.OutOfMemoryError: Java heap space，请将main函数里面map size的大小减小。
其中getHashMaps函数会返回不同size的HashMap。

测试环境为Windows7 32位系统 3.2G双核CPU 4G内存，Java 7，Eclipse -Xms512m -Xmx512m

```
compare loop performance of HashMap
-----------------------------------------------------------------------
map size               | 10,000    | 100,000   | 1,000,000 | 2,000,000
-----------------------------------------------------------------------
for each entrySet      | 2 ms      | 6 ms      | 36 ms     | 91 ms     
-----------------------------------------------------------------------
for iterator entrySet  | 0 ms      | 4 ms      | 35 ms     | 89 ms     
-----------------------------------------------------------------------
for each keySet        | 1 ms      | 6 ms      | 48 ms     | 126 ms    
-----------------------------------------------------------------------
for entrySet=entrySet()| 1 ms      | 4 ms      | 35 ms     | 92 ms     
-----------------------------------------------------------------------
```

PS：由于首次遍历HashMap会稍微多耗时一点，for each的结果稍微有点偏差，将测试代码中的几个Type顺序调换会发现，for each entrySet耗时和for iterator entrySet接近。

### 3、遍历方式性能测试结果分析

(1) foreach介绍

见：ArrayList和LinkedList的几种循环遍历方式及性能对比分析中介绍。

(2) HashMap遍历方式结果分析

从上面知道for each与显示调用Iterator等价，上表的结果中可以看出除了第三种方式(for each map.keySet())，再调用get获取方式外，其他三种方式性能相当。本例还是hash值散列较好的情况，若散列算法较差，第三种方式会更加耗时。
我们看看HashMap entrySet和keySet的源码

```java
private final class KeyIterator extends HashIterator<K> {
    public K next() {
        return nextEntry().getKey();
    }
}

private final class EntryIterator extends HashIterator<Map.Entry<K,V>> {
    public Map.Entry<K,V> next() {
        return nextEntry();
    }
}
```

分别是keySet()和entrySet()返回的set的迭代器，从中我们可以看到只是返回值不同而已，父类相同，所以性能相差不多。只是第三种方式多了一步根据key get得到value的操作而已。get的时间复杂度根据hash算法而异，源码如下：

```java
public V get(Object key) {
    if (key == null)
        return getForNullKey();
    Entry<K,V> entry = getEntry(key);

    return null == entry ? null : entry.getValue();
}

/**
 * Returns the entry associated with the specified key in the
 * HashMap.  Returns null if the HashMap contains no mapping
 * for the key.
 */
final Entry<K,V> getEntry(Object key) {
    int hash = (key == null) ? 0 : hash(key);
    for (Entry<K,V> e = table[indexFor(hash, table.length)];
         e != null;
         e = e.next) {
        Object k;
        if (e.hash == hash &&
            ((k = e.key) == key || (key != null && key.equals(k))))
            return e;
    }
    return null;
}
```

get的时间复杂度取决于for循环循环次数，即hash算法。

### 4、结论总结

从上面的分析来看：

a. HashMap的循环，如果既需要key也需要value，直接用

```java
Map<String, String> map = new HashMap<String, String>();
for (Entry<String, String> entry : map.entrySet()) {
    entry.getKey();
    entry.getValue();
}
```

即可，foreach简洁易懂。

b. 如果只是遍历key而无需value的话，可以直接用

```java
Map<String, String> map = new HashMap<String, String>();
for (String key : map.keySet()) {
    // key process
}
```
